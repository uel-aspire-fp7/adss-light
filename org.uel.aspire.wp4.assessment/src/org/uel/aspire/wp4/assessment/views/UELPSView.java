/**
 * 
 */
package org.uel.aspire.wp4.assessment.views;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.AkbFactory;
import eu.aspire_fp7.adss.akb.Model;

/**
 * @author Gaofeng
 *
 */
public class UELPSView 
extends ViewPart implements ISelectionListener, ISelectionChangedListener
{	
	private ArrayList<ProtectionSolution> pss ;
	private ListViewer viewer;
	//private UELPSLabelProvider labelprovider;
	//private UELPSContentProvider contentprovider;
	
	//private Model model;
	
	private ADSS adss;

	/**
	 * 
	 */
	public UELPSView() {
		// TODO Auto-generated constructor stub
		
		ProtectionFilesIn pfi = new ProtectionFilesIn("aspire-protection/");	
		pss = new ArrayList<ProtectionSolution>();
		pss = pfi.getProtectionSolutions();
		
		//model = ADSS.this.getModel();
				//	AkbFactory.eINSTANCE.createModel();
		//System.out.println(model);
		//System.out.println(System.getProperties());
		//pss.add(ps);
		
		////////////////////////////////
		
		/*
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		//String path = System.getProperty("user.dir") + "MyProject";
		IProject project  = root.getProject("MyProject");
		System.out.println(project.getFullPath() );
		IFolder folder = project.getFolder("Folder1");
		IFile file = folder.getFile("akb.owl");
		try {
			adss = new ADSS(file);
		} catch (Exception e) {
			System.out.println("error to create adss in UEL!");
			e.printStackTrace();
		} 
		*/
		

		//		System.out.println(adss.toString());
		/////////////////////////////////
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
        viewer = new ListViewer(parent, SWT.BORDER);
        viewer.setContentProvider(new UELPSContentProvider());
        viewer.setLabelProvider(new UELPSLabelProvider());
        viewer.setInput(pss);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub
		/*try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		//String path = System.getProperty("user.dir") + "MyProject";
		IProject project  = root.getProject("MyProject");
		System.out.println(project.getFullPath() );
		IFolder folder = project.getFolder("Folder1");
		IFile file = folder.getFile("akb.owl");
		
			adss = new ADSS(file);
		} catch (Exception e) {
			System.out.println("error to create adss setFocus()!");
			e.printStackTrace();
		} */

	}

	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		/*try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		//String path = System.getProperty("user.dir") + "MyProject";
		IProject project  = root.getProject("MyProject");
		System.out.println(project.getFullPath() );
		IFolder folder = project.getFolder("Folder1");
		IFile file = folder.getFile("akb.owl");
		
			adss = new ADSS(file);
		} catch (Exception e) {
			System.out.println("error to create adss!");
			e.printStackTrace();
		} */
		
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
	/*	try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		IWorkspaceRoot root = workspace.getRoot();
		//String path = System.getProperty("user.dir") + "MyProject";
		IProject project  = root.getProject("MyProject");
		System.out.println(project.getFullPath() );
		IFolder folder = project.getFolder("Folder1");
		IFile file = folder.getFile("akb.owl");
		
			adss = new ADSS(file);
		} catch (Exception e) {
			System.out.println("error to create adss!");
			e.printStackTrace();
		} */
		
	}

}
