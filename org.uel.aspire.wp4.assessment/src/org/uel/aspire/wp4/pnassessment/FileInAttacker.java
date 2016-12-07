package org.uel.aspire.wp4.pnassessment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import org.uel.aspire.wp4.data.*;

public class FileInAttacker {
	
	private HashMap<String, AttackerCap> attackers;
	
	public FileInAttacker()
	{
		attackers = new HashMap<String, AttackerCap>();
	}
	
	final HashMap<String, AttackerCap> file_in()
	{
		try{
			Scanner inattacker = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\attacker.txt"));
		
			int num1 = inattacker.nextInt();	
			for(int i=0;i<num1;i++)
			{
				AttackerCap tempatt = new AttackerCap();
				String ts = inattacker.next();
				Double td = inattacker.nextDouble();
				tempatt.setatt_test(td);			
				attackers.put(ts, tempatt);
			}
			inattacker.close();
		}
		catch( FileNotFoundException e)
		{	
			System.out.println(e);
		}
	return attackers;
	}
}
