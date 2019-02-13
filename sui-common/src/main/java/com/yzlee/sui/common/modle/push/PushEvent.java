package com.yzlee.sui.common.modle.push;

import java.io.Serializable;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:55
 */
public class PushEvent implements Serializable {

    // 对应推送的json数据
    protected String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
