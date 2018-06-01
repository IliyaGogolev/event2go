package com.event2go.app.features.user.presentation;

import android.databinding.BaseObservable;

import com.event2go.app.R;
import com.event2go.app.databinding.ListItemUserBinding;
import com.event2go.app.features.user.data.User;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 6/25/15.
 */
public class UsersRecyclerViewAdapter extends BaseRecyclerAdapter<User> {

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_user;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item) {

        final ListItemUserBinding binding = (ListItemUserBinding) holder.binding;
        binding.setUser((User) item);

    }
}
