Taller Practico
"Contratos Polimórficos, Proyecciones Anidadas y Actualización Parcial Segura en la API del Hotel"
	Prerrequisito: Proyecto base con Cliente, Habitacion y Reserva ya funcionando con endpoints básicos. 
	Objetivo: Dominar el mapeo objeto-a-objeto en escenarios complejos: resolver jerarquías de herencia en la API REST, gestionar proyecciones anidadas sin recursión y mapear actualizaciones parciales mediante MapStruct. 
Tarea 1: Polimorfismo en la API REST (Módulo de Habitaciones)
Se mapeó la herencia en PostgreSQL con @Inheritance(strategy = InheritanceType.JOINED) para HabitacionEstandar y SuitePresidencial. En clase normalmente solo se hace el CRUD de clientes o reservas básicas. 
El reto: Diseñar el contrato de API para dar de alta y consultar habitaciones polimórficas sin duplicar controladores ni corromper los tipos concretos. 
Requerimientos Técnicos:
	DTOs de Entrada Polimórficos (request):
	Crear una jerarquía de DTOs o un DTO tipado con discriminador:
	CrearHabitacionEstandarRequest (incluye numero, precioPorNoche, capacidadMaxima, camasIndividuales).
	CrearSuitePresidencialRequest (incluye numero, precioPorNoche, capacidadMaxima, incluyeMayordomo, jacuzziPrivado).
	DTOs de Salida (response):
	Diseñar HabitacionResponse como clase base abstracta (o interfaz sellada / sealed class de Java 17) con sus implementaciones HabitacionEstandarResponse y SuitePresidencialResponse. 
	Mapeo con MapStruct (HabitacionMapper):
	Configurar el mapper con métodos de mapeo polimórfico o condicional para que un método HabitacionResponse toResponse(Habitacion habitacion) determine automáticamente la subclase concreta en tiempo de ejecución y devuelva la proyección exacta sin perder los atributos específicos (camasIndividuales o jacuzziPrivado). 
	Endpoint en HabitacionController:
	GET /api/habitaciones: Retorna una lista polimórfica homogénea en JSON (List<HabitacionResponse>), donde cada elemento del array incluye un campo "tipo": "ESTANDAR" o "tipo": "SUITE".
Tarea 2: Proyecciones Anidadas Complejas y Detalle del Huésped
En clase se mapearon IDs o textos planos simples (UUID clienteId, String nombreCliente). Para la vista de recepción del hotel se necesita un endpoint ejecutivo de consulta detallada. 
El reto: Crear el endpoint GET /api/clientes/{id}/resumen que retorne una vista agregada y limpia del cliente junto con el historial completo de sus reservas, sin caer en bucle de serialización infinita. 
Requerimientos Técnicos:
	Diseño del DTO Compuesto (dto.response.ClienteResumenResponse):
Java
public record ClienteResumenResponse(
    UUID id,
    String nombre,
    String email,
    boolean activo,
    int penalizaciones,
    int totalReservasRealizadas,
    double montoTotalGastado,
    List<ReservaItemResponse> reservasRecientes
) {}

	Diseño del sub-DTO (ReservaItemResponse):
	Contiene solo idReserva, numeroHabitacion, fechaInicio, fechaFin, estado y costoTotal (sin volver a referenciar al cliente). 

	Mapeo Personalizado en ClienteMapper:
	Utilizar MapStruct para calcular automáticamente:
	totalReservasRealizadas → basado en cliente.getReservas().size(). 
	montoTotalGastado → sumatoria calculada mediante Java Stream (reservas.stream().mapToDouble(...)). 
	Indicar a MapStruct cómo poblar la lista interna utilizando métodos auxiliares @Named o expresiones personalizadas.
Tarea 3: Actualización Parcial Segura con HTTP PATCH (NullValuePropertyMappingStrategy)
Un error clásico es actualizar entidades usando PUT sobrescribiendo campos no enviados con null o forzar al cliente a enviar el objeto completo. 
El reto: Implementar un endpoint PATCH /api/clientes/{id} que permita modificar selectivamente el nombre o el email del cliente sin alterar su estado de penalizaciones ni sobrescribir valores con nulos. 
Requerimientos Técnicos:
	DTO de Entrada: ActualizarClienteRequest(String nombre, String email). Ambos atributos son opcionales (null si no se desean cambiar).
	Método en ClienteMapper:
	Configurar en MapStruct la estrategia para ignorar nulos en actualizaciones sobre una instancia existente:
Java
@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
void updateClienteFromDto(ActualizarClienteRequest dto, @MappingTarget Cliente entity);
	Ignorar explícitamente id, activo, penalizaciones y reservas para evitar vulnerabilidad de sobre-asignación (Mass Assignment). 

	Flujo en el Servicio y Controlador:
	Cargar la entidad desde la base de datos. 
	Aplicar la actualización parcial mediante el mapper. 
	Guardar la entidad actualizada y retornar el ClienteResponse actualizado con código HTTP 200 OK. 
4. Guía de Pruebas y Evidencias Solicitadas
El estudiante debe incluir en su repositorio un archivo ejecutable solicitudes-avanzadas.http (o colección de Postman exportada) con las siguientes 4 pruebas documentadas:
	Creación de Suite Presidencial:
	POST /api/habitaciones/suites enviando { "numero": "P05-501", "precioPorNoche": 350.0, "capacidadMaxima": 2, "jacuzziPrivado": true, "incluyeMayordomo": true }. Respuesta esperada: 201 Created con el ID generado. 
	Consulta Polimórfica:
	GET /api/habitaciones. La respuesta debe mostrar un JSON array donde convivan habitaciones estándar y suites, cada una con sus campos propios sin errores de casteo.
	Resumen Ejecutivo del Cliente:
	GET /api/clientes/{id}/resumen. Se debe apreciar el total gastado calculado, la cantidad de reservas y la lista embebida limpia (sin desbordamiento de memoria ni $ref cíclicos). 
	Actualización Selectiva (PATCH):
	PATCH /api/clientes/{id} enviando únicamente { "nombre": "Nuevo Nombre Modificado" }. Confirmar que el email, su estado activo y sus penalizaciones permanecieron inalterados en la base de datos. 
