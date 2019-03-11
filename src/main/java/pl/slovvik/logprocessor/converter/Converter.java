package pl.slovvik.logprocessor.converter;

import java.util.List;
import java.util.stream.Collectors;

public interface Converter<T, V> {

    V convert(T object);

    default List<V> convert(List<T> objectsList) {
        return objectsList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
