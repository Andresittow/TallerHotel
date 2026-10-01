package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.request.ActualizarClienteRequest;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.dto.response.ClienteResumenResponse;
import com.hotel.Hotel.dto.response.ReservaItemResponse;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "penalizaciones", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Cliente toEntity(CrearClienteRequest request);

    // ---------- Tarea 2: resumen con proyección anidada ----------

    @Mapping(target = "totalReservasRealizadas",
            expression = "java(cliente.getReservas().size())")
    @Mapping(target = "montoTotalGastado",
            source = "reservas", qualifiedByName = "calcularMontoTotal")
    @Mapping(target = "reservasRecientes",
            source = "reservas", qualifiedByName = "mapearHistorialReservas")
    ClienteResumenResponse toResumenResponse(Cliente cliente);

    // Sub-DTO plano: NO referencia de vuelta al cliente => sin bucle de serialización.
    @Mapping(target = "idReserva", source = "id")
    @Mapping(target = "numeroHabitacion", source = "habitacion.numero")
    @Mapping(target = "fechaInicio", source = "periodo.fechaInicio")
    @Mapping(target = "fechaFin", source = "periodo.fechaFin")
    ReservaItemResponse toReservaItemResponse(Reserva reserva);

    @Named("calcularMontoTotal")
    default double calcularMontoTotal(List<Reserva> reservas) {
        if (reservas == null) {
            return 0.0;
        }
        return reservas.stream()
                .mapToDouble(Reserva::getCostoTotal)
                .sum();
    }

    @Named("mapearHistorialReservas")
    default List<ReservaItemResponse> mapearHistorialReservas(List<Reserva> reservas) {
        if (reservas == null) {
            return List.of();
        }
        // Historial completo, de la más reciente a la más antigua.
        return reservas.stream()
                .sorted(Comparator.comparing(
                        (Reserva r) -> r.getPeriodo().getFechaInicio()).reversed())
                .map(this::toReservaItemResponse)
                .toList();
    }

    // ---------- Tarea 3: PATCH seguro ----------

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "penalizaciones", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    void updateClienteFromDto(ActualizarClienteRequest dto, @MappingTarget Cliente entity);
}
