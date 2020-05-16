package com.example.errand;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = ShopActivity.class.getName();

    private DrawerLayout mDrawerLayout;
    private TextInputEditText shopText;
    private ActionBarDrawerToggle mToggle;
    private ImageButton nav_button;
    ViewFlipper adflipper;
    private Toolbar mToolbar;
    NavigationView navView;
    private MaterialButton shop_button;
    private Intent login_intent;
    private FirebaseFirestore db;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        db = FirebaseFirestore.getInstance();

        flipperAds();
        mToolbar = findViewById(R.id.topAppBar_shop);
        setSupportActionBar(mToolbar);
        nav_button = (ImageButton) findViewById(R.id.nav_button_shop);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_shop);
        navView = findViewById(R.id.nav_view);

        shopText = (TextInputEditText) findViewById(R.id.shop_text);

        navView.setNavigationItemSelectedListener(this);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.setDrawerIndicatorEnabled(true);
        mToggle.syncState();
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END)){
                    mDrawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });


        shopText.setOnTouchListener(new View.OnTouchListener() {
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


        // Add new user to Firestore database
        db.collection("Users").document(login_intent.getStringExtra("phno")).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.getResult().exists()){
                            Map<String, Object> user = new HashMap<>();
                            user.put("fname", login_intent.getStringExtra("phno"));
                            user.put("fname", login_intent.getStringExtra("fname"));
                            user.put("fname", login_intent.getStringExtra("lname"));
                            user.put("fname", login_intent.getStringExtra("email"));
                            user.put("fname", login_intent.getStringExtra("addr"));
                            db.collection("Users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                        }
                    }
                });


        shop_button = (MaterialButton) findViewById(R.id.shop_submit);
        shop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shopText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your order details",
                            Toast.LENGTH_LONG)
                            .show();
                }
                else {
                    String getFName = login_intent.getStringExtra("fname");
                    String getLName = login_intent.getStringExtra("lname");
                    String getEmail = login_intent.getStringExtra("email");
                    String getPhNo = login_intent.getStringExtra("phno");
                    String getAdd = login_intent.getStringExtra("addr");

                    Map<String, Object> new_order = new HashMap<>();
                    new_order.put("order_details", shopText.getText().toString());
                    new_order.put("timestamp", FieldValue.serverTimestamp());

                    db.collection("Users").document(login_intent.getStringExtra("phno")).collection("active_orders")
                            .document(login_intent.getStringExtra("phno"))
                            .set(new_order)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toast.makeText(getApplicationContext(),
                                            "Order Success!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });


                }
            }
        });

    }

    public void flipperAds(){
        adflipper = findViewById(R.id.ad_flipper_shop);
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
            intent.putExtra("fname", login_intent.getStringExtra("fname"));
            intent.putExtra("lname", login_intent.getStringExtra("lname"));
            intent.putExtra("email", login_intent.getStringExtra("email"));
            intent.putExtra("phno", login_intent.getStringExtra("phno"));
            intent.putExtra("addr", login_intent.getStringExtra("addr"));
            startActivity(intent);
            mDrawerLayout.closeDrawer(Gravity.RIGHT, false);
        }

        else if (item.getItemId() == R.id.menu_shop){
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("fname", login_intent.getStringExtra("fname"));
            intent.putExtra("lname", login_intent.getStringExtra("lname"));
            intent.putExtra("email", login_intent.getStringExtra("email"));
            intent.putExtra("phno", login_intent.getStringExtra("phno"));
            intent.putExtra("addr", login_intent.getStringExtra("addr"));
            startActivity(intent);
            mDrawerLayout.closeDrawer(Gravity.RIGHT, false);
        }

        else if (item.getItemId() == R.id.menu_feedback){
            Intent intent = new Intent(this, FeedbackActivity.class);
            intent.putExtra("fname", login_intent.getStringExtra("fname"));
            intent.putExtra("lname", login_intent.getStringExtra("lname"));
            intent.putExtra("email", login_intent.getStringExtra("email"));
            intent.putExtra("phno", login_intent.getStringExtra("phno"));
            intent.putExtra("addr", login_intent.getStringExtra("addr"));
            startActivity(intent);
            mDrawerLayout.closeDrawer(Gravity.RIGHT, false);

        }
        return true;
    }
}
