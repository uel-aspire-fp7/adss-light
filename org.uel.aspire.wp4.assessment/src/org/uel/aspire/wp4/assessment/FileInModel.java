package org.uel.aspire.wp4.assessment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import org.uel.aspire.wp4.data.EffortInTrans;

public class FileInModel {
	
	//private Vector<EffortinTrans> effortsin;
	private HashMap<String, EffortInTrans> effortsin;
	
	public FileInModel()
	{
		effortsin = new HashMap<String, EffortInTrans> ();//ArrayList HashMap
	}
	
	final HashMap<String, EffortInTrans> file_in()
	{
		//effortsin = new Vector<EffortinTrans>();
		try{
		Scanner inmodel = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment/attackmodeldata.txt"));//**
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
