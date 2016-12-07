package org.uel.aspire.wp4.assessment.APIs;

/*
 * This class is a container class to store metrics in attack step. 
 * The current version is in 07/07/2015, UEL, by Gaofeng ZHANG
 * */

import java.util.ArrayList;

public class Metrics {
	
	// this is the unique name of this metrics
	String  id;
	
	//this is the measure list, including all measures of this metrics
	ArrayList<Measure> measureList;
	
	//
	public Metrics()
	{
		id = "";
		measureList = new ArrayList<Measure>();
	}
	
	//this function is to get id of this metrics
	public String getID()
	{
		return id;
	}
	
	//this function is to set id of this metrics
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get measure_list of this metrics
	public ArrayList<Measure> getMeasureList()
	{
		return measureList;
	}
	
	//this function is to set measure_list of this metrics
	public void setMeasureList(ArrayList<Measure> measure_listin)
	{
		measureList = measure_listin;
	}

	//this function is used to add new measure or effort to existing metrics for (CompareToolChain) class, by Gaofeng ZHANG  07/07/2015
	public void addmetrics(String measurename, double effort) {
		
		boolean had = false;
		/*if (measureList.size() == 0) 
		{
			measureList.add(new Measure(measurename, effort));
		}*/
		for(int i = 0 ; i < measureList.size(); i++)
		{
			//System.out.println("i is " +i+ ", and measurename is " + measurename);
			if (measureList.get(i).getID().equals(measurename)) 
			{
				had = true;
				measureList.get(i).setWeight(measureList.get(i).getWeight() + effort);
				break;
			}
		}		
		if (had == false) measureList.add(new Measure(measurename, effort));
	}
	
	//this function will feedback the measure with the name of the input parameter, it is a key-searching process, by Gaofeng ZHANG  07/07/2015
	public Measure getMeasure(String measurename)
	{	
		for(int i=0;i<measureList.size(); i++)
		{
			if ( measureList.get(i).getID().equals(measurename))
				return measureList.get(i);
		}
		return null;
	}
	
	public boolean isEmpty()
	{
		return measureList.isEmpty();
		
	}
}
