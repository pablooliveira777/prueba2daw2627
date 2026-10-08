package es.cipfpmislata.prueba2daw2627.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprueba que la migracion V4 de la rama B8 se aplica sobre la base de datos
 * de test (H2) y que la tabla books queda con la columna rating.
 */
@SpringBootTest
@ActiveProfiles("test")
class RatingMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void flyway_shouldHaveAppliedV4() {
        // H2 mayuscula los identificadores sin comillas; Flyway la crea en minusculas.
        String version = jdbcTemplate.queryForObject(
                "SELECT MAX(\"version\") FROM \"flyway_schema_history\"", String.class);

        assertThat(version).isEqualTo("4");
    }

    @Test
    void booksTable_shouldContainNullableRatingColumn() {
        String nullable = jdbcTemplate.queryForObject(
                "SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE UPPER(TABLE_NAME) = 'BOOKS' AND UPPER(COLUMN_NAME) = 'RATING'",
                String.class);

        assertThat(nullable).isEqualToIgnoringCase("YES");
    }
}
