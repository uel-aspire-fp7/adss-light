package org.uel.aspire.wp4.assessment.views;

//import javax.swing.text.TableView;

import java.util.List;

import javax.swing.text.View;

import org.pnml.tools.epnk.diagram.edit.parts.*;
import org.pnml.tools.epnk.gmf.extensions.graphics.figures.TransitionFigure;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
//import org.eclipse.gef.EditPart;
//import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
//import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IShowInSource;
import org.eclipse.ui.part.ViewPart;


import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.uel.aspire.wp4.data.DataforView;
import org.uel.aspire.wp4.data.DataforView.Set;

public class UELView extends ViewPart implements ISelectionListener, ISelectionChangedListener{

	private IShowInSource resource;
	private TableViewer viewer;
	private UELTableLabelProvider labelprovider;
	private UELTableContentProvider contentprovider;
		
	public UELView()
	{
		super();
		labelprovider = new UELTableLabelProvider();
		contentprovider = new UELTableContentProvider();		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub	
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(2));
		Table table = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayout(tableLayout);
		
		//TableViewer tableviewer = new TableViewer(table);
		viewer = new TableViewer(table);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableViewerColumn column0 = new TableViewerColumn(viewer, SWT.NONE);
		column0.getColumn().setText("ID");
		//column0.getViewer().
		TableViewerColumn column1 = new TableViewerColumn(viewer, SWT.NONE);
		column1.getColumn().setText("NAME");
		TableViewerColumn column2 = new TableViewerColumn(viewer, SWT.NONE);
		column2.getColumn().setText("ARCS IN");
		TableViewerColumn column3 = new TableViewerColumn(viewer, SWT.NONE);
		column3.getColumn().setText("ARCS OUT");
		TableViewerColumn column4 = new TableViewerColumn(viewer, SWT.NONE);
		column4.getColumn().setText("MEASURABLE FEATURES");

		viewer.setContentProvider(contentprovider);
		viewer.setLabelProvider(labelprovider);
		
		getSite().getPage().addSelectionListener(this);
		selectionChanged(null, getSite().getPage().getSelection());
		viewer.addSelectionChangedListener(this);
		
		//DataforView[] input = new DataforView[]{new DataforView(getViewSite().getSelectionProvider(), Set.set1)};
		DataforView[] input = new DataforView[]{new DataforView(getViewSite().getSelectionProvider(), Set.set1)};
		viewer.setInput(input);
		
		//column0.getColumn().setData(input);	
		//System.out.println(column0.getColumn().getData());
		//column1.getColumn().setData(input);	
		//System.out.println(column1.getColumn().getData());
		//column2.getColumn().setData(input);	
		//System.out.println(column2.getColumn().getData());
		//column3.getColumn().setData(input);	
		//System.out.println(column3.getColumn().getData());
		column4.getColumn().setData(input);	
		//System.out.println(column4.getColumn().getData());
		
		UELEditingSupport uelEditingSupport = new UELEditingSupport(column4.getViewer());
		column4.setEditingSupport(uelEditingSupport);
		
		
		//viewer = tableviewer;
		
		/*viewer = new TableViewer(parent, SWT.SINGLE| SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		getSite().getPage().addSelectionListener(this);
		selectionChanged(null, getSite().getPage().getSelection());
		viewer.addSelectionChangedListener(this);
				
		viewer.setLabelProvider(labelprovider);
		viewer.setContentProvider(contentprovider);			
		viewer.setInput(getViewSite()); 
		
		String[] columnNames = new String[]{
				"ID", "NAME" , "ARCS IN", "ARCS OUT", "MEASURABLE FEATURES"
		};
		int[] columnWidths = new int[]{
				150, 150, 300, 300, 300
		};
		int[] columnAlignments = new int[]{
				SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER, SWT.CENTER
		};
		for(int i = 0 ; i < columnNames.length; i++)
		{
			TableColumn tableColumn = new TableColumn(table, columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);	
			tableColumn.setData(getViewSite());			
			
		}		
	*/
			
