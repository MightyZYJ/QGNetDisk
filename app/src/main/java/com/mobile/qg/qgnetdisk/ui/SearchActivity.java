package com.mobile.qg.qgnetdisk.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.widget.search.SearchTopView;

public class SearchActivity extends AppCompatActivity implements SearchTopView.OnTopActListener {

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchTopView searchTopView = findViewById(R.id.search_top);
        searchTopView.setTopActListener(this);

    }

    @Override
    public void onReturnPressed() {
        Log.e(TAG, "onReturnPressed: ");
        onBackPressed();
    }

    @Override
    public void onSearch(String key) {
        Log.e(TAG, "onSearch: " + key);
    }
}
