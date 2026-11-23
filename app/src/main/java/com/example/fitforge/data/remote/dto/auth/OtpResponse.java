package com.example.fitforge.data.remote.dto.auth;

public class OtpResponse {
    private String message;
    private String otp; // Used in send_otp response for testing

    public String getMessage() {
        return message;
    }

    public String getOtp() {
        return otp;
    }
}