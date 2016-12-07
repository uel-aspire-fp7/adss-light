/**
 * 
 */
package org.uel.aspire.wp5.assessment.APIs;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.emf.common.util.EList;
import org.pnml.tools.epnk.helpers.FlatAccess;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.uel.aspire.wp4.comparetoolchains.AssessFitness2;
import org.uel.aspire.wp5.assessment.model.convertOWL;

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.ApplicationPart;
import eu.aspire_fp7.adss.akb.AttackPath;
import eu.aspire_fp7.adss.akb.Property;
import eu.aspire_fp7.adss.akb.Solution;

/**
 * @author Gaofeng
 *
 */
public class AssessmentFunction {
	
	private ADSS adss; 
	private ArrayList<AttackPath> aps;
	private ArrayList<Solution> solutions;
	private ArrayList<AttackPath> keypaths;
	private ArrayList<Double> results;
	private PetriNet petrinet;
	
	public AssessmentFunction(ADSS adssin, ApplicationPart applicationpartsin, Property property, ArrayList<Solution> solutionsin)
	{
		adss = adssin;
		solutions = solutionsin;
		aps = new ArrayList<AttackPath>();
		for(int i=0; i< adss.getModel().getAttackPathsCount();i++)
		{
			for(int j=0; j < adss.getModel().getAttackPaths().get(i).getTargets().size();j++)
			{
				if(  (adss.getModel().getAttackPaths().get(i).getTargets().get(j).getApplicationPart().hashCode() ==
						applicationpartsin.hashCode())
						&& (adss.getModel().getAttackPaths().get(i).getTargets().get(j).getProperty().hashCode() ==
						property.hashCode() ) )
				{
					aps.add(adss.getModel().getAttackPaths().get(i));
					break;
				}
			}
		}
		keypaths = new ArrayList<AttackPath>() ;
		results = new ArrayList<Double>() ;
	};
	
	public AssessmentFunction(ADSS adssin, ApplicationPart applicationpartsin, Property property, EList<Solution> solutionsin)
	{
		adss = adssin;
		solutions = new ArrayList<Solution>();
		for(int i=0;i<solutionsin.size();i++)
		{
			solutions.add(solutionsin.get(i));			
		}
		
		aps = new ArrayList<AttackPath>();
		for(int i=0; i< adss.getModel().getAttackPathsCount();i++)
		{
			for(int j=0; j < adss.getModel().getAttackPaths().get(i).getTargets().size();j++)
			{
				if( (adss.getModel().getAttackPaths().get(i).getTargets().get(j).getApplicationPart().hashCode() ==
						applicationpartsin.hashCode())
						&& (adss.getModel().getAttackPaths().get(i).getTargets().get(j).getProperty().hashCode() ==
						property.hashCode() ))
				{
					aps.add(adss.getModel().getAttackPaths().get(i));
					break;
				}
			}
		}
		keypaths = new ArrayList<AttackPath>() ;
		results = new ArrayList<Double>() ;
	};
	
	public AssessmentFunction(ADSS adssin, ApplicationPart applicationpartsin, EList<Solution> solutionsin)
	{
		adss = adssin;
		solutions = new ArrayList<Solution>();
		for(int i=0;i<solutionsin.size();i++)
		{
			solutions.add(solutionsin.get(i));			
		}
		
		aps = new ArrayList<AttackPath>();
		for(int i=0; i< adss.getModel().getAttackPathsCount();i++)
		{
			for(int j=0; j < adss.getModel().getAttackPaths().get(i).getTargets().size();j++)
			{
				if( adss.getModel().getAttackPaths().get(i).getTargets().get(j).getApplicationPart().hashCode() ==
						applicationpartsin.hashCode() )
				{
					aps.add(adss.getModel().getAttackPaths().get(i));
					break;
				}
			}
		}
		keypaths = new ArrayList<AttackPath>() ;
		results = new ArrayList<Double>() ;
	};
	
	public AssessmentFunction(ADSS adssin, ArrayList<AttackPath> attackPathsin, ArrayList<Solution> solutionsin)
	{
		adss = adssin;
		aps = attackPathsin;
		solutions = solutionsin;
		keypaths = new ArrayList<AttackPath>();
		results = new ArrayList<Double>();
	}; 
	
	public AssessmentFunction(ADSS adssin, ArrayList<AttackPath> attackPathsin, EList<Solution> solutionsin)
	{
		adss = adssin;
		aps = attackPathsin;
		
		keypaths = new ArrayList<AttackPath>();
		results = new ArrayList<Double>();
		solutions = new ArrayList<Solution>();
		for(int i=0;i<solutionsin.size();i++)
		{
			solutions.add(solutionsin.get(i));			
		}
	}; 	


	public ArrayList<Double> runAssessment()
	{
		createPetriNet();
		//if(petrinet == null) System.out.println("petrinet == null ");
		//System.out.println(new FlatAccess(petrinet).getPlaces() );
		//System.out.println(new FlatAccess(petrinet).getTransitions() );
		for(int i=0;i<solutions.size();i++)
		{
			FitnessAssessment fa = new FitnessAssessment(petrinet, adss.getModel().getVanillaSolution().getApplicationMetrics(), solutions.get(i).getApplicationMetrics());
			double result = fa.doJob();
			results.add(result);			
		}
		return results;
	};
	
	public ArrayList<AttackPath> getKeyPath()
	{
		for(int i=0;i<solutions.size();i++)
		{
			AssessFitness2 af = new AssessFitness2();
			
			HashMap<String, HashMap<String, Double> > mappingin = new HashMap<String, HashMap<String, Double> >();
			HashMap<String, Double> met = new HashMap<String, Double>();
			for(int j=0;j<adss.getModel().getVanillaSolution().getApplicationMetrics().size();j++)
			{
				double value = 0.0;

				for(int k=0;k<solutions.get(i).getApplicationMetrics().size();k++)
				{
					if(adss.getModel().getVanillaSolution().getApplicationMetrics().get(j).getName() == solutions.get(i).getApplicationMetrics().get(k).getName())
					{
						if (adss.getModel().getVanillaSolution().getApplicationMetrics().get(j).getValue() == 0.0) {value = 0.0; break;}
							value = (double)( solutions.get(i).getApplicationMetrics().get(k).getValue()/adss.getModel().getVanillaSolution().getApplicationMetrics().get(j).getValue() );
							break;
					}
	
					met.put(adss.getModel().getVanillaSolution().getApplicationMetrics().get(j).getName(), value );
				}
			}
			mappingin.put(solutions.get(i).toString(), met);	
			af.createJob(aps, solutions.get(i), mappingin);
			double temp = af.doJob();
			keypaths.add(af.getKeyPath());
		}
		return keypaths;
	};
	
	private void createPetriNet()
	{
		convertOWL pngenerate = new convertOWL(adss, aps); 
		petrinet = pngenerate.getPN();
	}
	


}
