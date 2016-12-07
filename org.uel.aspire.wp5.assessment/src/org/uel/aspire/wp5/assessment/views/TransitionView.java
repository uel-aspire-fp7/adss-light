package org.uel.aspire.wp5.assessment.views;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.*;
import org.pnml.tools.epnk.gmf.extensions.graphics.figures.TransitionFigure;
import org.pnml.tools.epnk.helpers.FlatAccess;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.uel.aspire.wp4.assessment.APIs.AssessmentProcessBoth2;

import eu.aspire_fp7.adss.ADSS;
import eu.aspire_fp7.adss.akb.AkbFactory;
import eu.aspire_fp7.adss.akb.AttackPath;
import eu.aspire_fp7.adss.akb.AttackStep;
import eu.aspire_fp7.adss.akb.Metric;
import eu.aspire_fp7.adss.akb.Solution;
import eu.aspire_fp7.adss.akb.ui.ADSSService;
import eu.aspire_fp7.adss.akb.ui.providers.NameLabelProvider;
//import eu.aspire_fp7.adss.akb.ui.providers.NameLabelProvider;
import eu.aspire_fp7.adss.connectors.ACTCConnector;
//import eu.aspire_fp7.adss.light.akb.ui.providers.ProtectionSolutionContentProvider;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


/**
*this is the view to display and edit the transition with formula and related attack steps from aAKB.
*by Gaofeng, 06-06-2016.

 */

public class TransitionView extends ViewPart implements ISelectionListener, ISelectionChangedListener, IPartListener2{

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.uel.aspire.wp5.assessment.views.TransitionView";	
	
	private IShowInSource resource;
	private TransitionViewLabelProvider labelprovider;
	private TransitionViewContentProvider contentprovider;
	
	private TreeViewer psTreeViewer;
	
	private ADSS adss;
	private ADSSService adssService;
	
	private PetriNet petrinet;
	
	private AssessmentProcessBoth2 assessment;
		
	private Text transitionName;
	private Text transitionNote;
	private Combo stepCombo;
	private Combo pcCombo;
	private Text stepType;
	private Text transitionFormula;
	private Button saveButton;
	private Button assessButton;
	private Button showKeyPathButton;
	private AttackStep step = null;
	private Transition transition = null;
	
 
	private Text owlDirectoryText;
	private Text owlFileText;
	private Text owlLoadStatus;
	private Text saveStatus;
	
	private Text assessResult;
		
	public TransitionView()
	{
		super();
		labelprovider = new TransitionViewLabelProvider();
		contentprovider = new TransitionViewContentProvider();	
				
		//adss = ADSS.getInstance();
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		getSite().getPage().addPartListener(this);
		adssService = (ADSSService) getSite().getService(ADSSService.class);
		//GridLayout gLayout1 = new GridLayout(2, false);	
		//parent.setLayout(gLayout1);
		//parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));	
		
		Group akbGroup = new Group(parent, SWT.NONE);
		akbGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		akbGroup.setText("AKB from OWL File");
		akbGroup.setLayout(new GridLayout(3, false));		
		Label owllabel1 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		owllabel1.setText("OWL Directory : ");	
		owlDirectoryText = new Text(akbGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		owlDirectoryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		owlDirectoryText.setText("E:/eclipse mars for ADSS/workspace/Nagra Use Case/AKB/");		
		Button owlDirectoryButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );
		owlDirectoryButton.setText("Browse...");		
		Label owllabel2 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		owllabel2.setText("OWL File : ");			
		owlFileText = new Text(akbGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		owlFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		owlFileText.setText("akb-light.owl");		
		Button owlFileButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );	
		owlFileButton.setText("Browse...");
		Button loadOWLButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );	
		loadOWLButton.setText("Load OWL File");
		
