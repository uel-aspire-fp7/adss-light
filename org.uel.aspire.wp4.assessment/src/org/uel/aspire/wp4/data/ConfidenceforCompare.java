package org.uel.aspire.wp4.data;

import java.util.HashMap;

public class ConfidenceforCompare {
	Double PC;
	Double NegC;
	Double NeuC;
	HashMap<String, EffortInTrans> e1;
	Double p1;
	HashMap<String, EffortInTrans> e2;
	Double p2;
	Boolean exchanged;
	
	public ConfidenceforCompare(HashMap<String, EffortInTrans> e1in, HashMap<String, EffortInTrans> e2in, Double p1in, Double p2in)
	{
		e1 = e1in;
		e2 = e2in;
		p1 = p1in;
		p2 = p2in;
		exchanged  = false;
		if(p1 > p2)
		{
			Double temp = p1;
			p1 = p2;
			p2 = temp;
			exchanged = true;
		}
		PC = p2 - p1*p2;
		NegC = p1 - p1*p2;
		NeuC = 1 - p1 - p2 + 2*p1*p2;
	}
	
	public Double getPC()
	{
		return PC;
	}
	
	public Double getNegC()
	{
		return NegC;
	}
	
	public Double getNeuC()
	{
		return NeuC;
	}
	
	public Boolean getExchanged()
	{
		return exchanged;
	}
	
	public HashMap<String, EffortInTrans> getfirstEff()
	{
		return e1;
	}
	
	public Double getfirstPro()
	{
		return p1;
	}
	
	public HashMap<String, EffortInTrans> getsecondEff()
	{
		return e2;
	}
	
	public Double getsecondPro()
	{
		return p2;
	}

}
