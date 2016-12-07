/**
 * 
 */
package org.uel.aspire.wp4.logger;

import java.io.File;
import java.io.FileWriter;


/**
 * @author Gaofeng
 * 2016/04/21
 * to display the assessment process at UEL
 *
 */
public class SPALogger {
	private String filepath;
	String fullpath;
	String  contents = "";
	

	/**
	 * 
	 */
	public SPALogger(String pathin) {
		filepath = pathin;
	}
	
	public SPALogger() {
		filepath = "E:/eclipse mars for ADSS/workspace/org.uel.aspire.wp4.assessment/log/";
		fullpath = filepath + "log_" + System.currentTimeMillis() + ".txt";
		File file = new File(fullpath); 
	     if(file.exists()) {   System.out.println("Log File existed!");  }  
	     try{
	            if (file.createNewFile() == false) {           
	                System.out.println("Creating log file Failed!" );  	                
	            }   
	     } catch(Exception e)
	     { System.out.println("Creating log file Failed!" );}
		
	}
	
	public boolean print(String msg)
	{
		try{
		//System.out.println(msg);
		  FileWriter writer=new FileWriter(fullpath,true);		  
		  writer.write(msg);
		  writer.write(System.getProperty("line.separator"));
		  contents = contents+msg+System.getProperty("line.separator");
		  writer.close();
	    return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean changeLine()
	{
		try{
		//System.out.println();
		  FileWriter writer=new FileWriter(fullpath,true);		  
		  writer.write(System.getProperty("line.separator"));
		  contents = contents+System.getProperty("line.separator");
		  writer.close();
		return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public String getContent()
	{
		return contents;
	}
}
