package com.atuantes.mentes.user.domain.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LogMessage {

    LOG_ERROR("Exception: [{}] - Exception message: [{} - {}]"),
    LOG_START_CONTROLLER("START - controller - {} - Transaction ID: {}"),
    LOG_END_CONTROLLER("END - controller - {} - Transaction ID: {}"),
    LOG_START_USE_CASE(" start - use case - {} - Transaction ID: {}"),
    LOG_END_USE_CASE(" end - use case - {} - Transaction ID: {}"),
    LOG_START_SERVICE("   start - service - {} - Transaction ID: {}"),
    LOG_END_SERVICE("   end - service - {} - Transaction ID: {}"),
    LOG_START_REPOSITORY("   start - repository - {} - Transaction ID: {}"),
    LOG_END_REPOSITORY("   end - repository - {} - Transaction ID: {}"),
    ;

    private final String message;

}
