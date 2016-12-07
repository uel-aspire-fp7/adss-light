package org.uel.aspire.wp4.pnassessment;

import org.pnml.tools.epnk.actions.framework.jobs.AbstractEPNKAction;
import org.pnml.tools.epnk.actions.framework.jobs.AbstractEPNKJob;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;

public class PNAssessmentAction extends AbstractEPNKAction {
	
	PNAssessmentJob paj;
	
	@Override
	public boolean isEnabled(PetriNet petrinet) {

		return true;
	}
	
	@Override
	public AbstractEPNKJob createJob(PetriNet petrinet, String defaultInput) {
		
		paj = new PNAssessmentJob(petrinet,defaultInput);
		
		return paj;
	}
	
	public void do_job()
	{
		if (paj.prepare()) paj.run();
	}

}
