package org.uel.aspire.wp4.assessment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

import org.uel.aspire.wp4.data.AttackerCap;



public class FileInAttacker {
	
	private HashMap<String, AttackerCap> attackers;
	
	public FileInAttacker()
	{
		attackers = new HashMap<String, AttackerCap>();
	}
	
	final HashMap<String, AttackerCap> file_in()
	{
	try{
		Scanner inattacker = new Scanner(new FileReader("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\attacker.txt"));//TODO
		//Scanner inattacker = new Scanner(new FileReader(Filein_attacker.class.getClassLoader().getResource("attacker.txt").getPath()));
		int num1 = inattacker.nextInt();
		//System.out.println(num1);
		for(int i=0;i<num1;i++)
		{
			AttackerCap tempatt = new AttackerCap();
			String ts = inattacker.next();
			Double td = inattacker.nextDouble();
			tempatt.setatt_test(td);
			//tempatt.onetolist();
			//System.out.println(tempatt.getattacker_effort());
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
