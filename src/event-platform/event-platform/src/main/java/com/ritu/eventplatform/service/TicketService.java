package com.ritu.eventplatform.service;

import com.ritu.eventplatform.dto.CheckInRequest;
import com.ritu.eventplatform.dto.CheckInResponse;

public interface TicketService {
	
	byte[] getQrCode(String ticketNumber);
	CheckInResponse checkIn(CheckInRequest request);

}
