package com.event2go.app.features.user.presentation;

import android.databinding.BaseObservable;

import com.event2go.app.R;
import com.event2go.app.databinding.ListItemContactBinding;
import com.event2go.app.features.chat.data.Contact;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class ContactRecyclerViewAdapter extends BaseRecyclerAdapter<Contact> {

    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_contact;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item) {

        final ListItemContactBinding binding = (ListItemContactBinding) holder.binding;
        binding.setContact((Contact) item);

    }
}
