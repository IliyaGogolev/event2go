package com.event2go.app.features.event.presentation.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.databinding.DialogEventFrequencyBinding;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.RecurType;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Iliya Gogolev on 1/20/16.
 */
public class EventFrequencyDialogFragment extends DialogFragment {

    public static class ViewModel extends BaseObservable{

        public Event mEvent;

        public int getFrequencySelectedPosition() {
            switch (mEvent.getFrequency()) {
                case RecurType.NONE:
                    return 0;
                case RecurType.DAILY:
                    return 1;
                case RecurType.WEEKLY:
                    return 2;
                case RecurType.MONTHLY:
                    return 3;
            }

            return 0;
        }

        public void setFrequencySelectedPosition(int position) {
            switch (position) {
                case 0:
                    mEvent.setFrequancy(RecurType.NONE);
                    break;
                case 1:
                    mEvent.setFrequancy(RecurType.DAILY);
                    break;
                case 2:
                    mEvent.setFrequancy(RecurType.WEEKLY);
                    break;
                case 3:
                    mEvent.setFrequancy(RecurType.MONTHLY);
                    break;
            }
        }

        public String[] getFrequencyUntilStrings() {
            return AppApplication.getContext().getResources().getStringArray(R.array.frequency_until);
        }

        public String[] getFrequencyStrings() {

            AppApplication context = AppApplication.getContext();

            String[] values = new String[4];
            values[0] = context.getString(R.string.event_repeat_non);
            values[1] = context.getString(R.string.event_repeat_daily);
            values[2] = context.getString(R.string.event_repeat_weekly);
            values[3] = context.getString(R.string.event_repeat_monthly);
            return values;
        }
    }

    private Date mUntilDate;
    private ViewModel mModel = new ViewModel();

    public void setCustomContentView(Event event) {
        mModel.mEvent = event;
        mUntilDate = event.getRecurEndDate();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setCancelable(false);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_event_frequency, null);
        final DialogEventFrequencyBinding bind = DataBindingUtil.bind(view);
        bind.setModel(mModel);
        bind.setUntilDate(mUntilDate);
        bind.frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bind.untilSpinner.setEnabled(position != 0);
                bind.untilDate.setEnabled(position != 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        bind.untilSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bind.untilDate.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                if (position == 1 && mUntilDate == null) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, 1);
                    mUntilDate = cal.getTime();
                    bind.setUntilDate(mUntilDate);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bind.untilDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                cal.setTime(mUntilDate);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        cal.set(year, month, day);
                        mUntilDate.setTime(cal.getTimeInMillis());
                        bind.setUntilDate(mUntilDate);

                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedItemPosition = bind.frequencySpinner.getSelectedItemPosition();
                mModel.setFrequencySelectedPosition(selectedItemPosition);
                if (selectedItemPosition != 0) { // repeat
                    if (bind.untilSpinner.getSelectedItemPosition() == 0) {
                        mModel.mEvent.setRecurEndDate(null);
                    } else {
                        mModel.mEvent.setRecurEndDate(mUntilDate);
                    }
                }
            }
        });
        return builder.create();
    }


}
