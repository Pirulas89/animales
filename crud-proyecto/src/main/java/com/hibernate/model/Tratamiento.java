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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@Entity
	@Table(name = "Tratamiento")
public class Tratamiento {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @ManyToOne
	    @JoinColumn(name = "id_animal")
	    private Animal animal;

	    @ManyToOne
	    @JoinColumn(name = "id_medicina")
	    private Medicina medicina;

	    @Column(name = "fecha_tratamiento")
	    @Temporal(TemporalType.DATE) // Para guardar solo la fecha
	    private Date fechaTratamiento;

	    @Column(name = "proxima_cita")
	    @Temporal(TemporalType.DATE) // Ideal para usar con JCalendar
	    private Date proximaCita;

	    @Column(name = "observaciones")
	    private String observaciones;
	}

