package org.uel.aspire.wp5.assessment.APIs;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.assessment.APIs.AttackModel;
import org.uel.aspire.wp4.assessment.APIs.AttackPath;
//import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.comparetoolchains.AssessFitness2;
//import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.formula.StringtoFormula;
import org.uel.aspire.wp4.formula.StringtoMetrics;

import eu.aspire_fp7.adss.akb.Metric;
/*

 * by Gaofeng ZHANG, UEL, 08-01-2016
 * 
 * 2016/07/11 this is to update to match the adss integration
 */
public class FitnessAssessment {
	
	PetriNet ptmodel;
	EList<Metric> met1;
	EList<Metric> metv;
	/*
	ArrayList<Metric> deltamet;

	ProtectionSolution pc1;
	
	boolean pn;
	ArrayList<Measure> measurelist;
	HashMap<String, HashMap<String, String> > mapping;	
	*/
	Metrics deltameasure;
	//Metrics deltameasure2;
	
	
	//boolean result ; //the first pc1 is better than pc2. 
	double result1 ;
	//double result2 ;
	
	public FitnessAssessment(PetriNet petrinet, EList<Metric> applicationMetrics, EList<Metric> applicationMetrics2) {
		ptmodel = petrinet;
		met1 = metricsNameMap(applicationMetrics);
		metv = metricsNameMap(applicationMetrics2);
		deltameasure = getDeltaMeasure(met1, metv);
		/*for(int i=0;i<met1.size();i++)
		{
			System.out.println(met1.get(i).getName());
		}*/
	}



	//this function is the "constructor" method one to do data prepare (currently used )
	/*public void createJob(PetriNet ptmodelin, ProtectionSolution pc1in, ArrayList<Measure> measurelistin, HashMap<String, HashMap<String, String> > mappingin)
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
		deltameasure = getDeltaMeasure(pc1);
		
	}*/


	
	//this function is the main method to do the assessment
	public double doJob()
	{
		ArrayList<AttackPath> aps = AttackModel.fromPNtoPath(ptmodel);
		//System.out.println("For this Petri Net attack model, there are " + aps.size() + " attack paths.");

		//result1 = calculate(aps, deltameasure1);
		result1 = calculate(aps, deltameasure);
		return result1;
	}	



	//This function is to calculate one protection(delta metrics)'s security value on one attack model (pn)
	double calculate(ArrayList<AttackPath> aps, Metrics met)
	{
		double res = Double.MAX_VALUE;

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
/*	Metrics getDeltaMeasure(ProtectionSolution pcin)
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
	}*/
	
	private Metrics getDeltaMeasure(EList<Metric> met12, EList<Metric> metv2) {
		Metrics delta = new Metrics();
		try{
			for(int i=0;i<met12.size();i++)
			{
				double singlem = 0.0;
				for(int j=0;j<metv2.size();j++)
				{
					if(met12.get(i).getName() == metv2.get(j).getName())
					{
						if (metv2.get(j).getValue() == 0.0) {singlem = 0.0; break;}
						singlem = (double)( met12.get(i).getValue()/metv2.get(j).getValue() );
						break;
					}
				}
				//System.out.println(met12.get(i).getName() + " : "+ singlem);
				delta.addmetrics(met12.get(i).getName(), singlem );
			}
		
			return delta;
		}
		catch(Exception e)
		{		
			e.printStackTrace();	
			return null;
		}
	}
	
	static HashMap<String,String> metricMap = new HashMap<String,String>();
	static {
		metricMap.put("nr_ins_static","INS");
		metricMap.put("#region_idx","region_id");
		metricMap.put("nr_src_oper_static","SRC");
		metricMap.put("nr_dst_oper_static","SPS");
		metricMap.put("halstead_program_size_static","DPS");		
		metricMap.put("nr_edges_static","EDG");
		metricMap.put("nr_indirect_edges_CFIM_static","DPL");		
		metricMap.put("cyclomatic_complexity_static","CC");
		
		//the following list require supports from UGhent and POLITO, by Gaofeng 12/07/2016
		metricMap.put("nr_ins_dynamic_coverage", "Unknown");
		metricMap.put("halstead_program_size_dynamic_coverage", "Unknown");			
		metricMap.put("nr_indirect_edges_CFIM_dynamic_size", "Unknown");
		metricMap.put("nr_edges_dynamic_size", "Unknown");
		metricMap.put("nr_src_oper_dynamic_size", "Unknown");		
		metricMap.put("cyclomatic_complexity_dynamic_coverage", "Unknown");		
		metricMap.put("nr_dst_oper_dynamic_coverage", "Unknown");		
		metricMap.put("nr_indirect_edges_CFIM_dynamic_coverage", "Unknown");
		metricMap.put("nr_src_oper_dynamic_coverage", "Unknown");
		metricMap.put("nr_dst_oper_dynamic_size", "Unknown");
		metricMap.put("nr_edges_dynamic_coverage", "Unknown");
		metricMap.put("nr_ins_dynamic_size", "Unknown");
		metricMap.put("halstead_program_size_dynamic_size", "Unknown");
	}
	
	private EList<Metric> metricsNameMap(EList<Metric> metin)	
	{
		int size = metin.size();
		EList<Metric> met = metin;
		for(int i=0;i<size;i++)
		{
			String newName = metricMap.get(met.get(i).getName());
			if(newName == null) met.get(i).setName("Unknown");
			else
			met.get(i).setName(newName);
		}
		return met;
	}


}
