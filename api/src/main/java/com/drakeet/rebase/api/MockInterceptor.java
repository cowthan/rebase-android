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

package com.drakeet.rebase.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author drakeet
 */
class MockInterceptor implements Interceptor {

    static final Map<String, String> map = new HashMap<>();


    static {
        // map.put("feeds", Feed.getMockData());
    }


    @Override public Response intercept(Chain chain) throws IOException {
        HttpUrl url = chain.request().url();
        for (String path : map.keySet()) {
            if (url.toString().equals(RebaseRetrofit.ENDPOINT + path)) {
                return new Response.Builder()
                    .code(200)
                    .protocol(Protocol.HTTP_1_1)
                    .message("OK")
                    .body(ResponseBody.create(MediaType.parse("json"), map.get(path)))
                    .request(chain.request())
                    .build();
            }
        }
        return chain.proceed(chain.request());
    }
}
