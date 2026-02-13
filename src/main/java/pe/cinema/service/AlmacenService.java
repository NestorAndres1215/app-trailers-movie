package pe.cinema.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface AlmacenService {

    String almacenarArchivo(MultipartFile archivo);
    Resource cargarComoRecurso(String nombreArchivo);
    void eliminarArchivo(String nombreArchivo);
    Path cargarArchivo(String nombreArchivo);
}
