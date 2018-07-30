package com.mobile.qg.qgnetdisk.entity;

import com.mobile.qg.qgnetdisk.R;

import java.util.ArrayList;

/**
 * Created by 11234 on 2018/7/24.
 */
public class ClientFileFactory {

    //QG id = 1
    private final static int offset = 1;

    /**
     * 0.前端
     * 1.后台
     * 2.嵌入式
     * 3.移动
     * 4.数据挖掘
     * 5.设计师
     * 6.手游
     */
    private final static int[] GROUP_ICON_IDS = {
            R.drawable.icon_file_frontend,
            R.drawable.icon_file_backgroundnetwork,
            R.drawable.icon_file_flushbonading,
            R.drawable.icon_file_mobile,
            R.drawable.icon_file_datamining,
            R.drawable.icon_file_designer,
            R.drawable.icon_file_mobilegame
    };

    /**
     * 0.未知类型
     * 1.文件夹
     * 2.doc、docx、wps文件
     * 3.jpg、png、bmp、gif图片
     * 4.mp3,wma,flac、wav音乐文件
     * 5.pdf文件
     * 6.ppt幻灯片文件
     * 7.txt、html、java文本文档
     * 8.avi、mp4、mov视频文件
     * 9.xls、xlsx表格文件
     * 10.zip、7z压缩文件
     */
    private final static int[] FILE_ICON_IDS = {
            R.drawable.icon_unknown,
            R.drawable.icon_file,
            R.drawable.icon_doc,
            R.drawable.icon_image,
            R.drawable.icon_mp4,
            R.drawable.icon_pdf,
            R.drawable.icon_ppt,
            R.drawable.icon_txt,
            R.drawable.icon_video,
            R.drawable.icon_xls,
            R.drawable.icon_zip
    };


    public static ArrayList<ClientFile> packFile(ArrayList<NetFile> netFiles) {
        ArrayList<ClientFile> clientFiles = new ArrayList<>();
        for (NetFile file : netFiles) {
            int id = file.getFileid();
            ClientFile clientFile = (ClientFile) file.clone();
            if (id > 0 && id < 8) {
                clientFile.setIconId(GROUP_ICON_IDS[id - offset]);
            } else {
                String fileName = clientFile.getFilename();
                if (fileName.contains(",")) {
                    clientFile.setIconId(FILE_ICON_IDS[1]);
                } else {
                    String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    int position;
                    switch (type) {
                        case "doc":
                        case "wps":
                        case "docx":
                            position = 2;
                            break;
                        case "jpg":
                        case "bmp":
                        case "gif":
                        case "png":
                            position = 3;
                            break;
                        case "mp3":
                        case "wma":
                        case "flac":
                        case "wav":
                            position = 4;
                            break;
                        case "pdf":
                            position = 5;
                            break;
                        case "ppt":
                        case "pptx":
                            position = 6;
                            break;
                        case "txt":
                        case "c":
                        case "html":
                        case "java":
                            position = 7;
                            break;
                        case "avi":
                        case "mp4":
                        case "mov":
                            position = 8;
                            break;
                        case "xls":
                        case "xlsx":
                            position = 9;
                            break;
                        case "7z":
                        case "zip":
                            position = 10;
                            break;
                        default:
                            position = 0;
                            break;
                    }
                    clientFile.setIconId(FILE_ICON_IDS[position]);
                }
            }
            clientFiles.add(clientFile);
        }
        return clientFiles;
    }
}
