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
import android.util.Log;
import android.widget.Toast;
import com.drakeet.rebase.R;
import com.drakeet.rebase.api.tool.Errors;
import java.net.UnknownHostException;
import retrofit2.adapter.rxjava.HttpException;
import rx.functions.Action1;

/**
 * @author drakeet
 */
public class ErrorHandlers {

    private static final String TAG = ErrorHandlers.class.getSimpleName();


    public static void displayError(Context context, String message) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    public static void displayError(Context context, Throwable throwable) {
        if (context == null) {
            Log.e(TAG, "[300] " + "Context is null");
            return;
        }

        String errorMessage = null;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            errorMessage = Errors.errorResponse(httpException).error;
        } else if (throwable instanceof UnknownHostException) {
            errorMessage = context.getString(R.string.tip_network_is_not_available);
        }
        if (errorMessage != null) {
            displayError(context, errorMessage);
        } else {
            Log.e(TAG, "[301]", throwable);
            displayError(context, Errors.errorMessage(throwable));
        }
    }


    public static Action1<Throwable> displayErrorAction(final Context context) {
        return new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                displayError(context, throwable);
            }
        };
    }
}
