package com.example.errand;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {


    private boolean isFirstAnimation = false;
    RelativeLayout rellay1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };

    Dialog newAcc;


    FirebaseAuth firebaseAuth;

//    public void sendMessage(View view) {
//        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        newAcc = new Dialog(this);

        final EditText first_name = (EditText) findViewById(R.id.tv_first_name);
        final EditText last_name = (EditText) findViewById(R.id.last_name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText phoneno = (EditText)findViewById(R.id.ph_no);
        final EditText password = (EditText)findViewById(R.id.password);
        final Button btnSubmit = (Button) findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                        phoneno.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter all details",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    final String getFName = first_name.getText().toString();
                    final String getLName = last_name.getText().toString();
                    final String getEmail = email.getText().toString();
                    final String getPhNo = phoneno.getText().toString();
                    final String getPass = password.getText().toString();

                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser == null){
                        firebaseAuth.signInWithEmailAndPassword(getEmail, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    Toast.makeText(getApplicationContext(),
                                            "Signed in as " + getFName + getLName,
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                                else {
                                    // pop-up button to confirm new account
                                    ShowPopup(getEmail, getPass);
                                }
                            }
                        });
                    }
                    intent.putExtra("fname", getFName);
                    intent.putExtra("lname", getLName);
                    intent.putExtra("email", getEmail);
                    intent.putExtra("phno", getPhNo);
                    intent.putExtra("addr", getPass);
                    startActivity(intent);
                }
            }
        });

        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);

        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);
//        final ImageView imageView = findViewById(R.id.imgView_logo);

        final ImageView imageView = (ImageView) findViewById(R.id.imgView_logo);
        Glide
                .with(this)
                .load(R.drawable.errand_logo_white)
                        .into(imageView);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation();
                    rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
                    handler.postDelayed(runnable, 2000); // 2000 is the timeout for the splash
                }

                isFirstAnimation = true;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(hold);



        }

    public void ShowPopup(String email, String pass){
        final String emailId = email;
        final String password = pass;
        TextView txtClose;
        MaterialButton confirm;
        newAcc.setContentView(R.layout.new_acc_popup);
        txtClose = (TextView) newAcc.findViewById(R.id.cancel_popup);
        confirm = (MaterialButton) newAcc.findViewById(R.id.confirm_acc);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAcc.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(SplashScreen.this, "Account Created!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SplashScreen.this, "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                            newAcc.dismiss();
                        }
                    }
                });
            }
        });
        newAcc.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newAcc.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof TextInputEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    }