package com.wide.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WIDERootPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench workbench) {
		System.out.println("RootPreferencePage.innit : " + workbench);

	}

	@Override
	protected Control createContents(Composite parent) {
		System.out.println("RootPreferencePage.createContents : " + parent);

		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayout(new RowLayout());
		new Label(topComp, SWT.NONE).setText("Welcome To Use WIDE");
		return topComp;
	}

}
