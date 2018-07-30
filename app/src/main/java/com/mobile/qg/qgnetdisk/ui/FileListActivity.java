package com.mobile.qg.qgnetdisk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.adapter.BottomMenuAdapter;
import com.mobile.qg.qgnetdisk.adapter.FileAdapter;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.entity.ClientFileFactory;
import com.mobile.qg.qgnetdisk.entity.NetFile;
import com.mobile.qg.qgnetdisk.fragment.BottomPopFragment;
import com.mobile.qg.qgnetdisk.http.FileHttpHelper;

import java.util.ArrayList;

public class FileListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FileAdapter.OnItemFileClickListener, FileAdapter.OnMultiSelectionChangeListener {
    private static final String TAG = "FileListActivity";

    private RecyclerView mFileRecyclerView;
    private FileAdapter mFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList<NetFile> files1 = new ArrayList<>();
        for (int i = 1; i < 15; i++) {
            files1.add(new NetFile(i, "file", "2018-7-28", 123456789L));
        }
        ArrayList<ClientFile> files2 = ClientFileFactory.packFile(files1);

        mFileRecyclerView = findViewById(R.id.content_file_recycler);
        mFileRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        requstFileList();

        findViewById(R.id.search_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileListActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void requstFileList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetFile QG = new NetFile();
                QG.setFileid(1);
                ArrayList<ClientFile> files = ClientFileFactory.packFile(new FileHttpHelper().showFileList(QG));
                mFileAdapter = new FileAdapter(files);
                mFileAdapter.setOnFileClickListener(FileListActivity.this);
                mFileAdapter.setOnSelectionChangeListener(FileListActivity.this);
                mFileRecyclerView.setAdapter(mFileAdapter);
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mFileAdapter.selectionMode() == FileAdapter.MULTI_SELECTION) {
            mFileAdapter.close();
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void OnFileItemClick(ClientFile clientFile) {
        Log.e(TAG, "OnFileItemClick: " + clientFile.getFilename());
    }

    /**
     * 监听到列表中的“更多操作”按钮
     * 打开底部Fragment
     * 监听底部Fragment的点击事件
     * 连接网络层，发送：
     * 0.下载
     * 1.移动
     * 2.复制
     * 3.重命名
     * 4.删除
     *
     * @param clientFile 操作的对象文件
     */
    @Override
    public void OnRequestBottomMenu(ClientFile clientFile) {
        new BottomPopFragment().setListener(new BottomPopFragment.OnBottomItemClickListener() {
            @Override
            public void onItemClick(int action) {
                Log.e(TAG, "onItemClick: " + action);
                switch (action) {
                    case BottomMenuAdapter.DOWNLOAD:
                        break;
                    case BottomMenuAdapter.MOVE:
                        break;
                    case BottomMenuAdapter.COPY:
                        break;
                    case BottomMenuAdapter.RENAME:
                        break;
                    case BottomMenuAdapter.DELETE:
                        break;
                }
            }
        }).show(getSupportFragmentManager(), "Operation Bottom Pop Fragment Menu");
    }

    @Override
    public void OnMultiSelectionOpen() {
        Log.e(TAG, "OnMultiSelectionOpen: ");
    }

    @Override
    public void OnMultiSelectionClose() {
        Log.e(TAG, "OnMultiSelectionClose: ");
    }
}
