package io.huannguyen.swipetodeleterv.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import io.huannguyen.swipetodeleterv.STDRecyclerView;

public class DemoActivity extends AppCompatActivity {

    STDRecyclerView mRecyclerView;
    SampleAdapter mAdapter;
    List<String> versions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        inttViews();
    }

    private void inttViews() {
        mRecyclerView = (STDRecyclerView) findViewById(R.id.recycler_view);
        LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        versions.add("Marshmallow");
        versions.add("Lollipop");
        versions.add("KitKat");
        versions.add("Jelly Bean");
        versions.add("Ice Cream Sandwich");
        versions.add("Gingerbread");
        versions.add("Froyo");
        versions.add("Eclair");
        versions.add("Donut");
        versions.add("Cupcake");

        mAdapter = new SampleAdapter(versions);
        // allow swiping with both directions (left-to-right and right-to-left)
        mRecyclerView.setupSwipeToDelete(mAdapter, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
    }
}
