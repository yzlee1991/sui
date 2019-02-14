package com.yzlee.sui.client.enums;

/**
 * @Author: yzlee
 * @Date: 2019/2/14 10:47
 */
public enum MenuItemEnum {

    CONNECT_HOST(1,"链接主机"),CONNECT_DISK(2,"链接硬盘"),DOWNLOAD(3,"下载"),REMOTE_SREEN(4,"远程屏幕");

    public int VAL;

    public String NAME;

    private MenuItemEnum(int VAL, String NAME) {
        this.VAL = VAL;
        this.NAME = NAME;
    }

}
