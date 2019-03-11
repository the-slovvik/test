package pl.slovvik.logprocessor.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Event {

    @Id
    private String id;

    private Timestamp started;
    private Timestamp finished;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String host;

    public Event(Timestamp started, Timestamp finished) {
        this.started = started;
        this.finished = finished;
    }

    public Event(String id, Timestamp started, Timestamp finished) {
        this.id = id;
        this.started = started;
        this.finished = finished;
    }

    public Event(String id, Timestamp started) {
        this.id = id;
        this.started = started;
        this.finished = finished;
    }

    public Event(Event event, Timestamp finished) {
        this.id = event.id;
        this.started = event.started;
        this.finished = finished;
    }
}
