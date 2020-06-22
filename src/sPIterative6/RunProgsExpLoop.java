package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.BufferedReader;
import java.io.FileReader;


public class RunProgsExpLoop {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		String[] fileNames={
				
				
			"/experiments/allInstancesGP.txt",//
			//"/experiments/allInstancesSOPE.txt",//
			
			//"/experiments/numberOfLanesDemo.txt",//
			
			//"/experiments/Fig8_nLanesRuleBasedVersusProposedApproachVMI.txt",//

			//"/experiments/Fig9_ObjComparisonRect2RepeatJuly2019Obj0VMI_LS_finishing_touch.txt",
			//"/experiments/Fig9_ObjComparisonRect2RepeatJuly2019Obj1EXP_LS_finishing_touch.txt",
			//"/experiments/Fig9_ObjComparisonRect2RepeatJuly2019Obj0VMI_No_LS_finishing_touch.txt",
			//"/experiments/Fig9_ObjComparisonRect2RepeatJuly2019Obj1EXP_No_LS_finishing_touch.txt",
			
			//"/experiments/Figs10_11_RobustnessTestRectJuly2019Obj0VMI.txt",
			//"/experiments/Figs10_11_RobustnessTestRectJuly2019Obj1EXP.txt",
		};
		
		
		
		 String workingDir = System.getProperty("user.dir");
		 System.out.println("Current working directory : " + workingDir);

		
		for(int jj=0;jj<fileNames.length;jj++){
			BufferedReader expListR=new BufferedReader(new FileReader(workingDir+fileNames[jj]));
			boolean expsComplete=false;
			while(!expsComplete){
				String line=expListR.readLine();
				String expName=line.split("=")[1];
				//
				line=expListR.readLine().split("=")[1];
				boolean SGCKS=false;
				if(line.equals("SGCKS")){
					SGCKS=true;
				}
				//
				line=expListR.readLine().split("=")[1];
				String[] ferryShapes={line};
				//
				line=expListR.readLine().split("=")[1];
				String paramExpNum=line;
				//
				line=expListR.readLine().split("=")[1];
				boolean useRecommendedParameters=false;
				if(line.equals("true")){
					useRecommendedParameters=true;
				}
				//
				line=expListR.readLine().split("=")[1];
				String[] lineA=line.split(",");
				int[] numberOfLanes=new int[lineA.length];
				for(int i=0;i<lineA.length;i++){
					numberOfLanes[i]=Integer.parseInt(lineA[i]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(";");
				int[][] classInstanceNumbers=new int[lineA.length][2];
				for(int i=0;i<lineA.length;i++){
					String[] lineB=lineA[i].split(",");
					classInstanceNumbers[i][0]=Integer.parseInt(lineB[0]);
					classInstanceNumbers[i][1]=Integer.parseInt(lineB[1]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				int[] numberOfArrivalScenarios=new int[lineA.length];
				for(int i=0;i<lineA.length;i++){
					numberOfArrivalScenarios[i]=Integer.parseInt(lineA[i]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				int[] arrivalScenarioSeeds=new int[lineA.length];
				for(int i=0;i<lineA.length;i++){
					arrivalScenarioSeeds[i]=Integer.parseInt(lineA[i]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(";");
				int[][] subsetSizes=new int[lineA.length][1];
				for(int i=0;i<lineA.length;i++){
					String[] lineB=lineA[i].split(",");
					subsetSizes[i]=new int[lineB.length];
					for(int j=0;j<lineB.length;j++){
						subsetSizes[i][j]=Integer.parseInt(lineB[j]);
					}
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				double[] intersectionObjectiveWeights=new double[lineA.length];
				for(int i=0;i<lineA.length;i++){
					intersectionObjectiveWeights[i]=Double.parseDouble(lineA[i]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(";");
				int[][] iterationsSubIterations=new int[lineA.length][3];
				for(int i=0;i<lineA.length;i++){
					String[] lineB=lineA[i].split(",");
					iterationsSubIterations[i][0]=Integer.parseInt(lineB[0]);
					iterationsSubIterations[i][1]=Integer.parseInt(lineB[1]);
					iterationsSubIterations[i][2]=Integer.parseInt(lineB[2]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				double[] T0s=new double[lineA.length];
				for(int i=0;i<lineA.length;i++){
					T0s[i]=Double.parseDouble(lineA[i]);
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				boolean[] triangleDistMutate=new boolean[lineA.length];
				for(int i=0;i<lineA.length;i++){
					if(lineA[i].equals("true")){
						triangleDistMutate[i]=true;
					}else{
						triangleDistMutate[i]=false;
					}
				}
				//
				line=expListR.readLine().split("=")[1];
				boolean visualise=false;
				if(line.equals("true")){
					visualise=true;
				}
				//
				line=expListR.readLine().split("=")[1];
				boolean commitIntersectionVMix=false;
				if(line.equals("true")){
					commitIntersectionVMix=true;
				}
				//
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				int[] objectiveFunctions=new int[lineA.length];
				for(int i=0;i<lineA.length;i++){
					objectiveFunctions[i]=Integer.parseInt(lineA[i]);
				}
				//levels of VMC relaxation
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				int[] levelOfVehicleIntersectionMixRelaxtions=new int[lineA.length];
				for(int i=0;i<lineA.length;i++){
					levelOfVehicleIntersectionMixRelaxtions[i]=Integer.parseInt(lineA[i]);
				}
				//Best known solution as input
				line=expListR.readLine().split("=")[1];
				boolean bestKnownSolution=false;
				if(line.equals("true")){
					bestKnownSolution=true;
				}
				//Perform local search finishing touch
				line=expListR.readLine().split("=")[1];
				boolean performLSFinishingTouch=false;
				if(line.equals("true")){
					performLSFinishingTouch=true;
				}
				//yardPolicyChoices
				line=expListR.readLine().split("=")[1];
				lineA=line.split(",");
				boolean[] yardPolicyChoices=new boolean[lineA.length];
				for(int i=0;i<lineA.length;i++){
					yardPolicyChoices[i]=Boolean.parseBoolean(lineA[i]);
				}
				//testTrainingSetSolutionOnTestSet
				line=expListR.readLine().split("=")[1];
				boolean testTrainingSetSolutionOnTestSet=false;
				if(line.equals("true")){
					testTrainingSetSolutionOnTestSet=true;
				}
				//read gap
				if(expListR.readLine()==null){
					expsComplete=true;
				}
				runExp(expName, SGCKS, ferryShapes, paramExpNum, useRecommendedParameters, numberOfLanes, classInstanceNumbers, numberOfArrivalScenarios, arrivalScenarioSeeds, subsetSizes, intersectionObjectiveWeights, iterationsSubIterations, T0s, triangleDistMutate, visualise, commitIntersectionVMix, objectiveFunctions, levelOfVehicleIntersectionMixRelaxtions, bestKnownSolution, performLSFinishingTouch, yardPolicyChoices, testTrainingSetSolutionOnTestSet);
	
			}
			expListR.close();
						
		}
		
		
		
	}

	static void runExp(String expNameString, boolean SGCKS, String[] ferryShapes, String paramExpNum, boolean useRecommendedParameters, int[] numbersOfLanes, int[][] classInstanceNumbers, int[] numberOfArrivalScenarios, int[] initialArrivalScenarioSeeds, int[][] comittedVMixQuantileIndices, double[] intersectionObjectiveWeights, int[][] iterationsSubIterations, double[] T0s, boolean[] useTriangularDistributionGeneToMutate, boolean visualiseBestSolutions, boolean commitIntersectionVMix, int[] objectiveFunctions, int[] levelOfVehicleIntersectionMixRelaxtions, boolean bestKnownSolution, boolean performLSFinishingTouch, boolean[] yardPolicyChoices, boolean testTrainingSetSolutionOnTestSet) throws Exception{
		//String expNameString="EXP4";
		
		//boolean useRuleBasedYardPolicy=true;//false;//
		
		//boolean SGCKS=false;//true;//
		//boolean SANotGA=true;//false;//
		
		//String[] ferryShapes={"Rectangle"};//{"RF"};//
		//String[] methodology={}
		String[] ExperimentNamesSGCKS=null;//{"SOCKS_Scenario_GA"};//{"SOCKS_Iterative_SA"};//{"SOCKS_Scenario_GA"};//
		String[] ExperimentNamesGP=null;//{"GeneralPacking_Iterative_SA"};//,{"GeneralPacking_Scenario_GA"}};//{"GeneralPacking_Iterative_SA"};//{"GeneralPacking_Scenario_GA"};//
		String[][] ExperimentNamesGPA={{"GeneralPacking_Iterative_SA"},{"GeneralPacking_Scenario_GA"}};//{"GeneralPacking_Iterative_SA"};//{"GeneralPacking_Scenario_GA"};//
		
		//GetAndSetBestParamsPerInstance.setRecommendedParameters(paramExpNum, SGCKS, ferryShapes);
		double[][][] recommendedParams=GetAndSetBestParamsPerInstance.getRecommendedParameters(paramExpNum, SGCKS, ferryShapes);
		//turn the neighbourhood probabilities into cumulative distributions
		for(int i=0;i<recommendedParams.length;i++){
			for(int j=0;j<recommendedParams[i].length;j++){
				for(int k=1;k<6;k++){
					if(k>1){
						recommendedParams[i][j][k]=recommendedParams[i][j][k]+recommendedParams[i][j][k-1];
					}
				}
			}
		}
		
		
		boolean[] breakTiesLestFulls={false};
		boolean[] moduloScenarios={false};
		int[] yardPolicyTypes={0};
		
		
		//and neighbourhood distributionso
		double[][] neighbourTypeDistributions={{1, 0, 0 ,0, 0},
				{0.0, 1, 0.0 ,0.0, 0.0},
				{0.0, 0.0, 1, 0.0, 0.0},
				{0.0, 0.0, 0.0, 1, 0.0},
				{0.0, 0.0, 0.0, 0.0, 1},
				{0.5, 0.5, 0.0, 0.0, 0.0},
				{0.5, 0.0, 0.5, 0.0, 0.0},
				{0.5, 0.0, 0.0, 0.5, 0.0},
				{0.5, 0.0, 0.0, 0.0, 0.5},
				{0.0, 0.5, 0.5, 0.0, 0.0},
				{0.0, 0.5, 0.0, 0.5, 0.0},
				{0.0, 0.5, 0.0, 0.0, 0.5},
				{0.0, 0.0, 0.5, 0.5, 0.0},
				{0.0, 0.0, 0.5, 0.0, 0.5},
				{0.0, 0.0, 0.0, 0.5, 0.5},
				{0.333, 0.333, 0.334, 0.0, 0.0},
				{0.333, 0.333, 0.0, 0.334, 0.0},
				{0.333, 0.333, 0.0, 0.0, 0.334},
				{0.333, 0.0, 0.333, 0.334, 0.0},
				{0.333, 0.0, 0.333, 0.0, 0.334},
				{0.333, 0.0, 0.0, 0.333, 0.334},
				{0.0, 0.333, 0.333, 0.334, 0.0},
				{0.0, 0.333, 0.333, 0.0, 0.334},
				{0.0, 0.333, 0.0, 0.333, 0.334},
				{0.0, 0.0, 0.333, 0.333, 0.334},
				{0.25, 0.25, 0.25, 0.25, 0.0},
				{0.25, 0.25, 0.25, 0.0, 0.25},
				{0.25, 0.25, 0.0, 0.25, 0.25},
				{0.25, 0.0, 0.25, 0.25, 0.25},
				{0.0, 0.25, 0.25, 0.25, 0.25},
				{0.2, 0.2, 0.2, 0.2, 0.2},
				};//
		
		
		
		
		double[][] cumuNighbourDistributions=new double[neighbourTypeDistributions.length][5];
		for(int i=0;i<neighbourTypeDistributions.length;i++){
			double sum=0;
			for(int j=0;j<5;j++){
				sum+=neighbourTypeDistributions[i][j];
				cumuNighbourDistributions[i][j]=sum;
			}
		}
		
		if(useRecommendedParameters){
			//make the lengths of the relevant arrays one
			cumuNighbourDistributions=new double[1][5];;
			useTriangularDistributionGeneToMutate=new boolean[1];
			T0s=new double[1];
		}
		
		//, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls
		if(SGCKS){
			
			ExperimentNamesSGCKS=new String[1];
			ExperimentNamesSGCKS[0]="SOCKS_Iterative_SA";
		
			ZISAParallelOneUncertaintySet.main(expNameString, ferryShapes, yardPolicyChoices, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls, moduloScenarios, yardPolicyTypes, comittedVMixQuantileIndices, intersectionObjectiveWeights, iterationsSubIterations, T0s, cumuNighbourDistributions, visualiseBestSolutions, initialArrivalScenarioSeeds, useTriangularDistributionGeneToMutate, useRecommendedParameters, recommendedParams, commitIntersectionVMix, levelOfVehicleIntersectionMixRelaxtions, bestKnownSolution);
			
			if(testTrainingSetSolutionOnTestSet) {
				ZYardPolicyTestSOCKS.main(expNameString, ferryShapes, ExperimentNamesSGCKS, yardPolicyChoices, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls, moduloScenarios, yardPolicyTypes, comittedVMixQuantileIndices, intersectionObjectiveWeights, iterationsSubIterations, T0s, cumuNighbourDistributions, visualiseBestSolutions, initialArrivalScenarioSeeds, useTriangularDistributionGeneToMutate, useRecommendedParameters, recommendedParams, commitIntersectionVMix, levelOfVehicleIntersectionMixRelaxtions);
			}
			
			//ZSGCKSRevAndRobustCalculation.main(ferryShapes, ExperimentNamesSGCKS, useRuleBasedYardPolicy, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls, moduloScenarios, yardPolicyTypes);
		}else{
			
			ExperimentNamesGP=new String[1];
			ExperimentNamesGP[0]="GeneralPacking_Iterative_SA";
			
			
			ZISAGPParallelOneUncertaintySet.main(expNameString, ferryShapes, yardPolicyChoices, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls, moduloScenarios, yardPolicyTypes, comittedVMixQuantileIndices, intersectionObjectiveWeights, iterationsSubIterations, T0s, cumuNighbourDistributions, visualiseBestSolutions, initialArrivalScenarioSeeds, useTriangularDistributionGeneToMutate, useRecommendedParameters, recommendedParams, commitIntersectionVMix, levelOfVehicleIntersectionMixRelaxtions, bestKnownSolution, performLSFinishingTouch);
		
			if(testTrainingSetSolutionOnTestSet) {
				ZYardPolicyTestGP2.main(expNameString, ferryShapes, ExperimentNamesGP, yardPolicyChoices, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls, moduloScenarios, yardPolicyTypes, comittedVMixQuantileIndices, intersectionObjectiveWeights, iterationsSubIterations, T0s, cumuNighbourDistributions, visualiseBestSolutions, initialArrivalScenarioSeeds, useTriangularDistributionGeneToMutate, useRecommendedParameters, recommendedParams, commitIntersectionVMix, levelOfVehicleIntersectionMixRelaxtions);
			}
			
			
			//ZGPRevAndRobustCalculation.main(ferryShapes, ExperimentNamesGP, useRuleBasedYardPolicy, numbersOfLanes, classInstanceNumbers, numberOfArrivalScenarios, objectiveFunctions, breakTiesLestFulls, moduloScenarios, yardPolicyTypes);
		}
	}

}
