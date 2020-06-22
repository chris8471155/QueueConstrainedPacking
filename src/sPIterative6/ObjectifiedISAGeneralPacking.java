package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class ObjectifiedISAGeneralPacking extends ObjectifiedRecourseAlgorithmGeneralPacking{//
	public static void main(String[] args) throws Exception {
		String[] ferryShapes={"RF","Rectangle"};
		for(int i=0;i<ferryShapes.length;i++){
			callMain(ferryShapes[i]);
		}
	}
		
	static void callMain(String ferryShape) throws Exception{
	//public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		boolean printProgress=true;//false;//
		boolean resetBinNumbers=true;
		
		int numberOfVehicleSelectionQuantiles=25;//this parameter is worth experimenting with
		int numberOfOrientations=3;//no top row. This simplifies the shuffling possibility and means that the packing solution serves as the loading procedure
		int alphabetLength=numberOfVehicleSelectionQuantiles*numberOfOrientations;
		
		int numberOfYardQuantiles=25;//this parameter is worth experimenting with
		int numberOfYardOrientations=2;//no top row. This simplifies the shuffling possibility and means that the packing solution serves as the loading procedure
		int alphabetYardLength=numberOfYardQuantiles*numberOfYardOrientations;
		
		double[] setOfQuantiles=new double[numberOfVehicleSelectionQuantiles];
		for(int i=0;i<numberOfVehicleSelectionQuantiles;i++){
			setOfQuantiles[i]=i*((double)1/(numberOfVehicleSelectionQuantiles-1));
		}
		
		double[] setOfYardQuantiles=new double[numberOfYardQuantiles];
		for(int i=0;i<numberOfYardQuantiles;i++){
			setOfYardQuantiles[i]=i*((double)1/(numberOfYardQuantiles-1));
		}
		
		String solutionDirectory="solutionsAndResults/";
		
		String onlineRecourseAlgorithmName="Iterative_SA_GeneralPacking";
		String ExperimentName="GeneralPacking_Iterative_SA";
		int ExperimentNumber=0;
		String expNamePrefixPrefix=ExperimentName.concat(String.valueOf(ExperimentNumber));//is not a sensible beginning for a file name
		//String expNamePrefix=ExperimentName.concat(String.valueOf(ExperimentNumber));//is not a sensible beginning for a file name
		
		double onePixel=0.103076923;
		//String ferryShape="RF";//"Rectangle";//
		
		expNamePrefixPrefix=expNamePrefixPrefix.concat(ferryShape);
		
		//95440.0
		double totalRectangleArea=61009.0;//containerWidth*containerLength;
		
		if(ferryShape.equals("Rectangle")){
			totalRectangleArea=61009.0;
			
		}else if(ferryShape.equals("RF")){
			totalRectangleArea=95440.0;
		}
		
		int[] testInstanceClassSet={1,2,3};
		//int[][] instanceRangesPerInstanceClass={{1,30},{1,30},{1,25}};//always have a row for each class in this array
		int[][] instancesPerInstanceClass={{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30},{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30},{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25}};//{{1,30},{1,30},{1,25}};

		
		for(int ii=0;ii<testInstanceClassSet.length;ii++){
			for(int jj=0;jj<instancesPerInstanceClass[ii].length;jj++){
				
				//for(int jj=instanceRangesPerInstanceClass[testInstanceClassSet[ii]-1][0];jj<=instanceRangesPerInstanceClass[testInstanceClassSet[ii]-1][1];jj++){
				int testInstanceClass=testInstanceClassSet[ii];
				int instanceNumber=instancesPerInstanceClass[ii][jj];//stroke instance number
				int numWidths=-1;
				int numLengths=-1;
				
				
				String expNamePrefix=expNamePrefixPrefix.concat("Class").concat(String.valueOf(testInstanceClass)).concat("Instance").concat(String.valueOf(instanceNumber));
				
				String fileName="testInstances/TestInstanceClass".concat(String.valueOf(testInstanceClass)).concat("Distribution").concat(String.valueOf(instanceNumber)).concat("VehicleTypes.txt");
		
				BufferedReader testInstanceReader=new BufferedReader(new FileReader(fileName));
				//first line manually
				String[] lineA=testInstanceReader.readLine().split(",");//lengths of vehicles
				int numberOfVTypes=lineA.length;
				double[][] rectangleDistribution=new double[4][numberOfVTypes];
				for(int j=0;j<numberOfVTypes;j++){
					rectangleDistribution[0][j]=Double.parseDouble(lineA[j]);
				}
				//the remaining lines
				for(int i=1;i<4;i++){
					lineA=testInstanceReader.readLine().split(",");//lengths of vehicles
					for(int j=0;j<numberOfVTypes;j++){
						rectangleDistribution[i][j]=Double.parseDouble(lineA[j]);
					}
				}
				
				if(testInstanceClass==3){
					lineA=testInstanceReader.readLine().split(",");
					numWidths=Integer.parseInt(lineA[0]);
					numLengths=Integer.parseInt(lineA[0]);
				}
				testInstanceReader.close();
				
				
				String[] vehicleNames={"car","van","mini bus","caravan","other towed","motor cycles","coaches","freight medium","freight large","drop trailer","unaccomp car","parcel cage","misc"};
				
				
				BasicPalette BP=new BasicPalette(Math.max(2, rectangleDistribution[0].length));
				
				
				/*double containerWidth=17.382946;
				double containerLength=37.402631;
				double containerHeight=4.5;*/
				
				//yard lanes 
				int numberOfYardLanes=15;
				double yardLaneMaxWidth=3.5/onePixel;
				double yardLaneMaxLength=70/onePixel;
				
				
				//Iterative packing/yard policy genetic algorithm
				//	Use the best current solution to generate the population in each iteration
				
				//solution correction, if next gene is not feasible but a different one is use the feasible one
				//random number generator
				Random randNumGen=new Random(11);//12
				
				//initialise yard type static fields
				YardSolution.alphabetLength=alphabetYardLength;
				YardSolution.maxSolutionLength=numberOfYardLanes;//YA.totalRectangles;//HA.initialLength;
				Yard.numberOfYardOrientations=numberOfYardOrientations;
				Yard.numberOfYardQuantiles=numberOfYardQuantiles;
				Yard.setOfQuantiles=setOfYardQuantiles;
				
				int objectiveFunctionIndex=0;//false;//
				boolean breakTiesWithLeastFullLane=true;
				boolean useRuleBasedYardPolicy=false;
				
				int[] randomArrivalScenarioSeeds={2};//,1,3,4,5
				
				boolean nonConstrainedLoading=false;//true;//
				
				int vehicleScenarioSeed=1;
				
				//generate a problem instance
				//HoldingArea HA=new HoldingArea(rectangleDistribution, totalRectangleArea, BP, onePixel);
				Yard YA=new Yard(numberOfYardLanes, yardLaneMaxWidth, yardLaneMaxLength, rectangleDistribution, totalRectangleArea, BP, onePixel, new Random(vehicleScenarioSeed), false, nonConstrainedLoading, false, null);
				//YA.printQueues();
				//HA.printRectangles(true);
				//HA.printRectangles(false);
				
				//initialise static fields
				Solution.alphabetLength=4*rectangleDistribution[0].length;//alphabetLength;
				Solution.maxSolutionLength=YA.totalRectangles;//HA.initialLength;
				Solution.numberOfQuantiles=rectangleDistribution[0].length;//numberOfVehicleSelectionQuantiles;
				Solution.numberOfStripTypes=4;//numberOfOrientations;
				GeneralContainer.numberOfOrientations=4;//numberOfOrientations;
				GeneralContainer.numberOfVehicleSelectionQuantiles=rectangleDistribution[0].length;
				//GeneralContainer.setOfQuantiles=setOfQuantiles;
				GeneralContainer.onePixel=onePixel;//
				
				
				
				//Yard YA, Random randNumGen, boolean visualise, String ferryShape, int containerNumber
				//create a container for storing the best solution overall
				//GeneralContainer(Yard YA, Random randNumGen, boolean visualise, String ferryShape, int containerNumber, double totalArea, double minWidth, double minLength
				double minLength=Double.MAX_VALUE;
				double minWidth=Double.MAX_VALUE;
				for(int i=0;i<rectangleDistribution[0].length;i++){
					if(rectangleDistribution[0][i]<minLength){
						minLength=rectangleDistribution[0][i];
					}
					if(rectangleDistribution[1][i]<minWidth){
						minWidth=rectangleDistribution[1][i];
					}
				}
				
				//minWidth-=0.00001;
				//minLength-=0.00001;
				
				GeneralContainer bestSolContainer=new GeneralContainer(YA, randNumGen, true, ferryShape, -1, totalRectangleArea, randomArrivalScenarioSeeds.length, minWidth, minLength);
				bestSolContainer.YA.resetBinNumbers=true;
				
				
				ObjectifiedISAGeneralPacking OBJAlg=new ObjectifiedISAGeneralPacking();
				bestSolContainer=OBJAlg.solveRecourseProblem(randomArrivalScenarioSeeds, YA, randNumGen, ferryShape, totalRectangleArea, objectiveFunctionIndex, breakTiesWithLeastFullLane, bestSolContainer, printProgress, false, false, minWidth, minLength, false, useRuleBasedYardPolicy, false, null, 0, null, 0, null, true);
				
				
				
				
				////////////////////////////
				/////////////////////////////
				//Details for the yard policy test simulation
				//-ferry shape, the rectangle distribution, the random seeds for vehicle and arrival scenario(s),
				//-number of yard lanes and the dimensions of each lane
				//-read in the yard policy and the suggested initial packing solution for the recourse problem for any given arrival scenario
				PrintWriter problemInstanceParametersW=new PrintWriter(new FileOutputStream(solutionDirectory.concat("ProblemParameters".concat(expNamePrefix).concat(".txt"))));
				//randomArrivalScenarioSeeds,vehicleScenarioSeed
				for(int i=0;i<randomArrivalScenarioSeeds.length;i++){
					problemInstanceParametersW.print(randomArrivalScenarioSeeds[i]+",");
				}
				problemInstanceParametersW.println();
				//vehicle scenario random seed
				problemInstanceParametersW.println(vehicleScenarioSeed);
				//
				problemInstanceParametersW.close();
				
				//write the yard information and policy to a different file (not sure why yet but maybe for cross testing of some variety
				PrintWriter YardPolicyInformationAndPolicyW=new PrintWriter(new FileOutputStream(solutionDirectory.concat("YardPolicy".concat(expNamePrefix).concat(".txt"))));
				YardPolicyInformationAndPolicyW.println(numberOfYardLanes+","+yardLaneMaxWidth+","+yardLaneMaxLength);
				//alphabet and encoding information, form of yard policy
				YardPolicyInformationAndPolicyW.println(YardSolution.alphabetLength+","+YardSolution.maxSolutionLength+","+Yard.numberOfYardOrientations+","+Yard.numberOfYardQuantiles+","+breakTiesWithLeastFullLane);
				for(int i=0;i<Yard.setOfQuantiles.length;i++){
					YardPolicyInformationAndPolicyW.print(Yard.setOfQuantiles[i]+",");
				}
				YardPolicyInformationAndPolicyW.println();
				YardPolicyInformationAndPolicyW.println(bestSolContainer.YA.sol.getSolutionString());//store the single best packing solution in this container as well
				YardPolicyInformationAndPolicyW.close();
				
				PrintWriter packingSolutionW=new PrintWriter(new FileOutputStream(solutionDirectory.concat("PackingSolution".concat(expNamePrefix).concat(".txt"))));
				//alphabet information
				packingSolutionW.println(Solution.alphabetLength+","+Solution.maxSolutionLength+","+Solution.numberOfQuantiles+","+Solution.numberOfStripTypes+","+GeneralContainer.numberOfOrientations+","+GeneralContainer.numberOfVehicleSelectionQuantiles+","+objectiveFunctionIndex+","+onlineRecourseAlgorithmName);
				//objective function index for the recourse model (the recourse algorithm should match that used to derive this solution (objectify the loading algorithms for ease of coding)
				packingSolutionW.println(bestSolContainer.objectiveValue+","+bestSolContainer.utilisation);
				packingSolutionW.println(bestSolContainer.sol.getSolutionString());//store the single best packing solution in this container as well
				packingSolutionW.close();
			}
		}
	}
	
	GeneralContainer solveRecourseProblem(int[] arrivalScenarioRandomSeeds, Yard YA, Random randNumGen, String ferryShape, double totalRectangleArea, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, GeneralContainer bestSolContainer, boolean printProgress, boolean bestSolContainerContainsInitialSolution, boolean onlineRecourseProblem, double minWidth, double minLength, boolean recourseSolverOn, boolean useRuleBasedYardPolicy, boolean checkFeasibilityIPFirst, int[][] n, int yardPolicyType, int[] commitedVMix, double T0, double[] cumuNighbourDistributions, boolean useTriangularDistributionGeneToMutate) throws IOException, Exception{
		
		//total iterations
		//per iteration (given the type)
		//mutation rate of current solution in transition
		int numberOfIterations=300;
		
		//double a=yardLaneMaxLength*yardLaneMaxWidth*numberOfYardLanes;
		double bestObj=0;
		double currentObj=0;
		double bestUtil=0;
		
		//simulated annealing parameters
		//boolean useTriangularDistributionGeneToMutate=true;
		double A=0.5;
		double B=1-A;
		double[] neighbourTypeDistribution=cumuNighbourDistributions;//{0.3844, 0.3844+0.2832, 1};//
		int[] iterationsPerBigIteration={500,500};
		double t0Factor=T0;//0.4;//0.2293;
		
		boolean useOppositeTempScheme=true;
		double totalAreaOfPotentiallyArrivingRectangles=0;
		for(int r=0;r<YA.allRectangles.length;r++){
			totalAreaOfPotentiallyArrivingRectangles+=YA.allRectangles[r].area;
		}
		
		GeneralContainer currentSol=new GeneralContainer(YA, randNumGen, false, ferryShape, 0, totalRectangleArea, arrivalScenarioRandomSeeds.length ,minWidth,minLength);
		GeneralContainer neighbourSol=new GeneralContainer(YA, randNumGen, false, ferryShape, 1, totalRectangleArea, arrivalScenarioRandomSeeds.length, minWidth,minLength);
		
		if(bestSolContainerContainsInitialSolution){
			currentSol.sol.copyGeneSequence(bestSolContainer.sol);
		}
		
		if(onlineRecourseProblem){
			currentSol.YA.sol.copyGeneSequence(bestSolContainer.YA.sol);
			numberOfIterations=2;
			iterationsPerBigIteration[0]=2500;
			if(!recourseSolverOn){
				numberOfIterations=10;
				//iterationsPerBigIteration=09
				//t0Factor=0;//
				//iterationsPerBigIteration[0]=5000;
				//iterationsPerBigIteration[1]=0;
			}
		}
		
		/*Container[] population=new Container[populationSize];
		for(int i=0;i<populationSize;i++){
			population[i]=new Container(YA, randNumGen, false, ferryShape, i, totalRectangleArea);
		}*/
		//genetic algorithm code...
		//double bestObjective=Double.MAX_VALUE;//minimisation problem
		//
		
		currentSol.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, false, randNumGen);
		bestObj=currentSol.objectiveValue;
		currentObj=bestObj;
		bestUtil=currentSol.utilisation;
		
		//bestSolContainer.utilisationBeforeRecourse=bestUtil;
		
		boolean allVehiclesPacked=(currentSol.nonPackedVehicleArea==0);
		
		bestSolContainer.sol.copyGeneSequence(currentSol.sol);
		bestSolContainer.YA.sol.copyGeneSequence(currentSol.YA.sol);
		
		bestSolContainer.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, false, randNumGen);
		
		
		bestSolContainer.initialGPSolFeasible=allVehiclesPacked;
		bestSolContainer.recourseResolvedFromScratch=false;
		
		boolean recourseRequired=true;
		//feasibility IP inputs (if this is an online recourse action application)
		/*int[][][] w=new int[0][0][0];
		int V=YA.vTypes;
		int[] N=new int[YA.yardLanes.length];
		if(onlineRecourseProblem && checkFeasibilityIPFirst){
			//if the vehicles did not fit above
			//try the feasibility IP
			//input data: vCountsByCut (int[][]) from file
			//yard queue information (w, N, V):
			w=bestSolContainer.getYardQueueIndicatorMatrix(arrivalScenarioRandomSeeds[0], breakTiesWithLeastFullLane, false, 0, yardPolicyType, n);
			for(int i=0;i<bestSolContainer.YA.yardLanes.length;i++){
				N[i]=bestSolContainer.YA.yardLanes[i].length;
			}
			
			//check for feasibility
			FeasibilityIP FIP=new FeasibilityIP();
			//IP approach
			//recourseRequired=!FIP.isPackingSolutionFeasible(n, w, N, V);
			//implicit enumeration approach
			recourseRequired=!FIP.isPackingSolutionFeasible(n, bestSolContainer.YA.yardLanes, null, YA.vAreas);//bestSolContainer.v
			
			if(!recourseRequired){
				bestSolContainer.feasibilityEstablishedByIP=true;
			}
		}*/
		
		//what is the output (the promised utilisation, the planned packing solution can be implemented exactly
		//utilisation and objective value only
		
		//
		int yardIteration=0;
		for(int iteration=0;iteration<numberOfIterations && !allVehiclesPacked && recourseRequired;iteration++){
			
			/*if(iteration==96){
				System.out.println();
			}*/
			

			if(onlineRecourseProblem){
				yardIteration=0;//i.e. the yard policy is fixed in the online recourse problem case
			}
			if(useRuleBasedYardPolicy){
				yardIteration=0;
			}
			
			currentSol.sol.copyGeneSequence(bestSolContainer.sol);
			currentSol.YA.sol.copyGeneSequence(bestSolContainer.YA.sol);
			
				
			//if(yardIteration==0){
				neighbourSol.sol.copyGeneSequence(currentSol.sol);
				neighbourSol.YA.sol.copyGeneSequence(currentSol.YA.sol);
				neighbourSol.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, false, randNumGen);
			//}
			
			
			double bestObjThisIteration=Double.MAX_VALUE;
			//generations (for the iteration type)
			
			for(int subIteration=0;subIteration<iterationsPerBigIteration[yardIteration] && !allVehiclesPacked;subIteration++){
				
				/*if(iteration==96 && subIteration==831){
					System.out.println();
				}*/
				
				//temperature scheme
				double TT=(double)subIteration/iterationsPerBigIteration[yardIteration];
				double temp=(1-TT)*t0Factor*Math.abs((totalAreaOfPotentiallyArrivingRectangles-bestObj));
				
				if(useOppositeTempScheme){
					temp=(1-TT)*t0Factor*Math.abs(bestObj);
				}
				
				//set neigbouring solution to the current solution
				neighbourSol.sol.copyGeneSequence(currentSol.sol);
				neighbourSol.YA.sol.copyGeneSequence(currentSol.YA.sol);
				
				boolean useRandomReasonable=false;
				
				//generate a neighbouring solution
				//(update mutation operators in line with those used in the loading tool version)
				if(yardIteration==0){//useTriangularDistributionGeneToMutate
					//packing iteration
					//neighbourSol.sol.mutation(randNumGen);
					double randNeighbourNumber=randNumGen.nextDouble();
					if(randNeighbourNumber<=neighbourTypeDistribution[0]){
						if(useTriangularDistributionGeneToMutate){
							neighbourSol.sol.mutationTriangularDistributionGeneSelection(randNumGen, TT, A, B);
						}else{
							neighbourSol.sol.mutation(randNumGen);
						}
					}else if(randNeighbourNumber<=neighbourTypeDistribution[1]){
						if(useTriangularDistributionGeneToMutate){
							neighbourSol.sol.quantileBasedNeighbourTriangularDistributionGeneSelection(randNumGen, TT, A, B);
						}else{
							neighbourSol.sol.quantileBasedNeighbour(randNumGen);
						}
					}else if(randNeighbourNumber<=neighbourTypeDistribution[2]){
						if(useTriangularDistributionGeneToMutate){
							neighbourSol.sol.stripTypeBasedNeighbourTriangularDistributionGeneSelection(randNumGen, TT, A, B);
						}else{
							neighbourSol.sol.stripTypeBasedNeighbour(randNumGen);
						}
					}else if(randNeighbourNumber<=neighbourTypeDistribution[3]){
						neighbourSol.sol.swapConsecutiveRowsOrColumns(randNumGen);
					}else{
						useRandomReasonable=true;
					}
				}else{
					//yard iteration
					//extra consideration required for the search of yard policies
					//exploit order to avoid a continuum of symmetries
					neighbourSol.YA.sol.mutation(randNumGen);
				}
				
				
				//implement solution
				neighbourSol.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, yardIteration, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, useRandomReasonable, randNumGen);
				double obj=neighbourSol.objectiveValue;
				
				
				double invertedObj=(totalAreaOfPotentiallyArrivingRectangles-obj);
				double invertedBestSol=(totalAreaOfPotentiallyArrivingRectangles-bestObj);
				double invertedCurrentSol=(totalAreaOfPotentiallyArrivingRectangles-currentObj);
				
				
				/*if(Math.abs(10277.79016904905-obj)<0.000001){
					System.out.println();
				}*/
				
				boolean acceptNewSolution=false;
				if(useOppositeTempScheme){
					double delta=bestObj-obj;//current
					if(delta<0){
						//accept the solution
						acceptNewSolution=true;
					}else{
						//accept with temperature dependent probability
						if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (currentObj-obj<=0)){// && useLatestChanges 
							acceptNewSolution=true;
							//timesNonImprovingMoveAccepted++;
						}
					}
				}else{
					double delta=invertedObj-invertedBestSol;
					//is this solution an improvement
					if(delta<0){
						//accept the solution
						acceptNewSolution=true;
					}else{
						//accept with temperature dependent probability
						if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (invertedObj-invertedCurrentSol<=0)){// && useLatestChanges currentComittedUtilisation-obj
							acceptNewSolution=true;
							//timesNonImprovingMoveAccepted++;
						}
					}
				}
				//
				if(acceptNewSolution){
					currentObj=obj;
					
					currentSol.sol.copyGeneSequence(neighbourSol.sol);
					currentSol.YA.sol.copyGeneSequence(neighbourSol.YA.sol);

					if(obj>bestObj){
						bestObj=obj;
						
						allVehiclesPacked=(neighbourSol.nonPackedVehicleArea==0);
						bestSolContainer.recourseResolvedFromScratch=true;
						
						
						bestSolContainer.sol.copyGeneSequence(neighbourSol.sol);
						bestSolContainer.YA.sol.copyGeneSequence(neighbourSol.YA.sol);
						bestSolContainer.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, false, randNumGen);
						bestUtil=bestSolContainer.utilisation;

					}
				}
			}
			
			
			if(printProgress){
				//System.out.println(bestObjective+","+bestObjThisGeneration);
				//population[bestSolutionIndex].printSol(1, yardIteration);
				//bestSolContainer.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1);
				System.out.println(bestObj+","+bestObjThisIteration+","+bestSolContainer.objectiveValue);
				bestSolContainer.printSol(1, yardIteration);
				
				if(Math.abs(bestSolContainer.objectiveValue-bestObj)>0.000001){
					System.out.println();
				}
			}
			
			
			
			//
			yardIteration=1-yardIteration;//flip between yard and packing optimisation
		}
		//System.out.println("bestSolContainerCounter="+bestSolContainer.counter);
		bestSolContainer.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, false, randNumGen);
		
		return bestSolContainer;
	}
	
	int solvePreRecourseProblem(int[] arrivalScenarioRandomSeeds, Yard YA, Random randNumGen, String ferryShape, double totalRectangleArea, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, GeneralContainer bestSolContainer, boolean printProgress, boolean bestSolContainerContainsInitialSolution, boolean onlineRecourseProblem, double minWidth, double minLength, boolean recourseSolverOn, boolean useRuleBasedYardPolicy, boolean checkFeasibilityIPFirst, int[][] n, int yardPolicyType, int[] commitedVMix, double T0, double[] cumuNighbourDistributions, boolean useTriangularDistributionGeneToMutate, Solution[] initialSolutions) throws IOException, Exception{
		
		//total iterations
		//per iteration (given the type)
		//mutation rate of current solution in transition
		int numberOfIterations=300;
		
		//double a=yardLaneMaxLength*yardLaneMaxWidth*numberOfYardLanes;
		double bestObj=0;
		double currentObj=0;
		double bestUtil=0;
		
		//simulated annealing parameters
		//boolean useTriangularDistributionGeneToMutate=true;
		double A=0.5;
		double B=1-A;
		double[] neighbourTypeDistribution=cumuNighbourDistributions;//{0.3844, 0.3844+0.2832, 1};//
		int[] iterationsPerBigIteration={500,500};
		double t0Factor=T0;//0.4;//0.2293;
		
		boolean useOppositeTempScheme=true;
		double totalAreaOfPotentiallyArrivingRectangles=0;
		for(int r=0;r<YA.allRectangles.length;r++){
			totalAreaOfPotentiallyArrivingRectangles+=YA.allRectangles[r].area;
		}
		
		GeneralContainer currentSol=new GeneralContainer(YA, randNumGen, false, ferryShape, 0, totalRectangleArea, arrivalScenarioRandomSeeds.length ,minWidth,minLength);
		//GeneralContainer neighbourSol=new GeneralContainer(YA, randNumGen, false, ferryShape, 1, totalRectangleArea, arrivalScenarioRandomSeeds.length, minWidth,minLength);
		
		int bestInitialSolutionIndex=-1;
		double bestPreRecourseUtilisation=0;
		for(int i=0;i<initialSolutions.length;i++) {
			currentSol.sol.copyGeneSequence(initialSolutions[i]);
			currentSol.implementSolutionSimplifiedStrips(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, false, yardPolicyType, n, false, randNumGen);
			
			if(currentSol.utilisation>bestPreRecourseUtilisation) {
				bestPreRecourseUtilisation=currentSol.utilisation;
				bestInitialSolutionIndex=i;
			}
		}
		
		
		
		bestSolContainer.utilisationBeforeRecourse=bestPreRecourseUtilisation;
		return bestInitialSolutionIndex;
	}
	
	//crossover point should be with active parts of chromosomes
	//and not exactly at the start or end
	static void crossover(Solution sol1, Solution sol2, int crossoverPoint){
		Gene sol1Ref=sol1.getGene(crossoverPoint-1);
		Gene sol2Ref=sol2.getGene(crossoverPoint-1);
		//
		Gene geneRef1=sol1Ref.nextGene;
		Gene geneRef2=sol2Ref.nextGene;
		//
		geneRef1.prevGene=sol2Ref;
		sol2Ref.nextGene=geneRef1;
		//
		geneRef2.prevGene=sol1Ref;
		sol1Ref.nextGene=geneRef2;
		//an estimated active length for the new solutions are required for selecting
		//a mutation position
		int estimatedActiveLength=(int)(Math.round(((double)sol1.activeLength+sol2.activeLength)/2));
		sol1.activeLength=estimatedActiveLength;
		sol2.activeLength=estimatedActiveLength;
	}
	
	//crossover point should be with active parts of chromosomes
	//and not exactly at the start or end
	static void crossover(YardSolution sol1, YardSolution sol2, int crossoverPoint){
		Gene sol1Ref=sol1.getGene(crossoverPoint-1);
		Gene sol2Ref=sol2.getGene(crossoverPoint-1);
		//
		Gene geneRef1=sol1Ref.nextGene;
		Gene geneRef2=sol2Ref.nextGene;
		//
		geneRef1.prevGene=sol2Ref;
		sol2Ref.nextGene=geneRef1;
		//
		geneRef2.prevGene=sol1Ref;
		sol1Ref.nextGene=geneRef2;
	}
}
