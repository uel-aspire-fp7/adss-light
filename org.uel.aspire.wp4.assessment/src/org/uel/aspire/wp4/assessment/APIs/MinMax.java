package org.uel.aspire.wp4.assessment.APIs;

/*this class is to store the minimum and maximum values in each transition for our Monte Carlo simulation. 
 * In the members of this class, id is the unique name of this object, sizeofsteps is the number of the attack steps, mins and maxs are the values of each steps,
 * attackmodelis is the correspoding name of this attack model. 
 * added in 15/12/2015, UEL, by Gaofeng ZHANG.
 * 
 * methods updateOneMin() and updateOneMax() are addedd in 29/03/206, by gaofeng 
*/
import java.util.ArrayList;
import java.util.HashMap;

public class MinMax {
	String id;
	int size;
	ArrayList<String> stepsid;
	HashMap<String, Double> mins;
	HashMap<String, Double> maxs;
	String attackmodelid;
	
	public MinMax()
	{
		id = "";
		size = 0;
		stepsid = new ArrayList<String>();
		mins = new HashMap<String, Double>();
		maxs = new HashMap<String, Double>();
		attackmodelid = "";
	}
	
	public MinMax(String idin)
	{
		id = idin;
		size = 0;
		stepsid = new ArrayList<String>();
		mins = new HashMap<String, Double>();
		maxs = new HashMap<String, Double>();
		attackmodelid = "";
	}
	
	public String getID()
	{
		return id;
	}
	
	public void setID(String idin)
	{
		id = idin;
	}
	
	public String getAttackModelID()
	{
		return attackmodelid;
	}
	
	public void setAttackModelID(String idin)
	{
		attackmodelid = idin;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void setSize(int sizein)
	{
		size = sizein;
	}
	
	public ArrayList<String> getStepsID()
	{
		return stepsid;
	}
	
	public void setStepsID(ArrayList<String> sizein)
	{
		stepsid = sizein;
	}
	
	public HashMap<String, Double> getMins()
	{
		return mins;
	}
	
	public void setMins(HashMap<String, Double> sizein)
	{
		mins = sizein;
	}
	
	public HashMap<String, Double> getMaxs()
	{
		return maxs;
	}
	
	public void setMaxs(HashMap<String, Double> sizein)
	{
		maxs = sizein;
	}
	
	public boolean addOne(String stepid, double minin, double maxin)
	{
		try{
			mins.put(stepid, minin);
			maxs.put(stepid, maxin);
			stepsid.add(stepid);
			size++;
			return true;
		}
		catch(Exception e)
		{
			return  false;
		}
	}
	
	public boolean updateOneMin(String stepid, double minin)
	{
		try{
			if(!mins.containsKey(stepid)) return false;
			mins.put(stepid, minin);			
			
			return true;
		}
		catch(Exception e)
		{
			return  false;
		}
	}
	
	public boolean updateOneMax(String stepid,  double maxin)
	{
		try{	
			if (!maxs.containsKey(stepid)) return false;
			maxs.put(stepid, maxin);
		
			return true;
		}
		catch(Exception e)
		{
			return  false;
		}
	}
	
	public boolean removeOne(String stepid)
	{
		try{
			mins.remove(stepid);
			maxs.remove(stepid);
			for(int i = 0; i < stepsid.size(); i++)
			{
				if (stepsid.get(i) == stepid) 
				{
					stepsid.remove(i);
					break;
				}
			}
			size--;
			return true;
		}
		catch(Exception e)
		{
			return  false;
		}
	}

}
