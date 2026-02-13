package pe.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {

    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

    Optional<Pelicula> findByTitulo(String titulo);

    List<Pelicula> findByFechaEstreno(LocalDate fechaEstreno);

    List<Pelicula> findByGenerosIn(List<Genero> generos);

    List<Pelicula> findByTituloContainingIgnoreCaseAndFechaEstrenoAndGenerosIn(
            String titulo, LocalDate fechaEstreno, List<Genero> generos);
}
