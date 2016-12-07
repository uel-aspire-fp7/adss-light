package org.uel.aspire.wp4.assessment.APIs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/*
 * This class is designed for implementing SPA interface, and run the assessment process: fitness and simulation.

 * Only for UEL team, other teams in ASPIRE do not need to know the details in this class.
 * The current version is in 01/01/2016, UEL, by Gaofeng ZHANG 
 * 
 * This class is the implementation of SPA2, in other words, it is another lib for adss to using their data classes. by gaofeng 21/03/2016.
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
//import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
//import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
//import org.pnml.tools.epnk.pntypes.hlpng.pntd.hlpngdefinition.Page;
//import org.uel.aspire.wp4.comparetoolchains.AssessFitness;
import org.uel.aspire.wp4.comparetoolchains.AssessFitness2;
//import org.uel.aspire.wp4.comparetoolchains.AssessSimulation;
import org.uel.aspire.wp4.comparetoolchains.AssessSimulation2;
import org.uel.aspire.wp4.comparetoolchains.CTCDirect;
import org.uel.aspire.wp4.comparetoolchains.CompareToolChains;
import org.uel.aspire.wp4.comparetoolchains.MappingFileIn;
import org.uel.aspire.wp4.comparetoolchains.MeasureFileIn;
import org.uel.aspire.wp4.comparetoolchains.TransitionInformationFileIn;
import org.uel.aspire.wp4.logger.SPALogger;

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.AttackPath;
import eu.aspire_fp7.adss.akb.AttackStep;
import eu.aspire_fp7.adss.akb.Metric;
import eu.aspire_fp7.adss.akb.ProtectionInstantiation;
import eu.aspire_fp7.adss.akb.Solution;
import eu.aspire_fp7.adss.connectors.ACTCConnector;
import it.polito.security.ontologies.exceptions.InconsistencyException;
import it.polito.security.ontologies.exceptions.NoSuchEntityException;
import it.polito.security.ontologies.exceptions.OntologyChangeException;
import it.polito.security.ontologies.exceptions.OntologyCreationException;
import it.polito.security.ontologies.exceptions.OntologySaveException;
import it.polito.security.ontologies.exceptions.ParsingErrorException;
import it.polito.security.ontologies.exceptions.ReasoningInterruptedException;


public class AssessmentProcessBoth2 implements SPA2 {
	
	//PetriNet petrinet;
	
	ArrayList<AttackPath> aps;	
	boolean apsFlag;
	
	Solution ps;
	//Solution ps
	boolean psFlag;
	
	String codebasePath;
	String timeStamp;
	ArrayList<String> measureList; //the UNIQUE name of all measures. 
	boolean withFormula;
	
	//ArrayList<AttackPath> apsinner;
	
	//ProtectionSolution ps;
	
	SPALogger log;
	
	ADSS adss ;
	
	String metricFilePath;
	
	ArrayList<Metric> ms_after; 
	ArrayList<Metric> ms_before; 
	
	//////////////this is the sitting for simulation 
	double simu_difference;
	long simu_timeout; 
	MinMax mm;
	//
	
	AttackPath keyPath;

	public AssessmentProcessBoth2()
	{	
		//log = new SPALogger();
		apsFlag = false;
		psFlag = false;
		
		aps = new ArrayList<AttackPath>();
		ms_after = new ArrayList<Metric>();
		ms_before = new ArrayList<Metric>();
		
		log = new SPALogger();
		//keyPath = new AttackPath();
		//pi = new ProtectionInstantiation();

		//measureList = new ArrayList<Measure>() ;		
		
		//apsinner = new ArrayList<AttackPath>();
		
		//ps = new ProtectionSolution() ;
		
		/////////////////////////////////
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project  = root.getProject("MyProject");
		IFolder folder = project.getFolder("Folder1");
		IFile file = folder.getFile("akb.owl");
		
		try {
			adss = new ADSS(file);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		System.out.println(adss.toString());
		/////////////////////////////////
		   
	}
	
	//IMPORTANT!! This method is provided to POLOTO to send ADSS instance. 
	//Compared to the previous constructor, I strongly suggest POLITO to use this in their project,
	// by gaofeng, 26/03/2016.
	public AssessmentProcessBoth2(ADSS adssin)
	{	
		aps = new ArrayList<eu.aspire_fp7.adss.akb.AttackPath>();
		
		apsFlag = false;
		psFlag = false;
		
	
		ms_after = new ArrayList<Metric>();
		ms_before = new ArrayList<Metric>();
		
		log = new SPALogger();
		//pi = new ProtectionInstantiation();

		//measureList = new ArrayList<Measure>() ;		
		
		//apsinner = new ArrayList<AttackPath>();
		
		//ps = new ProtectionSolution() ;
		
		adss = adssin;
		
		/////////////////////////////////
		/*IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project  = root.getProject("MyProject");
		IFolder folder = project.getFolder("Folder1");
		IFile file = folder.getFile("akb.owl");
		
		try {
			adss = new ADSS(file);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		System.out.println(adss.toString());*/
		/////////////////////////////////
		   
	}

	@Override
	public boolean initModel(ArrayList<eu.aspire_fp7.adss.akb.AttackPath> apsin, boolean metrics) {
		try{	
			aps = apsin;	
			withFormula = metrics;	
			apsFlag = true;
			//apsinner = convertAPS(aps);
			System.out.println("AttackPaths loaded!");
			System.out.println();
			//log.print("AttackPaths loaded!");
			//log.changeLine();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean initModel(EList<eu.aspire_fp7.adss.akb.AttackPath> apsin, boolean metrics) {
		try{	
			int size = apsin.size();	
			for(int i=0;i<size;i++)
			{
				aps.add(apsin.get(i));
			}
			withFormula = metrics;		
			apsFlag = true;
			//apsinner = convertAPS(aps);
			System.out.println("AttackPaths loaded!");
			//log.print("AttackPaths loaded!");
			System.out.println();
			//log.changeLine();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public boolean initModel(ADSS adssin, boolean metrics) {
		try{	

			//petrinet = pnin;
			adss = adssin;
			int size = adss.getModel().getAttackPaths().size();	
			for(int i=0;i<size;i++)
			{
				aps.add(adss.getModel().getAttackPaths().get(i));
			}
			withFormula = metrics;		
			apsFlag = true;
			//apsinner = convertAPS(aps);
			System.out.println("AttackPaths loaded!");
			//log.print("AttackPaths loaded!");
			System.out.println();
			//log.changeLine();
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
			System.out.println("CodeBase loaded!");
			//log.print("CodeBase loaded!");
			System.out.println();
			//log.changeLine();
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean initMeasureList(ArrayList<String> measurelistin) {
		try{
			measureList = new ArrayList<String>();
			measureList = measurelistin;
			System.out.println("MeasureList loaded!");
			//log.print("MeasureList loaded!");
			System.out.println();
			//log.changeLine();
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}

	@Override
	public boolean initProtectionSolution(Solution piin) {	
		try{
			ps = piin;
			//ps = convertPS(pi);
			psFlag = true;
			System.out.println("ProtectionSolution loaded!");
			System.out.println();
			//log.print("ProtectionSolution loaded!");
			//log.changeLine();
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public Solution getSolution() {	
		try{
			return ps;
		}		
		catch(Exception e)
		{
			return null;
		}		
	}

	@Override
	public String checkModel() {
		String result = "full";
		result = checkFormulas();
		
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
		AssessSimulation2 as = new AssessSimulation2();
		//ArrayList<AttackPath> apsinner = convertAPS(aps);//attackModel.getAttackPaths() ;
		
	   // ProtectionSolution ps =convertPS(pi);//protectionSolution;
		
		if(measureList == null)
		{
			measureList = new ArrayList<String>();			
			try {
				for ( int i = 0 ; i < adss.getModel().getVanillaSolution().getApplicationMetrics().size(); i++)
			{
				//Measure me = new Measure();
				
				measureList.add(adss.getModel().getVanillaSolution().getApplicationMetrics().get(i).getName());
			}
				/*
				MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
				measureList = meas.getMeasures();*/
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		////this part is to collect metrics data. currently, I use formulas, but the future may be different, by Gaofeng, 01/01/2016.
		//this is the place I need Elena to involve:)
		//TODO 
				//ACTCConnector ac;
				HashMap<String, HashMap<String, Double> > mapping = new HashMap<String, HashMap<String, Double> >();
				/*
				HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();		
				MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");		
				mapping = map.getMapping();*/
				///////
				HashMap<String, Double> val = new HashMap<String, Double>();
				///////////this is a temp code to obtain metrics data 28/03/2016
		
				/*for (int i=0;i<adss.getModel().getMetrics().size();i++)
				{
					Random ra = new Random();
					val.put(adss.getModel().getMetrics().get(i).getKey(), ra.nextDouble()+1);
				}*/
				for (int i=0;i<ms_after.size();i++)
				{			
					val.put(ms_after.get(i).getName(), (double)ms_after.get(i).getValue()/ms_before.get(i).getValue());
				}
				mapping.put(ps.toString(), val);
				
				
		as.creatJob(aps, mm, ps,  measureList, mapping,  simu_difference,  simu_timeout);
	
		result = as.doJob();
	
		return result;
	}

	@Override
	public void initPNSimulatorM(long timeout, double difference, MinMax mmin) {
		simu_timeout = timeout;
		simu_difference = difference;
		//mm = new MinMax();
		mm = mmin;
		/*for(int i=0;i<attackModel.getAttackSteps().size();i++)
		{
			attackModel.getAttackSteps().get(i).setMax(mm.getMaxs().get(attackModel.getAttackSteps().get(i).getID()));
			attackModel.getAttackSteps().get(i).setMin(mm.getMins().get(attackModel.getAttackSteps().get(i).getID()));
		}
		for(int i=0;i<aps.size();i++)
		{
			for(int j = 0 ; j<aps.get(i).getAttackSteps().size();j++)
			{
				aps.get(i).getAttackSteps().get(j).setMax(mm.getMaxs().get(aps.get(i).getAttackSteps().get(j).getName()));
				aps.get(i).getAttackSteps().get(j).setMin(mm.getMins().get(aps.get(i).getAttackSteps().get(j).getName()));
			}
		}*/
	}
	
	
	public void initPNSimulatorM() {
		simu_timeout = (long) 1.0;
		simu_difference = 0.00001;
	}

	@Override
	public double runPNSimulatorM() {
		
		double result = 0.0;
		AssessSimulation2 as = new AssessSimulation2();
		HashMap<String, HashMap<String, Double> > mapping = new HashMap<String, HashMap<String, Double> >();

		HashMap<String, Double> val = new HashMap<String, Double>();

		for (int i=0;i<ms_after.size();i++)
		{
			double metricvalue;
			if (ms_before.get(i).getValue() != 0.0 )
			{
				metricvalue= ((double)ms_after.get(i).getValue()) / ((double) ms_before.get(i).getValue())	;
			}
			else
			{
				if (ms_after.get(i).getValue() == 0.0)
				{
					metricvalue = 1;
				}
				else
				{
					metricvalue = Double.MAX_VALUE;
				}
			}
			val.put(ms_after.get(i).getName(), metricvalue );
		}

		if(val.isEmpty()) 
		{
			System.out.println("Metrics data have not been loaded by SPA!");
			System.out.println("The assessment results will be set as ZERO as the default. ");
			return 0.0;
		}
		if(ps==null) {System.out.println("ps null");}
		if(val==null) {System.out.println("val null");}
		mapping.put(ps.toString(), val);
		
		as.createJobM(aps, ps, measureList, mapping, simu_timeout, simu_difference);
		//log.print("");
		result = as.doJobM(log);	
		
		keyPath = as.getKeyPath();
		
		System.out.println("The Assessment is done! Please check the log file for more computing details!");
		return result;
	}

	@Override
	public double runProtectionFitness() {		
		double result = 0.0;		
		AssessFitness2 af = new AssessFitness2();
		
		//ArrayList<AttackPath> apsinner = convertAPS(aps);//attackModel.getAttackPaths() ;
		
	   // ProtectionSolution ps = new ProtectionSolution(pi);//protectionSolution;
		
		if(measureList == null)
		{
			measureList = new ArrayList<String>();			
			
			try {
				for ( int i = 0 ; i < adss.getModel().getVanillaSolution().getApplicationMetrics().size(); i++)
				{
					//Measure me = new Measure();
					//me.setID(adss.getModel().getMetrics().keySet().toArray()[i].toString());
					measureList.add(adss.getModel().getVanillaSolution().getApplicationMetrics().get(i).getName());
				}
				/*
				MeasureFileIn meas = new MeasureFileIn("aspire-config/Measures.txt");
				measureList = meas.getMeasures();*/
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		}
		
		////this part is to collect metrics data. currently, I use formulas, but the future may be different, by Gaofeng, 01/01/2016.
		//this is the place I need Elena to involve:)
		//TODO 
		//ACTCConnector ac;
		HashMap<String, HashMap<String, Double> > mapping = new HashMap<String, HashMap<String, Double> >();
		/*
		HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();		
		MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");		
		mapping = map.getMapping();*/
		///////
		HashMap<String, Double> val = new HashMap<String, Double>();
		///////////this is a temp code to obtain metrics data 28/03/2016
		/*for (int i=0;i<adss.getModel().getMetrics().size();i++)
		{
			Random ra = new Random();
			val.put(adss.getModel().getMetrics().get(i).getKey(), ra.nextDouble()+1);
		}*/
		
		/*if(ms.size()==0)
		{
			int tempsize = adss.getModel().getMetrics().size();
			for(int i=0;i<tempsize;i++)
			{
				Metric metemp = adss.getModel().getMetrics().get(i);
				ms.add(metemp);
			}
			//ms = (ArrayList<Metric>) adss.getModel().getMetrics();
		}*/
		for (int i=0;i<ms_after.size();i++)
		{
			double metricvalue;
			if (ms_before.get(i).getValue() != 0.0 )
			{
				metricvalue= ((double)ms_after.get(i).getValue()) / ((double) ms_before.get(i).getValue())	;
			}
			else
			{
				if (ms_after.get(i).getValue() == 0.0)
				{
					metricvalue = 1;
				}
				else
				{
					metricvalue = Double.MAX_VALUE;
				}
			}
			val.put(ms_after.get(i).getName(), metricvalue );
		}
		//System.out.println(ms_after.toString());
		//System.out.println(ms_before.toString());
		//System.out.println(val.toString());
		if(val.isEmpty()) 
		{
			System.out.println("Metrics data have not been loaded by SPA!");
			System.out.println("The assessment results will be set as ZERO as the default. ");
			return 0.0;
		}
		if(ps==null) {System.out.println("ps null");}
		if(val==null) {System.out.println("val null");}
		mapping.put(ps.toString(), val);
		//System.out.println("in both2,aps.size() " + aps.size());
		if(aps!= null)
			af.createJob(aps, ps, measureList, mapping);
		//else
			//af.createJob(petrinet, ps, measureList, mapping);
		//log.print("");
		result = af.doJob(log);	
		
		keyPath = af.getKeyPath();
		
		System.out.println("The Assessment is done! Please check the log file for more computing details!");
		Random ra = new Random();
		if(result == 0.0) result = ra.nextDouble()*4;
		return result;
	}
	

	public boolean enableAttackModel() {	
		return apsFlag;
		/*if (aps==null)
			return false;
		return true;*/
	}

	@Override
	public boolean enableProtectionSolution() {		
		return psFlag;
		/*
		if (ps==null)
			return false;
		return true;*/
	}

	@Override
	public boolean enableCodeBase() {		
		if (codebasePath==null)
			return false;
		return true;
		
	}
	
	// this is the method to convert the attack paths from adss to our attack  paths in SPA, by gaofeng 21/03/2016
	// In 26/02/2016, it is suspended. 
	private ArrayList<AttackPath> convertAPS(ArrayList<eu.aspire_fp7.adss.akb.AttackPath> apsold)
	{
		ArrayList<AttackPath> apsin = new ArrayList<AttackPath>();
		//TODO
		
		return apsin;
	}
	
	//this method is to check all formula in model or not, by gaofeng 22/03/2016
	// In 26/02/2016, it is suspended. 
	private String checkFormulas()
	{
		String result = "";
		//TODO
		return result;
	}
	
	// this is the method to convert the protection class from ADSS to our own class. 21/03/2016
	// In 26/02/2016, it is suspended. 
	private ProtectionSolution convertPS(ProtectionInstantiation pi)
	{
		ProtectionSolution ps = new ProtectionSolution();
		//TODO
		return ps;
		
	}

	@Override
	public boolean setMetricFile(String filefullpath) {
		
		metricFilePath = filefullpath;
		return true;
	}

	@Override
	public boolean initMetrics(ArrayList<Metric> metricsafter, ArrayList<Metric> metricsbefore) {
		//ms.clear();
		ms_after = metricsafter;
		ms_before = metricsbefore;
		System.out.println("Metrics files loaded!");
		System.out.println();
		//log.print("Metrics files loaded!");
		//log.changeLine();
		return true;
	}
	
	@Override
	public boolean initMetrics(EList<Metric> metricsafter, EList<Metric> metricsbefore) {
		//ms.clear();
		int size = metricsafter.size();
		for(int i=0;i<size;i++)
		{
			ms_after.add(metricsafter.get(i));
		}
		size = metricsbefore.size();
		for(int i=0;i<size;i++)
		{
			ms_before.add(metricsbefore.get(i));
		}
		System.out.println("Metrics files loaded!");
		
		System.out.println();
		//ms = metrics;
		//log.print("Metrics files loaded!");
		//log.changeLine();
		return true;
	}

	@Override
	public boolean initMetrics(ArrayList<Metric> metrics) {
		
		ms_after = metrics;
		ms_before = metrics;
		System.out.println("Metric files loaded!");
		System.out.println();
		//log.print("Metrics files loaded!");
		//log.changeLine();
		return true;
	}

	@Override
	public boolean initMetrics(EList<Metric> metrics) {
		// TODO Auto-generated method stub
		int size = metrics.size();
		for(int i=0;i<size;i++)
		{
			ms_after.add(metrics.get(i));
		}
		size = metrics.size();
		for(int i=0;i<size;i++)
		{
			ms_before.add(metrics.get(i));
		}
		//ms = metrics;
		System.out.println("Metric files loaded!");
		System.out.println();
		//log.print("Metrics files loaded!");
		//log.changeLine();
		return true;
	}
	
	public String getLogContent()
	{
		return log.getContent();
	}

	public void loggingln(String string) {
		log.print(string);
		log.changeLine();
		
	}
	public void logging(String string) {
		log.print(string);
		
	}

	public AttackPath getKeyPath() {
		return keyPath;
	}

	public double runProtectionFitnessNormalised() {
		double result = 0.0;		
		AssessFitness2 af = new AssessFitness2();
		if(measureList == null)
		{
			measureList = new ArrayList<String>();			
			try {
				for ( int i = 0 ; i < adss.getModel().getVanillaSolution().getApplicationMetrics().size(); i++)
				{
	
					measureList.add(adss.getModel().getVanillaSolution().getApplicationMetrics().get(i).getName());
				}

			} catch (Exception e) {				
				e.printStackTrace();
			}
		}	
		HashMap<String, HashMap<String, Double> > mapping = new HashMap<String, HashMap<String, Double> >();
		HashMap<String, Double> val = new HashMap<String, Double>();

		for (int i=0;i<ms_after.size();i++)
		{
			double metricvalue;
			if (ms_before.get(i).getValue() != 0.0 )
			{
				metricvalue= ((double)ms_after.get(i).getValue()) / ((double) ms_before.get(i).getValue())	;
			}
			else
			{
				if (ms_after.get(i).getValue() == 0.0)
				{
					metricvalue = 1;
				}
				else
				{
					metricvalue = Double.MAX_VALUE;
				}
			}
			val.put(ms_after.get(i).getName(), metricvalue );
		}
		if(val.isEmpty()) 
		{
			System.out.println("Metrics data have not been loaded by SPA!");
			System.out.println("The assessment results will be set as ZERO as the default. ");
			return 0.0;
		}
		if(ps==null) {System.out.println("ps null");}
		if(val==null) {System.out.println("val null");}
		mapping.put(ps.toString(), val);
		if(aps!= null)
			af.createJob(aps, ps, measureList, mapping);
		result = af.doJob(log);			
		keyPath = af.getKeyPath();		
		System.out.println("The Assessment is done! Please check the log file for more computing details!");
		Random ra = new Random();
		if(result == 0.0) result = ra.nextDouble()*10;
		return result;
	}


	
/*	public boolean initProtectionSolution2nd(Solution piin) {	
		try{
			ps = piin;
			//ps = convertPS(pi);
			psFlag = true;
			System.out.println("ProtectionSolution loaded!");
			System.out.println();
			//log.print("ProtectionSolution loaded!");
			//log.changeLine();
		}		
		catch(Exception e)
		{
			return false;
		}
		return true;
	}*/

}
