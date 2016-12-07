/**
 * 
 */
package org.uel.aspire.wp4.assessment.views;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.comparetoolchains.MeasureFileIn;

/**
 * @author Gaofeng
 *
 */
public class UELMetricView extends ViewPart implements ISelectionListener, ISelectionChangedListener {

	/**
	 * 
	 * 
	 */
	private ArrayList<Measure> measures;
	private ListViewer viewer;
	private UELMetricsLabelProvider labelprovider;
	private UELMetricsContentProvider contentprovider;
	
	public UELMetricView() throws IOException {
		// TODO Auto-generated constructor stub
		
		MeasureFileIn mfi = new MeasureFileIn("aspire-config/Measures.txt");
		measures = mfi.getMeasures();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		
        viewer = new ListViewer(parent, SWT.BORDER);
        viewer.setContentProvider(new UELMetricsContentProvider());
        viewer.setLabelProvider(new UELMetricsLabelProvider());
        viewer.setInput(measures);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

}
