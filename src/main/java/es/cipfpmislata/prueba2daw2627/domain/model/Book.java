package es.cipfpmislata.prueba2daw2627.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class Book {

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
    private Author author;

    public Book() {
    }

    public Book(Long id, String title, String description, String isbn, Author author) {
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isbn='" + isbn + '\'' +
                ", author=" + author +
                '}';
    }
}
