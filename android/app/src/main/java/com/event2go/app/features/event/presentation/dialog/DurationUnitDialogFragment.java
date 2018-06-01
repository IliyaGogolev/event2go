package com.event2go.app.features.event.presentation.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.event2go.app.R;
import com.event2go.app.features.event.data.RecurType;
import com.event2go.app.features.event.data.RecurUtils;
import com.event2go.base.utils.LinearLayoutManager;
import com.event2go.app.features.event.presentation.EventViewModel;

import net.fortuna.ical4j.model.Dur;

import java.util.Collection;
import java.util.Set;
import java.util.SortedMap;


/**
 * Created by Iliya Gogolev on 6/19/15.
 */
public class DurationUnitDialogFragment extends DialogFragment {

    public interface OnDoneClickListener {
        void onDoneClicked(Dur duration);
    }

    // key - @DurationUnit.DurUnit
    private SortedMap<Integer, String> mDurValues;
    private RepeatsRecyclerViewAdapter mRepeatsRecyclerViewAdapter;
    private OnDoneClickListener mDoneClickListener;
    private int mSelectedIndex = 1;
    private Dur mInitialDur;
    private View myCustomView;
//    @DurUnits.DurUnit
//    private int mInitialDurationUnit = DurUnits.NONE;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        myCustomView= inflater.inflate(R.layout.dialog_fragment_reminder_to_confirm_time_chooser, null);

        mDurValues = RecurUtils.getAlertDurUnitNames(false);
        int alertDurUnit = EventViewModel.getAlertDurUnit(mInitialDur);
        int amount = EventViewModel.getRSVPAlertValue(mInitialDur);
        if (alertDurUnit != RecurType.NONE) {
            Set<Integer> integers = mDurValues.keySet();
            int index = 0;
            for (Integer i: integers) {
                if (i == alertDurUnit) {
                    mSelectedIndex = index;
                    break;
                }
                index++;
            }
        }

        mRepeatsRecyclerViewAdapter = new RepeatsRecyclerViewAdapter(mDurValues);

        final EditText durAmountEditText = (EditText) myCustomView.findViewById(R.id.dur_amount);
        durAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().compareToIgnoreCase("1") == 0) {
                    mDurValues = RecurUtils.getAlertDurUnitNames(true);
                } else {
                    mDurValues = RecurUtils.getAlertDurUnitNames(false);
                }

                mRepeatsRecyclerViewAdapter.setValues(mDurValues);
                mRepeatsRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        durAmountEditText.setText(Integer.toString(amount));


        RecyclerView recyclerView = (RecyclerView) myCustomView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mRepeatsRecyclerViewAdapter);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDoneClickListener != null) {

                    Set<Integer> integers = mDurValues.keySet();
                    int unit = (Integer) integers.toArray()[mSelectedIndex];

                    Dur dur =  getDur(Integer.parseInt(durAmountEditText.getText().toString()), unit);
                    mDoneClickListener.onDoneClicked(dur);
                }
            }
        });
        builder.setView(myCustomView);

        return builder.create();

    }


    public static Dur getDur(int value, int unit) {
        switch (unit) {
            case RecurType.HOURLY:
                return new Dur(0, value, 0, 0);
            case RecurType.DAILY:
                return new Dur(value, 0, 0, 0);
            case RecurType.WEEKLY:
                return new Dur(value);
        }

        return null;
    }

    public void setOnDoneClickListener(OnDoneClickListener mDoneClickListener) {
        this.mDoneClickListener = mDoneClickListener;
    }


    public class RepeatsRecyclerViewAdapter extends RecyclerView.Adapter<RepeatsRecyclerViewAdapter.ViewHolder> {

        private SortedMap<Integer, String>  mValues;


        public void setValues(SortedMap<Integer, String> mDurValues) {
            mValues = mDurValues;
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private final View view;
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);

                this.view = view;
                this.view.setOnClickListener(this);
                textView = (TextView) view.findViewById(R.id.text);
            }

            @Override
            public void onClick(View view) {
                mSelectedIndex = getLayoutPosition();
                notifyDataSetChanged();
            }
        }

        public RepeatsRecyclerViewAdapter(SortedMap<Integer, String>  items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_text, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            Collection<String> values = mValues.values();
            String value = (String)(values.toArray())[position];

            if (position == mSelectedIndex) {
                holder.textView.setTextColor(getActivity().getResources().getColor(R.color.suggestion_highlight_text));
                String formatedText = String.format(getString(R.string.repeat_messages), value);
                holder.textView.setText(formatedText);
            } else {

                holder.textView.setText(value);
                holder.textView.setTextColor(getActivity().getResources().getColor(android.R.color.primary_text_light));

            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    public void setInitialDur(Dur dur) {
        this.mInitialDur = dur;
    }


    @Override
    public void onStart() {
        // This MUST be called first! Otherwise the listener tweaking will not be present in the displayed Dialog (most likely overriden)
        super.onStart();

        forceWrapContent(myCustomView);
    }

    protected void forceWrapContent(View v) {
        // Start with the provided listener
        View current = v;

        // Travel up the tree until fail, modifying the LayoutParams
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the listener
                try {
                    current = (View) parent;
                } catch (ClassCastException e) {
                    // This will happen when at the top listener, it cannot be cast to a View
                    break;
                }

                // Modify the layout
                current.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
        } while (current.getParent() != null);

        // Request a layout to be re-done
        current.requestLayout();
    }
}
