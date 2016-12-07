 package org.uel.aspire.wp4.assessment.APIs;

/*
 * This class is a container class to store protection in protection configuration. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * */


import java.util.HashMap;

public class Protection {
	
	// this is the unique name of this protection 
	String id;
	
	//this is the protection parameter hashmap, including all parameters of this protection. The first string is the name or id of this parameter,
	//the second string is the value of this parameter. 
	HashMap<String, String> parameters;
	
	// this is the construction method one 
	public Protection()
	{
		id = "";
		parameters = new HashMap<String, String>();
	}
	
	//this function is to get id of this protection
	public String getID()
	{
		return id;
	}
	
	//this function is to set id of this protection
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get parameter_list of this protection
	public HashMap<String, String> getParameters()
	{
		return parameters;
	}
	
	//this function is to set parameter_list of this protection
	public void setParameters(HashMap<String, String> parametersin)
	{
		parameters = parametersin;
	}
}
