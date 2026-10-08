package es.cipfpmislata.prueba2daw2627.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Comprueba que la migracion V3 de la rama A8 se aplica sobre la base de datos
 * de test (H2) y que la tabla books queda con la columna pages.
 */
@SpringBootTest
@ActiveProfiles("test")
class PagesMigrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void flyway_shouldHaveAppliedV3() {
        // Se comprueba que la version 3 esta REGISTRADA (no que sea la ultima):
        // en develop, tras integrar B8, se aplican tambien V1..V4 y el MAX seria "4".
        Integer applied = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM \"flyway_schema_history\" WHERE \"version\" = '3'", Integer.class);

        assertThat(applied).isEqualTo(1);
    }

    @Test
    void booksTable_shouldContainNullablePagesColumn() {
        String nullable = jdbcTemplate.queryForObject(
                "SELECT IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS " +
                        "WHERE UPPER(TABLE_NAME) = 'BOOKS' AND UPPER(COLUMN_NAME) = 'PAGES'",
                String.class);

        assertThat(nullable).isEqualToIgnoringCase("YES");
    }
}
