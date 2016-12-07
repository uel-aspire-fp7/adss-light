package org.uel.aspire.wp4.assessment.handlers;

import java.util.List;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.uel.aspire.wp4.assessment.*;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pnmlcoremodel.PnmlcoremodelFactory;
import org.pnml.tools.epnk.pnmlcoremodel.PnmlcoremodelPackage;
import org.pnml.tools.epnk.pntypes.hlpng.pntd.hlpngdefinition.Page;
import org.uel.aspire.wp4.assessment.AttackAssessmentAction;
import org.uel.aspire.wp4.pnassessment.PNAssessmentAction;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class PetriNetAssessment extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public PetriNetAssessment() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		/*MessageDialog.openInformation(
				window.getShell(),
				"Assessment",
				"MessageAssessment in handler");*/
		
		////call the related functions: assessment or simulation 
		PNAssessmentAction ass = new PNAssessmentAction();
	    PetriNet ptmodel = null;
		
	    
		TreeSelection cursel = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		
		//System.out.print(cursel.getFirstElement().getClass().toString() );
		if (cursel.getFirstElement() instanceof PetriNetDoc )
		{
			ptmodel = ((PetriNetDoc) cursel.getFirstElement()).getNet().get(0);
			ass.createJob(ptmodel, null);
			ass.do_job();
			return null;
		}
		if (cursel.getFirstElement() instanceof PetriNet) 
		{
			ptmodel = (PetriNet) cursel.getFirstElement();
			ass.createJob(ptmodel, null);
			ass.do_job();
			return null;
		}

		MessageDialog.openInformation(
				window.getShell(),
				"Assessment",
				"!Please select a <PetriNetDoc> or <PetriNet> item in tree structure! " );
		/*if (cursel.getFirstElement() instanceof Page) 
		{
			ptmodel = ((Page) cursel.getFirstElement()).eAllContents().;
		}*/
		
		
		
		//PetriNetDoc cursel = (PetriNetDoc) HandlerUtil.getCurrentSelection(event);
		//EObject str =  cursel.iterator();
		//PetriNetDoc ptd = (PetriNetDoc) cursel.;
	//	List<PetriNet> nets = ptd.getNet();
		//ptmodel = str.getmodel();
		//event.
		//ptmodel = (PetriNet) cursel.getFirstElement().getClass();
		//ptmodel = (PetriNet) cursel;
		//PnmlcoremodelPackage ptmp = PnmlcoremodelFactory.eINSTANCE.getPnmlcoremodelPackage();
		//ptmodel = (PetriNet) ptmp.getPetriNet();
		//for (PetriNet net : nets)
		//{
			
		//}		
		//System.out.println("test for action start!");
		return null;
	}
}
