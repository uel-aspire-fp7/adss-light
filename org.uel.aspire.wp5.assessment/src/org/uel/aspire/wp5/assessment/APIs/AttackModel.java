package org.uel.aspire.wp5.assessment.APIs;

/*
 * This class is a container class to store attack model. 
 * The current version is in 07/10/2015, UEL, by Gaofeng ZHANG
 * modified in 15/12/2015, UEL, by Gaofeng ZHANG.
 * */

import java.util.ArrayList;

import org.pnml.tools.epnk.helpers.FlatAccess;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.uel.aspire.wp4.formula.GraphtoPaths;

public class AttackModel {
	
	//this is the unique name of this attack model
	String id;
	
	//this is the attack path list, including all attack paths of this attack model
	ArrayList<AttackPath> attackPaths;
	
	//this is the attack step list, including all attack steps of this attack model
	ArrayList<AttackStep> attackSteps;
	
	//this is the petrinet model of this attack model
	PetriNet petrinet;
	
	// this is the construction method one 
	public AttackModel()
	{
		id = "";
		attackPaths = new ArrayList<AttackPath>();
		attackSteps = new ArrayList<AttackStep>();
		//petrinet = new PetriNet();
	}
	
	// this is the construction method two 
	public AttackModel(String idin)
	{
		id = idin;
		attackPaths = new ArrayList<AttackPath>();
		attackSteps = new ArrayList<AttackStep>();
		//petrinet = new PetriNet();
	}
	
	// this is the construction method three 
	public AttackModel(String idin, ArrayList<AttackPath> attackpathsin)
	{
		id = idin;
		attackPaths = new ArrayList<AttackPath>();
		attackPaths = attackpathsin;
		attackSteps = new ArrayList<AttackStep>();
		//petrinet = new PetriNet();
	}
	
	//this function is to set ID of this attack model
	public void setID(String idin)
	{
		id = idin;
	}
	
	//this function is to get ID of  this attack model
	public String getID()
	{
		return id;
	}
	
	//this function is to set PetriNet model by input
	public boolean setPetriNet(PetriNet ptin)
	{
		petrinet = ptin;
		return true;
	}
	
	//this function is to set PetriNet model by its id from some database
	public boolean setPetriNet(String id)
	{
//unfinished	
		return true;
	}
	
	//this function is to return the current PetriNet model 
	public PetriNet getPetriNet()
	{
		return petrinet;
	}
	
	//this function is to set attack paths 
	public boolean setAttackPaths(ArrayList<AttackPath> attackpathsin)
	{
		attackPaths = attackpathsin;
		return true;
	}
	
	//this function is to return the current attack paths 
	public ArrayList<AttackPath> getAttackPaths()
	{
		return attackPaths;
	}
	
	//this function is to set attack steps 
	public boolean setAttackSteps(ArrayList<AttackStep> attackstepsin)
	{
		attackSteps = attackstepsin;
		return true;
	}
	
	//this function is to return the current attack steps 
	public ArrayList<AttackStep> getAttackSteps()
	{
		return attackSteps;
	}
	
