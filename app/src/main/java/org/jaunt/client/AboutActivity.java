/*
 * Copyright 2012 - 2017 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaunt.client;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        TextView title = findViewById(R.id.title);
        TextView phoneInfo = findViewById(R.id.phoneInfo);

        PhoneInfoUtil pu = new PhoneInfoUtil(getApplicationContext());

        try {
            title.setText(title.getText() + " Based on Traccar " + getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
            phoneInfo.setText(pu.model + "\n" + pu.carrier);
        } catch (NameNotFoundException e) {
            Log.w(AboutActivity.class.getSimpleName(), e);
        }
    }
}
