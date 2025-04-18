package com.techco.Agromuestreo.ui;

import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.techco.Agromuestreo.R;

public class top_mejores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_noticias);

            WebView mywebview = findViewById(R.id.webwiev);
            mywebview.setWebViewClient(new WebViewClient());
            mywebview.loadUrl("https://brandondm124.wixsite.com/agromuestreo/s-projects-basic#t%C3%ADtulo");
            mywebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            mywebview.getSettings().setBlockNetworkImage(false);
            mywebview.getSettings().setJavaScriptEnabled(true);
            mywebview.setHorizontalScrollBarEnabled(true);
            mywebview.getSettings().setLoadWithOverviewMode(true);
            mywebview.setFitsSystemWindows(true);

        }



    }


