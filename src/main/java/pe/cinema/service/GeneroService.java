package pe.cinema.service;

import pe.cinema.entity.Genero;

import java.util.List;

public interface GeneroService {

    List<Genero> listarTodos();
    Genero obtenerPorId(Integer id);
    Genero obtenerPorTitulo(String titulo);
    Genero crear(Genero genero);
    Genero actualizar(Integer id, Genero genero);
    void eliminar(Integer id);

}
