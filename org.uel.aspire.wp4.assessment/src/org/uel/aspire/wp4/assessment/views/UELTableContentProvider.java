package org.uel.aspire.wp4.assessment.views;

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

public class UELTableContentProvider implements IStructuredContentProvider{
	
	private TableViewer viewer;
	private ArrayList<Object> contents;

	
	public UELTableContentProvider()
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
		
		/*if (element instanceof Transition)
		{
			Transition trans = (Transition) element;
			switch (columnIndex)
			{
			case 0: return trans.getId().toString();
			case 1: return trans.getName().toString();
			case 2: return trans.getIn().get(0).getId().toString();
			case 3: return trans.getOut().get(0).getId().toString();
			case 4: return trans.getClass().getSimpleName().toString();
			}
			//contents.add(trans);
		}
		
		if (element instanceof Place)
		{
			Place place = (Place) element;
			switch (columnIndex)
			{
			case 0: return place.getId();
			case 1: return place.getName().toString();
			case 2: return place.getIn().toString();
			case 3: return place.getOut().toString();
			}
		}
		
		if (element instanceof Arc)
		{
			Arc arc = (Arc) element;
			switch (columnIndex)
			{
			case 0: return arc.getId();
			case 1: return arc.getName().toString();
			case 2: return arc.getSource().toString();
			case 3: return arc.getTarget().toString();
			}
		}*/
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
		// TODO Auto-generated method stub
		
		//return (Object[]) inputElement.toString();
		//contents.add(inputElement);
		//System.out.println("in UELTableContentProvider, inputElement.getClass() is : " + inputElement.getClass());
		
		if (inputElement instanceof DataforView)
		{
			//System.out.println("in UELTableContentProvider, inputElement instanceof DataforView");
			//if (((DataforView) inputElement).getISelectionProvider() !=null)
			//	System.out.println("in UELTableContentProvider, ((DataforView) inputElement).getISelectionProvider().getClass() is : " + ((DataforView) inputElement).getISelectionProvider().getClass());
			
			if ( ((DataforView) inputElement).getTransition() != null)
			{
				contents.clear();
				contents.add(inputElement);
			
			//contents.add(((Transition) inputElement).getId().toString()); 
			 //contents.add(inputElement.getClass().toString()); 
			// contents.add(((Transition) inputElement).getIn().toString()); 
			//contents.add(((Transition) inputElement).getOut()); 
			// contents.add(((Transition) inputElement).getGraphics().toString()); 
			
			//Vector v = (Vector) inputElement;
			//return v.toArray();		
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
			//System.out.println("in UELTableContentProvider, inputElement instanceof DataforView[] ");
			
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
		//objs[0] = ((Transition) inputElement).getId();
		//System.out.println("In UELTableContentProvider, getElements() return : " + objs.length);
		return objs;	

	}
}