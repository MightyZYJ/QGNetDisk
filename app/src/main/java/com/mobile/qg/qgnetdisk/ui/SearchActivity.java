package com.mobile.qg.qgnetdisk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.adapter.FileAdapter;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.entity.ClientFileFactory;
import com.mobile.qg.qgnetdisk.http.FileHttpHelper;
import com.mobile.qg.qgnetdisk.widget.search.SearchTopView;

import java.io.File;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchTopView.OnTopActListener {

    private static final String TAG = "SearchActivity";
    private RecyclerView mRecyclerView;
    private FileAdapter mAdapter;
    private LinearLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchTopView searchTopView = findViewById(R.id.search_top);
        searchTopView.setTopActListener(this);

        mContent = findViewById(R.id.search_content);
        mRecyclerView = findViewById(R.id.search_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onReturnPressed() {
        onBackPressed();
    }

    @Override
    public void onSearch(final String key) {
        Log.e(TAG, "onSearch: " + key);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ClientFile> files = ClientFileFactory.packFile(new FileHttpHelper().query(key, 1));
                //TODO
                if (mAdapter == null) {
                    mAdapter = new FileAdapter(files);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.refreshList(files);
                        }
                    });
                }
            }
        }).start();

    }
}
