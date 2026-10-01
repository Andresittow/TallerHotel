package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Habitacion;
import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.domain.SuitePresidencial;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import com.hotel.Hotel.dto.response.HabitacionEstandarResponse;
import com.hotel.Hotel.dto.response.SuitePresidencialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HabitacionMapper {

    @Mapping(target = "estado",
            expression = "java(habitacion.getEstado().name())")
    HabitacionEstandarResponse toEstandarResponse(
            HabitacionEstandar habitacion);

    @Mapping(target = "estado",
            expression = "java(habitacion.getEstado().name())")
    SuitePresidencialResponse toSuiteResponse(
            SuitePresidencial habitacion);

    default HabitacionResponse toResponse(Habitacion habitacion) {
        if (habitacion == null) {
            return null;
        }

        if (habitacion instanceof SuitePresidencial suite) {
            return toSuiteResponse(suite);
        }

        if (habitacion instanceof HabitacionEstandar estandar) {
            return toEstandarResponse(estandar);
        }

        throw new IllegalArgumentException(
                "Tipo de habitación no soportado: "
                        + habitacion.getClass().getName());
    }
}