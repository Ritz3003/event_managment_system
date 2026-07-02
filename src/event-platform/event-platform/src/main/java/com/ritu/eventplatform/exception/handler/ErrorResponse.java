package com.ritu.eventplatform.exception.handler;

import java.time.LocalDateTime;

public record ErrorResponse(

        String message,
        String errorCode,
        LocalDateTime timestamp

) {}