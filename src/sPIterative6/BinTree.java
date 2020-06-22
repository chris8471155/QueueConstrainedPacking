package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;

public class BinTree {
	static int vTypes;
	static double[][] rectangleDimensions;
	//int[] vCounts;//in strip
	int binNumber;
	
	boolean rowTypeBin;
	int summingDimensionIndex;
	double length;
	double width;
	
	BinTreeNode currentNode;
	
	BinTreeNode rootNode;//queue number -1
	
	int[] initialVMixRemaining;
	int[] finalVMixRemaining;
	
	int[] VOrder;
	int[] maxVs;
	int[] minVs;
	double[] smallestLengthOfRemainingVehicle;//MaxValue if non exist
	//
	boolean binFull;
	boolean leavesRemain=true;
	
	int[] binVMix;
	
	//tree constructor: current queues,
	BinTree(int[] initialVMixRemaining, boolean rowTypeBin, double length, double width, int[] VOrder){//vCounts is the number of vehicles of each type in the strip this tree is being built (to find a feasible queue path)
		this.rowTypeBin=rowTypeBin;
		if(rowTypeBin){
			summingDimensionIndex=1;
		}else{
			summingDimensionIndex=0;
		}
		//
		this.length=length;
		this.width=width;
		//
		this.VOrder=VOrder;
		//
		if(rowTypeBin){
			rootNode=new BinTreeNode(width);
		}else{
			rootNode=new BinTreeNode(length);
		}
		//
		currentNode=rootNode;
		
		this.initialVMixRemaining=new int[vTypes];//initialVMixRemaining;//
		for(int v=0;v<vTypes;v++){
			this.initialVMixRemaining[v]=initialVMixRemaining[v];
		}
		
		finalVMixRemaining=new int[vTypes];
		
		maxVs=new int[vTypes];
		minVs=new int[vTypes];
		
		binVMix=new int[vTypes];
		
		double[] sumOfVLengths=new double[vTypes];
		
		
		//work out the minimum and maximum number of each vehicle type
		for(int j=0;j<vTypes;j++){
			double vehicleDimension=rectangleDimensions[1-summingDimensionIndex][j];
			double summmingVehicleDimension=rectangleDimensions[summingDimensionIndex][j];
			if(rowTypeBin){
				if(vehicleDimension<=length){
					for(int k=0;k<initialVMixRemaining[j] && (sumOfVLengths[j]+summmingVehicleDimension)<=width;k++){
						sumOfVLengths[j]+=summmingVehicleDimension;
						maxVs[j]++;
					}
				}
			}else{
				if(vehicleDimension<=width){
					for(int k=0;k<initialVMixRemaining[j] && (sumOfVLengths[j]+summmingVehicleDimension)<=length;k++){
						sumOfVLengths[j]+=summmingVehicleDimension;
						maxVs[j]++;
					}
				}
			}
		}
		
		//minimum (subtract the sum of the other vehicle's summed lengths)
		for(int j=0;j<vTypes;j++){
			if(maxVs[j]>0){
				double sumOfOtherVehicleLengths=0;
				for(int k=0;k<vTypes;k++){
					if(j!=k){
						sumOfOtherVehicleLengths+=sumOfVLengths[k];
					}
				}
				//
				
				if(rowTypeBin){
					double remainingLaneLength=width-sumOfOtherVehicleLengths;
					double summmingVehicleDimension=rectangleDimensions[1][j];
					//	
					double minSumLengthOfVehicle=0;
					while(minSumLengthOfVehicle+summmingVehicleDimension<remainingLaneLength){
						minSumLengthOfVehicle+=summmingVehicleDimension;
						minVs[j]++;
					}
				}else{
					double remainingLaneLength=length-sumOfOtherVehicleLengths;
					double summmingVehicleDimension=rectangleDimensions[0][j];
					//	
					double minSumLengthOfVehicle=0;
					while(minSumLengthOfVehicle+summmingVehicleDimension<remainingLaneLength){
						minSumLengthOfVehicle+=summmingVehicleDimension;
						minVs[j]++;
					}
				}
				
			}
				
		}
		
		
		smallestLengthOfRemainingVehicle=new double[vTypes];
		for(int j=0;j<vTypes;j++){
			smallestLengthOfRemainingVehicle[j]=Double.MAX_VALUE;
			//if(maxVs[j]>0){
				for(int k=j+1;k<vTypes;k++){
					if(maxVs[k]>0){
						smallestLengthOfRemainingVehicle[j]=Math.min(rectangleDimensions[summingDimensionIndex][k], smallestLengthOfRemainingVehicle[j]);
					}
				}
			//}
		}
		
		
		//build initial path
		//generate the child nodes for the first queue
		//
		//boolean feasiblePathFound=false;//!feasiblePathFound
		boolean finished=false;
		while(currentNode!=null && !finished){
			//feasible path found
			if(currentNode.binFull){//note last queue (treeNode) can only exist it is exactly satisfies the strip requirements
				//feasiblePathFound=true;
				finished=true;
				//note: the queue information for the next tree/strip can be gathered from the parentNode relations from the currentNode
				//
				
			}else{
				//try to branch (if a child node then exists set it as the cuurentNode
				branchOnTreeNode(currentNode);
				if(currentNode.currentChildNode!=null){
					currentNode=currentNode.currentChildNode;
				}else{
					//if it does not exist then try the next child node of the parent node
					if((currentNode=updateCurrentNode(currentNode))==null){
						finished=true;
						leavesRemain=false;
					}
				}
			}
		}
	}
	
