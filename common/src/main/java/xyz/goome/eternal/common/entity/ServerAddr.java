package xyz.goome.eternal.common.entity;

/**
 * 服务器地址
 * Created by matrix on 16/4/12.
 */
public class ServerAddr {

    private String ip;
    private int port;

    public ServerAddr(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean same = false;
        if(obj != null && obj instanceof ServerAddr) {
            ServerAddr addr = (ServerAddr) obj;
            same = this.ip.equals(addr.getIp()) && this.port == addr.getPort();
        }
        return same;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
