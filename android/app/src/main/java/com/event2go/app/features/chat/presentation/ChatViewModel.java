package com.event2go.app.features.chat.presentation;

import android.support.annotation.NonNull;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.R;
import com.event2go.app.features.chat.data.repository.MessagesProvider;
import com.event2go.app.features.chat.data.Chat;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class ChatViewModel extends BaseRecyclerViewModel<Chat> {

    public ChatViewModel() {
        super(new ChatRecyclerViewAdapter());
    }

    @Override
    public void load(@NonNull final OnLoadCompleteCallback<Chat> callback) {
        super.load(callback);

        Observable<List<Chat>> userCall = MessagesProvider.getInstance().getChats();
        userCall.subscribe(new Consumer<List<Chat>>() {

            @Override
            public void accept(List<Chat> data) {
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
        return R.string.empty;
    }
}
