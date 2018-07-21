package srinivas.sams.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.UploadInstall;
import srinivas.sams.rest.ApiClient;
import srinivas.sams.rest.ApiInterface;
import srinivas.sams.validation.Validation;

public class Update_Install extends Activity {
    @BindView(R.id.recceImage)
    ImageView recceImage;
    @BindView(R.id.recceInstallImage)
    ImageView recceInstallImage;
    @BindView(R.id.ivOtherImage2)
    ImageView ivOtherImage2;
    @BindView(R.id.ivOtherImage3)
    ImageView ivOtherImage3;
    @BindView(R.id.InstallationRemarks_et)
    EditText InstallationRemarks_et;
    @BindView(R.id.InstallationDate_et)
    EditText InstallationDate_et;
    SQLiteDatabase db;
    File installimage = null;
    private DisplayImageOptions options;
    public int R_IMAGE = 2, O_IMAGE1 = 3, O_IMAGE2 = 4, O_IMAGE3 = 5, O_IMAGE4 = 6;
    File otherImagefile1 = null; File otherImagefile2 = null; File otherImagefile3 = null;
    String image1_select = "",image2_select="",image3_select="", installationdate;
    File otherImagefile1_offline = null, otherImagefile2_offline = null, otherImagefile3_offline = null;
    FileOutputStream fos = null;
    String offlineimgpath1 = "",offlineimgpath2 = "",offlineimgpath3 = "",image2,image3;
    MultipartBody.Part imageFilePart1 = null,imageFilePart2=null,imageFilePart3=null;
    protected Uri iv_url1 = null, iv_url2 = null, iv_url3 = null, iv_url4 = null;
    Bitmap bmImage = null,bmImage1 = null,bmImage2 = null,bmImage3 = null,bmImage4 = null;;
  //  public String web="http://128.199.131.14/sams/web/";
  //public String web="http://128.199.131.14/samsdev/web/";
    public String web="http://128.199.131.14/samsapp/web/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update__install);
        ButterKnife.bind(this);
        clearPreferences();
        image2 = getIntent().getStringExtra("install_image_2");
        image3 = getIntent().getStringExtra("install_image_3");
       // Toast.makeText(getBaseContext(),image2,Toast.LENGTH_SHORT).show();
        Log.d("image_install_path2",image2);
        InstallationDate_et.setText(getIntent().getStringExtra("install_date").toString());
        InstallationRemarks_et.setText(getIntent().getStringExtra("install_remark").toString());
        installimage = new File(getExternalCacheDir(), "noimage.png");
        if (!installimage.exists()) {
            try {
                installimage.createNewFile();
                // iv_url1 = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        otherImagefile2 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile3 = new File(getExternalCacheDir(), "noimage.png");
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

        Bitmap bmImage = null;
        if (!Validation.internet(Update_Install.this)) {
            ImageLoader.getInstance()
                    .displayImage(web+"image_uploads/recce_uploads/" + getIntent().getStringExtra("recce_image")
                            , recceImage, options);
            if (getIntent().getStringExtra("install_image").contains("storage")) {
                bmImage = BitmapFactory.decodeFile(getIntent().getStringExtra("install_image").toString(), null);
                recceInstallImage.setImageBitmap(bmImage);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile1_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bmImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile1_offline);
                    outStream.write(bytes.toByteArray());
                    // Toast.makeText(getBaseContext(), outStream.toString(), Toast.LENGTH_SHORT).show();
                    outStream.flush();
                    outStream.close();

                    offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();
                    // Toast.makeText(Update_Install.this, offlineimgpath1, Toast.LENGTH_SHORT).show();

                    imageFilePart1 = MultipartBody.Part.createFormData("installation_image", otherImagefile1_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                ImageLoader.getInstance().loadImage(web+"image_uploads/install_uploads/" +
                        getIntent().getStringExtra("install_image"), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        recceInstallImage.setImageBitmap(loadedImage);
                        //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                        // Do whatever you want with Bitmap
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();

                        otherImagefile1_offline = new File(myDir,
                                String.valueOf(System.currentTimeMillis()) + ".jpg");
                        OutputStream outStream = null;
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            outStream = new FileOutputStream(otherImagefile1_offline);
                            outStream.write(bytes.toByteArray());
                            // Toast.makeText(getBaseContext(), outStream.toString(), Toast.LENGTH_SHORT).show();
                            outStream.flush();
                            outStream.close();

                            offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();
                            //  Toast.makeText(Update_Install.this, offlineimgpath1, Toast.LENGTH_SHORT).show();

                            imageFilePart1 = MultipartBody.Part.createFormData("installation_image", otherImagefile1_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            if (getIntent().getStringExtra("install_image_2").contains("storage")) {
                bmImage = BitmapFactory.decodeFile(getIntent().getStringExtra("install_image_2").toString(), null);
                ivOtherImage2.setImageBitmap(bmImage);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile2_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bmImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile2_offline);
                    outStream.write(bytes.toByteArray());
                    // Toast.makeText(getBaseContext(), outStream.toString(), Toast.LENGTH_SHORT).show();
                    outStream.flush();
                    outStream.close();

                    offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();
                    // Toast.makeText(Update_Install.this, offlineimgpath1, Toast.LENGTH_SHORT).show();

                    imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile2_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
              /*  bmImage = BitmapFactory.decodeFile(image2.toString(), null);
                ivOtherImage2.setImageBitmap(bmImage);*/

                ivOtherImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivOtherImage2.setImageResource(R.drawable.dummy);
                Bitmap bitmap = ((BitmapDrawable)ivOtherImage2.getDrawable()).getBitmap();

               /* bmImage1 = BitmapFactory.decodeFile(image1, null);
                ivOtherImage1.setImageBitmap(bmImage1);*/
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile2_offline = new File(myDir,"noimage.jpg");
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile2_offline);
                    outStream.write(bytes.toByteArray());
                    outStream.flush();
                    outStream.close();

                    offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                    imageFilePart2 = MultipartBody.Part.createFormData("installation_image_3", otherImagefile2_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (getIntent().getStringExtra("install_image_3").contains("storage")) {
                bmImage = BitmapFactory.decodeFile(getIntent().getStringExtra("install_image_3").toString(), null);
                ivOtherImage3.setImageBitmap(bmImage);

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile3_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bmImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile3_offline);
                    outStream.write(bytes.toByteArray());
                    // Toast.makeText(getBaseContext(), outStream.toString(), Toast.LENGTH_SHORT).show();
                    outStream.flush();
                    outStream.close();

                    offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();
                    // Toast.makeText(Update_Install.this, offlineimgpath1, Toast.LENGTH_SHORT).show();

                    imageFilePart3 = MultipartBody.Part.createFormData("installation_image_3", otherImagefile3_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
              /*  bmImage = BitmapFactory.decodeFile(image2.toString(), null);
                ivOtherImage2.setImageBitmap(bmImage);*/

                ivOtherImage3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                ivOtherImage3.setImageResource(R.drawable.dummy);
                Bitmap bitmap = ((BitmapDrawable)ivOtherImage3.getDrawable()).getBitmap();

               /* bmImage1 = BitmapFactory.decodeFile(image1, null);
                ivOtherImage1.setImageBitmap(bmImage1);*/
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

                otherImagefile3_offline = new File(myDir,"noimage.jpg");
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile3_offline);
                    outStream.write(bytes.toByteArray());
                    outStream.flush();
                    outStream.close();

                    offlineimgpath3= otherImagefile3_offline.getAbsolutePath();

                    imageFilePart3 = MultipartBody.Part.createFormData("installation_image_3", otherImagefile2_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {

            ImageLoader.getInstance()
                    .displayImage(web+"image_uploads/recce_uploads/" + getIntent().getStringExtra("recce_image")
                            , recceImage, options);
            if (!getIntent().getStringExtra("install_image").equals("")) {
                ImageLoader.getInstance().loadImage(web+"image_uploads/install_uploads/" +
                        getIntent().getStringExtra("install_image"), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        recceInstallImage.setImageBitmap(loadedImage);
                        //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                        // Do whatever you want with Bitmap
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/RecceImages/");
                        myDir.mkdirs();

                        otherImagefile1_offline = new File(myDir,
                                String.valueOf(System.currentTimeMillis()) + ".jpg");
                        OutputStream outStream = null;
                        try {
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            outStream = new FileOutputStream(otherImagefile1_offline);
                            outStream.write(bytes.toByteArray());

                            //Toast.makeText(getBaseContext(), outStream.toString(), Toast.LENGTH_SHORT).show();
                            outStream.flush();
                            outStream.close();
                            /*imageFilePart1[0] = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));*/
                            offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();
                            // Toast.makeText(Update_Install.this, offlineimgpath1, Toast.LENGTH_SHORT).show();

                            imageFilePart1 = MultipartBody.Part.createFormData("installation_image", otherImagefile1_offline.getName(),
                                    RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else {
                // Toast.makeText(getBaseContext(),"else noimage.png",Toast.LENGTH_SHORT).show();
                recceInstallImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                recceInstallImage.setImageResource(R.drawable.dummy);

                Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
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

                    imageFilePart1 = MultipartBody.Part.createFormData("installation_image", otherImagefile1_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (image2.contains("storage")) {
               // Toast.makeText(getBaseContext(),"storareg image2",Toast.LENGTH_SHORT).show();
                bmImage2 = BitmapFactory.decodeFile(image2, null);
                ivOtherImage2.setImageBitmap(bmImage);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

               /* otherImagefile2_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                otherImagefile2_offline = new File(image2);
              //  Toast.makeText(Update_Install.this,image2,Toast.LENGTH_SHORT).show();
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bmImage2.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile2_offline);
                    outStream.write(bytes.toByteArray());
                    outStream.flush();
                    outStream.close();

                    offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                    imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile2_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (!image2.equals("")){
                    Log.d("image_path2",web+"image_uploads/" +
                            "installation_images_2_uploads/"+image2);
                  //  Toast.makeText(getBaseContext()," "+image2,Toast.LENGTH_SHORT).show();
                    ImageLoader.getInstance().loadImage(web+"image_uploads/" +
                                    "installation_images_1_uploads/"+image2
                            , new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            ivOtherImage2.setImageBitmap(loadedImage);
                            //  Bitmap bitmap = ((BitmapDrawable)recceInstallImage.getDrawable()).getBitmap();
                            // Do whatever you want with Bitmap
                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/RecceImages/");
                            myDir.mkdirs();

                          /*  otherImagefile2_offline = new File(myDir,
                                    String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                            otherImagefile2_offline = new File(myDir+image2);
                            OutputStream outStream = null;
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                outStream = new FileOutputStream(otherImagefile2_offline);
                                outStream.write(bytes.toByteArray());
                                outStream.flush();
                                outStream.close();

                                offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                                imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile2_offline.getName(),
                                        RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else {
              /*  bmImage = BitmapFactory.decodeFile(image2.toString(), null);
                ivOtherImage2.setImageBitmap(bmImage);*/
                  //  Toast.makeText(getBaseContext(),"storareg dummy",Toast.LENGTH_SHORT).show();
                    ivOtherImage2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivOtherImage2.setImageResource(R.drawable.dummy);

                    Bitmap bitmap = ((BitmapDrawable)ivOtherImage2.getDrawable()).getBitmap();

               /* bmImage1 = BitmapFactory.decodeFile(image1, null);
                ivOtherImage1.setImageBitmap(bmImage1);*/
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages");
                    myDir.mkdirs();

                    otherImagefile2_offline = new File(myDir,"noimage.jpg");
                    OutputStream outStream = null;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        outStream = new FileOutputStream(otherImagefile2_offline);
                        outStream.write(bytes.toByteArray());
                        outStream.flush();
                        outStream.close();

                        offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

                        imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile2_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (image3.contains("storage")) {
                bmImage3 = BitmapFactory.decodeFile(image3, null);
                ivOtherImage3.setImageBitmap(bmImage3);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/RecceImages/");
                myDir.mkdirs();

              /*  otherImagefile3_offline = new File(myDir,
                        String.valueOf(System.currentTimeMillis()) + ".jpg");*/
                otherImagefile3_offline = new File(image3);
                OutputStream outStream = null;
                try {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bmImage3.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    outStream = new FileOutputStream(otherImagefile3_offline);
                    outStream.write(bytes.toByteArray());
                    outStream.flush();
                    outStream.close();

                    offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                    imageFilePart3 = MultipartBody.Part.createFormData("installation_image_2", otherImagefile3_offline.getName(),
                            RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (!image3.equals("")){

                    ImageLoader.getInstance().loadImage(web+"image_uploads/" +
                            "installation_images_2_uploads/"+image3, new SimpleImageLoadingListener() {
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
                            otherImagefile3_offline = new File(myDir+image3);
                            OutputStream outStream = null;
                            try {
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                outStream = new FileOutputStream(otherImagefile3_offline);
                                outStream.write(bytes.toByteArray());
                                outStream.flush();
                                outStream.close();

                                offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                                imageFilePart3 = MultipartBody.Part.createFormData("installation_image_2", otherImagefile3_offline.getName(),
                                        RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
              /*  bmImage = BitmapFactory.decodeFile(image2.toString(), null);
                ivOtherImage2.setImageBitmap(bmImage);*/

                    ivOtherImage3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivOtherImage3.setImageResource(R.drawable.dummy);
                    Bitmap bitmap = ((BitmapDrawable)ivOtherImage3.getDrawable()).getBitmap();

               /* bmImage1 = BitmapFactory.decodeFile(image1, null);
                ivOtherImage1.setImageBitmap(bmImage1);*/
                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/RecceImages/");
                    myDir.mkdirs();

                    otherImagefile3_offline = new File(myDir,"noimage.jpg");
                    OutputStream outStream = null;
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        outStream = new FileOutputStream(otherImagefile3_offline);
                        outStream.write(bytes.toByteArray());
                        outStream.flush();
                        outStream.close();

                        offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

                        imageFilePart3 = MultipartBody.Part.createFormData("installation_image_2", otherImagefile3_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    @OnClick(R.id.update_install_btn)
    public void updateInstallrecce() {

       /* RequestBody key = RequestBody.create(MediaType.parse("text/plain"), "vwqoBF2z3p6k5yCMsoSF3hlI1wisRecY");
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), "50");
        RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), "33");
        RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), "2312");
        RequestBody project_id = RequestBody.create(MediaType.parse("text/plain"), "4");*/

        if (iv_url1 == null && !getIntent().getStringExtra("status").equals("Completed")) {
            Toast.makeText(Update_Install.this, "Please Take Installation Pic ", Toast.LENGTH_SHORT).show();
        } else {
            if (image1_select.equals("")) {
                installationdate = InstallationDate_et.getText().toString();
                imageFilePart1 = MultipartBody.Part.createFormData("installation_image", otherImagefile1_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));
                offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();
               /* if (Validation.internet(Update_Install.this)) {

                } else {
                    installationdate = InstallationDate_et.getText().toString();
                    if (!Validation.internet(Update_Install.this)) {
                        Toast.makeText(Update_Install.this, "Sorry Please Check Internet Connection we are moving offline mode",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        imageFilePart1 = MultipartBody.Part.createFormData("installation_image", otherImagefile1_offline.getName(),
                                RequestBody.create(MediaType.parse("image"), otherImagefile1_offline));
                        offlineimgpath1 = otherImagefile1_offline.getAbsolutePath();
                    }
                }*/
            } else {
                installationdate = InstallationDate_et.getText().toString();
                imageFilePart1 = MultipartBody.Part.createFormData("installation_image", installimage.getName(),
                        RequestBody.create(MediaType.parse("image"), installimage));
                offlineimgpath1 = installimage.getAbsolutePath();
            }

            if (image2_select.equals("")) {
                installationdate = InstallationDate_et.getText().toString();
                imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile2_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile2_offline));
                offlineimgpath2 = otherImagefile2_offline.getAbsolutePath();

            } else {
                installationdate = InstallationDate_et.getText().toString();
                imageFilePart2 = MultipartBody.Part.createFormData("installation_image_1", otherImagefile2.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile2));
                offlineimgpath2 = otherImagefile2.getAbsolutePath();

            }

            if (image3_select.equals("")) {
                installationdate = InstallationDate_et.getText().toString();
                imageFilePart3 = MultipartBody.Part.createFormData("installation_image_2", otherImagefile3_offline.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile3_offline));
                offlineimgpath3 = otherImagefile3_offline.getAbsolutePath();

            } else {
                installationdate = InstallationDate_et.getText().toString();
                imageFilePart3 = MultipartBody.Part.createFormData("installation_image_2", otherImagefile3.getName(),
                        RequestBody.create(MediaType.parse("image"), otherImagefile3));
                offlineimgpath3 = otherImagefile3.getAbsolutePath();
            }

            RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Preferences.getKey(Update_Install.this));
            RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserid(Update_Install.this));
            RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getCrewPersonid_project(Update_Install.this));
            RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("recce_id").toString());
            RequestBody project_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getProjectId(Update_Install.this).toString());
            Log.d("key", Preferences.getKey(Update_Install.this));
            Log.d("userid", Preferences.getUserid(Update_Install.this));
            Log.d("crewpersonid", Preferences.getCrewPersonid_project(Update_Install.this));
            Log.d("recceid", getIntent().getStringExtra("recce_id").toString());
            Log.d("projectid", Preferences.getProjectId(Update_Install.this));
            Log.d("imageFilePart1", offlineimgpath1);
            Log.d("imageFilePart1", offlineimgpath2);
            Log.d("imageFilePart1", offlineimgpath3);
            Log.d("installdate", InstallationDate_et.getText().toString());
            Log.d("remarks", InstallationRemarks_et.getText().toString());

            updateInstall(installationdate, InstallationRemarks_et.getText().toString(),
                    key, userid, crew_person_id, recce_id, project_id, imageFilePart1,imageFilePart2,imageFilePart3);

        }
        //updateInstall("21-2-2017","asdfasdf",key,userid,crew_person_id,recce_id,project_id,imageFilePart1);
    }

    @OnClick(R.id.mybutton_click)
    public void backme() {
        Intent project = new Intent(Update_Install.this, Install_display.class);
        startActivity(project);
        finish();
    }

    @OnClick(R.id.recceInstallImage)
    public void installImage_new() {
        /*image1_select = "selected";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        installimage = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url1 = Uri.fromFile(installimage);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url1);
        startActivityForResult(intent, 1);

*/
        image1_select = "selected";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        installimage = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        // iv_url3 = Uri.fromFile(otherImagefile3);
        iv_url1 = FileProvider.getUriForFile(
                Update_Install.this,
                Update_Install.this
                        .getPackageName() + ".provider", installimage);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url1);
        startActivityForResult(intent, 1);

    }
    @OnClick(R.id.ivOtherImage3)
    public void imagethree() {
       /* image3_select="selected";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile3 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url3 = Uri.fromFile(otherImagefile3);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url3);
        startActivityForResult(intent, O_IMAGE3);*/

        image3_select = "selected";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile3 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        // iv_url3 = Uri.fromFile(otherImagefile3);
        iv_url3 = FileProvider.getUriForFile(
                Update_Install.this,
                Update_Install.this
                        .getPackageName() + ".provider", otherImagefile3);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url3);
        startActivityForResult(intent, O_IMAGE3);
    }

    @OnClick(R.id.ivOtherImage2)
    public void imagtwo() {
       /* image2_select="2";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile2 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url2 = Uri.fromFile(otherImagefile2);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url2);
        startActivityForResult(intent, O_IMAGE2);*/

        image2_select = "selected";
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/RecceImages/");
        myDir.mkdirs();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile2 = new File(myDir,
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        // iv_url3 = Uri.fromFile(otherImagefile3);
        iv_url2 = FileProvider.getUriForFile(
                Update_Install.this,
                Update_Install.this
                        .getPackageName() + ".provider", otherImagefile2);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url2);
        startActivityForResult(intent, O_IMAGE2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(installimage.getPath().toString(), opt);
                recceInstallImage.setScaleType(ImageView.ScaleType.FIT_XY);
                recceInstallImage.setImageBitmap(bmImage);
                compressImage(installimage.getAbsolutePath().toString());
                //compressImage(installimage.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("e", e.getMessage());
            }
        }
        else if (requestCode == O_IMAGE2 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile2.getPath().toString(), opt);
                ivOtherImage2.setScaleType(ImageView.ScaleType.FIT_XY);
                ivOtherImage2.setImageBitmap(bmImage);
                compressImage(otherImagefile2.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("msg", e.getMessage());
            }
        }  else if (requestCode == O_IMAGE3 && resultCode == RESULT_OK) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = 8;
            opt.inMutable = true;
            Bitmap bmImage = BitmapFactory.decodeFile(otherImagefile3.getPath().toString(), opt);
            ivOtherImage3.setScaleType(ImageView.ScaleType.FIT_XY);
            ivOtherImage3.setImageBitmap(bmImage);
            compressImage(otherImagefile3.getAbsolutePath().toString());
        }
    }

    public void updateInstall(@Query("installation_date") final String installation_date,
                              @Query("installation_remarks") final String installation_remarks,
                              @Part("key") RequestBody key,
                              @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                              @Part("recce_id") final RequestBody recce_id, @Part("project_id") final RequestBody project_id,
                              @Part final MultipartBody.Part installation_image,
                              @Part final MultipartBody.Part installation_image_1,
                              @Part final MultipartBody.Part installation_image_2) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadInstall> call = apiService.getUploadInstall(installation_date, installation_remarks, key,
                user_id, crew_person_id, recce_id, project_id, installation_image,installation_image_1,installation_image_2);
        call.enqueue(new Callback<UploadInstall>() {
            @Override
            public void onResponse(Call<UploadInstall> call, Response<UploadInstall> response) {
                String result = String.valueOf(response.code());
                Log.d("goodma",result+" "+offlineimgpath1);
                Log.d("goodma",offlineimgpath2);
                if (result.equals("200")) {
                    updateInstall_Localdb(installation_date, installation_remarks, Preferences.getKey(Update_Install.this), Preferences.getUserid(Update_Install.this),
                            Preferences.getCrewpersonid(Update_Install.this), getIntent().getStringExtra("recce_id").toString(),
                            Preferences.getProjectId(Update_Install.this), offlineimgpath1, "online_update", "Completed",
                            offlineimgpath2,offlineimgpath3);
                    //Toast.makeText(getBaseContext(),"successfull ",Toast.LENGTH_SHORT).show();
                    // finish();
                } else {
                    updateInstall_Localdb(installation_date, installation_remarks, Preferences.getKey(Update_Install.this), Preferences.getUserid(Update_Install.this),
                            Preferences.getCrewpersonid(Update_Install.this), getIntent().getStringExtra("recce_id").toString(),
                            Preferences.getProjectId(Update_Install.this), offlineimgpath1, "offline_update", "Completed",
                            offlineimgpath2,offlineimgpath3);
                    updateInstallrecce();

                    Toast.makeText(getBaseContext(), "Please wait we are processing !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UploadInstall> call, Throwable throwable) {
                //  Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                updateInstall_Localdb(installation_date, installation_remarks, Preferences.getKey(Update_Install.this), Preferences.getUserid(Update_Install.this),
                        Preferences.getCrewpersonid(Update_Install.this), getIntent().getStringExtra("recce_id").toString(),
                        Preferences.getProjectId(Update_Install.this), offlineimgpath1, "offline_update", "Completed",
                        offlineimgpath2,offlineimgpath3 );

                Log.d("message_image", throwable.toString());
            }
        });
    }


    public void updateInstall_Localdb(String install_date, String install_remark, String key, String userid, String crewpersonid,
                                      String recce_id, String project_id, String imagefilepart1, String mode,
                                      String installstatus,String imagefilepart2,String imagefilepart3) {

        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        String instal_remarks;
        if (install_remark.length() > 0) {
            instal_remarks = install_remark.replaceAll("'", "");
        } else {
            instal_remarks = install_remark;
        }

        db.execSQL("UPDATE install SET installation_date='" + install_date + "',installation_remarks='" + instal_remarks + "',key='" + key + "',userid='" + userid
                + "',crew_person_id='" + crewpersonid + "',installation_image_upload_status='" + installstatus +
                "',project_id='" + project_id + "',installation_image='" + imagefilepart1
                + "',product0='" + mode + "',imagepath1='" +imagefilepart2+"',imagepath2='"+imagefilepart3+"'"+
                " WHERE recce_id=" + recce_id);


        db.close();
        Intent recce_display = new Intent(Update_Install.this, Install_display.class);
        startActivity(recce_display);
        finish();
        Log.d("success", "successfully updated recce");
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
