package pe.cinema.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import pe.cinema.excepciones.AlmacenExcepcion;
import pe.cinema.excepciones.FileNotFoundException;

import org.springframework.core.io.Resource;
import pe.cinema.service.AlmacenServicio;


@Service
public class AlmacenServicioImpl implements AlmacenServicio {

	@Value("${storage.location}")
	private String storageLocation;

	private Path storagePath;

	// -----------------------------
	// INICIALIZACIÓN DEL ALMACÉN
	// -----------------------------
	@PostConstruct
	@Override
	public void iniciarAlmacenDeArchivos() {
		storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
		try {
			Files.createDirectories(storagePath);
		} catch (IOException e) {
			throw new AlmacenExcepcion("Error al inicializar el almacenamiento de archivos", e);
		}
	}

	// -----------------------------
	// ALMACENAR ARCHIVO
	// -----------------------------
	@Override
	public String almacenarArchivo(MultipartFile archivo) {
		if (archivo == null || archivo.isEmpty()) {
			throw new AlmacenExcepcion("No se puede almacenar un archivo vacío o nulo");
		}

		// Limpiar el nombre de archivo
		String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());

		if (nombreArchivo.contains("..")) {
			throw new AlmacenExcepcion("Nombre de archivo inválido: " + nombreArchivo);
		}

		try (InputStream inputStream = archivo.getInputStream()) {
			Path destino = storagePath.resolve(nombreArchivo);
			Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new AlmacenExcepcion("Error al almacenar el archivo " + nombreArchivo, e);
		}

		return nombreArchivo;
	}


	// -----------------------------
	// CARGAR ARCHIVO COMO PATH
	// -----------------------------
	@Override
	public Path cargarArchivo(String nombreArchivo) {
		if (!StringUtils.hasText(nombreArchivo)) {
			throw new AlmacenExcepcion("El nombre del archivo no puede ser vacío");
		}

		return storagePath.resolve(nombreArchivo).normalize();
	}

	// -----------------------------
	// CARGAR ARCHIVO COMO RECURSO
	// -----------------------------
	@Override
	public Resource cargarComoRecurso(String nombreArchivo) {
		try {
			Path archivo = cargarArchivo(nombreArchivo);
			Resource recurso = new UrlResource(archivo.toUri());

			if (recurso.exists() && recurso.isReadable()) {
				return recurso;
			} else {
				throw new FileNotFoundException("No se pudo encontrar el archivo: " + nombreArchivo);
			}
		} catch (MalformedURLException e) {
			throw new FileNotFoundException("No se pudo encontrar el archivo: " + nombreArchivo, e);
		}
	}

	// -----------------------------
	// ELIMINAR ARCHIVO
	// -----------------------------
	@Override
	public void eliminarArchivo(String nombreArchivo) {
		if (!StringUtils.hasText(nombreArchivo)) {
			return; // No hay nada que eliminar
		}

		Path archivo = cargarArchivo(nombreArchivo);
		try {
			if (Files.exists(archivo)) {
				FileSystemUtils.deleteRecursively(archivo);
			}
		} catch (IOException e) {
			throw new AlmacenExcepcion("Error al eliminar el archivo: " + nombreArchivo, e);
		}
	}


}
