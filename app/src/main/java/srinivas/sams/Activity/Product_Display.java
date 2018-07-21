package srinivas.sams.Activity;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivas.sams.Adapter.ProductAdapter;
import srinivas.sams.R;
import srinivas.sams.helper.DBHelper;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.GetProducts;
import srinivas.sams.model.Products;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Product_Display extends Activity {
    @BindView(R.id.product_recyler)
    RecyclerView product_recyler;
    List<Products> productses = null;
    SQLiteDatabase db;
    SearchView searchView;
    ProductAdapter productAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product__display);
        ButterKnife.bind(this);
        product_recyler.setLayoutManager(new LinearLayoutManager(this));

        if (!Validation.hasActiveInternetConnection(Product_Display.this)) {
            getProduct_from_local();
           // Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            getProductlist();
        }

        searchView = (SearchView)findViewById(R.id.mysearchview);
        //search(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filter(newText);
                return false;
            }
        });
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    public void getProductlist() {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<GetProducts> call = apiService.getProduct(Preferences.getKey(Product_Display.this), Preferences.getUserid(Product_Display.this),
                Preferences.getProjectId(Product_Display.this).toString(), Preferences.getCrewpersonid(Product_Display.this));
        call.enqueue(new Callback<GetProducts>() {
            @Override
            public void onResponse(Call<GetProducts> call, Response<GetProducts> response) {
                productses = response.body().getProductses();
                String size = String.valueOf(productses.size());
                //Toast.makeText(getBaseContext(),size,Toast.LENGTH_SHORT).show();
                productAdapter = new ProductAdapter(productses, R.layout.single_product, getApplicationContext());
                product_recyler.setAdapter(productAdapter);
                new DBHelper( productses , Product_Display.this,"install","","");

            }

            @Override
            public void onFailure(Call<GetProducts> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public  void getProduct_from_local() {
        ArrayList<Products> product_offline = new ArrayList<Products>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
      //  Toast.makeText(Install_display.this, "view my db install data ", Toast.LENGTH_SHORT).show();
        //  Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+email+"' and resume='"+resumename+"'", null);
        Cursor c = db.rawQuery("SELECT * FROM product", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String product_id=c.getString(c.getColumnIndex("product_id"));
                String product_name=c.getString(c.getColumnIndex("product_name"));

                product_offline.add(new Products(product_name,product_id));
              //  Toast.makeText(Install_display.this, " " + name, Toast.LENGTH_SHORT).show();
                //  list.add(name);
                c.moveToNext();
            }
        }
        productAdapter = new ProductAdapter(product_offline, R.layout.single_product, getApplicationContext());
        product_recyler.setAdapter(productAdapter);

    }



}
