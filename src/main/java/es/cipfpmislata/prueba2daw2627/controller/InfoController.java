package es.cipfpmislata.prueba2daw2627.controller;

import es.cipfpmislata.prueba2daw2627.domain.service.BookService;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controlador nuevo creado en la rama A8 (Usuario A).
 * Endpoint: GET /api/info
 * Devuelve informacion general de la aplicacion y de la base de datos que usa.
 */
@RestController
@RequestMapping("/api/info")
public class InfoController {

    private final BookService bookService;
    private final Environment environment;

    public InfoController(BookService bookService, Environment environment) {
        this.bookService = bookService;
        this.environment = environment;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("application", environment.getProperty("spring.application.name"));
        body.put("feature", "A8");
        body.put("endpoint", "/api/info");
        body.put("profiles", environment.getActiveProfiles());
        // Solo para la demostracion: permite comprobar que cada instancia (8081/8082)
        // usa su propia base de datos. En un proyecto real NO se expone la URL de la BD.
        body.put("datasource", environment.getProperty("spring.datasource.url"));
        // Acceso a la capa de servicio -> demuestra que la BD esta disponible.
        body.put("books", bookService.findAll().size());
        return ResponseEntity.ok(body);
    }
}
