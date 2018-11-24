package com.dwipa.user.csc.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dwipa.user.csc.Activities.MainActivity;
import com.dwipa.user.csc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText inputPasswordConfirm, inputEmail, inputPassword;
    private Button btnBack, btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnBack = (Button) findViewById(R.id.btn_back);
        btnSignUp =  (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputPasswordConfirm = (EditText) findViewById(R.id.passwordConfirm);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String passwordConfirm = inputPasswordConfirm.getText().toString().trim();

                if (TextUtils.isEmpty((email))) {
                    Toast.makeText(getApplicationContext(), "Ketik alamat email anda!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty((password))) {
                    Toast.makeText(getApplicationContext(), "Ketik password anda!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (TextUtils.isEmpty((passwordConfirm))) {
                    Toast.makeText(getApplicationContext(), "Ulangi password anda!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password terlalu pendek, ketik minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passwordConfirm.equals(password)) {
                    Toast.makeText(getApplicationContext(), "Password tidak sama!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //create user
                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "Tinggal satu langkah lagi.." ,Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Pendaftaran Gagal." , Toast.LENGTH_SHORT).show();

                                }else {
                                    startActivity(new Intent(SignupActivity.this, UserDataActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
