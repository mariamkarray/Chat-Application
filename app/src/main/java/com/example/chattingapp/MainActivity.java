package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    

    EditText mgetphonenumber;
    android.widget.Button msendotp;
    CountryCodePicker mcountrycodepicker;

    String countrycode; //country code selected by the user
    String phonenumber; //final phone number which contains country code + phone number

    FirebaseAuth firebaseAuth;
     //public abstract class FirebaseAuth extends Object
     //The entry point of the Firebase Authentication SDK.
     //First, obtain an instance of this class by calling getInstance().


    ProgressBar mprogressbarofmain;


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    //Represents the phone number authentication mechanism.


    String codesent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mcountrycodepicker=findViewById(R.id.countrycode);
        msendotp=findViewById(R.id.sendbtn);
        mgetphonenumber=findViewById(R.id.phonenumber);
        mprogressbarofmain=findViewById(R.id.mainProgressbar);

        firebaseAuth=FirebaseAuth.getInstance(); //The entry point for accessing a Firebase Database,it will get the instance of the current user

        countrycode=mcountrycodepicker.getSelectedCountryCodeWithPlus();//store the default country that is selected



        //if someone wants to change their country
        mcountrycodepicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode=mcountrycodepicker.getSelectedCountryCodeWithPlus();

            }
        });


        //to sent the otp (combine the string of the number with the country code)
        msendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number;
                number=mgetphonenumber.getText().toString();
                if(number.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter your number",Toast.LENGTH_SHORT).show();
                }
                else if (number.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Please enter your correct number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbarofmain.setVisibility(View.VISIBLE);
                    phonenumber=countrycode+number;


                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60L, TimeUnit.SECONDS) // send otp after 60 secs
                            .setActivity(MainActivity.this)
                            .setCallbacks(mcallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });


        //check if the phone number is correct or not
        mcallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //how to automatically fetch code here
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
            @Override
            public void onCodeSent(@NonNull String s,@NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
            {
                super.onCodeSent(s,forceResendingToken);
                Toast.makeText(getApplicationContext(),"Otp is sent",Toast.LENGTH_SHORT).show();
                mprogressbarofmain.setVisibility(View.INVISIBLE);
                codesent=s;
                Intent intent=new Intent(MainActivity.this,verificationCodeAuthentication.class);
                intent.putExtra("otp",codesent);
                startActivity(intent);

            }
        };
    }


    @Override
    protected void onStart()
    {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)   //user is already verified
        {
            Intent intent=new Intent(MainActivity.this,chatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



}