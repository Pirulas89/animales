package com.hibernate.model;

import java.util.List;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Animal")
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usa lo que marquemos
public class Animal {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
    private int id;

	@Column(name = "nombre")
	@NotBlank(message = "El nombre del animal no puede estar vacío")
	@Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
	private String nombre;
	 
	@Column(name = "edad")
	@Min(value = 0, message = "La edad no puede ser negativa")
	@Max(value = 30, message = "Edad no válida")
	private int edad;
   
	@Column(name = "especie")
	@NotBlank(message = "El nombre de la especie no puede estar vacío")
	@Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
	private String especie;
	
	@Column(name = "raza")
	@NotBlank(message = "El nombre de la raza no puede estar vacío")
	@Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
	private String raza;
	
	 @jakarta.persistence.Lob
	 @Column(name = "foto", columnDefinition = "LONGBLOB") // LONGBLOB para MySQL
	 private byte[] foto;
    
    
    @Column(columnDefinition = "ENUM('Disponible', 'Adoptado', 'En Tratamiento')")
    private String estado;

    // Relación con Medicinas (Tabla intermedia Animal_Medicina)
    @ToString.Exclude // Evita bucle infinito en el log
    @EqualsAndHashCode.Exclude // Evita bucle infinito en comparaciones
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "Animal_Medicina",
        joinColumns = @JoinColumn(name = "id_animal"), // Verifica que el nombre en la BD sea exacto
        inverseJoinColumns = @JoinColumn(name = "id_medicina")
    )
    private List<Medicina> medicinas;
}
