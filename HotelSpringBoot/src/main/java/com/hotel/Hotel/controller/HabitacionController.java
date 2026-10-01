package com.hotel.Hotel.controller;

import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.dto.request.CrearSuitePresidencialRequest;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import com.hotel.Hotel.service.HabitacionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @PostMapping("/estandar")
    public ResponseEntity<HabitacionResponse> crearEstandar(
            @RequestBody CrearHabitacionEstandarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(habitacionService.crearEstandar(request));
    }

    @PostMapping("/suites")
    public ResponseEntity<HabitacionResponse> crearSuite(
            @RequestBody CrearSuitePresidencialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(habitacionService.crearSuite(request));
    }

    @GetMapping
    public ResponseEntity<List<HabitacionResponse>> listarTodas() {
        return ResponseEntity.ok(habitacionService.listarTodas());
    }
}