package com.errandcompany.errand;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView bottomNavigationView;
    ViewFlipper adflipper;
    private Toolbar mToolbar;
    Dialog shop_popup;
    private Intent login_intent;
    private MaterialButton shopButton;
    private MaterialButton acRepair;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_intent = getIntent();
        flipperAds();
        mToolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(mToolbar);
        shopButton = (MaterialButton) findViewById(R.id.shop_button);
        acRepair = (MaterialButton) findViewById(R.id.ac_repair);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_main);
        shop_popup = new Dialog(this);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

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

        acRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("name", login_intent.getStringExtra("name"));
                intent.putExtra("email", login_intent.getStringExtra("email"));
                intent.putExtra("phno", login_intent.getStringExtra("phno"));
                intent.putExtra("addr", login_intent.getStringExtra("addr"));
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", login_intent.getStringExtra("name"));
                    intent.putExtra("email", login_intent.getStringExtra("email"));
                    intent.putExtra("phno", login_intent.getStringExtra("phno"));
                    intent.putExtra("addr", login_intent.getStringExtra("addr"));
                    startActivity(intent);
                    finish();
                }

                else if (item.getItemId() == R.id.menu_shop){
                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                    intent.putExtra("name", login_intent.getStringExtra("name"));
                    intent.putExtra("email", login_intent.getStringExtra("email"));
                    intent.putExtra("phno", login_intent.getStringExtra("phno"));
                    intent.putExtra("addr", login_intent.getStringExtra("addr"));
                    shop_popup.setContentView(R.layout.shop_popup);
                    TextView closeShop = (TextView) shop_popup.findViewById(R.id.cancel_shop_popup);
                    closeShop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shop_popup.dismiss();
                        }
                    });
                    shop_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    shop_popup.show();
//                    startActivity(intent);
                }

                else if (item.getItemId() == R.id.menu_feedback){
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    intent.putExtra("name", login_intent.getStringExtra("name"));
                    intent.putExtra("email", login_intent.getStringExtra("email"));
                    intent.putExtra("phno", login_intent.getStringExtra("phno"));
                    intent.putExtra("addr", login_intent.getStringExtra("addr"));
                    startActivity(intent);
                }


                return true;
            }
        });

    }

    public void parlour(View view){
        final Intent intent = new Intent(this, LifestyleServices.class);
        intent.putExtra("name", login_intent.getStringExtra("name"));
        intent.putExtra("email", login_intent.getStringExtra("email"));
        intent.putExtra("phno", login_intent.getStringExtra("phno"));
        intent.putExtra("addr", login_intent.getStringExtra("addr"));
        startActivity(intent);
    }

    public void grooming(View view){
        final Intent intent = new Intent(this, LifestyleServices.class);
        intent.putExtra("name", login_intent.getStringExtra("name"));
        intent.putExtra("email", login_intent.getStringExtra("email"));
        intent.putExtra("phno", login_intent.getStringExtra("phno"));
        intent.putExtra("addr", login_intent.getStringExtra("addr"));
        startActivity(intent);
    }

    public void grocery(View view){
        final Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("name", login_intent.getStringExtra("name"));
        intent.putExtra("email", login_intent.getStringExtra("email"));
        intent.putExtra("phno", login_intent.getStringExtra("phno"));
        intent.putExtra("addr", login_intent.getStringExtra("addr"));
        startActivity(intent);
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


    public void feedback_submit_button(View view){
        rateApp();
    }

    public void fresh(View view){
        final Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("name", login_intent.getStringExtra("name"));
        intent.putExtra("email", login_intent.getStringExtra("email"));
        intent.putExtra("phno", login_intent.getStringExtra("phno"));
        intent.putExtra("addr", login_intent.getStringExtra("addr"));
        startActivity(intent);
    }

    public void office(View view){
        final Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("name", login_intent.getStringExtra("name"));
        intent.putExtra("email", login_intent.getStringExtra("email"));
        intent.putExtra("phno", login_intent.getStringExtra("phno"));
        intent.putExtra("addr", login_intent.getStringExtra("addr"));
        startActivity(intent);
    }

    public void other_services(View view){
        final Intent intent = new Intent(this, ShopActivity.class);
        intent.putExtra("name", login_intent.getStringExtra("name"));
        intent.putExtra("email", login_intent.getStringExtra("email"));
        intent.putExtra("phno", login_intent.getStringExtra("phno"));
        intent.putExtra("addr", login_intent.getStringExtra("addr"));
        startActivity(intent);
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

}
