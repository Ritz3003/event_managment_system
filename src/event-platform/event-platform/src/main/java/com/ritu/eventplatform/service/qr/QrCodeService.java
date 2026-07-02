package com.ritu.eventplatform.service.qr;


// Notice  that we are not passing the ticket or a user but a string to generate the QR code. 
//This is because we can generate a QR code for any string, not just for tickets or users. 
//The string can be a URL, a piece of text, or any other data that we want to encode in the QR code.
// This makes the service more flexible and reusable in different contexts.
public interface QrCodeService {

    byte[] generateQrCode(String text);

}

/*
 * Why are we creating a separate service?
 * 
 * Instead of:
 * 
 * TicketService.generateQr(...)
 * 
 * we use:
 * 
 * QrCodeService
 * 
 * because of the Single Responsibility Principle.
 * 
 * Tomorrow, if another feature needs QR generation:
 * 
 * Event invitations Discount coupons Payment receipts
 * 
 * we can reuse the same service.
 */

