package libsvm;
public class svm_node implements java.io.Serializable
{
	public int index;
	public double value;
	
	public String toString(){
		
		String str="";
		
		str=index+": "+value;
		
		
		return str;
		
	}
}
