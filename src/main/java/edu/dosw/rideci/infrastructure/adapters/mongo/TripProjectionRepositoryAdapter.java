package edu.dosw.rideci.infrastructure.adapters.mongo;

import edu.dosw.rideci.domain.model.Trip;
import edu.dosw.rideci.domain.model.enums.TravelType;
import edu.dosw.rideci.domain.ports.out.TripProjectionRepositoryPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TripProjectionRepositoryAdapter implements TripProjectionRepositoryPort {
    
    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION_NAME = "trips";
    
    public TripProjectionRepositoryAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public Optional<Trip> findById(String id) {
        Trip trip = mongoTemplate.findById(id, Trip.class, COLLECTION_NAME);
        return Optional.ofNullable(trip);
    }
    
    @Override
    public List<Trip> findByFilters(Optional<String> origen,
                                   Optional<String> destino,
                                   Optional<LocalDateTime> fechaDesde,
                                   Optional<LocalDateTime> fechaHasta,
                                   Optional<TravelType> travelType,
                                   Optional<Integer> cuposMinimos) {
        
        Query query = new Query();
        
        // Filtrar por activos
        query.addCriteria(Criteria.where("activo").is(true));
        
        // Aplicar filtros opcionales
        origen.ifPresent(o -> query.addCriteria(Criteria.where("origen").regex(o, "i")));
        destino.ifPresent(d -> query.addCriteria(Criteria.where("destino").regex(d, "i")));
        
        if (fechaDesde.isPresent() && fechaHasta.isPresent()) {
            query.addCriteria(Criteria.where("fechaHoraSalida")
                    .gte(fechaDesde.get())
                    .lte(fechaHasta.get()));
        } else if (fechaDesde.isPresent()) {
            query.addCriteria(Criteria.where("fechaHoraSalida").gte(fechaDesde.get()));
        }
        
        travelType.ifPresent(tt -> query.addCriteria(Criteria.where("travelType").is(tt)));
        cuposMinimos.ifPresent(cm -> query.addCriteria(Criteria.where("cuposDisponibles").gte(cm)));
        
        return mongoTemplate.find(query, Trip.class, COLLECTION_NAME);
    }
    
    @Override
    public List<Trip> findByFiltersWithPagination(Optional<String> origen,
                                                 Optional<String> destino,
                                                 Optional<LocalDateTime> fechaDesde,
                                                 Optional<LocalDateTime> fechaHasta,
                                                 Optional<TravelType> travelType,
                                                 Optional<Integer> cuposMinimos,
                                                 int page,
                                                 int size) {
        
        Query query = new Query();
        
        // Filtrar por activos
        query.addCriteria(Criteria.where("activo").is(true));
        
        // Aplicar filtros opcionales (mismo lógica que findByFilters)
        origen.ifPresent(o -> query.addCriteria(Criteria.where("origen").regex(o, "i")));
        destino.ifPresent(d -> query.addCriteria(Criteria.where("destino").regex(d, "i")));
        
        if (fechaDesde.isPresent() && fechaHasta.isPresent()) {
            query.addCriteria(Criteria.where("fechaHoraSalida")
                    .gte(fechaDesde.get())
                    .lte(fechaHasta.get()));
        } else if (fechaDesde.isPresent()) {
            query.addCriteria(Criteria.where("fechaHoraSalida").gte(fechaDesde.get()));
        }
        
        travelType.ifPresent(tt -> query.addCriteria(Criteria.where("travelType").is(tt)));
        cuposMinimos.ifPresent(cm -> query.addCriteria(Criteria.where("cuposDisponibles").gte(cm)));
        
        // Aplicar paginación
        Pageable pageable = PageRequest.of(page, size);
        query.with(pageable);
        
        return mongoTemplate.find(query, Trip.class, COLLECTION_NAME);
    }
    
    @Override
    public List<Trip> findNearbyTrips(double latitud, double longitud, double radioKm) {
        Query query = new Query();
        
        // Filtrar por activos
        query.addCriteria(Criteria.where("activo").is(true));
        
        // Búsqueda geoespacial cercana al origen
        GeoJsonPoint point = new GeoJsonPoint(longitud, latitud);
        NearQuery nearQuery = NearQuery.near(point).maxDistance(radioKm * 1000); // convertir km a metros
        
        // Para simplicidad, buscar por coordenadas de origen
        query.addCriteria(Criteria.where("location").near(point).maxDistance(radioKm * 1000));
        
        return mongoTemplate.find(query, Trip.class, COLLECTION_NAME);
    }
    
    @Override
    public List<Trip> findNearbyTripsWithFilters(double latitud, double longitud, 
                                                double radioKm, Integer cuposMinimos, 
                                                String travelType) {
        Query query = new Query();
        
        // Filtrar por activos
        query.addCriteria(Criteria.where("activo").is(true));
        
        // Búsqueda geoespacial
        GeoJsonPoint point = new GeoJsonPoint(longitud, latitud);
        query.addCriteria(Criteria.where("location").near(point).maxDistance(radioKm * 1000));
        
        // Filtros adicionales
        if (cuposMinimos != null) {
            query.addCriteria(Criteria.where("cuposDisponibles").gte(cuposMinimos));
        }
        
        if (travelType != null) {
            query.addCriteria(Criteria.where("travelType").is(TravelType.valueOf(travelType)));
        }
        
        return mongoTemplate.find(query, Trip.class, COLLECTION_NAME);
    }
    
    @Override
    public Trip save(Trip trip) {
        return mongoTemplate.save(trip, COLLECTION_NAME);
    }
    
    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Trip.class, COLLECTION_NAME);
    }
    
    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, Trip.class, COLLECTION_NAME);
    }
}