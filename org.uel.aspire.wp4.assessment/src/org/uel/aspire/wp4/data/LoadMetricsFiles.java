package org.uel.aspire.wp4.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.uel.aspire.wp4.assessment.APIs.Metrics;



public class LoadMetricsFiles {
	
	String filesPath = "";
	String pcName = "";
	
	public LoadMetricsFiles(String filesin, String pcnamein)
	{
		filesPath = filesin;
		pcName = pcnamein;	
	}
	
	public LoadMetricsFiles(String filesin)
	{
		filesPath = filesin;
		//pcName = pcnamein;		
	}
	
	public ArrayList<MetricsData> getMetricsDatas() throws IOException
	{			
		ArrayList<MetricsData> mds = new ArrayList<MetricsData>();		
		
		File[] files = new File(filesPath).listFiles();
	
		//System.out.println(filesPath);
		
		for (File file : files) 
		{
			if(file.isDirectory())
			{
				String name = file.getName();
				MetricsData md = new MetricsData(name);
				File[] files1 = new File( filesPath + File.separator + name ).listFiles();
				
				//System.out.println(filesPath+File.separator+name);
				
				for(File file1 : files1)
				{		   
					if (file1.isFile()) 
					{
						if(file1.getName().indexOf(".so.stat_complexity_info") != -1) 
						{
							md.setGMetrics(parseGMetrics(filesPath+File.separator+file.getName() + File.separator+file1.getName()));
						}
						
						if(file1.getName().indexOf(".so.stat_regions_complexity_info") != -1) 
						{
							md.setRMetrics(parseCMetrics(filesPath+File.separator+file.getName() + File.separator+file1.getName()));
						}
						//System.out.println(filesPath+File.separator+file.getName() + File.separator+file1.getName());
					}					
				}
				//System.out.println(md);
				mds.add(md);
			}
		}		
		return mds;		
	}
	
	public MetricsData getMetricsData() throws IOException
	{		
		MetricsData md = new MetricsData(pcName);	
		
		File[] files = new File(filesPath).listFiles();
	
		for (File file : files) {
		    if (file.isFile()) {
		    	if(file.getName().indexOf(".so.stat_complexity_info") != -1) {md.setGMetrics(parseGMetrics(filesPath+File.separator+file.getName()));}
		    	if(file.getName().indexOf(".so.stat_regions_complexity_info") != -1) {md.setRMetrics(parseCMetrics(filesPath+File.separator+file.getName()));}		        
		    }
		}		
		return md;		
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
	
	private Metrics parseGMetrics(String file) throws IOException 
	{
		Metrics mets  = new Metrics();
		//String content1 = null;
		String filename = file;// + "libnvdrmplugin.so.stat_complexity_info"; ///		
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
		String line1 = reader1.readLine();		
		String line2 = reader1.readLine();
		
		String[] parts1 = line1.split(",");
		String[] parts2 = line2.split(",");
		
		//System.out.println("parts1 length is :"+parts1.length);
		//System.out.println("parts2 length is :"+parts2.length);
		
		//initMetricMap();
		for (int i=0; i<parts1.length; i++) 
		{
			//System.out.println(parts1[i] + " : " + parts2[i]);
			//Measure mea =new Measure();
			//mea.setID(metricMap.get(parts1[i].trim()));
			//mea.setValue(Double.parseDouble(parts2[i].trim()));
			mets.addmetrics(metricMap.get(parts1[i].trim()), Double.parseDouble(parts2[i].trim()));
		}
		reader1.close();		
		if (mets.isEmpty()) {System.out.println("IMPORTANT: lOCAL FILES READING HAVE PROBLEMS to load metrics!");}
		if (parts1.length!=parts2.length) System.out.println("Mismatch: different metrics name from values"); 
		return mets;
	}

	private HashMap<String, Metrics> parseCMetrics(String file) throws IOException 
	{
		HashMap<String, Metrics> smets = new HashMap<String, Metrics>();
		//String content1 = null;
		String filename = file ;//+ "libnvdrmplugin.so.stat_complexity_info"; ///		
		BufferedReader reader1 = new BufferedReader(new FileReader(filename));
		String line1 = reader1.readLine();
		String[] parts1 = line1.split(",");
		while(true)
		{
			Metrics mets  = new Metrics();
			String line2 = reader1.readLine();
			if(line2 == null) break;
			String region = "";
		
			String[] parts2 = line2.split(",");
		
		//System.out.println("parts1 length is :"+parts1.length);
		//System.out.println("parts2 length is :"+parts2.length);
		
		//initMetricMap();
			for (int i=0; i<parts1.length; i++) 
			{
			//System.out.println(parts1[i] + " : " + parts2[i]);
			//Measure mea =new Measure();
			//mea.setID(metricMap.get(parts1[i].trim()));
			//mea.setValue(Double.parseDouble(parts2[i].trim()));
				mets.addmetrics(metricMap.get(parts1[i].trim()), Double.parseDouble(parts2[i].trim()));
				if(parts1[i].trim() == "#region_idx") region = parts2[i].trim();
			}
			smets.put(region, mets);
		}
		reader1.close();		
		if (smets.isEmpty()) {System.out.println("IMPORTANT: lOCAL FILES READING HAVE PROBLEMS to load metrics!");}
		//if (parts1.length!=parts2.length) System.out.println("Mismatch: different metrics name from values"); 
		return smets;
	}
}
