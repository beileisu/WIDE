package com.wide.preferences;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.google.gson.Gson;
import com.wide.internal.core.IInternalWIDECoreConstants;
import com.wide.internal.ui.MultipleInputDialog;
import com.wide.internal.ui.SWTFactory;
import com.wide.preferences.constans.PreferenceConstans;
import com.wide.ui.WIDEUIPlugin;

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

    protected SimpleVariableContentProvider variableContentProvider = new SimpleVariableContentProvider();


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
            MultipleInputDialog dialog = new MultipleInputDialog(getShell(), PreferenceConstans.EDIT_CONNECTION);
            dialog.addTextField(WINDCHILL_HOST_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(HOST_USER_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(HOST_PASSWORD_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addComboxField(WINDCHILL_HOST_OS, IInternalWIDECoreConstants.EMPTY_STRING, false, WIDEPreferences.systemTypeItems);
            dialog.addTextField(WINDCHILL_ADMIN, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addTextField(WINDCHILL_ADMIN_PASSWORD, IInternalWIDECoreConstants.EMPTY_STRING, false);
            dialog.addComboxField(WINDCHILL_VERSION, IInternalWIDECoreConstants.EMPTY_STRING, false, WIDEPreferences.windchillVersionItems);
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

                VariableWrapper wrapper = new VariableWrapper(windchillHost, hostUser, hostUserPassword, windchillHostOS, windchillAdmin, windchillAdminPassword, windchillVersion, httpServerHome,
                        windchillHome, windchillDSHome);

                done = addVariable(wrapper);
            }
        }
    }

    /**
     * Attempts to create and add a new variable with the given properties. Returns
     * whether the operation completed successfully (either the variable was added
     * successfully, or the user cancelled the operation). Returns false if the name
     * is null or the user chooses not to overwrite an existing variable.
     *
     * @param name        name of the variable, cannot be <code>null</code> or
     *                    empty.
     * @param description description of the variable or <code>null</code>
     * @param value       value of the variable or <code>null</code>
     * @return whether the operation completed successfully
     */
    private boolean addVariable(VariableWrapper wrapper) {
        if (wrapper == null) {
            MessageDialog.openError(getShell(), "DebugPreferencesMessages.StringVariablePreferencePage_21", "DebugPreferencesMessages.StringVariablePreferencePage_20");
            return false;
        }

        String windchillHost = StringUtils.trimToEmpty(wrapper.getfWindchillHost());
        String hostUser = StringUtils.trimToEmpty(wrapper.getfHostUser());
        String hostUserPassword = StringUtils.trimToEmpty(wrapper.getfHostUserPassword());
        String windchillHostOS = StringUtils.trimToEmpty(wrapper.getfWindchillHostOS());
        String windchillAdmin = StringUtils.trimToEmpty(wrapper.getfWindchillAdmin());
        String windchillAdminPassword = StringUtils.trimToEmpty(wrapper.getfWindchillAdminPassword());
        String windchillVersion = StringUtils.trimToEmpty(wrapper.getfWindchillVersion());
        String httpServerHome = StringUtils.trimToEmpty(wrapper.getfHttpServerHome());
        String windchillHome = StringUtils.trimToEmpty(wrapper.getfWindchillHome());
        String windchillDSHome = StringUtils.trimToEmpty(wrapper.getfWindchillDSHome());

        List<VariableWrapper> editedVariables = variableContentProvider.getWorkingSetVariables();
        Iterator<VariableWrapper> iter = editedVariables.iterator();
        while (iter.hasNext()) {
            VariableWrapper currentVariable = iter.next();
            if (!currentVariable.isRemoved()) {
                String currentWindchillHost = currentVariable.getfWindchillHost();
                String currentWindchillVersion = currentVariable.getfWindchillVersion();
                String currentWindchillHostOS = currentVariable.getfWindchillHostOS();

                boolean hostFlag = StringUtils.equals(currentWindchillHost, windchillHost);
                boolean versionFlag = StringUtils.equals(currentWindchillVersion, windchillVersion);
                boolean hostOSFlag = StringUtils.equals(currentWindchillHostOS, windchillHostOS);

                if (hostFlag && versionFlag && hostOSFlag) {
                    if (currentVariable.isReadOnly()) {
                        MessageDialog.openError(getShell(), "DebugPreferencesMessages.StringVariablePreferencePage_23",
                                MessageFormat.format("DebugPreferencesMessages.StringVariablePreferencePage_22", new Object[] { currentWindchillHost }));
                        return false;
                    } else {
                        MessageDialog dialog = new MessageDialog(getShell(), "DebugPreferencesMessages.SimpleVariablePreferencePage_15", null,
                                MessageFormat.format("DebugPreferencesMessages.SimpleVariablePreferencePage_16", new Object[] { currentWindchillHost }), MessageDialog.QUESTION,
                                new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
                        int overWrite = dialog.open();
                        if (overWrite == 0) {
                            currentVariable.setfHostUser(hostUser);
                            currentVariable.setfHostUserPassword(hostUserPassword);
                            currentVariable.setfWindchillAdmin(windchillAdmin);
                            currentVariable.setfWindchillAdminPassword(windchillAdminPassword);
                            currentVariable.setfHttpServerHome(httpServerHome);
                            currentVariable.setfWindchillHome(windchillHome);
                            currentVariable.setfWindchillDSHome(windchillDSHome);

                            variableTable.update(currentVariable, null);
                            return true;
                        } else if (overWrite == 1) {
                            return false;
                        } else {
                            return true; // Cancel was pressed, return true so operation is ended
                        }
                    }
                }
            }
        }

        VariableWrapper newVariable = new VariableWrapper(windchillHost, hostUser, hostUserPassword, windchillHostOS, windchillAdmin, windchillAdminPassword, windchillVersion, httpServerHome,
                windchillHome, windchillDSHome);
        variableContentProvider.addVariable(newVariable);
        variableTable.refresh();
        return true;
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
        variableTable.setContentProvider(variableContentProvider);
        variableTable.setColumnProperties(WIDEPreferences.variableTableColumnProperties);
        variableTable.addFilter(new VariableFilter());
        variableTable.setComparator(new ViewerComparator() {
            @Override
            public int compare(Viewer iViewer, Object e1, Object e2) {
                if (e1 == null) {
                    return -1;
                } else if (e2 == null) {
                    return 1;
                } else {

                    int hostFlag = StringUtils.trimToEmpty(((VariableWrapper) e1).getfWindchillHost()).compareToIgnoreCase(StringUtils.trimToEmpty(((VariableWrapper) e2).getfWindchillHost()));
                    int hostOSFlag = StringUtils.trimToEmpty(((VariableWrapper) e1).getfWindchillHostOS()).compareToIgnoreCase(StringUtils.trimToEmpty(((VariableWrapper) e2).getfWindchillHostOS()));
                    int versionFlag = StringUtils.trimToEmpty(((VariableWrapper) e1).getfWindchillVersion()).compareToIgnoreCase(StringUtils.trimToEmpty(((VariableWrapper) e2).getfWindchillVersion()));

                    return hostFlag + hostOSFlag + versionFlag;
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

        variableTable.setInput(getVariableManager());
        variableTable.setLabelProvider(new SimpleVariableLabelProvider());
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
        List<VariableWrapper> variablesToRemove = selection.toList();
        StringBuffer contributedVariablesToRemove = new StringBuffer();
        Iterator<VariableWrapper> iter = variablesToRemove.iterator();
        while (iter.hasNext()) {
            VariableWrapper variable = iter.next();
            if (variable.isContributed()) {
                contributedVariablesToRemove.append('\t').append(variable.getfWindchillHost()).append('\n');
            }
        }
        if (contributedVariablesToRemove.length() > 0) {
            boolean remove = MessageDialog.openQuestion(getShell(), "DebugPreferencesMessages.SimpleLaunchVariablePreferencePage_21",
                    MessageFormat.format("DebugPreferencesMessages.SimpleLaunchVariablePreferencePage_22", new Object[] { contributedVariablesToRemove.toString() })); //
            if (!remove) {
                return;
            }
        }
        VariableWrapper[] variables = variablesToRemove.toArray(new VariableWrapper[0]);
        for (int i = 0; i < variables.length; i++) {
            variables[i].setRemoved(true);
        }
        variableTable.refresh();
    }

    private void handleEditButtonPressed() {
        IStructuredSelection selection = variableTable.getStructuredSelection();
        VariableWrapper variable = (VariableWrapper) selection.getFirstElement();
        if (variable == null || variable.isReadOnly()) {
            return;
        }

        String windchillHost = variable.getfWindchillHost();
        String hostUser = variable.getfHostUser();
        String hostUserPassword = variable.getfHostUserPassword();
        String windchillHostOS = variable.getfWindchillHostOS();
        String windchillAdmin = variable.getfWindchillAdmin();
        String windchillAdminPassword = variable.getfWindchillAdminPassword();
        String windchillVersion = variable.getfWindchillVersion();
        String httpServerHome = variable.getfHttpServerHome();
        String windchillHome = variable.getfWindchillHome();
        String windchillDSHome = variable.getfWindchillDSHome();

        MultipleInputDialog dialog = new MultipleInputDialog(getShell(), MessageFormat.format(PreferenceConstans.EDIT_VARIABLE, new Object[] { windchillHost }));
        dialog.addTextField(HOST_USER_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
        dialog.addTextField(HOST_PASSWORD_LABEL, IInternalWIDECoreConstants.EMPTY_STRING, false);
        dialog.addComboxField(WINDCHILL_HOST_OS, IInternalWIDECoreConstants.EMPTY_STRING, false, WIDEPreferences.systemTypeItems);
        dialog.addTextField(WINDCHILL_ADMIN, IInternalWIDECoreConstants.EMPTY_STRING, false);
        dialog.addTextField(WINDCHILL_ADMIN_PASSWORD, IInternalWIDECoreConstants.EMPTY_STRING, false);
        dialog.addComboxField(WINDCHILL_VERSION, IInternalWIDECoreConstants.EMPTY_STRING, false, WIDEPreferences.windchillVersionItems);
        dialog.addTextField(HTTP_SERVER_HOME, IInternalWIDECoreConstants.EMPTY_STRING, false);
        dialog.addTextField(WINDCHILL_HOME, IInternalWIDECoreConstants.EMPTY_STRING, false);
        dialog.addTextField(WINDCHILLDS_HOME, IInternalWIDECoreConstants.EMPTY_STRING, false);

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
                variable.setfHostUser(hostUser);
            }
            if (StringUtils.isNotEmpty(hostUserPassword)) {
                variable.setfHostUserPassword(hostUserPassword);
            }
            if (StringUtils.isNotEmpty(hostUserPassword)) {
                variable.setfHostUserPassword(hostUserPassword);
            }
            if (StringUtils.isNotEmpty(windchillHostOS)) {
                variable.setfWindchillHostOS(windchillHostOS);
            }
            if (StringUtils.isNotEmpty(windchillAdmin)) {
                variable.setfWindchillAdmin(windchillAdmin);
            }
            if (StringUtils.isNotEmpty(windchillAdminPassword)) {
                variable.setfWindchillAdminPassword(windchillAdminPassword);
            }
            if (StringUtils.isNotEmpty(windchillVersion)) {
                variable.setfWindchillVersion(windchillVersion);
            }
            if (StringUtils.isNotEmpty(httpServerHome)) {
                variable.setfHttpServerHome(httpServerHome);
            }
            if (StringUtils.isNotEmpty(windchillHome)) {
                variable.setfWindchillHome(windchillHome);
            }
            if (StringUtils.isNotEmpty(windchillDSHome)) {
                variable.setfWindchillDSHome(windchillDSHome);
            }

            variableTable.update(variable, null);
        }
    }

    /**
     * Responds to a selection changed event in the variable table
     * 
     * @param event the selection change event
     */
    protected void handleTableSelectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (event.getStructuredSelection());
        VariableWrapper variable = (VariableWrapper) selection.getFirstElement();
        if (variable == null || variable.isReadOnly()) {
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
        variableContentProvider.init();
        variableTable.refresh();
        super.performDefaults();
    }

    /**
     * Sets the saved state for reversion.
     */
    @Override
    public boolean performOk() {
        variableContentProvider.saveChanges();
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
    private IStringVariableManager getVariableManager() {

        IPreferenceStore store = WIDEUIPlugin.getDefault().getPreferenceStore();

        if (store instanceof ScopedPreferenceStore) {
            ScopedPreferenceStore store2 = (ScopedPreferenceStore) store;

            System.out.println("Store:" + store2.getString("AAAA"));

        }

        return VariablesPlugin.getDefault().getStringVariableManager();
    }

    private class SimpleVariableLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int columnIndex) {
            if (element instanceof VariableWrapper) {
                VariableWrapper variable = (VariableWrapper) element;
                switch (columnIndex) {
                case 0:
                    StringBuffer windchillHost = new StringBuffer();
                    windchillHost.append(StringUtils.trimToEmpty(variable.getfWindchillHost()));
                    if (variable.isReadOnly()) {
                        // name.append(DebugPreferencesMessages.StringVariablePreferencePage_26);
                        windchillHost.append("");
                    }
                    return windchillHost.toString();
                case 1:
                    String windchillVersion = variable.getfWindchillVersion();
                    if (windchillVersion == null) {
                        windchillVersion = IInternalWIDECoreConstants.EMPTY_STRING;
                    }
                    return windchillVersion;
                case 2:
                    String windchillHostOS = variable.getfWindchillHostOS();
                    if (windchillHostOS == null) {
                        windchillHostOS = IInternalWIDECoreConstants.EMPTY_STRING;
                    }
                    return windchillHostOS;
                default:
                    break;

                }
            }
            return null;
        }

        @Override
        public Color getForeground(Object element) {
            if (element instanceof VariableWrapper) {
                if (((VariableWrapper) element).isReadOnly()) {
                    Display display = Display.getCurrent();
                    return display.getSystemColor(SWT.COLOR_INFO_FOREGROUND);
                }
            }
            return null;
        }

        @Override
        public Color getBackground(Object element) {
            if (element instanceof VariableWrapper) {
                if (((VariableWrapper) element).isReadOnly()) {
                    Display display = Display.getCurrent();
                    return display.getSystemColor(SWT.COLOR_INFO_BACKGROUND);
                }
            }
            return null;
        }
    }

    private class SimpleVariableContentProvider implements IStructuredContentProvider {
        /**
         * The content provider stores variable wrappers for use during editing.
         */
        private List<VariableWrapper> fWorkingSet = new ArrayList<>();

        @Override
        public Object[] getElements(Object inputElement) {
            return fWorkingSet.toArray();
        }

        /**
         * Adds the given variable to the 'wrappers'
         *
         * @param variable variable to add
         */
        public void addVariable(VariableWrapper variable) {
            fWorkingSet.add(variable);
        }

        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            if (newInput == null || !(newInput instanceof IStringVariableManager)) {
                return;
            }
            init();
        }

        /**
         * Saves the edited variable state to the variable manager.
         */
        public void saveChanges() {
            IStringVariableManager manager = getVariableManager();
            Iterator<VariableWrapper> iterator = fWorkingSet.iterator();
            List<IValueVariable> remove = new ArrayList<>();
            List<IValueVariable> add = new ArrayList<>();
            while (iterator.hasNext()) {
                VariableWrapper variable = iterator.next();

                String name = variable.getfWindchillHost();
                String description = variable.getfWindchillVersion();
                String value = "";
                if (name != null && description != null) {
                    value = new Gson().toJson(variable);
                }

                if (!variable.isReadOnly()) {
                    IValueVariable underlyingVariable = variable.getUnderlyingVariable();
                    if (variable.isRemoved()) {
                        if (underlyingVariable != null) {
                            // if added and removed there is no underlying variable
                            remove.add(underlyingVariable);
                        }
                    } else if (variable.isAdded()) {
                        IValueVariable vv = manager.newValueVariable(name, description);
                        vv.setValue(value);
                        add.add(vv);
                    } else if (variable.isChanged()) {
                        underlyingVariable.setValue(name);
                        underlyingVariable.setDescription(description);
                    }
                }
            }
            // remove
            if (!remove.isEmpty()) {
                manager.removeVariables(remove.toArray(new IValueVariable[remove.size()]));
            }
            // add
            if (!add.isEmpty()) {
                try {
                    manager.addVariables(add.toArray(new IValueVariable[add.size()]));
                } catch (CoreException e) {
                    WIDEUIPlugin.errorDialog(getShell(), "", "", e.getStatus()); //
                }
            }
        }

        /**
         * Re-initializes to the variables currently stored in the manager.
         */
        public void init() {
            fWorkingSet.clear();
            IStringVariableManager manager = getVariableManager();
            IValueVariable[] variables = manager.getValueVariables();
            for (int i = 0; i < variables.length; i++) {
                fWorkingSet.add(new VariableWrapper(variables[i]));
            }
        }

        /**
         * Returns the 'working set' of variables
         *
         * @return the working set of variables (not yet saved)
         */
        public List<VariableWrapper> getWorkingSetVariables() {
            return fWorkingSet;
        }

    }

    class VariableWrapper {

        protected IValueVariable fVariable;

        protected String fWindchillHost = null;
        protected String fHostUser = null;
        protected String fHostUserPassword = null;
        protected String fWindchillHostOS = null;
        protected String fWindchillAdmin = null;
        protected String fWindchillAdminPassword = null;
        protected String fWindchillVersion = null;
        protected String fHttpServerHome = null;
        protected String fWindchillHome = null;
        protected String fWindchillDSHome = null;

        boolean fRemoved = false;
        boolean fAdded = false;

        public VariableWrapper(IValueVariable variable) {
            fVariable = variable;
        }

        public VariableWrapper(String windchillHost, String hostUser, String hostUserPassword, String hostOS, String windchillAdmin, String windchillAdminPassword, String windchillVersion,
                String httpServerHome, String windchillHome, String windchillDSHome) {
            fWindchillHost = windchillHost;
            fHostUser = hostUser;
            fHostUserPassword = hostUserPassword;
            fWindchillHostOS = hostOS;
            fWindchillAdmin = windchillAdmin;
            fWindchillAdminPassword = windchillAdminPassword;
            fWindchillVersion = windchillVersion;
            fHttpServerHome = httpServerHome;
            fWindchillHome = windchillHome;
            fWindchillDSHome = windchillDSHome;
            fAdded = true;
        }

        public boolean isAdded() {
            return fAdded;
        }

        public String getfWindchillHost() {
            return fWindchillHost;
        }

        public void setfWindchillHost(String fWindchillHost) {
            this.fWindchillHost = fWindchillHost;
        }

        public String getfHostUser() {
            return fHostUser;
        }

        public void setfHostUser(String fHostUser) {
            this.fHostUser = fHostUser;
        }

        public String getfHostUserPassword() {
            return fHostUserPassword;
        }

        public void setfHostUserPassword(String fHostUserPassword) {
            this.fHostUserPassword = fHostUserPassword;
        }

        public String getfWindchillHostOS() {
            return fWindchillHostOS;
        }

        public void setfWindchillHostOS(String fWindchillHostOS) {
            this.fWindchillHostOS = fWindchillHostOS;
        }

        public String getfWindchillAdmin() {
            return fWindchillAdmin;
        }

        public void setfWindchillAdmin(String fWindchillAdmin) {
            this.fWindchillAdmin = fWindchillAdmin;
        }

        public String getfWindchillAdminPassword() {
            return fWindchillAdminPassword;
        }

        public void setfWindchillAdminPassword(String fWindchillAdminPassword) {
            this.fWindchillAdminPassword = fWindchillAdminPassword;
        }

        public String getfWindchillVersion() {
            return fWindchillVersion;
        }

        public void setfWindchillVersion(String fWindchillVersion) {
            this.fWindchillVersion = fWindchillVersion;
        }

        public String getfHttpServerHome() {
            return fHttpServerHome;
        }

        public void setfHttpServerHome(String fHttpServerHome) {
            this.fHttpServerHome = fHttpServerHome;
        }

        public String getfWindchillHome() {
            return fWindchillHome;
        }

        public void setfWindchillHome(String fWindchillHome) {
            this.fWindchillHome = fWindchillHome;
        }

        public String getfWindchillDSHome() {
            return fWindchillDSHome;
        }

        public void setfWindchillDSHome(String fWindchillDSHome) {
            this.fWindchillDSHome = fWindchillDSHome;
        }

        public boolean isChanged() {
            return !fAdded && !fRemoved && (fWindchillVersion != null || fWindchillHostOS != null);
        }

        public boolean isReadOnly() {
            if (fVariable == null) {
                return false;
            }
            return fVariable.isReadOnly();
        }

        public boolean isContributed() {
            if (fVariable == null) {
                return false;
            }
            return fVariable.isContributed();
        }

        public IValueVariable getUnderlyingVariable() {
            return fVariable;
        }

        public boolean isRemoved() {
            return fRemoved;
        }

        public void setRemoved(boolean removed) {
            fRemoved = removed;
        }
    }

    class VariableFilter extends ViewerFilter {

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            return !((VariableWrapper) element).isRemoved();
        }

    }

}