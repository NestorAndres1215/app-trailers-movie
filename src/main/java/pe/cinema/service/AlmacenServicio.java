package pe.cinema.service;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AlmacenServicio {

    void iniciarAlmacenDeArchivos();

    String almacenarArchivo(MultipartFile archivo);

    Path cargarArchivo(String nombreArchivo);

    Resource cargarComoRecurso(String nombreArchivo);

    void eliminarArchivo(String nombreArchivo);
}
