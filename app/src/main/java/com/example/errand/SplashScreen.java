package com.example.errand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashScreen extends AppCompatActivity{

    private static final String TAG = SplashScreen.class.getName();
    private boolean isFirstAnimation = false;
    RelativeLayout rellay1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
        }
    };
    Dialog newAcc;
    FirebaseAuth firebaseAuth;
    Geocoder geocoder;
    List<Address> addresses;
    final int PERMISSION_ID = 1;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    public static final String MyPREFERENCES = "MyPrefs" ;
    LocationManager locationManager;
    LocationListener locationListener;
    String provider;
    protected boolean gps_enabled,network_enabled;


    public static double latitude;
    public static double longitude;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_APPEND);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (ContextCompat.checkSelfPermission(
                SplashScreen.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
        } else {
           getCurrentLocation();
        }

        newAcc = new Dialog(this);

        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);

        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);

        final ImageView imageView = (ImageView) findViewById(R.id.imgView_logo);
        Glide
                .with(this)
                .load(R.drawable.errand_logo_white)
                        .into(imageView);

        translateScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation();
                    rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
                    handler.postDelayed(runnable, 2000); // 2000 is the timeout for the splash
                }

                isFirstAnimation = true;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hold.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.clearAnimation();
                imageView.startAnimation(translateScale);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(hold);

    }

    public void performSignUp(double lat, double lon){
        double lati = lat;
        double lono = lon;
        final Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lati, lono, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        final EditText first_name = (EditText) findViewById(R.id.tv_first_name);
        final EditText last_name = (EditText) findViewById(R.id.last_name);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText phoneno = (EditText)findViewById(R.id.ph_no);
        final EditText password = (EditText)findViewById(R.id.password);
        final Button btnSubmit = (Button) findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        final String address = addresses.get(0).getAddressLine(0);
        final String area = addresses.get(0).getLocality();
        final String city = addresses.get(0).getAdminArea();
        final String country = addresses.get(0).getCountryName();
        final String postalcode = addresses.get(0).getPostalCode();

        final String getAddr = address + ", " + area + ", " + city + ", " + postalcode;;
        Toast.makeText(getApplicationContext(),
                getAddr + ", yolo",
                Toast.LENGTH_LONG)
                .show();


        if (currentUser != null){
//            fetch user data from db by email
            DocumentReference docRef = db.collection("Users").document(currentUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            intent.putExtra("name", (String) document.get("name"));
                            intent.putExtra("email", (String) document.get("email"));
                            intent.putExtra("phno", (String) document.get("phno"));
                            intent.putExtra("addr", getAddr);
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                    else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            startActivity(intent);
        }
        else {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String getName = first_name.getText().toString() + " " + last_name.getText().toString();
                    final String getEmail = email.getText().toString();
                    final String getPhNo = phoneno.getText().toString();
                    final String getPass = password.getText().toString();

                    if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                            phoneno.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter all details",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        intent.putExtra("name", getName);
                        intent.putExtra("email", getEmail);
                        intent.putExtra("phno", getPhNo);
                        intent.putExtra("addr", getAddr);
                        if (currentUser == null){
                            firebaseAuth.signInWithEmailAndPassword(getEmail, getPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        Toast.makeText(getApplicationContext(),
                                                "Signed in as " + getName,
                                                Toast.LENGTH_LONG)
                                                .show();
                                        startActivity(intent);
                                    }
                                    else {
                                        ShowPopup(getEmail, getPass, intent);
                                    }
                                }
                            });
                        }
                        else Toast.makeText(getApplicationContext(),
                                "Nope",
                                Toast.LENGTH_SHORT)
                                .show();

                    }
                }
            });
        }
    }

//    @SuppressLint("MissingPermission")
//    private void getLastLocation(){
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.getLastLocation().addOnSuccessListener(
//                        new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                if (location == null) {
//                                    Toast.makeText(getApplicationContext(),
//                                            "got null",
//                                            Toast.LENGTH_SHORT)
//                                            .show();
//                                    requestNewLocationData();
//                                } else if(location != null) {
//                                    getLocationCoor(location.getLatitude(), location.getLongitude());
//                                }
//                            }
//                        }
//                );
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//                requestNewLocationData();
//            }
//        } else {
//            requestPermissions();
//        }
//
//    }
//
//    public void getLocationCoor(double lat, double lon){
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("latitude", lat + "");
//        editor.putString("longitude", lon + "");
//        editor.commit();
//    }
//
//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData(){
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }
//
//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Location mLastLocation = locationResult.getLastLocation();
//            getLocationCoor(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//        }
//    };
//
//
//    private boolean checkPermissions(){
//        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions(){
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID
//        );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                // Granted. Start getting the location information
//            }
//        }
//    }
//
//    private boolean isLocationEnabled(){
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }

//    private void OnGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

//    @SuppressLint("MissingPermission")



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We now have permission to use the location
                getCurrentLocation();
            } else {
                Toast.makeText(SplashScreen.this, "Permission denied!", Toast.LENGTH_SHORT)
                            .show();
            }
        }
    }

    private void getCurrentLocation(){
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.getFusedLocationProviderClient(SplashScreen.this)
                .requestLocationUpdates(locationRequest, new LocationCallback(){
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(SplashScreen.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0){
                            int latestlocationindex = locationResult.getLocations().size() - 1;
                            double lat = locationResult.getLocations().get(latestlocationindex).getLatitude();
                            double lon = locationResult.getLocations().get(latestlocationindex).getLongitude();
                            performSignUp(lat, lon);
                        }
                    }
                }, Looper.getMainLooper());

    }

    public void ShowPopup(String email, String pass, Intent i){
        final String emailId = email;
        final String password = pass;
        final Intent intent = i;
        TextView txtClose;
        MaterialButton confirm;
        newAcc.setContentView(R.layout.new_acc_popup);
        txtClose = (TextView) newAcc.findViewById(R.id.cancel_popup);
        confirm = (MaterialButton) newAcc.findViewById(R.id.confirm_acc);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAcc.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            // Add new user to Firestore database
                            db.collection("Users").document(intent.getStringExtra("phno")).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (!task.getResult().exists()){
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("phno", intent.getStringExtra("phno"));
                                                user.put("name", intent.getStringExtra("name"));
                                                user.put("email", intent.getStringExtra("email"));
                                                user.put("addr", intent.getStringExtra("addr"));  // Account creation address to be cross-referenced with current address ALWAYS
                                                db.collection("Users").document(intent.getStringExtra("phno")).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error writing document", e);
                                                    }
                                                });
                                            }
                                        }
                                    });
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(SplashScreen.this, "Account Created!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(SplashScreen.this, "Authentication failed!",
                                    Toast.LENGTH_SHORT).show();
                            newAcc.dismiss();
                        }
                    }
                });
            }
        });
        newAcc.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newAcc.show();
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

}