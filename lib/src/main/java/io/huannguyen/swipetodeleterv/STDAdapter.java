package io.huannguyen.swipetodeleterv;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.List;

/**
 * Created by huannguyen on 1/06/2016.
 */

public abstract class STDAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected List<T> mItems;

    /**
     * This method is invoked when an item is swiped to remove from a {@link STDRecyclerView}.
     * A {@link Snackbar} with a removal message and an undo button would be displayed.
     * Tapping on the undo button would bring the item back to the {@link STDRecyclerView}.
     *
     * <p>
     * In order to add additional actions on removing or adding the item back to the {@link STDRecyclerView},
     * you can set a {@link ItemRemovalListener} for the {@link STDRecyclerView}.
     *
     * @param viewHolder    The view holder associated with the item being removed
     * @param recyclerView  The recycler view contains the item
     *
     * <p>
     * See {@link STDRecyclerView#setItemRemovalListener(ItemRemovalListener)}
     */
    public void onItemCleared(final ViewHolder viewHolder, final STDRecyclerView recyclerView) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final T item = mItems.get(adapterPosition);
        final Snackbar snackbar = Snackbar
                .make(recyclerView, getDeleteMessage(viewHolder, recyclerView), Snackbar.LENGTH_LONG)
                .setAction(R.string.stdrv_action_undo, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItems.add(adapterPosition, item);
                        notifyItemInserted(adapterPosition);
                        if (recyclerView.getItemRemovalListener() != null) {
                            recyclerView.getItemRemovalListener().onItemAddedBack(viewHolder);
                        }
                    }
                });
        snackbar.show();
        snackbar.getView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        if (recyclerView.getItemRemovalListener() != null) {
            recyclerView.getItemRemovalListener().onItemRemoved(viewHolder);
        }
        mItems.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
    }

    /**
     * Get a message being displayed on a {@link Snackbar} when an item is removed.
     * <p>
     * By default, this return the deletion message set in the {@link STDRecyclerView} instance (via attributes or programmatically).
     * <p>
     * Override this method if you want to programmatically assign different deletion message to different {@link ViewHolder} or item type
     *
     * @param viewHolder    The {@link ViewHolder} associated with the item being removed
     * @param recyclerView  The {@link STDRecyclerView} containing the item being removed
     *
     * @return a deletion message
     */
    protected String getDeleteMessage(ViewHolder viewHolder, STDRecyclerView recyclerView) {
        return recyclerView.getDeleteMessage();
    }
}