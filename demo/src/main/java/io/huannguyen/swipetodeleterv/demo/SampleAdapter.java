package io.huannguyen.swipetodeleterv.demo;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.huannguyen.swipetodeleterv.STDAdapter;

/**
 * Created by huannguyen on 4/06/2016.
 */

public class SampleAdapter extends STDAdapter<String> {

    public SampleAdapter(List<String> versionList) {
        super(versionList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((SampleViewHolder)holder).initData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems == null? 0: mItems.size();
    }
}
