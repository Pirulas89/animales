package com.hibernate.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "Medicina")
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usa lo que marquemos
public class Medicina {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "nombre")
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombre;
	
	@Column(name = "descripcion")
	@NotBlank(message = "La descripcion no puede estar vacía")
	@Size(min = 3, message = "La descripcion debe tener al menos 3 caracteres")
    private String descripcion;
	
	@Column(name = "tipoMed")
	@NotBlank(message = "El tipo de medicamento no puede estar vacía")
	@Size(min = 3, max = 255, message = "La descripcion debe tener al menos 3 caracteres y maximo 255")
    private String tipoMed;
	
  
	// Dentro de Medicina.java

	// Elimina el antiguo @ManyToMany(mappedBy = "medicinas") y cámbialo por:
	@OneToMany(mappedBy = "medicina")
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Tratamiento> tratamientos;
   
}