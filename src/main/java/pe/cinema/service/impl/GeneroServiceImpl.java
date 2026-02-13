package pe.cinema.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.cinema.entity.Genero;
import pe.cinema.repository.GeneroRepository;
import pe.cinema.service.GeneroService;
import pe.cinema.util.AppConstants;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneroServiceImpl implements GeneroService {

    private final GeneroRepository generoRepositorio;

    public List<Genero> listarTodos() {
        return generoRepositorio.findAll();
    }

    @Override
    public Genero obtenerPorId(Integer id) {
        return generoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.GENERO_NO_ENCONTRADO, id)));
    }

    @Override
    public Genero obtenerPorTitulo(String titulo) {
        return generoRepositorio.findByTitulo(titulo)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.GENERO_NO_ENCONTRADO_TITULO, titulo)));
    }

    @Override
    public Genero crear(Genero genero) {
        generoRepositorio.findByTitulo(genero.getTitulo())
                .ifPresent(g -> {
                    throw new RuntimeException(String.format(AppConstants.GENERO_EXISTE, g.getTitulo()));
                });
        return generoRepositorio.save(genero);
    }

    @Override
    public Genero actualizar(Integer id, Genero genero) {
        Genero existente = obtenerPorId(id);
        existente.setTitulo(genero.getTitulo());
        return generoRepositorio.save(existente);
    }

    @Override
    public void eliminar(Integer id) {
        Genero existente = obtenerPorId(id);
        generoRepositorio.delete(existente);
    }
}
