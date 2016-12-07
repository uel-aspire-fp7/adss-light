package org.uel.aspire.spa;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The commands wizard page.
 * @author Gaofeng Zhang
 **/
public class SPAProjectPage extends WizardPage
{
	/**
	 * Creates the page.
	 **/
	protected SPAProjectPage()
	{
		super("Create an AKB-light file and a pnml file");
		setTitle("Create an AKB-light file and a pnml file");
		//setDescription("Select the external commands to use.");
		setPageComplete(true);
	}

	public SPAProjectPage(String string) {
		super(string);
		setTitle(string);
		setPageComplete(true);
	}

	/**
	 * Fills the page.
	 * @param parent
	 *            The parent composite.
	 **/
	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Label txt1 = new Label(container,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		txt1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		txt1.setText("This Wizard will generate one SPA project with a AKB-light file and a pnml file");	

	}


}
