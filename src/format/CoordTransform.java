package format;

public class CoordTransform {
	
	final static double PI = 3.1415926;
	
	final static double A = 6378160.0;
	final static double B = 6356774.7192;// 6356774.7192;
	
	final static double ICLDD = 123;	// 2 degree = 121, 6 degree = 123
	final static double ICLDM = 0;
	final static double CLDS = 0.0;
	final static double SF = 0.9996;	// 2 degree = 0.9999, 6 degree = 0.9996
	final static double XO = 500000.0;	// 2 degree = 250000, 6 degree = 500000
	
	
	final static double Pi = (Math.atan(1.)/162000.);
	final static double Pid = (Math.atan(1.)/45.);
	final static double E = (Math.sqrt((A*A-B*B)/(A*A)));
	final static double E2=(A*A-B*B)/(A*A);
	final static double E4=E2*E2;
	final static double E6=E4*E2;
	final static double E8=E6*E2;
	final static double A0=1.0-E2/4.-3.*E4/64.-5.*E6/256.-175.*E8/16384.;
	final static double A2=3./8.*(E2+E4/4.+15.*E6/128.-455.*E8/4096.);
	final static double A4=15./256.*(E4+3.*E6/4.-77.*E8/128.);
	final static double A6=35./3072.*(E6-41.*E8/32.);
	final static double A8=-315.*E8/131072.;
	
	
	public CoordTransform(){
		
	}
	
	
	
	public static double[] XYToLonLat(double cor_x, double cor_y){
		
		
		double lond, latd;
		
		double[] LonAndLat = new double[2];
		
		double phi, phi1, cmrad, dlam, olam;
		double sp, cp, t, eta, dn, dm, dm1, dm2, f, fp;
		double x,y;
		
		cmrad=(ICLDD*3600.+ICLDM*60.+CLDS)*Pi;
		
		x=(cor_x-XO)/SF;
		y=cor_y/SF;
		phi1=y/A;
		
		do{
			f = A*(A0*phi1-A2*Math.sin(2.*phi1)+A4*Math.sin(4.*phi1)-A6*Math.sin(6.*phi1)+
						A8*Math.sin(8.*phi1))-y;
			fp= A*(A0-2.*A2*Math.cos(2.*phi1)+4.*A4*Math.cos(4.*phi1)-6.*A6*Math.cos(6.*phi1)+
						A8*Math.cos(8.*phi1));
			phi1 = phi1 - f/fp;
		} while (Math.abs(f-fp) < 1.e-11);
		sp=Math.sin(phi1);
		cp=Math.cos(phi1);
		t =Math.tan(phi1);
		eta=Math.sqrt((A*A-B*B)/(B*B*cp*cp));
		dn=A/Math.sqrt(1.0-E*E*sp*sp);
		dm1=(1.-E*E*sp*sp);
		dm2=dm1*dm1*dm1;
		dm=A*(1.-E*E)/Math.sqrt(dm2);
		phi=phi1-t*x*x/2.0/dm/dn+t*Math.pow(x,4)/24.0/dm/Math.pow(dm,3)*(5.0+3.0*
				t*t+eta*eta-4.0*Math.pow(eta,4)-9.0*eta*eta*t*t)-t*Math.pow(x,6)/720.0/dm/
				Math.pow(dn,5)*(61.0+90.0*t*t+46.0*eta*eta+45.0*Math.pow(t,4)-252.0*t*t*
				eta*eta-3.0*Math.pow(eta,4)+100.0*Math.pow(eta,6)-66.0*t*t*Math.pow(eta,4)-90.0*
				Math.pow(t,4)*eta*eta+88.0*Math.pow(eta,8)+225.0*Math.pow(t,4)*Math.pow(eta,4)+84.0*t*t*
				Math.pow(eta,6)-192.0*t*t*Math.pow(eta,8));
		phi=phi+t*Math.pow(x,8)/40320.0/dm/Math.pow(dn,7)*(1385.0+3633.0*t*t+4095.0*Math.pow(t,4)+
				1575.0*Math.pow(t,6));
		dlam=(x/dn-Math.pow((x/dn),3)/6.0*(1.0+2.0*t*t+eta*eta)+Math.pow((x/dn),5)/
				120.0*(5.0+6.0*eta*eta+28.0*t*t-3.0*Math.pow(eta,4)+8.0*t*t*
				eta*eta+24.0*Math.pow(t,4)-4.0*Math.pow(eta,6)+4.0*t*t*Math.pow(eta,4)+24.0*t*t*
				Math.pow(eta,6))-Math.pow((x/dn),7)/5040.0*(61.0+662.0*t*t+1320.0*Math.pow(t,4)+
				720.0*Math.pow(t,6)))/cp;
		olam=cmrad+dlam;
		
		
		
		lond = olam/Pid;
		latd = phi/Pid;
		
		
		//System.out.println("Longitude: "+lond+"\tLatitude: "+latd);
		
		 LonAndLat[0]=getCarry(lond,3);
		 LonAndLat[1]=getCarry(latd,3);
		
		return LonAndLat;
	}
	
