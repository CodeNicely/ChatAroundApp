package com.fame.plumbum.chataround.helper;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * This class was initially created for Quick return floater behavior.
 * Created by Meghal on 6/6/2016.
 */
public class QuickReturnFloaterBehavior extends CoordinatorLayout.Behavior<View> {

    private int distance;

    public QuickReturnFloaterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0 && distance < 0 || dy < 0 && distance > 0) {
            child.animate().cancel();
            distance = 0;
        }
        distance += dy;
        final int height = child.getHeight() > 0 ? (child.getHeight()) : 600/*update this accordingly*/;
        if (distance > height && child.isShown()) {
            hide(child, height);
        } else if (distance < 0 && !child.isShown()) {
            show(child, height);
        }
    }

    private void hide(final View view, int height) {
        //     view.setVisibility(View.GONE);// use animate.translateY(height); instead
        ViewPropertyAnimator viewPropertyAnimator = view.animate().translationY(height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewPropertyAnimator.withEndAction(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            });
        }
/*
        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
*/
    }

    private void show(final View view, int height) {
        ViewPropertyAnimator viewPropertyAnimator = view.animate().translationY(0);

        viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);// use animate.translateY(-height); instead

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
//        view.setVisibility(View.VISIBLE);// use animate.translateY(-height); instead
    }

}
