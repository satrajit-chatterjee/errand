package com.errandcompany.errand;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = FeedbackActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TextInputEditText feedbackText;
    private ImageButton nav_button;
    ViewFlipper adflipper;
    private Toolbar mToolbar;
    NavigationView navView;
    private Intent login_intent;
    private MaterialButton feedbackSubmit;

    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        flipperAds();
        mToolbar = findViewById(R.id.topAppBar_feedback);
        setSupportActionBar(mToolbar);
        nav_button = (ImageButton) findViewById(R.id.nav_button_feedback);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_feedback);
        navView = findViewById(R.id.nav_view_feedback);
        navView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.setDrawerIndicatorEnabled(true);
        mToggle.syncState();

        feedbackText = (TextInputEditText) findViewById(R.id.feedback_text);

        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();

        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)){
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        feedbackText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                return false;
            }

        });

        login_intent = getIntent();


//         Add new user to Firestore database
        db.collection("Users").document(login_intent.getStringExtra("email")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String, Object> user = new HashMap<>();
                            user.put("phno", login_intent.getStringExtra("phno"));
                            user.put("name", login_intent.getStringExtra("name"));
                            user.put("email", login_intent.getStringExtra("email"));
                            user.put("addr", login_intent.getStringExtra("addr"));
                            db.collection("Users").document(login_intent.getStringExtra("email")).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);

                                }
                            });
                        }
                    }
                });


        feedbackSubmit = (MaterialButton) findViewById(R.id.feedback_submit);
        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedbackText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter a feedback.",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    Map<String, Object> new_feedback = new HashMap<>();
                    new_feedback.put("phno", login_intent.getStringExtra("phno"));
                    new_feedback.put("feedback", feedbackText.getText().toString());
                    new_feedback.put("name", login_intent.getStringExtra("name"));
                    new_feedback.put("email", login_intent.getStringExtra("email"));
                    new_feedback.put("addr", login_intent.getStringExtra("addr"));
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String dateTime = dateFormat.format(date);

                    db.collection("Users").document(login_intent.getStringExtra("email")).collection("feedback")
                            .document(dateTime)
                            .set(new_feedback)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toast.makeText(getApplicationContext(),
                                            "Thank you for your feedback!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    Toast.makeText(getApplicationContext(),
                                            "There was an error submitting your feedback.",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            });

                    feedbackText.getText().clear();
                    rateApp();
                }
            }
        });

    }

    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public void flipperAds(){
        adflipper = findViewById(R.id.ad_flipper_feedback);
        adflipper.setFlipInterval(2000);  // 2 sec
        adflipper.setInAnimation(this, android.R.anim.slide_in_left);
        adflipper.setOutAnimation(this, android.R.anim.slide_out_right);
        adflipper.startFlipping();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_home){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", login_intent.getStringExtra("name"));
            intent.putExtra("email", login_intent.getStringExtra("email"));
            intent.putExtra("phno", login_intent.getStringExtra("phno"));
            intent.putExtra("addr", login_intent.getStringExtra("addr"));
            startActivity(intent);
            mDrawerLayout.closeDrawer(Gravity.RIGHT, false);
        }

        else if (item.getItemId() == R.id.menu_shop){
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("name", login_intent.getStringExtra("name"));
            intent.putExtra("email", login_intent.getStringExtra("email"));
            intent.putExtra("phno", login_intent.getStringExtra("phno"));
            intent.putExtra("addr", login_intent.getStringExtra("addr"));
            startActivity(intent);
            mDrawerLayout.closeDrawer(Gravity.RIGHT, false);
        }

        else if (item.getItemId() == R.id.menu_feedback){
            Intent intent = new Intent(this, FeedbackActivity.class);
            intent.putExtra("name", login_intent.getStringExtra("name"));
            intent.putExtra("email", login_intent.getStringExtra("email"));
            intent.putExtra("phno", login_intent.getStringExtra("phno"));
            intent.putExtra("addr", login_intent.getStringExtra("addr"));
            startActivity(intent);
            mDrawerLayout.closeDrawer(Gravity.RIGHT, false);

        }
        return true;
    }
}