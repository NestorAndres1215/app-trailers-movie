package pe.cinema.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;
import pe.cinema.repository.GeneroRepositorio;
import pe.cinema.repository.PeliculaRepositorio;
import pe.cinema.service.AlmacenServicio;
import pe.cinema.service.PeliculaServicio;

import java.util.List;

@Service
public class PeliculaServicioImpl implements PeliculaServicio {

    private final PeliculaRepositorio peliculaRepositorio;
    private final GeneroRepositorio generoRepositorio;
    private final AlmacenServicio almacenServicio;


    public PeliculaServicioImpl(PeliculaRepositorio peliculaRepositorio, GeneroRepositorio generoRepositorio, AlmacenServicio almacenServicio) {
        this.peliculaRepositorio = peliculaRepositorio;
        this.generoRepositorio = generoRepositorio;
        this.almacenServicio = almacenServicio;
    }


    @Override
    public Page<Pelicula> listarPeliculas(Pageable pageable) {
        return peliculaRepositorio.findAll(pageable);
    }


    @Override
    public List<Genero> listarGeneros() {
        return generoRepositorio.findAll(Sort.by("titulo"));
    }

    @Override
    public Pelicula guardarPelicula(Pelicula pelicula) {
        validarPelicula(pelicula, true);

        String rutaPortada = almacenServicio.almacenarArchivo(pelicula.getPortada());
        pelicula.setRutaPortada(rutaPortada);

        return peliculaRepositorio.save(pelicula);
    }

    @Override
    public Pelicula obtenerPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de película inválido");
        }

        return peliculaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada con ID: " + id));
    }

    @Override
    public Pelicula actualizarPelicula(Integer id, Pelicula pelicula) {
        validarPelicula(pelicula, false);

        Pelicula peliculaDB = obtenerPorId(id);
        peliculaDB.setTitulo(pelicula.getTitulo());
        peliculaDB.setSinopsis(pelicula.getSinopsis());
        peliculaDB.setFechaEstreno(pelicula.getFechaEstreno());
        peliculaDB.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
        peliculaDB.setGeneros(pelicula.getGeneros());

        if (pelicula.getPortada() != null && !pelicula.getPortada().isEmpty()) {
            almacenServicio.eliminarArchivo(peliculaDB.getRutaPortada());
            String rutaPortada = almacenServicio.almacenarArchivo(pelicula.getPortada());
            peliculaDB.setRutaPortada(rutaPortada);
        }

        return peliculaRepositorio.save(peliculaDB);
    }


    @Override
    public void eliminarPelicula(Integer id) {
        Pelicula pelicula = obtenerPorId(id);
        // Eliminar archivo solo si existe
        if (StringUtils.hasText(pelicula.getRutaPortada())) {
            almacenServicio.eliminarArchivo(pelicula.getRutaPortada());
        }
        peliculaRepositorio.delete(pelicula);
    }


    private void validarPelicula(Pelicula pelicula, boolean esNuevo) {
        if (pelicula == null) {
            throw new IllegalArgumentException("La película no puede ser null");
        }

        if (!StringUtils.hasText(pelicula.getTitulo())) {
            throw new IllegalArgumentException("El título de la película es obligatorio");
        }

        if (esNuevo && (pelicula.getPortada() == null || pelicula.getPortada().isEmpty())) {
            throw new IllegalArgumentException("La portada de la película es obligatoria");
        }

        if (pelicula.getGeneros() == null || pelicula.getGeneros().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un género");
        }
    }
}
