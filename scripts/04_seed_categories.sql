-- Datos iniciales para categorías de servicios en Camello

INSERT INTO categories (id, name, description, icon, is_active) VALUES
-- Diseño y Creatividad
(uuid_generate_v4(), 'Diseño Gráfico', 'Logos, branding, diseño de materiales publicitarios', 'palette', true),
(uuid_generate_v4(), 'Diseño Web', 'Diseño de interfaces, UX/UI, mockups', 'monitor', true),
(uuid_generate_v4(), 'Ilustración', 'Ilustraciones digitales, arte conceptual, caricaturas', 'image', true),
(uuid_generate_v4(), 'Fotografía', 'Fotografía de productos, eventos, retratos', 'camera', true),
(uuid_generate_v4(), 'Video y Animación', 'Edición de video, motion graphics, animaciones', 'video', true),

-- Desarrollo y Tecnología
(uuid_generate_v4(), 'Desarrollo Web', 'Sitios web, aplicaciones web, e-commerce', 'code', true),
(uuid_generate_v4(), 'Desarrollo Móvil', 'Apps para iOS y Android', 'smartphone', true),
(uuid_generate_v4(), 'Desarrollo de Software', 'Aplicaciones de escritorio, sistemas personalizados', 'cpu', true),
(uuid_generate_v4(), 'Base de Datos', 'Diseño y administración de bases de datos', 'database', true),
(uuid_generate_v4(), 'DevOps y Cloud', 'Configuración de servidores, despliegue, AWS, Azure', 'cloud', true),

-- Marketing Digital
(uuid_generate_v4(), 'Redes Sociales', 'Gestión de redes sociales, contenido, community management', 'share-2', true),
(uuid_generate_v4(), 'SEO y SEM', 'Posicionamiento web, Google Ads, marketing de buscadores', 'search', true),
(uuid_generate_v4(), 'Email Marketing', 'Campañas de email, newsletters, automatización', 'mail', true),
(uuid_generate_v4(), 'Publicidad Digital', 'Facebook Ads, Instagram Ads, campañas publicitarias', 'megaphone', true),
(uuid_generate_v4(), 'Análisis de Datos', 'Google Analytics, reportes, métricas', 'bar-chart', true),

-- Contenido y Redacción
(uuid_generate_v4(), 'Redacción de Contenido', 'Artículos, blogs, contenido web', 'edit', true),
(uuid_generate_v4(), 'Copywriting', 'Textos publicitarios, ventas, marketing', 'pen-tool', true),
(uuid_generate_v4(), 'Traducción', 'Traducción de documentos, localización', 'globe', true),
(uuid_generate_v4(), 'Corrección de Textos', 'Revisión ortográfica, edición, proofreading', 'check-circle', true),

-- Consultoría y Negocios
(uuid_generate_v4(), 'Consultoría Empresarial', 'Estrategia de negocio, planes de negocio', 'briefcase', true),
(uuid_generate_v4(), 'Finanzas y Contabilidad', 'Contabilidad, análisis financiero, impuestos', 'calculator', true),
(uuid_generate_v4(), 'Recursos Humanos', 'Reclutamiento, capacitación, gestión de personal', 'users', true),
(uuid_generate_v4(), 'Legal', 'Asesoría legal, contratos, trámites', 'file-text', true),

-- Audio y Música
(uuid_generate_v4(), 'Producción Musical', 'Composición, arreglos, producción de audio', 'music', true),
(uuid_generate_v4(), 'Locución', 'Voice over, narraciones, doblaje', 'mic', true),
(uuid_generate_v4(), 'Edición de Audio', 'Edición, masterización, efectos de sonido', 'headphones', true),

-- Educación y Capacitación
(uuid_generate_v4(), 'Tutorías', 'Clases particulares, apoyo académico', 'book-open', true),
(uuid_generate_v4(), 'Creación de Cursos', 'Desarrollo de contenido educativo, e-learning', 'graduation-cap', true),
(uuid_generate_v4(), 'Capacitación Empresarial', 'Talleres, seminarios, entrenamiento corporativo', 'presentation', true),

-- Otros Servicios
(uuid_generate_v4(), 'Asistencia Virtual', 'Administración, gestión de tareas, soporte', 'user-check', true),
(uuid_generate_v4(), 'Investigación', 'Investigación de mercado, análisis de competencia', 'search', true),
(uuid_generate_v4(), 'Transcripción', 'Transcripción de audio a texto', 'type', true);
