package org.uel.aspire.wp4.assessment.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
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
import org.uel.aspire.wp4.assessment.AttackAssessmentAction;
import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.comparetoolchains.ProtectionFileIn;
import org.uel.aspire.wp4.data.AttackerCap;
import org.uel.aspire.wp4.data.FileInAttacker;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class RunSimulationHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public RunSimulationHandler() {
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
		AttackAssessmentAction ass = new AttackAssessmentAction();
		
	    PetriNet ptmodel = null;
		MessageDialog.openInformation(
				window.getShell(),
				"Select Protection File",
				"Please select the Protection File for Simulation in the NEXT Dialog!");
		
		FileDialog fd1 = new FileDialog(window.getShell(), SWT.OPEN);		
                
        fd1.setText("Open Protection's File"); 
        String pc1name = fd1.open(); 
        fd1.setFilterPath("/aspire-protection");        
        
                
		ProtectionFileIn pfi1 = new ProtectionFileIn(pc1name);
		ProtectionSolution pc1 = pfi1.getProtectionSolution();
		
		MessageDialog.openInformation(
				window.getShell(),
				"Select Attacker File",
				"Please select the Attacker File for Simulation in the NEXT Dialog!");
		FileDialog fd2 = new FileDialog(window.getShell(), SWT.OPEN);	
        
        fd2.setText("Open Attacker's File"); 
        String at = fd2.open(); 
        fd2.setFilterPath("/aspire-attacker"); 
		//System.out.println("The loading of protection files is done!");
        
        ArrayList<AttackerCap> attackers = new ArrayList<AttackerCap>();
        FileInAttacker fa = new FileInAttacker(at);
        attackers = fa.file_in();
        if (attackers==null ) System.out.println("attackers==null in runsimulationhandler ");
        //System.out.print("handleer"); System.out.println(attackers);
        
	    //System.out.print(attackers.get(0).getattacker_effort().get(0));
	    
		TreeSelection cursel = (TreeSelection) HandlerUtil.getCurrentSelection(event);
		if (cursel.getFirstElement() instanceof PetriNetDoc )
		{
			ptmodel = ((PetriNetDoc) cursel.getFirstElement()).getNet().get(0);
			ass.createJob(ptmodel, null, attackers);			
			double oneresult = ass.do_job();
			MessageDialog.openInformation(
					window.getShell(),
					"Assessment Result",
					"The PN simulator result is "+ oneresult );
			return null;
		}
		if (cursel.getFirstElement() instanceof PetriNet) 
		{
			ptmodel = (PetriNet) cursel.getFirstElement();
			ass.createJob(ptmodel, null);
			double oneresult = ass.do_job();
			MessageDialog.openInformation(
					window.getShell(),
					"Assessment Result",
					"The PN simulator result is "+ oneresult );
			return null;
		}

		MessageDialog.openInformation(
				window.getShell(),
				"Simulation",
				"!Please select a <PetriNetDoc> or <PetriNet> item in tree structure! " );
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
