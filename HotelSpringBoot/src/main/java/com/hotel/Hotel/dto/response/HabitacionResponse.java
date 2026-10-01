package com.hotel.Hotel.dto.response;

import java.util.UUID;

public abstract class HabitacionResponse {

    private final UUID id;
    private final String numero;
    private final double precioPorNoche;
    private final int capacidadMaxima;
    private final String estado;
    private final String tipo;

    protected HabitacionResponse(
            UUID id,
            String numero,
            double precioPorNoche,
            int capacidadMaxima,
            String estado,
            String tipo) {
        this.id = id;
        this.numero = numero;
        this.precioPorNoche = precioPorNoche;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = estado;
        this.tipo = tipo;
    }

    public UUID getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public double getPrecioPorNoche() {
        return precioPorNoche;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getEstado() {
        return estado;
    }

    public String getTipo() {
        return tipo;
    }
}