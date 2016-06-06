package io.huannguyen.swipetodeleterv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.SimpleCallback;
import android.util.AttributeSet;
import android.view.View;

import io.huannguyen.swipetodeleterv.utils.ResourceUtils;

/**
 * Created by huannguyen on 1/06/2016.
 */

public class STDRecyclerView extends RecyclerView {
    private @DrawableRes int mDeleteIconRes;
    private @ColorInt
    int mBorderColor;
    private float mBorderWidth;
    private @ColorInt int mDeleteViewBackground;
    private float mDeleteIconHeight;
    private float mDeleteIconWidth;
    private float mLeftDeleteIconMargin;
    private float mRightDeleteIconMargin;
    private String mDeleteMessage;
    private boolean mHasBorder;
    private @ColorInt int mDeleteIconColor;
    private Paint mPaint = new Paint();
    private Bitmap mDeleteIcon;

    public STDRecyclerView(Context context) {
        super(context);
    }

    public STDRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        processAttributes(context, attrs, 0);
    }

    public STDRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributes(context, attrs, defStyle);
    }

    private void processAttributes(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.stdrv, defStyle, 0);

        mDeleteIconRes = typedArray.getResourceId(R.styleable.stdrv_delete_icon, R.drawable.ic_delete);
        mBorderColor = typedArray.getColor(R.styleable.stdrv_border_color, ResourceUtils.getColor(context, R.color.stdrv_default_border_color));
        mBorderWidth = typedArray.getDimension(R.styleable.stdrv_border_width, ResourceUtils.getDimension(context, R.dimen.stdrv_default_border_width));
        mDeleteViewBackground = typedArray.getColor(R.styleable.stdrv_delete_view_background, ResourceUtils.getColor(context, R.color.stdrv_default_delete_view_background));
        mDeleteIconHeight = typedArray.getDimension(R.styleable.stdrv_delete_icon_height, ResourceUtils.getDimension(context, R.dimen.stdrv_default_icon_size));
        mDeleteIconWidth = typedArray.getDimension(R.styleable.stdrv_delete_icon_width, ResourceUtils.getDimension(context, R.dimen.stdrv_default_icon_size));
        mLeftDeleteIconMargin = typedArray.getDimension(R.styleable.stdrv_left_delete_icon_margin, ResourceUtils.getDimension(context, R.dimen.stdrv_default_icon_margin));
        mRightDeleteIconMargin = typedArray.getDimension(R.styleable.stdrv_right_delete_icon_margin, ResourceUtils.getDimension(context, R.dimen.stdrv_default_icon_margin));
        mDeleteMessage = typedArray.getString(R.styleable.stdrv_delete_message);
        mHasBorder = typedArray.getBoolean(R.styleable.stdrv_has_border, true);
        mDeleteIconColor = typedArray.getColor(R.styleable.stdrv_delete_icon_color, -1);
        typedArray.recycle();

        initResources();
    }

    private void initResources() {
        mDeleteIcon = BitmapFactory.decodeResource(getResources(), mDeleteIconRes);
        if(mDeleteIconColor != -1) {
            mDeleteIcon = ResourceUtils.changeBitmapColor(mDeleteIcon, mDeleteIconColor);
        }
    }

    /**
     * This method must be called in order to enable swipe-to-delete feature on the {@link STDRecyclerView}
     *
     * @param adapter           The adapter for interacting with the items in the {@link STDRecyclerView}
     * @param swipeDirection    The direction that items in the recycler view can be swiped
     */
    public void setupSwipeToDelete(final STDAdapter adapter, final int swipeDirection) {
        setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleCallback(0, swipeDirection) {
            @Override
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder
                    target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dirs = adapter.getSwipeDirs(viewHolder);
                if(dirs == -1) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                }
                return dirs;
            }

            @Override
            public void onSwiped(ViewHolder viewHolder, int direction) {
                adapter.onItemCleared(viewHolder, STDRecyclerView.this);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) (itemView.getBottom() - itemView.getTop());

                    if(dX != 0) {
                        float iconWidth = mDeleteIconWidth;
                        boolean hasBackground = itemView.getBackground() != null;

                        // set paint color as delete view background color
                        mPaint.setColor(mDeleteViewBackground);

                        if(dX > 0) {
                            // draw background (area which is visible when the item view is swiped)
                            c.drawRect(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom(), mPaint);

                            // draw delete icon
                            // in case the item view has a background (defined with i.e., android:background),
                            // then we can draw the full icon since it can be hidden by the item view.
                            // otherwise, we have to draw just a portion of the icon in case the item view hasn't been moved out of
                            // the area in which the icon should be displayed.
                            if(dX > mLeftDeleteIconMargin) {
                                if(!hasBackground && dX < mLeftDeleteIconMargin + mDeleteIconWidth) {
                                    iconWidth = (int)(dX - mLeftDeleteIconMargin);
                                    // specify the portion of the icon being drawn
                                    Rect iconDrawnPortion = new Rect(0, 0, (int)iconWidth, (int)mDeleteIconHeight);
                                    RectF deleteIconPos = new RectF(itemView.getLeft() + (int)mLeftDeleteIconMargin , (int)(itemView.getTop() + 0.5*(height - mDeleteIconHeight)), (int) (itemView.getLeft()+ mLeftDeleteIconMargin + iconWidth), (int)(itemView.getBottom() - 0.5*(height - mDeleteIconHeight)));
                                    c.drawBitmap(mDeleteIcon, iconDrawnPortion, deleteIconPos, mPaint);
                                } else {
                                    c.drawBitmap(mDeleteIcon, itemView.getLeft() + (int)mLeftDeleteIconMargin , (int)(itemView.getTop() + 0.5*(height - mDeleteIconHeight)), mPaint);
                                }
                            }
                        } else {
                            // draw background (area which is visible when the item view is swiped)
                            c.drawRect(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom(), mPaint);

                            // draw delete icon
                            if(dX < 0 - mRightDeleteIconMargin) {
                                if(!hasBackground && dX > 0 - mLeftDeleteIconMargin - mDeleteIconWidth) {
                                    iconWidth = (int)(0 - dX - mRightDeleteIconMargin);
                                    // specify the portion of the icon being drawn
                                    Rect iconDrawnPortion = new Rect((int)(mDeleteIconWidth - iconWidth), 0, (int)mDeleteIconWidth, (int)mDeleteIconHeight);
                                    RectF deleteIconPos = new RectF(itemView.getRight() - mRightDeleteIconMargin - iconWidth, (float)(itemView.getTop() + 0.5*(height - mDeleteIconHeight)), (float) itemView.getRight()- mRightDeleteIconMargin, (float)(itemView.getBottom() - 0.5*(height - mDeleteIconHeight)));
                                    c.drawBitmap(mDeleteIcon, iconDrawnPortion, deleteIconPos, mPaint);
                                } else {
                                    c.drawBitmap(mDeleteIcon, itemView
                                            .getRight() - mRightDeleteIconMargin - iconWidth, (float) (itemView
                                            .getTop() + 0.5 * (height - mDeleteIconHeight)), mPaint);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, ViewHolder
                    viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                // borders must be drawn within onChildDrawOver instead of onChildDraw to make sure it is on top of the item view
                // in case the item view has a background
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX != 0) {
                    if(mHasBorder) {
                        View itemView = viewHolder.itemView;
                        mPaint.setColor(mBorderColor);
                        c.drawRect(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getTop() + mBorderWidth, mPaint);
                        c.drawRect(itemView.getLeft(), itemView.getBottom() - mBorderWidth, itemView.getRight(), itemView.getBottom(), mPaint);
                    }
                }
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(STDRecyclerView.this);
    }

    public @DrawableRes int getDeleteIconRes() {
        return mDeleteIconRes;
    }

    public void setDeleteIconRes(@DrawableRes int deleteIconRes) {
        mDeleteIconRes = deleteIconRes;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
    }

    public int getDeleteViewBackground() {
        return mDeleteViewBackground;
    }

    public void setDeleteViewBackground(int deleteViewBackground) {
        mDeleteViewBackground = deleteViewBackground;
    }

    public float getDeleteIconHeight() {
        return mDeleteIconHeight;
    }

    public void setDeleteIconHeight(float deleteIconHeight) {
        mDeleteIconHeight = deleteIconHeight;
    }

    public float getDeleteIconWidth() {
        return mDeleteIconWidth;
    }

    public void setDeleteIconWidth(float deleteIconWidth) {
        mDeleteIconWidth = deleteIconWidth;
    }

    public float getLeftDeleteIconMargin() {
        return mLeftDeleteIconMargin;
    }

    public void setLeftDeleteIconMargin(float leftDeleteIconMargin) {
        mLeftDeleteIconMargin = leftDeleteIconMargin;
    }

    public float getRightDeleteIconMargin() {
        return mRightDeleteIconMargin;
    }

    public void setRightDeleteIconMargin(float rightDeleteIconMargin) {
        mRightDeleteIconMargin = rightDeleteIconMargin;
    }

    public String getDeleteMessage() {
        return mDeleteMessage;
    }

    public void setDeleteMessage(String deleteMessage) {
        mDeleteMessage = deleteMessage;
    }

    public boolean isHasBorder() {
        return mHasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        mHasBorder = hasBorder;
    }
}