	//FLEXIPAX (if the leaf node had flexibility and the current node was not for the last queue then try branching on the current node
	
	//get next feasible path (only called if at least one feasible path exist for this tree
	boolean getNextFeasiblePath(){//if a feasible path exists, the output will involve an array of rectangle of the fron of the queues that will be the input for the next tree for the next strip
		//
		if((currentNode=updateCurrentNode(currentNode))==null){
			leavesRemain=false;
		}else{
			//a back track to a different child node was possible
			//now try to find the next feasible path through this tree
			boolean finished=false;
			while(currentNode!=null && !finished){
				//feasible path found
				if(currentNode.binFull){//note last queue (treeNode) can only exist it is exactly satisfies the strip requirements
					//feasiblePathFound=true;
					finished=true;
					//note: the queue information for the next tree/strip can be gathered from the parentNode relations from the currentNode
				}else{
					//try to branch (if a child node then exists set it as the cuurentNode
					branchOnTreeNode(currentNode);
					if(currentNode.currentChildNode!=null){
						currentNode=currentNode.currentChildNode;
					}else{
						//if it does not exist then try the next child node of the parent node
						if((currentNode=updateCurrentNode(currentNode))==null){
							finished=true;
							leavesRemain=false;
						}
					}
				}
			}
		}
		return leavesRemain;
	}
	
	//branch on current tree Node (input: rectangles not yet covered in the strip, (feasibility 
	void branchOnTreeNode(BinTreeNode parentNode){
		//the next set of branches will correspond to the next queue, add child branch node in decreasing pop frequency
		//feasibility, strip not overfull and strip is still possible given prev and current pop frequencies
		//currentNode;
		//currentQueueIndex;
		//parentNode.
		if(parentNode.vehicleType+1<vTypes){
		//if(parentNode.vehicleType-1>-1){
			int vehicleType=parentNode.vehicleType+1;
			//int vehicleType=parentNode.vehicleType-1;
			//int queueNumber=queueOrder[vehicleType];
			for(int k=maxVs[vehicleType];k>=minVs[vehicleType];k--){
				//generate the tree node for k pops of queue (queueNumber)
				//check whether this tree node is feasible with respect to the remaining strip requirements and wheat is left in the queues that have not been considered yet
				//(check after creating this node, if it is feasible with respect to only popping rectangles that still contribute the remaining strip requirements
				
				
				//BinTreeNode(int vehicleType, BinTreeNode parentNode, int frequency, double length)
				
				BinTreeNode newChildNode=new BinTreeNode(vehicleType, parentNode, k, rectangleDimensions[summingDimensionIndex][vehicleType], smallestLengthOfRemainingVehicle[vehicleType]);
				if(!newChildNode.infeasibleNode){
					parentNode.addChildNodeToLinkedList(newChildNode);
				}
			}
			//and a zero node
			if(maxVs[vehicleType]==0){
				BinTreeNode newChildNode=new BinTreeNode(vehicleType, parentNode, 0, rectangleDimensions[summingDimensionIndex][vehicleType], smallestLengthOfRemainingVehicle[vehicleType]);
				if(!newChildNode.infeasibleNode){
					parentNode.addChildNodeToLinkedList(newChildNode);
				}
			}
			//set binFull if this is the last node
		}
	}
	
	BinTreeNode updateCurrentNode(BinTreeNode currentNode){
		boolean nextCurrentNodeFound=false;
		while(!nextCurrentNodeFound && currentNode!=null){
			//try the next child node of the parent node
			currentNode=currentNode.parentNode;
			//may have tried to backtrack from the root node, in which case the solution has been found
			if(currentNode!=null){
				//currentNode=currentNode.currentChildNode.nextChildNode;
				//if null backtrack
				if(currentNode.currentChildNode.nextChildNode!=null){
					nextCurrentNodeFound=true;
					//increment the current child node of the parent node
					currentNode.currentChildNode=currentNode.currentChildNode.nextChildNode;
					//
					currentNode=currentNode.currentChildNode;
				}
			}
		}
		//
		return currentNode;
	}
	
	//set final remaining vehicles
	int[] finalVMixRemaining(){
		/*int[] initialVMixRemaining;
		int[] finalVMixRemaining;*/
		//
		BinTreeNode currentTreeNode=currentNode;
		while(currentTreeNode.vehicleType>-1){//currentTreeNode!=null
		//while(currentTreeNode.vehicleType<vTypes){
			finalVMixRemaining[currentTreeNode.vehicleType]=initialVMixRemaining[currentTreeNode.vehicleType]-currentTreeNode.frequency;
			//
			currentTreeNode=currentTreeNode.parentNode;
		}
		return finalVMixRemaining;
	}
	
	int[] getBinVMix(){
		//assuming there is always a level for each vehicle type even if there are non of that type to pack or non fit this bin.
		//binVMix : int[];
		BinTreeNode cbtn=currentNode;
		while(cbtn.vehicleType>-1){
		//while(cbtn.vehicleType<vTypes){
			binVMix[cbtn.vehicleType]=cbtn.frequency;
			//
			cbtn=cbtn.parentNode;
		}
		return binVMix;
	}
	
	
}
