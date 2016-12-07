package org.uel.aspire.wp4.formula;

import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.Metrics;

/*this is to feedback the values of each measure in the pn model of each transition's string 8:(0.5*SCZ+0.7*DCZ)
 * 
 * by Gaofeng ZHANG, UEL, 07/07/2015
 */
public class StringtoMetrics {
	
	//This function is to feedback the values of each measure in the pn model of each transition's string 8:(0.5*SCZ+0.7*DCZ)
	public static Metrics getResults(String metrics) 
	{
		Metrics met = new Metrics();
		
		
		int loc = metrics.indexOf(":");
		String measures = metrics.substring(loc+1);
		while(true)
		{
			int add = measures.indexOf("+");
			int star = measures.indexOf("*");
			if(add!= -1)
			{
				double val = Double.parseDouble(measures.substring(0, star) );
				
				String id = measures.substring(star+1, add);
				//System.out.println("in StringtoMetrics, getResults(String metrics), val is : " + val);
				//System.out.println("in StringtoMetrics, getResults(String metrics), id is : " + id);
				met.addmetrics(id, val);
				/*for(int i=0;i<met.getMeasureList().size();i++)
				{
					System.out.println("in StringtoMetrics, getResults(String metrics), met.getMeasureList().get(i) is : " + met.getMeasureList().get(i).getValue());
				}*/
				measures = measures.substring(add+1);
			}
			else
			{
				double val=0.0 ;
				try{
				val = Double.parseDouble(measures.substring(0, star) );
				}
				catch(Exception e)
				{
					System.out.println(measures);
					System.out.println(e);
				}
				String id = measures.substring(star+1);
				met.addmetrics(id, val);			
				break;
			}
		}
		return met;
	}	
	
	//this function  is use to feedback the weight in the string of the pn model of each transition's string (10:0.5*SCZ+0.7*DCZ)
	public static double getWeight(String metrics) 
	{
		double wei = 0.0;		
		int loc = metrics.indexOf(":");
		String weis = metrics.substring(0, loc);
		wei = Double.parseDouble(weis);
		return wei;
	}
	

}
