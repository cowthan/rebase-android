/*
 * Copyright (C) 2017 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of rebase-android
 *
 * rebase-android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * rebase-android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rebase-android. If not, see <http://www.gnu.org/licenses/>.
 */

package com.drakeet.rebase.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.liuguangqiang.swipeback.SwipeBackLayout.DragEdge;
import java.util.HashMap;
import java.util.Map;

public class WebActivity extends ToolbarActivity
    implements ObservableWebView.OnScrollChangedListener {

    private static final String TAG = WebActivity.class.getSimpleName();

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";

    private static final Map<String, Integer> URL_POSITION_CACHES = new HashMap<>();

    NumberProgressBar progressbar;
    ObservableWebView webView;
    TextSwitcher textSwitcher;

    String url, title;
    int positionHolder;
    boolean overrideTitleEnabled = true;


    /**
     * Using newIntent trick, return WebActivity Intent, to avoid `public static`
     * constant
     * variable everywhere
     *
     * @return Intent to start WebActivity
     */
    public static Intent newIntent(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, extraURL);
        intent.putExtra(EXTRA_TITLE, extraTitle);
        return intent;
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        SwipeBackDelegate swipeBackDelegate = new SwipeBackDelegate();
        swipeBackDelegate.attach(this, R.layout.web_activity_web);
        swipeBackDelegate.setDragEdge(DragEdge.VERTICAL);
        /* setContentView(R.layout.web_activity_web); */

        progressbar = (NumberProgressBar) findViewById(R.id.progressbar);
        webView = (ObservableWebView) findViewById(R.id.web_view);
        textSwitcher = (TextSwitcher) findViewById(R.id.title);

        url = getIntent().getStringExtra(EXTRA_URL);
        title = getIntent().getStringExtra(EXTRA_TITLE);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        webView.setWebChromeClient(new ChromeClient());
        webView.setWebViewClient(new ReloadableClient());
        webView.setOnScrollChangedListener(this);

        webView.loadUrl(url);

        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @SuppressWarnings("deprecation") @Override public View makeView() {
                final Context context = WebActivity.this;
                final TextView textView = new TextView(context);
                textView.setTextAppearance(context, R.style.WebTitle);
                textView.setSingleLine(true);
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.postDelayed(new Runnable() {
                    @Override public void run() {
                        textView.setSelected(true);
                    }
                }, 1738);
                return textView;
            }
        });
        textSwitcher.setInAnimation(this, android.R.anim.fade_in);
        textSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if (title != null) setTitle(title);
    }


    public void setOverrideTitleEnabled(boolean enabled) {
        this.overrideTitleEnabled = enabled;
    }


    @Override public void setTitle(CharSequence title) {
        super.setTitle(title);
        textSwitcher.setText(title);
    }


    private void refresh() {
        webView.reload();
    }


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu_web, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        } else if (id == R.id.action_copy_url) {
            ClipBoards.copyToClipBoard(this, webView.getUrl());
            Toast.makeText(this, R.string.web_tip_copy_done, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_open_url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.web_tip_open_fail, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("deprecation")
    @Override protected void onDestroy() {
        final String url = webView.getUrl();
        int bottom = (int) Math.floor(webView.getContentHeight() * webView.getScale() * 0.8f);
        if (positionHolder >= bottom) {
            URL_POSITION_CACHES.remove(url);
        } else {
            URL_POSITION_CACHES.put(url, positionHolder);
        }
        super.onDestroy();
        if (webView != null) webView.destroy();
    }


    @Override public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }


    @Override public void onScrollChanged(WebView v, int x, int y, int oldX, int oldY) {
        positionHolder = y;
    }


    private class ChromeClient extends WebChromeClient {

        @Override public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressbar.setProgress(newProgress);
            if (newProgress == 100) {
                progressbar.setVisibility(View.INVISIBLE);
            } else {
                progressbar.setVisibility(View.VISIBLE);
            }
        }


        @Override public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (overrideTitleEnabled) {
                setTitle(title);
            }
        }
    }


    private class ReloadableClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }


        @Override public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            Integer _position = URL_POSITION_CACHES.get(url);
            int position = _position == null ? 0 : _position;
            view.scrollTo(0, position);
        }
    }
}
