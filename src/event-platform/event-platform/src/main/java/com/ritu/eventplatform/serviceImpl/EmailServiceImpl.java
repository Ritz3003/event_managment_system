package com.ritu.eventplatform.serviceImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ritu.eventplatform.service.EmailService;

import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServiceImpl implements EmailService {

	
	//Java MailSender is an interface provided by Spring Framework that defines a contract for sending emails. 
		//It provides methods for sending simple text emails, HTML emails, and emails with attachments. 
		//The JavaMailSender interface is part of the Spring Framework's mail support module and is typically used in conjunction with the JavaMail API to 
		//send emails from a Spring application.
    private final JavaMailSender mailSender;

    
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

	@Override
	public void sendRegistrationEmail(
	        String to,
	        String eventName,
	        String ticketNumber) {

	    SimpleMailMessage message = new SimpleMailMessage();

	    message.setFrom(fromEmail);

	    message.setTo(to);

	    message.setSubject("Event Registration Successful");

	    message.setText(
	            "Hello,\n\n" +

	            "Your registration for " + eventName +
	            " was successful.\n\n" +

	            "Ticket Number: " + ticketNumber +

	            "\n\nSee you at the event!"
	    );

	    try {
	        mailSender.send(message);
	    } catch (Exception ex) {
	        System.err.println("Failed to send registration email: " + ex.getMessage());
	    }
	        
	}

	@Override
	@Async
	public void sendCancellationEmail(
	        String to,
	        String eventName) {

	    try {

	    	// Plain text only
//	        SimpleMailMessage message =
//	                new SimpleMailMessage();
	    	
	    	MimeMessage message =
	    	        mailSender.createMimeMessage();

	    	MimeMessageHelper helper =
	    	        new MimeMessageHelper(message, true);

	        message.setFrom(fromEmail);

	        message.setTo(to);

	        message.setSubject("Event Registration Cancelled");

	        message.setText(
	                "Hello,\n\n" +

	                "Your registration for \"" +

	                eventName +

	                "\" has been cancelled successfully.\n\n" +

	                "We're sorry to see you go."
	        );

	        mailSender.send(message);

	    } catch (Exception ex) {

	        ex.printStackTrace();

	    }
	}

	
	@Override
	@Async
	public void sendWaitlistPromotionEmail(
	        String to,
	        String eventName,
	        String ticketNumber) {

	    try {

	        SimpleMailMessage message =
	                new SimpleMailMessage();

	        message.setFrom(fromEmail);

	        message.setTo(to);

	        message.setSubject(
	                "Good News! You're off the waitlist");

	        message.setText(

	                "Congratulations!\n\n" +

	                "A seat became available for " +

	                eventName +

	                ".\n\n" +

	                "Your Ticket Number: " +

	                ticketNumber +

	                "\n\nSee you at the event!"
	        );

	        mailSender.send(message);

	    } catch (Exception ex) {

	        ex.printStackTrace();

	    }
	}
}



	


