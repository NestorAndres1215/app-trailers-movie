package pe.cinema.util;
public class AppConstants {

    private AppConstants() {} // Evitar instanciación

    // Mensajes para Género
    public static final String GENERO_NO_ENCONTRADO = "Género no encontrado con ID: %d";
    public static final String GENERO_NO_ENCONTRADO_TITULO = "Género no encontrado con título: %s";
    public static final String GENERO_EXISTE = "El género ya existe: %s";
    // Películas
    public static final String PELICULA_NO_ENCONTRADA = "Película no encontrada con ID: %d";
    public static final String PELICULA_ID_INVALIDO = "ID de película inválido";
    public static final String PELICULA_NULL = "La película no puede ser null";
    public static final String PELICULA_TITULO_OBLIGATORIO = "El título de la película es obligatorio";
    public static final String PELICULA_PORTADA_OBLIGATORIA = "La portada de la película es obligatoria";
    public static final String PELICULA_GENEROS_OBLIGATORIOS = "Debe seleccionar al menos un género";
}