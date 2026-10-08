-- Rama B8 (Usuario B): valoracion (rating) de cada libro.
-- Columna opcional (NULL), coherente con las columnas opcionales ya existentes
-- en el proyecto (description en books, nationality en authors).
ALTER TABLE books ADD COLUMN rating INT NULL;
