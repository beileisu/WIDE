package com.wide.ui;

import org.eclipse.core.variables.IStringVariable;
import org.eclipse.swt.widgets.Shell;


/**
 * A variable presentation extension can contribute an argument selector
 * which is use to configure the argument for a string substitution
 * variable.
 *
 * @since 3.9
 */
public interface IArgumentSelector {

	/**
	 * Selects and returns an argument for the given variable,
	 * or <code>null</code> if none.
	 *
	 * @param variable the variable an argument is being selected for
	 * @param shell the shell to create any dialogs on, or <code>null</code> if none
	 * @return argument for the given variable or <code>null</code>
	 *  if none
	 */
	String selectArgument(IStringVariable variable, Shell shell);
}
