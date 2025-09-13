-- Índices para optimizar consultas en la base de datos de Camello

-- Índices para usuarios
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_active ON users(is_active);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Índices para perfiles de freelancers
CREATE INDEX idx_freelancer_profiles_user_id ON freelancer_profiles(user_id);
CREATE INDEX idx_freelancer_profiles_location ON freelancer_profiles(location);
CREATE INDEX idx_freelancer_profiles_rating ON freelancer_profiles(rating DESC);
CREATE INDEX idx_freelancer_profiles_availability ON freelancer_profiles(availability);
CREATE INDEX idx_freelancer_profiles_skills ON freelancer_profiles USING GIN(skills);

-- Índices para perfiles de contratantes
CREATE INDEX idx_contractor_profiles_user_id ON contractor_profiles(user_id);
CREATE INDEX idx_contractor_profiles_location ON contractor_profiles(location);
CREATE INDEX idx_contractor_profiles_industry ON contractor_profiles(industry);
CREATE INDEX idx_contractor_profiles_rating ON contractor_profiles(rating DESC);

-- Índices para categorías
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_categories_active ON categories(is_active);

-- Índices para servicios
CREATE INDEX idx_services_freelancer_id ON services(freelancer_id);
CREATE INDEX idx_services_category_id ON services(category_id);
CREATE INDEX idx_services_active ON services(is_active);
CREATE INDEX idx_services_featured ON services(is_featured);
CREATE INDEX idx_services_price ON services(price);
CREATE INDEX idx_services_rating ON services(rating DESC);
CREATE INDEX idx_services_created_at ON services(created_at DESC);
CREATE INDEX idx_services_tags ON services USING GIN(tags);

-- Índices para ofertas de trabajo
CREATE INDEX idx_job_offers_contractor_id ON job_offers(contractor_id);
CREATE INDEX idx_job_offers_category_id ON job_offers(category_id);
CREATE INDEX idx_job_offers_active ON job_offers(is_active);
CREATE INDEX idx_job_offers_featured ON job_offers(is_featured);
CREATE INDEX idx_job_offers_budget ON job_offers(budget_max DESC);
CREATE INDEX idx_job_offers_deadline ON job_offers(deadline);
CREATE INDEX idx_job_offers_created_at ON job_offers(created_at DESC);
CREATE INDEX idx_job_offers_skills ON job_offers USING GIN(required_skills);

-- Índices para aplicaciones
CREATE INDEX idx_job_applications_job_offer_id ON job_applications(job_offer_id);
CREATE INDEX idx_job_applications_freelancer_id ON job_applications(freelancer_id);
CREATE INDEX idx_job_applications_status ON job_applications(status);
CREATE INDEX idx_job_applications_applied_at ON job_applications(applied_at DESC);

-- Índices para contratos
CREATE INDEX idx_contracts_service_id ON contracts(service_id);
CREATE INDEX idx_contracts_job_offer_id ON contracts(job_offer_id);
CREATE INDEX idx_contracts_freelancer_id ON contracts(freelancer_id);
CREATE INDEX idx_contracts_contractor_id ON contracts(contractor_id);
CREATE INDEX idx_contracts_status ON contracts(status);
CREATE INDEX idx_contracts_payment_status ON contracts(payment_status);
CREATE INDEX idx_contracts_created_at ON contracts(created_at DESC);

-- Índices para entregas
CREATE INDEX idx_contract_deliveries_contract_id ON contract_deliveries(contract_id);
CREATE INDEX idx_contract_deliveries_delivered_at ON contract_deliveries(delivered_at DESC);

-- Índices para reseñas
CREATE INDEX idx_reviews_contract_id ON reviews(contract_id);
CREATE INDEX idx_reviews_reviewer_id ON reviews(reviewer_id);
CREATE INDEX idx_reviews_reviewee_id ON reviews(reviewee_id);
CREATE INDEX idx_reviews_rating ON reviews(rating DESC);
CREATE INDEX idx_reviews_created_at ON reviews(created_at DESC);

-- Índices para conversaciones
CREATE INDEX idx_conversations_participant1_id ON conversations(participant1_id);
CREATE INDEX idx_conversations_participant2_id ON conversations(participant2_id);
CREATE INDEX idx_conversations_contract_id ON conversations(contract_id);
CREATE INDEX idx_conversations_last_message_at ON conversations(last_message_at DESC);

-- Índices para mensajes
CREATE INDEX idx_messages_conversation_id ON messages(conversation_id);
CREATE INDEX idx_messages_sender_id ON messages(sender_id);
CREATE INDEX idx_messages_sent_at ON messages(sent_at DESC);
CREATE INDEX idx_messages_is_read ON messages(is_read);

-- Índices para notificaciones
CREATE INDEX idx_notifications_user_id ON notifications(user_id);
CREATE INDEX idx_notifications_type ON notifications(type);
CREATE INDEX idx_notifications_is_read ON notifications(is_read);
CREATE INDEX idx_notifications_created_at ON notifications(created_at DESC);

-- Índices para reportes
CREATE INDEX idx_reports_reporter_id ON reports(reporter_id);
CREATE INDEX idx_reports_reported_user_id ON reports(reported_user_id);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_reports_created_at ON reports(created_at DESC);

-- Índices compuestos para consultas comunes
CREATE INDEX idx_services_category_active_rating ON services(category_id, is_active, rating DESC);
CREATE INDEX idx_job_offers_category_active_created ON job_offers(category_id, is_active, created_at DESC);
CREATE INDEX idx_contracts_freelancer_status ON contracts(freelancer_id, status);
CREATE INDEX idx_contracts_contractor_status ON contracts(contractor_id, status);
