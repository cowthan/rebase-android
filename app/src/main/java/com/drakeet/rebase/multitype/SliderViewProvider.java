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

package com.drakeet.rebase.multitype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.drakeet.rebase.R;
import com.drakeet.rebase.api.type.Feed;
import com.drakeet.rebase.api.type.Slider;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import java.util.List;
import me.drakeet.multitype.ItemViewProvider;

/**
 * @author drakeet
 * @deprecated
 */
public class SliderViewProvider
    extends ItemViewProvider<Slider, SliderViewProvider.SliderViewHolder> {

    @NonNull @Override
    protected SliderViewHolder onCreateViewHolder(
        @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(root);
    }


    @Override
    protected void onBindViewHolder(
        @NonNull SliderViewHolder holder, @NonNull Slider slider) {
        holder.setFeeds(slider.feeds);
        // TODO: 2016/11/29 record scroll position
    }


    static class SliderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.slider) RecyclerViewPager slider;

        SliderItemAdapter adapter;


        SliderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            LinearLayoutManager manager = new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
            slider.setLayoutManager(manager);
            adapter = new SliderItemAdapter();
            slider.setAdapter(adapter);
            slider.scrollToPosition(1);
        }


        void setFeeds(List<Feed> feeds) {
            adapter.setFeeds(feeds);
            adapter.notifyDataSetChanged();
        }
    }


    static class SliderItemAdapter extends RecyclerView.Adapter<SliderItemAdapter.FeedViewHolder> {

        @Nullable List<Feed> feeds;


        @Override
        public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View root = inflater.inflate(R.layout.item_slider_feed, parent, false);
            return new FeedViewHolder(root);
        }


        @Override
        public void onBindViewHolder(FeedViewHolder holder, int position) {
            // TODO: 2016/11/29
        }


        @Override public int getItemCount() {
            return feeds == null ? 0 : feeds.size();
        }


        void setFeeds(@Nullable List<Feed> feeds) {
            this.feeds = feeds;
        }


        static class FeedViewHolder extends RecyclerView.ViewHolder {
            FeedViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}