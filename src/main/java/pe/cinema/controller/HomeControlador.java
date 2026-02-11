package pe.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import pe.cinema.entity.Pelicula;
import pe.cinema.service.PeliculaService;


import java.util.List;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class HomeControlador {

	private final PeliculaService peliculaServicio;

	@GetMapping("")
	public ModelAndView verPaginaDeInicio() {
		List<Pelicula> ultimasPeliculas = peliculaServicio
				.listarPeliculas(PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "fechaEstreno")))
				.toList();

		return new ModelAndView("index")
				.addObject("ultimasPeliculas", ultimasPeliculas);
	}

	@GetMapping("/peliculas")
	public ModelAndView listarPeliculas(
			@PageableDefault(sort = "fechaEstreno", direction = Sort.Direction.DESC) Pageable pageable) {

		Page<Pelicula> peliculas = peliculaServicio.listarPeliculas(pageable);
		return new ModelAndView("peliculas")
				.addObject("peliculas", peliculas);
	}

	@GetMapping("/peliculas/{id}")
	public ModelAndView mostrarDetallesDePelicula(@PathVariable Integer id) {
		Pelicula pelicula = peliculaServicio.obtenerPorId(id);
		return new ModelAndView("pelicula")
				.addObject("pelicula", pelicula);
	}
}
