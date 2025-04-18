package com.techco.Agromuestreo.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.techco.Agromuestreo.ExpLVAdapter;
import com.techco.Agromuestreo.R;
import com.techco.Agromuestreo.databinding.FragmentSlideshowBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {
    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;

    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> ListaPreguntas;
    private Map<String, ArrayList<String>> mapChild;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        setHasOptionsMenu(true);


        View root =  inflater.inflate(R.layout.fragment_noticias, container, false);

        setHasOptionsMenu(true);

        WebView mywebview = root.findViewById(R.id.webwiev);
        mywebview.setWebViewClient(new WebViewClient());
        mywebview.loadUrl("https://brandondm124.wixsite.com/agromuestreo/noticia-2");
        mywebview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mywebview.getSettings().setBlockNetworkImage(false);
        mywebview.getSettings().setJavaScriptEnabled(true);
        mywebview.setHorizontalScrollBarEnabled(true);
        mywebview.getSettings().setLoadWithOverviewMode(true);

        mywebview.setFitsSystemWindows(true);



        return root;
    }
}