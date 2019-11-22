package com.wide.internal.core;

import org.eclipse.core.runtime.IStatus;

import com.wide.ui.WIDEUIPlugin;

public interface IInternalWIDECoreConstants {

	/**
	 * Represents the empty string
	 */
	String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * Boolean preference controlling whether status handler extensions
	 * are enabled. Default value is <code>true</code>. When disabled
	 * any call to {@link DebugPlugin#getStatusHandler(IStatus)} will return <code>null</code>.
	 *
	 * @since 3.4.2
	 */
	String PREF_ENABLE_STATUS_HANDLERS = WIDEUIPlugin.getUniqueIdentifier() + ".PREF_ENABLE_STATUS_HANDLERS"; //$NON-NLS-1$

	/**
	 * Persistence of breakpoint manager enabled state.
	 *
	 * @since 3.6
	 */
	String PREF_BREAKPOINT_MANAGER_ENABLED_STATE =  WIDEUIPlugin.getUniqueIdentifier() + ".PREF_BREAKPOINT_MANAGER_ENABLED_STATE"; //$NON-NLS-1$

	
}
