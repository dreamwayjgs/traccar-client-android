package org.jaunt.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagingHelper {
    private static final String TAG = "Messaging Helper";

    public static void sendRegistrationToServer(String token, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String uniqueId = prefs.getString("id", "Unknown");
        Log.d(TAG, uniqueId + " / " + token);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.commit();

        Log.d(TAG, "저장했다" + prefs.getString("token", ""));

        String url = "https://hyudbprojectj.name/fcm/token";

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                ProviderInstaller.installIfNeeded(context);

                /**
                 * https://developer.android.com/training/articles/security-gms-provider.html
                 * this can take anywhere from 30-50 milliseconds (on more recent devices) to 350 ms (on older devices)
                 * keywords: installIfNeeded(), installIfNeededAsync()
                 *
                 * Once the Provider is updated, all calls to security APIs (including SSL APIs) are routed through it.
                 * (However, this does not apply to android.net.SSLCertificateSocketFactory,
                 * which remains vulnerable to such exploits as CVE-2014-0224.)
                 *
                 */
                SSLContext sslContext;
                sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(null, null, null);
                sslContext.createSSLEngine();

            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                URL theUrl = new URL(url);
                HttpsURLConnection con = (HttpsURLConnection) theUrl.openConnection();

                con.setDefaultUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestMethod("POST");

                // JSON style
//                String requestBody = String.format("{\"uniqueId\": \"%s\", \"token\": \"%s\"}"
//                        , uniqueId
//                        , token);

                String requestBody = String.format("uniqueId=%s&token=%s"
                        , uniqueId
                        , token);

                Log.i(TAG, requestBody);

                DataOutputStream wr = new DataOutputStream(
                        con.getOutputStream());
                wr.writeBytes(requestBody);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                Log.i(TAG, "HTTP 응답 코드 : " + responseCode);
                Log.i(TAG, "HTTP body : " + response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            RequestBody formBody = new FormBody.Builder()
                    .add("uniqueId", uniqueId)
                    .add("token", token)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
            OkHttpClient client = new OkHttpClient();
            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
