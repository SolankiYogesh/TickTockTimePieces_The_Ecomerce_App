package com.syd.ticktocktimepieces;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.syd.ticktocktimepieces.utils.Constants;

import static com.syd.ticktocktimepieces.utils.Constants.KEY_ISPAYMENTSUCCESS;
import static com.syd.ticktocktimepieces.utils.Constants.KEY_IS_LOGGED_IN;

public class GooglePayActivity extends AppCompatActivity {
    public static final String GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    EditText name, upiId, amount, note;
    TextView msg;
    Button pay;
    Uri uri;
    String approvalRefNo;

    public static String payerName, UpiId, msgNote, sendAmount, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay);

        name = findViewById(R.id.name);
        upiId = findViewById(R.id.upi_id);
        amount = findViewById(R.id.amount);
        note = findViewById(R.id.transaction_note);

        msg = findViewById(R.id.status);
        pay = findViewById(R.id.pay);


        //initialising default value
        name.setText("Vrushabh Jansari");
        upiId.setText("hacker261902@oksbi");
        note.setText("donation");
        amount.setText("50");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                payerName = name.getText().toString();
                UpiId = upiId.getText().toString();
                msgNote = note.getText().toString();
                sendAmount = amount.getText().toString();

                if(!payerName.equals("") && !upiId.equals("") && !msgNote.equals("") && !sendAmount.equals("")){

                    uri = getUpiPaymentUri(payerName, UpiId, msgNote, sendAmount);
                    payWithGpay(GPAY_PACKAGE_NAME);

                }
                else {
                    Toast.makeText(GooglePayActivity.this,"Fill all above details and try again.", Toast.LENGTH_SHORT).show();


                }



            }
        });

    }

    private static Uri getUpiPaymentUri(String name, String upiId, String note, String amount){
        return  new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();
    }

    private void payWithGpay(String packageName){

        if(isAppInstalled(this,packageName)){

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(packageName);
            startActivityForResult(intent,0);

        }
        else{
            Toast.makeText(GooglePayActivity.this,"Google pay is not installed. Please istall and try again.", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("SetTextI18n")
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
            approvalRefNo = data.getStringExtra("txnRef");
        }
        if ((RESULT_OK == resultCode) && status.equals("success")) {
            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_ISPAYMENTSUCCESS,true);
            editor.apply();
            Toast.makeText(GooglePayActivity.this, "Transaction successful. "+approvalRefNo, Toast.LENGTH_SHORT).show();
            msg.setText("Transaction successful of ₹" + sendAmount);
            msg.setTextColor(Color.GREEN);
            startActivity(new Intent(getApplicationContext(),CartActivity.class));


        }

        else{
            Toast.makeText(GooglePayActivity.this, "Transaction cancelled or failed please try again.", Toast.LENGTH_SHORT).show();
            msg.setText("Transaction Failed of ₹" + sendAmount);
            msg.setTextColor(Color.RED);
        }

    }


    public static boolean isAppInstalled(Context context, String packageName){
        try{
            context.getPackageManager().getApplicationInfo(packageName,0);
            return true;
        }catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }
}
