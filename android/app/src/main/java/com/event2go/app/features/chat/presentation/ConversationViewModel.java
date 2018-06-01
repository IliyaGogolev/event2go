package com.event2go.app.features.chat.presentation;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.R;
import com.event2go.app.features.chat.data.repository.MessagesProvider;
import com.event2go.app.features.chat.data.ChatMessage;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 4/10/16.
 */
public class ConversationViewModel extends BaseRecyclerViewModel<ChatMessage> {

    public interface OnDataChangedListener {
        void onChanged();
    }

    private OnDataChangedListener mDataChangeListener;

    public ConversationViewModel() {
        super(new ChatAdapter());
    }

//    @Override
//    protected BaseRecyclerAdapter getRecyclerAdapter() {
//        return new ChatAdapter();
//    }
//
//    @Override
//    protected void refreshData(OnLoadCompleteCallback callback) {
//
//    }
//

    @Override
    public void load(final OnLoadCompleteCallback<ChatMessage> callback) {
        Observable<List<ChatMessage>> userCall = MessagesProvider.getInstance().getChatMessages();
        userCall.subscribe(new Consumer<List<ChatMessage>>() {

            @Override
            public void accept(List<ChatMessage> data) {
                callback.onSuccess(data);

                if (mDataChangeListener != null) {
                    mDataChangeListener.onChanged();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable t) {

                callback.onFailure(t);
            }
        });
    }

    @Override
    public int getEmptyListStringId() {
        return R.id.empty_text;
    }

    public void setDataChangeListener(OnDataChangedListener listener) {
        this.mDataChangeListener = listener;
    }
}
