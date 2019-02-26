package me.mi.milab.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

import me.mi.milab.R;

public class WebviewActivity extends Activity {
    private WebView mWebView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }
    private void init() {
    }
}
