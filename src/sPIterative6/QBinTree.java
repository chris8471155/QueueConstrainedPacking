package sPIterative6;

import java.util.ArrayList;
/*import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;*/

public class QBinTree {
	static int vTypes;
	static int numberOfQueues;
	static int numberOfBins;
	static double[][] rectangleDimensions;
	
	int counter;
	
	//int[] vMix;//in strip
	double[] binLengths;
	double[] binWidths;
	boolean[] isRowBin;
	
	//dot reducing references
	int parentCurrentBinNumber;
	double[] parentBinRemLengths;
	double[] parentBinRemLengths2;
	Rectangle qFrontRectRef;
	Rectangle qFrontRectRef2;
	Rectangle qFrontRectRef3;
	
	
	///////////////////////////////
	///////////////////////////////
	//lower bound calculation fields...
	double Tol=0.000000000000001;
	double[] vAreas=new double[vTypes];
	int[] vMix=new int[vTypes];
	double[] vRemainingRelaxed=new double[vTypes];
	
	
	//minimise v remaining area
	
	
	//variables for streamlining LB calculation
	double[] effectiveBinLength=new double[numberOfBins];
	double[][] effectiveVehicleLength=new double[numberOfBins][vTypes];
	double[] remainingRelaxedLength=new double[numberOfBins];
	double[][] relaxedAllocation=new double[numberOfBins][vTypes];//units
	
	//improved lb2
	double[][] usedLaneLengthPerQueueEarly=new double[numberOfQueues][numberOfBins];
	double[][] usedLaneLengthPerQueueLate=new double[numberOfQueues][numberOfBins];
	double[][] minOverlapArea=new double[numberOfQueues][numberOfQueues+1];
	int[][][] numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueEarly=new int[numberOfQueues][numberOfBins][vTypes];
	int[][][] numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueLate=new int[numberOfQueues][numberOfBins][vTypes];
	
	//fits matrix
	boolean[][] fits=new boolean[numberOfBins][vTypes];
	//efficiency matrix
	double[][] efficiencyRate=new double[numberOfBins][vTypes];
	//
	boolean[] isRowType=new boolean[numberOfBins];
	int numColBins=0;
	int numRowBins=0;
	
	//The second constraint might need to removed to ensure an UB
	double[] binAreas=new double[numberOfBins];
	
	
	double[] colWidths;
	double[] rowLengths;
	int[] colBinIndices;
	int[] rowBinIndices;
	
	int[] colBinOrder;
	int[] rowBinOrder;
	
	//efficiency order
	int[][] VOrders=new int[2][vTypes];//indices of vehicle type sorted by width and length
	double[] vLengths=new double[vTypes];;
	double[] vWidths=new double[vTypes];;
	int[] vCounts;
	
	///////////////////////////////
	///////////////////////////////
	
	
	//problem instance
	Bin[] bins;
	Queue[] Qs;
	
	QBinTreeNode currentNode;
	
	QBinTreeNode rootNode;//queue number -1
	
	//boolean binsFull;
	boolean leavesRemain=true;
	
	//
	double overallUpperBound;//the minimum area of non-packed vehicles of any complete solution obtained so far
	double internalUpperBound=Double.MAX_VALUE;
	boolean newBestOverallSolutionFound=false;
	int[][] n;
	int[][] queuePopOrder;
	//int queuesPopped;
	
	//MinOverlapTree MOT=new MinOverlapTree();
	
	//LB4//
	//LB4Var head=null;
	//LB4Var tail=null;
	//IloCplex LP;
	
	//tree constructor: current queues,
	QBinTree(Bin[] bins, Queue[] Qs, double externalUB) throws Exception{//vCounts is the number of vehicles of each type in the strip this tree is being built (to find a feasible queue path)
		//LB4//
		//LP=new IloCplex();
		//n=new int[numberOfBins][vTypes];
		queuePopOrder=new int[1][1];
		queuePopOrder[0][0]=-1;
		//
		overallUpperBound=externalUB;
		//
		rootNode=new QBinTreeNode(bins, Qs);
		//
		currentNode=rootNode;
		//
		this.Qs=Qs;
		this.bins=bins;
		//
		binLengths=new double[numberOfBins];
		binWidths=new double[numberOfBins];
		isRowBin=new boolean[numberOfBins];
		for(int b=0;b<numberOfBins;b++){
			binLengths[b]=bins[b].l;
			binWidths[b]=bins[b].w;
			if(bins[b].orientation==0){
				isRowBin[b]=true;
			}
		}
		
		//lb1
		///////////////////////////
		/////////////////////////////
		vCounts=new int[vTypes];
		
		for(int i=0;i<vTypes;i++){
			vAreas[i]=(rectangleDimensions[0][i]*rectangleDimensions[1][i]);
			vLengths[i]=rectangleDimensions[0][i];
			vWidths[i]=rectangleDimensions[1][i];
		}
		
		for(int q=0;q<numberOfQueues;q++){
			Rectangle currentRectangle=Qs[q].rectAtFront;
			while(currentRectangle!=null){
				vMix[currentRectangle.type]++;
				//
				currentRectangle=currentRectangle.nextRectInQueue;
			}
		}
		
		//
		for(int v=0;v<vTypes;v++){
			vRemainingRelaxed[v]=vMix[v];
		}
		//
		for(int b=0;b<numberOfBins;b++){
			if(bins[b].orientation==0){//row
				effectiveBinLength[b]=bins[b].w;
				for(int v=0;v<vTypes;v++){
					effectiveVehicleLength[b][v]=rectangleDimensions[1][v];
				}
			}else{
				effectiveBinLength[b]=bins[b].l;
				for(int v=0;v<vTypes;v++){
					effectiveVehicleLength[b][v]=rectangleDimensions[0][v];
				}
			}
		}
		
		for(int b=0;b<numberOfBins;b++){
			//
			binAreas[b]=bins[b].w*bins[b].l;
			if(bins[b].orientation==0){//row
				isRowType[b]=true;
				numRowBins++;
				//
				for(int v=0;v<vTypes;v++){
					if(rectangleDimensions[0][v]<=bins[b].l && rectangleDimensions[1][v]<=bins[b].w){
						fits[b][v]=true;
						efficiencyRate[b][v]=(rectangleDimensions[0][v]/bins[b].l);
					}
				}	
			}else{//column (or first constraint)
				numColBins++;
				//
				for(int v=0;v<vTypes;v++){
					if(rectangleDimensions[0][v]<=bins[b].l && rectangleDimensions[1][v]<=bins[b].w){
						fits[b][v]=true;
						efficiencyRate[b][v]=(rectangleDimensions[1][v]/bins[b].w);
					}
				}
			}
			
		}
		
		colWidths=new double[numColBins];
		rowLengths=new double[numRowBins];
		colBinIndices=new int[numColBins];
		rowBinIndices=new int[numRowBins];
		numColBins=0;
		numRowBins=0;
		for(int b=numberOfBins-1;b>=0;b--){
			if(bins[b].orientation>0){//row
				colWidths[numColBins]=bins[b].l;
				colBinIndices[numColBins]=b;
				numColBins++;
			}else{
				rowLengths[numRowBins]=bins[b].w;
				rowBinIndices[numRowBins]=b;
				numRowBins++;
			}
		}
		colBinOrder=maths.Sort(colWidths);
		rowBinOrder=maths.Sort(rowLengths);
		
		colBinIndices=maths.inThisOrder(colBinOrder, colBinIndices);
		rowBinIndices=maths.inThisOrder(rowBinOrder, rowBinIndices);
		
		
		//efficiency order
		VOrders=new int[2][vTypes];//indices of vehicle type sorted by width and length
		VOrders[0]=maths.indexOrder(rectangleDimensions[0], true);
		VOrders[1]=maths.indexOrder(rectangleDimensions[1], true);
		///////////////////////////////
		//////////////////////////////
		internalUpperBound=Double.MAX_VALUE;
		//build initial path
		//generate the child nodes for the first queue
		//
		//boolean feasiblePathFound=false;//!feasiblePathFound
		boolean finished=false;
		while(currentNode!=null && !finished){
			//feasible path found
			if(currentNode.binsFull){//note last queue (treeNode) can only exist it is exactly satisfies the strip requirements
				//feasiblePathFound=true;
				finished=true;
				//note: the queue information for the next tree/strip can be gathered from the parentNode relations from the currentNode
				//
				//print the solution
				QBinTreeNode currentQBTN=currentNode;
				//System.out.print(currentNode.lb+",");
				while(currentQBTN.rect!=null){
					//System.out.print("("+currentQBTN.queueNumber+","+currentQBTN.currentBinNumber+"),");
					//
					currentQBTN=currentQBTN.parentNode;
				}
				//System.out.println();
			}else{
				//try to branch (if a child node then exists set it as the cuurentNode
				branchOnTreeNode(currentNode);
				if(currentNode.currentChildNode!=null){
					currentNode=currentNode.currentChildNode;
				}else{
					//if it does not exist then try the next child node of the parent node
					/*if(currentNode.lb>=overallUpperBound){
						if((currentNode=updateCurrentNode(currentNode))==null){
							finished=true;
							leavesRemain=false;
						}
					}*/
					/*if((currentNode=updateCurrentNode(currentNode))==null){
						finished=true;
						leavesRemain=false;
					}*/
				}
			}
		}
	}
	
