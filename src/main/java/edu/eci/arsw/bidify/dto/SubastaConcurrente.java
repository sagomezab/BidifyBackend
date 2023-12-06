package edu.eci.arsw.bidify.dto;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.model.Usuario;
import edu.eci.arsw.bidify.service.SubastaService;
import edu.eci.arsw.bidify.service.UsuarioService;
import lombok.Data;

@Data
public class SubastaConcurrente extends Thread{
    private Subasta subasta;
    @Autowired
    private SubastaService subastaService;
    private UsuarioService usuarioService; 
    private BigDecimal precioActual = BigDecimal.ZERO;
    private Usuario ganador;
    private final Lock lock = new ReentrantLock();
    private Queue<Puja> pujas = new PriorityQueue<>();
    private List<MessageDto> messageList;
    private int idSubasta;
    
    private final Monitor monitor;

    @Autowired
    public SubastaConcurrente(Subasta subasta, SubastaService subastaService, UsuarioService usuarioService, Monitor monitor) {
        this.subasta = subasta;
        this.subastaService = subastaService;
        this.usuarioService = usuarioService;
        this.idSubasta = subasta.getId();
        this.monitor = monitor;
        
    }
    
    public void run() {
        subasta = subastaService.getSubastaById(idSubasta).get();
        precioActual = subasta.getPrecioFinal();
        ganador = subasta.getGanador();
        messageList = subasta.getMessageList();
        if(!subasta.isEstado()){
            subasta.setEstado(true);
            subastaService.save(subasta);
        }
        System.out.println(subasta.isEstado());
        while (subasta.isEstado()) {
            subasta = subastaService.getSubastaById(idSubasta).get();
            precioActual = subasta.getPrecioFinal();
            ganador = subasta.getGanador();
            messageList = subasta.getMessageList();
            monitor.suspenderHilos();
            System.out.println("vamos a esperar...");
            try {
                monitor.esperarSiSuspendido();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("volvimos!!!");
            verificarPujas();
            Puja puja = pujas.poll();
            if (puja != null) {
                if (puja.getOferta().compareTo(precioActual) > 0) {
                    System.out.println("Nuestro precioActual es:"+ precioActual);
                    precioActual = puja.getOferta();
                    ganador = puja.getPostor();
                    subasta.setGanador(ganador);
                    subasta.setPrecioFinal(precioActual);
                    subastaService.save(subasta);
                }
            }
            subasta = subastaService.getSubastaById(subasta.getId()).get();
            precioActual = subasta.getPrecioFinal();
            ganador = subasta.getGanador();
            messageList = subasta.getMessageList();
                
        }
        
    }
    public void verificarPujas(){
        subasta = subastaService.getSubastaById(subasta.getId()).get();
        List<MessageDto> messageList2 = subasta.getMessageList();
        for(int i = 0; i < messageList2.size(); i++){
            MessageDto messageDto = messageList2.get(i);
            Optional<Usuario> usuarioOptional = usuarioService.getUsuarioByUserName(messageDto.getSenderEmail());
            usuarioOptional.ifPresent(usuario -> {
                BigDecimal oferta = new BigDecimal(messageDto.getReplymessage());
                recibirPuja(usuario, oferta);
            });
        }
    }   
    public synchronized void recibirPuja(Usuario postor, BigDecimal oferta) {
        subasta = subastaService.getSubastaById(idSubasta).get();
        precioActual = subasta.getPrecioFinal();
        ganador = subasta.getGanador();
        messageList = subasta.getMessageList();
        lock.lock();
        try {
            if (subasta.isEstado() && oferta.compareTo(precioActual) > 0) {
                pujas.offer(new Puja(postor, oferta));
            }
        } finally {
            lock.unlock();
        }
    }

}
