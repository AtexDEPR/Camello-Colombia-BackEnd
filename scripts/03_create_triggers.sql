-- Triggers para automatizar actualizaciones en la base de datos de Camello

-- Función para actualizar el campo updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Triggers para updated_at en todas las tablas relevantes
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_freelancer_profiles_updated_at BEFORE UPDATE ON freelancer_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_contractor_profiles_updated_at BEFORE UPDATE ON contractor_profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_services_updated_at BEFORE UPDATE ON services
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_job_offers_updated_at BEFORE UPDATE ON job_offers
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_job_applications_updated_at BEFORE UPDATE ON job_applications
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_contracts_updated_at BEFORE UPDATE ON contracts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Función para actualizar estadísticas de freelancer cuando se crea una reseña
CREATE OR REPLACE FUNCTION update_freelancer_stats_on_review()
RETURNS TRIGGER AS $$
DECLARE
    freelancer_user_id UUID;
    avg_rating DECIMAL(3,2);
    review_count INTEGER;
BEGIN
    -- Obtener el user_id del freelancer desde el contrato
    SELECT fp.user_id INTO freelancer_user_id
    FROM contracts c
    JOIN freelancer_profiles fp ON c.freelancer_id = fp.id
    WHERE c.id = NEW.contract_id;
    
    -- Solo actualizar si el reviewee es el freelancer
    IF NEW.reviewee_id = freelancer_user_id THEN
        -- Calcular nueva calificación promedio y total de reseñas
        SELECT AVG(rating), COUNT(*)
        INTO avg_rating, review_count
        FROM reviews r
        JOIN contracts c ON r.contract_id = c.id
        JOIN freelancer_profiles fp ON c.freelancer_id = fp.id
        WHERE fp.user_id = freelancer_user_id;
        
        -- Actualizar perfil del freelancer
        UPDATE freelancer_profiles
        SET rating = COALESCE(avg_rating, 0.00),
            total_reviews = review_count
        WHERE user_id = freelancer_user_id;
    END IF;
    
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_freelancer_stats_on_review_trigger
    AFTER INSERT ON reviews
    FOR EACH ROW EXECUTE FUNCTION update_freelancer_stats_on_review();

-- Función para actualizar estadísticas de contratante cuando se crea una reseña
CREATE OR REPLACE FUNCTION update_contractor_stats_on_review()
RETURNS TRIGGER AS $$
DECLARE
    contractor_user_id UUID;
    avg_rating DECIMAL(3,2);
    review_count INTEGER;
BEGIN
    -- Obtener el user_id del contratante desde el contrato
    SELECT cp.user_id INTO contractor_user_id
    FROM contracts c
    JOIN contractor_profiles cp ON c.contractor_id = cp.id
    WHERE c.id = NEW.contract_id;
    
    -- Solo actualizar si el reviewee es el contratante
    IF NEW.reviewee_id = contractor_user_id THEN
        -- Calcular nueva calificación promedio y total de reseñas
        SELECT AVG(rating), COUNT(*)
        INTO avg_rating, review_count
        FROM reviews r
        JOIN contracts c ON r.contract_id = c.id
        JOIN contractor_profiles cp ON c.contractor_id = cp.id
        WHERE cp.user_id = contractor_user_id;
        
        -- Actualizar perfil del contratante
        UPDATE contractor_profiles
        SET rating = COALESCE(avg_rating, 0.00),
            total_reviews = review_count
        WHERE user_id = contractor_user_id;
    END IF;
    
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_contractor_stats_on_review_trigger
    AFTER INSERT ON reviews
    FOR EACH ROW EXECUTE FUNCTION update_contractor_stats_on_review();

-- Función para actualizar contador de aplicaciones en ofertas de trabajo
CREATE OR REPLACE FUNCTION update_job_applications_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE job_offers
        SET applications_count = applications_count + 1
        WHERE id = NEW.job_offer_id;
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        UPDATE job_offers
        SET applications_count = applications_count - 1
        WHERE id = OLD.job_offer_id;
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_job_applications_count_trigger
    AFTER INSERT OR DELETE ON job_applications
    FOR EACH ROW EXECUTE FUNCTION update_job_applications_count();

-- Función para actualizar contador de órdenes en servicios
CREATE OR REPLACE FUNCTION update_service_orders_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' AND NEW.service_id IS NOT NULL THEN
        UPDATE services
        SET orders_count = orders_count + 1
        WHERE id = NEW.service_id;
        RETURN NEW;
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_service_orders_count_trigger
    AFTER INSERT ON contracts
    FOR EACH ROW EXECUTE FUNCTION update_service_orders_count();

-- Función para actualizar last_message_at en conversaciones
CREATE OR REPLACE FUNCTION update_conversation_last_message()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE conversations
    SET last_message_at = NEW.sent_at
    WHERE id = NEW.conversation_id;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_conversation_last_message_trigger
    AFTER INSERT ON messages
    FOR EACH ROW EXECUTE FUNCTION update_conversation_last_message();
