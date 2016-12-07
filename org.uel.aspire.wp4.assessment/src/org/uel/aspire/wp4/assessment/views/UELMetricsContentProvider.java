package org.uel.aspire.wp4.assessment.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.uel.aspire.wp4.assessment.APIs.Measure;

public class UELMetricsContentProvider implements IStructuredContentProvider{

	private ArrayList<Measure> measures;
	ListViewer viewer;
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		measures = null;
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		viewer = (ListViewer)viewer;
	    measures = (ArrayList<Measure>)newInput;
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return measures.toArray();
	}

}
