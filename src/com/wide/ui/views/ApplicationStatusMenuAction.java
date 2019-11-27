package com.wide.ui.views;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.wide.ui.preferences.bean.HostConfigurationSettings.SettingApplication;
import com.wide.ui.views.constans.ApplicationStatusViewConstans;
import com.wide.ui.views.util.ApplicationStatusUtil;
import com.wide.ui.views.util.SSHCommandsUtil;

public class ApplicationStatusMenuAction extends Action {

    private SettingApplication application;
    private Composite parent;
    
    protected ApplicationStatusMenuAction() {
    }

    protected ApplicationStatusMenuAction(String text) {
        setText(text);
    }

    protected ApplicationStatusMenuAction(String text, ImageDescriptor image) {
        this(text);
        setImageDescriptor(image);
    }

    protected ApplicationStatusMenuAction(String text, String imagePath, SettingApplication application,Composite parent) {
        
        Bundle bundle = FrameworkUtil.getBundle(ApplicationStatusMenuAction.class);
        URL url = FileLocator.find(bundle, new Path(imagePath));
        
        setText(text);
        setImageDescriptor(ImageDescriptor.createFromURL(url));
        setApplication(application);
        setParent(parent);
    }

    public SettingApplication getApplication() {
        return application;
    }

    public void setApplication(SettingApplication application) {
        this.application = application;
    }

    public Composite getParent() {
        return parent;
    }

    public void setParent(Composite parent) {
        this.parent = parent;
    }

    @Override
    public void run() {
        System.out.println("getParent : " + getParent());
        Composite composite = getParent();
        
        String message = SSHCommandsUtil.getConnectionMessage(getApplication());
        if(StringUtils.isEmpty(message)) {
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
        }else {
            MessageDialog.openError(composite.getShell(), "Error", message);
        }
    }
}
