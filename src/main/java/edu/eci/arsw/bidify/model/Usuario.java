package edu.eci.arsw.bidify.model;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userName;
    @NotNull
    private String nombre;
    @NotNull
    private String email;
    private String password;

    public Usuario(String userName, String nombre, String email, String password){
        this.userName=userName;
        this.nombre=nombre;
        this.email=email;
        this.password=password;
    }
    
}
