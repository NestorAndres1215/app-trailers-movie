package pe.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.cinema.service.AlmacenService;

@RestController
@RequestMapping("/assets")
@RequiredArgsConstructor
public class AssetsControlador {


	private final AlmacenService servicio;
	
	@GetMapping("/{filename:.+}")
	public Resource obtenerRecurso(@PathVariable("filename") String filename) {
		return servicio.cargarComoRecurso(filename);
	}
	
}
