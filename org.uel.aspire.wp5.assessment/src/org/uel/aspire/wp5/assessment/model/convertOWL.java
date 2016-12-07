/**
 * 
 */
package org.uel.aspire.wp5.assessment.model;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.pnml.tools.epnk.datatypes.pnmldatatypes.NonNegativeInteger;
import org.pnml.tools.epnk.pnmlcoremodel.AnnotationGraphics;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.ArcGraphics;
import org.pnml.tools.epnk.pnmlcoremodel.Coordinate;
import org.pnml.tools.epnk.pnmlcoremodel.Graphics;
import org.pnml.tools.epnk.pnmlcoremodel.Name;
import org.pnml.tools.epnk.pnmlcoremodel.Node;
import org.pnml.tools.epnk.pnmlcoremodel.NodeGraphics;
import org.pnml.tools.epnk.pnmlcoremodel.Object;
import org.pnml.tools.epnk.pnmlcoremodel.Page;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetType;
import org.pnml.tools.epnk.pnmlcoremodel.PlaceNode;
import org.pnml.tools.epnk.pnmlcoremodel.PnmlcoremodelFactory;
import org.pnml.tools.epnk.pnmlcoremodel.RefPlace;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.pnml.tools.epnk.pntypes.ptnet.PTMarking;
import org.pnml.tools.epnk.pntypes.ptnet.Place;
import org.pnml.tools.epnk.pntypes.ptnet.PtnetFactory;

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.AttackPath;
import eu.aspire_fp7.adss.akb.AttackStep;

/**
 * @author Gaofeng、
* by 18/06/2016, 基本完成，只有formula的copy尚缺，从知识库到pnml中
 *
 */
public class convertOWL {
	
	private PetriNet pn;
	private ADSS adss;
	private ArrayList<AttackPath> aps;
	
	public convertOWL(ADSS adssin, EList<AttackPath> apsin)
	{
		adss = adssin;
		ArrayList<AttackPath> aps = new ArrayList<AttackPath>();
		for(int i = 0 ; i <apsin.size(); i ++)
		{
			aps.add(apsin.get(i));
		}
	}
	
	public convertOWL(ADSS adssin, ArrayList<AttackPath> apsin)
	{
		adss = adssin;
		aps = apsin;
	}
	
	public convertOWL(ADSS adssin)
	{
		adss = adssin;
		aps = new ArrayList<AttackPath>();
		for(int i = 0 ; i <adss.getModel().getAttackPathsCount(); i ++)
		{
			aps.add(adss.getModel().getAttackPaths().get(i));
		}
		//System.out.println("asfaf");
	}
	
