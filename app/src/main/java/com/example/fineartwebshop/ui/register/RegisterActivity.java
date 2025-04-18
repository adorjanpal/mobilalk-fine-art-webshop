package com.example.fineartwebshop.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fineartwebshop.R;
import com.example.fineartwebshop.databinding.ActivityRegisterBinding;
import com.example.fineartwebshop.service.AuthService;
import com.example.fineartwebshop.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import validator.Validator;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private AuthService authService;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authService = AuthService.getInstance();

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final EditText passwordAgainEditText = binding.passwordAgain;
        final Button registerBtn = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;
        final TextView switchToLoginText = binding.switchToLogin;

        switchToLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password, passwordAgain;
                Validator validator = new Validator();
                mAuth = FirebaseAuth.getInstance();

                username = String.valueOf(usernameEditText.getText());
                password = String.valueOf(passwordEditText.getText());
                passwordAgain = String.valueOf(passwordAgainEditText.getText());

                HashMap<String, String> formData = new HashMap<>(){{
                        put("Username", username);
                        put("Password", password);
                }};

                validator.validateEmpty(formData);
                validator.validateMatch(password, passwordAgain, "Passwords");

                if (validator.hasErrors()) {
                    Toast.makeText(RegisterActivity.this, validator.getFirstError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingProgressBar.setVisibility(View.VISIBLE);

                authService.register(username, password, RegisterActivity.this);

                loadingProgressBar.setVisibility(View.GONE);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}