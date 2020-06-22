package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class BinPackingAlgorithm {
	
	
	int[][] packBins(Bin[] bins, int numberOfBins, Queue[] VQs, double[][] rectangleDistribution, double externalUB){//FLEXIPAX (, int[] committedVMix)
	
		double Tol=0.000000000000001;
		
		BinTree.rectangleDimensions=rectangleDistribution;
		BinTree.vTypes=VQs.length;
		
		BinTreeNode.vTypes=VQs.length;
		
		boolean packingIsComplete=false;
		int vTypes=VQs.length;
		//int numberOfBins=bins.length;
		
		BinTree[] trees=new BinTree[numberOfBins];
		//
			
		//Vehicles to pack
		double[] vAreas=new double[vTypes];
		int[] vMix=new int[vTypes];
		for(int i=0;i<vTypes;i++){
			vMix[i]+=VQs[i].length;
			vAreas[i]=(rectangleDistribution[0][i]*rectangleDistribution[1][i]);
		}
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
		for(int b=0;b<numberOfBins;b++){
			//
			binAreas[b]=bins[b].w*bins[b].l;
			if(bins[b].orientation==0){//row
				isRowType[b]=true;
				numRowBins++;
				//
				for(int v=0;v<vTypes;v++){
					if(rectangleDistribution[0][v]<=bins[b].l && rectangleDistribution[1][v]<=bins[b].w){
						fits[b][v]=true;
						efficiencyRate[b][v]=(rectangleDistribution[0][v]/bins[b].l);
					}
				}	
			}else{//column (or first constraint)
				numColBins++;
				//
				for(int v=0;v<vTypes;v++){
					if(rectangleDistribution[0][v]<=bins[b].l && rectangleDistribution[1][v]<=bins[b].w){
						fits[b][v]=true;
						efficiencyRate[b][v]=(rectangleDistribution[1][v]/bins[b].w);
					}
				}
			}
			
		}
		
		double[] colWidths=new double[numColBins];
		double[] rowLengths=new double[numRowBins];
		int[] colBinIndices=new int[numColBins];
		int[] rowBinIndices=new int[numRowBins];
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
		int[] colBinOrder=maths.Sort(colWidths);
		int[] rowBinOrder=maths.Sort(rowLengths);
		
		colBinIndices=maths.inThisOrder(colBinOrder, colBinIndices);
		rowBinIndices=maths.inThisOrder(rowBinOrder, rowBinIndices);
		
		
		//efficiency order
		int[][] VOrders=new int[2][vTypes];//indices of vehicle type sorted by width and length
		VOrders[0]=maths.indexOrder(rectangleDistribution[0], true);
		VOrders[1]=maths.indexOrder(rectangleDistribution[1], true);
		
		
		int[] vRemaining=new int[vTypes];
		double[] vRemainingRelaxed=new double[vTypes];
		for(int v=0;v<vTypes;v++){
			vRemaining[v]=vMix[v];
			vRemainingRelaxed[v]=vMix[v];
		}
		
		//minimise v remaining area
		
		
		//variables for streamlining LB calculation
		double[] effectiveBinLength=new double[numberOfBins];
		double[][] effectiveVehicleLength=new double[numberOfBins][vTypes];
		double[] remainingRelaxedLength=new double[numberOfBins];
		double[][] relaxedAllocation=new double[numberOfBins][vTypes];//units
		for(int b=0;b<numberOfBins;b++){
			if(bins[b].orientation==0){//row
				effectiveBinLength[b]=bins[b].w;
				for(int v=0;v<vTypes;v++){
					effectiveVehicleLength[b][v]=rectangleDistribution[1][v];
				}
			}else{
				effectiveBinLength[b]=bins[b].l;
				for(int v=0;v<vTypes;v++){
					effectiveVehicleLength[b][v]=rectangleDistribution[0][v];
				}
			}
		}
		
		int currentBinIndex=0;
		
		///////////////////////////////
		///////////////////////////////
		///////////////////////////////
		///////////////////////////relaxedAllocation
		//Does this lb rely on the assumption that the area of vehicles is greater than the area of bins
		//not anymore because it stops if no more vehicles remain to be packed
		for(int b=0;b<numberOfBins;b++){
			remainingRelaxedLength[b]=effectiveBinLength[b];
		}
		//calculate an initial lb
		//boolean lBComputed=false;
		boolean moveFound=true;
		while(moveFound){
			//System.out.println("move not found");
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
			double maxEfficiency=0;
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
						if(remainingRelaxedLength[colBinIndices[cb]]>Tol){//the relaxed packed bins are those after the current bin
							unusedBinSpaceFound=true;
							//
							//find the widest vehicle in a row bin that can be reallocated to this bin
							int reallocatableVType=-1;
							int reallocatableBinIndex=-1;
							double maxWidth=0;
							for(int rb=0;rb<numRowBins;rb++){
								//if(currentBinIndex>rowBinIndices[rb]){
									for(int v=0;v<vTypes;v++){
										if(maxWidth<rectangleDistribution[1][v] && colWidths[cb]>=rectangleDistribution[1][v] && relaxedAllocation[rowBinIndices[rb]][v]>Tol){
											maxWidth=rectangleDistribution[1][v];
											reallocatableVType=v;
											reallocatableBinIndex=rowBinIndices[rb];//there has to be something not packed that can be allocated to this bin
										}
									}
								//}
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
								double reallocationAmount=Math.min((remainingRelaxedLength[colBinIndices[cb]]/rectangleDistribution[0][reallocatableVType]), relaxedAllocation[reallocatableBinIndex][reallocatableVType]);
								relaxedAllocation[reallocatableBinIndex][reallocatableVType]-=reallocationAmount;
								relaxedAllocation[colBinIndices[cb]][reallocatableVType]+=reallocationAmount;
								remainingRelaxedLength[colBinIndices[cb]]-=(reallocationAmount*rectangleDistribution[0][reallocatableVType]);
								remainingRelaxedLength[reallocatableBinIndex]+=(reallocationAmount*rectangleDistribution[1][reallocatableVType]);
							}
						}
					}
					//try the same for row bins
					if(!moveFound){
						unusedBinSpaceFound=false;
						for(int rb=0;rb<numRowBins && !unusedBinSpaceFound;rb++){
							if(remainingRelaxedLength[rowBinIndices[rb]]>Tol){// && rowBinIndices[rb]>currentBinIndex
								unusedBinSpaceFound=true;
								//
								//find the longest vehicle in a column bin that can be reallocated to this bin
								int reallocatableVType=-1;
								int reallocatableBinIndex=-1;
								double maxLength=0;
								for(int cb=0;cb<numColBins;cb++){
									//if(colBinIndices[cb]>currentBinIndex){
										for(int v=0;v<vTypes;v++){
											if(maxLength<rectangleDistribution[0][v] && rowLengths[rb]>=rectangleDistribution[0][v] && relaxedAllocation[colBinIndices[cb]][v]>Tol){
												maxLength=rectangleDistribution[0][v];
												reallocatableVType=v;
												reallocatableBinIndex=colBinIndices[cb];
												
											}
										}
									//}
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
									double reallocationAmount=Math.min((remainingRelaxedLength[rowBinIndices[rb]]/rectangleDistribution[1][reallocatableVType]), relaxedAllocation[reallocatableBinIndex][reallocatableVType]);
									relaxedAllocation[reallocatableBinIndex][reallocatableVType]-=reallocationAmount;
									relaxedAllocation[rowBinIndices[rb]][reallocatableVType]+=reallocationAmount;
									remainingRelaxedLength[rowBinIndices[rb]]-=(reallocationAmount*rectangleDistribution[1][reallocatableVType]);
									remainingRelaxedLength[reallocatableBinIndex]+=(reallocationAmount*rectangleDistribution[0][reallocatableVType]);
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
		double lb=0;
		//vRemainingRelaxed
		for(int v=0;v<vTypes;v++){
			lb+=(vAreas[v]*vRemainingRelaxed[v]);
		}
		
		
		
		//used for bounding leaf nodes
		double upperBound=Double.MAX_VALUE;//non-packed vehicle areaexternalUB;//
		int[][] bestPackingSolution=new int[numberOfBins][vTypes];
		
		bestPackingSolution[0][0]=-1;
		
		if(lb>externalUB){
			currentBinIndex=-1;
			
		}
		///////////////////////////
		////////////////////////////
		///////////////////////////
		////////////////////////////
		
		
		
		int t1=(int)System.currentTimeMillis();
		//boolean feasibleTreeSequenceFound=false;
		
		
		
		while(currentBinIndex>-1){
			//System.out.println(currentBinIndex+","+upperBound);
			if(trees[currentBinIndex]==null){
				//FLEXIPAX (remaining flexibility is the input)
				if(bins[currentBinIndex].orientation==0){
					trees[currentBinIndex]=new BinTree(vRemaining, (bins[currentBinIndex].orientation==0), bins[currentBinIndex].l, bins[currentBinIndex].w, VOrders[1]);
				}else{
					trees[currentBinIndex]=new BinTree(vRemaining, (bins[currentBinIndex].orientation==0), bins[currentBinIndex].l, bins[currentBinIndex].w, VOrders[0]);
				}
			}else{
				//FLEXIPAX (output/store remaining flexibility at the current leaf node in this tree)
				trees[currentBinIndex].getNextFeasiblePath();
			}
			
			//compute LB for (unpacked vehicle area) the current partial solution
			//vRemaining=trees[currentBinIndex].finalVMixRemaining();
			/*for(int v=0;v<vTypes;v++){
				vRemainingRelaxed[v]=vRemaining[v];
			}*/
			
			
			//if this is the last bin check if this full solution improves the lower bound
			if(trees[currentBinIndex].leavesRemain){
				if(currentBinIndex==numberOfBins-1){
					packingIsComplete=true;
					//
					vRemaining=trees[currentBinIndex].finalVMixRemaining();
					double remainingVehicleAreaOfCompleteSolution=0;
					for(int v=0;v<vTypes;v++){
						remainingVehicleAreaOfCompleteSolution+=(vAreas[v]*vRemaining[v]);
					}
					//is this an improvement
					if(remainingVehicleAreaOfCompleteSolution<upperBound){
						upperBound=remainingVehicleAreaOfCompleteSolution;
						//extract the solution from the sequence of trees
						//BinTree currentBT=trees[currentBinIndex];
						for(int b=0;b<numberOfBins;b++){
							int[] binVMix=trees[b].getBinVMix();
							for(int v=0;v<vTypes;v++){
								bestPackingSolution[b][v]=binVMix[v];
							}
						}
						
					}
				}else{
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
					vRemaining=trees[currentBinIndex].finalVMixRemaining();
					for(int v=0;v<vTypes;v++){
						vRemainingRelaxed[v]=vRemaining[v];
					}
					
					//calculate an initial lb
					//boolean lBComputed=false;
					//int counter=0;
					moveFound=true;
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
						double maxEfficiency=0;
						for(int b=currentBinIndex+1;b<numberOfBins;b++){//+1
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
									if(remainingRelaxedLength[colBinIndices[cb]]>Tol && colBinIndices[cb]>currentBinIndex){//the relaxed packed bins are those after the current bin
										unusedBinSpaceFound=true;
										//
										//find the widest vehicle in a row bin that can be reallocated to this bin
										int reallocatableVType=-1;
										int reallocatableBinIndex=-1;
										double maxWidth=0;
										for(int rb=0;rb<numRowBins;rb++){
											if(currentBinIndex>rowBinIndices[rb]){
												for(int v=0;v<vTypes;v++){
													if(maxWidth<rectangleDistribution[1][v] && colWidths[cb]>=rectangleDistribution[1][v] && relaxedAllocation[rowBinIndices[rb]][v]>Tol){
														maxWidth=rectangleDistribution[1][v];
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
											double reallocationAmount=Math.min((remainingRelaxedLength[colBinIndices[cb]]/rectangleDistribution[0][reallocatableVType]), relaxedAllocation[reallocatableBinIndex][reallocatableVType]);
											relaxedAllocation[reallocatableBinIndex][reallocatableVType]-=reallocationAmount;
											relaxedAllocation[colBinIndices[cb]][reallocatableVType]+=reallocationAmount;
											remainingRelaxedLength[colBinIndices[cb]]-=(reallocationAmount*rectangleDistribution[0][reallocatableVType]);
											remainingRelaxedLength[reallocatableBinIndex]+=(reallocationAmount*rectangleDistribution[1][reallocatableVType]);
										}
									}
								}
								//try the same for row bins
								if(!moveFound){
									unusedBinSpaceFound=false;
									for(int rb=0;rb<numRowBins && !unusedBinSpaceFound;rb++){
										if(remainingRelaxedLength[rowBinIndices[rb]]>Tol && rowBinIndices[rb]>currentBinIndex){
											unusedBinSpaceFound=true;
											//
											//find the longest vehicle in a column bin that can be reallocated to this bin
											int reallocatableVType=-1;
											int reallocatableBinIndex=-1;
											double maxLength=0;
											for(int cb=0;cb<numColBins;cb++){
												if(colBinIndices[cb]>currentBinIndex){
													for(int v=0;v<vTypes;v++){
														if(maxLength<rectangleDistribution[0][v] && rowLengths[rb]>=rectangleDistribution[0][v] && relaxedAllocation[colBinIndices[cb]][v]>Tol){
															maxLength=rectangleDistribution[0][v];
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
												double reallocationAmount=Math.min((remainingRelaxedLength[rowBinIndices[rb]]/rectangleDistribution[1][reallocatableVType]), relaxedAllocation[reallocatableBinIndex][reallocatableVType]);
												relaxedAllocation[reallocatableBinIndex][reallocatableVType]-=reallocationAmount;
												relaxedAllocation[rowBinIndices[rb]][reallocatableVType]+=reallocationAmount;
												remainingRelaxedLength[rowBinIndices[rb]]-=(reallocationAmount*rectangleDistribution[1][reallocatableVType]);
												remainingRelaxedLength[reallocatableBinIndex]+=(reallocationAmount*rectangleDistribution[0][reallocatableVType]);
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
					lb=0;
					//vRemainingRelaxed
					for(int v=0;v<vTypes;v++){
						lb+=(vAreas[v]*vRemainingRelaxed[v]);
					}
					///////////////////////////
					////////////////////////////
					///////////////////////////
					////////////////////////////
					//is this greater than the UB (current best complete integer solution). If so "getNextPath()", else "currentBinInde++"
					if(upperBound<externalUB){
						externalUB=upperBound;
					}
					if(lb>=externalUB){//upperBound
						//just stay at the same tree. The algorithm will try to find the next feasible path in the next iteration
						//trees[currentBinIndex].getNextFeasiblePath();
						//System.out.println();
						//If the difference is bigger than the size of the current bin--
						//then no amount of repacking the current bin can reduce the current lb to something less than the external upprt bound
						if((lb-externalUB)>binAreas[currentBinIndex]){
							//
							trees[currentBinIndex]=null;
							currentBinIndex--;
						}
						
					}else{
						//increment "currentBinIndex"
						currentBinIndex++;
					}
					
					
				}
			}else{//else make null and backtrack
				//
				trees[currentBinIndex]=null;
				currentBinIndex--;
			}
			//}
		}
		int t2=(int)System.currentTimeMillis();
		//System.out.println(t2-t1);
		//return the optimal packing solution
		return bestPackingSolution;
	}
}
