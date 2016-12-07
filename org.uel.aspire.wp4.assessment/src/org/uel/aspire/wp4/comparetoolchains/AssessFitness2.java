package org.uel.aspire.wp4.comparetoolchains;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
//import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
//import org.uel.aspire.wp4.assessment.APIs.AttackModel;
//import org.uel.aspire.wp4.assessment.APIs.AttackPath;
//import org.uel.aspire.wp4.assessment.APIs.Measure;
//import org.uel.aspire.wp4.assessment.APIs.Metrics;
//import org.uel.aspire.wp4.assessment.APIs.Protection;
//import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.formula.StringtoFormula;
import org.uel.aspire.wp4.formula.StringtoMetrics;
import org.uel.aspire.wp4.logger.SPALogger;

import eu.aspire_fp7.adss.akb.AttackPath;
import eu.aspire_fp7.adss.akb.AttackStep;
import eu.aspire_fp7.adss.akb.ProtectionInstantiation;
import eu.aspire_fp7.adss.akb.Solution;
/*
 * on the basis of the class CTCDirect
 * this class is the main class to do fitness assessment
 * by Gaofeng ZHANG, UEL, 01/01/2016
 * 
 * In 26/03/2016, new  version to use ADSS data structure directly, by gaofeng. 
 */
public class AssessFitness2 {
	
	ArrayList<AttackPath> attackpaths;	
	Solution pc;	
	ArrayList<String> measurelist;
	HashMap<String, HashMap<String, Double> > mapping;		// the first string is the name of the 
														// protection configuration, the second string 
														// is the name of metric, the last double is the value of the delta effort.
	HashMap<String, Double> deltameasure; // this is different compared to Assessfitness2,
						//the first item is the name of metrics, and second is the delta value on this metric
						// by gaofeng, 27/03/2016.
	
	double result ;	
	SPALogger log;
	
	AttackPath keyPath;
	
	//this function is the "constructor" method one to do data prepare 
	public void createJob(ArrayList<AttackPath> aps, Solution pcin,  ArrayList<String> measurelistin, HashMap<String, HashMap<String, Double> > mappingin)
	{
		//log = new SPALogger();
		result = 0.0; 		
		attackpaths = aps;		
		pc = pcin;			
		measurelist = measurelistin;
		mapping = mappingin;		
		deltameasure = getDeltaMeasure(pc);	
		
		
		
		/*for (int i=0;i<attackpaths.size();i++)
		{
			for(int j = 0;j<attackpaths.get(i).getAttackSteps().size(); j++)
			{
				System.out.println(attackpaths.get(i).getAttackSteps().get(j).getFormula());
			}
		}
		
		
			for(int j = 0;j<deltameasure.size(); j++)
			{
				System.out.println(deltameasure.toString());
			}*/
		
	}	
	
	//this function is the "constructor" method one to do data prepare 
	public void createJob(ArrayList<AttackPath> aps, Solution pcin,  HashMap<String, HashMap<String, Double> > mappingin)
	{
		//log = new SPALogger();
		result = 0.0; 		
		attackpaths = aps;		
		pc = pcin;			
		
		mapping = mappingin;		
		deltameasure = getDeltaMeasure(pc);	
	}	
		

	//this function is the main method to do the comparison
	public double doJob(SPALogger login)
	{
		log = login;
		ArrayList<AttackPath> aps = attackpaths;
		//System.out.println("In Assessment Fitness Function process:");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.print("***************************************");
		log.print("Time: " + df.format(new Date()));
		log.print("Assessment Fitness Function Started!");
		log.changeLine();
		
		log.print("The delta metrics are :");
		for(int i=0;i<deltameasure.size();i++)
		{
			log.print((String)deltameasure.keySet().toArray()[i] + " : " + deltameasure.get((String)deltameasure.keySet().toArray()[i]));
		}
		log.changeLine();
		log.print("In Assessment Fitness Function process:");
		
		result = calculate(aps, deltameasure);
		return result;
	}
	
