package srinivas.sams.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import srinivas.sams.R;
import srinivas.sams.helper.Preferences;

public class Home extends Activity {
    @BindView(R.id.crewnames_tv)
    TextView crewnames_tv;
    @BindView(R.id.lastSync_tv)
    TextView lastSync_tv;
    @BindView(R.id.crewpersonnames_tv)
    TextView crewpersonnames_tv;
    SharedPreferences ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        clearPreferences();
        ss = getSharedPreferences("allow",MODE_PRIVATE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String format = simpleDateFormat.format(new Date());
        lastSync_tv.setText("Last Sync : "+format.toString());
        crewnames_tv.setText(Preferences.getVendorname(Home.this));
        crewpersonnames_tv.setText("Crew Name : "+Preferences.getCrewPersonname(Home.this));

    }

    @OnClick({ R.id.Recce_btn, R.id.Installtion_btn, R.id.Sync_btn ,R.id.logout_btn })
    public void buttonClicks(View view) {

        switch(view.getId()) {

            case R.id.Recce_btn:
               /* Intent i = new Intent(Home.this,Recces_display.class);
                startActivity(i);*/

               if (ss.getString("status","").equals("true")){
                   Preferences.setSelection("RECCES",Home.this);
                   Intent i = new Intent(Home.this,Project.class);
                   startActivity(i);
               }else {
                   Intent notallow = new Intent(Home.this, Notallow.class);
                   startActivity(notallow);
                   finish();
               }
                break;

            case R.id.Installtion_btn:
                if (ss.getString("status","").equals("true")){
                    Preferences.setSelection("INSTALLATIONS",Home.this);
                    Intent instal = new Intent(Home.this,Project.class);
                    startActivity(instal);

                }else {
                    Intent notallow = new Intent(Home.this, Notallow.class);
                    startActivity(notallow);
                    finish();
                }
                break;

            case R.id.Sync_btn:
                if (ss.getString("status","").equals("true")){
                    Intent sync = new Intent(Home.this,Sync.class);
                    startActivity(sync);
                }else {
                    Intent notallow = new Intent(Home.this, Notallow.class);
                    startActivity(notallow);
                    finish();
                }
                break;
            case R.id.logout_btn:
                Preferences.setVendor("login", "", "", Home.this);
                Intent login = new Intent(Home.this,Login.class);
                startActivity(login);
                 finish();
                break;

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
