package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.infrastructure.persistence.entity.TravelDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TravelRepository extends MongoRepository<TravelDocument, String> {

    Optional<TravelDocument> findByRideId(String rideId);

    List<TravelDocument> findByDriverId(Long driverId);

    @Query("{ 'departure_date_time': { $gte: ?0, $lte: ?1 } }")
    List<TravelDocument> findByDepartureDateAndTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("{ 'available_slots': { $gt: 0 } }")
    List<TravelDocument> findAvailableRides();

    @Query("{ 'origin_lat': { $gte: ?0, $lte: ?1 }, 'origin_lng': { $gte: ?2, $lte: ?3 } }")
    List<TravelDocument> findByOriginLocationBounds(Double minLat, Double maxLat, Double minLng, Double maxLng);

    boolean existsByRideId(String rideId);
}