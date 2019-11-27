package com.wide.ui.views.util;

import com.wide.ui.preferences.bean.HostConfigurationSettings;
import com.wide.ui.preferences.bean.HostConfigurationSettings.SettingApplication;

public class ApplicationStatusUtil {

    public static boolean isHostOpen(HostConfigurationSettings setting) {
        boolean isOpen = false;
        String ip = setting.getWindchillHost();
        
        return isOpen;
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

    
}
