package pe.cinema.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlmacenExcepcion.class)
    public ResponseEntity<String> manejarAlmacenExcepcion(AlmacenExcepcion e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error de almacén: " + e.getMessage());
    }


    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> manejarFileNotFoundException(FileNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Archivo no encontrado: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepcionGeneral(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrió un error inesperado en el servidor");
    }
}
