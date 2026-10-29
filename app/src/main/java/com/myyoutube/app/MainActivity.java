
package com.myyoutube.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        openYouTube();
    }

    private void openYouTube() {

        Uri youtubeUrl = Uri.parse("https://m.youtube.com/");

        try {
            CustomTabsIntent customTabsIntent =
                new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build();

            customTabsIntent.launchUrl(this, youtubeUrl);

        } catch (Exception e) {

            Intent intent = new Intent(
                Intent.ACTION_VIEW,
                youtubeUrl
            );

            startActivity(intent);
        }

        finish();
    }
}
