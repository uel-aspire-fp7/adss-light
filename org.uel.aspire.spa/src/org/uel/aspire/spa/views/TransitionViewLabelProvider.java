package org.uel.aspire.spa.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.uel.aspire.wp4.data.DataforView;


public class TransitionViewLabelProvider extends LabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		
		return getImage(element);
	}

	public String getColumnText(Object element, int columnIndex) {
	
		Object obj = element;
		
		if ( ((DataforView) obj).getTransition() != null)
		{
			
			Transition trans = ((DataforView) obj).getTransition();
			switch (columnIndex)
			{
			case 0: return trans.getId();
			case 1: return trans.getName().getText();
			case 2: {String re = ""; for (int i = 0;i<trans.getIn().size();i++) {re= re+" "+trans.getIn().get(i).getId() +" (Place: " + trans.getIn().get(i).getTarget().getId()+" ) ";} return re;}
			case 3: {String re = ""; for (int i = 0;i<trans.getOut().size();i++) {re= re+" "+trans.getOut().get(i).getId() +" (Place: " + trans.getOut().get(i).getTarget().getId()+" ) ";} return re;}
			case 4: return ((DataforView) element).getData().name();
			default: return"";
			}
		}
		
		if (((DataforView) obj).getPlace() != null)
		{
			Place place = ((DataforView) obj).getPlace();
			switch (columnIndex)
			{
			case 0: return place.getId();
			case 1: return place.getName().getText();
			case 2: {String re = ""; if (place.getIn().size() == 0) return "This is the start place. "; for (int i = 0;i<place.getIn().size();i++) {re= re+" "+place.getIn().get(i).getId() +" (Transition: " + place.getIn().get(i).getTarget().getId()+" ) ";} return re;}
			case 3: {String re = ""; if (place.getOut().size() == 0) return "This is the end place. "; for (int i = 0;i<place.getOut().size();i++) {re= re+" "+place.getOut().get(i).getId() +" (Transition: " + place.getOut().get(i).getTarget().getId()+" ) ";} return re;}
			case 4: return ((DataforView) element).getData().name();
			default: return"";
			}
		}
		
		if (((DataforView) obj).getArc() != null)
		{
			Arc arc = (Arc) ((DataforView) obj).getArc();
			switch (columnIndex)
			{
			case 0: return arc.getId();
			case 1: return arc.getClass().getSimpleName();
			case 2: {String re = ""; if (arc.getSource() instanceof Transition) re = "Transition: "+ arc.getSource().getId() ; else re = "Place: "+ arc.getSource().getId(); return re;}
			case 3: {String re = ""; if (arc.getTarget() instanceof Transition) re = "Transition: "+ arc.getTarget().getId() ; else re = "Place: "+ arc.getTarget().getId(); return re;}
			case 4: return arc.getUnknown().toString();
			default: return"";
			}
		}
		
		return getText(element);
	}
	
    public Image getImage(Object element) {
        return PlatformUI.getWorkbench().getSharedImages()
            .getImage(ISharedImages.IMG_OBJ_ELEMENT);
      }

}
