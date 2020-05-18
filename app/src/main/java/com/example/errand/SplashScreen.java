package com.example.errand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

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

//    public void sendMessage(View view) {
//        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText first_name = (EditText) findViewById(R.id.tv_first_name);
        final EditText last_name = (EditText) findViewById(R.id.last_name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText phoneno = (EditText)findViewById(R.id.ph_no);
        final EditText address = (EditText)findViewById(R.id.address);
        final Button btnSubmit = (Button) findViewById(R.id.button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty() || address.getText().toString().isEmpty() ||
                        phoneno.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter all details",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    String getFName = first_name.getText().toString();
                    String getLName = last_name.getText().toString();
                    String getEmail = email.getText().toString();
                    String getPhNo = phoneno.getText().toString();
                    String getAdd = address.getText().toString();

                    intent.putExtra("fname", getFName);
                    intent.putExtra("lname", getLName);
                    intent.putExtra("email", getEmail);
                    intent.putExtra("phno", getPhNo);
                    intent.putExtra("addr", getAdd);
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