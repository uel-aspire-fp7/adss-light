package org.uel.aspire.wp4.comparetoolchains;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.assessment.APIs.Protection;
import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
/*
 * this is a class to input all protection files in /aspire-protection/ folder. to transfer them to display in the UELPSView 
 * by Gaofeng 17/03/2016
 */

public class ProtectionFilesIn {

	String filepath = "";
	
	
	ArrayList<ProtectionSolution> pss = new ArrayList<ProtectionSolution>();
	
	public ProtectionFilesIn(String filepathin)
	{
		filepath = System.getProperty("user.dir");
		//System.out.println(fullpath);
		filepath = filepath.substring(0, filepath.length()-7 );
		//System.out.println(fullpath);
		filepath = filepath + "/"+ "workspace"+ "/"+"org.uel.aspire.wp4.assessment"+"/";
		filepath = filepath + filepathin;
	}
	
	ProtectionSolution readProtection(String fullfilepath)
	{
		ProtectionSolution pc = new ProtectionSolution();
		//pc.clear();
		try{
			Scanner inmodel;
			
		inmodel = new Scanner(new FileReader(fullfilepath));//**		
		
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
		return pc;
		}
	    catch( FileNotFoundException e)
		{			
	    	return null;
	    }
	}
	
	public ArrayList<ProtectionSolution> getProtectionSolutions()
	{
		File file = new File(filepath);   
       // System.out.print(filepath);  
        File[] array = file.listFiles();   
          
        for(int i=0;i<array.length;i++)
        {   
            if(array[i].isFile())
            {         //    System.out.println(   array[i].getPath());
                pss.add(readProtection(array[i].getPath()));    
            }		
		}
		return pss;
	}
}
