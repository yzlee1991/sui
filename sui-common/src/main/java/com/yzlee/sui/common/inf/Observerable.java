package com.yzlee.sui.common.inf;

import com.yzlee.sui.common.modle.ProtocolEntity;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:56
 */
public interface Observerable {

    //注册
    public void register(Listener listener);

    //注销
    public void remove(Listener listener);

    //通知观察者
    public void notifyListener(ProtocolEntity entity);

}
