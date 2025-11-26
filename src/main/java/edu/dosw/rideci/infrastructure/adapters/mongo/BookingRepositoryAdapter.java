package edu.dosw.rideci.infrastructure.adapters.mongo;

import edu.dosw.rideci.domain.model.Booking;
import edu.dosw.rideci.domain.model.enums.BookingStatus;
import edu.dosw.rideci.domain.ports.out.BookingRepositoryPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingRepositoryAdapter implements BookingRepositoryPort {
    
    private final MongoTemplate mongoTemplate;
    private static final String COLLECTION_NAME = "bookings";
    
    public BookingRepositoryAdapter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public Optional<Booking> findById(String id) {
        Booking booking = mongoTemplate.findById(id, Booking.class, COLLECTION_NAME);
        return Optional.ofNullable(booking);
    }
    
    @Override
    public List<Booking> findByPassengerId(String passengerId) {
        Query query = new Query(Criteria.where("passengerId").is(passengerId));
        return mongoTemplate.find(query, Booking.class, COLLECTION_NAME);
    }
    
    @Override
    public List<Booking> findByTripId(String tripId) {
        Query query = new Query(Criteria.where("tripId").is(tripId));
        return mongoTemplate.find(query, Booking.class, COLLECTION_NAME);
    }
    
    @Override
    public List<Booking> findByTripIdAndStatus(String tripId, BookingStatus status) {
        Query query = new Query(Criteria.where("tripId").is(tripId)
                                      .and("status").is(status));
        return mongoTemplate.find(query, Booking.class, COLLECTION_NAME);
    }
    
    @Override
    public List<Booking> findByPassengerIdAndStatus(String passengerId, BookingStatus status) {
        Query query = new Query(Criteria.where("passengerId").is(passengerId)
                                      .and("status").is(status));
        return mongoTemplate.find(query, Booking.class, COLLECTION_NAME);
    }
    
    @Override
    public Booking save(Booking booking) {
        return mongoTemplate.save(booking, COLLECTION_NAME);
    }
    
    @Override
    public void deleteById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Booking.class, COLLECTION_NAME);
    }
    
    @Override
    public boolean existsById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.exists(query, Booking.class, COLLECTION_NAME);
    }
    
    @Override
    public int countActiveBookingsByTrip(String tripId) {
        Query query = new Query(Criteria.where("tripId").is(tripId)
                                      .and("status").in(BookingStatus.PENDING, BookingStatus.CONFIRMED));
        return (int) mongoTemplate.count(query, Booking.class, COLLECTION_NAME);
    }
}