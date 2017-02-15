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

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author drakeet
 */
public class ObservableWebView extends WebView {

    private OnScrollChangedListener onScrollChangedListener;


    public ObservableWebView(Context context) {
        super(context);
    }


    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ObservableWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }


    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }


    public OnScrollChangedListener getOnScrollChangedListener() {
        return onScrollChangedListener;
    }


    public interface OnScrollChangedListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v The view whose scroll position has changed.
         * @param x Current horizontal scroll origin.
         * @param y Current vertical scroll origin.
         * @param oldX Previous horizontal scroll origin.
         * @param oldY Previous vertical scroll origin.
         */
        void onScrollChanged(WebView v, int x, int y, int oldX, int oldY);
    }
}
