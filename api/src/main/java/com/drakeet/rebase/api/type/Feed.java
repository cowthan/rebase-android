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

package com.drakeet.rebase.api.type;

import com.litesuits.orm.db.annotation.PrimaryKey;
import java.util.Date;

import static com.litesuits.orm.db.enums.AssignType.BY_MYSELF;

/**
 * @author drakeet
 */
public class Feed {

    public static final String PUBLISHED_AT = "publishedAt";
    public static final String CATEGORY = "category";

    @PrimaryKey(BY_MYSELF)
    public String _id;
    public String url;
    public String title;
    public String content;
    public String category;
    public String owner;
    public String coverUrl;
    public Date createdAt;
    public Date updatedAt;
    public Date publishedAt;
}
