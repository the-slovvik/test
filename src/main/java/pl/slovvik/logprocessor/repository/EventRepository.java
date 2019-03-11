package pl.slovvik.logprocessor.repository;

import org.springframework.data.repository.CrudRepository;
import pl.slovvik.logprocessor.model.Event;

public interface EventRepository extends CrudRepository<Event, String> {
}
