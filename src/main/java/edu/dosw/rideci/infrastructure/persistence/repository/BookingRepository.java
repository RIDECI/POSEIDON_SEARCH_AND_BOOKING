package edu.dosw.rideci.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.dosw.rideci.infrastructure.persistence.entity.BookingDocument;

public interface BookingRepository extends MongoRepository<BookingDocument, String> {
}
