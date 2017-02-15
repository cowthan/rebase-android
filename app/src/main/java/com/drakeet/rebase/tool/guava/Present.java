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
 * Implementation of an {@link Optional} containing a reference.
 */

final class Present<T> extends Optional<T> {
    private final T reference;


    Present(T reference) {
        this.reference = reference;
    }


    @Override
    public boolean isPresent() {
        return true;
    }


    @Override
    public T get() {
        return reference;
    }


    @Override
    public T or(T defaultValue) {
        checkNotNull(defaultValue, "use Optional.orNull() instead of Optional.or(null)");
        return reference;
    }


    @Override
    public Optional<T> or(Optional<? extends T> secondChoice) {
        checkNotNull(secondChoice);
        return this;
    }


    @Override
    public T orNull() {
        return reference;
    }


    @Override
    public Set<T> asSet() {
        return Collections.singleton(reference);
    }


    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Present) {
            Present<?> other = (Present<?>) object;
            return reference.equals(other.reference);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return 0x598df91c + reference.hashCode();
    }


    @Override
    public String toString() {
        return "Optional.of(" + reference + ")";
    }


    private static final long serialVersionUID = 0;
}
