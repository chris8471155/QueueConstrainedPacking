package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

//part of the checking is to ensure that .w and .l/widths and lengths are used correctly with "forNewRow", easy to mix up.

public class Yard{
	//
	int aCounter=0;
	//
	int numberOfYardLanes;
	Queue[] yardLanes;
	
	//problem instance, stores the itesm to be packed and keeps a record of those
	Rectangle[] allRectangles;
	
	Rectangle[] rectTypes;
	
	int totalRectangles;
	int remainingRectangles;
	
	double totalAreaOfRectangles;
	
	Rectangle head;
	Rectangle tail;
	int length;
	//Rectangle[] lastIncludableRectangleInEachQueue;//likely to be commented out
	//the following defines the set of reachable rectangles for the strip under consideration
	//Rectangle[] includableHeadTail;
	
	Random randGen=new Random(10);
	
	boolean nonConstrainedLoading=false;
	
	Rectangle headByWidthNonConstrainedLoading;
	Rectangle tailByWidthNonConstrainedLoading;
	Rectangle headByLengthNonConstrainedLoading;
	Rectangle tailByLengthNonConstrainedLoading;
	
	int initialLengthPolicyCreation;
	int lengthNonConstrainedLoading;
	
	Rectangle headByWidthYardPolicyCreation;
	Rectangle tailByWidthYardPolicyCreation;
	Rectangle headByLengthYardPolicyCreation;
	Rectangle tailByLengthYardPolicyCreation;
	
	static int numberOfYardQuantiles;
	static int numberOfYardOrientations;
	static double[] setOfQuantiles;
	
	int[] vCountByType;
	int vTypes;
	
	
	//solution representation: linked list of genes
	YardSolution sol;
	YardSolution sol2;
	//YardSolution solRef;//needed for swapping sol and sol2 in each generation (after selection)
	
	boolean resetBinNumbers=false;
	//Solution sol sol2 (just as in Container
	//a method for generating a new arrival scenario from a random seed
	//a solution implementation method (generate yard queues from an arrival order and yard policy string (sol)
	
	boolean useRuleBasedYardPolicy;
	
	double[][] rectangleDistribution;
	double maxVWidth;
	double maxVLength;
	int[] vWidthIndOrd;
	int[] vLengthIndOrd;
	int[] vTypesDecreasingWidth;
	int[] vTypesDecreasingLength;
	double[] vAreas;
	
	Yard(int numberOfYardLanes, double yardLaneMaxWidth, double yardLaneMaxLength, double[][] rectangleDistribution, double totalRectangleArea, BasicPalette BP, double onePixel, Random randGen, boolean resetBinNumbers, boolean nonConstrainedLoading, boolean useRuleBasedYardPolicy, boolean bestKnownSolution, int[] arrivingVehicles){
		this.rectangleDistribution=rectangleDistribution;
		vTypes=rectangleDistribution[0].length;
		
		//width and length index orders
		vAreas=new double[vTypes];
		double[] vWidths=new double[vTypes];
		double[] vLengths=new double[vTypes];
		for(int v=0;v<vTypes;v++){//add negligible contributions to break ties using the other dimension
			vWidths[v]=rectangleDistribution[1][v]+(0.000000000000001*rectangleDistribution[0][v]);
			vLengths[v]=rectangleDistribution[0][v]+(0.000000000000001*rectangleDistribution[1][v]);
			//
			vAreas[v]=vWidths[v]*vLengths[v];
		}
		vWidthIndOrd=maths.Sort(vWidths, true);
		vLengthIndOrd=maths.Sort(vLengths, true);
		
		vTypesDecreasingWidth=new int[vTypes];
		vTypesDecreasingLength=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			vTypesDecreasingWidth[vWidthIndOrd[v]]=v;
			vTypesDecreasingLength[vLengthIndOrd[v]]=v;
		}
		
		rectTypes=new Rectangle[vTypes];
		for(int v=0;v<vTypes;v++){
			rectTypes[v]=new Rectangle(rectangleDistribution[0][v], rectangleDistribution[1][v], rectangleDistribution[2][v], BP.palette[v], v);//, onePixel
		}
		
		
		computeMaxDimensionedVehicle();
		this.useRuleBasedYardPolicy=useRuleBasedYardPolicy;
		
		this.nonConstrainedLoading=nonConstrainedLoading;
		this.resetBinNumbers=resetBinNumbers;
		this.numberOfYardLanes=numberOfYardLanes;
		
		
		Queue.vTypes=vTypes;
		
