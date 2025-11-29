package edu.dosw.rideci.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoInitializer {

    private static final Logger logger = LoggerFactory.getLogger(MongoInitializer.class);

    @Bean
    public CommandLineRunner initDatabase(MongoTemplate mongoTemplate) {
        return args -> {
            try {
                // Crear colecciones si no existen
                if (!mongoTemplate.collectionExists("travels")) {
                    mongoTemplate.createCollection("travels");
                    logger.info("✅ Colección 'travels' creada exitosamente");
                } else {
                    logger.info("ℹ️  Colección 'travels' ya existe");
                }

                if (!mongoTemplate.collectionExists("bookings")) {
                    mongoTemplate.createCollection("bookings");
                    logger.info("✅ Colección 'bookings' creada exitosamente");
                } else {
                    logger.info("ℹ️  Colección 'bookings' ya existe");
                }

                if (!mongoTemplate.collectionExists("trips")) {
                    mongoTemplate.createCollection("trips");
                    logger.info("✅ Colección 'trips' creada exitosamente");
                } else {
                    logger.info("ℹ️  Colección 'trips' ya existe");
                }

                logger.info("🎉 Inicialización de MongoDB completada");

            } catch (Exception e) {
                logger.error("❌ Error inicializando colecciones de MongoDB: {}", e.getMessage(), e);
            }
        };
    }
}
