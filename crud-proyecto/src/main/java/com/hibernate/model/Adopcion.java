package com.hibernate.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"cliente", "animal"})
@Entity
@Table(name = "Adopcion")
public class Adopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotNull(message = "La fecha no puede estar vacía")
    private LocalDate fecha;

    @Size(max = 400, message = "Las observaciones no pueden superar 400 caracteres")
    private String observaciones;

    @NotNull(message = "El cliente no puede estar vacío")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El animal no puede estar vacío")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_animal", nullable = false)
    private Animal animal;
}