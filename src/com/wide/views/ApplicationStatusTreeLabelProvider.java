package com.wide.views;

import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.wide.preferences.bean.HostConfigurationSettings;
import com.wide.preferences.bean.HostConfigurationSettings.SettingApplication;
import com.wide.preferences.constans.PreferenceConstans;
import com.wide.views.constans.ApplicationIconsConstans;
import com.wide.views.constans.ApplicationStatusViewConstans;

public class ApplicationStatusTreeLabelProvider extends ColumnLabelProvider {

    private String columnName;

    public ApplicationStatusTreeLabelProvider(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public Image getImage(Object element) {
        Image image = super.getImage(element);

        String imagePath = "";

        switch (getColumnName()) {
        case ApplicationStatusViewConstans.TREE_COLUMN_NAME:
            if (element instanceof HostConfigurationSettings) {
                HostConfigurationSettings setting = (HostConfigurationSettings) element;

                switch (setting.getWindchillHostOS()) {
                case PreferenceConstans.SYSTEM_OS_WINDOWS:
                    imagePath = ApplicationIconsConstans.ICON_OS_WINDOWS_PNG;
                    break;
                case PreferenceConstans.SYSTEM_OS_LINUX:
                    imagePath = ApplicationIconsConstans.ICON_OS_LINUX_PNG;
                    break;
                }
            }
            if (element instanceof SettingApplication) {

                SettingApplication setting = (SettingApplication) element;

                switch (setting.getApplication()) {
                case PreferenceConstans.APPLICATION_TYPE_APACHE:
                    imagePath = ApplicationIconsConstans.ICON_HTTPD_PNG;
                    break;
                case PreferenceConstans.APPLICATION_TYPE_WINDCHILL:
                    imagePath = ApplicationIconsConstans.ICON_WINDCHILL_PNG;
                    break;
                }
            }
            break;
        case ApplicationStatusViewConstans.TREE_COLUMN_STATE:
            if (element instanceof HostConfigurationSettings) {
                HostConfigurationSettings setting = (HostConfigurationSettings) element;

                switch (setting.getWindchillHostOS()) {
                case PreferenceConstans.SYSTEM_OS_WINDOWS:
                    imagePath = ApplicationIconsConstans.ICON_OS_WINDOWS_PNG;
                    break;
                case PreferenceConstans.SYSTEM_OS_LINUX:
                    imagePath = ApplicationIconsConstans.ICON_OS_LINUX_PNG;
                    break;
                }
            }
            if (element instanceof SettingApplication) {

                SettingApplication setting = (SettingApplication) element;

                switch (setting.getApplication()) {
                case PreferenceConstans.APPLICATION_TYPE_APACHE:
                    imagePath = ApplicationIconsConstans.ICON_STATUS_GREEN_PNG;
                    break;
                case PreferenceConstans.APPLICATION_TYPE_WINDCHILL:
                    imagePath = ApplicationIconsConstans.ICON_STATUS_RED_PNG;
                    break;
                }
            }
            break;
        }
        
        

        if (StringUtils.isNotEmpty(imagePath)) {
            Bundle bundle = FrameworkUtil.getBundle(ApplicationStatusView.class);
            URL url = FileLocator.find(bundle, new Path(imagePath));
            LocalResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());
            image = resourceManager.createImage(ImageDescriptor.createFromURL(url));
        }

        return image;
    }

    @Override
    public String getText(Object element) {
        String columnValue = "";

        switch (getColumnName()) {
        case ApplicationStatusViewConstans.TREE_COLUMN_NAME:
            if (element instanceof HostConfigurationSettings) {
                columnValue = ((HostConfigurationSettings) element).getWindchillHost();
            } else if (element instanceof SettingApplication) {
                columnValue = ((SettingApplication) element).getName();
            }
            break;
        case ApplicationStatusViewConstans.TREE_COLUMN_STATE:
            if (element instanceof SettingApplication) {
                columnValue = ((SettingApplication) element).getHomePath();
            }
            break;
        }

        return columnValue;
    }

}
