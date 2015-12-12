package com.jraska.scalpelutil.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.jraska.scalpelutil.ScalpelUtil;

public class MainActivity extends AppCompatActivity {
  //region Fields

  @Bind(R.id.toolbar) Toolbar _toolbar;
  @Bind(R.id.recycler) RecyclerView _recyclerView;

  //endregion

  //region Activity overrides

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(_toolbar);

    _recyclerView.setLayoutManager(new LinearLayoutManager(this));
    _recyclerView.setAdapter(new SampleAdapter());
  }

  //endregion

  //region Methods

  @OnClick(R.id.fab) void wrapWithScalpel() {
    ScalpelUtil.wrapWithScalpel(this);
  }

  //endregion

  //region Nested classes

  static class SampleAdapter extends RecyclerView.Adapter<MainActivity.ItemHolder> {
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View inflated = inflater.inflate(R.layout.item_main, parent, false);

      ItemHolder itemHolder = new ItemHolder(inflated);
      return itemHolder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
      Context context = holder.itemView.getContext();

      holder._titleText.setText(context.getString(R.string.item, position));
      holder._descriptionText.setText(context.getString(R.string.item_description, position));
    }

    @Override
    public int getItemCount() {
      return 10;
    }
  }

  static class ItemHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.item_title) TextView _titleText;
    @Bind(R.id.item_description) TextView _descriptionText;

    public ItemHolder(View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }
  }

  //endregion
}
