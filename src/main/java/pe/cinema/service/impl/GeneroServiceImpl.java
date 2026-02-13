package pe.cinema.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.cinema.entity.Genero;
import pe.cinema.excepciones.AlmacenExcepcion;
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
                .orElseThrow(() -> new AlmacenExcepcion(AppConstants.GENERO_NO_ENCONTRADO));
    }

    @Override
    public Genero obtenerPorTitulo(String titulo) {
        return generoRepositorio.findByTitulo(titulo)
                .orElseThrow(() -> new AlmacenExcepcion(AppConstants.GENERO_NO_ENCONTRADO_TITULO));
    }

    @Override
    public Genero crear(Genero genero) {
        generoRepositorio.findByTitulo(genero.getTitulo())
                .ifPresent(g -> {
                    throw new AlmacenExcepcion(AppConstants.GENERO_EXISTE);
                });
        return generoRepositorio.save(genero);
    }


    @Override
    public Genero actualizar(Integer id, Genero genero) {
        Genero existente = obtenerPorId(id);

        // Validar que ningún otro género tenga el mismo título
        generoRepositorio.findByTitulo(genero.getTitulo())
                .filter(g -> !g.getId().equals(id))
                .ifPresent(g -> {
                    throw new AlmacenExcepcion(AppConstants.GENERO_EXISTE);
                });

        existente.setTitulo(genero.getTitulo());
        return generoRepositorio.save(existente);
    }

    @Override
    public Genero eliminar(Integer id) {

        Genero existente = obtenerPorId(id);
        generoRepositorio.delete(existente);

        return existente;
    }

}
