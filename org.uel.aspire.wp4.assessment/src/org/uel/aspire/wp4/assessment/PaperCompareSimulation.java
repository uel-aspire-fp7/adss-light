package org.uel.aspire.wp4.assessment;

import java.util.ArrayList;
import java.util.HashMap;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.ConfidenceforCompare;
import org.uel.aspire.wp4.data.EffortInTrans;

public class PaperCompareSimulation {		
		
		PetriNet petrinet;
		ArrayList<AttackerCap> attackers;
		HashMap<String, EffortInTrans> efforts1;
		HashMap<String, EffortInTrans> efforts2;

		
		public PaperCompareSimulation(PetriNet pnin, HashMap<String, EffortInTrans> efin1, HashMap<String, EffortInTrans> efin2, ArrayList<AttackerCap> atins)
		{
			petrinet = pnin;
			attackers = atins;
			efforts1 = efin1;
			efforts2 = efin2;	
			if(efforts1.equals(efforts2)) System.out.println("ERROR: TWO DATA FOR TOOL CHAINS ARE SAME!");
		}	

		public ConfidenceforCompare run_papercomparesimulation(Double boin)
		{
			ConfidenceforCompare result ;			
			
			MCSimulation mcsimu1 = new MCSimulation(petrinet, efforts1, attackers);		
			Double result1 = mcsimu1.run_mcsimulation(null);			
		
			System.out.println("result1: " + result1);
			
			MCSimulation mcsimu2 = new MCSimulation(petrinet, efforts2, attackers);		
			Double result2 = mcsimu2.run_mcsimulation(null);
						
			System.out.println("result2 : " + result2);
			
			result = new ConfidenceforCompare(efforts1, efforts2, result1, result2);
			return result;
		}	

}



