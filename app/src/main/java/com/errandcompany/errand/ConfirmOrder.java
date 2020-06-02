package com.errandcompany.errand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Type;
import java.util.List;

public class ConfirmOrder extends AppCompatActivity {

    Intent orderIntent;
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

                    TextView nameView = (TextView) findViewById(R.id.confirm_name);
                    nameView.setText(name[0]);

                    TextView addrView = (TextView) findViewById(R.id.confirm_address);
                    addrView.setText(addr[0]);

                    TextView phnoView = (TextView) findViewById(R.id.confirm_phone);
                    phnoView.setText(phno[0]);

                    TextView orderView = (TextView) findViewById(R.id.user_order);
                    String order = orderIntent.getStringExtra("order_details");
                    order = order.replace("--->", " \t\t\t");
                    orderView.setText(order);
                }
            }
        });

    }

    public void update_name(View view) {
    }
}