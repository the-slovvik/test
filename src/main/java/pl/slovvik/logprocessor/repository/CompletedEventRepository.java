package pl.slovvik.logprocessor.repository;

import org.springframework.data.repository.CrudRepository;
import pl.slovvik.logprocessor.model.CompletedEvent;

public interface CompletedEventRepository extends CrudRepository<CompletedEvent, String> {
}
