package srinivas.sams.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivas.sams.Adapter.Login_spinner;
import srinivas.sams.R;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.Appopen;
import srinivas.sams.model.Login_Service;
import srinivas.sams.model.Vendor;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Login extends Activity {
    ArrayList<String> items;
    Login_spinner login_spinner;
    List<Vendor> vendors;
    @BindView(R.id.spinner_login)
    Spinner spinner_login;
    @BindView(R.id.login_btn)
    Button login_btn;
    String validation_vendor_name = null;
    TelephonyManager manager;
    String imenumber1 = null, imenumber2 = null;
    // GPSTracker class


    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        //Preferences.setProject("key","65","2");


        //getProjects();
        //getRecceslist();
        // getInstalllist();
        manager = (TelephonyManager) getSystemService(Login.TELEPHONY_SERVICE);
        imenumber1 = manager.getDeviceId(0);
        imenumber2 = manager.getDeviceId(1);
        loginSams();
        spinner_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                validation_vendor_name = vendors.get(i).getVendor_name().toString();
                //  Toast.makeText(getBaseContext(), vendors.get(i).getCrew_person_id().toString(), Toast.LENGTH_SHORT).show();
                Preferences.setVendor(vendors.get(i).getVendor_id(),
                        vendors.get(i).getVendor_name().toString(),
                        vendors.get(i).getCrew_person_id().toString(), Login.this);
                getProjects(vendors.get(i).getCrew_person_id(), vendors.get(i).getVendor_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Preferences.setVendor("", "", "", Login.this);

            }
        });
        // Toast.makeText(getBaseContext(), Preferences.getKey(), Toast.LENGTH_LONG).show();

    }


    @OnClick(R.id.login_btn)
    public void onclick() {
        if (validation_vendor_name != null) {
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
        } else {
            Toast.makeText(getBaseContext(), "You are not authorized to proceed !!!", Toast.LENGTH_LONG).show();
        }
        /*Intent i = new Intent(Login.this, Home.class);
        startActivity(i);*/
    }
    private void loginSams(){
        if (!Validation.internet(Login.this)) {
            showInternet(Login.this);
        } else {
            Toast.makeText(getBaseContext(),imenumber1+" "+imenumber2,Toast.LENGTH_SHORT).show();

            appLogin();
        }
    }
    private void showInternet(Context activity) {
        this.overridePendingTransition(R.anim.slideleft,
                R.anim.slideleft);
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setMessage("Please Check Internet Connection before login");
        alertbox.setTitle("Sams" +
                "");
        alertbox.setIcon(R.drawable.samslogofinal);

        alertbox.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        loginSams();
                    }
                });
        // String negativeText = context.getApplicationContext().getString(android.R.string.cancel);
        alertbox.show();
    }

    public void appLogin() {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
          Call<Appopen> call = apiService.getVendors("865874035741786", "865874035741794");
        // Call<Appopen> call = apiService.getVendors("862114032689487", "862114032689487");
         //  Call<Appopen> call = apiService.getVendors("863675036469448", "863675036469448");
        //  Call<Appopen> call = apiService.getVendors(imenumber1, imenumber2);
        call.enqueue(new Callback<Appopen>() {
            @Override
            public void onResponse(Call<Appopen> call, Response<Appopen> response) {
                // String size = String.valueOf(response.body().getList().size());

                String result = String.valueOf(response.code());
                if (result.equals("200")) {
                    String status = response.body().getStatus().toString();
                    if (status.equals("success")){
                        vendors = response.body().getVendors_list();
                        login_spinner = new Login_spinner(Login.this, vendors);
                        spinner_login.setAdapter(login_spinner);
                    }
                    //    Toast.makeText(getBaseContext(), "   " + response.code(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getBaseContext(), "you are not authorized person", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Appopen> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), "Please Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProjects(String crepersonid, String vendorid) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        //Call<Login_Service> call = apiService.getProjects("10", "33");
        Call<Login_Service> call = apiService.getProjects(vendorid, crepersonid);
        call.enqueue(new Callback<Login_Service>() {
            @Override
            public void onResponse(Call<Login_Service> call, Response<Login_Service> response) {
                // Toast.makeText(getBaseContext(),response.body().getCrew_person_name().toString(),Toast.LENGTH_SHORT).show();
                // String size = String.valueOf(response.body().getList().size());
                String result = String.valueOf(response.code());
                if (result.equals("200")) {

                  /*  Log.d("key", response.body().getKey().toString());
                    Log.d("crewperson", response.body().getCrew_person_name().toString());
                    Log.d("userid", response.body().getUser_id().toString());
                    Log.d("getcrewpersonid", response.body().getCrew_person_id().toString());
                   */
                    if (response.body().getKey().toString() != null || response.body().getCrew_person_name().toString() != null
                            || response.body().getUser_id().toString() != null || response.body().getCrew_person_name().toString() != null) {
                        Preferences.setProject(response.body().getKey().toString(), response.body().getUser_id().toString(),
                                response.body().getCrew_person_id().toString(), response.body().getCrew_person_name().toString()
                                , Login.this);
                    } else {
                        Preferences.setProject("", "", response.body().getCrew_person_id().toString(), "", Login.this);
                    }

                }
            }

            @Override
            public void onFailure(Call<Login_Service> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
