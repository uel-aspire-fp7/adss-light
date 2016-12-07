package org.uel.aspire.wp4.data;

import java.util.ArrayList;
import java.util.Vector;

public class AttackerCap {
	double att_test;
	
	ArrayList<Double> attacker_effort;
	int size;
	
	public AttackerCap()
	{
		size = 5;
		attacker_effort = new ArrayList<Double>();
	}
	
	public double getatt_test()
	{
		return att_test;	
	}
	public void setatt_test(double in)
	{
		att_test = in;	
	}
	
	public ArrayList<Double> getattacker_effort()
	{
		return attacker_effort;	
	}
	public void setattacker_effort(ArrayList<Double> in)
	{
		attacker_effort = in;	
		size = in.size();
	}
	
	public int getsize()
	{
		return size;
	}
	
	public void onetolist(int sizein) // 这个method用来从一个effort去平均生成一个list, 仅用于attacker文件输入只有一维时
	{
		size = sizein;
		for(int i=0;i<size;i++)
		{
			attacker_effort.add((Double)att_test/size);
		}
	}

	
}
