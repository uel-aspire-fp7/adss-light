package org.uel.aspire.wp4.comparetoolchains;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.assessment.APIs.*;

/*
 * this class is used to file in mapping file 
 * the following is a sample of this txt file
 * ***********************
Obfuscation1 
SCZ (parameter1+1)*1.2
DCZ (parameter1+1)*1.2
IS (parameter1+1)*1.1
SV (parameter1+1)*1.1

Obfuscation2 
SCZ (parameter1+1)*1.4+3*parameter2
DCZ (parameter1+1)*1.1+parameter2

Obfuscation3 
SCZ (parameter1+1)*1.9-parameter2+(parameter3+2)*1.2
DCZ (parameter1+1)*1.2-parameter2+(parameter3+1)*3.2
SDFC (parameter1+1)*1.2+parameter2+(parameter3+1)*1.5
IS (parameter1+1)*1.9+(parameter3+2)*1.2
SV (parameter1+1)*1.9-parameter2
*******************************
and it is stored in  "E:\eclipse-SDK-3.7.2-win32-x86_64-epnk\eclipse\aspire-config\Protection-to-Measure.txt"
by Gaofeng ZHANG, UEL, 07/07/2015
 */
public class MappingFileIn {
	
	String filepath = "";
	HashMap<String, HashMap<String, String> > mapping;
	
	public MappingFileIn(String filepathin)
	{
		filepath = filepathin;
		mapping = new HashMap<String, HashMap<String, String> >();
	}
	
	public HashMap<String, HashMap<String, String> > getMapping()
	{
		//System.out.println(" getMapping() of class MappingFileIn started");
		try{
			Scanner inmodel;
			
			inmodel = new Scanner(new FileReader(System.getProperty("user.dir") + "/"+ filepath));//**		
		
			int size = inmodel.nextInt();		
			//System.out.println("in the getMapping() of class MappingFileIn, the size of mapping is "+ size);
			//this loop it used to from file input
	
			mapping.clear();
			for(int i=0;i<size;i++)
			{						
				String pro = inmodel.next();			
				HashMap<String, String> mea = new HashMap<String, String>();			
				int nummea = inmodel.nextInt();
				for(int j = 0; j<nummea; j++)
				{				
					String name = inmodel.next();
					String equation = inmodel.next();
					mea.put(name, equation);
				}			
				mapping.put(pro, mea);
			}
			/////////////////////////////////
		
			inmodel.close();
			//System.out.println("in the getMapping() of class MappingFileIn, the final mapping.size() is "+mapping.size());
		}
	    catch( FileNotFoundException e)
		{			
	    	System.out.println(e);
	    }			
		return mapping;
	}

}
