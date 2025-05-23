package com.example.fineartwebshop.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fineartwebshop.R;
import com.example.fineartwebshop.databinding.ActivityLoginBinding;
import com.example.fineartwebshop.databinding.ActivityRegisterBinding;
import com.example.fineartwebshop.service.AuthService;
import com.example.fineartwebshop.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

import validator.Validator;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authService = AuthService.getInstance();

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button button = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        final TextView switchToRegisterText = binding.switchToRegister;

        switchToRegisterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password, passwordAgain;
                Validator validator = new Validator();
                mAuth = FirebaseAuth.getInstance();

                username = String.valueOf(usernameEditText.getText());
                password = String.valueOf(passwordEditText.getText());

                HashMap<String, String> formData = new HashMap<>(){{
                    put("Username", username);
                    put("Password", password);
                }};

                validator.validateEmpty(formData);

                if (validator.hasErrors()) {
                    Toast.makeText(LoginActivity.this, validator.getFirstError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingProgressBar.setVisibility(View.VISIBLE);

                authService.login(username, password, LoginActivity.this);

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