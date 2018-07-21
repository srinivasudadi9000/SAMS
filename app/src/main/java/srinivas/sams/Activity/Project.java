package srinivas.sams.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivas.sams.Adapter.ProjectAdapter;
import srinivas.sams.R;
import srinivas.sams.helper.DBHelper;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.Login_Service;
import srinivas.sams.model.Products;
import srinivas.sams.model.Projects;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Project extends Activity {
    @BindView(R.id.project_recyler)
    RecyclerView project_recyler;
    @BindView(R.id.header_tv)
    TextView header_tv;
    List<Products> productses = null;
    SQLiteDatabase db;
    GPSTracker gps = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project);
        ButterKnife.bind(this);
        project_recyler.setLayoutManager(new LinearLayoutManager(this));
       // project_recyler.setItemAnimator(new DefaultItemAnimator());
        header_tv.setText(Preferences.getSelection(Project.this));
        if (!Validation.hasActiveInternetConnection(Project.this)) {
            getProject_from_local();
          //  Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            getProjects(Preferences.getCrewpersonid(Project.this), Preferences.getVendorid(Project.this));

        }
        //Preferences.setProject("fkfkkd3313132","u-45","123");
        //Toast.makeText(getBaseContext(),Preferences.getCrewpersonid()+"  "+Preferences.getVendorid(),Toast.LENGTH_LONG).show();
      getlatlong();
    }
    public void getlatlong(){
            gps = new GPSTracker(Project.this);
            // check if GPS enabled
            if(gps.canGetLocation()){
                String latitude = String.valueOf(gps.getLatitude());
                String longitude = String.valueOf(gps.getLongitude());
                // \n is for new line
                Preferences.setlatlong(latitude,longitude,Project.this);
              //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }

    }
     @OnClick(R.id.back)
     public void back(){
         finish();
     }
    public void getProject_from_local() {
        ArrayList<Projects> project_offline = new ArrayList<Projects>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        //  Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+email+"' and resume='"+resumename+"'", null);
       // Cursor c = db.rawQuery("SELECT * FROM project WHERE project_id='" + getIntent().getStringExtra("projectid").toString() + "'", null);
        Cursor c = db.rawQuery("SELECT * FROM project WHERE vendor_id='"+Preferences.getVendorid(Project.this)+"'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String project_name = c.getString(c.getColumnIndex("project_name"));
                String project_id = c.getString(c.getColumnIndex("project_id"));
                project_offline.add(new Projects(project_name, project_id));
               // Log.d("Project", project_id);
                //  list.add(name);
                c.moveToNext();
            }
        }
        project_recyler.setAdapter(new ProjectAdapter(project_offline, R.layout.project_single, Project.this));
        if (project_recyler.getAdapter().getItemCount() ==0) {
            showalert();
        }
       // Toast.makeText(Project.this, "call update project", Toast.LENGTH_SHORT).show();
       // updateProject_Localdb("6","srinivasu_offline");
    }


    public void getProjects(String crepersonid, String vendorid) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        //Call<Login_Service> call = apiService.getProjects("10", "33");
        Call<Login_Service> call = apiService.getProjects(vendorid, crepersonid);
        call.enqueue(new Callback<Login_Service>() {
            @Override
            public void onResponse(Call<Login_Service> call, Response<Login_Service> response) {
                // String size = String.valueOf(response.body().getList().size());
                String result = String.valueOf(response.code());
                if (result.equals("200")) {
/*
                    Log.d("key", response.body().getKey().toString());
                    Log.d("crewperson", response.body().getCrew_person_name().toString());
                    Log.d("userid", response.body().getUser_id().toString());
                    Log.d("getcrewpersonid", response.body().getCrew_person_id().toString());
                    if (response.body().getKey().toString() != null || response.body().getCrew_person_name().toString() != null
                            || response.body().getUser_id().toString() != null) {
                        Preferences.setProject(response.body().getKey().toString(), response.body().getUser_id().toString(),
                                response.body().getCrew_person_id().toString(),response.body().getCrew_person_name().toString());
                    } else {
                        Preferences.setProject("", "", response.body().getCrew_person_id().toString(),"");
                    }*/

                    List<Projects> projects = response.body().getProjects();
                    project_recyler.setAdapter(new ProjectAdapter(projects, R.layout.project_single, getApplicationContext()));
                    new DBHelper(projects, Project.this, null, null);
                    if (project_recyler.getAdapter().getItemCount() == 0) {
                        showalert();
                    }
              /*  String xx = String.valueOf(projects.size());
                Toast.makeText(getBaseContext(),xx.toString(),Toast.LENGTH_SHORT).show();
                for (int i = 0; i < projects.size(); i++) {
                    Toast.makeText(getBaseContext(), projects.get(i).getProject_name().toString(), Toast.LENGTH_SHORT).show();
                }*/
                }
            }

            @Override
            public void onFailure(Call<Login_Service> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProject_Localdb(String project_id, String project_name) {
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("UPDATE project SET project_name='" + project_name + "'"+"WHERE project_id="+project_id);
    }
    public void showalert() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(Project.this);
        alertbox.setMessage("Sorry!! No Projects Found Thankyou ");
        alertbox.setTitle("Sams");
        alertbox.setIcon(R.drawable.samslogofinal);

        alertbox.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                       finish();
                    }
                });

        alertbox.show();
    }



}
