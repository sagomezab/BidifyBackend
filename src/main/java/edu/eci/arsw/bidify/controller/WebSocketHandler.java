package edu.eci.arsw.bidify.controller;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import edu.eci.arsw.bidify.dto.MessageDto;
import edu.eci.arsw.bidify.dto.SubastaDto;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.model.Usuario;
import edu.eci.arsw.bidify.service.SubastaService;

@Controller
public class WebSocketHandler extends StompSessionHandlerAdapter{
    @Autowired
    SimpMessagingTemplate msgt;
    @Autowired
    private SubastaService subastaService;

    @MessageMapping("/{subastaId}/pujas")
    public ResponseEntity<Subasta> recibirPuja(@PathVariable int subastaId,@RequestBody Usuario usuario, @RequestBody BigDecimal oferta) {
        Subasta subasta = subastaService.getSubastaById(subastaId).orElse(null);
        
        if (subasta != null && subasta.isEstado()) {
            subastaService.recibirPuja(usuario, oferta);
            msgt.convertAndSend("/topic/subasta/" + subastaId + "/puja", subasta);
            return new ResponseEntity<>(subasta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @MessageMapping("/{subastaId}/messages")
    public ResponseEntity<Subasta> addMessageToSubasta(@DestinationVariable int subastaId, @RequestBody MessageDto messageDto) {
        
        Subasta subasta = subastaService.addMessage(messageDto, subastaId);

        if (subasta != null) {
            msgt.convertAndSend("/topic/subasta/" + subastaId + "/messages", subasta);
            return new ResponseEntity<>(subasta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @MessageMapping("/{subastaId}/finalizar")
    public ResponseEntity<Subasta> finalizarSubasta(@PathVariable int subastaId) {
        Optional<Subasta> subastaOptional = subastaService.getSubastaById(subastaId);
        if (subastaOptional.isPresent()) {
            Subasta subasta = subastaOptional.get();
            subastaService.finalizarSubasta(subastaId);
            msgt.convertAndSend("/topic/subasta/" + subastaId + "/finalizar", subasta);
            return new ResponseEntity<>(subasta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @MessageMapping("/{subastaId}")
    public ResponseEntity<Subasta> actualizarSubasta(@PathVariable("subastaId") int subastaId, @RequestBody SubastaDto subastaDto) {

        Optional<Subasta> subastaOptional = subastaService.getSubastaById(subastaId);
        if (subastaOptional.isPresent()) {
            Subasta subasta = subastaOptional.get();
            subasta.setId(subastaId);
            subasta.setEstado(subastaDto.isEstado());
            subasta.setGanador(subastaDto.getGanador());
            subasta.setMessageList(subastaDto.getMessageList());
            subasta.setOferentes(subastaDto.getOferentes());
            subasta.setPrecioFinal(subastaDto.getPrecioFinal());
            subastaService.addSubasta(subasta);
            msgt.convertAndSend("/topic/subasta/" + subastaId + "/actualizacion", subasta);
            return new ResponseEntity<>(subasta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
}
