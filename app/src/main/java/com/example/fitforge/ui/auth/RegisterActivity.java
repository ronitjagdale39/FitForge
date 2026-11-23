package com.example.fitforge.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitforge.R;

import com.example.fitforge.data.remote.ApiClient;
import com.example.fitforge.data.remote.AuthApi;
import com.example.fitforge.data.remote.dto.RegisterRequest;
import com.example.fitforge.data.remote.dto.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.fitforge.data.remote.dto.auth.OtpRequest;
import com.example.fitforge.data.remote.dto.auth.OtpResponse;
import com.example.fitforge.data.remote.dto.auth.OtpVerifyRequest;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    EditText etConfirmPassword;

    Button btnRegister;
    TextView tvLogin;
    
    private AuthApi authApi;
    private String registeredEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        
        // Initialize Retrofit API
        authApi = ApiClient.getClient().create(AuthApi.class);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()) {

            Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }

        if (password.length() < 8) {
            etPassword.setError("Minimum 8 characters required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }
        
        // --- Call Backend API ---
        
        btnRegister.setEnabled(false); // disable button while loading
        btnRegister.setText("Registering...");

        RegisterRequest request = new RegisterRequest(name, email, password);

        authApi.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registeredEmail = email; // Save for OTP verification
                    sendOtp(); // Trigger OTP instead of finishing
                } else {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("CREATE ACCOUNT");
                    Toast.makeText(RegisterActivity.this, "Registration Failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("CREATE ACCOUNT");
                Toast.makeText(RegisterActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOtp() {
        OtpRequest request = new OtpRequest(registeredEmail);
        authApi.sendOtp(request).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText("CREATE ACCOUNT");
                
                if (response.isSuccessful() && response.body() != null) {
                    String otp = response.body().getOtp();
                    // Temporary toast since no actual email is sent
                    Toast.makeText(RegisterActivity.this, "OTP Sent! Check your email. Code: " + otp, Toast.LENGTH_LONG).show();
                    showOtpDialog();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("CREATE ACCOUNT");
                Toast.makeText(RegisterActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOtpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_otp_verification, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        
        AlertDialog dialog = builder.create();
        
        android.widget.EditText etOtpCode = dialogView.findViewById(R.id.etOtpCode);
        android.widget.Button btnVerifyOtp = dialogView.findViewById(R.id.btnVerifyOtp);
        android.widget.Button btnCancelOtp = dialogView.findViewById(R.id.btnCancelOtp);
        
        btnCancelOtp.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(this, "Verification Cancelled", Toast.LENGTH_SHORT).show();
        });
        
        btnVerifyOtp.setOnClickListener(v -> {
            String otpCode = etOtpCode.getText().toString().trim();
            if (otpCode.length() != 6) {
                etOtpCode.setError("Enter 6-digit OTP");
                return;
            }
            verifyOtp(otpCode, dialog);
        });
        
        dialog.show();
    }

    private void verifyOtp(String otpCode, AlertDialog dialog) {
        OtpVerifyRequest request = new OtpVerifyRequest(registeredEmail, otpCode);
        authApi.verifyOtp(request).enqueue(new Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Email Verified Successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish(); // Return to Login
                } else {
                    Toast.makeText(RegisterActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}