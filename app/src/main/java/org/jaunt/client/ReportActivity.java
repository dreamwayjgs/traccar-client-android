package org.jaunt.client;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    WebView reportWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report);
        reportWebView = (WebView) findViewById(R.id.report);

        WebSettings webSettings = reportWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setGeolocationEnabled(true);
//        reportWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.i("devicePref", preferences.getAll().toString());

        reportWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        Log.i("Report URL", "https://hyudbprojectj.name/device/" + preferences.getString("id", "Unknown"));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", preferences.getString("token", ""));
        reportWebView.loadUrl("https://hyudbprojectj.name/device/" + preferences.getString("id", "Unknown")
                , headers);

        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotifyManager.deleteNotificationChannel("Report");
        }
    }

    @Override
    protected void onResume() {
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

