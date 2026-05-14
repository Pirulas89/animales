package com.hibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Cliente")
public class Cliente {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}[A-Za-z]", message = "El DNI debe tener 8 números y una letra")
    private String dni;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "[679]\\d{8}", message = "El teléfono debe tener 9 dígitos y empezar por 6, 7 o 9")
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Pattern(regexp = "^[\\w.+\\-]+@[a-zA-Z0-9\\-]+\\.(com|es)$", message = "El email debe terminar en .com o .es")
    private String gmail;

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Pattern(regexp = "\\d+", message = "El número de calle solo puede contener dígitos")
    private String numero;

    @NotBlank(message = "La ciudad es obligatoria")
    @Pattern(regexp = "[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+", message = "La ciudad solo puede contener letras y espacios")
    private String ciudad;

    @NotBlank(message = "El CP es obligatorio")
    @Pattern(regexp = "\\d{5}", message = "El código postal debe tener exactamente 5 dígitos")
    private String cp;


    @Override
    public String toString() {
        return nombre;
    }
}