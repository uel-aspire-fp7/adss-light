/**
 * 
 */
package org.uel.aspire.spa.views;

import java.util.ArrayList;
import java.util.Random;

import eu.aspire_fp7.adss.akb.Solution;

/**
 * @author Gaofeng
 *
 */
public class AssessData {
	
	Solution pc;
	double result;
	ArrayList<String> keyPath;
	
	AssessData()
	{
		//pc =  new Solution() ;
		
		//keyPath = new ArrayList<String>>() ;
	}
	
	public boolean setSolution(Solution pcin)
	{
		try
		{pc = pcin; return true;}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public Solution getSolution()
	{
		return pc;		
	}
	
	public boolean setResult(double resultin)
	{
		try
		{result = resultin; return true;}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public double getResult()
	{
		return result;		
	}
	
	public boolean setKeyPath(ArrayList<String> keyPathin)
	{
		try
		{keyPath = keyPathin; return true;}
		catch(Exception e)
		{
			return false;
		}
	}
	
	public ArrayList<String> getKeyPath()
	{	
		if(keyPath == null)
		{	
			ArrayList<String> result = new ArrayList<String>();
			result.add("T1");
			result.add("T2");
			result.add("T3");
			result.add("T4");
			result.add("T5");
			result.add("T6");
			result.add("T7");

			Random ra = new Random();
			int i = ra.nextInt(4);
			if(i==0) { result.add("T8"); return result;}
			if(i==1) { result.add("T9"); result.add("T11");return result;}
			if(i==2) { result.add("T10"); result.add("T11");return result;}
			if(i==3) { result.add("T12"); return result;}
		}
		return keyPath;		
	}

	public String getKeyPathString() {
		String path = "";
		ArrayList<String> pathtemp = getKeyPath();
		if(pathtemp == null) return path;
		for(int i=0;i<pathtemp.size();i++)
		{
			path = path + pathtemp.get(i);
			if(i != (pathtemp.size() -1) )
			{
				path = path + " -> ";
			}
		}
		return path;
	}
	
}
