-- Vistas útiles para consultas comunes en Camello

-- Vista de servicios con información del freelancer y categoría
CREATE VIEW services_with_details AS
SELECT 
    s.id,
    s.title,
    s.description,
    s.price,
    s.delivery_time,
    s.rating,
    s.orders_count,
    s.views_count,
    s.is_active,
    s.is_featured,
    s.created_at,
    -- Información del freelancer
    fp.first_name || ' ' || fp.last_name as freelancer_name,
    fp.profile_picture as freelancer_picture,
    fp.location as freelancer_location,
    fp.rating as freelancer_rating,
    -- Información de la categoría
    c.name as category_name,
    c.icon as category_icon
FROM services s
JOIN freelancer_profiles fp ON s.freelancer_id = fp.id
JOIN categories c ON s.category_id = c.id;

-- Vista de ofertas de trabajo con información del contratante
CREATE VIEW job_offers_with_details AS
SELECT 
    jo.id,
    jo.title,
    jo.description,
    jo.budget_min,
    jo.budget_max,
    jo.deadline,
    jo.required_skills,
    jo.experience_level,
    jo.project_type,
    jo.applications_count,
    jo.is_active,
    jo.is_featured,
    jo.created_at,
    -- Información del contratante
    cp.company_name,
    cp.contact_name,
    cp.location as company_location,
    cp.industry,
    cp.rating as company_rating,
    -- Información de la categoría
    c.name as category_name,
    c.icon as category_icon
FROM job_offers jo
JOIN contractor_profiles cp ON jo.contractor_id = cp.id
JOIN categories c ON jo.category_id = c.id;

-- Vista de contratos con información completa
CREATE VIEW contracts_with_details AS
SELECT 
    ct.id,
    ct.title,
    ct.description,
    ct.agreed_price,
    ct.delivery_date,
    ct.status,
    ct.payment_status,
    ct.created_at,
    ct.completed_at,
    -- Información del freelancer
    fp.first_name || ' ' || fp.last_name as freelancer_name,
    fp.profile_picture as freelancer_picture,
    u1.email as freelancer_email,
    -- Información del contratante
    cp.company_name,
    cp.contact_name,
    u2.email as contractor_email,
    -- Información del servicio (si aplica)
    s.title as service_title,
    -- Información de la oferta (si aplica)
    jo.title as job_offer_title
FROM contracts ct
JOIN freelancer_profiles fp ON ct.freelancer_id = fp.id
JOIN contractor_profiles cp ON ct.contractor_id = cp.id
JOIN users u1 ON fp.user_id = u1.id
JOIN users u2 ON cp.user_id = u2.id
LEFT JOIN services s ON ct.service_id = s.id
LEFT JOIN job_offers jo ON ct.job_offer_id = jo.id;

-- Vista de estadísticas por freelancer
CREATE VIEW freelancer_stats AS
SELECT 
    fp.id,
    fp.first_name || ' ' || fp.last_name as name,
    fp.rating,
    fp.total_reviews,
    fp.total_earnings,
    -- Servicios activos
    COUNT(DISTINCT s.id) as active_services,
    -- Contratos completados
    COUNT(DISTINCT CASE WHEN ct.status = 'COMPLETED' THEN ct.id END) as completed_contracts,
    -- Contratos activos
    COUNT(DISTINCT CASE WHEN ct.status = 'ACTIVE' THEN ct.id END) as active_contracts,
    -- Promedio de precio de servicios
    AVG(s.price) as avg_service_price
FROM freelancer_profiles fp
LEFT JOIN services s ON fp.id = s.freelancer_id AND s.is_active = true
LEFT JOIN contracts ct ON fp.id = ct.freelancer_id
GROUP BY fp.id, fp.first_name, fp.last_name, fp.rating, fp.total_reviews, fp.total_earnings;

-- Vista de estadísticas por contratante
CREATE VIEW contractor_stats AS
SELECT 
    cp.id,
    cp.company_name,
    cp.contact_name,
    cp.rating,
    cp.total_reviews,
    cp.total_spent,
    -- Ofertas activas
    COUNT(DISTINCT jo.id) as active_job_offers,
    -- Contratos completados
    COUNT(DISTINCT CASE WHEN ct.status = 'COMPLETED' THEN ct.id END) as completed_contracts,
    -- Contratos activos
    COUNT(DISTINCT CASE WHEN ct.status = 'ACTIVE' THEN ct.id END) as active_contracts,
    -- Promedio de presupuesto de ofertas
    AVG((jo.budget_min + jo.budget_max) / 2) as avg_job_budget
FROM contractor_profiles cp
LEFT JOIN job_offers jo ON cp.id = jo.contractor_id AND jo.is_active = true
LEFT JOIN contracts ct ON cp.id = ct.contractor_id
GROUP BY cp.id, cp.company_name, cp.contact_name, cp.rating, cp.total_reviews, cp.total_spent;

-- Vista de conversaciones con último mensaje
CREATE VIEW conversations_with_last_message AS
SELECT 
    c.id,
    c.participant1_id,
    c.participant2_id,
    c.contract_id,
    c.last_message_at,
    c.created_at,
    -- Último mensaje
    m.content as last_message_content,
    m.sender_id as last_message_sender_id,
    -- Información de participantes
    u1.email as participant1_email,
    u2.email as participant2_email,
    -- Nombres de participantes (freelancer o contratante)
    COALESCE(fp1.first_name || ' ' || fp1.last_name, cp1.contact_name) as participant1_name,
    COALESCE(fp2.first_name || ' ' || fp2.last_name, cp2.contact_name) as participant2_name
FROM conversations c
LEFT JOIN messages m ON c.id = m.conversation_id AND m.sent_at = c.last_message_at
JOIN users u1 ON c.participant1_id = u1.id
JOIN users u2 ON c.participant2_id = u2.id
LEFT JOIN freelancer_profiles fp1 ON u1.id = fp1.user_id
LEFT JOIN contractor_profiles cp1 ON u1.id = cp1.user_id
LEFT JOIN freelancer_profiles fp2 ON u2.id = fp2.user_id
LEFT JOIN contractor_profiles cp2 ON u2.id = cp2.user_id;

-- Vista de reseñas con información completa
CREATE VIEW reviews_with_details AS
SELECT 
    r.id,
    r.rating,
    r.comment,
    r.is_public,
    r.created_at,
    -- Información del contrato
    ct.title as contract_title,
    ct.agreed_price,
    -- Información del reviewer
    COALESCE(fp_reviewer.first_name || ' ' || fp_reviewer.last_name, cp_reviewer.contact_name) as reviewer_name,
    u_reviewer.email as reviewer_email,
    u_reviewer.role as reviewer_role,
    -- Información del reviewee
    COALESCE(fp_reviewee.first_name || ' ' || fp_reviewee.last_name, cp_reviewee.contact_name) as reviewee_name,
    u_reviewee.email as reviewee_email,
    u_reviewee.role as reviewee_role
FROM reviews r
JOIN contracts ct ON r.contract_id = ct.id
JOIN users u_reviewer ON r.reviewer_id = u_reviewer.id
JOIN users u_reviewee ON r.reviewee_id = u_reviewee.id
LEFT JOIN freelancer_profiles fp_reviewer ON u_reviewer.id = fp_reviewer.user_id
LEFT JOIN contractor_profiles cp_reviewer ON u_reviewer.id = cp_reviewer.user_id
LEFT JOIN freelancer_profiles fp_reviewee ON u_reviewee.id = fp_reviewee.user_id
LEFT JOIN contractor_profiles cp_reviewee ON u_reviewee.id = cp_reviewee.user_id;
