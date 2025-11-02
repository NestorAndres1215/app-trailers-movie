package pe.cinema.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pe.cinema.entity.Genero;
import pe.cinema.entity.Pelicula;
import pe.cinema.repository.GeneroRepositorio;
import pe.cinema.repository.PeliculaRepositorio;
import pe.cinema.util.AppConstants;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PeliculaServicio {

    private final PeliculaRepositorio peliculaRepositorio;
    private final GeneroRepositorio generoRepositorio;
    private final AlmacenServicio almacenServicio;

    /** Listar películas con paginación */
    public Page<Pelicula> listarPeliculas(Pageable pageable) {
        return peliculaRepositorio.findAll(pageable);
    }

    /** Listar todos los géneros ordenados por título */
    public List<Genero> listarGeneros() {
        return generoRepositorio.findAll(Sort.by("titulo"));
    }

    /** Guardar nueva película */
    public Pelicula guardarPelicula(Pelicula pelicula) {
        validarPelicula(pelicula, true);

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

    /** Obtener película por ID */
    public Pelicula obtenerPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(AppConstants.PELICULA_ID_INVALIDO);
        }

        return peliculaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.PELICULA_NO_ENCONTRADA, id)));
    }

    /** Actualizar película existente */
    public Pelicula actualizarPelicula(Integer id, Pelicula pelicula) {
        validarPelicula(pelicula, false);

        Pelicula peliculaDB = obtenerPorId(id);

        peliculaDB.setTitulo(pelicula.getTitulo());
        peliculaDB.setSinopsis(pelicula.getSinopsis());
        peliculaDB.setFechaEstreno(pelicula.getFechaEstreno());
        peliculaDB.setYoutubeTrailerId(pelicula.getYoutubeTrailerId());
        peliculaDB.setGeneros(pelicula.getGeneros());

        if (pelicula.getPortada() != null && !pelicula.getPortada().isEmpty()) {
            // Eliminar portada antigua si existe
            if (StringUtils.hasText(peliculaDB.getRutaPortada())) {
                almacenServicio.eliminarArchivo(peliculaDB.getRutaPortada());
            }
            String rutaPortada = almacenServicio.almacenarArchivo(pelicula.getPortada());
            peliculaDB.setRutaPortada(rutaPortada);
        }

        return peliculaRepositorio.save(peliculaDB);
    }

    /** Eliminar película */
    public void eliminarPelicula(Integer id) {
        Pelicula pelicula = obtenerPorId(id);

        if (StringUtils.hasText(pelicula.getRutaPortada())) {
            almacenServicio.eliminarArchivo(pelicula.getRutaPortada());
        }

        peliculaRepositorio.delete(pelicula);
    }

    /** Validar campos de película */
    private void validarPelicula(Pelicula pelicula, boolean esNuevo) {
        if (pelicula == null) {
            throw new IllegalArgumentException(AppConstants.PELICULA_NULL);
        }

        if (!StringUtils.hasText(pelicula.getTitulo())) {
            throw new IllegalArgumentException(AppConstants.PELICULA_TITULO_OBLIGATORIO);
        }

        if (esNuevo && (pelicula.getPortada() == null || pelicula.getPortada().isEmpty())) {
            throw new IllegalArgumentException(AppConstants.PELICULA_PORTADA_OBLIGATORIA);
        }

        if (pelicula.getGeneros() == null || pelicula.getGeneros().isEmpty()) {
            throw new IllegalArgumentException(AppConstants.PELICULA_GENEROS_OBLIGATORIOS);
        }
    }
    /** Buscar películas por título (contiene) */
    public List<Pelicula> buscarPorTitulo(String titulo) {
        if (!StringUtils.hasText(titulo)) {
            throw new IllegalArgumentException("Debe proporcionar un título para buscar");
        }
        return peliculaRepositorio.findByTituloContainingIgnoreCase(titulo);
    }

    /** Buscar películas por fecha de estreno */
    public List<Pelicula> buscarPorFechaEstreno(LocalDate fechaEstreno) {
        if (fechaEstreno == null) {
            throw new IllegalArgumentException("Debe proporcionar una fecha de estreno");
        }
        return peliculaRepositorio.findByFechaEstreno(fechaEstreno);
    }

    /** Buscar películas por género */
    public List<Pelicula> buscarPorGeneros(List<Genero> generos) {
        if (generos == null || generos.isEmpty()) {
            throw new IllegalArgumentException("Debe proporcionar al menos un género");
        }
        return peliculaRepositorio.findByGenerosIn(generos);
    }

    /** Buscar películas combinando título, fecha y géneros */
    public List<Pelicula> buscarAvanzado(String titulo, LocalDate fechaEstreno, List<Genero> generos) {
        return peliculaRepositorio.findByTituloContainingIgnoreCaseAndFechaEstrenoAndGenerosIn(
                titulo, fechaEstreno, generos);
    }
}
