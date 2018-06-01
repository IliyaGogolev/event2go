package com.event2go.app.features.notifications.repository;

import android.databinding.BaseObservable;

import com.event2go.app.R;
import com.event2go.app.databinding.IncludeListItemNotificationBinding;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 6/25/15.
 */
public class AppNotificationsRecyclerViewAdapter extends BaseRecyclerAdapter<AppNotification> {

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.include_list_item_notification;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, final BaseObservable item) {

        final IncludeListItemNotificationBinding binding = (IncludeListItemNotificationBinding) holder.binding;
        binding.setNotification((AppNotification) item);
        binding.executePendingBindings();
    }
}
