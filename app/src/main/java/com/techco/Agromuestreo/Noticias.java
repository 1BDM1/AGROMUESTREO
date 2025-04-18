package com.techco.Agromuestreo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Noticias extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_noticias, container, false);

        setHasOptionsMenu(true);

        WebView mywebview = root.findViewById(R.id.webwiev);
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.loadUrl("https://brandondm124.wixsite.com/agromuestreo/general-8");
        mywebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mywebview.getSettings().setBlockNetworkImage(false);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.setHorizontalScrollBarEnabled(true);
        mywebview.getSettings().setLoadWithOverviewMode(true);

        mywebview.setFitsSystemWindows(true);



        return root;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_setting).setVisible(false);
        menu.findItem(R.id.action_calendar).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

}