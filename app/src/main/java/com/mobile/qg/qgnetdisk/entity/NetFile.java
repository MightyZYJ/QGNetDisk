package com.mobile.qg.qgnetdisk.entity;

/**
 * 用于传输的基本File类
 * Created by 11234 on 2018/7/27.
 */
public class NetFile implements Cloneable {

    public NetFile() {

    }

    public NetFile(int fileid, String filename, String modifytime, long filesize) {
        this.fileid = fileid;
        this.filename = filename;
        this.modifytime = modifytime;
        this.filesize = filesize;
    }

    /**
     * 文件id，由上传时服务器提供
     */
    private int fileid;

    /**
     * 文件名，上传时客户端提供
     */
    private String filename;

    /**
     * 父目录id，服务器提供。用于上传文件
     */
    private int fatherid;

    /**
     * 上传者id
     */
    private int userid;

    /**
     * 上传者用户名
     */
    private String username;

    /**
     * 修改时间
     */
    private String modifytime;

    /**
     * 下载次数
     */
    private int downloadtimes;

    /**
     * 文件大小
     */
    private long filesize;

    /**
     * 服务器提供
     * 文件路径
     */
    private String realpath;

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getFatherid() {
        return fatherid;
    }

    public void setFatherid(int fatherid) {
        this.fatherid = fatherid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getModifytime() {
        return modifytime;
    }

    public void setModifytime(String modifytime) {
        this.modifytime = modifytime;
    }

    public int getDownloadtimes() {
        return downloadtimes;
    }

    public void setDownloadtimes(int downloadtimes) {
        this.downloadtimes = downloadtimes;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getRealpath() {
        return realpath;
    }

    public void setRealpath(String realpath) {
        this.realpath = realpath;
    }

    /**
     * 将NetFile克隆
     *
     * @return ClientFile对象
     */
    @Override
    protected Object clone() {
        ClientFile file = new ClientFile();
        file.setDownloadtimes(downloadtimes);
        file.setFatherid(fatherid);
        file.setFileid(fileid);
        file.setFilename(filename);
        file.setFilesize(filesize);
        file.setModifytime(modifytime);
        file.setUserid(userid);
        file.setUsername(username);
        file.setRealpath(realpath);
        return file;
    }
}
