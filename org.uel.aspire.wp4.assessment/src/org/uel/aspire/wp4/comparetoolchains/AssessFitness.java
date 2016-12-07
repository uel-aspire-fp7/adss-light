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
 * on the basis of the class CTCDirect
 * this class is the main class to do fitness assessment
 * by Gaofeng ZHANG, UEL, 01/01/2016
 */
public class AssessFitness {
	
	ArrayList<AttackPath> attackpaths;	
	ProtectionSolution pc;	
	ArrayList<Measure> measurelist;
	HashMap<String, HashMap<String, String> > mapping;		
	Metrics deltameasure;
	double result ;	
	
	//this function is the "constructor" method one to do data prepare 
	public void createJob(ArrayList<AttackPath> aps, ProtectionSolution pcin,  ArrayList<Measure> measurelistin, HashMap<String, HashMap<String, String> > mappingin)
	{
		result = 0.0; 		
		attackpaths = aps;		
		pc = pcin;			
		measurelist = measurelistin;
		mapping = mappingin;		
		deltameasure = getDeltaMeasure(pc);	
	}	

	//this function is the main method to do the comparison
	public double doJob()
	{
		ArrayList<AttackPath> aps = attackpaths;
		result = calculate(aps, deltameasure);
		return result;
	}
	
	//This function is to calculate one protection(delta metrics)'s security value on one attack model (pn)
	double calculate(ArrayList<AttackPath> aps, Metrics met)
	{
		double res = Double.MAX_VALUE;

		for (int i=0;i<aps.size();i++)
		{
			double restemp =  0.0;

			for(int j = 0;j<aps.get(i).getAttackSteps().size(); j++)
			{
				if (aps.get(i).getAttackSteps().get(j).getFormulas() == null) break;
				if (aps.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula() == null) break;
				
				Metrics mettemp = new Metrics();
				double weight  = 0.0;
				try{
					mettemp = StringtoMetrics.getResults(aps.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula());
					weight = StringtoMetrics.getWeight(aps.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula());
				}
				catch(Exception e)
				{
					break;
				}
				
				double resstep = 0.0;
				for (int k = 0; k < mettemp.getMeasureList().size();k++)
				{
					try{
					if(met.getMeasure( mettemp.getMeasureList().get(k).getID() ) != null)
					{	
						resstep = resstep + met.getMeasure( mettemp.getMeasureList().get(k).getID() ).getValue() 
								* mettemp.getMeasureList().get(k).getValue();

					}
					}
					catch(Exception e)
					{
						break;
					}
				}
				restemp = restemp + resstep*weight;			
			}		
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
			Iterator it = mapping.get(pro.getID()).entrySet().iterator(); 
			try{
			while (it.hasNext()) 
			{ 
				 java.util.Map.Entry entry = (java.util.Map.Entry) it.next(); 
				 String name = (String) entry.getKey();
				 String formula = (String) entry.getValue();				
				 double effort = StringtoFormula.getResults(formula, pro);
				 met.addmetrics(name, effort);			
			} 
			}
			catch(Exception e)
			{
				break;
			}		
		}
		return met;
	}
}
