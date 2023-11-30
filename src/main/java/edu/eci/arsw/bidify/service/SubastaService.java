package edu.eci.arsw.bidify.service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.bidify.dto.MessageDto;
import edu.eci.arsw.bidify.dto.Puja;
import edu.eci.arsw.bidify.model.Producto;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.model.Usuario;
import edu.eci.arsw.bidify.repository.SubastaRepository;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

@Service
@Data
@Transactional
public class SubastaService implements Runnable{
    @Autowired
    private SubastaRepository subastaRepository;
    private final Lock lock = new ReentrantLock();
    private boolean activa = true;
    private BigDecimal precioActual = BigDecimal.ZERO;
    private Usuario ganador;
    private Queue<Puja> pujas = new PriorityQueue<>();

    public Subasta addSubasta(Subasta Subasta) {    
        return subastaRepository.save(Subasta);
    }
    
    public List<Subasta> findAllSubastas(){
        return subastaRepository.findAll();
    }
    
    public Optional<Subasta> getSubastaById(int id){        
        return subastaRepository.findById(id);
    }
    public Optional<Subasta> getSubastaByProducto(Producto producto){        
        return subastaRepository.findByProducto(producto);
    }

    @Override
    public void run() {
        while (activa) {
            // Procesar ofertas en cola de pujas
            lock.lock();
            try {
                Puja puja = pujas.poll();
                if (puja != null) {
                    if (puja.getOferta().compareTo(precioActual) > 0) {
                        precioActual = puja.getOferta();
                        ganador = puja.getPostor();
                        
                        // Notificar aquí
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        // Finalizar la subasta
        changeEstado();
        // Notificar finalización
    }
    
    public void recibirPuja(Usuario postor, BigDecimal oferta) {
        lock.lock();
        try {
            if (activa && oferta.compareTo(precioActual) > 0) {
                pujas.offer(new Puja(postor, oferta));
            }
        } finally {
            lock.unlock();
        }
    }
    
    public Optional<Subasta> findBySubastador(Usuario subastador){
        return subastaRepository.findBySubastador(subastador);
    }
    
    public void finalizarSubasta(int subastaId) {
        Optional<Subasta> subastaOptional = subastaRepository.findById(subastaId);
        Subasta subasta = subastaOptional.get();
        subasta.setEstado(false);
        subastaRepository.save(subasta);
        activa = false;
    }

    private void changeEstado() {
        activa = !activa;
    }
    public Subasta addMessage(MessageDto messageDto, int subastaId) {
        Optional<Subasta> subastaOptional = subastaRepository.findById(subastaId);

        if (subastaOptional.isPresent()) {
            Subasta subasta = subastaOptional.get();
            subasta.addMessage(messageDto);
            subastaRepository.save(subasta);
            return subasta;
        } else {
            return null;
        }
    }

    public List<MessageDto> getMessageList(int subastaId) {
        Optional<Subasta> subastaOptional = subastaRepository.findById(subastaId);

        if (subastaOptional.isPresent()) {
            return subastaOptional.get().getMessageList();
        } else {
            return new ArrayList<>(); // O devolver null según tu lógica
        }
    }
}
