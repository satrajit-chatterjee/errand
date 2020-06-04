package com.errandcompany.errand;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrder extends AppCompatActivity {

    Intent orderIntent;
    MaterialButton placeOrder;
    private FirebaseFirestore db;
    final String[] name = {""};
    final String[] addr = {""};
    final String[] phno = {""};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);

        orderIntent = getIntent();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get user details
        db.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    name[0] = documentSnapshot.get("name").toString();
                    addr[0] = documentSnapshot.get("addr").toString();
                    phno[0] = documentSnapshot.get("phno").toString();

                    EditText nameView = (EditText) findViewById(R.id.confirm_name);
                    nameView.setText(name[0]);

                    EditText addrView = (EditText) findViewById(R.id.confirm_address);
                    if (orderIntent.getStringExtra("home_address") != null)
                        addrView.setText(orderIntent.getStringExtra("home_address"));
                    else
                        addrView.setText(addr[0]);

                    EditText phnoView = (EditText) findViewById(R.id.confirm_phone);
                    phnoView.setText(phno[0]);

                    TextView orderView = (TextView) findViewById(R.id.user_order);
                    String order = orderIntent.getStringExtra("order_details");
                    order = order.replace("--->", " \t\t\t");
                    orderView.setText(order);
                    orderView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                }
            }
        });

        placeOrder = (MaterialButton) findViewById(R.id.final_confirm);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                db.collection("Users").document(currentUser.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult().exists()){
                                    DocumentSnapshot documentSnapshot = task.getResult();

                                    EditText nameV = (EditText) findViewById(R.id.confirm_name);
                                    String name = nameV.getText().toString();

                                    EditText phoneV = (EditText) findViewById(R.id.confirm_phone);
                                    String phno = phoneV.getText().toString();

                                    EditText addressV = (EditText) findViewById(R.id.confirm_address);
                                    String deliveryAddr = addressV.getText().toString();

                                    Intent orderIntent = getIntent();
                                    String order = orderIntent.getStringExtra("order_details");
                                    order = order.replace("--->", " \t\t\t");

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("order_details", order);
                                    user.put("addr", deliveryAddr);
                                    user.put("name", name);
                                    user.put("phno", phno);
                                    Date date = Calendar.getInstance().getTime();
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                                    String dateTime = dateFormat.format(date);
                                    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("Users").document(currentUser.getUid()).collection("Orders").document(dateTime).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Your order has been placed!",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                            orderStatus(true);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(),
                                                    "There was an error confirming your order.",
                                                    Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }
                            }
                        });
            }
        });

    }

    public void orderStatus(boolean order){
        final Dialog showConfirmed = new Dialog(this);

        if (order){
            showConfirmed.setContentView(R.layout.confirmed_popup);
            MaterialButton done = (MaterialButton) showConfirmed.findViewById(R.id.confirmed_button);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    showConfirmed.dismiss();
                    finish();
                }
            });
            TextView closeShop = (TextView) showConfirmed.findViewById(R.id.cancel_confirmed_popup);
            closeShop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showConfirmed.dismiss();
                }
            });

            showConfirmed.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            showConfirmed.show();
        }
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