package com.atuantes.mentes.user.domain.service;

import java.util.UUID;

public interface UserDelete {
    void deleteByDocument(String document, UUID transactionId);
}
