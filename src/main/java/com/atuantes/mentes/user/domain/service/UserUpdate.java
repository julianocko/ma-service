package com.atuantes.mentes.user.domain.service;

import com.atuantes.mentes.user.domain.entity.User;

import java.util.UUID;

public interface UserUpdate {
    User update(User user, UUID transactionId);
}
