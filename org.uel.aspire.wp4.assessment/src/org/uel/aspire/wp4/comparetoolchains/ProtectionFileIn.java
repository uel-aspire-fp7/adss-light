package org.uel.aspire.wp4.comparetoolchains;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.assessment.APIs.*;


/*
 * this class is used to file in protection file 
 * the following is a sample of this txt file
 * ***********************
Protection2
1
Obfuscation3 3 
parameter1 0.9 
parameter2 0.4
parameter3 1.0
*******************************
and it is stored in  "E:\eclipse-SDK-3.7.2-win32-x86_64-epnk\eclipse\aspire-config\protection2.txt"
by Gaofeng ZHANG, UEL, 07/07/2015
 */


public class ProtectionFileIn {
	String filepath = "";
	ProtectionSolution pc;
	
	public ProtectionFileIn(String filepathin)
	{
		filepath = filepathin;
	}
	
	boolean readProtection()
	{
		pc = new ProtectionSolution();
		try{
			Scanner inmodel;
			
		inmodel = new Scanner(new FileReader(filepath));//**		
		
		String id = inmodel.next();
		pc.setID(id);
		
		//this loop it used to evaluate pc from file input
		int numpro = inmodel.nextInt();
		ArrayList<Protection> protectionlist = new ArrayList<Protection>();
		protectionlist.clear();
		for(int i=0;i<numpro;i++)
		{			
			String proid = inmodel.next();
			Protection pro = new Protection();
			pro.setID(proid);
			HashMap<String, String> proparameters = new HashMap<String, String>();
			int numpara = inmodel.nextInt();
			for(int j=0;j<numpara;j++)
			{
				String name = inmodel.next();
				String value = inmodel.next();
				proparameters.put(name, value);
			}
			pro.setParameters(proparameters);
			protectionlist.add(pro);
			
		}
		pc.setProtectionList(protectionlist);
		/////////////////////////////////
		
		inmodel.close();
		return true;
		}
	    catch( FileNotFoundException e)
		{			
	    	return false;
	    }
	}
	
	public ProtectionSolution getProtectionSolution()
	{
		readProtection();
		return pc;
	}

}
