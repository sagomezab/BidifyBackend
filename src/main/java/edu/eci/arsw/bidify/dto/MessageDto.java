package edu.eci.arsw.bidify.dto;
import java.util.Date;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

@Embeddable
@Data
public class MessageDto {
    
    @NotBlank
    private String senderEmail;
    @NotBlank
    private Date time = new Date(System.currentTimeMillis());
    @NotBlank
    private String replymessage;

    public MessageDto() {
    }

    public MessageDto(@NotBlank String senderEmail, @NotBlank Date time, @NotBlank String replymessage) {
        this.senderEmail = senderEmail;
        this.time = time;
        this.replymessage = replymessage;
    }
}
