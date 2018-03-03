package srinivas.sams.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import srinivas.sams.R;

public class AddRecceWeb extends Activity {

    protected WebView wvWebView;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_recce_web);
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        initView();
    }

        private void initView() {
        wvWebView = (WebView) findViewById(R.id.wvWebView);
        WebSettings webSettings = wvWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        wvWebView.getSettings().setJavaScriptEnabled(true);
        wvWebView.getSettings().setDomStorageEnabled(true);
        wvWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= 21) {
            wvWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().setAcceptThirdPartyCookies(wvWebView, true);
        }
        wvWebView.loadUrl(getIntent().getExtras().getString("url"));
        wvWebView.setWebViewClient(new WebViewClient(){

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        // TODO Auto-generated method stub
        super.onPageStarted(view, url, favicon);
        //M.showLoadingDialog(AddRecceWeb.this);

    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        view.loadUrl(url);
        return true;
    }
    public void onPageFinished(WebView view, String url) {

        view.getUrl();
        super.onPageFinished(view, url);
        //M.hideLoadingDialog();
    }
            @Override
            public void onReceivedHttpError(
                    WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                //Toast.makeText(getApplicationContext(),"httperror",Toast.LENGTH_SHORT).show();
              //  M.hideLoadingDialog();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                           SslError error) {
                // Toast.makeText(getApplicationContext(),"SslError",Toast.LENGTH_SHORT).show();
                //M.hideLoadingDialog();
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
                //Your code to do
                Toast.makeText(getApplicationContext(), "Your Internet Connection May not be active Or " + error , Toast.LENGTH_LONG).show();
                //M.hideLoadingDialog();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //db.execSQL("delete from install");
       /* db.execSQL("delete from recce");
        db.close();*/
        Intent it=new Intent(AddRecceWeb.this,Recces_display.class);
        it.putExtra("from","updaterecce");
        startActivity(it);
        finish();
    }

}
