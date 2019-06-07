package org.traccar.client;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {

    WebView reportWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report);
        reportWebView = (WebView) findViewById(R.id.report_view);
        WebSettings webSettings = reportWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        reportWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        reportWebView.loadUrl("http://dbapp.hanyang.ac.kr/report");

//        if (savedInstanceState == null) {
//            Toast.makeText(getApplicationContext(), "HELL World", Toast.LENGTH_LONG).show();
//        }
    }

    protected void onResume(){
        super.onResume();
        Log.d("Webview", "클리어");
        reportWebView.clearCache(true);
        reportWebView.clearHistory();
        reportWebView.clearFormData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (reportWebView.canGoBack()) {
                reportWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
