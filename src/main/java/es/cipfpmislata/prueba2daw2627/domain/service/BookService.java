package es.cipfpmislata.prueba2daw2627.domain.service;

import es.cipfpmislata.prueba2daw2627.domain.dto.BookDTO;

import java.util.List;

public interface BookService {

    List<BookDTO> findAll();

    BookDTO findById(Long id);

    BookDTO save(BookDTO bookDTO);

    BookDTO update(Long id, BookDTO bookDTO);

    void deleteById(Long id);
}
