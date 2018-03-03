package srinivas.sams.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import srinivas.sams.R;

public class CanvasEdit extends AppCompatActivity implements View.OnClickListener, CanvasView.RectangleListner {
    protected ImageButton btnUndo;
    protected ImageButton btnRedo;
    protected ImageButton btnClear;
    protected ImageButton btnDone;
    protected ImageButton btnEdit;
    protected ImageButton btnRect;
    protected ImageButton btnEraser;
    protected LinearLayout llEdit;
    protected RelativeLayout activityCanvasEdit;
    protected EditText etWidth;
    protected EditText etHeight;
    private CanvasView canvas = null;
    File file;
    Uri fileUri;
    final int RC_TAKE_PHOTO = 1;
    Rect rect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canvas_edit);
         clearPreferences();
//        String url=getIntent().getExtras().getString("cameraURL").toString();
      /*  this.canvas = (CanvasView) this.findViewById(R.id.canvas);
        // Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
        CanvasView.setRectangleListner(CanvasEdit.this);
        Intent intent_camera = getIntent();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        fileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RC_TAKE_PHOTO);
        this.canvas.setPaintStrokeWidth(10F);
        this.canvas.setPaintStrokeColor(Color.RED);
        this.canvas.setDrawer(CanvasView.Drawer.RECTANGLE);
        initView();*/

        this.canvas = (CanvasView) this.findViewById(R.id.canvas);
       // Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
        CanvasView.setRectangleListner(CanvasEdit.this);
        file = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        Log.d("file",file.toString());
// fileUri = Uri.fromFile(file);

// Intent install = new Intent(Intent.ACTION_VIEW);
        //install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        fileUri = FileProvider.getUriForFile(
                CanvasEdit.this,
                CanvasEdit.this
                        .getPackageName() + ".provider", file);
        Log.d("file",fileUri.toString());
// install.setDataAndType(apkURI, null);
        //install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, RC_TAKE_PHOTO);
        this.canvas.setPaintStrokeWidth(10F);
        this.canvas.setPaintStrokeColor(Color.RED);
        this.canvas.setDrawer(CanvasView.Drawer.RECTANGLE);

        initView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_TAKE_PHOTO && resultCode == RESULT_OK) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize=8;
            opt.inMutable = true;
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;
            int height = dm.heightPixels;
            //this.canvas.setBackground(getResources().getDrawable(R.drawable.draw));
            Bitmap bmImage = BitmapFactory.decodeFile(file.getPath().toString(), opt);
            Log.d("fileUri",file.getPath().toString());
            Bitmap resized = Bitmap.createScaledBitmap(bmImage, width, height - 100, true);
//            Bitmap resized = Bitmap.createScaledBitmap(bmImage, this.canvas.getWidth(), this.canvas.getHeight(), true);
          /*  bmImage.setHeight(this.canvas.getHeight());
            bmImage.setWidth(this.canvas.getWidth());*/
            this.canvas.drawBitmap(resized);
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnUndo) {
            this.canvas.undo();   // Undo
        } else if (view.getId() == R.id.btnRedo) {
            this.canvas.redo();   // Redo
        } else if (view.getId() == R.id.btnClear) {
            this.canvas.clear();  // Clear canvas
            finish();
        } else if (view.getId() == R.id.btnDone) {
            if(etWidth.getText().length() > 0 && etHeight.getText().length() >0 )
            {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/RecceImages");
            myDir.mkdirs();
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String fname = "RacceImage_"+ n +".jpg";
            File file = new File (myDir, fname);
            if (file.exists ()) file.delete ();
            try {
                FileOutputStream out = new FileOutputStream(file);
                Rect rect=new Rect((int)startX/2,(int)startY/2,(int)controlX/2,(int)controlY/2);
                String imageText="W:"+etWidth.getText().toString()+" &H:"+etHeight.getText().toString()+"\n"+"&P:"+
                       getIntent().getExtras().getString("product_name").toString()+"&U:"+
                       getIntent().getExtras().getString("uomname").toString()/*+"Uom:"+getIntent().getExtras().getString("UomID").toString()*/;
               // String imageText = "srinivasu ";

                Bitmap afterEdit=drawTextToBitmap(CanvasEdit.this,imageText,this.canvas.getBitmap());
                afterEdit.compress(Bitmap.CompressFormat.JPEG, 70, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
                if (rect != null) {
                    Intent intent=new Intent();
                    intent.putExtra("imagePath",file.getAbsolutePath());
                    intent.putExtra("startX",startX);
                    intent.putExtra("startY",startY);
                    intent.putExtra("controlX",controlX);
                    intent.putExtra("controlY",controlY);
                    intent.putExtra("Width",etWidth.getText().toString());
                    intent.putExtra("Height",etHeight.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Please Mark the image ", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Please enter width and height", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.btnEdit) {

            this.canvas.setMode(CanvasView.Mode.TEXT);
        } else if (view.getId() == R.id.btnRect) {
            this.canvas.setMode(CanvasView.Mode.TEXT);

        } else if (view.getId() == R.id.btnEraser) {

            this.canvas.setMode(CanvasView.Mode.ERASER);
        }
    }
    private void initView() {
        canvas = (CanvasView) findViewById(R.id.canvas);
        btnUndo = (ImageButton) findViewById(R.id.btnUndo);
        btnUndo.setOnClickListener(CanvasEdit.this);
        btnRedo = (ImageButton) findViewById(R.id.btnRedo);
        btnRedo.setOnClickListener(CanvasEdit.this);
        btnClear = (ImageButton) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(CanvasEdit.this);
        btnDone = (ImageButton) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(CanvasEdit.this);
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(CanvasEdit.this);
        btnRect = (ImageButton) findViewById(R.id.btnRect);
        btnRect.setOnClickListener(CanvasEdit.this);
        btnEraser = (ImageButton) findViewById(R.id.btnEraser);
        btnEraser.setOnClickListener(CanvasEdit.this);
        llEdit = (LinearLayout) findViewById(R.id.llEdit);
        activityCanvasEdit = (RelativeLayout) findViewById(R.id.canvas_edit);
        etWidth = (EditText) findViewById(R.id.etWidth);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etHeight.setText(getIntent().getExtras().getString("height"));
        etWidth.setText(getIntent().getExtras().getString("width"));
        /*etHeight.setText("20");
        etWidth.setText("20");*/
    }
    float startX, startY, controlX, controlY;
    @Override
    public void onRectangleDrawn(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.controlX = endX;
        this.controlY = endY;


        rect = new Rect((int) startX, (int) startY/4, (int) endX, (int) endY/4);
        /*
        this.canvas.setMode(CanvasView.Mode.TEXT);
        this.canvas.setText("Width = "+rect.width());
        this.canvas.setText("Height = "+rect.height());
        this.canvas.setMode(CanvasView.Mode.DRAW);*/
    }
    public Bitmap drawTextToBitmap(Context mContext, String mText, Bitmap bitmap) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.RED);
            // text size in pixels
            paint.setTextSize((int)(12 * scale));
            paint.setTextAlign(Paint.Align.LEFT);
            // text shadow
            //  paint.setShadowLayer(1f, 0f, 1f, Color.RED);
            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) /2;
            int y = (bitmap.getHeight() + bounds.height()) /2;
            //change height
           // y = y - y / 2;
            canvas.drawText(mText, x*scale/8,y/8, paint);
            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
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
