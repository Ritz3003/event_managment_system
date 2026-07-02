package com.ritu.eventplatform.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.ritu.eventplatform.dto.CheckInRequest;
import com.ritu.eventplatform.dto.CheckInResponse;
import com.ritu.eventplatform.service.TicketService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/tickets")
public class TicketController {
	
	private final TicketService ticketService;
	
	TicketController(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	@GetMapping("/{ticketNumber}/qrcode")
	public ResponseEntity<byte[]> getQrCode(
	        @PathVariable String ticketNumber){
		
		byte[] qr = ticketService.getQrCode(ticketNumber);
		
		return ResponseEntity.ok()
		        .contentType(MediaType.IMAGE_PNG)
		        .body(qr);
		
	}
	
	@PostMapping("/hello")
	public String hello() {
	    System.out.println("HELLO ENDPOINT");
	    return "Hello";
	}
	
	@PostMapping("/check-in"
		)
	public CheckInResponse checkIn(
	        @RequestBody CheckInRequest request) {
		
		System.out.println("Inside checkIn Controller");

	    return ticketService.checkIn(request);
	}
}
