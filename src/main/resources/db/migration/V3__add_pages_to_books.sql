-- Rama A8 (Usuario A): numero de paginas de cada libro.
-- Columna opcional (NULL), coherente con las columnas opcionales ya existentes
-- en el proyecto (description en books, nationality en authors).
ALTER TABLE books ADD COLUMN pages INT NULL;
