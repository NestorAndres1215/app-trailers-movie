package pe.cinema.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import pe.cinema.entity.Pelicula;
import pe.cinema.service.PeliculaServicio;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminControlador {

	private final PeliculaServicio peliculaServicio;



	@GetMapping("")
	public ModelAndView verPaginaDeInicio(@PageableDefault(sort = "titulo", size = 5) Pageable pageable) {
		return new ModelAndView("admin/index")
				.addObject("peliculas", peliculaServicio.listarPeliculas(pageable));
	}

	@GetMapping("/peliculas/nuevo")
	public ModelAndView mostrarFormularioDeNuevaPelicula() {
		return new ModelAndView("admin/nueva-pelicula")
				.addObject("pelicula", new Pelicula())
				.addObject("generos", peliculaServicio.listarGeneros());
	}

	@PostMapping("/peliculas/nuevo")
	public ModelAndView registrarPelicula(@Validated Pelicula pelicula, BindingResult bindingResult) {
		if (bindingResult.hasErrors() || pelicula.getPortada().isEmpty()) {
			if (pelicula.getPortada().isEmpty()) {
				bindingResult.rejectValue("portada", "MultipartNotEmpty");
			}
			return new ModelAndView("admin/nueva-pelicula")
					.addObject("pelicula", pelicula)
					.addObject("generos", peliculaServicio.listarGeneros());
		}

		peliculaServicio.guardarPelicula(pelicula);
		return new ModelAndView("redirect:/admin");
	}

	@GetMapping("/peliculas/{id}/editar")
	public ModelAndView mostrarFormularioDeEditarPelicula(@PathVariable Integer id) {
		return new ModelAndView("admin/editar-pelicula")
				.addObject("pelicula", peliculaServicio.obtenerPorId(id))
				.addObject("generos", peliculaServicio.listarGeneros());
	}

	@PostMapping("/peliculas/{id}/editar")
	public ModelAndView actualizarPelicula(@PathVariable Integer id,
										   @Validated Pelicula pelicula,
										   BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ModelAndView("admin/editar-pelicula")
					.addObject("pelicula", pelicula)
					.addObject("generos", peliculaServicio.listarGeneros());
		}

		peliculaServicio.actualizarPelicula(id, pelicula);
		return new ModelAndView("redirect:/admin");
	}

	@PostMapping("/peliculas/{id}/eliminar")
	public String eliminarPelicula(@PathVariable Integer id) {
		peliculaServicio.eliminarPelicula(id);
		return "redirect:/admin";
	}
}
