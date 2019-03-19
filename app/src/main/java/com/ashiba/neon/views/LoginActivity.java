package com.ashiba.neon.views;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ashiba.neon.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout loginEmail, loginPassword;
    TextView forgotPasswordTxt;
    Button loginBtn, createAccountBtn, loginFbCustomBtn, loginGoogleCustomBtn;
    LoginButton loginFbBtn;
    CallbackManager callbackManager;

    SignInButton loginGoogleBtn;
    GoogleSignInClient googleSignInClient;
    final int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initializeControls();
        loginWithFB();
        loginWithGoogle();

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        forgotPasswordTxt = findViewById(R.id.login_forgot_password);

        loginBtn = findViewById(R.id.signin_btn);
        createAccountBtn = findViewById(R.id.create_account_btn);

        forgotPasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(forgotPasswordIntent);
                finish();
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(registerIntent);
//                finish();
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

    public void onClickBtn(View v) {
        if (v == loginFbCustomBtn) {
            loginFbBtn.performClick();
        } else if (v == loginGoogleCustomBtn) {
            Intent signInGoogleIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInGoogleIntent, RC_SIGN_IN);
        }
    }

    private void initializeControls() {

        callbackManager = CallbackManager.Factory.create();
        loginFbBtn = findViewById(R.id.signin_facebook_btn);

        loginFbCustomBtn = findViewById(R.id.signin_facebook_custom_btn);

        loginFbBtn.setReadPermissions(Arrays.asList("public_profile", "email"));

        loginFbCustomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickBtn(v);
            }
        });


    }

    private void loginWithGoogle() {

        loginGoogleBtn = findViewById(R.id.signin_google_btn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        loginGoogleCustomBtn = findViewById(R.id.signin_google_custom_btn);

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        loginGoogleCustomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClickBtn(v);
            }
        });

    }

    private void loginWithFB() {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.v("LoginAct", loginResult.toString());

                Intent registerIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(registerIntent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task  = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

            Log.d("LoginActivity", account.getDisplayName());

        } catch (ApiException e) {

            e.printStackTrace();
            Log.d("LoginActivity", e.getMessage());

        }
    }

}
