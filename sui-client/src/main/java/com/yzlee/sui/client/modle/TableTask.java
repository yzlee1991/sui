package com.yzlee.sui.client.modle;

import javafx.concurrent.Task;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:27
 */
public abstract class TableTask extends Task<Void> {

    private String src;

    private String fileName;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
