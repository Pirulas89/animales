package com.hibernate.model;


import java.util.List;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Medicina")
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Solo usa lo que marquemos
public class Medicina {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String descripcion;
  
    @ManyToMany(mappedBy = "medicinas")
    private List<Animal> animales;
   
}