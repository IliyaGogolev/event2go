package com.event2go.base.presentation.widget;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.view.View;

import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

import java.util.List;

/**
 * Created by Iliya Gogolev on 4/20/16.
 */
public abstract class ExpandableRecyclerAdapter extends BaseRecyclerAdapter {

    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;

    /**
     * Created by Iliya Gogolev on 4/20/16.
     */
    public abstract class ParentListItem extends BaseObservable {

        abstract public List<?> getChildItemList();
        abstract public boolean isExpanded();
        abstract public void setIsExpanded(boolean isExpanded);
    }


    public abstract void onBindParentViewHolder(int viewType, ViewBindingHolder holder, ParentListItem item);

    public abstract void onBindChildViewHolder(int viewType, ViewBindingHolder holder, BaseObservable item);

    protected void onParentListCollapsed(ParentListItem parentItem, ViewBindingHolder parentViewHolder) {
    }

    protected void onParentListExpanded(ParentListItem parentItem, ViewBindingHolder parentViewHolder) {
    }


    protected abstract int getParentLayoutId();

    protected abstract int getChildLayoutId();

    public ExpandableRecyclerAdapter(List<? extends ParentListItem> parentListItems) {
        super();

        ObservableArrayList items = generateParentChildItemList(parentListItems);
        addAll(items);
    }

    @Override
    protected int getViewType(BaseObservable item) {

        if (item == null) {
            throw new IllegalStateException("Null object added");
        } else if (item instanceof ParentListItem) {
            return TYPE_PARENT;
        }

        return TYPE_CHILD;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        if (viewType == TYPE_PARENT) {
            return getParentLayoutId();
        }

        return getChildLayoutId();
    }


    @Override
    final protected void onBindViewHolderByViewType(int viewType, final ViewBindingHolder holder, final BaseObservable item) {
        if (viewType == TYPE_PARENT) {
            onBindParentViewHolder(viewType, holder, (ParentListItem) item);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean expanded = ((ParentListItem) item).isExpanded();
                    if (expanded) {
                        collapseParentListItem((ParentListItem) item, holder);
                    } else {
                        expandParentListItem((ParentListItem) item, holder);
                    }


                }
            });
        } else {
            onBindChildViewHolder(viewType, holder, item);
        }
    }

    public static ObservableArrayList<Object> generateParentChildItemList(List<? extends ParentListItem> parentItemList) {
        ObservableArrayList<Object> parentWrapperList = new ObservableArrayList<>();
        ParentListItem parentListItem;

        int parentListItemCount = parentItemList.size();
        for (int i = 0; i < parentListItemCount; i++) {
            parentListItem = parentItemList.get(i);
            parentWrapperList.add(parentListItem);

            if (parentListItem.isExpanded()) {

                List<?> childItemList = parentListItem.getChildItemList();
                for (int j = 0; childItemList != null && j < childItemList.size(); j++) {
                    parentWrapperList.add(childItemList.get(j));
                }
            }
        }

        return parentWrapperList;
    }

    private void collapseParentListItem(ParentListItem parentItem, ViewBindingHolder parentViewHolder) {
        if (parentItem.isExpanded()) {
            parentItem.setIsExpanded(false);

            int parentIndex = parentViewHolder.getAdapterPosition();

            List<?> childItemList = parentItem.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    mItems.remove(parentIndex + i + 1);
                }

                notifyItemRangeRemoved(parentIndex + 1, childListItemCount);
            }

            onParentListCollapsed(parentItem, parentViewHolder);
        }
    }


    private void expandParentListItem(ParentListItem parentItem, ViewBindingHolder parentViewHolder) {
        if (!parentItem.isExpanded()) {
            parentItem.setIsExpanded(true);
            int parentIndex = parentViewHolder.getAdapterPosition();

            List<?> childItemList = parentItem.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    mItems.add(parentIndex + i + 1, (BaseObservable) childItemList.get(i));
                }

                notifyItemRangeInserted(parentIndex + 1, childListItemCount);
            }

            onParentListExpanded(parentItem, parentViewHolder);
        }
    }
}
