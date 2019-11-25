package com.wide.preferences;

import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;

import com.wide.preferences.constans.PreferenceConstans;

public final class WIDEPreferences {

    protected static final String[] variableTableColumnHeaders = { 
            PreferenceConstans.CONFIG_HOST_NAME, 
            PreferenceConstans.CONFIG_WINDCHILL_VERSION, 
            PreferenceConstans.CONFIG_SYSTEM_TYPE 
    };

    protected static final ColumnLayoutData[] variableTableColumnLayouts = { 
            new ColumnWeightData(45), 
            new ColumnWeightData(30), 
            new ColumnWeightData(25) 
    };

    protected static final String[] windchillVersionItems = { 
            "Windchill 10.2", //$NON-NLS-1$
            "Windchill 11.1" 
    };

    protected static final String[] systemTypeItems = { 
            "Winddows", //$NON-NLS-1$
            "Linux" 
    };

    protected static final String[] variableTableColumnProperties = { 
            PreferenceConstans.CONFIG_HOST_NAME, 
            PreferenceConstans.CONFIG_WINDCHILL_VERSION, 
            PreferenceConstans.CONFIG_SYSTEM_TYPE 
    };
    
    
}
