package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

//import ilog.concert.IloException;


public class Container extends JFrame  implements KeyListener{
	int containerNumber;
	/////////////////////////
	int X;
	int Y;
	int topBorder=100;
	int leftBorder=100;
	//Pays less attention to la
	//More exceptions on this deck for special requirements and vehicle types
	int Height=1000;
	int Width=1800;
	BufferedImage bf;
	//Image bgi;
	static JTextField Control;
	boolean visualise=true;//false;//
	//int colourInd=0;
	//Color[] colours={Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
	///////////////////////////
	
	double totalArea;
	
	Bin[] bins;
	int numberOfBins;
	
	double[][] cornersOfRemSpace=new double[4][2];//initially these are the dimensions of the container
	double remainingWidth;
	double remainingLength;
	
	/////////////////////
	EdgeList LEdgeList=new EdgeList(true,1,false);
	EdgeList REdgeList=new EdgeList(false,2,true);
	EdgeList TEdgeList=new EdgeList(true,3,true);
	EdgeList BEdgeList=new EdgeList(false,4,false);
	
	//arrays of these for splitting the ferry
	//int currentSection
	
	Edge[] deckLeftEdges;
	Edge[] deckRightEdges;
	Edge[] deckTopEdges;
	Edge[] deckBottomEdges;
	/////////////////////
	
	//HoldingArea HA;
	Yard YA;
	
	//static means that all instances of this object share the same copies of these
	static int numberOfVehicleSelectionQuantiles;
	static int numberOfOrientations;
	static double[] setOfQuantiles;
	static double onePixel;
	
	//
	//boolean prevColLeft;
	
	//solution representation: linked list of genes
	Solution sol;
	Solution sol2;
	//Solution solRef;//needed for swapping sol and sol2 in each generation (after selection)
	
	//objective value
	double objectiveValue;//of "sol"
	double utilisation;
	double intersectionObjVal;
	
	double packingObj;
	
	double packedVehicleArea;//of "sol"
	
	//objective values for each arrival scenario
	double[] objValPerArrivalScenario;
	double[] intersectionObjValPerArrivalScenario;
	
	//useful objective measure for second stage 
	double nonPackedVehicleArea;
	
	
	boolean printBinAndRectangleCoordinates=false;
	
	int[][] queuesPopped;//=new int[][100];
	int[][] popMoveTypes;
	int[][] stripNumber;
	int[] numberOfQueuesPopped;
	
	int[][] vMixes;//the vehicle mixes achieved by this container's solution in each scenario (packing iterations only)
	int[][][] ns;
	
	boolean feasibilityEstablishedByIP;
	boolean initialSGCKSSolFeasible;
	boolean recourseResolvedFromScratch;
	
	//yard policy iteration
	int[][] n;
	int[] vMix;//the sum of n (the committed vehicle mix)
	
	//max util feasibility check
	int[] bestVMixFeasCheck;
	double bestUtilFeasCheck;
	boolean packingFeasibleFeasCheck;
	
	//
	int[] bestCommittedVMixReference;
	double bestCommitedUtilisation;
	
