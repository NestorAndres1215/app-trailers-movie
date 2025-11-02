package pe.cinema.util;
public class AppConstants {

    private AppConstants() {} // Evitar instanciación

    // ======= Mensajes generales =======
    public static final String ID_INVALIDO = "ID inválido: %d";
    public static final String ENTIDAD_NO_ENCONTRADA = "%s no encontrada con ID: %d";
    public static final String ENTIDAD_NULL = "El objeto %s no puede ser null";

    // ======= Películas =======
    public static final String PELICULA_NO_ENCONTRADA = "Película no encontrada con ID: %d";
    public static final String PELICULA_ID_INVALIDO = "ID de película inválido";
    public static final String PELICULA_NULL = "La película no puede ser null";
    public static final String PELICULA_TITULO_OBLIGATORIO = "El título de la película es obligatorio";
    public static final String PELICULA_PORTADA_OBLIGATORIA = "La portada de la película es obligatoria";
    public static final String PELICULA_GENEROS_OBLIGATORIOS = "Debe seleccionar al menos un género";
    public static final String PELICULA_TITULO_BUSQUEDA = "Debe proporcionar un título para buscar";
    public static final String PELICULA_FECHA_BUSQUEDA = "Debe proporcionar una fecha de estreno para buscar";
    public static final String PELICULA_GENERO_BUSQUEDA = "Debe proporcionar al menos un género para buscar";

    // ======= Géneros =======
    public static final String GENERO_NO_ENCONTRADO = "Género no encontrado con ID: %d";
    public static final String GENERO_NO_ENCONTRADO_TITULO = "Género no encontrado con título: %s";
    public static final String GENERO_EXISTE = "El género ya existe: %s";
    public static final String GENERO_NULL = "El género no puede ser null";
    public static final String GENERO_TITULO_OBLIGATORIO = "El título del género es obligatorio";
    // Almacenamiento de archivos
    public static final String ARCHIVO_VACIO = "No se puede almacenar un archivo vacío o nulo";
    public static final String NOMBRE_ARCHIVO_INVALIDO = "Nombre de archivo inválido: %s";
    public static final String ERROR_ALMACENAR_ARCHIVO = "Error al almacenar el archivo %s";
    public static final String ERROR_ELIMINAR_ARCHIVO = "Error al eliminar el archivo: %s";
    public static final String NOMBRE_ARCHIVO_OBLIGATORIO = "El nombre del archivo no puede ser vacío";
    public static final String ARCHIVO_NO_ENCONTRADO = "No se pudo encontrar el archivo: %s";
    public static final String ERROR_INICIALIZAR_ALMACEN = "Error al inicializar el almacenamiento de archivos";

}