package com.jraska.scalpelutil;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.obtain;
import static org.assertj.android.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ScalpelUtilTest {
  //region Test methods

  @Test
  public void testWrapsView() throws Exception {
    Context context = getContext();

    RelativeLayout root = new RelativeLayout(context);
    LinearLayout linearLayout = new LinearLayout(context);
    root.addView(linearLayout);

    for (int i = 0; i < 3; i++) {
      linearLayout.addView(new TextView(context));
    }

    ScalpelFrameLayout scalpel = ScalpelUtil.wrapWithScalpel(linearLayout);

    assertThat(linearLayout).hasParent(scalpel);
    assertThat(scalpel).hasParent(root);
  }

  @Test
  public void testUnwrapsViewOnThreeTaps() throws Exception {
    Context context = getContext();

    RelativeLayout root = new RelativeLayout(context);
    LinearLayout linearLayout = new LinearLayout(context);
    root.addView(linearLayout);

    ScalpelFrameLayout scalpel = ScalpelUtil.wrapWithScalpel(linearLayout);

    dispatchThreeDowns(scalpel);

    assertThat(linearLayout).hasParent(root);
    assertThat(scalpel).hasParent(null);
  }

  @Test
  public void testKeepsLayoutParams() throws Exception {
    Context context = getContext();

    RelativeLayout root = new RelativeLayout(context);
    FrameLayout frameLayout = new FrameLayout(context);
    root.addView(frameLayout);

    ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
    assertNotNull(layoutParams);

    ScalpelFrameLayout scalpel = ScalpelUtil.wrapWithScalpel(frameLayout);

    // Scalpel should adapt the previous layout params
    Assert.assertThat(scalpel.getLayoutParams(), equalTo(layoutParams));

    dispatchThreeDowns(scalpel);

    // parameters should be same
    Assert.assertThat(scalpel.getLayoutParams(), equalTo(layoutParams));
  }

  @Test
  public void testWrapsActivity() throws Exception {
    Activity activity = Robolectric.buildActivity(Activity.class).create().get();

    ScalpelFrameLayout scalpel = ScalpelUtil.wrapWithScalpel(activity);


    @IdRes
    int id = 0x11;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      id = View.generateViewId();
    }

    scalpel.setId(id);

    View viewFromActivity = activity.findViewById(id);

    Assert.assertThat(viewFromActivity, instanceOf(ScalpelFrameLayout.class));
    Assert.assertThat((ScalpelFrameLayout) viewFromActivity, equalTo(scalpel));
  }

  @Test
  public void testUnwrapsActivityOnThreeTaps() throws Exception {
    Activity activity = Robolectric.buildActivity(Activity.class).create().get();

    ScalpelFrameLayout scalpel = ScalpelUtil.wrapWithScalpel(activity);

    @IdRes
    int id = 0x11;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      id = View.generateViewId();
    }

    scalpel.setId(id);

    dispatchThreeDowns(scalpel);

    View nextViewFromActivity = activity.findViewById(id);
    Assert.assertThat(nextViewFromActivity, is(CoreMatchers.<View>nullValue()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentOnNullActivity() throws Exception {
    ScalpelUtil.wrapWithScalpel((Activity) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentOnNullView() throws Exception {
    ScalpelUtil.wrapWithScalpel((View) null);
  }

  //endregion

  //region Methods

  private void dispatchThreeDowns(ScalpelFrameLayout scalpel) {
    long millis = SystemClock.uptimeMillis();
    MotionEvent downEvent = obtain(millis, millis, ACTION_DOWN, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    for (int i = 0; i < 3; i++) {
      scalpel.dispatchTouchEvent(downEvent);
    }
  }

  Context getContext() {
    return RuntimeEnvironment.application;
  }

  //endregion
}
