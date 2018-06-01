package com.event2go.base.presentation.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.event2go.base.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSpinnerAdapter<T> implements SpinnerAdapter {

    LayoutInflater mInflater;
    private List<T> mList = new ArrayList<>();

    protected abstract String getItemText(int position);

    public BaseSpinnerAdapter(Context context, List<T> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    protected List<T> getItems() {
        return this.mList;
    }

    protected void setItems(List<T> items) {
        this.mList = items;
    }

    protected int getItemCount() {
        return (mList != null) ? mList.size() : 0;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(R.layout.list_item_spinner_dropdown_item, null);

        TextView txtLine1 = (TextView) convertView.findViewById(R.id.line1);
        txtLine1.setText(getItemText(position));

        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.list_item_spinner_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.txtLine1.setText(getItemText(position));

        return convertView;

    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private static class ViewHolder {
        public TextView txtLine1;

        public ViewHolder(View view) {
            txtLine1 = (TextView) view.findViewById(R.id.line1);
        }
    }
}
