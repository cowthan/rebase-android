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

package com.drakeet.rebase.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.drakeet.rebase.R;
import com.drakeet.rebase.tool.LoadMoreDelegate;
import com.drakeet.rebase.tool.SwipeRefreshDelegate;
import java.util.concurrent.atomic.AtomicInteger;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * @author drakeet
 */
public abstract class ListBaseFragment extends BaseFragment
    implements SwipeRefreshDelegate.OnSwipeRefreshListener, LoadMoreDelegate.LoadMoreSubject {

    private static final String TAG = ListBaseFragment.class.getSimpleName();

    @BindView(android.R.id.list) RecyclerView recyclerView;

    private SwipeRefreshDelegate refreshDelegate;
    private LoadMoreDelegate loadMoreDelegate;

    private AtomicInteger loadingCount;
    private boolean isEnd;

    protected MultiTypeAdapter adapter;
    protected Items items;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        refreshDelegate = new SwipeRefreshDelegate(this);
        loadMoreDelegate = new LoadMoreDelegate(this);
        loadingCount = new AtomicInteger(0);
    }


    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, root);
        recyclerView.setAdapter(adapter);
        loadMoreDelegate.attach(recyclerView);
        refreshDelegate.attach(root);
        loadData(true);
        return root;
    }


    protected abstract void loadData(boolean clear);


    protected boolean onInterceptLoadMore() {
        return false;
    }


    protected void setRefresh(boolean refresh) {
        refreshDelegate.setRefresh(refresh);
    }


    @Override public void onSwipeRefresh() {
        loadData(true);
    }


    @Override public final void onLoadMore() {
        if (!isEnd()) {
            Log.d(TAG, "[3801]");
            if (!onInterceptLoadMore()) {
                loadData(false);
            }
        }
    }


    protected boolean isShowingRefresh() {
        return refreshDelegate.isShowingRefresh();
    }


    public boolean isRequestDataRefresh() {
        return refreshDelegate.isRequestDataRefresh();
    }


    public void setEnd(boolean end) {
        isEnd = end;
    }


    public boolean isEnd() {
        return isEnd;
    }


    protected void setSwipeToRefreshEnabled(boolean enable) {
        refreshDelegate.setEnabled(enable);
    }


    public void smoothScrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }


    @Override public boolean isLoading() {
        return loadingCount.get() > 0;
    }


    protected void notifyLoadingStarted() {
        loadingCount.getAndIncrement();
    }


    protected void notifyLoadingFinished() {
        loadingCount.decrementAndGet();
    }
}
