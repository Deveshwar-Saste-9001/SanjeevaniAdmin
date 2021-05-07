package com.example.sanjeevaniadmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText email, password;
    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = findViewById(R.id.login_name);
        password = findViewById(R.id.login_password);
        done = findViewById(R.id.done_resistration);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });


    }


    private void CreateAccount() {
        if (TextUtils.isEmpty(email.getText().toString())) {
            Toast.makeText(this, "Enter your email..", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();

        } else {
            CheckEmailAndPassword(email.getText().toString(), password.getText().toString());
        }

    }

    private void CheckEmailAndPassword(final String Email, final String Password) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if (Email.matches(emailPattern)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final HashMap<String, Object> userdataMap = new HashMap<>();
                                userdataMap.put("Name", "Vikash Shirsath");
                                userdataMap.put("Email", Email);
                                userdataMap.put("profile", "");
                                userdataMap.put("Password", Password);

                                FirebaseFirestore.getInstance().collection("ADMIN").document(FirebaseAuth.getInstance().getUid())
                                        .set(userdataMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        });

                            } else {
                                Toast.makeText(RegistrationActivity.this, "Network Error Please Try Again", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }

    }
}