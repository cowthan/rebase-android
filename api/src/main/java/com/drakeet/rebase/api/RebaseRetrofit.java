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

import com.drakeet.rebase.api.tool.ResponseTypeAdapterFactory;
import com.drakeet.rebase.api.type.Auth;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Collections;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * @author drakeet
 */
@SuppressWarnings("WeakerAccess")
public class RebaseRetrofit {

    final Rebase service;

    public static boolean debug = true;
    private static Auth auth = null;

    public static final String ENDPOINT = "https://api.drakeet.com/rebase/";
    public static final Gson GSON = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .registerTypeAdapterFactory(new ResponseTypeAdapterFactory())
        .create();


    RebaseRetrofit() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .connectionSpecs(Collections.singletonList(ConnectionSpec.MODERN_TLS))
            .certificatePinner(new CertificatePinner.Builder()
                .add("api.drakeet.com", "sha256/9aQHJArF0zbDubXlykRKFeuSxbezVPkqiT7Cgg/HX6s=")
                .build());

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder builder = original.newBuilder();
                if (auth != null) {
                    builder.addHeader("Authorization", "token " + auth.accessToken);
                }
                return chain.proceed(builder.build());
            }
        });
        if (debug) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(logging);
            clientBuilder.addInterceptor(new MockInterceptor());
        }
        OkHttpClient client = clientBuilder.build();

        Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(HttpUrl.parse(ENDPOINT))
                .addConverterFactory(GsonConverterFactory.create(GSON))
                .addCallAdapterFactory(
                    RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client);

        Retrofit retrofit = builder.build();

        service = retrofit.create(Rebase.class);
    }


    public static void setAuth(Auth auth) {
        RebaseRetrofit.auth = auth;
    }


    Rebase getService() { return service; }
}
