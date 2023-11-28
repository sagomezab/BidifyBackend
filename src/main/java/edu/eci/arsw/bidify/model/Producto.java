package edu.eci.arsw.bidify.model;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
@Data
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String nombre;
    @NotNull
    private float precio;
    @NotNull
    private String img;
    
    @ManyToOne
    @JsonIgnore
    private Usuario usuario;

    public Producto(){
    }

    public Producto(String nombre, float precio, String img) {
        this.nombre = nombre;
        this.precio = precio;
        this.img = img;
    }

    
}
