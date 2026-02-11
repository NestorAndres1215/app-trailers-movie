package pe.cinema.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface AlmacenService {

    String almacenarArchivo(MultipartFile archivo);
    Resource cargarComoRecurso(String nombreArchivo);
    void eliminarArchivo(String nombreArchivo);
}
