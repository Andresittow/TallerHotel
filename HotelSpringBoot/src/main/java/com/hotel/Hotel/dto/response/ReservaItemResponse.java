package com.hotel.Hotel.dto.response;

import com.hotel.Hotel.domain.EstadoReserva;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservaItemResponse(
        UUID idReserva,
        String numeroHabitacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin,
        EstadoReserva estado,
        double costoTotal) {
}