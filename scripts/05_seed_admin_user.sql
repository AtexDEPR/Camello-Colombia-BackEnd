-- Crear usuario administrador por defecto para Camello

-- Insertar usuario admin (contraseña: admin123 - debe cambiarse en producción)
INSERT INTO users (id, email, password_hash, role, is_active, is_verified) VALUES
(uuid_generate_v4(), 'admin@camello.co', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN', true, true);

-- Insertar configuraciones iniciales del sistema
INSERT INTO system_settings (key, value, description) VALUES
('platform_commission', '5.0', 'Comisión de la plataforma en porcentaje'),
('min_service_price', '10000', 'Precio mínimo para servicios en COP'),
('max_service_price', '10000000', 'Precio máximo para servicios en COP'),
('featured_service_cost', '50000', 'Costo para destacar un servicio en COP'),
('max_images_per_service', '5', 'Número máximo de imágenes por servicio'),
('max_portfolio_items', '10', 'Número máximo de elementos en portafolio'),
('review_period_days', '14', 'Días para dejar reseña después de completar contrato'),
('dispute_period_days', '7', 'Días para abrir disputa después de entrega'),
('platform_name', 'Camello', 'Nombre de la plataforma'),
('platform_email', 'soporte@camello.co', 'Email de soporte de la plataforma'),
('maintenance_mode', 'false', 'Modo de mantenimiento activado/desactivado');
