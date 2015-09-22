//
// svm_model
//
package libsvm;
public class svm_model implements java.io.Serializable
{
	
	boolean normalize;
	double maxValue;
	double minValue;
	
	public svm_parameter param;	// parameter
	public int nr_class;		// number of classes, = 2 in regression/one class svm
	public int l;			// total #SV
	public svm_node[][] SV;	// SVs (SV[l])
	public double[][] sv_coef;	// coefficients for SVs in decision functions (sv_coef[k-1][l])
	public double[] rho;		// constants in decision functions (rho[k*(k-1)/2])
	public double[] probA;         // pariwise probability information
	public double[] probB;
	public int[] sv_indices;       // sv_indices[0,...,nSV-1] are values in [1,...,num_traning_data] to indicate SVs in the training set

	// for classification only

	public int[] label;		// label of each class (label[k])
	public int[] nSV;		// number of SVs for each class (nSV[k])
				// nSV[0] + nSV[1] + ... + nSV[k-1] = l
	
	
	public String toString(){
		
		String tem="";
		
		for(int i=0;i<SV.length;i++){
			for(int j=0;j<SV[i].length;j++){
				
				tem+=SV[i][j].value+" ";
				
			}
			tem+="\n";
		}
		
		
		
		
		return tem;
	}
	
	
}
