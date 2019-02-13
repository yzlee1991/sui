package com.yzlee.sui.common.modle.push;

import com.yzlee.sui.common.modle.ProtocolEntity;

import java.io.Serializable;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:54
 */
public class HostEntity implements Serializable {

    private String identityId;

    private ProtocolEntity.Identity identity;

    private String name;

    private long flushTime;

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public ProtocolEntity.Identity getIdentity() {
        return identity;
    }

    public void setIdentity(ProtocolEntity.Identity identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFlushTime() {
        return flushTime;
    }

    public void setFlushTime(long flushTime) {
        this.flushTime = flushTime;
    }

    @Override
    public String toString() {
        return "HostEntity [identityId=" + identityId + ", identity=" + identity + ", name=" + name + ", flushTime="
                + flushTime + "]";
    }

}
