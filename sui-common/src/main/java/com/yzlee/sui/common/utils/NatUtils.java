package com.yzlee.sui.common.utils;

import de.javawi.jstun.test.DiscoveryInfo;
import de.javawi.jstun.test.DiscoveryTest;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author: yzlee
 * @Date: 2019/2/14 11:59
 */
public class NatUtils {

    //获取锥形nat网卡ip地址
    public static InetAddress getLocalCoreInetAddress() throws Exception {
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = ifaces.nextElement();
            Enumeration<InetAddress> iaddresses = iface.getInetAddresses();
            while (iaddresses.hasMoreElements()) {
                InetAddress iaddress = iaddresses.nextElement();
                if (Class.forName("java.net.Inet4Address").isInstance(iaddress)) {
                    if ((!iaddress.isLoopbackAddress()) && (!iaddress.isLinkLocalAddress())) {
                        DiscoveryTest test = new DiscoveryTest(iaddress, 0, "jstun.javawi.de", 3478);
                        DiscoveryInfo di = test.test();
                        if (di.isFullCone() || di.isRestrictedCone() || di.isPortRestrictedCone()) {
                            return iaddress;
                        }
                    }
                }
            }
        }
        throw new RuntimeException("本地网络非锥形可穿透nat");
    }

}
