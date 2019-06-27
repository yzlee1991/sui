package com.yzlee.sui.common.utils;

import com.yzlee.sui.common.exception.NatException;
import com.yzlee.sui.common.modle.nat.MyDiscoveryInfo;
import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author: yzlee
 * @Date: 2019/2/14 11:59
 */
public class NatUtils {

    //获取锥形nat网卡ip地址
    public static MyDiscoveryInfo getLocalCoreInetAddress() throws Exception {
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = ifaces.nextElement();
            Enumeration<InetAddress> iaddresses = iface.getInetAddresses();
            while (iaddresses.hasMoreElements()) {
                InetAddress iaddress = iaddresses.nextElement();
                if (Class.forName("java.net.Inet4Address").isInstance(iaddress)) {
                    if ((!iaddress.isLoopbackAddress()) && (!iaddress.isLinkLocalAddress())) {
                        MyDiscovery test = new MyDiscovery(iaddress, 0, "jstun.javawi.de", 3478);
                        MyDiscoveryInfo di = test.test();
                        if (di.isFullCone() || di.isRestrictedCone() || di.isPortRestrictedCone()) {
                            return di;
                        }
                    }
                }
            }
        }
        throw new NatException("本地网络非锥形可穿透nat");
    }

}
