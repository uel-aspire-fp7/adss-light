package org.uel.aspire.wp4.comparetoolchains;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.math.*;
import java.text.SimpleDateFormat;

import org.eclipse.emf.common.util.EList;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
//import org.uel.aspire.wp4.assessment.APIs.AttackPath;
//import org.uel.aspire.wp4.assessment.APIs.AttackPath;
//import org.uel.aspire.wp4.assessment.APIs.AttackStep;
//import org.uel.aspire.wp4.assessment.APIs.Measure;
//import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.assessment.APIs.MinMax;
//import org.uel.aspire.wp4.assessment.APIs.Protection;
//import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.data.TransitionRelation;
import org.uel.aspire.wp4.formula.StringtoFormula;
import org.uel.aspire.wp4.formula.StringtoMetrics;
import org.uel.aspire.wp4.logger.SPALogger;

import eu.aspire_fp7.adss.akb.AttackPath;
import eu.aspire_fp7.adss.akb.ProtectionInstantiation;
import eu.aspire_fp7.adss.akb.Solution;

/*
 * 
 * this class is the main class to do simulation
 * there are classes: 
 * one is the createJob and doJob, they are for the simulation  with relations among attacksteps, 
 * another one is the createJobM and doJobM, they are for the simulation with customers-set mins and maxs
 * by Gaofeng ZHANG, UEL, 01/01/2016
 * 
 * In 26/03/2016, new  version to use ADSS data  structure directly, by gaofeng. 
 */

public class AssessSimulation2 {
	

	ArrayList<AttackPath> aps;
	double difference;
	long timeout;
	MinMax mm;
	
	SPALogger log;
	AttackPath keyPath;
	
	//////////
	
	Solution pc;
	ArrayList<String> measurelist;
	HashMap<String, HashMap<String, Double> > mapping;
	
	HashMap<String, Double> deltameasure; // this is different compared to Assessfitness2,
	//the first item is the name of metrics, and second is the delta value on this metric
	// by gaofeng, 28/03/2016.	
	
	public AssessSimulation2( )
	
	{	mm = new MinMax();	
	}
	
	public void creatJob(ArrayList<AttackPath> apsin, MinMax mmin, Solution pcin, ArrayList<String> measurelistin, HashMap<String, HashMap<String, Double> > mappingin, double differencein, long timeoutin)
	{	
		aps = apsin;
		mm = mmin;
		difference = differencein;
		timeout = timeoutin;
		///////////////
		pc = pcin;
		measurelist = measurelistin;
		mapping = mappingin;	
		deltameasure = getDeltaMeasure(pc);	
	}
	public double doJob()
	{
		double result  = simulate();			
		long timeini = System.currentTimeMillis();
		long round = 1;
		while(true)
		{			
			if (round >= Long.MAX_VALUE)  break;
			double temp = simulate();
			if( ( result-temp < difference) || (temp - result < difference) )
			{
				break;
			}
			long timecurr = System.currentTimeMillis();
			if(timecurr-timeini > timeout )
			{
				break;
			}
			result = (double)(result*round)/(round+1) + (double)temp/(round+1);
			round++;
		}
		return result;
	}
	
	double simulate()
	{
		double result = 0.0;
		ArrayList<AttackPath> apss = aps;
		result = calculate(apss, deltameasure);
		return result;
	}
	