	//this function is the main method to do the comparison
	public double doJob()
	{	
		log = new SPALogger();
		ArrayList<AttackPath> aps = attackpaths;		
		result = calculate(aps, deltameasure);
		return result;
	}
	
	//This function is to calculate one protection(delta metrics)'s security value on one attack model (pn)
	double calculate(ArrayList<AttackPath> aps, HashMap<String, Double> met)
	{
		double res = Double.MAX_VALUE;
		
		int indexofKeyPath = -1;

		for (int i=0;i<aps.size();i++)
		{
			double restemp =  0.0;
			//System.out.println("The " +i+ "th attackpath has " + aps.get(i).getAttackSteps().size() + " steps:" );
			log.print("The " +(i+1)+ "th attackpath has " + aps.get(i).getAttackSteps().size() + " steps:");
			for(int j = 0;j<aps.get(i).getAttackSteps().size(); j++)
			{
				//System.out.println("the " +j + "th step's formula is("+aps.get(i).getAttackSteps().get(j).getFormula() + ")");
				log.print("the " +(j+1) + "th step's formula is("+aps.get(i).getAttackSteps().get(j).getFormula() + ")");
				if (aps.get(i).getAttackSteps().get(j).getFormula() == null) continue;
				if (aps.get(i).getAttackSteps().get(j).getFormula().indexOf(":")==-1) continue;
				//if (aps.get(i).getAttackSteps().get(j).getFormula().get(0).getFormula() == null) break;
				
				Metrics mettemp = new Metrics();
				double weight  = 0.0;
				try{
					//System.out.println(aps.get(i).getAttackSteps().get(j));
					//System.out.println(aps.get(i).getAttackSteps().get(j).getFormula());
					mettemp = StringtoMetrics.getResults(aps.get(i).getAttackSteps().get(j).getFormula());
					/*for(int k=0;k<mettemp.getMeasureList().size();k++)
					{
						System.out.println(mettemp.getMeasureList().get(k).getID() + " : " + mettemp.getMeasureList().get(k).getValue());
					}*/
					weight = StringtoMetrics.getWeight(aps.get(i).getAttackSteps().get(j).getFormula());
					//System.out.println(weight);					
				}
				catch(Exception e)
				{
					System.out.println(e);
					continue;
				}
				
				double resstep = 0.0;
				for (int k = 0; k < mettemp.getMeasureList().size();k++)
				{
					try{
					if(met.get(mettemp.getMeasureList().get(k).getID()) != null)
					{	
						resstep = resstep + (met.get( mettemp.getMeasureList().get(k).getID() ).doubleValue() 
								* mettemp.getMeasureList().get(k).getValue() );
							//System.out.println(resstep);
					}
					}
					catch(Exception e)
					{
						System.out.println(e);
						continue;
					}
				}
				log.print("     and its result is : "+ resstep*weight);
				//System.out.println("     and its result is : "+ resstep*weight );	
				restemp = restemp + resstep*weight;			
			}	
			//System.out.println("The assessment result of this attackpath is " + restemp);	
			log.print("The assessment result of this attackpath is " + restemp);
			log.changeLine();
			//System.out.println();
			if (res > restemp) 
			{
				res = restemp;
				indexofKeyPath = i;
			}
		}
		log.print("The final assessment result is " + res + ", which is the lowest of of all paths.");
		//System.out.println("The final assessment result is " + res + ", which is the lowest of of all paths.");
		log.print("***************************************************");
		log.changeLine();
		
		if(indexofKeyPath == -1)
		{
			keyPath = null;
		}
		else
		{
			keyPath = aps.get(indexofKeyPath);
		}
		
		return res;
	}

	//this function is to get the delta metrics of each protection 
	HashMap<String, Double> getDeltaMeasure(Solution pcin)
	{
		HashMap<String, Double> met = new HashMap<String, Double>();
		String name  = pcin.toString();
		
		return mapping.get(name);
		/*for(int i=0;i<pcin.getProtectionList().size();i++)
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
		return met;*/
	}
	
	public AttackPath getKeyPath()
	{
		return keyPath;
	}


}
