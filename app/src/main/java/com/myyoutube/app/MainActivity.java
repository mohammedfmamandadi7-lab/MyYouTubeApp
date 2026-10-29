package com.myyoutube.app;

import android.app.Activity;
import android.os.Bundle;
import android.net.Uri;
import androidx.browser.customtabs.CustomTabsIntent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build();

        intent.launchUrl(
                this,
                Uri.parse("https://m.youtube.com/")
        );

        finish();
    }
}
