package org.uel.aspire.wp4.formula;

import java.util.HashMap;

import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.assessment.APIs.Protection;

/*this is to transfer the string type formula like "(parameter1+1)*1.9-parameter2+(parameter3+2)*1.2" and get the result to return
 * current version is a preliminary one
 * by Gaofeng ZHANG, UEL, 07/07/2015
 */
public class StringtoFormula {
	
	public static double getResults(String formula, Protection pro) 
	{
		 double result = 0;
		
		//TODO
		if (formula.equals("(parameter1+1)*1.2" )) 
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.2;
		if (formula.equals("(parameter1+1)*1.2") )
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.2;
		if (formula.equals("(parameter1+1)*1.1"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.1;
		if (formula.equals( "(parameter1+1)*1.1"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.1;

	
		if (formula.equals( "(parameter1+1)*1.4+3*parameter2"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.4+3*Double.parseDouble(pro.getParameters().get("parameter2"));
		if (formula.equals( "(parameter1+1)*1.1+parameter2"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.4+3*Double.parseDouble(pro.getParameters().get("parameter2"));

	
		if (formula.equals("(parameter1+1)*1.9-parameter2+(parameter3+2)*1.2"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.9-Double.parseDouble(pro.getParameters().get("parameter2"))+(Double.parseDouble(pro.getParameters().get("parameter3"))+2)*1.2;
		if (formula.equals("(parameter1+1)*1.2-parameter2+(parameter3+1)*3.2"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.2-Double.parseDouble(pro.getParameters().get("parameter2"))+(Double.parseDouble(pro.getParameters().get("parameter3"))+1)*3.2;
		if (formula.equals( "(parameter1+1)*1.2+parameter2+(parameter3+1)*1.5"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.2+Double.parseDouble(pro.getParameters().get("parameter2"))+(Double.parseDouble(pro.getParameters().get("parameter3"))+1)*1.5;
		if (formula.equals( "(parameter1+1)*1.9+(parameter3+2)*1.2"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.9+(Double.parseDouble(pro.getParameters().get("parameter3"))+2)*1.2;
		if (formula.equals( "(parameter1+1)*1.9-parameter2"))
			result = (Double.parseDouble(pro.getParameters().get("parameter1"))+1)*1.9-Double.parseDouble(pro.getParameters().get("parameter2"));
	
		return result;
	}	


}
