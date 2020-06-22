package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class BinTreeNode {
	//static int numberOfQueues;
	static int vTypes;
	//
	int vehicleType;
	int frequency;
	//
	BinTreeNode parentNode;
	//
	int numberOfChildNodes;
	BinTreeNode headChildNode;
	BinTreeNode tailChildNode;
	//
	BinTreeNode nextChildNode;
	BinTreeNode prevChildNode;
	//
	BinTreeNode currentChildNode;
	
	//
	double remainingLengthInBin;
	//FLEXIPAX (remaining flexibility)
	//
	boolean binFull;
	//
	boolean infeasibleNode;
	//root node constructor
	BinTreeNode(double lengthOfBin){
		remainingLengthInBin=lengthOfBin;
		//
		//this.vehicleType=vTypes;
		this.vehicleType=-1;
	}
	
	//bin tree nodes are hidden from the two orientations of bins
	BinTreeNode(int vehicleType, BinTreeNode parentNode, int frequency, double length, double smallestLengthOfRemainingV){
		this.vehicleType=vehicleType;
		//
		this.parentNode=parentNode;
		//
		this.frequency=frequency;
		//
		remainingLengthInBin=parentNode.remainingLengthInBin-(frequency*length);//check if this is satisfied
		//
		if(remainingLengthInBin<0){
			infeasibleNode=true;
		}else{
			if(vehicleType==vTypes-1){
			//if(smallestLengthOfRemainingV>remainingLengthInBin){
				binFull=true;
			}
		}
	}
	
	
	void addChildNodeToLinkedList(BinTreeNode newChildNode){
		
		newChildNode.nextChildNode=null;
		newChildNode.prevChildNode=null;
		BinTreeNode currentNum=headChildNode;
		if(headChildNode==null){
			//this implies there are no elements in the list
			headChildNode=newChildNode;
			headChildNode.prevChildNode=null;
			tailChildNode=newChildNode;
			tailChildNode.nextChildNode=null;
			//
			currentChildNode=headChildNode;
		}else if(headChildNode==tailChildNode){//one member
			if(newChildNode.frequency>=headChildNode.frequency){
				if(newChildNode.frequency==headChildNode.frequency){
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;	
				}else{
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;
				}
			}else{
				newChildNode.prevChildNode=headChildNode;
				headChildNode.nextChildNode=newChildNode;
				tailChildNode=newChildNode;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the headChildNode
			if(newChildNode.frequency>=headChildNode.frequency){
				if(newChildNode.frequency==headChildNode.frequency){
					posFound=true;
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;
				}else{
					//before the headChildNode
					posFound=true;
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;
				}
			}
			//does the edge go after the tailChildNode
			if(!posFound){
				if(newChildNode.frequency<=tailChildNode.frequency){
					if(newChildNode.frequency==tailChildNode.frequency){
						posFound=true;
						newChildNode.prevChildNode=tailChildNode;
						tailChildNode.nextChildNode=newChildNode;
						tailChildNode=newChildNode;
					}else{
						posFound=true;
						newChildNode.prevChildNode=tailChildNode;
						tailChildNode.nextChildNode=newChildNode;
						tailChildNode=newChildNode;
					}
				}
			}
			//if not somewhere in between
			if(!posFound){
				currentNum=headChildNode.nextChildNode;
				while(!posFound && currentNum!=null){
					if(newChildNode.frequency>=currentNum.frequency){
						if(newChildNode.frequency==currentNum.frequency){
							posFound=true;
							newChildNode.nextChildNode=currentNum;
							newChildNode.prevChildNode=currentNum.prevChildNode;
							currentNum.prevChildNode.nextChildNode=newChildNode;
							currentNum.prevChildNode=newChildNode;
						}else{
							//before the current edge
							posFound=true;
							newChildNode.nextChildNode=currentNum;
							newChildNode.prevChildNode=currentNum.prevChildNode;
							currentNum.prevChildNode.nextChildNode=newChildNode;
							currentNum.prevChildNode=newChildNode;
						}
					}
					currentNum=currentNum.nextChildNode;
				}
			}
		}
		numberOfChildNodes++;
		//printList();
		
	}
	
	void removeChildNodeFromLinkedList(BinTreeNode scenSolToRemove){
		if(headChildNode==tailChildNode){
			headChildNode=null;
			tailChildNode=null;
		}else{
			if(scenSolToRemove==headChildNode){
				headChildNode=headChildNode.nextChildNode;
				headChildNode.prevChildNode=null;
			}else if(scenSolToRemove==tailChildNode){
				tailChildNode=tailChildNode.prevChildNode;
				tailChildNode.nextChildNode=null;
			}else{
				scenSolToRemove.prevChildNode.nextChildNode=scenSolToRemove.nextChildNode;
				scenSolToRemove.nextChildNode.prevChildNode=scenSolToRemove.prevChildNode;
			}
		}
		numberOfChildNodes--;
	}
}
