package pe.cinema.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;

import java.time.LocalDate;
import java.util.List;

public interface PeliculaService {
    Page<Pelicula> listarPeliculas(Pageable pageable);

    List<Genero> listarGeneros();

    Pelicula guardarPelicula(Pelicula pelicula);

    Pelicula obtenerPorId(Integer id);

    Pelicula actualizarPelicula(Integer id, Pelicula pelicula);

    List<Pelicula> buscarPorTitulo(String titulo);

    Pelicula eliminarPelicula(Integer id);

    List<Pelicula> buscarPorFechaEstreno(LocalDate fechaEstreno);

    List<Pelicula> buscarPorGeneros(List<Genero> generos);

    List<Pelicula> buscarAvanzado(String titulo, LocalDate fechaEstreno, List<Genero> generos);
}
