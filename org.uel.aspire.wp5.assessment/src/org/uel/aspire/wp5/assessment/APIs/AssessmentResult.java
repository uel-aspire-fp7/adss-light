package org.uel.aspire.wp5.assessment.APIs;

/*
 * This class is a container class to store assessment result. 
 * The current version is in 07/10/2015, UEL, by Gaofeng ZHANG
 * */

public class AssessmentResult {
	
	//this is delta_effort in the assessment result
	double deltaEffort;
	
	//this is the attack path in the assessment result
	AttackPath attackPath;
	
	//this is the compare result for two protection configurations : the first one is better than the second one. 
	boolean compareresult;
	
	// this is the constructor method
	public AssessmentResult()
	{
		attackPath = new AttackPath();
	}
	
	//this function is to return the selected attack path 
	public AttackPath getAttackPath()
	{
		return attackPath;
	}

	//this function is to return deltaEffort
	public double getDeltaEffort()
	{
		return deltaEffort;
	}
	
	//this function is to set the selected attack path 
	public void setAttackPath(AttackPath apin)
	{
		attackPath = apin;
	}

	//this function is to set deltaEffort
	public void setDeltaEffort(double dein)
	{
		deltaEffort = dein;
	}

	//this function is to set the selected attack path 
	public void setCompareResult(boolean crin)
	{
		compareresult = crin;
	}

	//this function is to set deltaEffort
	public boolean getCompareResult()
	{
		return compareresult;
	}

}
