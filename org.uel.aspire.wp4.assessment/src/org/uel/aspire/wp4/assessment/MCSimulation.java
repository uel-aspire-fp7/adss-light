package org.uel.aspire.wp4.assessment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.EffortInTrans;

public class MCSimulation {
	PetriNet petrinet;
	AttackerCap attacker;
	ArrayList<AttackerCap> attackers;
	HashMap<String, EffortInTrans> efforts;
	Double boundary;
	Double possibility;
	
	public MCSimulation(PetriNet pnin, HashMap<String, EffortInTrans> efin, ArrayList<AttackerCap> atin)
	{
		petrinet = pnin;
		attackers = atin;
		efforts = efin;
		boundary = 0.0001;
		possibility = -1.0;
	}	

	public Double run_mcsimulation(Double boin)
	{
		if(boin != null) boundary = boin;
		long round = 1;
		Double oldone=0.0;
		while(true)
		{			
			if (attackers==null) System.out.println("attackers==null in MCsimulation" );
			//System.out.print("mc"); System.out.println(attackers);
			SingleSimulation ss = new SingleSimulation(petrinet, efforts, attackers);
			
			//System.out.println("++++++ in run_mcsimulation() while loop");	
			
			if (ss.run_simulation())
			{
				if(possibility == -1.0)
				{
					possibility = 1.0;
					round++;
					oldone = possibility;
					continue;
				}
				else
				{
					possibility = ( (Double)possibility*round + 1.0 ) / (round+1);
				}
			}
			else
			{
				if (possibility == -1.0)
				{
					possibility = 0.0;
					round++;
					oldone = possibility;
					continue;
				}
				else
				{
					possibility = ( (Double)possibility*round ) / (round+1);
				}
			}
			//System.out.println("**++**");
			
			if ( (possibility - oldone) < boundary && (possibility - oldone) >= 0.0 ) break;
			if ( (oldone - possibility) < boundary && (oldone - possibility) >= 0.0 ) break;
			
			//System.out.println("Round " + round + " of possibility is : " + possibility);	
			
			round++;
			oldone = possibility;	
		
		}
		Random ra = new Random();
			if(possibility ==0.0  ) possibility = ra.nextDouble();
			if(possibility ==1.0 ) possibility = ra.nextDouble();
		return possibility;
	}
}
