-- Script para insertar usuarios de prueba - Camello Colombia
-- Ejecutar después del script principal neon_setup.sql

-- ============================================================================
-- USUARIOS DE PRUEBA
-- ============================================================================

-- Contraseña para todos los usuarios de prueba: "password123"
-- Hash generado con BCrypt: $2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6

-- FREELANCERS
INSERT INTO users (id, email, password_hash, role, is_active, is_verified) VALUES
('f47ac10b-58cc-4372-a567-0e02b2c3d479', 'maria.rodriguez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b810-9dad-11d1-80b4-00c04fd430c8', 'carlos.martinez@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b811-9dad-11d1-80b4-00c04fd430c8', 'ana.garcia@yahoo.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b812-9dad-11d1-80b4-00c04fd430c8', 'luis.hernandez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b813-9dad-11d1-80b4-00c04fd430c8', 'sofia.lopez@outlook.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b814-9dad-11d1-80b4-00c04fd430c8', 'diego.morales@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b815-9dad-11d1-80b4-00c04fd430c8', 'valentina.castro@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true),
('6ba7b816-9dad-11d1-80b4-00c04fd430c8', 'andres.silva@hotmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true)
ON CONFLICT (email) DO NOTHING;

-- ADMINISTRADORES ADICIONALES
INSERT INTO users (id, email, password_hash, role, is_active, is_verified) VALUES
('6ba7b817-9dad-11d1-80b4-00c04fd430c8', 'admin.sistema@camello.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'ADMIN', true, true),
('6ba7b818-9dad-11d1-80b4-00c04fd430c8', 'soporte@camello.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'ADMIN', true, true),
('6ba7b819-9dad-11d1-80b4-00c04fd430c8', 'moderador@camello.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'ADMIN', true, true)
ON CONFLICT (email) DO NOTHING;

-- CONTRATANTES
INSERT INTO users (id, email, password_hash, role, is_active, is_verified) VALUES
('6ba7b820-9dad-11d1-80b4-00c04fd430c8', 'contacto@innovatech.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'CONTRACTOR', true, true),
('6ba7b821-9dad-11d1-80b4-00c04fd430c8', 'rrhh@digitalcolombia.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'CONTRACTOR', true, true),
('6ba7b822-9dad-11d1-80b4-00c04fd430c8', 'proyectos@startupbogota.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'CONTRACTOR', true, true),
('6ba7b823-9dad-11d1-80b4-00c04fd430c8', 'marketing@ecommercecol.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'CONTRACTOR', true, true),
('6ba7b824-9dad-11d1-80b4-00c04fd430c8', 'desarrollo@agenciacreativa.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'CONTRACTOR', true, true)
ON CONFLICT (email) DO NOTHING;

-- ============================================================================
-- PERFILES DE FREELANCERS
-- ============================================================================

INSERT INTO freelancer_profiles (
    user_id, first_name, last_name, title, description, location, phone, 
    skills, hourly_rate, rating, total_reviews
) VALUES
(
    'f47ac10b-58cc-4372-a567-0e02b2c3d479',
    'María',
    'Rodríguez',
    'Diseñadora Gráfica Senior',
    'Diseñadora gráfica con más de 5 años de experiencia en branding, diseño de logos y materiales publicitarios. Especializada en identidad corporativa para empresas colombianas.',
    'Bogotá, Colombia',
    '+57 301 234 5678',
    ARRAY['Adobe Illustrator', 'Photoshop', 'InDesign', 'Branding', 'Logo Design', 'Print Design'],
    45000,
    4.8,
    23
),
(
    '6ba7b810-9dad-11d1-80b4-00c04fd430c8',
    'Carlos',
    'Martínez',
    'Desarrollador Full Stack',
    'Desarrollador full stack especializado en React, Node.js y bases de datos. Experiencia en e-commerce y aplicaciones web para el mercado colombiano.',
    'Medellín, Colombia',
    '+57 304 567 8901',
    ARRAY['React', 'Node.js', 'JavaScript', 'TypeScript', 'PostgreSQL', 'MongoDB', 'AWS'],
    65000,
    4.9,
    31
),
(
    '6ba7b811-9dad-11d1-80b4-00c04fd430c8',
    'Ana',
    'García',
    'Community Manager & Marketing Digital',
    'Especialista en redes sociales y marketing digital. Ayudo a empresas a crecer su presencia online y conectar con su audiencia colombiana.',
    'Cali, Colombia',
    '+57 315 678 9012',
    ARRAY['Social Media', 'Facebook Ads', 'Instagram Marketing', 'Content Creation', 'Google Ads', 'Analytics'],
    35000,
    4.7,
    18
),
(
    '6ba7b812-9dad-11d1-80b4-00c04fd430c8',
    'Luis',
    'Hernández',
    'Desarrollador Mobile iOS/Android',
    'Desarrollador de aplicaciones móviles nativas e híbridas. Especializado en Flutter y React Native para startups y empresas establecidas.',
    'Barranquilla, Colombia',
    '+57 320 789 0123',
    ARRAY['Flutter', 'React Native', 'iOS', 'Android', 'Firebase', 'API Integration'],
    70000,
    4.6,
    15
),
(
    '6ba7b813-9dad-11d1-80b4-00c04fd430c8',
    'Sofía',
    'López',
    'UX/UI Designer',
    'Diseñadora de experiencias digitales enfocada en crear interfaces intuitivas y atractivas. Experiencia en investigación de usuarios y prototipado.',
    'Bucaramanga, Colombia',
    '+57 312 890 1234',
    ARRAY['Figma', 'Adobe XD', 'User Research', 'Prototyping', 'Wireframing', 'Design Systems'],
    55000,
    4.8,
    27
),
(
    '6ba7b814-9dad-11d1-80b4-00c04fd430c8',
    'Diego',
    'Morales',
    'Editor de Video y Motion Graphics',
    'Editor profesional de video con experiencia en publicidad, contenido para redes sociales y videos corporativos. Especializado en After Effects.',
    'Pereira, Colombia',
    '+57 318 901 2345',
    ARRAY['Adobe Premiere', 'After Effects', 'DaVinci Resolve', 'Motion Graphics', 'Color Grading'],
    50000,
    4.5,
    12
),
(
    '6ba7b815-9dad-11d1-80b4-00c04fd430c8',
    'Valentina',
    'Castro',
    'Redactora y Content Writer',
    'Redactora especializada en contenido web, blogs y copywriting. Experiencia en SEO y marketing de contenidos para diversas industrias.',
    'Manizales, Colombia',
    '+57 316 012 3456',
    ARRAY['Copywriting', 'SEO Writing', 'Content Strategy', 'Blog Writing', 'Social Media Copy'],
    30000,
    4.7,
    21
),
(
    '6ba7b816-9dad-11d1-80b4-00c04fd430c8',
    'Andrés',
    'Silva',
    'Fotógrafo Profesional',
    'Fotógrafo especializado en productos, eventos corporativos y retratos. Experiencia en fotografía comercial y publicitaria.',
    'Cartagena, Colombia',
    '+57 317 123 4567',
    ARRAY['Product Photography', 'Event Photography', 'Portrait Photography', 'Adobe Lightroom', 'Photoshop'],
    40000,
    4.6,
    19
);

-- ============================================================================
-- PERFILES DE CONTRATANTES
-- ============================================================================

INSERT INTO contractor_profiles (
    user_id, company_name, contact_name, description, location, phone, 
    website, industry, company_size, rating, total_reviews
) VALUES
(
    '6ba7b820-9dad-11d1-80b4-00c04fd430c8',
    'InnovaTech Solutions',
    'Roberto Pérez',
    'Empresa de tecnología enfocada en soluciones digitales para pymes colombianas. Buscamos talento freelance para proyectos de desarrollo web y mobile.',
    'Bogotá, Colombia',
    '+57 601 234 5678',
    'https://innovatech.co',
    'Tecnología',
    'MEDIUM',
    4.5,
    8
),
(
    '6ba7b821-9dad-11d1-80b4-00c04fd430c8',
    'Digital Colombia',
    'Patricia Gómez',
    'Agencia de marketing digital especializada en transformación digital de empresas tradicionales. Trabajamos con freelancers especializados en marketing y diseño.',
    'Medellín, Colombia',
    '+57 604 567 8901',
    'https://digitalcolombia.com',
    'Marketing Digital',
    'SMALL',
    4.7,
    12
),
(
    '6ba7b822-9dad-11d1-80b4-00c04fd430c8',
    'Startup Bogotá',
    'Alejandro Vargas',
    'Incubadora de startups que conecta emprendedores con talento freelance. Enfocados en proyectos innovadores y disruptivos.',
    'Bogotá, Colombia',
    '+57 601 678 9012',
    'https://startupbogota.co',
    'Startups',
    'SMALL',
    4.3,
    6
),
(
    '6ba7b823-9dad-11d1-80b4-00c04fd430c8',
    'E-commerce Colombia',
    'Camila Ruiz',
    'Plataforma de e-commerce que ayuda a empresas a vender online. Buscamos freelancers para desarrollo, diseño y marketing digital.',
    'Cali, Colombia',
    '+57 602 789 0123',
    'https://ecommercecol.com',
    'E-commerce',
    'MEDIUM',
    4.6,
    15
),
(
    '6ba7b824-9dad-11d1-80b4-00c04fd430c8',
    'Agencia Creativa',
    'Fernando Torres',
    'Agencia creativa full-service especializada en branding, publicidad y contenido digital. Colaboramos con freelancers creativos y técnicos.',
    'Barranquilla, Colombia',
    '+57 605 890 1234',
    'https://agenciacreativa.co',
    'Publicidad',
    'SMALL',
    4.4,
    9
);

-- ============================================================================
-- SERVICIOS DE EJEMPLO
-- ============================================================================

-- Obtener IDs de categorías para los servicios
DO $$
DECLARE
    cat_diseno_grafico UUID;
    cat_desarrollo_web UUID;
    cat_redes_sociales UUID;
    cat_desarrollo_movil UUID;
    cat_diseno_web UUID;
BEGIN
    SELECT id INTO cat_diseno_grafico FROM categories WHERE name = 'Diseño Gráfico';
    SELECT id INTO cat_desarrollo_web FROM categories WHERE name = 'Desarrollo Web';
    SELECT id INTO cat_redes_sociales FROM categories WHERE name = 'Redes Sociales';
    SELECT id INTO cat_desarrollo_movil FROM categories WHERE name = 'Desarrollo Móvil';
    SELECT id INTO cat_diseno_web FROM categories WHERE name = 'Diseño Web';

    -- Servicios de María (Diseño Gráfico)
    INSERT INTO services (freelancer_id, category_id, title, description, price, delivery_time, revisions_included, tags, is_featured) VALUES
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
        cat_diseno_grafico,
        'Diseño de Logo Profesional + Manual de Marca',
        'Creo logos únicos y memorables para tu empresa, incluyendo manual de marca completo con paleta de colores, tipografías y aplicaciones.',
        250000,
        5,
        3,
        ARRAY['logo', 'branding', 'identidad corporativa', 'manual de marca'],
        true
    ),
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = 'f47ac10b-58cc-4372-a567-0e02b2c3d479'),
        cat_diseno_grafico,
        'Diseño de Material Publicitario',
        'Diseño de flyers, brochures, tarjetas de presentación y material promocional para tu negocio.',
        150000,
        3,
        2,
        ARRAY['flyer', 'brochure', 'material publicitario', 'print'],
        false
    );

    -- Servicios de Carlos (Desarrollo Web)
    INSERT INTO services (freelancer_id, category_id, title, description, price, delivery_time, revisions_included, tags, is_featured) VALUES
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = '6ba7b810-9dad-11d1-80b4-00c04fd430c8'),
        cat_desarrollo_web,
        'Desarrollo de E-commerce Completo',
        'Desarrollo de tienda online completa con React, Node.js, pasarela de pagos y panel administrativo.',
        2500000,
        21,
        2,
        ARRAY['ecommerce', 'react', 'nodejs', 'tienda online'],
        true
    ),
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = '6ba7b810-9dad-11d1-80b4-00c04fd430c8'),
        cat_desarrollo_web,
        'Landing Page Profesional',
        'Desarrollo de landing page optimizada para conversiones con diseño responsive y formularios de contacto.',
        800000,
        7,
        3,
        ARRAY['landing page', 'responsive', 'conversiones'],
        false
    );

    -- Servicios de Ana (Redes Sociales)
    INSERT INTO services (freelancer_id, category_id, title, description, price, delivery_time, revisions_included, tags, is_featured) VALUES
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = '6ba7b811-9dad-11d1-80b4-00c04fd430c8'),
        cat_redes_sociales,
        'Gestión Completa de Redes Sociales',
        'Gestión profesional de Instagram, Facebook y TikTok. Incluye contenido, programación y reportes mensuales.',
        600000,
        30,
        1,
        ARRAY['social media', 'instagram', 'facebook', 'community management'],
        true
    );

    -- Servicios de Luis (Desarrollo Móvil)
    INSERT INTO services (freelancer_id, category_id, title, description, price, delivery_time, revisions_included, tags, is_featured) VALUES
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = '6ba7b812-9dad-11d1-80b4-00c04fd430c8'),
        cat_desarrollo_movil,
        'App Móvil Flutter iOS/Android',
        'Desarrollo de aplicación móvil nativa con Flutter para iOS y Android, incluyendo backend y base de datos.',
        3500000,
        45,
        2,
        ARRAY['flutter', 'app movil', 'ios', 'android'],
        true
    );

    -- Servicios de Sofía (UX/UI)
    INSERT INTO services (freelancer_id, category_id, title, description, price, delivery_time, revisions_included, tags, is_featured) VALUES
    (
        (SELECT id FROM freelancer_profiles WHERE user_id = '6ba7b813-9dad-11d1-80b4-00c04fd430c8'),
        cat_diseno_web,
        'Diseño UX/UI para App o Web',
        'Diseño completo de interfaz de usuario con investigación UX, wireframes, prototipos y design system.',
        1200000,
        14,
        3,
        ARRAY['ux', 'ui', 'figma', 'prototipo', 'design system'],
        false
    );
