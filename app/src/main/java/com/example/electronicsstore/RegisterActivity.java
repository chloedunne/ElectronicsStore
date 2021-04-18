package com.example.electronicsstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button register;

    private EditText editTextEmail, editTextPassword, editTextFirstname, editTextLastname, editTextConfirmPassword, editTextAddress, editTextPhone, editTextAdminCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.registerEditTextTextEmailAddress);
        editTextPassword = (EditText) findViewById(R.id.registerEditTextTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.registerEditTextTextPassword2);
        editTextFirstname = (EditText) findViewById(R.id.registerFirstNameEditText);
        editTextLastname = (EditText) findViewById(R.id.registerSurnameEditText);
        register = (Button) findViewById(R.id.registerButton2);
        editTextAddress = findViewById(R.id.registerEditTextAddress);
        editTextPhone = findViewById(R.id.registerEditTextPhone);
        editTextAdminCode = findViewById(R.id.registerEditTextAdminCode);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });


    }

    public void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String firstname = editTextFirstname.getText().toString().trim();
        String lastname = editTextLastname.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        Boolean admin = false;

        if(!(editTextAdminCode.getText().toString().isEmpty())){
            int adminCode = Integer.parseInt(editTextAdminCode.getText().toString().trim());
            if(adminCode == 1234){
                admin = true;
            }
            else {
                editTextAdminCode.setError("Admin code incorrect");
                editTextEmail.requestFocus();
                return;
            }
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email address is required");
            editTextEmail.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return;
        }

        if(phone.isEmpty()){
            editTextPhone.setError("Phone is required");
            editTextPhone.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Valid email address is required");
            editTextEmail.requestFocus();
            return;
        }

        if (firstname.isEmpty()) {
            editTextFirstname.setError("Name is required");
            editTextFirstname.requestFocus();
            return;
        }

        if (lastname.isEmpty()) {
            editTextLastname.setError("Surname is required");
            editTextLastname.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        Boolean finalAdmin = admin;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Profile profile = new Profile( firstname, lastname, email, password, address, phone, finalAdmin, null);
                    FirebaseDatabase.getInstance().getReference("Profiles")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Register has completed successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration failed, please try again!!!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed, please try again",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}