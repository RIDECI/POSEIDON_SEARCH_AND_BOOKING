package edu.dosw.rideci.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    
    @Value("${spring.data.mongodb.database:poseidon_search_booking}")
    private String databaseName;
    
    @Override
    protected String getDatabaseName() {
        return databaseName;
    }
    
    // Aquí podrías agregar configuraciones adicionales de MongoDB como:
    // - Configuración de índices
    // - Converters personalizados
    // - Configuración de conexión
}