package io.huannguyen.swipetodeleterv;

import android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by huannguyen on 3/06/2016.
 */

public interface ItemRemovalListener {
    /**
     * Method invoked when an item associated to the given view holder is removed from a {@link STDRecyclerView}.
     *
     * @param viewHolder
     */
    void onItemRemoved(ViewHolder viewHolder);

    /**
     * Method invoked when an item associated to the given view holder is added back to a {@link STDRecyclerView}.
     *
     * @param viewHolder
     */
    void onItemAddedBack(ViewHolder viewHolder);
}