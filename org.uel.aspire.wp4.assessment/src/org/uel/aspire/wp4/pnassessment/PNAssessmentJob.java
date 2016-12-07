package org.uel.aspire.wp4.pnassessment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.pnml.tools.epnk.actions.framework.jobs.AbstractEPNKJob;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;

import org.uel.aspire.wp4.assessment.SingleSimulation;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.EffortInTrans;
import org.uel.aspire.wp4.data.FileInAttacker;
///
public class PNAssessmentJob extends AbstractEPNKJob {
	
	private HashMap<String, EffortInTrans> efforts1;
	private HashMap<String, EffortInTrans> efforts2;
	private ArrayList<AttackerCap> attackers;
		
	public PNAssessmentJob(PetriNet petrinet, String defaultInput) {
		super(petrinet, "ePNK-uel: Attck Assessment job");
		
		efforts1 = new HashMap<String, EffortInTrans>();
		efforts2 = new HashMap<String, EffortInTrans>();
		attackers = new ArrayList<AttackerCap>();			
	}

	@Override
	protected boolean prepare() {		
		FileInModel model = new FileInModel();
		efforts1 = model.file_in(1);
		efforts2 = model.file_in(2);
		FileInAttacker att = new FileInAttacker();
		attackers = att.file_in();
		return true;		
	}	
	
	

	@Override
	protected void run() {						 
        FileWriter uelFile;
		try {
				uelFile = new FileWriter ("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\pnassessmentresult.txt");
				PrintWriter uelWriter = new PrintWriter (uelFile);
				//uelWriter.println(attackers.size());
				//for(int i=1;i<attackers.size()+1;i++)
				//{	
					PNSingleRun onerun = new PNSingleRun(getPetriNet(), efforts1, efforts2, attackers.get(0));
					String oneresult = onerun.run_simulation();
					
					
						uelWriter.println(oneresult);	
					
				
				//}	
				uelWriter.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		System.out.println("It is done by Petri Net Assessment, please check the result files! ");	
	}		
}

