package edu.eci.arsw.bidify.model;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import edu.eci.arsw.bidify.dto.MessageDto;

import lombok.Data;

@Data

@Entity
public class Subasta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @OneToOne 
    private Usuario subastador;
    @OneToOne
    private Producto producto;
    @NotNull
    private BigDecimal precioInicial;
    @NotNull
    private boolean estado;
    @NotNull
    private int cantidadDeOfertantes;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "subasta_usuario", joinColumns = @JoinColumn(name = "subasta_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> oferentes = new HashSet<>();
    private BigDecimal precioFinal;
    @OneToOne
    private Usuario ganador;
    @ElementCollection
    private List<MessageDto> messageList = new ArrayList<>();

    public Subasta(){
        this.messageList = new ArrayList<>();
    }
    public Subasta( Producto producto, BigDecimal precioInicial, boolean estado,
                   int cantidadDeOfertantes){
        
        this.producto = producto;
        this.precioInicial = precioInicial;
        this.estado = estado;
        this.cantidadDeOfertantes = cantidadDeOfertantes;
        this.messageList = new ArrayList<>();
        
    }
    public void addMessage(MessageDto message) {
        messageList.add(message);
        
    }

}
