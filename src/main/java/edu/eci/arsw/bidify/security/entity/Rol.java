package edu.eci.arsw.bidify.security.entity;
import edu.eci.arsw.bidify.security.enums.RolNombre;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.Data;
@Data
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolNombre rolNombre;

    public Rol() {
    }

    public Rol(@NotNull RolNombre rolNombre) {
        this.rolNombre = rolNombre;
    }
}
