package es.cipfpmislata.prueba2daw2627.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookDTO {

    private Long id;

    @NotBlank(message = "El título del libro es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String description;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20, message = "El ISBN no puede superar los 20 caracteres")
    private String isbn;

    @NotNull(message = "El autor es obligatorio")
    private AuthorDTO author;

    public BookDTO() {
    }

    public BookDTO(Long id, String title, String description, String isbn, AuthorDTO author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isbn = isbn;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }
}
