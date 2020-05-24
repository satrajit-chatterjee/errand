package com.example.errand;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ImageButton nav_button;
    ViewFlipper adflipper;
    private Toolbar mToolbar;
    NavigationView navView;
    private Intent login_intent;
    private MaterialButton shopButton;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_intent = getIntent();
        flipperAds();
        mToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);
        nav_button = (ImageButton) findViewById(R.id.nav_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        shopButton = (MaterialButton) findViewById(R.id.shop_button);
        navView = findViewById(R.id.nav_view);
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

        final Intent intent = new Intent(this, ShopActivity.class);


        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("name", login_intent.getStringExtra("name"));
                intent.putExtra("email", login_intent.getStringExtra("email"));
                intent.putExtra("phno", login_intent.getStringExtra("phno"));
                intent.putExtra("addr", login_intent.getStringExtra("addr"));
                startActivity(intent);
            }
        });
    }

    public void flipperAds(){
        adflipper = findViewById(R.id.ad_flipper);
        adflipper.setFlipInterval(2000);  // 2 sec
        adflipper.setInAnimation(this, android.R.anim.slide_in_left);
        adflipper.setOutAnimation(this, android.R.anim.slide_out_right);
        adflipper.startFlipping();
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
