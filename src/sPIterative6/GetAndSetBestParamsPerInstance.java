package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GetAndSetBestParamsPerInstance {

	//this will not be part of the usual procedure
	//just run once to get parameters for all experiments hereafter
	static void setRecommendedParameters(String paramSetString, boolean SGCKS, String[] ferryShapes) throws Exception {

		String expNameString=paramSetString;//String.valueOf(paramSetNumber);//"2"; //perhaps codify parameter sets with numbers
		
		String packingMethod="SOCKS";
		if(!SGCKS){
			packingMethod="GP";
		}
		
		
		String resultsDirectory="results/";
		/*if(useRuleBasedYardPolicy){
			resultsDirectory="resultsRB/";
		}*/
		
		String inputFileName=resultsDirectory.concat("resultsOutputPackingParamExps".concat(expNameString).concat(packingMethod));
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		for(int i=0;i<ferryShapes.length;i++){
			inputFileName=inputFileName.concat(ferryShapes[i]);
		}
		
		inputFileName=inputFileName.concat(".txt");
		BufferedReader resultsInput=new BufferedReader(new FileReader(inputFileName));
		
		String line=resultsInput.readLine();//headings
		//the second line has=[ in it
		line=resultsInput.readLine();
		String[] lineA=line.split("\\[");
		lineA=lineA[1].split(" ");
		
		int classInd=3;
		int instanceInd=4;
		
		int T0Ind=18;
		int[] neighbourHoodIndices={19,20,21,22,23};
		int triangularDistInd=10;
		int numberOfLanesInd=2;
		
		int packedAreaInd=24;
		
		//fill in as many values of this table as possible, then
		//drag up (to fill in missing rows)
		double[][][] bestSearchParamsForEachInstance=new double[3][100][9];
		
		//first line manually
		double T0=Double.parseDouble(lineA[T0Ind]);
		double nH1=Double.parseDouble(lineA[neighbourHoodIndices[0]]);
		double nH2=Double.parseDouble(lineA[neighbourHoodIndices[1]])-nH1;
		double nH3=Double.parseDouble(lineA[neighbourHoodIndices[2]])-nH2-nH1;
		double nH4=Double.parseDouble(lineA[neighbourHoodIndices[3]])-nH3-nH2-nH1;
		double nH5=Double.parseDouble(lineA[neighbourHoodIndices[4]])-nH4-nH3-nH2-nH1;
		
		double tD=Double.parseDouble(lineA[triangularDistInd]);
		
		double nL=Double.parseDouble(lineA[numberOfLanesInd]);
		
		double packedArea=Double.parseDouble(lineA[packedAreaInd]);
		
		int classNumber=Integer.parseInt(lineA[classInd]);
		int instanceNumber=Integer.parseInt(lineA[instanceInd]);
		
		//0:T0
		//1:nH1
		//2:nH2
		//3:nH3
		//4:nH4
		//5:nH5
		//6:triDist
		//7:numLanes
		
		
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][0]=T0;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][1]=nH1;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][2]=nH2;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][3]=nH3;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][4]=nH4;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][5]=nH5;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][6]=tD;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][7]=nL;
		bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][8]=packedArea;
		
		
		
		//the rest
		while((line=resultsInput.readLine())!=null){
			lineA=line.split(" ");
			//first line manually
			
			//is this a new best parameter set for this instance
			classNumber=Integer.parseInt(lineA[classInd]);
			instanceNumber=Integer.parseInt(lineA[instanceInd]);
			packedArea=Double.parseDouble(lineA[packedAreaInd]);
			if(packedArea>bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][8]){
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][8]=packedArea;
				
				T0=Double.parseDouble(lineA[T0Ind]);
				nH1=Double.parseDouble(lineA[neighbourHoodIndices[0]]);
				nH2=Double.parseDouble(lineA[neighbourHoodIndices[1]])-nH1;
				nH3=Double.parseDouble(lineA[neighbourHoodIndices[2]])-nH2-nH1;
				nH4=Double.parseDouble(lineA[neighbourHoodIndices[3]])-nH3-nH2-nH1;
				nH5=Double.parseDouble(lineA[neighbourHoodIndices[4]])-nH4-nH3-nH2-nH1;
				
				tD=Double.parseDouble(lineA[triangularDistInd]);
				
				nL=Double.parseDouble(lineA[numberOfLanesInd]);
				
				
				
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][0]=T0;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][1]=nH1;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][2]=nH2;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][3]=nH3;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][4]=nH4;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][5]=nH5;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][6]=tD;
				bestSearchParamsForEachInstance[classNumber-5][instanceNumber-1][7]=nL;
			}
		}
		resultsInput.close();
		
		//drag up (assign parameters to empty rows using the best parameters for the next biggest instance in the same class
		
		for(int i=0;i<3;i++){
			int lastNonEptyRow=99;//the rule is to always derive parameters for the largest instance
			for(int j=98;j>=0;j--){
				if(bestSearchParamsForEachInstance[i][j][8]>0){
					//
					lastNonEptyRow=j;
				}else{
					//copy row "lastNonEptyRow"
					for(int k=0;k<8;k++){
						bestSearchParamsForEachInstance[i][j][k]=bestSearchParamsForEachInstance[i][lastNonEptyRow][k];
					}
				}
			}
		}
		
		//store this array so that it can be read in and passed to first stage and second stage packing algorithms
		//which can be optional (useRecordedParamsForEach
		
		String recommendedParamsFileName=resultsDirectory.concat("recommendedParameters".concat(expNameString).concat(packingMethod));
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		for(int i=0;i<ferryShapes.length;i++){
			recommendedParamsFileName=recommendedParamsFileName.concat(ferryShapes[i]);
		}
		
		recommendedParamsFileName=recommendedParamsFileName.concat(".txt");
		
		PrintWriter paramStorageW=new PrintWriter(new FileOutputStream(recommendedParamsFileName));
		//line for each class
		//; for each instance
		//, for each parameter
		for(int i=0;i<bestSearchParamsForEachInstance.length;i++){
			for(int j=0;j<bestSearchParamsForEachInstance[i].length;j++){
				for(int k=0;k<8;k++){
					paramStorageW.print(bestSearchParamsForEachInstance[i][j][k]+",");
				}
				paramStorageW.print(";");
			}
			paramStorageW.println();
		}
		paramStorageW.close();
		
	}
	
	static double[][][] getRecommendedParameters(String paramSetString, boolean SGCKS, String[] ferryShapes) throws IOException{
		double[][][] recommendedParam=new double[3][100][8];
		
		
		String expNameString=paramSetString;//String.valueOf(paramSetNumber);//"2"; //perhaps codify parameter sets with numbers
		
		
		String resultsDirectory="results/";
		/*if(useRuleBasedYardPolicy){
			resultsDirectory="resultsRB/";
		}*/
		
		String packingMethod="SOCKS";
		if(!SGCKS){
			packingMethod="GP";
		}
		
		//store this array so that it can be read in and passed to first stage and second stage packing algorithms
		//which can be optional (useRecordedParamsForEach
		
		String recommendedParamsFileName=resultsDirectory.concat("recommendedParameters".concat(expNameString).concat(packingMethod));
		
		//it could be useful to write the defining characteristics of oeach experiment here (rather than extract it later)
		for(int i=0;i<ferryShapes.length;i++){
			recommendedParamsFileName=recommendedParamsFileName.concat(ferryShapes[i]);
		}
		
		recommendedParamsFileName=recommendedParamsFileName.concat(".txt");
		
		BufferedReader paramStorageR=new BufferedReader(new FileReader(recommendedParamsFileName));
		
		
		//line for each class
		//; for each instance
		//, for each parameter
		for(int i=0;i<recommendedParam.length;i++){
			String line=paramStorageR.readLine();
			String[] lineA=line.split(";");
			for(int j=0;j<recommendedParam[i].length;j++){
				String[] lineB=lineA[j].split(",");
				for(int k=0;k<8;k++){
					recommendedParam[i][j][k]=Double.parseDouble(lineB[k]);
				}
			}
		}
		paramStorageR.close();
		
		
		return recommendedParam;
	}

}
