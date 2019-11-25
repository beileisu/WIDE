package com.wide.internal.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.Lists;
import com.wide.internal.core.IInternalWIDECoreConstants;
import com.wide.ui.StringVariableSelectionDialog;

public class MultipleInputDialog extends Dialog {
    protected static final String FIELD_NAME = "FIELD_NAME"; //$NON-NLS-1$
    protected static final int TEXT = 100;
    protected static final int BROWSE = 101;
    protected static final int VARIABLE = 102;
    protected static final int MULTILINE_VARIABLE = 103;
    protected static final int COMBOX = 104;

    protected Composite panel;

    protected List<FieldSummary> fieldList = new ArrayList<>();
    protected List<Text> controlList = new ArrayList<>();
    protected List<Combo> controlComboList = new ArrayList<>();
    protected List<Validator> validators = new ArrayList<>();
    protected Map<Object, String> valueMap = new HashMap<>();

    private String title;

    public MultipleInputDialog(Shell shell, String title) {
        super(shell);
        this.title = title;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        if (title != null) {
            shell.setText(title);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.Dialog#createButtonBar(org.eclipse.swt.widgets.
     * Composite)
     */
    @Override
    protected Control createButtonBar(Composite parent) {
        Control bar = super.createButtonBar(parent);
        validateFields();
        return bar;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
     * Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(new GridData(GridData.FILL_BOTH));

        panel = new Composite(container, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        panel.setLayout(layout);
        panel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        for (FieldSummary field : fieldList) {
            switch (field.type) {
            case TEXT:
                createTextField(field.name, field.initialValue, field.allowsEmpty);
                break;
            case BROWSE:
                createBrowseField(field.name, field.initialValue, field.allowsEmpty);
                break;
            case VARIABLE:
                createVariablesField(field.name, field.initialValue, field.allowsEmpty);
                break;
            case MULTILINE_VARIABLE:
                createMultilineVariablesField(field.name, field.initialValue, field.allowsEmpty);
                break;
            case COMBOX:
                createCombosField(field.name, field.initialValue, field.allowsEmpty, field.items);
                break;
            default:
                break;
            }
        }

        fieldList = null; // allow it to be gc'd
        Dialog.applyDialogFont(container);
        return container;
    }

    public void addBrowseField(String labelText, String initialValue, boolean allowsEmpty) {
        fieldList.add(new FieldSummary(BROWSE, labelText, initialValue, allowsEmpty, null));
    }

    public void addTextField(String labelText, String initialValue, boolean allowsEmpty) {
        fieldList.add(new FieldSummary(TEXT, labelText, initialValue, allowsEmpty, null));
    }

    public void addVariablesField(String labelText, String initialValue, boolean allowsEmpty) {
        fieldList.add(new FieldSummary(VARIABLE, labelText, initialValue, allowsEmpty, null));
    }

    public void addMultilinedVariablesField(String labelText, String initialValue, boolean allowsEmpty) {
        fieldList.add(new FieldSummary(MULTILINE_VARIABLE, labelText, initialValue, allowsEmpty, null));
    }

    public void addComboxField(String labelText, String initialValue, boolean allowsEmpty, String[] items) {
        fieldList.add(new FieldSummary(COMBOX, labelText, initialValue, allowsEmpty, items));
    }

    protected void createTextField(String labelText, String initialValue, boolean allowEmpty) {
        Label label = new Label(panel, SWT.NONE);
        label.setText(labelText);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        final Text text = new Text(panel, SWT.SINGLE | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 200;
        text.setLayoutData(data);
        text.setData(FIELD_NAME, labelText);

        // make sure rows are the same height on both panels.
        label.setSize(label.getSize().x, text.getSize().y);

        if (initialValue != null) {
            text.setText(initialValue);
        }

        if (!allowEmpty) {
            validators.add(new Validator() {
                @Override
                public boolean validate() {
                    return !text.getText().equals(IInternalWIDECoreConstants.EMPTY_STRING);
                }
            });
            text.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    validateFields();
                }
            });
        }

        controlList.add(text);
    }

    protected void createBrowseField(String labelText, String initialValue, boolean allowEmpty) {
        Label label = new Label(panel, SWT.NONE);
        label.setText(labelText);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        Composite comp = new Composite(panel, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        comp.setLayout(layout);
        comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Text text = new Text(comp, SWT.SINGLE | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 200;
        text.setLayoutData(data);
        text.setData(FIELD_NAME, labelText);

        // make sure rows are the same height on both panels.
        label.setSize(label.getSize().x, text.getSize().y);

        if (initialValue != null) {
            text.setText(initialValue);
        }

        if (!allowEmpty) {
            validators.add(new Validator() {
                @Override
                public boolean validate() {
                    return !text.getText().equals(IInternalWIDECoreConstants.EMPTY_STRING);
                }
            });

            text.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    validateFields();
                }
            });
        }

