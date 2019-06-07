package org.traccar.client;

import android.os.Build;
import android.os.Bundle;
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
        reportWebView.loadUrl("http://dbapp.hanyang.ac.kr/");

//        if (savedInstanceState == null) {
//            Toast.makeText(getApplicationContext(), "HELL World", Toast.LENGTH_LONG).show();
//        }
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
