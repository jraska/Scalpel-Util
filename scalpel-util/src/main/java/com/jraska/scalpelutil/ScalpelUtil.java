package com.jraska.scalpelutil;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.jakewharton.scalpel.ScalpelFrameLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Utility class allowing inject {@link ScalpelFrameLayout} to provided Activity at runtime.
 */
public final class ScalpelUtil {
  //region Public API

  /**
   * Wraps activity content with new {@link ScalpelFrameLayout instance}
   * and turns on all of its features.
   * <p/>
   * You can unwrap the Activity with three fast clicks on screen.
   *
   * @param activity Activity to wrap content of.
   * @return New created Scalpel frame layout wrapping the activity content.
   */
  public static ScalpelFrameLayout wrapWithScalpel(Activity activity) {
    if (activity == null) {
      throw new IllegalArgumentException("Parameter activity cannot be null");
    }

    View view = (View) activity.findViewById(android.R.id.content).getParent();

    return wrapWithScalpelInternal(view);
  }

  /**
   * Wraps activity content with new {@link ScalpelFrameLayout instance}
   * and turns on all of its features.
   * <p/>
   * You can unwrap the Activity with three fast clicks on screen.
   *
   * @param view Activity to wrap content of.
   * @return New created Scalpel frame layout wrapping the view content.
   */
  public static ScalpelFrameLayout wrapWithScalpel(View view) {
    if (view == null) {
      throw new IllegalArgumentException("Parameter 'view' cannot be null");
    }

    return wrapWithScalpelInternal(view);
  }

  //endregion

  //region Methods

  private static ScalpelFrameLayout wrapWithScalpelInternal(View view) {
    ScalpelFrameLayout scalpelFrameLayout = createScalpelFrameLayout(view.getContext());
    wrapView(view, scalpelFrameLayout);

    view.setLayoutParams(new ScalpelFrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

    scalpelFrameLayout.setOnTouchListener(new UnwrapListener());

    return scalpelFrameLayout;
  }

  private static ScalpelFrameLayout createScalpelFrameLayout(Context context) {
    if (context == null) {
      throw new IllegalArgumentException("Parameter context cannot be null");
    }

    ScalpelFrameLayout scalpelFrameLayout = new ScalpelFrameLayout(context);
    scalpelFrameLayout.setLayerInteractionEnabled(true);
    scalpelFrameLayout.setDrawViews(true);
    scalpelFrameLayout.setDrawIds(true);

    return scalpelFrameLayout;
  }

  private static void wrapView(View view, ViewGroup wrapper) {
    final ViewParent parent = view.getParent();

    if (parent != null && parent instanceof ViewGroup) {
      final ViewGroup parentViewGroup = (ViewGroup) parent;

      // Wrapper should have same layout params as the previous view had
      ViewGroup.LayoutParams previousLayoutParams = view.getLayoutParams();

      final int indexOfChild = parentViewGroup.indexOfChild(view);
      parentViewGroup.removeView(view);

      parentViewGroup.addView(wrapper, indexOfChild, previousLayoutParams);
    }

    wrapper.addView(view);
  }

  private static void unwrapView(ViewGroup wrapper) {
    final int childCount = wrapper.getChildCount();
    View[] childViews = new View[childCount];


    ViewGroup parent = (ViewGroup) wrapper.getParent();
    parent.removeView(wrapper);

    for (int i = 0; i < childCount; i++) {
      childViews[i] = wrapper.getChildAt(i);
    }

    // If there was just one wrapper reuse the wrapper layout
    // params to ensure correct type for parent
    if (childCount == 1) {
      ViewGroup.LayoutParams wrapperParams = wrapper.getLayoutParams();
      childViews[0].setLayoutParams(wrapperParams);
    }

    for (int i = 0; i < childCount; i++) {
      final View childView = childViews[i];

      wrapper.removeView(childView);
      parent.addView(childView);
    }
  }

  //endregion

  //region Nested class

  private static class UnwrapListener implements View.OnTouchListener {
    private static final long THREE_CLICKS_THRESHOLD = 500;

    // Both values are initialized with very past values for test purposes
    // and also to avoid potential error on zero uptime millis :D
    private long _firstDown = Long.MIN_VALUE;
    private long _secondDown = Long.MIN_VALUE;

    private long getNextDiff() {
      long currentMillis = SystemClock.elapsedRealtime();

      long diff = (currentMillis - _firstDown);

      _firstDown = _secondDown;
      _secondDown = currentMillis;

      // Avoids start negative values errors
      if (diff < 0) {
        return -diff;
      }

      return diff;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
        long diff = getNextDiff();
        if (diff < THREE_CLICKS_THRESHOLD) {
          unwrapView((ScalpelFrameLayout) v);
          return true;
        }
      }

      return false;
    }
  }

  //endregion

  //region Constructors

  private ScalpelUtil() {
    // No instances
  }

  //endregion
}

