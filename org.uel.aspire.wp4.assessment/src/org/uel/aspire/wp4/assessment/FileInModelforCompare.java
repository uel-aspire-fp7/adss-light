package org.uel.aspire.wp4.assessment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.data.EffortInTrans;

public class FileInModelforCompare {
	
	private HashMap<String, EffortInTrans> effortsin1;
	private HashMap<String, EffortInTrans> effortsin2;
	
	public FileInModelforCompare()
	{
		effortsin1 = new HashMap<String, EffortInTrans> ();//ArrayList HashMap
		effortsin2 = new HashMap<String, EffortInTrans> ();//ArrayList HashMap
	}
	
	final HashMap<String, EffortInTrans> file_in1()
	{
		effortsin1.clear();
		try{
		Scanner inmodel1 = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment/attackmodeldata1.txt"));//**
		int num = inmodel1.nextInt();
		int ms = inmodel1.nextInt();
		for(int i=0;i<num;i++)
		{
			EffortInTrans effort = new EffortInTrans();
			String ts = inmodel1.next();
			ArrayList<Double> al = new ArrayList<Double>();
			for(int j=0;j<ms;j++)
			{
				Double td = inmodel1.nextDouble();
				al.add(td);				
			}
			effort.setefforts(al);
			effortsin1.put(ts, effort);
		}		
		inmodel1.close();
		}
	    catch( FileNotFoundException e)
		{			
	    	System.out.println(e);
	    }
		return effortsin1;
	}
	
	final HashMap<String, EffortInTrans> file_in2()
	{
		effortsin2.clear();
		try{
		Scanner inmodel2 = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment/attackmodeldata2.txt"));//**
		int num = inmodel2.nextInt();
		int ms = inmodel2.nextInt();
		for(int i=0;i<num;i++)
		{
			EffortInTrans effort = new EffortInTrans();
			String ts = inmodel2.next();
			ArrayList<Double> al = new ArrayList<Double>();
			for(int j=0;j<ms;j++)
			{
				Double td = inmodel2.nextDouble();
				al.add(td);				
			}
			effort.setefforts(al);
			effortsin2.put(ts, effort);
		}		
		inmodel2.close();
		}
	    catch( FileNotFoundException e)
		{			
	    	System.out.println(e);
	    }
		return effortsin2;
	}
	
	final HashMap<String, EffortInTrans> random_in()
	{
		effortsin1.clear();
		try{
		Scanner inmodel1 = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment/attackmodeldata1.txt"));//**
		int num = inmodel1.nextInt();
		int ms = inmodel1.nextInt();
		for(int i=0;i<num;i++)
		{
			EffortInTrans effort = new EffortInTrans();
			String ts = inmodel1.next();
			ArrayList<Double> al = new ArrayList<Double>();
			for(int j=0;j<ms;j++)
			{
				Double td = inmodel1.nextDouble();
				al.add(td);				
			}
			effort.setefforts(al);
			effortsin1.put(ts, effort);
		}		
		inmodel1.close();
		}
	    catch( FileNotFoundException e)
		{			
	    	System.out.println(e);
	    }
		return effortsin1;
	}

}
