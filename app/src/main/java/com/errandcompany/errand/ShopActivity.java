package com.errandcompany.errand;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShopActivity extends AppCompatActivity{

    private static final String TAG = ShopActivity.class.getName();
    private BottomNavigationView bottomNavigationView;
    private Toolbar mToolbar;
    LinearLayout itemsLayout;
    FloatingActionButton floatingActionButton;
    EditText initEditText;
    MaterialButton placeOrder;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    HashMap<Integer, String> orders;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        orders = new HashMap<Integer, String>();
        initEditText  = (EditText) findViewById(R.id.init_item);
        itemsLayout = (LinearLayout) findViewById(R.id.shop_items_layout);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.shop_fab);
        placeOrder = (MaterialButton) findViewById(R.id.place_order);

//        AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
//        p.setScrollFlags(0);
//        mToolbar.setLayoutParams(p);

//        flipperAds();
        mToolbar = findViewById(R.id.topAppBar_shop);
        setSupportActionBar(mToolbar);

//        shopText = (TextInputEditText) findViewById(R.id.shop_text);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_shop);

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
                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
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

        final String[] totalOrder = {""};

        initEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0){
                    TextView counter = (TextView) findViewById(R.id.init_count);
                    orders.put(-1, s.toString());
                    if (Integer.parseInt(counter.getText().toString()) == 0){
                        counter.setText(Integer.toString(Integer.parseInt(counter.getText().toString()) + 1));
                    }
                }
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orders.size() == 0)
                    Toast.makeText(getApplicationContext(), "Please create your order list first", Toast.LENGTH_LONG).show();
                else{
                    for (Integer name: orders.keySet())
                        totalOrder[0] += orders.get(name) + "\n\n";
                    // start confirmation activity
                    totalOrder[0] = "";
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemCountNum = itemsLayout.getChildCount() + 1;
                final int linid = LinearLayout.generateViewId();
                final int ckid = LinearLayout.generateViewId();
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setBackgroundResource(R.drawable.lin_lay_border);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setId(linid);

                // items count TextView
                Typeface font = ResourcesCompat.getFont(getApplicationContext(), R.font.avenir);
                final TextView itemCount = new TextView(getApplicationContext());
                TextView parentItemCount = (TextView) findViewById(R.id.item_count_parent);
                LinearLayout.LayoutParams itemCountLayoutParams = (LinearLayout.LayoutParams)  parentItemCount.getLayoutParams();
                itemCount.setLayoutParams(itemCountLayoutParams);
                itemCount.setText(Integer.toString(itemCountNum));
                itemCount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                itemCount.setTypeface(font);
                linearLayout.addView(itemCount);

                // vertical line
                View lineView = new View(getApplicationContext());
                LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.line_width),
                        LinearLayout.LayoutParams.MATCH_PARENT);
                lineParams.setMargins(32, 0, 0, 0);
                lineView.setLayoutParams(lineParams);
                lineView.setBackgroundColor(getResources().getColor(R.color.black));
                linearLayout.addView(lineView);

                // EditText for items
                EditText itemEditText = new EditText(getApplicationContext());
                Typeface itemEditFont = ResourcesCompat.getFont(getApplicationContext(), R.font.avenir);
                LinearLayout.LayoutParams itemEditTextLayout = (LinearLayout.LayoutParams)  initEditText.getLayoutParams();
                itemEditText.setLayoutParams(itemEditTextLayout);
                itemEditText.setTypeface(itemEditFont);
                itemEditText.setHint("Add item name");
                itemEditText.setTextColor(getResources().getColor(R.color.black));
                itemEditText.setMaxLines(1);
                final int edtxtid = LinearLayout.generateViewId();
                itemEditText.setId(edtxtid);
                linearLayout.addView(itemEditText);

                // Adding EditText inputs to global variable
                itemEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        orders.put(edtxtid, s.toString());
                    }
                });


                final int[] countNum = {1};

                // TextView for '-' sign
                TextView removeSign = new TextView(getApplicationContext());
                TextView subtract = (TextView) findViewById(R.id.init_subtract);
                Typeface signs = ResourcesCompat.getFont(getApplicationContext(), R.font.avenir_bold);
                LinearLayout.LayoutParams removeSignLayout = (LinearLayout.LayoutParams) subtract.getLayoutParams();
                removeSign.setLayoutParams(removeSignLayout);
                removeSign.setTypeface(signs, Typeface.BOLD);
                removeSign.setText("-");
                removeSign.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                removeSign.setTextColor(getResources().getColor(R.color.signs));
                removeSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (countNum[0] > 0){
                            --countNum[0];
                            TextView tv = (TextView) itemsLayout.findViewById(ckid);
                            tv.setText(Integer.toString(countNum[0]));
                        }
                        if (countNum[0] == 0) {
                            LinearLayout linlay = (LinearLayout) itemsLayout.findViewById(linid);
                            itemsLayout.removeView(linlay);
                            orders.remove(edtxtid);
                        }
                    }
                });
                linearLayout.addView(removeSign);

                // TextView for countKeeper
                TextView countKeeper = new TextView(getApplicationContext());
                TextView counter_ = (TextView) findViewById(R.id.init_count);
                LinearLayout.LayoutParams countKeeperLayout = (LinearLayout.LayoutParams) counter_.getLayoutParams();
                countKeeper.setLayoutParams(countKeeperLayout);
                countKeeper.setTypeface(signs, Typeface.BOLD);
                countKeeper.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                countKeeper.setTextColor(getResources().getColor(R.color.signs));
                countKeeper.setText(Integer.toString(countNum[0]));
                countKeeper.setId(ckid);
                linearLayout.addView(countKeeper);

                // TextView for '+' sign
                TextView addSign = new TextView(getApplicationContext());
                TextView initAdd = (TextView) findViewById(R.id.init_add);
                LinearLayout.LayoutParams addSignLayout = (LinearLayout.LayoutParams) initAdd.getLayoutParams();
                addSign.setLayoutParams(addSignLayout);
                addSign.setText("+");
                addSign.setTypeface(signs, Typeface.BOLD);
                addSign.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 23);
                addSign.setTextColor(getResources().getColor(R.color.signs));
                addSign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ++countNum[0];
                        TextView tv = (TextView) itemsLayout.findViewById(ckid);
                        tv.setText(Integer.toString(countNum[0]));
                    }
                });
                linearLayout.addView(addSign);

                itemsLayout.addView(linearLayout);
                NestedScrollView scroll = (NestedScrollView) findViewById(R.id.shop_scroll_view);
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });


