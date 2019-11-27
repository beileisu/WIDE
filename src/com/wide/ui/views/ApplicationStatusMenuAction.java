package com.wide.ui.views;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.wide.ui.preferences.bean.HostConfigurationSettings.SettingApplication;
import com.wide.ui.views.constans.ApplicationStatusViewConstans;
import com.wide.ui.views.util.ApplicationStatusUtil;

public class ApplicationStatusMenuAction extends Action {

    private SettingApplication application;
    
    protected ApplicationStatusMenuAction() {
    }

    protected ApplicationStatusMenuAction(String text) {
        this();
        setText(text);
    }

    protected ApplicationStatusMenuAction(String text, ImageDescriptor image) {
        this(text);
        setImageDescriptor(image);
    }

    protected ApplicationStatusMenuAction(String text, String imagePath, SettingApplication application) {
        this();
        
        Bundle bundle = FrameworkUtil.getBundle(ApplicationStatusMenuAction.class);
        URL url = FileLocator.find(bundle, new Path(imagePath));
        
        setText(text);
        setImageDescriptor(ImageDescriptor.createFromURL(url));
        setApplication(application);
    }

    public SettingApplication getApplication() {
        return application;
    }

    public void setApplication(SettingApplication application) {
        this.application = application;
    }

    @Override
    public void run() {
        switch (getText()) {
        case ApplicationStatusViewConstans.MENU_START:
            ApplicationStatusUtil.menuStartOperation(getApplication());
            break;
        case ApplicationStatusViewConstans.MENU_RESTART:
            ApplicationStatusUtil.menuReStartOperation(getApplication());
            break;
        case ApplicationStatusViewConstans.MENU_STOP:
            ApplicationStatusUtil.menuStopOperation(getApplication());
            break;
        default:
            System.out.println("Do Nothing");
            break;
        }
    }

}
