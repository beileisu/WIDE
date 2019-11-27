package com.wide.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.wide.ui.constants.WIDEUIConstants;

public class WIDEUIPlugin extends AbstractUIPlugin {

    /**
     * The singleton debug plug-in instance
     */
    private static WIDEUIPlugin wideUIPlugin = null;

    /**
     * Returns the singleton instance of the debug plug-in.
     * 
     * @return the singleton {@link DebugUIPlugin}
     */
    public static WIDEUIPlugin getDefault() {
        if (wideUIPlugin == null) {
            wideUIPlugin = new WIDEUIPlugin();
        }
        return wideUIPlugin;
    }

    /**
     * Returns the currently active workbench window shell or <code>null</code> if
     * none.
     *
     * @return the currently active workbench window shell or <code>null</code>
     */
    public static Shell getShell() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            if (windows.length > 0) {
                return windows[0].getShell();
            }
        } else {
            return window.getShell();
        }
        return null;
    }

    /**
     * Utility method with conventions
     * 
     * @param shell   the shell to open the dialog on
     * @param title   the title of the dialog
     * @param message the message to display in the dialog
     * @param s       the underlying {@link IStatus} to display
     */
    public static void errorDialog(Shell shell, String title, String message, IStatus s) {
        // if the 'message' resource string and the IStatus' message are the same,
        // don't show both in the dialog
        if (s != null && message.equals(s.getMessage())) {
            message = null;
        }
        ErrorDialog.openError(shell, title, message, s);
    }

    /**
     * Utility method with conventions
     * 
     * @param shell   the shell to open the dialog on
     * @param title   the title for the dialog
     * @param message the message to display in the dialog
     * @param t       the underlying exception for the dialog
     */
    public static void errorDialog(Shell shell, String title, String message, Throwable t) {
        IStatus status;
        if (t instanceof CoreException) {
            status = ((CoreException) t).getStatus();
            // if the 'message' resource string and the IStatus' message are the same,
            // don't show both in the dialog
            if (status != null && message.equals(status.getMessage())) {
                message = null;
            }
        } else {
            status = new Status(IStatus.ERROR, getUniqueIdentifier(), WIDEUIConstants.INTERNAL_ERROR, "Error within Debug UI: ", t); //$NON-NLS-1$
            log(status);
        }
        ErrorDialog.openError(shell, title, message, status);
    }

    /**
     * Convenience method which returns the unique identifier of this plug-in.
     * 
     * @return the identifier of the plug-in
     */
    public static String getUniqueIdentifier() {
        return WIDEUIConstants.PLUGIN_ID;
    }

    /**
     * Logs the specified status with this plug-in's log.
     *
     * @param status status to log
     */
    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }

    /**
     * Logs the specified throwable with this plug-in's log.
     *
     * @param t throwable to log
     */
    public static void log(Throwable t) {
        log(newErrorStatus("Error logged from Debug UI: ", t)); //$NON-NLS-1$
    }

    /**
     * Returns a new error status for this plug-in with the given message
     * 
     * @param message   the message to be included in the status
     * @param exception the exception to be included in the status or
     *                  <code>null</code> if none
     * @return a new error status
     */
    public static IStatus newErrorStatus(String message, Throwable exception) {
        return new Status(IStatus.ERROR, getUniqueIdentifier(), WIDEUIConstants.INTERNAL_ERROR, message, exception);
    }

    /**
     * Logs an internal error with the specified message.
     *
     * @param message the error message to log
     */
    public static void logErrorMessage(String message) {
        // this message is intentionally not internationalized, as an exception may
        // be due to the resource bundle itself
        log(newErrorStatus("Internal message logged from Debug UI: " + message, null)); //$NON-NLS-1$
    }

    /**
     * Returns the workbench's display.
     * 
     * @return the standard display
     */
    public static Display getStandardDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
    }

}
