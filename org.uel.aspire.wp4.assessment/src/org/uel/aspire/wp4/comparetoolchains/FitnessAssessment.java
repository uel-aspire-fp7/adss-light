package org.uel.aspire.wp4.comparetoolchains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.assessment.APIs.AttackModel;
import org.uel.aspire.wp4.assessment.APIs.AttackPath;
import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.assessment.APIs.Protection;
import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.formula.StringtoFormula;
import org.uel.aspire.wp4.formula.StringtoMetrics;
/*
 * this class is new added on the basis of ComapreToolChains class, to feedback the fitness of one protection configurations
 * by Gaofeng ZHANG, UEL, 08-01-2016
 * 
 * 2016/07/11 this is to update to match the adss integration
 */
public class FitnessAssessment {
	
	PetriNet ptmodel;
	AttackModel am;
	ProtectionSolution pc1;
	
	boolean pn;
	ArrayList<Measure> measurelist;
	HashMap<String, HashMap<String, String> > mapping;	
	
	Metrics deltameasure1;
	//Metrics deltameasure2;
	
	//boolean result ; //the first pc1 is better than pc2. 
	double result1 ;
	//double result2 ;
	
	//this function is the "constructor" method one to do data prepare (currently used )
	public void createJob(PetriNet ptmodelin, ProtectionSolution pc1in, ArrayList<Measure> measurelistin, HashMap<String, HashMap<String, String> > mappingin)
	{
		//result = true; //the first pc1 is better than pc2.
		result1 = 0.0;
		//result2 = 0.0;
		
		ptmodel = ptmodelin;		
		pc1 = pc1in;
			
		pn = true;
		measurelist = measurelistin;
		mapping = mappingin;
		//System.out.println("This is the createJob() method of CompareToolChains!");
		if (ptmodel==null) System.out.println("ptmodel is null in the createJob() method of CompareToolChains!");
		deltameasure1 = getDeltaMeasure(pc1);
		
	}
	
	//this function is the "constructor method" two to do data prepare (currently not used )
	public void createJob(AttackModel amin, ProtectionSolution pc1in)
	{
		am = amin;
		pc1 = pc1in;
				
		pn = false;
	}
	

	//this function is the main method to do the assessment
	public double doJob()
	{
		//System.out.println("This is the doJob() method of CompareToolChains!");		
		//graph operation on petri net models
		ArrayList<AttackPath> aps = AttackModel.fromPNtoPath(ptmodel);
		System.out.println("For this Petri Net attack model, there are " + aps.size() + " attack paths.");
		/*for(int i = 0 ; i<aps.size(); i++)
		{
			System.out.print("Attack path " + i + ":  ");
			for (int j = 0;j<aps.get(i).getAttackSteps().size();j++)
			{
				System.out.print(aps.get(i).getAttackSteps().get(j).getID() + "  ");
			}
			System.out.println();
		}*/
		//System.out.println("For Protection Configuration 1, these " + aps.size() + " attack paths have the following assessment results:");
		result1 = calculate(aps, deltameasure1);
		//System.out.println("For Protection Configuration 1, the assessment result is " + result1 );
		//System.out.println("For Protection Configuration 2, these " + aps.size() + " attack paths have the following assessment results:");
		//result2 = calculate(aps, deltameasure2);
		//System.out.println("For Protection Configuration 2, the assessment result is " + result2 );
		//if(result1 < result2) result = false;
		//if (result)
			//System.out.println("Hence, Protection Configuration 1 is better than Protection Configuration 2" );
		//else 
			//System.out.println("Hence, Protection Configuration 2 is better than Protection Configuration 1" );
		//String res = "the first protection fitness is :"+result1+" and the second one is :"+result2;
		return result1;
	}
	
	//This function is to calculate one protection(delta metrics)'s security value on one attack model (pn)
	double calculate(ArrayList<AttackPath> aps, Metrics met)
	{
		double res = Double.MAX_VALUE;
		//TODO	
		//ArrayList<AttackPath> aps = AttackModel.fromPNtoPath(pt);
	
		//Metrics mett = new Metrics();
		for (int i=0;i<aps.size();i++)
		{
			double restemp =  0.0;
			//Metrics mettemp = new Metrics();
			//System.out.println("in calculate() of class CompareToolChains, aps.size() is "+aps.size());
			//System.out.println("in calculate() of class CompareToolChains, aps.get(0).getAttackSteps() is "+aps.get(0).getAttackSteps());
			for(int j = 0;j<aps.get(i).getAttackSteps().size(); j++)
			{
				Metrics mettemp = StringtoMetrics.getResults(aps.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula());
				double weight = StringtoMetrics.getWeight(aps.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula());
				
				double resstep = 0.0;
				for (int k = 0; k < mettemp.getMeasureList().size();k++)
				{
					//System.out.println("met.getMeasureList().size() is " + met.getMeasureList().size());
					//System.out.println("mettemp.getMeasureList().get(k).getID() is " +mettemp.getMeasureList().get(k).getID());
					if(met.getMeasure( mettemp.getMeasureList().get(k).getID() ) != null)
					{				
						resstep = resstep + met.getMeasure( mettemp.getMeasureList().get(k).getID() ).getValue() 
								* mettemp.getMeasureList().get(k).getValue();
						//System.out.println(met.getMeasure( mettemp.getMeasureList().get(k).getID() ).getValue() + " " + mettemp.getMeasureList().get(k).getValue());
					
					//	System.out.println(resstep);
					}
				}
				restemp = restemp + resstep*weight;
				//System.out.println(resstep+ "  " + weight);
			}
			//System.out.println("For the " + (i+1) + "th attack path, the assessment result is " +restemp);
			if (res > restemp) res = restemp;
		}
		return res;
	}

	//this function is to get the delta metrics of each protection 
	Metrics getDeltaMeasure(ProtectionSolution pcin)
	{
		Metrics met = new Metrics();
		for(int i=0;i<pcin.getProtectionList().size();i++)
		{
			Protection pro = pcin.getProtectionList().get(i);
			//System.out.println(" in the getDeltaMeasure() of class CompareToolChains,  mapping.size() is "+mapping.size());
			Iterator it = mapping.get(pro.getID()).entrySet().iterator(); 
			while (it.hasNext()) 
			{ 
				 java.util.Map.Entry entry = (java.util.Map.Entry) it.next(); 
				 String name = (String) entry.getKey();
				 String formula = (String) entry.getValue();
				// System.out.println(formula);
				 double effort = StringtoFormula.getResults(formula, pro);
				 met.addmetrics(name, effort);
				// System.out.println(" effort is "+effort);
			} 
			//String formula = mapping.get(pro.getID()).;
		}
		return met;
	}
}
