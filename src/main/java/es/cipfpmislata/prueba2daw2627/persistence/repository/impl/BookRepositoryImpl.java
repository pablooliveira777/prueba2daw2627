package es.cipfpmislata.prueba2daw2627.persistence.repository.impl;

import es.cipfpmislata.prueba2daw2627.domain.model.Book;
import es.cipfpmislata.prueba2daw2627.domain.repository.BookRepository;
import es.cipfpmislata.prueba2daw2627.persistence.dao.BookDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final BookDao bookDao;

    public BookRepositoryImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> findAll() {
        return bookDao.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookDao.findById(id);
    }

    @Override
    public Book save(Book book) {
        return bookDao.save(book);
    }

    @Override
    public Book update(Book book) {
        return bookDao.update(book);
    }

    @Override
    public void deleteById(Long id) {
        bookDao.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return bookDao.existsById(id);
    }

    @Override
    public long count() {
        return bookDao.count();
    }
}
