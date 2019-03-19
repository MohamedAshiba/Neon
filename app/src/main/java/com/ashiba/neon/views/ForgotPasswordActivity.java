package com.ashiba.neon.views;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ashiba.neon.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextInputLayout forgotPassEmail;
    Button resetPassBtn, signinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgotPassEmail = findViewById(R.id.forgot_password_email);

        resetPassBtn = findViewById(R.id.reset_password_btn);
        signinBtn = findViewById(R.id.back_signin_btn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
