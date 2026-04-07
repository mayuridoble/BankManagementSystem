package com.Bank.ManagmentSystem.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class QRCodeGenerator {
    
    public static byte[] generateQRCode(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }
    
    public static byte[] generateUpiQRCode(String upiHandle, String payeeName, 
                                           Double amount, int width, int height) {
        StringBuilder upiUrl = new StringBuilder("upi://pay?");
        upiUrl.append("pa=").append(upiHandle);
        upiUrl.append("&pn=").append(payeeName);
        
        if (amount != null && amount > 0) {
            upiUrl.append("&am=").append(amount);
        }
        
        upiUrl.append("&cu=INR");
        
        return generateQRCode(upiUrl.toString(), width, height);
    }
}