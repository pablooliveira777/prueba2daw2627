package es.cipfpmislata.prueba2daw2627.domain.mapper;

import es.cipfpmislata.prueba2daw2627.domain.dto.AuthorDTO;
import es.cipfpmislata.prueba2daw2627.domain.model.Author;

public class AuthorMapper {

    public static AuthorDTO toDTO(Author author) {
        if (author == null) {
            return null;
        }
        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getNationality()
        );
    }

    public static Author toEntity(AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return null;
        }
        return new Author(
                authorDTO.getId(),
                authorDTO.getName(),
                authorDTO.getNationality()
        );
    }
}
