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

import java.util.Date;
import org.ocpsoft.prettytime.PrettyTime;

/**
 * @author drakeet
 */
public class TimeDesc {

    private TimeDesc() {}


    private static PrettyTime prettyTime = new PrettyTime();


    public static String format(final Date date) {
        return prettyTime.format(date).replace(" 前", "前").replace(" 后", "后");
    }
}