	public PetriNet getPN()
	{
		if (aps == null) return null;
		//PetriNetDoc doc =  createPetriNetDoc(1);
		//pn = doc.getNet().get(0);
		pn = createPetriNet();
		EList<Object> contents = pn.getPage().get(0).getObject();
		ArrayList<Transition> trs = new ArrayList<Transition>();
		ArrayList<Place> pls = new ArrayList<Place>();
		ArrayList<Arc> ars = new ArrayList<Arc>();		
		ArrayList<AttackStep> ass = new ArrayList<AttackStep> ();
		
	
		for(int i=0;i<adss.getModel().getAttackStepsCount();i++)
		{
			boolean selected = false;
			for(int j = 0;j < aps.size();j++)
			{
				if (selected == true) break;
				for(int k = 0; k < aps.get(j).getAttackSteps().size();k++)
				{
					AttackStep astemp = aps.get(j).getAttackSteps().get(k);
					if( astemp.hashCode() == adss.getModel().getAttackSteps().get(i).hashCode() ) 
					{
						selected = true;
						break;						
					}
				}
			}			
			if(selected) ass.add(adss.getModel().getAttackSteps().get(i));
		}
		//System.out.println("aaa");
		/*for(int i=0;i<aps.size();i++)
		{
			for(int j = 0; j < aps.get(i).getAttackSteps().size();j++)
			{
				System.out.print(aps.get(i).getAttackSteps().get(j).getName() + " // ");
			}
			System.out.println();
		}
		System.out.println("ass.size() is " + ass.size());
		for(int i=0;i<ass.size();i++)
		{
			System.out.println(ass.get(i).getName() + " // ");
		}*/
		
		for(int i=0;i<ass.size();i++)
		{
			AttackStep as= ass.get(i);
			//Transition tra = createTransition(pn.getType(), "T" + i, as.getName(), (float)0,(float) i*100);
			Transition tra = createTransition(pn.getType(), "T" + i, as.getFormula(), (float)0,(float) i*100);
			contents.add(tra);
			trs.add(tra);
			
			/*Place pla = createPlace(pn.getType(), "P" + i, "P" + i, 0, (float)1,(float) i);
			contents.add(pla);
			pls.add(pla);
			
			Arc arc = createArc(pn.getType(), "A" + i, pla, tra);
			contents.add(arc);
			ars.add(arc);*/
		}
		
		//this is the final place
		Place placefirst = createPlace(pn.getType(), "P0" , "START", 0, (float)200,(float) 0);
		contents.add(placefirst);
		pls.add(placefirst);
		
		//this is the last place
		Place placelast = createPlace(pn.getType(), "P" + ass.size() , "END", 0, (float)200,(float) ass.size()*100);
		contents.add(placelast);
		pls.add(placelast);
		
		//System.out.println("bbb");
		//System.out.println(aps.size());
		
		for(int i=0;i<aps.size();i++)
		{			
			AttackPath ap = aps.get(i);	
			
			//System.out.println(i);
			
			for(int j = 0; j < ap.getAttackSteps().size(); j++)
			{
				int index = ass.indexOf(aps.get(i).getAttackSteps().get(j));
				if(j == 0)
				{					
					Arc arc = createArc(pn.getType(), "A" + ars.size(), placefirst, trs.get(index));
					
					boolean existed = false;
					for(int k = 0 ; k < ars.size(); k++)
					{
						if ((ars.get(k).getSource().hashCode() == placefirst.hashCode()) && (ars.get(k).getTarget().hashCode() == trs.get(index).hashCode()))
						{
							existed = true;
							break;
						}
					}
					if(existed == false)
					{
						contents.add(arc);
						ars.add(arc);
					}
					continue;
				}
				
				if(j == (ap.getAttackSteps().size()-1) )
				{					
					Arc arc = createArc(pn.getType(), "A" + ars.size(), trs.get(index), placelast);
					
					boolean existed = false;
					for(int k = 0 ; k < ars.size(); k++)
					{
						if ((ars.get(k).getSource().hashCode() == trs.get(index).hashCode()) && (ars.get(k).getTarget().hashCode() == placelast.hashCode()))
						{
							existed = true;
							break;
						}
					}
					if(existed == false)
					{
						contents.add(arc);
						ars.add(arc);
					}
									
				}				
			
					int index1 = ass.indexOf(aps.get(i).getAttackSteps().get(j-1));
					
					//System.out.println("ddd");
					
					boolean existed = false;
					int preplace = -1;
					for(int k = 0 ; k < ars.size(); k++)
					{
						if ( (ars.get(k).getSource().hashCode() == trs.get(index1).hashCode()) )
						{
							existed = true;
							preplace = k;
							break;
						}
					}
					//System.out.println("eee");
					if(existed == false)
					{			
						//System.out.println("eee1");
						Place tempplace = createPlace(pn.getType(), "P"+ pls.size(), "P"+ pls.size(), 0, (float)200,(float) pls.size()*60);
						contents.add(tempplace);
						pls.add(tempplace);
						
						Arc arc1 = createArc(pn.getType(), "A" + ars.size(), trs.get(index1), tempplace);
						contents.add(arc1);
						ars.add(arc1);
						Arc arc2 = createArc(pn.getType(), "A" + ars.size(), tempplace, trs.get(index));
						contents.add(arc2);
						ars.add(arc2);
						//System.out.println("eee11");
					}
					else
					{
						//System.out.println("eee2");
						
						Place tempplace = (Place) ars.get(preplace).getTarget();
						//System.out.println("eee2_1");
						Arc arc = createArc(pn.getType(), "A" + ars.size(), tempplace, trs.get(index));
						contents.add(arc);
						ars.add(arc);		
						//System.out.println("eee21");
					}	
					//System.out.println("fff");
					//continue;
				
				
				/*int index = adss.getModel().getAttackSteps().indexOf(adss.getModel().getAttackPaths().get(i).getAttackSteps().get(j));
				int index1 = adss.getModel().getAttackSteps().indexOf(adss.getModel().getAttackPaths().get(i).getAttackSteps().get(j+1));
				Arc arc = createArc(pn.getType(), "A" + ars.size(), trs.get(index), pls.get(index1));
				
				boolean existed = false;
				for(int k = 0 ; k < ars.size(); k++)
				{
					if ((ars.get(k).getSource().hashCode() == trs.get(index).hashCode()) && (ars.get(k).getTarget().hashCode() == pls.get(index1).hashCode()))
					{
						existed = true;
						break;
					}
				}
				if(existed == false)
				{
					contents.add(arc);
					ars.add(arc);
				}*/
			}
		}
		//placelast.setGraphics(new Graphics((float)200,(float) ass.size()*50));

		//System.out.println("ccc");
		return pn;
	}
	
