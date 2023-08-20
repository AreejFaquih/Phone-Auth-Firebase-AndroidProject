package com.example.phoneauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.phoneauthentication.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {
    ActivityOtpBinding otpBinding;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth mauth;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otp);
        otpBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        initializeActivity();

        otpBinding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateOTP()) {
                    return;
                } else {
                    verifyOTP();

                }
            }
        });
        otpBinding.txtResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode();
                resendOTPVisibility();
            }
        });
    }

    private void resendVerificationCode() {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                otpBinding.prgrsVerify.setVisibility(View.GONE);
                Toast.makeText(OTP.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                resendingToken=forceResendingToken;
                verificationId=s;
            }
        };

//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mauth)
//                        .setPhoneNumber(phoneNumber)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(this)                 // (optional) Activity for callback binding
//                         If no activity is passed, reCAPTCHA verification can not be used.
//                        .setCallbacks(mCallbacks)
//                        .setForceResendingToken(resendingToken)// OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        mauth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(OTP.this, "Authenticated Successfully", Toast.LENGTH_SHORT).show();
                       Intent sendToMainIntent = new Intent(OTP.this,MainActivity.class);
                       startActivity(sendToMainIntent);
                   }
                   else{

                   }
                   otpBinding.prgrsVerify.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void resendOTPVisibility() {
        otpBinding.edtcode1.setText("");
        otpBinding.edtcode2.setText("");
        otpBinding.edtcode3.setText("");
        otpBinding.edtcode4.setText("");
        otpBinding.edtcode5.setText("");
        otpBinding.edtcode6.setText("");
        otpBinding.edtcode1.requestFocus();
        otpBinding.txtResendOTP.setVisibility(View.INVISIBLE);
        if(Looper.myLooper()!=null) {
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    otpBinding.txtResendOTP.setVisibility(View.VISIBLE);
                    otpBinding.txtResendOTP.setEnabled(true);
                }
            }, 60000);
        }

    }


    private void initializeActivity() {
        verificationId = getIntent().getStringExtra("verificationId");
        resendingToken=getIntent().getParcelableExtra("resendToken");
         phoneNumber=getIntent().getStringExtra("mobileNumber");
        otpBinding.prgrsVerify.setVisibility(View.GONE);
        addTextChangeListener();

    }

    private void addTextChangeListener() {
        otpBinding.edtcode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (s.length() == 1) {
                    otpBinding.edtcode2.requestFocus();
                }

            }
        });

        otpBinding.edtcode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otpBinding.edtcode3.requestFocus();
                } else if (s.length() == 0) {
                    otpBinding.edtcode1.requestFocus();
                }

            }
        });
        otpBinding.edtcode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otpBinding.edtcode4.requestFocus();
                } else if (s.length() == 0) {
                    otpBinding.edtcode2.requestFocus();
                }
            }
        });
        otpBinding.edtcode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    otpBinding.edtcode5.requestFocus();
                } else if (s.length() == 0) {
                    otpBinding.edtcode3.requestFocus();
                }
            }
        });
        otpBinding.edtcode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
if(s.length()==1){
    otpBinding.edtcode6.requestFocus();
}
else if(s.length()==0){
    otpBinding.edtcode5.requestFocus();
}
            }
        });
    }

    private void verifyOTP() {
        if (verificationId != null) {
            otpBinding.prgrsVerify.setVisibility(View.VISIBLE);
            otpBinding.btnVerifyOTP.setVisibility(View.GONE);
            String code = otpBinding.edtcode1.getText().toString() + otpBinding.edtcode2.getText().toString() +
                    otpBinding.edtcode3.getText().toString() + otpBinding.edtcode4.getText().toString() +
                    otpBinding.edtcode5.getText().toString() + otpBinding.edtcode6.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(OTP.this, MainActivity.class);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                otpBinding.prgrsVerify.setVisibility(View.GONE);
                                otpBinding.btnVerifyOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(OTP.this, "OTP is not valid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private boolean validateOTP() {
        String enteredOTP = otpBinding.edtcode1.getText().toString() + otpBinding.edtcode2.getText().toString() +
                otpBinding.edtcode3.getText().toString() + otpBinding.edtcode4.getText().toString() +
                otpBinding.edtcode5.getText().toString() + otpBinding.edtcode6.getText().toString();
        if (enteredOTP.isEmpty()) {
            Toast.makeText(this, "Enter OTP", Toast.LENGTH_SHORT).show();

        } else if (enteredOTP.length() != 6) {
            Toast.makeText(this, "Enter valid OTP", Toast.LENGTH_SHORT).show();

        }
        return true;
    }
}
