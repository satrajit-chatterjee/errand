package com.errandcompany.errand;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Objects;

public class ErrandActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    final String[] addr = {""};
    MaterialButton placeOrder;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    Dialog shop_popup;

    private MaterialButton popupShop;
    private MaterialButton popupErrand;
    private MaterialButton popupLifesyle;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.errand_activity);

        mToolbar = findViewById(R.id.topAppBar_shop);
        setSupportActionBar(mToolbar);
        shop_popup = new Dialog(this);

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
                    shop_popup.setContentView(R.layout.shop_popup);
                    popupShop = (MaterialButton) shop_popup.findViewById(R.id.shop_order_button);
                    popupErrand = (MaterialButton) shop_popup.findViewById(R.id.errand_order_button);
                    popupLifesyle = (MaterialButton) shop_popup.findViewById(R.id.lifestyle_order_button);
                    TextView closeShop = (TextView) shop_popup.findViewById(R.id.cancel_shop_popup);
                    closeShop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shop_popup.dismiss();
                        }
                    });

                    popupShop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    popupErrand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getApplicationContext(), ErrandActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    popupLifesyle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Intent intent = new Intent(getApplicationContext(), LifestyleServices.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    shop_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    shop_popup.show();
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

                    EditText addrView = (EditText) findViewById(R.id.errand_address);
                    addrView.setText(addr[0]);
                }
            }
        });

        placeOrder = (MaterialButton) findViewById(R.id.set_errand);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText dropOff = (EditText) findViewById(R.id.optional_address);
                final String optionalAddress = dropOff.getText().toString().trim();

                EditText errandDesc = (EditText) findViewById(R.id.errand_desc);
                final String[] errandOrder = {errandDesc.getText().toString().trim()};
                if (errandOrder[0].length() == 0)
                    Toast.makeText(getApplicationContext(), "Please give us a description of our errand first", Toast.LENGTH_LONG).show();
                else{
                    // start confirmation activity
                    if (optionalAddress.length() > 0)
                        errandOrder[0] = "Drop-off / Destination\n" + optionalAddress + "\n\nErrand Description\n\n" + errandOrder[0];
                    EditText addrView = (EditText) findViewById(R.id.errand_address);
                    Intent intent = new Intent(getApplicationContext(), ConfirmOrder.class);
                    intent.putExtra("home_address", addrView.getText().toString());
                    intent.putExtra("order_details", errandOrder[0]);
                    startActivity(intent);
                }
            }
        });

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
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
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
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }


    public void office_sanitization(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Office Sanitization");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("Opening after the Lockdown? Get your office sanitized by certified professionals. Place an order today!");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void medicine(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Medicine Delivery");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("We collect your prescription and deliver your medicines right to your doorstep!");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void blood_test(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Blood Test & Diagnostics");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("Have your blood tested, book appointments for X-Rays, Ultrasounds, and all kinds of other tests");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void courier(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("National & International Courier");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("We provide national and international courier services with our affiliate partners - Blue Dart, DTDC, The Professional Courier (Domestic) and DHL (International)");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void books(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Documents and Books");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("Buy books and have them delivered to your doorstep. Need documents picked up or delivered? Place an order with us.");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void pet_supplies(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Pet Supplies");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("We deliver all kinds of pet supplies. Place an order and have them delivered to your doorstep!");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void others(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Anything Else");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("Don't see what you need? Don't worry. Type out what you want and place an order with us. We'll call you right away to confirm your request! ");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
    }

    public void errand_info(View view){
        final Dialog errandInfoPopup = new Dialog(this);
        errandInfoPopup.setContentView(R.layout.errand_info);
        TextView closeInfo = (TextView) errandInfoPopup.findViewById(R.id.cancel_errand_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errandInfoPopup.dismiss();
            }
        });
        TextView pickTitle = (TextView) errandInfoPopup.findViewById(R.id.info_title);
        pickTitle.setText("Errand Fees");
        TextView pickDesc = (TextView) errandInfoPopup.findViewById(R.id.errand_info_desc);
        pickDesc.setText("Pick up and delivery fees start at Rs. 24 for the first 2 kms and Rs. 10 for every additional km. To know the rates of our other services, place an order to get a quotation (negotiable).");
        pickDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        errandInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        errandInfoPopup.show();
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