	//This function is to calculate one protection(delta metrics)'s security value on one attack model (pn)
	double calculate(ArrayList<AttackPath> apss, HashMap<String, Double> met)
	{
		double res = Double.MAX_VALUE;

		for (int i=0;i<apss.size();i++)
		{
			double restemp =  0.0;

			for(int j = 0;j<apss.get(i).getAttackSteps().size(); j++)
			{
				if (apss.get(i).getAttackSteps().get(j).getFormula() == null) break;
				//if (apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula() == null) break;
				
				/*if (apss.get(i).getAttackSteps().get(j).getFormula().indexOf("<") != -1
						||apss.get(i).getAttackSteps().get(j).getFormula().indexOf("<") != -1) 
					{
						apss.get(i).getAttackSteps().get(j).setMin(0.0);
						apss.get(i).getAttackSteps().get(j).setMax(Double.MAX_VALUE);
						continue;
					}*/
			
					Metrics mettemp = new Metrics();
					double weight  = 0.0;
					try{
						mettemp = StringtoMetrics.getResults(apss.get(i).getAttackSteps().get(j).getFormula());
						weight = StringtoMetrics.getWeight(apss.get(i).getAttackSteps().get(j).getFormula());
					}
					catch(Exception e)
					{
						break;
					}
				
					double resstep = 0.0;
					for (int k = 0; k < mettemp.getMeasureList().size();k++)
					{
						try{
							if(met.get( mettemp.getMeasureList().get(k).getID() ) != null)
							{	
								resstep = resstep + met.get( mettemp.getMeasureList().get(k).getID() ).floatValue() 
										* mettemp.getMeasureList().get(k).getValue();
							}
						}
						catch(Exception e)
						{
							break;
						}
					}
					//restemp = restemp + resstep*weight;
					//apss.get(i).getAttackSteps().get(j).setMin(resstep*weight);
					mm.addOne(apss.get(i).getAttackSteps().get(j).getName(), resstep*weight, resstep*weight);
					//apss.get(i).getAttackSteps().get(j).setMax(resstep*weight);
				
			}			
			//if (res > restemp) res = restemp;			
		}
		//this is the part to find out all the relations among attack steps. (T3>T4, or T6<T8)
		ArrayList<TransitionRelation> trs = new ArrayList<TransitionRelation>();
		for (int i=0;i<apss.size();i++)
		{
			for(int j = 0;j<apss.get(i).getAttackSteps().size(); j++)
			{
				String relations  = apss.get(i).getAttackSteps().get(j).getFormula();
				if(relations.indexOf("<")==-1 && relations.indexOf(">")==-1) continue;
				int co = relations.indexOf(",");				
				while(true)
				{
					if (relations == null) break;
					
					String onet;
					if (co==-1) onet = relations;
					else onet = relations.substring(0, co);
					//to recognise the relation "T3>T9, T1<T4"
					String large = ">";
					int li = onet.indexOf(large);
					String small = "<";
					int si = onet.indexOf(small);
					if (li == -1 && si == -1) break;
					if (li != -1 && si != -1) break;
					String largeID, smallID;
					if (li!=-1) 
					{
						largeID = onet.substring(0, co);
						smallID = onet.substring(co+1);
					}
					else
					{
						smallID = onet.substring(0, co);
						largeID = onet.substring(co+1);
					}
					TransitionRelation tetemp = new TransitionRelation(largeID, smallID);
					trs.add(tetemp);
					
					if(co==-1) break;
					relations = relations.substring(co+1);
					co = relations.indexOf(",");
				}				
			}
		}
					
		//based on the all relations, let us recompute the mins and maxs
		for(int i=0;i<trs.size();i++)
		{
			int largeindex1  = -1, largeindex2 = -1;
			int smallindex1 = -1, smallindex2 = -1;				
			for (int l=0;l<apss.size();l++)
			{						
				for(int k = 0;k<apss.get(l).getAttackSteps().size(); k++)
				{
					if (apss.get(l).getAttackSteps().get(k).getName() == trs.get(i).getLargeID()) {largeindex1=l; largeindex2=k;}
					if (apss.get(l).getAttackSteps().get(k).getName() == trs.get(i).getSmallID()) {smallindex1=l; smallindex2=k; }
					try{
							if (apss.get(largeindex1).getAttackSteps().get(largeindex2)!=null && apss.get(smallindex1).getAttackSteps().get(smallindex2)!=null) break;
						}
					catch (Exception e)
					{
						continue;
					}
				}
						
				try{
					if (apss.get(largeindex1).getAttackSteps().get(largeindex2)!=null && apss.get(smallindex1).getAttackSteps().get(smallindex2)!=null) break;
					}
				catch (Exception e)
				{
					continue;
				}
			}
			//set new values for mins and maxs
			/*if (apss.get(largeindex1).getAttackSteps().get(largeindex2).getMax() == apss.get(largeindex1).getAttackSteps().get(largeindex2).getMin()) 
				apss.get(smallindex1).getAttackSteps().get(smallindex2).setMax(apss.get(largeindex1).getAttackSteps().get(largeindex2).getMax());
			if (apss.get(smallindex1).getAttackSteps().get(smallindex2).getMax() == apss.get(smallindex1).getAttackSteps().get(smallindex2).getMin()) 
				apss.get(largeindex1).getAttackSteps().get(largeindex2).setMin(apss.get(smallindex1).getAttackSteps().get(smallindex2).getMax());	*/
			if (mm.getMaxs().get(apss.get(largeindex1).getAttackSteps().get(largeindex2).getName()) == mm.getMins().get(apss.get(largeindex1).getAttackSteps().get(largeindex2).getName())) 
			{
				double temp = mm.getMaxs().get(apss.get(largeindex1).getAttackSteps().get(largeindex2).getName());
				mm.updateOneMax(apss.get(smallindex1).getAttackSteps().get(smallindex2).getName(), temp);
				//apss.get(smallindex1).getAttackSteps().get(smallindex2).setMax(apss.get(largeindex1).getAttackSteps().get(largeindex2).getMax());
			}
			if (mm.getMaxs().get(apss.get(smallindex1).getAttackSteps().get(smallindex2).getName()) == mm.getMins().get(apss.get(smallindex1).getAttackSteps().get(smallindex2).getName())) 
			{
				double temp = mm.getMaxs().get(apss.get(smallindex1).getAttackSteps().get(smallindex2).getName());
				mm.updateOneMin(apss.get(largeindex1).getAttackSteps().get(largeindex2).getName(), temp);
				//apss.get(largeindex1).getAttackSteps().get(largeindex2).setMin(apss.get(smallindex1).getAttackSteps().get(smallindex2).getMax());
			}
		}

		res = simulateM(apss,mm);
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
	
	
	public void creatJobM(ArrayList<AttackPath> apsin, MinMax mmin, double differencein, long timeoutin)
	{	
		aps = apsin;
		mm = mmin;
		difference = differencein;
		timeout = timeoutin;		
	}
	public void createJobM(ArrayList<AttackPath> aps2, Solution ps, ArrayList<String> measureList2,
			HashMap<String, HashMap<String, Double>> mapping2, long simu_timeout, double simu_difference) {
		aps = aps2;
		difference = simu_difference;
		timeout = simu_timeout;
		mapping = mapping2;
		HashMap<String, Double> met = getDeltaMeasure(ps);
		mm = new MinMax();
		for(int i = 0; i < aps.size(); i++)
		{	
			log.print("The " +(i+1)+ "th attackpath has " + aps.get(i).getAttackSteps().size() + " steps:");
			for(int j =0; j<aps.get(i).getAttackSteps().size(); j++)
			{
				log.print("the " +(j+1) + "th step's formula is("+aps.get(i).getAttackSteps().get(j).getFormula() + ")");
				boolean added = false;
				for(int k=0;k<mm.getSize();k++)
				{
					if (mm.getStepsID().get(k) == aps.get(i).getAttackSteps().get(j).getName())
					{
						added = true;
						break;
					}
				}
				if(added) continue;
				else
				{				
					if (aps.get(i).getAttackSteps().get(j).getFormula() == null)
					{
						mm.addOne(aps.get(i).getAttackSteps().get(j).getName(), 0, 0);
						continue;
					};
					if (aps.get(i).getAttackSteps().get(j).getFormula().indexOf(":")==-1) 				
					{
						mm.addOne(aps.get(i).getAttackSteps().get(j).getName(), 0, 0);
						continue;
					};
					if (aps.get(i).getAttackSteps().get(j).getFormula().indexOf("[")!=-1) 				
					{
						continue;
					};
					
					Metrics mettemp = new Metrics();
					double weight  = 0.0;
					try{
						mettemp = StringtoMetrics.getResults(aps.get(i).getAttackSteps().get(j).getFormula());						
						weight = StringtoMetrics.getWeight(aps.get(i).getAttackSteps().get(j).getFormula());									
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
						}
						}
						catch(Exception e)
						{
							System.out.println(e);
							continue;
						}
					}
					log.print("     and its result is : "+ resstep*weight);						
					mm.addOne(aps.get(i).getAttackSteps().get(j).getName(), resstep*weight, resstep*weight);
				}
			}
		}
		
