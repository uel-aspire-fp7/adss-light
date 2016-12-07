package org.uel.aspire.wp5.assessment.APIs;

import java.util.ArrayList;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;

/*
 * This APIs are designed for ADSS to call related functions for delta-effort of protection configuration.
 * This is the interface, other files in this package are containers classes, except "AssessmentProcess". 
 * 
!!!!IMPORTNANT for ADSS people, when you call the assessment part, there are some issues:
1. Class "AssessmentProcess" is the implementation class of this interface.
2. Please run setAttackModel() and setProtectionConfigurations() BEFORE runCompareAssessment() or runCompare().
3. About the class "AttackModel", for your knowledge base, it may be easy to use the members: "ArrayList<AttackPath> attackPaths" and "ArrayList<AttackStep> attackSteps".
 	Please ignore the "PetriNet petrinet", that is for our UEL's part. :)
4. About the class "ProtectionConfiguration", please refer the class file. Generally speaking, it includes a series of protections with parameters.
	It is flexible if ADSS want to change it.
  * The current version is in 06/10/2015, UEL, by Gaofeng ZHANG*
  * 
  * 2015-11-15, a new method runCompare() is added to match polito's requirements by Gaofeng in UEL. 
  * 
  * IMPORTNAT: THIS INTEFACE HAS BEEN REPLACED BY SPA FOR ADSS BY 01/01/2016!!!!
  *   * */

public interface APIs {
	
	//this function is doing the assessing process, and the result is boolean: "true" means that the first PC is better, "false" means that the second PC is better
	public boolean runCompareAssessment();
	
	//this function is doing the assessing process. Different to the previous one, this one will return the better PC. The method is required by Polito, realised by Gaofeng in 24/11/2015
	public ProtectionSolution runCompare();
	
	//this function is to set a specific attack model which will be calculated for protection assessment
	public boolean setAttackModel(AttackModel am); 
	
	//this function is to check attack model being set or not
	public boolean enableAttackModel();

	//this function is to set some protection configurations which will be calculated for delta-effort, DO COMPARISON, which one is better 
	public boolean setProtectionSolutions(ProtectionSolution pc1, ProtectionSolution pc2);
	
	//this function is to check protection configuration being set or not
	public boolean enableProtectionSolution();
	
	
	
	/*the following part is for other calling ways and under constructing. :) 
	 * 
	//this function is to set some specific attack models which will be calculated for delta-effort of protection configuration, 
	//use weights to balance different attacks in assessment. (hope 1)
	public boolean setAttackModel(ArrayList<AttackModel> ams, ArrayList<Double> ws)
	
	//this function  will return current attack model; if it has not been set, return NULL; if more than one attack, return first one.
	public AttackModel getAttackModel();
	
	//this function is to return the number of protection configurations which will be assessed
	public int numberofProtectionConfiguration();
	
	//this function is the same to the previous one, 
	//only different is to use the same weights for each attack. 
	public boolean setAttackModel(ArrayList<AttackModel> ams);
	
	//this function is to return the number of attack models which will be assessed
	public int numberofAttackModel();
	
	//this function  will return current attack models; if it has not been set, return NULL 
	public ArrayList<AttackModel> getAttackModels();
	
	//this function is to set a protection configuration which will be calculated for delta-effort (hope 2)
	public boolean setProtectionConfiguration(ProtectionConfiguration pc);
	
	//this function  will return current Protection Configuration; if it has not been set, return NULL; if more than one, return first one.
	public ProtectionConfiguration getProtectionConfiguration();
	
	//this function is to set some protection configurations which will be calculated for delta-effort, DO COMPARISON, which one is better 
	public boolean setProtectionConfiguration(ArrayList<ProtectionConfiguration> pcs);
		
	//this function will return current Protection Configurations; if it has not been set, return NULL
	public ArrayList<ProtectionConfiguration> getProtectionConfigurations();
	
	//this function is to run the assessment based on all previous settings, calculate the delta-effort, and feedback the results
	public AssessmentResult runAssessment();	
	
*/
	
}
