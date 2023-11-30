package edu.eci.arsw.bidify.controller;
import java.math.BigDecimal;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import edu.eci.arsw.bidify.service.SubastaService;
import edu.eci.arsw.bidify.dto.MessageDto;
import edu.eci.arsw.bidify.dto.SubastaDto;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.model.Usuario;

@RestController
@RequestMapping("/subasta")
@CrossOrigin(origins = "*")
public class SubastaController{
    @Autowired
    private SubastaService subastaService;

    @PostMapping
    public ResponseEntity<Subasta> createSubasta(@RequestBody Subasta subasta) {
        Subasta newSubasta = subastaService.addSubasta(subasta);
        return new ResponseEntity<>(newSubasta, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Subasta>> getAllSubastas() {
        List<Subasta> subastas = subastaService.findAllSubastas();
        return new ResponseEntity<>(subastas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subasta> getSubastaById(@PathVariable int id) {
        Optional<Subasta> subasta = subastaService.getSubastaById(id);
        if (subasta.isPresent()) {
            return new ResponseEntity<>(subasta.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{subastaId}/messages")
    public ResponseEntity<List<MessageDto>> getMessageList(@PathVariable int subastaId) {
        List<MessageDto> messageList = subastaService.getMessageList(subastaId);

        if (messageList != null) {
            return new ResponseEntity<>(messageList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{subastaId}/pujas")
    public ResponseEntity<Subasta> recibirPuja(@PathVariable int subastaId,@RequestBody Usuario usuario, @RequestBody BigDecimal oferta) {
        Subasta subasta = subastaService.getSubastaById(subastaId).orElse(null);
        
        if (subasta != null && subasta.isEstado()) {
            subastaService.recibirPuja(usuario, oferta);

            return new ResponseEntity<>(subasta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }    
    
    
    
    
    

}
