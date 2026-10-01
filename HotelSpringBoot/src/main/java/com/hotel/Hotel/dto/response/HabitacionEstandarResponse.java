package com.hotel.Hotel.dto.response;

import java.util.UUID;

public class HabitacionEstandarResponse extends HabitacionResponse {

    private final int camasIndividuales;

    public HabitacionEstandarResponse(
            UUID id,
            String numero,
            double precioPorNoche,
            int capacidadMaxima,
            String estado,
            int camasIndividuales) {

        super(
                id,
                numero,
                precioPorNoche,
                capacidadMaxima,
                estado,
                "ESTANDAR");

        this.camasIndividuales = camasIndividuales;
    }

    public int getCamasIndividuales() {
        return camasIndividuales;
    }
}