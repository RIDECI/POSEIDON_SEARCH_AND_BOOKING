package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<BookingDocument, String> {

    List<BookingDocument> findByRideId(String rideId);

    List<BookingDocument> findByPassengerId(String passengerId);

    List<BookingDocument> findByDriverId(String driverId);

    Optional<BookingDocument> findByTransactionId(String transactionId);

    boolean existsByRideIdAndPassengerId(String rideId, String passengerId);

    Long countByRideId(String rideId);
}