package com.hotel.Hotel.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String error,
        String mensaje,
        LocalDateTime timestamp) {

    public static ErrorResponse of(int status, String error, String mensaje) {
        return new ErrorResponse(status, error, mensaje, LocalDateTime.now());
    }
}
