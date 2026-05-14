package com.hibernate.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "Medicina")
public class Medicina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = "El nombre del medicamento no puede estar vacío")
    @Size(max = 40, message = "El nombre no puede superar los 40 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo no puede estar vacío")
    @Size(max = 30, message = "El tipo no puede superar los 30 caracteres")
    private String tipoMed;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = 50, message = "El stock no puede superar las 50 unidades")
    private int stock;

    @ManyToMany(mappedBy = "medicinas", fetch = FetchType.LAZY)
    private List<Animal> animales;

    @Override
    public String toString() {
        return nombre + " (" + tipoMed + ")";
    }
}