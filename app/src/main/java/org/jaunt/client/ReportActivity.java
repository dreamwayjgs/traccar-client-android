package org.jaunt.client;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.*;

public class ReportActivity extends AppCompatActivity {

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c){
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast){
            makeText(mContext, toast, LENGTH_SHORT).show();
        }
    }

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
        reportWebView.addJavascriptInterface(new WebAppInterface(this), "Android");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.i("devicePref", preferences.getAll().toString());

        reportWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        reportWebView.loadUrl("https://hyudbprojectj.name/traccar/device/"+preferences.getString("id", "Unknown"));

        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mNotifyManager.deleteNotificationChannel("Report");
        }


    }

    @Override
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
