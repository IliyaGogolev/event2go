package com.event2go.app.features.chat.presentation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.event2go.base.presentation.fragment.BaseRecyclerFragment;
import com.event2go.app.features.chat.data.Chat;
import com.event2go.app.utils.NavUtils;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;

/**
 * Created by Iliya Gogolev on 9/10/15.
 */
public class ChatsRecyclerFragment extends BaseRecyclerFragment<Chat> {

    private ChatViewModel mChatViewModel;

    @NonNull
    @Override
    protected BaseRecyclerViewModel<Chat> getRecyclerViewModel() {
        mChatViewModel = new ChatViewModel();
        return mChatViewModel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSwipeRefreshEnabled(true);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);

        Chat chat = getAdapter().getItem(position);
        NavUtils.showOpenChatActivity(getContext(), null);
    }
}
