package org.uel.aspire.wp4.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Annotations {
	ArrayList<Annotation> list ;
	
	public Annotations()
	{
		list = new ArrayList<Annotation>();
	}
	
	public Annotations(String filepath)
	{		
		list = new ArrayList<Annotation>();
		try {
		FileReader file = new FileReader(filepath);
		BufferedReader buff = new BufferedReader(file);
		boolean eof = false;
		int index = 0;
		while (!eof) 
		{
			String line = buff.readLine();
			if (line == null)
				eof = true;
			else
			{			
				if(line.indexOf("{") != -1)
				{
					Annotation an = new Annotation();
					an.setIndex(index);
					ArrayList<String> onean = new ArrayList<String>();
					onean.clear();
					while(true)					{
						
						String temp = buff.readLine();
						if(temp != null && temp.indexOf("},") == -1)
							onean.add(temp) ;
						else
							break;
					}
					//System.out.println("onean.size() is " + onean.size() );
					//System.out.println("index is " + index );
					//if (index == 41) System.out.println(onean );
					for(int i=0;i<onean.size();i++)
					{
						if (onean.get(i).indexOf("annotation content") != -1) 
							an.setContent( onean.get(i).substring(onean.get(i).indexOf("annotation content")+22, onean.get(i).length()-2) );
						if (onean.get(i).indexOf("annotation type") != -1)
							an.setType( onean.get(i).substring(onean.get(i).indexOf("annotation type")+19, onean.get(i).length()-2) );
						if (onean.get(i).indexOf("file name") != -1)
							an.setFileName(onean.get(i).substring(onean.get(i).indexOf("file name")+13, onean.get(i).length()-2) );
						if (onean.get(i).indexOf("function name") != -1)
							an.setFunctionName( onean.get(i).substring(onean.get(i).indexOf("function name")+17, onean.get(i).length()-2) );
						if (onean.get(i).indexOf("line number") != -1)
						{
							if(onean.get(i).indexOf("[") != -1)
							{
								String a = onean.get(i+1);
								String b = onean.get(i+2);
								an.setLineNumber(a+b);
							}
							else
							{
								an.setLineNumber( onean.get(i).substring(onean.get(i).indexOf(":")+2, onean.get(i).length()-1) );
							}
						}
					}					
					index++;
					list.add(an);
				}				
			}
				
		}
		buff.close();
		file.close();
		} 
		catch (IOException e)
		{
			System.out.println("Annotation File Error -- " + e.toString());
		}		
	}
	
	public Annotation getAnnotation(int index)
	{
		try{			
			return list.get(index);
		}
		catch(Exception e)
		{return null;}
	}
	
	public int getAnnotationsSize()
	{
		try{			
			return list.size();
		}
		catch(Exception e)
		{return -1;}
	}
	
	
	public boolean setAnnotation(int index, Annotation an)
	{
		try{			
			list.set(index, an);
			return true;
		}
		catch(Exception e)
		{return false;}
	}
	
	public boolean addAnnotation(Annotation an)
	{
		try{			
			list.add(an);
			return true;
		}
		catch(Exception e)
		{return false;}
	}


}
