package gui;

import java.awt.Frame;
import java.awt.Graphics;

public class GUITasks {
	public static Frame getTopFrame(){
			
		Frame[] frames = Frame.getFrames();
		for(int i=0;i<frames.length;i++){
			if(frames[i].getFocusOwner()!=null){
				return frames[i];
			}
		}
		if(frames.length>0){
			return frames[0];
		}
		return null;
	}
	
	public static void drawDashedLine(Graphics g, int x1, int y1, int x2, int y2, int dashSize, int gapSize){
		
		
		int totalDash = dashSize + gapSize;
		if(y1 == y2){
			
			if(x2 < x1){
				int temp = x1;
				x1 = x2;
				x2 = temp;
			}
			if(y2 < y1){
				int temp = y1;
				y1 = y2;
				y2 = temp;
			}
			
			int virtualStartX = (x1 / totalDash) * totalDash;
			for(int x = virtualStartX; x < x2; x+=totalDash){
				int topX = x + dashSize; 
				if(topX > x2) {
					topX = x2;
				}
				int firstX = x;
				if(firstX < x1){
					firstX = x1;
				}
				if(firstX < topX){
					g.drawLine(firstX, y1, topX, y1);
				}
			}
		}
		else if(x1 == x2){
			
			if(x2 < x1){
				int temp = x1;
				x1 = x2;
				x2 = temp;
			}
			if(y2 < y1){
				int temp = y1;
				y1 = y2;
				y2 = temp;
			}
			
			int virtualStartY = (y1 / totalDash) * totalDash;
			for(int y = virtualStartY; y < y2; y+=totalDash){
				int topY = y + dashSize;
				if(topY > y2){
					topY = y2;
				}
				int firstY = y;
				if(firstY < y1){
					firstY = y1;
				}
				if(firstY < topY){
					g.drawLine(x1, firstY, x1, topY);
				}
			}
		}
		else{
			
			int startX;
			int startY;
			
			double slope = (y2-y1)/(x2-x1);
			
			//System.out.println(slope);
			
			if(x2>x1){
				
				double x=x1;
				double y=y1;
				
				while(x<x2){
					
					startX=(int)x;
					startY=(int)y;
					
					x=x+dashSize;
					
					if(x>x2)
						break;
					
					
					y=slope*(x-x1)+y1;
					
					//System.out.println("draw x="+startX+"-"+x);
					//System.out.println("draw y="+startY+"-"+y);
					
					g.drawLine(startX,startY,(int)x,(int)y);
					
					x=x+gapSize;
					y=slope*(x-x1)+y1;
					
					//System.out.println("x="+x);
					//System.out.println("y="+y);
					
				}
			}
			
			if(x2<x1){
				
				double x=x1;
				double y=y1;
				
				while(x>x2){
					
					startX=(int)x;
					startY=(int)y;
					
					x=x-dashSize;
					
					if(x<x2)
						break;
					
					y=slope*(x-x1)+y1;
					g.drawLine(startX,startY,(int)x,(int)y);
					
					x=x-gapSize;
					y=slope*(x-x1)+y1;
					
					//System.out.println("x="+x);
					//System.out.println("y="+y);
					
				}
			}
			
		}
	}
	
	public static void drawDashedPolyLine(Graphics g, int[] xPoints, int[] yPoints, int nPoints, int dashSize, int gapSize){
		
		for(int i=0;i<nPoints-1;i++){
			drawDashedLine(g,xPoints[i],yPoints[i],xPoints[i+1],yPoints[i+1],dashSize,gapSize);
		}
			
		
	}
	
	public static void main(String args[]){
		
		
		
		
	}
	
}
