package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class evenDistributionProblemInstances {
	public static void main(String[] args) throws IOException{
		
		String ferryShape="RF";//"Rectangle";//
		
		//95440.0
		double totalRectangleArea=61009.0;//containerWidth*containerLength;
		
		if(ferryShape.equals("Rectangle")){
			totalRectangleArea=61009.0;
			
		}else if(ferryShape.equals("RF")){
			totalRectangleArea=95440.0;
		}
		
		double onePixel=0.103076923;
		
		double[] minRectDimensions={3,1.6,1.5};
		double[] maxRectDimensions={11,3.4,4.0};
		
		int numberOfVTypes=15;
		
		for(numberOfVTypes=1;numberOfVTypes<=100;numberOfVTypes++){
			//
			double[][] rectangleDistribution=new double[4][numberOfVTypes];
			//
			double[] vAreas=new double[numberOfVTypes];
			double sumOfAreas=0;
			for(int i=0;i<numberOfVTypes;i++){
				if(numberOfVTypes==1){
					rectangleDistribution[0][i]=(0.5*minRectDimensions[0]+0.5*maxRectDimensions[0])/onePixel;
					rectangleDistribution[1][i]=(0.5*minRectDimensions[1]+0.5*maxRectDimensions[1])/onePixel;
					rectangleDistribution[2][i]=(0.5*minRectDimensions[2]+0.5*maxRectDimensions[2])/onePixel;
				}else{
					rectangleDistribution[0][i]=(minRectDimensions[0]+((i/(double)(numberOfVTypes-1))*(maxRectDimensions[0]-minRectDimensions[0])))/onePixel;
					rectangleDistribution[1][i]=(minRectDimensions[1]+((i/(double)(numberOfVTypes-1))*(maxRectDimensions[1]-minRectDimensions[1])))/onePixel;
					rectangleDistribution[2][i]=(minRectDimensions[2]+((i/(double)(numberOfVTypes-1))*(maxRectDimensions[2]-minRectDimensions[2])))/onePixel;
				}
				vAreas[i]=rectangleDistribution[0][i]*rectangleDistribution[1][i];
				sumOfAreas+=vAreas[i];
			}
			//
			double sumA=0;
			double[] cumuProbDist=new double[numberOfVTypes];
			for(int i=0;i<numberOfVTypes;i++){
				//
				System.out.println(rectangleDistribution[0][i]+","+rectangleDistribution[1][i]+","+rectangleDistribution[2][i]);
				//
				sumA+=1/vAreas[i];
				cumuProbDist[i]=sumA;
				rectangleDistribution[3][i]=1/vAreas[i];
			}
			//
			for(int i=0;i<numberOfVTypes;i++){
				rectangleDistribution[3][i]/=sumA;
				cumuProbDist[i]/=sumA;
				System.out.print(rectangleDistribution[3][i]+",");
			}
			System.out.println();
			for(int i=0;i<numberOfVTypes;i++){
				System.out.print(cumuProbDist[i]+",");
			}
			System.out.println();
			
			//totalRectangleArea
			//max(1,(int) (prob*totArea)/vArea)
			int[] vCounts=new int[numberOfVTypes];
			for(int i=0;i<numberOfVTypes;i++){
				vCounts[i]=Math.max(1, (int)((rectangleDistribution[3][i]*totalRectangleArea)/vAreas[i]));
			}
			
			//find the total area of rectangle then randomly generate rectangles until the total area is exceeded
			double sumOfRectangleArea=0;
			for(int i=0;i<numberOfVTypes;i++){
				sumOfRectangleArea+=vCounts[i]*vAreas[i];
			}
			
			//top up
			Random randNumGen=new Random(1);
			while(sumOfRectangleArea<totalRectangleArea){
				double randomNumber=randNumGen.nextDouble();
				int vInd=0;
				while(randomNumber>cumuProbDist[vInd]){
					vInd++;
				}
				//
				vCounts[vInd]++;
				sumOfRectangleArea+=vAreas[vInd];
			}
			//
			for(int i=0;i<numberOfVTypes;i++){
				System.out.println(vCounts[i]+",");
			}
			
			//
			String instanceClass1Prefix="testInstances/TestInstanceClass5Distribution".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1=new PrintWriter(new FileOutputStream(instanceClass1Prefix));
			for(int i=0;i<rectangleDistribution.length;i++){
				for(int j=0;j<rectangleDistribution[i].length;j++){
					testInstanceClass1.print(rectangleDistribution[i][j]+",");
				}
				testInstanceClass1.println();
			}
			//
			testInstanceClass1.close();
			
			//best solution ever record
			String instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass5RectangleBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
			testInstanceClass1BestEverW.println("0");
			testInstanceClass1BestEverW.close();
			

			//best solution ever record
			String instanceClass1PrefixBestEver2="bestSolutionsGP/TestInstanceClass5RectangleBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverW2=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver2));
			testInstanceClass1BestEverW2.println("0");
			testInstanceClass1BestEverW2.close();
			
			
			//best solution ever record
			instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass5RFBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverRFW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
			testInstanceClass1BestEverRFW.println("0");
			testInstanceClass1BestEverRFW.close();
			

			//best solution ever record
			instanceClass1PrefixBestEver2="bestSolutionsGP/TestInstanceClass5RFBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverRFW2=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver2));
			testInstanceClass1BestEverRFW2.println("0");
			testInstanceClass1BestEverRFW2.close();
		}
		
		
		
		
		
		
		//Instance class 2 (from long and narrow to short and wide.
		double[] longNarrowRectDimensions={11,1.6,1.5};
		double[] shortWideRectDimensions={3,3.4,4.0};
		numberOfVTypes=15;
		
		for(numberOfVTypes=1;numberOfVTypes<=100;numberOfVTypes++){
			System.out.println("rectangleDistribution2=[");
			//
			double[][] rectangleDistribution2=new double[4][numberOfVTypes];
			//
			double[] vAreas2=new double[numberOfVTypes];
			double sumOfAreas2=0;
			for(int i=0;i<numberOfVTypes;i++){
				if(numberOfVTypes==1){
					rectangleDistribution2[0][i]=(0.5*longNarrowRectDimensions[0]+0.5*shortWideRectDimensions[0])/onePixel;
					rectangleDistribution2[1][i]=(0.5*longNarrowRectDimensions[1]+0.5*shortWideRectDimensions[1])/onePixel;
					rectangleDistribution2[2][i]=(0.5*longNarrowRectDimensions[2]+0.5*shortWideRectDimensions[2])/onePixel;
				}else{
					rectangleDistribution2[0][i]=(longNarrowRectDimensions[0]+((i/(double)(numberOfVTypes-1))*(shortWideRectDimensions[0]-longNarrowRectDimensions[0])))/onePixel;
					rectangleDistribution2[1][i]=(longNarrowRectDimensions[1]+((i/(double)(numberOfVTypes-1))*(shortWideRectDimensions[1]-longNarrowRectDimensions[1])))/onePixel;
					rectangleDistribution2[2][i]=(longNarrowRectDimensions[2]+((i/(double)(numberOfVTypes-1))*(shortWideRectDimensions[2]-longNarrowRectDimensions[2])))/onePixel;
				}
				vAreas2[i]=rectangleDistribution2[0][i]*rectangleDistribution2[1][i];
				sumOfAreas2+=vAreas2[i];
				//
				System.out.println("type"+i+", {"+rectangleDistribution2[0][i]+","+rectangleDistribution2[1][i]+","+rectangleDistribution2[2][i]);
			}
			
			//
			double sumA2=0;
			double[] cumuProbDist2=new double[numberOfVTypes];
			for(int i=0;i<numberOfVTypes;i++){
				//
				System.out.println(rectangleDistribution2[0][i]+","+rectangleDistribution2[1][i]+","+rectangleDistribution2[2][i]);
				//
				sumA2+=1/vAreas2[i];
				cumuProbDist2[i]=sumA2;
				rectangleDistribution2[3][i]=1/vAreas2[i];
			}
			//
			for(int i=0;i<numberOfVTypes;i++){
				rectangleDistribution2[3][i]/=sumA2;
				cumuProbDist2[i]/=sumA2;
				System.out.print(rectangleDistribution2[3][i]+",");
			}
			System.out.println();
			for(int i=0;i<numberOfVTypes;i++){
				System.out.print(cumuProbDist2[i]+",");
			}
			System.out.println();
			
			//totalRectangleArea
			//max(1,(int) (prob*totArea)/vArea)
			int[] vCounts2=new int[numberOfVTypes];
			for(int i=0;i<numberOfVTypes;i++){
				vCounts2[i]=Math.max(1, (int)((rectangleDistribution2[3][i]*totalRectangleArea)/vAreas2[i]));
			}
			
			//find the total area of rectangle then randomly generate rectangles until the total area is exceeded
			double sumOfRectangleArea2=0;
			for(int i=0;i<numberOfVTypes;i++){
				sumOfRectangleArea2+=vCounts2[i]*vAreas2[i];
			}
			
			//top up
			Random randNumGen2=new Random(1);
			while(sumOfRectangleArea2<totalRectangleArea){
				double randomNumber=randNumGen2.nextDouble();
				int vInd=0;
				while(randomNumber>cumuProbDist2[vInd]){
					vInd++;
				}
				//
				vCounts2[vInd]++;
				sumOfRectangleArea2+=vAreas2[vInd];
			}
			//
			for(int i=0;i<numberOfVTypes;i++){
				System.out.println(vCounts2[i]+",");
			}
			
			
			//
			String instanceClass2Prefix="testInstances/TestInstanceClass6Distribution".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass2=new PrintWriter(new FileOutputStream(instanceClass2Prefix));
			for(int i=0;i<rectangleDistribution2.length;i++){
				for(int j=0;j<rectangleDistribution2[i].length;j++){
					testInstanceClass2.print(rectangleDistribution2[i][j]+",");
				}
				testInstanceClass2.println();
			}
			//
			testInstanceClass2.close();
			
			
			//best solution ever record
			String instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass6RectangleBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
			testInstanceClass1BestEverW.println("0");
			testInstanceClass1BestEverW.close();
			

			//best solution ever record
			String instanceClass1PrefixBestEver2="bestSolutionsGP/TestInstanceClass6RectangleBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverW2=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver2));
			testInstanceClass1BestEverW2.println("0");
			testInstanceClass1BestEverW2.close();
			
			
			//best solution ever record
			instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass6RFBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverRFW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
			testInstanceClass1BestEverRFW.println("0");
			testInstanceClass1BestEverRFW.close();
			

			//best solution ever record
			instanceClass1PrefixBestEver2="bestSolutionsGP/TestInstanceClass6RFBestUtilisation".concat(String.valueOf(numberOfVTypes)).concat("VehicleTypes.txt");
			//file the test instance to a file
			PrintWriter testInstanceClass1BestEverRFW2=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver2));
			testInstanceClass1BestEverRFW2.println("0");
			testInstanceClass1BestEverRFW2.close();
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//Instance class 3 (matrix of short-long and narrow-wide
		double minWidth=1.6;
		double maxWidth=3.4;
		double minLength=3;
		double maxLength=11;
		int numWidths=3;
		int numLengths=3;
		
		int instanceClass3Number=1;
		
		for(numWidths=1;numWidths<=10;numWidths++){
			for(numLengths=1;numLengths<=10;numLengths++){
				double[] widths=new double[numWidths];
				double[] lengths=new double[numLengths];
				for(int i=0;i<numWidths;i++){
					if(numWidths==1){
						widths[i]=(0.5*minWidth+0.5*maxWidth)/onePixel;
					}else{
						widths[i]=(minWidth+(i*((maxWidth-minWidth)/(numWidths-1))))/onePixel;
					}
					
				}
				for(int i=0;i<numLengths;i++){
					if(numLengths==1){
						lengths[i]=(0.5*minLength+0.5*maxLength)/onePixel;
					}else{
						lengths[i]=(minLength+(i*((maxLength-minLength)/(numLengths-1))))/onePixel;
					}
					
				}
				int nVTypes2=numWidths*numLengths;
				//
				System.out.println("rectangleDistribution3=[");
				double[][] rectangleDistribution3=new double[4][nVTypes2];
				double[] vAreas3=new double[nVTypes2];
				double sumOfAreas3=0;
				int typeNumber=0;
				for(int i=0;i<numWidths;i++){
					for(int j=0;j<numLengths;j++){
						rectangleDistribution3[0][typeNumber]=lengths[j];
						rectangleDistribution3[1][typeNumber]=widths[i];
						rectangleDistribution3[2][typeNumber]=2/onePixel;
						vAreas3[typeNumber]=rectangleDistribution3[0][typeNumber]*rectangleDistribution3[1][typeNumber];
						sumOfAreas3+=vAreas3[i];
						//
						System.out.println("type"+typeNumber+", {"+rectangleDistribution3[0][typeNumber]+","+rectangleDistribution3[1][typeNumber]+","+rectangleDistribution3[2][typeNumber]);
						//
						typeNumber++;
					}
				}
				
				//
				double sumA3=0;
				double[] cumuProbDist3=new double[nVTypes2];
				for(int i=0;i<nVTypes2;i++){
					//
					System.out.println(rectangleDistribution3[0][i]+","+rectangleDistribution3[1][i]+","+rectangleDistribution3[2][i]);
					//
					sumA3+=1/vAreas3[i];
					cumuProbDist3[i]=sumA3;
					rectangleDistribution3[3][i]=1/vAreas3[i];
				}
				//
				for(int i=0;i<nVTypes2;i++){
					rectangleDistribution3[3][i]/=sumA3;
					cumuProbDist3[i]/=sumA3;
					System.out.print(rectangleDistribution3[3][i]+",");
				}
				System.out.println();
				for(int i=0;i<nVTypes2;i++){
					System.out.print(cumuProbDist3[i]+",");
				}
				System.out.println();
				
				//totalRectangleArea
				//max(1,(int) (prob*totArea)/vArea)
				int[] vCounts3=new int[nVTypes2];
				for(int i=0;i<nVTypes2;i++){
					vCounts3[i]=Math.max(1, (int)((rectangleDistribution3[3][i]*totalRectangleArea)/vAreas3[i]));
				}
				
				//find the total area of rectangle then randomly generate rectangles until the total area is exceeded
				double sumOfRectangleArea3=0;
				for(int i=0;i<nVTypes2;i++){
					sumOfRectangleArea3+=vCounts3[i]*vAreas3[i];
				}
				
				//top up
				Random randNumGen3=new Random(1);
				while(sumOfRectangleArea3<totalRectangleArea){
					double randomNumber=randNumGen3.nextDouble();
					int vInd=0;
					while(randomNumber>cumuProbDist3[vInd]){
						vInd++;
					}
					//
					vCounts3[vInd]++;
					sumOfRectangleArea3+=vAreas3[vInd];
				}
				//
				for(int i=0;i<nVTypes2;i++){
					System.out.println(vCounts3[i]+",");
				}
				//
				//String instanceClass3Prefix="testInstances/TestInstanceClass3Distribution".concat(String.valueOf(numWidths)).concat("Widths").concat(String.valueOf(numLengths)).concat("Lengths.txt");
				//instanceClass3Number
				String instanceClass3Prefix="testInstances/TestInstanceClass7Distribution".concat(String.valueOf(instanceClass3Number)).concat("VehicleTypes.txt");
				//file the test instance to a file
				PrintWriter testInstanceClass3=new PrintWriter(new FileOutputStream(instanceClass3Prefix));
				for(int i=0;i<rectangleDistribution3.length;i++){
					for(int j=0;j<rectangleDistribution3[i].length;j++){
						testInstanceClass3.print(rectangleDistribution3[i][j]+",");
					}
					testInstanceClass3.println();
				}
				testInstanceClass3.println(numWidths+","+numLengths);
				//
				testInstanceClass3.close();
				//
				
				
				
				
				//best solution ever record
				String instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass7RectangleBestUtilisation".concat(String.valueOf(instanceClass3Number)).concat("VehicleTypes.txt");
				//file the test instance to a file
				PrintWriter testInstanceClass1BestEverW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
				testInstanceClass1BestEverW.println("0");
				testInstanceClass1BestEverW.close();
				

				//best solution ever record
				String instanceClass1PrefixBestEver2="bestSolutionsGP/TestInstanceClass7RectangleBestUtilisation".concat(String.valueOf(instanceClass3Number)).concat("VehicleTypes.txt");
				//file the test instance to a file
				PrintWriter testInstanceClass1BestEverW2=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver2));
				testInstanceClass1BestEverW2.println("0");
				testInstanceClass1BestEverW2.close();
				
				
				//best solution ever record
				instanceClass1PrefixBestEver="bestSolutionsSGCKS/TestInstanceClass7RFBestUtilisation".concat(String.valueOf(instanceClass3Number)).concat("VehicleTypes.txt");
				//file the test instance to a file
				PrintWriter testInstanceClass1BestEverRFW=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver));
				testInstanceClass1BestEverRFW.println("0");
				testInstanceClass1BestEverRFW.close();
				

				//best solution ever record
				instanceClass1PrefixBestEver2="bestSolutionsGP/TestInstanceClass7RFBestUtilisation".concat(String.valueOf(instanceClass3Number)).concat("VehicleTypes.txt");
				//file the test instance to a file
				PrintWriter testInstanceClass1BestEverRFW2=new PrintWriter(new FileOutputStream(instanceClass1PrefixBestEver2));
				testInstanceClass1BestEverRFW2.println("0");
				testInstanceClass1BestEverRFW2.close();
				
				
				instanceClass3Number++;
			}
		}
		
		
		
		
	}
}
