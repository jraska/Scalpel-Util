package com.jraska.scalpelutil.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jraska.scalpelutil.ScalpelUtil;

public class MainActivity extends AppCompatActivity {
  //region Fields

  @Bind(R.id.toolbar) Toolbar _toolbar;

  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(_toolbar);
  }

  //endregion

  //region Methods

  @OnClick(R.id.fab) void wrapWithScalpel() {
    ScalpelUtil.wrapWithScalpel(this);
  }

  //endregion
}