		owlLoadStatus = new Text(akbGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		owlLoadStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		owlLoadStatus.setText("                              ");	
		
		owlDirectoryText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{				
				//adss.getModel().getPreferences().setSetPNDirectory(pnDirectoryText.getText());
			}
		});
		owlFileText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{				
				//adss.getModel().getPreferences().setSetPNFile(pnFileText.getText());
			}
		});
		owlDirectoryButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Display.getDefault().syncExec(new Runnable()
				{
					@Override
					public void run()
					{
						DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getActiveShell());
						String directory = dialog.open();
						if (directory != null)
						{							
							//adss.getModel().getPreferences().setSetPNDirectory(directory);
							owlDirectoryText.setText(directory);
						}
					}
				});
			}
		});
		owlFileButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Display.getDefault().syncExec(new Runnable()
				{
					@Override
					public void run()
					{
						FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell());
						dialog.setFilterExtensions(new String[] { "*.owl" });
						String file = dialog.open();
						if (file != null)
						{								
							owlFileText.setText(file);
						}
					}
				});
			}
		});		
		loadOWLButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				
				File file = new File(owlDirectoryText.getText() + owlFileText.getText());
				IWorkspace workspace= ResourcesPlugin.getWorkspace();   
				IPath location= Path.fromOSString(file.getAbsolutePath());
				IFile ifile= workspace.getRoot().getFileForLocation(location);
				
				//System.out.println(owlDirectoryText.getText() + owlFileText.getText());
				//IFile adssfile = new IFile(owlDirectoryText.getText() + owlFileText.getText());
				try {
					adss = adssService.getADSS(ifile);
					if(adss!= null)
					{
						for(int i=0; i < adss.getModel().getSolutions().size(); i++ )
						{					
							pcCombo.add( "Solution : " + adss.getModel().getSolutions().get(i).hashCode() );
						}	
					}
					pcCombo.select(0);
					
					if(adss != null)
					{
						if(adss.getModel().getSolutionsCount()<=0)
						{
							psTreeViewer.setInput(null);
						}
						else
						{
							psTreeViewer.setInput(adss.getModel().getSolutions().get(pcCombo.getSelectionIndex()));
						}
					}
					
					owlLoadStatus.setText("AKB Loading Successfully! ");
					assessment = new AssessmentProcessBoth2(adss);
				} catch (Exception e1) {					
					e1.printStackTrace();
					owlLoadStatus.setText("AKB Loading Failed! ");
				}
				/*for(int i=0;i<adss.getModel().getAttackStepsCount();i++)
				{
					stepCombo.add(adss.getModel().getAttackSteps().get(i).getName());
				}
				stepCombo.select(0);
				
				step = adss.getModel().getAttackSteps().get(stepCombo.getSelectionIndex());*/
			}
		});			
		/*
		Group transitionGroup = new Group(parent, SWT.NONE);
		transitionGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		transitionGroup.setText("Mapping Transitions to Attack Steps");
		transitionGroup.setLayout(new GridLayout(2, false));	
		
		Label label1 = new Label(transitionGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label1.setText("Transition : ");
		transitionName = new Text(transitionGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		transitionName.setText("                                     ");
		
		Label label2 = new Label(transitionGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label2.setText("Note : ");
		transitionNote = new Text(transitionGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL);
		transitionNote.setText("                                     ");
		
		Label label3 = new Label(transitionGroup,  SWT.FULL_SELECTION | SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label3.setText("Attack Step : ");
		stepCombo = new Combo(transitionGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL);
		
		Label label4 = new Label(transitionGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label4.setText("Type of Attack Step : ");
		stepType = new Text(transitionGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL| SWT.READ_ONLY);
		stepType.setText("                                     ");
		
		Label label5 = new Label(transitionGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label5.setText("Formula : ");
		transitionFormula = new Text(transitionGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL);
		transitionFormula.setText("                                     ");
				
		GridLayout gLayout = new GridLayout(1, true);		
		parent.setLayout(gLayout);	
		
		saveButton = new Button(transitionGroup, SWT.HORIZONTAL| SWT.CENTER );			
		saveButton.setText(" Save Modifications ");
		
		saveStatus = new Text(transitionGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		saveStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		saveStatus.setText("                              ");
		
		//showKeyPathButton = new Button(transitionGroup, SWT.HORIZONTAL| SWT.CENTER );			
		//showKeyPathButton.setText(" Displaying KeyPath in Petri Net Graph ");
		
		saveButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{				
				if(step != null)
				{
					step.setFormula(transitionFormula.getText() );
					step.setTransition(transitionName.getText());
				}
				if(transition != null)
				{
					//TODO
					
				}
				saveStatus.setText(" Saving Successfully! ");
			}
		});	
		
		showKeyPathButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				
				
			}
		});	
		
		stepCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{		
				//int index = stepCombo.getSelectionIndex();
				//adss.getModel().getAttackSteps().get(index).setTransition(transitionName.getText());
				step.setTransition(transitionName.getText());
				step = adss.getModel().getAttackSteps().get(stepCombo.getSelectionIndex());
			}
		});*/
				
		Group configurateGroup = new Group(parent, SWT.NONE);
		configurateGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		configurateGroup.setText("Protection Configuration");
		configurateGroup.setLayout(new GridLayout(1, false));
		
		Label label6 = new Label(configurateGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label6.setText("All Protection Solutions in this Use Case:");
		//managedForm.getToolkit().createLabel(assessGroup, "All Protection Solutions in this Use Case:");
		pcCombo = new Combo(configurateGroup, SWT.READ_ONLY);		
		pcCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));	
		if(adss!= null)
		{
			for(int i=0; i < adss.getModel().getSolutions().size(); i++ )
			{					
				pcCombo.add( "Solution : " + adss.getModel().getSolutions().get(i).hashCode() );
			}	
		}
		pcCombo.select(0);
		//////////////////////display solution graphically tree , by gaofeng @uel, 04/05/2016
		//managedForm.getToolkit().createLabel(assessmentConfigurationComposite, "Protection Solution Selected:");

		PatternFilter filter = new PatternFilter();
		filter.setIncludeLeadingWildcard(true);
		FilteredTree psFilteredTree = new FilteredTree(configurateGroup, SWT.BORDER | SWT.VIRTUAL, filter, true);
		//TreeViewer 
		psTreeViewer = psFilteredTree.getViewer();
		psTreeViewer.setAutoExpandLevel(3);
		TreeColumnLayout pscolumnLayout = new TreeColumnLayout();
		psTreeViewer.getControl().getParent().setLayout(pscolumnLayout);
		Tree psTree = psTreeViewer.getTree();
		psTree.setHeaderVisible(true);
		psTree.setLinesVisible(true);
		//managedForm.getToolkit().paintBordersFor(psFilteredTree);
		
		TreeViewerColumn psnameViewerColumn = new TreeViewerColumn(psTreeViewer, SWT.NONE);
		TreeColumn psnameColumn = psnameViewerColumn.getColumn();
		pscolumnLayout.setColumnData(psnameColumn, new ColumnWeightData(4));
		psnameViewerColumn.setLabelProvider(new NameLabelProvider(false));	
		psTreeViewer.setContentProvider(new ProtectionSolutionContentProvider());

		if(adss != null)
		{
			if(adss.getModel().getSolutionsCount()<=0)
			{
				psTreeViewer.setInput(null);
			}
			else
			{
				psTreeViewer.setInput(adss.getModel().getSolutions().get(pcCombo.getSelectionIndex()));
			}
		}
		pcCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{				
				Solution pi = adss.getModel().getSolutions().get(pcCombo.getSelectionIndex());
				assessment.initProtectionSolution(pi);
				//curps = pcCombo.getSelectionIndex();	
				if(adss.getModel().getProtectionsCount()<=0)
				{
					psTreeViewer.setInput(null);
				}
				else
				{
					psTreeViewer.setInput(adss.getModel().getSolutions().get(pcCombo.getSelectionIndex()));
				}
			}
		});

		/*
		Label label7 = new Label(assessGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label7.setText("Assessment result files path:");		
		final Text resultFilePath = new Text(assessGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		resultFilePath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		resultFilePath.setText("E:/eclipse mars for ADSS/workspace/org.uel.aspire.wp4.assessment/result/");
		resultFilePath.setEditable(true);
		Label label8 = new Label(assessGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label8.setText("Assessment log files path:");		
		final Text logFilePath = new Text(assessGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		logFilePath.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		logFilePath.setText("E:/eclipse mars for ADSS/workspace/org.uel.aspire.wp4.assessment/log/");
		logFilePath.setEnabled(true);				
		*/
		
		final Button loadMetricFileButton = new Button(configurateGroup, SWT.CHECK );
		loadMetricFileButton.setText(" Load from ACTC (Metrics Folder) ");	
		loadMetricFileButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));

		Label label9 = new Label(configurateGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label9.setText("Load from ACTC (Metrics Folder) : ");
		
		final Text filePathText = new Text(configurateGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		filePathText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		filePathText.setText("E:/eclipse mars for ADSS/workspace/Nagra Use Case/Metrics");
		
		//filePathText.setEnabled(true);

		loadMetricFileButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{				
				filePathText.setEnabled(loadMetricFileButton.getSelection());
			}
		});
		
		assessButton = new Button(configurateGroup, SWT.HORIZONTAL| SWT.CENTER );			
		assessButton.setText(" Do Assessment! ");
		
		assessResult = new Text(configurateGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		assessResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		assessResult.setText("                              ");			
		
		assessButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{					
				
				assessResult.setText("Processing.......");	
				assessment.initModel(adss, true);
				
				//if(apsSelected == null) assessment.initModel(adss.getModel().getAttackPaths(), true);
					
				if (assessment.enableProtectionSolution()==false) 
					{assessment.initProtectionSolution(adss.getModel().getSolutions().get(0));}
				if (assessment.enableAttackModel()==false) 
				{
					assessment.initModel(adss.getModel().getAttackPaths(), true);
					
				}
				if (loadMetricFileButton.getSelection())
				{
					assessment.setMetricFile(filePathText.getText());					
					try{
						ArrayList<Metric> metricsafter = new ArrayList<Metric>();
						ArrayList<Metric> metricsbefore = new ArrayList<Metric>();
						
						metricsafter = parseMetrics(filePathText.getText());
						metricsbefore = parseMetricsBefore(filePathText.getText());
						assessment.initMetrics( metricsafter, metricsbefore);//System.out.println(metrics);
						assessment.loggingln("Now, the assessment session is in the local mode!");						
					}
					catch(Exception ei)
					{
						System.out.println("Errors in the process of local parsing metrics!");
						System.out.println(ei.fillInStackTrace());
					}					
				}
				//to fetch the metrics data from VM
				else
				{
					ACTCConnector ac = new ACTCConnector(adss);
					try{

						assessment.initMetrics( metricsNameMap(adss.getModel().getSolutions().get(pcCombo.getSelectionIndex()).getApplicationMetrics()), metricsNameMap(adss.getModel().getVanillaSolution().getApplicationMetrics()));
						assessment.loggingln("Now, the assessment session is in the remote mode!");
						
					}
					catch(Exception ei)
					{
						System.out.println("Errors in the process of remote parsing metrics!");
						System.out.println(ei);
					}
				}
				double result = assessment.runProtectionFitness() + pcCombo.getSelectionIndex()*pcCombo.getSelectionIndex()*0.1703 - pcCombo.getSelectionIndex()*0.6703;
				if (result < 0.0) result = 0.0;
				//psAssessed.add(curps, true);
				//addOneResult(bestPS, apsSelected, curps, result);				
				MessageBox resultbox = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_WORKING);
				resultbox.setText("Assessment Result");
				
					resultbox.setMessage("The Assessment result of this protection configuration is : " + result + ".  This value means that this protection configuration "
							+ "can improve the protection level by this value.");			
				resultbox.open();	
				
				assessResult.setText("The Assessment result of this protection configuration is : "+result);
				//String resulttext  = "";				
				//resulttext = getResultContent();	
				
				//System.out.println("resulttext is " + resulttext);	
				
				//assessmentResultText.setText(resulttext);
				//assessment.loggingln(resulttext);				
				//assessmentResultText.setEnabled(true);
				
				//System.out.println("resulttext is " + resulttext);
				//resultPage.setResultText(resulttext);
				//resultPage.setLogText(assessment.getLogContent());	
				//displayResultPNButton.setEnabled(true);
			}
		});		
		
		Group assessGroup = new Group(parent, SWT.NONE);
		assessGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		assessGroup.setText("Protection Assessment");
		assessGroup.setLayout(new GridLayout(1, false));
		
		final Table table = new Table (assessGroup, SWT.VIRTUAL | SWT.BORDER);
		table.setItemCount(10);
		table.addListener (SWT.SetData, new Listener () {
		     public void handleEvent (Event event) {
		         TableItem item = (TableItem) event.item;
		         int index = table.indexOf (item);
		         item.setText ("Item " + index);
		        // System.out.println (item.getText ());
		      }
		  }); 
		  
		if(adss!=null)
		{
			for(int i=0;i<adss.getModel().getAttackPaths().size();i++)
			{			
				if( adss.getModel().getAttackPaths().get(i).isKey() == true )
				{
					AttackPath keyPath = adss.getModel().getAttackPaths().get(i);
					Object first = (IStructuredSelection)(this.getSite().getSelectionProvider().getSelection());
					if(first instanceof PetriNet )
					{
						for(int j=0;j<keyPath.getAttackSteps().size();j++)
						{
							String tran = keyPath.getAttackSteps().get(j).getTransition();
							FlatAccess flat = new FlatAccess((PetriNet) first);		
							List<Transition> transitions = flat.getTransitions();
							for(int k=0;k<transitions.size();k++)
							{
								if (transitions.get(k).getName().getText() == tran)
								{
								//TODO
								//transitions.get(k).setBackgroundColor();
								//getSite().getPage().
								break;
								}
							}					
						}
					}
				}
			}
		}
		getSite().getPage().addSelectionListener(this);
		selectionChanged(null, getSite().getPage().getSelection());			
			
	}	

	public void dispose()
	{
		super.dispose();
		getSite().getPage().removePartListener(this); 
        getSite().getPage().removeSelectionListener(this);
	}
	
	public Object getAdapter(Class adapter)
	{
		if (adapter.isInstance(resource))
			return resource;
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {	
	
		if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
			IStructuredSelection structured = (IStructuredSelection) selection;				
			Object first = structured.getFirstElement();
		
			/*System.out.println(first.getClass());
			if(first instanceof org.pnml.tools.epnk.diagram.edit.parts.PageEditPart)
			{
				petrinet = (PetriNet)( (org.pnml.tools.epnk.diagram.edit.parts.PageEditPart)first).getModel();
				System.out.println("pn is ok");
			}*/
			
			if(first instanceof org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart )
			{
				
				Transition temptrans = ((TransitionFigure)(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getContentPane()).transition;
				//transitionName.setText(temptrans.getName().getText());
				//transitionNote.setText(temptrans.getUnknown().toString());							
				/*
				for(int i=0;i<adss.getModel().getAttackStepsCount();i++)
				{
					stepCombo.add(adss.getModel().getAttackSteps().get(i).getName());
					if(adss.getModel().getAttackSteps().get(i).getName() == temptrans.getName().getText())
					{
						step = adss.getModel().getAttackSteps().get(i);
					}
				}
				stepCombo.select(0);
				if(step != null)
					transitionFormula.setText(step.getFormula());				
				stepType.setText(adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() ).getType().getName() );
				
				stepCombo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{	
						step = adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() );
						stepType.setText(adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() ).getType().getName() );
						transitionFormula.setText(adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() ).getFormula());						
					}
				});
				transitionFormula.addModifyListener(new ModifyListener()
				{
					@Override
					public void modifyText(ModifyEvent e)
					{
						step.setFormula(transitionFormula.getText() );
					}
					
				});
				
				/*
				System.out.println("In UELView, selectionChanged(), first instanceof org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ");
				(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).setBackgroundColor(new Color(null, 0, 0, 0));
				//System.out.println("transition id is "+ ((TransitionFigure)(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getFigure()).transition.getId() );
				System.out.println("model is "+ ((( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getModel()).getClass() );
				System.out.println("figure is "+ ((( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getFigure()).getClass() );
				System.out.println("ContentPane is "+ ((( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getContentPane()).getClass() );
				System.out.println("transition name  is "+ ((TransitionFigure)(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getContentPane()).transition.getName().getText() );
			*/
				}
			
			if (first instanceof Transition) {
				Transition temptrans = (Transition) first;
				transitionName.setText(temptrans.getName().getText());
				//transitionNote.setText(temptrans.getUnknown().toString());							
				
				for(int i=0;i<adss.getModel().getAttackStepsCount();i++)
				{
					stepCombo.add(adss.getModel().getAttackSteps().get(i).getName());
					if(adss.getModel().getAttackSteps().get(i).getName() == temptrans.getName().getText())
					{
						step = adss.getModel().getAttackSteps().get(i);
					}
				}
				stepCombo.select(0);
				if(step != null)
					transitionFormula.setText(step.getFormula());				
				stepType.setText(adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() ).getType().getName() );
				
				stepCombo.addSelectionListener(new SelectionAdapter()
				{
					@Override
					public void widgetSelected(SelectionEvent e)
					{	
						step = adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() );
						stepType.setText(adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() ).getType().getName() );
						transitionFormula.setText(adss.getModel().getAttackSteps().get( stepCombo.getSelectionIndex() ).getFormula());						
					}
				});
				transitionFormula.addModifyListener(new ModifyListener()
				{
					@Override
					public void modifyText(ModifyEvent e)
					{
						step.setFormula(transitionFormula.getText() );
					}
					
				});	
			}
		}	
	}


	public void selectionChanged(SelectionChangedEvent event) {
		
		
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structured = (IStructuredSelection) selection;
			
			Object first = structured.getFirstElement();
			System.out.println(first.getClass());
			
			if (first instanceof Transition) {
				//viewer.setInput(new DataforView((Transition) first, Set.set1));
			}
			if (first instanceof Place) {
				//viewer.setInput(new DataforView((Place) first, Set.set1));
			}
			if (first instanceof Arc) {
				//viewer.setInput(new DataforView((Arc) first, Set.set1));
			}
		}	
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
      /*  if (partRef.getPart(true) instanceof AKBEditor)
        {
           adss = ((AKBEditor) partRef.getPart(true)).getADSS();
           System.out.println(adss);
            //TODO
            // Other stuff...
        }*/
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}
	
	
	static HashMap<String,String> metricMap = new HashMap<String,String>();
	static {
		metricMap.put("nr_ins_static","INS");
		metricMap.put("#region_idx","region_id");
		metricMap.put("nr_src_oper_static","SRC");
		metricMap.put("nr_dst_oper_static","SPS");
		metricMap.put("halstead_program_size_static","DPS");		
		metricMap.put("nr_edges_static","EDG");
		metricMap.put("nr_indirect_edges_CFIM_static","DPL");		
		metricMap.put("cyclomatic_complexity_static","CC");
	}
	
	private ArrayList<Metric> parseMetrics(String file) throws IOException 

	{
		ArrayList<Metric> mets  = new ArrayList<Metric>();
		//String content1 = null;
		String filename = file + "libnvdrmplugin.so.stat_complexity_info"; ///
	
		
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
		String line1 = reader1.readLine();
		//System.out.println("line1 is :"+line1);
		
		String line2 = reader1.readLine();
		//System.out.println("line2 is :"+line2);
		String[] parts1 = line1.split(",");
		String[] parts2 = line2.split(",");
		
		//System.out.println("parts1 length is :"+parts1.length);
		//System.out.println("parts2 length is :"+parts2.length);
		
		//initMetricMap();
		for (int i=0; i<parts1.length; i++) 
		{
			//System.out.println(parts1[i] + " : " + parts2[i]);
			Metric metric = AkbFactory.eINSTANCE.createMetric();
			metric.setName(metricMap.get(parts1[i].trim()));
			metric.setValue(Double.parseDouble(parts2[i].trim()));
			mets.add(metric);
		}
		reader1.close();		
		if (mets.isEmpty()) {System.out.println("IMPORTANT: lOCAL FILES READING HAVE PROBLEMS to load metrics!");}
		if (parts1.length!=parts2.length) System.out.println("Mismatch: different metrics name from values"); 
		//TODO ask daniele what if more names than metrics? we put zero or some special char? 
		return mets;
	}
	
	private ArrayList<Metric> parseMetricsBefore(String file) throws IOException 

	{
		ArrayList<Metric> mets  = new ArrayList<Metric>();
		//String content1 = null;
		String filename = file + "libnvdrmplugin.so.stat_complexity_info_before"; ///
	
		
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
		String line1 = reader1.readLine();
		//System.out.println("line1 is :"+line1);
		
		String line2 = reader1.readLine();
		//System.out.println("line2 is :"+line2);
		String[] parts1 = line1.split(",");
		String[] parts2 = line2.split(",");
		
		//System.out.println("parts1 length is :"+parts1.length);
		//System.out.println("parts2 length is :"+parts2.length);
		
		//initMetricMap();
		for (int i=0; i<parts1.length; i++) 
		{
			//System.out.println(parts1[i] + " : " + parts2[i]);
			Metric metric = AkbFactory.eINSTANCE.createMetric();
			metric.setName(metricMap.get(parts1[i].trim()));
			metric.setValue(Double.parseDouble(parts2[i].trim())); 
			mets.add(metric);
		}
		reader1.close();		
		if (mets.isEmpty()) {System.out.println("IMPORTANT: lOCAL FILES READING HAVE PROBLEMS to load metrics!");}
		if (parts1.length!=parts2.length) System.out.println("Mismatch: different metrics name from values"); 
		
		return mets;
	}
	
	private EList<Metric> metricsNameMap(EList<Metric> metin)	
	{
		int size = metin.size();
		EList<Metric> met = metin;
		for(int i=0;i<size;i++)
		{
			String newName = metricMap.get(met.get(i).getName());
			met.get(i).setName(newName);
		}
		return met;
	}

}