package pe.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import pe.cinema.entity.Pelicula;

import pe.cinema.service.PeliculaService;
import pe.cinema.util.AppConstants;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminControlador {

	private final PeliculaService peliculaServicio;

	@GetMapping("")
	public ModelAndView verPaginaDeInicio(@PageableDefault(sort = "titulo", size = 5) Pageable pageable) {
		return new ModelAndView("admin/index")
				.addObject("peliculas", peliculaServicio.listarPeliculas(pageable));
	}

	@GetMapping("/peliculas/nuevo")
	public ModelAndView mostrarFormularioDeNuevaPelicula() {
		return prepararFormulario(new Pelicula(), "admin/nueva-pelicula");
	}

	@PostMapping("/peliculas/nuevo")
	public ModelAndView registrarPelicula(@Validated Pelicula pelicula, BindingResult bindingResult) {
		if (bindingResult.hasErrors() || pelicula.getPortada().isEmpty()) {
			if (pelicula.getPortada().isEmpty()) {
				bindingResult.rejectValue("portada", "MultipartNotEmpty", AppConstants.PELICULA_PORTADA_OBLIGATORIA);
			}
			return prepararFormulario(pelicula, "admin/nueva-pelicula");
		}

		peliculaServicio.guardarPelicula(pelicula);
		return new ModelAndView("redirect:/admin");
	}

	@GetMapping("/peliculas/{id}/editar")
	public ModelAndView mostrarFormularioDeEditarPelicula(@PathVariable Integer id) {
		Pelicula pelicula = peliculaServicio.obtenerPorId(id);
		return prepararFormulario(pelicula, "admin/editar-pelicula");
	}

	@PostMapping("/peliculas/{id}/editar")
	public ModelAndView actualizarPelicula(@PathVariable Integer id,
										   @Validated Pelicula pelicula,
										   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return prepararFormulario(pelicula, "admin/editar-pelicula");
		}

		peliculaServicio.actualizarPelicula(id, pelicula);
		return new ModelAndView("redirect:/admin");
	}


	@PostMapping("/peliculas/{id}/eliminar")
	public String eliminarPelicula(@PathVariable Integer id) {
		peliculaServicio.eliminarPelicula(id);
		return "redirect:/admin";
	}

	private ModelAndView prepararFormulario(Pelicula pelicula, String vista) {
		return new ModelAndView(vista)
				.addObject("pelicula", pelicula)
				.addObject("generos", peliculaServicio.listarGeneros());
	}
}
