package org.uel.aspire.wp4.data;

/*this class is to store annotation data, used by SPA, edited by Gaofeng, 17/11/1983.
 * */

public class Annotation {
	//ArrayList<String> as = new ArrayList<String>();
	
	int index =-1;
	String annotationContent = "";
	String type= "";
	String fileName= "";
	String functionName= "";
	String lineNumber= "";
	
	public boolean setAnnotation(int indexin, String content, String typein, String filenamein, String functionname, String linenumber)
	{
		try{
			index = indexin;
			annotationContent = content;
			type= typein;
			fileName= filenamein;
			functionName= functionname;
			lineNumber= linenumber;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean setIndex(int indexin)
	{
		try{			
			index = indexin;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean setContent(String content)
	{
		try{			
			annotationContent = content;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean setType(String typein)
	{
		try{
			type= typein;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean setFileName(String filenamein)
	{
		try{
			fileName= filenamein;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean setFunctionName( String functionname)
	{
		try{
			functionName= functionname;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean setLineNumber(String linenumber)
	{
		try{

			lineNumber= linenumber;
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public int getIndex()
	{
		try{
			return index;
		}
		catch(Exception e)
		{return -1;}
	}
	
	public String getContent()
	{
		try{			
			return annotationContent ;			
		}
		catch(Exception e)
		{return "";}
	}
	
	public String getType()
	{
		try{
			return type;			
		}
		catch(Exception e)
		{return "";}
	}
	
	public String getFileName()
	{
		try{
			return fileName;		
		}
		catch(Exception e)
		{return "";}
	}
	
	public String getFunctionName( )
	{
		try{
			return functionName;		
		}
		catch(Exception e)
		{return "";}
	}
	
	public String getLineNumber()
	{
		try{
			return lineNumber;		
		}
		catch(Exception e)
		{return "";}
	}
}
