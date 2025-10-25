-- Cria o enum para categoria no schema 'users'
CREATE TYPE users.category_enum AS ENUM ('FATHER', 'MOTHER', 'SON', 'DAUGHTER', 'GRANDFATHER', 'GRANDMOTHER', 'OTHER');