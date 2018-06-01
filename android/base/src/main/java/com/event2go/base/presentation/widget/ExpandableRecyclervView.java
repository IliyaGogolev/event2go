package com.event2go.base.presentation.widget;

import java.util.List;

/**
 * Created by Iliya Gogolev on 4/19/16.
 */
public class ExpandableRecyclervView {// extends RecyclerView {

    /**
     * Interface for implementing required methods in a parent list item.
     */
    public interface ParentListItem {

        List<?> getChildItemList();

        boolean isInitiallyExpanded();
    }

}
