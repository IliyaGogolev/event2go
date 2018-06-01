package com.event2go.app.features.event.presentation;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.event2go.app.utils.OnCallbacListener;
import com.event2go.app.features.event.data.repository.AttendeeDataProvider;
import com.event2go.app.databinding.IncludeQuestionBarBinding;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.PartStatType;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 12/3/15.
 */
public class PartStatQuestionBarUtils {

    public static void addClickListeners(final IncludeQuestionBarBinding includeQuestionBar, final Event event, final OnCallbacListener listener) {

        // TODO demo, crashing right now
        if (event == null) return;

        includeQuestionBar.buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                enableBUttons(includeQuestionBar, false);
                Observable observable = AttendeeDataProvider.saveParticipationStatus(v.getContext(), event, PartStatType.ACCEPTED);
                onParticipationStatusSaved(observable, v, includeQuestionBar, listener);
            }
        });
        includeQuestionBar.buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBUttons(includeQuestionBar, false);
                Observable observable = AttendeeDataProvider.saveParticipationStatus(v.getContext(), event, PartStatType.DECLINED);
                onParticipationStatusSaved(observable, v, includeQuestionBar, listener);
            }
        });
        includeQuestionBar.buttonMaybe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableBUttons(includeQuestionBar, false);
                Observable observable = AttendeeDataProvider.saveParticipationStatus(v.getContext(), event, PartStatType.TENTATIVE);
                onParticipationStatusSaved(observable, v, includeQuestionBar, listener);
            }
        });
    }

    private static void onParticipationStatusSaved(Observable observable, final View v, final IncludeQuestionBarBinding includeQuestionBar,
                                                   final OnCallbacListener listener) {
        observable.subscribe(new Consumer() {
            @Override
            public void accept(Object object) {
                enableBUttons(includeQuestionBar, true);
                listener.onSuccess(object);
                Snackbar.make(v, "Saved", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                enableBUttons(includeQuestionBar, true);
                listener.onFailure(throwable);
                Snackbar.make(v, "Failed", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private static void enableBUttons(final IncludeQuestionBarBinding includeQuestionBar, boolean enable) {
        includeQuestionBar.buttonYes.setEnabled(enable);
        includeQuestionBar.buttonNo.setEnabled(enable);
        includeQuestionBar.buttonMaybe.setEnabled(enable);
    }
}
