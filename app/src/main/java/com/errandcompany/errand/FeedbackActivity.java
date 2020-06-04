package com.errandcompany.errand;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FeedbackActivity extends AppCompatActivity{

    Dialog feedback_popup;
    MaterialButton feedbackSubmit;
    BottomNavigationView bottomNavigationView;

    private MaterialButton popupShop;
    private MaterialButton popupErrand;
    private MaterialButton popupLifesyle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_popup = new Dialog(this);
        feedbackSubmit = (MaterialButton) findViewById(R.id.submit_feedback);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_feedback);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                else if (item.getItemId() == R.id.menu_shop){
                    feedback_popup.setContentView(R.layout.shop_popup);
                    popupShop = (MaterialButton) feedback_popup.findViewById(R.id.shop_order_button);
                    popupErrand = (MaterialButton) feedback_popup.findViewById(R.id.errand_order_button);
                    popupLifesyle = (MaterialButton) feedback_popup.findViewById(R.id.lifestyle_order_button);
                    TextView closeShop = (TextView) feedback_popup.findViewById(R.id.cancel_shop_popup);
                    closeShop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            feedback_popup.dismiss();
                        }
                    });

                    popupShop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                            startActivity(intent);
                        }
                    });

                    popupErrand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getApplicationContext(), ErrandActivity.class);
                            startActivity(intent);
                        }
                    });

                    popupLifesyle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getApplicationContext(), LifestyleServices.class);
                            startActivity(intent);
                        }
                    });
                    feedback_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    feedback_popup.show();
                }

                else if (item.getItemId() == R.id.menu_feedback){
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });

        final RatingBar ratingBar = findViewById(R.id.rating);
        final float[] givenRating = {0};
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                givenRating[0] = rating;
            }
        });

        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText feedbackText = (EditText) findViewById(R.id.feedback_desc);
                final String feedback = feedbackText.getText().toString().trim();
                if (feedback.length() > 0 && givenRating[0] != 0.0){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    db.collection("Users").document(currentUser.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.getResult().exists()){
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        String name = documentSnapshot.get("name").toString();
                                        String phno = documentSnapshot.get("phno").toString();
                                        String address = documentSnapshot.get("addr").toString();

                                        Map<String, Object> user = new HashMap<>();
                                        user.put("feedback", feedback + "\n\nRating = " + givenRating[0]);
                                        user.put("name", name);
                                        user.put("phno", phno);
                                        user.put("addr", address);
                                        Date date = Calendar.getInstance().getTime();
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                                        String dateTime = dateFormat.format(date);
                                        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                        final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("Users").document(currentUser.getUid()).collection("Feedback").document(dateTime).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Thank you for your feedback!",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                                rateApp();
                                                feedbackText.getText().clear();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),
                                                        "There was an error submitting your feedback.",
                                                        Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        });
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(getApplicationContext(), "Please enter a feedback",
                            Toast.LENGTH_LONG).show();
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(event);
    }

}