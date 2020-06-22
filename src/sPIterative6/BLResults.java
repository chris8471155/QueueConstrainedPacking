package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


public class BLResults extends ObjectifiedRecourseAlgorithmSGCKS{
	
	public static void main(String expNameString, String[] ferryShapes, boolean useRuleBasedYardPolicy, int[] numbersOfLanes, int[][] classInstanceNumbers, int[] numberOfArrivalScenarios, int[] objectiveFunctions, boolean[] breakTiesLestFulls, boolean[] moduloScenarios, int[] yardPolicyTypes, int[][] comittedVMixQuantileIndices, double[] intersectionObjectiveWeights, int[][] iterationsSubIterations, double[] T0s, double[][] cumuNighbourDistributions, boolean visualiseBestSolutions, int[] initialArrivalScenarioSeeds, boolean[] useTriangularDistributionGeneToMutate, boolean useRecommendedParameters, double[][][] recommendedParams, boolean commitIntersectionVMix, boolean bestKnownSolution) throws Exception {
		//int ExpNumber=1;
		
		
		String resultsDirectory="results/";

		
		String outputFileName=resultsDirectory.concat("resultsOutputPackingParamExps".concat(expNameString).concat("GP"));
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		for(int i=0;i<ferryShapes.length;i++){
			outputFileName=outputFileName.concat(ferryShapes[i]);
		}
		outputFileName=outputFileName.concat(".txt");
		PrintWriter resultsOutput=new PrintWriter(new FileOutputStream(outputFileName));
		
		System.out.println("0expNameNumber 1ferry1 2nLanes 3class 4classInst 5obj 6break_ties 7expNum 8nArrivalScenarios initialArrivalScenSeed triangularDistMutation 9moduloScenarios 10YardPolicyType 11subsetSize 12packObjWeight bigIterations smallIterationsPack smallIterationsYard T0 neighbPar0 neighbPar1 neighbPar2 neighbPar3 neighbPar4 commitIntersectionVMix 13averageRevenue 14commitedVArea 15averagePackedVArea 16commitedUtilisation 17averageUtilisation 18min 19q1 20q2 21q3 22max 23solTime 24recourseRate 25successfulRecoveryRate 26aveRecourseAttempts 27rateInitialSolFeas 28rateInitialSGCKSFeas 29successfulRecoveryRate 30prevExpTime");
		System.out.print("resultsData=[");
		
		resultsOutput.println("0expNameNumber 1ferry1 2nLanes 3class 4classInst 5obj 6break_ties 7expNum 8nArrivalScenarios initialArrivalScenSeed triangularDistMutation 9moduloScenarios 10YardPolicyType 11subsetSize 12packObjWeight bigIterations smallIterationsPack smallIterationsYard T0 neighbPar0 neighbPar1 neighbPar2 neighbPar3 neighbPar4 commitIntersectionVMix 13averageRevenue 14commitedVArea 15averagePackedVArea 16commitedUtilisation 17averageUtilisation 18min 19q1 20q2 21q3 22max 23solTime 24recourseRate 25successfulRecoveryRate 26aveRecourseAttempts 27rateInitialSolFeas 28rateInitialSGCKSFeas 29successfulRecoveryRate 30prevExpTime");
		resultsOutput.print("resultsData=[");
		
		int[] experimentNameNumbers={0,1};//SA,GA
		int[] ferryShapeNumber={0,1};
		int[] triangularDistributionMutationNumber={0,1};
		
		int totalExps=ferryShapes.length*numbersOfLanes.length*classInstanceNumbers.length*objectiveFunctions.length*breakTiesLestFulls.length*numberOfArrivalScenarios.length*moduloScenarios.length*yardPolicyTypes.length;
		
		for(int i=0;i<ferryShapes.length;i++){
			int currentExperimentNumber=0;
			for(int j=0;j<numbersOfLanes.length;j++){
				for(int k=0;k<classInstanceNumbers.length;k++){
					
					//the best known solution can be read from here
					int[] arrivingVehicles=new int[0];
					if(bestKnownSolution){
						//
						String bestSolutionFileName="bestSolutionsGP/"+"TestInstanceClass"+String.valueOf(classInstanceNumbers[k][0])+ferryShapes[i]+"BestSolution"+String.valueOf(classInstanceNumbers[k][1])+"VehicleTypes.txt";
						BufferedReader bestSolR=new BufferedReader(new FileReader(bestSolutionFileName));
						String line="";
						while(!(line=bestSolR.readLine()).equals("Rectangles packed of each type")){
							
						}
						line=bestSolR.readLine();
						String[] lineA=line.split(",");
						arrivingVehicles=new int[lineA.length];
						for(int zx=0;zx<lineA.length;zx++){
							arrivingVehicles[zx]=Integer.parseInt(lineA[zx]);
						}
					}
					
					
					for(int l=0;l<objectiveFunctions.length;l++){
						for(int m=0;m<breakTiesLestFulls.length;m++){
							for(int n=0;n<numberOfArrivalScenarios.length;n++){
								for(int nn=0;nn<initialArrivalScenarioSeeds.length;nn++){
									for(int o=0;o<moduloScenarios.length;o++){
										for(int p=0;p<yardPolicyTypes.length;p++){
											for(int q=0;q<comittedVMixQuantileIndices[n].length;q++){//subset cannot have a greater size that the uncertainty set (q<=n)
												for(int r=0;r<intersectionObjectiveWeights.length;r++){
													for(int s=0;s<iterationsSubIterations.length;s++){//double[] T0s;
														for(int t=0;t<T0s.length;t++){
															for(int u=0;u<cumuNighbourDistributions.length;u++){
																for(int v=0;v<useTriangularDistributionGeneToMutate.length;v++){
																	//codify booleans
																	int breakTiesLestFullsNumber=0;
																	if(breakTiesLestFulls[m]){
																		breakTiesLestFullsNumber=1;
																	}
																	int moduloScenariosNumber=0;
																	if(moduloScenarios[o]){
																		moduloScenariosNumber=1;
																	}
																	int triangDistMutation=0;
																	if(useTriangularDistributionGeneToMutate[v]){
																		triangDistMutation=1;
																	}
																	int commitIntersectionVMixIndicator=0;
																	if(commitIntersectionVMix){
																		commitIntersectionVMixIndicator=1;
																	}
																	//0:T0
																	//1:nH1
																	//2:nH2
																	//3:nH3
																	//4:nH4
																	//5:nH5
																	//6:triDist
																	//7:numLanes
																	if(useRecommendedParameters){
																		//identify the class and instance
																		int classNumber=classInstanceNumbers[k][0]-5;
																		int instanceNumber=classInstanceNumbers[k][1]-1;
																		for(int kk=0;kk<5;kk++){
																			cumuNighbourDistributions[u][kk]=recommendedParams[classNumber][instanceNumber][kk+1];
																		}
																		//
																		if(recommendedParams[classNumber][instanceNumber][6]==1){
																			useTriangularDistributionGeneToMutate[v]=true;
																		}else{
																			useTriangularDistributionGeneToMutate[v]=false;
																		}
																		//
																		T0s[t]=recommendedParams[classNumber][instanceNumber][0];
																	}
																	
																	
																	
																	
																	System.out.print(experimentNameNumbers[1]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+classInstanceNumbers[k][1]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+initialArrivalScenarioSeeds[nn]+" "+triangDistMutation+" "+moduloScenariosNumber+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[n][q]+" "+intersectionObjectiveWeights[r]+" "+iterationsSubIterations[s][0]+" "+iterationsSubIterations[s][1]+" "+iterationsSubIterations[s][2]+" "+T0s[t]+" "+cumuNighbourDistributions[u][0]+" "+cumuNighbourDistributions[u][1]+" "+cumuNighbourDistributions[u][2]+" "+cumuNighbourDistributions[u][3]+" "+cumuNighbourDistributions[u][4]+" "+commitIntersectionVMixIndicator+" ");
																	resultsOutput.print(experimentNameNumbers[1]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+classInstanceNumbers[k][1]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+initialArrivalScenarioSeeds[nn]+" "+triangDistMutation+" "+moduloScenariosNumber+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[n][q]+" "+intersectionObjectiveWeights[r]+" "+iterationsSubIterations[s][0]+" "+iterationsSubIterations[s][1]+" "+iterationsSubIterations[s][2]+" "+T0s[t]+" "+cumuNighbourDistributions[u][0]+" "+cumuNighbourDistributions[u][1]+" "+cumuNighbourDistributions[u][2]+" "+cumuNighbourDistributions[u][3]+" "+cumuNighbourDistributions[u][4]+" "+commitIntersectionVMixIndicator+" ");
																	
																	
																	
																	
																	
																	
																	int t1=(int)System.currentTimeMillis();
																	callMain(expNameString, ferryShapes[i], numbersOfLanes[j], classInstanceNumbers[k], objectiveFunctions[l], breakTiesLestFulls[m], currentExperimentNumber, numberOfArrivalScenarios[n], useRuleBasedYardPolicy, moduloScenarios[o], yardPolicyTypes[p], comittedVMixQuantileIndices[n][q], intersectionObjectiveWeights[r], iterationsSubIterations[s], T0s[t], cumuNighbourDistributions[u], visualiseBestSolutions, initialArrivalScenarioSeeds[nn], useTriangularDistributionGeneToMutate[v], resultsOutput, commitIntersectionVMix, arrivingVehicles);
																	currentExperimentNumber++;
																	//System.out.println(currentExperimentNumber+" SA SOCKS packing experiments completed out of "+totalExps);
																	//System.out.println("prev exp took: "+((int)System.currentTimeMillis()-t1)+" millis and involved "+numberOfArrivalScenarios[n]+" arrival scenarios");
																	
																	System.out.println();
																	resultsOutput.println();
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		resultsOutput.close();
	}
	
	static void callMain(String expNameString, String ferryShape, int numberOfYardLanes, int[] classAndInstance, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, int ExperimentNumber, int numberOfArrivalScenarios, boolean useRuleBasedYardPolicy, boolean moduloScenario, int yardPolicyType, int comitVMixPercentileIndex, double intersectionObjectiveWeight, int[] iterationsSubIterations, double T0, double[] cumuNighbourDistribution, boolean visualiseBestSolutions, int initialArrivalScenarioSeed, boolean useTriangularDistributionGeneToMutate, PrintWriter resultsOutput, boolean commitIntersectionVMix, int[] arrivingVehicles) throws Exception{
		
		boolean printProgressOnConsole=false;//true;//
		
		// TODO Auto-generated method stub
		
		String solutionDirectory="solutionsAndResults/";
		if(useRuleBasedYardPolicy){
			solutionDirectory="solutionsAndResultsRB/";
		}
		String onlineRecourseAlgorithmName="Iterative_SA_GeneralPacking";
		String ExperimentName="GeneralPacking_Iterative_SA";
		
		//int ExperimentNumber=0;
		String expNamePrefixPrefix=expNameString.concat(ExperimentName.concat(String.valueOf(ExperimentNumber)));//is not a sensible beginning for a file name
		//String expNamePrefix=ExperimentName.concat(String.valueOf(ExperimentNumber));//is not a sensible beginning for a file name
		
		double onePixel=0.103076923;
		//String ferryShape="Rectangle";//"RF";//
		
		expNamePrefixPrefix=expNamePrefixPrefix.concat(ferryShape);
		
		//for(int jj=instanceRangesPerInstanceClass[testInstanceClassSet[ii]-1][0];jj<=instanceRangesPerInstanceClass[testInstanceClassSet[ii]-1][1];jj++){
		int testInstanceClass=classAndInstance[0];//testInstanceClassSet[ii];
		int instanceNumber=classAndInstance[1];//;//stroke instance number
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
		
		
		
		
		
		// TODO Auto-generated method stub
		boolean printProgress=false;//true;//
		boolean resetBinNumbers=true;
		
		int numberOfVehicleSelectionQuantiles=numberOfVTypes;//25;//this parameter is worth experimenting with
		int numberOfOrientations=3;//no top row. This simplifies the shuffling possibility and means that the packing solution serves as the loading procedure
		int alphabetLength=numberOfVehicleSelectionQuantiles*numberOfOrientations;
		
		int numberOfYardQuantiles=numberOfVTypes;//25;//this parameter is worth experimenting with
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
		
		
		
		//95440.0
		double totalRectangleArea=61009.0;//containerWidth*containerLength;
		
		if(ferryShape.equals("Rectangle")){
			totalRectangleArea=61009.0;
			
		}else if(ferryShape.equals("RF")){
			totalRectangleArea=95440.0;
		}
		int[] testInstanceClassSet={3};//,2,3
		//int[][] instanceRangesPerInstanceClass={{1,30},{1,30},{1,25}};//always have a row for each class in this array
		int[][] instancesPerInstanceClass={{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30},{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30},{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25}};//{{1,30},{1,30},{1,25}};

		
		//for(int ii=0;ii<testInstanceClassSet.length;ii++){
			//for(int jj=0;jj<instancesPerInstanceClass[ii].length;jj++){
				
				
				String[] vehicleNames={"car","van","mini bus","caravan","other towed","motor cycles","coaches","freight medium","freight large","drop trailer","unaccomp car","parcel cage","misc"};
				
				
				BasicPalette BP=new BasicPalette(Math.max(2, rectangleDistribution[0].length));
				

				double yardLaneMaxWidth=3.5/onePixel;
				
				//((10/onePixel)*(70/onePixel))
				
				double yardLaneMaxLength=(3*totalRectangleArea)/(numberOfYardLanes*yardLaneMaxWidth);//70/onePixel;/onePixel
				
				
				
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
				
				int[] randomArrivalScenarioSeeds=new int[numberOfArrivalScenarios];
				for(int i=0;i<numberOfArrivalScenarios;i++){
					randomArrivalScenarioSeeds[i]=i+initialArrivalScenarioSeed;
				}
				//

				//
				boolean nonConstrainedLoading=false;//true;//
				
				//boolean useRuleBasedYardPolicy=false;
				
				int vehicleScenarioSeed=1;
				
				//generate a problem instance
				Yard YA=new Yard(numberOfYardLanes, yardLaneMaxWidth, yardLaneMaxLength, rectangleDistribution, totalRectangleArea, BP, onePixel, new Random(vehicleScenarioSeed), false, nonConstrainedLoading, false, arrivingVehicles.length>0, arrivingVehicles);
				
				Solution.alphabetLength=4*rectangleDistribution[0].length;//alphabetLength;
				Solution.maxSolutionLength=YA.totalRectangles;//HA.initialLength;
				Solution.numberOfQuantiles=rectangleDistribution[0].length;//numberOfVehicleSelectionQuantiles;
				Solution.numberOfStripTypes=4;//numberOfOrientations;
				GeneralContainer.numberOfOrientations=4;//numberOfOrientations;
				GeneralContainer.numberOfVehicleSelectionQuantiles=rectangleDistribution[0].length;
				//GeneralContainer.setOfQuantiles=setOfQuantiles;
				GeneralContainer.onePixel=onePixel;//
				
				
				
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
				
				
				GeneralContainer[] bestSolContainer=new GeneralContainer[numberOfArrivalScenarios];//(YA, randNumGen, true, ferryShape, -1, totalRectangleArea, randomArrivalScenarioSeeds.length);
				for(int s=0;s<numberOfArrivalScenarios;s++){//randomArrivalScenarioSeeds[0].length
					bestSolContainer[s]=new GeneralContainer(YA, randNumGen, visualiseBestSolutions, ferryShape, -1, totalRectangleArea, numberOfArrivalScenarios, minWidth, minLength);
					bestSolContainer[s].YA.resetBinNumbers=true;
				}
				
				if(!visualiseBestSolutions){
					for(int s=0;s<numberOfArrivalScenarios;s++){
						bestSolContainer[s].setVisible(false);
					}
				}
				
				//crossover solutions
				Solution crossoverSol1=new Solution(randNumGen);//solution reference
				Solution crossoverSol2=new Solution(randNumGen);//solution reference
				YardSolution yCrossoverSol1=new YardSolution(randNumGen);//solution reference
				YardSolution yCrossoverSol2=new YardSolution(randNumGen);//solution reference
				//generate a population of solutions
				
				int t1=(int)System.currentTimeMillis();
				
				
				BLResults OBJAlg=new BLResults();
				bestSolContainer=OBJAlg.solveRecourseProblem(randomArrivalScenarioSeeds, YA, randNumGen, ferryShape, totalRectangleArea, objectiveFunctionIndex, breakTiesWithLeastFullLane, bestSolContainer, printProgress, false, false, 0, false, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, comitVMixPercentileIndex, intersectionObjectiveWeight, iterationsSubIterations, printProgressOnConsole, T0, cumuNighbourDistribution, minWidth, minLength, visualiseBestSolutions, useTriangularDistributionGeneToMutate, resultsOutput, commitIntersectionVMix);
				
				int tt=(int)System.currentTimeMillis()-t1;
				
				if(visualiseBestSolutions){
					for(int i=0;i<bestSolContainer.length;i++){
						bestSolContainer[i].dispose();//.setVisible(false);
					}
				}
				
		
		
	}
	
	//This is never used to solve on online recourse problem
	
	GeneralContainer[] solveRecourseProblem(int[] arrivalScenarioRandomSeeds, Yard YA, Random randNumGen, String ferryShape, double totalRectangleArea, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, GeneralContainer[] bestSolContainer, boolean printProgress, boolean bestSolContainerContainsInitialSolution, boolean onlineRecourseProblem, double claimedUtil, boolean recourseSolverOn, boolean useRuleBasedYardPolicy, boolean moduloScenario, int yardPolicyType, int subsetSize, double intersectionObjectiveWeight, int[] iterationsSubIterations, boolean printProgressOnConsole, double T0, double[] cumuNighbourDistributions, double minWidth, double minLength, boolean visualiseBestSolutions, boolean useTriangularDistributionGeneToMutate, PrintWriter resultsOutput, boolean commitIntersectionVMix) throws Exception{
		//return the best container
		//this where the simulated annealing algorithm begins (objectify this)
		//What are the return items for any algorithm: maximax objective (packing solution measure) (basically the contents of bestSolContainer
		//Just return bestSolContainer?
		
		//boolean displayBestSolOnSetB=true;
		
		boolean useOppositeTempScheme=false;//true;//
		
		int vTypes=YA.vTypes;
		
		int numberOfArrivalScenarios=arrivalScenarioRandomSeeds.length;
		//int numberOfArrivalScenarios2=arrivalScenarioRandomSeeds[1].length;
		
		//double a=yardLaneMaxLength*yardLaneMaxWidth*numberOfYardLanes;
		double bestOverallComittedUtilisation=0;//zero because there is isn't one yet
		int[] bestCommittedVMix=new int[vTypes];;
		int[] bestYardIterationCommittedVMix=new int[vTypes];;
		
		//(packing subproblem) resets every packing iteration (this applies only in packing iterations
		//yard policy iterations involve only one objective value
		double[] bestObj=new double[numberOfArrivalScenarios];
		double[] currentObj=new double[numberOfArrivalScenarios];
		
		int[] indOrd=null;
		
		//simulated annealing parameters
		//boolean useTriangularDistributionGeneToMutate=true;
		double A=0.5;
		double B=1-A;
		//double[] neighbourTypeDistribution={0.3844, 0.3844+0.2832, 1, 1};//
		//include random reasonable
		double[] neighbourTypeDistribution=cumuNighbourDistributions;//{0.25, 0.5, 0.75 ,0.75, 1};//
		//double[] neighbourTypeDistribution={0.2, 0.4, 0.6, 0.8, 1};//
		
		int numberOfIterations=iterationsSubIterations[0];//100;
		int[] iterationsPerBigIteration={iterationsSubIterations[1],iterationsSubIterations[2]};
		double t0Factor=T0;//0.2293;
		
		
		double totalAreaOfPotentiallyArrivingRectangles=0;
		for(int r=0;r<YA.allRectangles.length;r++){
			totalAreaOfPotentiallyArrivingRectangles+=YA.allRectangles[r].area;
		}
		
		//total iterations
		//per iteration (given the type)
		//mutation rate of current solution in transition
		bestSolContainer[0].implementBLDecreasingSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
		//bestOverallComittedUtilisation
		
		System.out.print(bestSolContainer[0].utilisation);
		resultsOutput.print(bestSolContainer[0].utilisation);
		
		
		
		
		return bestSolContainer;
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
