package com.mobile.qg.qgnetdisk;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.entity.ClientUser;
import com.mobile.qg.qgnetdisk.entity.NetFile;
import com.mobile.qg.qgnetdisk.entity.User;
import com.mobile.qg.qgnetdisk.http.FileHttpHelper;
import com.mobile.qg.qgnetdisk.http.UserHttpHelper;
import com.mobile.qg.qgnetdisk.ui.FileListActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ClientUser user = ClientUser.getInstance();

        final UserHttpHelper userHttpHelper = new UserHttpHelper();
        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientUser.reset();
                        int result = userHttpHelper.sendRegisterVerifyCode("1123434219@qq.com");
                    }
                }).start();
            }
        });

        Button button1 = findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientUser.reset();
                        user.setPassword("123456789");
                        user.setEmail("1123434219@qq.com");
                        user.setNickName("张艺隽");
                        EditText text = findViewById(R.id.edit_text);
                        int result = userHttpHelper.register(user, text.getText().toString());
                        Log.e("TAG222", ClientUser.getInstance().toString());
                    }
                }).start();
            }
        });


        findViewById(R.id.butt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientUser.reset();
                        ClientUser user1 = ClientUser.getInstance();
                        user1.setPassword("111111");
                        user1.setEmail("929159338@qq.com");
                        Log.e(TAG, user1.toString());
                        int result = userHttpHelper.login(user1);
                        Log.e(TAG, "run: result" + result);
                        Log.e(TAG, ClientUser.getInstance().toString());
                    }
                }).start();


            }
        });


        findViewById(R.id.butt_modify_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientUser.reset();
                        ClientUser user1 = ClientUser.getInstance();
//                        user1.setUserId(2);
//                        user1.setEmail("1123434219@qq.com");
                        user1.setNickName("张艺隽");
                        user1.setUserId(16);
                        Log.e(TAG, "run: " + userHttpHelper.modifyNickname(user1));
//                        EditText text = findViewById(R.id.edit_text);
//                        user1.setPassword(((EditText) findViewById(R.id.edit_1)).getText().toString());
//                        Log.e(TAG, "run: " + userHttpHelper.resetPassword(user1,text.getText().toString()));
                    }
                }).start();
            }
        });

        findViewById(R.id.butt_file_ac).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FileListActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.butt_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<User> users = userHttpHelper.userList();
                        for (User u : users) {
                            Log.e(TAG, "onClick: " + u.toString());
                        }

                    }
                }).start();
            }
        });

        findViewById(R.id.butt_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        FileHttpHelper fhh = new FileHttpHelper();
                        ClientFile file = new ClientFile();
                        file.setFileid(1);
                        file.setRealpath("QG/");
                        File ioFile = new File("/storage/emulated/0/a/1.png");
                        fhh.upload(file, ioFile);

                    }
                }).start();
            }
        });

        findViewById(R.id.butt_do).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                while (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientFile file = new ClientFile();
                        file.setFilename("123.txt");
                        file.setRealpath("QG/移动组/123.txt");
                        new FileHttpHelper().download(file);
                    }
                }).start();
            }
        });

        final FileHttpHelper fileHttpHelper = new FileHttpHelper();


        findViewById(R.id.button_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ClientUser clientUser = ClientUser.getInstance();
                        setUser();
                        fileHttpHelper.deleteFile(clientUser, setFile(new NetFile()));

                    }
                }).start();
            }
        });

    }

    private void setUser() {
        ClientUser clientUser = ClientUser.getInstance();
        clientUser.setUserId(1);
        clientUser.setNickName("zyj");
        clientUser.setEmail("1123434219@qq.com");
        clientUser.setPassword("666666");
        clientUser.setStatus(1);
    }

    private NetFile setFile(NetFile file) {
        file.setFileid(1);
        file.setRealpath("QG/");
        return file;
    }

}
