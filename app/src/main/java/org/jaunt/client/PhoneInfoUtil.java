package org.jaunt.client;

import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class PhoneInfoUtil {
    TelephonyManager telephonyManager;
    public String model;
    public String carrier;

    public PhoneInfoUtil(){
        this.model = getDeviceName();
        this.carrier = getTelecomName();
    }

    public String getTelecomName(){
        return telephonyManager.getNetworkOperatorName();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

}
