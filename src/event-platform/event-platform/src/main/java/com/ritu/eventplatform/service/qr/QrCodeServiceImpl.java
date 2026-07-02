package com.ritu.eventplatform.service.qr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Service
public class QrCodeServiceImpl implements QrCodeService {

	@Override
	public byte[] generateQrCode(String text) {
		
		//Step 1 :  Create QR matrix
		BitMatrix bitmatrix = null;
		ByteArrayOutputStream  pngOutputStream = null;
		try {
			bitmatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 250, 250);
			// 250 , 250 is simply width and height of the QR code image. You can adjust these values as needed.
	
		
		// Step 2: Convert to PNG image
		pngOutputStream = new ByteArrayOutputStream();
	    MatrixToImageWriter.writeToStream(bitmatrix, "PNG", pngOutputStream);
			// The "PNG" parameter specifies the image format. You can change it to "JPG" or "GIF" if you prefer a different format.
			// The pngOutputStream will contain the PNG image data after this operation.
			// You can then use pngOutputStream.toByteArray() to get the byte array representation of the PNG image.
			//QR Pattern --> PNG --> Memory
	    
	    // here what we did is  String text --> MultiFormatWriter  --> BitMatrix --> MatrixToImageWriter --> ByteArrayOutputStream --> byte[]

		}catch(WriterException | IOException e) {
			 throw new RuntimeException("Failed to generate QR Code", e);
		}
		
		return  bitmatrix != null ? pngOutputStream.toByteArray() : new byte[0];
	}

}


/*
 * Some important points to note about the QrCodeServiceImpl class:
 * MultiFormatWriter: This is a class from the ZXing library that can encode
 * data into various barcode formats, including QR codes. It takes the input
 * data (in this case, the ticket number) and generates a BitMatrix representing
 * the QR code.
 * 
 * BitMatrix: This is a 2D array of bits that represents the QR code. Each bit
 * corresponds to a pixel in the QR code image. A value of true indicates a
 * black pixel, while a value of false indicates a white pixel.
 * 
 * MatrixToImageWriter: This is a utility class from the ZXing library that can
 * convert a BitMatrix into an image format (like PNG or JPEG). It takes the
 * BitMatrix and the desired image format as input and returns a BufferedImage.
 * 
 * ByteArrayOutputStream: This is a class from the Java standard library that
 * allows you to write data to a byte array. In this case, we use it to write the
 * BufferedImage to a byte array in PNG format. The resulting byte array can then be returned to the caller.
 *
 *ByteArrayOutputStream Stores the PNG in memory.
     Instead of saving: qr.png to disk, we keep it in RAM: byte[] Much faster for APIs.
 */