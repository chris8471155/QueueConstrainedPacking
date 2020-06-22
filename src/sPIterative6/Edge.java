package sPIterative6;

//Christopher Bayliss: University of Southampton, 2016
public class Edge {
	
	double initialPointPos;
	double[] initialInterval=new double[2];
	
	double pointPos;
	double[] interval=new double[2];
	
	Edge initialNextEdge;
	Edge initialPrevEdge;
	
	Edge nextEdge;
	Edge prevEdge;
	
	boolean edgeType;
	int edgeTypeNumber;//0:top, 1:left, 2:bottom, 3:right
	
	boolean edgeInList;
	
	boolean sideEdge;
	
	//boolean consideredThisIteration;
	boolean deleteEdge;
	
	//for more general loading using inner fit polygon approach
	Edge[] end=new Edge[2];
	
	Edge(){
		
	}
	
	Edge(double pointPos, double[] interval){
		this.pointPos=pointPos;
		this.interval=interval;
	}
	
	Edge(double pointPos, double[] interval, int edgeTypeNumber, boolean edgeType){
		this.pointPos=pointPos;
		this.interval=interval;
	}
	
	Edge(double pointPos, double[] interval, boolean sideEdge){
		this.pointPos=pointPos;
		this.interval=interval;
		this.sideEdge=sideEdge;
	}
	
	void reinitialise(double pointPos, double[] interval, boolean sideEdge){
		this.pointPos=pointPos;
		this.interval=interval;
		this.sideEdge=sideEdge;
	}
	
	Edge copyOfThis(){
		Edge edgeCopy=new Edge();
		edgeCopy.pointPos=this.pointPos;
		edgeCopy.interval[0]=this.interval[0];
		edgeCopy.interval[1]=this.interval[1];
		return edgeCopy;
	}
	
	Edge copyOfThisSpaceEdge(){
		Edge edgeCopy=new Edge();
		edgeCopy.pointPos=this.pointPos;
		edgeCopy.interval[0]=this.interval[0];
		edgeCopy.interval[1]=this.interval[1];
		edgeCopy.edgeType=this.edgeType;
		edgeCopy.edgeTypeNumber=this.edgeTypeNumber;
		edgeCopy.sideEdge=this.sideEdge;
		return edgeCopy;
	}
	
	double lengthOfEdge(){
		return interval[1]-interval[0];
	}
	
	//
	void initialiseEdgeReferences(double pointPositionCorrection, double intervalCorrection){
		pointPos-=pointPositionCorrection;
		initialPointPos=pointPos;
		
		interval[0]-=intervalCorrection;
		interval[1]-=intervalCorrection;
		
		initialInterval[0]=interval[0];
		initialInterval[1]=interval[1];
		
		initialNextEdge=nextEdge;
		initialPrevEdge=prevEdge;

	}
	
	void resetEdge(){
		pointPos=initialPointPos;
		
		interval[0]=initialInterval[0];
		interval[1]=initialInterval[1];
		
		nextEdge=initialNextEdge;
		prevEdge=initialPrevEdge;
	}
}
