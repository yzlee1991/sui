package com.yzlee.sui.common.inf;

import com.yzlee.sui.common.modle.nat.MyDiscoveryInfo;
import de.javawi.jstun.test.DiscoveryInfo;

import java.net.InetAddress;

/**
 * @Author: yzlee
 * @Date: 2019/2/15 11:14
 */
public interface NatInf {

    public MyDiscoveryInfo getRemoteCoreInetAddress() throws Exception;

}
