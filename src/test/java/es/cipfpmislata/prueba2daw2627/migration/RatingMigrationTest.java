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
        // Se comprueba que la version 4 esta REGISTRADA (no que sea la ultima),
        // para que el test siga valido si mas adelante se anade una V5.
        Integer applied = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM \"flyway_schema_history\" WHERE \"version\" = '4'", Integer.class);

        assertThat(applied).isEqualTo(1);
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
