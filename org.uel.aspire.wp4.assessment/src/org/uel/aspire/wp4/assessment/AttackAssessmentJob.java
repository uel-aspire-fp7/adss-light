package org.uel.aspire.wp4.assessment;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.pnml.tools.epnk.actions.framework.jobs.AbstractEPNKJob;
import org.pnml.tools.epnk.helpers.FlatAccess;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.Name;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PlaceNode;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.pnml.tools.epnk.pntypes.ptnet.PTMarking;
import org.pnml.tools.epnk.pntypes.ptnet.Place;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.ConfidenceforCompare;
import org.uel.aspire.wp4.data.EffortInTrans;
import org.uel.aspire.wp4.data.FileInAttacker;

/*import de.upb.swt.mcie.formulas.BinaryOp;
import de.upb.swt.mcie.formulas.Constant;
import de.upb.swt.mcie.formulas.Formula;
import de.upb.swt.mcie.formulas.UnaryOp;
import de.upb.swt.mcie.formulas.Variable;
import de.upb.swt.mcie.mc.Transitionsystem;
import de.upb.swt.mcie.parser.Parser;
import de.upb.swt.mcie.robdds.ChangeSet;
import de.upb.swt.mcie.robdds.Context;
import de.upb.swt.mcie.robdds.ROBDD;*/





///
import org.eclipse.emf.common.util.EList;
///
public class AttackAssessmentJob extends AbstractEPNKJob {
	

	private double result;
	private HashMap<String, EffortInTrans> efforts;
	
	private HashMap<String, EffortInTrans> efforts1;
	private HashMap<String, EffortInTrans> efforts2;
	
	private ArrayList< AttackerCap> attackers;
	
	
	public AttackAssessmentJob(PetriNet petrinet, String defaultInput, ArrayList< AttackerCap> attackersin) {
		super(petrinet, "ePNK-uel: Attck Assessment job");
		
		efforts = new HashMap<String, EffortInTrans>();
		efforts1 = new HashMap<String, EffortInTrans>();
		efforts2 = new HashMap<String, EffortInTrans>();
		
		//attackers = new ArrayList< AttackerCap>();
		
		attackers = attackersin;
		
		result = 0.0;
	}

	
	@Override
	protected boolean prepare() {

		FileInModel model = new FileInModel();
		efforts = model.file_in();
		
		FileInModelforCompare modelc = new FileInModelforCompare();
		efforts1 = modelc.file_in1();
		efforts2 = modelc.file_in2();
		
		if(efforts1.equals(efforts2)) System.out.println("ERROR: TWO DATA FOR TOOL CHAINS ARE SAME!");
		
		//FileInAttacker att = new FileInAttacker();
		//attackers = att.file_in();

		return true;		
	}

