package com.davifaustino.productregistrationsystem.api.dtos.responses;

import java.sql.Timestamp;

public record ErrorResponse(String message, Timestamp time, String path, String method) {
    
}