	//FLEXIPAX (if the leaf node had flexibility and the current node was not for the last queue then try branching on the current node
	
	//get next feasible path (only called if at least one feasible path exist for this tree
	boolean getNextFeasiblePath(){// throws IloExceptionif a feasible path exists, the output will involve an array of rectangle of the fron of the queues that will be the input for the next tree for the next strip
		//
		if((currentNode=updateCurrentNode(currentNode))==null){
			leavesRemain=false;
		}else{
			//a back track to a different child node was possible
			//now try to find the next feasible path through this tree
			boolean finished=false;
			while(currentNode!=null && !finished){
				//feasible path found
				if(currentNode.binsFull){//note last queue (treeNode) can only exist it is exactly satisfies the strip requirements
					//feasiblePathFound=true;
					finished=true;
					//print the solution
					/*QBinTreeNode currentQBTN=currentNode;
					System.out.print(currentNode.lb+",");
					while(currentQBTN.rect!=null){
						System.out.print("("+currentQBTN.queueNumber+","+currentQBTN.currentBinNumber+"),");
						//
						currentQBTN=currentQBTN.parentNode;
					}
					System.out.println();*/
					//note: the queue information for the next tree/strip can be gathered from the parentNode relations from the currentNode
				}else{
					//try to branch (if a child node then exists set it as the cuurentNode
					branchOnTreeNode(currentNode);
					if(currentNode.currentChildNode!=null){
						currentNode=currentNode.currentChildNode;
					}else{
						//if it does not exist then try the next child node of the parent node
						//System.out.println(currentNode.lb+","+overallUpperBound);
						if(currentNode.lb>=overallUpperBound){
							if((currentNode=updateCurrentNode(currentNode))==null){
								finished=true;
								leavesRemain=false;
							}else{
								/*QBinTreeNode currentQBTN=currentNode;
								System.out.print(currentNode.lb+",");
								while(currentQBTN.rect!=null){
									System.out.print("("+currentQBTN.queueNumber+","+currentQBTN.currentBinNumber+"),");
									//
									currentQBTN=currentQBTN.parentNode;
								}
								System.out.println();*/
							}
						}
						
					}
				}
			}
		}
		return leavesRemain;
	}
	
