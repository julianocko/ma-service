package com.atuantes.mentes.user.domain.service;

import com.atuantes.mentes.user.domain.entity.User;

import java.util.UUID;

public interface FindUserByDocument {
    User execute(String document, UUID transactionId);
}
