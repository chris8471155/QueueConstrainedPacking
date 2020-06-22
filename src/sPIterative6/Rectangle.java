package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.awt.Color;

public class Rectangle {
	int type;
	
	double l;
	double w;
	double h;
	
	double area;
	
	//
	//random arrival time
	double randArrivalTime;
	
	Color colour;
	//
	Rectangle nextRectByWidthNonConstrainedLoading;
	Rectangle prevRectByWidthNonConstrainedLoading;
	Rectangle nextRectByLengthNonConstrainedLoading;
	Rectangle prevRectByLengthNonConstrainedLoading;
	//
	Rectangle nextRectByWidthYardPolicyCreation;
	Rectangle prevRectByWidthYardPolicyCreation;
	Rectangle nextRectByLengthYardPolicyCreation;
	Rectangle prevRectByLengthYardPolicyCreation;
	
	//quantile approach references
	Rectangle nextRect;
	Rectangle prevRect;
	
	//yard queue references
	Rectangle nextRectInQueue;
	Rectangle prevRectInQueue;
	
	double rightPP=0;//point position
	double leftPP=0;//point position
	double topPP=0;//point position
	double bottomPP=0;//point position
	
	//
	boolean lastIncludableRectangleInQueue;//this instead of the Rectangle[] returning method for obtaining the set of includable rectangles (for the integer programming approach for filling a strip)
	
	//for visualisation
	int binNumber;
	
	//fields for inner fit polygon based bottom left
	double[] parkedPosition=new double[2];//
	Edge le2;
	Edge re2;
	Edge be2;
	Edge te2;
	
	//
	Edge horizontalAdjacentEdge;
	Edge verticalAdjacentEdge;
	
	//
	int queueNumber;
	

	Rectangle(double l, double w, double h, Color theColour, int type){//, double onePixel
		this.l=l;
		this.w=w;
		this.h=h;
		this.type=type;
		//
		area=l*w;
		//
		colour=theColour;
		//
	}
	
	Rectangle(Rectangle rect){
		l=rect.l;
		w=rect.w;
		h=rect.h;
		//
		area=l*w;
		//
		colour=rect.colour;
		//
		type=rect.type;
		//
		binNumber=rect.binNumber;
	}
	
	boolean overlap(Rectangle vehicle2){
		boolean overlapExists=false;
		
		double combinedWidth=Math.max(vehicle2.parkedPosition[0]+vehicle2.w, parkedPosition[0]+w)-Math.min(parkedPosition[0], vehicle2.parkedPosition[0]);
		double combinedHeight=Math.max(vehicle2.parkedPosition[1]+vehicle2.l, parkedPosition[1]+l)-Math.min(parkedPosition[1], vehicle2.parkedPosition[1]);
		double sumOfWidths=vehicle2.w+w;
		double sumOfHeights=vehicle2.l+l;
		if(combinedWidth+0.000001<sumOfWidths && combinedHeight+0.000001<sumOfHeights){//
			overlapExists=true;
		}else{
			overlapExists=false;
		}
		return overlapExists;
	}
	
	double overlapArea(Rectangle vehicle2){
		double overlapArea=0;
		double combinedWidth=Math.max(vehicle2.parkedPosition[0]+vehicle2.w, parkedPosition[0]+w)-Math.min(parkedPosition[0], vehicle2.parkedPosition[0]);
		double combinedLength=Math.max(vehicle2.parkedPosition[1]+vehicle2.l, parkedPosition[1]+l)-Math.min(parkedPosition[1], vehicle2.parkedPosition[1]);
		double sumOfWidths=vehicle2.w+w;
		double sumOfLengths=vehicle2.l+l;
		if(combinedWidth+0.000001<sumOfWidths && combinedLength+0.000001<sumOfLengths){
			overlapArea=(sumOfLengths-combinedLength)*(sumOfWidths-combinedWidth);
		}else{
			overlapArea=0;
		}
		return overlapArea;
	}
	
	void generateEdgeCopies(int vehicleNumber){//as edges often have to be associated with the vehicle that they belong to
		rightEdge2(vehicleNumber);
		leftEdge2(vehicleNumber);
		bottomEdge2(vehicleNumber);
		topEdge2(vehicleNumber);
		//link ends
		be2.end[0]=le2;
		be2.end[1]=re2;
		te2.end[0]=re2;
		te2.end[1]=le2;
		//
		le2.end[0]=te2;
		le2.end[1]=be2;
		re2.end[0]=be2;
		re2.end[1]=te2;
	}
	
	
	Edge rightEdge2(int number){
		return (re2=new Edge(parkedPosition[0]+w, new double[]{parkedPosition[1],parkedPosition[1]+l}));
	}
	Edge leftEdge2(int number){
		return (le2=new Edge(parkedPosition[0], new double[]{parkedPosition[1],parkedPosition[1]+l}));
	}
	Edge topEdge2(int number){
		return (te2=new Edge(parkedPosition[1], new double[]{parkedPosition[0],parkedPosition[0]+w}));
	}
	Edge bottomEdge2(int number){
		return (be2=new Edge(parkedPosition[1]+l, new double[]{parkedPosition[0],parkedPosition[0]+w}));
	}
	
	void updateTempPosition(){
		rightPP=parkedPosition[0]+w;//point position
		leftPP=parkedPosition[0];//point position
		topPP=parkedPosition[1];//point position
		bottomPP=parkedPosition[1]+l;//point position
	}
	
	double[][] XYLimits(){
		double[][] XYLims=new double[2][2];
		XYLims[0][0]=parkedPosition[0];
		XYLims[0][1]=parkedPosition[0]+w;
		XYLims[1][0]=parkedPosition[1];
		XYLims[1][1]=parkedPosition[1]+l;
		return XYLims;
	}
	
}
