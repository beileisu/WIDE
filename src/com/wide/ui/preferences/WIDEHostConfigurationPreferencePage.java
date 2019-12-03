package com.wide.ui.preferences;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.WorkbenchPlugin;

import com.wide.internal.core.IInternalWIDECoreConstants;
import com.wide.internal.ui.MultipleInputDialog;
import com.wide.internal.ui.SWTFactory;
import com.wide.ui.WIDEUIPlugin;
import com.wide.ui.preferences.bean.HostConfigurationSettings;
import com.wide.ui.preferences.constans.WIDEPreferencesConstans;

@SuppressWarnings({ "restriction", "unchecked" })
public class WIDEHostConfigurationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    protected static final String WIDE_HOST_CONFIGURATION_PREFERENCE_KEY = "WIDEHostConfigurationPreferencePage"; //$NON-NLS-1$

    protected static final String WINDCHILL_HOST_LABEL = "* Windchill Host";
    protected static final String HOST_USER_LABEL = "* Host User";
    protected static final String HOST_PASSWORD_LABEL = "* Host Password";
    protected static final String WINDCHILL_HOST_OS = "* Windchill Host OS";
    protected static final String WINDCHILL_ADMIN = "* Windchill Admin";
    protected static final String WINDCHILL_ADMIN_PASSWORD = "* Windchill Admin Password";
    protected static final String WINDCHILL_VERSION = "* Windchill Version";
    protected static final String HTTP_SERVER_HOME = "* Http Server Home";
    protected static final String WINDCHILL_HOME = "* Windchill Home";
    protected static final String WINDCHILLDS_HOME = "* WindchillDS Home";

    protected static final String VALUE_LABEL = "Host User";
    protected static final String DESCRIPTION_LABEL = "Host Password";

    private TableViewer variableTable;
    protected Button envAddButton;
    protected Button envEditButton;
    protected Button envRemoveButton;

    protected TableViewerLabelProvider labelProvider = new TableViewerLabelProvider();
    protected TableViewerContentProvider contentProvider = new TableViewerContentProvider();
    
    public WIDEHostConfigurationPreferencePage() {
        setPreferenceStore(WorkbenchPlugin.getDefault().getPreferenceStore());
        setDescription("A demonstration of a preference page implementation");
    }

    @Override
    protected Control createContents(Composite parent) {
        noDefaultAndApplyButton();
        Font font = parent.getFont();
        // The main composite
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 2;
        composite.setLayout(layout);
        composite.setFont(font);

        createTable(composite);
        createButtons(composite);

        return composite;
    }

    /**
     * Creates the new/edit/remove buttons for the variable table
     * 
     * @param parent the composite in which the buttons should be created
     */
    private void createButtons(Composite parent) {
        // Create button composite
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout glayout = new GridLayout();
        glayout.marginHeight = 0;
        glayout.marginWidth = 0;
        glayout.numColumns = 1;
        GridData gdata = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
        buttonComposite.setLayout(glayout);
        buttonComposite.setLayoutData(gdata);
        buttonComposite.setFont(parent.getFont());

        // Create buttons
        envAddButton = SWTFactory.createPushButton(buttonComposite, "New...", null);
        envAddButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                handleAddButtonPressed();
            }
        });
        envEditButton = SWTFactory.createPushButton(buttonComposite, "Edit...", null);
        envEditButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                handleEditButtonPressed();
            }
        });
        envEditButton.setEnabled(false);
        envRemoveButton = SWTFactory.createPushButton(buttonComposite, "Remove", null);
        envRemoveButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                handleRemoveButtonPressed();
            }
        });
        
        envRemoveButton.setEnabled(false);
    }

    private void handleAddButtonPressed() {
        boolean done = false;

        while (!done) {
            MultipleInputDialog dialog = new MultipleInputDialog(getShell(), WIDEPreferencesConstans.NEW_CONNECTION);
            dialog.addTextField(WINDCHILL_HOST_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(HOST_USER_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(HOST_PASSWORD_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addComboxField(WINDCHILL_HOST_OS, IInternalWIDECoreConstants.EMPTY_STRING, false, WIDEPreferences.HOST_OS_ITEMS);
            dialog.addTextField(WINDCHILL_ADMIN, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(WINDCHILL_ADMIN_PASSWORD, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addComboxField(WINDCHILL_VERSION, IInternalWIDECoreConstants.EMPTY_STRING, false, WIDEPreferences.WINDCHILL_VERSION_ITEMS);
            dialog.addTextField(HTTP_SERVER_HOME, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(WINDCHILL_HOME, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(WINDCHILLDS_HOME, IInternalWIDECoreConstants.EMPTY_STRING, false);

            if (dialog.open() != Window.OK) {
                done = true;
            } else {
                String windchillHost = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_HOST_LABEL));
                String hostUser = StringUtils.trimToEmpty(dialog.getStringValue(HOST_USER_LABEL));
                String hostUserPassword = StringUtils.trimToEmpty(dialog.getStringValue(HOST_PASSWORD_LABEL));
                String windchillHostOS = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_HOST_OS));
                String windchillAdmin = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_ADMIN));
                String windchillAdminPassword = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_ADMIN_PASSWORD));
                String windchillVersion = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_VERSION));
                String httpServerHome = StringUtils.trimToEmpty(dialog.getStringValue(HTTP_SERVER_HOME));
                String windchillHome = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_HOME));
                String windchillDSHome = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILLDS_HOME));

                HostConfigurationSettings setting = HostConfigurationSettings.getInstence(windchillHost, hostUser, hostUserPassword, windchillHostOS, windchillAdmin, windchillAdminPassword, windchillVersion, httpServerHome,
                        windchillHome, windchillDSHome);

                done = addHostConfigurationSettings(setting);
                
            }
        }
    }

    /**
     * Creates and configures the table containing launch configuration variables
     * and their associated value.
     */
    private void createTable(Composite parent) {
        Font font = parent.getFont();
        // Create table composite
        Composite tableComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.numColumns = 1;
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.heightHint = 150;
        gridData.widthHint = 400;
        tableComposite.setLayout(layout);
        tableComposite.setLayoutData(gridData);
        tableComposite.setFont(font);
        // Create table
        variableTable = new TableViewer(tableComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
        Table table = variableTable.getTable();
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setFont(font);
        gridData = new GridData(GridData.FILL_BOTH);
        variableTable.getControl().setLayoutData(gridData);
        variableTable.setColumnProperties(WIDEPreferences.variableTableColumnProperties);
        variableTable.setContentProvider(contentProvider);
        variableTable.setLabelProvider(labelProvider);
        variableTable.setComparator(new ViewerComparator() {
            @Override
            public int compare(Viewer iViewer, Object e1, Object e2) {
                if (e1 == null) {
                    return -1;
                } else if (e2 == null) {
                    return 1;
                } else {
                    int flag = ((HostConfigurationSettings) e1).equals((HostConfigurationSettings) e2)?0:2;
                    return flag;
                }
            }
        });

        variableTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                handleTableSelectionChanged(event);
            }
        });

        variableTable.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                if (!variableTable.getSelection().isEmpty()) {
                    handleEditButtonPressed();
                }
            }
        });
        
        variableTable.getTable().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.character == SWT.DEL && event.stateMask == 0) {
                    handleRemoveButtonPressed();
                }
            }
        });
        
        for (int i = 0; i < WIDEPreferences.variableTableColumnHeaders.length; i++) {
            TableColumn tc = new TableColumn(table, SWT.NONE, i);
            tc.setResizable(WIDEPreferences.variableTableColumnLayouts[i].resizable);
            tc.setText(WIDEPreferences.variableTableColumnHeaders[i]);
        }

        // Try restoring column widths from preferences, if widths aren't stored, init
        // columns to default
        if (!restoreColumnWidths()) {
            restoreDefaultColumnWidths();
        }
        variableTable.setInput(getHostConfigurationSettings());
    }

    private void restoreDefaultColumnWidths() {
        TableLayout layout = new TableLayout();
        for (int i = 0; i < WIDEPreferences.variableTableColumnLayouts.length; i++) {
            layout.addColumnData(WIDEPreferences.variableTableColumnLayouts[i]);
        }
        variableTable.getTable().setLayout(layout);
    }

    private boolean restoreColumnWidths() {
        String[] columnWidthStrings = WIDEUIPlugin.getDefault().getPreferenceStore().getString(WIDE_HOST_CONFIGURATION_PREFERENCE_KEY).split(","); //$NON-NLS-1$
        int columnCount = variableTable.getTable().getColumnCount();
        if (columnWidthStrings.length != columnCount) {
            return false; // Preferred column sizes not stored correctly.
        }
        for (int i = 0; i < columnCount; i++) {
            try {
                int columnWidth = Integer.parseInt(columnWidthStrings[i]);
                variableTable.getTable().getColumn(i).setWidth(columnWidth);
            } catch (NumberFormatException e) {
                WIDEUIPlugin.log(new Throwable("Problem loading persisted column sizes for StringVariablePreferencesPage", e)); //$NON-NLS-1$
            }
        }
        return true;
    }

    /**
     * Remove the selection variables.
     */
    private void handleRemoveButtonPressed() {
        IStructuredSelection selection = variableTable.getStructuredSelection();
        List<HostConfigurationSettings> settingsToRemove = selection.toList();
        IPreferenceStore store = WIDEUIPlugin.getDefault().getPreferenceStore();
        WIDEPreferences.removeHostConfigurationSettings(store, settingsToRemove);
        
        variableTable.refresh();
    }

    private void handleEditButtonPressed() {
        IStructuredSelection selection = variableTable.getStructuredSelection();
        HostConfigurationSettings setting = (HostConfigurationSettings) selection.getFirstElement();
        if (setting == null ) {
            return;
        }

        String windchillHost = setting.getWindchillHost();
        String hostUser = setting.getHostUser();
        String hostUserPassword = setting.getHostUserPassword();
        String windchillHostOS = setting.getWindchillHostOS();
        String windchillAdmin = setting.getWindchillAdmin();
        String windchillAdminPassword = setting.getWindchillAdminPassword();
        String windchillVersion = setting.getWindchillVersion();
        String httpServerHome = setting.getHttpServerHome();
        String windchillHome = setting.getWindchillHome();
        String windchillDSHome = setting.getWindchillDSHome();

        MultipleInputDialog dialog = new MultipleInputDialog(getShell(), MessageFormat.format(WIDEPreferencesConstans.EDIT_CONNECTION, new Object[] { windchillHost }));
        dialog.addTextField(HOST_USER_LABEL, hostUser, false);
        dialog.addTextField(HOST_PASSWORD_LABEL, hostUserPassword, false);
        dialog.addComboxField(WINDCHILL_HOST_OS, windchillHostOS, false, WIDEPreferences.HOST_OS_ITEMS);
        dialog.addTextField(WINDCHILL_ADMIN, windchillAdmin, false);
        dialog.addTextField(WINDCHILL_ADMIN_PASSWORD, windchillAdminPassword, false);
        dialog.addComboxField(WINDCHILL_VERSION, windchillVersion, false, WIDEPreferences.WINDCHILL_VERSION_ITEMS);
        dialog.addTextField(HTTP_SERVER_HOME, httpServerHome, false);
        dialog.addTextField(WINDCHILL_HOME, windchillHome, false);
        dialog.addTextField(WINDCHILLDS_HOME, windchillDSHome, false);

        if (dialog.open() == Window.OK) {
            hostUser = StringUtils.trimToEmpty(dialog.getStringValue(HOST_USER_LABEL));
            hostUserPassword = StringUtils.trimToEmpty(dialog.getStringValue(HOST_PASSWORD_LABEL));
            windchillHostOS = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_HOST_OS));
            windchillAdmin = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_ADMIN));
            windchillAdminPassword = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_ADMIN_PASSWORD));
            windchillVersion = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_VERSION));
            httpServerHome = StringUtils.trimToEmpty(dialog.getStringValue(HTTP_SERVER_HOME));
            windchillHome = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILL_HOME));
            windchillDSHome = StringUtils.trimToEmpty(dialog.getStringValue(WINDCHILLDS_HOME));

            if (StringUtils.isNotEmpty(hostUser)) {
                setting.setHostUser(hostUser);
            }
            if (StringUtils.isNotEmpty(hostUserPassword)) {
                setting.setHostUserPassword(hostUserPassword);
            }
            if (StringUtils.isNotEmpty(hostUserPassword)) {
                setting.setHostUserPassword(hostUserPassword);
            }
            if (StringUtils.isNotEmpty(windchillHostOS)) {
                setting.setWindchillHostOS(windchillHostOS);
            }
            if (StringUtils.isNotEmpty(windchillAdmin)) {
                setting.setWindchillAdmin(windchillAdmin);
            }
            if (StringUtils.isNotEmpty(windchillAdminPassword)) {
                setting.setWindchillAdminPassword(windchillAdminPassword);
            }
            if (StringUtils.isNotEmpty(windchillVersion)) {
                setting.setWindchillVersion(windchillVersion);
            }
            if (StringUtils.isNotEmpty(httpServerHome)) {
                setting.setHttpServerHome(httpServerHome);
            }
            if (StringUtils.isNotEmpty(windchillHome)) {
                setting.setWindchillHome(windchillHome);
            }
            if (StringUtils.isNotEmpty(windchillDSHome)) {
                setting.setWindchillDSHome(windchillDSHome);
            }

            variableTable.update(setting, null);
        }
    }

    /**
     * Responds to a selection changed event in the variable table
     * 
     * @param event the selection change event
     */
    protected void handleTableSelectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (event.getStructuredSelection());
        HostConfigurationSettings variable = (HostConfigurationSettings) selection.getFirstElement();
        if (variable == null) {
            envEditButton.setEnabled(false);
            envRemoveButton.setEnabled(false);
        } else {
            envEditButton.setEnabled(selection.size() == 1);
            envRemoveButton.setEnabled(selection.size() > 0);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
//        Activator.getDefault().getPreferenceStore();
    }

    /**
     * Clear the variables.
     */
    @Override
    protected void performDefaults() {
        variableTable.refresh();
        super.performDefaults();
    }

    /**
     * Sets the saved state for reversion.
     */
    @Override
    public boolean performOk() {
        saveColumnWidths();
        return super.performOk();
    }

    public void saveColumnWidths() {
        StringBuffer widthPreference = new StringBuffer();
        for (int i = 0; i < variableTable.getTable().getColumnCount(); i++) {
            widthPreference.append(variableTable.getTable().getColumn(i).getWidth());
            widthPreference.append(',');
        }
        if (widthPreference.length() > 0) {
            WIDEUIPlugin.getDefault().getPreferenceStore().setValue(WIDE_HOST_CONFIGURATION_PREFERENCE_KEY, widthPreference.toString());
        }
    }

    /**
     * Returns the DebugPlugin's singleton instance of the launch variable manager
     * 
     * @return the singleton instance of the simple variable registry.
     */
    private Object[] getHostConfigurationSettings() {

        IPreferenceStore store = WIDEUIPlugin.getDefault().getPreferenceStore();
        List<HostConfigurationSettings> settings = WIDEPreferences.getHostConfigurationSettings(store);
        
        return settings.toArray();
    }

    private boolean addHostConfigurationSettings(HostConfigurationSettings setting) {
        IPreferenceStore store = WIDEUIPlugin.getDefault().getPreferenceStore();

        WIDEPreferences.saveHostConfigurationSettings(store, setting);
        variableTable.refresh();
        return true;

    }
    
    class TableViewerLabelProvider implements ITableLabelProvider{

        @Override
        public void addListener(ILabelProviderListener listener) {
            
        }

        @Override
        public void dispose() {
            
        }

        @Override
        public boolean isLabelProperty(Object element, String property) {
            return false;
        }

        @Override
        public void removeListener(ILabelProviderListener listener) {
            
        }

        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            
            String columnValue = "";
            
            HostConfigurationSettings settings = (HostConfigurationSettings) element;
    
            switch (columnIndex) {
            case 0:
                columnValue = settings.getWindchillHost();
                break;
            case 1:
                columnValue = settings.getWindchillVersion();
                break;
            case 2:
                columnValue = settings.getWindchillHostOS();
                break;
            default:
                break;
            }
            
            return columnValue;

        }
        
    }
    
    class TableViewerContentProvider implements IStructuredContentProvider {

        @Override
        public Object[] getElements(Object inputElement) {
            
            return getHostConfigurationSettings();
        }
        
    }
    

}