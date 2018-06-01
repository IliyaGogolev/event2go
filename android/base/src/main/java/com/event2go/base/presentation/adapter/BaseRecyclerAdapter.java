package com.event2go.base.presentation.adapter;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

import com.event2go.base.R;
import com.event2go.base.utils.Logger;

/**
 * Adapters requiring support of single or multiple listener and data types within a
 * {@linkplain RecyclerView} should extend this class. A list is already provided
 * so there is no need to create a list instance. Modifying the data inside this adapter should be done
 * through the interfaces provided here if automatic updating of bounded data is preferred.
 * Direct access to the underlying list is provided for convenience.
 * Modifying the data directly requires notifying this adapter for updating bounded views. .
 * <p/>
 * Created by Iliya Gogolev on 8/24/15.
 */
public abstract class BaseRecyclerAdapter<T extends BaseObservable>
        extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewBindingHolder>
        implements View.OnClickListener {

    /**
     * Contains the list of items that represent the data of this RecyclerView.Adapter.
     * The this list is referred to as "the array" in the documentation.
     */
    protected final ObservableArrayList<T> mItems;

    /**
     * Lock used to modify the content of {@link #mItems}. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private final Object mLock = new Object();
    protected OnItemClickListener mItemClickListener;

    public BaseRecyclerAdapter() {
        mItems = new ObservableArrayList<>();
    }

    public BaseRecyclerAdapter(@NonNull ObservableArrayList<T> items) {
        mItems = items;
    }

    protected abstract int getViewType(BaseObservable item);

    protected abstract int getLayoutIdByViewType(int viewType);

    protected abstract void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, BaseObservable item);

    /**
     * Returns the data used by this adapter.
     * If registering a listener on changes to the array is required
     * it can be done here.
     *
     * @return The list of items in the array.
     */
    public final ObservableArrayList<T> getItems() {
        return mItems;
    }

    /**
     * Adds the specified item at the end of the array.
     *
     * @param item The item to add at the end of the array.
     */
    public void add(T item) {
        synchronized (mLock) {
            mItems.add(item);
            notifyItemInserted(mItems.size() - 1);
        }
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param items The Collection to add at the end of the array.
     */
    public void addAll(Collection<T> items) {
        synchronized (mLock) {
            mItems.addAll(items);
            notifyItemRangeChanged(mItems.size() - items.size(), items.size());
        }
    }

    /**
     * Adds the specified item at the end of the array.
     *
     * @param item The item to add at the end of the array.
     */
    public void add(int position, T item) {
        synchronized (mLock) {
            mItems.add(position, item);
            notifyItemInserted(position);
        }
    }

    /**
     * Removes the specified item from the array.
     *
     * @param item The item to remove.
     */
    public void remove(T item) {
        synchronized (mLock) {
            final int position = getPosition(item);
            mItems.remove(item);
            notifyItemRemoved(position);
        }
    }

    /**
     * Removes the specified position
     *
     * @param position
     */
    public void remove(int position) {
        synchronized (mLock) {
            mItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    /**
     * Removes all items in the array.
     */
    public void clear() {
        synchronized (mLock) {
            int size = mItems.size();
            mItems.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * Returns the item at the specified position in the array.
     */
    public T getItem(int position) {
        return mItems.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(T item) {
        return mItems.indexOf(item);
    }

    /**
     * Registers a click listener notifying when an item in the {@link RecyclerView} has
     * been clicked.
     *
     * @param listener The listener to call when an item is clicked.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    public boolean hasOnItemClickListener() {
        return mItemClickListener != null;
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewBindingHolder holder, int position) {
        setClickListenerOnViews(holder, holder.clickableView);

        final T item = mItems.get(position);
        onBindViewHolderByViewType(getViewType(item), holder, item);
        holder.binding.executePendingBindings();
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        final T observable = mItems.get(position);
        return getViewType(observable);
    }

    @Override
    public ViewBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflateView(parent, getLayoutIdByViewType(viewType));
        return new ViewBindingHolder(view, disableDefaultClickEffect(viewType));
    }

    protected boolean disableDefaultClickEffect(int viewType) {
        return false;
    }

    /**
     * Convenience method for inflating a layout into a ViewGroup.
     *
     * @param parent   The target ViewGroup
     * @param resource The layout resource ID to inflate.
     * @return The inflated listener.
     */
    protected View inflateView(ViewGroup parent, int resource) {
        return LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
    }

    /**
     * Method to set the adapter as the listener for clicks on nested controls. Only necessary if the
     * listener does NOT contain nested buttons or clickable views.
     *
     * @param holder Required for resolving the position of the clicked item.
     * @param views  The nested views to listen on. Can be one or many.
     */
    protected final void setClickListenerOnViews(@NonNull ViewBindingHolder holder, @NonNull View... views) {
        for (View view : views) {
            if (view != null) {
                view.setTag(R.id.tag_key_view_holder, holder);
                view.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag(R.id.tag_key_view_holder);
        if (tag == null) {
            Logger.i("The clicked listener has no tag! Click event has been ignored.");
            return;
        }

        int position = ((ViewBindingHolder) tag).getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            mItemClickListener.onItemClick(v, position);
        }
    }

    /**
     * Interface definition for a callback to be invoked when a listener in the {@code RecyclerView}
     * is clicked.
     */
    public interface OnItemClickListener {

        /**
         * Called when a listener in the {@code RecyclerView} has been clicked.
         *
         * @param view     The listener that was clicked
         * @param position The position of the corresponding item in the adapter.
         */
        void onItemClick(View view, int position);
    }

    /**
     * Uses {@link ViewDataBinding} to create a listener-holder
     */
    public static class ViewBindingHolder<T extends ViewDataBinding>
            extends RecyclerView.ViewHolder {

        public final T binding;
        public final View clickableView;

        /**
         * Creates a listener holder with default font style and can apply an optional click animation
         * to the itemView. By default the itemView will be considered the clickable listener for the
         * click animation unless another listener, designated by the ID {@code R.id.clickable_view},
         * is specified.
         *
         * @param itemView           The listener to be retained in this holder
         * @param disableClickEffect When set to true, does not apply click animation to the itemView.
         */
        public ViewBindingHolder(View itemView, boolean disableClickEffect) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);

            View tempClickableView = itemView.findViewById(R.id.clickable_view);
            if (tempClickableView == null) tempClickableView = itemView;
            clickableView = tempClickableView;

            if (!disableClickEffect) {
                clickableView.setBackgroundResource(R.drawable.button_background_transparent);
            }
        }

        /**
         * Creates a listener holder with default font style and click animation applied the itemView.
         *
         * @param itemView The listener to be retained in this holder.
         */
        public ViewBindingHolder(View itemView) {
            this(itemView, false);
        }
    }
}