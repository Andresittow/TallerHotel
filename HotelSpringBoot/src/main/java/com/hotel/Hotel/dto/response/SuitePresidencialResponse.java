package com.hotel.Hotel.dto.response;

import java.util.UUID;

public class SuitePresidencialResponse extends HabitacionResponse {

    private final boolean incluyeMayordomo;
    private final boolean jacuzziPrivado;

    public SuitePresidencialResponse(
            UUID id,
            String numero,
            double precioPorNoche,
            int capacidadMaxima,
            String estado,
            boolean incluyeMayordomo,
            boolean jacuzziPrivado) {

        super(
                id,
                numero,
                precioPorNoche,
                capacidadMaxima,
                estado,
                "SUITE");

        this.incluyeMayordomo = incluyeMayordomo;
        this.jacuzziPrivado = jacuzziPrivado;
    }

    public boolean isIncluyeMayordomo() {
        return incluyeMayordomo;
    }

    public boolean isJacuzziPrivado() {
        return jacuzziPrivado;
    }
}