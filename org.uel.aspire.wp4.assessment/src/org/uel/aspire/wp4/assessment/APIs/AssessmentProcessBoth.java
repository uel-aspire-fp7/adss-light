package org.uel.aspire.wp4.assessment.APIs;

import java.io.IOException;

/*
 * This class is designed for implementing SPA interface, and run the assessment process: fitness and simulation.
 * Only for UEL team, other teams in ASPIRE do not need to know the details in this class.
 * The current version is in 01/01/2016, UEL, by Gaofeng ZHANG 
 * */

import java.util.ArrayList;
import java.util.HashMap;



import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pntypes.hlpng.pntd.hlpngdefinition.Page;
import org.uel.aspire.wp4.comparetoolchains.AssessFitness;
import org.uel.aspire.wp4.comparetoolchains.AssessSimulation;
import org.uel.aspire.wp4.comparetoolchains.CTCDirect;
import org.uel.aspire.wp4.comparetoolchains.CompareToolChains;
import org.uel.aspire.wp4.comparetoolchains.MappingFileIn;
import org.uel.aspire.wp4.comparetoolchains.MeasureFileIn;
import org.uel.aspire.wp4.comparetoolchains.TransitionInformationFileIn;

import eu.aspire_fp7.adss.connectors.ACTCConnector;


public class AssessmentProcessBoth implements SPA {

	AttackModel attackModel;
	ProtectionSolution protectionSolution;
	String codebasePath;
	String timeStamp;
	ArrayList<Measure> measureList;
	boolean withFormula;
	
	//////////////this is the sitting for simulation 
	double simu_difference;
	long simu_timeout; 
	MinMax mm;
	//

	public AssessmentProcessBoth()
	{	
	}

	@Override
	public boolean initModel(AttackModel amin, boolean metrics) {
		try{	
			attackModel = new AttackModel() ;	
			attackModel = amin;	
			withFormula = metrics;
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean initCodeBase(String codebasePathin, String timestampin) {
		try{
			codebasePath = "";
			timeStamp = "";
			codebasePath = codebasePathin;
			timeStamp = timestampin;
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean initMeasureList(ArrayList<Measure> measurelistin) {
		try{
			measureList = new ArrayList<Measure>();
			measureList = measurelistin;
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean initProtectionSolution(ProtectionSolution psin) {	
		try{
			protectionSolution = new ProtectionSolution() ;
			protectionSolution = psin;
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public String checkModel() {
		String result = attackModel.checkFormulas();
		if(result=="part") 
		{
			
		}
		return result;
	}

	
	@Override
	public void initPNSimulator(long timeout, double difference) {
		simu_timeout = timeout;
		simu_difference = difference;		
	}

	@Override
	public double runPNSimulator() {
		
		double result = 0.0;
		AssessSimulation as = new AssessSimulation();
		ArrayList<AttackPath> aps = attackModel.getAttackPaths() ;
		
	    ProtectionSolution ps = protectionSolution;
		
		if(measureList == null)
		{
			measureList = new ArrayList<Measure>();
			MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
			try {
				measureList = meas.getMeasures();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		////this part is to collect metrics data. currently, I use formulas, but the future may be different, by Gaofeng, 01/01/2016.
		//this is the place I need Elena to involve:)
		//TODO 
		HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();		
		MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");		
		mapping = map.getMapping();
		///////
		
		as.creatJob(aps, mm, ps,  measureList, mapping,  simu_difference,  simu_timeout);
		result = as.doJob();
		return result;
	}

	@Override
	public void initPNSimulatorM(long timeout, double difference, MinMax mmin) {
		simu_timeout = timeout;
		simu_difference = difference;
		mm = new MinMax();
		mm = mmin;
		for(int i=0;i<attackModel.getAttackSteps().size();i++)
		{
			attackModel.getAttackSteps().get(i).setMax(mm.getMaxs().get(attackModel.getAttackSteps().get(i).getID()));
			attackModel.getAttackSteps().get(i).setMin(mm.getMins().get(attackModel.getAttackSteps().get(i).getID()));
		}
	}

	@Override
	public double runPNSimulatorM() {
		
		double result = 0.0;
		AssessSimulation as = new AssessSimulation();
		ArrayList<AttackPath> aps = attackModel.getAttackPaths() ;
		
	    ProtectionSolution ps = protectionSolution;
		
		if(measureList == null)
		{
			measureList = new ArrayList<Measure>();
			MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
			try {
				measureList = meas.getMeasures();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		as.creatJobM(aps, mm, simu_difference,  simu_timeout);
		result = as.doJobM();
		return result;
	}

	@Override
	public double runProtectionFitness() {		
		double result = 0.0;		
		AssessFitness af = new AssessFitness();
		
	    ArrayList<AttackPath> aps = attackModel.getAttackPaths() ;
	    
	    ProtectionSolution ps = protectionSolution;
		
		if(measureList == null)
		{
			measureList = new ArrayList<Measure>();
			MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
			try {
				measureList = meas.getMeasures();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		////this part is to collect metrics data. currently, I use formulas, but the future may be different, by Gaofeng, 01/01/2016.
		//this is the place I need Elena to involve:)
		//TODO 
		ACTCConnector ac;
		HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();		
		MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");		
		mapping = map.getMapping();
		///////
				
		af.createJob(aps, ps, measureList, mapping);
		result = af.doJob();		
		return result;
	}
	

	public boolean enableAttackModel() {	
		if (attackModel==null)
			return false;
		return true;
	}

	@Override
	public boolean enableProtectionSolution() {		
		if (protectionSolution==null)
			return false;
		return true;
	}

	@Override
	public boolean enableCodeBase() {		
		if (codebasePath==null)
			return false;
		return true;
		
	}


}
