package com.atuantes.mentes.user.infraestructure.persistence.repository;

import com.atuantes.mentes.user.domain.entity.User;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<User, UUID> {
    @Query("INSERT INTO users.users (full_name, document, email, phone, birthdate, category) " +
            "VALUES (:fullName, :document, :email, :phone, :birthdate, :category) RETURNING *")
    User save(String fullName, String document, String email, String phone, LocalDate birthdate, String category);

    @Query("SELECT id, active, full_name, document, email, phone, birthdate, category, created_at, updated_at " +
            "FROM users.users WHERE document = :document")
    Optional<User> findByDocument(String document);

}
