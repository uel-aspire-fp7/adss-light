package org.uel.aspire.wp4.pnassessment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.data.*;

public class FileInModel {
	
	private HashMap<String, EffortInTrans> effortsin;
	
	public FileInModel()
	{
		effortsin = new HashMap<String, EffortInTrans> ();//ArrayList HashMap
	}
	
	final HashMap<String, EffortInTrans> file_in(int  index)
	{		
		try{
			Scanner inmodel;
			if (index ==1)
		inmodel = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment/attackmodeldata1.txt"));//**
			else
		inmodel = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment/attackmodeldata2.txt"));
			
		int num = inmodel.nextInt();
		int ms = inmodel.nextInt();
		for(int i=0;i<num;i++)
		{
			EffortInTrans effort = new EffortInTrans();
			String ts = inmodel.next();
			ArrayList<Double> al = new ArrayList<Double>();
			for(int j=0;j<ms;j++)
			{
				Double td = inmodel.nextDouble();
				al.add(td);				
			}
			effort.setefforts(al);
			effortsin.put(ts, effort);
		}		
		inmodel.close();
		}
	    catch( FileNotFoundException e)
		{			
	    	System.out.println(e);
	    }
		return effortsin;
	}
}