	//branch on current tree Node (input: rectangles not yet covered in the strip, (feasibility 
	void branchOnTreeNode(QBinTreeNode parentNode){// throws IloException
		//
		counter++;
		
		if(counter==1){
			System.out.println(counter);
		}
		
		
		//Consider popping each queue,
		//check which bin will be used next
		//compute lbs
		//create node if the node is not bounded
		//if no vehicles can be added at all check if the overall upper bound has improved
		boolean atLeastOneChildNodeGenerated=false;
		//
		parentCurrentBinNumber=parentNode.currentBinNumber;
		parentBinRemLengths=parentNode.remLenPerBinAfterThis;
		
		boolean somethingFitsInTheSameBin=false;
		
		int initialQueueNumber=parentNode.queueNumber;
		
		for(int q=initialQueueNumber;q<numberOfQueues;q++){
			qFrontRectRef=parentNode.qFronts[q];
			if(qFrontRectRef!=null){
				//
				boolean nextBinFound=false;
				if(isRowBin[parentCurrentBinNumber]){
					if(parentBinRemLengths[parentCurrentBinNumber]>=qFrontRectRef.w && binLengths[parentCurrentBinNumber]>=qFrontRectRef.l){
						somethingFitsInTheSameBin=true;
						nextBinFound=true;
					}
				}else{
					if(parentBinRemLengths[parentCurrentBinNumber]>=qFrontRectRef.l && binWidths[parentCurrentBinNumber]>=qFrontRectRef.w){
						somethingFitsInTheSameBin=true;
						nextBinFound=true;
					}
				}
				
				
				//
				if(nextBinFound){
					//compute LBs
					//...
					//double lb0=lowerBound0(parentNode, nextBin, qFrontRectRef);
					double lb1=lowerBound1(parentNode, parentCurrentBinNumber, qFrontRectRef);
					if(lb1<overallUpperBound){
						//compute lb2
						double lb2=lowerBound2(parentNode, parentCurrentBinNumber, qFrontRectRef, q);
						if(lb2<overallUpperBound){
							//compute lb4
							//LB4//
							//double lb4=lowerBound4(parentNode, nextBin, qFrontRectRef, q);
							//if(lb4<overallUpperBound){
								atLeastOneChildNodeGenerated=true;
								//add child node and compute "placementEfficiencyScore"
								//
								//int queuePopped, Rectangle rect, QBinTreeNode parentNode, int binNumber, boolean isRowBin, double placementEfficiencyScore
								QBinTreeNode newChildNode=new QBinTreeNode(q, qFrontRectRef, parentNode, parentCurrentBinNumber, isRowBin[parentCurrentBinNumber], efficiencyRate[parentCurrentBinNumber][qFrontRectRef.type]);
								//add it
								newChildNode.lb=Math.max(lb1, lb2);//LB4//Math.max(lb2, lb4)
								parentNode.addChildNodeToLinkedList(newChildNode);
							//}
								/*else{
								System.out.println("lb4 useful");
								//else: no child node added
							}*/
						}/*else{
							System.out.println("lb2 useful");
							//else: no child node added
						}*///else: no child node added
					}//else: no child node added
				}
			}
			
		}
		
		if(!somethingFitsInTheSameBin){
			//This the same as starting a new tree (as in feasibility check) but different in a number of ways
			//(In the queue constrained case a bin a characterised by the number of times each queue has been popped
			//as well as those for the prior bins)
			//So for nodes within a bin; branch on the same queue number and higher (to capture combinations (as opposed to permutations)
			//The following uses parent nodes where no more queues can be popped into
			//the same bin to model the root node of a new bin
			//The condition above corresponds to placing rectangles in bins when there is no point in not doing so
			//Another difference is that the next queue pop may not even be into the next bin
			for(int q=0;q<numberOfQueues;q++){
				qFrontRectRef=parentNode.qFronts[q];
				if(qFrontRectRef!=null){
					
					//next bin (which the next vehicle popped from this queue fits into)
					boolean nextBinFound=false;
					int nextBin=0;
					for(int nb=parentCurrentBinNumber+1;nb<numberOfBins && !nextBinFound;nb++){
						if(isRowBin[nb]){
							if(parentBinRemLengths[nb]>=qFrontRectRef.w && binLengths[nb]>=qFrontRectRef.l){
								nextBinFound=true;
								nextBin=nb;
							}
						}else{
							if(parentBinRemLengths[nb]>=qFrontRectRef.l && binWidths[nb]>=qFrontRectRef.w){
								nextBinFound=true;
								nextBin=nb;
							}
						}
					}
					//
					if(nextBinFound){
						//compute LBs
						//...
						//double lb0=lowerBound0(parentNode, nextBin, qFrontRectRef);
						double lb1=lowerBound1(parentNode, nextBin, qFrontRectRef);
						if(lb1<overallUpperBound){
							//compute lb2
							double lb2=lowerBound2(parentNode, nextBin, qFrontRectRef, q);
							if(lb2<overallUpperBound){
								//compute lb4
								//LB4//
								//double lb4=lowerBound4(parentNode, nextBin, qFrontRectRef, q);
								//if(lb4<overallUpperBound){
									atLeastOneChildNodeGenerated=true;
									//add child node and compute "placementEfficiencyScore"
									//
									//int queuePopped, Rectangle rect, QBinTreeNode parentNode, int binNumber, boolean isRowBin, double placementEfficiencyScore
									QBinTreeNode newChildNode=new QBinTreeNode(q, qFrontRectRef, parentNode, nextBin, isRowBin[nextBin], efficiencyRate[nextBin][qFrontRectRef.type]);
									//add it
									newChildNode.lb=Math.max(lb1, lb2);//LB4//Math.max(lb2, lb4)
									parentNode.addChildNodeToLinkedList(newChildNode);
								//}
									/*else{
									System.out.println("lb4 useful");
									//else: no child node added
								}*/
							}/*else{
								System.out.println("lb2 useful");
								//else: no child node added
							}*///else: no child node added
						}//else: no child node added
					}
				}
				
			}
		}
		
		if(atLeastOneChildNodeGenerated){
			//set current child node
			parentNode.currentChildNode=parentNode.headChildNode;
		}else{
			parentNode.binsFull=true;
			if(parentNode.parentNode!=null){
				//
				
				//valid integer solution (with improved
				//calculate the new upper bound
				parentNode.upperBound=0;
				for(int q=0;q<numberOfQueues;q++){
					qFrontRectRef=parentNode.qFronts[q];
					//
					while(qFrontRectRef!=null){
						parentNode.upperBound+=vAreas[qFrontRectRef.type];
						//
						qFrontRectRef=qFrontRectRef.nextRectInQueue;
					}
				}
				parentNode.lb=parentNode.upperBound;
				//is this definitely a new best solution (just because the lbs were not greater than the overall upper bound and no more vehicle can be added whilst respect queue orders
				//internalUpperBound
				if(parentNode.upperBound<internalUpperBound){
					internalUpperBound=parentNode.upperBound;
					if(internalUpperBound<overallUpperBound){
						overallUpperBound=internalUpperBound;
						newBestOverallSolutionFound=true;
					}
						//store the best overall solution in terms of int[][] n
						n=new int[numberOfBins][vTypes];
						if(parentNode.queuePopNumber<1){
							System.out.println();
						}
						queuePopOrder=new int[parentNode.queuePopNumber][2];
						//queuesPopped=parentNode.queuePopNumber;
						QBinTreeNode currentQBTN=parentNode;
						int qpn=parentNode.queuePopNumber-1;
						while(currentQBTN.rect!=null){
							queuePopOrder[qpn][0]=currentQBTN.queueNumber;
							queuePopOrder[qpn][1]=currentQBTN.currentBinNumber;
							qpn--;
							n[currentQBTN.currentBinNumber][currentQBTN.rect.type]++;
							//
							currentQBTN=currentQBTN.parentNode;
						}
					
				}
			}
			
		}
	}
	
