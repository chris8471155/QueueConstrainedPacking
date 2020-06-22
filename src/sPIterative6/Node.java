package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;
//lower bound maximum feasible intersection are (lower bound due to the lost information on strip orientations, multiplicities of the number vehicles of each type fit into each other is the minimum of the two strip orientations, furthermore it does not consider mxing vehicles
//fractional remainders based on the maximum one dimensional fraction each vehicle types uses
public class Node {
	
	int vType;
	
	static double[] A;
	
	boolean nonZeroArcExists;
	
	boolean remainderNode;
	boolean vMix1HasTheRemainder;
	//remainder 1-remainder 2
	int remainder1;
	int remainder2;
	
	double currentRemainder;//
	
	boolean initialAllocationGenerated=false;//reset to false on backtrack to a larger vehicle type or when there is no next allocation combination
	
	//int remainderAllocatedToThisNode;
	
	Arc[] arcs;//sorted in decreasing area order
	Node[] nodesAbove;//sorted in decreasing area order
	Arc[] arcsToMe;
	//int[] howManyTimesInNodesAbove;
	int[] noChoiceRemainderAllocation;
	//Remainder is passed down the tree to accommodate extra smaller vehicles that are remaining after one for one intersection step
	
	Node(int vType){
		this.vType=vType;
	}
	
	void generateArcs(int[] fitsInsideThis, int[] thisFitsInside, double[] fitsInsideMaxFraction, Node[] nodes){
		arcs=new Arc[fitsInsideThis.length];
		for(int i=0;i<fitsInsideThis.length;i++){
			arcs[i]=new Arc(nodes[fitsInsideThis[i]], this,fitsInsideMaxFraction[i]);//how many times, does it depend on strip orientation (this information has been lost as vmixes are a blend of orientations), minimum of horizontal or vertical is the lower bound on achievable multiplicity
		}
		//
		nodesAbove=new Node[thisFitsInside.length];
		for(int i=0;i<thisFitsInside.length;i++){
			nodesAbove[i]=nodes[thisFitsInside[i]];
		}
	}
	
	void setInitialRemainders(int remainder){
		if(remainder>0){
			remainder1=0;
			remainder2=remainder;
			currentRemainder=remainder2;
			//
			remainderNode=true;
			vMix1HasTheRemainder=false;
		}else if(remainder<0){
			remainder1=-remainder;
			remainder2=0;
			currentRemainder=remainder1;
			//
			remainderNode=true;
			vMix1HasTheRemainder=true;
		}else{
			remainder1=0;
			remainder2=0;
			remainderNode=false;
			currentRemainder=0;
		}
		//
		for(int i=0;i<arcs.length;i++){
			Arc arcRef=arcs[i];
			arcRef.maxAllocation=0;
			arcRef.maxAllocationDemandPerspective=0;
		}
	}
	
	//this method is required during enumeration when remainders change
	void setMinMaxArcValues(){//useful to know if there are any non-zero max allocation values
		if(remainderNode){
			if(currentRemainder>0){
				for(int i=0;i<arcs.length;i++){
					Arc arcRef=arcs[i];
					Node nodeRefSmall=arcRef.smallerVTypeNode;
					if(nodeRefSmall.remainderNode && (vMix1HasTheRemainder!=nodeRefSmall.vMix1HasTheRemainder)){
						double maxFraction=arcRef.maxFraction;
						arcRef.maxAllocation=Math.min(maxFraction*nodeRefSmall.currentRemainder, maxFraction*Math.floor(currentRemainder/maxFraction));//currentRemainder may be fractional, nodeRefSmall.currentRemainder should be integer (and will be because demands are only served integrally, demand can be used fractionallly because nested types use reallocated space (lower bound of row and column vehicle aligments)
						arcRef.maxAllocationDemandPerspective=Math.round(arcRef.maxAllocation/maxFraction);
					}else{
						arcRef.maxAllocation=0;
						arcRef.maxAllocationDemandPerspective=0;
					}
				}
			}else{
				for(int i=0;i<arcs.length;i++){
					arcs[i].maxAllocation=0;
					arcs[i].maxAllocationDemandPerspective=0;
				}
			}
		}
		//return nonZeroArcExists;
	}
	
