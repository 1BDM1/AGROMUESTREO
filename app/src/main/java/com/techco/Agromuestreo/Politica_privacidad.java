package com.techco.Agromuestreo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Politica_privacidad extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidad);


       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        WebView mywebview = findViewById(R.id.webwiev);
        mywebview.loadUrl("https://brandondm124.wixsite.com/agromuestreo");
        mywebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mywebview.getSettings().setBlockNetworkImage(false);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.setHorizontalScrollBarEnabled(true);
        mywebview.getSettings().setLoadWithOverviewMode(true);

    }
}