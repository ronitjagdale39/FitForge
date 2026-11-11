package com.example.fitforge.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitforge.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    EditText etConfirmPassword, etAge, etHeight, etWeight;

    Spinner spGender;

    Button btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        etAge = findViewById(R.id.etAge);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);

        spGender = findViewById(R.id.spGender);

        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        String[] genders = {
                "Select Gender",
                "Male",
                "Female",
                "Other",
                "Prefer not to say"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                genders
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spGender.setAdapter(adapter);

        btnRegister.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() ||
                etAge.getText().toString().trim().isEmpty() ||
                etHeight.getText().toString().trim().isEmpty() ||
                etWeight.getText().toString().trim().isEmpty()) {

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

        if (spGender.getSelectedItemPosition() == 0) {
            Toast.makeText(this,
                    "Please select gender",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this,
                "Registration UI working!",
                Toast.LENGTH_SHORT).show();
    }
}