	//this functions is to check the attackmodel has transition formulas for not, like "9:SCZ*0.3"
	//"full" means the formulas are ready for fitness
	//"part" means some relations among transitions are ready for simulation
	//"null" mean that the inadequate require customers to set all mins and maxs of each transitions
	// it is for the branch of fitness or simulation, by Gaofeng 01/01/2016
	public String checkFormulas()
	{
		String result = "";
		
		for (int i=0;i<attackSteps.size();i++)
		{
			AttackStep as = new AttackStep();
			as = attackSteps.get(i);
			Formula form = as.getFormulas().get(0);
			if (form.getFormula() == null) { result = "null"; break;}
			if( form.getFormula().indexOf("<") != -1)  { result = "part"; continue;}
			if( form.getFormula().indexOf(">") != -1)  { result = "part"; continue;}
		}
		result = "full";
		return result;
	}	

	
	//this function is to transfer model from pN to attack path, added by Gaofeng 07/07/2015
	public static ArrayList<AttackPath> fromPNtoPath(PetriNet pn)
	{
		ArrayList<AttackPath> aps = new ArrayList<AttackPath>();
				
		///here we give places and transitions the model information  		
		ArrayList<Place> places = new ArrayList<Place>();//**
		ArrayList<Place> startplace = new ArrayList<Place>();
		ArrayList<Place> endplace = new ArrayList<Place>();
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		
		FlatAccess flat = new FlatAccess(pn);
		for (Place p : flat.getPlaces()) 
		{
			places.add(p); 			
			if (p.getIn().isEmpty())
			{
				startplace.add(p);			
			}
			if (p.getOut().isEmpty())
			{
				endplace.add(p);			
			}			
		}
		//if (places.isEmpty()) System.out.println("The places of attack model is empty!!!");
		ArrayList<AttackStep> ass = new ArrayList<AttackStep>();
		for (Transition transition : flat.getTransitions() ) 
		{
			transitions.add(transition);
			ass.add( AttackStep.changetoAttackStep(transition, pn.getPage().get(0).getLabelproxy() ));
	//		System.out.println(" in fromPNtoPath() , the  ass.get(ass.size()-1).getID() is " + ass.get(ass.size()-1).getID());
		}
		
		int relation[][] = new int[places.size()+transitions.size()][places.size()+transitions.size()];//m places + n transitions, directed graph
		 
		for(int i = 0; i < (places.size()+transitions.size()); i++)
		{
			for(int j = 0; j < (places.size()+transitions.size()); j++)
			{
				relation[i][j] = 0;
			}
		}
		for(int i = 0; i < places.size(); i++)
		{
			for (int j = 0 ; j < places.get(i).getOut().size(); j++)
			{
				String startid = places.get(i).getId();
				String endid =	places.get(i).getOut().get(j).getTarget().getId();
				startid = startid.substring(1);
				endid = endid.substring(1);
				
				int startindex = (int) Double.parseDouble(startid);
				int endindex = (int) Double.parseDouble(endid);
				//System.out.println(startindex +" + "+ endindex +"  + "+ places.size() + "  + "+transitions.size());
				
				//relation[startindex - 1][ endindex + places.size() - 1] = 1;
				
				relation[i][ transitions.indexOf( (Transition)places.get(i).getOut().get(j).getTarget() ) + places.size() ] = 1;
			}
		}			
		for(int i=0; i<transitions.size(); i++)
		{
			for (int j = 0 ; j < transitions.get(i).getOut().size(); j++)
			{
				String startid = transitions.get(i).getId();
				String endid =	transitions.get(i).getOut().get(j).getTarget().getId();
				startid = startid.substring(1);
				endid = endid.substring(1);
				int startindex = (int) Double.parseDouble(startid);
				int endindex = (int) Double.parseDouble(endid);
				
				//relation[ startindex + places.size() - 1][ endindex -1 ] = 1;
				relation[ places.size() + i][ places.indexOf( (Place)transitions.get(i).getOut().get(j).getTarget() )  ] = 1;
			}
		}
		
		ArrayList<String> relstring = new ArrayList<String>();
		for(int i=0; i < (places.size() + transitions.size()); i++)
		{
			String oneline = "";
			for(int j=0; j < (places.size() + transitions.size()); j++)
			{			
				oneline = oneline + "+" + relation[i][j];			
			}
			if (oneline.substring(0,0) == "+")
			{
				oneline = oneline.substring(1);
			}
			relstring.add(oneline);
		}
		
		GraphtoPaths gtp = new GraphtoPaths(relstring);
		//ArrayList<String> paths = gtp.getResults( (int) Double.parseDouble(startplace.get(0).getId().substring(1)), 
		//		(int) Double.parseDouble(endplace.get(0).getId().substring(1)));
		ArrayList<String> paths = gtp.getResults( places.indexOf(startplace.get(0)), 
				places.indexOf(endplace.get(0) ) );
		for(int i=0;i<paths.size();i++)
		{
			AttackPath ap = new AttackPath();
			String onepath = paths.get(i);
			while(true)
			{
				if(onepath.indexOf("+") != -1)
				{
					int loc = onepath.indexOf("+");
					if(loc ==0) 
					{
						onepath = onepath.substring(1);
						loc = onepath.indexOf("+");
					}
					int index = 0;
					try{
					index = (int) Double.parseDouble( onepath.substring(0, loc));
					}
					catch(Exception e)
					{
						System.out.println(onepath + "  " + loc);
						System.out.println(e);
					}
					index ++;
					String id="";
					if (index > places.size() )  id = "t" + ( index - places.size() );
					//System.out.println(id);
					for(int k = 0; k < ass.size(); k++)
					{
						//System.out.println(id + "  " + ass.get(k).getID());
						if (ass.get(k).getID().equals(id) ) 
						{
							ap.addAttackStep(ass.get(k));	
						//	System.out.println("add one attackstep");
							break;
						}
					
					}
					onepath = onepath.substring(loc+1);
				}
				else
				{
					//int loc = onepath.indexOf("+");
					int index = (int) Double.parseDouble( onepath ) + 1;
					String id="";
					if (index > places.size() )  id = "t" + ( index - places.size() );
					for(int j =0; j<ass.size(); j++)
					{
						if (ass.get(j).getID().equals(id) ) 
						{
							ap.addAttackStep(ass.get(j));
							break;
						}
					}
					break;
				}
			}
			aps.add(ap);			
		}
		return aps;
	}
	
	//this function is to transfer model from attack path to pn
	public PetriNet fromPathtoPN()
	{
		PetriNet pn = null;
		//TODO for the future student project / 2015-07-06 
		return pn;
	}


}
