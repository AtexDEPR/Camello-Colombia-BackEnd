# Camello Colombia - Backend API

## 📋 Descripción

Backend completo para la plataforma freelance Camello Colombia, desarrollado con Spring Boot 3.x. Proporciona una API REST completa para conectar freelancers colombianos con contratantes locales.

## 🚀 Características Principales

- ✅ **Autenticación JWT** completa con refresh tokens
- ✅ **Gestión de usuarios** (Freelancers, Contratantes, Administradores)
- ✅ **Perfiles profesionales** con portfolios y habilidades
- ✅ **Sistema de servicios** tipo "gig" para freelancers
- ✅ **Ofertas de trabajo** para contratantes
- ✅ **Sistema de aplicaciones** a trabajos
- ✅ **Gestión de contratos** con estados y pagos
- ✅ **Sistema de mensajería** entre usuarios
- ✅ **Sistema de calificaciones** y reseñas
- ✅ **Notificaciones** en tiempo real
- ✅ **Panel administrativo** completo
- ✅ **Búsqueda avanzada** con filtros
- ✅ **Documentación Swagger** automática

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.2.0**
- **Spring Security** + JWT
- **Spring Data JPA** + Hibernate
- **PostgreSQL** como base de datos
- **Swagger/OpenAPI 3** para documentación
- **MapStruct** para mapeo de DTOs
- **Lombok** para reducir boilerplate
- **Maven** como gestor de dependencias

## 📁 Estructura del Proyecto

```
src/main/java/com/Camello/Camello/
├── config/           # Configuraciones (CORS, Security)
├── controller/       # Controladores REST
├── dto/             # Data Transfer Objects
├── entity/          # Entidades JPA
├── exception/       # Manejo de excepciones
├── repository/      # Repositorios JPA
├── security/        # Configuración de seguridad y JWT
└── service/         # Lógica de negocio
```

## 🔧 Configuración

### Variables de Entorno

```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/camello_db
spring.datasource.username=camello_user
spring.datasource.password=camello_password

# JWT
app.jwt.secret=camelloSecretKeyForJWTTokenGenerationAndValidation2024
app.jwt.expiration=86400000
app.jwt.refresh-expiration=604800000

# CORS
app.cors.allowed-origins=http://localhost:3000,http://localhost:5173
```

### Base de Datos

1. Crear base de datos PostgreSQL:
```sql
CREATE DATABASE camello_db;
CREATE USER camello_user WITH PASSWORD 'camello_password';
GRANT ALL PRIVILEGES ON DATABASE camello_db TO camello_user;
```

2. Ejecutar scripts de inicialización en orden:
```bash
scripts/01_create_database.sql
scripts/02_create_indexes.sql
scripts/03_create_triggers.sql
scripts/04_seed_categories.sql
scripts/05_seed_admin_user.sql
```

## 🚀 Instalación y Ejecución

### Prerrequisitos
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Pasos

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd Camello-Colombia-BackEnd/Camello
```

2. **Configurar base de datos**
```bash
# Crear base de datos y usuario según la configuración anterior
```

3. **Instalar dependencias**
```bash
mvn clean install
```

4. **Ejecutar aplicación**
```bash
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación API

### Swagger UI
Una vez ejecutada la aplicación, accede a:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Endpoints Principales

#### Autenticación
- `POST /api/auth/register` - Registro de usuarios
- `POST /api/auth/login` - Inicio de sesión
- `POST /api/auth/refresh` - Renovar token
- `POST /api/auth/logout` - Cerrar sesión

#### Freelancers
- `GET /api/freelancers/profile` - Obtener perfil propio
- `PUT /api/freelancers/profile` - Actualizar perfil
- `POST /api/freelancers/services` - Crear servicio
- `GET /api/freelancers/services` - Listar servicios propios

#### Contratantes
- `GET /api/contractors/profile` - Obtener perfil propio
- `PUT /api/contractors/profile` - Actualizar perfil
- `POST /api/contractors/job-offers` - Crear oferta de trabajo
- `GET /api/contractors/job-offers` - Listar ofertas propias

#### Servicios Públicos
- `GET /api/services` - Listar todos los servicios
- `GET /api/services/{id}` - Obtener servicio específico
- `GET /api/services/search` - Buscar servicios con filtros

#### Ofertas de Trabajo
- `GET /api/job-offers` - Listar ofertas activas
- `GET /api/job-offers/{id}` - Obtener oferta específica
- `GET /api/job-offers/search` - Buscar ofertas con filtros

#### Mensajería
- `POST /api/messages` - Enviar mensaje
- `GET /api/messages/conversations` - Listar conversaciones
- `GET /api/messages/conversations/{id}` - Obtener mensajes

#### Administración
- `GET /api/admin/users` - Listar usuarios (Admin)
- `GET /api/admin/stats` - Estadísticas generales (Admin)
- `PUT /api/admin/users/{id}/activate` - Activar usuario (Admin)

## 🔐 Autenticación

### Registro
```json
POST /api/auth/register
{
  "email": "usuario@ejemplo.com",
  "password": "password123",
  "confirmPassword": "password123",
  "role": "FREELANCER" // o "CONTRACTOR"
}
```

### Login
```json
POST /api/auth/login
{
  "email": "usuario@ejemplo.com",
  "password": "password123"
}
```

### Respuesta de Autenticación
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": "uuid-del-usuario",
  "email": "usuario@ejemplo.com",
  "role": "FREELANCER",
  "success": true,
  "message": "Autenticación exitosa"
}
```

### Uso del Token
Incluir en el header de todas las peticiones autenticadas:
```
Authorization: Bearer <token>
```

## 🏗️ Arquitectura

### Capas de la Aplicación

1. **Controller Layer**: Maneja las peticiones HTTP y respuestas
2. **Service Layer**: Contiene la lógica de negocio
3. **Repository Layer**: Acceso a datos con Spring Data JPA
4. **Entity Layer**: Modelos de datos JPA
5. **DTO Layer**: Objetos de transferencia de datos
6. **Security Layer**: Autenticación y autorización

### Patrones Implementados

- **Repository Pattern**: Para acceso a datos
- **DTO Pattern**: Para transferencia de datos
- **Service Layer Pattern**: Para lógica de negocio
- **Exception Handling**: Manejo global de excepciones
- **Dependency Injection**: Con Spring Framework

## 🧪 Testing

### Ejecutar Tests
```bash
mvn test
```

### Cobertura de Tests
```bash
mvn jacoco:report
```

## 🚀 Despliegue

### Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/camello-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Variables de Producción
```properties
spring.profiles.active=prod
spring.datasource.url=${DATABASE_URL}
app.jwt.secret=${JWT_SECRET}
app.cors.allowed-origins=${FRONTEND_URL}
```

## 📈 Próximas Funcionalidades

- [ ] Integración con Wompi/PayU para pagos
- [ ] WebSocket para chat en tiempo real
- [ ] Sistema de archivos con AWS S3
- [ ] Notificaciones push
- [ ] Sistema de facturación
- [ ] Analytics y métricas
- [ ] API de geolocalización
- [ ] Sistema de verificación KYC

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para detalles.

## 📞 Contacto

- **Proyecto**: Camello Colombia
- **Repositorio**: [GitHub](https://github.com/tu-usuario/camello-colombia-backend)
- **Documentación**: [Swagger UI](http://localhost:8080/swagger-ui.html)

---

**¡Camello Colombia - Conectando talento local! 🐫🇨🇴**