package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;

public class ScenarioSolutionList {
	
	static int numberOfScenarios;
	
	ScenarioSolution head;
	ScenarioSolution tail;
	
	ScenarioSolution headCL;
	ScenarioSolution tailCL;
	
	VMixIntersectionCalculator VMC;
	
	double[] A;//area of each vehicle
	int vTypes;
	int[] vTypesDecreasingArea;
	
	int length;
	int lengthCL;
	
	double maxIntersectionArea;
	int[] maxIntersectionVMix;
	
	int[] vMixRef;
	
	int[] bestSolNumberPerScenario;
	boolean[] isInSubSet;
	//int solNumberInSubsetWithHighestIndividualUtil;
	int[] bestUtilSolutionNumberPerScenario;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int vTypes=6;
		
		/*double[][] rectangleDistribution={{2,4,2,4,1,3},
			{5,3,3,2,4,1},
			{1.5,1.5,2.5,3.0,4.0},
			//{0.2,0.2,0.2,0.2,0.2}};
			{0.4,0.2,0.15,0.1,0.05}};
		//1.00001,1.001,1.1}
		
		
		int[] vMix1={4,3,3,1,5,3};
		int[] vMix2={2,1,4,2,2,4};*/
		
		
		//counter example for the original algorithm
		double[][] rectangleDistribution={{1.5,1,1,1.8,0.2,1.2},
				{2,3,2,1.5,2.5,0.5},
				{1.5,1.5,2.5,3.0,4.0},
				//{0.2,0.2,0.2,0.2,0.2}};
				{0.4,0.2,0.15,0.1,0.05}};
		int[] vMix1={4,2,3,2,0,2};
		int[] vMix2={3,1,4,1,1,3};
		
		double[] vAreas=new double[vTypes];
		
		for(int v=0;v<vTypes;v++){
			vAreas[v]=rectangleDistribution[0][v]*rectangleDistribution[1][v];
		}
		
		ScenarioSolutionList SSL=new ScenarioSolutionList(rectangleDistribution, vAreas, 2);
		
		
		
		SSL.VMC.calculateIntersectionVMix(vMix1, vMix2);
		int[] vMixRef=SSL.VMC.intersectionVMix;
		int[] intersectionVMix=new int[vTypes];
		
