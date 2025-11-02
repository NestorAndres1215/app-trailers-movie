package pe.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;

import java.time.LocalDate;
import java.util.List;


public interface PeliculaRepositorio extends JpaRepository<Pelicula, Integer>{
    // Listar películas por título (contiene)
    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

    // Listar películas por fecha de estreno
    List<Pelicula> findByFechaEstreno(LocalDate fechaEstreno);

    // Listar películas por género
    List<Pelicula> findByGenerosIn(List<Genero> generos);

    // Opcional: combinar filtros por título, fecha y género
    List<Pelicula> findByTituloContainingIgnoreCaseAndFechaEstrenoAndGenerosIn(
            String titulo, LocalDate fechaEstreno, List<Genero> generos);
}
