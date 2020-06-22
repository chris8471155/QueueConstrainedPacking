package sPIterative6;

public class RemNode {
	int supplyNodeNumber;
	int demandNodeNumber;
	
	int arcNumber;//for restoring the corrent values when backtracking
	
	//
	int demandUnits;
	double supplyUnits;
	
	double initialSupplyRemainder;
	double initialDemandRemainder;
	
	double recycledRemainderBeforeThis;
	
	double recycledAreaContribution;
	
	double ub;
	
	//
	RemNode parentNode;
	//
	int numberOfChildNodes;
	RemNode headChildNode;
	RemNode tailChildNode;
	//
	RemNode nextChildNode;
	RemNode prevChildNode;
	//
	RemNode currentChildNode;
	
	
	RemNode(int arcNumber){
		this.arcNumber=arcNumber;
	}
	
	
	//branch on current node will check feasibility before using this
	//"initialParentSupply" so that remainder values can be updated with minimal chance of numerical errors accumulating
	RemNode(RemNode parentNode, int demandUnits, double supplyUnits, double initialSupplyRemainder, double initialDemandRemainder, int supplyNodeNumber, int demandNodeNumber, double recycledRemainderBeforeThis, double recycledAreaContribution, int arcNumber, double ub){
		this.initialSupplyRemainder=initialSupplyRemainder;
		this.parentNode=parentNode;
		this.demandUnits=demandUnits;
		this.supplyUnits=supplyUnits;
		this.recycledRemainderBeforeThis=recycledRemainderBeforeThis;
		this.recycledAreaContribution=recycledAreaContribution;
		this.initialDemandRemainder=initialDemandRemainder;
		this.supplyNodeNumber=supplyNodeNumber;
		this.demandNodeNumber=demandNodeNumber;
		this.arcNumber=arcNumber;
		this.ub=ub;
	}
	
	void addChildNodeToLinkedList(RemNode newChildNode){
		
		newChildNode.nextChildNode=null;
		newChildNode.prevChildNode=null;
		RemNode currentNum=headChildNode;
		if(headChildNode==null){
			//this implies there are no elements in the list
			headChildNode=newChildNode;
			headChildNode.prevChildNode=null;
			tailChildNode=newChildNode;
			tailChildNode.nextChildNode=null;
			//
			currentChildNode=headChildNode;
		}else if(headChildNode==tailChildNode){//one member
			if(newChildNode.demandUnits>=headChildNode.demandUnits){
				if(newChildNode.demandUnits==headChildNode.demandUnits){
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
			if(newChildNode.demandUnits>=headChildNode.demandUnits){
				if(newChildNode.demandUnits==headChildNode.demandUnits){
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
				if(newChildNode.demandUnits<=tailChildNode.demandUnits){
					if(newChildNode.demandUnits==tailChildNode.demandUnits){
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
					if(newChildNode.demandUnits>=currentNum.demandUnits){
						if(newChildNode.demandUnits==currentNum.demandUnits){
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
	
	void removeChildNodeFromLinkedList(RemNode scenSolToRemove){
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
