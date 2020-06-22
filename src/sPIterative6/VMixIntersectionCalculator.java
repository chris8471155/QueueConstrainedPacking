package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;

public class VMixIntersectionCalculator {
	
	int vTypes;
	double[] vAreas;
	
	int[] vTypesDecreasingArea;
	
	Node[] nodes;
	
	int[] intersectionVMix;
	double intersectionArea;
	
	int counter=0;
	
	int[][] nodeArcIndices;
	int totalArcs;
	double[] demandConversion;
	boolean[] vMix1HasTheRemainder;
	boolean[] isRemNode;
	
	double[] currentRemainder;
	double currentRecycledRemainderArea;
	int currentArcNumber;
	double[] currentRemainderRelaxed;
	
	int[] currentRecycledRemainderVMix;
	int[] bestRecycledRemainderVMix;
	
	Arc[] allArcs;
	
	static int levelOfRelaxation;//0: naive intersection; 1: nominal recycling of remainders using nested sizes; 2: 1-d bin packing use of remainders (this is the default mode in the current version)
	
	
	//build the graph structure
	VMixIntersectionCalculator(int vTypes, double[][] rectangleDistribution, double[] vAreas, int[] vTypesDecreasingArea){
		
		this.vTypes=vTypes;
		this.vAreas=vAreas;
		this.vTypesDecreasingArea=vTypesDecreasingArea;
		
		intersectionVMix=new int[vTypes];
		
		int[][] fitsInside=new int[vTypes][1];;
		int[][] thisFitsInside=new int[vTypes][1];;
		double[][] fitsInsideMaxFraction=new double[vTypes][1];;
		for(int vv=0;vv<vTypes;vv++){
			int v=vTypesDecreasingArea[vv];
			//for each vehicle type find the largest vehicle type that fits inside it
			//boolean largestInsideFitterFound=false;
			//fitsInside[v]=-1;
			ArrayList<Integer> fitsInsideAL=new ArrayList<Integer>(10);
			ArrayList<Double> fitsInsideMaxFractionAL=new ArrayList<Double>(10);
			for(int uu=vv+1;uu<vTypes;uu++){// && !largestInsideFitterFound
				int u=vTypesDecreasingArea[uu];
				if(rectangleDistribution[0][u]<=rectangleDistribution[0][v] && rectangleDistribution[1][u]<=rectangleDistribution[1][v]){
					//largestInsideFitterFound=true;
					fitsInsideAL.add(u);
					//fitsInside[v]=u;
					double ubFitFraction=Math.max(rectangleDistribution[0][u]/rectangleDistribution[0][v], rectangleDistribution[1][u]/rectangleDistribution[1][v]);
					if(levelOfRelaxation==1){
						ubFitFraction=1;
					}
					fitsInsideMaxFractionAL.add(ubFitFraction);
				}
			}
			fitsInside[v]=new int[fitsInsideAL.size()];
			fitsInsideMaxFraction[v]=new double[fitsInsideAL.size()];
			for(int u=0;u<fitsInsideAL.size();u++){
				fitsInside[v][u]=fitsInsideAL.get(u);
				fitsInsideMaxFraction[v][u]=fitsInsideMaxFractionAL.get(u);
			}
			////////////////////////////////
			////////////////////////////////
			//ArrayList<Integer> thisFitsInsideHowManyTimesAL=new ArrayList<Integer>(10);
			ArrayList<Integer> thisFitsInsideAL=new ArrayList<Integer>(10);
			for(int uu=0;uu<vv;uu++){// && !largestInsideFitterFound
				int u=vTypesDecreasingArea[uu];
				if(rectangleDistribution[0][u]>=rectangleDistribution[0][v] && rectangleDistribution[1][u]>=rectangleDistribution[1][v]){
					//largestInsideFitterFound=true;
					thisFitsInsideAL.add(u);
					//fitsInside[v]=u;
					//double lbMultiplicity=Math.min((int)(rectangleDistribution[0][u]/rectangleDistribution[0][v]), (int)(rectangleDistribution[1][u]/rectangleDistribution[1][v]));
					//thisFitsInsideHowManyTimesAL.add(lbMultiplicity);
				}
			}
			//thisFitsInsideHowManyTimes[v]=new int[thisFitsInsideAL.size()];
			thisFitsInside[v]=new int[thisFitsInsideAL.size()];
			for(int u=0;u<thisFitsInsideAL.size();u++){
				thisFitsInside[v][u]=thisFitsInsideAL.get(u);
				//thisFitsInsideHowManyTimes[v][u]=thisFitsInsideHowManyTimesAL.get(u);
			}
		}
		//
		Node.A=vAreas;
		//create the graph structure
		nodes=new Node[vTypes];
		for(int v=0;v<vTypes;v++){
			nodes[v]=new Node(v);
			nodes[v].noChoiceRemainderAllocation=new int[2];
		}//fitsInside[v]
		
		totalArcs=0;
		
		for(int v=0;v<vTypes;v++){
			nodes[v].generateArcs(fitsInside[v], thisFitsInside[v], fitsInsideMaxFraction[v], nodes);
			totalArcs+=nodes[v].arcs.length;
		}
		
		//graph structure for remainder usage algorithm
		for(int v=0;v<vTypes;v++){
			nodes[vTypesDecreasingArea[v]].fillInMeArcs();;
		}
		
		//node numbers arc numbers
		vMix1HasTheRemainder=new boolean[vTypes];
		nodeArcIndices=new int[totalArcs][2];
		demandConversion=new double[totalArcs];
		allArcs=new Arc[totalArcs];
		totalArcs=0;
		for(int v=0;v<vTypes;v++){
			for(int a=0;a<nodes[vTypesDecreasingArea[v]].arcs.length;a++){
				nodeArcIndices[totalArcs][0]=vTypesDecreasingArea[v];
				nodeArcIndices[totalArcs][1]=a;
				demandConversion[totalArcs]=nodes[vTypesDecreasingArea[v]].arcs[a].maxFraction;
				allArcs[totalArcs]=nodes[vTypesDecreasingArea[v]].arcs[a];
				allArcs[totalArcs].nodeNumberAbove=vTypesDecreasingArea[v];
				allArcs[totalArcs].nodeNumberBelow=nodes[vTypesDecreasingArea[v]].arcs[a].smallerVTypeNode.vType;
				allArcs[totalArcs].overallArcNumber=totalArcs;
				totalArcs++;
			}
		}
		
		currentRecycledRemainderVMix=new int[vTypes];
		bestRecycledRemainderVMix=new int[vTypes];
		
		isRemNode=new boolean[totalArcs];
		
		currentRemainder=new double[vTypes];
		currentRemainderRelaxed=new double[vTypes];
	}
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}*/
	
	void calculateIntersectionVMix(int[] vMix1, int[] vMix2){
		
		counter++;
		
		//System.out.println("counter: "+counter);
		/*if(counter==235){
			System.out.println("counter: "+counter);
			System.out.println("vMix1={");
			
			
			double naiveIntersectionArea=0;
			for(int v=0;v<vTypes;v++){
				System.out.print(vMix1[v]+",");
				naiveIntersectionArea+=(Math.min(vMix1[v], vMix2[v])*vAreas[v]);
			}
			System.out.println("}");
			//
			System.out.println("vMix2={");
			for(int v=0;v<vTypes;v++){
				System.out.print(vMix2[v]+",");
			}
			System.out.println("}");
		}*/
		
		int t1=(int)System.currentTimeMillis();
		int remNodeCount=0;
		
		double area1=0;
		double area2=0;
		
		int[] initialRemainders=new int[vTypes];
		
		for(int v=0;v<vTypes;v++){
			area1+=(vMix1[v]*vAreas[v]);
			area2+=(vMix2[v]*vAreas[v]);
			intersectionVMix[v]=Math.min(vMix1[v], vMix2[v]);
			initialRemainders[v]=vMix2[v]-vMix1[v];
			//System.out.print(remainder+",");
			nodes[v].setInitialRemainders(initialRemainders[v]);
			vMix1HasTheRemainder[v]=nodes[v].vMix1HasTheRemainder;
			//System.out.println(nodes[v].remainder1+","+nodes[v].remainder2);
			
			currentRecycledRemainderVMix[v]=0;
			bestRecycledRemainderVMix[v]=0;
		}
		
		
		
		
		//System.out.println();
		//min and max arc allocation values (remainder 2 counts if remainder 1 can be allocated down to the smaller type
		//The plan is to use the graph structure to enumerate remainder allocations to identify the most efficient usage of remainders
		//Bounding and tree disection are possible means for speed ups. 
		//Bounding: current area weighted remainder +upper bound on remaining asable area weighted remainder< current best recaptured
		//The graph structure could mean that jsut sequetial finding the optimal intersection is not enough. 
		//i.e. Three vehicle mix intersection may benefit from. Pretty sure it should be OK. A proof is a possibility otherwisae this is ( a worth while) heuristic (at worst)
		
		//boolean allocationsPossible=false;
		//int vSizeIndex=-1;//
		for(int v=0;v<vTypes;v++){
			nodes[vTypesDecreasingArea[v]].setMinMaxArcValues();
		}
		
		
		//identify the "no choice remainder allocations"
		//"no choice" remainder allocations are such that
		//	1. a node has no "arcs to me" and all positive arcs from me lead to nodes with no other
		//	2. in arc order allocate remainder to "arcs below me" such that 
		//	they have no other "arcs to me"
		//and furthermore apply this iteratively until no more "no choice" remainder allocations can be identified
		double noChoiceRemainderArea=0;
		boolean noChoiceRemainderAllocationsRemain=true;
		while(noChoiceRemainderAllocationsRemain){
			noChoiceRemainderAllocationsRemain=false;
			//check nodes for no choice situations (size order)
			for(int vv=0;vv<vTypes && !noChoiceRemainderAllocationsRemain;vv++){
				int[] noChoiceRemAllocation=nodes[vTypesDecreasingArea[vv]].allocateNoChoiceRemainder();
				if(noChoiceRemAllocation!=null){
					noChoiceRemainderAllocationsRemain=true;
					//count the remainder in the intersection vehicle mix
					intersectionVMix[noChoiceRemAllocation[0]]+=noChoiceRemAllocation[1];
					//
					noChoiceRemainderArea+=vAreas[noChoiceRemAllocation[0]]*noChoiceRemAllocation[1];
					//update min max values of all nodes
					for(int vvv=0;vvv<vTypes;vvv++){
						nodes[vTypesDecreasingArea[vvv]].setMinMaxArcValues();
					}
				}
			}
		}
		
		//set the initial remainders as those of the node values after allocating the no choice remainders
		for(int v=0;v<vTypes;v++){
			//remainderNode=true;
			//vMix1HasTheRemainder=false;
			if(initialRemainders[v]!=0){
				initialRemainders[v]=(initialRemainders[v]/Math.abs(initialRemainders[v]))*(int)nodes[v].currentRemainder;
			}
			
		}
		
		
		
		
		intersectionArea=0;
		
		//boolean[] isRemNode=new boolean[totalArcs];
		//double[] currentRemainder=new double[vTypes];
		
		
		double LB=0;//current best recycled remainder (vehicle area)
		int[] currentRecycledRemainderVMix=new int[vTypes];
		int[] bestRecycledRemainderVMix=new int[vTypes];
		
		
		
		if(levelOfRelaxation>0){
			
			//HEURISTIC ALGORITHM
			//System.out.println();
			//consider the nodes in decreasing size order
			//if a node above
			/*if(counter==31){
				System.out.println();
			}*/
			/*for(int vv=0;vv<vTypes;vv++){
				
				int v=vTypesDecreasingArea[vv];
				int remainderRecycled=0;
				while((remainderRecycled=nodes[v].yieldRemainderFromNodesAbove())>0){
					bestRecycledRemainderVMix[v]+=remainderRecycled;
					//update minMaxAllocation of all nodes
					for(int vvv=0;vvv<vTypes;vvv++){
						nodes[vTypesDecreasingArea[vvv]].setMinMaxArcValues();
					}
				}
				LB+=bestRecycledRemainderVMix[v]*vAreas[v];
				//System.out.print(intersectionVMix[v]+",");
			}
			double initialLB=LB;
			//reset initial remainders
			for(int v=0;v<vTypes;v++){
				nodes[v].setInitialRemainders(initialRemainders[v]);
				vMix1HasTheRemainder[v]=nodes[v].vMix1HasTheRemainder;
			}
			for(int v=0;v<vTypes;v++){
				nodes[vTypesDecreasingArea[v]].setMinMaxArcValues();
			}*/
			
			//HEURISTIC ALGORITHM
			
			
			
			for(int i=0;i<totalArcs;i++){//vTypesDecreasingArea[v]
				//nodes[v].arcs
				
				
				
				int nodeNumber=nodeArcIndices[i][0];
				int arcNumber=nodeArcIndices[i][1];
				//
				Node aNode=nodes[nodeNumber];//[]
				Arc anArc=aNode.arcs[arcNumber];//
				if(anArc.maxAllocationDemandPerspective>0){
					isRemNode[i]=true;
				}else{
					isRemNode[i]=false;
				}
			}
			//allArcs[0].maxAllocationDemandPerspective=0;
			//
			
			for(int v=0;v<vTypes;v++){
				currentRemainder[v]=nodes[v].currentRemainder;//vTypesDecreasingArea[v]
			}
			
			
			
			
			currentArcNumber=-1;//the arc to try to branch on next
			RemNode rootNode=new RemNode(-1);
			RemNode currentNode=rootNode;
			
			currentRecycledRemainderArea=0;
			
			while(currentNode!=null){
				//find the next arc with a positive supply node
				boolean nextArcBranchFound=false;
				while(!nextArcBranchFound && currentArcNumber+1<totalArcs){
					//does the current remainder of the node above have a non-zero integer supply for the node below
					
					//RemNode parentNode, int demandUnits, double initialParentSupply, double recycledRemainderSoFar
					currentArcNumber++;
					
					
					if(isRemNode[currentArcNumber]){
						int nodeAbove=allArcs[currentArcNumber].nodeNumberAbove;
						int nodeBelow=allArcs[currentArcNumber].nodeNumberBelow;
						//currentRemainder[nodeAbove]
						//currentRemainder[nodeBelow]
								
						double maxFraction=demandConversion[currentArcNumber];
						
						double maxSupplyAmount=Math.min(maxFraction*currentRemainder[nodeBelow], maxFraction*Math.floor(currentRemainder[nodeAbove]/maxFraction));
						int maxDemandUnits=(int)Math.round(maxSupplyAmount/maxFraction);
						
						//double initialParentSupply=currentRemainder[nodeAbove];
						
						
						if(maxDemandUnits>0){
							nextArcBranchFound=true;
							
							boolean branchAdded=false;
							
							//generate branches on currentNode
							//consider branches of demand unit amounts between "maxDemandUnits" and "0"
							//for each generate the child node and calculate a upper bound and the remainder that can be recycled using the remaining arcs
							for(int du=maxDemandUnits;du>=0;du=du-maxDemandUnits){//--
								//generate the child node
								//update currentRemaining for supply and demand nodes
								
								//
								double cRemAbove=currentRemainder[nodeAbove];
								double cRemBelow=currentRemainder[nodeBelow];
								currentRemainder[nodeAbove]-=maxFraction*du;
								currentRemainder[nodeBelow]-=du;
								
								//ub on remaining arcs
								double ub=upperBound3(currentArcNumber+1)+currentRecycledRemainderArea+(du*vAreas[nodeBelow]);
								
								
								
								if(ub>=LB){//= added in case the ub is exactly correct and this current path is the optimal solution being built
									
									remNodeCount++;
									RemNode newRemNode=new RemNode(currentNode, du, maxFraction*du, cRemAbove, cRemBelow, nodeAbove, nodeBelow, currentRecycledRemainderArea, du*vAreas[nodeBelow], currentArcNumber, ub);
									//RemNode newRemNode=new RemNode(currentNode, du, maxFraction*du, currentRemainder[nodeAbove], currentRemainder[nodeBelow], nodeAbove, nodeBelow, currentRecycledRemainderArea, du*vAreas[nodeBelow], currentArcNumber);
								
									
									//add as child node
									currentNode.addChildNodeToLinkedList(newRemNode);
									//
									branchAdded=true;
									
									
								}
								currentRemainder[nodeAbove]=cRemAbove;
								currentRemainder[nodeBelow]=cRemBelow;
								//undo currentRemainder change that was performed for the ub calculation
								//currentRemainder[nodeAbove]=newRemNode.initialSupplyRemainder;
								//currentRemainder[nodeBelow]=newRemNode.initialDemandRemainder;
								
							}
							//accept the first branch as the current branch and update..
							if(branchAdded){
								
								currentNode=currentNode.currentChildNode;
								currentRecycledRemainderArea+=currentNode.recycledAreaContribution;
								currentRemainder[nodeAbove]-=currentNode.supplyUnits;
								currentRemainder[nodeBelow]-=currentNode.initialDemandRemainder;
								
							}
						}
					}
				}
				//
				if(!nextArcBranchFound || currentArcNumber==totalArcs-1){
					
					//is this a new best solution
					if(currentRecycledRemainderArea>LB){
						LB=currentRecycledRemainderArea;
						//obtain the additional vehicle mixes
						for(int v=0;v<vTypes;v++){
							bestRecycledRemainderVMix[v]=0;
						}
						
						RemNode aRemNode=currentNode;
						while(aRemNode.arcNumber>-1){
							bestRecycledRemainderVMix[aRemNode.demandNodeNumber]+=aRemNode.demandUnits;
							//
							aRemNode=aRemNode.parentNode;
						}
					}
					
					//update current node (nextChildNode of currentNode.parentNode...)
					currentNode=updateCurrentNode(currentNode, LB);
					
					
				}
				
			}
		}
		
		
		
		
		
		
		
		
		//
		for(int v=0;v<vTypes;v++){
			intersectionVMix[v]+=bestRecycledRemainderVMix[v];
			intersectionArea+=intersectionVMix[v]*vAreas[v];
		}
		
		
		
		//HEURISTIC ALGORITHM
		//System.out.println();
		//consider the nodes in decreasing size order
		//if a node above
		/*for(int vv=0;vv<vTypes;vv++){
			int v=vTypesDecreasingArea[vv];
			int remainderRecycled=0;
			while((remainderRecycled=nodes[v].yieldRemainderFromNodesAbove())>0){
				intersectionVMix[v]+=remainderRecycled;
			}
			intersectionArea+=intersectionVMix[v]*vAreas[v];
			//System.out.print(intersectionVMix[v]+",");
		}*/
		//HEURISTIC ALGORITHM
		
		int t2=(int)System.currentTimeMillis();
		
		/*System.out.println(counter+"::"+remNodeCount+"::"+(t2-t1)+"::"+(LB+noChoiceRemainderArea)+"::"+noChoiceRemainderArea);//+"::"+initialLB
		
		if(counter==1000){
			System.out.println();
		}*/
		if(intersectionArea-area1>0.0000000001 || intersectionArea-area2>0.0000000001){
			System.out.println("defintely incorrect");
		}
		
		//System.out.println();
	}
	
	//get next feasible path (only called if at least one feasible path exist for this tree
	/*boolean getNextFeasiblePath(){//if a feasible path exists, the output will involve an array of rectangle of the fron of the queues that will be the input for the next tree for the next strip
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
	}*/
	
	//branch on current tree Node (input: rectangles not yet covered in the strip, (feasibility 
	/*void branchOnTreeNode(BinTreeNode parentNode){
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
	}*/
	
	//restore currentRemainder...
	
	RemNode updateCurrentNode(RemNode currentNode, double LB){
		boolean nextCurrentNodeFound=false;
		while(!nextCurrentNodeFound && currentNode!=null){
			//restore the state to that prior to current node
			currentRemainder[currentNode.supplyNodeNumber]=currentNode.initialSupplyRemainder;
			currentRemainder[currentNode.demandNodeNumber]=currentNode.initialDemandRemainder;
			currentArcNumber=currentNode.arcNumber;
			currentRecycledRemainderArea=currentNode.recycledRemainderBeforeThis;
			
			
			//try the next child node of the parent node
			currentNode=currentNode.parentNode;
			
			
			
			//may have tried to backtrack from the root node, in which case the solution has been found
			if(currentNode!=null){
				currentNode.currentChildNode=currentNode.currentChildNode.nextChildNode;
				//if null backtrack
				while(!nextCurrentNodeFound && currentNode.currentChildNode!=null){
					if(currentNode.currentChildNode.ub>=LB){
						nextCurrentNodeFound=true;
						//increment the current child node of the parent node
						//currentNode.currentChildNode=currentNode.currentChildNode.nextChildNode;
						//
						currentNode=currentNode.currentChildNode;
						
						currentRemainder[currentNode.supplyNodeNumber]-=currentNode.supplyUnits;
						currentRemainder[currentNode.demandNodeNumber]-=currentNode.demandUnits;
						currentArcNumber=currentNode.arcNumber;
						currentRecycledRemainderArea=currentNode.recycledRemainderBeforeThis+currentNode.recycledAreaContribution;
					}else{
						currentNode.currentChildNode=currentNode.currentChildNode.nextChildNode;
					}
				}
				
				
				if(!nextCurrentNodeFound){
					//so that the entire tree is not stored (dereference for garbage collection)
					currentNode.currentChildNode=null;
					currentNode.headChildNode=null;
					currentNode.tailChildNode=null;
				}
			}
		}
		//
		return currentNode;
	}
	
	RemNode updateCurrentNode2(RemNode currentNode, double LB){
		boolean nextCurrentNodeFound=false;
		while(!nextCurrentNodeFound && currentNode!=null){
			//restore the state to that prior to current node
			currentRemainder[currentNode.supplyNodeNumber]=currentNode.initialSupplyRemainder;
			currentRemainder[currentNode.demandNodeNumber]=currentNode.initialDemandRemainder;
			currentArcNumber=currentNode.arcNumber;
			currentRecycledRemainderArea=currentNode.recycledRemainderBeforeThis;
			
			
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
					
					currentRemainder[currentNode.supplyNodeNumber]-=currentNode.supplyUnits;
					currentRemainder[currentNode.demandNodeNumber]-=currentNode.demandUnits;
					currentArcNumber=currentNode.arcNumber;
					currentRecycledRemainderArea=currentNode.recycledRemainderBeforeThis+currentNode.recycledAreaContribution;
					
				}else{
					//so that the entire tree is not stored (dereference for garbage collection)
					currentNode.currentChildNode=null;
					currentNode.headChildNode=null;
					currentNode.tailChildNode=null;
				}
			}
		}
		//
		return currentNode;
	}
	
	//update current remaining for each node (this is why its useful to store the initial values in the childNodes
	double upperBound2(int currentArcNumber){
		double ub=0;
		
		double vMix1RemArea=0;
		double vMix2RemArea=0;
		
		//copy currentRemainder and iteraative decrease after each group of arcs
		
		//for each node in decreasing area order serve maximum amount of demand for each arc from that node
		//thenupdate the current remainder of all concerned
		
		for(int v=0;v<vTypes;v++){
			currentRemainderRelaxed[v]=currentRemainder[v];
			//nodes[vTypesDecreasingArea[v]].arcs
		}
		
		for(int v=0;v<vTypes;v++){
			int nodeNum=vTypesDecreasingArea[v];
			if(currentRemainderRelaxed[nodeNum]>0){
				
				Arc[] arcsA=nodes[nodeNum].arcs;
				double sumOfAllocatableDemand=0;
				for(int a=0;a<arcsA.length;a++){
					Arc arc=arcsA[a];
					int nodeBelow=arc.nodeNumberBelow;
					if(arc.overallArcNumber>=currentArcNumber && currentRemainderRelaxed[nodeBelow]>0){
						
						//currentRemainder[nodeAbove]
						//currentRemainder[nodeBelow]
								
						double maxFraction=arc.maxFraction;
						
						double maxSupplyAmount=Math.min(maxFraction*currentRemainderRelaxed[nodeBelow], maxFraction*Math.floor(currentRemainderRelaxed[nodeNum]/maxFraction));
						int maxDemandUnits=(int)Math.round(maxSupplyAmount/maxFraction);
						ub+=vAreas[nodeBelow]*maxDemandUnits;
						//
						currentRemainderRelaxed[nodeBelow]=Math.max(0, currentRemainderRelaxed[nodeBelow]-maxDemandUnits);
						//
						sumOfAllocatableDemand+=maxSupplyAmount;
					}
					
				}
				//update currentRemainderRelaxed
				//currentRemainderRelaxed[nodeNum]=Math.max(0, currentRemainderRelaxed[nodeNum]-sumOfAllocatableDemand);
			}
			
		}

		
		//
		//ub=Math.min(vMix1RemArea, vMix2RemArea);
		
		//
		return ub;
	}
	
	double upperBound(int currentArcNumber){
		double ub=0;
		
		double vMix1RemArea=0;
		double vMix2RemArea=0;
		
		//copy currentRemainder and iteraative decrease after each group of arcs
		
		//for each node in decreasing area order serve maximum amount of demand for each arc from that node
		//thenupdate the current remainder of all concerned
		
		/*for(int v=0;v<vTypes;v++){
			currentRemainderRelaxed[v]=currentRemainder[v];
			//nodes[vTypesDecreasingArea[v]].arcs
		}*/

		while(currentArcNumber<totalArcs){
			//does the current remainder of the node above have a non-zero integer supply for the node below
			
			//RemNode parentNode, int demandUnits, double initialParentSupply, double recycledRemainderSoFar
			
			
			if(isRemNode[currentArcNumber]){
				int nodeAbove=allArcs[currentArcNumber].nodeNumberAbove;
				int nodeBelow=allArcs[currentArcNumber].nodeNumberBelow;
				//currentRemainder[nodeAbove]
				//currentRemainder[nodeBelow]
						
				double maxFraction=demandConversion[currentArcNumber];
				
				double maxSupplyAmount=Math.min(maxFraction*currentRemainder[nodeBelow], maxFraction*Math.floor(currentRemainder[nodeAbove]/maxFraction));
				int maxDemandUnits=(int)Math.round(maxSupplyAmount/maxFraction);
				ub+=vAreas[nodeBelow]*maxDemandUnits;
				//if(vMix1HasTheRemainder[nodeBelow]){
					//vMix1RemArea+=vAreas[nodeBelow]*maxDemandUnits;
				//}else{
					//vMix2RemArea+=vAreas[nodeBelow]*maxDemandUnits;
				//}
			}
			currentArcNumber++;
		}
		//
		//ub=Math.min(vMix1RemArea, vMix2RemArea);
		
		//
		return ub;
	}

	
	double upperBound3(int currentArcNumber){
		double ub=0;
		
		double vMix1RemArea=0;
		double vMix2RemArea=0;
		
		//copy currentRemainder and iteraative decrease after each group of arcs
		
		//for each node in decreasing area order serve maximum amount of demand for each arc from that node
		//thenupdate the current remainder of all concerned
		
		for(int v=0;v<vTypes;v++){
			currentRemainderRelaxed[v]=currentRemainder[v];
			//nodes[vTypesDecreasingArea[v]].arcs
		}

		while(currentArcNumber<totalArcs){
			//does the current remainder of the node above have a non-zero integer supply for the node below
			
			//RemNode parentNode, int demandUnits, double initialParentSupply, double recycledRemainderSoFar
			
			
			if(isRemNode[currentArcNumber]){
				int nodeAbove=allArcs[currentArcNumber].nodeNumberAbove;
				int nodeBelow=allArcs[currentArcNumber].nodeNumberBelow;
				//currentRemainder[nodeAbove]
				//currentRemainder[nodeBelow]
						
				double maxFraction=demandConversion[currentArcNumber];
				
				double maxSupplyAmount=Math.min(maxFraction*currentRemainderRelaxed[nodeBelow], maxFraction*Math.floor(currentRemainderRelaxed[nodeAbove]/maxFraction));
				int maxDemandUnits=(int)Math.round(maxSupplyAmount/maxFraction);
				ub+=vAreas[nodeBelow]*maxDemandUnits;
				//if(vMix1HasTheRemainder[nodeBelow]){
					//vMix1RemArea+=vAreas[nodeBelow]*maxDemandUnits;
				//}else{
					//vMix2RemArea+=vAreas[nodeBelow]*maxDemandUnits;
				//}
				currentRemainderRelaxed[nodeBelow]-=maxDemandUnits;
			}
			currentArcNumber++;
		}
		//
		//ub=Math.min(vMix1RemArea, vMix2RemArea);
		
		//
		return ub;
	}
}