	//constructor to initialise a problem instance including 
	Container(Yard YA, Random randNumGen, boolean visualise, String ferryShape, int containerNumber, double totalArea, int numberOfArrivalScenarios) throws IOException{//HoldingArea HA,
		
		//////////////
		super();
		setSize(Width,Height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		boolean invertedColours=true;
		if(invertedColours) {
			getContentPane().setBackground(Color.WHITE);
		}else {
			getContentPane().setBackground(Color.BLACK);
		}
		//setLayout(new BorderLayout());
		
		//drawPanel
		JPanel ControlPanel=new JPanel();
		Control=new JTextField(1);
		Control.addKeyListener(this);
		ControlPanel.add(Control);//
		
		
		add(ControlPanel,BorderLayout.SOUTH);//
		
		this.visualise=visualise;//false;//
		if(this.visualise){
			setVisible(true);
		}else{
			setVisible(false);
		}
		//////////////
		
		objValPerArrivalScenario=new double[numberOfArrivalScenarios];
		intersectionObjValPerArrivalScenario=new double[numberOfArrivalScenarios];
		//
		vMixes=new int[numberOfArrivalScenarios][YA.vTypes];
		ns=new int[numberOfArrivalScenarios][1][1];
		
		this.totalArea=totalArea;
		
		
		this.containerNumber=containerNumber;
		
		
		this.YA=new Yard(YA);//, randomArrivalScenarioSeed
		
		//maximum possible number of bins
		bins=new Bin[100];
		for(int i=0;i<100;i++){
			bins[i]=new Bin(YA.totalRectangles);
		}
		
		
		//random initial solution
		sol=new Solution(randNumGen);
		//second solution copy (selection and crossover performed on these)
		sol2=new Solution(randNumGen);
		//
		
		////////////////////////
		int vCount=0;
		//left edges
		BufferedReader leftEdgesR=new BufferedReader(new FileReader("simFiles".concat(ferryShape.concat("/ferryLeftEdges.txt"))));
		String line=leftEdgesR.readLine();
		leftEdgesR.close();
		String[] lineA=line.split("£");
		deckLeftEdges=new Edge[lineA.length];
		for(int i=0;i<lineA.length;i++){
		String[] line2A=lineA[i].split(";");
		double[] interval=new double[2];
		double pointPos=0;
		for(int j=0;j<2;j++){//each edge has two ends
			String[] oneEndCoords=line2A[j].split(",");
			if(j==0){
				pointPos=Double.parseDouble(oneEndCoords[0]);
			}
			interval[j]=Double.parseDouble(oneEndCoords[1]);
			//double pointPos, double[] interval, int vehicleNumber
		}
		deckLeftEdges[i]=new Edge(pointPos, interval, vCount, true);//-1, not a vehicle edge
		LEdgeList.addEdgeToLinkedList(deckLeftEdges[i]);
		vCount++;
		}
		//right edges
		BufferedReader rightEdgesR=new BufferedReader(new FileReader("simFiles".concat(ferryShape.concat("/ferryRightEdges.txt"))));
		line=rightEdgesR.readLine();
		rightEdgesR.close();
		lineA=line.split("£");
		deckRightEdges=new Edge[lineA.length];
		for(int i=0;i<lineA.length;i++){
		String[] line2A=lineA[i].split(";");
		double[] interval=new double[2];
		double pointPos=0;
		for(int j=0;j<2;j++){//each edge has two ends
			String[] oneEndCoords=line2A[j].split(",");
			if(j==0){
				pointPos=Double.parseDouble(oneEndCoords[0]);
				//
				//minX=Math.min(minX, pointPos);
			}
			interval[j]=Double.parseDouble(oneEndCoords[1]);
			//double pointPos, double[] interval, int vehicleNumber
		}
		deckRightEdges[i]=new Edge(pointPos, interval, vCount, true);//-1, not a vehicle edge
		REdgeList.addEdgeToLinkedList(deckRightEdges[i]);
		vCount++;
		}
		//top edges
		BufferedReader topEdgesR=new BufferedReader(new FileReader("simFiles".concat(ferryShape.concat("/ferryTopEdges.txt"))));
		line=topEdgesR.readLine();
		topEdgesR.close();
		lineA=line.split("£");
		deckTopEdges=new Edge[lineA.length];
		for(int i=0;i<lineA.length;i++){
		String[] line2A=lineA[i].split(";");
		double[] interval=new double[2];
		double pointPos=0;
		for(int j=0;j<2;j++){//each edge has two ends
			String[] oneEndCoords=line2A[j].split(",");
			if(j==0){
				pointPos=Double.parseDouble(oneEndCoords[1]);
			}
			interval[j]=Double.parseDouble(oneEndCoords[0]);
			//double pointPos, double[] interval, int vehicleNumber
		}
		deckTopEdges[i]=new Edge(pointPos, interval, vCount, true);//-1, not a vehicle edge
		TEdgeList.addEdgeToLinkedList(deckTopEdges[i]);
		vCount++;
		}
		//bottom edges
		BufferedReader bottomEdgesR=new BufferedReader(new FileReader("simFiles".concat(ferryShape.concat("/ferryBottomEdges.txt"))));
		line=bottomEdgesR.readLine();
		bottomEdgesR.close();
		lineA=line.split("£");
		deckBottomEdges=new Edge[lineA.length];
		for(int i=0;i<lineA.length;i++){
		String[] line2A=lineA[i].split(";");
		double[] interval=new double[2];
		double pointPos=0;
		for(int j=0;j<2;j++){//each edge has two ends
			String[] oneEndCoords=line2A[j].split(",");
			if(j==0){
				pointPos=Double.parseDouble(oneEndCoords[1]);
				//
				//minY=Math.min(minY, pointPos);
			}
			interval[j]=Double.parseDouble(oneEndCoords[0]);
			//double pointPos, double[] interval, int vehicleNumber
		}
		deckBottomEdges[i]=new Edge(pointPos, interval, vCount, true);//-1, not a vehicle edge
		BEdgeList.addEdgeToLinkedList(deckBottomEdges[i]);
		vCount++;
		}
		//set the initial edge linked list references for faster resetting
		LEdgeList.initialiseLinkedListReferences(0,0);//(minX, minY);
		REdgeList.initialiseLinkedListReferences(0,0);//(minX, minY);
		TEdgeList.initialiseLinkedListReferences(0,0);//(minY, minX);
		BEdgeList.initialiseLinkedListReferences(0,0);//(minY, minX);
		////////////////////////
		reset();
		
	}
	//bin fits (feasibility check); or only generate feasible bins
	
	
	
	
	//implement solution, ending with solution evaluation
	//while the smallest remaining rectangle fits in the remaining space, int objectiveFunctionIndex
	void implementSolution(int[] scenarioRandomSeeds, int yardIteration, boolean useRuleBasedYardPolicy, boolean breakTiesWithLeastFullLane, boolean moduloScenarios, int yardPolicyType, int[][] n, boolean useRandomReasonableBins, Random randNumGen){
		//implement the yard policy solution here (
		//if(yardIteration==1){
			if(useRuleBasedYardPolicy){
				YA.implementEvenDistributionSolution();;//check resetting stuff here (as this has previously only been used once per yard)
			}else{
				YA.implementSolution();//check resetting stuff here (as this has previously only been used once per yard)
			}
		//}
			
		objectiveValue=0;
		
		for(int i=0;i<scenarioRandomSeeds.length;i++){
			//create get yard queue IP data method below (needs to call above method
			if(moduloScenarios){
				YA.generateYardQueues(scenarioRandomSeeds[0], breakTiesWithLeastFullLane, moduloScenarios, (double)i/scenarioRandomSeeds.length, yardPolicyType, n);
			}else{
				YA.generateYardQueues(scenarioRandomSeeds[i], breakTiesWithLeastFullLane, moduloScenarios, 0, yardPolicyType, n);
			}
			
			//YA.printQueues();
			//Option: Base the bin dimension on the remaining relaxed rectangles of each type assuming a fractional allocation of the rectangles that are the best fit
			//then update the distribution before selecting the size of the next bin
			//orientation is entirely random
			double[][] relaxedRemainingRectangleAndAreasByType=YA.vCountsAndAreasAndDistributionInitial();
			//in this case the filling of the bins interferes with random reasonable. Therefore generate the random reasonable gene sequence first
			
			if(useRandomReasonableBins){
				reset();//just in time
				//read the solution and implement its instructions (as closely as possible)
				boolean aRectangleFitsInTheRemainingSpace=true;
				Gene currentGene=sol.head;
				int activeLengthOfSolution=0;//optional (swap positions of parts of the solution that are not possible with the following genes which are possible)
				while(currentGene!=null && aRectangleFitsInTheRemainingSpace){
					//
					//initialise these variables (arbitrarily from the current gene sequence)
					int value=currentGene.value;
					int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
					int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
					
					
					//select the quantile index based on the remaining rectangles distribution
					//relaxedRemainingRectangleAndAreasByType
					double randQuantileNumber=randNumGen.nextDouble();
					quantileIndex=0;
					while(randQuantileNumber>relaxedRemainingRectangleAndAreasByType[3][quantileIndex]){
						quantileIndex++;
					}
					//(1 and 2 are vitally different when considering interchangeability)
					orientation=(int)(3*randNumGen.nextDouble());//0,1,2
					//set this as the gene value so that the solution can be recovered
					currentGene.value=(orientation*numberOfVehicleSelectionQuantiles)+quantileIndex;
					
					//update the distribution by allocating vehicles in relaxed manner in efficiency first order
					//the bin size needs to be calculated first
					
					if(!generateBin(quantileIndex, orientation, i)){
						aRectangleFitsInTheRemainingSpace=false;//no more rectangles fit (part of the objective should penalise the area of the rectangles that do not fit
						//set the remaining genes to unused
						currentGene.used=false;
						//
						currentGene=currentGene.nextGene;
						while(currentGene!=null){
							currentGene.used=false;
							currentGene=currentGene.nextGene;
						}
					}else{
						currentGene.used=true;
						
						//////////////////////////
						//////////////////////////
						//update the random reasonable quantile selection distribution
						//net vehicle index orders by width
						if(useRandomReasonableBins){
							if(orientation==0){//row
								//find the length index of the vehicle type corresponding to the current 
								int LengthIndex=YA.vLengthIndOrd[quantileIndex];
								int currentVType=quantileIndex;
								double binLength=bins[numberOfBins-1].w;
								boolean remainingVTypeFound=true;
								//allocate the relaxed remaining rectangles to this bin in decreasing efficiency order. Then update the distributions.
								while(binLength>0 && remainingVTypeFound){
									remainingVTypeFound=false;
									boolean remainingVTypeExists=true;
									while(!remainingVTypeFound && remainingVTypeExists){
										if(relaxedRemainingRectangleAndAreasByType[0][currentVType]>0){
											remainingVTypeFound=true;
											//use this and update the bin length
											double usedLength=Math.min(binLength, relaxedRemainingRectangleAndAreasByType[0][currentVType]*YA.rectTypes[currentVType].w);
											binLength-=usedLength;
											relaxedRemainingRectangleAndAreasByType[0][currentVType]-=(usedLength/YA.rectTypes[currentVType].w);
										}else{
											LengthIndex++;//a not longer vehicle type (break ties with width)
											if(LengthIndex<YA.vTypes){
												currentVType=YA.vTypesDecreasingLength[LengthIndex];
											}else{
												//the following has the effect of breaking out the loop
												remainingVTypeExists=false;
											}
										}
									}
								}
							}else{//column
								//find the length index of the vehicle type corrsponding to the current 
								int WidthIndex=YA.vWidthIndOrd[quantileIndex];
								int currentVType=quantileIndex;
								double binWidth=bins[numberOfBins-1].l;
								boolean remainingVTypeFound=true;
								//allocate the relaxed remaining rectangles to this bin in decreasing efficiency order. Then update the distributions.
								while(binWidth>0 && remainingVTypeFound){
									remainingVTypeFound=false;
									boolean remainingVTypeExists=true;
									while(!remainingVTypeFound && remainingVTypeExists){
										if(relaxedRemainingRectangleAndAreasByType[0][currentVType]>0){
											remainingVTypeFound=true;
											//use this and update the bin length
											double usedLength=Math.min(binWidth, relaxedRemainingRectangleAndAreasByType[0][currentVType]*YA.rectTypes[currentVType].l);
											binWidth-=usedLength;
											relaxedRemainingRectangleAndAreasByType[0][currentVType]-=(usedLength/YA.rectTypes[currentVType].l);
										}else{
											WidthIndex++;
											if(WidthIndex<YA.vTypes){
												currentVType=YA.vTypesDecreasingWidth[WidthIndex];
											}else{
												//the following has the effect of breaking out the loop
												remainingVTypeExists=false;
											}
										}
									}
								}
							}
							//update the distribution
							double totalArea=0;
							for(int v=0;v<YA.vTypes;v++){
								relaxedRemainingRectangleAndAreasByType[1][v]=relaxedRemainingRectangleAndAreasByType[0][v]*YA.rectTypes[v].area;
								totalArea+=relaxedRemainingRectangleAndAreasByType[0][v]*YA.rectTypes[v].area;
							}
							//
							double sum=0;
							for(int v=0;v<YA.vTypes;v++){
								relaxedRemainingRectangleAndAreasByType[2][v]=relaxedRemainingRectangleAndAreasByType[1][v]/totalArea;
								sum+=relaxedRemainingRectangleAndAreasByType[2][v];
								relaxedRemainingRectangleAndAreasByType[3][v]=sum;
							}
						}
						
						//
						//////////////////////////
						/////////////////////////
						
						//next gene
						currentGene=currentGene.nextGene;
					}
				}
			}
			useRandomReasonableBins=false;
			
			
			
			
			reset();//just in time
			//read the solution and implement its instructions (as closely as possible)
			boolean aRectangleFitsInTheRemainingSpace=true;
			Gene currentGene=sol.head;
			int activeLengthOfSolution=0;//optional (swap positions of parts of the solution that are not possible with the following genes which are possible)
			while(currentGene!=null && aRectangleFitsInTheRemainingSpace){
				int value=currentGene.value;
				int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
				int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
				
				if(!fillNextBin(setOfQuantiles[quantileIndex], orientation, i)){
					aRectangleFitsInTheRemainingSpace=false;//no more rectangles fit (part of the objective should penalise the area of the rectangles that do not fit
					//set the remaining genes to unused
					currentGene.used=false;
					//
					currentGene=currentGene.nextGene;
					while(currentGene!=null){
						currentGene.used=false;
						currentGene=currentGene.nextGene;
					}
				}else{
					currentGene.used=true;
					
					//work out the quantile of the bin size and store this as "value2"
					//because the procedural approach does not specify the sizes of the bins, it specifies roughly the sizes of vehicles it want to pack next and packs those nearest to this size
					//s
					boolean quantileFound=false;
					int quantile2=0;
					if(orientation==0){
						while(!quantileFound){
							if(Math.abs(YA.rectangleDistribution[0][quantile2]-bins[numberOfBins-1].l)<0.00000000000001){
								quantileFound=true;
							}else{
								quantile2++;
							}
						}
						currentGene.value2=(orientation*numberOfVehicleSelectionQuantiles)+quantile2;
					}else{
						while(!quantileFound){
							if(Math.abs(YA.rectangleDistribution[1][quantile2]-bins[numberOfBins-1].w)<0.00000000000001){
								quantileFound=true;
							}else{
								quantile2++;
							}
						}
						currentGene.value2=(orientation*numberOfVehicleSelectionQuantiles)+quantile2;
						
						//check that the same orientation can be recovered
						/*int quantileIndex2=Math.floorMod(currentGene.value2, numberOfVehicleSelectionQuantiles);
						int orientation2=(int)Math.floor((double)currentGene.value2/numberOfVehicleSelectionQuantiles);
						int ty=-9;*/
					}
					
					
					//next gene
					currentGene=currentGene.nextGene;
				}
			}
			//activeGenesFirst (which also updates the active length of the solution). Active length is used for mutation and crossover position selection
			sol.activeGenesFirstOrder();
			
			objValPerArrivalScenario[i]=evaluateFitness();
			
			fillInVMixForCurrentScenario(i);
			
			objectiveValue+=objValPerArrivalScenario[i];
			
		}
		
		if(visualise){
			repaint();
		}
	}
	
	//implement solution, ending with solution evaluation
	//while the smallest remaining rectangle fits in the remaining space
	void implementSolutionGenerateAndOptimallyFillBins(int[] scenarioRandomSeeds, int yardIteration, int objectiveFunctionIndex, boolean useRuleBasedYardPolicy, boolean breakTiesWithLeastFullLane, boolean moduloScenarios, int yardPolicyType, int[][] n, double externalUB){
		//in the iterative case: if the current iteration is for the yard policy solution
		//implement the yard policy solution here (
		//if(yardIteration==1){
			if(useRuleBasedYardPolicy){
				YA.implementEvenDistributionSolution();;//check resetting stuff here (as this has previously only been used once per yard)
			}else{
				YA.implementSolution();//check resetting stuff here (as this has previously only been used once per yard)
			}
		//}
		//minimise the maxi mini-max
		if(objectiveFunctionIndex==0){
			//maximax
			objectiveValue=-Double.MAX_VALUE;
		}else if(objectiveFunctionIndex==1){
			//maximin
			objectiveValue=Double.MAX_VALUE;
		}else if(objectiveFunctionIndex==2){
			//mean
			objectiveValue=0;
		}
		
		for(int i=0;i<scenarioRandomSeeds.length;i++){
			//create get yard queue IP data method below (needs to call above method
			if(moduloScenarios){
				YA.generateYardQueues(scenarioRandomSeeds[0], breakTiesWithLeastFullLane, moduloScenarios, (double)i/scenarioRandomSeeds.length, yardPolicyType, n);
			}else{
				YA.generateYardQueues(scenarioRandomSeeds[i], breakTiesWithLeastFullLane, moduloScenarios, 0, yardPolicyType, n);
			}
			
			//YA.printQueues();
			
			reset();//just in time
			//read the solution and implement its instructions (as closely as possible)
			boolean aRectangleFitsInTheRemainingSpace=true;
			Gene currentGene=sol.head;
			int activeLengthOfSolution=0;//optional (swap positions of parts of the solution that are not possible with the following genes which are possible)
			while(currentGene!=null && aRectangleFitsInTheRemainingSpace){
				//
				/*activeLengthOfSolution++;
				System.out.println(activeLengthOfSolution);
				if(activeLengthOfSolution==12){
					System.out.println(activeLengthOfSolution);
				}*/
				//
				int value=currentGene.value;
				int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
				int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
				if(!generateBin(quantileIndex, orientation, i)){
					aRectangleFitsInTheRemainingSpace=false;//no more rectangles fit (part of the objective should penalise the area of the rectangles that do not fit
					//set the remaining genes to unused
					currentGene.used=false;
					//
					currentGene=currentGene.nextGene;
					while(currentGene!=null){
						currentGene.used=false;
						currentGene=currentGene.nextGene;
					}
				}else{
					currentGene.used=true;
					//next gene
					currentGene=currentGene.nextGene;
				}
			}
			//activeGenesFirst (which also updates the active length of the solution). Active length is used for mutation and crossover position selection
			sol.activeGenesFirstOrder();
			
			
			//build an enumeration tree with branch and bound logic
			//each tree is a bin (in bin number order). Initial and final rectangle remaining to pack
			//levels for vehicle types
			//branches for the number of vehicle types
			//LP relaxation utilisation upper bound
			//sort branches in decreasing frequency order
			//The structure is very similar to the feasibility algorithm except this time the queues are being ignored
			//(The idea is to test the feasibility based approach by comparing it to the case where packing is initially optimised whilst ignoring the arrival process
			
			BinPackingAlgorithm BPA=new BinPackingAlgorithm();
			int[][] optBPSol=BPA.packBins(bins, numberOfBins, YA.yardLanes, YA.rectangleDistribution, externalUB);
			
			if(optBPSol[0][0]>-1){
				//add the rectangles to the bins using the optimal and feasible solution (feasible to highlight that additional feasibility check is not necessary provide the above algorithm has been implemented correctly)
				for(int b=0;b<optBPSol.length;b++){
					bins[b].numberOfRectangles=0;
					for(int v=0;v<optBPSol[b].length;v++){
						int numberOfRectangles=optBPSol[b][v];
						for(int f=0;f<numberOfRectangles;f++){
							Rectangle rect=YA.yardLanes[v].popQueue();
							bins[b].rectangles[bins[b].numberOfRectangles]=rect;
							bins[b].numberOfRectangles++;
						}
						
					}
				}
				
				
				
				objValPerArrivalScenario[i]=evaluateFitness();
				
				fillInVMixForCurrentScenario(i);
				
				//getVehicleCountsByCut();
				
				if(objectiveFunctionIndex==0){
					//maximax
					objectiveValue=Math.max(objectiveValue, objValPerArrivalScenario[i]);
				}else if(objectiveFunctionIndex==1){
					//maximin
					objectiveValue=Math.min(objectiveValue, objValPerArrivalScenario[i]);
				}else if(objectiveFunctionIndex==2){
					//mean
					objectiveValue+=objValPerArrivalScenario[i];
				}
			}
			
		}
		
		if(visualise){
			repaint();
		}
	}
	
	//implement solution, ending with solution evaluation
	//while the smallest remaining rectangle fits in the remaining space
	void implementSolutionQueueConstrainedOptimallyFillBins(int[] scenarioRandomSeeds, int yardIteration, int objectiveFunctionIndex, boolean useRuleBasedYardPolicy, boolean breakTiesWithLeastFullLane, boolean moduloScenarios, int yardPolicyType, int[][] n, double externalUB, boolean useRandomReasonableBins, Random randNumGen, boolean useValues2) throws Exception{
		
		//in the iterative case: if the current iteration is for the yard policy solution
		//implement the yard policy solution here (
		//if(yardIteration==1){
			if(useRuleBasedYardPolicy){
				YA.implementEvenDistributionSolution();;//check resetting stuff here (as this has previously only been used once per yard)
			}else{
				YA.implementSolution();//check resetting stuff here (as this has previously only been used once per yard)
			}
		//}
		//minimise the maxi mini-max
		if(objectiveFunctionIndex==0){
			//maximax
			objectiveValue=-Double.MAX_VALUE;
		}else if(objectiveFunctionIndex==1){
			//maximin
			objectiveValue=Double.MAX_VALUE;
		}else if(objectiveFunctionIndex==2){
			//mean
			objectiveValue=0;
		}
		
		double extUBCopy=externalUB;
		
		for(int i=0;i<scenarioRandomSeeds.length;i++){
			
			//different externalUBs are required for each scenarios (different scenario different problem)
			
			//create get yard queue IP data method below (needs to call above method
			if(moduloScenarios){
				YA.generateYardQueues(scenarioRandomSeeds[0], breakTiesWithLeastFullLane, moduloScenarios, (double)i/scenarioRandomSeeds.length, yardPolicyType, n);
			}else{
				YA.generateYardQueues(scenarioRandomSeeds[i], breakTiesWithLeastFullLane, moduloScenarios, 0, yardPolicyType, n);
			}
			
			//YA.printQueues();
			
			reset();//just in time
			//read the solution and implement its instructions (as closely as possible)
			boolean aRectangleFitsInTheRemainingSpace=true;
			Gene currentGene=sol.head;
			int activeLengthOfSolution=0;//optional (swap positions of parts of the solution that are not possible with the following genes which are possible)
			
			//Option: Base the bin dimension on the remaining relaxed rectangles of each type assuming a fractional allocation of the rectangles that are the best fit
			//then update the distribution before selecting the size of the next bin
			//orientation is entirely random
			double[][] relaxedRemainingRectangleAndAreasByType=YA.vCountsAndAreasAndDistributionInitial();
			
			
			
			
			while(currentGene!=null && aRectangleFitsInTheRemainingSpace){
				//
				int value=currentGene.value;
				int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
				int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
				
				if(useValues2){
					value=currentGene.value2;
					quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
					orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
				}
				
				if(useRandomReasonableBins){
					//select the quantile index based on the remaining rectangles distribution
					//relaxedRemainingRectangleAndAreasByType
					double randQuantileNumber=randNumGen.nextDouble();
					quantileIndex=0;
					while(randQuantileNumber>relaxedRemainingRectangleAndAreasByType[3][quantileIndex]){
						quantileIndex++;
					}
					//(1 and 2 are vitally different when considering interchangeability)
					orientation=(int)(3*randNumGen.nextDouble());//0,1,2
					//set this as the gene value so that the solution can be recovered
					currentGene.value=(orientation*numberOfVehicleSelectionQuantiles)+quantileIndex;
					
				}
				
				if(!generateBin(quantileIndex, orientation, i)){
					aRectangleFitsInTheRemainingSpace=false;//no more rectangles fit (part of the objective should penalise the area of the rectangles that do not fit
					//set the remaining genes to unused
					currentGene.used=false;
					//
					currentGene=currentGene.nextGene;
					while(currentGene!=null){
						currentGene.used=false;
						currentGene=currentGene.nextGene;
					}
				}else{
					currentGene.used=true;
					
					//////////////////////////
					//////////////////////////
					//update the random reasonable quantile selection distribution
					//net vehicle index orders by width
					if(useRandomReasonableBins){
						if(orientation==0){//row
							//find the length index of the vehicle type corresponding to the current 
							int LengthIndex=YA.vLengthIndOrd[quantileIndex];
							int currentVType=quantileIndex;
							double binLength=bins[numberOfBins-1].w;
							boolean remainingVTypeFound=true;
							//allocate the relaxed remaining rectangles to this bin in decreasing efficiency order. Then update the distributions.
							while(binLength>0 && remainingVTypeFound){
								remainingVTypeFound=false;
								boolean remainingVTypeExists=true;
								while(!remainingVTypeFound && remainingVTypeExists){
									if(relaxedRemainingRectangleAndAreasByType[0][currentVType]>0){
										remainingVTypeFound=true;
										//use this and update the bin length
										double usedLength=Math.min(binLength, relaxedRemainingRectangleAndAreasByType[0][currentVType]*YA.rectTypes[currentVType].w);
										binLength-=usedLength;
										relaxedRemainingRectangleAndAreasByType[0][currentVType]-=(usedLength/YA.rectTypes[currentVType].w);
									}else{
										LengthIndex++;//a not longer vehicle type (break ties with width)
										if(LengthIndex<YA.vTypes){
											currentVType=YA.vTypesDecreasingLength[LengthIndex];
										}else{
											//the following has the effect of breaking out the loop
											remainingVTypeExists=false;
										}
									}
								}
							}
						}else{//column
							//find the length index of the vehicle type corrsponding to the current 
							int WidthIndex=YA.vWidthIndOrd[quantileIndex];
							int currentVType=quantileIndex;
							double binWidth=bins[numberOfBins-1].l;
							boolean remainingVTypeFound=true;
							//allocate the relaxed remaining rectangles to this bin in decreasing efficiency order. Then update the distributions.
							while(binWidth>0 && remainingVTypeFound){
								remainingVTypeFound=false;
								boolean remainingVTypeExists=true;
								while(!remainingVTypeFound && remainingVTypeExists){
									if(relaxedRemainingRectangleAndAreasByType[0][currentVType]>0){
										remainingVTypeFound=true;
										//use this and update the bin length
										double usedLength=Math.min(binWidth, relaxedRemainingRectangleAndAreasByType[0][currentVType]*YA.rectTypes[currentVType].l);
										binWidth-=usedLength;
										relaxedRemainingRectangleAndAreasByType[0][currentVType]-=(usedLength/YA.rectTypes[currentVType].l);
									}else{
										WidthIndex++;
										if(WidthIndex<YA.vTypes){
											currentVType=YA.vTypesDecreasingWidth[WidthIndex];
										}else{
											//the following has the effect of breaking out the loop
											remainingVTypeExists=false;
										}
									}
								}
							}
						}
						//update the distribution
						double totalArea=0;
						for(int v=0;v<YA.vTypes;v++){
							relaxedRemainingRectangleAndAreasByType[1][v]=relaxedRemainingRectangleAndAreasByType[0][v]*YA.rectTypes[v].area;
							totalArea+=relaxedRemainingRectangleAndAreasByType[0][v]*YA.rectTypes[v].area;
						}
						//
						double sum=0;
						for(int v=0;v<YA.vTypes;v++){
							relaxedRemainingRectangleAndAreasByType[2][v]=relaxedRemainingRectangleAndAreasByType[1][v]/totalArea;
							sum+=relaxedRemainingRectangleAndAreasByType[2][v];
							relaxedRemainingRectangleAndAreasByType[3][v]=sum;
						}
					}
					
					//
					//////////////////////////
					/////////////////////////
					
					//next gene
					currentGene=currentGene.nextGene;
				}
			}
			//activeGenesFirst (which also updates the active length of the solution). Active length is used for mutation and crossover position selection
			sol.activeGenesFirstOrder();
			
			
			//build an enumeration tree with branch and bound logic
			//each tree is a bin (in bin number order). Initial and final rectangle remaining to pack
			//levels for vehicle types
			//branches for the number of vehicle types
			//LP relaxation utilisation upper bound
			//sort branches in decreasing frequency order
			//The structure is very similar to the feasibility algorithm except this time the queues are being ignored
			//(The idea is to test the feasibility based approach by comparing it to the case where packing is initially optimised whilst ignoring the arrival process
			
			//BinPackingAlgorithm BPA=new BinPackingAlgorithm();
			QueueConstrainedBinPackingAlgorithm QCBPA=new QueueConstrainedBinPackingAlgorithm();
			
			int[][] optBPSol=QCBPA.packBins(bins, numberOfBins, YA.yardLanes, YA.rectangleDistribution, externalUB);
			
			if(optBPSol[0][0]>-1){
				//add the rectangles to the bins using the optimal and feasible solution (feasible to highlight that additional feasibility check is not necessary provide the above algorithm has been implemented correctly)
				for(int b=0;b<numberOfBins;b++){
					bins[b].numberOfRectangles=0;
				}
				for(int p=0;p<optBPSol.length;p++){
					Rectangle rect=YA.yardLanes[optBPSol[p][0]].popQueue();
					rect.binNumber=optBPSol[p][1];
					bins[optBPSol[p][1]].rectangles[bins[optBPSol[p][1]].numberOfRectangles]=rect;
					bins[optBPSol[p][1]].numberOfRectangles++;
				}
				
				objValPerArrivalScenario[i]=evaluateFitness();
				
				fillInVMixForCurrentScenario(i);
				
				//getVehicleCountsByCut();
				
				if(objectiveFunctionIndex==0){
					//maximax
					objectiveValue=Math.max(objectiveValue, objValPerArrivalScenario[i]);
				}else if(objectiveFunctionIndex==1){
					//maximin
					objectiveValue=Math.min(objectiveValue, objValPerArrivalScenario[i]);
				}else if(objectiveFunctionIndex==2){
					//mean
					objectiveValue+=objValPerArrivalScenario[i];
				}
			}
			
			//
			externalUB=extUBCopy;
			
		}
		
		if(visualise){
			repaint();
		}
	}
	
	//the return value indicates whether it is possible to generate another bin after this
	boolean generateBin(int quantileIndex, int orientation, int arrivalScenario){//to test step into this method
		boolean byWidth=(orientation==0);
		//get remaining dimensions and corner positions for the given strip type
		getStripDimensionsAndCorners(orientation);
		//
		if(YA.aRectangleFitsRemainingSpace(remainingWidth, remainingLength)){
			//set the allocated length and width of the next bin (just the remaining space dimensions)
			bins[numberOfBins].allocatedLength=remainingLength;
			bins[numberOfBins].allocatedWidth=remainingWidth;
			//set bin dimensions
			if(byWidth){
				bins[numberOfBins].setBinDimensions(orientation, Math.min(remainingLength, YA.rectangleDistribution[0][quantileIndex]));
			}else{
				bins[numberOfBins].setBinDimensions(orientation, Math.min(remainingWidth, YA.rectangleDistribution[1][quantileIndex]));
			}
			
			
			//2) update corners, 1) set bin top-left and 3) update remaining space dimensions
			if(orientation==0){
				//bottom
				//top-left
				bins[numberOfBins].pos[0]=cornersOfRemSpace[0][0];	bins[numberOfBins].pos[1]=cornersOfRemSpace[0][1];
				double lengthOfBin=bins[numberOfBins].l;//onePixel
				//update edge lists
				updateEdgeListAfterNewStrip(orientation, cornersOfRemSpace[0][1]+lengthOfBin);
			}else if(orientation==1){
				//left
				//top-left
				bins[numberOfBins].pos[0]=cornersOfRemSpace[0][0];	bins[numberOfBins].pos[1]=cornersOfRemSpace[0][1];
				double widthOfBin=bins[numberOfBins].w;//onePixel
				//update edge lists
				updateEdgeListAfterNewStrip(orientation, cornersOfRemSpace[0][0]+widthOfBin);
			}else{//orientation==2
				//right
				double widthOfBin=bins[numberOfBins].w;//onePixel
				bins[numberOfBins].pos[0]=cornersOfRemSpace[1][0]-widthOfBin;	bins[numberOfBins].pos[1]=cornersOfRemSpace[0][1];
				//update edge lists
				updateEdgeListAfterNewStrip(orientation, cornersOfRemSpace[1][0]-widthOfBin);
			}
			//and finally increment the number of bins
			numberOfBins++;
			
			//return false if
			if(BEdgeList.length==0 || TEdgeList.length==0 || REdgeList.length==0 || LEdgeList.length==0){
				return false;
			}else{
				return true;
			}
			
		}else{
			return false;
		}
		
	}
	
	boolean fillNextBin(double quantile, int orientation, int arrivalScenario){//to test step into this method
		boolean rectangleAdded=false;
		boolean byWidth=(orientation==0);
		//get remaining dimensions and corner positions for the given strip type
		getStripDimensionsAndCorners(orientation);
		//
		if(YA.aRectangleFitsRemainingSpace(remainingWidth, remainingLength)){
			//set the allocated length and width of the next bin (just the remaining space dimensions)
			bins[numberOfBins].allocatedLength=remainingLength;
			bins[numberOfBins].allocatedWidth=remainingWidth;
			//
			Rectangle[] headTailOfQuantile=null;
			if(YA.nonConstrainedLoading){
				headTailOfQuantile=YA.getQuantileNonConstrainedLoading(byWidth, remainingLength, remainingWidth, quantile);
			}else{
				//byWidth means the same as forNewRow  (the difference being that the former refers to sorting rectangles the latter refers to strip orientation
				double quantileDimension=YA.getQuantileDimension(byWidth, remainingLength, remainingWidth, quantile);
				headTailOfQuantile=new Rectangle[2];
				if(byWidth){
					headTailOfQuantile=YA.getRectanglesForStrip(byWidth, Math.min(remainingLength, quantileDimension), remainingWidth);
				}else{
					headTailOfQuantile=YA.getRectanglesForStrip(byWidth, remainingLength, Math.min(remainingWidth, quantileDimension));
				}
			}
			
			
			//
			if(headTailOfQuantile[0]!=null){
				//orientation of bin
				bins[numberOfBins].orientation=orientation;
				//increasing: useful for shuffling (find the correct word)
				//bins[numberOfBins].increasing=;//make this opposite to the previous bin of the same orientation, shift up/down as approriate and slide rectangles across, set individual corrdinates and update remaining space
				//treat as possible extension, however in a vehicle parking scenario this procedure may not be appropriate
				rectangleAdded=true;
				//
				
				//add the rectangles to the next bin
				int numberOfRectangles=0;
				Rectangle currentRectangle=headTailOfQuantile[0];
				while(currentRectangle!=null){
					bins[numberOfBins].rectangles[numberOfRectangles]=currentRectangle;
					currentRectangle.binNumber=numberOfBins;
					//
					numberOfRectangles++;
					currentRectangle=currentRectangle.nextRect;
				}
				//
				bins[numberOfBins].numberOfRectangles=numberOfRectangles;//
				if(YA.nonConstrainedLoading){
					//and remove the rectangles from the holding area
					YA.removeRectanglesSetByGetQuantileNonConstrainedLoading(headTailOfQuantile);//yard version has already reset the rectangles at the fronts of the queues
				}
				
				//get dimensions
				bins[numberOfBins].getBinDimensions();
				//2) update corners, 1) set bin top-left and 3) update remaining space dimensions
				if(orientation==0){
					//bottom
					//top-left
					bins[numberOfBins].pos[0]=cornersOfRemSpace[0][0];	bins[numberOfBins].pos[1]=cornersOfRemSpace[0][1];
					double lengthOfBin=bins[numberOfBins].l;//onePixel
					//update edge lists
					updateEdgeListAfterNewStrip(orientation, cornersOfRemSpace[0][1]+lengthOfBin);
				}else if(orientation==1){
					//left
					//top-left
					bins[numberOfBins].pos[0]=cornersOfRemSpace[0][0];	bins[numberOfBins].pos[1]=cornersOfRemSpace[0][1];
					double widthOfBin=bins[numberOfBins].w;//onePixel
					//update edge lists
					updateEdgeListAfterNewStrip(orientation, cornersOfRemSpace[0][0]+widthOfBin);
				}else{//orientation==2
					//right
					double widthOfBin=bins[numberOfBins].w;//onePixel
					bins[numberOfBins].pos[0]=cornersOfRemSpace[1][0]-widthOfBin;	bins[numberOfBins].pos[1]=cornersOfRemSpace[0][1];
					//update edge lists
					updateEdgeListAfterNewStrip(orientation, cornersOfRemSpace[1][0]-widthOfBin);
				}
				//and finally increment the number of bins
				numberOfBins++;
			}
			return rectangleAdded;
		}else{
			return false;
		}
		
	}
	
	//get corners and remaining dimensions for strip
	void getStripDimensionsAndCorners(int orientation){
		switch(orientation){
		case 0:
			//the bottom edge
			Edge bEdge=BEdgeList.head;
			//find the maximum length, nearest opposite edge interval overlap
			double distToNearestOppositeEdge=TEdgeList.distanceToNearestOppositeEdge(bEdge, true);
			//max dimensions and corner positions
			cornersOfRemSpace[0][0]=bEdge.interval[0];	cornersOfRemSpace[0][1]=bEdge.pointPos;
			cornersOfRemSpace[1][0]=bEdge.interval[1];	cornersOfRemSpace[1][1]=bEdge.pointPos;
			cornersOfRemSpace[2][0]=bEdge.interval[0];	cornersOfRemSpace[2][1]=bEdge.pointPos+distToNearestOppositeEdge;
			cornersOfRemSpace[3][0]=bEdge.interval[1];	cornersOfRemSpace[3][1]=bEdge.pointPos+distToNearestOppositeEdge;
			remainingWidth=bEdge.lengthOfEdge();
			remainingLength=distToNearestOppositeEdge;
			break;
		case 1:
			//left column (right edge)
			Edge rEdge=REdgeList.head;
			//find the maximum length, nearest opposite edge interval overlap
			distToNearestOppositeEdge=LEdgeList.distanceToNearestOppositeEdge(rEdge, true);
			//max dimensions and corner positions
			cornersOfRemSpace[0][1]=rEdge.interval[0];	cornersOfRemSpace[0][0]=rEdge.pointPos;
			cornersOfRemSpace[1][1]=rEdge.interval[0];	cornersOfRemSpace[1][0]=rEdge.pointPos+distToNearestOppositeEdge;
			cornersOfRemSpace[2][1]=rEdge.interval[1];	cornersOfRemSpace[2][0]=rEdge.pointPos;
			cornersOfRemSpace[3][1]=rEdge.interval[1];	cornersOfRemSpace[3][0]=rEdge.pointPos+distToNearestOppositeEdge;
			remainingWidth=distToNearestOppositeEdge;
			remainingLength=rEdge.lengthOfEdge();
			//jostling parameters
			//columnMoveTopEdgeOverhang=TEdgeList.lengthOfEdgeWhichIntersectsThisCoordinate(rEdge.interval[1], rEdge.pointPos);
			//columnMoveBottomEdgeOverhang=BEdgeList.lengthOfEdgeWhichIntersectsThisCoordinate(rEdge.interval[0], rEdge.pointPos);
			break;
		case 2:
			//right column (left edge)
			Edge lEdge=LEdgeList.tail;
			//find the maximum length, nearest opposite edge interval overlap
			distToNearestOppositeEdge=REdgeList.distanceToNearestOppositeEdge(lEdge, false);
			//max dimensions and corner positions
			cornersOfRemSpace[0][1]=lEdge.interval[0];	cornersOfRemSpace[0][0]=lEdge.pointPos-distToNearestOppositeEdge;
			cornersOfRemSpace[1][1]=lEdge.interval[0];	cornersOfRemSpace[1][0]=lEdge.pointPos;
			cornersOfRemSpace[2][1]=lEdge.interval[1];	cornersOfRemSpace[2][0]=lEdge.pointPos-distToNearestOppositeEdge;
			cornersOfRemSpace[3][1]=lEdge.interval[1];	cornersOfRemSpace[3][0]=lEdge.pointPos;
			remainingWidth=distToNearestOppositeEdge;
			remainingLength=lEdge.lengthOfEdge();
			//jostling parameters
			//columnMoveTopEdgeOverhang=TEdgeList.lengthOfEdgeWhichIntersectsThisCoordinate(lEdge.interval[1], lEdge.pointPos);
			//columnMoveBottomEdgeOverhang=BEdgeList.lengthOfEdgeWhichIntersectsThisCoordinate(lEdge.interval[0], lEdge.pointPos);
			break;
		}
		/*if(cornersOfRemSpace[0][1]<0){
			System.out.println();
		}*/
	}
	
	//update edges after a strip has been used
	//remove edges that are now outside of the remaining rectangle
	//update the point position and interval of the extremal edge for the given strip type
	void updateEdgeListAfterNewStrip(int orientation, double newPointPosition){
		switch(orientation){
		case 0:
			//the bottom edge
			Edge bEdge=BEdgeList.head;
			//new point position
			bEdge.pointPos=newPointPosition;
			//update intervals
			double initialInterval0=bEdge.interval[0];
			double initialInterval1=bEdge.interval[1];
			//[0]
			bEdge.interval[0]=REdgeList.pointPosOfNearestOppositeEdge(initialInterval1, newPointPosition, true);
			//[1]
			bEdge.interval[1]=LEdgeList.pointPosOfNearestOppositeEdge(initialInterval0, newPointPosition, false);
			//remove edges that are outside of the remaining space
			BEdgeList.removeEdgesBeyondHeadOrTail(true);
			//remove left and right edges with intervals both less than "newPointPosition"
			REdgeList.removeEdgeWithIntervalsOutsideRemainingSpace(newPointPosition, true);
			LEdgeList.removeEdgeWithIntervalsOutsideRemainingSpace(newPointPosition, true);
			//the interval[0] of left and right edges need updating
			REdgeList.setIntervalOfEdgesInList(0, newPointPosition, false);
			LEdgeList.setIntervalOfEdgesInList(0, newPointPosition, false);
			break;
		case 1:
			//left column (right edge)
			Edge rEdge=REdgeList.head;
			//new point position
			rEdge.pointPos=newPointPosition;
			//update intervals
			initialInterval0=rEdge.interval[0];
			initialInterval1=rEdge.interval[1];
			//[0]
			rEdge.interval[0]=BEdgeList.pointPosOfNearestOppositeEdge(initialInterval1, newPointPosition, true);
			//[1]
			rEdge.interval[1]=TEdgeList.pointPosOfNearestOppositeEdge(initialInterval0, newPointPosition, false);
			//remove edges that are outside of the remaining space
			REdgeList.removeEdgesBeyondHeadOrTail(true);
			//remove left and right edges with intervals both less than "newPointPosition"
			BEdgeList.removeEdgeWithIntervalsOutsideRemainingSpace(newPointPosition, true);
			TEdgeList.removeEdgeWithIntervalsOutsideRemainingSpace(newPointPosition, true);
			//the interval[0] of top and bottom edges need updating
			BEdgeList.setIntervalOfEdgesInList(0, newPointPosition, false);
			TEdgeList.setIntervalOfEdgesInList(0, newPointPosition, false);
			break;
		case 2:
			//right column (left edge)
			Edge lEdge=LEdgeList.tail;
			//new point position
			lEdge.pointPos=newPointPosition;
			//update intervals
			initialInterval0=lEdge.interval[0];
			initialInterval1=lEdge.interval[1];
			//[0]
			lEdge.interval[0]=BEdgeList.pointPosOfNearestOppositeEdge(initialInterval1, newPointPosition, true);
			//[1]
			lEdge.interval[1]=TEdgeList.pointPosOfNearestOppositeEdge(initialInterval0, newPointPosition, false);
			//remove edges that are outside of the remaining space
			LEdgeList.removeEdgesBeyondHeadOrTail(false);
			//remove left and right edges with intervals both less than "newPointPosition"
			BEdgeList.removeEdgeWithIntervalsOutsideRemainingSpace(newPointPosition, false);
			TEdgeList.removeEdgeWithIntervalsOutsideRemainingSpace(newPointPosition, false);
			//
			BEdgeList.setIntervalOfEdgesInList(1, newPointPosition, true);
			TEdgeList.setIntervalOfEdgesInList(1, newPointPosition, true);
			
			break;
		}
	}
	
	//evaluate solution (including wasted space in the set of bins, for the case of zero shuffling)
	double evaluateFitness(){
		double objectiveValue=0;
		nonPackedVehicleArea=YA.areaOfRectanglesStillInQueue();

		
		utilisation=0;
		for(int i=0;i<numberOfBins;i++){
			
			//bottom row (shift x-coordinate of "currentTopLeft" right)
			int numberOfRectangles=bins[i].numberOfRectangles;
			for(int j=0;j<numberOfRectangles;j++){
				utilisation+=bins[i].rectangles[j].area;
				
			}
		}
		packedVehicleArea=utilisation;
		//
		objectiveValue=utilisation;
		//
		utilisation/=totalArea;//ferry utilisation
		
		return objectiveValue;//area of packed vehicles
	}
	
	int[][] getVehicleCountsByCut(){
		
		//account for left/right consecutive column interchangeability here (optional (good effect to show?
		
		int[][] vCountsByCut=new int[numberOfBins][YA.vTypes];
		//
		for(int i=0;i<numberOfBins;i++){
			for(int k=0;k<bins[i].numberOfRectangles;k++){
				vCountsByCut[i][bins[i].rectangles[k].type]++;
			}
		}
		//
		return vCountsByCut;
	}
	
	void fillInVMixForCurrentScenario(int scenarioNumber){
		
		ns[scenarioNumber]=new int[numberOfBins][YA.vTypes];
		//reset current scenario number vmix
		for(int v=0;v<vMixes[scenarioNumber].length;v++){
			vMixes[scenarioNumber][v]=0;
		}
		for(int i=0;i<numberOfBins;i++){
			for(int k=0;k<bins[i].numberOfRectangles;k++){
				vMixes[scenarioNumber][bins[i].rectangles[k].type]++;
				ns[scenarioNumber][i][bins[i].rectangles[k].type]++;
			}
		}
	}
	
	//the length of queues array can be worked out later from the dimensions of the arrya that is returned from this method
	//w
	int[][][] getYardQueueIndicatorMatrix(int randSeed, boolean breakTiesWithLeastFullLane, boolean moduloScenarios, double moduloFraction, int yardPolicyType, int[][] n){
		YA.generateYardQueues(randSeed, breakTiesWithLeastFullLane, moduloScenarios,  moduloFraction, yardPolicyType, n);
		int[][][] yardQueueIndicatorMatrix=new int[YA.yardLanes.length][1][1];
		for(int i=0;i<YA.yardLanes.length;i++){
			Queue q=YA.yardLanes[i];
			//length of queue
			yardQueueIndicatorMatrix[i]=new int[YA.yardLanes[i].length][YA.vTypes];
			//indicate which vehicle type is in each position in this queue
			int j=0;
			Rectangle currentRect=q.head;
			while(currentRect!=null){
				yardQueueIndicatorMatrix[i][j][currentRect.type]=1;
				//
				j++;
				currentRect=currentRect.nextRectInQueue;
			}
		}
		return yardQueueIndicatorMatrix;
	}
	
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
			int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
			int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
			System.out.print(currentGene.value+" ");
			
			if(orientation==0){
				System.out.print("(row),");
			}else if(orientation==1){
				System.out.print("(left col),");
			}else{
				System.out.print("(right col),");
			}
			
			geneCounter++;
			if(geneCounter==sol.activeLength){
				System.out.print("|");
			}
			currentGene=currentGene.nextGene;
		}
		System.out.println();
	}
	
	//iterative version
	void printSol(int solNumber, int yardIteration){
		if(yardIteration==0){
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
				int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
				int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
				System.out.print(currentGene.value+" ");
				
				if(orientation==0){
					System.out.print("(row),");
				}else if(orientation==1){
					System.out.print("(left col),");
				}else{
					System.out.print("(right col),");
				}
				
				geneCounter++;
				if(geneCounter==sol.activeLength){
					System.out.print("|");
				}
				currentGene=currentGene.nextGene;
			}
			System.out.println();
		}else{
			YA.printSol(solNumber);
		}
		
	}
	
	void reset(){
		numberOfBins=0;
		
		LEdgeList.reinitialiseLinkedList();
		REdgeList.reinitialiseLinkedList();
		TEdgeList.reinitialiseLinkedList();
		BEdgeList.reinitialiseLinkedList();
		
		YA.resetQueues();
	}
	
	//make sol2 a copy of the sol gene sequence
	void sol2EqualsSol(){
		sol2.copyGeneSequence(sol);
	}
	
	void sol2EqualsSol(int yardIteration){
		if(yardIteration==0){
			sol2.copyGeneSequence(sol);
		}else{
			YA.sol2.copyGeneSequence(YA.sol);
		}
		
	}
	
	//
	@Override
	public void keyPressed(KeyEvent a) {
		// TODO Auto-generated method stub
		int keyCommand=a.getKeyCode();
		if(a.getSource().equals(Control)){
			if(keyCommand==KeyEvent.VK_ENTER){
				
			}else if(keyCommand==KeyEvent.VK_U){
				//E.removeVehicle();
			}else if(keyCommand==KeyEvent.VK_UP){
				//E.forwards();
				Y--;
			}else if(keyCommand==KeyEvent.VK_DOWN){
				//E.backwards();
				Y++;
			}else if(keyCommand==KeyEvent.VK_LEFT){
				//E.left();
				X--;
			}else if(keyCommand==KeyEvent.VK_RIGHT){
				//E.right();
				X++;
			}else if(keyCommand==KeyEvent.VK_W){
				Y-=10;
			}else if(keyCommand==KeyEvent.VK_S){
				Y+=10;
			}else if(keyCommand==KeyEvent.VK_A){
				X-=10;
			}else if(keyCommand==KeyEvent.VK_D){
				X+=10;
			}
			this.repaint();
		}
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void paint(Graphics g){
		bf=new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		try{
			animation(bf.getGraphics());//
			g.drawImage(bf, 0, 0, null);
		}catch(Exception e){
			//System.out.
			e.printStackTrace();
		}
	}
	//public void paint(Graphics g){ throws IOException
	void animation(Graphics g){
		super.paint(g);
		String fontChoice="ROMAN_BASELINE";//"TimesRoman";
		g.setFont(new Font(fontChoice,Font.BOLD,15));//|Font.ITALIC"Serif"
		//
		boolean floorNotRound=true;
		
		boolean invertedColours=true;
		if(invertedColours) {
			g.setColor(Color.ORANGE);
		}else {
			g.setColor(Color.GREEN);
		}
		
		
		
		/*g.drawLine(X-5, Y, X+5, Y);
		g.drawLine(X, Y-5, X, Y+5);
		g.drawString("X=".concat(String.valueOf(X)), 260, 800);
		g.drawString("Y=".concat(String.valueOf(Y)), 260, 850);*/
		
		//Ferry outline
		Edge currentEdge=LEdgeList.initialHead;
		while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
			
			/*String coordString1=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[0])));
			g.drawString(coordString1,(int)Math.round(currentEdge.initialPointPos)+40, (int)Math.round(currentEdge.initialInterval[0]));
			String coordString2=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[1])));
			g.drawString(coordString2,(int)Math.round(currentEdge.initialPointPos)+140, (int)Math.round(currentEdge.initialInterval[1]));
			*///
			g.drawLine((int)Math.round(currentEdge.initialPointPos), (int)Math.round(currentEdge.initialInterval[0]), (int)Math.round(currentEdge.initialPointPos), (int)Math.round(currentEdge.initialInterval[1]));
			currentEdge=currentEdge.initialNextEdge;
		}
		currentEdge=REdgeList.initialHead;
		while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
			/*String coordString1=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[0])));
			g.drawString(coordString1,(int)Math.round(currentEdge.initialPointPos)-100, (int)Math.round(currentEdge.initialInterval[0]));
			String coordString2=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[1])));
			g.drawString(coordString2,(int)Math.round(currentEdge.initialPointPos)-200, (int)Math.round(currentEdge.initialInterval[1]));
			*///
			g.drawLine((int)Math.round(currentEdge.initialPointPos), (int)Math.round(currentEdge.initialInterval[0]), (int)Math.round(currentEdge.initialPointPos), (int)Math.round(currentEdge.initialInterval[1]));
			currentEdge=currentEdge.initialNextEdge;
		}
		//
		currentEdge=BEdgeList.initialHead;
		while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
			g.drawLine((int)Math.round(currentEdge.initialInterval[0]), (int)Math.round(currentEdge.initialPointPos), (int)Math.round(currentEdge.initialInterval[1]), (int)Math.round(currentEdge.initialPointPos));
			currentEdge=currentEdge.initialNextEdge;
		}
		currentEdge=TEdgeList.initialHead;
		while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
			g.drawLine((int)Math.round(currentEdge.initialInterval[0]), (int)Math.round(currentEdge.initialPointPos), (int)Math.round(currentEdge.initialInterval[1]), (int)Math.round(currentEdge.initialPointPos));
			currentEdge=currentEdge.initialNextEdge;
		}
		int[] v_count=new int[YA.vCountByType.length];
		//
		for(int i=0;i<numberOfBins;i++){
			double[] currentTopLeft=new double[2];
			currentTopLeft[0]=bins[i].pos[0];
			currentTopLeft[1]=bins[i].pos[1];
			
			if(printBinAndRectangleCoordinates){
				System.out.println("bin_"+i+", width="+bins[i].w+", length="+bins[i].l+", top left coordinate={"+currentTopLeft[0]+","+currentTopLeft[1]+"}");
			}
			
			/*g.setColor(Color.CYAN);
			g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(bins[i].w/onePixel), (int)Math.round(bins[i].l/onePixel));*/
			if(bins[i].orientation==0){
				//bottom row (shift x-coordinate of "currentTopLeft" right)
				int numberOfRectangles=bins[i].numberOfRectangles;
				for(int j=0;j<numberOfRectangles;j++){
					Rectangle currentRectangle=bins[i].rectangles[j];
					
					if(printBinAndRectangleCoordinates){
						v_count[currentRectangle.type]++;
						System.out.println("rectangle_"+i+"_"+j+", type="+currentRectangle.type+", width="+currentRectangle.w+", length="+currentRectangle.l+", top left coordinate={"+currentTopLeft[0]+","+currentTopLeft[1]+"}");
					}
					
					g.setColor(currentRectangle.colour);
					if(floorNotRound) {
						g.fillRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(currentRectangle.w), (int)Math.floor(currentRectangle.l));
					}else {
						g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					}
					
					if(invertedColours) {
						g.setColor(Color.ORANGE);
						g.setColor(Color.BLACK);
					}else {
						g.setColor(Color.BLACK);
					}
					if(floorNotRound) {
						g.drawRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(currentRectangle.w), (int)Math.floor(currentRectangle.l));
					}else {
						g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					}
					
					g.setColor(Color.BLACK);//"bin=".concat()
					if(i>=10) {
						g.setFont(new Font(fontChoice,Font.BOLD,11));
						g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+2, (int)Math.round(currentTopLeft[1])+25);
					}else {
						g.setFont(new Font(fontChoice,Font.BOLD,15));
						g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+5, (int)Math.round(currentTopLeft[1])+25);
					}
					
					currentTopLeft[0]+=currentRectangle.w;//pixelWidth;
				}
			}else{
				//left or right column (shift y-coordinate of "currentTopLeft" right)
				int numberOfRectangles=bins[i].numberOfRectangles;
				for(int j=0;j<numberOfRectangles;j++){
					Rectangle currentRectangle=bins[i].rectangles[j];
					
					if(printBinAndRectangleCoordinates){
						v_count[currentRectangle.type]++;
						System.out.println("rectangle_"+i+"_"+j+", type="+currentRectangle.type+", width="+currentRectangle.w+", length="+currentRectangle.l+", top left coordinate={"+currentTopLeft[0]+","+currentTopLeft[1]+"}");
					}
					
					g.setColor(currentRectangle.colour);
					if(floorNotRound) {
						g.fillRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(currentRectangle.w), (int)Math.floor(currentRectangle.l));
					}else {
						g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					}
					
					if(invertedColours) {
						g.setColor(Color.ORANGE);
						g.setColor(Color.BLACK);
					}else {
						g.setColor(Color.BLACK);
					}
					if(floorNotRound) {
						g.drawRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(currentRectangle.w), (int)Math.floor(currentRectangle.l));
					}else {
						g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					}
					
					g.setColor(Color.BLACK);//"bin=".concat()
					if(i>=10) {
						g.setFont(new Font(fontChoice,Font.BOLD,11));
						g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+2, (int)Math.round(currentTopLeft[1])+25);
					}else {
						g.setFont(new Font(fontChoice,Font.BOLD,15));
						g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+5, (int)Math.round(currentTopLeft[1])+25);
					}
					
					currentTopLeft[1]+=currentRectangle.l;//.pixelLength;
				}
			}
			//for better visual effect
			currentTopLeft[0]=bins[i].pos[0];
			currentTopLeft[1]=bins[i].pos[1];
			if(invertedColours) {
				g.setColor(Color.ORANGE);
				g.setColor(Color.BLACK);
			}else {
				g.setColor(Color.CYAN);
			}
			
			g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(bins[i].w), (int)Math.round(bins[i].l));//onePixel/onePixel
			//
			//g.setColor(Color.BLACK);//"bin=".concat()
			//g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+5, (int)Math.round(currentTopLeft[1])+25);
		}
		
		//yard queues (plus bin labels)
		double[] yardTopLeft={650,100};
		double yardLaneWidth=YA.yardLanes[0].maxWidth;
		double[] currentTopLeft={yardTopLeft[0], yardTopLeft[1]};
		Queue[] YL=YA.yardLanes;
		int yardLanes=YL.length;
		for(int i=0;i<yardLanes;i++){
			Queue queue=YL[i];
			//
			currentTopLeft[0]=yardTopLeft[0]+(i*yardLaneWidth);
			currentTopLeft[1]=yardTopLeft[1];
			//
			if(invertedColours) {
				g.setColor(Color.BLACK);//"bin=".concat()
			}else {
				g.setColor(Color.YELLOW);//"bin=".concat()
			}
			
			g.setFont(new Font(fontChoice,Font.BOLD,15));//
			
			g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-15);
			if(queue.widthIsTargetDimension){
				g.drawString("W", (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-40);
			}else{
				g.drawString("L", (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-40);
			}
			g.setFont(new Font(fontChoice,Font.BOLD,11));//|Font.ITALIC
			g.drawString(String.valueOf(queue.targetDimensionLength).substring(0, 5), (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-55);
			//g.setFont(new Font(fontChoice,Font.BOLD,11));//|Font.ITALIC
			//			
			if(invertedColours) {
				g.setColor(Color.ORANGE);
				
			}else {
				g.setColor(Color.GREEN);
			}
			if(floorNotRound) {
				g.drawRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(YL[i].maxWidth), (int)Math.floor(YL[i].maxLength));
			}else {
				g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(YL[i].maxWidth), (int)Math.round(YL[i].maxLength));
			}
			
			//
			Rectangle qRect=YL[i].head;
			while(qRect!=null){
				if(qRect.binNumber==-1){
					g.setColor(Color.LIGHT_GRAY);
				}else{
					g.setColor(qRect.colour);
				}
				//g.setColor(qRect.colour);
				if(floorNotRound) {
					g.fillRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(qRect.w), (int)Math.floor(qRect.l));
				}else {
					g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(qRect.w), (int)Math.round(qRect.l));
				}
				
				if(invertedColours) {
					g.setColor(Color.ORANGE);
					g.setColor(Color.BLACK);
				}else {
					g.setColor(Color.BLACK);
				}
				if(floorNotRound) {
					g.drawRect((int)Math.floor(currentTopLeft[0]), (int)Math.floor(currentTopLeft[1]), (int)Math.floor(qRect.w), (int)Math.floor(qRect.l));
				}else {
					g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(qRect.w), (int)Math.round(qRect.l));
				}
				
				//
				g.setColor(Color.BLACK);//"bin=".concat()
				//g.drawString(String.valueOf(qRect.binNumber), (int)Math.round(currentTopLeft[0])+2, (int)Math.round(currentTopLeft[1])+25);
				
				if(qRect.binNumber>=10) {
					g.setFont(new Font(fontChoice,Font.BOLD,11));//
				
					g.drawString(String.valueOf(qRect.binNumber), (int)Math.round(currentTopLeft[0])+2, (int)Math.round(currentTopLeft[1])+25);
				}else {
					if(qRect.binNumber==-1) {
						g.setFont(new Font(fontChoice,Font.BOLD,15));//
						g.drawString(String.valueOf(qRect.binNumber), (int)Math.round(currentTopLeft[0])+2, (int)Math.round(currentTopLeft[1])+25);
					}else {
						g.setFont(new Font(fontChoice,Font.BOLD,15));//
						g.drawString(String.valueOf(qRect.binNumber), (int)Math.round(currentTopLeft[0])+5, (int)Math.round(currentTopLeft[1])+25);
					}
					
				}
				
				//
				currentTopLeft[1]+=qRect.l;
				qRect=qRect.nextRectInQueue;
			}
		}
		
		//vehicle packed those and the total vehicles of each type to begin with
		for(int i=0;i<v_count.length;i++){
			g.setColor(Color.YELLOW);//"bin=".concat()
			g.drawString("v_type".concat(String.valueOf(i)).concat(":"), 1000, 600+(i*20));
			g.drawString(String.valueOf(YA.vCountByType[i]), 1100, 600+(i*20));
			g.drawString(String.valueOf(v_count[i]), 1150, 600+(i*20));
		}
		
		if(printBinAndRectangleCoordinates){
			/*for(int i=0;i<5;i++){
				System.out.println(v_count[i]);
			}*/
			
		}
		if(invertedColours) {
			g.setColor(Color.BLACK);
		}else {
			g.setColor(Color.YELLOW);
		}
		
		g.drawString("objective_value=".concat(String.valueOf((totalArea-objectiveValue)).substring(0, 7)), 200, 80);
		//utilisation
		g.drawString("utilisation=".concat(String.valueOf(utilisation).substring(0, 7)), 200, 120);
		g.drawString("utilisation=".concat(String.valueOf(utilisation)), 2000, 1520);
	}
	
	public void saveImage(String fileName, String type) throws IOException{
		ImageIO.write(bf, type, new File(fileName.concat(".").concat(type)));
	}
	/*private class ScrollPanel extends JPanel{
		
		ScrollPanel(){
			super();
			setSize(2*Width,Height);
			setVisible(true);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			setBackground(Color.BLACK);
			g.setColor(Color.GREEN);
			g.fillRect(30, 20, 20, 20);
			g.fillRect(3000, 2000, 20, 20);
		}
	}*/
	
	void paintMultipleSolutions(int[] scenarioRandomSeeds, boolean breakTiesWithLeastFullLane, boolean moduloScenarios, int yardPolicyType, int[][] n){
		if(visualise){
			objectiveValue=0;
			queuesPopped=new int[scenarioRandomSeeds.length][200];//=new int[][100];
			popMoveTypes=new int[scenarioRandomSeeds.length][200];//
			stripNumber=new int[scenarioRandomSeeds.length][200];//
			numberOfQueuesPopped=new int[scenarioRandomSeeds.length];
			for(int i=0;i<scenarioRandomSeeds.length;i++){
				if(moduloScenarios){
					YA.generateYardQueues(scenarioRandomSeeds[0], breakTiesWithLeastFullLane, moduloScenarios, (double)i/scenarioRandomSeeds.length, yardPolicyType, n);
				}else{
					YA.generateYardQueues(scenarioRandomSeeds[i], breakTiesWithLeastFullLane, moduloScenarios, 0, yardPolicyType, n);
				}
				reset();//just in time
				//read the solution and implement its instructions (as closely as possible)
				boolean aRectangleFitsInTheRemainingSpace=true;
				Gene currentGene=sol.head;
				int activeLengthOfSolution=0;//optional (swap positions of parts of the solution that are not possible with the following genes which are possible)
				while(currentGene!=null && aRectangleFitsInTheRemainingSpace){
					int value=currentGene.value;
					int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
					int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
					if(!fillNextBin(setOfQuantiles[quantileIndex], orientation, i)){
						aRectangleFitsInTheRemainingSpace=false;//no more rectangles fit (part of the objective should penalise the area of the rectangles that do not fit
						//set the remaining genes to unused
						currentGene.used=false;
						//
						currentGene=currentGene.nextGene;
						while(currentGene!=null){
							currentGene.used=false;
							currentGene=currentGene.nextGene;
						}
					}else{
						currentGene.used=true;
						//next gene
						currentGene=currentGene.nextGene;
					}
				}
				//activeGenesFirst (which also updates the active length of the solution). Active length is used for mutation and crossover position selection
				sol.activeGenesFirstOrder();
				double scenarioObjective=evaluateFitness();
				double scenarioUtilisation=utilisation;
				objectiveValue+=scenarioObjective;
				
				//new JFrame object
				new SolDraw(YA, LEdgeList, REdgeList, BEdgeList, TEdgeList, bins, numberOfBins, scenarioObjective, scenarioUtilisation);
				
			}
		}
		
		
		
	}
	
}
