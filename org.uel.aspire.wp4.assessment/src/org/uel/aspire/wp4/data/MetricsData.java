package org.uel.aspire.wp4.data;
//this metrics data include general metric and code region metrics, by Gaofeng

import java.util.HashMap;

import org.uel.aspire.wp4.assessment.APIs.Metrics;

public class MetricsData {
	
	String pcname = "";
	Metrics generalMetrics;
	HashMap<String, Metrics> regionMetrics;
	
	public MetricsData(String name)
	{
		pcname = name;
		generalMetrics = new Metrics();
		regionMetrics = new HashMap<String, Metrics>();
	}
	
	public boolean addGMetrics(String mname, double mvalue)
	{
		try{
		generalMetrics.addmetrics(mname, mvalue);
		return true;
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return false;
		}
	}
	
	public boolean setGMetrics(Metrics mets)
	{
		try{
		generalMetrics = mets;
		return true;
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return false;
		}
	}
	
	public Metrics getGMetrics()
	{
		try{
		return generalMetrics;
		
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return null;
		}
	}
	
	public boolean addRMetrics(String code, Metrics met)
	{
		try{
		regionMetrics.put(code, met);
		return true;
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return false;
		}
	}
	
	public boolean setRMetrics(HashMap<String, Metrics> mets)
	{
		try{
		regionMetrics = mets;
		return true;
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return false;
		}
	}
	
	public Metrics getRMetrics(String region)
	{
		try{
		return regionMetrics.get(region);
		
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return null;
		}
	}
	
	public HashMap<String, Metrics> getALLRMetrics()
	{
		try{
		return regionMetrics;
		
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return null;
		}
	}
	
	public boolean setPCName(String namein)
	{
		try{
		pcname = namein;
		return true;
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return false;
		}
	}
	
	public String getPCName()
	{
		try{
			return pcname;
		}
		catch(Exception e)
		{
			System.out.println("In Class MetricsData, Errors happen!");
			return "";
		}
	}

}
