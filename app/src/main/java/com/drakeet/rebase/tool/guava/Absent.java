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

package com.drakeet.rebase.tool.guava;

import android.support.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

import static com.drakeet.rebase.tool.guava.Preconditions.checkNotNull;

/**
 * Implementation of an {@link Optional} not containing a reference.
 */
final class Absent<T> extends Optional<T> {
    static final Absent<Object> INSTANCE = new Absent<Object>();


    @SuppressWarnings("unchecked") // implementation is "fully variant"
    static <T> Optional<T> withType() {
        return (Optional<T>) INSTANCE;
    }


    private Absent() {}


    @Override
    public boolean isPresent() {
        return false;
    }


    @Override
    public T get() {
        throw new IllegalStateException("Optional.get() cannot be called on an absent value");
    }


    @Override
    public T or(T defaultValue) {
        return checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
    }


    @SuppressWarnings("unchecked") // safe covariant cast
    @Override
    public Optional<T> or(Optional<? extends T> secondChoice) {
        return (Optional<T>) checkNotNull(secondChoice);
    }


    @Override
    @Nullable
    public T orNull() {
        return null;
    }


    @Override
    public Set<T> asSet() {
        return Collections.emptySet();
    }


    @Override
    public boolean equals(@Nullable Object object) {
        return object == this;
    }


    @Override
    public int hashCode() {
        return 0x79a31aac;
    }


    @Override
    public String toString() {
        return "Optional.absent()";
    }


    private Object readResolve() {
        return INSTANCE;
    }


    private static final long serialVersionUID = 0;
}