	@Override
	protected void run() {
		//result = "Attack Assessment results:\n\r";
		//System.out.println( getPetriNet().getPage().get(0).getPageLabelProxy().get(0).getText())  ;		
		///here we do the attack assessment function
		 
		 ////////++++++++++++++++	20150413 use Singlesimulation class	to wp4, use txt file iput 简单attack过程的仿真，单次执行
      /*  FileWriter uelFile;
		try {
				uelFile = new FileWriter ("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\simulationresult.txt");
				PrintWriter uelWriter = new PrintWriter (uelFile);
				uelWriter.println(attackers.size());
				if(attackers.isEmpty()) System.out.println("attackers is empty!");
				if(efforts.isEmpty()) System.out.println("efforts is empty!");
				for(int i=1;i<attackers.size()+1;i++)
				{
			//		System.out.println("+++++++++++");
					SingleSimulation onesimu = new SingleSimulation(getPetriNet(), efforts, attackers.get("attacker"+i));
					//System.out.println("+++++++++++");
					boolean oneresult = onesimu.run_simulation();
					//System.out.println("+++++++++++");
					uelWriter.println(oneresult);
			//		System.out.println("In Singlesimulation, one result is " + oneresult);
				}	
				uelWriter.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}		 
		 ////////++++++++++++++++
		System.out.println("It is done by 20150413 Singlesimulation, please check the result 'simulationresult.txt' file! ");	
		
		//////////////+++++++++++++++ 20150414 for monte carlo idea by singlesimulation 使用Monte Carlo思想，多次执行仿真，指定执行次数
		try {
			FileWriter paperFile;
			paperFile = new FileWriter ("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\simulationresult_for_paper.txt");
			PrintWriter paperWriter = new PrintWriter (paperFile);			
			int round = 10;
			for(int i=0;i<round;i++)
			{
				AttackerCap tempattacker = new AttackerCap();
				tempattacker.getattacker_effort().clear();						
				for (int k =0; k<tempattacker.getsize(); k++)
				{			
					Random rn = new Random();				
					tempattacker.getattacker_effort().add(rn.nextDouble()*20);					
				}
							
				HashMap<String, EffortInTrans> tempefforts = new HashMap<String, EffortInTrans>();
				tempefforts.clear();
			
				//System.out.println("++++++++++++++++");
			
				EffortInTrans tempeffort = new EffortInTrans();
				//tempeffort.getefforts() = new ArrayList<Double>();
				//tempeffort.effortsmap = new HashMap<String, Double>();
			
				//System.out.println("++++++++++++++++");
				//System.out.println("In paper's Singlesimulation, tempefforts is " + tempefforts);
			
				FlatAccess flat = new FlatAccess(getPetriNet());				
				for (int k = 1; k < flat.getTransitions().size() +1 ; k++)
				{		
					tempeffort.getefforts().clear();
					for (int j = 0; j < tempeffort.getsize(); j++)
					{
						Random rn = new Random();
						tempeffort.getefforts().add(rn.nextDouble());
						//tempeffort.effortsmap.put("m" + j, rn.nextDouble());
					}
					tempefforts.put("t" + k, tempeffort);
				}
				//System.out.println("In paper's Singlesimulation, tempefforts is " + tempefforts);
			
		
				SingleSimulation onesimu = new SingleSimulation(getPetriNet(), tempefforts, tempattacker);
				//System.out.println("++++++++++++++++");
				boolean oneresult = onesimu.run_simulation();
				paperWriter.println(oneresult);
				//System.out.println("In paper's Singlesimulation, one result is " + oneresult + " , in round " + i + " . ");
			}	
			paperWriter.close();
		} catch (IOException e) {				
			e.printStackTrace();
		}
		System.out.println("It is done by 20150414 for Monte Carlo idea by SingleSimulation, please check the result 'simulationresult_for_paper.txt' file! ");	*/
		
		//////////////+++++++++++++++ 20150420 for monte carlo idea by mcsimulation, 使用Monte Carlo思想，多次执行仿真，不指定执行次数，指定结果稳定程度来终止循环(class: MCSimulation)
		try {
			//FileWriter mcsFile;
		//mcsFile = new FileWriter ("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\simulationresult_mc.txt");
		//PrintWriter paperWriter = new PrintWriter (mcsFile);
		
			AttackerCap tempattacker = new AttackerCap();
			tempattacker.getattacker_effort().clear();
		
			//System.out.println("tempattacker.getsize()is "  + tempattacker.getsize());
			for (int k =0; k<tempattacker.getsize(); k++)
			{				
				Random rn = new Random();			
				tempattacker.getattacker_effort().add(rn.nextDouble()*20);				
			}
			
			HashMap<String, EffortInTrans> tempefforts = new HashMap<String, EffortInTrans>();
			tempefforts.clear();			
			EffortInTrans tempeffort = new EffortInTrans();
			
			FlatAccess flat = new FlatAccess(getPetriNet());				
			for (int k = 1; k < flat.getTransitions().size() +1 ; k++)
			{		
				tempeffort.getefforts().clear();
				for (int j = 0; j < tempeffort.getsize(); j++)
				{
					Random rn = new Random();
					tempeffort.getefforts().add(rn.nextDouble());					
				}
				tempefforts.put("t" + k, tempeffort);
			}
			//System.out.println(attackers);
			if (attackers==null) System.out.println("attackers==null in AttackAssessmentjob" );
			//System.out.print("job"); System.out.println(attackers);
			MCSimulation onesimu = new MCSimulation(getPetriNet(), tempefforts, attackers);
			
			result = onesimu.run_mcsimulation(null);
			//paperWriter.println(oneresult);
			
		//return oneresult;
			//paperWriter.close();
		} catch (Exception e) {				
			e.printStackTrace();
		}
		//System.out.println("It is done by 20150420 for Monte Carlo idea by MCSimulation, please check the result 'simulationresult_mc.txt' file! ");
				
	/*	
		//////////////+++++++++++++++ 20150505 for monte carlo idea by compare two tool chains, 使用Monte Carlo思想，用于比较两个不同的tool chain
	try {
		FileWriter mcsFile;
		mcsFile = new FileWriter ("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\simulationresult_for_compare.txt");
		PrintWriter paperWriter = new PrintWriter (mcsFile);
	
		AttackerCap tempattacker = new AttackerCap();
		tempattacker.getattacker_effort().clear();
		
		//System.out.println("tempattacker.getsize() is "  + tempattacker.getsize());
		
		for (int k =0; k<tempattacker.getsize(); k++)
		{				
			Random rn = new Random();			
			tempattacker.getattacker_effort().add(rn.nextDouble()*20);				
		}	
		
		CompareSimulation onesimu = new CompareSimulation(getPetriNet(), efforts1, efforts2, tempattacker);
		
		String oneresult = onesimu.run_comparesimulation(null);
		paperWriter.println(oneresult);
		
		
		paperWriter.close();
	} catch (IOException e) {				
		e.printStackTrace();
	}
	System.out.println("It is done by 20150505 for Monte Carlo idea by compare two tool chains, please check the result 'simulationresult_for_compare.txt' file! ");
	
	
	//////////////+++++++++++++++ 20150522 for monte carlo idea 使用文章中的confidence比较系列pcs
	try {
		FileWriter mcsFile;
		mcsFile = new FileWriter ("E:\\Java-workspace-indigo-epnk\\org.uel.aspire.wp4.assessment\\simulationresult_for_paper20150522.txt");
		PrintWriter paperWriter = new PrintWriter (mcsFile);

		AttackerCap tempattacker = new AttackerCap();
		tempattacker.getattacker_effort().clear();
	
		//System.out.println("tempattacker.getsize() is "  + tempattacker.getsize());
	
		for (int k =0; k<tempattacker.getsize(); k++)
		{				
			Random rn = new Random();			
			tempattacker.getattacker_effort().add(rn.nextDouble()*20);				
		}	
	
		PaperCompareSimulation onesimu = new PaperCompareSimulation(getPetriNet(), efforts1, efforts2, tempattacker);
	
		ConfidenceforCompare oneresult = onesimu.run_papercomparesimulation(null);
		paperWriter.println(oneresult);
	
	
		paperWriter.close();
	} 
	catch (IOException e) 
	{				
		e.printStackTrace();
	}
	System.out.println("It is done by 20150522 for Monte Carlo idea by compare two tool chains with confidences, please check the result 'simulationresult_for_paper20150522.txt' file! ");
*/

	System.out.println("It is done by Monte Carlo Simulation ");		
	}	
	
	public double getresult()
	{return result;}
}
