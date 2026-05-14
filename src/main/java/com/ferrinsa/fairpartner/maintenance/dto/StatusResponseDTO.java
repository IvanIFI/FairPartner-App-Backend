package com.ferrinsa.fairpartner.maintenance.dto;

import java.time.Instant;

public record StatusResponseDTO(
        String status,
        String message,
        Instant timestamp
) {
}
