package edu.dosw.rideci.unit.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    @DisplayName("Should validate application main class exists")
    void shouldValidateApplicationMainClassExists() {
        // Given & When
        String className = "edu.dosw.rideci.PoseidonSearchAndBookingApplication";
        
        // Then
        assertDoesNotThrow(() -> {
            Class<?> applicationClass = Class.forName(className);
            assertNotNull(applicationClass);
            assertTrue(applicationClass.getName().contains("PoseidonSearchAndBookingApplication"));
        });
    }

    @Test
    @DisplayName("Should validate package structure")
    void shouldValidatePackageStructure() {
        // Test that core packages exist by checking if classes can be loaded
        assertDoesNotThrow(() -> {
            // Domain classes
            Class.forName("edu.dosw.rideci.domain.model.Booking");
            Class.forName("edu.dosw.rideci.domain.model.Trip");
            
            // Use case classes
            Class.forName("edu.dosw.rideci.application.usecase.CreateBookingUseCase");
            Class.forName("edu.dosw.rideci.application.usecase.CancelBookingUseCase");
            
            // Infrastructure classes
            Class.forName("edu.dosw.rideci.infrastructure.controllers.BookingController");
            Class.forName("edu.dosw.rideci.infrastructure.controllers.TripQueryController");
        });
    }

    @Test
    @DisplayName("Should validate enum classes")
    void shouldValidateEnumClasses() {
        assertDoesNotThrow(() -> {
            // Validate BookingStatus enum
            Class<?> bookingStatusClass = Class.forName("edu.dosw.rideci.domain.model.enums.BookingStatus");
            assertTrue(bookingStatusClass.isEnum());
            
            // Validate TravelType enum
            Class<?> travelTypeClass = Class.forName("edu.dosw.rideci.domain.model.enums.TravelType");
            assertTrue(travelTypeClass.isEnum());
        });
    }
}