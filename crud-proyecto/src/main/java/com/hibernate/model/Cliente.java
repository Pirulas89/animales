package com.hibernate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "nombre")
	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String nombre;
    
    @Column(name = "dni")
	@NotBlank(message = "El dni no puede estar vacío")
	@Size(min = 3, message = "El dni debe tener al menos 3 caracteres")
    private String dni;
    
    @Column(name = "telefono")
	@Min(value = 0, message = "El telefono no puede ser negativo")
	@Max(value = 30, message = "Telefono no válido")
    private int telefono;
    
    @Column(name = "gmail")
	@NotBlank(message = "El gmail no puede estar vacío")
	@Size(min = 3, message = "El gmail debe tener al menos 3 caracteres")
    private String gmail; 
}
