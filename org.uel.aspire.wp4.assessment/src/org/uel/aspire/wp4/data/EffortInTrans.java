package org.uel.aspire.wp4.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class EffortInTrans {
	 double effort;
	 
		ArrayList<Double> efforts; //TODO
		//HashMap<String, Double> effortsmap;
		int size;
		
		public EffortInTrans()
		{
			size = 5;
			efforts = new ArrayList<Double>();			
		}
		
		public double geteffort()
		{
			return effort;	
		}
		public void seteffort(double in)
		{
			effort = in;	
		}
		
		public ArrayList<Double> getefforts()
		{
			return efforts;	
		}
		public void setefforts(ArrayList<Double> in)
		{
			efforts = in;	
			this.size = in.size();
		}
		
		public int getsize()
		{
			return size;
		}
		
		static public EffortInTrans addtwo( EffortInTrans a , EffortInTrans b)
		{
			EffortInTrans result = new EffortInTrans();
			if (a.efforts.size() == b.efforts.size() )
			{
				for(int i = 0 ; i < a.efforts.size(); i++)
				{
					Double temp = a.efforts.get(i) + b.efforts.get(i);
					result.efforts.add(temp);
				}
				return result;
			}
			return null;
		}

		static public boolean compare(EffortInTrans a , EffortInTrans b)// it is for PN based comparison. 
		{
			boolean result = true;
			int temp = 0;
			for(int i=0;i<a.efforts.size();i++)
			{
				if(a.efforts.get(i)>=b.efforts.get(i))
					temp++;
				else
					temp--;
			}
			if (temp >= 0) 
				result = true;
			else
				result = false;
			return result;
		}

}