	void fillInMeArcs(){
		ArrayList<Arc> toMeArcs=new ArrayList<Arc>(10);
		for(int i=0;i<nodesAbove.length;i++){
			boolean arcToThisFound=false;
			for(int j=0;j<nodesAbove[i].arcs.length && !arcToThisFound;j++){
				if(nodesAbove[i].arcs[j].smallerVTypeNode==this){
					toMeArcs.add(nodesAbove[i].arcs[j]);
					arcToThisFound=true;
				}
			}
		}
		//
		arcsToMe=new Arc[toMeArcs.size()];
		for(int i=0;i<toMeArcs.size();i++){
			arcsToMe[i]=toMeArcs.get(i);
		}
	}
	//when allocations change or a backtrack occurs reset currentRemainder of the arc nodes
	
	//How is this effected by maxFraction
	double demandFromOtherNodes(Arc notIncludingThisArc){
		double otherDemand=0;
		for(int i=0;i<arcs.length;i++){
			Arc arcNode=arcs[i];
			if(arcNode!=notIncludingThisArc){
				otherDemand+=(arcNode.maxAllocationDemandPerspective*A[arcNode.smallerVTypeNode.vType]);
			}
		}
		//
		return otherDemand;
	}
	//How is this effected by maxFraction
	int yieldRemainderFromNodesAbove(){
		//find the arc to me with the least other demand
		int remainderYielded=0;

		if(currentRemainder>0){
			//
			double leastOtherDemand=Double.MAX_VALUE;
			int nodeAboveIndex=-1;
			for(int i=0;i<nodesAbove.length;i++){
				if(arcsToMe[i].maxAllocation>0 && (nodesAbove[i].vMix1HasTheRemainder!=vMix1HasTheRemainder)){
					//allPossibleRemainderYielded=false;
					double demandOther=nodesAbove[i].demandFromOtherNodes(arcsToMe[i]);
					if(demandOther<leastOtherDemand){
						leastOtherDemand=demandOther;
						nodeAboveIndex=i;
					}
				}
				
			}
			//
			if(nodeAboveIndex>-1){
				remainderYielded=(int)arcsToMe[nodeAboveIndex].maxAllocationDemandPerspective;
				//
				if(remainderYielded>currentRemainder){
					System.out.println();
				}
				currentRemainder-=remainderYielded;
				nodesAbove[nodeAboveIndex].currentRemainder-=arcsToMe[nodeAboveIndex].maxAllocation;
				//the following has to be applied to all nodes every time any currentRemainder values change
				nodesAbove[nodeAboveIndex].setMinMaxArcValues();
				setMinMaxArcValues();
			}

			
		}
		//
		return remainderYielded;
	}
	
	boolean hasSupplyFromOtherNodes(Arc notIncludingThisArc){
		//double otherDemand=0;
		for(int i=0;i<arcsToMe.length;i++){
			Arc arcNode=arcsToMe[i];
			if(arcNode!=notIncludingThisArc){
				if(arcNode.maxAllocation>0){
					return true;
				}
			}
		}
		//
		return false;
	}
	
	int[] allocateNoChoiceRemainder(){//return the additional intersection vehicles of each type
		if(currentRemainder>0){
			//if there are no positive arcs to me
			//boolean positiveArcToMeExists=false;
			for(int i=0;i<arcsToMe.length;i++){
				if(arcsToMe[i].maxAllocation>0){
					//positiveArcToMeExists=true;
					return null;
				}
			}
			
			//no arcs from me lead to nodes with other supplies
			for(int i=0;i<arcs.length;i++){
				if(arcs[i].smallerVTypeNode.hasSupplyFromOtherNodes(arcs[i])){
					return null;
				}
			}
			
			
			//then allocate remainder sequentially along the arcs update min max arc values
			for(int i=0;i<arcs.length;i++){
				if(arcs[i].maxAllocation>0){
					noChoiceRemainderAllocation[0]=arcs[i].smallerVTypeNode.vType;
					noChoiceRemainderAllocation[1]=(int)arcs[i].maxAllocationDemandPerspective;
					currentRemainder-=arcs[i].maxAllocation;
					arcs[i].smallerVTypeNode.currentRemainder-=arcs[i].maxAllocationDemandPerspective;
					return noChoiceRemainderAllocation;
				}
			}
			return null;
		}else{
			return null;
		}
		
		
		
	}
}
