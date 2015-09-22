package libsvm;
public class svm_problem implements java.io.Serializable
{
	public int l;
	public double[] y;
	public svm_node[][] x;
	
	public String toString(){
		
		String str="";
		
		str+="length of problem is:"+l+"\n";
		
		for(int i=0;i<l;i++){
			
			str+=y[i]+"\t";
			
			for(int j=0;j<x[i].length;j++){
				
				str+=x[i][j].toString()+"   ";
				
			}
			
			str+="\n";
		}
			
			
		
		return str;
	}
}
