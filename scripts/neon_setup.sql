-- Script adaptado para Neon - Camello Colombia
-- Ejecutar este script completo en la consola de Neon

-- Crear extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Tabla de usuarios base
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('FREELANCER', 'CONTRACTOR', 'ADMIN')),
    is_active BOOLEAN DEFAULT true,
    is_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP WITH TIME ZONE NULL
);

-- Tabla de categorías
CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    icon VARCHAR(50),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de perfiles de freelancers
CREATE TABLE IF NOT EXISTS freelancer_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    profile_picture VARCHAR(500),
    title VARCHAR(200),
    description TEXT,
    location VARCHAR(100),
    phone VARCHAR(20),
    skills TEXT[],
    portfolio_urls TEXT[],
    hourly_rate DECIMAL(10,2),
    availability VARCHAR(50) DEFAULT 'AVAILABLE',
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_reviews INTEGER DEFAULT 0,
    total_earnings DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de perfiles de contratantes
CREATE TABLE IF NOT EXISTS contractor_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    company_name VARCHAR(200),
    contact_name VARCHAR(200) NOT NULL,
    profile_picture VARCHAR(500),
    description TEXT,
    location VARCHAR(100),
    phone VARCHAR(20),
    website VARCHAR(500),
    industry VARCHAR(100),
    company_size VARCHAR(50) DEFAULT 'SMALL',
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_reviews INTEGER DEFAULT 0,
    total_spent DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de servicios
CREATE TABLE IF NOT EXISTS services (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(id),
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    delivery_time INTEGER NOT NULL,
    revisions_included INTEGER DEFAULT 1,
    images TEXT[],
    tags TEXT[],
    is_active BOOLEAN DEFAULT true,
    is_featured BOOLEAN DEFAULT false,
    views_count INTEGER DEFAULT 0,
    orders_count INTEGER DEFAULT 0,
    rating DECIMAL(3,2) DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de ofertas de trabajo
CREATE TABLE IF NOT EXISTS job_offers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    contractor_id UUID NOT NULL REFERENCES contractor_profiles(id) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories(id),
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    budget_min DECIMAL(10,2),
    budget_max DECIMAL(10,2),
    deadline DATE,
    required_skills TEXT[],
    experience_level VARCHAR(50) DEFAULT 'INTERMEDIATE',
    project_type VARCHAR(50) DEFAULT 'ONE_TIME',
    is_active BOOLEAN DEFAULT true,
    is_featured BOOLEAN DEFAULT false,
    applications_count INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de aplicaciones a trabajos
CREATE TABLE IF NOT EXISTS job_applications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    job_offer_id UUID NOT NULL REFERENCES job_offers(id) ON DELETE CASCADE,
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(id) ON DELETE CASCADE,
    cover_letter TEXT NOT NULL,
    proposed_price DECIMAL(10,2),
    estimated_delivery_days INTEGER,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de contratos
CREATE TABLE IF NOT EXISTS contracts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    service_id UUID REFERENCES services(id),
    job_offer_id UUID REFERENCES job_offers(id),
    freelancer_id UUID NOT NULL REFERENCES freelancer_profiles(id),
    contractor_id UUID NOT NULL REFERENCES contractor_profiles(id),
    title VARCHAR(200) NOT NULL,
    description TEXT,
    agreed_price DECIMAL(10,2) NOT NULL,
    delivery_date DATE,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE
);

-- Tabla de conversaciones
CREATE TABLE IF NOT EXISTS conversations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    participant1_id UUID NOT NULL REFERENCES users(id),
    participant2_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de mensajes
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    conversation_id UUID NOT NULL REFERENCES conversations(id) ON DELETE CASCADE,
    sender_id UUID NOT NULL REFERENCES users(id),
    content TEXT NOT NULL,
    attachments TEXT[],
    is_read BOOLEAN DEFAULT false,
    sent_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de reseñas
CREATE TABLE IF NOT EXISTS reviews (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    contract_id UUID NOT NULL REFERENCES contracts(id),
    reviewer_id UUID NOT NULL REFERENCES users(id),
    reviewee_id UUID NOT NULL REFERENCES users(id),
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    reviewer_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de notificaciones
CREATE TABLE IF NOT EXISTS notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT,
    data JSONB,
    is_read BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Insertar categorías iniciales
INSERT INTO categories (name, description, icon, is_active) VALUES
('Diseño Gráfico', 'Logos, branding, diseño de materiales publicitarios', 'palette', true),
('Diseño Web', 'Diseño de interfaces, UX/UI, mockups', 'monitor', true),
('Desarrollo Web', 'Sitios web, aplicaciones web, e-commerce', 'code', true),
('Desarrollo Móvil', 'Apps para iOS y Android', 'smartphone', true),
('Redes Sociales', 'Gestión de redes sociales, contenido, community management', 'share-2', true),
('Marketing Digital', 'SEO, SEM, publicidad digital, estrategias de marketing', 'trending-up', true),
('Redacción y Contenido', 'Copywriting, blogs, artículos, contenido web', 'edit-3', true),
('Traducción', 'Traducción de documentos, localización, interpretación', 'globe', true),
('Video y Animación', 'Edición de video, motion graphics, animaciones', 'video', true),
('Fotografía', 'Fotografía de productos, eventos, retratos', 'camera', true)
ON CONFLICT (name) DO NOTHING;

-- Crear usuario administrador
INSERT INTO users (id, email, password_hash, role, is_active, is_verified) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'admin@camello.co', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'ADMIN', true, true)
ON CONFLICT (email) DO NOTHING;

-- Crear índices para optimización
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_role ON users(role);
CREATE INDEX IF NOT EXISTS idx_services_active ON services(is_active, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_services_category ON services(category_id, is_active);
CREATE INDEX IF NOT EXISTS idx_job_offers_active ON job_offers(is_active, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_job_offers_category ON job_offers(category_id, is_active);
CREATE INDEX IF NOT EXISTS idx_messages_conversation ON messages(conversation_id, sent_at DESC);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id, is_read, created_at DESC);

-- Función para actualizar updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para updated_at
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_freelancer_profiles_updated_at ON freelancer_profiles;
CREATE TRIGGER update_freelancer_profiles_updated_at BEFORE UPDATE ON freelancer_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_contractor_profiles_updated_at ON contractor_profiles;
CREATE TRIGGER update_contractor_profiles_updated_at BEFORE UPDATE ON contractor_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_services_updated_at ON services;
CREATE TRIGGER update_services_updated_at BEFORE UPDATE ON services
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_job_offers_updated_at ON job_offers;
CREATE TRIGGER update_job_offers_updated_at BEFORE UPDATE ON job_offers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_contracts_updated_at ON contracts;
CREATE TRIGGER update_contracts_updated_at BEFORE UPDATE ON contracts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_conversations_updated_at ON conversations;
CREATE TRIGGER update_conversations_updated_at BEFORE UPDATE ON conversations
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();