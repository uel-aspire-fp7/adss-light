package org.uel.aspire.wp4.assessment.APIs;

/*
 * This class is a container class to store attack path. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * modified in 15/12/2015, UEL, by Gaofeng ZHANG.
 * */

import java.util.ArrayList;

public class AttackPath {
	
	//this is the unique name of this attack path
	String id;
	
	//this is the attack step list, including all attack steps of this attack path
	ArrayList<AttackStep> attackSteps;
	
	//ArrayList<Double> weights;
	
	//this is the constructor method
	public AttackPath()
	{
		id = "";
		attackSteps = new ArrayList<AttackStep>();
	}
	
	//this function is to get ID of this attack path
	public String getID()
	{
		return id;
	}
	
	//this function is to set ID of this attack path
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get attack steps of this attack path
	public ArrayList<AttackStep> getAttackSteps()
	{
		return attackSteps;
	}
	
	//this function is to set attack steps of this attack path
	public void setAttackSteps(ArrayList<AttackStep> attackstepsin)
	{
		attackSteps = attackstepsin;
	}
	
	//this function is to add one attack step in the end of  the attackpath , added by Gaofeng ZHANG 07/07/2015
	public boolean addAttackStep(AttackStep step)
	{
		try{
			attackSteps.add(step);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	//this function is to remove one attack step in the attackpath , added by Gaofeng ZHANG 15/12/2015
	public boolean removeAttackStep(String stepid)
	{
		try{
			for (int i = 0 ; i < attackSteps.size();i++)
			{
				if(attackSteps.get(i).getID() == stepid)
				{
					attackSteps.remove(i);
					break;
				}
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
