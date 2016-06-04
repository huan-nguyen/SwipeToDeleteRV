# SwipeToDeleteRV
`SwipeToDeleteRV` is a library that provides a fast and convenient way to implement the 'wipe to delete and undo' feature in Recycler View, as seen in apps such as Messenger.

`SwipeToDeleteRV` wraps around `ItemTouchHelper` from the `Android Support Library`. Therefore, developers do not need to do any extra work on `ItemTouchHelper.Callback` themselve. Instead they can just focus on creating a recycler view, adapter, and view holder as normal, plus some minimal work on specifying some details such as supported swipe directions, deletion message or icon.

Below is how `SwipeToDeleteRV` looks.

![Demo Screenshot](./screenshot/dmeo.gif)

# Usage
`SwipeToDeleteRV` is made with the goal of making things as simple as possible. To implement the `RecyclerView` shown above, the following steps are needed.

## XML Layout
````
...
xmlns:stdrv="http://schemas.android.com/apk/res-auto"
...
// instead of normal RecyclerView, 
<io.huannguyen.swipetodeleterv.STDRecyclerView
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    stdrv:border_color="@android:color/darker_gray" // specify things like border color, border width, etc.
    stdrv:delete_view_background="#cccccc"
    stdrv:delete_icon="@drawable/ic_archive"
    stdrv:delete_icon_height="24dp"
    stdrv:delete_icon_width="24dp"
    stdrv:left_delete_icon_margin="32dp"
    stdrv:delete_message="@string/delete_message"
    stdrv:right_delete_icon_margin="32dp"
    stdrv:has_border="true"/>
````
All the above attributes are optional. If you don't specify them, then the default values (defined in the library) are used.

Notes:
- These attributes can also be set programmatically through an instance of the `STDRecyclerView` class.
- By default, the `has_border` attribute has the value of `true`. Setting it `false` would remove the top and bottom borders (shown in the above screen). 

## Java Code
### Adapter
You have to subclass `STDAdapter`. However, all what you need to do is just specifying the type of the items being displayed within the `STDRecyclerView` (i.e., `String` in this example) and define a constructor which takes a list of items as follows.
````
public class SampleAdapter extends STDAdapter<String> {
    public SampleAdapter(List<String> versionList) {
        this.mItems = versionList;
    }
}
````

### Recycler View Setup
Set up `STDRecyclerView` just the same way as you do for a normal `RecyclerView`, except that you have to include this line:
````
mRecyclerView.setupSwipeToDelete(your_adapter_instance, swipe_directions);
````
This is a key method that sets up the swipe-to-delete feature on the recycler view behind the scene. The `swipe_directions` parameter specifies which directions your items can be swiped (to delete). For example, `ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT` indicates that they can be swiped in both directions (left-to-right as well as right-to-left).

A working example looks something like this:
````
// Get your recycler view from the XML layout
mRecyclerView = (STDRecyclerView) findViewById(R.id.recycler_view);
LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
mRecyclerView.setLayoutManager(layoutManager);
mAdapter = new SampleAdapter(versions);

// allow swiping in both directions (left-to-right and right-to-left)
mRecyclerView.setupSwipeToDelete(mAdapter, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
````

That's it! Now you've got a recycler view with swipe-to-delete-and-undo feature. How simple and quick that is?

# More Advanced Settings
Above I've shown how to use `SwipeToDeleteRV` in the simplest case in which the same deletion message is displayed whenever an item is swiped to delete, every item has the same swipe directions, and no additional processing is made whenever an item is removed or added back (by clicking on the `undo` button). In this section, I'll show you some additional features of `SwipeToDeleteRV`.

## Specify different deletion messages for different items
You can override the `getDeleteMessage(ViewHolder viewHolder, STDRecyclerView recyclerView)` function in the `STDAdapter` class. In the default implementation, the `delete_message` specified in `STDRecyclerView` is returned.

## Specify different swipe directions for different items
You can override the `getSwipeDirs(ViewHolder viewHolder)` function in the `STDAdapter` class. By default, this function returns -1, which means the swipe directions for this item is identical to the swipe directions that was set via `STDRecyclerView`'s `setupSwipeToDelete(STDAdapter, int)` method.

## Specify additional actions when an item is removed from or added back to a Recycler View
You can create a listener that implements the `ItemRemovalListener` interface and assign this listener to an instance of `STDAdapter`. Within this interface, the following 2 methods must be implemented:
- `onItemRemoved(ViewHolder viewHolder)`: this method is called when an item is swiped to remove from a `STDRecyclerView` and a `Snackbar` is shown.
- `onItemAddedBack(ViewHolder viewHolder)`: this method is called when an item is added back to a `STDRecyclerView` after the `undo` button on a `Snackbar` is tapped.

# Download
`SwipeToDeleteRV` has been uploaded to JCenter. You can download it by adding the following dependency into your project.
````
compile 'io.huannguyen.SwipeToDeleteRV:swipe-to-delete-rv:1.0.3'
````
# Sample Project
You can access a sample project using `SwipeToDeleteRV` from the `demo` module in this repo.