	private PetriNetDoc createPetriNetDoc(int number) {
		PetriNetDoc doc = PnmlcoremodelFactory.eINSTANCE.createPetriNetDoc();
		PetriNet net = PnmlcoremodelFactory.eINSTANCE.createPetriNet();
		net.setId("n1");
		doc.getNet().add(net);
		PetriNetType type = PtnetFactory.eINSTANCE.createPTNet();
		net.setType(type);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText("Mutual exclusion");
		net.setName(nameLabel);
		
		Page page = createPage(type,"pg0","semaphor page");
		EList<Page> pages = net.getPage();
		pages.add(page);
		
		Place semaphor = this.createPlace(type, "semaphor", "semaphor", 1, 380, 140);
		page.getObject().add(semaphor);
		
		for (int i=1; i<= number; i++) {
			page = createAgentPage(type, semaphor, i);
			pages.add(page);
		}
		
		return doc;
	}
	
	private PetriNet createPetriNet() {
		//PetriNetDoc doc = PnmlcoremodelFactory.eINSTANCE.createPetriNetDoc();
		PetriNet net = PnmlcoremodelFactory.eINSTANCE.createPetriNet();
		net.setId("n1");
		//doc.getNet().add(net);
		PetriNetType type = PtnetFactory.eINSTANCE.createPTNet();
		net.setType(type);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText("Mutual exclusion");
		net.setName(nameLabel);
		
		Page page = createPage(type,"page1","page1");
		EList<Page> pages = net.getPage();
		pages.add(page);
		/*
		Place semaphor = this.createPlace(type, "semaphor", "semaphor", 1, 380, 140);
		page.getObject().add(semaphor);
		
		for (int i=1; i<= number; i++) {
			page = createAgentPage(type, semaphor, i);
			pages.add(page);
		}*/
		
		return net;
	}
	
	private Page createAgentPage(PetriNetType type, Place semaphor, int i) {
		Page page = createPage(type, "pg"+i, "agent"+i);
		
		Place idle = createPlace(type, "idle"+i, "idle"+i, 1, 100, 220);
		Place pending = createPlace(type, "pending"+i, "pending"+i, 0, 100, 60);
		Place critical = createPlace(type, "critical"+i, "critical"+i, 0, 300, 140);

		RefPlace semRef = createRefPlace("semaphor"+i, "semaphor", semaphor, 380, 140);
		
		Transition t1 = createTransition(type, "t1."+i, "t1."+i, 40, 140);
		Transition t2 = createTransition(type, "t2."+i, "t2."+i, 220, 60);
		Transition t3 = createTransition(type, "t3."+i, "t3."+i, 220, 220);
		
		Arc a1 = createArc(type, "a1."+i, idle, t1);
		Arc a2 = createArc(type, "a2."+i, t1, pending);
		Arc a3 = createArc(type, "a3."+i, pending, t2);
		Arc a4 = createArc(type, "a4."+i, t2, critical);
		Arc a5 = createArc(type, "a5."+i, critical, t3);
		Arc a6 = createArc(type, "a6."+i, t3, idle);
		
		Arc a7 = createArc(type, "a7."+i, semRef, t2);
		Coordinate coordinate = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		coordinate.setX(300);
		coordinate.setY(60);
		ArcGraphics arcGraphics = PnmlcoremodelFactory.eINSTANCE.createArcGraphics();
		arcGraphics.getPosition().add(coordinate);
		a7.setGraphics(arcGraphics);
		
		Arc a8 = createArc(type, "a8."+i, t3, semRef);
		coordinate = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		coordinate.setX(300);
		coordinate.setY(220);
		arcGraphics = PnmlcoremodelFactory.eINSTANCE.createArcGraphics();
		arcGraphics.getPosition().add(coordinate);
		a8.setGraphics(arcGraphics);
		
		EList<Object> contents = page.getObject();
		contents.add(idle);
		contents.add(pending);
		contents.add(critical);
		contents.add(semRef);
		contents.add(t1);
		contents.add(t2);
		contents.add(t3);
		contents.add(a1);
		contents.add(a2);
		contents.add(a3);
		contents.add(a4);
		contents.add(a5);
		contents.add(a6);
		contents.add(a7);
		contents.add(a8);

		return page;		
	}
	
