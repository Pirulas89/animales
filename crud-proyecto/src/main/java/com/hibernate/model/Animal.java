package com.hibernate.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
	
	// @jakarta.persistence.Lob
	 //@Column(name = "foto", columnDefinition = "LONGBLOB") // LONGBLOB para MySQL
	// private byte[] foto;
	 
	 @Column(name = "foto")
	 @NotBlank(message = "El url no puede estar vacío")
	@Size(min = 3, message = "El url debe tener al menos 3 caracteres")// LONGBLOB para MySQL
	 private String foto;
    
    
	 
	 @Column(name = "estado")
	 @NotBlank(message = "El estado no puede estar vacío")
	@Size(min = 3, message = "El estado debe tener al menos 3 caracteres")// LONGBLOB para MySQL
    private String estado;

    // Relación con Medicinas (Tabla intermedia Animal_Medicina)
	// Dentro de Animal.java

	// Elimina el antiguo @ManyToMany y cámbialo por esto:
	@OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Tratamiento> tratamientos;

	
}
