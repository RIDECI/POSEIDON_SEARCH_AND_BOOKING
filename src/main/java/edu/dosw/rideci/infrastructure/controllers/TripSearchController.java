package edu.dosw.rideci.infrastructure.controllers;

import edu.dosw.rideci.infrastructure.controllers.dto.Response.TripDetailResponse;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.TripSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/trips")
@CrossOrigin(origins = "*")
public class TripSearchController {

    @GetMapping("/{id}")
    public ResponseEntity<TripDetailResponse> getTripById(@PathVariable String id) {
        // Datos detallados hardcodeados por ID
        Map<String, TripDetailResponse> detailedTrips = getDetailedTrips();
        
        TripDetailResponse trip = detailedTrips.get(id);
        if (trip != null) {
            return ResponseEntity.ok(trip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TripSearchResponse>> searchTrips(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String departureTime,
            @RequestParam(defaultValue = "false") Boolean nearbySearch) {
        
        // Convertir hora de 12h a 24h si viene en formato "06:30 p. m."
        final String normalizedDepartureTime;
        if (departureTime != null && !departureTime.trim().isEmpty()) {
            normalizedDepartureTime = convertTo24Hour(departureTime);
        } else {
            normalizedDepartureTime = departureTime;
        }
        
        // Obtener todos los viajes
        List<TripSearchResponse> allTrips = getAllTrips();
        
        // Filtrar por destino y hora de salida
        List<TripSearchResponse> filteredTrips = allTrips.stream()
                .filter(trip -> {
                    boolean matchesDestination = destination == null || 
                            destination.trim().isEmpty() || 
                            trip.getRoute().toLowerCase().contains(destination.toLowerCase());
                    
                    boolean matchesDepartureTime = normalizedDepartureTime == null || 
                            normalizedDepartureTime.trim().isEmpty() || 
                            trip.getDepartureTime().equals(normalizedDepartureTime);
                    
                    return matchesDestination && matchesDepartureTime;
                })
                .toList();
        
        return ResponseEntity.ok(filteredTrips);
    }
    
    private List<TripSearchResponse> getAllTrips() {
        return Arrays.asList(
            TripSearchResponse.builder()
                .id("1")
                .driverName("Carlos Santana")
                .vehicleType("Minivan")
                .rating("4.9")
                .route("Universidad @ Portal 80")
                .departureTime("18:30")
                .price(6000.0)
                .availableSeats(4)
                .build(),
            TripSearchResponse.builder()
                .id("2")
                .driverName("Raquel Selma")
                .vehicleType("Minivan")
                .rating("4.8")
                .route("Universidad @ Portal 80")
                .departureTime("18:30")
                .price(8000.0)
                .availableSeats(6)
                .build(),
            TripSearchResponse.builder()
                .id("3")
                .driverName("Sebastian Garcia")
                .vehicleType("Sedan")
                .rating("4.9")
                .route("Universidad @ Portal 80")
                .departureTime("18:30")
                .price(5000.0)
                .availableSeats(1)
                .build(),
            TripSearchResponse.builder()
                .id("4")
                .driverName("Alex Forero")
                .vehicleType("SUV")
                .rating("4.9")
                .route("Universidad @ Portal 80")
                .departureTime("18:30")
                .price(12000.0)
                .availableSeats(2)
                .build(),
            TripSearchResponse.builder()
                .id("5")
                .driverName("Carlos Santana")
                .vehicleType("Minivan")
                .rating("4.8")
                .route("Universidad @ Portal 80")
                .departureTime("18:30")
                .price(9000.0)
                .availableSeats(3)
                .build(),
            TripSearchResponse.builder()
                .id("6")
                .driverName("Carlos Santana")
                .vehicleType("Sedan")
                .rating("4.7")
                .route("Universidad @ Portal 80")
                .departureTime("18:30")
                .price(7000.0)
                .availableSeats(3)
                .build()
        );
    }
    
    private String convertTo24Hour(String time12h) {
        try {
            // Normalizar formato: "06:30 p. m." -> "06:30 PM"
            String normalized = time12h.trim()
                    .replace(" p. m.", " PM")
                    .replace(" a. m.", " AM")
                    .replace(" pm", " PM")
                    .replace(" am", " AM");
            
            String[] parts = normalized.split(" ");
            if (parts.length < 2) return time12h; // Ya está en formato 24h
            
            String[] timeParts = parts[0].split(":");
            int hour = Integer.parseInt(timeParts[0]);
            String minutes = timeParts[1];
            String period = parts[1].toUpperCase();
            
            if (period.equals("PM") && hour != 12) {
                hour += 12;
            } else if (period.equals("AM") && hour == 12) {
                hour = 0;
            }
            
            return String.format("%02d:%s", hour, minutes);
        } catch (Exception e) {
            return time12h; // Si falla, devuelve el original
        }
    }
    
    private Map<String, TripDetailResponse> getDetailedTrips() {
        Map<String, TripDetailResponse> trips = new HashMap<>();
        
        trips.put("1", TripDetailResponse.builder()
                .id("1")
                .driver(TripDetailResponse.DriverInfo.builder()
                        .id("driver001")
                        .name("Carlos Santana")
                        .rating("4.9")
                        .totalTrips(150)
                        .build())
                .vehicle(TripDetailResponse.VehicleInfo.builder()
                        .brand("Toyota")
                        .model("Hiace")
                        .color("Blanco")
                        .plate("ABC 123")
                        .type("Minivan")
                        .build())
                .trip(TripDetailResponse.TripInfo.builder()
                        .origin("Universidad Escuela Colombiana de Ingeniería")
                        .destination("Portal 80")
                        .date("18 de Noviembre, 2025")
                        .departureTime("18:30")
                        .arrivalTime("20:00")
                        .availableSeats(4)
                        .build())
                .pricing(TripDetailResponse.PricingInfo.builder()
                        .total(6000.0)
                        .currency("COP")
                        .build())
                .route("Universidad @ Portal 80")
                .build());
        
        trips.put("2", TripDetailResponse.builder()
                .id("2")
                .driver(TripDetailResponse.DriverInfo.builder()
                        .id("driver002")
                        .name("Raquel Selma")
                        .rating("4.8")
                        .totalTrips(120)
                        .build())
                .vehicle(TripDetailResponse.VehicleInfo.builder()
                        .brand("Toyota")
                        .model("Camry")
                        .color("Gris Plata")
                        .plate("1234 ABC")
                        .type("Minivan")
                        .build())
                .trip(TripDetailResponse.TripInfo.builder()
                        .origin("Universidad Escuela Colombiana de Ingeniería")
                        .destination("Portal 80")
                        .date("18 de Noviembre, 2025")
                        .departureTime("18:30")
                        .arrivalTime("20:00")
                        .availableSeats(6)
                        .build())
                .pricing(TripDetailResponse.PricingInfo.builder()
                        .total(8000.0)
                        .currency("COP")
                        .build())
                .route("Universidad @ Portal 80")
                .build());
        
        trips.put("3", TripDetailResponse.builder()
                .id("3")
                .driver(TripDetailResponse.DriverInfo.builder()
                        .id("driver003")
                        .name("Sebastian Garcia")
                        .rating("4.9")
                        .totalTrips(200)
                        .build())
                .vehicle(TripDetailResponse.VehicleInfo.builder()
                        .brand("Chevrolet")
                        .model("Spark")
                        .color("Rojo")
                        .plate("DEF 456")
                        .type("Sedan")
                        .build())
                .trip(TripDetailResponse.TripInfo.builder()
                        .origin("Universidad Escuela Colombiana de Ingeniería")
                        .destination("Portal 80")
                        .date("18 de Noviembre, 2025")
                        .departureTime("18:30")
                        .arrivalTime("20:00")
                        .availableSeats(1)
                        .build())
                .pricing(TripDetailResponse.PricingInfo.builder()
                        .total(5000.0)
                        .currency("COP")
                        .build())
                .route("Universidad @ Portal 80")
                .build());
        
        trips.put("4", TripDetailResponse.builder()
                .id("4")
                .driver(TripDetailResponse.DriverInfo.builder()
                        .id("driver004")
                        .name("Alex Forero")
                        .rating("4.9")
                        .totalTrips(180)
                        .build())
                .vehicle(TripDetailResponse.VehicleInfo.builder()
                        .brand("Ford")
                        .model("Explorer")
                        .color("Negro")
                        .plate("GHI 789")
                        .type("SUV")
                        .build())
                .trip(TripDetailResponse.TripInfo.builder()
                        .origin("Universidad Escuela Colombiana de Ingeniería")
                        .destination("Portal 80")
                        .date("18 de Noviembre, 2025")
                        .departureTime("18:30")
                        .arrivalTime("20:00")
                        .availableSeats(2)
                        .build())
                .pricing(TripDetailResponse.PricingInfo.builder()
                        .total(12000.0)
                        .currency("COP")
                        .build())
                .route("Universidad @ Portal 80")
                .build());
        
        trips.put("5", TripDetailResponse.builder()
                .id("5")
                .driver(TripDetailResponse.DriverInfo.builder()
                        .id("driver001")
                        .name("Carlos Santana")
                        .rating("4.8")
                        .totalTrips(150)
                        .build())
                .vehicle(TripDetailResponse.VehicleInfo.builder()
                        .brand("Toyota")
                        .model("Sienna")
                        .color("Azul")
                        .plate("JKL 012")
                        .type("Minivan")
                        .build())
                .trip(TripDetailResponse.TripInfo.builder()
                        .origin("Universidad Escuela Colombiana de Ingeniería")
                        .destination("Portal 80")
                        .date("18 de Noviembre, 2025")
                        .departureTime("18:30")
                        .arrivalTime("20:00")
                        .availableSeats(3)
                        .build())
                .pricing(TripDetailResponse.PricingInfo.builder()
                        .total(9000.0)
                        .currency("COP")
                        .build())
                .route("Universidad @ Portal 80")
                .build());
        
        trips.put("6", TripDetailResponse.builder()
                .id("6")
                .driver(TripDetailResponse.DriverInfo.builder()
                        .id("driver001")
                        .name("Carlos Santana")
                        .rating("4.7")
                        .totalTrips(150)
                        .build())
                .vehicle(TripDetailResponse.VehicleInfo.builder()
                        .brand("Mazda")
                        .model("3")
                        .color("Plateado")
                        .plate("MNO 345")
                        .type("Sedan")
                        .build())
                .trip(TripDetailResponse.TripInfo.builder()
                        .origin("Universidad Escuela Colombiana de Ingeniería")
                        .destination("Portal 80")
                        .date("18 de Noviembre, 2025")
                        .departureTime("18:30")
                        .arrivalTime("20:00")
                        .availableSeats(3)
                        .build())
                .pricing(TripDetailResponse.PricingInfo.builder()
                        .total(7000.0)
                        .currency("COP")
                        .build())
                .route("Universidad @ Portal 80")
                .build());
        
        return trips;
    }
}
