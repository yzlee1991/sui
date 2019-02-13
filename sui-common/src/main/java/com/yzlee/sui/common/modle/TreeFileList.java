package com.yzlee.sui.common.modle;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:50
 */
public class TreeFileList implements Serializable {

    private String fileName;
    private long fileSize;
    private String filePath;
    private long modifyTime;
    private boolean isDirectory;

    private List<TreeFileList> list;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public List<TreeFileList> getList() {
        return list;
    }

    public void setList(List<TreeFileList> list) {
        this.list = list;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

}
