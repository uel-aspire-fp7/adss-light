package org.uel.aspire.wp4.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.data.*;

public class FileInAttacker {
	
	private HashMap<String, AttackerCap> attackers;
	private ArrayList<AttackerCap> atts;
	String filepath = "";
	
	public FileInAttacker(String fpin)
	{
		attackers = new HashMap<String, AttackerCap>();
		atts = new ArrayList<AttackerCap>();
		filepath = fpin;
	}
	public FileInAttacker()
	{
		attackers = new HashMap<String, AttackerCap>();
		//filepath = fpin;
	}
	
	public final ArrayList<AttackerCap> file_in()
	{
		try{
			Scanner inattacker = new Scanner(new FileReader(filepath));
		
			int num1 = inattacker.nextInt();	
			for(int i=0;i<num1;i++)
			{
				AttackerCap tempatt = new AttackerCap();
				String ts = inattacker.next();
				Double td = inattacker.nextDouble();
				tempatt.setatt_test(td);			
				attackers.put(ts, tempatt);
				atts.add(tempatt);
			}
			inattacker.close();
		}
		catch( FileNotFoundException e)
		{	
			System.out.println(e);
		}
	return atts;
	}
}
