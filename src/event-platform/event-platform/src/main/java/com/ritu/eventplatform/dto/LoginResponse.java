package com.ritu.eventplatform.dto;


/*
 * below record is for this flow
 * Email + Password
      ↓
Validate Credentials
      ↓
"Login Successful"
 */
//public record LoginResponse(
//
//		String message
//		) {
//
//}


public record LoginResponse(
		
		String token
		) {

}

/*
 * 
 * Above record is for this flow 
 * Email + Password
      ↓
Validate Credentials
      ↓
Generate JWT
      ↓
Return JWT

*/
 