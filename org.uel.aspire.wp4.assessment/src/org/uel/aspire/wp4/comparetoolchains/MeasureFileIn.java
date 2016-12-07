package org.uel.aspire.wp4.comparetoolchains;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.uel.aspire.wp4.assessment.APIs.Measure;

/*
 * this class is used to file in measure file 
 * the following is a sample of this txt file
 * ***********************
15
SCZ
DCZ
SCFC
DCFC
IS
CLV
SDFC
DDFC
SD
SDP
DDP
SV
IEV
SR
S
*******************************
and it is stored in  "E:\eclipse-SDK-3.7.2-win32-x86_64-epnk\eclipse\aspire-config\Measures.txt"
by Gaofeng ZHANG, UEL, 07/07/2015
 */
public class MeasureFileIn {
	
	String filepath = "";
	ArrayList<Measure> measures;
	
	public MeasureFileIn(String filepathin)
	{
		filepath = filepathin;
		measures = new ArrayList<Measure>();
	}
	public ArrayList<Measure> getMeasures() throws IOException
	{
		
		try{
			Scanner inmodel;
			String fullpath = System.getProperty("user.dir");
			//System.out.println(fullpath);
			fullpath = fullpath.substring(0, fullpath.length()-7 );
			//System.out.println(fullpath);
			fullpath = fullpath + "/"+ "workspace"+ "/"+"org.uel.aspire.wp4.assessment";
			//System.out.println(fullpath);
			
			inmodel = new Scanner(new FileReader(fullpath + "/"+ filepath));//**//**		
		
		int num = inmodel.nextInt();	
		
		//this loop it used to evaluate measures from file input	
	
		for(int i=0;i<num;i++)
		{			
			String id = inmodel.next();
			Measure mea = new Measure();
			mea.setID(id);
			if (!measures.add(mea)) continue;
			
		}	
		/////////////////////////////////
		
		inmodel.close();		
		}
	    catch( FileNotFoundException e)
		{			
	    	System.out.println(e);
	    }
		return measures;
	}

}
