package com.wide.ui.views.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wide.ui.preferences.bean.HostConfigurationSettings;
import com.wide.ui.preferences.bean.HostConfigurationSettings.SettingApplication;
import com.wide.ui.preferences.constans.WIDEPreferencesConstans;

public class ApplicationStatusUtil {

    public static void main(String[] args) throws JSchException {
        String hostname = "192.168.197.132";
        String username = "wcadmin";
        String password = "wcadmin";
        //boolean isAuthenticated = getHostConnection(hostname, username, password);
        //System.out.println(isAuthenticated);
        
        boolean isAuthenticated = isHostReachable(hostname);
        System.out.println(isAuthenticated);
        
    }
    
    public static boolean isHostReachable(String host) {
        try {
            return InetAddress.getByName(host).isReachable(1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static boolean getHostConnectionFromHostConfiguration(HostConfigurationSettings setting) throws JSchException {

        String hostname = setting.getWindchillHost();
        String username = setting.getHostUser();
        String password = setting.getHostUserPassword();

        return getHostConnection(hostname, username, password);
    }
    
    private static boolean getHostConnection(String hostname, String username, String password) throws JSchException {
        boolean isAuthenticated = false;
        Session session = null;
        try {
            JSch jSch = new JSch();
            // 建立连接
            session = jSch.getSession(username,hostname);
            session.setPassword(password);
            session.setTimeout(1000);
            
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            
            session.connect();
            isAuthenticated = session.isConnected();
            session.disconnect();
            session = null;
            System.out.println("Connection : " + isAuthenticated);
        } finally {
            System.out.println("finally");
            if(session != null) {
                session.disconnect();
                session = null;
            }
        }
        return isAuthenticated;
    }
    
    public static boolean getHTTPServerStatus(SettingApplication setting) {
        boolean flag = false;
        
        return flag;
    }
    
    public static boolean getWindchillStatus(SettingApplication setting) {
        boolean flag = false;
        
        return flag;
    }
    
    public static boolean getWindchillDSStatus(SettingApplication setting) {
        boolean flag = false;
        
        return flag;
    }

    
    public static void menuStartOperation(SettingApplication application) {
        
        doStartOperation(application);
        
    }
    
    public static void menuReStartOperation(SettingApplication application) {
        
        doReStartOperation(application);
        
    }
    
    public static void menuStopOperation(SettingApplication application) {
        
        doStopOperation(application);
        
    }
    
    public static void doStartOperation(SettingApplication application) {
        System.out.println("Start : " + application.getName());
        
        switch (application.getName()) {
        case WIDEPreferencesConstans.APPLICATION_HTTP_SERVER:
            SSHCommandsUtil.startHTTPServer(application);
            break;
        case WIDEPreferencesConstans.APPLICATION_WINDCHILL_METHOD_SERVER:
            SSHCommandsUtil.startWindchill(application);
            break;
        case WIDEPreferencesConstans.APPLICATION_WINDCHILLDS:
            SSHCommandsUtil.startWindchillDS(application);
            break;
        default:
            break;
        }
        
    }
    
    public static void doReStartOperation(SettingApplication application) {
        System.out.println("ReStart : " + application.getName());
        
        switch (application.getName()) {
        case WIDEPreferencesConstans.APPLICATION_HTTP_SERVER:
            SSHCommandsUtil.reStartHTTPServer(application);
            break;
        case WIDEPreferencesConstans.APPLICATION_WINDCHILL_METHOD_SERVER:
            SSHCommandsUtil.restartWindchill(application);
            break;
        case WIDEPreferencesConstans.APPLICATION_WINDCHILLDS:
            SSHCommandsUtil.reStartWindchillDS(application);
            break;
        default:
            break;
        }
    }
    
    public static void doStopOperation(SettingApplication application) {
        
        System.out.println("Stop : " + application.getName());
        
        switch (application.getName()) {
        case WIDEPreferencesConstans.APPLICATION_HTTP_SERVER:
            SSHCommandsUtil.stopHTTPServer(application);
            break;
        case WIDEPreferencesConstans.APPLICATION_WINDCHILL_METHOD_SERVER:
            SSHCommandsUtil.stopWindchill(application);
            break;
        case WIDEPreferencesConstans.APPLICATION_WINDCHILLDS:
            SSHCommandsUtil.stopWindchillDS(application);
            break;
        default:
            break;
        }
    }
    
}
