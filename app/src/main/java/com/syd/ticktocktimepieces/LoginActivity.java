package com.syd.ticktocktimepieces;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.syd.ticktocktimepieces.models.LoginResponseModel;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;
import com.syd.ticktocktimepieces.utils.Constants;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.syd.ticktocktimepieces.utils.Constants.KEY_IS_GOOGLE_LOGED_IN;
import static com.syd.ticktocktimepieces.utils.Constants.KEY_IS_LOGGED_IN;
import static com.syd.ticktocktimepieces.utils.Constants.PREFERENCE_NAME;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView textCreateAccount;
    private static final int SING_IN = 1;
    AutoCompleteTextView inputEmail, inputPassword, inputPhoneNumber;
    OtpTextView PhoneOtp;
    TextView buttonLogin;
    ImageView GoogleLogin;
    TextView PhoneLogin;
    Button Verify, GetCode;
    FirebaseAuth mAuth;
    String codesent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        mAuth   = FirebaseAuth.getInstance();
        inputPhoneNumber = findViewById(R.id.phoneotpedit);
        PhoneLogin = findViewById(R.id.phonelogin);
        Verify = findViewById(R.id.verifyotp);
        GetCode = findViewById(R.id.getotpcode);
        PhoneOtp = findViewById(R.id.otp_view);

        PhoneLogin.setOnClickListener(v -> {
            inputEmail.setVisibility(View.GONE);
            inputPassword.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.GONE);
            inputPhoneNumber.setVisibility(View.VISIBLE);
            GetCode.setVisibility(View.VISIBLE);
        });
       GetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendvarificationcode();
            }
        });
        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyingCode();
            }
        });

        textCreateAccount = findViewById(R.id.tvSignIn);
        textCreateAccount.setPaintFlags(textCreateAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textCreateAccount.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));

        inputEmail = findViewById(R.id.atvEmailLog);
        inputPassword = findViewById(R.id.atvPasswordLog);
        ImageView skip = findViewById(R.id.skipimg);
        skip.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
            finish();
        });

        GoogleLogin = findViewById(R.id.googlebutton);
        GoogleLogin.setOnClickListener(v -> GooGleLogin());
        buttonLogin = findViewById(R.id.btnSignIn);

        buttonLogin.setOnClickListener(v -> {
            if (inputEmail.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            } else if (inputPassword.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            } else {
                login();
            }

        });
    }

    private void VerifyingCode() {
        String OTP = PhoneOtp.getOTP();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesent, OTP);
        signInWithPhoneAuthCredential(credential);
    }

   private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            PhoneOtp.showSuccess();
                            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                PhoneOtp.showError();
                                Toast.makeText(LoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void sendvarificationcode() {
        String phoneNumber = inputPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty()) {
            inputPhoneNumber.setError("PhoneNumber Required!");
            inputPhoneNumber.requestFocus();
            return;
        }
        if (phoneNumber.length() < 10) {
            inputPhoneNumber.setError("Invalid PhoneNumber");
            inputPhoneNumber.requestFocus();
            return;
        }
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codesent = s;
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    private void login() {
        AlertDialog alertDialog = new SpotsDialog(LoginActivity.this, R.style.Custom3);
        alertDialog.show();

        NetworkServices networkServices = NetworkClient.getClient().create(NetworkServices.class);
        Call<LoginResponseModel> login = networkServices.login(inputEmail.getText().toString(), inputPassword.getText().toString());
        login.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseModel> call, @NonNull Response<LoginResponseModel> response) {
                LoginResponseModel responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(KEY_IS_LOGGED_IN, true);
                        editor.putString(Constants.KEY_USRNAME, responseBody.getUserDetailsObject().getUser_Details().get(0).getFirst_name() + " " + responseBody.getUserDetailsObject().getUser_Details().get(0).getLast_name());
                        editor.putString(Constants.KEY_EMAIL, responseBody.getUserDetailsObject().getUser_Details().get(0).getEmail());
                        editor.apply();
                        Toast.makeText(LoginActivity.this, responseBody.getMassage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, responseBody.getMassage(), Toast.LENGTH_LONG).show();
                    }
                }
                alertDialog.cancel();
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "in onFailure", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });
    }

    private void GooGleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(i, SING_IN);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SING_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(KEY_IS_GOOGLE_LOGED_IN, true);
                editor.apply();
                startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "!Login Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}




