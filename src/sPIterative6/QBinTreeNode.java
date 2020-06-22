package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class QBinTreeNode {
	//static int numberOfQueues;
	static int vTypes;
	static int numberOfBins;
	static int numberOfQueues;
	//
	Rectangle rect;
	int currentBinNumber;
	//int frequency;
	Rectangle[] qFronts;
	//
	//instead of generating new arrays for every node use a reference to the same array and change the values as you move back and fourth in the tree
	//Similarly for "qFronts"
	//int[] vMixAfterThis;//for lower bound calculations
	int[] remVMixAfterThis;
	//
	double[] remLenPerBinAfterThis;
	//
	double placementEfficiencyScore;
	//
	QBinTreeNode parentNode;
	//
	int numberOfChildNodes;
	QBinTreeNode headChildNode;
	QBinTreeNode tailChildNode;
	//
	QBinTreeNode nextChildNode;
	QBinTreeNode prevChildNode;
	//
	QBinTreeNode currentChildNode;
	
	//
	//double remainingLengthInBin;
	//FLEXIPAX (remaining flexibility)
	//
	boolean binsFull;
	//
	boolean newBinNode;
	//
	//boolean infeasibleNode;
	int queuePopNumber;
	int queueNumber;
	double lb;
	//The objective
	double upperBound;
	
	//root node constructor
	QBinTreeNode(Bin[] bins, Queue[] Qs){//double lengthOfBin
		//
		//vMixAfterThis=new int[vTypes];//for lower bound calculations
		remVMixAfterThis=new int[vTypes];;
		remLenPerBinAfterThis=new double[numberOfBins];
		for(int b=0;b<numberOfBins;b++){
			if(bins[b].orientation==0){
				remLenPerBinAfterThis[b]=bins[b].w;
			}else{
				remLenPerBinAfterThis[b]=bins[b].l;
			}
		}
		qFronts=new Rectangle[numberOfQueues];
		for(int q=0;q<numberOfQueues;q++){
			qFronts[q]=Qs[q].rectAtFront;
			//count the initial vehicle mix
			Rectangle currentRectangle=qFronts[q];
			while(currentRectangle!=null){
				remVMixAfterThis[currentRectangle.type]++;
				//
				currentRectangle=currentRectangle.nextRectInQueue;
			}
		}
		this.rect=null;
	}
	
	//only generate nodes for feasible actions (fit in bin) and where the lb is not greater than the overall upper bound
	//bin tree nodes are hidden from the two orientations of bins
	//Queues do not have to be popped, qFronts is enough
	QBinTreeNode(int queuePopped, Rectangle rect, QBinTreeNode parentNode, int binNumber, boolean isRowBin, double placementEfficiencyScore){//
		this.rect=rect;
		//
		queuePopNumber=parentNode.queuePopNumber+1;
		queueNumber=queuePopped;
		//
		currentBinNumber=binNumber;
		//
		this.parentNode=parentNode;
		//
		if(currentBinNumber!=parentNode.currentBinNumber){
			newBinNode=true;
		}
		//vMixAfterThis=new int[vTypes];//is this required
		remVMixAfterThis=new int[vTypes];//for lower bound calculations
		remLenPerBinAfterThis=new double[numberOfBins];
		qFronts=new Rectangle[numberOfQueues];
		//copy parent node values then increment as appropriate
		for(int v=0;v<vTypes;v++){
			//vMixAfterThis[v]=parentNode.vMixAfterThis[v];
			remVMixAfterThis[v]=parentNode.remVMixAfterThis[v];
		}
		//increment
		//vMixAfterThis[rect.type]++;
		remVMixAfterThis[rect.type]--;
		//remaining length in each bin
		for(int b=0;b<numberOfBins;b++){
			remLenPerBinAfterThis[b]=parentNode.remLenPerBinAfterThis[b];
		}
		//increment
		if(isRowBin){
			remLenPerBinAfterThis[binNumber]-=rect.w;
		}else{
			remLenPerBinAfterThis[binNumber]-=rect.l;
		}
		
		for(int q=0;q<numberOfQueues;q++){
			qFronts[q]=parentNode.qFronts[q];
		}
		//increment
		qFronts[queuePopped]=rect.nextRectInQueue;
		//
		this.placementEfficiencyScore=placementEfficiencyScore-binNumber;
	}
	
	
	void addChildNodeToLinkedList(QBinTreeNode newChildNode){
		
		newChildNode.nextChildNode=null;
		newChildNode.prevChildNode=null;
		QBinTreeNode currentNum=headChildNode;
		if(headChildNode==null){
			//this implies there are no elements in the list
			headChildNode=newChildNode;
			headChildNode.prevChildNode=null;
			tailChildNode=newChildNode;
			tailChildNode.nextChildNode=null;
			//
			currentChildNode=headChildNode;
		}else if(headChildNode==tailChildNode){//one member
			if(newChildNode.placementEfficiencyScore>=headChildNode.placementEfficiencyScore){
				if(newChildNode.placementEfficiencyScore==headChildNode.placementEfficiencyScore){
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
			if(newChildNode.placementEfficiencyScore>=headChildNode.placementEfficiencyScore){
				if(newChildNode.placementEfficiencyScore==headChildNode.placementEfficiencyScore){
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
				if(newChildNode.placementEfficiencyScore<=tailChildNode.placementEfficiencyScore){
					if(newChildNode.placementEfficiencyScore==tailChildNode.placementEfficiencyScore){
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
					if(newChildNode.placementEfficiencyScore>=currentNum.placementEfficiencyScore){
						if(newChildNode.placementEfficiencyScore==currentNum.placementEfficiencyScore){
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
	
	void removeChildNodeFromLinkedList(QBinTreeNode scenSolToRemove){
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
