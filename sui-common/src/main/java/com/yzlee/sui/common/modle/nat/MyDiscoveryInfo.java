package com.yzlee.sui.common.modle.nat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * @Author: yzlee
 * @Date: 2019/6/27 16:25
 */
public class MyDiscoveryInfo {

    private InetAddress testIP;
    private int testPort = -1;
    private boolean error = false;
    private int errorResponseCode = 0;
    private String errorReason;
    private boolean openAccess = false;
    private boolean blockedUDP = false;
    private boolean fullCone = false;
    private boolean restrictedCone = false;
    private boolean portRestrictedCone = false;
    private boolean symmetric = false;
    private boolean symmetricUDPFirewall = false;
    private InetAddress publicIP;
    private int publicPort = -1;

    public MyDiscoveryInfo(InetAddress testIP) {
        this.testIP = testIP;
    }

    public int getTestPort() {
        return testPort;
    }

    public void setTestPort(int testPort) {
        this.testPort = testPort;
    }

    public boolean isError() {
        return this.error;
    }

    public void setError(int responseCode, String reason) {
        this.error = true;
        this.errorResponseCode = responseCode;
        this.errorReason = reason;
    }

    public boolean isOpenAccess() {
        return this.error ? false : this.openAccess;
    }

    public void setOpenAccess() {
        this.openAccess = true;
    }

    public boolean isBlockedUDP() {
        return this.error ? false : this.blockedUDP;
    }

    public void setBlockedUDP() {
        this.blockedUDP = true;
    }

    public boolean isFullCone() {
        return this.error ? false : this.fullCone;
    }

    public void setFullCone() {
        this.fullCone = true;
    }

    public boolean isPortRestrictedCone() {
        return this.error ? false : this.portRestrictedCone;
    }

    public void setPortRestrictedCone() {
        this.portRestrictedCone = true;
    }

    public boolean isRestrictedCone() {
        return this.error ? false : this.restrictedCone;
    }

    public void setRestrictedCone() {
        this.restrictedCone = true;
    }

    public boolean isSymmetric() {
        return this.error ? false : this.symmetric;
    }

    public void setSymmetric() {
        this.symmetric = true;
    }

    public boolean isSymmetricUDPFirewall() {
        return this.error ? false : this.symmetricUDPFirewall;
    }

    public void setSymmetricUDPFirewall() {
        this.symmetricUDPFirewall = true;
    }

    public InetAddress getPublicIP() {
        return this.publicIP;
    }

    public InetAddress getLocalIP() {
        return this.testIP;
    }

    public void setPublicIP(InetAddress publicIP) {
        this.publicIP = publicIP;
    }

    public int getPublicPort() {
        return this.publicPort;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Network interface: ");

        try {
            sb.append(NetworkInterface.getByInetAddress(this.testIP).getName());
        } catch (SocketException var3) {
            sb.append("unknown");
        }

        sb.append("\n");
        sb.append("Local IP address: ");
        sb.append(this.testIP.getHostAddress());
        sb.append("\n");
        if (this.error) {
            sb.append(this.errorReason + " - Responsecode: " + this.errorResponseCode);
            return sb.toString();
        } else {
            sb.append("Result: ");
            if (this.openAccess) {
                sb.append("Open access to the Internet.\n");
            }

            if (this.blockedUDP) {
                sb.append("Firewall blocks UDP.\n");
            }

            if (this.fullCone) {
                sb.append("Full Cone NAT handles connections.\n");
            }

            if (this.restrictedCone) {
                sb.append("Restricted Cone NAT handles connections.\n");
            }

            if (this.portRestrictedCone) {
                sb.append("Port restricted Cone NAT handles connections.\n");
            }

            if (this.symmetric) {
                sb.append("Symmetric Cone NAT handles connections.\n");
            }

            if (this.symmetricUDPFirewall) {
                sb.append("Symmetric UDP Firewall handles connections.\n");
            }

            if (!this.openAccess && !this.blockedUDP && !this.fullCone && !this.restrictedCone && !this.portRestrictedCone && !this.symmetric && !this.symmetricUDPFirewall) {
                sb.append("unkown\n");
            }

            sb.append("Public IP address: ");
            if (this.publicIP != null) {
                sb.append(this.publicIP.getHostAddress());
            } else {
                sb.append("unknown");
            }

            sb.append("\n");
            return sb.toString();
        }
    }

}
