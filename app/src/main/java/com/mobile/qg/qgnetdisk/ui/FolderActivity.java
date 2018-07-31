package com.mobile.qg.qgnetdisk.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.adapter.BottomMenuAdapter;
import com.mobile.qg.qgnetdisk.adapter.FileAdapter;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.entity.ClientFileFactory;
import com.mobile.qg.qgnetdisk.entity.ClientUser;
import com.mobile.qg.qgnetdisk.entity.NetFile;
import com.mobile.qg.qgnetdisk.fragment.BottomPopFragment;
import com.mobile.qg.qgnetdisk.http.FileHttpHelper;
import com.mobile.qg.qgnetdisk.widget.TitleView;

import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity implements
        FileAdapter.OnMultiSelectionChangeListener,
        FileAdapter.OnItemFileClickListener,
        TitleView.OnTiTleOperationListener,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private ClientFile mFile;
    private FileAdapter mFileAdapter;
    private RecyclerView mFileRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        TitleView titleView = findViewById(R.id.folder_title);
        titleView.setOnTitleOperationListener(this);

        Intent intent = getIntent();
        mFile = (ClientFile) intent.getSerializableExtra("file");

        if (mFile == null) {
            finish();
        }

        Log.e(TAG, "onCreate: " + mFile.toString());
        titleView.setTitle(mFile.getFilename());

        mFileRecyclerView = findViewById(R.id.folder_recycler);
        mFileRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSwipeRefreshLayout = findViewById(R.id.folder_swipe);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        requestFileList();
    }

    private void requestFileList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ClientFile> files = ClientFileFactory.packFile(new FileHttpHelper().showFileList(mFile));

                if (mFileAdapter == null) {
                    mFileAdapter = new FileAdapter(files);
                    mFileAdapter.setOnSelectionChangeListener(FolderActivity.this);
                    mFileAdapter.setOnFileClickListener(FolderActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFileRecyclerView.setAdapter(mFileAdapter);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFileAdapter.refreshList(files);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        if (mFileAdapter.selectionMode() == FileAdapter.MULTI_SELECTION) {
            mFileAdapter.close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void OnMultiSelectionOpen() {

    }

    @Override
    public void OnMultiSelectionClose() {

    }

    private static final String TAG = "FolderActivity";

    @Override
    public void OnFileItemClick(ClientFile clientFile) {
        Log.e(TAG, "OnFileItemClick: " + clientFile.getFilename());
        if (!clientFile.getFilename().contains(".")) {
            Intent intent = new Intent(FolderActivity.this, FolderActivity.class);
            intent.putExtra("file", clientFile);
            startActivity(intent);
        } else {
            Toast.makeText(this, "文件预览功能正在开发", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnRequestBottomMenu(final ClientFile clientFile) {
        new BottomPopFragment().setListener(new BottomPopFragment.OnBottomItemClickListener() {
            @Override
            public void onItemClick(int action) {
                Log.e(TAG, "onItemClick: " + action);
                switch (action) {
                    case BottomMenuAdapter.DOWNLOAD:
                        //TODO
                        //new FileHttpHelper().download(clientFile);
                        break;
                    case BottomMenuAdapter.MOVE:
                        break;
                    case BottomMenuAdapter.COPY:
                        break;
                    case BottomMenuAdapter.RENAME:
                        break;
                    case BottomMenuAdapter.DELETE:
                        //TODO
                        mFileAdapter.refreshList(ClientFileFactory.packFile(new FileHttpHelper().deleteFile(ClientUser.getInstance(), clientFile)));
                        break;
                }
            }
        }).show(getSupportFragmentManager(), "Operation Bottom Pop Fragment Menu");
    }

    @Override
    public void onBack() {
        finish();
    }

    @Override
    public void onDownload() {
        Intent intent = new Intent(FolderActivity.this, DownloadAndUploadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearch() {
        Intent intent = new Intent(FolderActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_sort:
                Log.e(TAG, "onClick: sort");
                break;
            case R.id.action_add:
                Log.e(TAG, "onClick: add");
                break;
            case R.id.action_detail:
                Log.e(TAG, "onClick: detail");
                break;
        }
    }

    @Override
    public void onRefresh() {
        requestFileList();
    }
}
