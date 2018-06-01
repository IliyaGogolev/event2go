package com.event2go.app.features.event.presentation;

import android.databinding.BaseObservable;

import com.event2go.app.R;
import com.event2go.app.databinding.ListItemEventUserBinding;
import com.event2go.app.features.user.data.User;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 6/29/15.
 */
public class EventUsersRecycleViewAdapter extends BaseRecyclerAdapter<User> {

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_event_user;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item) {
        User user = (User) item;

        ListItemEventUserBinding binding = (ListItemEventUserBinding) holder.binding;
        binding.setUser(user);
    }
}
