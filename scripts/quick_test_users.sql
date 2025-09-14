-- Script rápido para crear usuarios de prueba básicos
-- Contraseña para todos: "password123"

-- Usuario Freelancer de prueba
INSERT INTO users (email, password_hash, role, is_active, is_verified) VALUES
('freelancer@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'FREELANCER', true, true)
ON CONFLICT (email) DO NOTHING;

-- Usuario Contratante de prueba  
INSERT INTO users (email, password_hash, role, is_active, is_verified) VALUES
('contractor@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'CONTRACTOR', true, true)
ON CONFLICT (email) DO NOTHING;

-- Usuario Admin adicional
INSERT INTO users (email, password_hash, role, is_active, is_verified) VALUES
('admin@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6', 'ADMIN', true, true)
ON CONFLICT (email) DO NOTHING;

-- Perfil básico para el freelancer
INSERT INTO freelancer_profiles (user_id, first_name, last_name, title, description, location, hourly_rate)
SELECT u.id, 'Juan', 'Pérez', 'Desarrollador Web', 'Desarrollador con experiencia en React y Node.js', 'Bogotá, Colombia', 50000
FROM users u 
WHERE u.email = 'freelancer@test.com'
AND NOT EXISTS (SELECT 1 FROM freelancer_profiles fp WHERE fp.user_id = u.id);

-- Perfil básico para el contratante
INSERT INTO contractor_profiles (user_id, company_name, contact_name, description, location)
SELECT u.id, 'Empresa Test', 'María González', 'Empresa de prueba para testing', 'Medellín, Colombia'
FROM users u 
WHERE u.email = 'contractor@test.com'
AND NOT EXISTS (SELECT 1 FROM contractor_profiles cp WHERE cp.user_id = u.id);