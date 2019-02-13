package com.yzlee.sui.client.listener;

import com.yzlee.sui.common.modle.push.PushEvent;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:22
 */
public interface Listener {

    public void action(PushEvent event);

}
