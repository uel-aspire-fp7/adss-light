package org.uel.aspire.wp4.formula;

import java.util.ArrayList;

/*this class is used to search all paths for one node to another nodes in a graph
 * by Gaofeng ZHANG, UEL, 07/07/2015
 */

public class GraphtoPaths {
	
		private int[][] graph;
		private boolean[] hasFlag;
		private ArrayList<String> res;
		
		public GraphtoPaths(ArrayList<String> relationin)
		{
			int m = relationin.size();
			graph  = new int[m][m];
			for(int i=0;i<m;i++)
			{
				String tem = relationin.get(i);
				for(int j=0;j<m;j++)
				{
					int loc = tem.indexOf("+");
					if(loc == 0) 
					{
						tem = tem.substring(1);
						loc = tem.indexOf("+");
					}
					if(loc != -1)
					{
						
						graph[i][j] = (int) Double.parseDouble(tem.substring(0, loc));
							
							//System.out.println(tem +  " " + loc + "  "+i + "  "+ j + "  "+ tem.substring(0, loc));
							//System.out.println(e);
						
						tem = tem.substring(loc+1);
					}
					else
					{
						graph[i][j] = (int) Double.parseDouble(tem);
					}
				}
			}
			 hasFlag = new boolean[m];
			//true-表示该结点已访问过。false-表示还没有访问过。
			res = new ArrayList<String>();
			 //最后的所有的路径的结果。每一条路径的格式是如：0->2->1->3:7
		}
		
		public ArrayList<String> getResults(int start, int end)
		{
			getPaths(start, end, "");
			return res;
		}
		
		//求在图graph上源点s到目标点d之间所有的简单路径，并求出路径的和。	
		void getPaths(int s,int d,String path)
		{
			hasFlag[s]=true;//源点已访问过. 
			for(int i=0;i< graph.length;i++)
			{
				if (graph[s][i]==0 || hasFlag[i])
				{continue;}				//若无路可通或已访问过，则找下一个结点。	
				if(i==d)//若已找到一条路径
				{ 
					res.add(path+"+"+d);//加入结果。
						continue;
				}
				getPaths(i, d, path+"+"+i);//继续找
				hasFlag[i]=false;	
			}//for(i)
		}
}
