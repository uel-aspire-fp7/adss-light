package org.uel.aspire.wp4.assessment.APIs;

//import java.util.ArrayList;
//import java.util.HashMap;

/*
 * This class is a container class to store formula in attack step. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * modified in 15/12/2015, UEL, by Gaofeng ZHANG.
 * */

public class Formula {
	
	// this is the unique name of this formula 1:1 to step
	String id;

	//this is the value of this formula
	String formula;

	//this is the structural formula, with different metrics and their weights in one attack step
	Metrics formula_sorted;
	//ArrayList<Double> weights;
	
	// this is the construction method one 
	public Formula()
	{
		id = "";
		formula = "";
		formula_sorted = new Metrics();			
	}
	
	// this is the construction method two 
	public Formula(Metrics metin)
	{
		id = "";
		formula = "";
		formula_sorted = new Metrics();
		formula_sorted = metin;				
	}
	
	
	//this function is to get id of this formula
	public String getID()
	{
		return id;
	}
	
	//this function is to set id of this formula
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get formula of this formula
	public String getFormula()
	{
		return formula;
	}
	
	//this function is to set formula of this formula
	public void setFormula(String formulain)
	{
		formula = formulain;
	}
	
	//this function is to get formula_sorted of this formula
	public Metrics getFormula_sorted()
	{
		return formula_sorted;
	}
	
	//this function is to set formula of this formula
	public void setFormula_sorted(Metrics formulain)
	{
		formula_sorted = formulain;
	}

}
