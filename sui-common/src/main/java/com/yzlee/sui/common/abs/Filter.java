package com.yzlee.sui.common.abs;

import com.yzlee.sui.common.modle.ProtocolEntity;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:49
 */
public abstract class Filter {

    public Filter filter;

    public void register(Filter filter) {
        this.filter = filter;
    }

    public abstract void handle(ProtocolEntity entity);

}