		for(int i=0;i<vTypes;i++){
			intersectionVMix[i]=vMixRef[i];
			System.out.println(intersectionVMix[i]+",");
		}
		System.out.println();
		System.out.println(SSL.VMC.intersectionArea);
		
	}
	
	ScenarioSolutionList(double[][] rectangleDistribution, double[] A, int numberOfScenarios){
		
		bestSolNumberPerScenario=new int[numberOfScenarios];
		isInSubSet=new boolean[numberOfScenarios];
		bestUtilSolutionNumberPerScenario=new int[numberOfScenarios];
		
		vTypes=rectangleDistribution[0].length;
		
		this.A=A;
		ScenarioSolutionList.numberOfScenarios=numberOfScenarios;
		
		//vMix1Copy=new int[vTypes];
		//vMix2Copy=new int[vTypes];
		
		//fitsInside=new int[vTypes];
		double[] areas=new double[vTypes];
		//
		for(int v=0;v<vTypes;v++){
			areas[v]=rectangleDistribution[0][v]*rectangleDistribution[1][v]+0.000000000000001*v;//break ties the same way throughout
		}
		vTypesDecreasingArea=maths.Sort(areas, true);
		
		vMixRef=new int[vTypes];
		
		/*for(int vv=0;vv<vTypes;vv++){
			int v=vTypesDecreasingArea[vv];
			//for each vehicle type find the largest vehicle type that fits inside it
			//boolean largestInsideFitterFound=false;
			//fitsInside[v]=-1;
			ArrayList<Integer> fitsInsideAL=new ArrayList<Integer>(10);
			for(int uu=0;uu<=vv;uu++){// && !largestInsideFitterFound
				int u=vTypesDecreasingArea[uu];
				if(rectangleDistribution[0][u]>=rectangleDistribution[0][v] && rectangleDistribution[1][u]>=rectangleDistribution[1][v]){
					//largestInsideFitterFound=true;
					fitsInsideAL.add(u);
					//fitsInside[v]=u;
				}
			}
			fitsInside[v]=new int[fitsInsideAL.size()];
			for(int u=0;u<fitsInsideAL.size();u++){
				fitsInside[v][u]=fitsInsideAL.get(u);
			}
		}*/
		
		IntersectionNode.A=A;;
		//static double lb;
		//IntersectionNode.vTypesDecreasingArea=vTypesDecreasingArea;
		//IntersectionNode.fitsInside=fitsInside;
		
		IntersectionNode.vTypes=vTypes;	
		//IntersectionNode.vMix1Copy=vMix1Copy;
		//IntersectionNode.vMix2Copy=vMix2Copy;
		
		
		
		
		//create a vMixCalculator and test it here (why? already done)
		VMC=new VMixIntersectionCalculator(vTypes, rectangleDistribution, A, vTypesDecreasingArea);
		IntersectionNode.VMC=VMC;
		/*int[] vMix1={4,3,3,1,5};
		int[] vMix2={2,1,4,2,2};
		
		VMC.calculateIntersectionVMix(vMix1, vMix2);
		vMixRef=VMC.intersectionVMix;
		int[] intersectionVMix=new int[vTypes];
		
		for(int i=0;i<vTypes;i++){
			intersectionVMix[i]=vMixRef[i];
		}
		System.out.println(VMC.intersectionArea);*/
		
	}
	
	void addScenarioSolutionToNonDominatedSolutionsLinkList(ScenarioSolution newScenarioSolution){//call removeDominatedSolutions method first
		//
		newScenarioSolution.nextScenSol=null;
		newScenarioSolution.prevScenSol=null;
		newScenarioSolution.nextScenSolCL=null;
		newScenarioSolution.prevScenSolCL=null;
		if(paretoFrontUpdatedByNewSol(newScenarioSolution)){
			if(head==null){
				//this implies there are no elements in the list
				head=newScenarioSolution;
				head.prevScenSol=null;
				tail=newScenarioSolution;
				tail.nextScenSol=null;
			}else if(head==tail){
				head.nextScenSol=newScenarioSolution;
				newScenarioSolution.prevScenSol=head;
				tail=newScenarioSolution;
			}else{
				tail.nextScenSol=newScenarioSolution;
				newScenarioSolution.prevScenSol=tail;
				tail=newScenarioSolution;
			}
			length++;
		}

	}
	
	void removeScenarioSolutionFromLinkedList(ScenarioSolution scenSolToRemove){
		if(head==tail){
			head=null;
			tail=null;
		}else{
			if(scenSolToRemove==head){
				head=head.nextScenSol;
				head.prevScenSol=null;
			}else if(scenSolToRemove==tail){
				tail=tail.prevScenSol;
				tail.nextScenSol=null;
			}else{
				scenSolToRemove.prevScenSol.nextScenSol=scenSolToRemove.nextScenSol;
				scenSolToRemove.nextScenSol.prevScenSol=scenSolToRemove.prevScenSol;
			}
		}
		length--;
	}
	//
	void removeScenarioSolutionFromLinkedListCL(ScenarioSolution scenSolToRemove){
		if(headCL==tailCL){
			headCL=null;
			tailCL=null;
		}else{
			if(scenSolToRemove==headCL){
				headCL=headCL.nextScenSolCL;
				headCL.prevScenSolCL=null;
			}else if(scenSolToRemove==tailCL){
				tailCL=tailCL.prevScenSolCL;
				tailCL.nextScenSolCL=null;
			}else{
				scenSolToRemove.prevScenSolCL.nextScenSolCL=scenSolToRemove.nextScenSolCL;
				scenSolToRemove.nextScenSolCL.prevScenSolCL=scenSolToRemove.prevScenSolCL;
			}
		}
		lengthCL--;
	}
	//name is
	boolean paretoFrontUpdatedByNewSol(ScenarioSolution newScenarioSolution){//
		//check which vehicle mixes are dominated by this
		
		//does this solution dominate any single existing solution (set of vehicles mixes (or all of them stretched out as a vector)). If so add it and remove the one(s) it dominates.
		//Do any of this solutions vehicle mixes for each scenario dominate any existing vehicle mixes for the same scenario
		//(if it does not dominate any do not add, if it does update the relevant "dominated[]" value (remove those if they are full dominated)
		//Replace existing solution if new one dominates it. And do not add the new one if it does not dominate any existing solutions (across scenarios)
		//
		boolean addNewSolution=false;
		if(head==null){
			addNewSolution=true;
		}else{
			//boolean newSolDominatesOneOtherSol=false;
			int[][] vNew=newScenarioSolution.vMix;
			ScenarioSolution currentScenSol=head;
			while(currentScenSol!=null){
				boolean newSolDominatesThisSol=true;//try to find a counter example
				//boolean thisSolDominatesNewSol=true;
				boolean newSolutionHasAtLeastOneGreaterThanCurrentSol=false;
				//1. Does the new solution dominate any of the vehicle mixes (for each scenario) of this solution
				int[][] vThis=currentScenSol.vMix;
				for(int i=0;i<vThis.length;i++){
					if(!currentScenSol.dominated[i] && !newScenarioSolution.dominated[i]){
						boolean newSolVMixDominatesThisSolVMix=true;
						boolean thisSolVMixDominatesNewSolVMix=true;
						for(int j=0;j<vThis[i].length;j++){// && newSolVMixDominatesThisSolVMix
							if(vNew[i][j]<vThis[i][j]){
								newSolVMixDominatesThisSolVMix=false;
								newSolDominatesThisSol=false;//i.e. the new solution cannot fully replace this
							}else if(vNew[i][j]>vThis[i][j]){
								thisSolVMixDominatesNewSolVMix=false;
								newSolutionHasAtLeastOneGreaterThanCurrentSol=true;
								//thisSolDominatesNewSol=false;
								addNewSolution=true;
								
							}
						}
						//existing solution (for the current scenario i) has to be dominated by
						if(newSolVMixDominatesThisSolVMix && !thisSolVMixDominatesNewSolVMix){//
							currentScenSol.dominated[i]=true;
							//
							//add new solution=true; (the new solution (has a vehicle mix which) dominates an existing dominant vehicle mix
							addNewSolution=true;
						}
						//if the new solution for scenario i does not dominate the current solution for scenario i
						//if equal to an existing solution count the new solution as dominated
						if(!newSolVMixDominatesThisSolVMix && thisSolVMixDominatesNewSolVMix){//!thisSolVMixDominatesNewSolVMix && newSolVMixDominatesThisSolVMix!newSolutionHasAtLeastOneGreaterThanCurrentSol
							newScenarioSolution.dominated[i]=true;
						}
					}
				}
				//check if the current solution is not completely dominated
				boolean currentSolutionIsNowDominated=true;
				for(int i=0;i<vThis.length && currentSolutionIsNowDominated;i++){
					if(!currentScenSol.dominated[i]){
						currentSolutionIsNowDominated=false;
					}
				}
				//
				if(currentSolutionIsNowDominated){
					//remove the current soloution from the list
					removeScenarioSolutionFromLinkedList(currentScenSol);
					//add new solution=true;
					addNewSolution=true;
				}else{ 
					/*if(newSolDominatesThisSol && newSolutionHasAtLeastOneGreaterThanCurrentSol){//
						//then the current solution can be removed and replaced with the new one
						//this is not covered above because existing ones are counted as being dominated in cases where the new one has equal vehicle mixes
						//but if there are only equal vehicle mixes and the new one has at least one better one then the new one alone is more efficient to keep
						
						//remove the current soloution from the list
						removeScenarioSolutionFromLinkedList(currentScenSol);
						//add new solution=true;
						addNewSolution=true;
					}*/
				}
				//check if the new solution is completely dominated by existing solutions
				//if so there is no point continuing in this loop
				boolean newSolutionIsNowDominated=true;
				for(int i=0;i<vNew.length && newSolutionIsNowDominated;i++){
					if(!newScenarioSolution.dominated[i]){
						newSolutionIsNowDominated=false;
					}
				}
				//
				if(newSolutionIsNowDominated){//can't be true if addNewSolution=true;
					//addNewSolution=false
					break;
				}
				//
				currentScenSol=currentScenSol.nextScenSol;
			}
		}
		return addNewSolution;
	}
	
	ScenarioSolution[] bestUtilisationSubSet(int subsetSize){//ScenarioSolution SS
			
		maxIntersectionArea=0;
		
		//
		//Stop if one of the intersection members has the highest utilisation of all the remaining candidates for that scenario
		//(optimality gap) so keep a record of the highest utilisations (or total vehicle area) for each scenario over the remaining candidates for those scenarios
		//
		initialiseCandidateList();
		//step 1 find the maximum utilisation vehicle mixes/solutions for each scenario
		int vTypes=headCL.vMix[0].length;
		//int numberOfScenarios=headCL.vMix.length;
		ScenarioSolution[] SSs=new ScenarioSolution[numberOfScenarios];
		double[] bestUtilisationPerScenario=new double[numberOfScenarios];
		//
		maxIntersectionVMix=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			maxIntersectionVMix[v]=0;
		}
		//
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			currentScenSol.numberOfCandidates=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.dominated[i]){
					currentScenSol.candidate[i]=true;
					currentScenSol.numberOfCandidates++;
					double scenUtil=currentScenSol.totalRectangleArea[i];;
					int[] vMix=currentScenSol.vMix[i];
					/*for(int v=0;v<vTypes;v++){
						scenUtil+=(A[v]*vMix[v]);
					}*/
					currentScenSol.totalRectangleArea[i]=scenUtil;
					if(scenUtil>bestUtilisationPerScenario[i]){
						//update isInIntersection
						if(SSs[i]!=null){//being replaced as a member of the intersection
							SSs[i].isInIntersection[i]=false;
						}
						//
						bestUtilSolutionNumberPerScenario[i]=currentScenSol.packingSolutionNumber;
						bestUtilisationPerScenario[i]=scenUtil;
						SSs[i]=currentScenSol;
						//
						SSs[i].isInIntersection[i]=true;
					}
				}else{
					currentScenSol.candidate[i]=false;
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		//
		int[] bestScenUtilsIndOrd=maths.Sort(bestUtilisationPerScenario, true);
		
		//determine the intersection vehicle mix (initial solution fitness)
		int[] intersectionVMix=new int[vTypes];
		for(int i=0;i<subsetSize;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=true;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber;//currentIN.SS.packingSolutionNumber;
			//
			//maxIntersectionArea+=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];
			maxIntersectionArea+=bestUtilisationPerScenario[bestScenUtilsIndOrd[i]];
			/*if(i==0){
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v];
				}
			}else{
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=Math.min(intersectionVMix[v], SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v]);
				}
				
				//
				VMC.calculateIntersectionVMix(intersectionVMix, SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]]);
				vMixRef=VMC.intersectionVMix;
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=vMixRef[v];
				}
			}*/
		}
		//not in subset (initially)
		for(int i=subsetSize;i<numberOfScenarios;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=false;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];//SSs[].packingSolutionNumber;//if not in the subset of covered scenarios use the solution with the highest utilisation for that scenario
		}
		
		maxIntersectionArea/=subsetSize;
			
		return SSs;
	}
	
	//The assmuption here is that SS is in the list alreadyScenarioSolution[] 
	double constrainedBestUtilisationSubSet(int subsetSize, ScenarioSolution SS){//ScenarioSolution SS
		
		maxIntersectionArea=0;
		
		int solutionToInclude=SS.packingSolutionNumber;
		
		//
		//Stop if one of the intersection members has the highest utilisation of all the remaining candidates for that scenario
		//(optimality gap) so keep a record of the highest utilisations (or total vehicle area) for each scenario over the remaining candidates for those scenarios
		//
		initialiseCandidateList();
		//step 1 find the maximum utilisation vehicle mixes/solutions for each scenario
		int vTypes=headCL.vMix[0].length;
		//int numberOfScenarios=headCL.vMix.length;
		ScenarioSolution[] SSs=new ScenarioSolution[numberOfScenarios];
		double[] bestUtilisationPerScenario=new double[numberOfScenarios];
		//
		maxIntersectionVMix=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			maxIntersectionVMix[v]=0;
		}
		//find the scenario the solution SS has the highest utilisation for
		
		
		
		//
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			currentScenSol.numberOfCandidates=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.dominated[i]){
					currentScenSol.candidate[i]=true;
					currentScenSol.numberOfCandidates++;
					double scenUtil=currentScenSol.totalRectangleArea[i];;
					int[] vMix=currentScenSol.vMix[i];
					/*for(int v=0;v<vTypes;v++){
						scenUtil+=(A[v]*vMix[v]);
					}*/
					currentScenSol.totalRectangleArea[i]=scenUtil;
					if(scenUtil>bestUtilisationPerScenario[i]){
						//update isInIntersection
						if(SSs[i]!=null){//being replaced as a member of the intersection
							SSs[i].isInIntersection[i]=false;
						}
						//
						bestUtilSolutionNumberPerScenario[i]=currentScenSol.packingSolutionNumber;
						bestUtilisationPerScenario[i]=scenUtil;
						SSs[i]=currentScenSol;
						//
						SSs[i].isInIntersection[i]=true;
					}
				}else{
					currentScenSol.candidate[i]=false;
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		
		//
		int[] bestScenUtilsIndOrd=maths.Sort(bestUtilisationPerScenario, true);
		
		//check if the solution SS is part of the potential initial slution
		boolean solutionSSIncluded=false;
		//SSs[i].packingSolutionNumber
		for(int i=0;i<subsetSize;i++){
			if(SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber==SS.packingSolutionNumber){
				solutionSSIncluded=true;
			}
		}
		
		//if the solution is not included then find the scenario for which the solution achieves the highest utilisation
		//use it to replace the solution used for that scenario
		if(!solutionSSIncluded){
			int highestUtilScenForSS=0;
			double highestUtilForSS=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(SS.totalRectangleArea[i]>highestUtilForSS){
					highestUtilForSS=SS.totalRectangleArea[i];
					highestUtilScenForSS=i;
				}
			}
			//assign the solution SS as the solution for this scenario
			//check that the solution for the scenario is ranked within the subset size
			boolean SSHighestUtilScenIsWithinSubsetRank=false;
			
			for(int i=0;i<subsetSize && !SSHighestUtilScenIsWithinSubsetRank;i++){
				if(bestScenUtilsIndOrd[i]==highestUtilScenForSS){
					SSHighestUtilScenIsWithinSubsetRank=true;
					
				}
			}
			//
			if(SSHighestUtilScenIsWithinSubsetRank){
				//replace the solutiuon assigned to scenario "highestUtilScenForSS"
				SSs[highestUtilScenForSS].isInIntersection[highestUtilScenForSS]=false;
				//
				SSs[highestUtilScenForSS]=SS;
				//
				SSs[highestUtilScenForSS].isInIntersection[highestUtilScenForSS]=true;
			}else{
				//replace the "subsetSize-1"th ranked (scenario's solution)
				//bestScenUtilsIndOrd[subsetSize-1]=;
				SSs[bestScenUtilsIndOrd[subsetSize-1]].isInIntersection[highestUtilScenForSS]=false;
				//undo what was already assigned
				SSs[bestScenUtilsIndOrd[subsetSize-1]]=SS;
				//
				SSs[bestScenUtilsIndOrd[subsetSize-1]].isInIntersection[highestUtilScenForSS]=true;
			}
		}
		
		
		
		
		//determine the intersection vehicle mix (initial solution fitness)
		int[] intersectionVMix=new int[vTypes];
		for(int i=0;i<subsetSize;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=true;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber;//currentIN.SS.packingSolutionNumber;
			
			maxIntersectionArea+=SSs[bestScenUtilsIndOrd[i]].totalRectangleArea[bestScenUtilsIndOrd[i]];
			
			/*if(i==0){
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v];
				}
			}else{
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=Math.min(intersectionVMix[v], SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v]);
				}
				//
				VMC.calculateIntersectionVMix(intersectionVMix, SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]]);
				vMixRef=VMC.intersectionVMix;
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=vMixRef[v];
				}
				
			}*/
		}
		//
		//not in subset (initially)
		for(int i=subsetSize;i<numberOfScenarios;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=false;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];//SSs[].packingSolutionNumber;//if not in the subset of covered scenarios use the solution with the highest utilisation for that scenario
		}
		//average based on the max utilisation solution for each scenario with the constraint that the packing solution string associated with the given scenario is used in one scenario
		maxIntersectionArea/=subsetSize;
		
		return maxIntersectionArea;
		//return SSs;
	}
	
	
	ScenarioSolution[] worstCaseSubSet(int subsetSize){//ScenarioSolution SS
		
		maxIntersectionArea=0;
		
		//
		//Stop if one of the intersection members has the highest utilisation of all the remaining candidates for that scenario
		//(optimality gap) so keep a record of the highest utilisations (or total vehicle area) for each scenario over the remaining candidates for those scenarios
		//
		initialiseCandidateList();
		//step 1 find the maximum utilisation vehicle mixes/solutions for each scenario
		int vTypes=headCL.vMix[0].length;
		//int numberOfScenarios=headCL.vMix.length;
		ScenarioSolution[] SSs=new ScenarioSolution[numberOfScenarios];
		double[] bestUtilisationPerScenario=new double[numberOfScenarios];
		//
		maxIntersectionVMix=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			maxIntersectionVMix[v]=0;
		}
		//
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			currentScenSol.numberOfCandidates=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.dominated[i]){
					currentScenSol.candidate[i]=true;
					currentScenSol.numberOfCandidates++;
					double scenUtil=currentScenSol.totalRectangleArea[i];;
					int[] vMix=currentScenSol.vMix[i];
					/*for(int v=0;v<vTypes;v++){
						scenUtil+=(A[v]*vMix[v]);
					}*/
					currentScenSol.totalRectangleArea[i]=scenUtil;
					if(scenUtil>bestUtilisationPerScenario[i]){
						//update isInIntersection
						if(SSs[i]!=null){//being replaced as a member of the intersection
							SSs[i].isInIntersection[i]=false;
						}
						//
						bestUtilSolutionNumberPerScenario[i]=currentScenSol.packingSolutionNumber;
						bestUtilisationPerScenario[i]=scenUtil;
						SSs[i]=currentScenSol;
						//
						SSs[i].isInIntersection[i]=true;
					}
				}else{
					currentScenSol.candidate[i]=false;
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		//
		int[] bestScenUtilsIndOrd=maths.Sort(bestUtilisationPerScenario, true);
		double worstBestUtilisation=Double.MAX_VALUE;
		//determine the intersection vehicle mix (initial solution fitness)
		int[] intersectionVMix=new int[vTypes];
		for(int i=0;i<subsetSize;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=true;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber;//currentIN.SS.packingSolutionNumber;
			//
			if(SSs[bestScenUtilsIndOrd[i]].totalRectangleArea[bestScenUtilsIndOrd[i]]<worstBestUtilisation) {
				worstBestUtilisation=SSs[bestScenUtilsIndOrd[i]].totalRectangleArea[bestScenUtilsIndOrd[i]];
			}
			//maxIntersectionArea+=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];
			/*if(i==0){
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v];
				}
			}else{
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=Math.min(intersectionVMix[v], SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v]);
				}
				
				//
				VMC.calculateIntersectionVMix(intersectionVMix, SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]]);
				vMixRef=VMC.intersectionVMix;
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=vMixRef[v];
				}
			}*/
		}
		//not in subset (initially)
		for(int i=subsetSize;i<numberOfScenarios;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=false;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];//SSs[].packingSolutionNumber;//if not in the subset of covered scenarios use the solution with the highest utilisation for that scenario
		}
		
		maxIntersectionArea=worstBestUtilisation;
			
		return SSs;
	}
	
	//The assmuption here is that SS is in the list alreadyScenarioSolution[] 
	double constrainedWorstCaseSubSet(int subsetSize, ScenarioSolution SS){//ScenarioSolution SS
		
		//worst best
		
		maxIntersectionArea=0;
		
		int solutionToInclude=SS.packingSolutionNumber;
		
		//
		//Stop if one of the intersection members has the highest utilisation of all the remaining candidates for that scenario
		//(optimality gap) so keep a record of the highest utilisations (or total vehicle area) for each scenario over the remaining candidates for those scenarios
		//
		initialiseCandidateList();
		//step 1 find the maximum utilisation vehicle mixes/solutions for each scenario
		int vTypes=headCL.vMix[0].length;
		//int numberOfScenarios=headCL.vMix.length;
		ScenarioSolution[] SSs=new ScenarioSolution[numberOfScenarios];
		double[] bestUtilisationPerScenario=new double[numberOfScenarios];
		//
		maxIntersectionVMix=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			maxIntersectionVMix[v]=0;
		}
		//find the scenario the solution SS has the highest utilisation for
		
		
		
		//
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			currentScenSol.numberOfCandidates=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.dominated[i]){
					currentScenSol.candidate[i]=true;
					currentScenSol.numberOfCandidates++;
					double scenUtil=currentScenSol.totalRectangleArea[i];;
					int[] vMix=currentScenSol.vMix[i];
					/*for(int v=0;v<vTypes;v++){
						scenUtil+=(A[v]*vMix[v]);
					}*/
					currentScenSol.totalRectangleArea[i]=scenUtil;
					if(scenUtil>bestUtilisationPerScenario[i]){
						//update isInIntersection
						if(SSs[i]!=null){//being replaced as a member of the intersection
							SSs[i].isInIntersection[i]=false;
						}
						//
						bestUtilSolutionNumberPerScenario[i]=currentScenSol.packingSolutionNumber;
						bestUtilisationPerScenario[i]=scenUtil;
						SSs[i]=currentScenSol;
						//
						SSs[i].isInIntersection[i]=true;
					}
				}else{
					currentScenSol.candidate[i]=false;
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		
		//
		int[] bestScenUtilsIndOrd=maths.Sort(bestUtilisationPerScenario, true);
		
		//check if the solution SS is part of the potential initial slution
		boolean solutionSSIncluded=false;
		//SSs[i].packingSolutionNumber
		for(int i=0;i<subsetSize;i++){
			if(SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber==SS.packingSolutionNumber){
				solutionSSIncluded=true;
			}
		}
		
		//if the solution is not included then find the scenario for which the solution achieves the highest utilisation
		//use it to replace the solution used for that scenario
		if(!solutionSSIncluded){
			int highestUtilScenForSS=0;
			double highestUtilForSS=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(SS.totalRectangleArea[i]>highestUtilForSS){
					highestUtilForSS=SS.totalRectangleArea[i];
					highestUtilScenForSS=i;
				}
			}
			//assign the solution SS as the solution for this scenario
			//check that the solution for the scenario is ranked within the subset size
			boolean SSHighestUtilScenIsWithinSubsetRank=false;
			
			for(int i=0;i<subsetSize && !SSHighestUtilScenIsWithinSubsetRank;i++){
				if(bestScenUtilsIndOrd[i]==highestUtilScenForSS){
					SSHighestUtilScenIsWithinSubsetRank=true;
					
				}
			}
			//
			if(SSHighestUtilScenIsWithinSubsetRank){
				//replace the solutiuon assigned to scenario "highestUtilScenForSS"
				SSs[highestUtilScenForSS].isInIntersection[highestUtilScenForSS]=false;
				//
				SSs[highestUtilScenForSS]=SS;
				//
				SSs[highestUtilScenForSS].isInIntersection[highestUtilScenForSS]=true;
			}else{
				//replace the "subsetSize-1"th ranked (scenario's solution)
				//bestScenUtilsIndOrd[subsetSize-1]=;
				SSs[bestScenUtilsIndOrd[subsetSize-1]].isInIntersection[highestUtilScenForSS]=false;
				//undo what was already assigned
				SSs[bestScenUtilsIndOrd[subsetSize-1]]=SS;
				//
				SSs[bestScenUtilsIndOrd[subsetSize-1]].isInIntersection[highestUtilScenForSS]=true;
			}
		}
		
		//find the lowest utilisation of the best packing solutions for each scenario (in the subset (the subset feature is not used as now the subset size is the number of training scenarios)
		//int indexOfWorstBestUtilisation=-1;
		double worstBestUtilisation=Double.MAX_VALUE;
		
		//determine the intersection vehicle mix (initial solution fitness)
		//int[] intersectionVMix=new int[vTypes];
		for(int i=0;i<subsetSize;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=true;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber;//currentIN.SS.packingSolutionNumber;
			
			if(SSs[bestScenUtilsIndOrd[i]].totalRectangleArea[bestScenUtilsIndOrd[i]]<worstBestUtilisation) {
				worstBestUtilisation=SSs[bestScenUtilsIndOrd[i]].totalRectangleArea[bestScenUtilsIndOrd[i]];
			}
			
			//maxIntersectionArea+=SSs[bestScenUtilsIndOrd[i]].totalRectangleArea[bestScenUtilsIndOrd[i]];
			
			/*if(i==0){
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v];
				}
			}else{
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=Math.min(intersectionVMix[v], SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v]);
				}
				//
				VMC.calculateIntersectionVMix(intersectionVMix, SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]]);
				vMixRef=VMC.intersectionVMix;
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=vMixRef[v];
				}
				
			}*/
		}
		//
		//not in subset (initially)
		for(int i=subsetSize;i<numberOfScenarios;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=false;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];//SSs[].packingSolutionNumber;//if not in the subset of covered scenarios use the solution with the highest utilisation for that scenario
		}
		//average based on the max utilisation solution for each scenario with the constraint that the packing solution string associated with the given scenario is used in one scenario
		maxIntersectionArea=worstBestUtilisation;
		
		return worstBestUtilisation;
		//return SSs;
	}
	
	ScenarioSolution[] maxIntersectionAreaSolutionSubSet(int subsetSize){//ScenarioSolution SS
		
		maxIntersectionArea=0;
		
		//
		//Stop if one of the intersection members has the highest utilisation of all the remaining candidates for that scenario
		//(optimality gap) so keep a record of the highest utilisations (or total vehicle area) for each scenario over the remaining candidates for those scenarios
		//
		initialiseCandidateList();
		//step 1 find the maximum utilisation vehicle mixes/solutions for each scenario
		int vTypes=headCL.vMix[0].length;
		//int numberOfScenarios=headCL.vMix.length;
		ScenarioSolution[] SSs=new ScenarioSolution[numberOfScenarios];
		double[] bestUtilisationPerScenario=new double[numberOfScenarios];
		//
		maxIntersectionVMix=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			maxIntersectionVMix[v]=0;
		}
		//
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			currentScenSol.numberOfCandidates=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.dominated[i]){
					currentScenSol.candidate[i]=true;
					currentScenSol.numberOfCandidates++;
					double scenUtil=currentScenSol.totalRectangleArea[i];;
					int[] vMix=currentScenSol.vMix[i];
					/*for(int v=0;v<vTypes;v++){
						scenUtil+=(A[v]*vMix[v]);
					}*/
					currentScenSol.totalRectangleArea[i]=scenUtil;
					if(scenUtil>bestUtilisationPerScenario[i]){
						//update isInIntersection
						if(SSs[i]!=null){//being replaced as a member of the intersection
							SSs[i].isInIntersection[i]=false;
						}
						//
						bestUtilSolutionNumberPerScenario[i]=currentScenSol.packingSolutionNumber;
						bestUtilisationPerScenario[i]=scenUtil;
						SSs[i]=currentScenSol;
						//
						SSs[i].isInIntersection[i]=true;
					}
				}else{
					currentScenSol.candidate[i]=false;
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		//
		int[] bestScenUtilsIndOrd=maths.Sort(bestUtilisationPerScenario, true);
		
		//determine the intersection vehicle mix (initial solution fitness)
		int[] intersectionVMix=new int[vTypes];
		for(int i=0;i<subsetSize;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=true;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber;//currentIN.SS.packingSolutionNumber;
			if(i==0){
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v];
				}
			}else{
				/*for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=Math.min(intersectionVMix[v], SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v]);
				}*/
				
				//
				VMC.calculateIntersectionVMix(intersectionVMix, SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]]);
				vMixRef=VMC.intersectionVMix;
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=vMixRef[v];
				}
			}
		}
		//not in subset (initially)
		for(int i=subsetSize;i<numberOfScenarios;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=false;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];//SSs[].packingSolutionNumber;//if not in the subset of covered scenarios use the solution with the highest utilisation for that scenario
		}
		
		//Calculate the intersection area
		double currentIntersectionArea=0;
		for(int v=0;v<vTypes;v++){
			currentIntersectionArea+=(A[v]*intersectionVMix[v]);
			//
			maxIntersectionVMix[v]=intersectionVMix[v];
		}
		//
		ScenarioSolution[] maxUtilRemainingCandidatePerScenario=new ScenarioSolution[numberOfScenarios];
		
		//check if the maximum intersection area has already been identified
		boolean maxIntersectionSolutionIdentified=updateCandidateListAndEvaluateTerminationCriteria(numberOfScenarios, currentIntersectionArea, maxUtilRemainingCandidatePerScenario);
		
		//IntersectionNode.A=A;
		//for each candidate ruled out update "maxUtilRemainingCandidatePerScenario" (i.e. one of its elements has been ruled out
		
		if(!maxIntersectionSolutionIdentified){
			//build the branch and bound tree depth first
			//for each leaf node generate the child nodes
			//delete if the intersection is equal to or less than the current lb
			//explore the branch with the maximum intersection area
			//if a new lb is found prune the tree
			//Depth first next branch backtrack
			//a currentNode id enough
			
			
			//branch on the current node
			//if not possible because it is the last scenario try to update the lb
			//	set the current node to the first child node
			//else backtrack
			//	set the current node to the next child node
			//	if not possible backtrack
			
			//rule out candidates with<=lb
			
			//stop when you try to back track from the root node (currentNode!=null && !solutionFound
			//or the other termination criterion is met. (record the highest utilisation for a remaining candidate for each scenario, stop if any do not exceed the current lb
			//maxUtilRemainingCandidatePerScenario
			//update when lb is updated
			
			IntersectionNode rootNode=new IntersectionNode();
			//
			IntersectionNode currentNode=rootNode;
			//
			boolean solutionFound=false;
			while(currentNode!=null && !solutionFound){
				if(currentNode.scenariosIncluded==subsetSize){
					//final scenario/last level
					//then do not try to branch, this (reaching this part of the code) implies that an improved solution has been found
					//reset inInIntersection of the current solution then set this for the new solution
					//update the current best solution
					for(int v=0;v<vTypes;v++){
						maxIntersectionVMix[v]=currentNode.intersectionVMix[v];
					}
					//
					currentIntersectionArea=currentNode.intersectionArea;
					for(int i=0;i<numberOfScenarios;i++){
						SSs[i].isInIntersection[i]=false;
					}
					//new solution
					IntersectionNode currentIN=currentNode;
					int currentScenarioNumber=currentIN.scenarioNumber;
					while(currentScenarioNumber>-1){//currentIN!=null
						if(!currentIN.skipNode){
							SSs[currentScenarioNumber]=currentIN.SS;
							SSs[currentScenarioNumber].isInIntersection[currentScenarioNumber]=true;
							//
							isInSubSet[currentScenarioNumber]=true;
							bestSolNumberPerScenario[currentScenarioNumber]=currentIN.SS.packingSolutionNumber;
						}else{
							SSs[currentScenarioNumber]=null;
							//
							isInSubSet[currentScenarioNumber]=false;
							bestSolNumberPerScenario[currentScenarioNumber]=bestUtilSolutionNumberPerScenario[currentScenarioNumber];//
						}
						//
						currentScenarioNumber--;
						currentIN=currentIN.parentNode;
					}
					//
					//update the candidate list (remove scenario solutions with a total rectangle area that is less than the current maximum intersection area
					//update "maxUtilRemainingCandidatePerScenario" 
					//termination criteria satisfied?
					solutionFound=updateCandidateListAndEvaluateTerminationCriteria(numberOfScenarios, currentIntersectionArea, maxUtilRemainingCandidatePerScenario);
					//
					if((currentNode=updateCurrentNode(currentNode))==null){
						solutionFound=true;
					}
				}else{
					//branch on the current node
					branchOnIntersectionNodeSubset(currentNode, currentIntersectionArea, subsetSize);
					//set the current node as the first child node
					if(currentNode.currentChildNode!=null){
						currentNode=currentNode.currentChildNode;
					}else{
						//
						if((currentNode=updateCurrentNode(currentNode))==null){
							solutionFound=true;
						}
					}
				}
			}
		}
		//
		maxIntersectionArea=currentIntersectionArea;
		//
		//double highestIndividualUtil=0;
		for(int i=0;i<numberOfScenarios;i++){
			if(SSs[i]!=null){
				SSs[i].intersectionArea=maxIntersectionArea;
				//
				/*if(isInSubSet[i]){
					if(SSs[i].totalRectangleArea[i]>highestIndividualUtil){
						highestIndividualUtil=SSs[i].totalRectangleArea[i];
						solNumberInSubsetWithHighestIndividualUtil=SSs[i].packingSolutionNumber;
						//=true;
						//bestSolNumberPerScenario[currentScenarioNumber]
					}
				}*/
			}
		}
		
		//find the solution in the subset with the highest individual utilisation
		
		//
		return SSs;
	}
	
	//The assmuption here is that SS is in the list alreadyScenarioSolution[] 
	double constrainedMaxIntersectionAreaSolutionSubSet(int subsetSize, ScenarioSolution SS){//ScenarioSolution SS
		
		maxIntersectionArea=0;
		
		int solutionToInclude=SS.packingSolutionNumber;
		
		//
		//Stop if one of the intersection members has the highest utilisation of all the remaining candidates for that scenario
		//(optimality gap) so keep a record of the highest utilisations (or total vehicle area) for each scenario over the remaining candidates for those scenarios
		//
		initialiseCandidateList();
		//step 1 find the maximum utilisation vehicle mixes/solutions for each scenario
		int vTypes=headCL.vMix[0].length;
		//int numberOfScenarios=headCL.vMix.length;
		ScenarioSolution[] SSs=new ScenarioSolution[numberOfScenarios];
		double[] bestUtilisationPerScenario=new double[numberOfScenarios];
		//
		maxIntersectionVMix=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			maxIntersectionVMix[v]=0;
		}
		//find the scenario the solution SS has the highest utilisation for
		
		
		
		//
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			currentScenSol.numberOfCandidates=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.dominated[i]){
					currentScenSol.candidate[i]=true;
					currentScenSol.numberOfCandidates++;
					double scenUtil=currentScenSol.totalRectangleArea[i];;
					int[] vMix=currentScenSol.vMix[i];
					/*for(int v=0;v<vTypes;v++){
						scenUtil+=(A[v]*vMix[v]);
					}*/
					currentScenSol.totalRectangleArea[i]=scenUtil;
					if(scenUtil>bestUtilisationPerScenario[i]){
						//update isInIntersection
						if(SSs[i]!=null){//being replaced as a member of the intersection
							SSs[i].isInIntersection[i]=false;
						}
						//
						bestUtilSolutionNumberPerScenario[i]=currentScenSol.packingSolutionNumber;
						bestUtilisationPerScenario[i]=scenUtil;
						SSs[i]=currentScenSol;
						//
						SSs[i].isInIntersection[i]=true;
					}
				}else{
					currentScenSol.candidate[i]=false;
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		
		//
		int[] bestScenUtilsIndOrd=maths.Sort(bestUtilisationPerScenario, true);
		
		//check if the solution SS is part of the potential initial slution
		boolean solutionSSIncluded=false;
		//SSs[i].packingSolutionNumber
		for(int i=0;i<subsetSize;i++){
			if(SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber==SS.packingSolutionNumber){
				solutionSSIncluded=true;
			}
		}
		
		//if the solution is not included then find the scenario for which the solution achieves the highest utilisation
		//use it to replace the solution used for that scenario
		if(!solutionSSIncluded){
			int highestUtilScenForSS=0;
			double highestUtilForSS=0;
			for(int i=0;i<numberOfScenarios;i++){
				if(SS.totalRectangleArea[i]>highestUtilForSS){
					highestUtilForSS=SS.totalRectangleArea[i];
					highestUtilScenForSS=i;
				}
			}
			//assign the solution SS as the solution for this scenario
			//check that the solution for the scenario is ranked within the subset size
			boolean SSHighestUtilScenIsWithinSubsetRank=false;
			
			for(int i=0;i<subsetSize && !SSHighestUtilScenIsWithinSubsetRank;i++){
				if(bestScenUtilsIndOrd[i]==highestUtilScenForSS){
					SSHighestUtilScenIsWithinSubsetRank=true;
					
				}
			}
			//
			if(SSHighestUtilScenIsWithinSubsetRank){
				//replace the solutiuon assigned to scenario "highestUtilScenForSS"
				SSs[highestUtilScenForSS].isInIntersection[highestUtilScenForSS]=false;
				//
				SSs[highestUtilScenForSS]=SS;
				//
				SSs[highestUtilScenForSS].isInIntersection[highestUtilScenForSS]=true;
			}else{
				//replace the "subsetSize-1"th ranked (scenario's solution)
				//bestScenUtilsIndOrd[subsetSize-1]=;
				SSs[bestScenUtilsIndOrd[subsetSize-1]].isInIntersection[highestUtilScenForSS]=false;
				//undo what was already assigned
				SSs[bestScenUtilsIndOrd[subsetSize-1]]=SS;
				//
				SSs[bestScenUtilsIndOrd[subsetSize-1]].isInIntersection[highestUtilScenForSS]=true;
			}
		}
		
		
		
		
		//determine the intersection vehicle mix (initial solution fitness)
		int[] intersectionVMix=new int[vTypes];
		for(int i=0;i<subsetSize;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=true;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=SSs[bestScenUtilsIndOrd[i]].packingSolutionNumber;//currentIN.SS.packingSolutionNumber;
			if(i==0){
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v];
				}
			}else{
				/*for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=Math.min(intersectionVMix[v], SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]][v]);
				}*/
				//
				VMC.calculateIntersectionVMix(intersectionVMix, SSs[bestScenUtilsIndOrd[i]].vMix[bestScenUtilsIndOrd[i]]);
				vMixRef=VMC.intersectionVMix;
				for(int v=0;v<vTypes;v++){
					intersectionVMix[v]=vMixRef[v];
				}
				
			}
		}
		//
		//not in subset (initially)
		for(int i=subsetSize;i<numberOfScenarios;i++){
			isInSubSet[bestScenUtilsIndOrd[i]]=false;
			bestSolNumberPerScenario[bestScenUtilsIndOrd[i]]=bestUtilSolutionNumberPerScenario[bestScenUtilsIndOrd[i]];//SSs[].packingSolutionNumber;//if not in the subset of covered scenarios use the solution with the highest utilisation for that scenario
		}
		//Calculate the intersection area
		double currentIntersectionArea=0;
		for(int v=0;v<vTypes;v++){
			currentIntersectionArea+=(A[v]*intersectionVMix[v]);
			//
			maxIntersectionVMix[v]=intersectionVMix[v];
		}
		//
		ScenarioSolution[] maxUtilRemainingCandidatePerScenario=new ScenarioSolution[numberOfScenarios];
		
		//check if the maximum intersection area has already been identified
		boolean maxIntersectionSolutionIdentified=updateCandidateListAndEvaluateTerminationCriteria(numberOfScenarios, currentIntersectionArea, maxUtilRemainingCandidatePerScenario);
		
		IntersectionNode.A=A;
		//for each candidate ruled out update "maxUtilRemainingCandidatePerScenario" (i.e. one of its elements has been ruled out
		
		if(!maxIntersectionSolutionIdentified){
			//build the branch and bound tree depth first
			//for each leaf node generate the child nodes
			//delete if the intersection is equal to or less than the current lb
			//explore the branch with the maximum intersection area
			//if a new lb is found prune the tree
			//Depth first next branch backtrack
			//a currentNode id enough
			
			
			//branch on the current node
			//if not possible because it is the last scenario try to update the lb
			//	set the current node to the first child node
			//else backtrack
			//	set the current node to the next child node
			//	if not possible backtrack
			
			//rule out candidates with<=lb
			
			//stop when you try to back track from the root node (currentNode!=null && !solutionFound
			//or the other termination criterion is met. (record the highest utilisation for a remaining candidate for each scenario, stop if any do not exceed the current lb
			//maxUtilRemainingCandidatePerScenario
			//update when lb is updated
			
			IntersectionNode rootNode=new IntersectionNode();
			//
			IntersectionNode currentNode=rootNode;
			//
			boolean solutionFound=false;
			while(currentNode!=null && !solutionFound){
				if(currentNode.scenariosIncluded==subsetSize){
					//final scenario/last level
					//then do not try to branch, this (reaching this part of the code) implies that an improved solution has been found
					//reset inInIntersection of the current solution then set this for the new solution
					//update the current best solution
					for(int v=0;v<vTypes;v++){
						maxIntersectionVMix[v]=currentNode.intersectionVMix[v];
					}
					//
					currentIntersectionArea=currentNode.intersectionArea;
					for(int i=0;i<numberOfScenarios;i++){
						SSs[i].isInIntersection[i]=false;
					}
					//new solution
					IntersectionNode currentIN=currentNode;
					int currentScenarioNumber=currentIN.scenarioNumber;
					while(currentScenarioNumber>-1){//currentIN!=null
						if(!currentIN.skipNode){
							SSs[currentScenarioNumber]=currentIN.SS;
							SSs[currentScenarioNumber].isInIntersection[currentScenarioNumber]=true;
							//
							isInSubSet[currentScenarioNumber]=true;
							bestSolNumberPerScenario[currentScenarioNumber]=currentIN.SS.packingSolutionNumber;
						}else{
							SSs[currentScenarioNumber]=null;
							//
							isInSubSet[currentScenarioNumber]=false;
							bestSolNumberPerScenario[currentScenarioNumber]=bestUtilSolutionNumberPerScenario[currentScenarioNumber];//
						}
						//
						currentScenarioNumber--;
						currentIN=currentIN.parentNode;
					}
					//
					//update the candidate list (remove scenario solutions with a total rectangle area that is less than the current maximum intersection area
					//update "maxUtilRemainingCandidatePerScenario" 
					//termination criteria satisfied?
					solutionFound=updateCandidateListAndEvaluateTerminationCriteria(numberOfScenarios, currentIntersectionArea, maxUtilRemainingCandidatePerScenario);
					//
					if((currentNode=updateCurrentNode(currentNode))==null){
						solutionFound=true;
					}
				}else{
					//branch on the current node
					constrainedBranchOnIntersectionNodeSubset(currentNode, currentIntersectionArea, subsetSize, solutionToInclude);
					//Add a constrained versio of this
					//branchOnIntersectionNodeSubset(currentNode, currentIntersectionArea, subsetSize);
					//set the current node as the first child node
					if(currentNode.currentChildNode!=null){
						currentNode=currentNode.currentChildNode;
					}else{
						//
						if((currentNode=updateCurrentNode(currentNode))==null){
							solutionFound=true;
						}
					}
				}
			}
		}
		//
		maxIntersectionArea=currentIntersectionArea;
		//
		//double highestIndividualUtil=0;
		for(int i=0;i<numberOfScenarios;i++){
			if(SSs[i]!=null){
				SSs[i].intersectionArea=maxIntersectionArea;
				//
				/*if(isInSubSet[i]){
					if(SSs[i].totalRectangleArea[i]>highestIndividualUtil){
						highestIndividualUtil=SSs[i].totalRectangleArea[i];
						solNumberInSubsetWithHighestIndividualUtil=SSs[i].packingSolutionNumber;
						//=true;
						//bestSolNumberPerScenario[currentScenarioNumber]
					}
				}*/
			}
		}
		//
		return maxIntersectionArea;
		//return SSs;
	}
	
	
	
	
	boolean updateCandidateListAndEvaluateTerminationCriteria(int numberOfScenarios, double currentIntersectionArea, ScenarioSolution[] maxUtilRemainingCandidatePerScenario){
		boolean terminationCriteriaSatisfied=false;
		//
		//rule out candidates with a rectangle area <= lb
		ScenarioSolution currentScenSol=headCL;
		while(currentScenSol!=null){
			for(int i=0;i<numberOfScenarios;i++){
				if(currentScenSol.candidate[i]){//what if this solution is part of the current solution: the logic is that if it is remove it will never be replaced, however how does this hold for the subset case
					if(currentScenSol.totalRectangleArea[i]<=currentIntersectionArea){
						currentScenSol.candidate[i]=false;
						currentScenSol.numberOfCandidates--;
						if(currentScenSol.numberOfCandidates==0){
							removeScenarioSolutionFromLinkedListCL(currentScenSol);
							break;
						}
					}
				}
			}
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		
		for(int i=0;i<numberOfScenarios;i++){
			maxUtilRemainingCandidatePerScenario[i]=null;
		}
		
		//ScenarioSolution[] maxUtilRemainingCandidatePerScenario=new ScenarioSolution[numberOfScenarios];
		//find "maxUtilRemainingCandidatePerScenario" from the candidates that are not in the current best intersection set
		currentScenSol=headCL;
		while(currentScenSol!=null){
			//
			for(int i=0;i<numberOfScenarios;i++){
				if(!currentScenSol.isInIntersection[i]){
					if(currentIntersectionArea<currentScenSol.totalRectangleArea[i]){
						if(maxUtilRemainingCandidatePerScenario[i]==null){
							maxUtilRemainingCandidatePerScenario[i]=currentScenSol;
						}else{
							if(maxUtilRemainingCandidatePerScenario[i].totalRectangleArea[i]<currentScenSol.totalRectangleArea[i]){
								maxUtilRemainingCandidatePerScenario[i]=currentScenSol;
							}
						}
					}
				}
			}
			
			//maxUtilRemainingCandidatePerScenario[i]=currentScenSol;
			//
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		
		//check if the maximum intersection area has already been identified
		//boolean maxIntersectionSolutionIdentified=false;
		for(int i=0;i<numberOfScenarios && !terminationCriteriaSatisfied;i++){
			if(maxUtilRemainingCandidatePerScenario[i]==null){
				terminationCriteriaSatisfied=true;
			}
		}
		//
		return terminationCriteriaSatisfied;
	}
	
	//add node method
	/*void branchOnIntersectionNode(IntersectionNode parentNode, double lb){
		//search the list for vehicle mixes with a total area greater than lb for the next scenario
		ScenarioSolution currentScenSol=headCL;
		int nextScenarioNumber=parentNode.scenarioNumber+1;
		while(currentScenSol!=null){
			if(currentScenSol.candidate[nextScenarioNumber]){
				if(currentScenSol.totalRectangleArea[nextScenarioNumber]>lb){
					IntersectionNode childNode=new IntersectionNode(nextScenarioNumber, parentNode, currentScenSol);
					if(childNode.intersectionArea>lb){
						parentNode.addChildNodeToLinkedList(childNode);
					}
				}
			}
			currentScenSol=currentScenSol.nextScenSolCL;
		}
	}*/
	
	//add node method
	void branchOnIntersectionNodeSubset(IntersectionNode parentNode, double lb, int subsetSize){
		//search the list for vehicle mixes with a total area greater than lb for the next scenario
		ScenarioSolution currentScenSol=headCL;
		boolean noBranchesDecreaseTheParentNodeIntersectionArea=true;
		int nextScenarioNumber=parentNode.scenarioNumber+1;
		int nextScenariosIncluded=parentNode.scenariosIncluded+1;
		while(currentScenSol!=null){
			if(currentScenSol.candidate[nextScenarioNumber]){
				if(currentScenSol.totalRectangleArea[nextScenarioNumber]>lb){
					IntersectionNode childNode=new IntersectionNode(nextScenarioNumber, nextScenariosIncluded, parentNode, currentScenSol);
					if(childNode.intersectionArea>lb){
						parentNode.addChildNodeToLinkedList(childNode);
					}
					//
					if(childNode.intersectionArea<parentNode.intersectionArea){
						noBranchesDecreaseTheParentNodeIntersectionArea=false;
					}
				}
			}
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		//add a skip scenario branch, a specific IntersectionNode is required for this (such a node should be more or less a copy of its parent node
		//however if the next node does not decrease the intersection area there is no point adding a skip node
		if(!noBranchesDecreaseTheParentNodeIntersectionArea){
			//then add a ship node could be beneficial
			if(numberOfScenarios-subsetSize>(parentNode.scenarioNumber+1)-parentNode.scenariosIncluded){
				//means there are skips remaining on the current (parentNode)
				IntersectionNode childNode=new IntersectionNode(nextScenarioNumber, parentNode.scenariosIncluded, parentNode);
				parentNode.addChildNodeToLinkedList(childNode);
			}
		}
	}
	
	//this version applies for constrining a solution to be used to cover any scnenario, not a specific one as previous
	void constrainedBranchOnIntersectionNodeSubset(IntersectionNode parentNode, double lb, int subsetSize, int solutionToInclude){
		//search the list for vehicle mixes with a total area greater than lb for the next scenario
		ScenarioSolution currentScenSol=headCL;
		boolean noBranchesDecreaseTheParentNodeIntersectionArea=true;
		int nextScenarioNumber=parentNode.scenarioNumber+1;
		int nextScenariosIncluded=parentNode.scenariosIncluded+1;
		
		//if the number of scenarios included=subsetSize-1 and parentNode.solutionToIncludeIncluded==false
		//then there is only one possible branch
		
		while(currentScenSol!=null){
			if(currentScenSol.candidate[nextScenarioNumber]){
				if(currentScenSol.totalRectangleArea[nextScenarioNumber]>lb){
					boolean currentSolIsSolToInclude=(currentScenSol.packingSolutionNumber==solutionToInclude);
					IntersectionNode childNode=new IntersectionNode(nextScenarioNumber, nextScenariosIncluded, parentNode, currentScenSol, currentSolIsSolToInclude);
					if(childNode.intersectionArea>lb){
						if(parentNode.solutionToIncludeIncluded || (nextScenariosIncluded<subsetSize) || (currentSolIsSolToInclude)){
							parentNode.addChildNodeToLinkedList(childNode);
						}
					}
					//
					if(childNode.intersectionArea<parentNode.intersectionArea){
						noBranchesDecreaseTheParentNodeIntersectionArea=false;
					}
				}
			}
			currentScenSol=currentScenSol.nextScenSolCL;
		}
		//add a skip scenario branch, a specific IntersectionNode is required for this (such a node should be more or less a copy of its parent node
		//however if the next node does not decrease the intersection area there is no point adding a skip node
		if(!noBranchesDecreaseTheParentNodeIntersectionArea){
			//then add a ship node could be beneficial
			if(numberOfScenarios-subsetSize>(parentNode.scenarioNumber+1)-parentNode.scenariosIncluded){
				//means there are skips remaining on the current (parentNode)
				IntersectionNode childNode=new IntersectionNode(nextScenarioNumber, parentNode.scenariosIncluded, parentNode);
				parentNode.addChildNodeToLinkedList(childNode);
			}
		}
	}
	
	IntersectionNode updateCurrentNode(IntersectionNode currentNode){
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
					//increment the parent node current child node
					currentNode.currentChildNode=currentNode.currentChildNode.nextChildNode;
					//
					currentNode=currentNode.currentChildNode;
				}else{
					//nullify/dereference child nodes
					currentNode.currentChildNode=null;
					currentNode.headChildNode=null;
					currentNode.tailChildNode=null;
				}
			}
		}
		//
		return currentNode;
	}
	
	
	void initialiseCandidateList(){
		ScenarioSolution currentScenSol=head;
		headCL=head;
		tailCL=headCL;
		tailCL.nextScenSolCL=null;
		//
		currentScenSol=currentScenSol.nextScenSol;
		//
		while(currentScenSol!=null){
			tailCL.nextScenSolCL=currentScenSol;
			currentScenSol.prevScenSolCL=tailCL;
			tailCL=currentScenSol;
			//
			currentScenSol=currentScenSol.nextScenSol;
		}
		//
		lengthCL=length;
	}
	
	void constrainedInitialiseCandidateList(ScenarioSolution SS, int constrainedScenSolScenNum){
		//include this solution in the candidate list and suppress other solutions for the same scenario
		
		
		//add SS first
		//
		SS.nextScenSolCL=null;
		SS.prevScenSolCL=null;
		//
		headCL=SS;
		tailCL=SS;
		tailCL.nextScenSolCL=null;
		//
		lengthCL=1;
		/*if(tailCL.nextScenSolCL!=null){
			System.out.println();
		}*/
		//tailCL.nextScenSol=null;
		//
		for(int i=0;i<numberOfScenarios;i++){//good that this has already been already been added to the list (or at least an attempt was made)
			SS.constrainedDominated[i]=SS.dominated[i];
		}
		SS.constrainedDominated[constrainedScenSolScenNum]=false;
		
		
		//
		ScenarioSolution currentScenSol=head;
		//
		//currentScenSol.nextScenSolCL=null;
		//currentScenSol.prevScenSolCL=null;
		//
		
		
		/*headCL=head;
		tailCL=headCL;
		tailCL.nextScenSolCL=null;*/
		//for each scenaSol added to the list set constrainedDominated to dominated (values) and rule out the solution for the specified scenario
		//add the specified solution at the eand with the relevant element of constrainedDominated to false
		/*for(int i=0;i<numberOfScenarios;i++){
			currentScenSol.constrainedDominated[i]=currentScenSol.dominated[i];
		}
		currentScenSol.constrainedDominated[constrainedScenSolScenNum]=true;*/
		
		int counter=0;
		
		//
		//currentScenSol=currentScenSol.nextScenSol;
		//
		while(currentScenSol!=null){
			counter++;
			//System.out.println("counter="+counter);
			//
			
			if(currentScenSol!=SS){
				lengthCL++;
				//
				currentScenSol.nextScenSolCL=null;
				currentScenSol.prevScenSolCL=null;
				//
				tailCL.nextScenSolCL=currentScenSol;
				currentScenSol.prevScenSolCL=tailCL;
				tailCL=currentScenSol;
				//
				for(int i=0;i<numberOfScenarios;i++){
					currentScenSol.constrainedDominated[i]=currentScenSol.dominated[i];
				}
				currentScenSol.constrainedDominated[constrainedScenSolScenNum]=true;
			}
			//
			currentScenSol=currentScenSol.nextScenSol;
		}
		
		//
		//lengthCL=length+1;
	}
	
	
	void listNonDominatedVMixesPerScenario(){
		for(int i=0;i<numberOfScenarios;i++){
			System.out.println("scenarion "+i+" non dominated vehicle mixes");
			ScenarioSolution currentScenSol=head;
			int vTypes=currentScenSol.vMix[0].length;
			while(currentScenSol!=null){
			
				if(!currentScenSol.dominated[i]){
					for(int v=0;v<vTypes;v++){
						System.out.print(currentScenSol.vMix[i][v]+" ");
					}
					System.out.println();
				}
				//
				currentScenSol=currentScenSol.nextScenSol;
			}
		}
	}
	
	void clear(){
		head=null;
		tail=null;
		length=0;
	}
}
