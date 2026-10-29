package com.myyoutube.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

private WebView webView;
private FrameLayout rootLayout;
private View customView;
private WebChromeClient.CustomViewCallback customViewCallback;
private int oldOrientation;
private int oldSystemUiVisibility;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getWindow().setStatusBarColor(Color.BLACK);
    getWindow().setNavigationBarColor(Color.BLACK);

    rootLayout = new FrameLayout(this);
    webView = new WebView(this);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setDatabaseEnabled(true);
    settings.setLoadWithOverviewMode(true);
    settings.setUseWideViewPort(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setMediaPlaybackRequiresUserGesture(false);
    settings.setUserAgentString(
        "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
    );

    CookieManager cookieManager = CookieManager.getInstance();
    cookieManager.setAcceptCookie(true);
    cookieManager.setAcceptThirdPartyCookies(webView, true);

    webView.setBackgroundColor(Color.BLACK);
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, WebResourceRequest request) {
            return false;
        }
    });

    webView.setWebChromeClient(new WebChromeClient() {
        @Override
        public void onShowCustomView(
                View view, CustomViewCallback callback) {

            if (customView != null) {
                callback.onCustomViewHidden();
                return;
            }

            customView = view;
            customViewCallback = callback;

            oldOrientation = getRequestedOrientation();
            oldSystemUiVisibility =
                getWindow().getDecorView().getSystemUiVisibility();

            webView.setVisibility(View.GONE);

            rootLayout.addView(customView,
                new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));

            getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }
    });

    rootLayout.addView(webView, new FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT));

    setContentView(rootLayout);

    if (savedInstanceState != null) {
        webView.restoreState(savedInstanceState);
    } else {
        webView.loadUrl("https://www.youtube.com/");
    }
}

private void hideCustomView() {
    if (customView == null) return;

    rootLayout.removeView(customView);
    customView = null;

    webView.setVisibility(View.VISIBLE);

    getWindow().clearFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    getWindow().getDecorView().setSystemUiVisibility(
        oldSystemUiVisibility);

    setRequestedOrientation(oldOrientation);

    if (customViewCallback != null) {
        customViewCallback.onCustomViewHidden();
        customViewCallback = null;
    }
}

@Override
public void onBackPressed() {
    if (customView != null) {
        hideCustomView();
    } else if (webView != null && webView.canGoBack()) {
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
