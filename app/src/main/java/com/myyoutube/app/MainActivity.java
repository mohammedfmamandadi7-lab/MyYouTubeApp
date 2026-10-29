package com.myyoutube.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.graphics.Color;
import android.view.KeyEvent;

public class MainActivity extends Activity {

private WebView webView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setStatusBarColor(Color.BLACK);
    getWindow().setNavigationBarColor(Color.BLACK);

    webView = new WebView(this);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setDatabaseEnabled(true);
    settings.setLoadWithOverviewMode(true);
    settings.setUseWideViewPort(true);
    settings.setMediaPlaybackRequiresUserGesture(false);

    CookieManager.getInstance().setAcceptCookie(true);
    CookieManager.getInstance()
            .setAcceptThirdPartyCookies(webView, true);

    webView.setBackgroundColor(Color.BLACK);
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view,
                WebResourceRequest request) {
            return false;
        }
    });

    webView.setWebChromeClient(new WebChromeClient());
    setContentView(webView);

    if (savedInstanceState != null) {
        webView.restoreState(savedInstanceState);
    } else {
        webView.loadUrl("https://m.youtube.com/");
    }
}

@Override
public void onBackPressed() {
    if (webView != null && webView.canGoBack()) {
        webView.goBack();
    } else {
        super.onBackPressed();
    }
}

@Override
protected void onSaveInstanceState(Bundle outState) {
    if (webView != null) {
        webView.saveState(outState);
    }
    super.onSaveInstanceState(outState);
}

@Override
protected void onDestroy() {
    if (webView != null) {
        webView.destroy();
        webView = null;
    }
    super.onDestroy();
}

}
