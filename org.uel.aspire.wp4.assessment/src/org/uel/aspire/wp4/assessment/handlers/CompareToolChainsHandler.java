package org.uel.aspire.wp4.assessment.handlers;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.uel.aspire.wp4.assessment.*;
import org.pnml.tools.epnk.diagram.edit.parts.PageEditPart;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pnmlcoremodel.PnmlcoremodelFactory;
import org.pnml.tools.epnk.pnmlcoremodel.PnmlcoremodelPackage;
import org.pnml.tools.epnk.pntypes.hlpng.pntd.hlpngdefinition.Page;
import org.uel.aspire.wp4.assessment.AttackAssessmentAction;
import org.uel.aspire.wp4.assessment.APIs.AttackModel;
import org.uel.aspire.wp4.assessment.APIs.Measure;
import org.uel.aspire.wp4.assessment.APIs.Metrics;
import org.uel.aspire.wp4.assessment.APIs.Protection;
import org.uel.aspire.wp4.assessment.APIs.ProtectionSolution;
import org.uel.aspire.wp4.comparetoolchains.CompareToolChains;
import org.uel.aspire.wp4.comparetoolchains.MappingFileIn;
import org.uel.aspire.wp4.comparetoolchains.MeasureFileIn;
import org.uel.aspire.wp4.comparetoolchains.ProtectionFileIn;


//this handler is the main class triggered by the CompareTwoProtections command in menu, the execute() method 
//the general process of this method is : 1) select the pnml file in eclipse view, 2) select two protection files by dialogs, 
//3)load config files, including measure file and protection-to-measure file. 
//4)send all previous data to class "CompareToolChains" class to compare.

public class CompareToolChainsHandler extends AbstractHandler {

	public CompareToolChainsHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		
		////call the related functions: assessment or simulation 
		CompareToolChains ctc = new CompareToolChains();
	    PetriNet ptmodel = null;
			    
		//TreeSelection cursel = (TreeSelection) HandlerUtil.getCurrentSelection(event);
	    StructuredSelection cursel = (StructuredSelection) HandlerUtil.getCurrentSelection(event);
		if (!(cursel.getFirstElement() instanceof PetriNetDoc ))
		{
			if (!(cursel.getFirstElement() instanceof PetriNet)) 
			{
				if (!(cursel.getFirstElement() instanceof PageEditPart))
				{
					MessageDialog.openInformation(
						window.getShell(),
						"Assessment",
						"!Please select a <PetriNetDoc> or <PetriNet> item in tree structure! " );
					return null;
				}
			}
		}

		
		ProtectionSolution pc1 = new ProtectionSolution();
		ProtectionSolution pc2 = new ProtectionSolution();
		//Obtain protection configurations from files and dialog box
		String pc1name = "", pc2name = "";
		
		MessageDialog.openInformation(
				window.getShell(),
				"Select Protection File",
				"Please select the FIRST Protection File for Comparison in the NEXT Dialog!");
		
		FileDialog fd1 = new FileDialog(window.getShell(), SWT.OPEN);		
        //fd1.setFilterExtensions(new String[] { "txt" });         
        fd1.setText("Open Protection 1's File"); 
        pc1name = fd1.open(); 
        fd1.setFilterPath("/aspire-config");
        //System.out.println(file1);
        //System.out.println(pc1name);
		MessageDialog.openInformation(
				window.getShell(),
				"Select Protection File",
				"Please select the SECOND Protection File for Comparison in the NEXT Dialog!");		
		
		FileDialog fd2 = new FileDialog(window.getShell(), SWT.OPEN);
        //fd2.setFilterExtensions(new String[] { "txt" });         
        fd2.setText("Open Protection 2's File"); 
        fd2.setFilterPath("/aspire-config");
        pc2name = fd2.open(); 
        
                
		ProtectionFileIn pfi1 = new ProtectionFileIn(pc1name);
		pc1 = pfi1.getProtectionSolution();
		//System.out.println("In the handler, pc1 is "+pc1.toString());
		ProtectionFileIn pfi2 = new ProtectionFileIn(pc2name);
		pc2 = pfi2.getProtectionSolution();
		
		System.out.println("The loading of protection files is done!");
		
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
		
		System.out.println("The loading of measures file is done!");
		
		HashMap<String, HashMap<String, String> > mapping = new HashMap<String, HashMap<String, String> >();
		//obtain mapping from file (protection to measure)
		MappingFileIn map = new MappingFileIn("aspire-config/Protection-to-Measure.txt");
		
		mapping = map.getMapping();
		//if (mapping.isEmpty()) System.out.println("mapping.isEmpty() is ture");
		System.out.println("The loading of mapping files is done! ");
		
		String result;// this first one is better than second one.
		
		if (cursel.getFirstElement() instanceof PetriNetDoc )
		{
			ptmodel = ((PetriNetDoc) cursel.getFirstElement()).getNet().get(0);
			ctc.createJob(ptmodel, pc1, pc2, measurelist, mapping);
			result = ctc.doJob();
			/*if (result) MessageDialog.openInformation(
					window.getShell(),
					"Compare Result",
					"!The Protection Configruation 1 is better than The protection Configruation 2! " );
			else MessageDialog.openInformation(
					window.getShell(),
					"Compare Result",
					"!The Protection Configruation 2 is better than The Protection Configruation 1! " );*/
			MessageDialog.openInformation(
					window.getShell(),
					"Compare Result",
					result );
			return null;
		}
		if (cursel.getFirstElement() instanceof PetriNet) 
		{
			ptmodel = (PetriNet) cursel.getFirstElement();
			ctc.createJob(ptmodel, pc1, pc2, measurelist, mapping);
			result = ctc.doJob();
			/*if (result) MessageDialog.openInformation(
				window.getShell(),
				"Compare Result",
				"!The Protection Configruation 1 is better than The protection Configruation 2! " );
			else MessageDialog.openInformation(
				window.getShell(),
				"Compare Result",
				"!The Protection Configruation 2 is better than The Protection Configruation 1! " );*/
			MessageDialog.openInformation(
					window.getShell(),
					"Compare Result",
					result );
			return null;
		}
		
		if (cursel.getFirstElement() instanceof Page)
		{
			ptmodel = (PetriNet) cursel.getFirstElement();
			ctc.createJob(ptmodel, pc1, pc2, measurelist, mapping);
			result = ctc.doJob();
			/*if (result) MessageDialog.openInformation(
				window.getShell(),
				"Compare Result",
				"!The Protection Configruation 1 is better than The protection Configruation 2! " );
			else MessageDialog.openInformation(
				window.getShell(),
				"Compare Result",
				"!The Protection Configruation 2 is better than The Protection Configruation 1! " );*/
			MessageDialog.openInformation(
					window.getShell(),
					"Compare Result",
					result );
			return null;
		}

		MessageDialog.openInformation(
				window.getShell(),
				"Assessment",
				"!Please select a <PetriNetDoc> or <PetriNet> item in tree structure! " );

		return null;
	}
}
