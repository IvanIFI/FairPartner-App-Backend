package com.ferrinsa.fairpartner.maintenance.controller;

import com.ferrinsa.fairpartner.maintenance.dto.StatusResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    private static final String STATUS_OK = "OK";
    private static final String STATUS_OK_MESSAGE = "Service is running";

    @GetMapping("/ping")
    public ResponseEntity<StatusResponseDTO> getStatus() {
        return ResponseEntity.ok(new StatusResponseDTO(STATUS_OK, STATUS_OK_MESSAGE, Instant.now()));
    }

}
