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

package com.drakeet.rebase.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.drakeet.rebase.R;
import com.drakeet.rebase.api.type.Feed;
import com.drakeet.rebase.web.WebActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author drakeet
 */
public class FeedBrowserActivity extends WebActivity {

    private static final String EXTRA_FEED_ID = "extra_feed_id";
    private static final String EXTRA_FEED_TITLE = "extra_feed_title";
    private boolean initialCheckState = true;
    private String feedId, feedTitle;


    public static Intent newIntent(Context context, Feed feed) {
        Intent intent = WebActivity.newIntent(context, feed.url, feed.title);
        intent.setClass(context, FeedBrowserActivity.class);
        intent.putExtra(EXTRA_FEED_ID, feed._id);
        intent.putExtra(EXTRA_FEED_TITLE, feed.title);
        return intent;
    }


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        feedId = getIntent().getStringExtra(EXTRA_FEED_ID);
        feedTitle = getIntent().getStringExtra(EXTRA_FEED_TITLE);
    }


    @Override protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    @Override protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_browser, menu);
        MenuItem item = menu.findItem(R.id.action_favorite);
        initFavoriteItemState(item);
        return super.onCreateOptionsMenu(menu);
    }


    private void initFavoriteItemState(MenuItem item) {
        item.setChecked(initialCheckState);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_favorite:
                // removed
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
