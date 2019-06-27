package com.yzlee.sui.common.service;

import com.yzlee.sui.common.exception.NatException;
import com.yzlee.sui.common.inf.NatInf;
import com.yzlee.sui.common.modle.nat.MyDiscoveryInfo;
import com.yzlee.sui.common.utils.NatUtils;
import de.javawi.jstun.test.DiscoveryInfo;

import java.net.InetAddress;

/**
 * @Author: yzlee
 * @Date: 2019/2/15 11:14
 */
public class NatService implements NatInf {


    @Override
    public MyDiscoveryInfo getRemoteCoreInetAddress() throws Exception {
        MyDiscoveryInfo di = null;
        try {
            di = NatUtils.getLocalCoreInetAddress();
        } catch (NatException e) {
            //这边仅仅是为了捕获nat异常和转换异常信息而自定义异常类，后面优化看看有没有优雅的解决方案
            throw new NatException("远端网络非锥形可穿透nat");
        }
        return di;
    }
}
