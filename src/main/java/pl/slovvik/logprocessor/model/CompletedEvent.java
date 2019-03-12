package pl.slovvik.logprocessor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedEvent {

    @Id
    private String id;

    private long eventDuration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String host;

    //What is the sens of this field if we want only to sore events that are longer than some time
    private boolean alert;

    public CompletedEvent(String id, long eventDuration, String type, String host) {
        this.id = id;
        this.eventDuration = eventDuration;
        this.type = type;
        this.host = host;
    }
}
