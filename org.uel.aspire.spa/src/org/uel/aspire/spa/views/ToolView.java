package org.uel.aspire.spa.views;

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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.*;
import org.pnml.tools.epnk.gmf.extensions.graphics.figures.TransitionFigure;
import org.pnml.tools.epnk.helpers.FlatAccess;
import org.pnml.tools.epnk.pnmlcoremodel.Arc;
import org.pnml.tools.epnk.pnmlcoremodel.PetriNet;
import org.pnml.tools.epnk.pnmlcoremodel.Place;
import org.pnml.tools.epnk.pnmlcoremodel.Transition;
import org.uel.aspire.spa.NewWizard;
import org.uel.aspire.wp4.assessment.APIs.AssessmentProcessBoth2;
import org.uel.aspire.wp4.data.AnnotationTransitionMap;
import org.uel.aspire.wp4.data.Annotations;

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
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

public class ToolView extends ViewPart implements ISelectionListener, ISelectionChangedListener, IPartListener2{

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "org.uel.aspire.spa.views.ToolView";	
	
	private IShowInSource resource;
	private TransitionViewLabelProvider labelprovider;
	private TransitionViewContentProvider contentprovider;
	
	private TreeViewer psTreeViewer;
	
	private ADSS adss;
	private ADSSService adssService;
	
	private Annotations annotations;	
	private AnnotationTransitionMap annotationtransitionmap;
	
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
	//private Button assessNormalisedButton;
	private Button showKeyPathButton;
	private AttackStep step = null;
	private Transition transition = null;
	
	private Table table;
	private ArrayList<AssessData> tableData;

	//private Combo annotationCombo;
 
	private Text owlDirectoryText;
	private Text owlFileText;
	private Text owlLoadStatus;
	private Text saveStatus;
	private Text transitionSelectedText;
	
	private Text annotationFileText;
	private Text annotationLoadStatus;
	private Text annotationSelectedText;
	private Text allAnnotationText;
	
	private Text metricsPathText; 
	private Text vanilaMetricsPathText;
	
	private Text assessResult;
		
