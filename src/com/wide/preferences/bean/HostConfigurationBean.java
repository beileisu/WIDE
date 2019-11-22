package com.wide.preferences.bean;

public class HostConfigurationBean {

    private String windchillHost;
    private String hostUser;
    private String hostUserPassword;
    private String windchillHostOS;
    private String windchillAdmin;
    private String windchillAdminPassword;
    private String windchillVersion;
    private String HttpServerHome;
    private String windchillHome;
    private String windchillDSHome;

    public HostConfigurationBean() {
    }

    public HostConfigurationBean(String windchillHost, String hostUser, String hostUserPassword, String windchillHostOS, String windchillAdmin, String windchillAdminPassword, String windchillVersion,
            String httpServerHome, String windchillHome, String windchillDSHome) {
        this.windchillHost = windchillHost;
        this.hostUser = hostUser;
        this.hostUserPassword = hostUserPassword;
        this.windchillHostOS = windchillHostOS;
        this.windchillAdmin = windchillAdmin;
        this.windchillAdminPassword = windchillAdminPassword;
        this.windchillVersion = windchillVersion;
        HttpServerHome = httpServerHome;
        this.windchillHome = windchillHome;
        this.windchillDSHome = windchillDSHome;
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
        return HttpServerHome;
    }

    public void setHttpServerHome(String httpServerHome) {
        HttpServerHome = httpServerHome;
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

    @Override
    public String toString() {
        return "HostConfigurationBean [windchillHost=" + windchillHost + ", windchillHostOS=" + windchillHostOS + ", windchillVersion=" + windchillVersion + "]";
    }

}