		yardLanes=new Queue[numberOfYardLanes];
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i]=new Queue(yardLaneMaxLength, yardLaneMaxWidth, i);
		}
		//rectangle distribution has l,w,h,probability rows
		ArrayList<Rectangle> rectangleAL=new ArrayList<Rectangle>(10);
		double areaOfRectanglesGenerated=0;
		double[] cumulativeRectangleDistribution=new double[rectangleDistribution[0].length];
		
		double sumP=0;
		for(int i=0;i<rectangleDistribution[0].length;i++){
			sumP+=rectangleDistribution[3][i];
			//cumulativeRectangleDistribution[i]=sumP;
		}
		double sumP2=0;
		for(int i=0;i<rectangleDistribution[0].length;i++){
			sumP2+=rectangleDistribution[3][i];
			cumulativeRectangleDistribution[i]=sumP2/sumP;
		}
		vCountByType=new int[rectangleDistribution[0].length];
		//bestKnownSolution
		if(bestKnownSolution){
			//read the best known solution file
			for(int i=0;i<arrivingVehicles.length;i++){
				for(int j=0;j<arrivingVehicles[i];j++){
					vCountByType[i]++;
					Rectangle newRectangle=new Rectangle(rectangleDistribution[0][i], rectangleDistribution[1][i], rectangleDistribution[2][i], BP.palette[i], i);//, onePixel
					areaOfRectanglesGenerated+=newRectangle.area;
					//
					rectangleAL.add(newRectangle);
				}
			}
		}else{
			//int[] v_counts=new int[13];
			while(areaOfRectanglesGenerated<totalRectangleArea){
				double randNum=randGen.nextDouble();
				int ind=0;
				while(cumulativeRectangleDistribution[ind]<randNum){
					ind++;
				}
				//
				vCountByType[ind]++;
				Rectangle newRectangle=new Rectangle(rectangleDistribution[0][ind], rectangleDistribution[1][ind], rectangleDistribution[2][ind], BP.palette[ind], ind);//, onePixel
				areaOfRectanglesGenerated+=newRectangle.area;
				//
				rectangleAL.add(newRectangle);
			}
		}
		
		
		totalAreaOfRectangles=areaOfRectanglesGenerated;
		/*for(int i=0;i<5;i++){
			System.out.println(v_counts[i]);
		}*/
		
		totalRectangles=rectangleAL.size();
		allRectangles=new Rectangle[rectangleAL.size()];
		//
		for(int i=0;i<rectangleAL.size();i++){
			allRectangles[i]=rectangleAL.get(i);
		}
		
		//generate width and length order linked lists of all rectangles
		for(int i=0;i<allRectangles.length;i++){
			addRectByWidthYardPolicyCreation(allRectangles[i], true);
		}
		//by length
		for(int i=0;i<allRectangles.length;i++){
			addRectByLengthYardPolicyCreation(allRectangles[i], true);
		}
		initialLengthPolicyCreation=totalRectangles;
		
		//the linked lists of rectangles are used for the translation
		//of a yard policy string into a set of yard queues for a given vehicle arrival scenario
		
		
		//generate an initial YardSolution
		//optionally generate a yard policy based on an orderly unbiased rule of thumb
		//such as an even distribution of quantiles, with have width and half length orientated
		//random initial solution
		sol=new YardSolution(randGen);
		//second solution copy (selection and crossover performed on these)
		sol2=new YardSolution(randGen);
		//
		
		//implement yard policy solution
		if(useRuleBasedYardPolicy){
			implementEvenDistributionSolution();
		}else{
			implementSolution();
		}
		//implementSolution();
		
		//arrival scenario
		//generateArrivalScenario(arrivalScenarioRandomSeed);
		//generateYardQueues(arrivalScenarioRandomSeed);
		
		/*
		
		//reset list
		head=null;
		tail=null;
		length=0;
		//generate uniform random arrival times
		for(int i=0;i<allRectangles.length;i++){
			allRectangles[i].randArrivalTime=randGen.nextDouble();
			addRectByArrivalTime(allRectangles[i]);
		}
		//go through the arrivals and use a yard lane allocation policy
		//first policy=first fit
		Rectangle currentRectangle=head;
		while(currentRectangle!=null){
			
			if(currentRectangle.type==4){
				System.out.println();
			}
			
			boolean laneFound=false;
			//add details/conditions here to implement other yard lane allocation policies
			for(int i=0;i<numberOfYardLanes && !laneFound;i++){
				if(yardLanes[i].addRectangleToQueue(currentRectangle)){
					laneFound=true;
				}
			}
			currentRectangle=currentRectangle.nextRect;
		}
		//
		resetQueues();*/
	}
	
	Yard(int numberOfYardLanes, double yardLaneMaxWidth, double yardLaneMaxLength, double[][] rectangleDistribution, double totalRectangleArea, BasicPalette BP, double onePixel, Random randGen, boolean resetBinNumbers, boolean nonConstrainedLoading, boolean useRuleBasedYardPolicy, int[] vCounts){
		this.rectangleDistribution=rectangleDistribution;
		vTypes=rectangleDistribution[0].length;
		
		//width and length index orders
		vAreas=new double[vTypes];
		double[] vWidths=new double[vTypes];
		double[] vLengths=new double[vTypes];
		for(int v=0;v<vTypes;v++){
			vWidths[v]=rectangleDistribution[1][v];
			vLengths[v]=rectangleDistribution[0][v];
			//
			vAreas[v]=vWidths[v]*vLengths[v];
		}
		vWidthIndOrd=maths.Sort(vWidths, true);
		vLengthIndOrd=maths.Sort(vLengths, true);
		
		vTypesDecreasingWidth=new int[vTypes];
		vTypesDecreasingLength=new int[vTypes];
		for(int v=0;v<vTypes;v++){
			vTypesDecreasingWidth[vWidthIndOrd[v]]=v;
			vTypesDecreasingLength[vLengthIndOrd[v]]=v;
		}
		
		rectTypes=new Rectangle[vTypes];
		for(int v=0;v<vTypes;v++){
			rectTypes[v]=new Rectangle(rectangleDistribution[0][v], rectangleDistribution[1][v], rectangleDistribution[2][v], BP.palette[v], v);//, onePixel
		}
		
		computeMaxDimensionedVehicle();
		this.useRuleBasedYardPolicy=useRuleBasedYardPolicy;
		
		this.nonConstrainedLoading=nonConstrainedLoading;
		this.resetBinNumbers=resetBinNumbers;
		this.numberOfYardLanes=numberOfYardLanes;
		
		
		Queue.vTypes=vTypes;
		
		yardLanes=new Queue[numberOfYardLanes];
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i]=new Queue(yardLaneMaxLength, yardLaneMaxWidth, i);
		}
		//rectangle distribution has l,w,h,probability rows
		ArrayList<Rectangle> rectangleAL=new ArrayList<Rectangle>(10);
		double areaOfRectanglesGenerated=0;
		double[] cumulativeRectangleDistribution=new double[rectangleDistribution[0].length];
		
		double sumP=0;
		for(int i=0;i<rectangleDistribution[0].length;i++){
			sumP+=rectangleDistribution[3][i];
			//cumulativeRectangleDistribution[i]=sumP;
		}
		double sumP2=0;
		for(int i=0;i<rectangleDistribution[0].length;i++){
			sumP2+=rectangleDistribution[3][i];
			cumulativeRectangleDistribution[i]=sumP2/sumP;
		}
		vCountByType=new int[rectangleDistribution[0].length];
		//int[] v_counts=new int[13];
		
		for(int i=0;i<vCounts.length;i++){
			for(int j=0;j<vCounts[i];j++){
				vCountByType[i]++;
				Rectangle newRectangle=new Rectangle(rectangleDistribution[0][i], rectangleDistribution[1][i], rectangleDistribution[2][i], BP.palette[i], i);//, onePixel
				areaOfRectanglesGenerated+=newRectangle.area;
				//
				rectangleAL.add(newRectangle);
			}
		}
		
		totalAreaOfRectangles=areaOfRectanglesGenerated;
		/*for(int i=0;i<5;i++){
			System.out.println(v_counts[i]);
		}*/
		
		totalRectangles=rectangleAL.size();
		allRectangles=new Rectangle[rectangleAL.size()];
		//
		for(int i=0;i<rectangleAL.size();i++){
			allRectangles[i]=rectangleAL.get(i);
		}
		
		//generate width and length order linked lists of all rectangles
		for(int i=0;i<allRectangles.length;i++){
			addRectByWidthYardPolicyCreation(allRectangles[i], true);
		}
		//by length
		for(int i=0;i<allRectangles.length;i++){
			addRectByLengthYardPolicyCreation(allRectangles[i], true);
		}
		initialLengthPolicyCreation=totalRectangles;
		
		//the linked lists of rectangles are used for the translation
		//of a yard policy string into a set of yard queues for a given vehicle arrival scenario
		
		
		//generate an initial YardSolution
		//optionally generate a yard policy based on an orderly unbiased rule of thumb
		//such as an even distribution of quantiles, with have width and half length orientated
		//random initial solution
		sol=new YardSolution(randGen);
		//second solution copy (selection and crossover performed on these)
		sol2=new YardSolution(randGen);
		//
		
		//implement yard policy solution
		if(useRuleBasedYardPolicy){
			implementEvenDistributionSolution();
		}else{
			implementSolution();
		}
		//implementSolution();
		
		//arrival scenario
		//generateArrivalScenario(arrivalScenarioRandomSeed);
		//generateYardQueues(arrivalScenarioRandomSeed);
		
		/*
		
		//reset list
		head=null;
		tail=null;
		length=0;
		//generate uniform random arrival times
		for(int i=0;i<allRectangles.length;i++){
			allRectangles[i].randArrivalTime=randGen.nextDouble();
			addRectByArrivalTime(allRectangles[i]);
		}
		//go through the arrivals and use a yard lane allocation policy
		//first policy=first fit
		Rectangle currentRectangle=head;
		while(currentRectangle!=null){
			
			if(currentRectangle.type==4){
				System.out.println();
			}
			
			boolean laneFound=false;
			//add details/conditions here to implement other yard lane allocation policies
			for(int i=0;i<numberOfYardLanes && !laneFound;i++){
				if(yardLanes[i].addRectangleToQueue(currentRectangle)){
					laneFound=true;
				}
			}
			currentRectangle=currentRectangle.nextRect;
		}
		//
		resetQueues();*/
	}
	
	Yard(Yard y){//, int arrivalScenarioRandomSeedproduce identical copies of the holding area for each container/solution
		//allRectangles is not needed in this case:
		/*allRectangles=new Rectangle[y.allRectangles.length];
		//* 
		for(int i=0;i<allRectangles.length;i++){
			allRectangles[i]=new Rectangle(y.allRectangles[i]);
		}*/
		this.rectangleDistribution=y.rectangleDistribution;
		//
		this.vWidthIndOrd=y.vWidthIndOrd;
		this.vLengthIndOrd=y.vLengthIndOrd;
		
		this.vTypesDecreasingLength=y.vTypesDecreasingLength;
		this.vTypesDecreasingWidth=y.vTypesDecreasingWidth;
		
		this.vAreas=y.vAreas;//
		
		this.rectTypes=y.rectTypes;
		
		computeMaxDimensionedVehicle();
		vTypes=rectangleDistribution[0].length;
		//
		nonConstrainedLoading=y.nonConstrainedLoading;
		resetBinNumbers=y.resetBinNumbers;
		//just copy the queues and their rectangles (and their queue references) and their lengths (plus any other details that are required--just to be sure)
		totalRectangles=y.totalRectangles;
		numberOfYardLanes=y.numberOfYardLanes;
		yardLanes=new Queue[numberOfYardLanes];
		
		Queue.vTypes=vTypes;
		
		//copy the rectangles (to allow the generation of the ordered lists of rectangles below) and the yard policy, implement the yard policy
		allRectangles=new Rectangle[y.allRectangles.length];
		for(int i=0;i<y.allRectangles.length;i++){
			Rectangle newRectangle=new Rectangle(y.allRectangles[i]);
			allRectangles[i]=newRectangle;
		}
		
		this.totalAreaOfRectangles=y.totalAreaOfRectangles;
		vCountByType=new int[y.vCountByType.length];
		for(int i=0;i<vCountByType.length;i++){
			vCountByType[i]=y.vCountByType[i];
		}
		//copy yard policy solution
		sol=new YardSolution(y.sol);
		sol2=new YardSolution(y.sol2);
		
		//generate queue objects ready to implement yard policy
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i]=new Queue(y.yardLanes[i]);
		}
		
		//generate width and length order linked lists of all rectangles
		for(int i=0;i<allRectangles.length;i++){
			addRectByWidthYardPolicyCreation(allRectangles[i], true);
		}
		//by length
		for(int i=0;i<allRectangles.length;i++){
			addRectByLengthYardPolicyCreation(allRectangles[i], true);
		}
		initialLengthPolicyCreation=y.initialLengthPolicyCreation;
		
		//implement yard policy solution
		implementSolution();
		
		//arrival scenario
		//generateArrivalScenario(arrivalScenarioRandomSeed);
		//generateYardQueues(arrivalScenarioRandomSeed);
		
		
		//resetQueues();
		//printQueues();
	}
	
	Yard(double[][] rectangleDistribution, double totalRectangleArea, BasicPalette BP, Random randGen){
		/*this.nonConstrainedLoading=nonConstrainedLoading;
		this.resetBinNumbers=resetBinNumbers;
		this.numberOfYardLanes=numberOfYardLanes;
		yardLanes=new Queue[numberOfYardLanes];
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i]=new Queue(yardLaneMaxLength, yardLaneMaxWidth);
		}*/
		this.rectangleDistribution=rectangleDistribution;
		computeMaxDimensionedVehicle();
		//
		//rectangle distribution has l,w,h,probability rows
		ArrayList<Rectangle> rectangleAL=new ArrayList<Rectangle>(10);
		double areaOfRectanglesGenerated=0;
		double[] cumulativeRectangleDistribution=new double[rectangleDistribution[0].length];
		double sumP=0;
		for(int i=0;i<rectangleDistribution[0].length;i++){
			sumP+=rectangleDistribution[3][i];
			//cumulativeRectangleDistribution[i]=sumP;
		}
		double sumP2=0;
		for(int i=0;i<rectangleDistribution[0].length;i++){
			sumP2+=rectangleDistribution[3][i];
			cumulativeRectangleDistribution[i]=sumP2/sumP;
		}
		vCountByType=new int[rectangleDistribution[0].length];
		vTypes=rectangleDistribution[0].length;
		Queue.vTypes=vTypes;
		//int[] v_counts=new int[13];
		while(areaOfRectanglesGenerated<totalRectangleArea){
			double randNum=randGen.nextDouble();
			int ind=0;
			while(cumulativeRectangleDistribution[ind]<randNum){
				ind++;
			}
			//
			vCountByType[ind]++;
			Rectangle newRectangle=new Rectangle(rectangleDistribution[0][ind], rectangleDistribution[1][ind], rectangleDistribution[2][ind], BP.palette[ind], ind);//, onePixel
			areaOfRectanglesGenerated+=newRectangle.area;
			//
			rectangleAL.add(newRectangle);
		}
		totalAreaOfRectangles=areaOfRectanglesGenerated;
		//
		totalRectangles=rectangleAL.size();
		allRectangles=new Rectangle[rectangleAL.size()];
		//
		for(int i=0;i<rectangleAL.size();i++){
			allRectangles[i]=rectangleAL.get(i);
		}
	}
	
	void computeMaxDimensionedVehicle(){
		for(int v=0;v<rectangleDistribution[0].length;v++){
			if(rectangleDistribution[0][v]>maxVLength){
				maxVLength=rectangleDistribution[0][v];
			}
			if(rectangleDistribution[1][v]>maxVWidth){
				maxVWidth=rectangleDistribution[1][v];
			}
		}
	}
	
	//methods
	//get quantile dimension
	/*aCounter++;
	System.out.println(aCounter);
	if(aCounter==50){
		System.out.println(aCounter);
	}*/
	double getQuantileDimension(boolean forNewRow, double remainingLength, double remainingWidth, double quantile){
		double maxPerpendicularDimension=0;
		double maxSumOfDimensions=0;
		if(forNewRow){
			maxSumOfDimensions=remainingWidth;
			maxPerpendicularDimension=remainingLength;
		}else{
			maxSumOfDimensions=remainingLength;
			maxPerpendicularDimension=remainingWidth;
		}
		double quantileDimension=-1;//quantileDimension=minMaxDim;
		//quantile 0
		double minMaxDim=minMaxDimensionOfIncludibleRectangle(forNewRow, maxSumOfDimensions, maxPerpendicularDimension);
		//set of includable rectangles
		setOfMaxDimRectangles(forNewRow, minMaxDim, maxSumOfDimensions);//Rectangle[] includableHeadTail=
		//find the first rectangle in the list with "minMaxDim"
		if(head!=null){
			/*boolean minMaxIndFound=false;
			Rectangle quantileZeroRect=head;
			int countOfRectBeforeQuantileZero=0;
			while(!minMaxIndFound && quantileZeroRect!=null){
				if(forNewRow){
					if(Math.abs(quantileZeroRect.l-minMaxDim)<0.000001){
						minMaxIndFound=true;
					}else{
						quantileZeroRect=quantileZeroRect.nextRect;
						countOfRectBeforeQuantileZero++;
					}
				}else{
					if(Math.abs(quantileZeroRect.w-minMaxDim)<0.000001){
						minMaxIndFound=true;
					}else{
						quantileZeroRect=quantileZeroRect.nextRect;
						countOfRectBeforeQuantileZero++;
					}
				}
			}*/
			//get quantile dimension
			int remainingListLength=length;//-countOfRectBeforeQuantileZero
			int quantileIndex=(int)((remainingListLength-1)*quantile);
			Rectangle currentRect=head;
			int count=0;
			while(count<quantileIndex){
				currentRect=currentRect.nextRect;
				count++;
			}
			//
			if(forNewRow){
				quantileDimension=currentRect.l;
			}else{
				quantileDimension=currentRect.w;
			}
		}else{
			quantileDimension=minMaxDim;
			//System.out.println();
		}
		
		return quantileDimension;
	}
	//minimum maximum dimension of vehicle that can be included in the next row or column
	double minMaxDimensionOfIncludibleRectangle(boolean forNewRow, double maxSumOfDimensions, double maxPerpendicularDimension){
		double sumOfDimensions=0;
		double minMaxDimension=0;
		//reset current rectangle
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i].resetCurrentRectangleToFront();
		}
		//
		//this needs checking for the case of one yard lane
		//
		boolean minMaxDimensionIdentified=(remainingRectangles==0);
		while(!minMaxDimensionIdentified){
			//find the queue with the minimum dimension rectangle
			double minimumDimensionRectangleAtFrontOfQueue=Double.MAX_VALUE;
			double summedDimensionOfMinDimRect=0;
			int minDimQueueHead=-1;
			for(int i=0;i<numberOfYardLanes;i++){
				Rectangle currentRect=yardLanes[i].currentRect;
				if(currentRect!=null){
					double nextQueueRectangleSummedDimension=0;//yardLanes[i].currentRectangleDimension(forNewRow);
					double nextQueueRectangleOtherDimension=0;//yardLanes[i].currentRectangleDimension(!forNewRow);
					if(forNewRow){
						nextQueueRectangleSummedDimension=currentRect.w;
						nextQueueRectangleOtherDimension=currentRect.l;
					}else{
						nextQueueRectangleSummedDimension=currentRect.l;
						nextQueueRectangleOtherDimension=currentRect.w;
					}
					//
					if(nextQueueRectangleOtherDimension<minimumDimensionRectangleAtFrontOfQueue && nextQueueRectangleSummedDimension+sumOfDimensions<=maxSumOfDimensions && nextQueueRectangleOtherDimension<=maxPerpendicularDimension){
						minimumDimensionRectangleAtFrontOfQueue=nextQueueRectangleOtherDimension;
						summedDimensionOfMinDimRect=nextQueueRectangleSummedDimension;
						minDimQueueHead=i;
					}
				}
			}
			//
			if(minDimQueueHead==-1){
				minMaxDimensionIdentified=true;
			}else{
				//nextQueueRectangleOtherDimension+sumOfDimensions<=maxSumOfDimensions
				sumOfDimensions+=summedDimensionOfMinDimRect;//minimumDimensionRectangleAtFrontOfQueue;//otherDimensionOfMinDimRect;
				minMaxDimension=Math.max(minMaxDimension, minimumDimensionRectangleAtFrontOfQueue);
				yardLanes[minDimQueueHead].nextRectangle();
			}
		}
		return minMaxDimension;
	}
	
	//set of includible vehicles (this method should solve the subproblem 
	//IP method (this method gives the set of rectangles that will be included in the IP
	//Heuristic: this method will be used in conjunction with the minMax method to convert the quantile to a maximal dimension for the next strip that will be generated
	//not sure the return values will be used as head-tail capture the set of rectangles and "currentRect.lastIncludableRectangleInQueue=false" marks the last one in each queue
	void setOfMaxDimRectangles(boolean forNewRow, double minMaxDimension, double maxSumOfDimensions){//Rectangle[]
		//int[] numberOfIncludableRectanglesPerQueue=new int[numberOfYardLanes];
		//double sumOfDimensions=0;
		//reset current rectangle
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i].resetCurrentRectangleToFront();
		}
		//reset list of rectangles
		head=null;
		tail=null;
		length=0;
		//
		for(int i=0;i<numberOfYardLanes;i++){
			Rectangle currentRect=yardLanes[i].currentRect;
			if(currentRect!=null){
				currentRect.lastIncludableRectangleInQueue=true;
				boolean rectanglesGatheredFromQueue=false;
				double sumOfDimensionsInQueue=0;
				while(!rectanglesGatheredFromQueue && currentRect!=null){
					double summedDimension=0;
					double perpendicularDimension=0;
					if(forNewRow){
						summedDimension=currentRect.w;
						perpendicularDimension=currentRect.l;
					}else{
						summedDimension=currentRect.l;
						perpendicularDimension=currentRect.w;
					}
					//
					
					//if(sumOfDimensionsInQueue+summedDimension<=maxSumOfDimensions && perpendicularDimension<=minMaxDimension){
					if(sumOfDimensionsInQueue+summedDimension<=maxSumOfDimensions && perpendicularDimension>=minMaxDimension){//maximum minimum deimension?
						sumOfDimensionsInQueue+=summedDimension;
						//
						//lastIncludableRectangleInEachQueue[i]=currentRect;
						currentRect.lastIncludableRectangleInQueue=false;
						if(forNewRow){
							addRectByLength(currentRect);
						}else{
							addRectByWidth(currentRect);
						}
						//
						currentRect=currentRect.nextRectInQueue;
						//yardLanes[i].nextRectangle();
					}else{
						rectanglesGatheredFromQueue=true;
					}
				}
			}
			
		}
		//
		//return lastIncludableRectangleInEachQueue;
	}
	
	//this method is copied from the "HoldingArea" object
	//in a "Yard" context this method is a very heuristic method of selecting a subset
	//of rectangles to fill a strip.
	//Sequentially select rectangles, from the fronts of queues, that are as close as possible to the target (max)width/length
	//, double quantileCentre. The name get quantile is not appropriate because the "quantile" aspect has already
	//been used 
	Rectangle[] getRectanglesForStrip(boolean forNewRow, double maxLength, double maxWidth){
		Rectangle[] stripHeadTail=new Rectangle[2];
		//reset current rectangles to the queue fronts
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i].resetCurrentRectangleToFront();
		}
		//
		double sumOfDimensions=0;
		//first one then loop
		double minDiff=Double.MAX_VALUE;
		int queueNumberOfBestFitRect=-1;
		//
		for(int i=0;i<numberOfYardLanes;i++){
			Rectangle frontRect=yardLanes[i].rectAtFront;
			if(frontRect!=null){
				if(forNewRow){
					if(sumOfDimensions+frontRect.w<=maxWidth && frontRect.l<=maxLength){
						double newDiff=maxLength-frontRect.l;
						if(newDiff<minDiff){
							minDiff=newDiff;
							queueNumberOfBestFitRect=i;
						}
					}
				}else{
					if(sumOfDimensions+frontRect.l<=maxLength && frontRect.w<=maxWidth){
						double newDiff=maxWidth-frontRect.w;
						if(newDiff<minDiff){
							minDiff=newDiff;
							queueNumberOfBestFitRect=i;
						}
					}
				}
			}
			
		}
		//
		if(queueNumberOfBestFitRect!=-1){
			Rectangle newRect=yardLanes[queueNumberOfBestFitRect].popQueue();
			stripHeadTail[0]=newRect;//returns the front of queue and reset the front of the queue to the next rectangle in the queue
			stripHeadTail[1]=stripHeadTail[0];
			newRect.nextRect=null;
			//
			if(forNewRow){
				sumOfDimensions+=newRect.w;
			}else{
				sumOfDimensions+=newRect.l;
			}
			//
			boolean anotherRectangleMightFitInTheStrip=true;
			while(anotherRectangleMightFitInTheStrip){
				minDiff=Double.MAX_VALUE;
				queueNumberOfBestFitRect=-1;
				//
				for(int i=0;i<numberOfYardLanes;i++){
					Rectangle frontRect=yardLanes[i].rectAtFront;
					if(frontRect!=null){
						if(forNewRow){
							if(sumOfDimensions+frontRect.w<=maxWidth && frontRect.l<=maxLength){
								double newDiff=maxLength-frontRect.l;
								if(newDiff<minDiff){
									minDiff=newDiff;
									queueNumberOfBestFitRect=i;
								}
							}
						}else{
							if(sumOfDimensions+frontRect.l<=maxLength && frontRect.w<=maxWidth){
								double newDiff=maxWidth-frontRect.w;
								if(newDiff<minDiff){
									minDiff=newDiff;
									queueNumberOfBestFitRect=i;
								}
							}
						}
					}
					
				}
				//
				if(queueNumberOfBestFitRect!=-1){
					newRect=yardLanes[queueNumberOfBestFitRect].popQueue();
					//
					newRect.prevRect=stripHeadTail[1];//returns the front of queue and reset the front of the queue to the next rectangle in the queue
					stripHeadTail[1].nextRect=newRect;
					stripHeadTail[1]=newRect;
					newRect.nextRect=null;
					//
					if(forNewRow){
						sumOfDimensions+=newRect.w;
					}else{
						sumOfDimensions+=newRect.l;
					}
				}else{
					anotherRectangleMightFitInTheStrip=false;
				}
			}
		}
		//
		return stripHeadTail;
	}
	
	Rectangle getLargestRectangleAtFrontOfAQueue(){
		Rectangle retRect=null;
		
		return retRect;
	}
	
	
	
	//implement yard policy (generates yard queues by following the yard policy
	//that is associated with this yard
	void implementSolution(){
		//reset();//just in time
		//read the solution and implement its instructions (as closely as possible)
		Gene currentGene=sol.head;
		int yardLanrNumber=0;
		while(currentGene!=null){
			int value=currentGene.value;
			int quantileIndex=Math.floorMod(value, numberOfYardQuantiles);
			int orientation=(int)Math.floor((double)value/numberOfYardQuantiles);
			if(orientation==0){
				yardLanes[yardLanrNumber].widthIsTargetDimension=true;
				int vehicleWidthIndex=(int)((totalRectangles-1)*setOfQuantiles[quantileIndex]);
				//get the width of this vehicle in the linked list of vehicles ordered by width
				yardLanes[yardLanrNumber].targetDimensionLength=rectangleDistribution[1][quantileIndex];//getRectangleWidthYardPolicyCreation(vehicleWidthIndex);
				
			}else{
				yardLanes[yardLanrNumber].widthIsTargetDimension=false;
				int vehicleLengthIndex=(int)((totalRectangles-1)*setOfQuantiles[quantileIndex]);
				//get the width of this vehicle in the linked list of vehicles ordered by length
				//System.out.println("vehicleLengthIndex="+vehicleLengthIndex+", totalRectangles="+totalRectangles+", quantileIndex="+quantileIndex+", setOfQuantiles[quantileIndex]="+setOfQuantiles[quantileIndex]);
				yardLanes[yardLanrNumber].targetDimensionLength=rectangleDistribution[0][quantileIndex];//getRectangleLengthYardPolicyCreation(vehicleLengthIndex);
			}
			//
			currentGene=currentGene.nextGene;
			yardLanrNumber++;
		}
		//evaluate requires set of arrival scenarios to be simulated and packed
		//so policy solution evaluation is significantly more computationally demanding than that of one packing solution)
	}
	
	void implementEvenDistributionSolution(){
		//reset();//just in time
		//read the solution and implement its instructions (as closely as possible)
		Gene currentGene=sol.head;
		int yardLanrNumber=0;
		while(currentGene!=null){
			int value=(currentGene.value=(int)Math.round(((double)yardLanrNumber/YardSolution.maxSolutionLength)*(YardSolution.alphabetLength-1)));
			int quantileIndex=Math.floorMod(value, numberOfYardQuantiles);
			int orientation=(int)Math.floor((double)value/numberOfYardQuantiles);
			if(orientation==0){
				yardLanes[yardLanrNumber].widthIsTargetDimension=true;
				int vehicleWidthIndex=(int)((totalRectangles-1)*setOfQuantiles[quantileIndex]);
				//get the width of this vehicle in the linked list of vehicles ordered by width
				yardLanes[yardLanrNumber].targetDimensionLength=getRectangleWidthYardPolicyCreation(vehicleWidthIndex);
			}else{
				yardLanes[yardLanrNumber].widthIsTargetDimension=false;
				int vehicleLengthIndex=(int)((totalRectangles-1)*setOfQuantiles[quantileIndex]);
				//get the width of this vehicle in the linked list of vehicles ordered by length
				//System.out.println("vehicleLengthIndex="+vehicleLengthIndex+", totalRectangles="+totalRectangles+", quantileIndex="+quantileIndex+", setOfQuantiles[quantileIndex]="+setOfQuantiles[quantileIndex]);
				yardLanes[yardLanrNumber].targetDimensionLength=getRectangleLengthYardPolicyCreation(vehicleLengthIndex);
			}
			//
			currentGene=currentGene.nextGene;
			yardLanrNumber++;
		}
		//evaluate requires set of arrival scenarios to be simulated and packed
		//so policy solution evaluation is significantly more computationally demanding than that of one packing solution)
	}
	
	//generate yard queues. This method will be called in an inner loop after the implementSolution method above
	//Yard policy types: 0 target dimensions, 1 strip numbers, 2 vehicle types
	
	
	//, boolean moduloScenarios, double moduloFraction
	void generateYardQueuesMajorFeasibilityCheck(int randomSeed, boolean breakTiesWithLeastFullLane, int[] vMix){//use yard policy
		
		//n will not be null in this method
		//and yard policy 0 will always be used
		
		
		//reset queues
		clearQueues();
		//generate the rectangle arrival order for the given scenario/random seed
		//if(moduloScenarios){
			//generateArrivalScenario(randomSeed, moduloFraction);
		//}else{
		
		generateArrivalScenario(randomSeed);
		
		//}

		
		//cycle through rectangles in the arrival order and implement a closest fit/minimum length policy
		Rectangle currentRectangle=head;
		while(currentRectangle!=null){
			//find the closest fit lanes breaking ties by (first found/least full)
			double minDiff=Double.MAX_VALUE;
			int bestYardLane=-1;
			double bestQueueLength=Double.MAX_VALUE;
			if(!breakTiesWithLeastFullLane){
				bestQueueLength=-Double.MAX_VALUE;
			}
			
			double bestLaneScore=Double.MAX_VALUE;
			
			for(int i=0;i<numberOfYardLanes;i++){
				
				
				//firstly check if the rectangle fits into the yard lane
				if(currentRectangle.w<=yardLanes[i].maxWidth && yardLanes[i].lengthOfQueue+currentRectangle.l<=yardLanes[i].maxLength){
					
					double laneScore=0;
					//calculate a measure of the overall vehicle type purity of the lanes
					//for each lane (given that the vehicle is added to this lane
					if(yardLanes[i].vCounts[currentRectangle.type]==0){
						//laneScore+=(Math.max(0, yardLanes[i].numberOfVehicleTypesInQueue+1-1))/vTypes;
						//maximise the concentration of the vehicle type in the queue that it is added to
						//
						//plus a minimum value so that new lanes are not started unnecessarily
						laneScore=0.1*(Double.MIN_VALUE+1-(((yardLanes[i].vCounts[currentRectangle.type]+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
					}else{
						laneScore=0.1*(1-(((yardLanes[i].vCounts[currentRectangle.type]+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
					}
					
					//
					if(yardLanes[i].widthIsTargetDimension){
						laneScore+=(Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.w))/maxVWidth;
					}else{
						laneScore+=(Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.l))/maxVLength;
					}
					
					//compare the lane score to the current best
					if(laneScore<bestLaneScore){
						bestLaneScore=laneScore;
						bestYardLane=i;
						bestQueueLength=yardLanes[i].lengthOfQueue;
					}else if(laneScore==bestLaneScore){
						if(breakTiesWithLeastFullLane){
							if(yardLanes[i].lengthOfQueue<bestQueueLength){
								bestQueueLength=yardLanes[i].lengthOfQueue;
								bestYardLane=i;
							}
						}else{
							if(yardLanes[i].lengthOfQueue>bestQueueLength){
								//minDiff=diff;//has not changed, is the same
								bestQueueLength=yardLanes[i].lengthOfQueue;
								bestYardLane=i;
							}
						}
					}

				}
				
				
			}
			//add the rectangle into the closest fitting lane where ties were broken by choosing the least full lane
			yardLanes[bestYardLane].addRectangleToQueue(currentRectangle);

			currentRectangle=currentRectangle.nextRect;
			
		}
		//
		resetQueues();
	}
	
	
	
	
	
	
	//in the following scenarios are generated from a single random seed
	//as opposed to storing a set of yard queues, this is more convenient
	//as the yard policy will keep changing anyway and the arrival order will be different in each scenario
	
	//generate yard queues. This method will be called in an inner loop after the implementSolution method above
	//Yard policy types: 0 target dimensions, 1 strip numbers, 2 vehicle types
	void generateYardQueues(int randomSeed, boolean breakTiesWithLeastFullLane, boolean moduloScenarios, double moduloFraction, int yardPolicyType, int[][] n){//use yard policy
		//reset queues
		clearQueues();
		//generate the rectangle arrival order for the given scenario/random seed
		if(moduloScenarios){
			generateArrivalScenario(randomSeed, moduloFraction);
		}else{
			generateArrivalScenario(randomSeed);
		}
		//Yard policy types 
		//It would be good to have the option of using a yard policy that is designed to protect a
		//planned packing solution. However this only makes sense online.
		//So include n as an input argument but it will only be used if it is not null (which will be the case online
		//Let this approach be "yardPolicyType=1"
		
		//Another yard policy type is the same as that from the loading simulator implementation
		//i.e. take the number of different vehicle types into account when breaking ties
		//Let this approach be "yardPolicyType=2"
		//This yard policy is slight tweak of the default lane allocation policy
		
		
		
		if(yardPolicyType==0){// || (yardPolicyType==1 && n==null)
			//cycle through rectangles in the arrival order and implement a closest fit/minimum length policy
			Rectangle currentRectangle=head;
			while(currentRectangle!=null){
				//find the closest fit lanes breaking ties by (first found/least full)
				double minDiff=Double.MAX_VALUE;
				int bestYardLane=-1;
				double bestQueueLength=Double.MAX_VALUE;
				if(!breakTiesWithLeastFullLane){
					bestQueueLength=-Double.MAX_VALUE;
				}
				
				double bestLaneScore=Double.MAX_VALUE;
				
				for(int i=0;i<numberOfYardLanes;i++){
					
					
					//firstly check if the rectangle fits into the yard lane
					if(currentRectangle.w<=yardLanes[i].maxWidth && yardLanes[i].lengthOfQueue+currentRectangle.l<=yardLanes[i].maxLength){
						
						double laneScore=0;
						//calculate a measure of the overall vehicle type purity of the lanes
						//for each lane (given that the vehicle is added to this lane
						if(yardLanes[i].vCounts[currentRectangle.type]==0){
							//laneScore+=(Math.max(0, yardLanes[i].numberOfVehicleTypesInQueue+1-1))/vTypes;
							//maximise the concentration of the vehicle type in the queue that it is added to
							//
							//plus a maximum value so that new lanes are not started unnecessarily
							laneScore=0.1*(Double.MAX_VALUE+1-(((yardLanes[i].vCounts[currentRectangle.type]+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
						}else{
							laneScore=0.1*(1-(((yardLanes[i].vCounts[currentRectangle.type]+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
						}
						
						//minimimise the variance
						
						//
						if(yardLanes[i].widthIsTargetDimension){
							laneScore+=(Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.w))/maxVWidth;
						}else{
							laneScore+=(Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.l))/maxVLength;
						}
						
						//compare the lane score to the current best
						if(laneScore<bestLaneScore){
							bestLaneScore=laneScore;
							bestYardLane=i;
							bestQueueLength=yardLanes[i].lengthOfQueue;
						}else if(laneScore==bestLaneScore){
							if(breakTiesWithLeastFullLane){
								if(yardLanes[i].lengthOfQueue<bestQueueLength){
									bestQueueLength=yardLanes[i].lengthOfQueue;
									bestYardLane=i;
								}
							}else{
								if(yardLanes[i].lengthOfQueue>bestQueueLength){
									//minDiff=diff;//has not changed, is the same
									bestQueueLength=yardLanes[i].lengthOfQueue;
									bestYardLane=i;
								}
							}
						}

					}
					
					
				}
				//add the rectangle into the closest fitting lane where ties were broken by choosing the least full lane
				yardLanes[bestYardLane].addRectangleToQueue(currentRectangle);
	
				currentRectangle=currentRectangle.nextRect;
			}
		}else if(yardPolicyType==2){
			//cycle through rectangles in the arrival order and implement a closest fit/minimum length policy
			Rectangle currentRectangle=head;
			while(currentRectangle!=null){
				//find the closest fit lanes breaking ties by (first found/least full)
				double minDiff=Double.MAX_VALUE;
				int bestYardLane=-1;
				double bestQueueLength=Double.MAX_VALUE;
				if(!breakTiesWithLeastFullLane){
					bestQueueLength=-Double.MAX_VALUE;
				}
				
				double bestLaneScore=Double.MAX_VALUE;
				
				for(int i=0;i<numberOfYardLanes;i++){
					
					
					
					
					//firstly check if the rectangle fits into the yard lane
					if(currentRectangle.w<=yardLanes[i].maxWidth && yardLanes[i].lengthOfQueue+currentRectangle.l<=yardLanes[i].maxLength){
						
						double laneScore=0;
						//calculate a measure of the overall vehicle type purity of the lanes
						//for each lane (given that the vehicle is added to this lane
						if(yardLanes[i].vCounts[currentRectangle.type]==0){
							//laneScore+=(Math.max(0, yardLanes[i].numberOfVehicleTypesInQueue+1-1))/vTypes;
							//maximise the concentration of the vehicle type in the queue that it is added to
							//
							//plus a minimum value so that new lanes are not started unnecessarily
							double dimensionSimilarityInLane=0;
							if(yardLanes[i].widthIsTargetDimension){
								for(int v=0;v<vTypes;v++){
									dimensionSimilarityInLane+=(yardLanes[i].vCounts[v]*(1-Math.pow(((rectangleDistribution[1][currentRectangle.type]-rectangleDistribution[1][v])/maxVWidth),2)));
								}
								laneScore=0.1*(Double.MIN_VALUE+1-(((dimensionSimilarityInLane+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
							}else{
								for(int v=0;v<vTypes;v++){
									dimensionSimilarityInLane+=(yardLanes[i].vCounts[v]*(1-Math.pow(((rectangleDistribution[0][currentRectangle.type]-rectangleDistribution[0][v])/maxVLength),2)));
								}
								laneScore=0.1*(Double.MIN_VALUE+1-(((dimensionSimilarityInLane+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
							}
							
							
							//laneScore=0.1*(Double.MIN_VALUE+1-(((yardLanes[i].vCounts[currentRectangle.type]+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
						}else{
							
							double dimensionSimilarityInLane=0;
							if(yardLanes[i].widthIsTargetDimension){
								for(int v=0;v<vTypes;v++){
									dimensionSimilarityInLane+=(yardLanes[i].vCounts[v]*(1-Math.pow(((rectangleDistribution[1][currentRectangle.type]-rectangleDistribution[1][v])/maxVWidth),2)));
								}
								laneScore=0.1*(Double.MIN_VALUE+1-(((dimensionSimilarityInLane+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
							}else{
								for(int v=0;v<vTypes;v++){
									dimensionSimilarityInLane+=(yardLanes[i].vCounts[v]*(1-Math.pow(((rectangleDistribution[0][currentRectangle.type]-rectangleDistribution[0][v])/maxVLength),2)));
								}
								laneScore=0.1*(1-(((dimensionSimilarityInLane+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
							}
							
							
							//laneScore=0.1*(1-(((yardLanes[i].vCounts[currentRectangle.type]+1)*rectangleDistribution[0][currentRectangle.type])/(yardLanes[i].lengthOfQueue+rectangleDistribution[0][currentRectangle.type])));
						}
						
						//minimimise the variance
						
						//
						if(yardLanes[i].widthIsTargetDimension){
							laneScore+=(Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.w))/maxVWidth;
						}else{
							laneScore+=(Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.l))/maxVLength;
						}
						
						//compare the lane score to the current best
						if(laneScore<bestLaneScore){
							bestLaneScore=laneScore;
							bestYardLane=i;
							bestQueueLength=yardLanes[i].lengthOfQueue;
						}else if(laneScore==bestLaneScore){
							if(breakTiesWithLeastFullLane){
								if(yardLanes[i].lengthOfQueue<bestQueueLength){
									bestQueueLength=yardLanes[i].lengthOfQueue;
									bestYardLane=i;
								}
							}else{
								if(yardLanes[i].lengthOfQueue>bestQueueLength){
									//minDiff=diff;//has not changed, is the same
									bestQueueLength=yardLanes[i].lengthOfQueue;
									bestYardLane=i;
								}
							}
						}

					}
					
					
				}
				//add the rectangle into the closest fitting lane where ties were broken by choosing the least full lane
				yardLanes[bestYardLane].addRectangleToQueue(currentRectangle);
	
				currentRectangle=currentRectangle.nextRect;
			}
		}else if(yardPolicyType==1 && n!=null){
			//reaching this part of the code implies that (n!=null)=true
			//System.out.println();
			//System.out.println();
			
			int[][] nCopy=new int[n.length][n[0].length];//it has a rectangular shape 
			int[][] nCountdownCopy=new int[n.length][n[0].length];//it has a rectangular shape 
			//int[][] nCountdownCopyCopy=new int[n.length][n[0].length];//
			boolean[] completedStrips=new boolean[n.length];
			for(int i=0;i<n.length;i++){
				for(int j=0;j<n[i].length;j++){
					nCopy[i][j]=n[i][j];
					nCountdownCopy[i][j]=n[i][j];
					//nCountdownCopyCopy[i][j]=n[i][j];
					//System.out.print(n[i][j]+" ");
				}
				//System.out.println();
			}
			//
			//yardLanes[0].lengthOfQueue
			//yardLanes[0].maxLength
			int[] stripNumberLastInEachLane=new int[numberOfYardLanes];
			//int[] desiredStripNumberInEachLane=new int[numberOfYardLanes];
			for(int i=0;i<numberOfYardLanes;i++){
				stripNumberLastInEachLane[i]=-1;//indicate empty lane
			}
			//
			Rectangle currentRectangle=head;
			while(currentRectangle!=null){
				//
				int selectedLane=-1;
				/*for(int i=0;i<n.length;i++){
					for(int j=0;j<n[i].length;j++){
						nCountdownCopyCopy[i][j]=nCountdownCopy[i][j];
					}
				}*/
				//find the remaining length in each yard lane
				double[] remainingLengthPerLane=new double[numberOfYardLanes];
				for(int i=0;i<numberOfYardLanes;i++){
					remainingLengthPerLane[i]=yardLanes[0].maxLength-yardLanes[i].lengthOfQueue;
				}
				//what is the strip number of the next arrival
				ArrayList<Integer> possibleStripNumbers=new ArrayList<Integer>(10);
				int stripNumberOfNextArrival=-1;//currentRectangle
				for(int i=0;i<nCountdownCopy.length;i++){// && stripNumberOfNextArrival==-1
					if(nCountdownCopy[i][currentRectangle.type]>0){
						if(stripNumberOfNextArrival==-1){
							stripNumberOfNextArrival=i;
						}
						possibleStripNumbers.add(i);
					}
				}
				//The following should be the first step.
				//Then look for the first lane matching strip number with enough space
				//If no match exists (i.e. the new arrivals minimum strip number id too high
				//use the lane with the highest allocated strip number 
				
				//fill in "stripNumberPerYardLane"
				//pretend the remaining vehicles arrive in perfect strip number order
				//until each lane has been assigned a number
				int[] stripNumberLastInEachLaneCopy=new int[numberOfYardLanes];
				//int[] desiredStripNumberInEachLane=new int[numberOfYardLanes];
				for(int i=0;i<numberOfYardLanes;i++){
					stripNumberLastInEachLaneCopy[i]=stripNumberLastInEachLane[i];
				}
				double[] remainingLengthPerLaneCopy=new double[numberOfYardLanes];
				for(int i=0;i<numberOfYardLanes;i++){
					remainingLengthPerLaneCopy[i]=remainingLengthPerLane[i];
				}
				int[] stripNumberPerYardLane=new int[numberOfYardLanes];//until each lane has an associate strip number
				for(int i=0;i<numberOfYardLanes;i++){
					stripNumberPerYardLane[i]=-1;
				}
				//
				for(int i=0;i<nCountdownCopy.length;i++){
					for(int j=0;j<nCountdownCopy[i].length;j++){
						//
						int numberOfTypeInStrip=nCountdownCopy[i][j];
						for(int k=0;k<numberOfTypeInStrip;k++){//non-zero number of vehicle type j remaining to load from strip i
							//find a lane with equal strip number
							boolean equalStripNumberFound=false;
							for(int l=0;l<numberOfYardLanes && !equalStripNumberFound;l++){
								if(stripNumberLastInEachLaneCopy[l]==i && rectangleDistribution[0][j]<=remainingLengthPerLaneCopy[l]){
									equalStripNumberFound=true;
									//updates
									remainingLengthPerLaneCopy[l]-=rectangleDistribution[0][j];
									//nCountdownCopyCopy[i][j]--;
									//
									if(stripNumberPerYardLane[l]==-1){
										stripNumberPerYardLane[l]=i;
									}
								}
							}
							//find an empty lane
							boolean emptyLaneFound=false;
							if(!equalStripNumberFound){
								
								for(int l=0;l<numberOfYardLanes && !emptyLaneFound;l++){
									if(stripNumberLastInEachLaneCopy[l]==-1 && rectangleDistribution[0][j]<=remainingLengthPerLaneCopy[l]){
										emptyLaneFound=true;
										//updates
										stripNumberLastInEachLaneCopy[l]=i;
										remainingLengthPerLaneCopy[l]-=rectangleDistribution[0][j];
										//nCountdownCopyCopy[i][j]--;
										//
										if(stripNumberPerYardLane[l]==-1){
											stripNumberPerYardLane[l]=i;
										}
									}
								}
							}
							//find the lowest strip number (>-1) at the end of a lane
							if(!equalStripNumberFound && !emptyLaneFound){
								int minStripNumber=Integer.MAX_VALUE;
								int minStripNumberLane=-1;
								for(int l=0;l<numberOfYardLanes;l++){
									if(stripNumberLastInEachLaneCopy[l]<minStripNumber && rectangleDistribution[0][j]<=remainingLengthPerLaneCopy[l]){
										minStripNumberLane=l;
										minStripNumber=stripNumberLastInEachLaneCopy[l];
									}
								}
								//
								if(minStripNumberLane>-1){
									//updates
									stripNumberLastInEachLaneCopy[minStripNumberLane]=i;
									remainingLengthPerLaneCopy[minStripNumberLane]-=rectangleDistribution[0][j];
									//nCountdownCopyCopy[i][j]--;
									//
									if(stripNumberPerYardLane[minStripNumberLane]==-1){
										stripNumberPerYardLane[minStripNumberLane]=i;
									}
								}
							}
						}
					}
				}
				//
				//check if the new arrival can be added to a lane with the same strip number added previously
				int perfectMatchingLane=-1;
				int highestStripNumberLane=0;
				int highestStripNumber=0;
				//Optional: instead of prioritising the feasibility of low strip numbers protect the strip that are already in the yard
				int infimumStripNumber=-1; //(find the largest strip number where strip numbers are still non-decreasing in each lane)
				int infimumLaneNumber=-1;
				for(int i=0;i<numberOfYardLanes && perfectMatchingLane==-1;i++){
					if(stripNumberPerYardLane[i]==stripNumberOfNextArrival && currentRectangle.l<=remainingLengthPerLane[i]){
						perfectMatchingLane=i;//stripNumberOfNextArrival;
					}
					//
					if(stripNumberPerYardLane[i]>highestStripNumber){
						highestStripNumberLane=i;
						highestStripNumber=stripNumberPerYardLane[i];
					}
					//
					if(stripNumberPerYardLane[i]<stripNumberOfNextArrival && stripNumberPerYardLane[i]>infimumStripNumber){
						infimumStripNumber=stripNumberPerYardLane[i];
						infimumLaneNumber=i;
					}
				}
				//
				if(perfectMatchingLane!=-1){
					//add the vehicle to the lane, update "nCountdownCopy"
					selectedLane=perfectMatchingLane;//debugging variable
					//
					yardLanes[perfectMatchingLane].addRectangleToQueue(currentRectangle);
					nCountdownCopy[stripNumberOfNextArrival][currentRectangle.type]--;
					//this following may be changing for several reason, including a previously completed strip (add to the yard)
					stripNumberLastInEachLane[perfectMatchingLane]=stripNumberOfNextArrival;
					//check if the strip is now complete
				}else{
					for(int j=0;j<possibleStripNumbers.size() && perfectMatchingLane==-1;j++){
						for(int i=0;i<numberOfYardLanes && perfectMatchingLane==-1;i++){
							if(stripNumberLastInEachLane[i]==possibleStripNumbers.get(j) && currentRectangle.l<=remainingLengthPerLane[i]){
								perfectMatchingLane=i;//stripNumberOfNextArrival;
							}
						}
					}
					//
					if(perfectMatchingLane!=-1){
						//
						selectedLane=perfectMatchingLane;//debugging variable
						//
						yardLanes[perfectMatchingLane].addRectangleToQueue(currentRectangle);
						nCountdownCopy[stripNumberOfNextArrival][currentRectangle.type]--;
						//not true in this case (stays the same)
						//stripNumberLastInEachLane[perfectMatchingLane]=stripNumberOfNextArrival;
					}else{
						//highest strip number (as to prioritise being able to load things first that were to be loaded first according to the packing plan
						//highestStripNumberLane;
						//highestStripNumber;
						//
						selectedLane=highestStripNumberLane;//debugging variable
						//
						yardLanes[highestStripNumberLane].addRectangleToQueue(currentRectangle);
						nCountdownCopy[stripNumberOfNextArrival][currentRectangle.type]--;
						//this following may be changing for several reason, including a previously completed strip (add to the yard)
						stripNumberLastInEachLane[highestStripNumberLane]=stripNumberOfNextArrival;
					}
				}
				//
				//System.out.println(currentRectangle.type+", "+stripNumberOfNextArrival+", "+selectedLane);
				//
				currentRectangle=currentRectangle.nextRect;
			}
			//System.out.println("yardPolicyType 1 debugging");
		}
		//
		resetQueues();
	}
	
	//generateArrivalScenario
	void generateArrivalScenario(int randomSeed){//useful for when solving for a set of scenarios
		//reset (arrival time order list)
		head=null;
		tail=null;
		length=0;
		//set random seed
		randGen=new Random(randomSeed);
		//generate uniform random arrival times
		for(int i=0;i<allRectangles.length;i++){
			allRectangles[i].randArrivalTime=randGen.nextDouble();
			//reset rectangle queue linked list references
			allRectangles[i].nextRectInQueue=null;
			allRectangles[i].prevRectInQueue=null;
			//
			addRectByArrivalTime(allRectangles[i]);
		}
	}
	
	//generateArrivalScenario
	void generateArrivalScenario(int randomSeed, double moduloFraction){//useful for when solving for a set of scenarios
		//reset (arrival time order list)
		head=null;
		tail=null;
		length=0;
		//set random seed
		randGen=new Random(randomSeed);
		//generate uniform random arrival times
		for(int i=0;i<allRectangles.length;i++){
			allRectangles[i].randArrivalTime=randGen.nextDouble();
			//
			if(allRectangles[i].randArrivalTime<moduloFraction){
				allRectangles[i].randArrivalTime+=1;
			}
			//reset rectangle queue linked list references
			allRectangles[i].nextRectInQueue=null;
			allRectangles[i].prevRectInQueue=null;
			//
			addRectByArrivalTime(allRectangles[i]);
		}
	}
	
	void generateArrivalScenario(int randomSeed, int[]vMix){//useful for when solving for a set of scenarios
		//reset (arrival time order list)
		head=null;
		tail=null;
		length=0;
		//set random seed
		randGen=new Random(randomSeed);
		//generate uniform random arrival times
		for(int i=0;i<allRectangles.length;i++){
			allRectangles[i].randArrivalTime=randGen.nextDouble();
			//reset rectangle queue linked list references
			allRectangles[i].nextRectInQueue=null;
			allRectangles[i].prevRectInQueue=null;
			//
			addRectByArrivalTime(allRectangles[i]);
		}
	}
	
	//generate yard queues. 
	//Given the number of yard lanes and the set of vehicles
	//generate a balanced heuristic yard policy (so that all lanes will be used evenly
	/*void generateYardQueuesEvenDistribution(int randomSeed){//use yard policy
		//reset queues
		clearQueues();
		//generate the rectangle arrival order for the given scenario/random seed
		generateArrivalScenario(randomSeed);
		//cycle through rectangles in the arrival order and implement a closest fit/minimum length policy
		Rectangle currentRectangle=head;
		while(currentRectangle!=null){
			//find the closest fit lanes breaking ties by (first found/least full)
			double minDiff=Double.MAX_VALUE;
			int bestYardLane=-1;
			double bestQueueLength=Double.MAX_VALUE;
			for(int i=0;i<numberOfYardLanes;i++){
				//firstly check if the rectangle fits into the yard lane
				if(currentRectangle.w<=yardLanes[i].maxWidth && yardLanes[i].lengthOfQueue+currentRectangle.l<=yardLanes[i].maxLength){
					if(yardLanes[i].widthIsTargetDimension){
						double diff=Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.w);
						if(diff<=minDiff){
							if(diff<minDiff){
								minDiff=diff;
								bestQueueLength=yardLanes[i].lengthOfQueue;
								bestYardLane=i;
							}else{
								//check if this length is less full than the current best yard lane
								if(yardLanes[i].lengthOfQueue<bestQueueLength){
									//minDiff=diff;//has not changed, is the same
									bestQueueLength=yardLanes[i].lengthOfQueue;
									bestYardLane=i;
								}
							}
						}
					}else{
						double diff=Math.abs(yardLanes[i].targetDimensionLength-currentRectangle.l);
						if(diff<=minDiff){
							if(diff<minDiff){
								minDiff=diff;
								bestQueueLength=yardLanes[i].lengthOfQueue;
								bestYardLane=i;
							}else{
								//check if this length is less full than the current best yard lane
								if(yardLanes[i].lengthOfQueue<bestQueueLength){
									//minDiff=diff;//has not changed, is the same
									bestQueueLength=yardLanes[i].lengthOfQueue;
									bestYardLane=i;
								}
							}
						}
					}
				}
			}
			
			//add the rectangle into the closest fitting lane where ties were broken by choosing the least full lane
			yardLanes[bestYardLane].addRectangleToQueue(currentRectangle);

			currentRectangle=currentRectangle.nextRect;
		}
		//
		resetQueues();
	}*/
	
	//methods below are regarded as procedural and not conceptual
	//or instead simple booking keeping that none the less has to be implemented correctly
	//please don't be offended methods below
	double areaOfRectanglesStillInQueue(){
		double remainingAreaOfRectangleInTheQueue=0;
		for(int i=0;i<numberOfYardLanes;i++){
			Rectangle currentRect=yardLanes[i].rectAtFront;
			while(currentRect!=null){
				remainingAreaOfRectangleInTheQueue+=currentRect.area;
				currentRect=currentRect.nextRectInQueue;
			}
		}
		return remainingAreaOfRectangleInTheQueue;
	}
	
	double areaOfRectanglesStillInYardNonConstrainedLoading(){
		double remainingAreaOfRectangleInYardNonConstrainedLoading=0;
		Rectangle currentRect=headByWidthNonConstrainedLoading;
		while(currentRect!=null){
			remainingAreaOfRectangleInYardNonConstrainedLoading+=currentRect.area;
			currentRect=currentRect.nextRectByWidthNonConstrainedLoading;
		}
		return remainingAreaOfRectangleInYardNonConstrainedLoading;
	}
	
	boolean aRectangleFitsRemainingSpace(double remainingWidth, double remainingLength){
		//find a queued rectangle that fits
		//boolean rectangleFits=false;
		//reset current rectangle
		for(int i=0;i<numberOfYardLanes;i++){// && !rectangleFits
			yardLanes[i].resetCurrentRectangleToFront();
			Rectangle currentRect=yardLanes[i].currentRect;
			while(currentRect!=null){// && !rectangleFits
				if(currentRect.w<=remainingWidth && currentRect.l<=remainingLength){
					return true;
				}
				currentRect=currentRect.nextRectInQueue;
			}
		}
		return false;
	}
	
	//methods for the ordered list of includable rectangles
	void addRectByWidth(Rectangle newRect){
		//System.out.println();
		newRect.nextRect=null;
		newRect.prevRect=null;
		Rectangle currentRect=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newRect;
			head.prevRect=null;
			tail=newRect;
			tail.nextRect=null;
		}else if(head==tail){//one member
			if(newRect.w<=head.w){
				if(newRect.w==head.w){
					newRect.nextRect=head;
					head.prevRect=newRect;
					head=newRect;	
				}else{
					newRect.nextRect=head;
					head.prevRect=newRect;
					head=newRect;
				}
			}else{
				newRect.prevRect=head;
				head.nextRect=newRect;
				tail=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newRect.w<=head.w){
				//before the head
				posFound=true;
				newRect.nextRect=head;
				head.prevRect=newRect;
				head=newRect;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newRect.w>=tail.w){
					posFound=true;
					newRect.prevRect=tail;
					tail.nextRect=newRect;
					tail=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the head or the tail
				if((newRect.w-head.w)<(tail.w-newRect.w)){
					//start searching for the position from the head
					currentRect=head.nextRect;
					while(!posFound && currentRect!=null){
						if(newRect.w<=currentRect.w){
							//before the current edge
							posFound=true;
							newRect.nextRect=currentRect;
							newRect.prevRect=currentRect.prevRect;
							/*if(currentRect.prevRect==null){
								System.out.println();
							}*/
							currentRect.prevRect.nextRect=newRect;
							currentRect.prevRect=newRect;
						}
						currentRect=currentRect.nextRect;
					}
				}else{
					//start searching for the position from the tail
					currentRect=tail.prevRect;
					while(!posFound && currentRect!=null){
						if(newRect.w>=currentRect.w){
							//before the current edge
							posFound=true;
							newRect.prevRect=currentRect;
							newRect.nextRect=currentRect.nextRect;
							currentRect.nextRect.prevRect=newRect;
							currentRect.nextRect=newRect;
						}
						currentRect=currentRect.prevRect;
					}
				}
			}
		}
		length++;
	}
	
	void addRectByLength(Rectangle newRect){
		/*aCounter++;
		System.out.println(aCounter);
		if(aCounter==50){
			System.out.println(aCounter);
		}*/
		newRect.nextRect=null;
		newRect.prevRect=null;
		Rectangle currentRect=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newRect;
			head.prevRect=null;
			tail=newRect;
			tail.nextRect=null;
		}else if(head==tail){//one member
			if(newRect.l<=head.l){
				if(newRect.l==head.l){
					newRect.nextRect=head;
					head.prevRect=newRect;
					head=newRect;	
				}else{
					newRect.nextRect=head;
					head.prevRect=newRect;
					head=newRect;
				}
			}else{
				newRect.prevRect=head;
				head.nextRect=newRect;
				tail=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newRect.l<=head.l){
				//before the head
				posFound=true;
				newRect.nextRect=head;
				head.prevRect=newRect;
				head=newRect;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newRect.l>=tail.l){
					posFound=true;
					newRect.prevRect=tail;
					tail.nextRect=newRect;
					tail=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the head or the tail
				if((newRect.l-head.l)<(tail.l-newRect.l)){
					//start searching for the position from the head
					currentRect=head.nextRect;
					while(!posFound && currentRect!=null){
						if(newRect.l<=currentRect.l){
							//before the current edge
							posFound=true;
							newRect.nextRect=currentRect;
							newRect.prevRect=currentRect.prevRect;
							/*if(currentRect.prevRect==null){
								System.out.println();
							}*/
							currentRect.prevRect.nextRect=newRect;
							currentRect.prevRect=newRect;
						}
						currentRect=currentRect.nextRect;
					}
				}else{
					//start searching for the position from the tail
					currentRect=tail.prevRect;
					while(!posFound && currentRect!=null){
						if(newRect.l>=currentRect.l){
							//before the current edge
							posFound=true;
							newRect.prevRect=currentRect;
							newRect.nextRect=currentRect.nextRect;
							currentRect.nextRect.prevRect=newRect;
							currentRect.nextRect=newRect;
						}
						currentRect=currentRect.prevRect;
					}
				}
			}
		}
		length++;
	}
	
	void addRectByArrivalTime(Rectangle newRect){
		newRect.nextRect=null;
		newRect.prevRect=null;
		Rectangle currentRect=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newRect;
			head.prevRect=null;
			tail=newRect;
			tail.nextRect=null;
		}else if(head==tail){//one member
			if(newRect.randArrivalTime<=head.randArrivalTime){
				if(newRect.randArrivalTime==head.randArrivalTime){
					newRect.nextRect=head;
					head.prevRect=newRect;
					head=newRect;	
				}else{
					newRect.nextRect=head;
					head.prevRect=newRect;
					head=newRect;
				}
			}else{
				newRect.prevRect=head;
				head.nextRect=newRect;
				tail=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newRect.randArrivalTime<=head.randArrivalTime){
				//before the head
				posFound=true;
				newRect.nextRect=head;
				head.prevRect=newRect;
				head=newRect;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newRect.randArrivalTime>=tail.randArrivalTime){
					posFound=true;
					newRect.prevRect=tail;
					tail.nextRect=newRect;
					tail=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the head or the tail
				if((newRect.randArrivalTime-head.randArrivalTime)<(tail.randArrivalTime-newRect.randArrivalTime)){
					//start searching for the position from the head
					currentRect=head.nextRect;
					while(!posFound && currentRect!=null){
						if(newRect.randArrivalTime<=currentRect.randArrivalTime){
							//before the current edge
							posFound=true;
							newRect.nextRect=currentRect;
							newRect.prevRect=currentRect.prevRect;
							/*if(currentRect.prevRect==null){
								System.out.println();
							}*/
							currentRect.prevRect.nextRect=newRect;
							currentRect.prevRect=newRect;
						}
						currentRect=currentRect.nextRect;
					}
				}else{
					//start searching for the position from the tail
					currentRect=tail.prevRect;
					while(!posFound && currentRect!=null){
						if(newRect.randArrivalTime>=currentRect.randArrivalTime){
							//before the current edge
							posFound=true;
							newRect.prevRect=currentRect;
							newRect.nextRect=currentRect.nextRect;
							currentRect.nextRect.prevRect=newRect;
							currentRect.nextRect=newRect;
						}
						currentRect=currentRect.prevRect;
					}
				}
			}
		}
		length++;
	}
	
	//rectangles by width and length methods
	//add/remove
	void addRectByWidthYardPolicyCreation(Rectangle newRect, boolean initialLinkedListGeneration){
		newRect.nextRectByWidthYardPolicyCreation=null;
		newRect.prevRectByWidthYardPolicyCreation=null;
		Rectangle currentRect=headByWidthYardPolicyCreation;
		if(headByWidthYardPolicyCreation==null){
			//this implies there are no elements in the list
			headByWidthYardPolicyCreation=newRect;
			headByWidthYardPolicyCreation.prevRectByWidthYardPolicyCreation=null;
			tailByWidthYardPolicyCreation=newRect;
			tailByWidthYardPolicyCreation.nextRectByWidthYardPolicyCreation=null;
		}else if(headByWidthYardPolicyCreation==tailByWidthYardPolicyCreation){//one member
			if(newRect.w<=headByWidthYardPolicyCreation.w){
				if(newRect.w==headByWidthYardPolicyCreation.w){
					newRect.nextRectByWidthYardPolicyCreation=headByWidthYardPolicyCreation;
					headByWidthYardPolicyCreation.prevRectByWidthYardPolicyCreation=newRect;
					headByWidthYardPolicyCreation=newRect;	
				}else{
					newRect.nextRectByWidthYardPolicyCreation=headByWidthYardPolicyCreation;
					headByWidthYardPolicyCreation.prevRectByWidthYardPolicyCreation=newRect;
					headByWidthYardPolicyCreation=newRect;
				}
			}else{
				newRect.prevRectByWidthYardPolicyCreation=headByWidthYardPolicyCreation;
				headByWidthYardPolicyCreation.nextRectByWidthYardPolicyCreation=newRect;
				tailByWidthYardPolicyCreation=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the headByWidth
			if(newRect.w<=headByWidthYardPolicyCreation.w){
				//before the headByWidth
				posFound=true;
				newRect.nextRectByWidthYardPolicyCreation=headByWidthYardPolicyCreation;
				headByWidthYardPolicyCreation.prevRectByWidthYardPolicyCreation=newRect;
				headByWidthYardPolicyCreation=newRect;
			}
			//does the edge go after the tailByWidth
			if(!posFound){
				if(newRect.w>=tailByWidthYardPolicyCreation.w){
					posFound=true;
					newRect.prevRectByWidthYardPolicyCreation=tailByWidthYardPolicyCreation;
					tailByWidthYardPolicyCreation.nextRectByWidthYardPolicyCreation=newRect;
					tailByWidthYardPolicyCreation=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the headByWidth or the tailByWidth
				if((newRect.w-headByWidthYardPolicyCreation.w)<(tailByWidthYardPolicyCreation.w-newRect.w)){
					//start searching for the position from the headByWidth
					currentRect=headByWidthYardPolicyCreation.nextRectByWidthYardPolicyCreation;
					while(!posFound && currentRect!=null){
						if(newRect.w<=currentRect.w){
							//before the current edge
							posFound=true;
							newRect.nextRectByWidthYardPolicyCreation=currentRect;
							newRect.prevRectByWidthYardPolicyCreation=currentRect.prevRectByWidthYardPolicyCreation;
							if(currentRect.prevRectByWidthYardPolicyCreation==null){
								System.out.println();
							}
							currentRect.prevRectByWidthYardPolicyCreation.nextRectByWidthYardPolicyCreation=newRect;
							currentRect.prevRectByWidthYardPolicyCreation=newRect;
						}
						currentRect=currentRect.nextRectByWidthYardPolicyCreation;
					}
				}else{
					//start searching for the position from the tailByWidth
					currentRect=tailByWidthYardPolicyCreation.prevRectByWidthYardPolicyCreation;
					while(!posFound && currentRect!=null){
						if(newRect.w>=currentRect.w){
							//before the current edge
							posFound=true;
							newRect.prevRectByWidthYardPolicyCreation=currentRect;
							newRect.nextRectByWidthYardPolicyCreation=currentRect.nextRectByWidthYardPolicyCreation;
							currentRect.nextRectByWidthYardPolicyCreation.prevRectByWidthYardPolicyCreation=newRect;
							currentRect.nextRectByWidthYardPolicyCreation=newRect;
						}
						currentRect=currentRect.prevRectByWidthYardPolicyCreation;
					}
				}
			}
		}
	}
	//add by length
	void addRectByLengthYardPolicyCreation(Rectangle newRect, boolean initialLinkedListGeneration){
		newRect.nextRectByLengthYardPolicyCreation=null;
		newRect.prevRectByLengthYardPolicyCreation=null;
		Rectangle currentRect=headByLengthYardPolicyCreation;
		if(headByLengthYardPolicyCreation==null){
			//this implies there are no elements in the list
			headByLengthYardPolicyCreation=newRect;
			headByLengthYardPolicyCreation.prevRectByLengthYardPolicyCreation=null;
			tailByLengthYardPolicyCreation=newRect;
			tailByLengthYardPolicyCreation.nextRectByLengthYardPolicyCreation=null;
		}else if(headByLengthYardPolicyCreation==tailByLengthYardPolicyCreation){//one member
			if(newRect.l<=headByLengthYardPolicyCreation.l){
				if(newRect.l==headByLengthYardPolicyCreation.l){
					newRect.nextRectByLengthYardPolicyCreation=headByLengthYardPolicyCreation;
					headByLengthYardPolicyCreation.prevRectByLengthYardPolicyCreation=newRect;
					headByLengthYardPolicyCreation=newRect;	
				}else{
					newRect.nextRectByLengthYardPolicyCreation=headByLengthYardPolicyCreation;
					headByLengthYardPolicyCreation.prevRectByLengthYardPolicyCreation=newRect;
					headByLengthYardPolicyCreation=newRect;
				}
			}else{
				newRect.prevRectByLengthYardPolicyCreation=headByLengthYardPolicyCreation;
				headByLengthYardPolicyCreation.nextRectByLengthYardPolicyCreation=newRect;
				tailByLengthYardPolicyCreation=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the headByLength
			if(newRect.l<=headByLengthYardPolicyCreation.l){
				//before the headByLength
				posFound=true;
				newRect.nextRectByLengthYardPolicyCreation=headByLengthYardPolicyCreation;
				headByLengthYardPolicyCreation.prevRectByLengthYardPolicyCreation=newRect;
				headByLengthYardPolicyCreation=newRect;
			}
			//does the edge go after the tailByLength
			if(!posFound){
				if(newRect.l>=tailByLengthYardPolicyCreation.l){
					posFound=true;
					newRect.prevRectByLengthYardPolicyCreation=tailByLengthYardPolicyCreation;
					tailByLengthYardPolicyCreation.nextRectByLengthYardPolicyCreation=newRect;
					tailByLengthYardPolicyCreation=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the headByLength or the tailByLength
				if((newRect.l-headByLengthYardPolicyCreation.l)<(tailByLengthYardPolicyCreation.l-newRect.l)){
					//start searching for the position from the headByLength
					currentRect=headByLengthYardPolicyCreation.nextRectByLengthYardPolicyCreation;
					while(!posFound && currentRect!=null){
						if(newRect.l<=currentRect.l){
							//before the current edge
							posFound=true;
							newRect.nextRectByLengthYardPolicyCreation=currentRect;
							newRect.prevRectByLengthYardPolicyCreation=currentRect.prevRectByLengthYardPolicyCreation;
							if(currentRect.prevRectByLengthYardPolicyCreation==null){
								System.out.println();
							}
							currentRect.prevRectByLengthYardPolicyCreation.nextRectByLengthYardPolicyCreation=newRect;
							currentRect.prevRectByLengthYardPolicyCreation=newRect;
						}
						currentRect=currentRect.nextRectByLengthYardPolicyCreation;
					}
				}else{
					//start searching for the position from the tailByLength
					currentRect=tailByLengthYardPolicyCreation.prevRectByLengthYardPolicyCreation;
					while(!posFound && currentRect!=null){
						if(newRect.l>=currentRect.l){
							//before the current edge
							posFound=true;
							newRect.prevRectByLengthYardPolicyCreation=currentRect;
							newRect.nextRectByLengthYardPolicyCreation=currentRect.nextRectByLengthYardPolicyCreation;
							currentRect.nextRectByLengthYardPolicyCreation.prevRectByLengthYardPolicyCreation=newRect;
							currentRect.nextRectByLengthYardPolicyCreation=newRect;
						}
						currentRect=currentRect.prevRectByLengthYardPolicyCreation;
					}
				}
			}
		}
	}
	
	double getRectangleWidthYardPolicyCreation(int index){
		Rectangle currentRectangle=headByWidthYardPolicyCreation;
		int indexCounter=0;
		while(indexCounter<index){
			currentRectangle=currentRectangle.nextRectByWidthYardPolicyCreation;
			indexCounter++;
		}
		return currentRectangle.w;
	}

	double getRectangleLengthYardPolicyCreation(int index){
		Rectangle currentRectangle=headByLengthYardPolicyCreation;
		int indexCounter=0;
		while(indexCounter<index){
			currentRectangle=currentRectangle.nextRectByLengthYardPolicyCreation;
			indexCounter++;
		}
		return currentRectangle.l;
	}
	
	//rectangle list methods for the case where the ferry loading process is not constrained by
	//yard queues
	/*void addRectByWidthNonConstrainedLoading(Rectangle newRect){
		//System.out.println();
		newRect.nextRectByWidthNonConstrainedLoading=null;
		newRect.prevRectByWidthNonConstrainedLoading=null;
		Rectangle currentRect=headByWidthNonConstrainedLoading;
		if(headByWidthNonConstrainedLoading==null){
			//this implies there are no elements in the list
			headByWidthNonConstrainedLoading=newRect;
			headByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=null;
			tailByWidthNonConstrainedLoading=newRect;
			tailByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading=null;
		}else if(headByWidthNonConstrainedLoading==tailByWidthNonConstrainedLoading){//one member
			if(newRect.w<=headByWidthNonConstrainedLoading.w){
				if(newRect.w==headByWidthNonConstrainedLoading.w){
					newRect.nextRectByWidthNonConstrainedLoading=headByWidthNonConstrainedLoading;
					headByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=newRect;
					headByWidthNonConstrainedLoading=newRect;	
				}else{
					newRect.nextRectByWidthNonConstrainedLoading=headByWidthNonConstrainedLoading;
					headByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=newRect;
					headByWidthNonConstrainedLoading=newRect;
				}
			}else{
				newRect.prevRectByWidthNonConstrainedLoading=headByWidthNonConstrainedLoading;
				headByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading=newRect;
				tailByWidthNonConstrainedLoading=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newRect.w<=headByWidthNonConstrainedLoading.w){
				//before the head
				posFound=true;
				newRect.nextRectByWidthNonConstrainedLoading=headByWidthNonConstrainedLoading;
				headByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=newRect;
				headByWidthNonConstrainedLoading=newRect;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newRect.w>=tailByWidthNonConstrainedLoading.w){
					posFound=true;
					newRect.prevRectByWidthNonConstrainedLoading=tailByWidthNonConstrainedLoading;
					tailByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading=newRect;
					tailByWidthNonConstrainedLoading=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the head or the tail
				if((newRect.w-headByWidthNonConstrainedLoading.w)<(tailByWidthNonConstrainedLoading.w-newRect.w)){
					//start searching for the position from the head
					currentRect=headByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading;
					while(!posFound && currentRect!=null){
						if(newRect.w<=currentRect.w){
							//before the current edge
							posFound=true;
							newRect.nextRectByWidthNonConstrainedLoading=currentRect;
							newRect.prevRectByWidthNonConstrainedLoading=currentRect.prevRectByWidthNonConstrainedLoading;
							if(currentRect.prevRect==null){
								System.out.println();
							}
							currentRect.prevRectByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading=newRect;
							currentRect.prevRectByWidthNonConstrainedLoading=newRect;
						}
						currentRect=currentRect.nextRectByWidthNonConstrainedLoading;
					}
				}else{
					//start searching for the position from the tail
					currentRect=tail.prevRectByWidthNonConstrainedLoading;
					while(!posFound && currentRect!=null){
						if(newRect.w>=currentRect.w){
							//before the current edge
							posFound=true;
							newRect.prevRectByWidthNonConstrainedLoading=currentRect;
							newRect.nextRectByWidthNonConstrainedLoading=currentRect.nextRectByWidthNonConstrainedLoading;
							currentRect.nextRectByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=newRect;
							currentRect.nextRectByWidthNonConstrainedLoading=newRect;
						}
						currentRect=currentRect.prevRectByWidthNonConstrainedLoading;
					}
				}
			}
		}
		initialLengthNonConstrainedLoading++;
	}
	
	void addRectByLengthNonConstrainedLoading(Rectangle newRect){
		aCounter++;
		System.out.println(aCounter);
		if(aCounter==50){
			System.out.println(aCounter);
		}
		newRect.nextRectByLengthNonConstrainedLoading=null;
		newRect.prevRectByLengthNonConstrainedLoading=null;
		Rectangle currentRect=headByLengthNonConstrainedLoading;
		if(headByLengthNonConstrainedLoading==null){
			//this implies there are no elements in the list
			headByLengthNonConstrainedLoading=newRect;
			headByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=null;
			tailByLengthNonConstrainedLoading=newRect;
			tailByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading=null;
		}else if(headByLengthNonConstrainedLoading==tailByLengthNonConstrainedLoading){//one member
			if(newRect.l<=headByLengthNonConstrainedLoading.l){
				if(newRect.l==headByLengthNonConstrainedLoading.l){
					newRect.nextRectByLengthNonConstrainedLoading=headByLengthNonConstrainedLoading;
					headByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=newRect;
					headByLengthNonConstrainedLoading=newRect;	
				}else{
					newRect.nextRectByLengthNonConstrainedLoading=headByLengthNonConstrainedLoading;
					headByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=newRect;
					headByLengthNonConstrainedLoading=newRect;
				}
			}else{
				newRect.prevRectByLengthNonConstrainedLoading=headByLengthNonConstrainedLoading;
				headByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading=newRect;
				tailByLengthNonConstrainedLoading=newRect;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newRect.l<=headByLengthNonConstrainedLoading.l){
				//before the head
				posFound=true;
				newRect.nextRectByLengthNonConstrainedLoading=head;
				headByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=newRect;
				headByLengthNonConstrainedLoading=newRect;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newRect.l>=tailByLengthNonConstrainedLoading.l){
					posFound=true;
					newRect.prevRectByLengthNonConstrainedLoading=tailByLengthNonConstrainedLoading;
					tailByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading=newRect;
					tailByLengthNonConstrainedLoading=newRect;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the head or the tail
				if((newRect.l-headByLengthNonConstrainedLoading.l)<(tailByLengthNonConstrainedLoading.l-newRect.l)){
					//start searching for the position from the head
					currentRect=headByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading;
					while(!posFound && currentRect!=null){
						if(newRect.l<=currentRect.l){
							//before the current edge
							posFound=true;
							newRect.nextRectByLengthNonConstrainedLoading=currentRect;
							newRect.prevRectByLengthNonConstrainedLoading=currentRect.prevRectByLengthNonConstrainedLoading;
							if(currentRect.prevRect==null){
								System.out.println();
							}
							currentRect.prevRectByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading=newRect;
							currentRect.prevRectByLengthNonConstrainedLoading=newRect;
						}
						currentRect=currentRect.nextRectByLengthNonConstrainedLoading;
					}
				}else{
					//start searching for the position from the tail
					currentRect=tailByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading;
					while(!posFound && currentRect!=null){
						if(newRect.l>=currentRect.l){
							//before the current edge
							posFound=true;
							newRect.prevRectByLengthNonConstrainedLoading=currentRect;
							newRect.nextRectByLengthNonConstrainedLoading=currentRect.nextRectByLengthNonConstrainedLoading;
							currentRect.nextRectByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=newRect;
							currentRect.nextRectByLengthNonConstrainedLoading=newRect;
						}
						currentRect=currentRect.prevRectByLengthNonConstrainedLoading;
					}
				}
			}
		}
		//length++;
	}*/
	
	Rectangle[] getQuantileNonConstrainedLoading(boolean byWidth, double maxLength, double maxWidth, double quantileCentre){//double maxSumOfDimension
		//for example if quantile centre is 0.5, return set of rectangles of total dimensional length
		//up to the given value either side of the given quantile
		Rectangle[] quantileHeadTail=new Rectangle[2];
		//central index
		int centralIndex=(int)Math.floor((lengthNonConstrainedLoading-1)*quantileCentre);
		//index range approach may not be feasible because 
		Rectangle quantileHead=getRectangleNonConstrainedLoading(centralIndex, byWidth);
		Rectangle quantileTail=quantileHead;
		//reset quantile approach references
		quantileHead.nextRect=null;
		quantileHead.prevRect=null;
		quantileTail.nextRect=null;
		quantileTail.prevRect=null;
		//
		Rectangle nextTailRect;//rectangle reference to the next rectangle that may be added to the quantile
		Rectangle nextHeadRect;
		//
		double sumOfDimension=0;
		if(byWidth){
			nextTailRect=quantileTail.nextRectByWidthNonConstrainedLoading;
			nextHeadRect=quantileHead.prevRectByWidthNonConstrainedLoading;
			//
			if(quantileHead.w>maxWidth || quantileHead.l>maxLength){
				quantileHead=null;
				quantileTail=null;
			}else{
				sumOfDimension=quantileHead.w;
			}
		}else{
			nextTailRect=quantileTail.nextRectByLengthNonConstrainedLoading;
			nextHeadRect=quantileHead.prevRectByLengthNonConstrainedLoading;
			//
			if(quantileHead.l>maxLength || quantileHead.w>maxWidth){
				quantileHead=null;
				quantileTail=null;
			}else{
				sumOfDimension=quantileHead.l;
			}
		}
		
		//alternative between move=ing the head back and the tail forwards with
		//fixed rules for dealing with situations where this approach does not work
		if(byWidth){
			//
			int direction=0;
			if(quantileCentre<0.5){
				//try moving the head back first
				direction=-1;
			}else{
				//try moving the tail forwards first
				direction=1;
			}
			boolean movesStillPossible=(nextTailRect!=null || nextHeadRect!=null);
			while(movesStillPossible){
				//
				if(direction==1){
					//check whether the tail can be moved fords feasibly
					if(nextTailRect!=null){
						//
						double newDimensionSum=sumOfDimension+nextTailRect.w;
						if(newDimensionSum<=maxWidth && nextTailRect.l<=maxLength){
							if(quantileTail==null && quantileHead==null){
								quantileTail=nextTailRect;
								quantileHead=nextTailRect;
								//reset quantile references
								quantileHead.nextRect=null;
								quantileHead.prevRect=null;
								quantileTail.nextRect=null;
								quantileTail.prevRect=null;
							}else{
								quantileTail.nextRect=nextTailRect;
								nextTailRect.prevRect=quantileTail;
								nextTailRect.nextRect=null;
								quantileTail=nextTailRect;
							}
							sumOfDimension=newDimensionSum;
						}
						//
						nextTailRect=nextTailRect.nextRectByWidthNonConstrainedLoading;
					}
				}else{
					//check whether the tail can be moved fords feasibly
					if(nextHeadRect!=null){
						//
						double newDimensionSum=sumOfDimension+nextHeadRect.w;
						if(newDimensionSum<=maxWidth && nextHeadRect.l<=maxLength){
							if(quantileTail==null && quantileHead==null){
								quantileTail=nextHeadRect;
								quantileHead=nextHeadRect;
								//reset quantile references
								quantileHead.nextRect=null;
								quantileHead.prevRect=null;
								quantileTail.nextRect=null;
								quantileTail.prevRect=null;
							}else{
								quantileHead.prevRect=nextHeadRect;
								nextHeadRect.nextRect=quantileHead;
								nextHeadRect.prevRect=null;
								quantileHead=nextHeadRect;
							}
							sumOfDimension=newDimensionSum;
						}
						//
						nextHeadRect=nextHeadRect.prevRectByWidthNonConstrainedLoading;
					}
				}
				//
				movesStillPossible=(nextTailRect!=null || nextHeadRect!=null);
				direction*=-1;
			}
		}else{
			//
			int direction=0;
			if(quantileCentre<0.5){
				//try moving the head back first
				direction=-1;
			}else{
				//try moving the tail forwards first
				direction=1;
			}
			boolean movesStillPossible=(nextTailRect!=null || nextHeadRect!=null);
			while(movesStillPossible){
				//
				if(direction==1){
					//check whether the tail can be moved fords feasibly
					if(nextTailRect!=null){
						//
						double newDimensionSum=sumOfDimension+nextTailRect.l;
						if(newDimensionSum<=maxLength && nextTailRect.w<=maxWidth){
							if(quantileTail==null && quantileHead==null){
								quantileTail=nextTailRect;
								quantileHead=nextTailRect;
								//reset quantile references
								quantileHead.nextRect=null;
								quantileHead.prevRect=null;
								quantileTail.nextRect=null;
								quantileTail.prevRect=null;
							}else{
								quantileTail.nextRect=nextTailRect;
								nextTailRect.prevRect=quantileTail;
								nextTailRect.nextRect=null;
								quantileTail=nextTailRect;
							}
							sumOfDimension=newDimensionSum;
						}
						//
						nextTailRect=nextTailRect.nextRectByLengthNonConstrainedLoading;
					}
				}else{
					//check whether the tail can be moved fords feasibly
					if(nextHeadRect!=null){
						//
						double newDimensionSum=sumOfDimension+nextHeadRect.l;
						if(newDimensionSum<=maxLength && nextHeadRect.w<=maxWidth){
							if(quantileTail==null && quantileHead==null){
								quantileTail=nextHeadRect;
								quantileHead=nextHeadRect;
								//reset quantile references
								quantileHead.nextRect=null;
								quantileHead.prevRect=null;
								quantileTail.nextRect=null;
								quantileTail.prevRect=null;
							}else{
								quantileHead.prevRect=nextHeadRect;
								nextHeadRect.nextRect=quantileHead;
								nextHeadRect.prevRect=null;
								quantileHead=nextHeadRect;
							}
							sumOfDimension=newDimensionSum;
						}
						//
						nextHeadRect=nextHeadRect.prevRectByLengthNonConstrainedLoading;
					}
				}
				//
				movesStillPossible=(nextTailRect!=null || nextHeadRect!=null);
				direction*=-1;
			}
		}
		quantileHeadTail[0]=quantileHead;
		quantileHeadTail[1]=quantileTail;
		return quantileHeadTail;
	}
	
	Rectangle getRectangleNonConstrainedLoading(int ind, boolean byWidth){
		int counter=0;
		Rectangle rect=null;
		if(byWidth){
			rect=headByWidthNonConstrainedLoading;
			while(counter<ind){
				rect=rect.nextRectByWidthNonConstrainedLoading;
				counter++;
			}
		}else{
			rect=headByLengthNonConstrainedLoading;
			while(counter<ind){
				rect=rect.nextRectByLengthNonConstrainedLoading;
				counter++;
			}
		}
		//
		
		return rect;
	}
	
	//find the vehicle at the front of a queue as close as possible to the target dimension
	Rectangle getRectangleByTargetDimension(boolean byWidth, double targetDimension){
		Rectangle closestRectangleToTargetDim=null;
		double minDiff=Double.MAX_VALUE;
		double newDiff=0;
		Rectangle RectangleAtFrontOfAQueue=null;
		for(int i=0;i<numberOfYardLanes;i++){
			if((RectangleAtFrontOfAQueue=yardLanes[i].rectAtFront)!=null){
				//if(vehicleStripPositions[RectangleAtFrontOfAQueue.type][stripType]!=null){
					if(byWidth){//for a column the width is the target dimension
						newDiff=Math.abs(RectangleAtFrontOfAQueue.w-targetDimension);
					}else{//for a row the length is the target dimension
						newDiff=Math.abs(RectangleAtFrontOfAQueue.l-targetDimension);
					}
					//
					if(newDiff<minDiff){
						//
						//if(!stopColumnBeforeEntranceZone){
							minDiff=newDiff;
							closestRectangleToTargetDim=RectangleAtFrontOfAQueue;
						/*}else{
							E.setVehicleParkedPositionToOP(vehicleStripPositions[RectangleAtFrontOfAQueue.type][stripType], RectangleAtFrontOfAQueue);
							if(!entranceVehicle.overlap(RectangleAtFrontOfAQueue)){
								minDiff=newDiff;
								closestRectangleToTargetDim=RectangleAtFrontOfAQueue;
							}
						}*/
					}
				//}
				
			}
		}
		return closestRectangleToTargetDim;
	}
			
	
	//remove from both linked lists
	void removeRectNonConstrainedLoading(Rectangle rectToRemove){
		//width
		if(headByWidthNonConstrainedLoading==tailByWidthNonConstrainedLoading){
			headByWidthNonConstrainedLoading=null;
			tailByWidthNonConstrainedLoading=null;
		}else{
			if(rectToRemove==headByWidthNonConstrainedLoading){
				headByWidthNonConstrainedLoading=headByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading;
				headByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=null;
			}else if(rectToRemove==tailByWidthNonConstrainedLoading){
				tailByWidthNonConstrainedLoading=tailByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading;
				tailByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading=null;
				
			}else{
				rectToRemove.prevRectByWidthNonConstrainedLoading.nextRectByWidthNonConstrainedLoading=rectToRemove.nextRectByWidthNonConstrainedLoading;
				rectToRemove.nextRectByWidthNonConstrainedLoading.prevRectByWidthNonConstrainedLoading=rectToRemove.prevRectByWidthNonConstrainedLoading;
			}
		}
		//length
		if(headByLengthNonConstrainedLoading==tailByLengthNonConstrainedLoading){
			headByLengthNonConstrainedLoading=null;
			tailByLengthNonConstrainedLoading=null;
		}else{
			if(rectToRemove==headByLengthNonConstrainedLoading){
				headByLengthNonConstrainedLoading=headByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading;
				headByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=null;
			}else if(rectToRemove==tailByLengthNonConstrainedLoading){
				tailByLengthNonConstrainedLoading=tailByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading;
				tailByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading=null;
			}else{
				rectToRemove.prevRectByLengthNonConstrainedLoading.nextRectByLengthNonConstrainedLoading=rectToRemove.nextRectByLengthNonConstrainedLoading;
				rectToRemove.nextRectByLengthNonConstrainedLoading.prevRectByLengthNonConstrainedLoading=rectToRemove.prevRectByLengthNonConstrainedLoading;
			}
		}
		//decrement
		lengthNonConstrainedLoading--;
	}
	//
	void removeRectanglesSetByGetQuantileNonConstrainedLoading(Rectangle[] headTailOfQuantile){
		Rectangle currentRectangle=headTailOfQuantile[0];
		while(currentRectangle!=null){
			removeRectNonConstrainedLoading(currentRectangle);
			currentRectangle=currentRectangle.nextRect;
		}
	}
	
	double[][] vCountsAndAreasAndDistributionInitial(){
		//row 1: counts
		//row 2: areas
		//row 3: relative areas
		//row 4: cumulative relative areas
		double[][] vCounts=new double[4][vTypes];
		double totalArea=0;
		for(int i=0;i<totalRectangles;i++){
			vCounts[0][allRectangles[i].type]++;
			vCounts[1][allRectangles[i].type]+=allRectangles[i].area;
			totalArea+=allRectangles[i].area;
		}
		//
		double sum=0;
		for(int v=0;v<vTypes;v++){
			vCounts[2][v]=vCounts[1][v]/totalArea;
			sum+=vCounts[2][v];
			vCounts[3][v]=sum;
		}
		//
		return vCounts;
	}
	
	//
	void printSol(int solNumber){
		Gene currentGene=null;//
		if(solNumber==1){//1 or 2
			currentGene=sol.head;
		}else{
			currentGene=sol2.head;
		}
		//
		int geneCounter=0;
		while(currentGene!=null){
			System.out.print(currentGene.value+" ");
			int value=currentGene.value;
			int quantileIndex=Math.floorMod(value, numberOfYardQuantiles);
			int orientation=(int)Math.floor((double)value/numberOfYardQuantiles);
			System.out.print(currentGene.value+" ");
			
			if(orientation==0){
				System.out.print("(W, "+(double)quantileIndex/numberOfYardQuantiles+"),");
			}else if(orientation==1){
				System.out.print("(L), "+(double)quantileIndex/numberOfYardQuantiles+"),");
			}
			
			geneCounter++;
			if(geneCounter==sol.activeLength){
				System.out.print("|");
			}
			currentGene=currentGene.nextGene;
		}
		System.out.println();
	}
	
	void resetQueues(){
		remainingRectangles=totalRectangles;
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i].resetFrontRectangle(resetBinNumbers);
		}
		//
		if(nonConstrainedLoading){
			lengthNonConstrainedLoading=initialLengthPolicyCreation;
			//
			headByWidthNonConstrainedLoading=headByWidthYardPolicyCreation;//initialHeadByWidth;
			tailByWidthNonConstrainedLoading=tailByWidthYardPolicyCreation;
			headByLengthNonConstrainedLoading=headByLengthYardPolicyCreation;
			tailByLengthNonConstrainedLoading=tailByLengthYardPolicyCreation;
			//w
			Rectangle currentRect=headByWidthNonConstrainedLoading;
			while(currentRect!=null){
				//width
				currentRect.nextRectByWidthNonConstrainedLoading=currentRect.nextRectByWidthYardPolicyCreation;
				currentRect.prevRectByWidthNonConstrainedLoading=currentRect.prevRectByWidthYardPolicyCreation;
				//length
				currentRect.nextRectByLengthNonConstrainedLoading=currentRect.nextRectByLengthYardPolicyCreation;
				currentRect.prevRectByLengthNonConstrainedLoading=currentRect.prevRectByLengthYardPolicyCreation;
				//next rect
				currentRect=currentRect.nextRectByWidthYardPolicyCreation;
			}
		}
	}
	
	void clearQueues(){
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i].clearQueue();
		}
	}
	
	void printQueues(){
		for(int i=0;i<numberOfYardLanes;i++){
			yardLanes[i].resetCurrentRectangleToFront();
		}
		//reset list of rectangles
		head=null;
		tail=null;
		length=0;
		//
		for(int i=0;i<numberOfYardLanes;i++){
			System.out.print("yard lane "+i+","+yardLanes[i].lengthOfQueue+"::");
			Rectangle currentRect=yardLanes[i].currentRect;
			while(currentRect!=null){
				//System.out.print("{"+currentRect.l+","+currentRect.w+","+currentRect.h+"},");
				System.out.print(currentRect.type+",");
				currentRect=currentRect.nextRectInQueue;
			}
			System.out.println();
		}
		
	}
	
}
