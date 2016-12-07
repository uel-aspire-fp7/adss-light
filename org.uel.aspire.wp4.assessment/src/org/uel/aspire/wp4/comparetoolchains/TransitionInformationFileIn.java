package org.uel.aspire.wp4.comparetoolchains;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.assessment.APIs.*;


/*
 * this class is used to file in TransitionInformation file 
 * this is for ADSS when their owl files do not have weights and metrics formulas
 * the following is a sample of this txt file
 * ***********************
to be appear~~~~~
*******************************
and it is stored in  "E:\eclipse-SDK-3.7.2-win32-x86_64-epnk\eclipse\aspire-config\***.txt"
by Gaofeng ZHANG, UEL, 07/10/2015
 */


public class TransitionInformationFileIn {
	String filepath = "";
	ArrayList<AttackPath> aps;
	
	public TransitionInformationFileIn(String filepathin)
	{
		filepath = filepathin;
	}
	
	public TransitionInformationFileIn()
	{
		//filepath = filepathin;
	}
	
	boolean readTransitionInformation()
	{
		/*pc = new ProtectionConfiguration();
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
	    }*/
		return true;
	}
	
	public ArrayList<AttackPath> getTransitionInformation( ArrayList<AttackPath> apin)
	{
		readTransitionInformation();
		aps = apin;
		return aps;
	}

}
