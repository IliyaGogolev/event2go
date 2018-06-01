package com.event2go.app.features.chat.presentation;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.R;
import com.event2go.app.features.chat.data.repository.MessagesProvider;
import com.event2go.app.features.chat.data.ChatMessage;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class OneOnOneChatViewModel extends BaseRecyclerViewModel<ChatMessage> {


    public OneOnOneChatViewModel() {
        super(new OneOnOneChatRecyclerViewAdapter());
    }

    @Override
    public void load(final OnLoadCompleteCallback<ChatMessage> callback) {
        Observable<List<ChatMessage>> userCall = MessagesProvider.getInstance().getChatMessages();
        userCall.subscribe(new Consumer<List<ChatMessage>>() {

            @Override
            public void accept(List<ChatMessage> data) {
                callback.onSuccess(data);
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
}
