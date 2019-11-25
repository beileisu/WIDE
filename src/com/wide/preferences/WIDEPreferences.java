package com.wide.preferences;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.wide.preferences.bean.HostConfigurationSettings;
import com.wide.preferences.constans.PreferenceConstans;

@SuppressWarnings({ "rawtypes" })
public final class WIDEPreferences {

    protected static final String KEY_HOSTCONFIGURATIONSETTINGS  = "WIDE.Preferences.HostConfigurationSettings";
    
    protected static final Gson GSON = new Gson();
    
    protected static final String[] variableTableColumnHeaders = { 
            PreferenceConstans.CONFIG_HOST_NAME, 
            PreferenceConstans.CONFIG_WINDCHILL_VERSION, 
            PreferenceConstans.CONFIG_HOST_OS 
    };

    protected static final ColumnLayoutData[] variableTableColumnLayouts = { 
            new ColumnWeightData(45), 
            new ColumnWeightData(30), 
            new ColumnWeightData(25) 
    };

    protected static final String[] WINDCHILL_VERSION_ITEMS = { 
            "Windchill 10.2", 
            "Windchill 11.1" 
    };

    protected static final String[] HOST_OS_ITEMS = { 
            "Windows", 
            "Linux" 
    };

    protected static final String[] variableTableColumnProperties = { 
            PreferenceConstans.CONFIG_HOST_NAME, 
            PreferenceConstans.CONFIG_WINDCHILL_VERSION, 
            PreferenceConstans.CONFIG_HOST_OS 
    };
    
    protected static List<HostConfigurationSettings> getHostConfigurationSettings(IPreferenceStore store) {

        List<HostConfigurationSettings> settings = Lists.newArrayList();
        
        if (store instanceof ScopedPreferenceStore) {
            ScopedPreferenceStore scopedStore = (ScopedPreferenceStore) store;
            String hostConfigurationStr = StringUtils.trimToEmpty(scopedStore.getString(KEY_HOSTCONFIGURATIONSETTINGS));

            if(StringUtils.isNotEmpty(hostConfigurationStr)) {
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(hostConfigurationStr);
                if (element.isJsonArray()) {
                    JsonArray jsonArray = element.getAsJsonArray();
                    Iterator it = jsonArray.iterator();
                    while (it.hasNext()) {
                        JsonPrimitive primitive = (JsonPrimitive) it.next();
                        String string = primitive.getAsString();
                        HostConfigurationSettings bean = GSON.fromJson(string, HostConfigurationSettings.class);
                        settings.add(bean);
                    }
                }
            }
        }
        return settings;
    }
    
    protected static void saveHostConfigurationSettings(IPreferenceStore store, List<HostConfigurationSettings> settings) {
        
        settings = settings.stream().distinct().collect(Collectors.toList());
        
        saveSettingsOp(store, settings);
    }
    
    protected static void saveHostConfigurationSettings(IPreferenceStore store, HostConfigurationSettings setting) {
        
        List<HostConfigurationSettings> settings = getHostConfigurationSettings(store);
        settings.add(setting);
        settings = settings.stream().distinct().collect(Collectors.toList());
        
        saveSettingsOp(store, settings);
    }
    
    protected static void saveSettingsOp(IPreferenceStore store, List<HostConfigurationSettings> settings) {
        JsonArray jsonArray = new JsonArray();
        settings.forEach(item -> {
            jsonArray.add(GSON.toJson(item));
        });

        String settingsArrayStr = jsonArray.toString();

        if (store instanceof ScopedPreferenceStore) {
            ScopedPreferenceStore scopedStore = (ScopedPreferenceStore) store;
            scopedStore.setValue(KEY_HOSTCONFIGURATIONSETTINGS, settingsArrayStr);
            try {
                scopedStore.save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        
    
    protected static void removeHostConfigurationSettings(IPreferenceStore store, List<HostConfigurationSettings> removeSettings) {
        List<HostConfigurationSettings> settings = getHostConfigurationSettings(store);
        settings.removeAll(removeSettings);
        
        saveHostConfigurationSettings(store, settings);
    }
    
    
}
