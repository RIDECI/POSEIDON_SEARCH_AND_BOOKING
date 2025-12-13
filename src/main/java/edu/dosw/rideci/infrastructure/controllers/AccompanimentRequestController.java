package edu.dosw.rideci.infrastructure.controllers;

import edu.dosw.rideci.infrastructure.controllers.dto.Request.AccompanimentRequestDto;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.AccompanimentDetailResponse;
import edu.dosw.rideci.infrastructure.controllers.dto.Response.AccompanimentRequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accompaniment-requests")
@CrossOrigin(origins = "*")
public class AccompanimentRequestController {

    private final Map<String, AccompanimentRequestResponse> requests = new HashMap<>();
    private final AccompanimentController accompanimentController = new AccompanimentController();

    @PostMapping
    public ResponseEntity<AccompanimentRequestResponse> createAccompanimentRequest(
            @RequestBody AccompanimentRequestDto request) {
        
        String requestId = "req-" + UUID.randomUUID().toString().substring(0, 8);
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        
        AccompanimentRequestResponse response = AccompanimentRequestResponse.builder()
                .requestId(requestId)
                .accompanimentId(request.getAccompanimentId())
                .passengerId(request.getPassengerId())
                .status("confirmed")
                .notes(request.getNotes())
                .createdAt(createdAt)
                .build();
        
        // Guardar en memoria (en producción iría a MongoDB)
        requests.put(requestId, response);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<AccompanimentRequestResponse.AccompanimentRequestDetailResponse> getAccompanimentRequest(
            @PathVariable String requestId) {
        
        AccompanimentRequestResponse request = requests.get(requestId);
        
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Obtener detalles del acompañamiento
        ResponseEntity<AccompanimentDetailResponse> accompanimentResponse = 
                accompanimentController.getAccompanimentById(request.getAccompanimentId());
        
        if (!accompanimentResponse.hasBody()) {
            return ResponseEntity.notFound().build();
        }
        
        AccompanimentRequestResponse.AccompanimentRequestDetailResponse detailResponse = 
                AccompanimentRequestResponse.AccompanimentRequestDetailResponse.builder()
                        .requestId(request.getRequestId())
                        .accompanimentId(request.getAccompanimentId())
                        .passengerId(request.getPassengerId())
                        .status(request.getStatus())
                        .notes(request.getNotes())
                        .createdAt(request.getCreatedAt())
                        .accompanimentDetails(accompanimentResponse.getBody())
                        .build();
        
        return ResponseEntity.ok(detailResponse);
    }

    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<AccompanimentRequestResponse> cancelAccompanimentRequest(
            @PathVariable String requestId) {
        
        AccompanimentRequestResponse request = requests.get(requestId);
        
        if (request == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Actualizar estado a cancelado
        AccompanimentRequestResponse updatedRequest = AccompanimentRequestResponse.builder()
                .requestId(request.getRequestId())
                .accompanimentId(request.getAccompanimentId())
                .passengerId(request.getPassengerId())
                .status("cancelled")
                .notes(request.getNotes())
                .createdAt(request.getCreatedAt())
                .build();
        
        requests.put(requestId, updatedRequest);
        
        return ResponseEntity.ok(updatedRequest);
    }
}
