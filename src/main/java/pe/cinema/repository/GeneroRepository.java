package pe.cinema.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.cinema.entity.Genero;

import java.util.Optional;


public interface GeneroRepository extends JpaRepository<Genero, Integer>{
    Optional<Genero> findByTitulo(String titulo);
}
