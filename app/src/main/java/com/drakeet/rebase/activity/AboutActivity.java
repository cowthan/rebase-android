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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.drakeet.rebase.BuildConfig;
import com.drakeet.rebase.R;
import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;
import me.drakeet.support.about.Line;

/**
 * @author drakeet
 */
public class AboutActivity extends AbsAboutActivity {

    private String share;


    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }


    @SuppressWarnings("deprecation") @SuppressLint("SetTextI18n")
    @Override protected void onCreateHeader(ImageView icon, TextView slogan, TextView version) {
        setHeaderContentColor(getResources().getColor(R.color.textColorPrimary));
        setNavigationIcon(R.drawable.ic_close_black_24dp);
        icon.setImageResource(R.drawable.ic_rebase_flat_w192);
        slogan.setText(R.string.slogan);
        version.setText("v" + BuildConfig.VERSION_NAME);
    }


    @SuppressWarnings("SpellCheckingInspection")
    @Override protected void onItemsCreated(@NonNull Items items) {
        share = getString(R.string.share);
        // @formatter:off

        items.add(new Category("Developers"));
        items.add(new Contributor(R.drawable.avatar_drakeet, "drakeet", "客户端开发、服务端开发和设计师"));
        items.add(new Line());

        items.add(new Category("关于日常..."));
        items.add(new Card(getString(R.string.card_app), share));
        items.add(new Line());

        items.add(new Category("关于 Rebase..."));
        items.add(new Card(getString(R.string.card_story), share));
        items.add(new Line());

        items.add(new Category("Open Source Licenses"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2, github("drakeet/MultiType")));
        items.add(new License("about-page", "drakeet", License.APACHE_2, github("drakeet/about-page")));
        items.add(new License("NumberProgressbar", "daimajia", License.MIT, github("daimajia/NumberProgressBar")));
        items.add(new License("RxJava", "RxJava Contributors", License.APACHE_2, github("ReactiveX/RxJava")));
        items.add(new License("retrofit", "Square Inc", License.APACHE_2, github("square/retrofit")));
        items.add(new License("okhttp", "Square Inc", License.APACHE_2, github("square/okhttp")));
        items.add(new License("gson", "Google Inc", License.APACHE_2, github("google/gson")));
        items.add(new License("android support libs", "AOSP", License.APACHE_2, "https://source.android.com/"));
        items.add(new License("butterknife", "Jake Wharton", License.APACHE_2, github("JakeWharton/butterknife")));
        items.add(new License("glide", "Sam Judd", "BSD, part MIT and Apache 2.0", github("bumptech/glide")));
        items.add(new License("RxAndroid", "RxAndroid authors", License.APACHE_2, github("ReactiveX/RxAndroid")));
        items.add(new License("objectify", "ionull", "All Rights Reserved", github("ionull/objectify")));
        items.add(new License("CircleImageView", "Henning Dodenhof", License.APACHE_2, github("hdodenhof/CircleImageView")));
        items.add(new License("prettytime", "ocpsoft", License.APACHE_2, github("ocpsoft/prettytime")));
        items.add(new License("RxLifecycle", "Trello", License.APACHE_2, github("trello/RxLifecycle")));
        items.add(new License("android-lite-orm", "litesuits", License.APACHE_2, github("litesuits/android-lite-orm")));

        // @formatter:on
    }


    @Override protected void onActionClick(View view) {
        TextView action = (TextView) view;
        if (action.getText().equals(share)) {
            onClickShare();
        }
    }


    public void onClickShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, share);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getTitle()));
    }


    @SuppressWarnings("SpellCheckingInspection")
    private static String github(String dir) {
        return "https://github.com/" + dir;
    }


    @Override public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
