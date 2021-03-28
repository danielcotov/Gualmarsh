package com.sc703.gualmarsh;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sc703.gualmarsh.R;

public class WebFragment  extends Fragment {
    WebView webView;

    String url = "https://www.walmart.com/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_web, container, false);
        webView = root.findViewById(R.id.webview);
        webView.setWebViewClient(new Cliente_Web());
        WebSettings configuracion= webView.getSettings();
        configuracion.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        return root;

    }
    private class Cliente_Web extends WebViewClient {
        public boolean URLCargado(WebView webView, String URL){
            webView.loadUrl(URL);
            return true;
        }
    }
}
