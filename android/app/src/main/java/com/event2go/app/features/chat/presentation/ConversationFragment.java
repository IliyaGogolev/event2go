package com.event2go.app.features.chat.presentation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.event2go.base.presentation.fragment.BaseRecyclerFragment;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.R;
import com.event2go.app.databinding.IncludeComposeChatMessageBinding;
import com.event2go.app.features.chat.data.ChatMessage;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Iliya Gogolev on 3/18/16.
 */
public class ConversationFragment extends BaseRecyclerFragment implements ConversationViewModel.OnDataChangedListener {

    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // todo iliya base
//        setHasOptionsMenu(true);
//        ActionBar actionBar = getSupportedActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(getString(R.string.chat));
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final View headerView = inflateFooterLayout(R.layout.include_compose_chat_message);
        final IncludeComposeChatMessageBinding addressBannerBinding = DataBindingUtil.findBinding(headerView);
        addressBannerBinding.chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newMesage = addressBannerBinding.messageEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(newMesage)) {
                    addressBannerBinding.messageEdit.setText("");

                    ChatMessage msg = new ChatMessage();
                    msg.setDate(new Date());
                    msg.setMessage(newMesage);
                    msg.setIsMe(true);
                    mRecyclerViewModel.getAdapter().add(msg);
                    scrollToEndPosition();

                    // todo send to server, if failed remove from adapter
                }

            }
        });
    }

    @NonNull
    @Override
    protected BaseRecyclerViewModel getRecyclerViewModel() {
        ConversationViewModel viewModel = new ConversationViewModel();
        viewModel.setDataChangeListener(this);
        return viewModel;
    }


    @Override
    public void onStart() {
        super.onStart();

        if (mRecyclerViewModel != null) {
            ((ConversationViewModel) mRecyclerViewModel).setDataChangeListener(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        ((ConversationViewModel) mRecyclerViewModel).setDataChangeListener(null);
    }

    @Override
    protected void onAddItemDecoration(RecyclerView recyclerView) {
        // remove default divider
    }


    @Override
    public void onChanged() {
        scrollToEndPosition();
    }
}
