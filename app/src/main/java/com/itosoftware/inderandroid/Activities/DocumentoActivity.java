package com.itosoftware.inderandroid.Activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;

public class DocumentoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle b = getIntent().getExtras();
            String documento = b.getString("documento");

            WebView mWebView = new WebView(DocumentoActivity.this);
            mWebView.setVisibility(View.VISIBLE);

            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setAllowContentAccess(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.setScrollContainer(true);
            String ruta = "";
            ruta = documento;
            mWebView.loadUrl(ruta);
            setContentView(mWebView);
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(ruta));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Documento");
// You can change the name of the downloads, by changing "download" to everything you want, such as the mWebview title...
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);

            finish();
        } catch (Exception e) {
            Log.e("Error pdf", e.toString());
        }
    }

}
