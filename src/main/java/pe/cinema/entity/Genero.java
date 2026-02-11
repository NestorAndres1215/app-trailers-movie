package pe.cinema.entity;




import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "genero")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_genero")
	private Integer id;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "El título del género no puede estar vacío")
	private String titulo;

}