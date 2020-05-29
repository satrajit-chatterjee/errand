package com.errandcompany.errand;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    protected boolean gps_enabled,network_enabled;




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
        getCurrentLocation();

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


    public void performSignUp(double lati, double lono){
        final Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        final EditText first_name = (EditText) findViewById(R.id.tv_first_name);
        final EditText last_name = (EditText) findViewById(R.id.last_name);
        final EditText phoneno = (EditText)findViewById(R.id.ph_no);
        final Button btnSubmit = (Button) findViewById(R.id.button);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        geocoder = new Geocoder(SplashScreen.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lati, lono, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        final String address = addresses.get(0).getAddressLine(0);
        final String area = addresses.get(0).getLocality();
        final String city = addresses.get(0).getAdminArea();
        final String country = addresses.get(0).getCountryName();
        final String postalcode = addresses.get(0).getPostalCode();
        final String getAddr = address + ", " + area + ", " + city + ", " + postalcode;

        if (currentUser != null){
            intent.putExtra("name", currentUser.getDisplayName());
            intent.putExtra("phno", currentUser.getPhoneNumber());
            intent.putExtra("addr", getAddr);
            startActivity(intent);
            finish();
        }

        else {
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String getName = first_name.getText().toString() + " " + last_name.getText().toString();
                    final String getPhNo = phoneno.getText().toString();

                    if (first_name.getText().toString().isEmpty() || last_name.getText().toString().isEmpty() ||
                            phoneno.getText().toString().isEmpty() || phoneno.getText().toString().length() != 10) {
                        Toast.makeText(getApplicationContext(),
                                "Please check entered details",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                    else {
                        intent.putExtra("name", getName);
                        intent.putExtra("phno", getPhNo);
                        intent.putExtra("addr", getAddr);
                        if (currentUser == null){
                            requestOTP(getPhNo, intent);
                        }
                        else Toast.makeText(getApplicationContext(),
                                "Error in fetching current user state",
                                Toast.LENGTH_SHORT)
                                .show();

                    }
                }
            });
        }
    }

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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private boolean haveLocationConnection() {
        boolean isConnected = true;
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception ex){}
        try{
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception ex){}
        if(!gps_enabled && !network_enabled){
            isConnected = false;
        }
        return isConnected;
    }

    private void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        builder.setMessage("Connect to wifi or quit")
                .setCancelable(false)
                .setPositiveButton("Connect to WIFI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(),
                                "Close the Application",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void getCurrentLocation(){
        if(haveLocationConnection() && haveNetworkConnection()){
            if (ContextCompat.checkSelfPermission(
                    SplashScreen.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
            }
            else{
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

        }
        else if (!haveLocationConnection() || !haveNetworkConnection()){
            Toast.makeText(getApplicationContext(),
                    "Check if internet and location and try again",
                    Toast.LENGTH_LONG)
                    .show();
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 66);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 66)
            getCurrentLocation();
    }

    public void ShowPopup(Intent i, final Editable editable){
        final Intent intent = i;
        MaterialButton confirm;
        confirm = (MaterialButton) newAcc.findViewById(R.id.confirm_acc);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, editable.toString());
                firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SplashScreen.this,
                                    "Phone Number verified\nLogged in as " + intent.getStringExtra("name"),
                                    Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(SplashScreen.this,
                                    "Verification failed. Please recheck entered details",
                                    Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void requestOTP(String phone, final Intent i) {
        newAcc.setContentView(R.layout.new_acc_popup);
        TextView txtClose = (TextView) newAcc.findViewById(R.id.cancel_popup);
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAcc.dismiss();
            }
        });
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + phone, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationId = s;
                token = forceResendingToken;
                OtpEditText otpEditText = (OtpEditText) newAcc.findViewById(R.id.verify_otp);
                Toast.makeText(SplashScreen.this, "Sending OTP",
                        Toast.LENGTH_SHORT).show();
                otpEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().length() == 6){
                            ShowPopup(i, editable);

                        }
                    }
                });
                newAcc.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                newAcc.show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(SplashScreen.this,
                        "Verification failed. Please recheck entered number" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
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