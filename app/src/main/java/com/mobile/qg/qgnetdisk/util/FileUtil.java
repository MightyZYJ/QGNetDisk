package com.mobile.qg.qgnetdisk.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 11234 on 2018/7/24.
 */
public class FileUtil {

    /**
     * 获得文件的大小：
     * 3,137byte --> 3.06KB
     *
     * @param fileLength 长整型的文件大小
     * @return 字符串型的文件大小
     */
    public static String transformLength(long fileLength) {

        final int GB = 1024 * 1024 * 1024;
        final int MB = 1024 * 1024;
        final int KB = 1024;
        String result;
        DecimalFormat format = new DecimalFormat("###.00");
        if (fileLength >= GB) {
            result = format.format(fileLength / (float) GB) + "GB";
        } else if (fileLength >= MB) {
            result = format.format(fileLength / (float) MB) + "MB";
        } else if (fileLength >= KB) {
            result = format.format(fileLength / (float) KB) + "KB";
        } else if (fileLength > 0) {
            result = (int) fileLength + "B";
        } else {
            result = "";//文件夹
        }
        return result;
    }

    /**
     * 获得文件的时间：
     * 1532399837000 -->
     *
     * @param time 长整型时间
     * @return 字符串型时间
     */
    public static String transformTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date(time));
    }

}
