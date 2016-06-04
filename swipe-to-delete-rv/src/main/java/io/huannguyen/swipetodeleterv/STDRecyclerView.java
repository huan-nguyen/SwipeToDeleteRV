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
    private Paint mPaint = new Paint();

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

        typedArray.recycle();
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

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) (itemView.getBottom() - itemView.getTop());

                    if(dX != 0) {
                        Bitmap deleteIcon = BitmapFactory.decodeResource(getResources(), mDeleteIconRes);
                        RectF deleteIconPos;
                        Rect iconDrawnPortion = null;
                        float iconWidth = mDeleteIconWidth;

                        // set paint color as delete view background color
                        mPaint.setColor(mDeleteViewBackground);

                        if(dX > 0) {
                            // draw background (area which is visible when the item view is swiped)
                            RectF background = new RectF(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
                            c.drawRect(background, mPaint);

                            // draw delete icon
                            if(dX > mLeftDeleteIconMargin) {
                                if(dX < mLeftDeleteIconMargin + mDeleteIconWidth) {
                                    iconWidth = (int)(dX - mLeftDeleteIconMargin);
                                    // specify the portion of the icon being drawn
                                    iconDrawnPortion = new Rect(0, 0, (int)iconWidth, (int)mDeleteIconHeight);
                                }
                                deleteIconPos = new RectF(itemView.getLeft() + (int)mLeftDeleteIconMargin , (int)(itemView.getTop() + 0.5*(height - mDeleteIconHeight)), (int) (itemView.getLeft()+ mLeftDeleteIconMargin + iconWidth), (int)(itemView.getBottom() - 0.5*(height - mDeleteIconHeight)));
                                c.drawBitmap(deleteIcon, iconDrawnPortion, deleteIconPos, mPaint);
                            }
                        } else {
                            // draw background (area which is visible when the item view is swiped)
                            RectF background = new RectF(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            c.drawRect(background, mPaint);

                            // draw delete icon
                            if(dX < 0 - mRightDeleteIconMargin) {
                                if(dX > 0 - mLeftDeleteIconMargin - mDeleteIconWidth) {
                                    iconWidth = (int)(0 - dX - mRightDeleteIconMargin);
                                    // specify the portion of the icon being drawn
                                    iconDrawnPortion = new Rect((int)(mDeleteIconWidth - iconWidth), 0, (int)mDeleteIconWidth, (int)mDeleteIconHeight);
                                }
                                deleteIconPos = new RectF(itemView.getRight() - mRightDeleteIconMargin - iconWidth, (float)(itemView.getTop() + 0.5*(height - mDeleteIconHeight)), (float) itemView.getRight()- mRightDeleteIconMargin, (float)(itemView.getBottom() - 0.5*(height - mDeleteIconHeight)));
                                c.drawBitmap(deleteIcon, iconDrawnPortion, deleteIconPos, mPaint);
                            }
                        }

                        // draw borders on item view area
                        if(mHasBorder) {
                            mPaint.setColor(mBorderColor);
                            RectF topBorder = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getRight(), itemView.getTop() + mBorderWidth);
                            c.drawRect(topBorder, mPaint);
                            RectF bottomBorder = new RectF(itemView.getLeft(), itemView.getBottom() - mBorderWidth, itemView.getRight(), itemView.getBottom());
                            c.drawRect(bottomBorder, mPaint);
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
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
