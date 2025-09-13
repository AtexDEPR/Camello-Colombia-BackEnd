-- Políticas de seguridad y permisos para Camello

-- Crear roles de base de datos
CREATE ROLE camello_app_user;
CREATE ROLE camello_admin_user;

-- Permisos para el usuario de la aplicación
GRANT CONNECT ON DATABASE postgres TO camello_app_user;
GRANT USAGE ON SCHEMA public TO camello_app_user;

-- Permisos de lectura y escritura en tablas principales
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO camello_app_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO camello_app_user;

-- Permisos para ejecutar funciones
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO camello_app_user;

-- Permisos para el administrador
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO camello_admin_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO camello_admin_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO camello_admin_user;

-- Habilitar Row Level Security en tablas sensibles
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE freelancer_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE contractor_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE messages ENABLE ROW LEVEL SECURITY;
ALTER TABLE notifications ENABLE ROW LEVEL SECURITY;

-- Política para que los usuarios solo puedan ver/editar su propia información
CREATE POLICY user_own_data ON users
    FOR ALL
    TO camello_app_user
    USING (id = current_setting('app.current_user_id')::UUID);

-- Política para perfiles de freelancer
CREATE POLICY freelancer_profile_policy ON freelancer_profiles
    FOR ALL
    TO camello_app_user
    USING (user_id = current_setting('app.current_user_id')::UUID);

-- Política para perfiles de contratante
CREATE POLICY contractor_profile_policy ON contractor_profiles
    FOR ALL
    TO camello_app_user
    USING (user_id = current_setting('app.current_user_id')::UUID);

-- Política para mensajes (solo participantes de la conversación)
CREATE POLICY message_participants_policy ON messages
    FOR ALL
    TO camello_app_user
    USING (
        conversation_id IN (
            SELECT id FROM conversations 
            WHERE participant1_id = current_setting('app.current_user_id')::UUID
            OR participant2_id = current_setting('app.current_user_id')::UUID
        )
    );

-- Política para notificaciones (solo del usuario)
CREATE POLICY notification_owner_policy ON notifications
    FOR ALL
    TO camello_app_user
    USING (user_id = current_setting('app.current_user_id')::UUID);

-- Los administradores pueden ver todo
CREATE POLICY admin_all_access ON users
    FOR ALL
    TO camello_admin_user
    USING (true);

-- Crear función para establecer el usuario actual en la sesión
CREATE OR REPLACE FUNCTION set_current_user(user_id UUID)
RETURNS void AS $$
BEGIN
    PERFORM set_config('app.current_user_id', user_id::text, true);
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;
