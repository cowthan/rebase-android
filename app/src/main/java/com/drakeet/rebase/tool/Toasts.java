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

package com.drakeet.rebase.tool;

import android.annotation.SuppressLint;
import android.app.Application;
import android.widget.Toast;

/**
 * @author drakeet
 */
public class Toasts {

    @SuppressLint("StaticFieldLeak")
    private static Application application;


    public static void install(Application application) {
        Toasts.application = application;
    }


    public static void showShort(String text) {
        Toast.makeText(application, text, Toast.LENGTH_SHORT).show();
    }


    public static void showLong(String text) {
        Toast.makeText(application, text, Toast.LENGTH_LONG).show();
    }
}
