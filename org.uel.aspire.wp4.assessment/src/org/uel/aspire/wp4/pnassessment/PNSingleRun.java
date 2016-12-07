package org.uel.aspire.wp4.pnassessment;

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

public class PNSingleRun {
	
	private PetriNet petrinet;
	private ArrayList<Place> places;//**
	private ArrayList<Place> startplace;
	private ArrayList<Place> endplace;
	private ArrayList<Transition> transitions;
	
	private HashMap<String, StateInPlace> states;
	private HashMap<String, EffortInTrans> efforts;
	private HashMap<String, EffortInTrans> efforts1;
	private HashMap<String, EffortInTrans> efforts2;
	
	private AttackerCap attacker;
	
	
	Place curplace;
	AttackerCap curattacker;
	ArrayList<Place> did = new ArrayList<Place>();
	ArrayList<Place> didnot = new ArrayList<Place>();
	
	public PNSingleRun(PetriNet petrinetin, HashMap<String, EffortInTrans> effortsin1, HashMap<String, EffortInTrans> effortsin2, AttackerCap attackerin) 
	{
		petrinet = petrinetin;
		attacker = attackerin;
		efforts1  = effortsin1;
		efforts2  = effortsin2;
		if(efforts1.isEmpty()) System.out.println("ERROR: efforts1 is empty!");
		if(efforts2.isEmpty()) System.out.println("ERROR: efforts2 is empty!");
		efforts = efforts1;
		
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
	
	public String run_simulation()
	{
		String result = "";
		////////
		if (startplace.isEmpty() )
		{	
			System.out.println("The Petri net based attack model has NO places as the starting place! ");
			return "The Petri net based attack model has NO places as the starting place! ";
		}	
		if (startplace.size() > 1)
		{	
			System.out.println("The Petri net based attack model has more than ONE places as the starting place! ");
			return "The Petri net based attack model has more than ONE places as the starting place! ";
		}
		if (endplace.isEmpty() )
		{
			System.out.println("The Petri net based attack model has NO places as the ending place! ");
			return "The Petri net based attack model has NO places as the ending place! ";
		}	
		if (endplace.size() > 1 )
		{
			System.out.println("The Petri net based attack model has more than ONE places as the ending place! ");
			return "The Petri net based attack model has more than ONE places as the ending place! ";
		}	
	
		EffortInTrans assess1 = new EffortInTrans();
		for(int i = 0;i< efforts1.get(transitions.get(i).getId()).getefforts().size();i++)
		{
			assess1.getefforts().add(0.0);
		}
		for(int i = 0;i< transitions.size();i++)
		{
			//ArrayList<Double> temp;
			for(int j = 0; j < efforts1.get(transitions.get(i).getId()).getefforts().size(); j++)
			{
				assess1 = EffortInTrans.addtwo(assess1, efforts1.get(transitions.get(i).getId()));				
			}
		}
		EffortInTrans assess2 = new EffortInTrans();
		for(int i = 0;i< efforts2.get(transitions.get(i).getId()).getefforts().size();i++)
		{
			assess2.getefforts().add(0.0);
		}
		for(int i = 0;i< transitions.size();i++)
		{
			//ArrayList<Double> temp;
			for(int j = 0; j < efforts2.get(transitions.get(i).getId()).getefforts().size(); j++)
			{
				assess2 = EffortInTrans.addtwo(assess2, efforts2.get(transitions.get(i).getId()));				
			}
		}
		
		if (EffortInTrans.compare(assess1, assess2) )
		{
			result = "Tool chain 1 is better than tool chain 2! ";
		}
		else			
		{
			result = "Tool chain 2 is better than tool chain 1! ";
		};
		return result;
		
		/* the rest of this method is from previous simulation 
		curplace = startplace.get(0);		
		curattacker = attacker;		

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
				//System.out.println("We reach the end place! ");
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
				//System.out.println("*******");
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
		*/
	}
	
	protected int choose_transition(Place curplace, AttackerCap curattacker)
	{
		int indexresult = -1;//the index of next transition
		if (curplace.getOut().size() < 1) return -1;
		int indexsize = curplace.getOut().size();
		if (indexsize < 2) return  0;
		double measure = -1;		
		//System.out.println("In the choose_transition function, it starts ");
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
		//System.out.println("In the choose_transition function, the mid is : " + indexresult);
		for(int i=0;i<indexsize;i++)
		{
			if (!pass_judge(curplace, i, curattacker) ) continue;		
			
			if (measure < measure_nexttransition(curplace, i) )
			{
				measure = measure_nexttransition(curplace, i);
				indexresult = i;
			}		
			
		}		
		return indexresult;		
	}
	
	protected boolean pass_judge(Place curplace, int transition_index, AttackerCap curattacker)
	{
		boolean result = true;
		EffortInTrans condition;
		
		condition = efforts.get(transitions.indexOf(curplace.getOut().get(transition_index).getTarget()));
		condition = efforts.get(curplace.getOut().get(transition_index).getTarget().getId());
		
		//System.out.println("The effort in this transition is : " + condition.effort);
		
		// this is the place to compare attack effort in transitions with attackers current capability
		
		if((condition.getefforts() != null) && !condition.getefforts().isEmpty() )// &&&&&&&&&&&&&&&&&&to judge the two vectors to decide
		{
			for(int i = 0; i < condition.getefforts().size(); i++)
			{
				if (condition.getefforts().get(i) > curattacker.getattacker_effort().get(i)) //TODO
				{
					result = false;
					return result;
				}
			}
		}
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
		
		//System.out.println("In pass_process function, curattacker.attacker_effort " + curattacker.attacker_effort);
		
		if ( (curattacker.getattacker_effort() != null) && !curattacker.getattacker_effort().isEmpty() ) //$$$$$$$$$$$$$$$$$$do this transition in vector level
		{
			//System.out.println("curattacker.attacker_effort.isEmpty() is : " + curattacker.attacker_effort.isEmpty());
			for (int i = 0; i < curattacker.getattacker_effort().size(); i++)
			{
				curattacker.getattacker_effort().set(i, curattacker.getattacker_effort().get(i) - condition.getefforts().get(i)) ;
			}
			return nextplace;
		}
		
		//System.out.println("In pass_process function, curattacker.att_test is :" + curattacker.att_test);
		
		curattacker.setatt_test( curattacker.getatt_test() - condition.geteffort() );		//$$$$$$$$$$$$$$$$$$$$$To pass one transition, this attacker has to pay something. 
		return nextplace;
	}
}
