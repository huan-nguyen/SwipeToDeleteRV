package io.huannguyen.swipetodeleterv;

/**
 * Created by huannguyen on 3/06/2016.
 */

public interface ItemRemovalListener<T> {
    /**
     * Method invoked when an item is temporarily removed from a {@link STDRecyclerView}.
     *
     * At this stage, user still has a chance to add the item back into the list using the undo button.
     *
     * @param position    The position of the item being temporarily removed
     */
    void onItemTemporarilyRemoved(int position);

    /**
     * Method invoked when it is no longer possible to add the previously removed item back into a {@link STDRecyclerView}.
     *
     * This is a good place for taking the next step in the removal such as database persistence
     *
     * @param item    The item being permanently removed
     */
    void onItemPermanentlyRemoved(T item);

    /**
     * Method invoked when an item associated to the given view holder is added back to a {@link STDRecyclerView}.
     *
     * @param position    The position of the item being added back
     */
    void onItemAddedBack(int position);
}