		//to update the [name of  step, another name]
		for(int i = 0; i < aps.size(); i++)
		{			
			for(int j =0; j<aps.get(i).getAttackSteps().size(); j++)
			{				
				boolean added = false;
				for(int k=0;k<mm.getSize();k++)
				{
					if (mm.getStepsID().get(k) == aps.get(i).getAttackSteps().get(j).getName())
					{
						added = true;
						break;
					}
				}
				if(added) continue;	
				if (aps.get(i).getAttackSteps().get(j).getFormula().indexOf("[")!=-1) 				
				{
					double min = 0;
					double max = 0;
					String fullname = aps.get(i).getAttackSteps().get(j).getFormula();
					String minname = fullname.substring(fullname.indexOf('[')+1, fullname.indexOf(','));
					String maxname = fullname.substring(fullname.lastIndexOf(',')+1, fullname.lastIndexOf(']'));
					try{
					min = mm.getMins().get(minname);
					}
					catch(Exception e)
					{
						min = 0;
					}
					try{
					max = mm.getMaxs().get(maxname);
					}
					catch(Exception e)
					{
						max = 0;
					}
					mm.addOne(aps.get(i).getAttackSteps().get(j).getName(), min, max);
				};				
			}
		}
		
	}

	

	
	public double doJobM(SPALogger login)
	{
		log = login;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		log.print("***************************************");
		log.print("Time: " + df.format(new Date()));
		log.print("Assessment Simulation Function Started!");
		log.changeLine();
		log.print("In Assessment Simulation Function process:");		
		
		double result  = simulateM(aps, mm);			
		long timeini = System.currentTimeMillis();
		long round = 1;
		while(true)
		{			
			if (round >= Long.MAX_VALUE)  break;
			double temp = simulateM(aps, mm);
			if( ( result-temp < difference) || (temp - result < difference) )
			{
				break;
			}
			long timecurr = System.currentTimeMillis();
			if(timecurr-timeini > timeout )
			{
				break;
			}
			result = (double)(result*round)/(round+1) + (double)temp/(round+1);
			round++;
		}
		return result;
	}
	
	//This function is to simulate security value on MinMax values
	double simulateM(ArrayList<AttackPath> apss, MinMax mmin)
	{
		double res = 0.0;
		Random rand = new Random();
		int index = rand.nextInt(apss.size()) ;		
		
		for(int i = 0 ; i < apss.get(index).getAttackSteps().size();i++)
		{
			double temp = rand.nextDouble();
			String asname = apss.get(index).getAttackSteps().get(i).getName();
			try{
			temp = temp*(mm.getMaxs().get(asname) - mm.getMins().get(asname))-mm.getMins().get(asname);
			}
			catch(Exception e)
			{
				temp = 0;
			}
			//temp = temp*(apss.get(index).getAttackSteps().get(i).getMax()-apss.get(index).getAttackSteps().get(i).getMin()) + apss.get(index).getAttackSteps().get(i).getMin();
			res+=temp;
		}
		return res;
	}

	//in the current process of simulation, the key path is not stable (not it will return  null)
	public AttackPath getKeyPath() {
		// TODO Auto-generated method stub
		return keyPath;
	}




}