	public ToolView()
	{
		super();
		labelprovider = new TransitionViewLabelProvider();
		contentprovider = new TransitionViewContentProvider();	
				
		//adss = ADSS.getInstance();
		tableData = new ArrayList<AssessData>();
		annotationtransitionmap = new AnnotationTransitionMap();
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
		akbGroup.setText("Files Loading ");
		akbGroup.setLayout(new GridLayout(3, false));		
		Label owllabel1 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		owllabel1.setText("OWL Directory : ");	
		owlDirectoryText = new Text(akbGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL |SWT.READ_ONLY);
		owlDirectoryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		//String temppath = NewWizard.getProjectPath();
		//owlDirectoryText.setText(temppath);	
		owlDirectoryText.setText("E:/eclipse mars for ADSS/workspace/Nagra Use Case/AKB/");		
		Button owlDirectoryButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );
		owlDirectoryButton.setText("Browse...");		
		Label owllabel2 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		owllabel2.setText("OWL File : ");			
		owlFileText = new Text(akbGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		owlFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		owlFileText.setText("akb-light.owl");		
		Button owlFileButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );	
		owlFileButton.setText("Browse...");
		Button loadOWLButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );	
		loadOWLButton.setText("Load OWL File");
		
		owlLoadStatus = new Text(akbGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		owlLoadStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		owlLoadStatus.setText("                              ");	
		
		Label annotationblank1 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		annotationblank1.setText("  ");	
		
		Label annotationlabel1 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		annotationlabel1.setText("Annotation File: ");	
		annotationFileText = new Text(akbGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		annotationFileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		annotationFileText.setText("C:/Users/Gaofeng/Desktop/ASPIRE CODE/development/RA/AnnotationInterpreter/annotations.json");		
		Button annotationFileButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );
		annotationFileButton.setText("Browse...");	
		
		Button loadannotationFileButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );	
		loadannotationFileButton.setText("Load Annotation File");		
		annotationLoadStatus = new Text(akbGroup, SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		annotationLoadStatus.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		annotationLoadStatus.setText("                              ");			
		Label annotationblank2 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		annotationblank2.setText("  ");	
		
		Label transitionSelectedLabel = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		transitionSelectedLabel.setText("Transition Selected: ");	
		transitionSelectedText = new Text(akbGroup, SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL | SWT.READ_ONLY);
		transitionSelectedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		transitionSelectedText.setText(" ");			
		Label annotationblank3 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		annotationblank3.setText("  ");	
		
		Label annotationlabel2 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		annotationlabel2.setText("Associated Annotations :");
		/*
		annotationCombo = new Combo(akbGroup, SWT.READ_ONLY);		
		annotationCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		annotationCombo.select(0);
		*/
		annotationSelectedText = new Text(akbGroup, SWT.BORDER |SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		annotationSelectedText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));		
		annotationSelectedText.setText(" ");		
		Button saveAnnotationsButton = new Button(akbGroup, SWT.HORIZONTAL| SWT.CENTER );	
		saveAnnotationsButton.setText("Save Annotations");		
		
		Label annotationlabel3 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		annotationlabel3.setText("All Annotations :");
		allAnnotationText = new Text(akbGroup, SWT.BORDER | SWT.HORIZONTAL| SWT.VERTICAL| SWT.V_SCROLL| SWT.H_SCROLL| SWT.MULTI | SWT.READ_ONLY);
		allAnnotationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));		
		//allAnnotationText.setText("                              ");	
		/*Label label21 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label21.setText("");	*/
		
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
						
						//table.setItemCount(adss.getModel().getSolutionsCount());
						//table.redraw();
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
		
		annotationFileButton.addSelectionListener(new SelectionAdapter()
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
						dialog.setFilterExtensions(new String[] { "*.json" });
						String file = dialog.open();
						if (file != null)
						{								
							annotationFileText.setText(file);
						}
					}
				});
			}
		});		
		loadannotationFileButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{	
				try {
					annotations = new Annotations(annotationFileText.getText());
					annotationLoadStatus.setText("Annotation Loading Successfully!");
					String temp = "";
					for(int i= 0;i<annotations.getAnnotationsSize();i++)
					{
						//annotationCombo.add(annotations.getAnnotation(i).getFunctionName());
						temp = temp + "A" +i + " : "+ annotations.getAnnotation(i).getContent() + "; \r\n";
					}
					//annotationCombo.select(0);	*/
					allAnnotationText.setText(temp);

				} catch (Exception e1) {					
					e1.printStackTrace();
					annotationLoadStatus.setText("Annotation Loading Failed! ");
				}				
			}
		});	
		
		saveAnnotationsButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{	
				try {					
					String newas = annotationSelectedText.getText();
					if(transition != null)
						annotationtransitionmap.addMap(transition.getName().getText(), newas);
				} catch (Exception e1) {					
					e1.printStackTrace();
					annotationLoadStatus.setText("Annotations Saving Failed! ");
				}				
			}
		});	
		
		annotationSelectedText.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e)
			{				
				String newas = annotationSelectedText.getText();
				if(transition != null)
					annotationtransitionmap.addMap(transition.getName().getText(), newas);
			}
		});
		/*annotationCombo.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{	
				
				//annotationCombo.getSelectionIndex()
				
			}
		});*/
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
		configurateGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
		/*Label label61 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label61.setText("");*/
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
/*
		Label label62 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label62.setText("");
		Label label63 = new Label(akbGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label63.setText("");*/
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
		loadMetricFileButton.setSelection(false);

		Label label9 = new Label(configurateGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label9.setText("Load from ACTC (Metrics Folder) : ");
		
		metricsPathText = new Text(configurateGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		metricsPathText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		metricsPathText.setText("E:/eclipse mars for ADSS/workspace/Nagra Use Case/Metrics");
		metricsPathText.setEnabled(false);
		//filePathText.setEnabled(true);

		loadMetricFileButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{				
				metricsPathText.setEnabled(loadMetricFileButton.getSelection());
			}
		});
		
		final Button normalWeightButton = new Button(configurateGroup, SWT.CHECK );
		normalWeightButton.setText(" Normalise the Weights of Transition Formulas ");	
		normalWeightButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		normalWeightButton.setEnabled(false);
		
		Label label10 = new Label(configurateGroup,  SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.CENTER |  SWT.FILL);
		label10.setText("Load from ACTC (Vanilla Metrics Folder) : ");		
		vanilaMetricsPathText = new Text(configurateGroup,SWT.BORDER | SWT.FULL_SELECTION |SWT.HORIZONTAL| SWT.LEFT |  SWT.FILL );
		vanilaMetricsPathText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		vanilaMetricsPathText.setText("E:/eclipse mars for ADSS/workspace/Nagra Use Case/Metrics");
		vanilaMetricsPathText.setEnabled(false);
		//filePathText.setEnabled(true);

		loadMetricFileButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{				
				metricsPathText.setEnabled(normalWeightButton.getSelection());
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
					assessment.setMetricFile(metricsPathText.getText());					
					try{
						ArrayList<Metric> metricsafter = new ArrayList<Metric>();
						ArrayList<Metric> metricsbefore = new ArrayList<Metric>();
						//TODO
						//LoadMetricsFiles lmfs = new LoadMetricsFiles( adss.getModel().getSolutions().get(pcCombo.getSelectionIndex()).toString(), metricsPathText.getText());
						//metricsafter = convertAfter(lmfs.getMetricsData());//
						metricsafter =  parseMetrics(metricsPathText.getText());
						//metricsbefore = convertBefore(lmfs.getMetricsData()); //
						metricsbefore = parseMetricsBefore(vanilaMetricsPathText.getText());
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
				double result;
				if(normalWeightButton.getSelection() == false)
				{
					result = assessment.runProtectionFitness();
					//+ pcCombo.getSelectionIndex()*pcCombo.getSelectionIndex()*0.1703 - pcCombo.getSelectionIndex()*0.6703;
				}
				else
				{
					result = assessment.runProtectionFitnessNormalised();
					//+ pcCombo.getSelectionIndex()*pcCombo.getSelectionIndex()*0.1503 - pcCombo.getSelectionIndex()*0.7703;
				}
				if (result < 0.0) result = 0.0;
				AssessData ad = new AssessData();
				ad.setResult(result);
				ad.setSolution(adss.getModel().getSolutions().get( pcCombo.getSelectionIndex() ) );
				//ad.setKeyPath(assessment.getKeypath());
				//ad.setKeyPath();
				//TODO
				tableData.add(ad);
				displayAssessmentTable(ad);
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
		assessGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		assessGroup.setText("Protection Assessment");
		assessGroup.setLayout(new GridLayout(1, false));
		
		//final Table 
		table = new Table(assessGroup,  SWT.VIRTUAL| SWT.BORDER |  SWT.MULTI | SWT.V_SCROLL
		        | SWT.H_SCROLL);
		//table.setItemCount(10);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL , true, true) );
		table.setLayout(new GridLayout(3, false));
		TableColumn tc1 = new TableColumn(table, SWT.CENTER);
		TableColumn tc2 = new TableColumn(table, SWT.CENTER);
		TableColumn tc3 = new TableColumn(table, SWT.CENTER);
		//TableColumn tc4 = new TableColumn(table, SWT.CENTER);
		tc1.setText(" Protection Solution: ");
	    tc2.setText(" Fitness Level: ");
	    tc3.setText(" Key Attack Path: ");
	    //tc4.setText(" Petri Net ");
	    tc1.setWidth(130);
	    tc2.setWidth(90);
	    tc3.setWidth(270);
	    //tc4.setWidth(60);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    Listener sortListener = new Listener() {
	        public void handleEvent(Event e) {
	          TableItem[] items = table.getItems();
	          Collator collator = Collator.getInstance(Locale.getDefault());
	          TableColumn column = (TableColumn) e.widget;
	          int index = 1;
	          for (int i = 1; i < items.length; i++) 
	          {
	            String value1 = items[i].getText(index);
	            for (int j = 0; j < i; j++) {
	              String value2 = items[j].getText(index);
	              if (collator.compare(value1, value2) > 0) 
	              {
	                String[] values = { items[i].getText(0), items[i].getText(1), items[i].getText(2), items[i].getText(3) };
	                items[i].dispose();
	                TableItem item = new TableItem(table, SWT.NONE, j);
	                item.setText(values);
	                items = table.getItems();
	                break;
	              }
	            }
	          }
	          table.setSortColumn(column);
	        }
	      };
	      tc2.addListener(SWT.Selection, sortListener);
	      //column2.addListener(SWT.Selection, sortListener);
	      table.setSortColumn(tc2);
	      
	      table.setSortDirection(SWT.DOWN);
	    //displayAssessmentTable(tableData);
		//table.setSortColumn(tc);
		/*table.addListener (SWT.SetData, new Listener () {
		     public void handleEvent (Event event) {
		         TableItem item = (TableItem) event.item;
		         int index = table.indexOf (item);
		         item.setText("Solution : "+ adss.getModel().getSolutions().get(index).hashCode());
		         if(!tableData.isEmpty())
		        	 item.setData(tableData.get(index));
		        // System.out.println (item.getText ());
		         table.redraw();
		      }
		  }); 
		table.setRedraw(true);*/
		
		  
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

	protected ArrayList<Metric> convertBefore(MetricsData metricsData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private ArrayList<Metric> convertAfter(MetricsData metricsData) {
		// TODO Auto-generated method stub
		return null;
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
			
			if(first instanceof org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart )
			{				
				transition = ((TransitionFigure)(( org.pnml.tools.epnk.diagram.edit.parts.TransitionEditPart ) first).getContentPane()).transition;
				//System.out.println(temptrans);
				transitionSelectedText.setText(transition.getName().getText());
				if(annotationtransitionmap.getMap(transition.getName().getText()) != null)
					annotationSelectedText.setText(annotationtransitionmap.getMap(transition.getName().getText()));
				else
					annotationSelectedText.setText("");
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
				transition = (Transition) first;
				//transitionSelectedText.setText(temptrans.getName().getText());
				transitionSelectedText.setText(transition.getName().getText());
				if(annotationtransitionmap.getMap(transition.getName().getText()) != null)
					annotationSelectedText.setText(annotationtransitionmap.getMap(transition.getName().getText()));
				else
					annotationSelectedText.setText("");
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
					
				});	*/
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
		
		File[] files = new File(file).listFiles();
	
		String filename= "";
		for (File file1 : files) {
		    if (file1.isFile()) {
		    	if(file1.getName().indexOf(".so.stat_complexity_info") != -1) { filename = file + file1.getName(); break;}
		    	//if(file1.getName().indexOf(".so.stat_regions_complexity_info") != -1) {md.setRMetrics(parseCMetrics(filesPath+file.getName()));}		        
		    }
		}	
		if(filename == "") return null;
		//String filename = file + "libnvdrmplugin.so.stat_complexity_info"; ///
	
		
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
	
	private ArrayList<Metric> parseMetricsBefore(String file) throws IOException 

	{
		ArrayList<Metric> mets  = new ArrayList<Metric>();
		//String content1 = null;
		File[] files = new File(file).listFiles();
		
		String filename= "";
		for (File file1 : files) {
		    if (file1.isFile()) {
		    	if(file1.getName().indexOf(".so.stat_complexity_info") != -1) { filename = file + file1.getName(); break;}
		    	//if(file1.getName().indexOf(".so.stat_regions_complexity_info") != -1) {md.setRMetrics(parseCMetrics(filesPath+file.getName()));}		        
		    }
		}	
		if(filename == "") return null;
		//String filename = file + "libnvdrmplugin.so.stat_complexity_info"; ///
	
		
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
	
	/*private boolean displayAssessmentTable(ArrayList<AssessData> tableData)
	{		
		int size = tableData.size();
		if(size < 1) return true;
		ArrayList<TableItem> items = new ArrayList<TableItem>();
		for(int i=0;i<size;i++)
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String[] { "Solution "+ tableData.get(i).getSolution().hashCode(), "" + tableData.get(i).getResult(),
					 tableData.get(i).getKeyPathString() });
			items.add(item);
		}
		
		return true;
	}*/
	private boolean displayAssessmentTable(AssessData Data)
	{		
		//int size = tableData.size();
		//if(size < 1) return true;
		//ArrayList<TableItem> items = new ArrayList<TableItem>();
		//for(int i=0;i<size;i++)
		//{
			TableItem item = new TableItem(table, SWT.NONE );
			item.setText(new String[] { "Solution-"+ Data.getSolution().hashCode(), "" + Data.getResult(),
					Data.getKeyPathString() });
			//Button bu = new Button(table, SWT.HORIZONTAL| SWT.CENTER );
			//item.setData("Petri Net", bu);
			
			//items.add(item);			
		//}
		
		return true;
	}

}