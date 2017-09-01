package com.ashokvarma.bottomnavigation.behaviour;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Bottom menu bar show/hide effect.
 *
 * @author RobinVanYang created at 2017-08-24 13:36.
 */

public class BottomViewBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {
    private static final int VIEW_STATE_SHOW = 1;
    private static final int VIEW_STATE_HIDING = 1 << 1;
    private static final int VIEW_STATE_HIDE = 1 << 2;
    private static final int VIEW_STATE_SHOWING = 1 << 3;

    private int mViewState = VIEW_STATE_SHOW;

    private ViewPropertyAnimatorCompat mViewPropertyAnimatorCompatShow;
    private ViewPropertyAnimatorCompat mViewPropertyAnimatorCompatHide;

    public BottomViewBehavior() {
        super();
    }

    public BottomViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V child, View directTargetChild, View target, int axes) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);

        if (dy > 0 && (mViewState & (VIEW_STATE_SHOW | VIEW_STATE_SHOWING)) != 0) {
            if ((mViewState & VIEW_STATE_SHOWING) != 0)
                mViewPropertyAnimatorCompatShow.cancel();

            animateHide(child);
        } else if (dy < 0 && (mViewState & (VIEW_STATE_HIDING | VIEW_STATE_HIDE)) != 0) {
            if ((mViewState & VIEW_STATE_HIDING) != 0)
                mViewPropertyAnimatorCompatHide.cancel();

            animateShow(child);
        }
    }

    private void animateHide(final View view) {
        mViewPropertyAnimatorCompatHide = ViewCompat.animate(view).translationY(view.getHeight())
                .setInterpolator(new FastOutSlowInInterpolator())
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    public void onAnimationStart(View view) {
                        mViewState = VIEW_STATE_HIDING;
                    }

                    public void onAnimationEnd(View view) {
                        mViewState = VIEW_STATE_HIDE;
                    }

                    public void onAnimationCancel(View view) {
                    }
                });
        mViewPropertyAnimatorCompatHide.start();
    }

    private void animateShow(View view) {
        mViewPropertyAnimatorCompatShow = ViewCompat.animate(view).translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        mViewState = VIEW_STATE_SHOWING;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        mViewState = VIEW_STATE_SHOW;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                    }
                });
        mViewPropertyAnimatorCompatShow.start();
    }
}
