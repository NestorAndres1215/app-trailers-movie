package pe.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.cinema.entity.Genero;
import pe.cinema.repository.GeneroRepositorio;
import pe.cinema.util.AppConstants;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneroService {

    private final GeneroRepositorio generoRepositorio;

    public List<Genero> listarTodos() {
        return generoRepositorio.findAll();
    }

    public Genero obtenerPorId(Integer id) {
        return generoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.GENERO_NO_ENCONTRADO, id)));
    }

    public Genero obtenerPorTitulo(String titulo) {
        return generoRepositorio.findByTitulo(titulo)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.GENERO_NO_ENCONTRADO_TITULO, titulo)));
    }

    public Genero crear(Genero genero) {
        generoRepositorio.findByTitulo(genero.getTitulo())
                .ifPresent(g -> { throw new RuntimeException(String.format(AppConstants.GENERO_EXISTE, g.getTitulo())); });
        return generoRepositorio.save(genero);
    }

    public Genero actualizar(Integer id, Genero genero) {
        Genero existente = obtenerPorId(id);
        existente.setTitulo(genero.getTitulo());
        return generoRepositorio.save(existente);
    }

    public void eliminar(Integer id) {
        Genero existente = obtenerPorId(id);
        generoRepositorio.delete(existente);
    }
}
