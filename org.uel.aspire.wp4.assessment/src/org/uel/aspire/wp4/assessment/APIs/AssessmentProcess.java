package org.uel.aspire.wp4.assessment.APIs;

import java.io.IOException;

/*
 * This class is designed for implementing APIs interface, and run the assessment process.
 * Only for UEL team, other teams in ASPIRE do not need to know the details in this class.
 * The current version is in 06/10/2015, UEL, by Gaofeng ZHANG
 * 
 * In 2015-11-25, a new method "public ProtectionConfiguration runCompare()" is added by Gaofeng in UEL, to provide a more option for assessment result (not only boolean). 
 * */

import java.util.ArrayList;
import java.util.HashMap;



import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pntypes.hlpng.pntd.hlpngdefinition.Page;
import org.uel.aspire.wp4.comparetoolchains.CTCDirect;
import org.uel.aspire.wp4.comparetoolchains.CompareToolChains;
import org.uel.aspire.wp4.comparetoolchains.MappingFileIn;
import org.uel.aspire.wp4.comparetoolchains.MeasureFileIn;
import org.uel.aspire.wp4.comparetoolchains.TransitionInformationFileIn;


public class AssessmentProcess implements APIs {

	AttackModel attackModel;
	ArrayList<AttackModel> attackModels;
	ProtectionSolution protectionSolution;
	ArrayList<ProtectionSolution> protectionSolutions;
	ArrayList<Double> weights;
	
	public AssessmentProcess()
	{
		attackModel = new AttackModel() ;
		attackModels = new ArrayList<AttackModel>() ;
		protectionSolution = new ProtectionSolution() ;
		protectionSolutions = new ArrayList<ProtectionSolution>() ;
		weights = new ArrayList<Double>() ;
	}
	
	public boolean setAttackModel(AttackModel amin) {
		// TODO Auto-generated method stub
		attackModel = amin;
		attackModels.clear();
		attackModels.add(amin);
		weights.clear();
		weights.add(1.0);
		return true;
	}

	public boolean enableAttackModel() {
		// TODO Auto-generated method stub
		if (attackModel==null)
			return false;
		return true;
	}

	public boolean setAttackModel(ArrayList<AttackModel> amsin,
			ArrayList<Double> wsin) {
		// TODO Auto-generated method stub
		attackModels.clear();
		attackModels = amsin;
		weights.clear();
		weights = wsin;
		return true;
	}

	public AttackModel getAttackModel() {
		// TODO Auto-generated method stub
		return attackModel;
	}

	public boolean setAttackModel(ArrayList<AttackModel> amsin) {
		// TODO Auto-generated method stub
		attackModel = amsin.get(0);
		attackModels.clear();
		attackModels = amsin;
		weights.clear();
		for(int i=0;i<amsin.size();i++)
		{
			weights.add(1.0);
		}
		return true;
	}

	public int numberofAttackModel() {
		// TODO Auto-generated method stub
		return attackModels.size();
	}

	public ArrayList<AttackModel> getAttackModels() {
		// TODO Auto-generated method stub
		return attackModels;
	}

	public boolean setProtectionSolution(ProtectionSolution pcin) {
		// TODO Auto-generated method stub
		protectionSolution = pcin;
		protectionSolutions.clear();
		protectionSolutions.add(pcin);
		return true;
	}

	public ProtectionSolution getProtectionSolution() {
		// TODO Auto-generated method stub
		return protectionSolution;
	}

	public boolean enableProtectionSolution() {
		// TODO Auto-generated method stub
		if (protectionSolution==null || (protectionSolutions.size()<2) )
			return false;
		return true;
	}

	public boolean setProtectionSolution(
			ArrayList<ProtectionSolution> pcsin) {
		// TODO Auto-generated method stub
		protectionSolution = pcsin.get(0);
		protectionSolutions.clear();
		protectionSolutions = pcsin;
		return true;
	}

	public int numberofProtectionConfiguration() {
		// TODO Auto-generated method stub
		return protectionSolutions.size();
	}

	public ArrayList<ProtectionSolution> getProtectionSolutions() {
		// TODO Auto-generated method stub
		return protectionSolutions;
	}

