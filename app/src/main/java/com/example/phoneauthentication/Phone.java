package com.example.phoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.phoneauthentication.databinding.ActivityPhoneBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Phone extends AppCompatActivity {
    ActivityPhoneBinding phoneBinding;
    FirebaseAuth mauth;
    private static final String TAG="TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_phone);
        phoneBinding= DataBindingUtil.setContentView(this,R.layout.activity_phone);
        phoneBinding.prgsLogin.setVisibility(View.GONE);
        mauth=FirebaseAuth.getInstance();

        phoneBinding.btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isMobileNumberEmpty()&& !isValidMobileNumber()){
                    return;
                }
                    sendVerificationCode();


            }
        });

    }



    private void sendVerificationCode() {
        phoneBinding.prgsLogin.setVisibility(View.VISIBLE);
        phoneBinding.btnSendOTP.setVisibility(View.GONE);

        String phoneNumber="+91"+phoneBinding.edtPhoneNumber.getText().toString();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mauth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks
            = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            signInWithPhoneAuthCredential(phoneAuthCredential);
        }



        @Override
        public void onVerificationFailed(FirebaseException e) {
            phoneBinding.prgsLogin.setVisibility(View.GONE);
            phoneBinding.btnSendOTP.setVisibility(View.VISIBLE);
            Toast.makeText(Phone.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            String phoneNumber="+91"+phoneBinding.edtPhoneNumber.getText().toString();
            phoneBinding.btnSendOTP.setVisibility(View.GONE);
            phoneBinding.prgsLogin.setVisibility(View.VISIBLE);
            Intent otpIntent= new Intent(Phone.this,OTP.class);
            otpIntent.putExtra("verificationId",s);
            otpIntent.putExtra("resendToken",forceResendingToken);
//            send mobile number to otp activity in case of resending code
            otpIntent.putExtra("mobileNumber",phoneNumber);
            startActivity(otpIntent);


            //storing the verification id that is sent to the user
//            mVerificationId = s;
        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }
                        }
                    }
                });
    }


    private boolean isMobileNumberEmpty(){
        String phoneNumber=phoneBinding.edtPhoneNumber.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            phoneBinding.edtPhoneNumber.setError("Enter mobile number");
            return  false;
        }
        return true;
    }

    private boolean isValidMobileNumber() {
       if(phoneBinding.edtPhoneNumber.length()!=10){
           phoneBinding.edtPhoneNumber.setError("Invalid mobile number");
           return false;
       }
       return true;
    }


}
