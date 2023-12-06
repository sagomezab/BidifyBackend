package edu.eci.arsw.bidify.service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.bidify.dto.MessageDto;
import edu.eci.arsw.bidify.dto.SubastaConcurrente;
import edu.eci.arsw.bidify.model.Producto;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.model.Usuario;
import edu.eci.arsw.bidify.repository.SubastaRepository;


import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

@Service
@Data
@Transactional
public class SubastaService{
    @Autowired
    private SubastaRepository subastaRepository;
    @Autowired
    private UsuarioService usuarioService;

    public Subasta addSubasta(Subasta Subasta) {    
        return subastaRepository.save(Subasta);
    }
    
    public List<Subasta> findAllSubastas(){
        return subastaRepository.findAll();
    }
    
    public synchronized Optional<Subasta> getSubastaById(int id){        
        return subastaRepository.findById(id);
    }
    public Optional<Subasta> getSubastaByProducto(Producto producto){        
        return subastaRepository.findByProducto(producto);
    }
    
    public Optional<Subasta> findBySubastador(Usuario subastador){
        return subastaRepository.findBySubastador(subastador);
    }

    public void save(Subasta subasta){
        subastaRepository.save(subasta);
    }
    
    public void finalizarSubasta(int subastaId) {
        Optional<Subasta> subastaOptional = subastaRepository.findById(subastaId);
        Subasta subastaActual = subastaOptional.get();
        subastaActual.setEstado(false);
        subastaRepository.save(subastaActual);
        
    }

    public void iniciarSubasta(int subastaId){
        Optional<Subasta> subastaOptional = subastaRepository.findById(subastaId);
        Subasta subastaActual = subastaOptional.get();
        subastaActual.setEstado(true);
        subastaRepository.save(subastaActual);
        SubastaConcurrente nuevaSubasta = new SubastaConcurrente(subastaActual, this, usuarioService);
        nuevaSubasta.setIdSubasta(subastaId);
        nuevaSubasta.start();
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
            return new ArrayList<>(); 
        }
    }
    public Subasta addMessageAndProcessBid(MessageDto messageDto, int subastaId) {
        Optional<Subasta> subastaOptional = subastaRepository.findById(subastaId);
        if (subastaOptional.isPresent()) {
            Subasta subastaActual = subastaOptional.get();
            subastaActual.addMessage(messageDto);
            subastaRepository.save(subastaActual);
            return subastaActual;
        } else {
            return null;
        }
    }
}
