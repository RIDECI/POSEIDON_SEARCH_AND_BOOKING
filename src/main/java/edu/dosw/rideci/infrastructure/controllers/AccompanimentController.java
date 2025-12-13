package edu.dosw.rideci.infrastructure.controllers;

import edu.dosw.rideci.infrastructure.controllers.dto.Response.AccompanimentDetailResponse;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.AccompanimentSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/accompaniments")
@CrossOrigin(origins = "*")
public class AccompanimentController {

    // Endpoint para buscar acompañamientos
    @GetMapping("/search")
    public ResponseEntity<List<AccompanimentSearchResponse>> searchAccompaniments(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String departureTime,
            @RequestParam(defaultValue = "false") Boolean nearbySearch) {
        
        List<AccompanimentSearchResponse> allAccompaniments = getAllAccompaniments();
        
        // Filtrar por destino y hora
        List<AccompanimentSearchResponse> filtered = allAccompaniments.stream()
                .filter(acc -> {
                    boolean matchesDestination = destination == null || 
                            destination.trim().isEmpty() || 
                            acc.getRoute().toLowerCase().contains(destination.toLowerCase());
                    
                    boolean matchesDepartureTime = departureTime == null || 
                            departureTime.trim().isEmpty() || 
                            acc.getDepartureTime().equals(departureTime);
                    
                    return matchesDestination && matchesDepartureTime;
                })
                .toList();
        
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccompanimentDetailResponse> getAccompanimentById(@PathVariable String id) {
        Map<String, AccompanimentDetailResponse> details = getDetailedAccompaniments();
        
        AccompanimentDetailResponse accompaniment = details.get(id);
        if (accompaniment != null) {
            return ResponseEntity.ok(accompaniment);
        }
        return ResponseEntity.notFound().build();
    }

    private List<AccompanimentSearchResponse> getAllAccompaniments() {
        return Arrays.asList(
            AccompanimentSearchResponse.builder()
                .id("acc1")
                .passengerName("Carlos Santana")
                .transportMethod("Transmilenio")
                .rating("3.5")
                .route("Universidad • Portal 80")
                .departureTime("18:30")
                .build(),
            AccompanimentSearchResponse.builder()
                .id("acc2")
                .passengerName("Carlos Gomez")
                .transportMethod("Bus")
                .rating("4.5")
                .route("Universidad • Portal 80")
                .departureTime("18:30")
                .build(),
            AccompanimentSearchResponse.builder()
                .id("acc3")
                .passengerName("Alex Ramirez")
                .transportMethod("Transmilenio")
                .rating("4.0")
                .route("Universidad • Portal 80")
                .departureTime("18:30")
                .build(),
            AccompanimentSearchResponse.builder()
                .id("acc4")
                .passengerName("José Torres")
                .transportMethod("Carro Particular")
                .rating("3.5")
                .route("Universidad • Portal 80")
                .departureTime("18:30")
                .build(),
            AccompanimentSearchResponse.builder()
                .id("acc5")
                .passengerName("Fernando Lopez")
                .transportMethod("Transmilenio")
                .rating("4.5")
                .route("Universidad • Portal 80")
                .departureTime("18:30")
                .build(),
            AccompanimentSearchResponse.builder()
                .id("acc6")
                .passengerName("Camilo Saenz")
                .transportMethod("Bus")
                .rating("4.5")
                .route("Universidad • Portal 80")
                .departureTime("18:30")
                .build()
        );
    }

    private Map<String, AccompanimentDetailResponse> getDetailedAccompaniments() {
        Map<String, AccompanimentDetailResponse> accompaniments = new HashMap<>();
        
        accompaniments.put("acc1", AccompanimentDetailResponse.builder()
                .id("acc1")
                .passenger(AccompanimentDetailResponse.PassengerInfo.builder()
                        .name("Carlos Santana")
                        .rating("3.5")
                        .verificationStatus("Estudiante Verificado")
                        .build())
                .route(AccompanimentDetailResponse.RouteInfo.builder()
                        .transportMethod("Transmilenio")
                        .meetingPoint("Entrada Universidad")
                        .destination("Portal 80")
                        .departureTime("18:30")
                        .estimatedArrival("19:45")
                        .build())
                .build());
        
        accompaniments.put("acc2", AccompanimentDetailResponse.builder()
                .id("acc2")
                .passenger(AccompanimentDetailResponse.PassengerInfo.builder()
                        .name("Carlos Gomez")
                        .rating("4.5")
                        .verificationStatus("Estudiante Verificado")
                        .build())
                .route(AccompanimentDetailResponse.RouteInfo.builder()
                        .transportMethod("Bus")
                        .meetingPoint("Parada Universidad")
                        .destination("Portal 80")
                        .departureTime("18:30")
                        .estimatedArrival("19:30")
                        .build())
                .build());
        
        accompaniments.put("acc3", AccompanimentDetailResponse.builder()
                .id("acc3")
                .passenger(AccompanimentDetailResponse.PassengerInfo.builder()
                        .name("Alex Ramirez")
                        .rating("4.0")
                        .verificationStatus("Estudiante Verificado")
                        .build())
                .route(AccompanimentDetailResponse.RouteInfo.builder()
                        .transportMethod("Transmilenio")
                        .meetingPoint("Entrada Universidad")
                        .destination("Portal 80")
                        .departureTime("18:30")
                        .estimatedArrival("19:50")
                        .build())
                .build());
        
        accompaniments.put("acc4", AccompanimentDetailResponse.builder()
                .id("acc4")
                .passenger(AccompanimentDetailResponse.PassengerInfo.builder()
                        .name("José Torres")
                        .rating("3.5")
                        .verificationStatus("Estudiante Verificado")
                        .build())
                .route(AccompanimentDetailResponse.RouteInfo.builder()
                        .transportMethod("Carro Particular")
                        .meetingPoint("Parqueadero Universidad")
                        .destination("Portal 80")
                        .departureTime("18:30")
                        .estimatedArrival("19:15")
                        .build())
                .build());
        
        accompaniments.put("acc5", AccompanimentDetailResponse.builder()
                .id("acc5")
                .passenger(AccompanimentDetailResponse.PassengerInfo.builder()
                        .name("Fernando Lopez")
                        .rating("4.5")
                        .verificationStatus("Estudiante Verificado")
                        .build())
                .route(AccompanimentDetailResponse.RouteInfo.builder()
                        .transportMethod("Transmilenio")
                        .meetingPoint("Entrada Universidad")
                        .destination("Portal 80")
                        .departureTime("18:30")
                        .estimatedArrival("19:40")
                        .build())
                .build());
        
        accompaniments.put("acc6", AccompanimentDetailResponse.builder()
                .id("acc6")
                .passenger(AccompanimentDetailResponse.PassengerInfo.builder()
                        .name("Camilo Saenz")
                        .rating("4.5")
                        .verificationStatus("Estudiante Verificado")
                        .build())
                .route(AccompanimentDetailResponse.RouteInfo.builder()
                        .transportMethod("Bus")
                        .meetingPoint("Parada Universidad")
                        .destination("Portal 80")
                        .departureTime("18:30")
                        .estimatedArrival("19:35")
                        .build())
                .build());
        
        return accompaniments;
    }
}
