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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mobile.qg.qgnetdisk.R;
import com.mobile.qg.qgnetdisk.adapter.BottomMenuAdapter;
import com.mobile.qg.qgnetdisk.adapter.FileAdapter;
import com.mobile.qg.qgnetdisk.adapter.GroupAdapter;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.entity.ClientFileFactory;
import com.mobile.qg.qgnetdisk.entity.ClientUser;
import com.mobile.qg.qgnetdisk.entity.NetFile;
import com.mobile.qg.qgnetdisk.fragment.BottomPopFragment;
import com.mobile.qg.qgnetdisk.http.FileHttpHelper;
import com.mobile.qg.qgnetdisk.util.ToastUtil;
import com.mobile.qg.qgnetdisk.widget.NavHeadView;
import com.mobile.qg.qgnetdisk.widget.TitleView;

import java.util.ArrayList;

public class FileListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FileAdapter.OnItemFileClickListener,
        FileAdapter.OnMultiSelectionChangeListener,
        TitleView.OnTiTleOperationListener {

    private static final String TAG = "FileListActivity";

    private RecyclerView mFileRecyclerView;
    private FileAdapter mFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TitleView titleView = findViewById(R.id.title_view);
        titleView.setOnTitleOperationListener(this);

        ClientUser user = ClientUser.getInstance();
        titleView.setHead(user.getNickName());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavHeadView navHeadView = navigationView.getHeaderView(0).findViewById(R.id.nav_head_view);
        navHeadView.initNav(user);

        mFileRecyclerView = findViewById(R.id.content_file_recycler);
        mFileRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        RecyclerView recent = findViewById(R.id.content_recent_recycler);
//        LinearLayoutManager manager = new LinearLayoutManager(this);
//        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recent.setLayoutManager(manager);
//        GroupAdapter adapter = new GroupAdapter(files2);
//        recent.setAdapter(adapter);

        requestFileList();

    }

    private void requestFileList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetFile QG = new NetFile();
                QG.setFileid(1);
                final ArrayList<ClientFile> files = ClientFileFactory.packFile(new FileHttpHelper().showFileList(QG));
                files.remove(0);

                if (mFileAdapter == null) {
                    Log.e(TAG, "run: 1");
                    mFileAdapter = new FileAdapter(files);
                    mFileAdapter.setOnSelectionChangeListener(FileListActivity.this);
                    mFileAdapter.setOnFileClickListener(FileListActivity.this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFileRecyclerView.setAdapter(mFileAdapter);
                        }
                    });
                } else {
                    Log.e(TAG, "run: 1");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFileAdapter.refreshList(files);
                        }
                    });
                }
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (item.getItemId()) {
            case R.id.menu_permission:
                break;
            case R.id.menu_message:
                break;
            case R.id.menu_search:
                startActivity(new Intent(FileListActivity.this, SearchActivity.class));
                break;
            case R.id.menu_setting:
                break;
            case R.id.menu_quit:
                break;
        }
        return true;
    }

    @Override
    public void OnFileItemClick(ClientFile clientFile) {
        Log.e(TAG, "OnFileItemClick: " + clientFile.toString());
        if (clientFile.getFilename().contains(".")) {
            ToastUtil.CenterToast(FileListActivity.this, "文件预览功能正在开发中。。。");
        } else {
            Intent intent = new Intent(FileListActivity.this, FolderActivity.class);
            intent.putExtra("file", clientFile);
            startActivity(intent);
        }
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

    @Override
    public void onBack() {

    }

    @Override
    public void onDownload() {
        Intent intent = new Intent(FileListActivity.this, DownloadAndUploadActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearch() {
        Intent intent = new Intent(FileListActivity.this, SearchActivity.class);
        startActivity(intent);
    }

}
