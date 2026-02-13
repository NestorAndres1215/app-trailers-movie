package pe.cinema.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import pe.cinema.excepciones.AlmacenExcepcion;
import pe.cinema.service.AlmacenService;
import pe.cinema.util.AppConstants;

import javax.annotation.PostConstruct;

@Service
public class AlmacenServiceImpl implements AlmacenService {

	@Value("${storage.location}")
	private String storageLocation;

	private Path storagePath;

	@PostConstruct
	public void init() {
		this.storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.storagePath);
		} catch (IOException e) {
			throw new AlmacenExcepcion("No se pudo crear el directorio de almacenamiento");
		}
	}

	@Override
	public String almacenarArchivo(MultipartFile archivo) {

		String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());

		if (nombreArchivo.contains("..")) {
			throw new AlmacenExcepcion(String.format(AppConstants.NOMBRE_ARCHIVO_INVALIDO, nombreArchivo));
		}

		try (InputStream inputStream = archivo.getInputStream()) {
			Path destino = storagePath.resolve(nombreArchivo);
			Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new AlmacenExcepcion(String.format(AppConstants.ERROR_ALMACENAR_ARCHIVO, nombreArchivo));
		}

		return nombreArchivo;
	}

	@Override
	public Path cargarArchivo(String nombreArchivo) {
		return storagePath.resolve(nombreArchivo).normalize();
	}

	@Override
	public Resource cargarComoRecurso(String nombreArchivo) {
		try {
			Path archivo = cargarArchivo(nombreArchivo);
			Resource recurso = new UrlResource(archivo.toUri());

			if (recurso.exists() && recurso.isReadable()) {
				return recurso;
			} else {
				throw new AlmacenExcepcion("El archivo no existe o no se puede leer");
			}
		} catch (MalformedURLException e) {
			throw new AlmacenExcepcion("El archivo no existe o no se puede leer");
		}
	}

	@Override
	public void eliminarArchivo(String nombreArchivo) {
		Path archivo = cargarArchivo(nombreArchivo);
		try {
			if (Files.exists(archivo)) {
				FileSystemUtils.deleteRecursively(archivo);
			}
		} catch (IOException e) {
			throw new AlmacenExcepcion(String.format(AppConstants.ERROR_ELIMINAR_ARCHIVO, nombreArchivo));
		}
	}
}
