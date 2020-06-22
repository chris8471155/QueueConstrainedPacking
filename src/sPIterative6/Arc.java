package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class Arc {

	double maxAllocation;
	double maxAllocationDemandPerspective;
	
	double maxFraction;
	
	int nodeNumberAbove;
	int nodeNumberBelow;
	int overallArcNumber;
	
	Node smallerVTypeNode;
	Node biggerVTypeNode;
	
	Arc(Node smallerVTypeNode, Node biggerVTypeNode, double maxFraction){
		this.smallerVTypeNode=smallerVTypeNode;
		this.biggerVTypeNode=biggerVTypeNode;
		this.maxFraction=maxFraction;
	}
	
}
