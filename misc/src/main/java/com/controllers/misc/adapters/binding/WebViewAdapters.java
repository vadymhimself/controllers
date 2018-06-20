package com.controllers.misc.adapters.binding;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Artem Sisetskyi on 6/20/18.
 * AppDevelopmentShop
 * sisetskyi.a@gmail.com
 */
public abstract class WebViewAdapters {

    @BindingAdapter(value = {"url", "webClient"})
    public static void _bindUrl(WebView webView, String uri, WebViewClient webViewClient) {
        if (webViewClient == null) {
            webView.setWebViewClient(new WebViewClient());
        } else {
            webView.setWebViewClient(webViewClient);
        }
        webView.loadUrl(uri);
    }

    @BindingAdapter("html")
    public static void _bindHtml(WebView wv, String html) {
        wv.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @BindingAdapter("javascriptEnabled")
    public static void _bindJavascriptEnabled(WebView wv, Boolean enabled) {
        wv.getSettings().setJavaScriptEnabled(enabled);
        wv.getSettings().setLoadWithOverviewMode(enabled);
        wv.getSettings().setUseWideViewPort(enabled);
    }
}
