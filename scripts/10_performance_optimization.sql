-- Optimizaciones de rendimiento para Camello

-- Configuraciones de PostgreSQL recomendadas para la aplicación
-- (Estas configuraciones deben aplicarse en postgresql.conf)

-- Comentarios con configuraciones recomendadas:
/*
# Configuraciones de memoria
shared_buffers = 256MB                    # 25% de RAM disponible
effective_cache_size = 1GB                # 75% de RAM disponible
work_mem = 4MB                           # Para operaciones de ordenamiento
maintenance_work_mem = 64MB              # Para operaciones de mantenimiento

# Configuraciones de escritura
wal_buffers = 16MB
checkpoint_completion_target = 0.9
wal_writer_delay = 200ms

# Configuraciones de consultas
random_page_cost = 1.1                   # Para SSDs
effective_io_concurrency = 200           # Para SSDs

# Configuraciones de logging
log_min_duration_statement = 1000        # Log queries > 1 segundo
log_checkpoints = on
log_connections = on
log_disconnections = on
log_lock_waits = on
*/

-- Crear índices parciales para mejorar rendimiento
CREATE INDEX CONCURRENTLY idx_services_active_featured 
ON services (created_at DESC) 
WHERE is_active = true AND is_featured = true;

CREATE INDEX CONCURRENTLY idx_job_offers_active_recent 
ON job_offers (created_at DESC) 
WHERE is_active = true;

CREATE INDEX CONCURRENTLY idx_contracts_active_status 
ON contracts (status, created_at DESC) 
WHERE status IN ('ACTIVE', 'DELIVERED');

CREATE INDEX CONCURRENTLY idx_messages_unread 
ON messages (conversation_id, sent_at DESC) 
WHERE is_read = false;

-- Crear índices para búsqueda de texto completo
CREATE INDEX CONCURRENTLY idx_services_search 
ON services USING gin(to_tsvector('spanish', title || ' ' || description));

CREATE INDEX CONCURRENTLY idx_freelancer_search 
ON freelancer_profiles USING gin(to_tsvector('spanish', 
    first_name || ' ' || last_name || ' ' || COALESCE(title, '') || ' ' || COALESCE(description, '')));

-- Función para limpiar datos antiguos (mantenimiento)
CREATE OR REPLACE FUNCTION cleanup_old_data()
RETURNS void AS $$
BEGIN
    -- Eliminar notificaciones leídas más antiguas de 30 días
    DELETE FROM notifications 
    WHERE is_read = true 
    AND created_at < CURRENT_DATE - INTERVAL '30 days';
    
    -- Eliminar mensajes de conversaciones inactivas más antiguos de 1 año
    DELETE FROM messages 
    WHERE sent_at < CURRENT_DATE - INTERVAL '1 year'
    AND conversation_id IN (
        SELECT id FROM conversations 
        WHERE last_message_at < CURRENT_DATE - INTERVAL '6 months'
    );
    
    -- Soft delete de usuarios inactivos más de 2 años
    UPDATE users 
    SET deleted_at = CURRENT_TIMESTAMP
    WHERE is_active = false 
    AND updated_at < CURRENT_DATE - INTERVAL '2 years'
    AND deleted_at IS NULL;
    
    -- Log de limpieza
    RAISE NOTICE 'Cleanup completed at %', CURRENT_TIMESTAMP;
END;
$$ LANGUAGE plpgsql;

-- Crear tabla de estadísticas agregadas para dashboard admin
CREATE TABLE admin_stats_cache (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    stat_date DATE NOT NULL DEFAULT CURRENT_DATE,
    total_users INTEGER DEFAULT 0,
    total_freelancers INTEGER DEFAULT 0,
    total_contractors INTEGER DEFAULT 0,
    active_services INTEGER DEFAULT 0,
    active_job_offers INTEGER DEFAULT 0,
    completed_contracts INTEGER DEFAULT 0,
    total_revenue DECIMAL(12,2) DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(stat_date)
);

-- Función para actualizar estadísticas del cache
CREATE OR REPLACE FUNCTION update_admin_stats_cache()
RETURNS void AS $$
BEGIN
    INSERT INTO admin_stats_cache (
        stat_date,
        total_users,
        total_freelancers,
        total_contractors,
        active_services,
        active_job_offers,
        completed_contracts,
        total_revenue
    )
    SELECT 
        CURRENT_DATE,
        (SELECT COUNT(*) FROM users WHERE is_active = true),
        (SELECT COUNT(*) FROM users WHERE role = 'FREELANCER' AND is_active = true),
        (SELECT COUNT(*) FROM users WHERE role = 'CONTRACTOR' AND is_active = true),
        (SELECT COUNT(*) FROM services WHERE is_active = true),
        (SELECT COUNT(*) FROM job_offers WHERE is_active = true),
        (SELECT COUNT(*) FROM contracts WHERE status = 'COMPLETED'),
        (SELECT COALESCE(SUM(agreed_price), 0) FROM contracts WHERE status = 'COMPLETED')
    ON CONFLICT (stat_date) 
    DO UPDATE SET
        total_users = EXCLUDED.total_users,
        total_freelancers = EXCLUDED.total_freelancers,
        total_contractors = EXCLUDED.total_contractors,
        active_services = EXCLUDED.active_services,
        active_job_offers = EXCLUDED.active_job_offers,
        completed_contracts = EXCLUDED.completed_contracts,
        total_revenue = EXCLUDED.total_revenue,
        created_at = CURRENT_TIMESTAMP;
END;
$$ LANGUAGE plpgsql;

-- Programar actualización diaria de estadísticas (requiere pg_cron extension)
-- SELECT cron.schedule('update-admin-stats', '0 1 * * *', 'SELECT update_admin_stats_cache();');
