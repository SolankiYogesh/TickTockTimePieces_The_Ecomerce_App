package com.syd.ticktocktimepieces;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;
import com.syd.ticktocktimepieces.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    ImageView menu;
    DrawerLayout drawerLayout;
    CardView Cart, Watches,myorder,myteam;
    TextView txtemail, txtusername;
    LinearLayout login,logout,watchelayout,aboutlayput,rate,share;
    View loginview,logoutview;
    public String Link;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions gso;





    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        myteam = findViewById(R.id.myteam);
        myteam.setOnClickListener(v -> {
            startActivity(new Intent(DashBoardActivity.this,MyTeamsActivity.class));
            finish();
        });
        share = findViewById(R.id.layoutshare);
        share.setOnClickListener(v -> {
            Intent shareIntent =   new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String app_url = Link;
            shareIntent.putExtra(Intent.EXTRA_TEXT,app_url);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
        rate = findViewById(R.id.rate);
        rate.setOnClickListener(v -> Toast.makeText(DashBoardActivity.this,"Sorry! Our App Not Available In PlayStore",Toast.LENGTH_LONG).show());
        aboutlayput = findViewById(R.id.layout_about) ;
        aboutlayput.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),AboutActiviti.class)));
        watchelayout = findViewById(R.id.layoutwatches);
        watchelayout.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),MainActivity.class)));
        login = findViewById(R.id.layou_login);
        logout = findViewById(R.id.layout_logout);
        loginview = findViewById(R.id.login_view);
        logoutview = findViewById(R.id.logout_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.image_menu);
        txtemail = findViewById(R.id.text_email);
        txtusername = findViewById(R.id.text_username);
        Cart = findViewById(R.id.card_cart);
        Watches = findViewById(R.id.card_watches);
        myorder = findViewById(R.id.myorders);
        myorder.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),MyOrdersActivity.class)));

        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
        boolean isGoogleLogin = preferences.getBoolean(Constants.KEY_IS_GOOGLE_LOGED_IN, false);
        if (!isLoggedIn && !isGoogleLogin) {
            txtusername.setText(R.string.Welcome_Guest);
            txtusername.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            loginview.setVisibility(View.VISIBLE);
            logout.setVisibility(View.GONE);
            logoutview.setVisibility(View.GONE);
        } else {
            if (isGoogleLogin){
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
                if (opr.isDone()){
                    GoogleSignInResult result = opr.get();
                    GoogleSignInAccount account = result.getSignInAccount();
                    txtusername.setText(account.getDisplayName());
                    txtusername.setVisibility(View.VISIBLE);
                    txtemail.setText(account.getEmail());
                    txtemail.setVisibility(View.VISIBLE);
                    login.setVisibility(View.GONE);
                    loginview.setVisibility(View.GONE);
                    logout.setVisibility(View.VISIBLE);
                    logoutview.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constants.KEY_EMAIL,account.getEmail());
                    editor.putString(Constants.KEY_USRNAME,account.getDisplayName());
                    editor.apply();
                }else {
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(@NonNull GoogleSignInResult result) {
                            GoogleSignInAccount account = result.getSignInAccount();
                            txtusername.setText(account.getDisplayName());
                            txtusername.setVisibility(View.VISIBLE);
                            txtemail.setText(account.getEmail());
                            txtemail.setVisibility(View.VISIBLE);
                            login.setVisibility(View.GONE);
                            loginview.setVisibility(View.GONE);
                            logout.setVisibility(View.VISIBLE);
                            logoutview.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }else{
                txtusername.setText(preferences.getString(Constants.KEY_USRNAME,"N/A"));
                txtusername.setVisibility(View.VISIBLE);
                txtemail.setText(preferences.getString(Constants.KEY_EMAIL,"N/A"));
                txtemail.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
                loginview.setVisibility(View.GONE);
                logout.setVisibility(View.VISIBLE);
                logoutview.setVisibility(View.VISIBLE);}

        }
        menu.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        Cart.setOnClickListener(v -> {
            Intent intent = new Intent(DashBoardActivity.this, CartActivity.class);
            startActivity(intent);
        });
        Watches.setOnClickListener(v -> {
            Intent intent = new Intent(DashBoardActivity.this, MainActivity.class);
            startActivity(intent);
        });

        logout.setOnClickListener(v -> {
            AlertDialog.Builder alert=new AlertDialog.Builder(DashBoardActivity.this);
            alert.setTitle("Alert");
            alert.setMessage("Do you Really Want to Log Out ?");
            alert.setPositiveButton("YES", (dialog, which) -> {
                if(isGoogleLogin){
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if(status.isSuccess()){
                                startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                                finish();
                            }
                        }
                    });
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(DashBoardActivity.this,"You Have Been Logged Out!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                    finish();
                }
                else {
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.clear();
                    editor.apply();
                    Toast.makeText(DashBoardActivity.this,"You Have Been Logged Out!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));
                    finish();
                }

            });
            alert.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
            alert.show();
        });
        login.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}