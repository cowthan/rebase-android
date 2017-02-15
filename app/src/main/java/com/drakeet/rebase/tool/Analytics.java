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

import android.content.Context;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author drakeet
 */
public class Analytics {

    public static class Umeng {

        private final Context context;


        private Umeng(Context context) {this.context = context;}


        public void logEvent(String event, String key, String value) {
            Map<String, String> map = new HashMap<>();
            map.put(key, value);
            logEvent(event, map);
        }


        public void logEvent(String event, Map<String, String> map) {
            MobclickAgent.onEvent(context, event, map);
        }


        public void logEvent(String event) {
            MobclickAgent.onEvent(context, event);
        }
    }


    public static Umeng of(Context context) {
        return new Umeng(context);
    }
}
