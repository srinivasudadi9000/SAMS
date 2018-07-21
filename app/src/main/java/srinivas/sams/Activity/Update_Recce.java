package srinivas.sams.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;
import retrofit2.http.Query;
import srinivas.sams.R;
import srinivas.sams.helper.Calulations;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.UploadRecce;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Update_Recce extends Activity {
    @BindView(R.id.tvRwidth)
    EditText tvRwidth;
    @BindView(R.id.tvRheight)
    EditText tvRheight;
    @BindView(R.id.tvRwf)
    EditText tvRwf;
    @BindView(R.id.tvRwi)
    EditText tvRwi;
    @BindView(R.id.tvRhf)
    EditText tvRhf;
    @BindView(R.id.tvRhi)
    EditText tvRhi;
    @BindView(R.id.ivRecceImage)
    ImageView ivRecceImage;
    @BindView(R.id.ivOtherImage1)
    ImageView ivOtherImage1;
    @BindView(R.id.ivOtherImage2)
    ImageView ivOtherImage2;
    @BindView(R.id.ivOtherImage3)
    ImageView ivOtherImage3;
    @BindView(R.id.ivOtherImage4)
    ImageView ivOtherImage4;
    @BindView(R.id.spnOums)
    Spinner spnOums;
    String iv_urlRC = null;
    Bitmap bmImage_main = null;
    String width, height, uomid, productname;
    String mainpic, image1, image2, image3, image4, address, latitude, longitude, width_feet, height_feet, width_inch, height_inch;
    File file;
    public int R_IMAGE = 2, O_IMAGE1 = 3, O_IMAGE2 = 4, O_IMAGE3 = 5, O_IMAGE4 = 6;
    protected Uri iv_url1 = null, iv_url2 = null, iv_url3 = null, iv_url4 = null;
    String offlineimgpath1 = "", offlineimgpath2 = "", offlineimgpath3 = "", offlineimgpath4 = "";
    File otherImagefile1 = null;
    File otherImagefile2 = null;
    File otherImagefile3 = null;
    File otherImagefile4 = null;
    File RimgFile = null;
    File otherImagefile1_offline = null, otherImagefile2_offline = null, otherImagefile3_offline = null, otherImagefile4_offline = null;
    String compress_image4 = null, compress_image3 = null, compress_image2 = null, compress_1 = null, product_id;
    String image1_select = "", image2_select = "", image3_select = "", image4_select = "", uomname, outlet_name;
    SQLiteDatabase db;
    ArrayAdapter<String> uomsadapter;
    FileOutputStream fos = null;
    private DisplayImageOptions options;
    MultipartBody.Part imageFilePart1 = null, imageFilePart2 = null, imageFilePart3 = null, imageFilePart4 = null;
   // public String web = "http://128.199.131.14/sams/web/";
  // public String web = "http://128.199.131.14/samsdev/web/";
    public String web = "http://128.199.131.14/samsapp/web/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update__recce);
        ButterKnife.bind(this);
        spnOums = (Spinner) findViewById(R.id.spnOums);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Update_Recce.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1257);
        }
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher_round)
                .showImageForEmptyUri(R.drawable.clear)
                .showImageOnFail(R.drawable.dummy)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        outlet_name = getIntent().getStringExtra("outlet_name");
        width = (getIntent().getStringExtra("width"));
        height = (getIntent().getStringExtra("height"));
        uomid = (getIntent().getStringExtra("uomid"));
        product_id = getIntent().getStringExtra("product_id");
        productname = getIntent().getStringExtra("productname");
        uomname = getIntent().getStringExtra("uomname");
        mainpic = getIntent().getStringExtra("mainpic");
        image1 = getIntent().getStringExtra("image1");
        image2 = getIntent().getStringExtra("image2");
        image3 = getIntent().getStringExtra("image3");
        image4 = getIntent().getStringExtra("image4");
        address = getIntent().getStringExtra("address");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        //  Toast.makeText(getBaseContext(),latitude+" "+longitude,Toast.LENGTH_SHORT).show();
        otherImagefile1 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile2 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile3 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile4 = new File(getExternalCacheDir(), "noimage.png");
        displayimages();
        if (!otherImagefile1.exists()) {
            try {
                otherImagefile1.createNewFile();
                // iv_url1 = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        spnOums.setSelection(Integer.parseInt(uomid));
        spnOums.setSelection(Integer.parseInt(uomid));
        spnOums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!tvRwidth.getText().toString().isEmpty() && !tvRheight.getText().toString().isEmpty()) {

                    switch (i) {
                        case 0:
                            Toast.makeText(Update_Recce.this, "Please Select type of uom", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            uomid = "1";
                            uomname = "Millimeter";
                            tvRhi.setText(String.valueOf(Calulations.mm_feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.mm_feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.mm_to_feet(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.mm_to_feet(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 2:
                            uomid = "2";
                            uomname = "Centimeter";
                            tvRhi.setText(String.valueOf(Calulations.mm_cm_m_feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.mm_cm_m_feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches

                            tvRhf.setText(String.valueOf(Calulations.cm_to_feet(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.cm_to_feet(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 3:
                            uomid = "3";
                            uomname = "inch";
                            tvRhi.setText(String.valueOf(Calulations.inch_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRhf.setText(String.valueOf(Calulations.inch_to_feets(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.inch_to_feets(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            tvRwi.setText(String.valueOf(Calulations.inch_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            Double inches = Double.valueOf(tvRwi.getText().toString());
                            Double feets = Double.valueOf(tvRwf.getText().toString());
                            if (inches >= 12) {
                                //feets++;
                                //inches = 0.0;
                                tvRwf.setText(String.valueOf(feets));//set widht inches
                                //Toast.makeText(getBaseContext(),"hello",Toast.LENGTH_SHORT).show();
                                tvRwi.setText("0.0");     //set widht inches
                            }
                            Double heinches = Double.valueOf(tvRhi.getText().toString());
                            Double hefeets = Double.valueOf(tvRhf.getText().toString());
                            if (heinches >= 12) {
                                //feets++;
                                //inches = 0.0;
                                tvRhf.setText(String.valueOf(hefeets));//set widht inches
                                //Toast.makeText(getBaseContext(),"hello",Toast.LENGTH_SHORT).show();
                                tvRhi.setText("0.0");     //set widht inches
                            }
                            break;
                        case 4:
                            uomid = "4";
                            uomname = "Meter";
                            tvRhi.setText(String.valueOf(Calulations.m_feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.m_feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.m_to_feets(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.m_to_feets(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 5:
                            uomid = "5";
                            uomname = "Foot";
                            tvRhi.setText(String.valueOf(Calulations.feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.feet_to_feet(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.feet_to_feet(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 6:
                            uomid = "6";
                            uomname = "NA";
                            tvRhi.setText("0");   //set height
                            tvRwi.setText("0");     //set widht inches
                            tvRhf.setText("0");   //set height
                            tvRwf.setText("0");//set widht inches
                            break;

                    }

                } else {
                    Toast.makeText(Update_Recce.this, "Please add image!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @OnClick(R.id.back_update)
    public void back() {
        Intent project = new Intent(Update_Recce.this, Recces_display.class);
        startActivity(project);
        finish();
    }

    public void displayimages() {
        tvRwidth.setText(width);
        tvRheight.setText(height);
        Bitmap bmImage = null, bmImage1 = null, bmImage2 = null, bmImage3 = null, bmImage4 = null;
        ;
        if (!Validation.internet(Update_Recce.this)) {

            if (mainpic.contains("noimage.png") || !mainpic.contains("storage")) {
                ivRecceImage.setImageResource(R.drawable.dummy);
            } else {
                bmImage = BitmapFactory.decodeFile(mainpic.toString(), null);
                ivRecceImage.setImageBitmap(bmImage);
            }
            if (image1.contains("storage") && !image1.contains("noimage.png")) {
                bmImage1 = BitmapFactory.decodeFile(image1, null);
                ivOtherImage1.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage1.setImageBitmap(bmImage1);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile1_offline = new File(image1);

                offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));
            } else if (image1.contains("noimage.png")) {
                // Toast.makeText(getBaseContext(),"noimage.png",Toast.LENGTH_SHORT).show();
                ivOtherImage1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivOtherImage1.setImageResource(R.drawable.dummy);
            } else {

                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                        !Preferences.getidpath1(Update_Recce.this).equals("")) {
                    bmImage1 = BitmapFactory.decodeFile(Preferences.getidpath1(Update_Recce.this), null);
                    ivOtherImage1.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivOtherImage1.setImageBitmap(bmImage1);
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile1_offline = new File(Preferences.getidpath1(Update_Recce.this));
                    offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                    imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                } else {
                    ivOtherImage1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivOtherImage1.setImageResource(R.drawable.dummy);
                    Bitmap bitmap = ((BitmapDrawable) ivOtherImage1.getDrawable()).getBitmap();

                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile1_offline = new File(myDir, "noimage.jpg");

                    OutputStream outStream = null;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        outStream = new FileOutputStream(otherImagefile1_offline);
                        outStream.write(bytes.toByteArray());
                        outStream.flush();
                        outStream.close();

                        offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                        imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            if (image2.contains("storage") && !image2.contains("noimage.png")) {
                bmImage2 = BitmapFactory.decodeFile(image2, null);
                ivOtherImage2.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage2.setImageBitmap(bmImage2);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile2_offline = new File(image2);
                offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));
            } else if (image2.contains("noimage.png")) {
                ivOtherImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivOtherImage2.setImageResource(R.drawable.dummy);
            } else {
              /*  */
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                        !Preferences.getidpath2(Update_Recce.this).equals("")) {
                    bmImage2 = BitmapFactory.decodeFile(Preferences.getidpath2(Update_Recce.this), null);
                    ivOtherImage2.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivOtherImage2.setImageBitmap(bmImage2);
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile2_offline = new File(Preferences.getidpath2(Update_Recce.this));
                    offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                    imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                } else {
                    ivOtherImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivOtherImage2.setImageResource(R.drawable.dummy);
                    Bitmap bitmap = ((BitmapDrawable) ivOtherImage2.getDrawable()).getBitmap();

                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile2_offline = new File(myDir, "noimage.jpg");

                    OutputStream outStream = null;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        outStream = new FileOutputStream(otherImagefile2_offline);
                        outStream.write(bytes.toByteArray());
                        outStream.flush();
                        outStream.close();

                        offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                        imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


            if (image3.contains("storage") && !image3.contains("noimage.png")) {
                bmImage3 = BitmapFactory.decodeFile(image3, null);
                ivOtherImage3.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage3.setImageBitmap(bmImage3);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

               /* otherImagefile3_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                otherImagefile3_offline = new File(image3);

                offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));
            } else if (image3.contains("noimage.png")) {
                ivOtherImage3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivOtherImage3.setImageResource(R.drawable.dummy);
            } else {
              /*  */
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                        !Preferences.getidpath3(Update_Recce.this).equals("")) {
                    bmImage3 = BitmapFactory.decodeFile(Preferences.getidpath3(Update_Recce.this), null);
                    ivOtherImage3.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivOtherImage3.setImageBitmap(bmImage3);
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile3_offline = new File(Preferences.getidpath3(Update_Recce.this));
                    offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                    imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                } else {
                    ivOtherImage3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivOtherImage3.setImageResource(R.drawable.dummy);
                    Bitmap bitmap = ((BitmapDrawable) ivOtherImage3.getDrawable()).getBitmap();

                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile3_offline = new File(myDir, "noimage.jpg");

                    OutputStream outStream = null;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        outStream = new FileOutputStream(otherImagefile3_offline);
                        outStream.write(bytes.toByteArray());
                        outStream.flush();
                        outStream.close();

                        offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                        imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


            if (image4.contains("storage") && !image4.contains("noimage.png")) {
                bmImage4 = BitmapFactory.decodeFile(image4, null);
                ivOtherImage4.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage4.setImageBitmap(bmImage4);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile4_offline = new File(image4);

                offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));
            } else if (image4.contains("noimage.png")) {
                ivOtherImage4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivOtherImage4.setImageResource(R.drawable.dummy);
            } else {
              /*  */
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                        !Preferences.getidpath4(Update_Recce.this).equals("")) {
                    bmImage4 = BitmapFactory.decodeFile(Preferences.getidpath4(Update_Recce.this), null);
                    ivOtherImage4.setScaleType(ImageView.ScaleType.FIT_XY);
                    ivOtherImage4.setImageBitmap(bmImage4);
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile4_offline = new File(Preferences.getidpath4(Update_Recce.this));
                    offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                    imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));
                } else {
                    ivOtherImage4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivOtherImage4.setImageResource(R.drawable.dummy);
                    Bitmap bitmap = ((BitmapDrawable) ivOtherImage4.getDrawable()).getBitmap();

                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();
                    otherImagefile4_offline = new File(myDir, "noimage.jpg");

                    OutputStream outStream = null;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        outStream = new FileOutputStream(otherImagefile4_offline);
                        outStream.write(bytes.toByteArray());
                        outStream.flush();
                        outStream.close();

                        offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                        imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }


        } else {
            if (!mainpic.toString().equals("")){
                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_uploads/" + mainpic
                                , ivRecceImage, options);
            }

            if (image1.contains("storage")) {
                bmImage1 = BitmapFactory.decodeFile(image1, null);
                ivOtherImage1.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage1.setImageBitmap(bmImage1);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

               /* otherImagefile1_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                otherImagefile1_offline = new File(image1);
                offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));
            } else {
                if (!image1.equals("")) {
                    ImageLoader.getInstance().loadImage(web+"image_uploads/recce_images_1_uploads/" + image1, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ivOtherImage1.setImageBitmap(loadedImage);
                            //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                            // Do whatever you want with Bitmap
                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/RecceImages/");
                            myDir.mkdirs();
                            otherImagefile1_offline = new File(myDir + image1);
                            OutputStream outStream = null;
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                outStream = new FileOutputStream(otherImagefile1_offline);
                                outStream.write(bytes.toByteArray());
                                outStream.flush();
                                outStream.close();

                                offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                                imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                                        RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
              /*  */
                    if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                            !Preferences.getidpath1(Update_Recce.this).equals("")) {
                        bmImage1 = BitmapFactory.decodeFile(Preferences.getidpath1(Update_Recce.this), null);
                        ivOtherImage1.setScaleType(ImageView.ScaleType.FIT_XY);
                        ivOtherImage1.setImageBitmap(bmImage1);
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile1_offline = new File(Preferences.getidpath1(Update_Recce.this));
                        offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                        imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                    } else {
                        ivOtherImage1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivOtherImage1.setImageResource(R.drawable.dummy);
                        Bitmap bitmap = ((BitmapDrawable) ivOtherImage1.getDrawable()).getBitmap();

                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile1_offline = new File(myDir, "noimage.jpg");

                        OutputStream outStream = null;
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            outStream = new FileOutputStream(otherImagefile1_offline);
                            outStream.write(bytes.toByteArray());
                            outStream.flush();
                            outStream.close();

                            offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();

                            imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            if (image2.contains("storage")) {
                bmImage2 = BitmapFactory.decodeFile(image2, null);
                ivOtherImage1.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage2.setImageBitmap(bmImage2);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

               /* otherImagefile2_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                otherImagefile2_offline = new File(image2);
                offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));
            } else {
                if (!image2.equals("")) {

                    ImageLoader.getInstance().loadImage(web+"image_uploads/recce_images_2_uploads/" + image2, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ivOtherImage2.setImageBitmap(loadedImage);
                            //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                            // Do whatever you want with Bitmap
                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/RecceImages/");
                            myDir.mkdirs();

                            otherImagefile2_offline = new File(myDir + image2);
                            OutputStream outStream = null;
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                outStream = new FileOutputStream(otherImagefile2_offline);
                                outStream.write(bytes.toByteArray());
                                outStream.flush();
                                outStream.close();

                                offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                                imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                                        RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
              /*  */
                    if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                            !Preferences.getidpath2(Update_Recce.this).equals("")) {
                        bmImage2 = BitmapFactory.decodeFile(Preferences.getidpath2(Update_Recce.this), null);
                        ivOtherImage2.setScaleType(ImageView.ScaleType.FIT_XY);
                        ivOtherImage2.setImageBitmap(bmImage2);
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile2_offline = new File(Preferences.getidpath2(Update_Recce.this));
                        offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                        imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                    } else {
                        ivOtherImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivOtherImage2.setImageResource(R.drawable.dummy);
                        Bitmap bitmap = ((BitmapDrawable) ivOtherImage2.getDrawable()).getBitmap();

                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile2_offline = new File(myDir, "noimage.jpg");

                        OutputStream outStream = null;
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            outStream = new FileOutputStream(otherImagefile2_offline);
                            outStream.write(bytes.toByteArray());
                            outStream.flush();
                            outStream.close();

                            offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                            imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }


            if (image3.contains("storage")) {
                bmImage3 = BitmapFactory.decodeFile(image3, null);
                ivOtherImage3.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage3.setImageBitmap(bmImage3);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

              /*  otherImagefile3_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                otherImagefile3_offline = new File(image3);
                offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));
            } else {
                if (!image3.equals("")) {

                    ImageLoader.getInstance().loadImage(web+"image_uploads/recce_images_3_uploads/" + image3, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ivOtherImage3.setImageBitmap(loadedImage);
                            //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                            // Do whatever you want with Bitmap
                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/RecceImages/");
                            myDir.mkdirs();

                          /*  otherImagefile3_offline = new File(myDir,
                                    String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                            otherImagefile3_offline = new File(myDir + image3);
                            OutputStream outStream = null;
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                outStream = new FileOutputStream(otherImagefile3_offline);
                                outStream.write(bytes.toByteArray());
                                outStream.flush();
                                outStream.close();

                                offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                                imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                                        RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
              /*  */
                    if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                            !Preferences.getidpath3(Update_Recce.this).equals("")) {
                        bmImage3 = BitmapFactory.decodeFile(Preferences.getidpath3(Update_Recce.this), null);
                        ivOtherImage3.setScaleType(ImageView.ScaleType.FIT_XY);
                        ivOtherImage3.setImageBitmap(bmImage3);
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile3_offline = new File(Preferences.getidpath3(Update_Recce.this));
                        offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                        imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                    } else {
                        ivOtherImage3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivOtherImage3.setImageResource(R.drawable.dummy);
                        Bitmap bitmap = ((BitmapDrawable) ivOtherImage3.getDrawable()).getBitmap();

                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile3_offline = new File(myDir, "noimage.jpg");

                        OutputStream outStream = null;
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            outStream = new FileOutputStream(otherImagefile3_offline);
                            outStream.write(bytes.toByteArray());
                            outStream.flush();
                            outStream.close();

                            offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                            imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            if (image4.contains("storage")) {
                bmImage4 = BitmapFactory.decodeFile(image4, null);
                ivOtherImage4.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage4.setImageBitmap(bmImage4);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile4_offline = new File(image4);
                offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));
            } else {
                if (!image4.equals("")) {

                    ImageLoader.getInstance().loadImage(web+"image_uploads/recce_images_4_uploads/" + image4, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ivOtherImage4.setImageBitmap(loadedImage);
                            //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                            // Do whatever you want with Bitmap
                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/RecceImages/");
                            myDir.mkdirs();
                            otherImagefile4_offline = new File(myDir + image4);

                            OutputStream outStream = null;
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                outStream = new FileOutputStream(otherImagefile4_offline);
                                outStream.write(bytes.toByteArray());
                                outStream.flush();
                                outStream.close();

                                offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                                imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                                        RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
              /*  */
                    if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name) &&
                            !Preferences.getidpath4(Update_Recce.this).equals("")) {
                        bmImage4 = BitmapFactory.decodeFile(Preferences.getidpath4(Update_Recce.this), null);
                        ivOtherImage4.setScaleType(ImageView.ScaleType.FIT_XY);
                        ivOtherImage4.setImageBitmap(bmImage4);
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile4_offline = new File(Preferences.getidpath4(Update_Recce.this));
                        offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                        imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));

                    } else {
                        ivOtherImage4.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        ivOtherImage4.setImageResource(R.drawable.dummy);
                        Bitmap bitmap = ((BitmapDrawable) ivOtherImage4.getDrawable()).getBitmap();

                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();
                        otherImagefile4_offline = new File(myDir, "noimage.jpg");

                        OutputStream outStream = null;
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            outStream = new FileOutputStream(otherImagefile4_offline);
                            outStream.write(bytes.toByteArray());
                            outStream.flush();
                            outStream.close();

                            offlineimgpath4 = otherImagefile4_offline.getAbsolutePath();

                            imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    }

    @OnClick(R.id.ivOtherImage4)
    public void imagefour() {
        image4_select = "selected";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile4 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
       // iv_url4 = Uri.fromFile(otherImagefile4);
        iv_url4 = FileProvider.getUriForFile(
                Update_Recce.this,
                Update_Recce.this
                        .getPackageName() + ".provider", otherImagefile4);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url4);
        startActivityForResult(intent, O_IMAGE4);
    }

    @OnClick(R.id.ivOtherImage3)
    public void imagethree() {
        image3_select = "selected";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile3 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
       // iv_url3 = Uri.fromFile(otherImagefile3);
        iv_url3 = FileProvider.getUriForFile(
                Update_Recce.this,
                Update_Recce.this
                        .getPackageName() + ".provider", otherImagefile3);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url3);
        startActivityForResult(intent, O_IMAGE3);
    }

    @OnClick(R.id.ivOtherImage2)
    public void imagtwo() {
        image2_select = "2";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile2 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
      //  iv_url2 = Uri.fromFile(otherImagefile2);
        iv_url2 = FileProvider.getUriForFile(
                Update_Recce.this,
                Update_Recce.this
                        .getPackageName() + ".provider", otherImagefile2);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url2);
        startActivityForResult(intent, O_IMAGE2);
    }

    @OnClick(R.id.ivOtherImage1)
    public void imageone() {
        image1_select = "1";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile1 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");

        iv_url1 = FileProvider.getUriForFile(
                Update_Recce.this,
                Update_Recce.this
                        .getPackageName() + ".provider", otherImagefile1);

       // iv_url1 = Uri.fromFile(otherImagefile1);

        Log.d("hu", otherImagefile1.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url1);
        startActivityForResult(intent, O_IMAGE1);
    }

    @OnClick(R.id.ivRecceImage)
    public void recceMan() {
          Toast.makeText(getBaseContext(), "hello", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Update_Recce.this, CanvasEdit.class);
        intent.putExtra("width", tvRwidth.getText().toString());
        intent.putExtra("height", tvRheight.getText().toString());
        intent.putExtra("product_name", productname);
        intent.putExtra("uomname", uomname);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.done_rl)
    public void done() {

        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Preferences.getKey(Update_Recce.this));
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserid(Update_Recce.this));
        RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getCrewPersonid_project(Update_Recce.this));
        RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("recce_id").toString());
        //Log.d("key",Preferences.getKey());
        //Log.d("userid",Preferences.getUserid());
        //Log.d("crewpersonid",Preferences.getCrewPersonid_project());
        //Log.d("recceid",getIntent().getStringExtra("recce_id").toString());
        if (iv_urlRC == null) {
            Toast.makeText(getBaseContext(), "please select Main image", Toast.LENGTH_SHORT).show();
        } else if (iv_url1 == null && !getIntent().getStringExtra("imagestatus").toString().equals("Completed")
                && !outlet_name.equals(Preferences.getOutletname(Update_Recce.this))) {
            Toast.makeText(getBaseContext(), "please select First Identification Image ", Toast.LENGTH_SHORT).show();
        } else {
            width = tvRwidth.getText().toString();
            height = tvRheight.getText().toString();
            width_feet = tvRwf.getText().toString();
            height_feet = tvRhf.getText().toString();
            width_inch = tvRwi.getText().toString();
            height_inch = tvRhi.getText().toString();

            File mainpic = new File(iv_urlRC.toString());
            compressImage(mainpic.getAbsolutePath().toString());
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("recce_image", mainpic.getName(),
                    RequestBody.create(MediaType.parse("image/*"), mainpic));

            if (!image1_select.equals("")) {
                imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile1));
                offlineimgpath1 = otherImagefile1.getAbsolutePath();

            } else if (image1.equals("")) {
               /* imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile1));
                offlineimgpath1 = otherImagefile1.getAbsolutePath();*/
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name)) {
                } else {
                    imageFilePart3 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile3.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile3));
                    offlineimgpath3 = otherImagefile3.getAbsolutePath();
                }
            }
            if (!image2_select.equals("")) {
                imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile2));
                offlineimgpath2 = otherImagefile2.getAbsolutePath();

            } else if (image2.equals("")) {
               /* imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile2));
                offlineimgpath2 = otherImagefile2.getAbsolutePath();*/
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name)) {
                } else {
                    imageFilePart3 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile3.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile3));
                    offlineimgpath3 = otherImagefile3.getAbsolutePath();
                }
            }

            if (!image3_select.equals("")) {
                imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile3));
                offlineimgpath3 = otherImagefile3.getAbsolutePath();

            } else if (image3.equals("")) {
               /* imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile3));
                offlineimgpath3 = otherImagefile3.getAbsolutePath();*/
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name)) {
                } else {
                    imageFilePart3 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile3.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile3));
                    offlineimgpath3 = otherImagefile3.getAbsolutePath();
                }

            }
            if (!image4_select.equals("")) {
                imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile4));
                offlineimgpath4 = otherImagefile4.getAbsolutePath();

            } else if (image4.equals("")) {
                if (Preferences.getOutletname(Update_Recce.this).equals(outlet_name)) {
                  /*  otherImagefile4_offline = new File(Preferences.getidpath4(Update_Recce.this));
                    imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile4_offline));
                    offlineimgpath4 = otherImagefile4.getAbsolutePath();*/
                } else {
                    imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile4));
                    offlineimgpath4 = otherImagefile4.getAbsolutePath();
                }

              /*  imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile4));
                offlineimgpath4 = otherImagefile4.getAbsolutePath();*/

            }

            RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), latitude);
            RequestBody log = RequestBody.create(MediaType.parse("text/plain"), longitude);
            RequestBody address_s = RequestBody.create(MediaType.parse("text/plain"), address);

            Preferences.setOutlet(outlet_name, offlineimgpath1.toString(), offlineimgpath2.toString(), offlineimgpath3.toString(),
                    offlineimgpath4.toString(), Update_Recce.this);
            uploadRecce(uomid, width, height
                    , width_feet, height_feet, width_inch
                    , height_inch, product_id, key, userid, crew_person_id, recce_id, filePart, imageFilePart1,
                    imageFilePart2, imageFilePart3, imageFilePart4, lat, log, address_s);


            //Log.d("updateimagereccepath", mainpic.getAbsolutePath().toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1257:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "Please Allow Permissions to continue.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                //this.canvas.setBackground(getResources().getDrawable(R.drawable.draw));
                //Log.d("hh", Uri.parse(data.getExtras().get("imagePath").toString()).toString());
                Bitmap bmImage = BitmapFactory.decodeFile(data.getExtras().get("imagePath").toString(), opt);
                iv_urlRC = data.getExtras().get("imagePath").toString();
                //ivRecceImage.setImageBitmap(bmImage);
                if (data.getExtras().containsKey("startX")) {
                    int startX = (int) data.getExtras().getFloat("startX");
                    int startY = (int) data.getExtras().getFloat("startY");
                    int endX = (int) data.getExtras().getFloat("controlX");
                    int endY = (int) data.getExtras().getFloat("controlY");
                    Rect rect = new Rect(startX, startY, endX, endY);
                    //calculate height and length
                    tvRwidth.setText(data.getExtras().getString("Width") + "");
                    tvRheight.setText(data.getExtras().getString("Height") + "");

                    try {
                        BitmapFactory.Options opt_hel = new BitmapFactory.Options();
                        opt_hel.inSampleSize = 8;
                        opt_hel.inMutable = true;
                        bmImage_main = BitmapFactory.decodeFile(iv_urlRC.toString(), opt);
                        ivRecceImage.setImageBitmap(bmImage_main);
                        compressImage(iv_urlRC.toString());
                        //ivOtherImage1.setImageBitmap(bmImage_main);
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (requestCode == O_IMAGE1 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile1.getPath().toString(), opt);
                ivOtherImage1.setImageBitmap(bmImage);
                compressImage(otherImagefile1.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (requestCode == O_IMAGE2 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
               // Bitmap bmImage = BitmapFactory.decodeFile(iv_url2.getPath().toString(), opt);
                Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile2.getPath().toString(), opt);
                ivOtherImage2.setImageBitmap(bmImage);
                compressImage(otherImagefile2.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("msg", e.getMessage());
            }
        } else if (requestCode == O_IMAGE3 && resultCode == RESULT_OK) {

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = 8;
            opt.inMutable = true;
           // Bitmap bmImage = BitmapFactory.decodeFile(iv_url3.getPath().toString(), opt);
            Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile3.getPath().toString(), opt);
            ivOtherImage3.setImageBitmap(bmImage);
            compressImage(otherImagefile3.getAbsolutePath().toString());
        } else if (requestCode == O_IMAGE4 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
               // Bitmap bmImage = BitmapFactory.decodeFile(iv_url4.getPath().toString(), opt);
                Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile4.getPath().toString(), opt);
                ivOtherImage4.setImageBitmap(bmImage);
                compressImage(otherImagefile4.getAbsolutePath().toString());
            } /*catch (OutOfMemoryError ome) {
                ome.printStackTrace();
            }*/ catch (Exception e) {
                Log.e("e", e.getMessage());
            }

        } else {
            Toast.makeText(getBaseContext(), "Sorry !! please go back and do recce..", Toast.LENGTH_SHORT).show();
            Intent displarecce = new Intent(Update_Recce.this, Recces_display.class);
            startActivity(displarecce);
            finish();
        }
    }


    public void uploadRecce(@Query("uom_id") String uom_id, @Query("width") String width,
                            @Query("height") String height, @Query("w_f") String w_f, @Query("h_f") String h_f,
                            @Query("w_i") String w_i, @Query("h_i") String h_i, @Query("product_id") String pdct_id,
                            @Part("key") RequestBody key,
                            @Part("user_id") RequestBody user_id, @Part("h_i") RequestBody crew_person_id,
                            @Part("recce_id") RequestBody recce_id,
                            @Part MultipartBody.Part recce_image,
                            @Part MultipartBody.Part recce_image_1,
                            @Part MultipartBody.Part recce_image_2,
                            @Part MultipartBody.Part recce_image_3,
                            @Part MultipartBody.Part recce_image_4,
                            @Part("latitude") final RequestBody lat,
                            @Part("longitude") RequestBody longi, @Part("outlet_address") RequestBody outlet_address) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadRecce> call = apiService.getUploadRecce(uom_id, width, height, w_f, h_f, w_i, h_i, pdct_id, key,
                user_id, crew_person_id, recce_id, recce_image, recce_image_1, recce_image_2, recce_image_3, recce_image_4,
                lat, longi, outlet_address);
        call.enqueue(new Callback<UploadRecce>() {
            @Override
            public void onResponse(Call<UploadRecce> call, Response<UploadRecce> response) {
                String result = String.valueOf(response.code());
                Log.d("goodma", result + " " + offlineimgpath1);
                Log.d("goodma", offlineimgpath2);
                if (result.equals("200")) {
                    updateRecce_Localdb(uomid, tvRwidth.getText().toString(), tvRheight.getText().toString()
                            , width_feet, height_feet, width_inch, height_inch, product_id, Preferences.getKey(Update_Recce.this), Preferences.getUserid(Update_Recce.this), Preferences.getCrewPersonid_project(Update_Recce.this)
                            , getIntent().getStringExtra("recce_id").toString(), iv_urlRC,
                            offlineimgpath1.toString(), offlineimgpath2.toString()
                            , offlineimgpath3.toString(), offlineimgpath4.toString(),
                            latitude, longitude, address.replaceAll("'", ""), Preferences.getProjectId(Update_Recce.this), "online_update", "Completed");
                } else {
                    updateRecce_Localdb(uomid, tvRwidth.getText().toString(), tvRheight.getText().toString()
                            , width_feet, height_feet, width_inch, height_inch, product_id, Preferences.getKey(Update_Recce.this), Preferences.getUserid(Update_Recce.this),
                            Preferences.getCrewPersonid_project(Update_Recce.this)
                            , getIntent().getStringExtra("recce_id").toString(), iv_urlRC,
                            offlineimgpath1.toString(), offlineimgpath2.toString()
                            , offlineimgpath3.toString(), offlineimgpath4.toString(),
                            latitude, longitude, address, Preferences.getProjectId(Update_Recce.this), "offline_update", "Completed");
                }

            }

            @Override
            public void onFailure(Call<UploadRecce> call, Throwable throwable) {
                // Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("message_image", throwable.toString());
                updateRecce_Localdb(uomid, tvRwidth.getText().toString(), tvRheight.getText().toString()
                        , width_feet, height_feet, width_inch, height_inch, product_id, Preferences.getKey(Update_Recce.this), Preferences.getUserid(Update_Recce.this),
                        Preferences.getCrewPersonid_project(Update_Recce.this)
                        , getIntent().getStringExtra("recce_id").toString(), iv_urlRC,
                        offlineimgpath1.toString(), offlineimgpath2.toString()
                        , offlineimgpath3.toString(), offlineimgpath4.toString(),
                        latitude, longitude, address, Preferences.getProjectId(Update_Recce.this), "offline_update", "Completed");

            }
        });
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    // Uploading Image/Video
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        // String filename = getFilename();
        try {
            out = new FileOutputStream(imageUri);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return imageUri;

    }

    public void updateRecce_Localdb(String uom_id, String width, String height, String width_feet, String height_feet,
                                    String width_inches, String height_inches, String product_id, String key, String userid, String crewpersonid,
                                    String recce_id, String recce_image,
                                    String recce_image_1, String recce_image_2, String recce_image_3, String recce_image_4,
                                    String latitude, String longitude, String outlet_address, String project_id, String uoms, String status) {

        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("UPDATE recce SET uom_id='" + uom_id + "',width='" + width + "',key='" + key + "',userid='" + userid
                + "',crewpersonid='" + crewpersonid +
                "',height='" + height + "',width_feet='" + width_feet + "',uoms='" + uoms +
                "',height_feet='" + height_feet + "',width_inches='" + width_inches +
                "',height_inches='" + height_inches + "',product_id='" + product_id +
                "',recce_image='" + recce_image + "',recce_image_1='" + recce_image_1 +
                "',recce_image_2='" + recce_image_2 + "',recce_image_3='" + recce_image_3 +
                "',recce_image_4='" + recce_image_4 + "',latitude='" + latitude
                + "',recce_image_upload_status='" + status + "',longitude='" + longitude + "',outlet_address='" + outlet_address + "'" + "WHERE recce_id=" + recce_id);
        db.close();
        //Log.d("success", "successfully updated recce");
        Intent recce_display = new Intent(Update_Recce.this, Recces_display.class);
        startActivity(recce_display);
        finish();
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
