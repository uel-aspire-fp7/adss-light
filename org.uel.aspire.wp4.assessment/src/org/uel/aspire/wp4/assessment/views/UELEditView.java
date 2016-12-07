/**
 * 
 */
package org.uel.aspire.wp4.assessment.views;

import java.util.ArrayList;
import java.lang.Object;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.comparetoolchains.MeasureFileIn;
import org.uel.aspire.wp4.comparetoolchains.ProtectionFileIn;
import org.uel.aspire.wp4.comparetoolchains.ProtectionFilesIn;


import org.pnml.tools.epnk.pnmlcoremodel.*;

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.AkbFactory;
import eu.aspire_fp7.adss.akb.Model;

/**
 * @author Gaofeng
 *
 */
public class UELEditView extends ViewPart implements ISelectionListener, ISelectionChangedListener
{	
	//private ArrayList<ProtectionSolution> pss ;
	PetriNet pt;
	//private ListViewer viewer;	
	
	private ADSS adss;
	
	Transition tr;
	Place pl;
	Arc ar;

	/**
	 * 
	 */
	public UELEditView() {
		// TODO Auto-generated constructor stub
		/*
		ProtectionFilesIn pfi = new ProtectionFilesIn("aspire-protection/");	
		pss = new ArrayList<ProtectionSolution>();
		pss = pfi.getProtectionSolutions();*/		
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
       // viewer = new ListViewer(parent, SWT.BORDER);
        //viewer.setContentProvider(new UELEditContentProvider());
        //viewer.setLabelProvider(new UELEditLabelProvider());
        //viewer.setInput(pss);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {		

	}

	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		if (event.getSelectionProvider().getSelection() instanceof Transition)
		{
			tr = (Transition) event.getSelectionProvider().getSelection();
			//viewer.setInput(tr);
		}
		if (event.getSelectionProvider().getSelection() instanceof Place)
		{
			pl = (Place) event.getSelectionProvider().getSelection();
			//viewer.setInput(pl);
		}
		if (event.getSelectionProvider().getSelection() instanceof Arc)
		{
			ar = (Arc) event.getSelectionProvider().getSelection();
			//viewer.setInput(ar);
		}
		
		
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		
		if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
			IStructuredSelection structured = (IStructuredSelection) selection;				
			
			Object first = structured.getFirstElement();
			
		if (first instanceof Transition)
		{
			tr = (Transition) first;
			//viewer.setInput(tr);
		}
		if (first instanceof Place)
		{
			pl = (Place) first;
			//viewer.setInput(pl);
		}
		if (first instanceof Arc)
		{
			ar = (Arc) first;
			//viewer.setInput(ar);
		}
		}
	
		
	}

}
