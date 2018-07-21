package srinivas.sams.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import srinivas.sams.Constants;
import srinivas.sams.EventItem;
import srinivas.sams.R;
import srinivas.sams.helper.DBHelper;
import srinivas.sams.helper.Preferences;
import srinivas.sams.validation.Validation;

public class SplashScreen extends Activity {
    TextView myspla;
    ImageView myschemax;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    String allow="true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        mAuth = FirebaseAuth.getInstance();


        clearPreferences();

/*
        mAuth.createUserWithEmailAndPassword("srinivasdadi9000@gmail.com", "Leeladadi@123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                        } else {
                           // Toast.makeText(SplashScreen.this, "Email Already Exists.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
*/

        myRef = FirebaseDatabase.getInstance().getReference("sams");
        // myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot issuesnapshot : dataSnapshot.getChildren()) {
                    SharedPreferences.Editor s = getSharedPreferences("allow",MODE_PRIVATE).edit();
                    Log.d("superbinloop", issuesnapshot.getValue().toString());
                    if (issuesnapshot.getValue().toString().equals("{name={-LHkcjlOSBp4SJeu2GnD={name=daditrick}}}")) {
                        allow = "true";
                        s.putString("status","true");
                        s.commit();
                    } else {
                        Log.d("superbinloop", "superra");
                        s.putString("status","false");
                        s.commit();
                        allow = "false";
                        // System.exit(0);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("superb_dataerr", databaseError.toString());
            }
        });


        /*myRef = myRef.child("First").child("name");
        String id = myRef.push().getKey();
        EventItem aUser = new EventItem("dadi Nilsson");
        myRef.child(id).setValue(aUser);
*/
        myspla = (TextView) findViewById(R.id.myspla);
        myschemax = (ImageView) findViewById(R.id.myschemax);
        Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        myschemax.startAnimation(slideUp);
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation slidezoom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.dancing);
                myschemax.startAnimation(slidezoom);
            }
        },2000);
*/

        Preferences.setProducts("notdone", SplashScreen.this);
        new DBHelper("", "", "", "", "", SplashScreen.this);
       /*
*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 0);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Preferences.setVendor("", "", "", SplashScreen.this);
                    if (allow.equals("true")) {
                        if (!Validation.internet(SplashScreen.this)) {
                            if (Preferences.getVendorid(SplashScreen.this).equals("login")) {
                                Intent i = new Intent(SplashScreen.this, Login.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SplashScreen.this, Home.class);
                                startActivity(i);
                            }
                        } else {
                            if (Preferences.getVendorid(SplashScreen.this).equals("login")) {
                                Intent i = new Intent(SplashScreen.this, Login.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SplashScreen.this, Home.class);
                                startActivity(i);
                            }
                        }

                    } else {
                        Intent notallow = new Intent(SplashScreen.this, Notallow.class);
                        startActivity(notallow);
                        finish();
                    }

                }
            }, 3500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
                && grantResults[4] == PackageManager.PERMISSION_GRANTED && grantResults[5] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Preferences.setVendor("", "", "", SplashScreen.this);
                    if (allow.equals("true")) {
                        if (Preferences.getVendorid(SplashScreen.this).equals("")) {
                            Intent i = new Intent(SplashScreen.this, Login.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SplashScreen.this, Home.class);
                            startActivity(i);
                        }

                    } else {
                        Intent notallow = new Intent(SplashScreen.this, Notallow.class);
                        startActivity(notallow);
                        finish();
                    }
                }
            }, 3500);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 0);
            }
        }
    }

    private void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear YOUR_APP_PACKAGE_GOES HERE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
