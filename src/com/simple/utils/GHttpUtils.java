package com.simple.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author chenxi
 */
public class GHttpUtils {
    /**
     * Get user real ip.
     * @param request
     * @return 
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null) {  
            if (ip.indexOf(',') == -1) { 
                return ip;  
            }
            return ip.split(",")[0];  
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * Get mac address by ip.
     * @param ip
     * @return 
     */
    public String getMACAddress(String ip) {  
        String str = "";  
        String macAddress = "";  
        try {  
            Process p = Runtime.getRuntime().exec("netstat -A " + ip);  
            InputStreamReader ir = new InputStreamReader(p.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
            for (int i = 1; i < 100; i++) {  
                str = input.readLine();  
                if (str != null) {  
                    if (str.indexOf("MAC Address") > 1) {  
                        macAddress = str.substring(  
                                str.indexOf("MAC Address") + 14, str.length());  
                        break;  
                    }  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace(System.out);  
        }  
        return macAddress;  
    } 
}