package com.hibernate.model;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name = "Animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Min(value = 0, message = "La edad no puede ser negativa")
    @Max(value = 50, message = "La edad no puede superar los 50 años")
    private int edad;

    @NotBlank(message = "La especie no puede estar vacía")
    @Size(max = 40, message = "La especie no puede superar los 40 caracteres")
    private String especie;

    @NotBlank(message = "La raza no puede estar vacía")
    @Size(max = 40, message = "La raza no puede superar los 40 caracteres")
    private String raza;

    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(regexp = "Disponible|Adoptado|En tratamiento",
             message = "El estado debe ser Disponible, Adoptado o En tratamiento")
    private String estado;

    @Lob
    private byte[] foto;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "Animal_Medicina",
        joinColumns = @JoinColumn(name = "animal_id"),
        inverseJoinColumns = @JoinColumn(name = "medicina_id")
    )
    private List<Medicina> medicinas;

    @Column(name = "medicinaNombre")
    private String medicinaNombre;

    @Column(name = "medicinaTipo")
    private String medicinaTipo;

    @Override
    public String toString() {
        return nombre + " (" + raza + ")";
    }
}