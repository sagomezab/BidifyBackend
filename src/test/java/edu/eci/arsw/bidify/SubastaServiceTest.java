package edu.eci.arsw.bidify;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import edu.eci.arsw.bidify.dto.MessageDto;
import edu.eci.arsw.bidify.model.Subasta;
import edu.eci.arsw.bidify.model.Usuario;
import edu.eci.arsw.bidify.repository.SubastaRepository;
import edu.eci.arsw.bidify.service.SubastaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class SubastaServiceTest {

    @Mock
    private SubastaRepository subastaRepository;

    @InjectMocks
    private SubastaService subastaService;

    @Test
    public void testAddSubasta() {
        // Arrange
        Subasta subasta = new Subasta();
        when(subastaRepository.save(subasta)).thenReturn(subasta);
        // Act
        Subasta result = subastaService.addSubasta(subasta);
        // Assert
        assertEquals(subasta, result);
    }


    @Test
    public void testFinalizarSubasta() {
        // Arrange
        int subastaId = 1;
        Optional<Subasta> subastaOptional = Optional.of(new Subasta());
        when(subastaRepository.findById(subastaId)).thenReturn(subastaOptional);
        // Act
        subastaService.finalizarSubasta(subastaId);
        // Assert
    }

    @Test
    public void testAddMessage() {
        // Arrange
        MessageDto messageDto = new MessageDto();
        int subastaId = 1;
        Optional<Subasta> subastaOptional = Optional.of(new Subasta());
        when(subastaRepository.findById(subastaId)).thenReturn(subastaOptional);
        // Act
        Subasta result = subastaService.addMessage(messageDto, subastaId);
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetMessageList() {
        // Arrange
        int subastaId = 1;
        Optional<Subasta> subastaOptional = Optional.of(new Subasta());
        when(subastaRepository.findById(subastaId)).thenReturn(subastaOptional);
        // Act
        List<MessageDto> result = subastaService.getMessageList(subastaId);
        // Assert
        assertNotNull(result);
    }
}

