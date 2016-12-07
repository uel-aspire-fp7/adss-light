package org.uel.aspire.wp4.assessment.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.uel.aspire.wp4.data.DataforView;
import org.uel.aspire.wp4.data.DataforView.Set;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;

public class UELEditingSupport extends EditingSupport implements SelectionListener{
	
	private ComboBoxViewerCellEditor cellEditor = null;

	
	public UELEditingSupport(ColumnViewer viewer) {
		super(viewer);
		// TODO Auto-generated constructor stub
		 cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
	     cellEditor.setLabelProvider(new LabelProvider());
	     cellEditor.setContentProvider(new ArrayContentProvider());
	     cellEditor.setInput(Set.values());
	     //System.out.println("in UELEditingSupport, viewer.toString() is :  " + viewer.toString());
	     //cellEditor.setLabelProvider(viewer.getLabelProvider());
	     //cellEditor.setContentProvider((IStructuredContentProvider) viewer.getContentProvider());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		// TODO Auto-generated method stub
		return cellEditor;		
	}

	@Override
	protected boolean canEdit(Object element) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		// TODO Auto-generated method stub
	      if (element instanceof DataforView) {
	    	  DataforView data = (DataforView)element;
	            return data.getData();
	        }
	      //System.out.println("in UELEditingSupport, getValue() return null");
		return null;
	}

	@Override
	protected void setValue(Object element, Object value) {
		// TODO Auto-generated method stub
	  if (element instanceof DataforView && value instanceof Set) {
		  DataforView data = (DataforView) element;
	           Set newValue = (Set) value;
	            /* only set new value if it differs from old one */
	            if (!((DataforView) data).getData().equals(newValue)) {
	                ((DataforView) data).setData(newValue);
	            }
	      }
	}

	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("in UELEditingSupport, widgetSelected(SelectionEvent e) started! ");
		if(e.getSource() == cellEditor)
		{}
		
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
