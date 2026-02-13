package pe.cinema.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;
import lombok.*;

@Entity
@Table(name = "pelicula")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula", nullable = false, updatable = false)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @Column(nullable = false, length = 2000)
    @NotBlank(message = "La sinopsis no puede estar vacía")
    private String sinopsis;

    @Column(nullable = false)
    @NotNull(message = "La fecha de estreno es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaEstreno;

    @Column(nullable = false)
    @NotBlank(message = "El ID de YouTube del tráiler es obligatorio")
    private String youtubeTrailerId;

    @Column(nullable = false)
    @NotBlank(message = "La ruta de la portada es obligatoria")
    private String rutaPortada;

    @NotEmpty(message = "Debe seleccionar al menos un género")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "genero_pelicula",
            joinColumns = @JoinColumn(name = "id_pelicula"),
            inverseJoinColumns = @JoinColumn(name = "id_genero")
    )
    private List<Genero> generos;

    @Transient
    private MultipartFile portada;
}
