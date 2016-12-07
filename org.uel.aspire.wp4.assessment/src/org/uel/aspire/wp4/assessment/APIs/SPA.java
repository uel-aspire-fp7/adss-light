package org.uel.aspire.wp4.assessment.APIs;

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
  * 2015-12-15, this is the new version of the interface of SPA tool for ADSS, on the basis of APIs.java. 
  *   * */

public interface SPA {
	
	//this function is to set a specific attack model which will be calculated for protection assessment
	
		//one petri net, with a list of attack paths
		//metrics true=> the model contains attacksteps with formulas set with metrics/steps associations 
		//metrics false=> the model is just a PN, the metrics/steps associations are determined by SPA 
		public boolean initModel(AttackModel am, boolean metrics);
		
		//codebase for the toolchain metrics, timestamp represents version of the code 
		public boolean initCodeBase(String codebasePath, String timestamp); 
		
		//all measure considered in this assessment, came from the Knowlodge base 
		public boolean initMeasureList(ArrayList<Measure> measurelist); 
		
		// Initialize the protectionConfiguration to retrieve the security metrics. 
		// If there is no precalculated metrics, it calls ACTC to obtain them passing the source code and the corresponfinf protection configuration
		public boolean initProtectionSolution(ProtectionSolution pc);
		
		// if "full", customers can call the runprotectionfitness with no random variables and the metrics/step associations have been set
		// if "part", ADSS has to call initPNSimulator + runPNSimualtor, the mins and maxs are decided by relations among transitions. 
		// if "null", ADSS has to set down mins and maxs for simulation, and initPNSimulatorM + runPNSimualtorM.
		public String checkModel();
		
		// in the condition of data missing for fitness, adss call this method to check the data missing, and obtain these steps without min and max for simulation
		//public MinMax checkDatamissing();	
		
		//this method is used to initiate the simulation with the the timeout, the results' difference for terminate the simulation. 
		public void initPNSimulator(long timeout, double difference);
		
		//this method is to run simulation. 
		public double runPNSimulator();
		
		//this method is used to initiate the simulation with the the timeout, the results' difference for terminate the simulation, and the MinMax to set tow values for each attack steps. 
		public void initPNSimulatorM(long timeout, double difference, MinMax mm);
		
		//this method is to run simulation. 
		public double runPNSimulatorM();
		
		// return the metric delta effort based on attackmodel and th pc parameter
		public double runProtectionFitness();	
		
		//this function is doing the assessing process, and the result is boolean: "true" means that the first PC is better, "false" means that the second PC is better
		//public boolean runCompareAssessment();
		
		//this function is doing the assessing process. Different to the previous one, this one will return the better PC. The method is required by Polito, realised by Gaofeng in 24/11/2015
		//public ProtectionConfiguration runCompare();	
			
		//this function is to check attack model being set or not
		public boolean enableAttackModel();

		//this function is to set some protection configurations which will be calculated for delta-effort, DO COMPARISON, which one is better 
		//public boolean setProtectionConfigurations(ProtectionConfiguration pc1, ProtectionConfiguration pc2);
		
		//this function is to check protection configuration being set or not
		public boolean enableProtectionSolution();
		
		//this function is to check codebase being set or not
		public boolean enableCodeBase();
		
	
	
	
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
