-- Cria a tabela 'user' no schema 'users'
CREATE TABLE users."users" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    active boolean not null default true,-- ID único gerado automaticamente
    full_name VARCHAR(255) NOT NULL,  -- Nome completo do usuário
    document VARCHAR(14) NOT NULL UNIQUE,  -- CPF único (formato: XXX.XXX.XXX-XX)
    email VARCHAR(255) NOT NULL UNIQUE,  -- Email único
    phone VARCHAR(20) NOT NULL,  -- Telefone (ex.: +55 (XX) XXXXX-XXXX)
    birthdate DATE NOT NULL,  -- Data de nascimento
    category text NOT NULL,  -- Categoria usando o enum criado
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Índices adicionais para performance
CREATE INDEX idx_user_document ON users."users" (document);
CREATE INDEX idx_user_email ON users."users" (email);

-- Função para atualizar updated_at
CREATE OR REPLACE FUNCTION users.update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger na tabela 'user'
CREATE TRIGGER trg_update_user_updated_at
BEFORE UPDATE ON users."users"
FOR EACH ROW
EXECUTE FUNCTION users.update_updated_at();