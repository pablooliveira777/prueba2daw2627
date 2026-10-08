package es.cipfpmislata.prueba2daw2627.persistence.dao;

import es.cipfpmislata.prueba2daw2627.domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    Book update(Book book);

    void deleteById(Long id);

    boolean existsById(Long id);

    long count();
}
