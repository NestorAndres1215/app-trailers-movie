package pe.cinema.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;

import java.util.List;


public interface PeliculaServicio {

    Page<Pelicula> listarPeliculas(Pageable pageable);

    List<Genero> listarGeneros();

    Pelicula guardarPelicula(Pelicula pelicula);

    Pelicula obtenerPorId(Integer id);

    Pelicula actualizarPelicula(Integer id, Pelicula pelicula);

    void eliminarPelicula(Integer id);

}
