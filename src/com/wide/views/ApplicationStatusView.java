package com.wide.views;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.wide.preferences.WIDEPreferences;
import com.wide.preferences.bean.HostConfigurationSettings;
import com.wide.preferences.bean.HostConfigurationSettings.SettingApplication;
import com.wide.ui.WIDEUIPlugin;
import com.wide.views.constans.ApplicationIconsConstans;
import com.wide.views.constans.ApplicationStatusViewConstans;

public class ApplicationStatusView extends ViewPart {

    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "com.wide.views.ApplicationStatusView";

    public static final String[] TREE_COLS = new String[] { "Name", "State" };

    protected static final ColumnLayoutData[] TREE_COLUMN_LAYOUTS = { new ColumnWeightData(100) };

    @Inject
    IWorkbench workbench;

    private TreeViewer viewer;

    @Override
    public void createPartControl(Composite parent) {
        viewer = new TreeViewer(parent, 65540);
        viewer.getTree().setHeaderVisible(true);
        viewer.getTree().setLinesVisible(true);

        TreeViewerColumn nameColumn = new TreeViewerColumn(viewer, SWT.NONE);
        nameColumn.getColumn().setWidth(300);
        nameColumn.getColumn().setText(ApplicationStatusViewConstans.TREE_COLUMN_NAME);
        nameColumn.setLabelProvider(new ApplicationStatusTreeLabelProvider(ApplicationStatusViewConstans.TREE_COLUMN_NAME));
        
        TreeViewerColumn stateColumn = new TreeViewerColumn(viewer, SWT.NONE);
        stateColumn.getColumn().setWidth(300);
        stateColumn.getColumn().setText(ApplicationStatusViewConstans.TREE_COLUMN_STATE);
        stateColumn.setLabelProvider(new ApplicationStatusTreeLabelProvider(ApplicationStatusViewConstans.TREE_COLUMN_STATE));
        
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object selectedObject = getSingleSelection(event.getStructuredSelection());
                if(selectedObject instanceof SettingApplication) {
                    SettingApplication application = (SettingApplication) selectedObject;
                    createContextMenu(viewer, application);
                }
            }
        });
        
        viewer.setContentProvider(new ApplicationStatusContentProvider());
        
        IPreferenceStore store = WIDEUIPlugin.getDefault().getPreferenceStore();
        viewer.setInput(WIDEPreferences.getHostConfigurationSettings(store));
        
    }

    
    /**
     * Creates the context menu
     *
     * @param viewer
     */
    protected void createContextMenu(Viewer viewer, SettingApplication application) {
        MenuManager contextMenu = new MenuManager("#ViewerMenu"); //$NON-NLS-1$
        contextMenu.setRemoveAllWhenShown(true);
        contextMenu.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr, application);
            }
        });

        Menu menu = contextMenu.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }
    
    /**
     * Fill dynamic context menu
     *
     * @param contextMenu
     */
    protected void fillContextMenu(IMenuManager contextMenu, SettingApplication application) {
        contextMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        contextMenu.add(new ApplicationStatusMenuAction(
                ApplicationStatusViewConstans.MENU_START,
                ApplicationIconsConstans.ICON_START_GIF,
                application));
        
        contextMenu.add(new ApplicationStatusMenuAction(
                ApplicationStatusViewConstans.MENU_RESTART,
                ApplicationIconsConstans.ICON_RESTART_PNG,
                application));
        
        contextMenu.add(new ApplicationStatusMenuAction(
                ApplicationStatusViewConstans.MENU_STOP,
                ApplicationIconsConstans.ICON_STOP_GIF,
                application));
        
    }
    
    protected Object getSingleSelection(IStructuredSelection selection) {
        return selection.size() == 1 ? selection.getFirstElement() : null;
    }
    
    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    class ApplicationStatusContentProvider implements ITreeContentProvider {

        @Override
        public Object[] getElements(Object inputElement) {
            IPreferenceStore store = WIDEUIPlugin.getDefault().getPreferenceStore();
            List<HostConfigurationSettings> settings = WIDEPreferences.getHostConfigurationSettings(store);
            return settings.toArray();
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof HostConfigurationSettings) {
                HostConfigurationSettings setting = (HostConfigurationSettings) parentElement;
                return setting.getChildren().toArray();
            }
            return null;
        }

        @Override
        public Object getParent(Object element) {
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof HostConfigurationSettings) {
                HostConfigurationSettings setting = (HostConfigurationSettings) element;
                return !setting.getChildren().isEmpty();
            }
            return false;
        }
    }

}
