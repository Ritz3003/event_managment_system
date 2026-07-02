package com.ritu.eventplatform.service;

public interface EmailService {

    void sendRegistrationEmail(
            String to,
            String eventName,
            String ticketNumber
    );

    void sendCancellationEmail(
            String to,
            String eventName
    );

    void sendWaitlistPromotionEmail(
            String to,
            String eventName,
            String ticketNumber
    );
}