	private Page createPage(PetriNetType type, String id, String name) {
		Page page = type.createPage();
		page.setId(id);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText(name);
		page.setName(nameLabel);
		return page;
	}
	
	private Place createPlace(PetriNetType type, String id, String name, int marking, float x, float y) {
		Place place = (Place) type.createPlace();
		place.setId(id);
		NodeGraphics nodeGraphics = PnmlcoremodelFactory.eINSTANCE.createNodeGraphics();
		Coordinate position = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		position.setX(x);
		position.setY(y);
		nodeGraphics.setPosition(position);
		Coordinate size = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		size.setX(40);
		size.setY(40);
		nodeGraphics.setDimension(size);
		place.setGraphics(nodeGraphics);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText(name);
		AnnotationGraphics labelPos = PnmlcoremodelFactory.eINSTANCE.createAnnotationGraphics();
		Coordinate offset = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		offset.setX(0);
		offset.setY(30);
		labelPos.setOffset(offset);

		
		nameLabel.setGraphics(labelPos);
		place.setName(nameLabel);
		if (marking > 0) {
			PTMarking markingLabel = PtnetFactory.eINSTANCE.createPTMarking();
			NonNegativeInteger value = new NonNegativeInteger(""+marking);
			markingLabel.setText(value);
			labelPos = PnmlcoremodelFactory.eINSTANCE.createAnnotationGraphics();
			offset = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
			offset.setX(0);
			offset.setY(-40);
			labelPos.setOffset(offset);
			markingLabel.setGraphics(labelPos);
			place.setInitialMarking(markingLabel);
		}
		return place;
	}
	
	private Transition createTransition(PetriNetType type, String id, String name, float x, float y) {
		Transition transition = type.createTransition();
		transition.setId(id);
		NodeGraphics nodeGraphics = PnmlcoremodelFactory.eINSTANCE.createNodeGraphics();
		Coordinate position = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		position.setX(x);
		position.setY(y);
		nodeGraphics.setPosition(position);
		Coordinate size = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		size.setX(40);
		size.setY(40);
		nodeGraphics.setDimension(size);
		transition.setGraphics(nodeGraphics);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText(name);
		AnnotationGraphics labelPos = PnmlcoremodelFactory.eINSTANCE.createAnnotationGraphics();
		Coordinate offset = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		offset.setX(0);
		offset.setY(30);
		labelPos.setOffset(offset);
		nameLabel.setGraphics(labelPos);
		transition.setName(nameLabel);
		return transition;
	}
	
	private RefPlace createRefPlace(String id, String name, PlaceNode place, float x, float y) {
		RefPlace refPlace = PnmlcoremodelFactory.eINSTANCE.createRefPlace();
		refPlace.setId(id);
		refPlace.setRef(place);
		NodeGraphics nodeGraphics = PnmlcoremodelFactory.eINSTANCE.createNodeGraphics();
		Coordinate position = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		position.setX(x);
		position.setY(y);
		nodeGraphics.setPosition(position);
		Coordinate size = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		size.setX(40);
		size.setY(40);
		nodeGraphics.setDimension(size);
		refPlace.setGraphics(nodeGraphics);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText(name);
		AnnotationGraphics labelPos = PnmlcoremodelFactory.eINSTANCE.createAnnotationGraphics();
		Coordinate offset = PnmlcoremodelFactory.eINSTANCE.createCoordinate();
		offset.setX(0);
		offset.setY(30);
		labelPos.setOffset(offset);
		nameLabel.setGraphics(labelPos);
		refPlace.setName(nameLabel);
		return refPlace;
	}
	
	private Arc createArc(PetriNetType type, String id, Node source, Node target) {
		Arc arc = type.createArc();
		arc.setId(id);
		arc.setSource(source);
		arc.setTarget(target);
		return arc;	
	}

}
