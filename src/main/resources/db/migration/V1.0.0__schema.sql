-- Cria o schema 'users' se não existir
CREATE SCHEMA IF NOT EXISTS users;

-- Define permissões básicas (ajuste conforme necessário para LGPD e segurança)
GRANT USAGE ON SCHEMA users TO public;  -- Permite uso geral, mas restrinja em produção
GRANT CREATE ON SCHEMA users TO user_application;  -- Substitua 'seu_usuario' pelo user do app