	public static double[] LonLatToXY( double longd, double latd ){
		
		int cor_x, cor_y;
		
		double[] corXY = new double[2];
		
		double phi, ald, cld, cmrad, dlam;
		double sp, cp, t, eta, dn, sphi;
		double x,y;

		phi=latd*3600.*Pi;
		ald=longd*3600.;
		cld=ICLDD*3600.+ICLDM*60.+CLDS;
		cmrad=cld*Pi;
		dlam=(ald-cld)*Pi;
		sp=Math.sin(phi);
		cp=Math.cos(phi);
		t =Math.tan(phi);
		eta=Math.sqrt((A*A-B*B)/(B*B*cp*cp));
		sphi=A*(A0*phi-A2*Math.sin(2.*phi)+A4*Math.sin(4.*phi)-A6*Math.sin(6.*phi)+A8*Math.sin(8.*phi));
		dn=A/Math.sqrt(1.0-E*E*sp*sp);
		x=dn*(dlam*cp+Math.pow(dlam,3)*Math.pow(cp,3)/6.0*(1.0-t*t+eta*eta)+Math.pow(dlam,5)*
				Math.pow(cp,5)/120.0*(5.0-18.0*t*t+Math.pow(t,4)+14.0*eta*eta-58.0*t*t*
				eta*eta+13.*Math.pow(eta,4)+4.*Math.pow(eta,6)-64.*Math.pow(eta,4)*t*t-24.*Math.pow(eta,6)*
				t*t) +Math.pow(dlam,7)/5040.0*Math.pow(cp,7)*(61.0-479.0*t*t+179.0*
						Math.pow(t,4)-Math.pow(t,6)));
		y=sphi+dn*(dlam*dlam/2.0*sp*cp+Math.pow(dlam,4)/24.0*sp*Math.pow(cp,3)*(5.0-
				t*t+9.0*eta*eta+4.0*Math.pow(eta,4))+Math.pow(dlam,6)/720.0*sp*Math.pow(cp,5)*
				(61.0-58.0*t*t+Math.pow(t,4)+270.0*eta*eta-330.0*t*t*eta*eta+
				445.*Math.pow(eta,4)+324.*Math.pow(eta,6)-680.*Math.pow(eta,4)*t*t+88.*Math.pow(eta,8)-
				600.*Math.pow(eta,6)*t*t-192.*Math.pow(eta,8)*t*t)+Math.pow(dlam,8)/40320.*sp*cp*
				7*(1385.-3111.*t*t+543.*Math.pow(t,4)-Math.pow(t,6)));
		cor_x=(int)(SF*x+XO);
		cor_y=(int)(SF*y);
		
		//System.out.println("X: "+cor_x+"\tY:"+cor_y);
		
		
		corXY[0]=cor_x;
		corXY[1]=cor_y;
		
		return corXY;
	}
	
	public static double getCarry(double number, int digital){
		
		boolean isCarray=false;
		int determineBit;
		
		determineBit=(int)(number*Math.pow(10, digital+1))%10;
		
		if(determineBit>=5)
			isCarray=true;
			
		if(isCarray){
			
			number=number*Math.pow(10, digital);
			number=Math.ceil(number);
			number=number/Math.pow(10, digital);
			
		}
		else{
			
			number=number*Math.pow(10, digital);
			number=Math.floor(number);
			number=number/Math.pow(10, digital);
			
		}
		
		
		//System.out.println(determineBit);
		
		
		return number;
	}
	
	
	
	public static void main(String args[]){
		
		//System.out.println(Math.abs(2.5-6.4));
		
//		double[] tem; 
//		
//		tem=CoordTransform.XYToLonLat(543343, 2857584);
//		
//		CoordTransform.LonLatToXY(tem[0], tem[1]);
//		
//		tem=CoordTransform.XYToLonLat(-303190, 2489089);
//		
//		CoordTransform.LonLatToXY(tem[0], tem[1]);
		
		
	}
	
	
	

}
