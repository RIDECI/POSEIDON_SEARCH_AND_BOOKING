package edu.dosw.rideci.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;

public interface BookingRepository extends MongoRepository<BookingDocument, String> {

    List<BookingDocument> findByPassengerId(Long passengerId);

    List<BookingDocument> findByTravelId(String travelId);

}
