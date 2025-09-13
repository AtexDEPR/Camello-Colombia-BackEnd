-- Funciones útiles para la aplicación Camello

-- Función para buscar servicios con filtros
CREATE OR REPLACE FUNCTION search_services(
    p_search_term TEXT DEFAULT NULL,
    p_category_id UUID DEFAULT NULL,
    p_min_price DECIMAL DEFAULT NULL,
    p_max_price DECIMAL DEFAULT NULL,
    p_location TEXT DEFAULT NULL,
    p_min_rating DECIMAL DEFAULT NULL,
    p_limit INTEGER DEFAULT 20,
    p_offset INTEGER DEFAULT 0
)
RETURNS TABLE (
    id UUID,
    title VARCHAR,
    description TEXT,
    price DECIMAL,
    delivery_time INTEGER,
    rating DECIMAL,
    orders_count INTEGER,
    freelancer_name TEXT,
    freelancer_location VARCHAR,
    category_name VARCHAR,
    images TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        s.id,
        s.title,
        s.description,
        s.price,
        s.delivery_time,
        s.rating,
        s.orders_count,
        fp.first_name || ' ' || fp.last_name as freelancer_name,
        fp.location as freelancer_location,
        c.name as category_name,
        s.images
    FROM services s
    JOIN freelancer_profiles fp ON s.freelancer_id = fp.id
    JOIN categories c ON s.category_id = c.id
    WHERE s.is_active = true
        AND (p_search_term IS NULL OR 
             s.title ILIKE '%' || p_search_term || '%' OR 
             s.description ILIKE '%' || p_search_term || '%' OR
             EXISTS (SELECT 1 FROM unnest(s.tags) tag WHERE tag ILIKE '%' || p_search_term || '%'))
        AND (p_category_id IS NULL OR s.category_id = p_category_id)
        AND (p_min_price IS NULL OR s.price >= p_min_price)
        AND (p_max_price IS NULL OR s.price <= p_max_price)
        AND (p_location IS NULL OR fp.location ILIKE '%' || p_location || '%')
        AND (p_min_rating IS NULL OR s.rating >= p_min_rating)
    ORDER BY 
        CASE WHEN s.is_featured THEN 0 ELSE 1 END,
        s.rating DESC,
        s.orders_count DESC,
        s.created_at DESC
    LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

-- Función para buscar freelancers
CREATE OR REPLACE FUNCTION search_freelancers(
    p_search_term TEXT DEFAULT NULL,
    p_skills TEXT[] DEFAULT NULL,
    p_location TEXT DEFAULT NULL,
    p_min_rating DECIMAL DEFAULT NULL,
    p_max_hourly_rate DECIMAL DEFAULT NULL,
    p_availability VARCHAR DEFAULT NULL,
    p_limit INTEGER DEFAULT 20,
    p_offset INTEGER DEFAULT 0
)
RETURNS TABLE (
    id UUID,
    name TEXT,
    title VARCHAR,
    description TEXT,
    location VARCHAR,
    hourly_rate DECIMAL,
    rating DECIMAL,
    total_reviews INTEGER,
    skills TEXT[],
    profile_picture VARCHAR,
    availability VARCHAR
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        fp.id,
        fp.first_name || ' ' || fp.last_name as name,
        fp.title,
        fp.description,
        fp.location,
        fp.hourly_rate,
        fp.rating,
        fp.total_reviews,
        fp.skills,
        fp.profile_picture,
        fp.availability
    FROM freelancer_profiles fp
    JOIN users u ON fp.user_id = u.id
    WHERE u.is_active = true
        AND (p_search_term IS NULL OR 
             fp.first_name ILIKE '%' || p_search_term || '%' OR 
             fp.last_name ILIKE '%' || p_search_term || '%' OR
             fp.title ILIKE '%' || p_search_term || '%' OR
             fp.description ILIKE '%' || p_search_term || '%')
        AND (p_skills IS NULL OR fp.skills && p_skills)
        AND (p_location IS NULL OR fp.location ILIKE '%' || p_location || '%')
        AND (p_min_rating IS NULL OR fp.rating >= p_min_rating)
        AND (p_max_hourly_rate IS NULL OR fp.hourly_rate <= p_max_hourly_rate)
        AND (p_availability IS NULL OR fp.availability = p_availability)
    ORDER BY 
        fp.rating DESC,
        fp.total_reviews DESC,
        fp.created_at DESC
    LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener estadísticas del dashboard de freelancer
CREATE OR REPLACE FUNCTION get_freelancer_dashboard_stats(p_freelancer_id UUID)
RETURNS TABLE (
    active_services INTEGER,
    total_orders INTEGER,
    pending_deliveries INTEGER,
    total_earnings DECIMAL,
    avg_rating DECIMAL,
    total_reviews INTEGER,
    this_month_earnings DECIMAL,
    pending_applications INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT COUNT(*)::INTEGER FROM services WHERE freelancer_id = p_freelancer_id AND is_active = true),
        (SELECT COUNT(*)::INTEGER FROM contracts WHERE freelancer_id = p_freelancer_id),
        (SELECT COUNT(*)::INTEGER FROM contracts WHERE freelancer_id = p_freelancer_id AND status = 'DELIVERED'),
        (SELECT COALESCE(SUM(agreed_price), 0) FROM contracts WHERE freelancer_id = p_freelancer_id AND status = 'COMPLETED'),
        (SELECT rating FROM freelancer_profiles WHERE id = p_freelancer_id),
        (SELECT total_reviews FROM freelancer_profiles WHERE id = p_freelancer_id),
        (SELECT COALESCE(SUM(agreed_price), 0) FROM contracts 
         WHERE freelancer_id = p_freelancer_id 
         AND status = 'COMPLETED' 
         AND DATE_TRUNC('month', completed_at) = DATE_TRUNC('month', CURRENT_DATE)),
        (SELECT COUNT(*)::INTEGER FROM job_applications ja 
         JOIN job_offers jo ON ja.job_offer_id = jo.id 
         WHERE ja.freelancer_id = p_freelancer_id AND ja.status = 'PENDING' AND jo.is_active = true);
END;
$$ LANGUAGE plpgsql;

-- Función para obtener estadísticas del dashboard de contratante
CREATE OR REPLACE FUNCTION get_contractor_dashboard_stats(p_contractor_id UUID)
RETURNS TABLE (
    active_job_offers INTEGER,
    total_contracts INTEGER,
    pending_reviews INTEGER,
    total_spent DECIMAL,
    avg_rating DECIMAL,
    total_reviews INTEGER,
    this_month_spent DECIMAL,
    pending_applications INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        (SELECT COUNT(*)::INTEGER FROM job_offers WHERE contractor_id = p_contractor_id AND is_active = true),
        (SELECT COUNT(*)::INTEGER FROM contracts WHERE contractor_id = p_contractor_id),
        (SELECT COUNT(*)::INTEGER FROM contracts WHERE contractor_id = p_contractor_id AND status = 'DELIVERED'),
        (SELECT COALESCE(SUM(agreed_price), 0) FROM contracts WHERE contractor_id = p_contractor_id AND status = 'COMPLETED'),
        (SELECT rating FROM contractor_profiles WHERE id = p_contractor_id),
        (SELECT total_reviews FROM contractor_profiles WHERE id = p_contractor_id),
        (SELECT COALESCE(SUM(agreed_price), 0) FROM contracts 
         WHERE contractor_id = p_contractor_id 
         AND status = 'COMPLETED' 
         AND DATE_TRUNC('month', completed_at) = DATE_TRUNC('month', CURRENT_DATE)),
        (SELECT COUNT(*)::INTEGER FROM job_applications ja 
         JOIN job_offers jo ON ja.job_offer_id = jo.id 
         WHERE jo.contractor_id = p_contractor_id AND ja.status = 'PENDING');
END;
$$ LANGUAGE plpgsql;

-- Función para crear una conversación entre dos usuarios
CREATE OR REPLACE FUNCTION create_conversation(
    p_user1_id UUID,
    p_user2_id UUID,
    p_contract_id UUID DEFAULT NULL
)
RETURNS UUID AS $$
DECLARE
    conversation_id UUID;
    participant1_id UUID;
    participant2_id UUID;
BEGIN
    -- Asegurar orden consistente de participantes
    IF p_user1_id < p_user2_id THEN
        participant1_id := p_user1_id;
        participant2_id := p_user2_id;
    ELSE
        participant1_id := p_user2_id;
        participant2_id := p_user1_id;
    END IF;
    
    -- Verificar si ya existe la conversación
    SELECT id INTO conversation_id
    FROM conversations
    WHERE participant1_id = participant1_id 
    AND participant2_id = participant2_id
    AND (contract_id = p_contract_id OR (contract_id IS NULL AND p_contract_id IS NULL));
    
    -- Si no existe, crear nueva conversación
    IF conversation_id IS NULL THEN
        INSERT INTO conversations (participant1_id, participant2_id, contract_id)
        VALUES (participant1_id, participant2_id, p_contract_id)
        RETURNING id INTO conversation_id;
    END IF;
    
    RETURN conversation_id;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener recomendaciones de servicios para un usuario
CREATE OR REPLACE FUNCTION get_service_recommendations(
    p_user_id UUID,
    p_limit INTEGER DEFAULT 10
)
RETURNS TABLE (
    id UUID,
    title VARCHAR,
    price DECIMAL,
    rating DECIMAL,
    freelancer_name TEXT,
    category_name VARCHAR,
    images TEXT[]
) AS $$
BEGIN
    RETURN QUERY
    -- Recomendaciones basadas en categorías de servicios previamente contratados
    WITH user_categories AS (
        SELECT DISTINCT s.category_id
        FROM contracts c
        JOIN services s ON c.service_id = s.id
        JOIN contractor_profiles cp ON c.contractor_id = cp.id
        WHERE cp.user_id = p_user_id
        UNION
        SELECT DISTINCT jo.category_id
        FROM contracts c
        JOIN job_offers jo ON c.job_offer_id = jo.id
        JOIN contractor_profiles cp ON c.contractor_id = cp.id
        WHERE cp.user_id = p_user_id
    )
    SELECT 
        s.id,
        s.title,
        s.price,
        s.rating,
        fp.first_name || ' ' || fp.last_name as freelancer_name,
        cat.name as category_name,
        s.images
    FROM services s
    JOIN freelancer_profiles fp ON s.freelancer_id = fp.id
    JOIN categories cat ON s.category_id = cat.id
    WHERE s.is_active = true
    AND (
        -- Servicios de categorías previamente contratadas
        s.category_id IN (SELECT category_id FROM user_categories)
        OR
        -- Servicios populares si no hay historial
        (NOT EXISTS (SELECT 1 FROM user_categories) AND s.orders_count > 5)
    )
    ORDER BY 
        CASE WHEN s.category_id IN (SELECT category_id FROM user_categories) THEN 0 ELSE 1 END,
        s.rating DESC,
        s.orders_count DESC,
        RANDOM()
    LIMIT p_limit;
END;
$$ LANGUAGE plpgsql;

-- Función para calcular comisión de la plataforma
CREATE OR REPLACE FUNCTION calculate_platform_commission(p_amount DECIMAL)
RETURNS DECIMAL AS $$
DECLARE
    commission_rate DECIMAL;
BEGIN
    SELECT CAST(value AS DECIMAL) INTO commission_rate
    FROM system_settings
    WHERE key = 'platform_commission';
    
    IF commission_rate IS NULL THEN
        commission_rate := 5.0; -- Default 5%
    END IF;
    
    RETURN ROUND(p_amount * (commission_rate / 100), 2);
END;
$$ LANGUAGE plpgsql;

-- Función para marcar mensajes como leídos
CREATE OR REPLACE FUNCTION mark_messages_as_read(
    p_conversation_id UUID,
    p_user_id UUID
)
RETURNS INTEGER AS $$
DECLARE
    updated_count INTEGER;
BEGIN
    UPDATE messages
    SET is_read = true
    WHERE conversation_id = p_conversation_id
    AND sender_id != p_user_id
    AND is_read = false;
    
    GET DIAGNOSTICS updated_count = ROW_COUNT;
    RETURN updated_count;
END;
$$ LANGUAGE plpgsql;
