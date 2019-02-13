package com.yzlee.sui.client.modle;

import com.yzlee.sui.common.modle.ProtocolEntity;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:28
 */
public class TreeEntity {

    public enum TYPE {
        ROOT, HOST, DISK, DIRECTORY, FILE,
    }

    private ProtocolEntity.Identity identity;
    private String identityId;
    private String name;
    private long fileSize;
    private String filePath;
    private TYPE type;

    public ProtocolEntity.Identity getIdentity() {
        return identity;
    }

    public void setIdentity(ProtocolEntity.Identity identity) {
        this.identity = identity;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

}
