package pe.cinema.entity;




import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "genero")
@Data // Genera getters, setters, toString, equals, hashCode
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los campos
@Builder // Para usar patrón builder
public class Genero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
	@Column(name = "id_genero")
	private Integer id;

	@Column(nullable = false, unique = true) // único y obligatorio
	@NotBlank(message = "El título del género no puede estar vacío")
	private String titulo;

}