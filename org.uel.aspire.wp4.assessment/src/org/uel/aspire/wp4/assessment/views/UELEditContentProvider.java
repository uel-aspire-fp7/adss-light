/**
 * 
 */
package org.uel.aspire.wp4.assessment.views;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;

/**
 * @author Gaofeng
 *
 */
public class UELEditContentProvider implements IStructuredContentProvider{
	
	private ArrayList<ProtectionSolution> pss;
	ListViewer viewer;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		pss = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		viewer = (ListViewer)viewer;
	    pss = (ArrayList<ProtectionSolution>)newInput;
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return pss.toArray();
	}

}
