package org.uel.aspire.wp4.assessment.APIs;

/*
 * This class is a container class to store measure in metrics. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * modified in 15/12/2015, UEL, by Gaofeng ZHANG.
 * */

public class Measure {
	
	// this is the unique name of this measure
	String id;
	
	//this is the weight of this measure
	double weight;
	
	// this is the construction method one 
	public Measure(String idin, double valuein)
	{
		id = idin;
		weight = valuein;
	}
	
	// this is the construction method two 
	public Measure()
	{
		id = "";
		weight = 0.0;
	}
	
	
	//this function is to get id of this measure
	public String getID()
	{
		return id;
	}
	
	//this function is to set id of this measure
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get value of this measure
	public double getWeight()
	{
		return weight;
	}
	
	//this function is to set value of this measure
	public void setWeight(double valuein)
	{
		weight = valuein;
	}
	
	//this function is to get value of this measure
	public double getValue()
	{
		return weight;
	}
	
	//this function is to set value of this measure
	public void setValue(double valuein)
	{
		weight = valuein;
	}

}
