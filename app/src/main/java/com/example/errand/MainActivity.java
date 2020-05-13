package com.example.errand;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MainAdapter.OnShopListener {

    ViewFlipper adflipper;
    ViewFlipper serflipper;
    private Toolbar mToolbar;
    private ArrayList<ModelMain> mList;
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int service_images[] = {R.drawable.cosmetics, R.drawable.courier, R.drawable.grocery, R.drawable.medicine,
                R.drawable.photo, R.drawable.ppe, R.drawable.stationery};

        serflipper = (ViewFlipper) findViewById(R.id.service_flipper);

        for (int image: service_images){
            flipperServices(image);
        }
        flipperAds();
        mToolbar = findViewById(R.id.topAppBar);

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        mList = new ArrayList<>();
        // TODO: Add garbage


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLayoutmanager = layoutManager;

        recyclerView.setLayoutManager(rvLayoutmanager);
        mRecyclerAdapter = new MainAdapter(context, mList, this);
        recyclerView.setAdapter(mRecyclerAdapter);
    }

    public void flipperServices(int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);
        serflipper.addView(imageView);
        serflipper.setFlipInterval(2500);
        serflipper.setAutoStart(true);

        serflipper.setInAnimation(this, android.R.anim.slide_in_left);
        serflipper.setOutAnimation(this, android.R.anim.slide_out_right);
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
        return true;
    }

    @Override
    public void onShopClick(int position) {
        Intent intent = new Intent(this, ShopActivity.class);
        startActivity(intent);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }
}
