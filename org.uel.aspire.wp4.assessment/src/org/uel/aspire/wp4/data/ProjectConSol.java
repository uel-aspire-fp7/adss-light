package org.uel.aspire.wp4.data;

import java.util.HashMap;

public class ProjectConSol {
	
	HashMap<String, EffortInTrans> efforts;
	
	public ProjectConSol(HashMap<String, EffortInTrans> effortsin)
	{
		efforts = effortsin;
	}
	
	public void setEff(HashMap<String, EffortInTrans> effortsin)
	{
		efforts = effortsin;
	}
	
	public HashMap<String, EffortInTrans> getEff()
	{
		return efforts;
	}

}
