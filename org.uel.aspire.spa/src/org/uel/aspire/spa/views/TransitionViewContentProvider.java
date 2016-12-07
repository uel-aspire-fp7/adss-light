package org.uel.aspire.spa.views;

import java.util.ArrayList;


import org.eclipse.jface.viewers.IContentProvider;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.uel.aspire.wp4.data.DataforView;
import org.eclipse.jface.viewers.TableViewer;

public class TransitionViewContentProvider implements IStructuredContentProvider{
	
	private TableViewer viewer;
	private ArrayList<Object> contents;

	
	public TransitionViewContentProvider()
	{
		contents = new ArrayList<Object>();
		//contents = (ArrayList<Object>) this.getSelection();
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer viewerin, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		this.viewer = (TableViewer) viewerin; 		
	}

	public Object[] getElements(Object inputElement) {
		
		
		if (inputElement instanceof DataforView)
		{
			
			if ( ((DataforView) inputElement).getTransition() != null)
			{
				contents.clear();
				contents.add(inputElement);
			
		
			}
			if ( ((DataforView) inputElement).getPlace() != null)
			{
				contents.clear();
				contents.add(inputElement);
			}
			if ( ((DataforView) inputElement).getArc() != null)
			{
				contents.clear();
				contents.add(inputElement);
			}
		}
		if (inputElement instanceof DataforView[])
		{
			
			if ( ((DataforView[]) inputElement)[0].getISelectionProvider() instanceof Transition)
			{
				contents.clear();
				contents.add(((DataforView[])inputElement)[0]);		
				
			}
			if (((DataforView[]) inputElement)[0].getISelectionProvider()  instanceof Place)
			{
				contents.clear();
				contents.add(((DataforView[])inputElement)[0]);
			}
			if (((DataforView[]) inputElement)[0].getISelectionProvider() instanceof Arc)
			{
				contents.clear();
				contents.add(((DataforView[])inputElement)[0]);
			}
		}
		Object[] objs = contents.toArray();

		return objs;	

	}
}