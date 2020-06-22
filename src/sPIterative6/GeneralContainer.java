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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;




public class GeneralContainer extends JFrame  implements KeyListener{
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
	
	double totalArea;
	
	Bin[] bins;
	int numberOfBins;
	
	double[][] cornersOfRemSpace=new double[4][2];//initially these are the dimensions of the container
	double remainingWidth;
	double remainingLength;
	
	/////////////////////
	EdgeList LEdgeList=new EdgeList(true,1,false);
	EdgeList REdgeList=new EdgeList(false,2,true);
	EdgeList TEdgeList=new EdgeList(true,3,false);
	EdgeList BEdgeList=new EdgeList(false,4,true);
	
	double top;
	double bottom;
	double left;
	double right;
	
	//arrays of these for splitting the ferry
	
	Edge[] deckLeftEdges;
	Edge[] deckRightEdges;
	Edge[] deckTopEdges;
	Edge[] deckBottomEdges;
	/////////////////////
	
	Edge nonCornerPosAlternativeEntrance;//for the RF ferry this the top edge on the left and null for the rectangule case
	Edge cornerPosAlternativeEntrance;//that on the right
	Edge cornerPosAlternativeEntrance2;//that on the right
	
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
	
	//objective values for each arrival scenario
	double[] objValPerArrivalScenario;

	double intersectionObjVal;
	
	double packingObj;
	
	double packedVehicleArea;//of "sol"
	
	//objective values for each arrival scenario
	double[] intersectionObjValPerArrivalScenario;
	
	//useful objective measure for second stage 
	double nonPackedVehicleArea;	
		

	boolean printBinAndRectangleCoordinates=false;

	
	EdgeList spaceLEdgeListRef;
	EdgeList spaceREdgeListRef;
	EdgeList spaceTEdgeListRef;
	EdgeList spaceBEdgeListRef;
	
	EdgeList spaceLEdgeList=new EdgeList(false,-1,false);
	EdgeList spaceREdgeList=new EdgeList(true,-1,true);
	EdgeList spaceTEdgeList=new EdgeList(true,-1,false);
	EdgeList spaceBEdgeList=new EdgeList(false,-1,true);
	

	EdgeList newSpaceLEdgeList=new EdgeList(false,-1,false);
	EdgeList newSpaceREdgeList=new EdgeList(true,-1,true);
	EdgeList newSpaceTEdgeList=new EdgeList(true,-1,false);
	EdgeList newSpaceBEdgeList=new EdgeList(false,-1,true);
	
	int counter=0;
	
	Rectangle nextRectangle;
	int prevMoveDir;
	int moveDir;
	boolean atCornerPosition=false;
	Edge firstEdge;
	Edge prevEdge;
	
	boolean prevMoveWasInY=true;
	boolean prevMoveWasPos=true;
	
	double[] prevSpaceCoordinate=new double[2];
	
	Rectangle minDimensionedRect;
	
	aNumberList ALN;
	
	
	//implement knapsack-guillotine algorithm in the general container to obtain compacting benefits
	double[][] BLLikePositions=new double[5][2];
	double[] previousPosition=new double[2];
	double[] previousRectangleDims=new double[2];
	
	//efficient corner position scores
	double leftBottomBestScore=Double.MAX_VALUE;
	double rightBottomBestScore=Double.MAX_VALUE;
	double bottomLeftBestScore=Double.MAX_VALUE;
	double bottomRighBestScore=Double.MAX_VALUE;
	
	ArrayList<Rectangle> parkedRectangles=new ArrayList<Rectangle>(10);
	
	
	boolean initialGPSolFeasible;
	boolean recourseResolvedFromScratch;
	
	
	int[][] vMixes;//
	
	//strip generation with the general container
	//GA fields
	int vTypes;
	double[][] targetDimensions;
	
	double maxWidthOfNextStripRectangle;
	double maxLengthOfNextStripRectangle;
	
	//
	int[] bestCommittedVMixReference;
	double bestCommitedUtilisation;
	
	double utilisationBeforeRecourse;
	
	double tolerance=0.000001;
	
	//constructor to initialise a problem instance including 
	GeneralContainer(Yard YA, Random randNumGen, boolean visualise, String ferryShape, int containerNumber, double totalArea, int numberOfArrivalScenarios, double minWidth, double minLength) throws IOException{//HoldingArea HA,
		
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
		
		vMixes=new int[numberOfArrivalScenarios][YA.vTypes];
		
		objValPerArrivalScenario=new double[numberOfArrivalScenarios];
		intersectionObjValPerArrivalScenario=new double[numberOfArrivalScenarios];
		//////////////
		this.totalArea=totalArea;
		
		this.containerNumber=containerNumber;
		
		this.YA=new Yard(YA);//
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
				}
				interval[j]=Double.parseDouble(oneEndCoords[1]);
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
		//
		top=TEdgeList.tail.pointPos;
		bottom=BEdgeList.head.pointPos;
		left=REdgeList.head.pointPos;
		right=LEdgeList.tail.pointPos;
		//
		if(TEdgeList.length>1){
			//TEdgeList.printList();
			nonCornerPosAlternativeEntrance=TEdgeList.head.nextEdge.nextEdge;
			cornerPosAlternativeEntrance=TEdgeList.head.nextEdge;
			cornerPosAlternativeEntrance2=TEdgeList.head;
		}
		
