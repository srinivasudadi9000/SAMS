package srinivas.sams.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;
import retrofit2.http.Query;
import srinivas.sams.R;
import srinivas.sams.helper.DBHelper;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.UploadInstall;
import srinivas.sams.model.UploadRecce;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Sync extends Activity {
    SQLiteDatabase db;
    String rec_id, project_id, product_name, uom_id, uom_name, outlet_name, outlet_address, longitude, latitude,
            width, height, width_feet, height_feet, width_inches, height_inches, recce_image, recce_image_1,
            recce_image_2, recce_image_3, recce_image_4, recce_image_upload_status, product0, product_id;
    File RimgFile = null, RimgFile_install = null, otherImagefile_install_1=null,otherImagefile_install_2= null,
            otherImagefile1 = null, otherImagefile2 = null, otherImagefile3 = null, otherImagefile4 = null;
    String installation_date_tv, installation_remarks_tv, key_tv, userid_tv, crew_person_id_tv, recce_id_tv, project_id_tv,
            installation_image,install_image1,install_image2;
    TextView sync_status,sync_count;
    int index = 0, mycount = 0;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync);
        sync_status = (TextView) findViewById(R.id.sync_status);
        sync_count = (TextView) findViewById(R.id.sync_count);
        clearPreferences();
        if (!Validation.internet(Sync.this)) {
            showalert();
        } else {


            mProgressDialog = new ProgressDialog(this);
            //mProgressDialog.setMessage("Processing..");
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
            final Handler handle = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    mProgressDialog.incrementProgressBy(9);
                }
            };

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (mProgressDialog.getProgress() <= mProgressDialog
                                .getMax()) {
                            Thread.sleep(500);
                            handle.sendMessage(handle.obtainMessage());
                            if (mProgressDialog.getProgress() == mProgressDialog
                                    .getMax()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            getRecces_from_local();
            getInstall_from_local();
            checking();
        }

    }

    public void checking() {
        //  Toast.makeText(getBaseContext(),"chekcing",Toast.LENGTH_SHORT).show();

        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM install WHERE product0='offline_update' LIMIT 1", null);
        Cursor re = db.rawQuery("SELECT * FROM recce WHERE product0='offline_update' LIMIT 1", null);
        if (re.getCount() == 0 && c.getCount() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /*  db.execSQL("delete from install");
                  db.execSQL("delete from recce");
                  db.close();*/
                    finish();
                }
            }, 20000);
        }
    }

    public void getInstall_from_local() {

        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM install WHERE product0='offline_update' LIMIT 1", null);

        String ccc = String.valueOf(c.getCount());
       // Toast.makeText(getBaseContext(),"installation "+ccc.toString(),Toast.LENGTH_SHORT).show();
        sync_count.setText(sync_count.getText().toString()+"Installations :"+ ccc);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                installation_date_tv = c.getString(c.getColumnIndex("installation_date"));
                installation_remarks_tv = c.getString(c.getColumnIndex("installation_remarks"));
                key_tv = c.getString(c.getColumnIndex("key"));
                userid_tv = c.getString(c.getColumnIndex("userid"));
                crew_person_id_tv = c.getString(c.getColumnIndex("crew_person_id"));
                recce_id_tv = c.getString(c.getColumnIndex("recce_id"));
                project_id_tv = c.getString(c.getColumnIndex("project_id"));
                installation_image = c.getString(c.getColumnIndex("installation_image"));
                RimgFile_install = new File(installation_image);
                install_image1 = c.getString(c.getColumnIndex("imagepath1"));
                otherImagefile_install_1 = new File(install_image1);
                install_image2 = c.getString(c.getColumnIndex("imagepath2"));
                otherImagefile_install_2 = new File(install_image2);

                RequestBody key = RequestBody.create(MediaType.parse("text/plain"), key_tv);
                RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), userid_tv);
                RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), crew_person_id_tv);
                RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), recce_id_tv);
                RequestBody project_id = RequestBody.create(MediaType.parse("text/plain"), project_id_tv);
                MultipartBody.Part imageFilePart1 = MultipartBody.Part.createFormData("installation_image", RimgFile_install.getName(),
                        RequestBody.create(MediaType.parse("image/*"), RimgFile_install));
                MultipartBody.Part imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile_install_1.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile_install_1));
                MultipartBody.Part imageFilePart3 = MultipartBody.Part.createFormData("installation_image_2", otherImagefile_install_2.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile_install_2));

                updateInstall(installation_date_tv, installation_remarks_tv, key, userid, crew_person_id, recce_id,
                        project_id, imageFilePart1,imageFilePart2,imageFilePart3);

                c.moveToNext();
            }
        }
        //finish();
    }

    public void updateInstall(@Query("installation_date") final String installation_date,
                              @Query("installation_remarks") final String installation_remarks,
                              @Part("key") RequestBody key,
                              @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                              @Part("recce_id") final RequestBody recce_id, @Part("project_id") final RequestBody project_id,
                              @Part final MultipartBody.Part installation_image,@Part final MultipartBody.Part installation_image_1,
                              @Part final MultipartBody.Part installation_image_2) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadInstall> call = apiService.getUploadInstall(installation_date, installation_remarks, key,
                user_id, crew_person_id, recce_id, project_id, installation_image,installation_image_1,installation_image_2);
        call.enqueue(new Callback<UploadInstall>() {
            @Override
            public void onResponse(Call<UploadInstall> call, Response<UploadInstall> response) {
                mycount++;
                String state = String.valueOf(mycount);
                sync_status.setText(state + " Installation Uploaded To Server ");
                String result = String.valueOf(response.code());
                if (result.equals("200")) {
                    DBHelper.updateInstall_Localdb(installation_date_tv, installation_remarks_tv, key_tv, userid_tv,
                            crew_person_id_tv, recce_id_tv,
                            project_id_tv, RimgFile_install.getAbsolutePath().toString(),
                            otherImagefile_install_1.getAbsolutePath(),
                            otherImagefile_install_2.getAbsolutePath(),"online_update", Sync.this);

                } else {
                    DBHelper.updateInstall_Localdb(installation_date_tv, installation_remarks_tv, key_tv, userid_tv,
                            crew_person_id_tv, recce_id_tv,
                            project_id_tv, RimgFile_install.getAbsolutePath().toString(),
                            otherImagefile_install_1.getAbsolutePath(),
                            otherImagefile_install_2.getAbsolutePath(),"offline_update", Sync.this);

                }
                getInstall_from_local();
            }

            @Override
            public void onFailure(Call<UploadInstall> call, Throwable throwable) {
                // Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                DBHelper.updateInstall_Localdb(installation_date_tv, installation_remarks_tv, key_tv, userid_tv,
                        crew_person_id_tv, recce_id_tv,
                        project_id_tv, RimgFile_install.getAbsolutePath().toString(),
                        otherImagefile_install_1.getAbsolutePath(),
                        otherImagefile_install_2.getAbsolutePath(),"offline_update", Sync.this);

                //Log.d("message_image", throwable.toString());
            }
        });
    }


    public void getRecces_from_local() {
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT * FROM recce WHERE uoms='offline_update'", null);
        String ccc = String.valueOf(c1.getCount());
        sync_count.setText("Recce : "+ccc);
        // Toast.makeText(getBaseContext(),ccc.toString(),Toast.LENGTH_SHORT).show();
        Cursor c = db.rawQuery("SELECT * FROM recce WHERE uoms='offline_update' LIMIT 1", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                rec_id = c.getString(c.getColumnIndex("recce_id"));
                // Toast.makeText(getBaseContext(),"recce update "+rec_id,Toast.LENGTH_SHORT).show();

                project_id = c.getString(c.getColumnIndex("project_id"));
                product_name = c.getString(c.getColumnIndex("product_name"));
                uom_id = c.getString(c.getColumnIndex("uom_id"));
                uom_name = c.getString(c.getColumnIndex("uom_name"));
                outlet_name = c.getString(c.getColumnIndex("outlet_name"));
                outlet_address = c.getString(c.getColumnIndex("outlet_address"));
                longitude = c.getString(c.getColumnIndex("longitude"));
                latitude = c.getString(c.getColumnIndex("latitude"));
                width = c.getString(c.getColumnIndex("width"));
                height = c.getString(c.getColumnIndex("height"));
                width_feet = c.getString(c.getColumnIndex("width_feet"));
                height_feet = c.getString(c.getColumnIndex("height_feet"));
                width_inches = c.getString(c.getColumnIndex("width_inches"));
                height_inches = c.getString(c.getColumnIndex("height_inches"));
                recce_image = c.getString(c.getColumnIndex("recce_image"));
                recce_image_1 = c.getString(c.getColumnIndex("recce_image_1"));
                recce_image_2 = c.getString(c.getColumnIndex("recce_image_2"));
                recce_image_3 = c.getString(c.getColumnIndex("recce_image_3"));
                recce_image_4 = c.getString(c.getColumnIndex("recce_image_4"));
                product0 = c.getString(c.getColumnIndex("product0"));
                // JsonElement uoms=c.getString(c.getColumnIndex("uoms"));
                recce_image_upload_status = c.getString(c.getColumnIndex("recce_image_upload_status"));
                product_id = c.getString(c.getColumnIndex("product_id"));
                //  Toast.makeText(getBaseContext(),"  "+recce_image,Toast.LENGTH_SHORT).show();
                //Log.d("outlet_name_sync", outlet_name);
                //Log.d("uomid",uom_id);
                //Log.d("product_id",product_id);
                //Log.d("key", Preferences.getKey());
                //Log.d("userid", Preferences.getUserid());
                //Log.d("crew_person_id", Preferences.getCrewPersonid_project());
                //Log.d("recce_id", rec_id);
                //Log.d("filePart", recce_image);
                //Log.d("imageFilePart2", recce_image_1);
                //Log.d("logtitude", latitude);
                //  list.add(name);
                RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Preferences.getKey(Sync.this));
                RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserid(Sync.this));
                RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), rec_id);
                RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getCrewPersonid_project(Sync.this));


                RimgFile = new File(recce_image);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("recce_image", RimgFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), RimgFile));
                otherImagefile1 = new File(recce_image_1);
                MultipartBody.Part imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile1));
                otherImagefile2 = new File(recce_image_2);
                MultipartBody.Part imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile2));
                otherImagefile3 = new File(recce_image_3);
                MultipartBody.Part imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile3));
                otherImagefile4 = new File(recce_image_4);
                MultipartBody.Part imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile4));
                RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), latitude);
                RequestBody logtitude = RequestBody.create(MediaType.parse("text/plain"), longitude);
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), outlet_address);

                uploadRecce(uom_id, width, height
                        , width_feet, height_feet, width_inches, height_inches, product_id, key, userid, crew_person_id, recce_id, filePart, imageFilePart1,
                        imageFilePart2, imageFilePart3, imageFilePart4, lat, logtitude, address);

                c.moveToNext();

            }
        }
    }

    public void uploadRecce(@Query("uom_id") final String uom_id, @Query("width") final String width,
                            @Query("height") final String height, @Query("w_f") String w_f, @Query("h_f") String h_f,
                            @Query("w_i") String w_i, @Query("h_i") String h_i, @Query("product_id") String pcdt_id, @Part("key") RequestBody key,
                            @Part("user_id") RequestBody user_id, @Part("h_i") RequestBody crew_person_id,
                            @Part("recce_id") final RequestBody recce_id,
                            @Part MultipartBody.Part recce_image,
                            @Part MultipartBody.Part recce_image_1,
                            @Part MultipartBody.Part recce_image_2,
                            @Part MultipartBody.Part recce_image_3,
                            @Part MultipartBody.Part recce_image_4,
                            @Part("latitude") final RequestBody latit,
                            @Part("longitude") final RequestBody longit, @Part("outlet_address") final RequestBody outlet_addr) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadRecce> call = apiService.getUploadRecce(uom_id, width, height, w_f, h_f, w_i, h_i, pcdt_id, key,
                user_id, crew_person_id, recce_id, recce_image, recce_image_1, recce_image_2, recce_image_3, recce_image_4,
                latit, longit, outlet_addr);
        call.enqueue(new Callback<UploadRecce>() {
            @Override
            public void onResponse(Call<UploadRecce> call, Response<UploadRecce> response) {
                String result = String.valueOf(response.code());
                //  Toast.makeText(Sync.this,"  "+result,Toast.LENGTH_SHORT).show();
                index++;
                String state = String.valueOf(index);
                sync_status.setText(state + "  Recce Uploaded To Server ");

                if (result.equals("200")) {
                    DBHelper.updateRecce_Localdb(uom_id, width, Preferences.getKey(Sync.this), Preferences.getUserid(Sync.this)
                            , Preferences.getCrewPersonid_project(Sync.this), height
                            , width_feet, height_feet, width_inches, height_inches, product_id, rec_id, RimgFile.getAbsolutePath().toString(),
                            otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                            , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                            latitude, longitude, outlet_address, Preferences.getProjectId(Sync.this), "online_update", "Completed", Sync.this);

                } else {
                    DBHelper.updateRecce_Localdb(uom_id, width, Preferences.getKey(Sync.this), Preferences.getUserid(Sync.this)
                            , Preferences.getCrewPersonid_project(Sync.this), height
                            , width_feet, height_feet, width_inches, height_inches, product_id, rec_id, RimgFile.getAbsolutePath().toString(),
                            otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                            , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                            latitude, longitude, outlet_address, Preferences.getProjectId(Sync.this), "offline_update", "Completed", Sync.this);

                }
                getRecces_from_local();

            }

            @Override
            public void onFailure(Call<UploadRecce> call, Throwable throwable) {
               /*  Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                 Log.d("message_image", throwable.toString());
                Log.d("message_image", RimgFile.getAbsolutePath().toString());
                Log.d("message_image", otherImagefile1.getAbsolutePath().toString());
                Log.d("message_image", otherImagefile2.getAbsolutePath().toString());
                Log.d("message_image", otherImagefile3.getAbsolutePath().toString());
                Log.d("message_image", otherImagefile4.getAbsolutePath().toString());*/
                DBHelper.updateRecce_Localdb(uom_id, width, Preferences.getKey(Sync.this), Preferences.getUserid(Sync.this)
                        , Preferences.getCrewPersonid_project(Sync.this), height
                        , width_feet, height_feet, width_inches, height_inches, product_id, rec_id, RimgFile.getAbsolutePath().toString(),
                        otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                        , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                        latitude, longitude, outlet_address, Preferences.getProjectId(Sync.this), "offline_update", "Completed", Sync.this);

            }
        });
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

    public void showalert() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(Sync.this);
        alertbox.setMessage("Please Check Internet Connection !! ");
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

    @Override
    public void onBackPressed() {
       /* super.onBackPressed();
       */
//        Toast.makeText(Sync.this, "Please Wait a moment..", Toast.LENGTH_SHORT).show();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM install WHERE product0='offline_update' LIMIT 1", null);
        Cursor re = db.rawQuery("SELECT * FROM recce WHERE product0='offline_update' LIMIT 1", null);
        if (re.getCount() == 0 && c.getCount() == 0) {
            finish();
        }
    }
}