	QBinTreeNode updateCurrentNode(QBinTreeNode currentNode){
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
				}else{
					currentNode.headChildNode=null;
					currentNode.tailChildNode=null;
					currentNode.currentChildNode=null;
				}
			}
		}
		//
		return currentNode;
	}
	
	double lowerBound0(QBinTreeNode parentNode, int currentBinIndex, Rectangle rect){
		double lb=0;
		//is there feasible move available
		return lb;
	}
	
	//compute lb1 for the parent node with rect added to currentBinIndex
	double lowerBound1(QBinTreeNode parentNode, int currentBinIndex, Rectangle rect){
		double lb=0;
		//compute lb on the final remaining vehicle area
		///////////////////////////////
		///////////////////////////////
		///////////////////////////////
		///////////////////////////relaxedAllocation
		//Does this lb rely on the assumption that the area of vehicles is greater than the area of bins
		//not anymore because it stops if no more vehicles remain to be packed
		for(int b=0;b<numberOfBins;b++){
			remainingRelaxedLength[b]=effectiveBinLength[b];
			for(int v=0;v<vTypes;v++){
				relaxedAllocation[b][v]=0;
			}
		}
		//
		if(isRowBin[currentBinIndex]){
			remainingRelaxedLength[currentBinIndex]-=rect.w;
		}else{
			remainingRelaxedLength[currentBinIndex]-=rect.l;
		}
		//
		for(int v=0;v<vTypes;v++){
			vRemainingRelaxed[v]=parentNode.remVMixAfterThis[v];
		}
		vRemainingRelaxed[rect.type]-=1;
		
		//calculate an initial lb
		//boolean lBComputed=false;
		//int counter=0;
		boolean moveFound=true;
		while(moveFound){
			//System.out.println(counter++);
			moveFound=false;
			//this should be simplex (use the most efficient move until a shadow cost changes
			//Hungarian algorithm?
			//check for the next most efficient vehicle to bin allocation
			//if non exists check for unused space in any bin, 
			//check if a swap is useful (i.e. move something from another  bin that can use this space (
			//Most efficient next move
			//-efficiency rate, remaining space
			
			int binChoice=-1;
			int vehicleChoice=-1;
			double maxEfficiency=0;//+1
			for(int b=currentBinIndex;b<numberOfBins;b++){//+1
				if(remainingRelaxedLength[b]>Tol){
					for(int v=0;v<vTypes;v++){
						if(vRemainingRelaxed[v]>Tol){
							if(efficiencyRate[b][v]>maxEfficiency){
								maxEfficiency=efficiencyRate[b][v];
								binChoice=b;
								vehicleChoice=v;
								moveFound=true;
							}
						}
					}
				}
			}
			//marginal value
			if(moveFound){
				//determine the amount of the vehicle type that can be allocated to the selected lane
				double allocatedUnits=Math.min(vRemainingRelaxed[vehicleChoice], remainingRelaxedLength[binChoice]/effectiveVehicleLength[binChoice][vehicleChoice]);
				vRemainingRelaxed[vehicleChoice]-=allocatedUnits;
				remainingRelaxedLength[binChoice]-=(allocatedUnits*effectiveVehicleLength[binChoice][vehicleChoice]);
				relaxedAllocation[binChoice][vehicleChoice]+=allocatedUnits;
			}else{
				//check if there are any vehicles remaining to be packed
				boolean vehiclesRemain=false;
				for(int v=0;v<vTypes;v++){
					if(vRemainingRelaxed[v]>Tol){
						vehiclesRemain=true;
					}
				}
				if(vehiclesRemain){
					//check for unused lane space
					//if so find if any vehicles already allocated can be assigned to this empty space whilst making romm for any unpacked vehicle length
					boolean unusedBinSpaceFound=false;
					for(int cb=0;cb<numColBins && !unusedBinSpaceFound;cb++){
						if(remainingRelaxedLength[colBinIndices[cb]]>Tol && colBinIndices[cb]>=currentBinIndex){//the relaxed packed bins are those after the current bin
							unusedBinSpaceFound=true;
							//
							//find the widest vehicle in a row bin that can be reallocated to this bin
							int reallocatableVType=-1;
							int reallocatableBinIndex=-1;
							double maxWidth=0;
							for(int rb=0;rb<numRowBins;rb++){
								if(currentBinIndex>=rowBinIndices[rb]){
									for(int v=0;v<vTypes;v++){
										if(maxWidth<rectangleDimensions[1][v] && colWidths[cb]>=rectangleDimensions[1][v] && relaxedAllocation[rowBinIndices[rb]][v]>Tol){
											maxWidth=rectangleDimensions[1][v];
											reallocatableVType=v;
											reallocatableBinIndex=rowBinIndices[rb];
										}
									}
								}
							}
							//
							if(reallocatableVType>-1){
								//there has to be something that can be packed to "reallocatableBinIndex"
								boolean thisMakesRoomForSomethingNotPacked=false;
								for(int v=0;v<vTypes && !thisMakesRoomForSomethingNotPacked;v++){
									if(vRemainingRelaxed[v]>Tol && fits[reallocatableBinIndex][v]){
										thisMakesRoomForSomethingNotPacked=true;
									}
								}
								
								if(thisMakesRoomForSomethingNotPacked){
									moveFound=true;
								}
								
								//then perform this reallocation
								double reallocationAmount=Math.min((remainingRelaxedLength[colBinIndices[cb]]/rectangleDimensions[0][reallocatableVType]), relaxedAllocation[reallocatableBinIndex][reallocatableVType]);
								relaxedAllocation[reallocatableBinIndex][reallocatableVType]-=reallocationAmount;
								relaxedAllocation[colBinIndices[cb]][reallocatableVType]+=reallocationAmount;
								remainingRelaxedLength[colBinIndices[cb]]-=(reallocationAmount*rectangleDimensions[0][reallocatableVType]);
								remainingRelaxedLength[reallocatableBinIndex]+=(reallocationAmount*rectangleDimensions[1][reallocatableVType]);
							}
						}
					}
					//try the same for row bins
					if(!moveFound){
						unusedBinSpaceFound=false;
						for(int rb=0;rb<numRowBins && !unusedBinSpaceFound;rb++){
							if(remainingRelaxedLength[rowBinIndices[rb]]>Tol && rowBinIndices[rb]>=currentBinIndex){
								unusedBinSpaceFound=true;
								//
								//find the longest vehicle in a column bin that can be reallocated to this bin
								int reallocatableVType=-1;
								int reallocatableBinIndex=-1;
								double maxLength=0;
								for(int cb=0;cb<numColBins;cb++){
									if(colBinIndices[cb]>=currentBinIndex){
										for(int v=0;v<vTypes;v++){
											if(maxLength<rectangleDimensions[0][v] && rowLengths[rb]>=rectangleDimensions[0][v] && relaxedAllocation[colBinIndices[cb]][v]>Tol){
												maxLength=rectangleDimensions[0][v];
												reallocatableVType=v;
												reallocatableBinIndex=colBinIndices[cb];
												
											}
										}
									}
								}
								//
								if(reallocatableVType>-1){
									//there has to be something that can be packed to "reallocatableBinIndex"
									boolean thisMakesRoomForSomethingNotPacked=false;
									for(int v=0;v<vTypes && !thisMakesRoomForSomethingNotPacked;v++){
										if(vRemainingRelaxed[v]>Tol && fits[reallocatableBinIndex][v]){
											thisMakesRoomForSomethingNotPacked=true;
										}
									}
									
									if(thisMakesRoomForSomethingNotPacked){
										moveFound=true;
									}
									//then perform this reallocation
									double reallocationAmount=Math.min((remainingRelaxedLength[rowBinIndices[rb]]/rectangleDimensions[1][reallocatableVType]), relaxedAllocation[reallocatableBinIndex][reallocatableVType]);
									relaxedAllocation[reallocatableBinIndex][reallocatableVType]-=reallocationAmount;
									relaxedAllocation[rowBinIndices[rb]][reallocatableVType]+=reallocationAmount;
									remainingRelaxedLength[rowBinIndices[rb]]-=(reallocationAmount*rectangleDimensions[1][reallocatableVType]);
									remainingRelaxedLength[reallocatableBinIndex]+=(reallocationAmount*rectangleDimensions[0][reallocatableVType]);
								}
							}
						}
					}
		
				}
				
			}
			//
			//Calculate LBs for row and column bin separately
			//allocate the unique parts of each
			//else: for rows and columns allocate the most efficient 
		}
		//lb=0;
		//vRemainingRelaxed
		for(int v=0;v<vTypes;v++){
			lb+=(vAreas[v]*vRemainingRelaxed[v]);
		}
		return lb;
		///////////////////////////
		////////////////////////////
		///////////////////////////
		////////////////////////////
	}
	
	double lowerBound2(QBinTreeNode parentNode, int currentBinIndex, Rectangle rect, int queueBeingUsed){
		double lb=0;//sum the areas of the vehicle not added when each queue is loaded sequentially into the remaining bins
		//separately (and non-fractionally, different type of relaxation)
		for(int q=0;q<numberOfQueues;q++){
			//reset the remaining lengths of
			for(int b=0;b<numberOfBins;b++){
				remainingRelaxedLength[b]=effectiveBinLength[b];
			}
			//
			if(isRowBin[currentBinIndex]){
				remainingRelaxedLength[currentBinIndex]-=rect.w;
			}else{
				remainingRelaxedLength[currentBinIndex]-=rect.l;
			}
			//current Rectangle whilst accounting for the child node this lower bound is being calculated for
			if(q==queueBeingUsed){
				qFrontRectRef2=rect.nextRectInQueue;
			}else{
				qFrontRectRef2=parentNode.qFronts[q];
			}
			//sequentially add the remaining rectangles in this queues to the remaining bins starting from the partially filled current bin (It might in fact be already full, but this wont matter because rectangle will then have to be added to higher numbered bins)
			boolean rectanglesStillFit=true;
			int currBin=currentBinIndex;
			while(qFrontRectRef2!=null && rectanglesStillFit){
				//find the first next bin the current rectangle fits into
				boolean binFound=false;
				while(currBin<numberOfBins && !binFound){// rectanglesStillFit && 
					if(isRowBin[currBin]){
						if(qFrontRectRef2.w<=remainingRelaxedLength[currBin] && qFrontRectRef2.l<=binLengths[currBin]){
							binFound=true;
						}
					}else{
						if(qFrontRectRef2.l<=remainingRelaxedLength[currBin] && qFrontRectRef2.w<=binWidths[currBin]){
							binFound=true;
						}
					}
					//
					if(!binFound){
						currBin++;
					}
				}
				//bin found?//next rectangle
				if(binFound){
					if(isRowBin[currentBinIndex]){
						remainingRelaxedLength[currBin]-=qFrontRectRef2.w;
					}else{
						remainingRelaxedLength[currBin]-=qFrontRectRef2.l;
					}
					//
					qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
				}else{
					rectanglesStillFit=false;
				}
			}
			//sum the area of the non-packed vehicle in this queue (lb+=...)
			while(qFrontRectRef2!=null){
				lb+=vAreas[qFrontRectRef2.type];
				//
				qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
			}
		}
		//
		return lb;
	}
	
	/*double[][] usedLaneLengthPerQueueEarly=new double[numberOfQueues][numberOfBins];
	double[][] usedLaneLengthPerQueueLate=new double[numberOfQueues][numberOfBins];
	double[][] minOverlapArea=new double[numberOfQueues+1][numberOfQueues+1];
	int[][][] numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueEarly=new int[numberOfQueues][numberOfBins][vTypes];
	int[][][] numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueLate=new int[numberOfQueues][numberOfBins][vTypes];*/
	/*double lowerBound3(QBinTreeNode parentNode, int currentBinIndex, Rectangle rect, int queueBeingUsed){
		double lb=0;//sum the areas of the vehicle not added when each queue is loaded sequentially into the remaining bins
		//separately (and non-fractionally, different type of relaxation)
		for(int q=0;q<numberOfQueues;q++){
			//Early
			//reset the remaining lengths of
			for(int b=0;b<numberOfBins;b++){
				remainingRelaxedLength[b]=effectiveBinLength[b];
				usedLaneLengthPerQueueEarly[q][b]=0;
				for(int v=0;v<vTypes;v++){
					numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueEarly[q][b][v]=0;
				}
			}
			//the following is unchanged (this is what this potential new child node is adding)
			if(isRowBin[currentBinIndex]){
				remainingRelaxedLength[currentBinIndex]-=rect.w;
			}else{
				remainingRelaxedLength[currentBinIndex]-=rect.l;
			}
			//current Rectangle whilst accounting for the child node this lower bound is being calculated for
			if(q==queueBeingUsed){
				qFrontRectRef2=rect.nextRectInQueue;
			}else{
				qFrontRectRef2=parentNode.qFronts[q];
			}
			//sequentially add the remaining rectangles in this queues to the remaining bins starting from the partially filled current bin (It might in fact be already full, but this wont matter because rectangle will then have to be added to higher numbered bins)
			boolean rectanglesStillFit=true;
			int currBin=currentBinIndex;
			while(qFrontRectRef2!=null && rectanglesStillFit){
				//find the first next bin the current rectangle fits into
				boolean binFound=false;
				while(currBin<numberOfBins && !binFound){// rectanglesStillFit && 
					if(isRowBin[currBin]){
						if(qFrontRectRef2.w<=remainingRelaxedLength[currBin] && qFrontRectRef2.l<=binLengths[currBin]){
							binFound=true;
						}
					}else{
						if(qFrontRectRef2.l<=remainingRelaxedLength[currBin] && qFrontRectRef2.w<=binWidths[currBin]){
							binFound=true;
						}
					}
					//
					if(!binFound){
						currBin++;
					}
				}
				//bin found?//next rectangle
				if(binFound){
					if(isRowBin[currBin]){
						remainingRelaxedLength[currBin]-=qFrontRectRef2.w;
						//
						usedLaneLengthPerQueueEarly[q][currBin]+=qFrontRectRef2.w;
						
					}else{
						remainingRelaxedLength[currBin]-=qFrontRectRef2.l;
						//
						usedLaneLengthPerQueueEarly[q][currBin]+=qFrontRectRef2.l;
					}
					//
					numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueEarly[q][currBin][qFrontRectRef2.type]++;
					//
					qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
				}else{
					rectanglesStillFit=false;
				}
			}
			//sum the area of the non-packed vehicle in this queue (lb+=...)
			minOverlapArea[q][numberOfQueues]=0;
			while(qFrontRectRef2!=null){
				minOverlapArea[q][numberOfQueues]+=vAreas[qFrontRectRef2.type];
				//
				qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
			}
			
			//Late (if everything was packed, otherwise early=late
			if(minOverlapArea[q][numberOfQueues]>0){
				//copy early
				for(int b=0;b<numberOfBins;b++){
					remainingRelaxedLength[b]=effectiveBinLength[b];
					usedLaneLengthPerQueueLate[q][b]=usedLaneLengthPerQueueEarly[q][b];
					for(int v=0;v<vTypes;v++){
						numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueLate[q][b][v]=numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueEarly[q][b][v];
					}
				}
			}else{
				//reset and compute late
				//Late
				//reset the remaining lengths of
				for(int b=0;b<numberOfBins;b++){
					remainingRelaxedLength[b]=effectiveBinLength[b];
					usedLaneLengthPerQueueLate[q][b]=0;
					for(int v=0;v<vTypes;v++){
						numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueLate[q][b][v]=0;
					}
				}
				//the following is unchanged (this is what this potential new child node is adding)
				if(isRowBin[currentBinIndex]){
					remainingRelaxedLength[currentBinIndex]-=rect.w;
				}else{
					remainingRelaxedLength[currentBinIndex]-=rect.l;
				}
				
				//current Rectangle whilst accounting for the child node this lower bound is being calculated for
				if(q==queueBeingUsed){
					qFrontRectRef2=rect.nextRectInQueue;
				}else{
					qFrontRectRef2=parentNode.qFronts[q];
				}
				//
				qFrontRectRef3=Qs[q].tail;
				//sequentially add the remaining rectangles in this queues to the remaining bins starting from the partially filled current bin (It might in fact be already full, but this wont matter because rectangle will then have to be added to higher numbered bins)
				rectanglesStillFit=true;
				currBin=numberOfBins-1;//currentBinIndex
				while(qFrontRectRef3!=qFrontRectRef2.prevRectInQueue && rectanglesStillFit){
					//find the first next bin the current rectangle fits into
					boolean binFound=false;
					while(currBin<numberOfBins && !binFound){// rectanglesStillFit && 
						if(isRowBin[currBin]){
							if(qFrontRectRef3.w<=remainingRelaxedLength[currBin] && qFrontRectRef3.l<=binLengths[currBin]){
								binFound=true;
							}
						}else{
							if(qFrontRectRef3.l<=remainingRelaxedLength[currBin] && qFrontRectRef3.w<=binWidths[currBin]){
								binFound=true;
							}
						}
						//
						if(!binFound){
							currBin--;
						}
					}
					//bin found?//next rectangle
					if(binFound){
						if(isRowBin[currBin]){
							remainingRelaxedLength[currBin]-=qFrontRectRef3.w;
							//
							usedLaneLengthPerQueueLate[q][currBin]+=qFrontRectRef3.w;
							
						}else{
							remainingRelaxedLength[currBin]-=qFrontRectRef3.l;
							//
							usedLaneLengthPerQueueLate[q][currBin]+=qFrontRectRef3.l;
						}
						//
						numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueLate[q][currBin][qFrontRectRef3.type]++;
						//
						qFrontRectRef3=qFrontRectRef3.prevRectInQueue;
					}else{
						rectanglesStillFit=false;
					}
				}
				//sum the area of the non-packed vehicle in this queue (lb+=...)
				minOverlapArea[q][numberOfQueues]=0;
				while(qFrontRectRef3!=qFrontRectRef2.prevRectInQueue){
					minOverlapArea[q][numberOfQueues]+=vAreas[qFrontRectRef3.type];
					//
					qFrontRectRef3=qFrontRectRef3.prevRectInQueue;
				}
			}
		}
		//compute the minimm overlap between early and late (bin i early-bin j late and vice versa)
		for(int q=0;q<numberOfQueues;q++){//earliest bins used (q)
			for(int r=0;r<numberOfQueues;r++){//latest bins used (r)
				if(q!=r){
					minOverlapArea[q][r]=0;
					for(int b=0;b<numberOfBins;b++){
						//does an overlap occur
						double lengthOfOverlap=usedLaneLengthPerQueueEarly[q][b]+usedLaneLengthPerQueueLate[r][b]-effectiveBinLength[b];
						if(lengthOfOverlap>0){
							
							//yes. remove vehicle(s) to maximise the remaining vehicle area under the constraint that
							//this is an optimisation problem (remove vehicles such that the utilisation is maximised 
							//the sum of the remaining lengths has to be less than or equal to the effective lane length
							//whilst the area of the removed vehicles is to be minimised.
							
							//the minimum area combination of vehicles whose effective lengths are>=length violation
							//consider the set of vehicles allocated to this bin
							//enumeration combinations in increasing are size
							
							//find vCounts
							
							for(int v=0;v<vTypes;v++){
								vCounts[v]=numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueLate[r][b][v]+numberOfVehiclesOfEachTypeAllocatedToEachBinPerQueueEarly[q][b][v];
							}
							if(isRowType[b]){
								minOverlapArea[q][r]+=MOT.minOverlapArea(vWidths, lengthOfOverlap, vCounts, vAreas);
							}else{
								minOverlapArea[q][r]+=MOT.minOverlapArea(vLengths, lengthOfOverlap, vCounts, vAreas);
							}
							
							
						}else{
							
						}
					}
				}
			}
		}
		//calculate lb from minOverlapArea
		
		//
		return lb;
	}*/
	
	/*double lowerBound4(QBinTreeNode parentNode, int currentBinIndex, Rectangle rect, int queueBeingUsed){// throws IloException
		double lb=0;
		//find the earliest and latest bin each rectangle in each queue can be allocated (and each bin in between)
		//these will be the relaxed decision variables
		//Allows fractional packing and partial violation of queue orders.
		//Purely LP relaxation so it should be fast
		//boolean[][][] variablesRequired=new boolean[numberOfBins][numberOfQueues][0];
		int[][] earliestBinPerRectPerQueue=new int[numberOfQueues][1];
		int[][] latestBinPerRectPerQueue=new int[numberOfQueues][1];
		//which bins in between
		int[] rectangleThatFitPerQueue=new int[numberOfQueues];
		//
		//LB4//
		//head=null;
		//tail=null;
		//
		double totalAreaOfRemainingRect=0;
		for(int q=0;q<numberOfQueues;q++){
			
			
			//Early
			
			//reset the remaining lengths of
			//Early
			//reset the remaining lengths of
			for(int b=0;b<numberOfBins;b++){
				remainingRelaxedLength[b]=effectiveBinLength[b];
			}
			//the following is unchanged (this is what this potential new child node is adding)
			if(isRowBin[currentBinIndex]){
				remainingRelaxedLength[currentBinIndex]-=rect.w;
			}else{
				remainingRelaxedLength[currentBinIndex]-=rect.l;
			}
			//current Rectangle whilst accounting for the child node this lower bound is being calculated for
			if(q==queueBeingUsed){
				qFrontRectRef2=rect.nextRectInQueue;
			}else{
				qFrontRectRef2=parentNode.qFronts[q];
			}
			//How many rectangles are remaining in this queue?
			int rectanglesInQueue=0;
			while(qFrontRectRef2!=null){
				totalAreaOfRemainingRect+=vAreas[qFrontRectRef2.type];
				//reset the LB4Var List refs
				qFrontRectRef2.head=null;
				qFrontRectRef2.tail=null;
				rectanglesInQueue++;
				//
				qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
			}
			earliestBinPerRectPerQueue[q]=new int[rectanglesInQueue];
			latestBinPerRectPerQueue[q]=new int[rectanglesInQueue];
			//
			//
			if(q==queueBeingUsed){
				qFrontRectRef2=rect.nextRectInQueue;
			}else{
				qFrontRectRef2=parentNode.qFronts[q];
			}
			//
			boolean rectanglesStillFit=true;
			int currBin=currentBinIndex;
			while(qFrontRectRef2!=null && rectanglesStillFit){
				//find the first next bin the current rectangle fits into
				boolean binFound=false;
				while(currBin<numberOfBins && !binFound){// rectanglesStillFit && 
					if(isRowBin[currBin]){
						if(qFrontRectRef2.w<=remainingRelaxedLength[currBin] && qFrontRectRef2.l<=binLengths[currBin]){
							binFound=true;
						}
					}else{
						if(qFrontRectRef2.l<=remainingRelaxedLength[currBin] && qFrontRectRef2.w<=binWidths[currBin]){
							binFound=true;
						}
					}
					//
					if(!binFound){
						currBin++;
					}
				}
				//bin found?//next rectangle
				if(binFound){
					earliestBinPerRectPerQueue[q][rectangleThatFitPerQueue[q]]=currBin;
					if(isRowBin[currBin]){
						remainingRelaxedLength[currBin]-=qFrontRectRef2.w;
						//generate a variable
						LB4Var lb4=new LB4Var(currBin, q, rectangleThatFitPerQueue[q], vAreas[qFrontRectRef2.type], qFrontRectRef2.w, LP.numVar(0, 1, IloNumVarType.Float,"x("+currBin+","+q+","+rectangleThatFitPerQueue[q]+")"));
						addVarToLinkedList(lb4);
						qFrontRectRef2.addVarToLinkedList(lb4);
					}else{
						remainingRelaxedLength[currBin]-=qFrontRectRef2.l;
						//generate a variable
						LB4Var lb4=new LB4Var(currBin, q, rectangleThatFitPerQueue[q], vAreas[qFrontRectRef2.type], qFrontRectRef2.l, LP.numVar(0, 1, IloNumVarType.Float,"x("+currBin+","+q+","+rectangleThatFitPerQueue[q]+")"));
						qFrontRectRef2.addVarToLinkedList(lb4);
						addVarToLinkedList(lb4);
					}
					rectangleThatFitPerQueue[q]++;
					qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
				}else{
					rectanglesStillFit=false;
				}
			}
			//
			if(qFrontRectRef2!=null){
				//then min bin=max bin (one choice of bin per rectangle)
				//make max a copy of min
				latestBinPerRectPerQueue[q]=earliestBinPerRectPerQueue[q];
				//no additional variables as there is no choice as to which bin each rectangle in this queue can possibly be assigned to
			}else{
				int rectNumber=rectangleThatFitPerQueue[q];//will equal the same as for the early case
				//the rectangles fit, now consider the latest bins that each rectangle can be allocated to
				//All rectangles will fit and there is a chance that max still equals min
				//reset and compute late
				//Late
				//reset the remaining lengths of
				for(int b=0;b<numberOfBins;b++){
					remainingRelaxedLength[b]=effectiveBinLength[b];
				}
				//the following is unchanged (this is what this potential new child node is adding)
				if(isRowBin[currentBinIndex]){
					remainingRelaxedLength[currentBinIndex]-=rect.w;
				}else{
					remainingRelaxedLength[currentBinIndex]-=rect.l;
				}
				
				//current Rectangle whilst accounting for the child node this lower bound is being calculated for
				if(q==queueBeingUsed){
					qFrontRectRef2=rect.nextRectInQueue;
				}else{
					qFrontRectRef2=parentNode.qFronts[q];
				}
				//
				qFrontRectRef3=Qs[q].tail;
				//sequentially add the remaining rectangles in this queues to the remaining bins starting from the partially filled current bin (It might in fact be already full, but this wont matter because rectangle will then have to be added to higher numbered bins)
				rectanglesStillFit=true;
				currBin=numberOfBins-1;//currentBinIndex
				if(qFrontRectRef2!=null){
					while(qFrontRectRef3!=qFrontRectRef2.prevRectInQueue && rectanglesStillFit){
						//find the first next bin the current rectangle fits into
						boolean binFound=false;
						while(currBin<numberOfBins && !binFound){// rectanglesStillFit && 
							if(isRowBin[currBin]){
								if(qFrontRectRef3.w<=remainingRelaxedLength[currBin] && qFrontRectRef3.l<=binLengths[currBin]){
									binFound=true;
								}
							}else{
								if(qFrontRectRef3.l<=remainingRelaxedLength[currBin] && qFrontRectRef3.w<=binWidths[currBin]){
									binFound=true;
								}
							}
							//
							if(!binFound){
								currBin--;
								//the following is removed when filling bin backwards
								if(currBin<currentBinIndex){
									rectanglesStillFit=false;
								}
							}
						}
						//bin found?//next rectangle
						if(binFound){
							rectNumber--;//the rectangles are now being considered in reverse order
							latestBinPerRectPerQueue[q][rectNumber]=currBin;
							
							if(isRowBin[currBin]){
								remainingRelaxedLength[currBin]-=qFrontRectRef3.w;
								//
								if(latestBinPerRectPerQueue[q][rectNumber]!=earliestBinPerRectPerQueue[q][rectNumber]){
									//and any feasible bins in between
									//
									for(int qq=earliestBinPerRectPerQueue[q][rectNumber]+1;qq<currBin;qq++){
										if(isRowBin[qq]){
											if(qFrontRectRef3.l<=binLengths[qq]){
												LB4Var lb4=new LB4Var(qq, q, rectNumber, vAreas[qFrontRectRef3.type], qFrontRectRef3.w, LP.numVar(0, 1, IloNumVarType.Float,"x("+qq+","+q+","+rectNumber+")"));
												qFrontRectRef3.addVarToLinkedList(lb4);
												addVarToLinkedList(lb4);
											}
										}else{
											if(qFrontRectRef3.w<=binWidths[qq]){
												LB4Var lb4=new LB4Var(qq, q, rectNumber, vAreas[qFrontRectRef3.type], qFrontRectRef3.l, LP.numVar(0, 1, IloNumVarType.Float,"x("+qq+","+q+","+rectNumber+")"));
												qFrontRectRef3.addVarToLinkedList(lb4);
												addVarToLinkedList(lb4);
											}
										}
									}
									//a variable for latest
									//generate the variables for the min and max bin numbers (if not equal)
									//int binNumber, int queueNumber, int rectangleNumber, double areaOfRectangle, double effectiveLength
									LB4Var lb5=new LB4Var(currBin, q, rectNumber, vAreas[qFrontRectRef3.type], qFrontRectRef3.w, LP.numVar(0, 1, IloNumVarType.Float,"x("+currBin+","+q+","+rectNumber+")"));
									qFrontRectRef3.addVarToLinkedList(lb5);
									addVarToLinkedList(lb5);
								}
							}else{
								remainingRelaxedLength[currBin]-=qFrontRectRef3.l;
								//
								if(latestBinPerRectPerQueue[q][rectNumber]!=earliestBinPerRectPerQueue[q][rectNumber]){
									//a variable for latest
									//
									for(int qq=earliestBinPerRectPerQueue[q][rectNumber]+1;qq<currBin;qq++){
										if(isRowBin[qq]){
											if(qFrontRectRef3.l<=binLengths[qq]){
												
												addVarToLinkedList(new LB4Var(qq, q, rectNumber, vAreas[qFrontRectRef3.type], qFrontRectRef3.w, LP.numVar(0, 1, IloNumVarType.Float,"x("+qq+","+q+","+rectNumber+")")));
											}
										}else{
											if(qFrontRectRef3.w<=binWidths[qq]){
												addVarToLinkedList(new LB4Var(qq, q, rectNumber, vAreas[qFrontRectRef3.type], qFrontRectRef3.l, LP.numVar(0, 1, IloNumVarType.Float,"x("+qq+","+q+","+rectNumber+")")));
											}
										}
									}
									//generate the variables for the min and max bin numbers (if not equal)
									//int binNumber, int queueNumber, int rectangleNumber, double areaOfRectangle, double effectiveLength
									addVarToLinkedList(new LB4Var(currBin, q, rectNumber, vAreas[qFrontRectRef3.type], qFrontRectRef3.l, LP.numVar(0, 1, IloNumVarType.Float,"x("+currBin+","+q+","+rectNumber+")")));
									//and any feasible bins in between
								}
							}
							
							//
							qFrontRectRef3=qFrontRectRef3.prevRectInQueue;
						}else{
							rectanglesStillFit=false;
						}
					}
				}
				
				
			}
			
		}
		//each rectangle allocated at most once
		IloLinearNumExpr[][] eachRectAllocatedOnceConstraints=new IloLinearNumExpr[numberOfQueues][1];//=FIP.linearNumExpr();
		for(int q=0;q<numberOfQueues;q++){
			eachRectAllocatedOnceConstraints[q]=new IloLinearNumExpr[rectangleThatFitPerQueue[q]];
			for(int r=0;r<rectangleThatFitPerQueue[q];r++){
				eachRectAllocatedOnceConstraints[q][r]=LP.linearNumExpr();
			}
		}
		//remaining lengths in remaining bins constraints
		IloLinearNumExpr[] binLengthConstraints=new IloLinearNumExpr[numberOfBins];//=FIP.linearNumExpr();
		for(int b=currentBinIndex;b<numberOfBins;b++){
			binLengthConstraints[b]=LP.linearNumExpr();
		}
		//objective: maximise the area of the allocated vehicles
		IloLinearNumExpr objectiveFunction=LP.linearNumExpr();//
		//fill in the linear expressions
		LB4Var currentVar=head;
		while(currentVar!=null){
			int binNum=currentVar.binNumber;
			int qNum=currentVar.queueNumber;
			int rectNum=currentVar.rectangleNumber;
			//
			eachRectAllocatedOnceConstraints[qNum][rectNum].addTerm(1, currentVar.x);
			//
			binLengthConstraints[binNum].addTerm(currentVar.effectiveLength, currentVar.x);
			//
			objectiveFunction.addTerm(currentVar.areaOfRectangle, currentVar.x);
			//
			currentVar=currentVar.nextVar;
		}
		//
		//LP.clearModel();
		//queue order constraints
		for(int q=0;q<numberOfQueues;q++){
			//expressions for each consecutive pair of rectangles where both have had variables generated for them
			if(q==queueBeingUsed){
				qFrontRectRef2=rect.nextRectInQueue;
			}else{
				qFrontRectRef2=parentNode.qFronts[q];
			}
			//
			boolean rectanglesStillFit=true;//false if the next rectangle has no variables
			while(qFrontRectRef2!=null && rectanglesStillFit){
				qFrontRectRef3=qFrontRectRef2.nextRectInQueue;
				if(qFrontRectRef3!=null){
					if(qFrontRectRef3.head!=null){
						//expression for each consecutive subset
						LB4Var currentLB4Var=qFrontRectRef2.head;
						while(currentLB4Var!=null){//subset
							IloLinearNumExpr queueConstraintExpression=LP.linearNumExpr();//
							//LHS
							LB4Var currentLB4VarLHS=currentLB4Var;
							while(currentLB4VarLHS!=null){
								queueConstraintExpression.addTerm(1, currentLB4VarLHS.x);
								//
								currentLB4VarLHS=currentLB4VarLHS.prevVar2;
							}
							//RHS
							LB4Var anLB4Var=qFrontRectRef3.head;
							boolean varsNotFound=true;
							while(anLB4Var!=null && varsNotFound){
								if(anLB4Var.binNumber<=currentLB4Var.binNumber){
									queueConstraintExpression.addTerm(-1, anLB4Var.x);
								}else{
									varsNotFound=false;
								}
								//
								anLB4Var=anLB4Var.nextVar2;
							}
							//add the constraint
							LP.addRange(0, queueConstraintExpression, 1);
							//
							currentLB4Var=currentLB4Var.nextVar2;
						}
						//
						qFrontRectRef2=qFrontRectRef2.nextRectInQueue;
					}else{
						rectanglesStillFit=false;
					}
				}else{
					rectanglesStillFit=false;
				}
			}
		}
		
		//add the constraints to the model
		for(int q=0;q<numberOfQueues;q++){
			for(int r=0;r<rectangleThatFitPerQueue[q];r++){
				LP.addRange(0, eachRectAllocatedOnceConstraints[q][r], 1);
			}
		}
		
		/////////////////////////////////
		/////////////////////////////////
		//reset the remaining lengths of
		for(int b=0;b<numberOfBins;b++){
			remainingRelaxedLength[b]=effectiveBinLength[b];
		}
		//the following is unchanged (this is what this potential new child node is adding)
		if(isRowBin[currentBinIndex]){
			remainingRelaxedLength[currentBinIndex]-=rect.w;
		}else{
			remainingRelaxedLength[currentBinIndex]-=rect.l;
		}
		////////////////////////////////
		/////////////////////////////////
		
		for(int b=currentBinIndex;b<numberOfBins;b++){
			LP.addRange(0, binLengthConstraints[b], remainingRelaxedLength[b]);
		}
		
		LP.addMaximize(objectiveFunction);
		
		//LP.setParam(IloCplex.IntParam.MIPDisplay,0);
		//LP.setParam(IloCplex.IntParam.NetDisplay,0);
		LP.setOut(null);
		//LP.setParam(IloCplex.IntParam.,0);
		//stop when there exists one integer solution
		//FIP.setParam(IloCplex.IntParam.IntSolLim, 1);
		//FIP.setParam(IloCplex.DoubleParam.EpGap, 1);
		//find how to terminate the solve procedure once feasibility is established
		
		//export queues
		
		//LP.exportModel("model.lp");
		
		if(LP.solve()){
			double objectiveValue=LP.getObjValue();
			//double bestObjectiveValue=LP.getBestObjValue();
			//System.out.println(objectiveValue);//bestObjectiveValue+","+
			//lower bound = total_area_of_remaining_rectangle-objVal
			lb=totalAreaOfRemainingRect-objectiveValue;
		}
		LP.clearModel();
		//
		//solve to determine a lower bound on the area of the rectangles that cannot be packed
		//
		return lb;
	}
	
	void addVarToLinkedList(LB4Var newVar){
		if(head==null){
			//this implies there are no elements in the list
			head=newVar;
			head.prevVar=null;
			tail=newVar;
			tail.nextVar=null;
		}else if(head==tail){
			head.nextVar=newVar;
			newVar.prevVar=head;
			tail=newVar;
		}else{
			tail.nextVar=newVar;
			newVar.prevVar=tail;
			tail=newVar;
		}
	}*/
	
}
