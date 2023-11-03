package edu.eci.arsw.bidify.security.dto;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
@Data
public class NuevoUsuario {
    @NotBlank
    private String nombre;
    @NotBlank
    private String nombreUsuario;
    @Email
    private String email;
    @NotBlank
    private String password;
    private Set<String> roles = new HashSet<>();
}
