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

package com.drakeet.rebase.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.drakeet.rebase.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 暂时仅适用于白色 tab layout，有优化空间
 *
 * @author drakeet
 */
public class CompactTabLayout extends TabLayout {

    private final int INDICATOR_HEIGHT;
    private int height;
    private int tabCount, lastTabCount;
    private float indicatorLeftOffset;
    private int selectedPosition;
    private Paint hoverPaint;
    private List<TextView> tabViews;
    private int[] tabWidths;


    public CompactTabLayout(Context context) {
        this(context, null);
    }


    public CompactTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public CompactTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        INDICATOR_HEIGHT = dpToPx(2);
        hoverPaint = new Paint();
        hoverPaint.setColor(Color.WHITE);
        hoverPaint.setStyle(Paint.Style.FILL);
        tabViews = new ArrayList<>(tabCount);
    }


    @Override public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new HoverOnPageChangeListener(this));
        }
        assert viewPager != null;
        lastTabCount = tabCount;
        tabCount = viewPager.getAdapter().getCount();
        tabWidths = new int[tabCount];
    }


    @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
    }


    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureTabWidths();
    }


    private void measureTabWidths() {
        for (int i = 0; i < tabCount; i++) {
            int tabWidth = ((LinearLayout) getChildAt(0)).getChildAt(i).getWidth();
            if (tabWidth == 0) {
                break;
            }
            tabWidths[i] = tabWidth;
        }
    }


    @Override protected void onFinishInflate() {
        super.onFinishInflate();
    }


    @Override protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (tabCount == 0) {
            return;
        }
        if (lastTabCount != tabCount) {
            measureTabWidths();
            lastTabCount = tabCount;
        }
        int offset = 0;
        for (int i = 0; i < selectedPosition; i++) {
            offset += tabWidths[i];
        }
        int tabWidth = tabWidths[selectedPosition];
        int indicatorWidth = tabWidth / 5;
        canvas.translate(offset, 0);    // 移动到第 N 个 tab 起点
        canvas.translate(indicatorLeftOffset * tabWidth, 0);    // 第 N 个 tab 当前滑动相对自身偏移量
        canvas.save();
        canvas.translate(0, height - INDICATOR_HEIGHT);     // 移动到底部下划线位置
        int halfHoverWidth = tabWidth / 2 - indicatorWidth / 2;
        canvas.drawRect(-2, 0, halfHoverWidth, INDICATOR_HEIGHT + 9, hoverPaint);
        canvas.translate((tabWidth / 2 + indicatorWidth / 2), 0);
        canvas.drawRect(0, 0, halfHoverWidth * 2, INDICATOR_HEIGHT + 9, hoverPaint);
        canvas.restore();
    }


    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        TextView textView = (TextView) View.inflate(getContext(), R.layout.tab_text, null);
        textView.setText(tab.getText());
        /* Selected first one by default */
        if (tabViews.isEmpty()) {
            renderSelectedText(textView);
        }
        tab.setCustomView(textView);
        tabViews.add(textView);
        super.addTab(tab, setSelected);
    }


    public class HoverOnPageChangeListener extends TabLayoutOnPageChangeListener {

        HoverOnPageChangeListener(TabLayout tabLayout) {
            super(tabLayout);
        }


        @Override public void onPageSelected(int position) {
            super.onPageSelected(position);
            for (int i = 0; i < tabCount; i++) {
                final TextView textView = tabViews.get(i);
                if (i == position) {
                    renderSelectedText(textView);
                } else {
                    renderNormalText(textView);
                }
            }
        }


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            ViewCompat.postInvalidateOnAnimation(CompactTabLayout.this);
            indicatorLeftOffset = positionOffset;
            selectedPosition = position;
        }
    }


    private void renderSelectedText(TextView textView) {
        // textView.setTextSize(14);
        textView.setTypeface(null, Typeface.BOLD);
    }


    private void renderNormalText(TextView textView) {
        // textView.setTextSize(14);
        textView.setTypeface(null, Typeface.NORMAL);
    }


    int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }
}
