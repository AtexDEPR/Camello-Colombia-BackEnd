-- Script para corregir la longitud del campo password_hash y actualizar los datos

-- Primero, modificar la columna para que tenga la longitud correcta
ALTER TABLE users ALTER COLUMN password_hash TYPE VARCHAR(60);

-- Actualizar todos los usuarios con el hash correcto de BCrypt para "password123"
-- Hash: $2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6

UPDATE users SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMye7VFnjZcmcOjjhyW0OK68QH.H1S1o8K6' 
WHERE password_hash LIKE '$2a$10$N9qo8uLOickgx%';

-- Verificar los cambios
SELECT email, LENGTH(password_hash) as hash_length, password_hash 
FROM users 
LIMIT 5;