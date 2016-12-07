/**
 * 
 */
package org.uel.aspire.spa;

import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.pnml.tools.epnk.pnmlcoremodel.Name;
import org.pnml.tools.epnk.pnmlcoremodel.Page;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetType;
import org.pnml.tools.epnk.pnmlcoremodel.PnmlcoremodelFactory;
import org.pnml.tools.epnk.pntypes.ptnet.PtnetFactory;

import eu.aspire_fp7.adss.light.akb.ui.wizards.CommandsPage;
import eu.aspire_fp7.adss.light.akb.ui.wizards.Nature;
import eu.aspire_fp7.adss.light.akb.ui.wizards.OverviewPage;
import eu.aspire_fp7.adss.light.akb.ui.wizards.RemoteConnectionPage;

//import eu.aspire_fp7.adss.light.akb.ui.wizards.Nature;

/**
 * @author Gaofeng
 *
 */
public class NewWizard extends Wizard implements INewWizard {

	/**
	 * 
	 */
	private WizardNewProjectCreationPage pageOne;
	private SPAProjectPage pageTwo;
	
	private static String projectPath = "";
	
	private PetriNet pn;
	//TODO
	
	public NewWizard() {
		// TODO Auto-generated constructor stub
		setWindowTitle("ADSS-Light Project");
	}
	
	static public String getProjectPath()
	{
		return projectPath;
	}
	


	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		
		try////////////////////////creating akb file
		{
			// Creates the project.
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(pageOne.getProjectName());
			project.create(null);
			// Opens it.
			project.open(null);
			
			ResourceSet resourceSet2 = new ResourceSetImpl();
			final URI uri2 = URI.createFileURI(pageOne.getLocationPath() + "/" +pageOne.getProjectName() + "/AKB/akb-light.owl" );			
			final Resource resource2 = resourceSet2.createResource(uri2);			
			EList<EObject> contents2 = resource2.getContents();	
			try {
				resource2.save(null);
			} catch (IOException e) {			
				e.printStackTrace();
			}
			// Sets property given by the user.		
			/*
			project.setSessionProperty(new QualifiedName("ADSS", "workingDirectory"), page1.getWorkingDirectory());
			project.setSessionProperty(new QualifiedName("ADSS", "actcConfigFile"), page1.getACTCConfigurationFile());
			project.setSessionProperty(new QualifiedName("ADSS", "adssPatchFile"), page1.getADSSPatchFile());
			project.setSessionProperty(new QualifiedName("ADSS", "adssJSONFile"), page1.getADSSJSONFile());
			project.setSessionProperty(new QualifiedName("ADSS", "remoteConnectionEnabled"), page2.isRemoteConnection());
			project.setSessionProperty(new QualifiedName("ADSS", "remoteHost"), page2.getRemoteHost());
			project.setSessionProperty(new QualifiedName("ADSS", "remotePort"), page2.getRemotePort());
			project.setSessionProperty(new QualifiedName("ADSS", "remoteUsername"), page2.getRemoteUsername());
			project.setSessionProperty(new QualifiedName("ADSS", "remotePassword"), page2.getRemotePassword());
			project.setSessionProperty(new QualifiedName("ADSS", "remoteFileSep"), page2.getRemoteServerFileSeparator());			
			project.setSessionProperty(new QualifiedName("ADSS", "actcCommand"), page3.getACTCCommand());
			project.setSessionProperty(new QualifiedName("ADSS", "codeSurferCommand"), page3.getCodeSurferCommand());
			project.setSessionProperty(new QualifiedName("ADSS", "compilationCommand"), page3.getCompilationCommand());
			// Sets the nature.
			 * 
			 
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = Nature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);			 
			// Opens the right perspective.
			 * */
			BasicNewProjectResourceWizard.updatePerspective(config);					
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		
		////////////////////////creating empty pnml file
		pn = PnmlcoremodelFactory.eINSTANCE.createPetriNet();
		pn.setId("n1");			
		PetriNetType type = PtnetFactory.eINSTANCE.createPTNet();
		pn.setType(type);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText("Mutual exclusion");
		pn.setName(nameLabel);			
		Page page = createPage(type,"page1","page1");
		EList<Page> pages = pn.getPage();
		pages.add(page);			
		String file = pageOne.getLocationPath() + "/" +pageOne.getProjectName() + "/PNML/" + pageOne.getProjectName()  + ".pnml";	
		//System.out.println(file);
		PetriNetDoc doc = PnmlcoremodelFactory.eINSTANCE.createPetriNetDoc();
		doc.getNet().add(pn);
		ResourceSet resourceSet = new ResourceSetImpl();
		final URI uri = URI.createFileURI(file);			
		final Resource resource = resourceSet.createResource(uri);			
		EList<EObject> contents = resource.getContents();		
		contents.add(doc);				
		try {					
			resource.save(null);					
		} catch (IOException e1) {							
			e1.printStackTrace();					
		}
		
		////////////////////////creating metrics folder		
		ResourceSet resourceSet1 = new ResourceSetImpl();
		final URI uri1 = URI.createFileURI(pageOne.getLocationPath() + "/" +pageOne.getProjectName() + "/Metrics/metrics.txt" );			
		final Resource resource1 = resourceSet1.createResource(uri1);			
		EList<EObject> contents1 = resource1.getContents();	
		try {
			resource1.save(null);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		projectPath = pageOne.getLocationPath() + "/" +pageOne.getProjectName() + "/";
		
		return true;
	}
	
	@Override
	public void addPages() {
	    super.addPages();
	 
	    pageOne = new WizardNewProjectCreationPage("From ADSS-Light Project Wizard");
	    pageOne.setTitle("ADSS-Light Project");
	    pageOne.setDescription("");
	 
	    addPage(pageOne);
	    
		page1 = new OverviewPage();
		page2 = new RemoteConnectionPage();
		page3 = new CommandsPage();
		addPage(page1);
		addPage(page2);
		addPage(page3);
	    
	    pageTwo = new SPAProjectPage("From SPA Project Wizard");
	    pageTwo.setTitle("From SPA Project");
	    pageTwo.setDescription("");
	 
	    addPage(pageTwo);
	}
	
	private Page createPage(PetriNetType type, String id, String name) {
		Page page = type.createPage();
		page.setId(id);
		Name nameLabel = PnmlcoremodelFactory.eINSTANCE.createName();
		nameLabel.setText(name);
		page.setName(nameLabel);
		return page;
	}
	
	/** The configuration element. **/
	private IConfigurationElement config;
	/** The overview page. **/
	private OverviewPage page1;
	/** The source directory page. **/
	private RemoteConnectionPage page2;
	/** The remote connection page. **/
	private CommandsPage page3;
	
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException
	{
		this.config = config;
	}

}
