package org.uel.aspire.wp4.data;

import java.util.ArrayList;
import java.util.HashMap;

public class AnnotationTransitionMap {
	ArrayList<String> transitions;
	ArrayList<String> annotations;
	HashMap<String, String> map; // name of transition, name of annotations. 
	
	public AnnotationTransitionMap()
	{
		transitions = new ArrayList<String>();
		annotations = new ArrayList<String>();
		map = new HashMap<String, String>() ;
	}
	
	public boolean addMap(String a, String b)
	{
		try
		{
			/*boolean thad = false;
			for(int i=0;i<transitions.size();i++)
			{
				if (transitions.equals(a)) {thad = true; break;}
			}
			if(thad == false) 
			{
				transitions.add(a);
				map.replace(a, b);
			}
			else*/
			{
				map.put(a, b);
			}
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public ArrayList<String> getTransitions()
	{
		try
		{
			return transitions;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public boolean setTransitions(ArrayList<String> a)
	{
		try
		{
			transitions = a;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public ArrayList<String> getAnnotations()
	{
		try
		{
			return annotations;
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public boolean setAnnotations(ArrayList<String> a)
	{
		try
		{
			annotations = a;
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public String getMap(String a)
	{
		try
		{
			String ann = "";
			ann = map.get(a);
			return ann;
		}
		catch(Exception e)
		{
			return null;
		}
	}

}
