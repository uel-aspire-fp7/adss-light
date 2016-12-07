package org.uel.aspire.wp4.assessment.APIs;

/*
 * This class is a container class to store protection parameter in protection. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * */

public class ProtectionParameter {
	
	// this is the unique name of this protection parameter
	String id;
	
	//this is the value of this protection parameter
	double value;
	
	// this is the construction method one 
	public ProtectionParameter()
	{
		id = "";
		//value = valuein;
	}
	
	
	//this function is to get id of this protection parameter
	public String getID()
	{
		return id;
	}
	
	//this function is to set id of this protection parameter
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get value of this protection parameter
	public double getValue()
	{
		return value;
	}
	
	//this function is to set value of this protection parameter
	public void setValue(double valuein)
	{
		value = valuein;
	}
}
