package pe.cinema.service;

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
import pe.cinema.util.AppConstants;

import javax.annotation.PostConstruct;

@Service
public class AlmacenServicio {

	@Value("${storage.location}")
	private String storageLocation;

	private Path storagePath;

	// -----------------------------
	// INICIALIZACIÓN DEL ALMACÉN
	// -----------------------------
	@PostConstruct
	public void iniciarAlmacenDeArchivos() {
		storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
		try {
			Files.createDirectories(storagePath);
		} catch (IOException e) {
			throw new AlmacenExcepcion(AppConstants.ERROR_INICIALIZAR_ALMACEN, e);
		}
	}

	// -----------------------------
	// ALMACENAR ARCHIVO
	// -----------------------------
	public String almacenarArchivo(MultipartFile archivo) {
		if (archivo == null || archivo.isEmpty()) {
			throw new AlmacenExcepcion(AppConstants.ARCHIVO_VACIO);
		}

		String nombreArchivo = StringUtils.cleanPath(archivo.getOriginalFilename());

		if (nombreArchivo.contains("..")) {
			throw new AlmacenExcepcion(String.format(AppConstants.NOMBRE_ARCHIVO_INVALIDO, nombreArchivo));
		}

		try (InputStream inputStream = archivo.getInputStream()) {
			Path destino = storagePath.resolve(nombreArchivo);
			Files.copy(inputStream, destino, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new AlmacenExcepcion(String.format(AppConstants.ERROR_ALMACENAR_ARCHIVO, nombreArchivo), e);
		}

		return nombreArchivo;
	}

	// -----------------------------
	// CARGAR ARCHIVO COMO PATH
	// -----------------------------
	public Path cargarArchivo(String nombreArchivo) {
		if (!StringUtils.hasText(nombreArchivo)) {
			throw new AlmacenExcepcion(AppConstants.NOMBRE_ARCHIVO_OBLIGATORIO);
		}

		return storagePath.resolve(nombreArchivo).normalize();
	}

	// -----------------------------
	// CARGAR ARCHIVO COMO RECURSO
	// -----------------------------
	public Resource cargarComoRecurso(String nombreArchivo) {
		try {
			Path archivo = cargarArchivo(nombreArchivo);
			Resource recurso = new UrlResource(archivo.toUri());

			if (recurso.exists() && recurso.isReadable()) {
				return recurso;
			} else {
				throw new AlmacenExcepcion(String.format(AppConstants.ARCHIVO_NO_ENCONTRADO, nombreArchivo));
			}
		} catch (MalformedURLException e) {
			throw new AlmacenExcepcion(String.format(AppConstants.ARCHIVO_NO_ENCONTRADO, nombreArchivo), e);
		}
	}

	// -----------------------------
	// ELIMINAR ARCHIVO
	// -----------------------------
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
			throw new AlmacenExcepcion(String.format(AppConstants.ERROR_ELIMINAR_ARCHIVO, nombreArchivo), e);
		}
	}
}
