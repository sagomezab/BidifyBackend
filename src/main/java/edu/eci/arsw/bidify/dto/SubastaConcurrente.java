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

    @Autowired
    public SubastaConcurrente(Subasta subasta, SubastaService subastaService, UsuarioService usuarioService) {
        this.subasta = subasta;
        this.subastaService = subastaService;
        this.usuarioService = usuarioService;
        this.idSubasta = subasta.getId();
    }
    public SubastaConcurrente(){

    }
    public void run(){
        subasta = subastaService.getSubastaById(idSubasta).get();
        precioActual = subasta.getPrecioFinal();
        ganador = subasta.getGanador();
        messageList = subasta.getMessageList();
        while(subasta.isEstado()){
            lock.lock();
                try {
                    verificarPujas();
                    Puja puja = pujas.poll();
                    if (puja != null) {
                        if (puja.getOferta().compareTo(precioActual) > 0) {
                            precioActual = puja.getOferta();
                            ganador = puja.getPostor();
                            subasta.setGanador(ganador);
                            subasta.setPrecioFinal(precioActual);
                            subastaService.save(subasta);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            subasta = subastaService.getSubastaById(subasta.getId()).get();
            precioActual = subasta.getPrecioFinal();
            ganador = subasta.getGanador();
            messageList = subasta.getMessageList();
        }
    }
    public synchronized void verificarPujas(){
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
