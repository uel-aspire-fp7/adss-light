package org.uel.aspire.wp5.assessment.APIs;

/*
 * This class is a container class to store protection solution. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * */

import java.util.ArrayList;

import eu.aspire_fp7.adss.akb.ProtectionInstantiation;

public class ProtectionSolution {

	// this is the unique name of this protection solution
	String id;
	
	//this is the protection list, including names of each protection, and all parameters of each protection
	ArrayList<Protection> protectionList;
	
	// this is the construction method one 
	public ProtectionSolution()
	{
		id = "";
		protectionList = new ArrayList<Protection>();
	}

	//this function is to set id of this protection solution
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to return id of this protection solution
	public String getID()
	{
		return id;
	}
	
	//this function is to set method_list by input	
	public boolean setProtectionList(ArrayList<Protection> protectionlistin)
	{
		protectionList = protectionlistin;
		return true;
	}
	
	//this function is to set method_list by searching id of this protection solution
	public boolean setProtectionList(String id)
	{
		//TODO
		return true;
	}
	
	//this function is to return the protection solution
	public ArrayList<Protection> getProtectionList()
	{
		return protectionList;
	}


}
