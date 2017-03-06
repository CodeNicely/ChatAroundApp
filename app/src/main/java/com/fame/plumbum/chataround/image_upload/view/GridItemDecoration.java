package com.fame.plumbum.chataround.image_upload.view;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Decoration class that is not used
 * Created by Meghal on 5/28/2016.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int mHorizontalSpacing = 10;
    private int mVerticalSpacing = 10;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Only handle the vertical situation
        int position = parent.getChildAdapterPosition(view);
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) parent.getLayoutManager();
            int spanCount, column;
            // Check the last item and is alone. Then set the parent's width
            if (position == parent.getAdapter().getItemCount() - 1 && position % 2 == 0) {
                spanCount = 1;
                outRect.left = mHorizontalSpacing;
                outRect.right = parent.getWidth() - mHorizontalSpacing;
            } else {
                spanCount = layoutManager.getSpanCount();
                column = position % spanCount;
                outRect.left = mHorizontalSpacing * (spanCount - column) / spanCount;
                outRect.right = mHorizontalSpacing * (column + 1) / spanCount;
            }

            if (position < spanCount) {
                outRect.top = mVerticalSpacing;
            }
            outRect.bottom = mVerticalSpacing;
        }
    }
}