//        shopText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                view.getParent().requestDisallowInterceptTouchEvent(true);
//                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
//                    case MotionEvent.ACTION_UP:
//                        view.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//
//                return false;
//            }
//
//        });

        // Add new user to Firestore database
//        db.collection("Users").document(login_intent.getStringExtra("phno")).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (!task.getResult().exists()){
//                            Map<String, Object> user = new HashMap<>();
//                            user.put("phno", login_intent.getStringExtra("phno"));
//                            user.put("name", login_intent.getStringExtra("name"));
//                            user.put("email", login_intent.getStringExtra("email"));
//                            user.put("addr", login_intent.getStringExtra("addr"));
//                            db.collection("Users").document(login_intent.getStringExtra("phno")).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error writing document", e);
//                                }
//                            });
//                        }
//                    }
//                });

//        shop_button = (MaterialButton) findViewById(R.id.shop_submit);
//        shop_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (shopText.getText().toString().isEmpty()) {
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter your order details",
//                            Toast.LENGTH_LONG)
//                            .show();
//                }
//                else {
//                    Map<String, Object> new_order = new HashMap<>();
//                    new_order.put("phno", login_intent.getStringExtra("phno"));
//                    new_order.put("order_details", shopText.getText().toString());
//                    new_order.put("name", login_intent.getStringExtra("name"));
//                    new_order.put("email", login_intent.getStringExtra("email"));
//                    new_order.put("addr", login_intent.getStringExtra("addr"));
//                    Date date = Calendar.getInstance().getTime();
//                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//                    String dateTime = dateFormat.format(date);
//
//                    db.collection("Users").document(login_intent.getStringExtra("phno")).collection("active_orders")
//                            .document(dateTime)
//                            .set(new_order)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                    Toast.makeText(getApplicationContext(),
//                                            "Order Success!",
//                                            Toast.LENGTH_LONG)
//                                            .show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.w(TAG, "Error writing document", e);
//                                    Toast.makeText(getApplicationContext(),
//                                            "There was an error submitting your order.",
//                                            Toast.LENGTH_LONG)
//                                            .show();
//                                }
//                            });
//
//                    shopText.getText().clear();
//
//
//                }
//            }
//        });

    }

//    public void flipperAds(){
//        adflipper = findViewById(R.id.ad_flipper_shop);
//        adflipper.setFlipInterval(2000);  // 2 sec
//        adflipper.setInAnimation(this, android.R.anim.slide_in_left);
//        adflipper.setOutAnimation(this, android.R.anim.slide_out_right);
//        adflipper.startFlipping();
//    }

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

    public void init_remove(View view){
        TextView counter = (TextView) findViewById(R.id.init_count);
        if (Integer.parseInt(counter.getText().toString()) > 0)
            counter.setText(Integer.toString(Integer.parseInt(counter.getText().toString()) - 1));

        if (Integer.parseInt(counter.getText().toString()) == 0){
            initEditText.getText().clear();
            orders.remove(-1);
        }

    }

    public void init_add(View view){
        TextView counter = (TextView) findViewById(R.id.init_count);
        counter.setText(Integer.toString(Integer.parseInt(counter.getText().toString()) + 1));
    }

    public void shop_info(View view){
        final Dialog shopInfoPopup = new Dialog(this);
        shopInfoPopup.setContentView(R.layout.shop_info_popup);
        TextView closeInfo = (TextView) shopInfoPopup.findViewById(R.id.cancel_shop_info);
        closeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopInfoPopup.dismiss();
            }
        });
        shopInfoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        shopInfoPopup.show();


    }

}
