package pe.cinema.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;
import pe.cinema.excepciones.AlmacenExcepcion;
import pe.cinema.repository.GeneroRepository;
import pe.cinema.repository.PeliculaRepository;
import pe.cinema.service.AlmacenService;
import pe.cinema.service.PeliculaService;
import pe.cinema.util.AppConstants;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeliculaServiceImpl implements PeliculaService {

    private final PeliculaRepository peliculaRepositorio;
    private final GeneroRepository generoRepositorio;
    private final AlmacenService almacenServicio;

    @Override
    public Page<Pelicula> listarPeliculas(Pageable pageable) {
        return peliculaRepositorio.findAll(pageable);
    }

    @Override
    public List<Genero> listarGeneros() {
        return generoRepositorio.findAll();
    }

    @Override
    public Pelicula guardarPelicula(Pelicula pelicula) {

        peliculaRepositorio.findByTitulo(pelicula.getTitulo())
                .ifPresent(p -> {
                    throw new AlmacenExcepcion(AppConstants.PELICULA_NO_ENCONTRADA);
                });

        pelicula.getGeneros().forEach(genero -> {
            if (!generoRepositorio.existsById(genero.getId())) {
                throw new AlmacenExcepcion(AppConstants.GENERO_NO_ENCONTRADO);
            }
        });

        if (pelicula.getPortada() == null || pelicula.getPortada().isEmpty()) {
            throw new AlmacenExcepcion(AppConstants.PELICULA_PORTADA_OBLIGATORIA);
        }
        String rutaPortada = almacenServicio.almacenarArchivo(pelicula.getPortada());


        Pelicula peliculaAGuardar = Pelicula.builder()
                .titulo(pelicula.getTitulo())
                .sinopsis(pelicula.getSinopsis())
                .fechaEstreno(pelicula.getFechaEstreno())
                .youtubeTrailerId(pelicula.getYoutubeTrailerId())
                .generos(pelicula.getGeneros())
                .rutaPortada(rutaPortada)
                .portada(pelicula.getPortada())
                .build();

        return peliculaRepositorio.save(peliculaAGuardar);
    }

    @Override
    public Pelicula obtenerPorId(Integer id) {
        return peliculaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.PELICULA_NO_ENCONTRADA, id)));
    }

    @Override
    public Pelicula actualizarPelicula(Integer id, Pelicula pelicula) {

        validarTituloUnico(id, pelicula.getTitulo());

        Pelicula peliculaDB = obtenerPorId(id);

        peliculaDB.setTitulo(pelicula.getTitulo());
        peliculaDB.setSinopsis(pelicula.getSinopsis());
        peliculaDB.setFechaEstreno(pelicula.getFechaEstreno());
        peliculaDB.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
        peliculaDB.setGeneros(pelicula.getGeneros());

        if (pelicula.getPortada() != null && !pelicula.getPortada().isEmpty()) {
            if (StringUtils.hasText(peliculaDB.getRutaPortada())) {
                almacenServicio.eliminarArchivo(peliculaDB.getRutaPortada());
            }
            String rutaPortada = almacenServicio.almacenarArchivo(pelicula.getPortada());
            peliculaDB.setRutaPortada(rutaPortada);
        }

        return peliculaRepositorio.save(peliculaDB);
    }
    private void validarTituloUnico(Integer id, String titulo) {
        Optional<Pelicula> peliculaExistente = peliculaRepositorio.findByTitulo(titulo);
        if (peliculaExistente.isPresent() && !peliculaExistente.get().getId().equals(id)) {
            throw new AlmacenExcepcion(AppConstants.PELICULA_TITULO_EXISTE);
        }
    }


    @Override
    public Pelicula eliminarPelicula(Integer id) {
        Pelicula pelicula = obtenerPorId(id);

        if (StringUtils.hasText(pelicula.getRutaPortada())) {
            almacenServicio.eliminarArchivo(pelicula.getRutaPortada());
        }

        peliculaRepositorio.delete(pelicula);

        return pelicula;
    }

    @Override
    public List<Pelicula> buscarPorTitulo(String titulo) {
        return peliculaRepositorio.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public List<Pelicula> buscarPorFechaEstreno(LocalDate fechaEstreno) {
        return peliculaRepositorio.findByFechaEstreno(fechaEstreno);
    }

    @Override
    public List<Pelicula> buscarPorGeneros(List<Genero> generos) {
        return peliculaRepositorio.findByGenerosIn(generos);
    }

    @Override
    public List<Pelicula> buscarAvanzado(String titulo, LocalDate fechaEstreno, List<Genero> generos) {
        return peliculaRepositorio.findByTituloContainingIgnoreCaseAndFechaEstrenoAndGenerosIn(
                titulo, fechaEstreno, generos);
    }
}
