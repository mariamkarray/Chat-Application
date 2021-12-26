package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class verificationCodeAuthentication extends AppCompatActivity {
    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enterotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofotpauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_authentication);


        mchangenumber=findViewById(R.id.changenumber);
        mverifyotp=findViewById(R.id.verify);
        mgetotp=findViewById(R.id.verfcode);
        mprogressbarofotpauth=findViewById(R.id.verfcodeProgressbar);

        firebaseAuth=FirebaseAuth.getInstance();

        // Android Intent is the message that is passed between components such as activities.
        //It is generally used with startActivity() method to invoke activity, broadcast receivers etc.
        // when clicked on the re enter your number textview, we go to the main activity
        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(verificationCodeAuthentication.this,MainActivity.class);
                startActivity(intent);
            }
        });
        // when click on verify btn
        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterotp=mgetotp.getText().toString();
                if(enterotp.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your otp first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbarofotpauth.setVisibility(View.VISIBLE);
                    // the otp is sent from the main activity, so we take it using the key (otp), put it in a string
                    String coderecieved = getIntent().getStringExtra("otp");

                    // Wraps phone number and *verification* information for authentication purposes.
                    // (compares between the otp sent to the user from the main activity and the entered otp)
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(coderecieved,enterotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        // Use this method to sign in a user into your Firebase Authentication system by passing credential (el e3tmad)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // the task here is the whether the authentication succeeded or not
           if (task.isSuccessful())
           {
               mprogressbarofotpauth.setVisibility(View.INVISIBLE);
               Toast.makeText(getApplicationContext(),"Login suceeded",Toast.LENGTH_SHORT).show();
               Intent intent=new Intent(verificationCodeAuthentication.this,setProfile.class);
               startActivity(intent);
               finish();

           }
           else
           {
               if(task.getException()instanceof FirebaseAuthInvalidCredentialsException)
               {
                   mprogressbarofotpauth.setVisibility(View.INVISIBLE);
                   Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_SHORT).show();
               }

           }
            }
        });
    }
}