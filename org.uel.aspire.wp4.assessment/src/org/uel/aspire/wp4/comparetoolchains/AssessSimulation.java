package org.uel.aspire.wp4.comparetoolchains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.math.*;

import org.uel.aspire.wp4.assessment.APIs.AttackPath;
import org.uel.aspire.wp4.assessment.APIs.AttackStep;
import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.assessment.APIs.MinMax;
import org.uel.aspire.wp4.assessment.APIs.Protection;
import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.data.TransitionRelation;
import org.uel.aspire.wp4.formula.StringtoFormula;
import org.uel.aspire.wp4.formula.StringtoMetrics;

/*
 * 
 * this class is the main class to do simulation
 * there are classes: 
 * one is the createJob and doJob, they are for the simulation  with relations among attacksteps, 
 * another one is the createJobM and doJobM, they are for the simulation with customers-set mins and maxs
 * by Gaofeng ZHANG, UEL, 01/01/2016
 */

public class AssessSimulation {
	

	ArrayList<AttackPath> aps;
	double difference;
	long timeout;
	MinMax mm;
	
	//////////
	
	ProtectionSolution pc;
	ArrayList<Measure> measurelist;
	HashMap<String, HashMap<String, String> > mapping;
	
	Metrics deltameasure;	
	
	public AssessSimulation( )
	{		
	}
	
	public void creatJob(ArrayList<AttackPath> apsin, MinMax mmin, ProtectionSolution pcin, ArrayList<Measure> measurelistin, HashMap<String, HashMap<String, String> > mappingin, double differencein, long timeoutin)
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
	double calculate(ArrayList<AttackPath> apss, Metrics met)
	{
		double res = Double.MAX_VALUE;

		for (int i=0;i<apss.size();i++)
		{
			double restemp =  0.0;

			for(int j = 0;j<apss.get(i).getAttackSteps().size(); j++)
			{
				if (apss.get(i).getAttackSteps().get(j).getFormulas() == null) break;
				if (apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula() == null) break;
				
				if (apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula().indexOf("<") != -1
						||apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula().indexOf("<") != -1) 
					{
						apss.get(i).getAttackSteps().get(j).setMin(0.0);
						apss.get(i).getAttackSteps().get(j).setMax(Double.MAX_VALUE);
						continue;
					}
			
					Metrics mettemp = new Metrics();
					double weight  = 0.0;
					try{
						mettemp = StringtoMetrics.getResults(apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula());
						weight = StringtoMetrics.getWeight(apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula());
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
					//restemp = restemp + resstep*weight;
					apss.get(i).getAttackSteps().get(j).setMin(resstep*weight);
					apss.get(i).getAttackSteps().get(j).setMax(resstep*weight);
				
			}			
			//if (res > restemp) res = restemp;			
		}
		//this is the part to find out all the relations among attack steps. (T3>T4, or T6<T8)
		ArrayList<TransitionRelation> trs = new ArrayList<TransitionRelation>();
		for (int i=0;i<apss.size();i++)
		{
			for(int j = 0;j<apss.get(i).getAttackSteps().size(); j++)
			{
				String relations  = apss.get(i).getAttackSteps().get(j).getFormulas().get(0).getFormula();
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
					if (apss.get(l).getAttackSteps().get(k).getID() == trs.get(i).getLargeID()) {largeindex1=l; largeindex2=k;}
					if (apss.get(l).getAttackSteps().get(k).getID() == trs.get(i).getSmallID()) {smallindex1=l; smallindex2=k; }
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
			if (apss.get(largeindex1).getAttackSteps().get(largeindex2).getMax() == apss.get(largeindex1).getAttackSteps().get(largeindex2).getMin()) 
				apss.get(smallindex1).getAttackSteps().get(smallindex2).setMax(apss.get(largeindex1).getAttackSteps().get(largeindex2).getMax());
			if (apss.get(smallindex1).getAttackSteps().get(smallindex2).getMax() == apss.get(smallindex1).getAttackSteps().get(smallindex2).getMin()) 
				apss.get(largeindex1).getAttackSteps().get(largeindex2).setMin(apss.get(smallindex1).getAttackSteps().get(smallindex2).getMax());				
		}

		res = simulateM(apss);
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
	
	
	public void creatJobM(ArrayList<AttackPath> apsin, MinMax mmin, double differencein, long timeoutin)
	{	
		aps = apsin;
		mm = mmin;
		difference = differencein;
		timeout = timeoutin;		
	}
	
	public double doJobM()
	{
		double result  = simulateM(aps);			
		long timeini = System.currentTimeMillis();
		long round = 1;
		while(true)
		{			
			if (round >= Long.MAX_VALUE)  break;
			double temp = simulateM(aps);
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
	double simulateM(ArrayList<AttackPath> apss)
	{
		double res = 0.0;
		Random rand = new Random();
		int index = rand.nextInt(apss.size()) ;		
		
		for(int i = 0 ; i < apss.get(index).getAttackSteps().size();i++)
		{
			double temp = rand.nextDouble()*(apss.get(index).getAttackSteps().get(i).getMax()-apss.get(index).getAttackSteps().get(i).getMin()) + apss.get(index).getAttackSteps().get(i).getMin();
			res+=temp;
		}
		return res;
	}

}
