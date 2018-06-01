package com.event2go.app.features.chat.presentation;

import android.databinding.BaseObservable;

import com.event2go.app.R;
import com.event2go.app.databinding.ListItemChatsBinding;
import com.event2go.app.features.chat.data.Chat;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class ChatRecyclerViewAdapter extends BaseRecyclerAdapter<Chat> {

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_chats;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item) {

        final ListItemChatsBinding binding = (ListItemChatsBinding) holder.binding;
        binding.setChat((Chat) item);

    }
}