	//	System.out.println("viewer.getLabelProvider().getClass() is : " + viewer.getLabelProvider());
	//	System.out.println("parent.getData() is : " + parent.getData());
		
	}
	

	public void dispose()
	{
		super.dispose();
        getSite().getPage().removeSelectionListener(this);
	}
	
	public void saveState(IMemento memento)
	{

	}
	
	public Object getAdapter(Class adapter)
	{
		if (adapter.isInstance(resource))
			return resource;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public void setFocus() {
		
		viewer.getControl().setFocus();
		
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
	
		//System.out.println("In UELView, selectionChanged(), started!  ");
		if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
			IStructuredSelection structured = (IStructuredSelection) selection;
				
			
			Object first = structured.getFirstElement();
		
			//System.out.println("In UELView, selectionChanged(), first.getClass().getName() is : "+ first.getClass().getName());
			//Object first = structured;
			//if (first != null)	System.out.println("In UELView, selectionChanged(), first.getClass() is : "+ first.getClass());
		
			if(first instanceof org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart )
			{
				//System.out.println("In UELView, selectionChanged(), first instanceof org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ");
				//(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).setBackgroundColor(new Color(null, 0, 0, 0));
				//System.out.println("transition id is "+ ((TransitionFigure)(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getFigure()).transition.getId() );
				//System.out.println("model is "+ ((( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getModel()).getClass() );
				//System.out.println("figure is "+ ((( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getFigure()).getClass() );
				//System.out.println("ContentPane is "+ ((( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getContentPane()).getClass() );
				//System.out.println("transition name  is "+ ((TransitionFigure)(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getContentPane()).transition.getName().getText() );
			}
			
			if (first instanceof Transition) {
				//System.out.println("In UELView, selectionChanged(), first instanceof Transition");
				//viewer.setInput(getTransitionInfo((Transition) first));	
				viewer.setInput(new DataforView((Transition) first, Set.set1));
			}
			/*if (first instanceof TransitionEditPart) {
	
				viewer.setInput(new DataforView((TransitionEditPart) first, Set.set1));
			}*/
			if (first instanceof Place) {
				viewer.setInput(new DataforView((Place) first, Set.set1));
			}
			/*if (first instanceof PlaceEditPart) {
				viewer.setInput(new DataforView((Place) first, Set.set1));
			}*/
			if (first instanceof Arc) {
				viewer.setInput(new DataforView((Arc) first, Set.set1));
			}
			/*if (first instanceof ArcEditPart) {
				viewer.setInput(new DataforView((Arc) first, Set.set1));
			}*/
			
			/*
			if(first instanceof TransitionEditPart)
			{
				TransitionEditPart part1 = (TransitionEditPart) first;
				
				System.out.println("part1.getClass() is : "+ part1.getClass());
				Object model = part1.getModel();
			//	System.out.println("model.getClass() is : "+ model.getClass());
			//	model = part1.getViewer();
			//	System.out.println("model.getClass() is : "+ model.getClass());
				if( model != null && model instanceof View )
				{					
					EObject object = (EObject) ((View)model).getElement();
					System.out.println(" object is : "+  object);
					//if(object != null && object instanceof Transition)
					//	viewer.setInput(new DataforView((Transition) object, Set.set1));
				}
			}
			*/
					
			//////////////////////////////////
		//	if (first!=null && first.getClass() != null)	 
		//	{
		//	if (first.getClass().toString().contains("Transition") == true) {
				//System.out.println("In UELView, selectionChanged(), first instanceof Transition");
				//viewer.setInput(getTransitionInfo((Transition) first));	
				//viewer.setInput(new DataforView((Transition) first, Set.set1));
		//	}
		//	if (first.getClass().toString().contains("Place") == true) {
				//viewer.setInput(new DataforView((Place) first, Set.set1));
		//	}
		//	if (first.getClass().toString().contains("Arc") == true) {
				//viewer.setInput(new DataforView((Arc) first, Set.set1));
		//	}
		//	}
			////////////////////////////////////
		}		
	}
	
	public IStructuredSelection getSelection()
	{
		return  (IStructuredSelection) viewer.getSelection();
	}
	
	
	public DataforView getTransitionInfo(Transition transin) {
		//String[] result = {"No Transition selected"};
		DataforView ne = new DataforView(transin, Set.set1);
		//System.out.println("In UELView, getTransitionInfo() is : " + transin.getId());
		//System.out.println("In UELView, getTransitionInfo() return is : " + ((Transition) ne.getISelectionProvider()).getId());
		return ne;		
	}
	
	public DataforView getPlaceInfo(Place placein) {
		//String[] result = {"No Place selected"};
		DataforView ne = new DataforView(placein, Set.set1);
		return ne;}
	
	public DataforView getArcInfo(Arc arcin) {
		//String[] result = {"No Arc selected"};
		DataforView ne = new DataforView(arcin, Set.set1);
		return ne;
		}

	public void selectionChanged(SelectionChangedEvent event) {
		
		//System.out.println("In UELView, method: selectionChanged(SelectionChangedEvent event) started !" );
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structured = (IStructuredSelection) selection;
			
			Object first = structured.getFirstElement();
			//System.out.println(first.getClass());
			
			if (first instanceof Transition) {
				viewer.setInput(new DataforView((Transition) first, Set.set1));
			}
			if (first instanceof Place) {
				viewer.setInput(new DataforView((Place) first, Set.set1));
			}
			if (first instanceof Arc) {
				viewer.setInput(new DataforView((Arc) first, Set.set1));
			}
		}	
		
	}

}