		////////////////////////
		ALN=new aNumberList(-1);
		/////////////
		//
		minDimensionedRect=new Rectangle(minLength, minWidth, 0.0001, Color.YELLOW, -1);
		//
		vTypes=YA.rectangleDistribution[0].length;//L,W,H,P
		targetDimensions=new double[2][vTypes];
		for(int i=0;i<vTypes;i++){
			targetDimensions[0][i]=YA.rectangleDistribution[1][i];//W//yard.vehicleTypes[i].w;
			targetDimensions[1][i]=YA.rectangleDistribution[0][i];//L//yard.vehicleTypes[i].l;
		}
		maths.justSort(targetDimensions[0]);
		maths.justSort(targetDimensions[1]);
		//numbers of quantiles and move types
		numberOfVehicleSelectionQuantiles=vTypes;
		numberOfOrientations=4;
		//
		reset();
	}
	
	//initialisation
	void updateEdgeLists(EdgeList newSpaceLEdgeList, EdgeList newSpaceREdgeList, EdgeList newSpaceTEdgeList, EdgeList newSpaceBEdgeList){
		//
		spaceLEdgeList.copyOfEdgeList(newSpaceLEdgeList);
		spaceREdgeList.copyOfEdgeList(newSpaceREdgeList);
		spaceTEdgeList.copyOfEdgeList(newSpaceTEdgeList);
		spaceBEdgeList.copyOfEdgeList(newSpaceBEdgeList);
		
		
		//generateOPs(space);
		//simplify space edgeLists
		simplifySpaceEdgeList(spaceLEdgeList);
		simplifySpaceEdgeList(spaceREdgeList);
		simplifySpaceEdgeList(spaceTEdgeList);
		simplifySpaceEdgeList(spaceBEdgeList);
		
		linkSpaceEdgeCorners();
	}
	
	
	void linkSpaceEdgeCorners(){

		Edge alEdge=spaceLEdgeList.head;
		while(alEdge!=null){
			alEdge.end[0]=null;
			alEdge.end[1]=null;
			alEdge=alEdge.nextEdge;
		}
		Edge aEdge=spaceREdgeList.head;
		while(aEdge!=null){
			aEdge.end[0]=null;
			aEdge.end[1]=null;
			aEdge=aEdge.nextEdge;
		}
		aEdge=spaceTEdgeList.head;
		while(aEdge!=null){
			aEdge.end[0]=null;
			aEdge.end[1]=null;
			aEdge=aEdge.nextEdge;
		}
		aEdge=spaceBEdgeList.head;
		while(aEdge!=null){
			aEdge.end[0]=null;
			aEdge.end[1]=null;
			aEdge=aEdge.nextEdge;
		}
		
		
		
		Edge lEdge=spaceLEdgeList.head;
		while(lEdge!=null){
			//find the top and bottom space edges that connect to each end
			boolean end1Found=false;
			boolean end2Found=false;
			if(lEdge.end[0]==null){
				//bottom
				Edge bEdge=spaceBEdgeList.head;
				while(bEdge!=null){
					if(bEdge.end[1]==null){
						if(Math.abs(bEdge.interval[1]-lEdge.pointPos)<tolerance && Math.abs(bEdge.pointPos-lEdge.interval[0])<tolerance){
							bEdge.end[1]=lEdge;
							lEdge.end[0]=bEdge;
							bEdge=null;
							end1Found=true;
						}else{
							bEdge=bEdge.nextEdge;
						}
					}else{
						bEdge=bEdge.nextEdge;
					}
				}
				//
				if(!end1Found){
					//top
					Edge tEdge=spaceTEdgeList.head;
					while(tEdge!=null){
						if(tEdge.end[1]==null){
							if(Math.abs(tEdge.interval[0]-lEdge.pointPos)<tolerance && Math.abs(tEdge.pointPos-lEdge.interval[0])<tolerance){
								tEdge.end[1]=lEdge;
								lEdge.end[0]=tEdge;
								tEdge=null;
								end1Found=true;
							}else{
								tEdge=tEdge.nextEdge;
							}
						}else{
							tEdge=tEdge.nextEdge;
						}
					}
				}
			}else{
				end1Found=true;
			}
			//left end 2
			if(lEdge.end[1]==null){
				//bottom
				Edge bEdge=spaceBEdgeList.head;
				while(bEdge!=null){
					if(bEdge.end[0]==null){
						if(Math.abs(bEdge.interval[0]-lEdge.pointPos)<tolerance && Math.abs(bEdge.pointPos-lEdge.interval[1])<tolerance){
							bEdge.end[0]=lEdge;
							lEdge.end[1]=bEdge;
							bEdge=null;
							end2Found=true;
						}else{
							bEdge=bEdge.nextEdge;
						}
					}else{
						bEdge=bEdge.nextEdge;
					}
				}
				//
				if(!end2Found){
					//top
					Edge tEdge=spaceTEdgeList.head;
					while(tEdge!=null){
						if(tEdge.end[0]==null){
							if(Math.abs(tEdge.interval[1]-lEdge.pointPos)<tolerance && Math.abs(tEdge.pointPos-lEdge.interval[1])<tolerance){
								tEdge.end[0]=lEdge;
								lEdge.end[1]=tEdge;
								tEdge=null;
								end2Found=true;
							}else{
								tEdge=tEdge.nextEdge;
							}
						}else{
							tEdge=tEdge.nextEdge;
						}
					}
				}
			}else{
				end2Found=true;
			}
			lEdge=lEdge.nextEdge;
		}
		
		//rEdge
		Edge rEdge=spaceREdgeList.head;
		while(rEdge!=null){
			//find the top and bottom space edges that connect to each end
			boolean end1Found=false;
			boolean end2Found=false;
			if(rEdge.end[0]==null){
				//bottom
				Edge bEdge=spaceBEdgeList.head;
				while(bEdge!=null){
					if(bEdge.end[1]==null){
						if(Math.abs(bEdge.interval[1]-rEdge.pointPos)<tolerance && Math.abs(bEdge.pointPos-rEdge.interval[1])<tolerance){
							bEdge.end[1]=rEdge;
							rEdge.end[0]=bEdge;
							bEdge=null;
							end1Found=true;
						}else{
							bEdge=bEdge.nextEdge;
						}
					}else{
						bEdge=bEdge.nextEdge;
					}
				}
				//
				if(!end1Found){
					//top
					Edge tEdge=spaceTEdgeList.head;
					while(tEdge!=null){
						if(tEdge.end[1]==null){
							if(Math.abs(tEdge.interval[0]-rEdge.pointPos)<tolerance && Math.abs(tEdge.pointPos-rEdge.interval[1])<tolerance){
								tEdge.end[1]=rEdge;
								rEdge.end[0]=tEdge;
								tEdge=null;
								end1Found=true;
							}else{
								tEdge=tEdge.nextEdge;
							}
						}else{
							tEdge=tEdge.nextEdge;
						}
					}
				}
			}else{
				end1Found=true;
			}
			//left end 2
			if(rEdge.end[1]==null){
				//bottom
				Edge bEdge=spaceBEdgeList.head;
				while(bEdge!=null){
					if(bEdge.end[0]==null){
						if(Math.abs(bEdge.interval[0]-rEdge.pointPos)<tolerance && Math.abs(bEdge.pointPos-rEdge.interval[0])<tolerance){
							bEdge.end[0]=rEdge;
							rEdge.end[1]=bEdge;
							bEdge=null;
							end2Found=true;
						}else{
							bEdge=bEdge.nextEdge;
						}
					}else{
						bEdge=bEdge.nextEdge;
					}
				}
				//
				if(!end2Found){
					//top
					Edge tEdge=spaceTEdgeList.head;
					while(tEdge!=null){
						if(tEdge.end[0]==null){
							if(Math.abs(tEdge.interval[1]-rEdge.pointPos)<tolerance && Math.abs(tEdge.pointPos-rEdge.interval[0])<tolerance){
								tEdge.end[0]=rEdge;
								rEdge.end[1]=tEdge;
								tEdge=null;
								end2Found=true;
							}else{
								tEdge=tEdge.nextEdge;
							}
						}else{
							tEdge=tEdge.nextEdge;
						}
					}
				}
			}else{
				end2Found=true;
			}
			rEdge=rEdge.nextEdge;
		}
	}
	
	void simplifySpaceEdgeList(EdgeList spaceEdgeList){
		//boolean didSomething=false;
		Edge edgeI=spaceEdgeList.head;
		//int tEnter=(int)System.currentTimeMillis();
		while(edgeI!=null){
			if(edgeI.nextEdge!=null){
				boolean samePointPos=true;
				Edge edgeJ=edgeI.nextEdge;
				while(edgeJ!=null && samePointPos){
					if(edgeJ.pointPos-edgeI.pointPos<tolerance){
						//check if the intervals join
						if(Math.abs(edgeJ.interval[0]-edgeI.interval[1])<tolerance){
							//then j fits onto the end of i
							//extend i
							edgeI.interval[1]=edgeJ.interval[1];
							//remove j
							spaceEdgeList.removeEdgeFromLinkedList(edgeJ);
							edgeJ=edgeI;
						}else if(Math.abs(edgeJ.interval[1]-edgeI.interval[0])<tolerance){
							//then j fits onto the front of i
							//extend i
							edgeI.interval[0]=edgeJ.interval[0];
							//remove j
							spaceEdgeList.removeEdgeFromLinkedList(edgeJ);
							edgeJ=edgeI;
						}
					}else{
						samePointPos=false;
					}
					edgeJ=edgeJ.nextEdge;
				}
			}
			edgeI=edgeI.nextEdge;
		}
	}
	
	
	void resetEdgeLists(){
		spaceLEdgeList.reset();;
		spaceREdgeList.reset();;
		spaceTEdgeList.reset();;
		spaceBEdgeList.reset();;
		
	}
	
	
	boolean[] addRectangleBLUpdateInnerFitPolygonStrip(Rectangle v, int positionIndex, boolean sameStripAsPrevious, boolean strictStripAdjacency, int stripNumber){
		boolean[] spaceRemainsVehicleAdded=new boolean[2];
		if(spaceLEdgeList.length==0 || spaceREdgeList.length==0 || spaceTEdgeList.length==0 || spaceBEdgeList.length==0){
			return spaceRemainsVehicleAdded;
		}
		
		//In this method:
		//	-slide the rectangle v around to see if it fits and if it does find the BL position
		//	-If the rectangle does fit, add it, then update the inner fit polygon for the minimum dimensioned rectangle

		if(slidingProcedure(false, v, positionIndex, sameStripAsPrevious) && !(strictStripAdjacency && BLLikePositions[4][0]==-1)){
			spaceRemainsVehicleAdded[1]=true;
			if(BLLikePositions[4][0]!=-1 && sameStripAsPrevious){
				v.parkedPosition[0]=BLLikePositions[4][0];
				v.parkedPosition[1]=BLLikePositions[4][1];
				
				previousPosition[0]=BLLikePositions[4][0];
				previousPosition[1]=BLLikePositions[4][1];
			}else{
				v.parkedPosition[0]=BLLikePositions[positionIndex][0];
				v.parkedPosition[1]=BLLikePositions[positionIndex][1];
				
				previousPosition[0]=BLLikePositions[positionIndex][0];
				previousPosition[1]=BLLikePositions[positionIndex][1];
			}
			previousRectangleDims[0]=v.w;
			previousRectangleDims[1]=v.l;
			
			//
			v.generateEdgeCopies(100001);
			
			
			//add the length of the rectangle for columns
			//add the width of the rectangle for rows
			if(positionIndex<2){
				previousPosition[1]+=v.l;
				maxLengthOfNextStripRectangle=(top-previousPosition[1]);//spaceTEdgeList.distanceToNearestOppositeEdge(v.be2, true);
			}else{
				if(positionIndex==2){
					previousPosition[0]+=v.w;
					maxWidthOfNextStripRectangle=(right-previousPosition[0]);//spaceLEdgeList.distanceToNearestOppositeEdge(v.re2, true);
					//}
				}else{
					//this ends the strip (which can be exploited to deliberately truncate a strip
					previousPosition[0]-=v.w;
				}
			}
			
			
			spaceLEdgeList.addEdgeToLinkedList(v.le2);
			spaceREdgeList.addEdgeToLinkedList(v.re2);
			spaceTEdgeList.addEdgeToLinkedList(v.te2);
			spaceBEdgeList.addEdgeToLinkedList(v.be2);
			
			parkedRectangles.add(v);
			
			v.binNumber=stripNumber;
			
			if(slidingProcedure(true, minDimensionedRect, positionIndex, sameStripAsPrevious)){
				spaceRemainsVehicleAdded[0]=true;
				return spaceRemainsVehicleAdded;
			}else{
				return spaceRemainsVehicleAdded;
			}
			
		}else{
			return spaceRemainsVehicleAdded;//
		}
		
	}
	
	boolean rightTraverseAdjacentEdge(boolean generateNewEdges){//boolean exploratory		
		int vt=nextRectangle.type;
		double[] parkedPosition=nextRectangle.parkedPosition;
		Edge horizontalAdjacentEdge=nextRectangle.horizontalAdjacentEdge;
		
		boolean vehicleMoved=false;
		//cycle through the left edges (right or equal of the current position)
		//stop if a further move right causes an overlap
		//set the parkedPosition to
		boolean nextLEReached=false;
		double initialXPos=parkedPosition[0];
		double initialYPos=parkedPosition[1];
		double newAdjacentX=-1;
		Edge currentLEdge=spaceLEdgeList.head;//LHeadEdge;
		boolean passedValidCornerPos=false;
		while(currentLEdge!=null && !nextLEReached){
			if(currentLEdge.pointPos+tolerance>=nextRectangle.rightPP){
				
				if(horizontalAdjacentEdge.interval[1]+nextRectangle.w<currentLEdge.pointPos){
					passedValidCornerPos=true;
				}
				
				//check interval for overlap
				if((currentLEdge.interval[0]+tolerance<nextRectangle.bottomPP && currentLEdge.interval[1]>nextRectangle.topPP+tolerance) || passedValidCornerPos){
					nextLEReached=true;//nextRectangle.
					if(horizontalAdjacentEdge.interval[1]+nextRectangle.w<currentLEdge.pointPos){
						atCornerPosition=true;
						newAdjacentX=horizontalAdjacentEdge.interval[1];
						//set the new position and call the update temp position method of the nextRectangle
						//add an additional lateral gap after removing the previous additional LG
						parkedPosition[0]=horizontalAdjacentEdge.interval[1];//
						nextRectangle.updateTempPosition();

						nextRectangle.verticalAdjacentEdge=horizontalAdjacentEdge.end[1];//.vehicle.re;
						
					}else{
						
						atCornerPosition=false;
						//
						newAdjacentX=currentLEdge.pointPos;
						//set the new position and call the update temp position method of the nextRectangle
						parkedPosition[0]=currentLEdge.pointPos-nextRectangle.w;
						nextRectangle.updateTempPosition();
						nextRectangle.verticalAdjacentEdge=currentLEdge;
					}
				}
			}
			currentLEdge=currentLEdge.nextEdge;
		}
		////////////////////////////////////////////
		
		//attempted move type
		prevMoveWasInY=false;
		prevMoveWasPos=true;
		if(Math.abs(initialXPos-parkedPosition[0])>tolerance || Math.abs(initialYPos-parkedPosition[1])>tolerance){
			vehicleMoved=true;
		}
		prevMoveDir=0;

		if(atCornerPosition){//nextRectangle.
			if(horizontalAdjacentEdge.interval[1]-prevSpaceCoordinate[0]>tolerance){
				if(generateNewEdges){
					
					Edge newTEdge=new Edge(prevSpaceCoordinate[1], new double[]{prevSpaceCoordinate[0], horizontalAdjacentEdge.interval[1]});//,-33, horizontalAdjacentEdge.vehicle, vehicleStartsAdjacentToSide
					newSpaceTEdgeList.addEdgeToLinkedList(newTEdge);//nextRectangle.horizontalAdjacentEdge.interval[0]vehicle number not required here
					if(prevEdge!=null){
						newTEdge.end[1]=prevEdge;
						prevEdge.end[0]=newTEdge;
					}else{
						firstEdge=newTEdge;
					}
					prevEdge=newTEdge;
				}
				
				prevSpaceCoordinate[0]=horizontalAdjacentEdge.interval[1];
				
			}
		}else{
			if(newAdjacentX-prevSpaceCoordinate[0]>tolerance){
				if(generateNewEdges){
					
					
					Edge newTEdge=new Edge(prevSpaceCoordinate[1], new double[]{prevSpaceCoordinate[0], newAdjacentX});//
					newSpaceTEdgeList.addEdgeToLinkedList(newTEdge);//vehicle number not required here
					if(prevEdge!=null){
						newTEdge.end[1]=prevEdge;
						prevEdge.end[0]=newTEdge;
					}else{
						firstEdge=newTEdge;
					}
					prevEdge=newTEdge;
				}
				
				prevSpaceCoordinate[0]=newAdjacentX;
				
			}
		}
		
		return vehicleMoved;
	}
	
	
	boolean leftTraverseAdjacentEdge(boolean generateNewEdges, int positionIndex, boolean sameStripAsPrevious){
		double[] parkedPosition=nextRectangle.parkedPosition;
		Edge horizontalAdjacentEdge=nextRectangle.horizontalAdjacentEdge;

		boolean vehicleMoved=false;
		//cycle through the right edges (left or equal of the current position)
		//stop if a further move left causes an overlap
		//set the parkedPosition to
		boolean nextREReached=false;
		double initialXPos=parkedPosition[0];
		double initialYPos=parkedPosition[1];
		//
		Edge currentREdge=spaceREdgeList.tail;//RTailEdge;

		boolean passedValidCornerPos=false;
		while(currentREdge!=null && !nextREReached){
			
			if(currentREdge.pointPos-tolerance<=nextRectangle.leftPP){
				
				if(horizontalAdjacentEdge.interval[0]-nextRectangle.w>currentREdge.pointPos){
					passedValidCornerPos=true;
				}
				
				//check for overlap
				if((currentREdge.interval[0]+tolerance<nextRectangle.bottomPP && currentREdge.interval[1]>nextRectangle.topPP+tolerance) || passedValidCornerPos){
					//interval overlap found
					nextREReached=true;
					//nextRectangle.
					if(horizontalAdjacentEdge.interval[0]-nextRectangle.w>currentREdge.pointPos){
						atCornerPosition=true;
						//set the new position and call the update temp position method of the nextRectangle
						//add an additional lateral gap after removing the previous additional LG
						parkedPosition[0]=horizontalAdjacentEdge.interval[0]-nextRectangle.w;
						nextRectangle.updateTempPosition();
						
						nextRectangle.verticalAdjacentEdge=horizontalAdjacentEdge.end[1];//.vehicle.le;
						
					}else{
						atCornerPosition=false;
						//set the new position and call the update temp position method of the nextRectangle

						parkedPosition[0]=currentREdge.pointPos;
						nextRectangle.updateTempPosition();
						nextRectangle.verticalAdjacentEdge=currentREdge;
					}
				}
			}
			currentREdge=currentREdge.prevEdge;
		}
		
		
		//attempted move type
		prevMoveWasInY=false;
		prevMoveWasPos=false;
		if(Math.abs(initialXPos-parkedPosition[0])>tolerance || Math.abs(initialYPos-parkedPosition[1])>tolerance){
			vehicleMoved=true;
		}
		prevMoveDir=2;

		if(atCornerPosition){
			if(prevSpaceCoordinate[0]-horizontalAdjacentEdge.interval[0]>tolerance){
				if(generateNewEdges){

					Edge newBEdge=new Edge(prevSpaceCoordinate[1], new double[]{horizontalAdjacentEdge.interval[0], prevSpaceCoordinate[0]});
					newSpaceBEdgeList.addEdgeToLinkedList(newBEdge);//horizontalAdjacentEdge.interval[1]vehicle number not required here
					if(prevEdge!=null){
						newBEdge.end[1]=prevEdge;
						prevEdge.end[0]=newBEdge;
					}else{
						firstEdge=newBEdge;
					}
					prevEdge=newBEdge;
				}
				
				prevSpaceCoordinate[0]=horizontalAdjacentEdge.interval[0];
				
			}
		}else{
			if(prevSpaceCoordinate[0]-parkedPosition[0]>tolerance){
				if(generateNewEdges){
					
					Edge newBEdge=new Edge(prevSpaceCoordinate[1], new double[]{parkedPosition[0], prevSpaceCoordinate[0]});
					newSpaceBEdgeList.addEdgeToLinkedList(newBEdge);//vehicle number not required here

					if(prevEdge!=null){
						newBEdge.end[1]=prevEdge;
						prevEdge.end[0]=newBEdge;
					}else{
						firstEdge=newBEdge;
					}
					prevEdge=newBEdge;
				}
				prevSpaceCoordinate[0]=parkedPosition[0];

				if(!generateNewEdges){
					//replace the above with the scores approach (as opposed to sequential)
					double opr0=prevSpaceCoordinate[0];
					double opr1=prevSpaceCoordinate[1];
					
					//double fr=right-opr0;
					double fl=opr0-left;
					double fb=opr1-bottom;
					
					double fbls=(fb/(top-bottom))+0.1*(fl/(right-left));
					double flbs=0.1*(fb/(top-bottom))+(fl/(right-left));
					
					//BL,2
					if(fbls<bottomLeftBestScore){
						bottomLeftBestScore=fbls;
						//
						BLLikePositions[2][1]=prevSpaceCoordinate[1];
						BLLikePositions[2][0]=prevSpaceCoordinate[0];
					}
					
					//LB,0
					if(flbs<leftBottomBestScore){
						leftBottomBestScore=flbs;
						//
						BLLikePositions[0][1]=prevSpaceCoordinate[1];
						BLLikePositions[0][0]=prevSpaceCoordinate[0];
					}
					
					//check if the current position is the continuation of the current strip
					//Only applicable when trying to follow instructions from the "toy model". 
					//When this is not applicable set "sameStripAsPrevious=false"
					if(sameStripAsPrevious){
						//0 or 2
						if(positionIndex==0){//0, left col
							
							if(Math.abs(prevSpaceCoordinate[1]-previousPosition[1])<tolerance && prevSpaceCoordinate[0]<=previousPosition[0]+previousRectangleDims[0]+tolerance && prevSpaceCoordinate[0]+nextRectangle.w+tolerance>=previousPosition[0]){
								//then record this as position index five
								BLLikePositions[4][0]=prevSpaceCoordinate[0];
								BLLikePositions[4][1]=prevSpaceCoordinate[1];
								
								//
								//nextStripPositionFound=true;
							}
						}else if(positionIndex==2){//2, bottom row (from left)
							if(Math.abs(prevSpaceCoordinate[0]-previousPosition[0])<tolerance && prevSpaceCoordinate[1]<=previousPosition[1]+previousRectangleDims[1]+tolerance && prevSpaceCoordinate[1]+nextRectangle.l+tolerance>=previousPosition[1]){
								//then record this as position index five
								BLLikePositions[4][0]=prevSpaceCoordinate[0];
								BLLikePositions[4][1]=prevSpaceCoordinate[1];
								
								//
								//nextStripPositionFound=true;
								
								
								
							}
						}
						
					}
				}
				
				
			}
		}
		
		
		return vehicleMoved;
	}
	
	boolean forwardsTraverseAdjacentEdge(boolean generateNewEdges, int positionIndex, boolean sameStripAsPrevious){
		
		int vt=nextRectangle.type;
		double[] parkedPosition=nextRectangle.parkedPosition;
		Edge verticalAdjacentEdge=nextRectangle.verticalAdjacentEdge;
		
		boolean vehicleMoved=false;
		//cycle through the top edges (in front or equal of the current position)
		//stop if a further move forward causes an overlap
		//set the parkedPosition to
		boolean nextTEReached=false;
		double initialXPos=parkedPosition[0];
		double initialYPos=parkedPosition[1];

		Edge currentBEdge=spaceBEdgeList.tail;//BTailEdge;
		
		//////////////////////
		boolean passedValidCornerPos=false;

		while(currentBEdge!=null && !nextTEReached){
			
			if(currentBEdge.pointPos-tolerance<=nextRectangle.topPP){
				if(verticalAdjacentEdge.interval[0]-nextRectangle.l>currentBEdge.pointPos){
					passedValidCornerPos=true;
				}
				//
				//check for overlap
				if((currentBEdge.interval[0]+tolerance<nextRectangle.rightPP && currentBEdge.interval[1]>nextRectangle.leftPP+tolerance) || passedValidCornerPos){
					//interval overlap found
					nextTEReached=true;//nextRectangle.
					if(verticalAdjacentEdge.interval[0]-nextRectangle.l>currentBEdge.pointPos){
						atCornerPosition=true;
						//set the new position and call the update temp position method of the nextRectangle
						//nextRectangle.
						parkedPosition[1]=verticalAdjacentEdge.interval[0]-nextRectangle.l;
						nextRectangle.updateTempPosition();
	
						nextRectangle.horizontalAdjacentEdge=verticalAdjacentEdge.end[1];//;
						
					}else{
						atCornerPosition=false;
						//set the new position and call the update temp position method of the nextRectangle
						parkedPosition[1]=currentBEdge.pointPos;
						nextRectangle.updateTempPosition();
						nextRectangle.horizontalAdjacentEdge=currentBEdge;
					}
				}
			}
			currentBEdge=currentBEdge.prevEdge;
		}
		//////////////////////
		
		
		
		
		//attempted move type
		prevMoveWasInY=true;
		prevMoveWasPos=false;
		if(Math.abs(initialXPos-parkedPosition[0])>tolerance || Math.abs(initialYPos-parkedPosition[1])>tolerance){
			vehicleMoved=true;
		}
		prevMoveDir=1;
		
		if(atCornerPosition){
			if(prevSpaceCoordinate[1]-verticalAdjacentEdge.interval[0]>tolerance){
				if(generateNewEdges){

					Edge newLEdge=new Edge(prevSpaceCoordinate[0], new double[]{verticalAdjacentEdge.interval[0], prevSpaceCoordinate[1]});//,-33, verticalAdjacentEdge.vehicle, vehicleStartsAdjacentToSide
					newSpaceLEdgeList.addEdgeToLinkedList(newLEdge);//verticalAdjacentEdge.interval[1]vehicle number not required here

					if(prevEdge!=null){
						newLEdge.end[1]=prevEdge;
						prevEdge.end[0]=newLEdge;
					}else{
						firstEdge=newLEdge;
					}
					prevEdge=newLEdge;
				}
				prevSpaceCoordinate[1]=verticalAdjacentEdge.interval[0];
			}
		}else{
			if(prevSpaceCoordinate[1]-parkedPosition[1]>tolerance){
				if(generateNewEdges){
					
					Edge newLEdge=new Edge(prevSpaceCoordinate[0], new double[]{parkedPosition[1], prevSpaceCoordinate[1]});//,-33, verticalAdjacentEdge.vehicle, vehicleStartsAdjacentToSide
					newSpaceLEdgeList.addEdgeToLinkedList(newLEdge);//vehicle number not required here

					if(prevEdge!=null){
						newLEdge.end[1]=prevEdge;
						prevEdge.end[0]=newLEdge;
					}else{
						firstEdge=newLEdge;
					}
					prevEdge=newLEdge;
				}
				prevSpaceCoordinate[1]=parkedPosition[1];
				
				//
				if(!generateNewEdges){
					double opr0=prevSpaceCoordinate[0];
					double opr1=prevSpaceCoordinate[1];
					
					double fr=right-opr0;//((right-Math.min(right, (theOP.r[0]+vehicleTypes[vInd].w)))/(right-left));//
					double fb=opr1-bottom;
					
					double fbrs=(fb/(top-bottom))+0.1*(fr/(right-left));
					double frbs=0.1*(fb/(top-bottom))+(fr/(right-left));
					
					//BR,3
					if(fbrs<bottomRighBestScore){
						bottomRighBestScore=fbrs;
						//
						BLLikePositions[3][1]=prevSpaceCoordinate[1];
						BLLikePositions[3][0]=prevSpaceCoordinate[0]-nextRectangle.w;
					}
					//RB,1
					if(frbs<rightBottomBestScore){
						rightBottomBestScore=frbs;
						//
						BLLikePositions[1][1]=prevSpaceCoordinate[1];
						BLLikePositions[1][0]=prevSpaceCoordinate[0]-nextRectangle.w;
					}
					
					//check if the current position is the continuation of the current strip
					//Only applicable when trying to follow instructions from the "toy model". 
					//When this is not applicable set "sameStripAsPrevious=false"
					if(sameStripAsPrevious){
						//1 or 3
						if(positionIndex==1){//0, right col
							//(prevSpaceCoordinate[0]-nextRectangle.w)
							if(Math.abs(prevSpaceCoordinate[1]-previousPosition[1])<tolerance && prevSpaceCoordinate[0]-nextRectangle.w<=previousPosition[0]+previousRectangleDims[0]+tolerance && prevSpaceCoordinate[0]+tolerance>=previousPosition[0]){
								//then record this as position index five
								BLLikePositions[4][0]=prevSpaceCoordinate[0]-nextRectangle.w;
								BLLikePositions[4][1]=prevSpaceCoordinate[1];
							}
						}else if(positionIndex==3){//2, bottom row (from right)
							if(Math.abs((prevSpaceCoordinate[0]-nextRectangle.w)-previousPosition[0])<tolerance && prevSpaceCoordinate[1]<=previousPosition[1]+previousRectangleDims[1]+tolerance && prevSpaceCoordinate[1]+nextRectangle.l+tolerance>=previousPosition[1]){
								//then record this as position index five
								BLLikePositions[4][0]=prevSpaceCoordinate[0]-nextRectangle.w;
								BLLikePositions[4][1]=prevSpaceCoordinate[1];
							}
						}
						
					}
				}
				
				
			}
		}
		
		
		
		return vehicleMoved;
	}
	boolean backwardsTraverseAdjacentEdge(boolean generateNewEdges){//
		
		int vt=nextRectangle.type;
		double[] parkedPosition=nextRectangle.parkedPosition;
		Edge verticalAdjacentEdge=nextRectangle.verticalAdjacentEdge;
		
		
		boolean vehicleMoved=false;
		//cycle through the bottom edges (behind or equal of the current position)
		//stop if a further move backwards causes an overlap
		//set the parkedPosition to
		boolean nextBEReached=false;
		double initialXPos=parkedPosition[0];
		double initialYPos=parkedPosition[1];
		double maxYPos=-1000000;

		/////////////////////
		Edge currentTEdge=spaceTEdgeList.head;//verticalAdjacentEdge.vehicle.te;//spaceTEdgeList.head;//nextRectangle.verticalAdjacentEdge.vehicle.te;//THeadEdge;

		boolean passedValidCornerPos=false;
		while(currentTEdge!=null && !nextBEReached){
			
			if(currentTEdge.pointPos+tolerance>=nextRectangle.bottomPP){
				if(verticalAdjacentEdge.interval[1]+nextRectangle.l<currentTEdge.pointPos){
					passedValidCornerPos=true;
				}
				//check for overlap
				if((currentTEdge.interval[0]+tolerance<nextRectangle.rightPP && currentTEdge.interval[1]>nextRectangle.leftPP+tolerance) || passedValidCornerPos){
					//overlap interval found
					maxYPos=currentTEdge.pointPos;//required for maxLength of previous open position (if
					//the previous move was left (2)).
					nextBEReached=true;//nextRectangle.
					if(verticalAdjacentEdge.interval[1]+nextRectangle.l<currentTEdge.pointPos){
						atCornerPosition=true;
						//set the new position and call the update temp position method of the nextRectangle
						//nextRectangle.
						parkedPosition[1]=verticalAdjacentEdge.interval[1];//-nextRectangle.l
						nextRectangle.updateTempPosition();
						//adjacent to side?
						//nextRectangle.vehicleAdjacentToSide=false;
						//set horizontalAdjacentEdge as the top edge of the vehicle the current verticalAdjacentEdge is connected to
						//the top edge with the asme vehicle number		//nextRectangle.
						nextRectangle.horizontalAdjacentEdge=verticalAdjacentEdge.end[1];//.vehicle.be;
						
					}else{
						
						
						atCornerPosition=false;
						//set the new position and call the update temp position method of the nextRectangle
						parkedPosition[1]=currentTEdge.pointPos-nextRectangle.l;
						nextRectangle.updateTempPosition();
						//adjacent to side?
						nextRectangle.horizontalAdjacentEdge=currentTEdge;
					}
					
				}
			}
			currentTEdge=currentTEdge.nextEdge;
		}
		////////////////////
		
		
		
		//attempted move type
		prevMoveWasInY=true;
		prevMoveWasPos=true;
		if(Math.abs(initialXPos-parkedPosition[0])>tolerance || Math.abs(initialYPos-parkedPosition[1])>tolerance){
			vehicleMoved=true;
		}
		
		prevMoveDir=3;
		//Edge(double pointPos, double[] interval, int vehicleNumber){
		//
		
		
		if(atCornerPosition){
			if(verticalAdjacentEdge.interval[1]-prevSpaceCoordinate[1]>tolerance){
				if(generateNewEdges){
					
					Edge newREdge=new Edge(prevSpaceCoordinate[0], new double[]{prevSpaceCoordinate[1], verticalAdjacentEdge.interval[1]});
					newSpaceREdgeList.addEdgeToLinkedList(newREdge);//nextRectangle.verticalAdjacentEdge.interval[0]vehicle number not required here
					//spaceREdges.add(new Edge(prevSpaceCoordinate[0], new double[]{prevSpaceCoordinate[1], nextRectangle.verticalAdjacentEdge.interval[1]},-33));//nextRectangle.verticalAdjacentEdge.interval[0]vehicle number not required here
					
					if(prevEdge!=null){
						newREdge.end[1]=prevEdge;
						prevEdge.end[0]=newREdge;
					}else{
						firstEdge=newREdge;
					}
					prevEdge=newREdge;
				}
				prevSpaceCoordinate[1]=verticalAdjacentEdge.interval[1];
			}
		}else{
			if((parkedPosition[1]+nextRectangle.l)-prevSpaceCoordinate[1]>tolerance){
				if(generateNewEdges){
					Edge newREdge=new Edge(prevSpaceCoordinate[0], new double[]{prevSpaceCoordinate[1], parkedPosition[1]+nextRectangle.l});
					newSpaceREdgeList.addEdgeToLinkedList(newREdge);//vehicle number not required here
					//spaceREdges.add(new Edge(prevSpaceCoordinate[0], new double[]{prevSpaceCoordinate[1], parkedPosition[1]+nextRectangle.l},-33));//vehicle number not required here
					
					if(prevEdge!=null){
						newREdge.end[1]=prevEdge;
						prevEdge.end[0]=newREdge;
					}else{
						firstEdge=newREdge;
					}
					prevEdge=newREdge;
				}
				prevSpaceCoordinate[1]=parkedPosition[1]+nextRectangle.l;
			}
		}
		
		
		return vehicleMoved;
	}
	
	
	
	//2
	boolean slidingProcedure(boolean generateNewEdges, Rectangle v, int positionIndex, boolean sameStripAsPrevious){
		boolean slidingProcedureCompleted=true;//until false
		boolean vehicleFitsInInitialPosition=false;
		double[] initialPosition=new double[2];
		//
		//place vehicle in entrance, if there is an overlap shift right of the overlap, keep going until a valid position is found or until it has been established that the rectangle does not fit anywhere along the entrance
		
		Edge entranceEdge=null;//spaceTEdgeList.tail;
		int attemptNumber=0;
		int attemptsAvailable=1;//rectangular ferry
		if(nonCornerPosAlternativeEntrance!=null && cornerPosAlternativeEntrance!=null && cornerPosAlternativeEntrance!=null){
			attemptsAvailable=4;
		}
		boolean initialPositionFound=false;
		while(!initialPositionFound && attemptNumber<attemptsAvailable){
			
			double xLimit=0;
			double[][] XYLims=null;
			if(attemptNumber==0){
				entranceEdge=spaceTEdgeList.tail;
				//
				v.parkedPosition[0]=entranceEdge.interval[0];
				v.parkedPosition[1]=entranceEdge.pointPos-v.l;//
				v.updateTempPosition();
				XYLims=v.XYLimits();
				//
				xLimit=entranceEdge.interval[1]-v.w;
			}else if(attemptNumber==1){
				//find this edge in the current edge list
				boolean thisEdgeFound=false;
				Edge currentTEdge=spaceTEdgeList.tail;//BTailEdge;
				entranceEdge=null;
				while(currentTEdge!=null && !thisEdgeFound){
					if(Math.abs(nonCornerPosAlternativeEntrance.pointPos-currentTEdge.pointPos)<tolerance && nonCornerPosAlternativeEntrance.interval[0]<currentTEdge.interval[1] && currentTEdge.interval[0]<nonCornerPosAlternativeEntrance.interval[1]){
						thisEdgeFound=true;
						entranceEdge=currentTEdge;
					}else{
						currentTEdge=currentTEdge.prevEdge;
					}
				}
				//
				if(entranceEdge!=null){
					v.parkedPosition[0]=entranceEdge.interval[0];
					v.parkedPosition[1]=entranceEdge.pointPos-v.l;//
					v.updateTempPosition();
					XYLims=v.XYLimits();
					//
					xLimit=entranceEdge.interval[1]-v.w;
				}
			}else if(attemptNumber==2){
				boolean thisEdgeFound=false;
				Edge currentTEdge=spaceTEdgeList.tail;//BTailEdge;
				entranceEdge=null;
				while(currentTEdge!=null && !thisEdgeFound){
					if(Math.abs(cornerPosAlternativeEntrance.pointPos-currentTEdge.pointPos)<tolerance && cornerPosAlternativeEntrance.interval[0]<currentTEdge.interval[1] && currentTEdge.interval[0]<cornerPosAlternativeEntrance.interval[1]){
						thisEdgeFound=true;
						entranceEdge=currentTEdge;
					}else{
						currentTEdge=currentTEdge.prevEdge;
					}
				}
				//
				if(entranceEdge!=null){
					v.parkedPosition[0]=entranceEdge.interval[0]-v.w+000000.1;//+0.1
					v.parkedPosition[1]=entranceEdge.pointPos-v.l;//
					v.updateTempPosition();
					XYLims=v.XYLimits();
					//
					xLimit=entranceEdge.interval[1]-v.w;
				}
			}else{
				boolean thisEdgeFound=false;
				Edge currentTEdge=spaceTEdgeList.tail;//BTailEdge;
				entranceEdge=null;
				while(currentTEdge!=null && !thisEdgeFound){
					if(Math.abs(cornerPosAlternativeEntrance2.pointPos-currentTEdge.pointPos)<tolerance && cornerPosAlternativeEntrance2.interval[0]<currentTEdge.interval[1] && currentTEdge.interval[0]<cornerPosAlternativeEntrance2.interval[1]){
						thisEdgeFound=true;
						entranceEdge=currentTEdge;
					}else{
						currentTEdge=currentTEdge.prevEdge;
					}
				}
				//
				if(entranceEdge!=null){
					v.parkedPosition[0]=entranceEdge.interval[0]-v.w+000000.1;//
					v.parkedPosition[1]=entranceEdge.pointPos-v.l;//
					v.updateTempPosition();
					XYLims=v.XYLimits();
					//
					xLimit=entranceEdge.interval[1]-v.w;
				}
			}
			
			//
			if(entranceEdge!=null){
				while(!initialPositionFound){
					Edge overlapEdge=spaceBEdgeList.overlapEdge(XYLims[1], XYLims[0]);
					if(overlapEdge!=null){
						v.parkedPosition[0]=overlapEdge.interval[1];
						if(v.parkedPosition[0]>xLimit){
							break;
						}
						v.updateTempPosition();
						XYLims=v.XYLimits();
					}else if((overlapEdge=spaceTEdgeList.overlapEdge(XYLims[1], XYLims[0]))!=null){
						v.parkedPosition[0]=overlapEdge.interval[1];
						if(v.parkedPosition[0]>xLimit){
							break;
						}
						v.updateTempPosition();
						XYLims=v.XYLimits();
					}else if((overlapEdge=spaceREdgeList.overlapEdge(XYLims[0], XYLims[1]))!=null){
						v.parkedPosition[0]=overlapEdge.pointPos;
						if(v.parkedPosition[0]>xLimit){
							break;
						}
						v.updateTempPosition();
						XYLims=v.XYLimits();
					}else if((overlapEdge=spaceLEdgeList.overlapEdge(XYLims[0], XYLims[1]))!=null){
						v.parkedPosition[0]=overlapEdge.pointPos+0.0000001;//
						if(v.parkedPosition[0]>xLimit){
							break;
						}
						v.updateTempPosition();
						XYLims=v.XYLimits();
					}else if(!spaceTEdgeList.anEdgeOverlapsRectangle(XYLims[1], XYLims[0]) && !spaceBEdgeList.anEdgeOverlapsRectangle(XYLims[1], XYLims[0]) && !spaceREdgeList.anEdgeOverlapsRectangle(XYLims[0], XYLims[1]) && !spaceLEdgeList.anEdgeOverlapsRectangle(XYLims[0], XYLims[1])){
						
						initialPositionFound=true;
					}
				}
			}
			//
			attemptNumber++;
		}
		
		
		//
		if(initialPositionFound){//
			vehicleFitsInInitialPosition=true;
			
			v.horizontalAdjacentEdge=entranceEdge;//.vehicle.te;
			v.verticalAdjacentEdge=entranceEdge.end[1];//.vehicle.re;
			
			if(entranceEdge.end[1]==null){
				System.out.println();
			}
			
			//
			initialPosition[0]=v.parkedPosition[0];
			initialPosition[1]=v.parkedPosition[1];
			
			//
			prevMoveWasInY=true;
			prevMoveWasPos=true;
			//
			prevSpaceCoordinate[0]=v.leftPP;
			prevSpaceCoordinate[1]=v.bottomPP;
			
			//
			prevMoveDir=3;
			moveDir=3;
			//
			prevEdge=null;
			firstEdge=null;
			atCornerPosition=false;
		}else{
			//does not fit at all, there is no space left for this vehicle type
			return (slidingProcedureCompleted=false);
		}
		
		
		if(vehicleFitsInInitialPosition){
			
			//Store the bottom most position
			//and left most position (after left moves that do not end on an outer corner position)
			
			//
			BLLikePositions[0][1]=Double.MAX_VALUE;//lb
			BLLikePositions[0][0]=Double.MAX_VALUE;
			BLLikePositions[1][1]=Double.MAX_VALUE;//rb
			BLLikePositions[1][0]=-Double.MAX_VALUE;
			BLLikePositions[2][1]=Double.MAX_VALUE;//bl
			BLLikePositions[2][0]=Double.MAX_VALUE;
			BLLikePositions[3][1]=Double.MAX_VALUE;//br
			BLLikePositions[3][0]=-Double.MAX_VALUE;
			BLLikePositions[4][1]=-1;//br
			BLLikePositions[4][0]=-1;
			
			leftBottomBestScore=Double.MAX_VALUE;//0,lb
			rightBottomBestScore=Double.MAX_VALUE;//1,rb
			bottomLeftBestScore=Double.MAX_VALUE;//2.bl
			bottomRighBestScore=Double.MAX_VALUE;//3,br
			
			if(generateNewEdges){
				newSpaceLEdgeList.reset();
				newSpaceREdgeList.reset();
				newSpaceTEdgeList.reset();
				newSpaceBEdgeList.reset();
			}
			
			nextRectangle=v;
			
			///////////////////////
			
			boolean infinteLoopingDetected=false;
			boolean finished=false;
			//
			ALN.reset();

			//
			int numberOfNonMoves=0;
			int numberOfMoves=0;
			boolean moved=false;
			while(!finished && !infinteLoopingDetected){
				
				
				if(atCornerPosition){
					moveDir=Mod(moveDir-1, 4);
					atCornerPosition=false;
				}else{
					moveDir=Mod(moveDir+1, 4);
				}
				
				
				if(moveDir==0){
					//right
					moved=rightTraverseAdjacentEdge(generateNewEdges);
					//directionString="right";
				}else if(moveDir==1){
					//forwards
					moved=forwardsTraverseAdjacentEdge(generateNewEdges, positionIndex, sameStripAsPrevious);
					//directionString="forwards";
				}else if(moveDir==2){
					//left
					moved=leftTraverseAdjacentEdge(generateNewEdges, positionIndex, sameStripAsPrevious);
					//directionString="left";
				}else{
					//backwards
					moved=backwardsTraverseAdjacentEdge(generateNewEdges);
					//directionString="backwards";
				}
				//check for the termination criterion 
				
				
				
				double distFromInitialOP=distBetween2DCoords(initialPosition, nextRectangle.parkedPosition);
				if(distFromInitialOP<tolerance && numberOfMoves>0){//
					if(moveDir==3){
						finished=true;
					}
				}else{
					if(moved){
						numberOfNonMoves=0;
						if(ALN.valueInList(nextRectangle.parkedPosition) ){
							infinteLoopingDetected=true;
						}else{
							ALN.add(new aNumber(nextRectangle.parkedPosition));
						}
					}else{
						if(numberOfNonMoves>40){
							infinteLoopingDetected=true;
						}
						numberOfNonMoves++;
					}
				}
				numberOfMoves++;
			}
			
			
			//use the "spaceEdges" to calculate the area of the remaining deck space for that vehicle
			if(!infinteLoopingDetected && numberOfMoves>3){
				if(generateNewEdges){
					if(firstEdge==null){
						System.out.println("first edge null");
					}
					firstEdge.end[1]=prevEdge;
					prevEdge.end[0]=firstEdge;
				}
				
				if(generateNewEdges){
					//accept new edges
					spaceLEdgeList.assignToThisEdgeList(newSpaceLEdgeList);
					spaceREdgeList.assignToThisEdgeList(newSpaceREdgeList);
					spaceTEdgeList.assignToThisEdgeList(newSpaceTEdgeList);
					spaceBEdgeList.assignToThisEdgeList(newSpaceBEdgeList);
					//simplify
					simplifySpaceEdgeList(spaceLEdgeList);
					simplifySpaceEdgeList(spaceREdgeList);
					simplifySpaceEdgeList(spaceTEdgeList);
					simplifySpaceEdgeList(spaceBEdgeList);

					linkSpaceEdgeCorners();
				}
			}else{
				return (slidingProcedureCompleted=false);//vehicle did not slide well (should not happen if the vehicle fits in the first place)
			}
		}else{
			return (slidingProcedureCompleted=false);
		}
		//
		return slidingProcedureCompleted;
	}
	
	double distBetween2DCoords(double[] p1, double[] p2){
		return Math.pow(Math.pow(p1[0]-p2[0], 2)+Math.pow(p1[1]-p2[1], 2), 0.5);
	}

	int Mod(int b,int c)
	{
		int a=0;
		a=(int)Math.round((((double)b/(double)c)-(Math.floor((double)b/(double)c)))*((double)c));
		return a;
	}
	
	
	
	//generate strip (adds vehicles using one move type with the constraint that the next vehicle is adjacent to the previously loaded rectangle
	boolean generateStrip(int positionIndex, int quantileIndex, int stripNumber){
		boolean byWidth=(positionIndex<2);
		double targetDimension=targetDimensions[1][quantileIndex];
		if(byWidth){
			targetDimension=targetDimensions[0][quantileIndex];
		}
		//
		double sumOfDimensionsToAdd=0;//find the length/width of vehicles to be added in this strip generation
		//if row find the width available
		//if a column find the length of the available space//
		//find an inner fit rectangle to rule out vehicles that will not fit at all
		//add the first rectangle manually
		//get first rectangle
		Rectangle rectToAdd=YA.getRectangleByTargetDimension(byWidth, targetDimension);
		
		if(rectToAdd!=null){
			
			boolean[] spaceRemainsAndRectangleCanBeAddedToStrip=addRectangleBLUpdateInnerFitPolygonStrip(rectToAdd, positionIndex, false, false, stripNumber);//boolean sameStripAsPrevioustrue;
			
			//find the largest vehicle type (by target dimension) that fits into the container
			if(!spaceRemainsAndRectangleCanBeAddedToStrip[1]){
				while(!spaceRemainsAndRectangleCanBeAddedToStrip[1] && quantileIndex>0){
					quantileIndex--;
					targetDimension=targetDimensions[1][quantileIndex];
					if(byWidth){
						targetDimension=targetDimensions[0][quantileIndex];
					}
					//
					rectToAdd=YA.getRectangleByTargetDimension(byWidth, targetDimension);
					//
					spaceRemainsAndRectangleCanBeAddedToStrip=addRectangleBLUpdateInnerFitPolygonStrip(rectToAdd, positionIndex, false, false, stripNumber);//boolean sameStripAsPrevioustrue;
				}
			}
			
			
			boolean oneRectangleAdded=spaceRemainsAndRectangleCanBeAddedToStrip[1];
			
			//boolean vehicleLoadedLastIteration=spaceRemainsAndRectangleCanBeAddedToStrip;
			Rectangle refToLoadedVehicle=rectToAdd;
			
			while(spaceRemainsAndRectangleCanBeAddedToStrip[1]){
				//pop previously added rectangle from its queue
				if(refToLoadedVehicle!=null){
					YA.yardLanes[rectToAdd.queueNumber].popQueue();
				}
				
				//get next rectangle
				rectToAdd=YA.getRectangleByTargetDimension(byWidth, targetDimension);
				//try to add to the same strip (with the strict constraint that the rectangle is adjacent to that previously placed
				if(rectToAdd==null){
					spaceRemainsAndRectangleCanBeAddedToStrip[1]=false;
				}else{
					
					spaceRemainsAndRectangleCanBeAddedToStrip=addRectangleBLUpdateInnerFitPolygonStrip(rectToAdd, positionIndex, true, true, stripNumber);//boolean sameStripAsPrevioustrue;
					//if the initial vehicle did not fit decrease target dimension until a fit is found or there are no smaller vehicle types
					if(spaceRemainsAndRectangleCanBeAddedToStrip[1]){
						refToLoadedVehicle=rectToAdd;
					}else{
						refToLoadedVehicle=null;
						//
						if(quantileIndex>0){
							
							spaceRemainsAndRectangleCanBeAddedToStrip[1]=true;
							quantileIndex--;
							if(positionIndex<3){
								//decrease the quantile index until the target dimension is less than or equal to
								//
								boolean quantileFound=false;
								if(byWidth){//length is presumed to be the reason (trying to fit another rectangle in the same strip
									while(quantileIndex>0 && !quantileFound){
										if(targetDimensions[1][quantileIndex]>maxLengthOfNextStripRectangle){
											quantileIndex--;
										}else{
											quantileFound=true;
										}
									}
								}else{
									while(quantileIndex>0 && !quantileFound){
										if(targetDimensions[0][quantileIndex]>maxWidthOfNextStripRectangle){
											quantileIndex--;
										}else{
											quantileFound=true;
										}
									}
								}
							}
							targetDimension=targetDimensions[1][quantileIndex];
							if(byWidth){
								targetDimension=targetDimensions[0][quantileIndex];
							}
						}
					}
				}
				
				
			}
			
			return oneRectangleAdded;
		}else{
			return false;
		}
		
		
	}
	
	//int objectiveFunctionIndex, 
	void implementSolutionSimplifiedStrips(int[] scenarioRandomSeeds, int yardIteration, boolean breakTiesWithLeastFullLane, boolean useRuleBasedYardPolicy, boolean moduloScenarios, int yardPolicyType, int[][] n, boolean useRandomReasonableBins, Random randNumGen){
		
		if(useRuleBasedYardPolicy){
			YA.implementEvenDistributionSolution();;//check resetting stuff here (as this has previously only been used once per yard)
		}else{
			YA.implementSolution();//check resetting stuff here (as this has previously only been used once per yard)
		}
		
		objectiveValue=0;
		
		for(int i=0;i<scenarioRandomSeeds.length;i++){
			if(moduloScenarios){
				YA.generateYardQueues(scenarioRandomSeeds[0], breakTiesWithLeastFullLane, moduloScenarios, (double)i/scenarioRandomSeeds.length, yardPolicyType, n);
			}else{
				YA.generateYardQueues(scenarioRandomSeeds[i], breakTiesWithLeastFullLane, moduloScenarios, 0, yardPolicyType, n);
			}
			/////////////////////////////////////////
			/////////////////////////////////////////
			
			//YA.printQueues();
			//Option: Base the bin dimension on the remaining relaxed rectangles of each type assuming a fractional allocation of the rectangles that are the best fit
			//then update the distribution before selecting the size of the next bin
			//orientation is entirely random
			double[][] relaxedRemainingRectangleAndAreasByType=YA.vCountsAndAreasAndDistributionInitial();
			//in this case the filling of the bins interferes with random reasonable. Therefore generate the random reasonable gene sequence first
			//boolean useRandomReasonableBins=true;
			if(useRandomReasonableBins){
				reset();//just in time
				//read the solution and implement its instructions (as closely as possible)
				boolean aRectangleFitsInTheRemainingSpace=true;
				Gene currentGene=sol.head;
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
			
			/////////////////////////////////////////
			/////////////////////////////////////////
			reset();//just in time
			
			//YA.printQueues();
			//read the solution and implement its instructions (as closely as possible)
			boolean aRectangleFitsInTheRemainingSpace=true;
			Gene currentGene=sol.head;
			int activeLengthOfSolution=0;//optional (swap positions of parts of the solution that are not possible with the following genes which are possible)
			while(currentGene!=null && aRectangleFitsInTheRemainingSpace){
				int value=currentGene.value;
				int quantileIndex=Math.floorMod(value, numberOfVehicleSelectionQuantiles);
				int orientation=(int)Math.floor((double)value/numberOfVehicleSelectionQuantiles);
				/*if(activeLengthOfSolution==8){
					System.out.println(activeLengthOfSolution);
				}*/
				if(!generateStrip(orientation, quantileIndex, activeLengthOfSolution)){//fillNextBin(setOfQuantiles[quantileIndex], orientation)
					//
					//YA.printQueues();
					//System.out.println(activeLengthOfSolution);
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
					activeLengthOfSolution++;
					currentGene.used=true;
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
	
	
	
	//int objectiveFunctionIndex, 
	void implementBLDecreasingSolution(int[] scenarioRandomSeeds, int yardIteration, boolean breakTiesWithLeastFullLane, boolean useRuleBasedYardPolicy, boolean moduloScenarios, int yardPolicyType, int[][] n, boolean useRandomReasonableBins, Random randNumGen){
		

		if(useRuleBasedYardPolicy){
			YA.implementEvenDistributionSolution();;//check resetting stuff here (as this has previously only been used once per yard)
		}else{
			YA.implementSolution();//check resetting stuff here (as this has previously only been used once per yard)
		}
		
		objectiveValue=0;
		
		for(int i=0;i<scenarioRandomSeeds.length;i++){
			if(moduloScenarios){
				YA.generateYardQueues(scenarioRandomSeeds[0], breakTiesWithLeastFullLane, moduloScenarios, (double)i/scenarioRandomSeeds.length, yardPolicyType, n);
			}else{
				YA.generateYardQueues(scenarioRandomSeeds[i], breakTiesWithLeastFullLane, moduloScenarios, 0, yardPolicyType, n);
			}
			
			reset();//just in time
			
			//form a list of rectangles in decreasing area order
			//sequentially add the largest vehicle possible to BL position
			//positionIndex=0 for BL
			double[] vAreasArray=new double[YA.allRectangles.length];
			for(int j=0;j<YA.allRectangles.length;j++){
				vAreasArray[j]=YA.allRectangles[j].area;
			}
			int[] decreasingAreaOrderIndices=maths.Sort(vAreasArray, true);
			boolean[] parkedV=new boolean[vAreasArray.length];
			boolean positionFound=true;
			while(positionFound){
				positionFound=false;
				//
				for(int j=0;j<parkedV.length && !positionFound;j++){
					if(!parkedV[j]){
						if(BLPlacementFromScratch(YA.allRectangles[decreasingAreaOrderIndices[j]])){
							positionFound=true;
							parkedV[j]=true;
						}
					}
				}
			}
			
			objValPerArrivalScenario[i]=evaluateFitness();
			
			fillInVMixForCurrentScenario(i);
			
			objectiveValue+=objValPerArrivalScenario[i];//objValPerArrivalScenario[i];
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
			break;
		}

	}
	
	
	
	//evaluate solution (including wasted space in the set of bins, for the case of zero shuffling)
	double evaluateFitness(){
		double objectiveValue=0;
		nonPackedVehicleArea=YA.areaOfRectanglesStillInQueue();
		
		utilisation=0;
		
		for(int i=0;i<parkedRectangles.size();i++){
			utilisation+=parkedRectangles.get(i).area;
		}
		
		//
		objectiveValue=utilisation;
		
		packedVehicleArea=utilisation;
		
		utilisation/=totalArea;
		return objectiveValue;
	}
	
	int[][] getVehicleCountsByCut(){
		//in general packing strict feasibilty is harder to determine
		//each individual has to be treated as an individual cut
		//It would be useful to account for alternative loading orders here. However
		//the generation of complete rows and columns sequentially is what makes the approach simple and convenient (and also a good starting point for the general case case where these factors are taken into account
		
		//a simple rule based approach that is proven to account for for cases would be preferable (indeed)
		
		int[][] vCountsByCut=new int[parkedRectangles.size()][YA.vTypes];
		
		//
		for(int i=0;i<parkedRectangles.size();i++){
			Rectangle rect=parkedRectangles.get(i);
			vCountsByCut[i][rect.type]++;//
		}
		//
		return vCountsByCut;
	}
	
	void fillInVMixForCurrentScenario(int scenarioNumber){
		
		//ns[scenarioNumber]=new int[numberOfBins][YA.vTypes];
		//reset current scenario number vmix
		for(int v=0;v<vMixes[scenarioNumber].length;v++){
			vMixes[scenarioNumber][v]=0;
		}
		for(int i=0;i<parkedRectangles.size();i++){
			vMixes[scenarioNumber][parkedRectangles.get(i).type]++;;
		}
	}
	
	//the length of queues array can be worked out later from the dimensions of the arrya that is returned from this method
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
	
	//ideal if this method returns boolean if argument vehicle was parked in BL after regenerating the IFP from scratch
	boolean BLPlacementFromScratch(Rectangle rect){
		//boolean RectPlaced=false;
		
		LEdgeList.reinitialiseLinkedList();
		REdgeList.reinitialiseLinkedList();
		TEdgeList.reinitialiseLinkedList();
		BEdgeList.reinitialiseLinkedList();
		
		//
		updateEdgeLists(LEdgeList, REdgeList, TEdgeList, BEdgeList);
		
		//regenerate the vehicle edges and add them then see
		for(int i=0;i<parkedRectangles.size();i++){
			Rectangle v=parkedRectangles.get(i);
			v.generateEdgeCopies(100001);

			spaceLEdgeList.addEdgeToLinkedList(v.le2);
			spaceREdgeList.addEdgeToLinkedList(v.re2);
			spaceTEdgeList.addEdgeToLinkedList(v.te2);
			spaceBEdgeList.addEdgeToLinkedList(v.be2);
		}
		
		//try to add vehicle to BL
		boolean[] spaceRemainsVehicleAdded=addRectangleBLUpdateInnerFitPolygonStrip(rect, 2, false, false, -1);
		
		
		
		//
		return spaceRemainsVehicleAdded[1];
	}
	
	void reset(){
		numberOfBins=0;
		
		/*cornersOfRemSpace[0][0]=leftBorder;				cornersOfRemSpace[0][1]=topBorder;//initially these are the dimensions of the container
		cornersOfRemSpace[1][0]=leftBorder+pixelWidth;	cornersOfRemSpace[1][1]=topBorder;
		cornersOfRemSpace[2][0]=leftBorder;				cornersOfRemSpace[2][1]=topBorder+pixelLength;
		cornersOfRemSpace[3][0]=leftBorder+pixelWidth;	cornersOfRemSpace[3][1]=topBorder+pixelLength;
		remainingWidth=W;
		remainingLength=L;*/
		
		LEdgeList.reinitialiseLinkedList();
		REdgeList.reinitialiseLinkedList();
		TEdgeList.reinitialiseLinkedList();
		BEdgeList.reinitialiseLinkedList();
		//
		parkedRectangles.clear();
		//
		updateEdgeLists(LEdgeList, REdgeList, TEdgeList, BEdgeList);
		
		//HA.resetToFullLinkedList();//reset to full linked lists of the rectangles to be packed
		//
		YA.resetQueues();
	}
	
	/*void swapSolAndSol2Around(){//after selection and crossover in each generation this method is called
		solRef=sol;
		sol=sol2;
		sol2=solRef;//so now sol refers to the Solution object that sol2 previously referred to and vice versa.
	}*/
	
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
			}else if(keyCommand==KeyEvent.VK_X){
				/*ferryEdgeCoordinates.print(curserXPosition+","+curserYPosition+";");
				edgeCounter++;
				if(edgeCounter==4){
					ferryEdgeCoordinates.print("£");
					edgeCounter=0;
				}
				Control.setText("");*/
			}else if(keyCommand==KeyEvent.VK_T){
				/*ferryEdgeCoordinates.print("£");
				Control.setText("");*/
			}else if(keyCommand==KeyEvent.VK_H){
				/*ferryEdgeCoordinates.print(Control.getText()+";");
				Control.setText("");*/
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
	
	public void saveImage(String fileName, String type) throws IOException{
		ImageIO.write(bf, type, new File(fileName.concat(".").concat(type)));
	}
	
	//public void paint(Graphics g){ throws IOException
	void animation(Graphics g){
		super.paint(g);
		String fontChoice="ROMAN_BASELINE";//"TimesRoman";//
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
		////////////////
		//spaceLEdgeList
		//Remaining space outline
		boolean drawRemainingSpace=false;
		if(drawRemainingSpace) {
			g.setColor(Color.CYAN);
			currentEdge=spaceLEdgeList.head;
			while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
				
				/*String coordString1=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[0])));
				g.drawString(coordString1,(int)Math.round(currentEdge.initialPointPos)+40, (int)Math.round(currentEdge.initialInterval[0]));
				String coordString2=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[1])));
				g.drawString(coordString2,(int)Math.round(currentEdge.initialPointPos)+140, (int)Math.round(currentEdge.initialInterval[1]));
				*///
				g.drawLine((int)Math.round(currentEdge.pointPos), (int)Math.round(currentEdge.interval[0]), (int)Math.round(currentEdge.pointPos), (int)Math.round(currentEdge.interval[1]));
				currentEdge=currentEdge.nextEdge;
			}
			currentEdge=spaceREdgeList.head;
			while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
				/*String coordString1=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[0])));
				g.drawString(coordString1,(int)Math.round(currentEdge.initialPointPos)-100, (int)Math.round(currentEdge.initialInterval[0]));
				String coordString2=String.valueOf((int)Math.round(currentEdge.initialPointPos)).concat(",").concat(String.valueOf((int)Math.round(currentEdge.initialInterval[1])));
				g.drawString(coordString2,(int)Math.round(currentEdge.initialPointPos)-200, (int)Math.round(currentEdge.initialInterval[1]));
				*///
				g.drawLine((int)Math.round(currentEdge.pointPos), (int)Math.round(currentEdge.interval[0]), (int)Math.round(currentEdge.pointPos), (int)Math.round(currentEdge.interval[1]));
				currentEdge=currentEdge.nextEdge;
			}
			//
			currentEdge=spaceBEdgeList.head;
			while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
				g.drawLine((int)Math.round(currentEdge.interval[0]), (int)Math.round(currentEdge.pointPos), (int)Math.round(currentEdge.interval[1]), (int)Math.round(currentEdge.pointPos));
				currentEdge=currentEdge.nextEdge;
			}
			currentEdge=spaceTEdgeList.head;
			while(currentEdge!=null){//leftBorder+topBorder+leftBorder+topBorder+
				g.drawLine((int)Math.round(currentEdge.interval[0]), (int)Math.round(currentEdge.pointPos), (int)Math.round(currentEdge.interval[1]), (int)Math.round(currentEdge.pointPos));
				currentEdge=currentEdge.nextEdge;
			}
		}
		
		g.setFont(new Font(fontChoice,Font.BOLD,11));//|Font.ITALIC"Serif"
		///////////////
		//
		for(int i=0;i<parkedRectangles.size();i++){
			Rectangle currentRectangle=parkedRectangles.get(i);
			if(currentRectangle.colour==null){
				g.setColor(Color.YELLOW);
			}else{
				g.setColor(currentRectangle.colour);
			}
			
			if(floorNotRound) {
				g.fillRect((int)Math.floor(currentRectangle.parkedPosition[0]), (int)Math.floor(currentRectangle.parkedPosition[1]), (int)Math.floor(currentRectangle.w), (int)Math.floor(currentRectangle.l));
			}else {
				g.fillRect((int)Math.round(currentRectangle.parkedPosition[0]), (int)Math.round(currentRectangle.parkedPosition[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
			}
			
			
			if(invertedColours) {
				g.setColor(Color.ORANGE);
				g.setColor(Color.BLACK);
			}else {
				g.setColor(Color.LIGHT_GRAY);
			}
			
			if(floorNotRound) {
				g.drawRect((int)Math.floor(currentRectangle.parkedPosition[0]), (int)Math.floor(currentRectangle.parkedPosition[1]), (int)Math.floor(currentRectangle.w), (int)Math.floor(currentRectangle.l));
			}else {
				g.drawRect((int)Math.round(currentRectangle.parkedPosition[0]), (int)Math.round(currentRectangle.parkedPosition[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
			}
			
			
			//strip number
			g.setColor(Color.BLACK);//"bin=".concat()
			if(currentRectangle.binNumber>=10) {
				g.setFont(new Font(fontChoice,Font.BOLD,11));
				g.drawString(String.valueOf(currentRectangle.binNumber), (int)Math.round(currentRectangle.parkedPosition[0])+2, (int)Math.round(currentRectangle.parkedPosition[1])+25);
			}else {
				g.setFont(new Font(fontChoice,Font.BOLD,15));
				g.drawString(String.valueOf(currentRectangle.binNumber), (int)Math.round(currentRectangle.parkedPosition[0])+5, (int)Math.round(currentRectangle.parkedPosition[1])+25);
			}
			
			
		}
		if(visualise){
			//if(counter>2940 && parkedRectangles.size()==48){
				//Rectangle currentRectangle=parkedRectangles.get(47);
				//g.drawString(String.valueOf(currentRectangle.parkedPosition[0]).concat(",").concat(String.valueOf(currentRectangle.parkedPosition[1])), 700,400);
				//System.out.println(currentRectangle.parkedPosition[0]+","+currentRectangle.parkedPosition[1]);
			//}
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
					
					currentTopLeft[1]+=currentRectangle.l;//.pixelLength;
				}
			}
			//for better visual effect
			currentTopLeft[0]=bins[i].pos[0];
			currentTopLeft[1]=bins[i].pos[1];
			
			if(invertedColours) {
				g.setColor(Color.ORANGE);
			}else {
				g.setColor(Color.CYAN);
			}
			
			g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(bins[i].w), (int)Math.round(bins[i].l));//onePixel/onePixel
			//
			g.setFont(new Font(fontChoice,Font.BOLD,11));//|Font.ITALIC"Serif"
			g.setColor(Color.BLACK);//"bin=".concat()
			g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+2, (int)Math.round(currentTopLeft[1])+25);
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
			g.setFont(new Font(fontChoice,Font.BOLD,15));//|Font.ITALIC
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
				g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(qRect.w), (int)Math.round(qRect.l));
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
				if(invertedColours) {
					g.setColor(Color.BLACK);//"bin=".concat()
				}else {
					g.setColor(Color.BLACK);//"bin=".concat()
				}
				
				
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
			for(int i=0;i<5;i++){
				System.out.println(v_count[i]);
			}
			
		}
		if(invertedColours) {
			g.setColor(Color.BLACK);//"bin=".concat()
		}else {
			g.setColor(Color.YELLOW);//"bin=".concat()
		}
		//g.setColor(Color.YELLOW);
		g.drawString("objective_value=".concat(String.valueOf((totalArea-objectiveValue)).substring(0, 7)), 200, 80);
		//utilisation
		g.drawString("utilisation=".concat(String.valueOf(utilisation).substring(0, Math.min(String.valueOf(utilisation).length(),7))), 200, 120);
		g.drawString("utilisation=".concat(String.valueOf(utilisation)), 2000, 1520);
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
					if(!generateStrip(orientation, quantileIndex, activeLengthOfSolution)){//!fillNextBin(setOfQuantiles[quantileIndex], orientation)
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
						activeLengthOfSolution++;
						//
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
