package edu.eci.arsw.bidify.security.dto;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUsuario {
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String password;
}
