package org.uel.aspire.wp4.assessment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.pnml.tools.epnk.helpers.FlatAccess;

import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.EffortInTrans;
import org.uel.aspire.wp4.data.StateInPlace;

public class SingleSimulation {
	
	private PetriNet petrinet;
	private ArrayList<Place> places;//**
	private ArrayList<Place> startplace;
	private ArrayList<Place> endplace;
	private ArrayList<Transition> transitions;
	
	private HashMap<String, StateInPlace> states;
	private HashMap<String, EffortInTrans> efforts;
	
	private ArrayList<AttackerCap> attackers ;
	
	
	Place curplace;
	AttackerCap curattacker;
	ArrayList<Place> did = new ArrayList<Place>();
	ArrayList<Place> didnot = new ArrayList<Place>();
	
	public SingleSimulation(PetriNet petrinetin, HashMap<String, EffortInTrans> effortsin, ArrayList<AttackerCap> attackerin) 
	{
		petrinet = petrinetin;
		attackers = new ArrayList<AttackerCap>();
		attackers = attackerin;
		curattacker = new AttackerCap();
		efforts  = effortsin;
		if(efforts.isEmpty()) System.out.println("ERROR: efforts is empty!");
		
		places = new ArrayList<Place>();
		startplace = new ArrayList<Place>();
		endplace = new ArrayList<Place>();
		transitions = new ArrayList<Transition>();
		states = new HashMap<String, StateInPlace>();
				
		FlatAccess flat = new FlatAccess(petrinet);
		
		//System.out.println( petrinet.getPage().get(0).getPageLabelProxy().get(0).getText())  ;
		
		///here we give places and transitions the model information  
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
		if (places.isEmpty()) System.out.println("The places of attack model is empty!!!");
		for (Transition transition : flat.getTransitions() ) 
		{
			transitions.add(transition);;
		}
		if (transitions.isEmpty()) System.out.println("The transitions of attack model is empty!!!");		
	}
	
	public boolean run_simulation()
	{
		boolean result = false;
		////////
		if (startplace.isEmpty() )
		{	
			System.out.println("The Petri net based attack model has NO places as the starting place! ");
			return false;
		}			
		if (endplace.isEmpty() )
		{
			System.out.println("The Petri net based attack model has NO places as the ending place! ");
			return false;
		}		
	
		curplace = startplace.get(0);
		Random ra = new Random();
		int tempindex = 0;
		//System.out.print("singlesimulation"); System.out.println(attackers);
		if (attackers==null) System.out.println("attackers==null in singlesimulation" );
		//System.out.println("attackers.size() " + attackers.size());
		tempindex = ra.nextInt(attackers.size());
		if (attackers.isEmpty()) System.out.println("attackers is empty in singlesimulation");
		//System.out.println("tempindex " + tempindex);
		
		
		curattacker = attackers.get(tempindex);	
		//int sizein = states.values().;
		int sizein = 5;
		curattacker.getattacker_effort().clear();
		curattacker.onetolist(sizein);
		//System.out.println("sizein is "+sizein);
		//System.out.println("curattacker.getattacker_effort().size() is "+curattacker.getattacker_effort().size());
		//System.out.println("curattacker.getatt_test() " + curattacker.getatt_test());

		did.clear();
		didnot.clear();
		didnot = places;
		didnot.remove(curplace);
	    did.add(curplace);	    

		int tag = 1;
		while(true)
		{				
			//System.out.println("The Assessment process is runing in the main loop! ");
			//System.out.println("Round is : " + tag);
			tag++;
			
			if (endplace.contains(curplace) )
			{
				result = true;
		//		System.out.println("We reach the end place! ");
				break;
			}
			if (didnot.isEmpty() )
			{
				System.out.println("The didnot is empty to end the main loop! ");
				result = false;
				break;
			}					
			
			//start to search the next place after curplace
			//System.out.println("+++++++++");
			int next_trans = choose_transition(curplace, curattacker);	
			
			
			if (next_trans == -1 )
			{	//System.out.println("+++++++++");
				try{
					curplace= did.get(did.indexOf(curplace)-1);
				}
				catch(Exception e)
				{
					result = false;
					return result;
					//break;
				}				
				continue;
			}
			else
			{
				Place tempnext;
			//	System.out.println("*******");
				tempnext = pass_process(curplace, next_trans);
				
				//System.out.println(tempnext);
				
				if(tempnext == null)
				{
					try{
						curplace= did.get( did.indexOf(curplace)-1 );
					}
					catch(Exception e)
					{
						result = false;
						return false;
					}				
					continue;
				}
				else
				{
					curplace = tempnext;
					
					didnot.remove(curplace);
					did.add(curplace);
				}
			}		
		}
		return result;
	}
	
