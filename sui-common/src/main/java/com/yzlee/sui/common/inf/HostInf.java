package com.yzlee.sui.common.inf;

import com.yzlee.sui.common.modle.push.HostEntity;

import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:53
 */
public interface HostInf extends Rmiable {

    public List<HostEntity> getOnlineHostEntity();

}
