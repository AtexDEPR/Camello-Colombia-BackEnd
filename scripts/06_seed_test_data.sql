-- Datos de prueba para desarrollo y testing de Camello

-- Usuarios de prueba (contraseña para todos: test123)
INSERT INTO users (id, email, password_hash, role, is_active, is_verified) VALUES
-- Freelancers
('550e8400-e29b-41d4-a716-446655440001', 'maria.designer@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'FREELANCER', true, true),
('550e8400-e29b-41d4-a716-446655440002', 'carlos.dev@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'FREELANCER', true, true),
('550e8400-e29b-41d4-a716-446655440003', 'ana.marketing@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'FREELANCER', true, true),
('550e8400-e29b-41d4-a716-446655440004', 'luis.writer@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'FREELANCER', true, true),
('550e8400-e29b-41d4-a716-446655440005', 'sofia.photo@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'FREELANCER', true, true),

-- Contratantes
('550e8400-e29b-41d4-a716-446655440010', 'empresa1@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'CONTRACTOR', true, true),
('550e8400-e29b-41d4-a716-446655440011', 'startup@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'CONTRACTOR', true, true),
('550e8400-e29b-41d4-a716-446655440012', 'pyme.bogota@gmail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'CONTRACTOR', true, true);

-- Perfiles de freelancers
INSERT INTO freelancer_profiles (id, user_id, first_name, last_name, title, description, location, phone, skills, hourly_rate, rating, total_reviews) VALUES
('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440001', 'María', 'González', 'Diseñadora Gráfica Senior', 'Diseñadora con 5 años de experiencia en branding y diseño digital. Especializada en identidad corporativa y diseño web.', 'Bogotá, Colombia', '+57 300 123 4567', ARRAY['Photoshop', 'Illustrator', 'Figma', 'Branding', 'Logo Design'], 45000, 4.8, 23),
('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002', 'Carlos', 'Rodríguez', 'Desarrollador Full Stack', 'Desarrollador con experiencia en React, Node.js y bases de datos. Especializado en aplicaciones web modernas y APIs.', 'Medellín, Colombia', '+57 301 234 5678', ARRAY['React', 'Node.js', 'JavaScript', 'PostgreSQL', 'MongoDB'], 60000, 4.9, 31),
('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003', 'Ana', 'Martínez', 'Especialista en Marketing Digital', 'Experta en redes sociales y publicidad digital. Ayudo a empresas a crecer su presencia online y aumentar ventas.', 'Cali, Colombia', '+57 302 345 6789', ARRAY['Facebook Ads', 'Google Ads', 'Instagram', 'SEO', 'Analytics'], 35000, 4.7, 18),
('650e8400-e29b-41d4-a716-446655440004', '550e8400-e29b-41d4-a716-446655440004', 'Luis', 'Herrera', 'Redactor de Contenido', 'Redactor especializado en contenido web, blogs y copywriting. Experiencia en diversos sectores y SEO.', 'Barranquilla, Colombia', '+57 303 456 7890', ARRAY['Copywriting', 'SEO Writing', 'Content Strategy', 'WordPress', 'Research'], 25000, 4.6, 15),
('650e8400-e29b-41d4-a716-446655440005', '550e8400-e29b-41d4-a716-446655440005', 'Sofía', 'Vargas', 'Fotógrafa Profesional', 'Fotógrafa especializada en productos, eventos corporativos y retratos. Equipo profesional y estudio propio.', 'Cartagena, Colombia', '+57 304 567 8901', ARRAY['Fotografía de Producto', 'Eventos', 'Retratos', 'Lightroom', 'Photoshop'], 80000, 4.9, 27);

-- Perfiles de contratantes
INSERT INTO contractor_profiles (id, user_id, company_name, contact_name, description, location, industry, company_size, rating, total_reviews) VALUES
('750e8400-e29b-41d4-a716-446655440010', '550e8400-e29b-41d4-a716-446655440010', 'TechSolutions SAS', 'Roberto Pérez', 'Empresa de tecnología especializada en soluciones empresariales. Buscamos talento para proyectos innovadores.', 'Bogotá, Colombia', 'Tecnología', 'MEDIUM', 4.5, 12),
('750e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440011', 'StartupColombia', 'Camila Torres', 'Startup en crecimiento enfocada en e-commerce. Necesitamos apoyo en diseño y desarrollo.', 'Medellín, Colombia', 'E-commerce', 'SMALL', 4.3, 8),
('750e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440012', 'PYME Bogotá', 'Fernando Castro', 'Pequeña empresa familiar que busca digitalizarse y mejorar su presencia online.', 'Bogotá, Colombia', 'Retail', 'SMALL', 4.7, 5);

-- Obtener IDs de categorías para los servicios
DO $$
DECLARE
    cat_diseno_grafico UUID;
    cat_desarrollo_web UUID;
    cat_redes_sociales UUID;
    cat_redaccion UUID;
    cat_fotografia UUID;
BEGIN
    SELECT id INTO cat_diseno_grafico FROM categories WHERE name = 'Diseño Gráfico';
    SELECT id INTO cat_desarrollo_web FROM categories WHERE name = 'Desarrollo Web';
    SELECT id INTO cat_redes_sociales FROM categories WHERE name = 'Redes Sociales';
    SELECT id INTO cat_redaccion FROM categories WHERE name = 'Redacción de Contenido';
    SELECT id INTO cat_fotografia FROM categories WHERE name = 'Fotografía';

    -- Servicios de prueba
    INSERT INTO services (id, freelancer_id, category_id, title, description, price, delivery_time, images, tags, rating, orders_count) VALUES
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440001', cat_diseno_grafico, 'Diseño de Logo Profesional', 'Creo logos únicos y memorables para tu marca. Incluye 3 propuestas iniciales, revisiones ilimitadas y archivos en todos los formatos.', 150000, 3, ARRAY['https://example.com/logo1.jpg', 'https://example.com/logo2.jpg'], ARRAY['logo', 'branding', 'identidad'], 4.8, 15),
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440001', cat_diseno_grafico, 'Identidad Corporativa Completa', 'Desarrollo completo de identidad visual: logo, tarjetas, papelería, manual de marca y más.', 450000, 7, ARRAY['https://example.com/brand1.jpg'], ARRAY['branding', 'corporativo', 'identidad'], 4.9, 8),
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440002', cat_desarrollo_web, 'Sitio Web Responsivo', 'Desarrollo de sitio web moderno y responsivo con React. Incluye diseño, desarrollo y despliegue.', 800000, 14, ARRAY['https://example.com/web1.jpg'], ARRAY['react', 'responsive', 'moderno'], 4.9, 12),
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440002', cat_desarrollo_web, 'API REST con Node.js', 'Desarrollo de API REST robusta y escalable con Node.js, Express y base de datos.', 600000, 10, ARRAY['https://example.com/api1.jpg'], ARRAY['nodejs', 'api', 'backend'], 4.8, 6),
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440003', cat_redes_sociales, 'Gestión de Redes Sociales', 'Gestión completa de tus redes sociales por 1 mes. Incluye contenido, programación y reportes.', 300000, 30, ARRAY['https://example.com/social1.jpg'], ARRAY['instagram', 'facebook', 'contenido'], 4.7, 20),
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440004', cat_redaccion, 'Artículos de Blog SEO', 'Redacción de artículos optimizados para SEO. Investigación de keywords incluida.', 80000, 5, ARRAY['https://example.com/blog1.jpg'], ARRAY['seo', 'blog', 'contenido'], 4.6, 25),
    (uuid_generate_v4(), '650e8400-e29b-41d4-a716-446655440005', cat_fotografia, 'Sesión de Fotos de Producto', 'Sesión profesional de fotografía de productos con iluminación de estudio y edición incluida.', 200000, 3, ARRAY['https://example.com/photo1.jpg'], ARRAY['producto', 'estudio', 'profesional'], 4.9, 18);

    -- Ofertas de trabajo de prueba
    INSERT INTO job_offers (id, contractor_id, category_id, title, description, budget_min, budget_max, deadline, required_skills, applications_count) VALUES
    (uuid_generate_v4(), '750e8400-e29b-41d4-a716-446655440010', cat_desarrollo_web, 'Desarrollo de Plataforma E-commerce', 'Necesitamos desarrollar una plataforma de e-commerce completa con carrito de compras, pagos y administración.', 2000000, 3500000, '2024-04-15', ARRAY['React', 'Node.js', 'E-commerce', 'Pagos'], 5),
    (uuid_generate_v4(), '750e8400-e29b-41d4-a716-446655440011', cat_diseno_grafico, 'Rediseño de Identidad Visual', 'Startup busca rediseñar completamente su identidad visual y crear materiales de marketing.', 800000, 1200000, '2024-03-30', ARRAY['Branding', 'Logo', 'Marketing'], 3),
    (uuid_generate_v4(), '750e8400-e29b-41d4-a716-446655440012', cat_redes_sociales, 'Estrategia de Marketing Digital', 'PYME necesita estrategia completa de marketing digital y gestión de redes sociales por 3 meses.', 600000, 900000, '2024-05-01', ARRAY['Marketing Digital', 'Redes Sociales', 'Estrategia'], 7);
END $$;
