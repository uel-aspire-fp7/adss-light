package org.uel.aspire.wp4.assessment;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.pnml.tools.epnk.actions.framework.jobs.AbstractEPNKAction;
import org.pnml.tools.epnk.actions.framework.jobs.AbstractEPNKJob;
//import org.pnml.tools.epnk.*;
//import org.pnml.tools.epnk.functions.*;
//import org.eclipse.emf.ecore.*;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetType;
import org.pnml.tools.epnk.pntypes.ptnet.PTNet;
import org.uel.aspire.wp4.data.AttackerCap;

public class AttackAssessmentAction extends AbstractEPNKAction {
	
	AttackAssessmentJob aaj;
	ArrayList<AttackerCap> attackers;
	@Override
	public boolean isEnabled(PetriNet petrinet) {
		//if (petrinet != null) {
		//	PetriNetType type = petrinet.getType();
		//	return type != null && type instanceof PTNet;
		//}
		//return false;
		return true;
	}
	
	public AbstractEPNKJob createJob(PetriNet petrinet, String defaultInput, ArrayList<AttackerCap> atin) {
		
		/*MessageDialog.openInformation(
				null,
				"ePNK: Attack Assessment by UEL",
				"The Assessment will start in action! \n\r"					
		);*/
		attackers = new ArrayList<AttackerCap>();
		attackers = atin; 
		if (attackers==null) System.out.println("attackers==null in AttackAssessmentaction" );
		//System.out.print("action"); System.out.println(attackers);
		aaj = new AttackAssessmentJob(petrinet,defaultInput, attackers);
		return aaj;
	}

	public double do_job() // this is a new method to run out algorithm 
	{
		double result =0.0;
		if (aaj.prepare() ) 
			{ aaj.run(); result = aaj.getresult();}
		return result;
	}

	@Override
	public AbstractEPNKJob createJob(PetriNet petrinet, String defaultInput) {
		// TODO Auto-generated method stub
		return null;
	}
}
