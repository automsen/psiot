package cn.com.tw.common.utils.tools.ip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取IP
 * @author admin
 *
 */
public class IpAddrUtils {
	
	public static String getLocalIp() throws SocketException {
		String localIp = null;// 本地IP，如果没有配置外网IP则返回它  
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();  
        InetAddress ip = null;  
        boolean finded = false;// 是否找到外网IP  
        while (netInterfaces.hasMoreElements() && !finded) {  
            NetworkInterface ni = netInterfaces.nextElement();  
            Enumeration<InetAddress> address = ni.getInetAddresses();  
            while (address.hasMoreElements()) {  
                ip = address.nextElement();  
                if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 内网IP  
                	localIp = ip.getHostAddress();
                }  
            }  
        }  
        
        return localIp;
        
	}
	
	public static String getNetIp() throws SocketException {
        String netip = null;// 外网IP  
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();  
        InetAddress ip = null;  
        boolean finded = false;// 是否找到外网IP  
        while (netInterfaces.hasMoreElements() && !finded) {  
            NetworkInterface ni = netInterfaces.nextElement();  
            Enumeration<InetAddress> address = ni.getInetAddresses();  
            while (address.hasMoreElements()) {  
                ip = address.nextElement();
                //外网IP
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    netip = ip.getHostAddress();
                    return netip;
                } 
            }  
        }  
        
        return null;
	}
}
