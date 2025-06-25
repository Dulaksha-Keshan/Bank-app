package data.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Dao <T,Id> {
    Map<Id,T> getAll();
    T create(T entity );
    Optional<T> getOne(Id id);
    T update(T entity);
    void delete(Id id);

}
