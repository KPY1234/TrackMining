package core;

import core.CallTimesMatrix;

import java.util.HashMap;



public class TrackPassMatrix extends HashMap<Integer, HashMap<Integer, Integer>>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int X_dir = 0;
	private int Y_dir = 0;
	
	
	public int compareDifference(TrackPassMatrix tpm){
		
		int diff=0;
		
		Object[] key_X = super.keySet().toArray();
		
		for(int i=0;i<key_X.length;i++){
			
		
			
			Object[] key_Y = get(key_X[i]).keySet().toArray();
			
			for(int j=0;j<key_Y.length;j++){
				
				
				
				
			}
			
			
		}
		
		
		
		return diff;
	}
	
	public CallTimesMatrix getDefaultCallTimesMatrix(){
		
		CallTimesMatrix  ctm = new CallTimesMatrix();
		
		Object[] key_X = super.keySet().toArray();
		
		for(int i=0;i<key_X.length;i++){
			
			
			HashMap<Integer,Integer> hm = new HashMap<Integer,Integer>();
			
			Object[] key_Y = get(key_X[i]).keySet().toArray();
			
			for(int j=0;j<key_Y.length;j++){
				
				hm.put((int)key_Y[j], get(key_X[i]).get(key_Y[j])-1);
				
				
			}
			
			ctm.put((int)key_X[i], hm);
			
		}
		
		return ctm;
	}
	
	public void bubbleSort(int[] array){
		
		for(int i=0;i<array.length-1;i++){
			for(int j=0;j<array.length-1;j++){
				if(array[j]>array[j+1]){
					int tem=array[j+1];
					array[j+1]=array[j];
					array[j]=tem;
				}
			}
		}
	}
	
	public void setX_direction(int dir){
		X_dir = dir;
	}
	
	public void setY_direction(int dir){
		Y_dir = dir;
	}
	
	public int getX_direction(){
		return X_dir;
	}
	
	public int getY_direction(){
		return Y_dir;
	}
	
	public int getValueSize(){
		
		int count=0;
		
		Object[] key_X = super.keySet().toArray();
		
		for(int i=0;i<key_X.length;i++){
			
			Object[] key_Y = get(key_X[i]).keySet().toArray();
			
			for(int j=0;j<key_Y.length;j++)
				count++;
			
		}
		
		return count;
		
	}
	
	public String toString(){
		
		String str="{";
		
		Object[] key_X = super.keySet().toArray();
		
		int[] Int_key_X = new int[key_X.length];
		
		for(int i=0;i<Int_key_X.length;i++)
			Int_key_X[i]=(int)key_X[i];
		
		bubbleSort(Int_key_X);
		
		for(int i=0;i<Int_key_X.length;i++){
			
			str+=Int_key_X[i]+"=";
		
			Object[] key_Y = get(Int_key_X[i]).keySet().toArray();
			
			int[] Int_key_Y = new int[key_Y.length];
			
			for(int j=0;j<Int_key_Y.length;j++)
				Int_key_Y[j]=(int)key_Y[j];
			
			bubbleSort(Int_key_Y);
			
			str+="{";
			
			for(int j=0;j<Int_key_Y.length;j++){
				
				str+=Int_key_Y[j]+"=";
				str+=get(Int_key_X[i]).get(Int_key_Y[j]);
				if(j!=Int_key_Y.length-1)
					str+=",";
				
			}
			str+="}";
			
			if(i!=Int_key_X.length-1)
				str+=",";
			
		}
		
		str+="}\n";
		return str;
	}
	
	
	

	public static void main(String args[]){
		
		
		
		
		
		
	}
	
	
}
