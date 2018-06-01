package com.event2go.app.features.chat.presentation;

import android.databinding.BaseObservable;

import com.event2go.app.R;
import com.event2go.app.databinding.ListItemChatMessageBinding;
import com.event2go.app.features.chat.data.ChatMessage;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class OneOnOneChatRecyclerViewAdapter extends BaseRecyclerAdapter<ChatMessage> {

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_chat_message;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item) {

        final ListItemChatMessageBinding binding = (ListItemChatMessageBinding) holder.binding;
        binding.setMessage((ChatMessage) item);

    }
}