	protected int choose_transition(Place curplace, AttackerCap curattacker)
	{
		int indexresult = -1;//the index of next transition
		if (curplace.getOut().size() < 1) return -1;
		int indexsize = curplace.getOut().size();
		if (indexsize < 2) return  0;
		double measure = -1;		
		//System.out.println("In the choose_transition function, it starts ");
		//System.out.println("In the choose_transition function, the indexsize is : " + indexsize);
		////if the all next places are in the did set, feedback to a -1 to roll back curplace
		boolean temp = false;
		for(int i=0;i<indexsize;i++)
		{
			for(int j=0;j<curplace.getOut().get(i).getTarget().getOut().size();j++)
			{
				if (!did.contains(curplace.getOut().get(i).getTarget().getOut().get(j).getTarget())) 
				{
					temp = true;
					break;
				}
			}
		}
		if (!temp) return -1;
		//////////////////////////////////
		//System.out.println("In the choose_transition function, the mid indexresult is : " + indexresult);	
		for(int i=0;i<indexsize;i++)
		{
			if (!pass_judge(curplace, i, curattacker) ) continue;	
			
		//	System.out.println("+++++++++++");
			
			if (measure < measure_nexttransition(curplace, i) )
			{
				measure = measure_nexttransition(curplace, i);
				indexresult = i;
			}		
			
		}	
	//	System.out.println("In the choose_transition function, the indexresult is : " + indexresult);	
		return indexresult;		
	}
	
	protected boolean pass_judge(Place curplace, int transition_index, AttackerCap curattacker)
	{
		boolean result = true;
		EffortInTrans condition;
		
		condition = efforts.get(transitions.indexOf(curplace.getOut().get(transition_index).getTarget()));
		condition = efforts.get(curplace.getOut().get(transition_index).getTarget().getId());
		
		//System.out.println("in pass_judge(), The effort in this transition is : " + condition.getefforts());
		
		// this is the place to compare attack effort in transitions with attackers current capability
		
		if((condition.getefforts() != null) && !condition.getefforts().isEmpty() )// &&&&&&&&&&&&&&&&&&to judge the two vectors to decide
		{
			//System.out.println("in pass_judge(),, condition.getefforts().size() is : " + condition.getefforts().size());
			for(int i = 0; i < condition.getefforts().size(); i++)
			{
			//	System.out.println("in pass_judge(), condition.getefforts().get(i) is : " + condition.getefforts().get(i));
			//	System.out.println("in pass_judge(), curattacker.getattacker_effort().get(i) is : " + curattacker.getattacker_effort().get(i));
				if (condition.getefforts().get(i) > curattacker.getattacker_effort().get(i)) //TODO
				{
			//		System.out.println("**************" );
					result = false;
					return result;
				}
			}
		}
		//System.out.println("++++++++++++++++++" );
		
		if (condition.geteffort() > curattacker.getatt_test())//$$$$$$$$$$$$$$$$$$$$$does this attacker is strong enough to pass this transition?
			result = false;
		return result;		
	}
	
	protected double measure_nexttransition(Place curplace, int transition_index ) 
	{
		//for multiple potential next path, we use random to get the next path
		Random rn = new Random();
		return rn.nextDouble();
	}
	
	protected Place pass_process(Place curplace, int transition_index)
	{
		Place nextplace ;
		nextplace = curplace; 		
		Transition next = (Transition) nextplace.getOut().get(transition_index).getTarget();		
			
		//if the all next places are in the did set, feedback to a null to roll back curplace
		boolean temp = false;
		for(int i=0;i<next.getOut().size();i++)
		{			
				if (!did.contains(next.getOut().get(i).getTarget())) 
				{
					temp = true;
					break;
				}			
		}
		if (!temp) return null;
		/////////////////////////////////////////
		
		for (int i = 0; i < next.getOut().size(); i++)
		{
			nextplace = (Place) next.getOut().get(i).getTarget();			
			//System.out.println("In pass_process function, the for loop, the nextplace is : " + nextplace.getId() );
			
			if(!did.contains(nextplace))
			{
				did.add(nextplace);
				didnot.remove(nextplace);
			}
		}
		//change attacker capability here 
		EffortInTrans condition;	
		//System.out.println("transitions.indexOf(next) is " + transitions.indexOf(next));
		//System.out.println("The size of efforts is : "+efforts.size());
		
		condition = efforts.get(next.getId());
		//System.out.println("condition.getefforts() : "+condition.getefforts());
		//System.out.println("In pass_process function, curattacker.attacker_effort " + curattacker.attacker_effort);
		
		if ( (curattacker.getattacker_effort() != null) && !curattacker.getattacker_effort().isEmpty() ) //$$$$$$$$$$$$$$$$$$do this transition in vector level
		{
			//System.out.println("curattacker.attacker_effort.isEmpty() is : " + curattacker.attacker_effort.isEmpty());
			for (int i = 0; i < curattacker.getattacker_effort().size(); i++)
			{
				System.out.println("curattacker.getattacker_effort().size()" + curattacker.getattacker_effort().size() + "condition.getefforts().size()"+condition.getefforts().size());
				curattacker.getattacker_effort().set(i, curattacker.getattacker_effort().get(i) 
						- condition.getefforts().get(i)) ;
			}
			return nextplace;
		}
		
		//System.out.println("In pass_process function, curattacker.att_test is :" + curattacker.att_test);
		
		curattacker.setatt_test( curattacker.getatt_test() - condition.geteffort() );		//$$$$$$$$$$$$$$$$$$$$$To pass one transition, this attacker has to pay something. 
		return nextplace;
	}
}
