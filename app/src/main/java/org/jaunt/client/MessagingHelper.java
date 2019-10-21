package org.jaunt.client;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessagingHelper {
    private static final String TAG = "Messaging Helper";

    public static void sendRegistrationToServer(String uniqueId, String token, @Nullable Context context) {
        Log.d(TAG, uniqueId + " / " + token);
        String url = "https://hyudbprojectj.name/register/fcm/token";

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
