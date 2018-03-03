package srinivas.sams.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import srinivas.sams.R;
import srinivas.sams.helper.DBHelper;
import srinivas.sams.helper.Preferences;
import srinivas.sams.validation.Validation;

public class SplashScreen extends Activity {
    TextView myspla;
    ImageView myschemax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        clearPreferences();
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
                    if (Preferences.getVendorid(SplashScreen.this).equals("")) {
                        Intent i = new Intent(SplashScreen.this, Login.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashScreen.this, Home.class);
                        startActivity(i);
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
