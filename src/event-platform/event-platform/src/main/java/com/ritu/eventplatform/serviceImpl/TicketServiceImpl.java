package com.ritu.eventplatform.serviceImpl;


import org.springframework.stereotype.Service;

import com.ritu.eventplatform.dto.CheckInRequest;
import com.ritu.eventplatform.dto.CheckInResponse;
import com.ritu.eventplatform.entity.Ticket;
import com.ritu.eventplatform.enums.TicketStatus;
import com.ritu.eventplatform.exception.TicketAlreadyUsedException;
import com.ritu.eventplatform.exception.TicketCancelledException;
import com.ritu.eventplatform.exception.TicketNotFoundException;
import com.ritu.eventplatform.repository.TicketRepository;
import com.ritu.eventplatform.service.TicketService;
import com.ritu.eventplatform.service.qr.QrCodeService;

import jakarta.transaction.Transactional;

@Service
public class TicketServiceImpl implements TicketService {
	
	private final TicketRepository ticketRepository;

	private final QrCodeService qrCodeService;
	
	 TicketServiceImpl(TicketRepository ticketRepository, QrCodeService qrCodeService) {
		this.ticketRepository = ticketRepository;
		this.qrCodeService = qrCodeService;
	}

	@Override
	public byte[] getQrCode(String ticketNumber) {
		
		// Check if the ticket exists in the database
		Ticket ticket = ticketRepository
		        .findByTicketNumber(ticketNumber)
		        .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
		
		 return qrCodeService.generateQrCode(ticket.getQrToken());
	}

	@Override
	@Transactional
	public CheckInResponse checkIn(CheckInRequest request) {
		
		System.out.println("Inside TicketServiceImpl");
		Ticket ticket = ticketRepository
		        .findWithLockByQrToken(request.qrToken())
		        .orElseThrow(() ->
		                new TicketNotFoundException("Ticket not found"));
		if(ticket.getStatus() == TicketStatus.CANCELLED){
		    throw new TicketCancelledException("Ticket is cancelled");
		}
		
		if(ticket.getStatus() == TicketStatus.USED){
		    throw new TicketAlreadyUsedException("Ticket already checked in");
		}
		
		// Mark the ticket as used
		ticket.setStatus(TicketStatus.USED);
		
		 return new CheckInResponse("Check-in successful");
	}
	
	

}
