package com.atuantes.mentes.user.domain.service;

import com.atuantes.mentes.user.domain.entity.User;

import java.util.UUID;

public interface UserInsert {
    User insert(User user, UUID transactionId);
}
