package com.wide.ui.preferences.bean;

import java.util.List;

import com.google.common.collect.Lists;
import com.wide.ui.preferences.constans.WIDEPreferencesConstans;

public class HostConfigurationSettings {

    private String windchillHost;
    private String hostUser;
    private String hostUserPassword;
    private String windchillHostOS;
    private String windchillAdmin;
    private String windchillAdminPassword;
    private String windchillVersion;
    private String httpServerHome;
    private String windchillHome;
    private String windchillDSHome;

    private List<SettingApplication> children = Lists.newArrayList();
    
    public HostConfigurationSettings() {
    }

    public HostConfigurationSettings(String windchillHost, String hostUser, String hostUserPassword, String windchillHostOS, String windchillAdmin, String windchillAdminPassword, String windchillVersion,
            String httpServerHome, String windchillHome, String windchillDSHome) {
        this.windchillHost = windchillHost;
        this.hostUser = hostUser;
        this.hostUserPassword = hostUserPassword;
        this.windchillHostOS = windchillHostOS;
        this.windchillAdmin = windchillAdmin;
        this.windchillAdminPassword = windchillAdminPassword;
        this.windchillVersion = windchillVersion;
        this.httpServerHome = httpServerHome;
        this.windchillHome = windchillHome;
        this.windchillDSHome = windchillDSHome;
        
        SettingApplication httpServer = new SettingApplication();
        httpServer.setName(WIDEPreferencesConstans.APPLICATION_HTTP_SERVER);
        httpServer.setHomePath(httpServerHome);
        httpServer.setApplication(WIDEPreferencesConstans.APPLICATION_TYPE_APACHE);
        httpServer.setHostIp(windchillHost);
        children.add(httpServer);
        
        SettingApplication windchill = new SettingApplication();
        windchill.setName(WIDEPreferencesConstans.APPLICATION_WINDCHILL_METHOD_SERVER);
        windchill.setHomePath(windchillHome);
        windchill.setApplication(WIDEPreferencesConstans.APPLICATION_TYPE_WINDCHILL);
        windchill.setHostIp(windchillHost);
        children.add(windchill);
        
        SettingApplication windchillDS = new SettingApplication();
        windchillDS.setName(WIDEPreferencesConstans.APPLICATION_WINDCHILLDS);
        windchillDS.setHomePath(windchillDSHome);
        windchillDS.setApplication(WIDEPreferencesConstans.APPLICATION_TYPE_WINDCHILL);
        windchillDS.setHostIp(windchillHost);
        children.add(windchillDS);
        
    }

    public String getWindchillHost() {
        return windchillHost;
    }

    public void setWindchillHost(String windchillHost) {
        this.windchillHost = windchillHost;
    }

    public String getHostUser() {
        return hostUser;
    }

    public void setHostUser(String hostUser) {
        this.hostUser = hostUser;
    }

    public String getHostUserPassword() {
        return hostUserPassword;
    }

    public void setHostUserPassword(String hostUserPassword) {
        this.hostUserPassword = hostUserPassword;
    }

    public String getWindchillHostOS() {
        return windchillHostOS;
    }

    public void setWindchillHostOS(String windchillHostOS) {
        this.windchillHostOS = windchillHostOS;
    }

    public String getWindchillAdmin() {
        return windchillAdmin;
    }

    public void setWindchillAdmin(String windchillAdmin) {
        this.windchillAdmin = windchillAdmin;
    }

    public String getWindchillAdminPassword() {
        return windchillAdminPassword;
    }

    public void setWindchillAdminPassword(String windchillAdminPassword) {
        this.windchillAdminPassword = windchillAdminPassword;
    }

    public String getWindchillVersion() {
        return windchillVersion;
    }

    public void setWindchillVersion(String windchillVersion) {
        this.windchillVersion = windchillVersion;
    }

    public String getHttpServerHome() {
        return httpServerHome;
    }

    public void setHttpServerHome(String httpServerHome) {
        this.httpServerHome = httpServerHome;
    }

    public String getWindchillHome() {
        return windchillHome;
    }

    public void setWindchillHome(String windchillHome) {
        this.windchillHome = windchillHome;
    }

    public String getWindchillDSHome() {
        return windchillDSHome;
    }

    public void setWindchillDSHome(String windchillDSHome) {
        this.windchillDSHome = windchillDSHome;
    }

    public List<SettingApplication> getChildren() {
        return children;
    }

    public void setChildren(List<SettingApplication> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((windchillHost == null) ? 0 : windchillHost.hashCode());
        result = prime * result + ((windchillHostOS == null) ? 0 : windchillHostOS.hashCode());
        result = prime * result + ((windchillVersion == null) ? 0 : windchillVersion.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HostConfigurationSettings other = (HostConfigurationSettings) obj;
        if (windchillHost == null) {
            if (other.windchillHost != null)
                return false;
        } else if (!windchillHost.equals(other.windchillHost))
            return false;
        if (windchillHostOS == null) {
            if (other.windchillHostOS != null)
                return false;
        } else if (!windchillHostOS.equals(other.windchillHostOS))
            return false;
        if (windchillVersion == null) {
            if (other.windchillVersion != null)
                return false;
        } else if (!windchillVersion.equals(other.windchillVersion))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "HostConfigurationBean [windchillHost=" + windchillHost + ", windchillHostOS=" + windchillHostOS + ", windchillVersion=" + windchillVersion + "]";
    }

    public class SettingApplication{
        
        private String name;
        private String homePath;
        private String application;

        private String hostIp;
        
        public SettingApplication() {
        }

        public SettingApplication(String name, String homePath) {
            this.name = name;
            this.homePath = homePath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHomePath() {
            return homePath;
        }

        public void setHomePath(String homePath) {
            this.homePath = homePath;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getHostIp() {
            return hostIp;
        }

        public void setHostIp(String hostIp) {
            this.hostIp = hostIp;
        }

        @Override
        public String toString() {
            return "SettingApplication [name=" + name + ", homePath=" + homePath + "]";
        }

    }
    
}
