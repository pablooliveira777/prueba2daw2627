package es.cipfpmislata.prueba2daw2627.domain.mapper;

import es.cipfpmislata.prueba2daw2627.domain.dto.BookDTO;
import es.cipfpmislata.prueba2daw2627.domain.model.Book;

public class BookMapper {

    public static BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getIsbn(),
                AuthorMapper.toDTO(book.getAuthor())
        );
    }

    public static Book toEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        return new Book(
                bookDTO.getId(),
                bookDTO.getTitle(),
                bookDTO.getDescription(),
                bookDTO.getIsbn(),
                AuthorMapper.toEntity(bookDTO.getAuthor())
        );
    }
}
