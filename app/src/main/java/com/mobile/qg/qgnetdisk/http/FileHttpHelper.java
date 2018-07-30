package com.mobile.qg.qgnetdisk.http;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.qg.qgnetdisk.entity.ClientFile;
import com.mobile.qg.qgnetdisk.entity.NetFile;
import com.mobile.qg.qgnetdisk.entity.User;
import com.mobile.qg.qgnetdisk.util.Parameter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by 11234 on 2018/7/28.
 */
public class FileHttpHelper {

    private static final String TAG = "FileHttpHelper";

    /**
     * 处理POST条目的内容头、尾
     * 包括边界、内容类型、文件名
     */
    private static class Former {

        private final static String BOUNDARY = UUID.randomUUID().toString();
        private final static String HYPHENS = "--";
        private final static String SPLIT = "\r\n";
        private final static String CONTENT_TYPE = "Content-Type: text/plain";

        private final static String ROOT = "name=\"file\"; fileid=*; filepath=*; filename=*";

        String makeHead(NetFile folder, File file) {

            return HYPHENS + BOUNDARY + SPLIT +
                    "Content-Disposition: form-data;" + new Parameter(ROOT).setParameter(String.valueOf(folder.getFileid()), folder.getRealpath(), file.getName()) + SPLIT +
                    CONTENT_TYPE + SPLIT +
                    SPLIT;
        }

        String makeContinue() {
            return SPLIT +
                    HYPHENS + BOUNDARY + SPLIT;
        }

        String makeTail() {
            return SPLIT +
                    HYPHENS + BOUNDARY + HYPHENS + SPLIT;
        }

        String getBoundary() {
            return BOUNDARY;
        }

        private static Former mInstance = new Former();

        private Former() {

        }

        static Former getInstance() {
            return mInstance;
        }

    }

    /**
     * QGNetDisk根目录
     */
    private final static String ROOT = "http://192.168.31.248:8080/qgnetdisk/";

    /**
     * File文件模块
     */
    private final static String FILE = "file/";

    private final static String UPLOAD = "upload";

    private final static String DOWNLOAD = "download?filepath=*";

    private final static String CREATE_FOLDER = "newfolder?filepath=*&fileid=*&filename";

    private final static String DELETE_FILE = "deletefile";
    private final static String DELETE_FILE_POST = "{\n" +
            "    \"user\":文件json,\n" +
            "    \"filepath\":\"文件的路径\",\n" +
            "    \"fileid\": 1//假设为1，这里是整型数据\n" +
            "}";

    private final static String SHOW_FILE_LIST = "listfile?fileid=*";

    private final static String QUERY_FILE = "searchfile?key=*&page=*&type=*";

    /**
     * 读取输入流
     *
     * @param inputStream 输入流
     * @return JSON字段
     * @throws IOException
     */
    public String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
            Log.e(TAG, line);
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    public void upload(NetFile folderFile, File file) {

        try {
            Former former = Former.getInstance();

            HttpURLConnection connection = (HttpURLConnection) (new URL(ROOT + FILE + UPLOAD)).openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + former.getBoundary());

            OutputStream outputStream = connection.getOutputStream();

            //写内容头
            outputStream.write(former.makeHead(folderFile, file).getBytes());
            Log.e(TAG, former.makeHead(folderFile, file));

            //写内容正文
            FileInputStream fileInputStream = new FileInputStream(file);
            int length;
            byte[] buffer = new byte[1024];
            while ((length = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            //写内容尾
            outputStream.write(former.makeTail().getBytes());

            readInputStream(connection.getInputStream());

            System.out.println(connection.getResponseCode());

            outputStream.close();
            fileInputStream.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private final static String mDownloadPath = "/storage/emulated/0/Download/";

    public void download(NetFile netFile) {

        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(ROOT + FILE
                    + new Parameter(DOWNLOAD).setParameter(netFile.getRealpath()))).openConnection();
            connection.setRequestMethod("GET");
            File file = new File(mDownloadPath + netFile.getFilename());
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = connection.getInputStream();

            int length;
            byte[] buffer = new byte[1024];

            while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
                fileOutputStream.write(buffer, 0, length);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<NetFile> JSON2File(JSONObject json) {
        try {
            ArrayList<NetFile> netFiles = new ArrayList<>();
            JSONArray files = json.getJSONObject("data").getJSONArray("files");
            Gson gson = new Gson();
            for (int i = 0; i < files.length(); i++) {
                NetFile file = gson.fromJson(files.getJSONObject(i).toString(), NetFile.class);
                netFiles.add(file);
            }
            return netFiles;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private ArrayList<NetFile> JSON2File(String json) {
        try {
            return JSON2File(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<NetFile> createNewFolder(NetFile folder, String folderName) {
        try {

            HttpURLConnection connection = (HttpURLConnection) (new URL(ROOT + FILE
                    + new Parameter(CREATE_FOLDER).setParameter(
                    folder.getRealpath(),
                    String.valueOf(folder.getFileid()),
                    folderName
            ))).openConnection();
            connection.setRequestMethod("GET");

            ArrayList<NetFile> netFiles = JSON2File(readInputStream(connection.getInputStream()));

            connection.disconnect();
            return netFiles;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<NetFile> deleteFile(User user, NetFile file) {
        ArrayList<NetFile> files = new ArrayList<>();
        try {

            HttpURLConnection connection = (HttpURLConnection) (new URL(ROOT + FILE + DELETE_FILE)).openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");

            JSONObject jsonObject = new JSONObject(DELETE_FILE_POST);
            jsonObject.put("user", new JSONObject(new Gson().toJson(user)));
            jsonObject.put("filepath", file.getRealpath());
            jsonObject.put("fileid", file.getFileid());
            Log.e(TAG, "deleteFile: " + jsonObject.toString());
            Log.e(TAG, "deleteFile: " + jsonObject.getString("filepath"));

            connection.getOutputStream().write(jsonObject.toString().getBytes());
            files = JSON2File(readInputStream(connection.getInputStream()));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return files;
    }

    public ArrayList<NetFile> showFileList(NetFile folder) {
        ArrayList<NetFile> files = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(ROOT + FILE
                    + new Parameter(SHOW_FILE_LIST).setParameter(
                    String.valueOf(folder.getFileid())
            ))).openConnection();
            connection.setRequestMethod("GET");
            files = JSON2File(readInputStream(connection.getInputStream()));
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    public ArrayList<NetFile> query(String key, int page, int type) {
        ArrayList<NetFile> files = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(ROOT + FILE
                    + new Parameter(SHOW_FILE_LIST).setParameter(
                    key,
                    String.valueOf(page),
                    String.valueOf(type)
            ))).openConnection();
            connection.setRequestMethod("GET");
            files = JSON2File(readInputStream(connection.getInputStream()));
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

}
