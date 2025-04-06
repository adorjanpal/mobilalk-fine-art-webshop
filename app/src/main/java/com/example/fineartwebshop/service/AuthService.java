package com.example.fineartwebshop.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fineartwebshop.MainActivity;
import com.example.fineartwebshop.ui.login.LoginActivity;
import com.example.fineartwebshop.ui.register.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    FirebaseAuth mAuth;
    private static AuthService instance;

    private AuthService() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }

        return instance;
    }

    public void login(String username, String password, Activity context) {
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Login successful.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                            context.startActivity(intent);

                            context.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Invalid email/password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(String username, String password, Activity context) {
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Account created.",Toast.LENGTH_SHORT).show();
                            login(username, password, context);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
