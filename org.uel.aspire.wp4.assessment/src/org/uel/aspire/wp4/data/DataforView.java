package org.uel.aspire.wp4.data;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IViewSite;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;

public class DataforView {
	  public static enum Set {
		    set1, set2, set3, set4, set5, set6, set7, set8, set9, set10;
		  }
	  	
		  private Set value;
		  private ISelectionProvider iselectionprovider = null;
		  private Transition transition = null;
		  private Place place = null;
		  private Arc arc = null;
		         
		  public DataforView(ISelectionProvider iselectionproviderin, Set data) {	
			  
			  this.value = data;
		    this.iselectionprovider = iselectionproviderin;
		    if(iselectionprovider instanceof Transition)
		    	this.transition = (Transition) iselectionprovider;
		    if(iselectionprovider instanceof Place)
		    	this.place = (Place) iselectionprovider;
		    if(iselectionprovider instanceof Arc)
		    	this.arc = (Arc) iselectionprovider;
		  }
		  
		  public DataforView(Transition trin, Set data) {	
			  
			    this.value = data;
			    this.transition = trin;
			    //this.iselectionprovider = (ISelectionProvider) trin;
		  }
		  
		  public DataforView(Place plin, Set data) {		 
			    this.value = data;
			    this.place = plin;			  
		  }
		  
		  public DataforView(Arc arin, Set data) {		 
			    this.value = data;
			    this.arc = arin;			  
		  }
		  
   
		  public Set getData() {
		    return value;
		  }
		         
		  public void setData(Set data) {
		    this.value = data;
		  }
		  
		  public ISelectionProvider getISelectionProvider()
		  {
			  return iselectionprovider;
		  }
		  
		  public void setISelectionProvider(ISelectionProvider isin)
		  {
			  this.iselectionprovider = isin;
		  }
		  
		  public Place getPlace()
		  {
			  return this.place;
		  }
		  
		  public Transition getTransition()
		  {
			  return this.transition;
		  }
		  public Arc getArc()
		  {
			  return this.arc;
		  }
		    

}
