
package com.myyoutube.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.FrameLayout;

public class MainActivity extends Activity {

private WebView webView;
private View fullscreenView;
private CustomViewCallback fullscreenCallback;
private FrameLayout rootLayout;
private int originalOrientation;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    originalOrientation = getRequestedOrientation();

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
    settings.setMediaPlaybackRequiresUserGesture(false);

    CookieManager.getInstance().setAcceptCookie(true);
    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

    webView.setBackgroundColor(Color.BLACK);
    webView.setWebViewClient(new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view,
                WebResourceRequest request) {
            return false;
        }
    });

    webView.setWebChromeClient(new WebChromeClient() {
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if (fullscreenView != null) {
                callback.onCustomViewHidden();
                return;
            }

            fullscreenView = view;
            fullscreenCallback = callback;

            webView.setVisibility(View.GONE);
            rootLayout.addView(fullscreenView,
                    new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT));

            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        @Override
        public void onHideCustomView() {
            exitFullscreen();
        }
    });

    rootLayout.addView(webView, new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT));

    setContentView(rootLayout);

    if (savedInstanceState != null) {
        webView.restoreState(savedInstanceState);
    } else {
        webView.loadUrl("https://m.youtube.com/");
    }
}

private void exitFullscreen() {
    if (fullscreenView == null) {
        return;
    }

    rootLayout.removeView(fullscreenView);
    fullscreenView = null;

    webView.setVisibility(View.VISIBLE);

    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    if (fullscreenCallback != null) {
        fullscreenCallback.onCustomViewHidden();
        fullscreenCallback = null;
    }
}

@Override
public void onBackPressed() {
    if (fullscreenView != null) {
        exitFullscreen();
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
