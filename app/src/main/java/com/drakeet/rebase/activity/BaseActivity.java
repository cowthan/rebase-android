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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.drakeet.rebase.R;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * @author drakeet
 */
public class BaseActivity extends RxAppCompatActivity {

    public static final int INSTANT_IN = 0;
    public static final int INSTANT_OUT = 0;

    ProgressDialog dialog;
    boolean doubleClickExit;
    boolean doubleClickToExitEnabled;


    @Override protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(INSTANT_IN, INSTANT_OUT);
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("请稍候...");
        dialog.setCanceledOnTouchOutside(false);
    }


    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


    public void showProgress(boolean show) {
        if (show) {
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }


    public void closeKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
            Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null) imm.showSoftInput(v, 0);
    }


    public void setDoubleClickToExitEnabled(boolean enabled) {
        this.doubleClickToExitEnabled = enabled;
    }


    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (doubleClickToExitEnabled) {
                attemptDoubleClickExit();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private Toast exitToast;


    void attemptDoubleClickExit() {
        if (doubleClickExit) {
            onBackPressed();
            exitToast.cancel();
        } else {
            doubleClickExit = true;
            exitToast = Toast.makeText(this, R.string.double_click_to_exit, Toast.LENGTH_SHORT);
            exitToast.show();
            new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    doubleClickExit = false;
                }
            }, 1200);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override public void finish() {
        super.finish();
        overridePendingTransition(INSTANT_IN, INSTANT_OUT);
    }
}
