package org.uel.aspire.wp5.assessment.APIs;

/*
 * This class is a container class to store attack step. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * modified in 15/12/2015, UEL, by Gaofeng ZHANG.
 * */

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.pnml.tools.epnk.pnmlcoremodel.LabelProxy;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;

public class AttackStep {
	
	// this is the unique name of this attack step
	String id;
	
	//this is the formula list, including all formulas of this attack step
	ArrayList<Formula> formulas;//TODO multipe in editor
	
	//this is the metrics of this attack step
	//ArrayList<Metrics> metrics;
	
	//these two are used to simulate the effort.
	double min;	
	double max;
	
	double weight; // relevance of step to represent attacker expertise
	//TODO e.g. 1 = beginner, 0.5 expert, 0.1 guru 
	
	//this is the constructor method
	public AttackStep()
	{
		id = "";
		formulas = new ArrayList<Formula>(); 
		//metrics = new ArrayList<Metrics>();
		min = -1.0;
		max = -1.0;
		weight = 1.0;
	}
	
	//this function is to get ID of this attack step
	public String getID()
	{
		return id;
	}
	
	//this function is to set ID of this attack step
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get formulas of this attack step
	public ArrayList<Formula> getFormulas()
	{
		return formulas;
	}
	
	//this function is to set formulas of this attack step
	public void setFormulas(ArrayList<Formula> formulasin)
	{
		formulas = formulasin;
	}
	
	//this function is to get min of this attack step
	public double getMin()
	{
		return min;
	}
	
	//this function is to set min of this attack step
	public void setMin(double minin)
	{
		min = minin;
	}
	
	//this function is to get max of this attack step
	public double getMax()
	{
		return max;
	}
	
	//this function is to set max of this attack step
	public void setMax(double maxin)
	{
		max = maxin;
	}
	
	//this function is to get weight of this attack step
	public double getWeight()
	{
		return weight;
	}
	
	//this function is to set weight of this attack step
	public void setWeight(double weightin)
	{
		weight = weightin;
	}
	
	
	//this function is to get metrics of this attack step
	//public ArrayList<Metrics> getMetrics()
	//{
	//	return metrics;
	//}
	
	//this function is to set metrics of this attack step
	//public void setMetrics(ArrayList<Metrics> metricsin)
	//{
	//	metrics = metricsin;
	//}
	
	//this function is to transfer a Transition object in epnk to a attack step in our uel aspire data model, by Gaofeng ZHANG 07/07/2015
	public static AttackStep changetoAttackStep(Transition tran, EList<LabelProxy> labs)
	{
		AttackStep as = new AttackStep();
		as.setID(tran.getId());
		
		//getPetriNet().getPage().get(0).getLabelproxy().get(0).getText()
		String formu="";
		for(int i=0;i<labs.size();i++)
		{
			if (tran.getId() == labs.get(i).getObject().getId()) 
				formu = labs.get(i).getText();
		}
		//TODO
		Formula fo = new Formula();
		fo.setFormula(formu);
		ArrayList<Formula> af = new ArrayList<Formula>();
		af.add(fo);		
		as.setFormulas(af);
		
		return as;
	}

}
