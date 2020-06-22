package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class ZYardPolicyTestGP2 {
	
	public static void main(String expNameString, String[] ferryShapes, String[] ExperimentNames, boolean[] yardPolicyChoices, int[] numbersOfLanes, int[][] classInstanceNumbers, int[] numberOfArrivalScenarios, int[] objectiveFunctions, boolean[] breakTiesLestFulls, boolean[] moduloScenarios, int[] yardPolicyTypes, int[][] comittedVMixQuantileIndices, double[] intersectionObjectiveWeights, int[][] iterationsSubIterations, double[] T0s, double[][] cumuNighbourDistributions, boolean visualiseBestSolutions, int[] initialArrivalScenarioSeeds, boolean[] useTriangularDistributionGeneToMutate, boolean useRecommendedParameters, double[][][] recommendedParams, boolean commitIntersectionVMix, int[] levelOfVehicleIntersectionMixRelations) throws Exception {
		//int ExpNumber=1;
		String resultsDirectory="results/";
		//if(useRuleBasedYardPolicy){
			//resultsDirectory="resultsRB/";
		//}
		
		String outputFileName=resultsDirectory.concat("resultsOutput".concat(expNameString).concat("GP"));
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		for(int i=0;i<ferryShapes.length;i++){
			outputFileName=outputFileName.concat(ferryShapes[i]);
		}
		for(int i=0;i<ExperimentNames.length;i++){
			outputFileName=outputFileName.concat(ExperimentNames[i]);
		}
		outputFileName=outputFileName.concat(".txt");
		PrintWriter resultsOutput=new PrintWriter(new FileOutputStream(outputFileName));
		//																																												averageRevenue+" "+commitedVArea+" "+averagePackedVArea+" "+commitedUtilisation+" "+(averagePackedVArea/totalRecta
		//experimentNameNumbers[z]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber[m]+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+moduloScenarios[o]+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[q]+" "+intersectionObjectiveWeights[r]+" "+averageRevenue+" "+commitedVArea+" "+averagePackedVArea+" "+commitedUtilisation+" "+min+" "+q1+" "+q2+" "+q3+" "+max+" "+solutionTime+" "+rateOfRecourseRequirement+" "+rateOfSuccessfulRecourse+" "+averageNumberOfRecourseAttempts+" "+rateOfInitialSolutionFeasibility+" "+rateThatSGCKSIsFeasibleWithoutSearch+
		
		System.out.println("0expNameNumber 1ferry1 2nLanes 3class 4classInst 5obj 6break_ties 7expNum 8nArrivalScenarios arrivalScenSeed triangularDistMutation 9moduloScenarios 10YardPolicyType 11subsetSize 12packObjWeight bigIterations smallIterationsPack smallIterationsYard T0 neighbPar0 neighbPar1 neighbPar2 neighbPar3 neighbPar4 commitIntersectionVMix levelOfVMIRelaxation useFCFSYardPolicy initialAreaOfVehicles 13averageRevenue 14commitedVArea 15averagePackedVArea 16commitedUtilisation 17averageUtilisation 18min 19q1 20q2 21q3 22max 23solTime 24recourseRate 25successfulRecoveryRate 26aveRecourseAttempts 27rateInitialSolFeas 28rateInitialSGCKSFeas 29successfulRecoveryRate 30penaltyBeforeRecourse 31prevExpTime");//, boolean commitIntersectionVMix
		System.out.print("resultsData=[");
		
		resultsOutput.println("0expNameNumber 1ferry1 2nLanes 3class 4classInst 5obj 6break_ties 7expNum 8nArrivalScenarios arrivalScenSeed triangularDistMutation 9moduloScenarios 10YardPolicyType 11subsetSize 12packObjWeight bigIterations smallIterationsPack smallIterationsYard T0 neighbPar0 neighbPar1 neighbPar2 neighbPar3 neighbPar4 commitIntersectionVMix levelOfVMIRelaxation useFCFSYardPolicy initialAreaOfVehicles 13averageRevenue 14commitedVArea 15averagePackedVArea 16commitedUtilisation 17averageUtilisation 18min 19q1 20q2 21q3 22max 23solTime 24recourseRate 25successfulRecoveryRate 26aveRecourseAttempts 27rateInitialSolFeas 28rateInitialSGCKSFeas 29successfulRecoveryRate 30penaltyBeforeRecourse 31prevExpTime");
		resultsOutput.print("resultsData=[");
		resultsOutput.close();
		
		//String[] ExperimentNames={"SOCKS_Iterative_SA","SOCKS_Scenario_GA"};
		//String[] ferryShapes={"Rectangle","RF"};
		//boolean useRuleBasedYardPolicy=false;
		
		/*int[] numbersOfLanes={5,20};
		int[][] classInstanceNumbers={{1,5},{3,25}};
		int[] numberOfArrivalScenarios={1,5};
		int[] objectiveFunctions={0,1,2};
		boolean[] breakTiesLestFulls={true, false};*/
		
		/*int[] numbersOfLanes={5};
		int[][] classInstanceNumbers={{1,5}};
		int[] numberOfArrivalScenarios={1};
		int[] objectiveFunctions={0};
		boolean[] breakTiesLestFulls={true};*/
	
		int[] experimentNameNumbers={0,1};
		int[] ferryShapeNumber={0,1};
		//int[] breakTiesLestFullsNumber={0,1};
		
		for(int z=0;z<ExperimentNames.length;z++){//sa, ga
			for(int i=0;i<ferryShapes.length;i++){
				//CHANGE BACK
				int currentExperimentNumber=numberOfArrivalScenarios[0];//0;
				for(int j=0;j<numbersOfLanes.length;j++){
					for(int k=0;k<classInstanceNumbers.length;k++){
						for(int l=0;l<objectiveFunctions.length;l++){
							for(int m=0;m<breakTiesLestFulls.length;m++){
								for(int n=0;n<numberOfArrivalScenarios.length;n++){
									for(int nn=0;nn<initialArrivalScenarioSeeds.length;nn++){
										for(int o=0;o<moduloScenarios.length;o++){
											for(int p=0;p<yardPolicyTypes.length;p++){
												for(int q=0;q<comittedVMixQuantileIndices[n].length;q++){
													for(int r=0;r<intersectionObjectiveWeights.length;r++){
														for(int s=0;s<iterationsSubIterations.length;s++){
															for(int t=0;t<T0s.length;t++){
																for(int u=0;u<cumuNighbourDistributions.length;u++){
																	for(int v=0;v<useTriangularDistributionGeneToMutate.length;v++){
																		for(int w=0;w<levelOfVehicleIntersectionMixRelations.length;w++){
																			for(int x=0;x<yardPolicyChoices.length;x++){
																				//use static field in VMC for minimal code change
																				VMixIntersectionCalculator.levelOfRelaxation=levelOfVehicleIntersectionMixRelations[w];
																				int t1=(int)System.currentTimeMillis();
																				//System.out.println("ferry: "+ferryShapes[i]+", nLanes: "+numbersOfLanes[j]+", classInst: "+classInstanceNumbers[k][0]+","+classInstanceNumbers[k][1]+", obj: "+objectiveFunctions[l]+", least full break ties: "+breakTiesLestFulls[m]+", expNum: "+currentExperimentNumber+", nArrivalScenarios: "+numberOfArrivalScenarios[n]+",");
																				//System.out.print(ferryShapeNumber[i]+", "+numbersOfLanes[j]+", "+classInstanceNumbers[k][0]+", "+objectiveFunctions[l]+", "+breakTiesLestFullsNumber[m]+", "+currentExperimentNumber+", "+numberOfArrivalScenarios[n]+", ");
																				
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
																				resultsOutput=new PrintWriter(new FileOutputStream(outputFileName, true));
																				System.out.print(experimentNameNumbers[z]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+classInstanceNumbers[k][1]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+initialArrivalScenarioSeeds[nn]+" "+triangDistMutation+" "+moduloScenariosNumber+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[n][q]+" "+intersectionObjectiveWeights[r]+" "+iterationsSubIterations[s][0]+" "+iterationsSubIterations[s][1]+" "+iterationsSubIterations[s][2]+" "+T0s[t]+" "+cumuNighbourDistributions[u][0]+" "+cumuNighbourDistributions[u][1]+" "+cumuNighbourDistributions[u][2]+" "+cumuNighbourDistributions[u][3]+" "+cumuNighbourDistributions[u][4]+" "+commitIntersectionVMixIndicator+" "+levelOfVehicleIntersectionMixRelations[w]+" "+yardPolicyChoices[x]+" ");
																				resultsOutput.print(experimentNameNumbers[z]+" "+ferryShapeNumber[i]+" "+numbersOfLanes[j]+" "+classInstanceNumbers[k][0]+" "+classInstanceNumbers[k][1]+" "+objectiveFunctions[l]+" "+breakTiesLestFullsNumber+" "+currentExperimentNumber+" "+numberOfArrivalScenarios[n]+" "+initialArrivalScenarioSeeds[nn]+" "+triangDistMutation+" "+moduloScenariosNumber+" "+yardPolicyTypes[p]+" "+comittedVMixQuantileIndices[n][q]+" "+intersectionObjectiveWeights[r]+" "+iterationsSubIterations[s][0]+" "+iterationsSubIterations[s][1]+" "+iterationsSubIterations[s][2]+" "+T0s[t]+" "+cumuNighbourDistributions[u][0]+" "+cumuNighbourDistributions[u][1]+" "+cumuNighbourDistributions[u][2]+" "+cumuNighbourDistributions[u][3]+" "+cumuNighbourDistributions[u][4]+" "+commitIntersectionVMixIndicator+" "+levelOfVehicleIntersectionMixRelations[w]+" "+yardPolicyChoices[x]+" ");
																				/*if(currentExperimentNumber>89) {
																					callMain(expNameString, ferryShapes[i], numbersOfLanes[j], classInstanceNumbers[k], objectiveFunctions[l], breakTiesLestFulls[m], currentExperimentNumber, numberOfArrivalScenarios[n], ExperimentNames[z], resultsOutput, useRuleBasedYardPolicy, moduloScenarios[o], yardPolicyTypes[p], comittedVMixQuantileIndices[n][q], intersectionObjectiveWeights[r], iterationsSubIterations[s], T0s[t], cumuNighbourDistributions[u], visualiseBestSolutions, initialArrivalScenarioSeeds[nn], useTriangularDistributionGeneToMutate[v]);
																				}*/
																				callMain(expNameString, ferryShapes[i], numbersOfLanes[j], classInstanceNumbers[k], objectiveFunctions[l], breakTiesLestFulls[m], currentExperimentNumber, numberOfArrivalScenarios[n], ExperimentNames[z], resultsOutput, yardPolicyChoices[x], moduloScenarios[o], yardPolicyTypes[p], comittedVMixQuantileIndices[n][q], intersectionObjectiveWeights[r], iterationsSubIterations[s], T0s[t], cumuNighbourDistributions[u], visualiseBestSolutions, initialArrivalScenarioSeeds[nn], useTriangularDistributionGeneToMutate[v]);
																				currentExperimentNumber++;
																				System.out.println(((int)System.currentTimeMillis()-t1)+";");
																				resultsOutput.println(((int)System.currentTimeMillis()-t1)+";");
																				//System.out.println(currentExperimentNumber+" completed out of 96");
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
		
		System.out.print("];");
		//resultsOutput.print("];");
		//resultsOutput.close();
	}
	
	static void callMain(String expNameString, String ferryShape, int numberOfYardLanes, int[] classAndInstance, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, int ExperimentNumber, int numberOfArrivalScenarios, String ExperimentName, PrintWriter resultsOutput, boolean useRuleBasedYardPolicy, boolean moduloScenario, int yardPolicyType, int comitVMixPercentileIndex, double intersectionObjectiveWeight, int[] iterationsSubIterations, double T0, double[] cumuNighbourDistributions, boolean visualiseBestSolutions, int initialArrivalScenarioSeeds, boolean useTriangularDistributionGeneToMutate) throws Exception{
			// TODO Auto-generated method stub
		
		int testInstanceClass=classAndInstance[0];//testInstanceClassSet[ii];
		int instanceNumber=classAndInstance[1];//;//stroke instance number
		int numWidths=-1;
		int numLengths=-1;
		
		String solutionsDirectory="solutionsAndResults/";
		if(useRuleBasedYardPolicy){
			solutionsDirectory="solutionsAndResultsRB/";
		}
		
		//experiment name and number range
		//String ExperimentName="SOCKS_Iterative_SA";
		//int ExperimentNumber=0;
		String expNamePrefixPrefix=ExperimentName.concat(String.valueOf(ExperimentNumber));//is not a sensible beginning for a file name

				
		double onePixel=0.103076923;
		//String ferryShape="Rectangle";//"RF";//
		
		expNamePrefixPrefix=expNameString.concat(expNamePrefixPrefix).concat(ferryShape);
		

		String expNamePrefix=expNamePrefixPrefix.concat("Class").concat(String.valueOf(testInstanceClass)).concat("Instance").concat(String.valueOf(instanceNumber));
		
		String fileName="testInstances/TestInstanceClass".concat(String.valueOf(testInstanceClass)).concat("Distribution").concat(String.valueOf(instanceNumber)).concat("VehicleTypes.txt");
		
		BufferedReader testInstanceReader=new BufferedReader(new FileReader(fileName));
		//first line manually
		String[] lineA=testInstanceReader.readLine().split(",");//lengths of vehicles
		int numberOfVehicleTypes=lineA.length;
		double[][] rectangleDistribution=new double[4][numberOfVehicleTypes];
		for(int j=0;j<numberOfVehicleTypes;j++){
			rectangleDistribution[0][j]=Double.parseDouble(lineA[j]);
		}
		//the remaining lines
		for(int i=1;i<4;i++){
			lineA=testInstanceReader.readLine().split(",");//lengths of vehicles
			for(int j=0;j<numberOfVehicleTypes;j++){
				rectangleDistribution[i][j]=Double.parseDouble(lineA[j]);
			}
		}
		
		if(testInstanceClass==3 || testInstanceClass==7){
			lineA=testInstanceReader.readLine().split(",");
			numWidths=Integer.parseInt(lineA[0]);
			numLengths=Integer.parseInt(lineA[0]);
		}
		
		testInstanceReader.close();
		boolean printProgress=false;//true;//
		boolean resetBinNumbers=true;
		
		boolean recourseSolverOn=true;
		
		int numberOfVehicleSelectionQuantiles=numberOfVehicleTypes;//this parameter is worth experimenting with
		int numberOfOrientations=3;//no top row. This simplifies the shuffling possibility and means that the packing solution serves as the loading procedure
		int alphabetLength=numberOfVehicleSelectionQuantiles*numberOfOrientations;
		
		int numberOfYardQuantiles=numberOfVehicleTypes;//this parameter is worth experimenting with
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
		
		
		//for(int ii=0;ii<testInstanceClassSet.length;ii++){
		//for(int jj=0;jj<instancesPerInstanceClass[ii].length;jj++){
			
			//for(int jj=instanceRangesPerInstanceClass[testInstanceClassSet[ii]-1][0];jj<=instanceRangesPerInstanceClass[testInstanceClassSet[ii]-1][1];jj++){
				
				
				//read in the details that define the problem tackled by the experiment name-number combination
				//-ferry shape, the rectangle distribution, the random seeds for vehicle and arrival scenario(s),
				//-number of yard lanes and the dimensions of each lane
				//-read in the yard policy and the suggested initial packing solution for the recourse problem for any given arrival scenario
				BufferedReader problemInstanceParametersR=new BufferedReader(new FileReader(solutionsDirectory.concat("ProblemParameters").concat(expNamePrefix).concat(".txt")));
				
				//randomArrivalScenarioSeeds,vehicleScenarioSeed
				lineA=problemInstanceParametersR.readLine().split(",");
				int[] randomArrivalScenarioSeeds=new int[lineA.length];
				for(int i=0;i<randomArrivalScenarioSeeds.length;i++){
					randomArrivalScenarioSeeds[i]=Integer.parseInt(lineA[i]);//problemInstanceParametersW.print(randomArrivalScenarioSeeds[i]+",");
				}
				//vehicle scenario random seed
				int vehicleScenarioSeed=Integer.parseInt(problemInstanceParametersR.readLine());//problemInstanceParametersW.println(vehicleScenarioSeed);
				//
				//read the commited vehicle mix
				lineA=problemInstanceParametersR.readLine().split(",");
				int[] commitedVehicleMix=new int[numberOfVehicleTypes];
				for(int v=0;v<numberOfVehicleTypes;v++){
					commitedVehicleMix[v]=Integer.parseInt(lineA[v]);
				}
				//the original vehicle mix
				lineA=problemInstanceParametersR.readLine().split(",");//for computing the peanalty
				int[] originalVMix=new int[numberOfVehicleTypes];
				for(int v=0;v<numberOfVehicleTypes;v++){
					originalVMix[v]=Integer.parseInt(lineA[v]);
				}
				//
				problemInstanceParametersR.close();
				
				
				//initial area of vehicles
				double initialAreaOfVehicles=0;
				for(int v=0;v<numberOfVehicleTypes;v++){
					initialAreaOfVehicles+=(originalVMix[v]*rectangleDistribution[0][v]*rectangleDistribution[1][v]);
				}
				
				//write the yard information and policy to a different file (not sure why yet but maybe for cross testing of some variety
				BufferedReader YardPolicyInformationAndPolicyR=new BufferedReader(new FileReader(solutionsDirectory.concat("YardPolicy").concat(expNamePrefix).concat(".txt")));
				lineA=YardPolicyInformationAndPolicyR.readLine().split(",");//YardPolicyInformationAndPolicyW.println(numberOfYardLanes+","+yardLaneMaxWidth+","+yardLaneMaxLength);
				
				
				double yardLaneMaxWidth=3.5/onePixel;
				
				//((10/onePixel)*(70/onePixel))
				
				double yardLaneMaxLength=(2.5*totalRectangleArea)/(numberOfYardLanes*yardLaneMaxWidth);//70/onePixel;/onePixel
				
				
				
				//alphabet and encoding information, form of yard policy
				lineA=YardPolicyInformationAndPolicyR.readLine().split(",");//YardPolicyInformationAndPolicyW.println(YardSolution.alphabetLength+","+YardSolution.maxSolutionLength+","+Yard.numberOfYardOrientations+","+Yard.numberOfYardQuantiles+","+Yard.setOfQuantiles+","+breakTiesWithLeastFullLane);
				YardSolution.alphabetLength=Integer.parseInt(lineA[0]);
				YardSolution.maxSolutionLength=Integer.parseInt(lineA[1]);
				Yard.numberOfYardOrientations=Integer.parseInt(lineA[2]);
				Yard.numberOfYardQuantiles=Integer.parseInt(lineA[3]);
				breakTiesWithLeastFullLane=false;
				if(lineA[4].equals("true")){
					breakTiesWithLeastFullLane=true;
				}
				lineA=YardPolicyInformationAndPolicyR.readLine().split(",");//
				Yard.setOfQuantiles=new double[lineA.length];
				for(int i=0;i<lineA.length;i++){
					Yard.setOfQuantiles[i]=Double.parseDouble(lineA[i]);
				}
				////breakTiesWithLeastFullLane
				String yardPolicyString=YardPolicyInformationAndPolicyR.readLine();//YardPolicyInformationAndPolicyW.println(bestSolContainer.YA.sol.getSolutionString());//store the single best packing solution in this container as well
				YardPolicyInformationAndPolicyR.close();
				
				BufferedReader packingSolutionR=new BufferedReader(new FileReader(solutionsDirectory.concat("PackingSolution").concat(expNamePrefix).concat(".txt")));
				//alphabet information
				//packingSolutionW.println(Solution.alphabetLength+","+Solution.maxSolutionLength+","+Solution.numberOfQuantiles+","+Solution.numberOfStripTypes+","+GeneralContainer.numberOfOrientations+","+GeneralContainer.numberOfVehicleSelectionQuantiles+","+objectiveFunctionIndex+","+algorithmName);
				lineA=packingSolutionR.readLine().split(",");
				Solution.alphabetLength=Integer.parseInt(lineA[0]);
				Solution.maxSolutionLength=Integer.parseInt(lineA[1]);
				Solution.numberOfQuantiles=Integer.parseInt(lineA[2]);
				Solution.numberOfStripTypes=Integer.parseInt(lineA[3]);
				GeneralContainer.numberOfOrientations=Integer.parseInt(lineA[4]);
				GeneralContainer.numberOfVehicleSelectionQuantiles=Integer.parseInt(lineA[5]);
				objectiveFunctionIndex=Integer.parseInt(lineA[6]);//objective function index for the recourse model (the recourse algorithm should match that used to derive this solution (objectify the loading algorithms for ease of coding)
				String algorithmName=lineA[7];
				int solutionTime=Integer.parseInt(lineA[8]);
				//
				/*GeneralContainer.setOfQuantiles=new double[GeneralContainer.numberOfVehicleSelectionQuantiles];
				lineA=packingSolutionR.readLine().split(",");
				for(int i=0;i<GeneralContainer.numberOfVehicleSelectionQuantiles;i++){
					GeneralContainer.setOfQuantiles[i]=Double.parseDouble(lineA[i]);
				}*/
				//objective and utilisation
				lineA=packingSolutionR.readLine().split(",");
				double commitedVArea=Double.parseDouble(lineA[0]);
				double commitedUtilisation=Double.parseDouble(lineA[1]);
				int numberOfFirstChoiceRecourseActions=Integer.parseInt(packingSolutionR.readLine());
				//double algUtil=Double.parseDouble(lineA[1]);
				//(initial) packing solution string
				String[] packingSolutionString=new String[numberOfFirstChoiceRecourseActions];
				String[] packingSolutionString2=new String[numberOfFirstChoiceRecourseActions];
				for(int s=0;s<numberOfFirstChoiceRecourseActions;s++){
					packingSolutionString[s]=packingSolutionR.readLine();//packingSolutionW.println(bestSolContainer.sol.getSolutionString());//store the single best packing solution in this container as well
				}
				for(int s=0;s<numberOfFirstChoiceRecourseActions;s++){
					packingSolutionString2[s]=packingSolutionR.readLine();//packingSolutionW.println(bestSolContainer.sol.getSolutionString());//store the single best packing solution in this container as well
				}
				//
				packingSolutionR.close();
				//output: utilisation (mean, min, max mode), proportion of scenarios where utilisaation was less than the maximax associated with the derivation of the given solution (measures robustness
				
				
				//read in the characteristic features of the planned packing solution (int[][] n)
				/*BufferedReader vehiclesOfEachTypePerCutR=new BufferedReader(new FileReader(solutionsDirectory.concat("vehiclesOfEachTypePerCut").concat(expNamePrefix).concat(".txt")));
				ArrayList<String> linesA=new ArrayList<String>(10);
				String line="";
				vehiclesOfEachTypePerCutR.readLine();//number of solutions from first stage
				while((line=vehiclesOfEachTypePerCutR.readLine())!=null){
					linesA.add(line);
				}
				vehiclesOfEachTypePerCutR.close();
				
				int[][][] n=new int[numberOfFirstChoiceRecourseActions][1][1];
				
				for(int s=0;s<numberOfFirstChoiceRecourseActions;s++){
					
					String[] cuts=linesA.get(s).split(";");
					
					n[s]=new int[cuts.length][numberOfVehicleTypes];
					
					for(int i=0;i<cuts.length;i++){
						String[] lineB=cuts[i].split(",");
						//n[i]=new int[lineB.length];
						for(int j=0;j<lineB.length;j++){
							n[s][i][j]=Integer.parseInt(lineB[j]);
						}
					}
					
				}*/
				
				
				//from the above generate a vehicle counts vector
				//then, create a Yard constructor that takes it as the input
				/*int[] vCounts=new int[rectangleDistribution[0].length];
				for(int i=0;i<n.length;i++){
					for(int j=0;j<n[i].length;j++){
						vCounts[j]+=n[i][j];
					}
				}*/
				
				
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
				
				BasicPalette BP=new BasicPalette(Math.max(2, rectangleDistribution[0].length));
				
				//solution correction, if next gene is not feasible but a different one is use the feasible one
				//random number generator
				Random randNumGen=new Random(11);//12
				
				boolean nonConstrainedLoading=false;//true;//
				
				Yard YA=new Yard(numberOfYardLanes, yardLaneMaxWidth, yardLaneMaxLength, rectangleDistribution, totalRectangleArea, BP, onePixel, new Random(vehicleScenarioSeed), false, nonConstrainedLoading, false, commitedVehicleMix);//vCounts
				
				
				//Yard YA, Random randNumGen, boolean visualise, String ferryShape, int containerNumber
				//create a container for storing the best solution overall
				GeneralContainer recourseContainer=new GeneralContainer(YA, randNumGen, visualiseBestSolutions, ferryShape, -1, totalRectangleArea, 1, minWidth, minLength);
				recourseContainer.YA.resetBinNumbers=true;
				
				Solution[] initialPackingSolutions=new Solution[numberOfFirstChoiceRecourseActions];//(packingSolutionString);
				for(int s=0;s<numberOfFirstChoiceRecourseActions;s++){
					initialPackingSolutions[s]=new Solution(packingSolutionString[s]);
				}
				//
				YardSolution fixedYardPolicy=new YardSolution(yardPolicyString, false);
				recourseContainer.sol.copyGeneSequence(initialPackingSolutions[0]);
				recourseContainer.YA.sol.copyGeneSequence(fixedYardPolicy);
				
				//recourse algorithm object
				ObjectifiedRecourseAlgorithmGeneralPacking ORA=null;//new 
				//if(algorithmName.equals("Iterative_SA_GeneralPacking")){
					ORA=new ObjectifiedISAGeneralPacking();
				//}else{
					//ORA=new ObjectifiedIGAGeneralPacking();
				//}
				
				//collect statistics
				//average utilisation
				//average area of vehicles left off
				//rate at which the utilisation is lower than that associated with the average (and maximax) objective value from the algorithm used to derive the solution
				double averagePackedVArea=0;
				double averageRevenue=0;
				
				aNumberList sortedUtils=new aNumberList(0);
				aNumberList nonSortedUtils=new aNumberList(1);
				
				double successfulPackingRate=0;
				
				//IP formulations
				double rateOfInitialSolutionFeasibility=0;
				
				//count this inclusively with the above as to be comparable with GP equivalent experiments
				double rateThatSGCKSIsFeasibleWithoutSearch=0;
				//double frequencySGCKSImplemtedWithoutSearch=0;
				
				//double rateThatPackingIsResolved=0;
				//double frequencyThatPackingIsResolved=0;
				
				double rateOfRecourseRequirement=0;
				double rateOfSuccessfulRecourse=0;//(measures recoverability)
				
				double averageNumberOfRecourseAttempts=0;
				
				double penaltyBeforeRecourse=0;
				
				//generate a larger (so that's what it meant) set of arrival scenarios (integer random seeds)
				//recourseContainer.YA.sol.printSolutionString();
				int simulationRuns=1000;
				for(int i=0;i<simulationRuns;i++){
					int arrivalScenarioSeed=i+1000;//(int)(10000000*randNumGen.nextDouble());
					int[] arrivalSeedSet={arrivalScenarioSeed};//to make use of the multiple arrival scenario framework for considering the online recourse problem
					
					//
					//recourseContainer.feasibilityEstablishedByIP=false;
					recourseContainer.YA.sol.copyGeneSequence(fixedYardPolicy);
					
					int packSolutionNumber=0;
					boolean recourseSolved=false;
					//while(!recourseSolved && packSolutionNumber<numberOfFirstChoiceRecourseActions){
						//set the initial solutions to those derived from the algorithm
					
						double bestBeforeRecourseUtilisation=0;
						
						
						packSolutionNumber=ORA.solvePreRecourseProblem(arrivalSeedSet, YA, randNumGen, ferryShape, totalRectangleArea, objectiveFunctionIndex, breakTiesWithLeastFullLane, recourseContainer, printProgress, true, true, minWidth, minLength, recourseSolverOn, useRuleBasedYardPolicy, true, null, yardPolicyType, commitedVehicleMix, T0, cumuNighbourDistributions, useTriangularDistributionGeneToMutate, initialPackingSolutions);
						penaltyBeforeRecourse+=recourseContainer.utilisationBeforeRecourse;
						
						
						//try each packing solution from the first stage
						//for(int j=0;j<initialPackingSolutions.length;j++) {
						recourseContainer.sol.copyGeneSequence(initialPackingSolutions[packSolutionNumber]);
						
						
						
						
						//why does this need algUtil
						//this method treats "recourseContainer" as the best solution found so far
						//int[] arrivalScenarioRandomSeeds, Yard YA, Random randNumGen, String ferryShape, double totalRectangleArea, int objectiveFunctionIndex, boolean breakTiesWithLeastFullLane, GeneralContainer bestSolContainer, boolean printProgress, boolean bestSolContainerContainsInitialSolution, boolean onlineRecourseProblem, double minWidth, double minLength, double claimedUtil, boolean recourseSolverOn, boolean useRuleBasedYardPolicy, boolean checkFeasibilityIPFirst, int[][] n, int yardPolicyType, int[] commitedVMix
						recourseContainer=ORA.solveRecourseProblem(arrivalSeedSet, YA, randNumGen, ferryShape, totalRectangleArea, objectiveFunctionIndex, breakTiesWithLeastFullLane, recourseContainer, printProgress, true, true, minWidth, minLength, recourseSolverOn, useRuleBasedYardPolicy, true, null, yardPolicyType, commitedVehicleMix, T0, cumuNighbourDistributions, useTriangularDistributionGeneToMutate);
						if(recourseContainer.utilisationBeforeRecourse>bestBeforeRecourseUtilisation) {
							bestBeforeRecourseUtilisation=recourseContainer.utilisationBeforeRecourse;
						}
						//
						//}
					
						
						//recourseContainer.feasibilityEstablishedByIP=false;
						//recourseContainer.initialSGCKSSolFeasible;
						//recourseContainer.recourseResolvedFromScratch=false;
						
						
						
						//recourseContainer.feasibilityEstablishedByIP || 
						if(recourseContainer.initialGPSolFeasible){
							//set utilisation and algorithm objective value to those planned
							recourseContainer.utilisation=commitedVArea/totalRectangleArea;//as promised
							recourseContainer.packedVehicleArea=commitedVArea;
							recourseContainer.nonPackedVehicleArea=0;
							recourseSolved=true;
							//
							/*if(recourseContainer.feasibilityEstablishedByIP){
								rateOfInitialSolutionFeasibility++;
							}*/
							//
							//if(recourseContainer.initialSGCKSSolFeasible){
								rateThatSGCKSIsFeasibleWithoutSearch++;
							//}
							
							//recourseContainer.objectiveValue=algObj;
						}else{
							//count this occurrence
							rateOfRecourseRequirement++;
							//if all vehicle were packed
							if(recourseContainer.nonPackedVehicleArea==0 && recourseContainer.recourseResolvedFromScratch){
								rateOfSuccessfulRecourse++;
								recourseContainer.utilisation=commitedVArea/totalRectangleArea;//as promised
								//recourseContainer.packedVehicleArea=commitedVArea;this is true
								recourseSolved=true;
								
							}
						}
						//
					//	packSolutionNumber++;
					//}
					
					
					
					
					if(recourseSolved){
						successfulPackingRate++;
					}
					
					averageNumberOfRecourseAttempts+=packSolutionNumber;
					//utilisation is completely useless here since the maximum utilisation has been bounded below 100% in the first stage
					//for a relative objective measure here use loaded rectangle area relative
					//although utilisation will be useful for comparison across experiments
					//So max util (first stage objective is a useful statistics for putting these results into context
					
					//if instead of storing "utilisation statistics we opt for loaded vehicle area, utilisation, relative to commited statsics can be easily recovered
					
					
					averagePackedVArea+=recourseContainer.packedVehicleArea;
					//what is this objective? It should be the loaded rectangles revenue-minus penalties for the area of rectangles not loaded
					
					averageRevenue+=commitedVArea-2*Math.max(0, recourseContainer.nonPackedVehicleArea);	//(totalRectangleArea)
					
					//ratio of the committed vehicle mix packed
					aNumber aNum=new aNumber(recourseContainer.packedVehicleArea/commitedVArea);
					sortedUtils.addEdgeToLinkedList(aNum);
					
					aNumber aNum2=new aNumber(recourseContainer.packedVehicleArea/commitedVArea);
					nonSortedUtils.add(aNum2);
					
				}
				
				successfulPackingRate/=simulationRuns;
				
				averageNumberOfRecourseAttempts/=simulationRuns;
				
				rateOfInitialSolutionFeasibility/=simulationRuns;
				
				rateThatSGCKSIsFeasibleWithoutSearch/=simulationRuns;
				
				penaltyBeforeRecourse/=simulationRuns;
				
				//
				//averageNumberOfRecourseAttempts+" "+rateOfInitialSolutionFeasibility+" "+rateThatSGCKSIsFeasibleWithoutSearch+" "+
				
				//
				//recourseContainer.YA.sol.printSolutionString();
				averagePackedVArea/=simulationRuns;
				//System.out.println("averageUtilisation="+averageUtilisation);
				
				//System.out.println(algObj);
				averageRevenue/=simulationRuns;
				
				//
				if(rateOfRecourseRequirement>0){
					rateOfSuccessfulRecourse/=rateOfRecourseRequirement;//for the case when recourse was required
				}
				//
				rateOfRecourseRequirement/=simulationRuns;
				
				//print out results: average, min, max, quartiles
				double min=sortedUtils.getQuantile(0);
				double max=sortedUtils.getQuantile(1);
				double q1=sortedUtils.getQuantile(0.25);
				double q2=sortedUtils.getQuantile(0.50);
				double q3=sortedUtils.getQuantile(0.75);
				
				//expNamePrefix+", "+
				//System.out.println(expNamePrefix+": "+averageRevenue+",("+algObj+"),"+averageUtilisation+",("+algUtil+"),"+min+","+q1+","+q2+","+q3+","+max);
				//System.out.println(averageRevenue+", "+algObj+", "+averageUtilisation+", "+algUtil+", "+min+", "+q1+", "+q2+", "+q3+", "+max);
				System.out.print(initialAreaOfVehicles+" "+averageRevenue+" "+commitedVArea+" "+averagePackedVArea+" "+commitedUtilisation+" "+(averagePackedVArea/totalRectangleArea)+" "+min+" "+q1+" "+q2+" "+q3+" "+max+" "+solutionTime+" "+rateOfRecourseRequirement+" "+rateOfSuccessfulRecourse+" "+averageNumberOfRecourseAttempts+" "+rateOfInitialSolutionFeasibility+" "+rateThatSGCKSIsFeasibleWithoutSearch+" "+successfulPackingRate+" "+penaltyBeforeRecourse+" ");

				resultsOutput.print(initialAreaOfVehicles+" "+averageRevenue+" "+commitedVArea+" "+averagePackedVArea+" "+commitedUtilisation+" "+(averagePackedVArea/totalRectangleArea)+" "+min+" "+q1+" "+q2+" "+q3+" "+max+" "+solutionTime+" "+rateOfRecourseRequirement+" "+rateOfSuccessfulRecourse+" "+averageNumberOfRecourseAttempts+" "+rateOfInitialSolutionFeasibility+" "+rateThatSGCKSIsFeasibleWithoutSearch+" "+successfulPackingRate+" "+penaltyBeforeRecourse+" ");
				
				String resultsDirectory="results/";
				if(useRuleBasedYardPolicy){
					resultsDirectory="resultsRB/";
				}
				
				
				//the following are useless, append or create file names that depend on the values of each treatment
				
				PrintWriter summaryW=new PrintWriter(new FileOutputStream(resultsDirectory.concat("Summary").concat(expNamePrefix).concat(".txt")));
				summaryW.println(averageRevenue+",("+commitedVArea+"),"+averagePackedVArea+",("+commitedUtilisation+"),"+(averagePackedVArea/totalRectangleArea)+min+","+q1+","+q2+","+q3+","+max+" "+solutionTime+" "+rateOfRecourseRequirement+" "+rateOfSuccessfulRecourse+" "+averageNumberOfRecourseAttempts+" "+rateOfInitialSolutionFeasibility+" "+rateThatSGCKSIsFeasibleWithoutSearch+" "+successfulPackingRate+" "+initialAreaOfVehicles+" ");
				summaryW.close();
				
				PrintWriter aSummaryW=new PrintWriter(new FileOutputStream(resultsDirectory.concat("ASummary.txt")));
				aSummaryW.println(expNamePrefix+": "+averageRevenue+",("+commitedVArea+"),"+averagePackedVArea+",("+commitedUtilisation+"),"+(averagePackedVArea/totalRectangleArea)+","+min+","+q1+","+q2+","+q3+","+max+" "+solutionTime+" "+rateOfRecourseRequirement+" "+rateOfSuccessfulRecourse+" "+averageNumberOfRecourseAttempts+" "+rateOfInitialSolutionFeasibility+" "+rateThatSGCKSIsFeasibleWithoutSearch+" "+successfulPackingRate+" "+initialAreaOfVehicles+" ");
				aSummaryW.close();
				
				//sorted and non-sorted utilisations (comparison matrix can then be calculated
				PrintWriter sortedUtilsW=new PrintWriter(new FileOutputStream(resultsDirectory.concat("UtilsSorted").concat(expNamePrefix).concat(".txt")));
				sortedUtils.printWrite(sortedUtilsW);
				sortedUtilsW.close();
				
				PrintWriter nonSortedUtilsW=new PrintWriter(new FileOutputStream(resultsDirectory.concat("UtilsNonSorted").concat(expNamePrefix).concat(".txt")));
				nonSortedUtils.printWrite(nonSortedUtilsW);
				nonSortedUtilsW.close();
		//	}
		//}
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
