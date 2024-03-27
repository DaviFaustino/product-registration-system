package com.davifaustino.productregistrationsystem.api.dtos;

import java.sql.Timestamp;

public record ErrorResponseDto(String message, Timestamp time, String path, String method) {
    
}
