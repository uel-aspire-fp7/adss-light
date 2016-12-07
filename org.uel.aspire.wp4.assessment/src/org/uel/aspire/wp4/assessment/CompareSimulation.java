package org.uel.aspire.wp4.assessment;

import java.util.ArrayList;
import java.util.HashMap;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.EffortInTrans;

public class CompareSimulation {
	
	PetriNet petrinet;
	ArrayList<AttackerCap> attackers;
	HashMap<String, EffortInTrans> efforts1;
	HashMap<String, EffortInTrans> efforts2;

	
	public CompareSimulation(PetriNet pnin, HashMap<String, EffortInTrans> efin1, HashMap<String, EffortInTrans> efin2, ArrayList<AttackerCap> atin)
	{
		petrinet = pnin;
		attackers = atin;
		efforts1 = efin1;
		efforts2 = efin2;	
		if(efforts1.equals(efforts2)) System.out.println("ERROR: TWO DATA FOR TOOL CHAINS ARE SAME!");
	}	

	public String run_comparesimulation(Double boin)
	{
		String result = "";
		
		//System.out.println("In CompareSimulation, efforts1.toString() is : "+efforts1.toString());
		
		MCSimulation mcsimu1 = new MCSimulation(petrinet, efforts1, attackers);		
		Double result1 = mcsimu1.run_mcsimulation(null);
		
		//System.out.println("In CompareSimulation, efforts2.toString() is : "+efforts2.toString());
		//System.out.println("In CompareSimulation, attacker.toString() is : "+attacker.toString());
		//System.out.println("In CompareSimulation, petrinet.toString() is : "+petrinet.toString());
		System.out.println("result1: " + result1);
		
		MCSimulation mcsimu2 = new MCSimulation(petrinet, efforts2, attackers);		
		Double result2 = mcsimu2.run_mcsimulation(null);
		
		//System.out.println("+++++++++++++++");
		System.out.println("result2 : " + result2);
		
		if( result1 < result2)
			result = "Tool chain 1 better than tool chain 2 by " + result1/result2;
		else
			result = "Tool chain 2 better than tool chain 1 by " + result2/result1;
		return result;
	}	

}
