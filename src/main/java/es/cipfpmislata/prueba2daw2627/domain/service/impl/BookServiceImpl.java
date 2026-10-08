package es.cipfpmislata.prueba2daw2627.domain.service.impl;

import es.cipfpmislata.prueba2daw2627.domain.dto.BookDTO;
import es.cipfpmislata.prueba2daw2627.domain.exception.NotFoundException;
import es.cipfpmislata.prueba2daw2627.domain.mapper.AuthorMapper;
import es.cipfpmislata.prueba2daw2627.domain.mapper.BookMapper;
import es.cipfpmislata.prueba2daw2627.domain.model.Book;
import es.cipfpmislata.prueba2daw2627.domain.repository.BookRepository;
import es.cipfpmislata.prueba2daw2627.domain.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        return BookMapper.toDTO(book);
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        Book book = BookMapper.toEntity(bookDTO);
        book.setId(null);
        Book savedBook = bookRepository.save(book);
        return BookMapper.toDTO(savedBook);
    }

    @Override
    public BookDTO update(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book", id));
        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setDescription(bookDTO.getDescription());
        existingBook.setIsbn(bookDTO.getIsbn());
        if (bookDTO.getAuthor() != null) {
            existingBook.setAuthor(AuthorMapper.toEntity(bookDTO.getAuthor()));
        }
        Book updatedBook = bookRepository.update(existingBook);
        return BookMapper.toDTO(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Book", id);
        }
        bookRepository.deleteById(id);
    }
}
