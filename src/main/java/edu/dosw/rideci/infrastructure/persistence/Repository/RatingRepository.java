package edu.dosw.rideci.infrastructure.persistence.Repository;

import edu.dosw.rideci.infrastructure.persistence.entity.RatingDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<RatingDocument, String> {

    List<RatingDocument> findByRideId(String rideId);

    List<RatingDocument> findByDriverId(String driverId);

    List<RatingDocument> findByRaterId(String raterId);

    List<RatingDocument> findByBookingId(String bookingId);

    boolean existsByBookingIdAndRaterId(String bookingId, String raterId);

    Long countByDriverId(String driverId);

    @Aggregation(pipeline = {
            "{ '$match': { 'driverId': ?0 } }",
            "{ '$group': { '_id': null, 'averageScore': { '$avg': '$score' } } }"
    })
    Double getAverageScoreByDriverId(String driverId);
}