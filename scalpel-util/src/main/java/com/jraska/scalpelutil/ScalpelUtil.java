package com.jraska.scalpelutil;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.jakewharton.scalpel.ScalpelFrameLayout;

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

    ScalpelFrameLayout scalpelFrameLayout = createScalpelFrameLayout(activity);

    View view = (View) activity.findViewById(android.R.id.content).getParent();

    wrapView(view, scalpelFrameLayout);

    view.setLayoutParams(new ScalpelFrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    scalpelFrameLayout.setOnTouchListener(new UnwrapListener());

    return scalpelFrameLayout;
  }

  //endregion

  //region Methods

  private static ScalpelFrameLayout createScalpelFrameLayout(Context context) {
    if (context == null) {
      throw new IllegalArgumentException("Parameter context cannot be null");
    }

    ScalpelFrameLayout scalpelFrameLayout = new ScalpelFrameLayout(context);
    scalpelFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    scalpelFrameLayout.setLayerInteractionEnabled(true);
    scalpelFrameLayout.setDrawViews(true);
    scalpelFrameLayout.setDrawIds(true);

    return scalpelFrameLayout;
  }

  private static void wrapView(View view, ViewGroup wrapper) {
    final ViewParent parent = view.getParent();

    if (parent != null && parent instanceof ViewGroup) {
      final ViewGroup parentViewGroup = (ViewGroup) parent;

      final int indexOfChild = parentViewGroup.indexOfChild(view);
      parentViewGroup.removeView(view);

      parentViewGroup.addView(wrapper, indexOfChild);
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

    private long _down1;
    private long _down2;

    private long getNextDiff() {
      long currentMillis = SystemClock.elapsedRealtime();

      long diff = (currentMillis - _down2) + (_down2 - _down1);

      _down1 = _down2;
      _down2 = currentMillis;

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

