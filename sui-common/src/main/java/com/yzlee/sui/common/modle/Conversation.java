package com.yzlee.sui.common.modle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:49
 */
public class Conversation {

    public static class Data {

        private String lock;

        private ProtocolEntity entity;

        public String getLock() {
            return lock;
        }

        public void setLock(String lock) {
            this.lock = lock;
        }

        public ProtocolEntity getEntity() {
            return entity;
        }

        public void setEntity(ProtocolEntity entity) {
            this.entity = entity;
        }

    }

    public static ConcurrentMap<String, Data> MAP = new ConcurrentHashMap<String, Data>();

    public static long REQUESTTIMEOUT = 50000;

}
