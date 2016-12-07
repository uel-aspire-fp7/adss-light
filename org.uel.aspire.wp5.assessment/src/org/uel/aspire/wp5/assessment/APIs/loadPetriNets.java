/**
 * 
 */
package org.uel.aspire.wp5.assessment.APIs;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNetDoc;

/**
 * @author Gaofeng
 * this class is to load PNML file to the memory
 *
 */
public class loadPetriNets {
	
	private PetriNet pn;
	private String path="";
	private String projectname = "eu.aspire_fp7.adss.tests";
	private String foldername = "NAGRA-test3";
	private String filename = "";
	
	public loadPetriNets(String pathin, String filenamein)
	{
		path = pathin;
		filename = filenamein;
	}
	
	public loadPetriNets(PetriNet pnin)
	{
		pn = pnin;
	}	
	
	public List<PetriNet> getPN()
	{
		//TODO
		
		File file = new File(path+filename);
		IWorkspace workspace= ResourcesPlugin.getWorkspace();   
		IPath location= Path.fromOSString(file.getAbsolutePath());
		IFile ifile= workspace.getRoot().getFileForLocation(location);
		
		//IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		//IWorkspaceRoot root = workspace.getRoot();
		//IProject project  = root.getProject(projectname);
		//IFolder folder = project.getFolder(foldername);
		//IFile file = folder.getFile(filename);


		String extension = ifile.getFileExtension();
		if (extension != null && 
				(extension.equals("pnml" ) || 
						extension.equals("pnx"))) 
		{
			String path = ifile.getLocationURI().toString();
		
			URI uri = URI.createURI(path);
			ResourceSet resourceSet = new ResourceSetImpl();
			Resource resource = null;
			try 
			{
				resource = resourceSet.getResource(uri, true);
			} 
			catch (Exception e) 
			{
				System.out.println("File could not be read.");										
			}

			List<EObject> contents = resource.getContents();
			if (contents != null && contents.size() > 0) 
			{
				EObject object = contents.get(0);
				if (object instanceof PetriNetDoc) 
				{
					PetriNetDoc document = (PetriNetDoc) object;
					List<PetriNet> nets = document.getNet();
					int no = nets.size();
					if(no==0) 
						return null;
					else
					{
						pn = nets.get(0);
						return  nets;
					}
				}
					
			}
		//
		}
		return null;			
	}	

	public void setPN(String pathin, String filenamein)
	{
		path = pathin;
		filename = filenamein;
	}
}
