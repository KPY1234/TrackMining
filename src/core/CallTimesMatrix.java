package core;

import java.util.HashMap;

public class CallTimesMatrix extends HashMap<Integer, HashMap<Integer, Integer>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
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
	
	
	
	

}
