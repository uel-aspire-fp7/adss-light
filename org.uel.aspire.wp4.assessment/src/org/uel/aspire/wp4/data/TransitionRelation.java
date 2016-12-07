package org.uel.aspire.wp4.data;

/*
 * this class is used to support the attack steps' relation, like "T3>T8". 
 * by Gaofeng, UEL, 04/01/2016.
 */

public class TransitionRelation {
	String largeID;
	String smallID;
	
	public TransitionRelation()
	{}
	
	public TransitionRelation(String lin, String sin)
	{
		largeID = lin; 
		smallID = sin;
	}
	public void setLargeID(String lin)
	{
		largeID = lin;
	}
	public String getLargeID()
	{
		return largeID;
	}
	public void setSmallID(String sin)
	{
		smallID = sin;
	}
	public String getSmallID()
	{
		return smallID;
	}
	

}