        Button button = createButton(comp, IDialogConstants.IGNORE_ID, "DebugUIMessages.MultipleInputDialog_6", false);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.SHEET);
                dialog.setMessage("DebugUIMessages.MultipleInputDialog_7");
                String currentWorkingDir = text.getText();
                if (!currentWorkingDir.trim().equals(IInternalWIDECoreConstants.EMPTY_STRING)) {
                    File path = new File(currentWorkingDir);
                    if (path.exists()) {
                        dialog.setFilterPath(currentWorkingDir);
                    }
                }

                String selectedDirectory = dialog.open();
                if (selectedDirectory != null) {
                    text.setText(selectedDirectory);
                }
            }
        });

        controlList.add(text);

    }

    public void createVariablesField(String labelText, String initialValue, boolean allowEmpty) {
        Label label = new Label(panel, SWT.NONE);
        label.setText(labelText);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        Composite comp = new Composite(panel, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        comp.setLayout(layout);
        comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Text text = new Text(comp, SWT.SINGLE | SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 200;
        text.setLayoutData(data);
        text.setData(FIELD_NAME, labelText);

        // make sure rows are the same height on both panels.
        label.setSize(label.getSize().x, text.getSize().y);

        if (initialValue != null) {
            text.setText(initialValue);
        }

        if (!allowEmpty) {
            validators.add(new Validator() {
                @Override
                public boolean validate() {
                    return !text.getText().equals(IInternalWIDECoreConstants.EMPTY_STRING);
                }
            });

            text.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    validateFields();
                }
            });
        }

        Button button = createButton(comp, IDialogConstants.IGNORE_ID, "DebugUIMessages.MultipleInputDialog_8", false);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
                int code = dialog.open();
                if (code == IDialogConstants.OK_ID) {
                    String variable = dialog.getVariableExpression();
                    if (variable != null) {
                        text.insert(variable);
                    }
                }
            }
        });

        controlList.add(text);

    }

    public void createMultilineVariablesField(String labelText, String initialValue, boolean allowEmpty) {
        Label label = new Label(panel, SWT.NONE);
        label.setText(labelText);
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        final Text text = new Text(panel, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 300;
        gd.heightHint = 4 * text.getLineHeight();
        gd.horizontalSpan = 2;
        text.setLayoutData(gd);
        text.setData(FIELD_NAME, labelText);

        text.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN && e.stateMask == SWT.SHIFT) {
                    e.doit = true;
                }
            }
        });

        // make sure rows are the same height on both panels.
        label.setSize(label.getSize().x, text.getSize().y);

        if (initialValue != null) {
            text.setText(initialValue);
        }

        if (!allowEmpty) {
            validators.add(new Validator() {
                @Override
                public boolean validate() {
                    return !text.getText().equals(IInternalWIDECoreConstants.EMPTY_STRING);
                }
            });

            text.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    validateFields();
                }
            });
        }
        Composite comp = SWTFactory.createComposite(panel, panel.getFont(), 1, 2, GridData.HORIZONTAL_ALIGN_END);
        GridLayout ld = (GridLayout) comp.getLayout();
        ld.marginHeight = 1;
        ld.marginWidth = 0;
        ld.horizontalSpacing = 0;
        Button button = createButton(comp, IDialogConstants.IGNORE_ID, "DebugUIMessages.MultipleInputDialog_8", false);

        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getShell());
                int code = dialog.open();
                if (code == IDialogConstants.OK_ID) {
                    String variable = dialog.getVariableExpression();
                    if (variable != null) {
                        text.insert(variable);
                    }
                }
            }
        });

        controlList.add(text);
    }

    /**
     * Creates a set of combo boxes on a per-selection basis that display a listing
     * of available perspectives to switch to
     * 
     * @param parent    the parent to add the created combo boxes to
     * @param selection the selection in the tree viewer
     */
    public void createCombosField(String labelText, String initialValue, boolean allowEmpty, String[] items) {

        Label label = new Label(panel, SWT.NONE);
        label.setText(labelText);
        label.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        final Combo combo = new Combo(panel, SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        combo.setData(FIELD_NAME, labelText);
        if (items != null) {
            combo.setItems(items);
        }
        
        if(StringUtils.isEmpty(initialValue)) {
            combo.select(0);
        }else {
            List<String> itemList = Lists.newArrayList(items);
            int index = itemList.indexOf(initialValue);
            if(index >= 0) {
                combo.select(index);
            }
        }
        
        if (!allowEmpty) {
            validators.add(new Validator() {
                @Override
                public boolean validate() {
                    return !combo.getText().equals(IInternalWIDECoreConstants.EMPTY_STRING);
                }
            });
            combo.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    validateFields();
                }
            });
        }

        controlComboList.add(combo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        for (Text control : controlList) {
            valueMap.put(control.getData(FIELD_NAME), control.getText());
        }
        for (Combo combox : controlComboList) {
            valueMap.put(combox.getData(FIELD_NAME), combox.getText());
        }
        controlComboList = null;
        controlList = null;
        super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#open()
     */
    @Override
    public int open() {
        applyDialogFont(panel);
        return super.open();
    }

    public Object getValue(String key) {
        return valueMap.get(key);
    }

    public String getStringValue(String key) {
        return (String) getValue(key);
    }

    public void validateFields() {
        for (Validator validator : validators) {
            if (!validator.validate()) {
                getButton(IDialogConstants.OK_ID).setEnabled(false);
                return;
            }
        }
        getButton(IDialogConstants.OK_ID).setEnabled(true);
    }

    protected class FieldSummary {
        int type;
        String name;
        String initialValue;
        boolean allowsEmpty;
        String[] items;

        public FieldSummary(int type, String name, String initialValue, boolean allowsEmpty, String[] items) {
            this.type = type;
            this.name = name;
            this.initialValue = initialValue;
            this.allowsEmpty = allowsEmpty;
            this.items = items;
        }
    }

    protected class Validator {
        boolean validate() {
            return true;
        }
    }
}