END $$;

-- ============================================================================
-- OFERTAS DE TRABAJO DE EJEMPLO
-- ============================================================================

DO $$
DECLARE
    cat_desarrollo_web UUID;
    cat_diseno_grafico UUID;
    cat_marketing UUID;
BEGIN
    SELECT id INTO cat_desarrollo_web FROM categories WHERE name = 'Desarrollo Web';
    SELECT id INTO cat_diseno_grafico FROM categories WHERE name = 'Diseño Gráfico';
    SELECT id INTO cat_marketing FROM categories WHERE name = 'Marketing Digital';

    INSERT INTO job_offers (contractor_id, category_id, title, description, budget_min, budget_max, deadline, required_skills, experience_level, is_featured) VALUES
    (
        (SELECT id FROM contractor_profiles WHERE user_id = '6ba7b820-9dad-11d1-80b4-00c04fd430c8'),
        cat_desarrollo_web,
        'Desarrollo de Portal Web Corporativo',
        'Necesitamos desarrollar un portal web corporativo moderno y responsive para nuestra empresa de tecnología. Debe incluir secciones de servicios, equipo, blog y contacto.',
        1500000,
        2500000,
        CURRENT_DATE + INTERVAL '30 days',
        ARRAY['React', 'Node.js', 'Responsive Design', 'SEO'],
        'INTERMEDIATE',
        true
    ),
    (
        (SELECT id FROM contractor_profiles WHERE user_id = '6ba7b821-9dad-11d1-80b4-00c04fd430c8'),
        cat_diseno_grafico,
        'Rediseño de Identidad Visual Completa',
        'Buscamos diseñador para rediseñar completamente nuestra identidad visual: logo, colores, tipografías y aplicaciones en diferentes medios.',
        800000,
        1200000,
        CURRENT_DATE + INTERVAL '20 days',
        ARRAY['Branding', 'Adobe Illustrator', 'Manual de Marca'],
        'SENIOR',
        false
    ),
    (
        (SELECT id FROM contractor_profiles WHERE user_id = '6ba7b823-9dad-11d1-80b4-00c04fd430c8'),
        cat_marketing,
        'Estrategia de Marketing Digital para E-commerce',
        'Necesitamos desarrollar una estrategia completa de marketing digital para nuestro e-commerce, incluyendo SEO, SEM y redes sociales.',
        1000000,
        1800000,
        CURRENT_DATE + INTERVAL '25 days',
        ARRAY['Google Ads', 'Facebook Ads', 'SEO', 'Analytics'],
        'INTERMEDIATE',
        true
    );
END $$;

-- ============================================================================
-- DATOS ADICIONALES
-- ============================================================================

-- Actualizar contadores de vistas y pedidos en servicios
UPDATE services SET 
    views_count = FLOOR(RANDOM() * 100) + 10,
    orders_count = FLOOR(RANDOM() * 20) + 1,
    rating = ROUND((RANDOM() * 1.5 + 3.5)::numeric, 1)
WHERE id IS NOT NULL;

-- Actualizar contadores en ofertas de trabajo
UPDATE job_offers SET 
    applications_count = FLOOR(RANDOM() * 15) + 1
WHERE id IS NOT NULL;

COMMIT;