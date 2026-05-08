package com.hibernate.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "Adopcion")
public class Adopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


	@Column(name = "fecha")
	@NotBlank(message = "La fecha no puede estar vacío")
	@Size(min = 3, message = "La fecha debe tener al menos 3 caracteres")
    private Date fecha;

	@Column(name = "observaciones")
	@NotBlank(message = "El campo observaciones no puede estar vacío")
	@Size(min = 3, message = "El campo observaciones debe tener al menos 3 caracteres")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_animal")
    private Animal animal;
}
