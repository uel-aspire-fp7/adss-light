package eu.aspire_fp7.adss.light.akb.ui.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.AkbFactory;
import eu.aspire_fp7.adss.akb.Preferences;
import eu.aspire_fp7.adss.light.akb.ui.Activator;

/**
 * The CryptoStudio nature.
 * @author Daniele Canavese
 **/
public class Nature implements IProjectNature
{
	/** The nature id. **/
	public static final String NATURE_ID = Activator.PLUGIN_ID + ".nature";

	/**
	 * Configures the current project.
	 * @throws CoreException
	 *             If something went wrong.
	 **/
	@Override
	public void configure() throws CoreException
	{
		final IFile file = getProject().getFile("akb-light.owl");
		final IEditorDescriptor descriptor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(file.getName());
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		Job job = new Job("Creating initial AKB")
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try
				{
					ADSS.create(file, getPreferences(getProject()));
					Display.getDefault().asyncExec(new Runnable()
					{
						@Override
						public void run()
						{
							try
							{
								page.openEditor(new FileEditorInput(file), descriptor.getId());
							}
							catch (PartInitException e)
							{
								e.printStackTrace();
							}
						}
					});
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return Status.CANCEL_STATUS;
				}

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	/**
	 * Deconfigures the current project.
	 * @throws CoreException
	 *             If something went wrong.
	 **/
	@Override
	public void deconfigure() throws CoreException
	{
	}

	/**
	 * Retrieves the project.
	 * @return The project.
	 **/
	@Override
	public IProject getProject()
	{
		return project;
	}

	/**
	 * Sets the project.
	 * @param project
	 *            The project to set.
	 **/
	@Override
	public void setProject(IProject project)
	{
		this.project = project;
	}

	/**
	 * Creates the user preferences for a project.
	 * @param project
	 *            The project.
	 * @return
	 * @return The project preferences.
	 * @throws CoreException
	 *             If something goes wrong.
	 **/
	public Preferences getPreferences(IProject project) throws CoreException
	{
		String workingDirectory = (String) (project.getSessionProperty(new QualifiedName("ADSS", "workingDirectory")));
		String actcConfigFile = (String) (project.getSessionProperty(new QualifiedName("ADSS", "actcConfigFile")));
		String adssPatchFile = (String) (project.getSessionProperty(new QualifiedName("ADSS", "adssPatchFile")));
		String adssJSONFile = (String) (project.getSessionProperty(new QualifiedName("ADSS", "adssJSONFile")));
		String actcCommand = (String) (project.getSessionProperty(new QualifiedName("ADSS", "actcCommand")));
		String codeSurferCommand = (String) (project.getSessionProperty(new QualifiedName("ADSS", "codeSurferCommand")));
		String compilationCommand = (String) (project.getSessionProperty(new QualifiedName("ADSS", "compilationCommand")));
		Boolean remoteConnectionEnabled = (Boolean) (project.getSessionProperty(new QualifiedName("ADSS", "remoteConnectionEnabled")));
		String remoteHost = (String) (project.getSessionProperty(new QualifiedName("ADSS", "remoteHost")));
		String remotePort = (String) (project.getSessionProperty(new QualifiedName("ADSS", "remotePort")));
		String remoteUsername = (String) (project.getSessionProperty(new QualifiedName("ADSS", "remoteUsername")));
		String remotePassword = (String) (project.getSessionProperty(new QualifiedName("ADSS", "remotePassword")));
		String remoteFileSep = (String) (project.getSessionProperty(new QualifiedName("ADSS", "remoteFileSep")));

		Preferences preferences = AkbFactory.eINSTANCE.createPreferences();

		if (workingDirectory != null && !workingDirectory.isEmpty())
			preferences.setWorkingDirectory(workingDirectory);
		if (actcConfigFile != null && !actcConfigFile.isEmpty())
			preferences.setActcConfigurationFile(actcConfigFile);
		if (adssPatchFile != null && !adssPatchFile.isEmpty())
			preferences.setAdssPatchFile(adssPatchFile);
		if (adssJSONFile != null && !adssJSONFile.isEmpty())
			preferences.setAdssJSONFile(adssJSONFile);
		if (actcCommand != null && !actcCommand.isEmpty())
			preferences.setActcCommand(actcCommand);
		if (codeSurferCommand != null && !codeSurferCommand.isEmpty())
			preferences.setCodeSurferCommand(codeSurferCommand);
		if (compilationCommand != null && !compilationCommand.isEmpty())
			preferences.setCompilationCommand(compilationCommand);
		preferences.setRemoteConnection(remoteConnectionEnabled);
		if (remoteHost != null && !remoteHost.isEmpty())
			preferences.setRemoteHost(remoteHost);
		if (remotePort != null && !remotePort.isEmpty())
			preferences.setRemotePort(Integer.parseInt(remotePort));
		if (remoteUsername != null && !remoteUsername.isEmpty())
			preferences.setRemoteUsername(remoteUsername);
		if (remotePassword != null && !remotePassword.isEmpty())
			preferences.setRemotePassword(remotePassword);
		if (remoteFileSep != null && !remoteFileSep.isEmpty())
			preferences.setRemoteFileSeparator(remoteFileSep);

		return preferences;
	}

	/** The project. **/
	private IProject project;
}
