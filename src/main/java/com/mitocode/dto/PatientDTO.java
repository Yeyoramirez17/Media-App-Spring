package com.mitocode.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PatientDTO {

    @EqualsAndHashCode.Include
    private Integer idPatient;

    @NotNull
    @Size(min = 3, message = "{firstname.size}")
    private String firstName;

    @NotNull
    @Size(min = 3, message = "{lastname.size}")
    private String lastName;

    @NotNull
    private String dni;
    private String address;

    @Pattern(regexp = "[0-9]+")
    private String phone;

    @Email
    private String email;
    /**
    @Min(value = 1) // Para valores numericos se usa @Min 0 @Max (@Size para cadenas)
    private int age;
     */
}