	public AssessmentResult runAssessment() {
		// TODO Auto-generated method stub
		
			////call the related functions: assessment or simulation 
			CompareToolChains ctc = new CompareToolChains();
		    PetriNet ptmodel = attackModel.getPetriNet();
		    
		    if ( protectionSolutions.size()<2 ) return null;
			ProtectionSolution pc1 = protectionSolution;
			ProtectionSolution pc2 = protectionSolutions.get(1);
			
			ArrayList<Measure> measurelist = new ArrayList<Measure>();
			//obtain measure list from file 
			//System.out.println(System.getProperty("user.dir"));
			MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
			try {
				measurelist = meas.getMeasures();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("In the handler, measurelist is "+ measurelist.toString());
			
			//System.out.println("The loading of measures file is done!");
			
			HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();
			//obtain mapping from file (protection to measure)
			MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");
			
			mapping = map.getMapping();
			//if (mapping.isEmpty()) System.out.println("mapping.isEmpty() is ture");
			//System.out.println("The loading of mapping files is done! ");
			
			boolean result = true;// this first one is better than second one.
				
			ctc.createJob(ptmodel, pc1, pc2, measurelist, mapping);
			//result = ctc.doJob();
			//TODO
			AssessmentResult ar = new AssessmentResult();
			ar.setCompareResult(result);
		return ar;
	}

	@Override
	public boolean runCompareAssessment() {
		// TODO Auto-generated method stub
		////call the related functions: assessment or simulation 
		CTCDirect ctc = new CTCDirect();
		TransitionInformationFileIn tifi = new TransitionInformationFileIn();
	    ArrayList<AttackPath> aps = tifi.getTransitionInformation( attackModel.getAttackPaths() );
	    
	    
	   //if ( protectionConfigurations.size()<2 ) return null;
		ProtectionSolution pc1 = protectionSolution;
		ProtectionSolution pc2 = protectionSolutions.get(1);
		
		ArrayList<Measure> measurelist = new ArrayList<Measure>();
		//obtain measure list from file 
		//System.out.println(System.getProperty("user.dir"));
		MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
		try {
			measurelist = meas.getMeasures();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("In the handler, measurelist is "+ measurelist.toString());
		
		//System.out.println("The loading of measures file is done!");
		
		HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();
		//obtain mapping from file (protection to measure)
		MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");
		
		mapping = map.getMapping();
		//if (mapping.isEmpty()) System.out.println("mapping.isEmpty() is ture");
		//System.out.println("The loading of mapping files is done! ");
		
		boolean result = true;// this first one is better than second one.
			
		ctc.createJob(aps, pc1, pc2, measurelist, mapping);
		result = ctc.doJob();
		return result;
	}

	@Override
	public boolean setProtectionSolutions(ProtectionSolution pc1,
			ProtectionSolution pc2) {
		// TODO Auto-generated method stub
		protectionSolution = pc1;
		protectionSolutions.add(pc1);
		protectionSolutions.add(pc2);
		return false;
	}

	@Override
	public ProtectionSolution runCompare() {
				// TODO Auto-generated method stub
		////call the related functions: assessment or simulation 
		CTCDirect ctc = new CTCDirect();
		TransitionInformationFileIn tifi = new TransitionInformationFileIn();
	    ArrayList<AttackPath> aps = tifi.getTransitionInformation( attackModel.getAttackPaths() );
	    
	    
	   //if ( protectionConfigurations.size()<2 ) return null;
		ProtectionSolution pc1 = protectionSolution;
		ProtectionSolution pc2 = protectionSolutions.get(1);
		
		ArrayList<Measure> measurelist = new ArrayList<Measure>();
		//obtain measure list from file 
		//System.out.println(System.getProperty("user.dir"));
		MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
		try {
			measurelist = meas.getMeasures();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("In the handler, measurelist is "+ measurelist.toString());
		
		//System.out.println("The loading of measures file is done!");
		
		HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();
		//obtain mapping from file (protection to measure)
		MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");
		
		mapping = map.getMapping();
		//if (mapping.isEmpty()) System.out.println("mapping.isEmpty() is ture");
		//System.out.println("The loading of mapping files is done! ");
		
		boolean result = true;// this first one is better than second one.
			
		ctc.createJob(aps, pc1, pc2, measurelist, mapping);
		result = ctc.doJob();
		if(result) return pc1;
		else return pc2;		
	}

}
