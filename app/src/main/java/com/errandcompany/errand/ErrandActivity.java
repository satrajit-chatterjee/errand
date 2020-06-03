package com.errandcompany.errand;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Objects;

public class ErrandActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    final String[] addr = {""};
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errand_activity);

        mToolbar = findViewById(R.id.topAppBar_shop);
        setSupportActionBar(mToolbar);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_errand);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                else if (item.getItemId() == R.id.menu_shop){
                    Intent intent = new Intent(getApplicationContext(), ErrandActivity.class);
                    startActivity(intent);
                    finish();
                }

                else if (item.getItemId() == R.id.menu_feedback){
                    Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
                    startActivity(intent);
                    finish();
                }


                return true;
            }
        });

        db = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get user details
        db.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    addr[0] = documentSnapshot.get("addr").toString();

                    TextView addrView = (TextView) findViewById(R.id.errand_address);
                    addrView.setText(addr[0]);
                }
            }
        });

        EditText dropOff = (EditText) findViewById(R.id.optional_address);
        String optionalAddress = dropOff.getText().toString();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    public void pickanddrop(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.shop_info_popup);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_shop_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Pick Up & Drop");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("Forgot something at home? Need something picked up and delivered to you? Set your pick-up and drop-off locations, then give a short description of what you would like us to do.");
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

}
