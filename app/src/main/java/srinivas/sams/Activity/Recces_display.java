package srinivas.sams.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivas.sams.Adapter.RecceAdapter;
import srinivas.sams.R;
import srinivas.sams.helper.DBHelper;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.GetProducts;
import srinivas.sams.model.GetRecce;
import srinivas.sams.model.Products;
import srinivas.sams.model.Recce;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Recces_display extends Activity {
    @BindView(R.id.recee_recyler)
    RecyclerView recee_recyler;
    SQLiteDatabase db;
    List<Recce> recces = null;
    @BindView(R.id.svOutletNameAddress)
    SearchView svOutletNameAddress;
    @BindView(R.id.fabAddRecce)
    FloatingActionButton fabAddRecce;
    RecceAdapter recceAdapter;
    @BindView(R.id.spFilter)
    Spinner spFilter;
    @BindView(R.id.header_tv)
    TextView header_tv;
    String position;
    List<Products> productses = null;
    public String web = "http://128.199.131.14/sams/web/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recces_display);
        ButterKnife.bind(this);
        clearPreferences();
        header_tv.setText("RECCES");
        recee_recyler.setLayoutManager(new LinearLayoutManager(this));
        //recee_recyler.addOnItemTouchListener(new Recces_display.DrawerItemClickListener());
        recee_recyler.setHasFixedSize(true);
        recee_recyler.setItemViewCacheSize(20);
        recee_recyler.setDrawingCacheEnabled(true);
        recee_recyler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
       /* Log.d("key", Preferences.getKey());
        Log.d("userid", Preferences.getUserid());
        Log.d("crewpersionid", Preferences.getCrewPersonid_project());
       // Log.d("projectid", getIntent().getStringExtra("projectid").toString());
        Log.d("projectid", Preferences.getProjectId().toString());
*/
        //
        if (!Validation.internet(Recces_display.this)) {
            fabAddRecce.setVisibility(View.GONE);
            getRecces_from_local();
            // Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            getRecceslist();
            if (Preferences.getProducts(Recces_display.this).equals("notdone")) {
                getProductlist();
            } else {
                Preferences.setProducts("done", Recces_display.this);
            }
        }
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = adapterView.getSelectedItem().toString();
                // Toast.makeText(getBaseContext(), position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //loadReccesFromDatabase(getIntent().getExtras().getString("projectid"), "All", "");
            }
        });

        svOutletNameAddress.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (position.equals("O.Name")) {
                    recceAdapter.filter(newText.toString());
                } else {
                    recceAdapter.filteraddress(newText.toString());
                }
                // rla.filteraddress(newText.toString());
                return false;

            }
        });

    }

    @OnClick(R.id.fabAddRecce)
    public void addrecce_web() {
        Intent intent = new Intent(getApplicationContext(), AddRecceWeb.class);
        intent.putExtra("path", "create");
        //intent.putExtra("url", "http://sams.mmos.in/sams/web/index.php?r=app-outlets/app-recce-create&user_id=" + Preferences.getUserid(Recces_display.this) + "&crew_person_id=" + Preferences.getCrewPersonid_project(Recces_display.this) + "&project_id=" + Preferences.getProjectId(Recces_display.this));
        intent.putExtra("url", web+"index.php?r=app-outlets/app-recce-create&user_id=" + Preferences.getUserid(Recces_display.this) + "&crew_person_id=" + Preferences.getCrewPersonid_project(Recces_display.this) + "&project_id=" + Preferences.getProjectId(Recces_display.this));
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    public void getRecces_from_local() {
        ArrayList<Recce> recces_offline = new ArrayList<Recce>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        // Toast.makeText(Recces_display.this, "view my db", Toast.LENGTH_SHORT).show();
        //  Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+email+"' and resume='"+resumename+"'", null);
        Cursor c = db.rawQuery("SELECT * FROM recce WHERE project_id='" + Preferences.getProjectId(Recces_display.this).toString() + "'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex("recce_id"));
                String recce_id = c.getString(c.getColumnIndex("recce_id"));
                String project_id = c.getString(c.getColumnIndex("project_id"));
                String product_name = c.getString(c.getColumnIndex("product_name"));
                String zone_id = c.getString(c.getColumnIndex("zone_id"));
                String uom_id = c.getString(c.getColumnIndex("uom_id"));
                String uom_name = c.getString(c.getColumnIndex("uom_name"));
                String recce_date = c.getString(c.getColumnIndex("recce_date"));
                String outlet_name = c.getString(c.getColumnIndex("outlet_name"));
                String outlet_owner_name = c.getString(c.getColumnIndex("outlet_owner_name"));
                String outlet_address = c.getString(c.getColumnIndex("outlet_address"));
                String longitude = c.getString(c.getColumnIndex("longitude"));
                String latitude = c.getString(c.getColumnIndex("latitude"));
                String width = c.getString(c.getColumnIndex("width"));
                String height = c.getString(c.getColumnIndex("height"));
                String width_feet = c.getString(c.getColumnIndex("width_feet"));
                String height_feet = c.getString(c.getColumnIndex("height_feet"));
                String width_inches = c.getString(c.getColumnIndex("width_inches"));
                String height_inches = c.getString(c.getColumnIndex("height_inches"));
                String recce_image = c.getString(c.getColumnIndex("recce_image"));
                String recce_image_1 = c.getString(c.getColumnIndex("recce_image_1"));
                String recce_image_2 = c.getString(c.getColumnIndex("recce_image_2"));
                String recce_image_3 = c.getString(c.getColumnIndex("recce_image_3"));
                String recce_image_4 = c.getString(c.getColumnIndex("recce_image_4"));
                String product0 = c.getString(c.getColumnIndex("product0"));
                // JsonElement uoms=c.getString(c.getColumnIndex("uoms"));
                String recce_image_upload_status = c.getString(c.getColumnIndex("recce_image_upload_status"));
                String product_id = c.getString(c.getColumnIndex("product_id"));
                recces_offline.add(new Recce(recce_id, project_id, product_name, zone_id, uom_id, uom_name, recce_date, outlet_name,
                        outlet_owner_name, outlet_address, longitude, latitude, width, height, width_feet, height_feet,
                        width_inches, height_inches, recce_image, recce_image_1, recce_image_2, recce_image_3, recce_image_4,
                        product0, null, recce_image_upload_status, product_id));
                // Log.d("values", name);
                //  list.add(name);
                c.moveToNext();
            }
        }
        recceAdapter = new RecceAdapter(recces_offline, R.layout.recee_single, getApplicationContext());
        recee_recyler.setAdapter(recceAdapter);

        Collections.sort(recces_offline, new Comparator<Recce>() {
            @Override
            public int compare(Recce o1, Recce o2) {
                return o1.getOutlet_name().compareTo(o2.getOutlet_name());
            }

        });
        recceAdapter.notifyDataSetChanged();

        if (recee_recyler.getAdapter().getItemCount() == 0) {
            showalert();
        }

    }

    public void getRecceslist() {
       /* db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("delete from recce");
        db.close();*/
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
     /*   Log.d("key",Preferences.getKey(Recces_display.this).toString());
        Log.d("userid",Preferences.getUserid(Recces_display.this).toString());
        Log.d("projectid",Preferences.getProjectId(Recces_display.this).toString());
        Log.d("crewpersonid",Preferences.getCrewPersonid_project(Recces_display.this).toString());
        */
        Call<GetRecce> call = apiService.getRecces(Preferences.getKey(Recces_display.this), Preferences.getUserid(Recces_display.this),
                Preferences.getProjectId(Recces_display.this).toString(), Preferences.getCrewPersonid_project(Recces_display.this));
        // Call<GetRecce> call = apiService.getRecces("E7Wj3SafrHp88clikIGc6nKQKf2LDr1K", "101", "114", "357");
        //   Call<GetRecce> call = apiService.getRecces("vwqoBF2z3p6k5yCMsoSF3hlI1wisRecY", "50", getIntent().getStringExtra("projectid"), "33");
        call.enqueue(new Callback<GetRecce>() {
            @Override
            public void onResponse(Call<GetRecce> call, Response<GetRecce> response) {
                // String size = String.valueOf(response.body().getList().size());
               /* List<Umo> Umo = response.body().getUoms_list();
                for (int j = 0; j < Umo.size(); j++) {
                    Toast.makeText(getBaseContext(), "   " + Umo.get(j).getUom_name(), Toast.LENGTH_SHORT).show();

                }*/
              /*  String result = String.valueOf(response.code());
                if (result.equals("200")) {

                } else {
                    Toast.makeText(getBaseContext(), "Please Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
                }*/
                recces = response.body().getRecces();
                new DBHelper(recces, Recces_display.this);
                recceAdapter = new RecceAdapter(recces, R.layout.recee_single, getApplicationContext());
                recee_recyler.setAdapter(recceAdapter);

                Collections.sort(recces, new Comparator<Recce>() {
                    @Override
                    public int compare(Recce o1, Recce o2) {
                        return o1.getOutlet_name().compareTo(o2.getOutlet_name());
                    }

                });
                recceAdapter.notifyDataSetChanged();
                if (recee_recyler.getAdapter().getItemCount() == 0) {
                    showalert();
                }
                deletedb(recces, Recces_display.this);
            }

            @Override
            public void onFailure(Call<GetRecce> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), "Please Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });

//

    }

    public void deletedb(List<Recce> recces, Context context) {
       String find="";
        ArrayList<String> localrecce = new ArrayList<String>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Cursor c = db.rawQuery("SELECT * FROM recce WHERE project_id='" + Preferences.getProjectId(context).toString() + "'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String recce_id = c.getString(c.getColumnIndex("recce_id"));
                localrecce.add(recce_id);
                c.moveToNext();
            }
        }
        if (localrecce.size() > 0) {
            for (int i = 0; i < localrecce.size(); i++) {
                for (int j = 0; j < recces.size(); j++) {
                    if (localrecce.get(i).toString().equals(recces.get(j).getRecce_id())) {
                        find="equal";
                    }else {
                        find="notequal";
                    }
                    if (find.equals("equal")){
                        Log.d("Equal_Reccee",localrecce.get(i).toString()+"  "+recces.get(j).getRecce_id());
                        find="notequal";
                        break;
                       //
                    }else {
                        if (j == recces.size()-1){
                            db.execSQL("DELETE FROM recce WHERE recce_id = " + localrecce.get(i).toString());
                            Log.d("Delete Reccee",localrecce.get(i).toString()+"  "+recces.get(j).getRecce_id());
                        }
                    }
                }
            }
        }



      /*  else {
            db.execSQL("DELETE FROM recce WHERE recce_id = " + localrecce.get(i).toString());
            break;
        }*/
    }


    public void getProductlist() {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<GetProducts> call = apiService.getProduct(Preferences.getKey(Recces_display.this), Preferences.getUserid(Recces_display.this),
                Preferences.getProjectId(Recces_display.this).toString(), Preferences.getCrewpersonid(Recces_display.this));
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                String result = String.valueOf(response.code());
                if (result.equals("200")) {
                    productses = response.body().getProductses();
                    String size = String.valueOf(productses.size());
                    //Toast.makeText(getBaseContext(),size,Toast.LENGTH_SHORT).show();
                    //productAdapter = new ProductAdapter(productses, R.layout.single_product, getApplicationContext());
                    //product_recyler.setAdapter(productAdapter);
                    Preferences.setProducts("done", Recces_display.this);
                    new DBHelper(productses, Recces_display.this, "product", "", "");
                }

            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String validaterecord(String recceid) {

        Cursor c = db.rawQuery("SELECT * FROM recce WHERE recce_id='" + recceid + "'", null);
        if (c.moveToFirst()) {
            return "validate";
        } else {
            return "notvalidate";
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

    public void showalert() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(Recces_display.this);
        alertbox.setMessage("Sorry!! No Recces Found Thankyou ");
        alertbox.setTitle("Sams");
        alertbox.setIcon(R.drawable.samslogofinal);

        alertbox.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        // finish();
                    }
                });

        alertbox.show();
    }
}
