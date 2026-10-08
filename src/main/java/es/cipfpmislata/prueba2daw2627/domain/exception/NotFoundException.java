package es.cipfpmislata.prueba2daw2627.domain.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String entityName, Long id) {
        super(entityName + " con id " + id + " no encontrado");
    }
}
