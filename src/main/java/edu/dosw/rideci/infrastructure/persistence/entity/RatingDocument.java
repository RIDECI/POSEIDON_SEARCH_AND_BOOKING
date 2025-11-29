package edu.dosw.rideci.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@Document(collection = "ratings")
public class RatingDocument {

    @Id
    private String id;

    @Indexed
    private String bookingId;

    @Indexed
    private String raterId;          // Quién califica

    @Indexed
    private String driverId;

    @Indexed
    private String rideId;

    private Integer score;
    private String comment;
    private LocalDateTime createdAt;

}