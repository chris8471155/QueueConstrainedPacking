package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class ZISAParallelOneUncertaintySet extends ObjectifiedRecourseAlgorithmSGCKS{
	
	public static void main(String expNameString, String[] ferryShapes, boolean[] yardPolicyChoices, int[] numbersOfLanes, int[][] classInstanceNumbers, int[] numberOfArrivalScenarios, int[] objectiveFunctions, boolean[] breakTiesLestFulls, boolean[] moduloScenarios, int[] yardPolicyTypes, int[][] comittedVMixQuantileIndices, double[] intersectionObjectiveWeights, int[][] iterationsSubIterations, double[] T0s, double[][] cumuNighbourDistributions, boolean visualiseBestSolutions, int[] initialArrivalScenarioSeeds, boolean[] useTriangularDistributionGeneToMutate, boolean useRecommendedParameters, double[][][] recommendedParams, boolean commitIntersectionVMix, int[] levelOfVehicleIntersectionMixRelations, boolean bestKnownSolution) throws Exception {
		//int ExpNumber=1;
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		
		
		///=false;
		//String[] ferryShapes={"Rectangle","RF"};
		
		/*int[] numbersOfLanes={5,20};
		int[][] classInstanceNumbers={{1,5},{3,25}};
		int[] numberOfArrivalScenarios={1,5};
		int[] objectiveFunctions={0,1,2};
		boolean[] breakTiesLestFulls={true, false};*/
		
		//={5};
		//={{1,5}};
		//={1};
		//={0};
		//={true};
		
		String resultsDirectory="results/";
		//if(useRuleBasedYardPolicy){
			//resultsDirectory="resultsRB/";
		//}
		
		String outputFileName=resultsDirectory.concat("resultsOutputPackingParamExps".concat(expNameString).concat("SOCKS"));
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		for(int i=0;i<ferryShapes.length;i++){
			outputFileName=outputFileName.concat(ferryShapes[i]);
		}
		
		outputFileName=outputFileName.concat(".txt");
		PrintWriter resultsOutput=new PrintWriter(new FileOutputStream(outputFileName));
		
		System.out.println("0expNameNumber 1ferry1 2nLanes 3class 4classInst 5obj 6break_ties 7expNum 8nArrivalScenarios initialArrivalScenSeed triangularDistMutation 9moduloScenarios 10YardPolicyType 11subsetSize 12packObjWeight bigIterations smallIterationsPack smallIterationsYard T0 neighbPar0 neighbPar1 neighbPar2 neighbPar3 neighbPar4 commitIntersectionVMix levelOfVMIRelaxation useFCFSYardPolicy 13averageRevenue firstStageSolutionTime 14commitedVArea 15averagePackedVArea 16commitedUtilisation 17averageUtilisation 18min 19q1 20q2 21q3 22max 23solTime 24recourseRate 25successfulRecoveryRate 26aveRecourseAttempts 27rateInitialSolFeas 28rateInitialSGCKSFeas 29successfulRecoveryRate 30prevExpTime");
		System.out.print("resultsData=[");
		
		resultsOutput.println("0expNameNumber 1ferry1 2nLanes 3class 4classInst 5obj 6break_ties 7expNum 8nArrivalScenarios initialArrivalScenSeed triangularDistMutation 9moduloScenarios 10YardPolicyType 11subsetSize 12packObjWeight bigIterations smallIterationsPack smallIterationsYard T0 neighbPar0 neighbPar1 neighbPar2 neighbPar3 neighbPar4 commitIntersectionVMix levelOfVMIRelaxation useFCFSYardPolicy 13averageRevenue firstStageSolutionTime 14commitedVArea 15averagePackedVArea 16commitedUtilisation 17averageUtilisation 18min 19q1 20q2 21q3 22max 23solTime 24recourseRate 25successfulRecoveryRate 26aveRecourseAttempts 27rateInitialSolFeas 28rateInitialSGCKSFeas 29successfulRecoveryRate 30prevExpTime");
		resultsOutput.print("resultsData=[");
		
		//close the results file
		resultsOutput.close();
		
		int[] experimentNameNumbers={0,1};
		int[] ferryShapeNumber={0,1};
		
		int totalExps=ferryShapes.length*numbersOfLanes.length*classInstanceNumbers.length*objectiveFunctions.length*breakTiesLestFulls.length*numberOfArrivalScenarios.length*moduloScenarios.length*yardPolicyTypes.length;
		
		for(int i=0;i<ferryShapes.length;i++){
			int currentExperimentNumber=0;
			for(int j=0;j<numbersOfLanes.length;j++){
				for(int k=0;k<classInstanceNumbers.length;k++){
					
					//the best known solution can be read from here
					int[] arrivingVehicles=new int[0];
					if(bestKnownSolution){
						//
						String bestSolutionFileName="bestSolutionsSGCKS/"+"TestInstanceClass"+String.valueOf(classInstanceNumbers[k][0])+ferryShapes[i]+"BestSolution"+String.valueOf(classInstanceNumbers[k][1])+"VehicleTypes.txt";
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
								//int[] initialArrivalScenarioSeeds
								for(int nn=0;nn<initialArrivalScenarioSeeds.length;nn++){
									for(int o=0;o<moduloScenarios.length;o++){
										for(int p=0;p<yardPolicyTypes.length;p++){
											for(int q=0;q<comittedVMixQuantileIndices[n].length;q++){
												for(int r=0;r<intersectionObjectiveWeights.length;r++){
													for(int s=0;s<iterationsSubIterations.length;s++){//double[] T0s;
														for(int t=0;t<T0s.length;t++){
															for(int u=0;u<cumuNighbourDistributions.length;u++){
																for(int v=0;v<useTriangularDistributionGeneToMutate.length;v++){
																	for(int w=0;w<levelOfVehicleIntersectionMixRelations.length;w++){
																		for(int x=0;x<yardPolicyChoices.length;x++){
																			//use static field in VMC for minimal code change
																			VMixIntersectionCalculator.levelOfRelaxation=levelOfVehicleIntersectionMixRelations[w];
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
																			
																			//open the results file append=true
																			resultsOutput=new PrintWriter(new FileOutputStream(outputFileName,true));
																			
																			System.out.print(experimentNameNumbers[0]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+classInstanceNumbers[k][1]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+initialArrivalScenarioSeeds[nn]+" "+triangDistMutation+" "+moduloScenariosNumber+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[n][q]+" "+intersectionObjectiveWeights[r]+" "+iterationsSubIterations[s][0]+" "+iterationsSubIterations[s][1]+" "+iterationsSubIterations[s][2]+" "+T0s[t]+" "+cumuNighbourDistributions[u][0]+" "+cumuNighbourDistributions[u][1]+" "+cumuNighbourDistributions[u][2]+" "+cumuNighbourDistributions[u][3]+" "+cumuNighbourDistributions[u][4]+" "+commitIntersectionVMixIndicator+" "+levelOfVehicleIntersectionMixRelations[w]+" "+yardPolicyChoices[x]+" ");
																			resultsOutput.print(experimentNameNumbers[0]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+classInstanceNumbers[k][1]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+initialArrivalScenarioSeeds[nn]+" "+triangDistMutation+" "+moduloScenariosNumber+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[n][q]+" "+intersectionObjectiveWeights[r]+" "+iterationsSubIterations[s][0]+" "+iterationsSubIterations[s][1]+" "+iterationsSubIterations[s][2]+" "+T0s[t]+" "+cumuNighbourDistributions[u][0]+" "+cumuNighbourDistributions[u][1]+" "+cumuNighbourDistributions[u][2]+" "+cumuNighbourDistributions[u][3]+" "+cumuNighbourDistributions[u][4]+" "+commitIntersectionVMixIndicator+" "+levelOfVehicleIntersectionMixRelations[w]+" "+yardPolicyChoices[x]+" ");
																			
																			long t1=System.currentTimeMillis();
																			callMain(expNameString, ferryShapes[i], numbersOfLanes[j], classInstanceNumbers[k], objectiveFunctions[l], breakTiesLestFulls[m], currentExperimentNumber, numberOfArrivalScenarios[n], yardPolicyChoices[x], moduloScenarios[o], yardPolicyTypes[p], comittedVMixQuantileIndices[n][q], intersectionObjectiveWeights[r], iterationsSubIterations[s], T0s[t], cumuNighbourDistributions[u], visualiseBestSolutions, initialArrivalScenarioSeeds[nn], useTriangularDistributionGeneToMutate[v], resultsOutput, commitIntersectionVMix, arrivingVehicles);
																			currentExperimentNumber++;
																			//System.out.println(currentExperimentNumber+" SA SOCKS packing experiments completed out of "+totalExps);
																			//System.out.println("prev exp took: "+((int)System.currentTimeMillis()-t1)+" millis and involved "+numberOfArrivalScenarios[n]+" arrival scenarios");
																			
																			long t2=System.currentTimeMillis();
																			resultsOutput.print((t2-t1));
																			
																			System.out.println();
																			resultsOutput.println();
																			
																			//close the results file
																			resultsOutput.close();
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
			}
		}
	}
	
	static void callMain(String expNameString, String ferryShape, int numberOfYardLanes, int[] classAndInstance, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, int ExperimentNumber, int numberOfArrivalScenarios, boolean useRuleBasedYardPolicy, boolean moduloScenario, int yardPolicyType, int comitVMixPercentileIndex, double intersectionObjectiveWeight, int[] iterationsSubIterations, double T0, double[] cumuNighbourDistribution, boolean visualiseBestSolutions, int initialArrivalScenarioSeed, boolean useTriangularDistributionGeneToMutate, PrintWriter resultsOutput, boolean commitIntersectionVMix, int[] arrivingVehicles) throws Exception{
		
		
		
		
		boolean printProgressOnConsole=false;
		
		// TODO Auto-generated method stub
		
		String solutionDirectory="solutionsAndResults/";
		if(useRuleBasedYardPolicy){
			solutionDirectory="solutionsAndResultsRB/";
		}
		
		String onlineRecourseAlgorithmName="Iterative_SA_SOCKS";
		String ExperimentName="SOCKS_Iterative_SA";
		//int ExperimentNumber=0;
		String expNamePrefixPrefix=expNameString.concat(ExperimentName).concat(String.valueOf(ExperimentNumber));//is not a sensible beginning for a file name
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
				
				
				
				
				/*double containerWidth=17.382946;
				double containerLength=37.402631;
				double containerHeight=4.5;*/
				
				//yard lanes 
				//yard lanes 
				//int numberOfYardLanes=15;
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
				
				//int objectiveFunctionIndex=0;//false;//
				//boolean breakTiesWithLeastFullLane=false;
				
				//randomArrivalScenarioSeeds=new int[2][1];//{2};//,1,3,4,5
				int[] randomArrivalScenarioSeeds=new int[numberOfArrivalScenarios];
				for(int i=0;i<numberOfArrivalScenarios;i++){
					randomArrivalScenarioSeeds[i]=i+initialArrivalScenarioSeed;
				}
				//
				/*randomArrivalScenarioSeeds[1]=new int[numberOfArrivalScenarios2];
				for(int i=0;i<numberOfArrivalScenarios2;i++){
					randomArrivalScenarioSeeds[1][i]=i+1;//34
				}*/
				//
				boolean nonConstrainedLoading=false;//true;//
				
				//boolean useRuleBasedYardPolicy=false;
				
				int vehicleScenarioSeed=1;
				
				//generate a problem instance
				//HoldingArea HA=new HoldingArea(rectangleDistribution, totalRectangleArea, BP, onePixel);
				Yard YA=new Yard(numberOfYardLanes, yardLaneMaxWidth, yardLaneMaxLength, rectangleDistribution, totalRectangleArea, BP, onePixel, new Random(vehicleScenarioSeed), false, nonConstrainedLoading, false, arrivingVehicles.length>0, arrivingVehicles);
				
				//set the initial solution to the rule based solution
				YA.implementEvenDistributionSolution();
				
				
				//YA.printQueues();
				//HA.printRectangles(true);
				//HA.printRectangles(false);
				
				//initialise static fields
				
				Solution.alphabetLength=alphabetLength;
				Solution.maxSolutionLength=YA.totalRectangles;//HA.initialLength;
				Solution.numberOfQuantiles=numberOfVehicleSelectionQuantiles;
				Solution.numberOfStripTypes=numberOfOrientations;
				Container.numberOfOrientations=numberOfOrientations;
				Container.numberOfVehicleSelectionQuantiles=numberOfVehicleSelectionQuantiles;
				Container.setOfQuantiles=setOfQuantiles;
				Container.onePixel=onePixel;//

				
				
				//Yard YA, Random randNumGen, boolean visualise, String ferryShape, int containerNumber
				//create a container for storing the best solution overall
				
				Container[] bestSolContainer=new Container[numberOfArrivalScenarios];//(YA, randNumGen, true, ferryShape, -1, totalRectangleArea, randomArrivalScenarioSeeds.length);
				for(int s=0;s<numberOfArrivalScenarios;s++){//randomArrivalScenarioSeeds[0].length
					bestSolContainer[s]=new Container(YA, randNumGen, visualiseBestSolutions, ferryShape, -1, totalRectangleArea, numberOfArrivalScenarios);
					bestSolContainer[s].YA.resetBinNumbers=true;
				}
				
				/*if(!visualiseBestSolutions){
					for(int s=0;s<numberOfArrivalScenarios;s++){
						bestSolContainer[s].setVisible(false);
					}
				}*/
				
				//crossover solutions
				Solution crossoverSol1=new Solution(randNumGen);//solution reference
				Solution crossoverSol2=new Solution(randNumGen);//solution reference
				YardSolution yCrossoverSol1=new YardSolution(randNumGen);//solution reference
				YardSolution yCrossoverSol2=new YardSolution(randNumGen);//solution reference
				//generate a population of solutions
				
				int t1=(int)System.currentTimeMillis();
				
				ZISAParallelOneUncertaintySet OBJAlg=new ZISAParallelOneUncertaintySet();
				bestSolContainer=OBJAlg.solveRecourseProblem(randomArrivalScenarioSeeds, YA, randNumGen, ferryShape, totalRectangleArea, objectiveFunctionIndex, breakTiesWithLeastFullLane, bestSolContainer, printProgress, false, false, 0, false, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, comitVMixPercentileIndex, intersectionObjectiveWeight, iterationsSubIterations, printProgressOnConsole, T0, cumuNighbourDistribution, visualiseBestSolutions, useTriangularDistributionGeneToMutate, resultsOutput, commitIntersectionVMix);
				
				int tt=(int)System.currentTimeMillis()-t1;
				
				
				
				//Check for a best solution ever, if so store it
				//vehicle counts arrived, packed
				//vehicle dimensions
				//ferry shape
				//vehicle positions
				//solution image
				double maxPackedArea=0;
				double maxUtil=0;
				int bestSolutionIndex=-1;
				for(int i=0;i<bestSolContainer.length;i++){
					if(maxPackedArea<bestSolContainer[i].packedVehicleArea){
						maxPackedArea=bestSolContainer[i].packedVehicleArea;
						maxUtil=bestSolContainer[i].utilisation;
						bestSolutionIndex=i;
					}
				}
				
				String instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass".concat(String.valueOf(testInstanceClass)).concat(ferryShape).concat("BestUtilisation").concat(String.valueOf(instanceNumber)).concat("VehicleTypes.txt");
				
				double bestPackedAreaPreviously=0;
				//file the test instance to a file
				try {
					BufferedReader testInstanceClass1BestEverW=new BufferedReader(new FileReader(instanceClass1PrefixBestEver));
					bestPackedAreaPreviously=Double.parseDouble(testInstanceClass1BestEverW.readLine());
					testInstanceClass1BestEverW.close();
				}catch(Exception e) {
					
				}
				
				

				if(maxPackedArea>bestPackedAreaPreviously && visualiseBestSolutions){
					//a new best solution has been found
					//write the problem and solution to a file
					String bestSolutionFileName="bestSolutionsSGCKS/TestInstanceClass".concat(String.valueOf(testInstanceClass)).concat(ferryShape).concat("BestSolution").concat(String.valueOf(instanceNumber)).concat("VehicleTypes.txt");
					PrintWriter bestSolW=new PrintWriter(new FileOutputStream(bestSolutionFileName));
					//read the ferry shape file
					BufferedReader ferryShapeTraceR=new BufferedReader(new FileReader("simFiles".concat(ferryShape.concat("/ferryShapeTrace.txt"))));
					String line=ferryShapeTraceR.readLine();
					//write the bottom left
					bestSolW.println("Container details");
					//write the bottom left
					bestSolW.println(line);
					bestSolW.println("Container corner sequence");
					//write the ferry shape trace coordinates in a line 2,2;2,3; (format)
					while((line=ferryShapeTraceR.readLine())!=null){
						bestSolW.print(line+";");
					}
					//new line
					bestSolW.println();
					//
					bestSolW.println("Rectangle type dimensions (width,length)");//include ony those which have arrived
					//ArrayList<Integer> nonZeroVehicleTypes=new ArrayList<Integer>(10);
					int[] nonZeroVTypeIndex=new int[numberOfVTypes];
					//
					int[] vCountsArrived=bestSolContainer[bestSolutionIndex].YA.vCountByType;//new int[numberOfVTypes];
					int[] vCountsPacked=bestSolContainer[bestSolutionIndex].vMixes[0];//new int[numberOfVTypes];
					
					int nonZeroVTypeCount=0;
					for(int v=0;v<numberOfVTypes;v++){
						if(vCountsArrived[v]>0){
							nonZeroVTypeIndex[v]=nonZeroVTypeCount;
							nonZeroVTypeCount++;
						}else{
							nonZeroVTypeIndex[v]=-1;
						}
					}
					
					for(int v=0;v<numberOfVTypes;v++){
						if(nonZeroVTypeIndex[v]>-1){
							bestSolW.println(rectangleDistribution[1][v]+","+rectangleDistribution[0][v]);
						}
					}
					
					bestSolW.println("Rectangles to pack of each type");
					for(int v=0;v<numberOfVTypes;v++){
						if(nonZeroVTypeIndex[v]>-1){
							bestSolW.print(vCountsArrived[v]+",");
						}
					}
					bestSolW.println();
					
					bestSolW.println("Rectangles packed of each type");
					for(int v=0;v<numberOfVTypes;v++){
						if(nonZeroVTypeIndex[v]>-1){
							bestSolW.print(vCountsPacked[v]+",");
						}
					}
					bestSolW.println();
					
					bestSolW.println("Packed vehicle area="+maxPackedArea);
					bestSolW.println("Container utilisation="+maxUtil);
					
					bestSolW.println("Rectangle coordinates (rectangle type, x-coordinate, y-coordinate),");
					//
					int numberOfBins=bestSolContainer[bestSolutionIndex].numberOfBins;
					Bin[] bins=bestSolContainer[bestSolutionIndex].bins;
					//
					for(int i=0;i<numberOfBins;i++){
						double[] currentTopLeft=new double[2];
						currentTopLeft[0]=bins[i].pos[0];
						currentTopLeft[1]=bins[i].pos[1];
						
						/*if(printBinAndRectangleCoordinates){
							System.out.println("bin_"+i+", width="+bins[i].w+", length="+bins[i].l+", top left coordinate={"+currentTopLeft[0]+","+currentTopLeft[1]+"}");
						}*/
						
						/*g.setColor(Color.CYAN);
						g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(bins[i].w/onePixel), (int)Math.round(bins[i].l/onePixel));*/
						if(bins[i].orientation==0){
							//bottom row (shift x-coordinate of "currentTopLeft" right)
							int numberOfRectangles=bins[i].numberOfRectangles;
							for(int j=0;j<numberOfRectangles;j++){
								Rectangle currentRectangle=bins[i].rectangles[j];
								
								bestSolW.print("("+nonZeroVTypeIndex[currentRectangle.type]+","+currentTopLeft[0]+","+currentTopLeft[1]+"),");
								
								currentTopLeft[0]+=currentRectangle.w;//pixelWidth;
							}
						}else{
							//left or right column (shift y-coordinate of "currentTopLeft" right)
							int numberOfRectangles=bins[i].numberOfRectangles;
							for(int j=0;j<numberOfRectangles;j++){
								Rectangle currentRectangle=bins[i].rectangles[j];
								
								bestSolW.print("("+nonZeroVTypeIndex[currentRectangle.type]+","+currentTopLeft[0]+","+currentTopLeft[1]+"),");
								
								currentTopLeft[1]+=currentRectangle.l;//.pixelLength;
							}
						}
					}
					bestSolW.println();
					bestSolW.close();
					
					//generate solution image
					String instanceClass1PrefixImage="bestSolutionsSGCKS/TestInstanceClass".concat(String.valueOf(testInstanceClass)).concat(ferryShape).concat("BestSolutionImage").concat(String.valueOf(instanceNumber)).concat("VehicleTypes");
					bestSolContainer[bestSolutionIndex].saveImage(instanceClass1PrefixImage,"png");
					
					//overwrite the best packed vehicle area for this test instance
					PrintWriter testInstanceClass1BestEverWW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
					testInstanceClass1BestEverWW.println(maxPackedArea);
					testInstanceClass1BestEverWW.close();
				}
				
				
				if(visualiseBestSolutions){
					for(int i=0;i<bestSolContainer.length;i++){
						//bestSolContainer[i].dispose();//.setVisible(false);
					}
				}
				
				
				
				//now store a selection of possible solutions. If in simulation testing none of the packing solutions is feasible use the solution which manages
				//to load the highest area of vehicles as the initial solution when resolving the packing problem (optimal queue constrained sgcks packing can be used then after starting with the heuristic approach
				
				/*queuesPopped[arrivalScenario][numberOfQueuesPopped[arrivalScenario]]=currentRectangle.queueNumber;
				popMoveTypes[arrivalScenario][numberOfQueuesPopped[arrivalScenario]]=orientation;//
				numberOfQueuesPopped[arrivalScenario]++;*/
				//Print the instructions for implementing the best solution in the general container
				//assume 1 for now
				/*PrintWriter packInstructionsW=new PrintWriter(new FileOutputStream("generalPackingInstrictions.txt"));
				
				//bestSolContainer.queuesPopped
				for(int i=0;i<bestSolContainer.numberOfQueuesPopped[0];i++){
					packInstructionsW.print(bestSolContainer.queuesPopped[0][i]+","+bestSolContainer.popMoveTypes[0][i]+","+bestSolContainer.stripNumber[0][i]+"£");
				}
				packInstructionsW.println();
				packInstructionsW.close();
				*/
				//bestSolContainer.printBinAndRectangleCoordinates=false;//true;
				
				
				//Write the chromosome for the best yard policy
				PrintWriter bestYardPolicyW=new PrintWriter(new FileOutputStream("bestYardChromosome.txt"));
				
				String yardSolutionString=bestSolContainer[0].YA.sol.getSolutionString();
				
				bestYardPolicyW.print(yardSolutionString);//yardSolutionString
				
				bestYardPolicyW.close();
				
				///////////////////////////////////////////////
				///////////////////////////////////////////////
				//visualise the best solution
				//bestSolContainer.implementSolution(randomArrivalScenarioSeeds);
				//bestSolContainer[0].paintMultipleSolutions(randomArrivalScenarioSeeds[0], breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null);
				
				
				
				
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
				//write the commited vehicle mix here
				int vTypes=YA.vTypes;
				for(int v=0;v<vTypes;v++){
					if(commitIntersectionVMix){
						problemInstanceParametersW.print(bestSolContainer[0].bestCommittedVMixReference[v]+",");
					}else{
						problemInstanceParametersW.print(bestSolContainer[0].YA.vCountByType[v]+",");
					}
				}
				problemInstanceParametersW.println();
				//put the actual full initial mix of vehicle here
				for(int v=0;v<vTypes;v++){//for computing the penalty
					problemInstanceParametersW.print(bestSolContainer[0].YA.vCountByType[v]+",");
				}
				problemInstanceParametersW.println();
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
				YardPolicyInformationAndPolicyW.println(bestSolContainer[0].YA.sol.getSolutionString());//store the single best packing solution in this container as well
				YardPolicyInformationAndPolicyW.close();
				
				
				PrintWriter packingSolutionW=new PrintWriter(new FileOutputStream(solutionDirectory.concat("PackingSolution".concat(expNamePrefix).concat(".txt"))));
				//alphabet information
				packingSolutionW.println(Solution.alphabetLength+","+Solution.maxSolutionLength+","+Solution.numberOfQuantiles+","+Solution.numberOfStripTypes+","+Container.numberOfOrientations+","+Container.numberOfVehicleSelectionQuantiles+","+objectiveFunctionIndex+","+onlineRecourseAlgorithmName+","+tt);
				for(int i=0;i<Container.setOfQuantiles.length;i++){
					packingSolutionW.print(Container.setOfQuantiles[i]+",");
				}
				packingSolutionW.println();
				//objective function index for the recourse model (the recourse algorithm should match that used to derive this solution (objectify the loading algorithms for ease of coding)
				//this should be the utilsation of the comitted vehicle mix
				//also include value 2 (in additional lines
				//packingSolutionW.println(bestSolContainer[0].objectiveValue+","+bestSolContainer[0].utilisation);
				packingSolutionW.println(bestSolContainer[0].bestCommitedUtilisation+","+(bestSolContainer[0].bestCommitedUtilisation/bestSolContainer[0].totalArea));//+","+bestSolContainer[0].utilisation
				packingSolutionW.println(numberOfArrivalScenarios);//+","+bestSolContainer[0].utilisation
				for(int s=0;s<numberOfArrivalScenarios;s++){
					packingSolutionW.println(bestSolContainer[s].sol.getSolutionString());//store the single best packing solution in this container as well
				}
				for(int s=0;s<numberOfArrivalScenarios;s++){
					packingSolutionW.println(bestSolContainer[s].sol.getSolutionString2());//store the single best packing solution in this container as well
				}
				//value2 is required for the optimal SGCKS packing formulation (values2s just set the bin sizes)
				packingSolutionW.close();
				
				//THE FOLLOWING IS ALSO THE SET OF VEHICLES THAT THE ALGORITHM HAS COMITTED TO PACKING 
				//I.E. THIS REPRESENTS THE SET OF VEHICLES WHO HAVE BOUGHT TICKETS AND HAVE BEEN ALLOCATED TO 
				//A PARTICULAR SAILING
				
				//THIS IS STILL SLIGHT WEIRD (THIS IMPLIES THAT SOME BOOKINGS HAVE TO BE RENEGOTIATED)
				
				
				//the number of vehicles of each type in each cut
				//Noting that some of the orders of the cuts are interchangeable
				//a packing solution only has to be feasible in one of these
				//What/how alternative cut orderings are there
				//1. left columns followed by right columns can be interchanged (in some cases).
				//start with strict ordering (this may still not be that restrictive provided there are enough yard lanes and relatively few vehicle types
				//the key question is at what point(s) random arrivals begin to be a problem. Frthermore once this has been identified the following techniques
				//will reduce the thresholds at which problems arise.
				PrintWriter vehiclesOfEachTypePerCutW=new PrintWriter(new FileOutputStream(solutionDirectory.concat("vehiclesOfEachTypePerCut").concat(expNamePrefix).concat(".txt")));
				vehiclesOfEachTypePerCutW.println(numberOfArrivalScenarios);
				for(int s=0;s<numberOfArrivalScenarios;s++){
					int[][] n=bestSolContainer[s].ns[0];//getVehicleCountsByCut();//input for feasibility IP
					//line per cut
					for(int i=0;i<n.length;i++){//cut
						for(int j=0;j<n[i].length;j++){//vehicle type
							//total numbers
							vehiclesOfEachTypePerCutW.print(n[i][j]+",");
						}
						vehiclesOfEachTypePerCutW.print(";");
					}
					vehiclesOfEachTypePerCutW.println();
				}
				vehiclesOfEachTypePerCutW.close();
			//}
		//}
		
				if(visualiseBestSolutions){
					for(int i=0;i<bestSolContainer.length;i++){
						bestSolContainer[i].dispose();//.setVisible(false);
					}
				}
		
	}
	
	//This is never used to solve on online recourse problem
	
	Container[] solveRecourseProblem(int[] arrivalScenarioRandomSeeds, Yard YA, Random randNumGen, String ferryShape, double totalRectangleArea, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, Container[] bestSolContainer, boolean printProgress, boolean bestSolContainerContainsInitialSolution, boolean onlineRecourseProblem, double claimedUtil, boolean recourseSolverOn, boolean useRuleBasedYardPolicy, boolean moduloScenario, int yardPolicyType, int subsetSize, double intersectionObjectiveWeight, int[] iterationsSubIterations, boolean printProgressOnConsole, double T0, double[] cumuNighbourDistributions, boolean visualiseBestSolutions, boolean useTriangularDistributionGeneToMutate, PrintWriter resultsOutput, boolean commitIntersectionVMix) throws Exception{
		//return the best container
		//this where the simulated annealing algorithm begins (objectify this)
		//What are the return items for any algorithm: maximax objective (packing solution measure) (basically the contents of bestSolContainer
		//Just return bestSolContainer?
		
		//boolean displayBestSolOnSetB=true;
		
		boolean useOppositeTempScheme=true;
		
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
		
		
		Container[] currentSol=new Container[numberOfArrivalScenarios];//(YA, randNumGen, false, ferryShape, 0, totalRectangleArea, arrivalScenarioRandomSeeds.length);//each packing solution is evaluated in each arrival scenario
		Container[] neighbourSol=new Container[numberOfArrivalScenarios];//(YA, randNumGen, false, ferryShape, 1, totalRectangleArea, arrivalScenarioRandomSeeds.length);
		//Container[] bestPackSols=new Container[numberOfArrivalScenarios];//packing iterations are like subproblems. This array stores the best packing solutions for the incumbent yard policy 
		
		Solution[] tempSolStorage=new Solution[numberOfArrivalScenarios];
		for(int s=0;s<numberOfArrivalScenarios;s++){
			tempSolStorage[s]=new Solution(randNumGen);
		}
		
		
		//for evaluating the current solution for each arrival scenario only on its own arrival scenario
		int[][] arrivalScenarioArrivalScenarioSeeds=new int[numberOfArrivalScenarios][1];
		//int[][] arrivalScenarioArrivalScenarioSeeds2=new int[numberOfArrivalScenarios2][1];
		//one packing solution per arrival scenario
		for(int s=0;s<numberOfArrivalScenarios;s++){
			currentSol[s]=new Container(YA, randNumGen, false, ferryShape, s, totalRectangleArea, numberOfArrivalScenarios);//each packing solution is evaluated in each arrival scenario
			neighbourSol[s]=new Container(YA, randNumGen, false, ferryShape, s+numberOfArrivalScenarios, totalRectangleArea, numberOfArrivalScenarios);
			//bestPackSols[s]=new Container(YA, randNumGen, false, ferryShape, s+2*numberOfArrivalScenarios, totalRectangleArea, numberOfArrivalScenarios);
			arrivalScenarioArrivalScenarioSeeds[s][0]=arrivalScenarioRandomSeeds[s];
		}
		
		
		//if(bestSolContainerContainsInitialSolution){
			for(int s=0;s<numberOfArrivalScenarios;s++){
				currentSol[s].sol.copyGeneSequence(bestSolContainer[s].sol);
			}
		//}
		
		if(onlineRecourseProblem){
			numberOfIterations=2;
			iterationsPerBigIteration[0]=2500;
			for(int s=0;s<numberOfArrivalScenarios;s++){
				currentSol[s].YA.sol.copyGeneSequence(bestSolContainer[s].YA.sol);
			}
			
			if(!recourseSolverOn){
				numberOfIterations=0;
				//iterationsPerBigIteration=09
			}
		}
		
		
		//one uncertainty set, the objective is the same in yard and packing iterations
		//
		ScenarioSolutionList SSL=new ScenarioSolutionList(YA.rectangleDistribution, YA.vAreas, numberOfArrivalScenarios);//Pareto optimal set of packing solutions for the current best yard policy
		SSL.A=YA.vAreas;
		ScenarioSolution.A=YA.vAreas;
		//ScenarioSolutionList.numberOfScenarios=numberOfArrivalScenarios;
		ScenarioSolution[] newScenSols=new ScenarioSolution[numberOfArrivalScenarios];
		
		ScenarioSolution[] scenSolArray=new ScenarioSolution[numberOfArrivalScenarios];
		ScenarioSolution[] tempScenSolArray=new ScenarioSolution[numberOfArrivalScenarios];
		
		ScenarioSolution[] bestPackSolScenSolArray=new ScenarioSolution[numberOfArrivalScenarios];
		
		//refs to SSL arrays
		//solNumberInSubsetWithHighestIndividualUtil
		boolean assignBestSolutionsToScenarioContainers=true;//false;//
		boolean[] isInSubSet=SSL.isInSubSet;
		int[] bestSolNumberPerScenario=SSL.bestSolNumberPerScenario;
		
		
		//initial evaluation for setting the current and best objective value (per arrival scenario)
		//in this first evaluation, evaluate each solution (i.e. for each arrival scenario) for each arrival scenario
		
		for(int s=0;s<numberOfArrivalScenarios;s++){
			currentSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
			
			ScenarioSolution newScenarioSolution=new ScenarioSolution(currentSol[s].sol, currentSol[s].YA.sol, currentSol[s].vMixes, s);
			scenSolArray[s]=newScenarioSolution;
			bestPackSolScenSolArray[s]=newScenarioSolution;
			
			SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
			
			//initialise some reusable ScenarioSolution objects
			ScenarioSolution newScenarioSolution2=new ScenarioSolution(currentSol[s].sol, currentSol[s].YA.sol, currentSol[s].vMixes, s);
			tempScenSolArray[s]=newScenarioSolution2;
		}
		
		for(int s=0;s<numberOfArrivalScenarios;s++){//The solution for scenario s
			//
			//ScenarioSolution SS, int constrainedScenSolScenNum, int subsetSize
			if(objectiveFunctionIndex==0){
				currentSol[s].intersectionObjVal=SSL.constrainedMaxIntersectionAreaSolutionSubSet(subsetSize, scenSolArray[s]);
			}else if(objectiveFunctionIndex==1){
				currentSol[s].intersectionObjVal=SSL.constrainedBestUtilisationSubSet(subsetSize, scenSolArray[s]);
			}else if(objectiveFunctionIndex==2){
				currentSol[s].intersectionObjVal=SSL.constrainedWorstCaseSubSet(subsetSize, scenSolArray[s]);
			}
			//=SSL.maxIntersectionArea;
			currentSol[s].packingObj=((1-intersectionObjectiveWeight)*currentSol[s].objValPerArrivalScenario[s])+(intersectionObjectiveWeight*currentSol[s].intersectionObjVal);
			bestObj[s]=currentSol[s].packingObj;
		}
		SSL.clear();
		
		
		//copy the best solutions for each arrival scenario, copy these to bestSolContainer[]
		//and set the current objective values
		for(int s=0;s<numberOfArrivalScenarios;s++){

			bestSolContainer[s].sol.copyGeneSequence(currentSol[s].sol);
			bestSolContainer[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);
			
			/*if(!visualiseBestSolutions){
				bestSolContainer[s].setVisible(true);
			}*/
			
			bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
			
			/*if(!visualiseBestSolutions){
				bestSolContainer[s].setVisible(false);
			}*/
			
			boolean useValues2=true;//arrivalScenarioArrivalScenarioSeeds[s]
			//bestSolContainer[s].implementSolutionQueueConstrainedOptimallyFillBins(arrivalScenarioArrivalScenarioSeeds2[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, bestSolContainer[s].totalArea, false, randNumGen, useValues2);
			bestSolContainer[s].n=bestSolContainer[s].ns[0];
			//
			currentObj[s]=bestObj[s];
			//
		}
		
		//////////////////////////////////The ns from set 1 feasibility evaluate in set 2
		//////////////////////////////////
		//compute the yard iteration objective value "bestOverallComittedUtilisation"
		double currentComittedUtilisation=0;
		//double bestProceduralComittedUtilisation=0;
		//indOrd=maths.maths.Sort(bestObj, true);
		
		
		//CREATE A REUSABLE ARRAY OF SCENARIO SOLUTIONS (ALTHOUGH INTERSECTIONODE MAYBE MORE OF A BOTTLENECK)

		
		boolean maximumUtilEstablished=false;
		double maxUtilSoFar=0;
		//consider each minor uncertainty set packing solution in turn (int[][] n)
		//stored in "neighbourSol" in decreasing utilisation order
		//stop and store the best achievable vehicle mix (and the associated vehicle mix)
		//if: 1) full feasibility is proven, or the best vehicle mix achieved has a utilisation greater than or equal to the maximum utilisation associated with the next packing solution that will be considered
		for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
			//bestSolContainer[indOrd[s]].YA.printQueues();
			//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType);
			
			bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
			//now that values2 are filled in, 
			//bestSolContainer[s].implementSolutionQueueConstrainedOptimallyFillBins(arrivalScenarioRandomSeeds[1], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, bestSolContainer[s].totalArea, false, randNumGen, true);
			if(printProgressOnConsole){
				for(int ss=0;ss<numberOfArrivalScenarios;ss++){
					System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
				}
				System.out.println();
			}
			
			ScenarioSolution newScenarioSolution=new ScenarioSolution(bestSolContainer[s].sol, bestSolContainer[s].YA.sol, bestSolContainer[s].vMixes, s);
			bestPackSolScenSolArray[s]=newScenarioSolution;
			SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
			
		}
		
		//compute the intersection area vehicle mix and the associated utilisation
		//the overall packing objective is the max intersection area of the current set of non-dominated packing solutions for the current yard policy
		if(objectiveFunctionIndex==0){
			newScenSols=SSL.maxIntersectionAreaSolutionSubSet(subsetSize);//.maxIntersectionAreaSolution();
		}else if(objectiveFunctionIndex==1){
			//the average of the best utilisations for each scenario
			newScenSols=SSL.bestUtilisationSubSet(subsetSize);//.maxIntersectionAreaSolution();
		}else if(objectiveFunctionIndex==2){
			//the average of the best utilisations for each scenario
			newScenSols=SSL.worstCaseSubSet(subsetSize);//.maxIntersectionAreaSolution();
		}
		//has a new best intersection area been found
		if(SSL.maxIntersectionArea>bestOverallComittedUtilisation){
			if(objectiveFunctionIndex==0){
				for(int v=0;v<vTypes;v++){
					bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
				}
			}
			bestOverallComittedUtilisation=SSL.maxIntersectionArea;//this only becomes the new overall objective value if a yard policy is found that guarantees that this intersection area is achieved with a probability that is above the specified minimum level
			//do best solution sharing if used
			if(assignBestSolutionsToScenarioContainers){
				/*solNumberInSubsetWithHighestIndividualUtil
				isInSubSet[i]
				bestSolNumberPerScenario[*/
				
				for(int s=0;s<numberOfArrivalScenarios;s++){
					
					//if(isInSubSet[s]){
						tempSolStorage[s].copyGeneSequence(bestSolContainer[bestSolNumberPerScenario[s]].sol);
						tempScenSolArray[s].reuseScenarioSolution(bestPackSolScenSolArray[bestSolNumberPerScenario[s]], s);
						//scenSolArray[s]=bestPackSolScenSolArray[bestSolNumberPerScenario[s]];//bestPackSolScenSolArray
					/*}else{
						tempSolStorage[s].copyGeneSequence(bestSolContainer[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
						tempScenSolArray[s].reuseScenarioSolution(bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], s);
						//scenSolArray[s]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
					}*/
				}
				
				for(int s=0;s<numberOfArrivalScenarios;s++){
					
					bestPackSolScenSolArray[s].reuseScenarioSolution(tempScenSolArray[s], s);
					bestSolContainer[s].sol.copyGeneSequence(tempSolStorage[s]);
					
					/*if(!visualiseBestSolutions){
						bestSolContainer[s].setVisible(true);
					}*/
					
					bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
					
					/*if(!visualiseBestSolutions){
						bestSolContainer[s].setVisible(false);
					}*/
				}
			}
		}
		SSL.clear();
		
		
			

		////////////////////////////////////
		////////////////////////////////////
		int counter=0;
		//
		int yardIteration=0;
		for(int iteration=0;iteration<numberOfIterations;iteration++){// && (!onlineRecourseProblem || (onlineRecourseProblem && claimedUtil>bestUtil))
			
			if(onlineRecourseProblem){
				yardIteration=0;//i.e. the yard policy is fixed in the online recourse problem case
			}
			if(useRuleBasedYardPolicy){
				yardIteration=0;
			}
			
			//set the solutions to the best so far at the beginning for each big iteration
			for(int s=0;s<numberOfArrivalScenarios;s++){
				
				//in the current version "bestPackSol" always contains the packing solutions associated with the best overall yard policy
				//in which case why not get rid of "bestPackSol"
				currentSol[s].sol.copyGeneSequence(bestSolContainer[s].sol);
				currentSol[s].YA.sol.copyGeneSequence(bestSolContainer[s].YA.sol);
				
					
				neighbourSol[s].sol.copyGeneSequence(currentSol[s].sol);
				neighbourSol[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);
			}
			
			
			//completely reset the objective values (yard iterations have only a single objective value
			//Within iteration objectives
			if(yardIteration==0){
				//may have changed because the yard policy may have changed
				
				//reset the packing subproblem objective values as the objective values of the minor uncertainty set packing solutions may have changes due to a change in the yard policy
				bestObj=new double[numberOfArrivalScenarios];
				currentObj=new double[numberOfArrivalScenarios];
				
				//evaluation of the initial and best within iteration solutions
				for(int s=0;s<numberOfArrivalScenarios;s++){
					currentSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
					
					ScenarioSolution newScenarioSolution=new ScenarioSolution(currentSol[s].sol, currentSol[s].YA.sol, currentSol[s].vMixes, s);
					bestPackSolScenSolArray[s]=newScenarioSolution;
					//scenSolArray[s]=newScenarioSolution;
					SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
					
				}
				//
				for(int s=0;s<numberOfArrivalScenarios;s++){//The solution for scenario s
					//
					if(objectiveFunctionIndex==0){
						currentSol[s].intersectionObjVal=SSL.constrainedMaxIntersectionAreaSolutionSubSet(subsetSize, bestPackSolScenSolArray[s]);
					}else if(objectiveFunctionIndex==1){
						currentSol[s].intersectionObjVal=SSL.constrainedBestUtilisationSubSet(subsetSize, bestPackSolScenSolArray[s]);
					}else if(objectiveFunctionIndex==2){
						currentSol[s].intersectionObjVal=SSL.constrainedWorstCaseSubSet(subsetSize, bestPackSolScenSolArray[s]);
					}
					
					currentSol[s].packingObj=((1-intersectionObjectiveWeight)*currentSol[s].objValPerArrivalScenario[s])+(intersectionObjectiveWeight*currentSol[s].intersectionObjVal);
					bestObj[s]=currentSol[s].packingObj;
					//for(int ss=0;ss<numberOfArrivalScenarios;ss++){//Implemented in scenario ss
						//currentSol[s].intersectionObjValPerArrivalScenario[ss]=((1-intersectionObjectiveWeight)*currentSol[s].objValPerArrivalScenario[ss])+(intersectionObjectiveWeight*currentSol[s].intersectionObjVal);
					//}
				}
				SSL.clear();
				
				
			}else{
			}
			
			
			for(int subIteration=0;subIteration<iterationsPerBigIteration[yardIteration];subIteration++){// && (!onlineRecourseProblem || (onlineRecourseProblem && claimedUtil>bestUtil))
				//System.out.println("subIteration number="+subIteration);
				//subiterations are now completely different for packing and yard iterations
				if(yardIteration==0){
					for(int s=0;s<numberOfArrivalScenarios;s++){
						//temperature scheme
						double TT=(double)subIteration/iterationsPerBigIteration[yardIteration];
						double temp=(1-TT)*t0Factor*Math.abs((totalAreaOfPotentiallyArrivingRectangles-bestObj[s]));
						
						if(useOppositeTempScheme){
							temp=(1-TT)*t0Factor*Math.abs(bestObj[s]);
						}
						
						//set neigbouring solution to the current solution
						neighbourSol[s].sol.copyGeneSequence(currentSol[s].sol);
						neighbourSol[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);
						
						boolean generateARandomReasonablePackingSolution=false;
						
						/*if(printProgressOnConsole){
							if(counter==22 && s==2 && subIteration==396 && iteration==20){
								System.out.println();
							}
						}*/
						
						//generate a neighbouring solution
						//(update mutation operators in line with those used in the loading tool version)
						double randNeighbourNumber=randNumGen.nextDouble();
						if(randNeighbourNumber<=neighbourTypeDistribution[0]){
							if(useTriangularDistributionGeneToMutate){
								neighbourSol[s].sol.mutationTriangularDistributionGeneSelection(randNumGen, TT, A, B);
							}else{
								neighbourSol[s].sol.mutation(randNumGen);
							}
						}else if(randNeighbourNumber<=neighbourTypeDistribution[1]){
							if(useTriangularDistributionGeneToMutate){
								neighbourSol[s].sol.quantileBasedNeighbourTriangularDistributionGeneSelection(randNumGen, TT, A, B);
							}else{
								neighbourSol[s].sol.quantileBasedNeighbour(randNumGen);
							}
						}else if(randNeighbourNumber<=neighbourTypeDistribution[2]){
							if(useTriangularDistributionGeneToMutate){
								neighbourSol[s].sol.stripTypeBasedNeighbourTriangularDistributionGeneSelection(randNumGen, TT, A, B);
							}else{
								neighbourSol[s].sol.stripTypeBasedNeighbour(randNumGen);
							}
						}else if(randNeighbourNumber<=neighbourTypeDistribution[3]){
							neighbourSol[s].sol.swapConsecutiveRowsOrColumns(randNumGen);
						}else{
							//random reasonable
							generateARandomReasonablePackingSolution=true;
							
							//if UB exists consider using optimisation here
						}
						
						//implement solution   arrivalScenarioArrivalScenarioSeeds[s]
						neighbourSol[s].implementSolution(arrivalScenarioRandomSeeds, yardIteration, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, generateARandomReasonablePackingSolution, randNumGen);
						
						
						ScenarioSolution newScenarioSolution=new ScenarioSolution(neighbourSol[s].sol, neighbourSol[s].YA.sol, neighbourSol[s].vMixes, s);
						
						scenSolArray[s]=newScenarioSolution;
						SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
						
						//Add the current best solution for the other scenarios
						for(int ss=0;ss<numberOfArrivalScenarios;ss++){
							if(s!=ss){
								SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(bestPackSolScenSolArray[ss]);//allways contains the best solution containers scenario solution objects
							}
						}
						if(objectiveFunctionIndex==0){
							neighbourSol[s].intersectionObjVal=SSL.constrainedMaxIntersectionAreaSolutionSubSet(subsetSize, scenSolArray[s]);//bestPackSolScenSolArray[s]
						}else if(objectiveFunctionIndex==1){
							neighbourSol[s].intersectionObjVal=SSL.constrainedBestUtilisationSubSet(subsetSize, scenSolArray[s]);//bestPackSolScenSolArray[s]
						}else if(objectiveFunctionIndex==2){
							neighbourSol[s].intersectionObjVal=SSL.constrainedWorstCaseSubSet(subsetSize, scenSolArray[s]);//bestPackSolScenSolArray[s]
						}
						
						neighbourSol[s].packingObj=((1-intersectionObjectiveWeight)*neighbourSol[s].objValPerArrivalScenario[s])+(intersectionObjectiveWeight*neighbourSol[s].intersectionObjVal);
						SSL.clear();//clears the list of ScenarioSolutions only (not the solution just calculated
						
						double obj=neighbourSol[s].packingObj;//for one arrival scenario evaluations the objective value is always the utilisation
						
						double invertedObj=(totalAreaOfPotentiallyArrivingRectangles-obj);
						double invertedBestSol=(totalAreaOfPotentiallyArrivingRectangles-bestObj[s]);
						double invertedCurrentSol=(totalAreaOfPotentiallyArrivingRectangles-currentObj[s]);
						
						boolean acceptNewSolution=false;
						if(useOppositeTempScheme){
							double delta=bestObj[s]-obj;//current
							
							//is this solution an improvement
							if(delta<0){
								//accept the solution
								acceptNewSolution=true;
							}else{
								//accept with temperature dependent probability
								if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (currentObj[s]-obj<=0)){// && useLatestChanges 
									acceptNewSolution=true;
									//timesNonImprovingMoveAccepted++;
								}
							}
						}else{
							double delta=invertedObj-invertedBestSol;//current
							
							//is this solution an improvement
							if(delta<0){
								//accept the solution
								acceptNewSolution=true;
							}else{
								//accept with temperature dependent probability
								if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (invertedObj-invertedCurrentSol<=0)){// && useLatestChanges 
									acceptNewSolution=true;
									//timesNonImprovingMoveAccepted++;
								}
							}
						}
						
						
						//
						if(acceptNewSolution){
							currentObj[s]=obj;
							//if the solution that has just been evaluated is accepted
							currentSol[s].sol.copyGeneSequence(neighbourSol[s].sol);
							currentSol[s].YA.sol.copyGeneSequence(neighbourSol[s].YA.sol);
							
							//
							if(obj>bestObj[s]){
								
								bestObj[s]=obj;
		
								//bestPackSols[s].sol.copyGeneSequence(neighbourSol[s].sol);
								//bestPackSols[s].YA.sol.copyGeneSequence(neighbourSol[s].YA.sol);
								//bestPackSols[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
							}
						}
						
						
						//It may also be the case that the overall objective has been improved by the current solution
						if(neighbourSol[s].intersectionObjVal>bestOverallComittedUtilisation){
							
							counter++;
							if(printProgressOnConsole){
								System.out.println(counter);
							}
							
							
							
							
							bestOverallComittedUtilisation=neighbourSol[s].intersectionObjVal;
							if(objectiveFunctionIndex==0){
								for(int v=0;v<vTypes;v++){
									bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
								}
							}
							
							//store the best yard policy this iteration in "bestSolPack"
							
							//get rid of "bestPackSol", use "bestSolContainer"
							
							//have I just overwritten something this is used
							bestSolContainer[s].sol.copyGeneSequence(neighbourSol[s].sol);
							
							if(printProgressOnConsole){
								if(counter>-1){
									System.out.println(counter+","+s+","+subIteration+","+iteration);
									
									neighbourSol[s].sol.printSolutionString();
									//System.out.print("obj vals of new best solutions");
									bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, yardIteration, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);//generateARandomReasonablePackingSolution
									for(int ss=0;ss<numberOfArrivalScenarios;ss++){
										bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[ss], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
										//System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
									}
									//System.out.println();
									System.out.print("bestSolNumberPerScenario: ");
									for(int ss=0;ss<numberOfArrivalScenarios;ss++){
										System.out.print(bestSolNumberPerScenario[ss]+",("+isInSubSet[ss]+"),");
									}
									System.out.println();
								}
							}
							
							//bestPackSols[s].YA.sol.copyGeneSequence(neighbourSol[s].YA.sol);//this is not necessary because this is a packing iteration and the yard policy cannot change
							//n needs to be stores for the subsequence simulation feasibility checking (The assumption here
							//is that n is never automatically assigned, I set n=ns[0] at the end of packing iterations
							//bestSolContainer[0].n=bestPackSols[0].n;
							
							
							bestPackSolScenSolArray[s]=scenSolArray[s];
							
							if(assignBestSolutionsToScenarioContainers){//sometimes its good to forget
								/*solNumberInSubsetWithHighestIndividualUtil
								isInSubSet[i]
								bestSolNumberPerScenario[*/
								//scenSolArray[s
								for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									
									//if(isInSubSet[ss]){
										tempSolStorage[ss].copyGeneSequence(bestSolContainer[bestSolNumberPerScenario[ss]].sol);
										tempScenSolArray[ss].reuseScenarioSolution(bestPackSolScenSolArray[bestSolNumberPerScenario[ss]], ss);
										//tempScenSolArray[ss]=bestPackSolScenSolArray[bestSolNumberPerScenario[ss]];//bestPackSolScenSolArray
									/*}else{
										tempSolStorage[ss].copyGeneSequence(bestSolContainer[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
										tempScenSolArray[ss].reuseScenarioSolution(bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], ss);
										//tempScenSolArray[ss]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
									}*/
								}
								if(printProgressOnConsole){
									System.out.println("line 811 bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
								}
								for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									//this may over write objects before they have been copied elsewhere they may be required
									bestPackSolScenSolArray[ss].reuseScenarioSolution(tempScenSolArray[ss], ss);
									bestSolContainer[ss].sol.copyGeneSequence(tempSolStorage[ss]);
									currentSol[ss].sol.copyGeneSequence(tempSolStorage[ss]);
									
									/*if(!visualiseBestSolutions){
										bestSolContainer[ss].setVisible(true);
									}*/
									
									bestSolContainer[ss].implementSolution(arrivalScenarioArrivalScenarioSeeds[ss], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
									
									/*if(!visualiseBestSolutions){
										bestSolContainer[s].setVisible(false);
									}*/
									
									if(printProgressOnConsole){
										System.out.print(bestSolContainer[ss].objectiveValue+",");
									}
									
								}
								if(printProgressOnConsole){
									System.out.println("-----------");
								}
								
								/*for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									bestSolContainer[ss].sol.printSolutionString();//.YA
									neighbourSol[ss].sol.printSolutionString();//.YA
									bestSolContainer[ss].sol.printSolutionString();
									System.out.println("----------------");
								}
								System.out.println();*/
								
							}else{
								//The issue here is that we do not know in which scenario the new solution helped to improve the overall solution
								
								//FOR CHECKING PURPOSES, REEVALUATE ALL THE BEST SOLUTIONS AND PRINT OUT THE UTILISATIONS IN EACH SCENARIO
								//NO ACHIEVED UTILISATION SHOULD BE IN EXCESS OF "bestOverallComittedUtilisation"
								//if(printProgressOnConsole){
									System.out.println("line 932 checking new best overall objective "+bestOverallComittedUtilisation);
									for(int sss=0;sss<numberOfArrivalScenarios;sss++){
										bestSolContainer[sss].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
										for(int ss=0;ss<numberOfArrivalScenarios;ss++){
											System.out.print(bestSolContainer[sss].objValPerArrivalScenario[ss]+",");
										}
										System.out.println();
									}
								//}
								
								
								//replace the SceanrioSolution object for bestPackSol[s]
								//ScenarioSolution newScenarioSolution=
								bestPackSolScenSolArray[s]=new ScenarioSolution(bestSolContainer[s].sol, bestSolContainer[s].YA.sol, bestSolContainer[s].vMixes, s);//newScenarioSolution;
								//scenSolArray[s]
								
								
								bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
								
							}
							
							
							
							
							
							
							if(printProgressOnConsole){
								System.out.println("bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
							}
							
						}
						//SSL.clear();
						
					}
					
					//If a current solution was changed it may be possible that the set of current packing solution may be a better combination of packing solutions for the
					//set of arrival scenarios
					//perform a non constrreained maximum intersection area based on the current solutions (which have already been implemented)
					
					for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
						//bestSolContainer[indOrd[s]].YA.printQueues();
						//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType);
						
						currentSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
						
						ScenarioSolution newScenarioSolution=new ScenarioSolution(currentSol[s].sol, currentSol[s].YA.sol, currentSol[s].vMixes, s);
						scenSolArray[s]=newScenarioSolution;
						SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
						
					}
					
					//compute the intersection area vehicle mix and the associated utilisation
					//the overall packing objective is the max intersection area of the current set of non-dominated packing solutions for the current yard policy
					if(objectiveFunctionIndex==0){
						newScenSols=SSL.maxIntersectionAreaSolutionSubSet(subsetSize);//.maxIntersectionAreaSolution();
					}else if(objectiveFunctionIndex==1){
						newScenSols=SSL.bestUtilisationSubSet(subsetSize);//.maxIntersectionAreaSolution();
					}else if(objectiveFunctionIndex==2){
						newScenSols=SSL.worstCaseSubSet(subsetSize);//.maxIntersectionAreaSolution();
					}
					
					//has a new best intersection area been found
					
					double obj=SSL.maxIntersectionArea;
					
					//no simulated annealing here
					//Just accept if it is an overall improvement
					if(obj>bestOverallComittedUtilisation){
							
						bestOverallComittedUtilisation=obj;
						if(objectiveFunctionIndex==0){
							for(int v=0;v<vTypes;v++){
								bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
							}
						}
						
						
						if(assignBestSolutionsToScenarioContainers){
							/*solNumberInSubsetWithHighestIndividualUtil
							isInSubSet[i]
							bestSolNumberPerScenario[*/
							//scenSolArray[s
							for(int s=0;s<numberOfArrivalScenarios;s++){
								
								//if(isInSubSet[s]){
									tempSolStorage[s].copyGeneSequence(currentSol[bestSolNumberPerScenario[s]].sol);
									tempScenSolArray[s].reuseScenarioSolution(scenSolArray[bestSolNumberPerScenario[s]], s);
									//tempScenSolArray[ss]=bestPackSolScenSolArray[bestSolNumberPerScenario[ss]];//bestPackSolScenSolArray
								/*}else{
									tempSolStorage[s].copyGeneSequence(currentSol[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
									tempScenSolArray[s].reuseScenarioSolution(scenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], s);
									//tempScenSolArray[ss]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
								}*/
							}
							if(printProgressOnConsole){
								System.out.println("line 908 bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
							}
							for(int s=0;s<numberOfArrivalScenarios;s++){
								//this may over write objects before they have been copied elsewhere they may be required
								bestPackSolScenSolArray[s].reuseScenarioSolution(tempScenSolArray[s], s);
								bestSolContainer[s].sol.copyGeneSequence(tempSolStorage[s]);
								currentSol[s].sol.copyGeneSequence(tempSolStorage[s]);
								
								/*if(!visualiseBestSolutions){
									bestSolContainer[s].setVisible(true);
								}*/
								
								bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
								
								/*if(!visualiseBestSolutions){
									bestSolContainer[s].setVisible(false);
								}*/
								
								if(printProgressOnConsole){
									System.out.print(bestSolContainer[s].objectiveValue+",");
								}
							}
							if(printProgressOnConsole){
								System.out.println();
								for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									bestSolContainer[ss].YA.sol.printSolutionString();
									bestSolContainer[ss].sol.printSolutionString();
									System.out.println("----------------");
								}
								System.out.println();	
							}
							
						}else{
							//same again "bestPackSol"...
							System.out.println("line 987 checking new best overall objective "+bestOverallComittedUtilisation);
							for(int s=0;s<numberOfArrivalScenarios;s++){//
								//the packing solutions can also be accepted as best overall as well
								bestSolContainer[s].sol.copyGeneSequence(currentSol[s].sol);
								//bestPackSols[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);//DELIBERATELY "neighbourSol["0"].YA.sol"! SEE ABOVE
	
								bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
								for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
								}
								System.out.println();
								
								
								//System.out.print("scenario "+s+" uses packing solution "+newScenSols[s].packingSolutionNumber+", ");
								bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
								
								bestPackSolScenSolArray[s]=scenSolArray[s];
							}
						}
						
						
						
						
						
						//System.out.println();
						if(printProgressOnConsole){
							System.out.println("bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
						}
						
					}
					
					SSL.clear();
					
				}else{
					//temperature scheme
					double TT=(double)subIteration/iterationsPerBigIteration[yardIteration];
					double temp=(1-TT)*t0Factor*Math.abs((totalAreaOfPotentiallyArrivingRectangles-bestOverallComittedUtilisation));
					
					if(useOppositeTempScheme){
						temp=(1-TT)*t0Factor*Math.abs(bestOverallComittedUtilisation);
					}
					//Generate a neighbouring yard policy (copy it into the neighbouring solution container for
					//each minor uncertainty set solution)
					//Consider each arrival scenario in the major uncertainty set (might be smaller)
					
					neighbourSol[0].YA.sol.copyGeneSequence(currentSol[0].YA.sol);
					
					neighbourSol[0].YA.sol.mutation(randNumGen);
					for(int s=1;s<numberOfArrivalScenarios;s++){
						neighbourSol[s].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);
					}
					
					for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
						//bestSolContainer[indOrd[s]].YA.printQueues();
						//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType);
						
						neighbourSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
						//now that values2 are filled in, 
						
						ScenarioSolution newScenarioSolution=new ScenarioSolution(neighbourSol[s].sol, neighbourSol[s].YA.sol, neighbourSol[s].vMixes, s);
						scenSolArray[s]=newScenarioSolution;
						
						SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
						
					}
					
					//compute the intersection area vehicle mix and the associated utilisation
					//the overall packing objective is the max intersection area of the current set of non-dominated packing solutions for the current yard policy
					if(objectiveFunctionIndex==0){
						newScenSols=SSL.maxIntersectionAreaSolutionSubSet(subsetSize);//.maxIntersectionAreaSolution();
					}else if(objectiveFunctionIndex==1){
						newScenSols=SSL.bestUtilisationSubSet(subsetSize);//.maxIntersectionAreaSolution();
					}else if(objectiveFunctionIndex==2){
						newScenSols=SSL.worstCaseSubSet(subsetSize);//.maxIntersectionAreaSolution();
					}
					
					//has a new best intersection area been found
					
					double obj=SSL.maxIntersectionArea;
					
					double invertedObj=(totalAreaOfPotentiallyArrivingRectangles-obj);
					double invertedBestSol=(totalAreaOfPotentiallyArrivingRectangles-bestOverallComittedUtilisation);
					double invertedCurrentSol=(totalAreaOfPotentiallyArrivingRectangles-currentComittedUtilisation);
					
					//double delta=invertedObj-invertedBestSol;//current
					
					SSL.clear();
					
					boolean acceptNewSolution=false;
					
					if(useOppositeTempScheme){
						//do simulated annealing accept/reject, current and best overall solution update
						double delta=bestOverallComittedUtilisation-obj;//current
						
						//is this solution an improvement
						if(delta<0){
							//accept the solution
							acceptNewSolution=true;
						}else{
							//accept with temperature dependent probability
							if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (currentComittedUtilisation-obj<=0)){// && useLatestChanges 
								acceptNewSolution=true;
								//timesNonImprovingMoveAccepted++;
							}
						}
					}else{
						//do simulated annealing accept/reject, current and best overall solution update
						double delta=invertedObj-invertedBestSol;//bestOverallComittedUtilisation-obj;//current
						
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
						currentComittedUtilisation=obj;
						//if the solution that has just been evaluated is accepted
						
						currentSol[0].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);
						
						if(obj>bestOverallComittedUtilisation){//"bestProceduralComittedUtilisation" not required
							
							bestOverallComittedUtilisation=obj;
							
							/*for(int s=0;s<numberOfArrivalScenarios;s++){
								neighbourSol[s].sol.printSolutionString();
								bestSolContainer[s].sol.printSolutionString();
							}*/
							
							if(objectiveFunctionIndex==0){
								for(int v=0;v<vTypes;v++){
									bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
								}
							}
							
							
							//store the best yard policy this iteration in "bestSolPack"
							bestSolContainer[0].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);
							
							
							for(int s=1;s<numberOfArrivalScenarios;s++){
								bestSolContainer[s].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);//DELIBERATELY "neighbourSol["0"].YA.sol"! SEE ABOVE
							}
							
							if(assignBestSolutionsToScenarioContainers){
								/*solNumberInSubsetWithHighestIndividualUtil
								isInSubSet[i]
								bestSolNumberPerScenario[*/
								//scenSolArray[s
								for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									
									//if(isInSubSet[ss]){
										tempSolStorage[ss].copyGeneSequence(bestSolContainer[bestSolNumberPerScenario[ss]].sol);
										tempScenSolArray[ss].reuseScenarioSolution(scenSolArray[bestSolNumberPerScenario[ss]], ss);
										//tempScenSolArray[ss]=bestPackSolScenSolArray[bestSolNumberPerScenario[ss]];//bestPackSolScenSolArray
									/*}else{
										tempSolStorage[ss].copyGeneSequence(bestSolContainer[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
										tempScenSolArray[ss].reuseScenarioSolution(scenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], ss);
										//tempScenSolArray[ss]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
									}*/
								}
								if(printProgressOnConsole){
									System.out.println("line 1045 bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
								}
								
								for(int ss=0;ss<numberOfArrivalScenarios;ss++){
									//this may over write objects before they have been copied elsewhere they may be required
									bestPackSolScenSolArray[ss].reuseScenarioSolution(tempScenSolArray[ss], ss);
									bestSolContainer[ss].sol.copyGeneSequence(tempSolStorage[ss]);
									
									/*if(!visualiseBestSolutions){
										bestSolContainer[ss].setVisible(true);
									}*/
									
									bestSolContainer[ss].implementSolution(arrivalScenarioArrivalScenarioSeeds[ss], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
									
									/*if(!visualiseBestSolutions){
										bestSolContainer[ss].setVisible(false);
									}*/
									
									if(printProgressOnConsole){
										System.out.print(bestSolContainer[ss].objectiveValue+",");
									}
								}
								if(printProgressOnConsole){
									System.out.println();
									for(int ss=0;ss<numberOfArrivalScenarios;ss++){
										bestSolContainer[ss].YA.sol.printSolutionString();
										bestSolContainer[ss].sol.printSolutionString();
										System.out.println("----------------");
									}
									System.out.println();	
								}
								
							}else{
								//for checking
								System.out.println("line 1124 checking new best overall objective "+bestOverallComittedUtilisation);
								for(int s=0;s<numberOfArrivalScenarios;s++){
									bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
									for(int ss=0;ss<numberOfArrivalScenarios;ss++){
										System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
									}
									System.out.println();
								}
								
								
								//implement the solutions for display purposes and store the ScenarioSolution objectis
								//newScenarioSolution;
								//For display and setting "bestPackSolScenSolArray[s]"
								for(int s=0;s<numberOfArrivalScenarios;s++){
									//for
									bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
									bestPackSolScenSolArray[s]=scenSolArray[s];
								}
							}
							
							
							
							
							
							
							
							
							
							
							
							
							

							if(printProgressOnConsole){
								System.out.println();
							}
						}
					}
				}
			}
			
			
			//END OF BIG ITERATION TASKS
			if(yardIteration==0){
				
			}else{
				
			}
			
			if(printProgress && printProgressOnConsole){
				for(int s=0;s<numberOfArrivalScenarios;s++){
					//System.out.println(bestObjective+","+bestObjThisGeneration);
					//population[bestSolutionIndex].printSol(1, yardIteration);
					
					/*if(!visualiseBestSolutions){
						bestSolContainer[s].setVisible(true);
					}*/
					
					bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);
					System.out.println(bestObj+","+bestSolContainer[s].objectiveValue);
					bestSolContainer[s].printSol(1, yardIteration);
					
					/*if(!visualiseBestSolutions){
						bestSolContainer[s].setVisible(false);
					}*/
					
					/*if(Math.abs(bestSolContainer[s].objectiveValue-bestObj[s])>0.000001){
						System.out.println();
					}*/
				}
			}
			//
			yardIteration=1-yardIteration;//flip between yard and packing optimisation
		}
		
		///////////////////////
		
		//simple local search (Just the same as a simulated annealing algorithm but with all neighbours explored in)
				boolean performFinishingTouchLocalSearch=true;
				
				if(performFinishingTouchLocalSearch) {
					
					useTriangularDistributionGeneToMutate=false;//since there is no set iteration scheme in this finishing touch local search
					
					//boolean improvementFoundInPreviousLocalSearchIteration=true;
					int bigIterationSinceLastImprovement=0;
					while(bigIterationSinceLastImprovement<2) {
						
						bigIterationSinceLastImprovement++;
						
						// improvementFoundInPreviousLocalSearchIteration=false;
						 
						 //neighbourhoods (steepest ascent)
						 if(onlineRecourseProblem){
								yardIteration=0;//i.e. the yard policy is fixed in the online recourse problem case
							}
							if(useRuleBasedYardPolicy){
								yardIteration=0;
							}
							
							//set the solutions to the best so far at the beginning for each big iteration
							for(int s=0;s<numberOfArrivalScenarios;s++){
								
								//in the current version "bestPackSol" always contains the packing solutions associated with the best overall yard policy
								//in which case why not get rid of "bestPackSol"
								currentSol[s].sol.copyGeneSequence(bestSolContainer[s].sol);
								currentSol[s].YA.sol.copyGeneSequence(bestSolContainer[s].YA.sol);
								
									
								neighbourSol[s].sol.copyGeneSequence(currentSol[s].sol);
								neighbourSol[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);
							}
							
							
							//completely reset the objective values (yard iterations have only a single objective value
							//Within iteration objectives
							if(yardIteration==0){
								//may have changed because the yard policy may have changed
								
								//reset the packing subproblem objective values as the objective values of the minor uncertainty set packing solutions may have changes due to a change in the yard policy
								bestObj=new double[numberOfArrivalScenarios];
								currentObj=new double[numberOfArrivalScenarios];
								
								//evaluation of the initial and best within iteration solutions
								for(int s=0;s<numberOfArrivalScenarios;s++){
									currentSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
									
									ScenarioSolution newScenarioSolution=new ScenarioSolution(currentSol[s].sol, currentSol[s].YA.sol, currentSol[s].vMixes, s);//currentSol[s]
									bestPackSolScenSolArray[s]=newScenarioSolution;
									//scenSolArray[s]=newScenarioSolution;
									SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
									
								}
								
								for(int s=0;s<numberOfArrivalScenarios;s++){//The solution for scenario s
									//
									if(objectiveFunctionIndex==0){
										currentSol[s].intersectionObjVal=SSL.constrainedMaxIntersectionAreaSolutionSubSet(subsetSize, bestPackSolScenSolArray[s]);
									}else if(objectiveFunctionIndex==1){
										currentSol[s].intersectionObjVal=SSL.constrainedBestUtilisationSubSet(subsetSize, bestPackSolScenSolArray[s]);
									}else if(objectiveFunctionIndex==2){
										currentSol[s].intersectionObjVal=SSL.constrainedWorstCaseSubSet(subsetSize, bestPackSolScenSolArray[s]);
									}
									
									currentSol[s].packingObj=((1-intersectionObjectiveWeight)*currentSol[s].objValPerArrivalScenario[s])+(intersectionObjectiveWeight*currentSol[s].intersectionObjVal);
									bestObj[s]=currentSol[s].packingObj;
									//for(int ss=0;ss<numberOfArrivalScenarios;ss++){//Implemented in scenario ss
										//currentSol[s].intersectionObjValPerArrivalScenario[ss]=((1-intersectionObjectiveWeight)*currentSol[s].objValPerArrivalScenario[ss])+(intersectionObjectiveWeight*currentSol[s].intersectionObjVal);
									//}
								}
								SSL.clear();
								
								
							}else{
							}
							
							boolean improvementFoundInSubIterations=true;
							//for(int subIteration=0;subIteration<iterationsPerBigIteration[yardIteration];subIteration++){// && (!onlineRecourseProblem || (onlineRecourseProblem && claimedUtil>bestUtil))
							while(improvementFoundInSubIterations) {	
								improvementFoundInSubIterations=false;
								
								//System.out.println("subIteration number="+subIteration);
								//subiterations are now completely different for packing and yard iterations
								if(yardIteration==0){
									for(int s=0;s<numberOfArrivalScenarios;s++){
										//temperature scheme
										//double TT=(double)subIteration/iterationsPerBigIteration[yardIteration];
										//double temp=(1-TT)*t0Factor*Math.abs((totalAreaOfPotentiallyArrivingRectangles-bestObj[s]));
										
										//if(useOppositeTempScheme){
											//temp=(1-TT)*t0Factor*Math.abs(bestObj[s]);
										//}
										
										
										
										//boolean generateARandomReasonablePackingSolution=false;
										
										/*if(printProgressOnConsole){
											if(counter==22 && s==2 && subIteration==396 && iteration==20){
												System.out.println();
											}
										}*/
										
										
										//enumerate the mutation neighbourhood
										int activeLengthOfSolution=neighbourSol[s].YA.sol.activeLength;
										int alphabetLength=neighbourSol[s].YA.sol.alphabetLength;
										
										for(int e=0;e<activeLengthOfSolution;e++) {
											for(int a=0;a<alphabetLength;a++) {
												//set neigbouring solution to the current solution
												neighbourSol[s].sol.copyGeneSequence(currentSol[s].sol);
												neighbourSol[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);
												
												if(neighbourSol[s].YA.sol.mutationTypeNeighbour(e, a)) {
													//implementa solution   arrivalScenarioArrivalScenarioSeeds[s]
													neighbourSol[s].implementSolution(arrivalScenarioRandomSeeds, yardIteration, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
													
													
													ScenarioSolution newScenarioSolution=new ScenarioSolution(neighbourSol[s].sol, neighbourSol[s].YA.sol, neighbourSol[s].vMixes, s);//neighbourSol[s]
													
													scenSolArray[s]=newScenarioSolution;
													SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
													
													//Add the current best solution for the other scenarios
													for(int ss=0;ss<numberOfArrivalScenarios;ss++){
														if(s!=ss){
															SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(bestPackSolScenSolArray[ss]);//allways contains the best solution containers scenario solution objects
														}
													}
													if(objectiveFunctionIndex==0){
														neighbourSol[s].intersectionObjVal=SSL.constrainedMaxIntersectionAreaSolutionSubSet(subsetSize, scenSolArray[s]);//bestPackSolScenSolArray[s]
													}else if(objectiveFunctionIndex==1){
														neighbourSol[s].intersectionObjVal=SSL.constrainedBestUtilisationSubSet(subsetSize, scenSolArray[s]);//bestPackSolScenSolArray[s]
													}else if(objectiveFunctionIndex==2){
														neighbourSol[s].intersectionObjVal=SSL.constrainedWorstCaseSubSet(subsetSize, scenSolArray[s]);//bestPackSolScenSolArray[s]
													}
													
													neighbourSol[s].packingObj=((1-intersectionObjectiveWeight)*neighbourSol[s].objValPerArrivalScenario[s])+(intersectionObjectiveWeight*neighbourSol[s].intersectionObjVal);
													SSL.clear();//clears the list of ScenarioSolutions only (not the solution just calculated
													
													double obj=neighbourSol[s].packingObj;//for one arrival scenario evaluations the objective value is always the utilisation
													
													double invertedObj=(totalAreaOfPotentiallyArrivingRectangles-obj);
													double invertedBestSol=(totalAreaOfPotentiallyArrivingRectangles-bestObj[s]);
													double invertedCurrentSol=(totalAreaOfPotentiallyArrivingRectangles-currentObj[s]);
													
													boolean acceptNewSolution=false;
													if(useOppositeTempScheme){
														double delta=bestObj[s]-obj;//current
														
														//is this solution an improvement
														if(delta<0){
															//accept the solution
															acceptNewSolution=true;
														}/*else{
															//accept with temperature dependent probability
															if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (currentObj[s]-obj<=0)){// && useLatestChanges 
																acceptNewSolution=true;
																//timesNonImprovingMoveAccepted++;
															}
														}*/
													}else{
														double delta=invertedObj-invertedBestSol;//current
														
														//is this solution an improvement
														if(delta<0){
															//accept the solution
															acceptNewSolution=true;
														}/*else{
															//accept with temperature dependent probability
															if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (invertedObj-invertedCurrentSol<=0)){// && useLatestChanges 
																acceptNewSolution=true;
																//timesNonImprovingMoveAccepted++;
															}
														}*/
													}
													
													
													//
													if(acceptNewSolution){
														
														//this apples for the case of first ascent
														
														//for the case of steepest ascent record the move type so that if it does turn out to be the best in the whole iteration
														//implement it at the end of the iteration
														
														currentObj[s]=obj;
														//if the solution that has just been evaluated is accepted
														currentSol[s].sol.copyGeneSequence(neighbourSol[s].sol);
														currentSol[s].YA.sol.copyGeneSequence(neighbourSol[s].YA.sol);
														
														//
														if(obj>bestObj[s]){
															
															bestObj[s]=obj;
									
															//bestPackSols[s].sol.copyGeneSequence(neighbourSol[s].sol);
															//bestPackSols[s].YA.sol.copyGeneSequence(neighbourSol[s].YA.sol);
															//bestPackSols[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
														}
													}
													
													
													//It may also be the case that the overall objective has been improved by the current solution
													if(neighbourSol[s].intersectionObjVal>bestOverallComittedUtilisation){
														
														bigIterationSinceLastImprovement=0;
														
														counter++;
														if(printProgressOnConsole){
															System.out.println(counter);
														}
														
														
														bestOverallComittedUtilisation=neighbourSol[s].intersectionObjVal;
														if(objectiveFunctionIndex==0){
															for(int v=0;v<vTypes;v++){
																bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
															}
														}
														
														//store the best yard policy this iteration in "bestSolPack"
														
														//get rid of "bestPackSol", use "bestSolContainer"
														
														//have I just overwritten something this is used
														bestSolContainer[s].sol.copyGeneSequence(neighbourSol[s].sol);
														
														/*if(printProgressOnConsole){
															if(counter>-1){
																System.out.println(counter+","+s+","+subIteration+","+iteration);
																
																neighbourSol[s].sol.printSolutionString();
																//System.out.print("obj vals of new best solutions");
																bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, yardIteration, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null);//generateARandomReasonablePackingSolution
																for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																	bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[ss], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null);
																	//System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
																}
																//System.out.println();
																System.out.print("bestSolNumberPerScenario: ");
																for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																	System.out.print(bestSolNumberPerScenario[ss]+",("+isInSubSet[ss]+"),");
																}
																System.out.println();
															}
														}*/
														
														//bestPackSols[s].YA.sol.copyGeneSequence(neighbourSol[s].YA.sol);//this is not necessary because this is a packing iteration and the yard policy cannot change
														//n needs to be stores for the subsequence simulation feasibility checking (The assumption here
														//is that n is never automatically assigned, I set n=ns[0] at the end of packing iterations
														//bestSolContainer[0].n=bestPackSols[0].n;
														
														
														bestPackSolScenSolArray[s]=scenSolArray[s];
														
														if(assignBestSolutionsToScenarioContainers){//sometimes its good to forget
															/*solNumberInSubsetWithHighestIndividualUtil
															isInSubSet[i]
															bestSolNumberPerScenario[*/
															//scenSolArray[s
															for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																
																//if(isInSubSet[ss]){
																	tempSolStorage[ss].copyGeneSequence(bestSolContainer[bestSolNumberPerScenario[ss]].sol);
																	tempScenSolArray[ss].reuseScenarioSolution(bestPackSolScenSolArray[bestSolNumberPerScenario[ss]], ss);
																	//tempScenSolArray[ss]=bestPackSolScenSolArray[bestSolNumberPerScenario[ss]];//bestPackSolScenSolArray
																/*}else{
																	tempSolStorage[ss].copyGeneSequence(bestSolContainer[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
																	tempScenSolArray[ss].reuseScenarioSolution(bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], ss);
																	//tempScenSolArray[ss]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
																}*/
															}
															if(printProgressOnConsole){
																System.out.println("line 811 bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
															}
															for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																//this may over write objects before they have been copied elsewhere they may be required
																bestPackSolScenSolArray[ss].reuseScenarioSolution(tempScenSolArray[ss], ss);
																bestSolContainer[ss].sol.copyGeneSequence(tempSolStorage[ss]);
																currentSol[ss].sol.copyGeneSequence(tempSolStorage[ss]);
																
																/*if(!visualiseBestSolutions){	
																	bestSolContainer[ss].setVisible(true);
																}*/
																
					
																bestSolContainer[ss].implementSolution(arrivalScenarioArrivalScenarioSeeds[ss], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
																if(printProgressOnConsole){
																	
																	System.out.print(bestSolContainer[ss].objectiveValue+",");
																}
																
																/*if(!visualiseBestSolutions){
																	bestSolContainer[ss].setVisible(false);
																}*/
																
																
															}
															if(printProgressOnConsole){
																System.out.println("-----------");
															}
															
															/*for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																bestSolContainer[ss].sol.printSolutionString();//.YA
																neighbourSol[ss].sol.printSolutionString();//.YA
																bestSolContainer[ss].sol.printSolutionString();
																System.out.println("----------------");
															}
															System.out.println();*/
															
														}else{
															//The issue here is that we do not know in which scenario the new solution helped to improve the overall solution
															
															//FOR CHECKING PURPOSES, REEVALUATE ALL THE BEST SOLUTIONS AND PRINT OUT THE UTILISATIONS IN EACH SCENARIO
															//NO ACHIEVED UTILISATION SHOULD BE IN EXCESS OF "bestOverallComittedUtilisation"
															//if(printProgressOnConsole){
																System.out.println("line 932 checking new best overall objective "+bestOverallComittedUtilisation);
																for(int sss=0;sss<numberOfArrivalScenarios;sss++){
																	bestSolContainer[sss].implementSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
																	for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																		System.out.print(bestSolContainer[sss].objValPerArrivalScenario[ss]+",");
																	}
																	System.out.println();
																}
															//}
															
															
															//replace the SceanrioSolution object for bestPackSol[s]
															//ScenarioSolution newScenarioSolution=
															bestPackSolScenSolArray[s]=new ScenarioSolution(bestSolContainer[s].sol, bestSolContainer[s].YA.sol, bestSolContainer[s].vMixes, s);//bestSolContainer[s]newScenarioSolution;
															//scenSolArray[s]
															
															/*if(!visualiseBestSolutions){
																bestSolContainer[s].setVisible(true);
															}*/
															
															bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
															
															/*if(!visualiseBestSolutions){
																bestSolContainer[s].setVisible(false);
															}*/
														}
														
														
														
														
														
														
														if(printProgressOnConsole){
															System.out.println("bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
														}
														
													}
													//SSL.clear();
												}//else: gene value is the same as that in the current solution
												
												
											}
										}
										
										
										
										
										
									}
									
									//If a current solution was changed it may be possible that the set of current packing solution may be a better combination of packing solutions for the
									//set of arrival scenarios
									//perform a non constrreained maximum intersection area based on the current solutions (which have already been implemented)
									
									for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
										//bestSolContainer[indOrd[s]].YA.printQueues();
										//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType);
										
										currentSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
										
										ScenarioSolution newScenarioSolution=new ScenarioSolution(currentSol[s].sol, currentSol[s].YA.sol, currentSol[s].vMixes, s);//currentSol[s]
										scenSolArray[s]=newScenarioSolution;
										SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
										
									}
									
									//compute the intersection area vehicle mix and the associated utilisation
									//the overall packing objective is the max intersection area of the current set of non-dominated packing solutions for the current yard policy
									if(objectiveFunctionIndex==0){
										newScenSols=SSL.maxIntersectionAreaSolutionSubSet(subsetSize);//.maxIntersectionAreaSolution();
									}else if(objectiveFunctionIndex==1){
										newScenSols=SSL.bestUtilisationSubSet(subsetSize);//.maxIntersectionAreaSolution();
									}else if(objectiveFunctionIndex==2){
										newScenSols=SSL.worstCaseSubSet(subsetSize);//.maxIntersectionAreaSolution();
									}
									
									//has a new best intersection area been found
									
									double obj=SSL.maxIntersectionArea;
									
									//no simulated annealing here
									//Just accept if it is an overall improvement
									if(obj>bestOverallComittedUtilisation){
											
										bestOverallComittedUtilisation=obj;
										if(objectiveFunctionIndex==0){
											for(int v=0;v<vTypes;v++){
												bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
											}
										}
										
										
										
										if(assignBestSolutionsToScenarioContainers){
											/*solNumberInSubsetWithHighestIndividualUtil
											isInSubSet[i]
											bestSolNumberPerScenario[*/
											//scenSolArray[s
											for(int s=0;s<numberOfArrivalScenarios;s++){
												
												//if(isInSubSet[s]){
													tempSolStorage[s].copyGeneSequence(currentSol[bestSolNumberPerScenario[s]].sol);
													tempScenSolArray[s].reuseScenarioSolution(scenSolArray[bestSolNumberPerScenario[s]], s);
													//tempScenSolArray[ss]=bestPackSolScenSolArray[bestSolNumberPerScenario[ss]];//bestPackSolScenSolArray
												/*}else{
													tempSolStorage[s].copyGeneSequence(currentSol[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
													tempScenSolArray[s].reuseScenarioSolution(scenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], s);
													//tempScenSolArray[ss]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
												}*/
											}
											if(printProgressOnConsole){
												System.out.println("line 908 bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
											}
											for(int s=0;s<numberOfArrivalScenarios;s++){
												//this may over write objects before they have been copied elsewhere they may be required
												bestPackSolScenSolArray[s].reuseScenarioSolution(tempScenSolArray[s], s);
												bestSolContainer[s].sol.copyGeneSequence(tempSolStorage[s]);
												currentSol[s].sol.copyGeneSequence(tempSolStorage[s]);
												
											/*	if(!visualiseBestSolutions){
													bestSolContainer[s].setVisible(true);
												}*/
												
												bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
												if(printProgressOnConsole){
													System.out.print(bestSolContainer[s].objectiveValue+",");
												}
												
												/*if(!visualiseBestSolutions){
													bestSolContainer[s].setVisible(false);
												}*/
											}
											if(printProgressOnConsole){
												System.out.println();
												for(int ss=0;ss<numberOfArrivalScenarios;ss++){
													bestSolContainer[ss].YA.sol.printSolutionString();
													bestSolContainer[ss].sol.printSolutionString();
													System.out.println("----------------");
												}
												System.out.println();	
											}
											
										}else{
											//same again "bestPackSol"...
											System.out.println("line 987 checking new best overall objective "+bestOverallComittedUtilisation);
											for(int s=0;s<numberOfArrivalScenarios;s++){//
												//the packing solutions can also be accepted as best overall as well
												bestSolContainer[s].sol.copyGeneSequence(currentSol[s].sol);
												//bestPackSols[s].YA.sol.copyGeneSequence(currentSol[s].YA.sol);//DELIBERATELY "neighbourSol["0"].YA.sol"! SEE ABOVE
					
												bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
												for(int ss=0;ss<numberOfArrivalScenarios;ss++){
													System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
												}
												System.out.println();
												
												
												//System.out.print("scenario "+s+" uses packing solution "+newScenSols[s].packingSolutionNumber+", ");
												bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
												
												bestPackSolScenSolArray[s]=scenSolArray[s];
											}
										}
										
										
										
										
										
										//System.out.println();
										if(printProgressOnConsole){
											System.out.println("bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
										}
										
									}
									
									SSL.clear();
									
								}else{
									//temperature scheme
									//double TT=(double)subIteration/iterationsPerBigIteration[yardIteration];
									//double temp=(1-TT)*t0Factor*Math.abs((totalAreaOfPotentiallyArrivingRectangles-bestOverallComittedUtilisation));
									
									//if(useOppositeTempScheme){
										//temp=(1-TT)*t0Factor*Math.abs(bestOverallComittedUtilisation);
									//}
									//Generate a neighbouring yard policy (copy it into the neighbouring solution container for
									//each minor uncertainty set solution)
									//Consider each arrival scenario in the major uncertainty set (might be smaller)
									
									//full mutation neighbourhood of yard policies
									int lanes=neighbourSol[0].YA.sol.activeLength;
									int alphabetLength=neighbourSol[0].YA.sol.alphabetLength;
									
									for(int l=0;l<lanes;l++) {
										for(int a=0;a<alphabetLength;a++) {
											
											neighbourSol[0].YA.sol.copyGeneSequence(currentSol[0].YA.sol);
											if(neighbourSol[0].YA.sol.mutationTypeNeighbour(l, a)) {//is this a different value to the current value
												//neighbourSol[0].YA.sol.mutation(randNumGen);
												for(int s=1;s<numberOfArrivalScenarios;s++){
													neighbourSol[s].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);
												}
												
												for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
													//bestSolContainer[indOrd[s]].YA.printQueues();
													//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType);
													
													neighbourSol[s].implementSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
													//now that values2 are filled in, 
													
													ScenarioSolution newScenarioSolution=new ScenarioSolution(neighbourSol[s].sol, neighbourSol[s].YA.sol, neighbourSol[s].vMixes, s);//neighbourSol[s]
													scenSolArray[s]=newScenarioSolution;
													
													SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
													
												}
												
												//compute the intersection area vehicle mix and the associated utilisation
												//the overall packing objective is the max intersection area of the current set of non-dominated packing solutions for the current yard policy
												if(objectiveFunctionIndex==0){
													newScenSols=SSL.maxIntersectionAreaSolutionSubSet(subsetSize);//.maxIntersectionAreaSolution();
												}else if(objectiveFunctionIndex==1){
													newScenSols=SSL.bestUtilisationSubSet(subsetSize);//.maxIntersectionAreaSolution();
												}else if(objectiveFunctionIndex==2){
													newScenSols=SSL.worstCaseSubSet(subsetSize);//.maxIntersectionAreaSolution();
												}
												
												//has a new best intersection area been found
												
												double obj=SSL.maxIntersectionArea;
												
												double invertedObj=(totalAreaOfPotentiallyArrivingRectangles-obj);
												double invertedBestSol=(totalAreaOfPotentiallyArrivingRectangles-bestOverallComittedUtilisation);
												double invertedCurrentSol=(totalAreaOfPotentiallyArrivingRectangles-currentComittedUtilisation);
												
												//double delta=invertedObj-invertedBestSol;//current
												
												SSL.clear();
												
												boolean acceptNewSolution=false;
												
												if(useOppositeTempScheme){
													//do simulated annealing accept/reject, current and best overall solution update
													double delta=bestOverallComittedUtilisation-obj;//current
													
													//is this solution an improvement
													if(delta<0){
														//accept the solution
														acceptNewSolution=true;
													}/*else{
														//accept with temperature dependent probability
														if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (currentComittedUtilisation-obj<=0)){// && useLatestChanges 
															acceptNewSolution=true;
															//timesNonImprovingMoveAccepted++;
														}
													}*/
												}else{
													//do simulated annealing accept/reject, current and best overall solution update
													double delta=invertedObj-invertedBestSol;//bestOverallComittedUtilisation-obj;//current
													
													//is this solution an improvement
													if(delta<0){
														//accept the solution
														acceptNewSolution=true;
													}/*else{
														//accept with temperature dependent probability
														if(randNumGen.nextDouble()<Math.exp(-delta/temp) || (invertedObj-invertedCurrentSol<=0)){// && useLatestChanges currentComittedUtilisation-obj
															acceptNewSolution=true;
															//timesNonImprovingMoveAccepted++;
														}
													}*/
												}
												
												
												//
												if(acceptNewSolution){
													
													bigIterationSinceLastImprovement=0;
													
													currentComittedUtilisation=obj;
													//if the solution that has just been evaluated is accepted
													
													currentSol[0].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);
													
													if(obj>bestOverallComittedUtilisation){//"bestProceduralComittedUtilisation" not required
														
														bestOverallComittedUtilisation=obj;
														
														/*for(int s=0;s<numberOfArrivalScenarios;s++){
															neighbourSol[s].sol.printSolutionString();
															bestSolContainer[s].sol.printSolutionString();
														}*/
														if(objectiveFunctionIndex==0){
															for(int v=0;v<vTypes;v++){
																bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
															}
														}
														
														
														//store the best yard policy this iteration in "bestSolPack"
														bestSolContainer[0].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);
														
														
														for(int s=1;s<numberOfArrivalScenarios;s++){
															bestSolContainer[s].YA.sol.copyGeneSequence(neighbourSol[0].YA.sol);//DELIBERATELY "neighbourSol["0"].YA.sol"! SEE ABOVE
														}
														
														if(assignBestSolutionsToScenarioContainers){
															/*solNumberInSubsetWithHighestIndividualUtil
															isInSubSet[i]
															bestSolNumberPerScenario[*/
															//scenSolArray[s
															for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																
																//if(isInSubSet[ss]){
																	tempSolStorage[ss].copyGeneSequence(bestSolContainer[bestSolNumberPerScenario[ss]].sol);
																	tempScenSolArray[ss].reuseScenarioSolution(scenSolArray[bestSolNumberPerScenario[ss]], ss);
																	//tempScenSolArray[ss]=bestPackSolScenSolArray[bestSolNumberPerScenario[ss]];//bestPackSolScenSolArray
																/*}else{
																	tempSolStorage[ss].copyGeneSequence(bestSolContainer[SSL.solNumberInSubsetWithHighestIndividualUtil].sol);
																	tempScenSolArray[ss].reuseScenarioSolution(scenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil], ss);
																	//tempScenSolArray[ss]=bestPackSolScenSolArray[SSL.solNumberInSubsetWithHighestIndividualUtil];//
																}*/
															}
															if(printProgressOnConsole){
																System.out.println("line 1045 bestOverallComittedUtilisation: "+bestOverallComittedUtilisation);
															}
															
															for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																//this may over write objects before they have been copied elsewhere they may be required
																bestPackSolScenSolArray[ss].reuseScenarioSolution(tempScenSolArray[ss], ss);
																bestSolContainer[ss].sol.copyGeneSequence(tempSolStorage[ss]);
																
																/*if(!visualiseBestSolutions){
																	bestSolContainer[ss].setVisible(true);
																}*/
																
																bestSolContainer[ss].implementSolution(arrivalScenarioArrivalScenarioSeeds[ss], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
																
																/*if(!visualiseBestSolutions){
																	bestSolContainer[ss].setVisible(false);
																}*/
																
																if(printProgressOnConsole){
																	System.out.print(bestSolContainer[ss].objectiveValue+",");
																}
															}
															if(printProgressOnConsole){
																System.out.println();
																for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																	bestSolContainer[ss].YA.sol.printSolutionString();
																	bestSolContainer[ss].sol.printSolutionString();
																	System.out.println("----------------");
																}
																System.out.println();	
															}
															
														}else{
															//for checking
															System.out.println("line 1124 checking new best overall objective "+bestOverallComittedUtilisation);
															for(int s=0;s<numberOfArrivalScenarios;s++){
																bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
																for(int ss=0;ss<numberOfArrivalScenarios;ss++){
																	System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
																}
																System.out.println();
															}
															
															
															//implement the solutions for display purposes and store the ScenarioSolution objectis
															//newScenarioSolution;
															//For display and setting "bestPackSolScenSolArray[s]"
															for(int s=0;s<numberOfArrivalScenarios;s++){
																//for
																bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
																bestPackSolScenSolArray[s]=scenSolArray[s];
															}
														}
														
														
														
														
														
														
														
														
														
														
														
														
														
					
														if(printProgressOnConsole){
															System.out.println();
														}
													}
												}
											}
											
										}
									}
									
									
								}
							}
							
							
							//END OF BIG ITERATION TASKS
							if(yardIteration==0){
								
							}else{
								
							}
							
							if(printProgress && printProgressOnConsole){
								for(int s=0;s<numberOfArrivalScenarios;s++){
									//System.out.println(bestObjective+","+bestObjThisGeneration);
									//population[bestSolutionIndex].printSol(1, yardIteration);
									
									/*if(!visualiseBestSolutions){
										bestSolContainer[s].setVisible(true);
									}*/
									
									bestSolContainer[s].implementSolution(arrivalScenarioArrivalScenarioSeeds[s], 1, breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType, null, false, randNumGen);
									System.out.println(bestObj+","+bestSolContainer[s].objectiveValue);
									bestSolContainer[s].printSol(1, yardIteration);
									
									/*if(!visualiseBestSolutions){
										bestSolContainer[s].setVisible(false);
									}*/
									
									/*if(Math.abs(bestSolContainer[s].objectiveValue-bestObj[s])>0.000001){
										System.out.println();
									}*/
								}
							}
							//
							yardIteration=1-yardIteration;//flip between yard and packing optimisation
						 
						 
					}
				}
		
		////////////////////
		
		if(objectiveFunctionIndex==1 && commitIntersectionVMix){
			
			//in this case the vehicle intersection aspect is not used
			//but optionally calculate the maximum area intersection vehicle mix now
			//(this is the problem (deciding which vehicles to commit to loading---all or which exact subset) which lead to the "objectiveFunctionIndex==0" case/algorithm, this is doubling back to check that it was worth the effort
			for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
				//bestSolContainer[indOrd[s]].YA.printQueues();
				//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType);
				
				bestSolContainer[s].implementSolution(arrivalScenarioRandomSeeds, 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, false, randNumGen);//-bestObj[s]+0.000000000000001
				//now that values2 are filled in, 
				//bestSolContainer[s].implementSolutionQueueConstrainedOptimallyFillBins(arrivalScenarioRandomSeeds[1], 1, useRuleBasedYardPolicy, breakTiesWithLeastFullLane, moduloScenario, yardPolicyType, null, bestSolContainer[s].totalArea, false, randNumGen, true);
				if(printProgressOnConsole){
					for(int ss=0;ss<numberOfArrivalScenarios;ss++){
						System.out.print(bestSolContainer[s].objValPerArrivalScenario[ss]+",");
					}
					System.out.println();
				}
				
				ScenarioSolution newScenarioSolution=new ScenarioSolution(bestSolContainer[s].sol, bestSolContainer[s].YA.sol, bestSolContainer[s].vMixes, s);
				bestPackSolScenSolArray[s]=newScenarioSolution;
				SSL.addScenarioSolutionToNonDominatedSolutionsLinkList(newScenarioSolution);
				
			}
			
			
			newScenSols=SSL.maxIntersectionAreaSolutionSubSet(subsetSize);//.maxIntersectionAreaSolution();

			for(int v=0;v<vTypes;v++){
				bestCommittedVMix[v]=SSL.maxIntersectionVMix[v];
			}
			
			bestOverallComittedUtilisation=SSL.maxIntersectionArea;
		}
		
		//store intersection area regrdless of the commit policy
		double copyOfBestOverallComittedUtilisation=bestOverallComittedUtilisation;
		
		if(!commitIntersectionVMix){
			bestOverallComittedUtilisation=YA.totalAreaOfRectangles;
		}
		
		System.out.print(bestOverallComittedUtilisation);
		resultsOutput.print(bestOverallComittedUtilisation);
		//store the best intersection vehicle mix by attaching it to specially created reference in
		//bestSolContainer[0]
		bestSolContainer[0].bestCommitedUtilisation=bestOverallComittedUtilisation;
		bestSolContainer[0].bestCommittedVMixReference=bestCommittedVMix;
		
		//similarly store the expected revenue objective (average area of vehicles packed)
		double expectedRevenue=0;
		for(int s=0;s<numberOfArrivalScenarios;s++){// && !maximumUtilEstablished
			//bestSolContainer[indOrd[s]].YA.printQueues();
			//bestSolContainer[s].checkFeasibilitySolution(arrivalScenarioRandomSeeds[1][u], breakTiesWithLeastFullLane, useRuleBasedYardPolicy, moduloScenario, yardPolicyType);
			
			expectedRevenue+=bestSolContainer[s].packedVehicleArea;
		}
		
		expectedRevenue/=numberOfArrivalScenarios;
		
		resultsOutput.print(copyOfBestOverallComittedUtilisation+" "+expectedRevenue+" ");
		System.out.print(copyOfBestOverallComittedUtilisation+" "+expectedRevenue+" ");
		
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
