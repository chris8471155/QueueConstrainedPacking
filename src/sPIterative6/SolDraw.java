package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class SolDraw extends JFrame{
	
	int scenarioNumber;
	
	int topBorder=100;
	int leftBorder=100;
	 //Pays less attention to la
	 //More exceptions on this deck for special requirements and vehicle types
	int Height=1000;
	int Width=1800;
	BufferedImage bf;
	
	Bin[] bins;
	int numberOfBins;
	
	EdgeList LEdgeList;
	EdgeList REdgeList;
	EdgeList TEdgeList;
	EdgeList BEdgeList;
	
	//from Yard
	int[] vCountByType;
	Queue[] yardLanes;
	
	double objectiveValue;
	double utilisation;
	
	
	int numberXShift=1;
	
	SolDraw(Yard YA, EdgeList LEdgeList, EdgeList REdgeList, EdgeList BEdgeList, EdgeList TEdgeList, Bin[] bins, int numberOfBins, double objectiveValue, double utilisation){
		super();
		setSize(Width,Height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground(Color.BLACK);
		
		/////////////////////////////
		this.objectiveValue=objectiveValue;
		this.utilisation=utilisation;
		//
		vCountByType=new int[YA.vCountByType.length];
		for(int i=0;i<vCountByType.length;i++){
			vCountByType[i]=YA.vCountByType[i];
		}
		//bins
		this.numberOfBins=numberOfBins;
		this.bins=new Bin[bins.length];
		for(int i=0;i<bins.length;i++){
			this.bins[i]=new Bin(bins[i]);
		}
		//queues
		yardLanes=new Queue[YA.yardLanes.length];
		for(int i=0;i<yardLanes.length;i++){
			yardLanes[i]=new Queue(YA.yardLanes[i], true);
		}
		//
		this.LEdgeList=LEdgeList;
		this.REdgeList=REdgeList;
		this.BEdgeList=BEdgeList;
		this.TEdgeList=TEdgeList;
		/////////////////////////////
		
		setVisible(true);
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

		g.setFont(new Font("Serif",Font.BOLD,15));//|Font.ITALIC
		//
		
		int[] v_count=new int[vCountByType.length];
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
					
					//if(printBinAndRectangleCoordinates){
						v_count[currentRectangle.type]++;
						//System.out.println("rectangle_"+i+"_"+j+", type="+currentRectangle.type+", width="+currentRectangle.w+", length="+currentRectangle.l+", top left coordinate={"+currentTopLeft[0]+","+currentTopLeft[1]+"}");
					//}
					
					g.setColor(currentRectangle.colour);
					g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					g.setColor(Color.BLACK);
					g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					currentTopLeft[0]+=currentRectangle.w;//pixelWidth;
				}
			}else{
				//left or right column (shift y-coordinate of "currentTopLeft" right)
				int numberOfRectangles=bins[i].numberOfRectangles;
				for(int j=0;j<numberOfRectangles;j++){
					Rectangle currentRectangle=bins[i].rectangles[j];
					
					//if(printBinAndRectangleCoordinates){
						v_count[currentRectangle.type]++;
						//System.out.println("rectangle_"+i+"_"+j+", type="+currentRectangle.type+", width="+currentRectangle.w+", length="+currentRectangle.l+", top left coordinate={"+currentTopLeft[0]+","+currentTopLeft[1]+"}");
					//}
					
					g.setColor(currentRectangle.colour);
					g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					g.setColor(Color.BLACK);
					g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(currentRectangle.w), (int)Math.round(currentRectangle.l));
					currentTopLeft[1]+=currentRectangle.l;//.pixelLength;
				}
			}
			//for better visual effect
			currentTopLeft[0]=bins[i].pos[0];
			currentTopLeft[1]=bins[i].pos[1];
			g.setColor(Color.LIGHT_GRAY);
			g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(bins[i].w), (int)Math.round(bins[i].l));//onePixel/onePixel
			//
			g.setColor(Color.BLACK);//"bin=".concat()
			g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0])+numberXShift, (int)Math.round(currentTopLeft[1])+25);
		}
		
		//yard queues (plus bin labels)
		double[] yardTopLeft={650,100};
		double yardLaneWidth=yardLanes[0].maxWidth;
		double[] currentTopLeft={yardTopLeft[0], yardTopLeft[1]};
		//Queue[] YL=YA.yardLanes;
		int numberOfYardLanes=yardLanes.length;
		for(int i=0;i<numberOfYardLanes;i++){
			Queue queue=yardLanes[i];
			//
			currentTopLeft[0]=yardTopLeft[0]+(i*yardLaneWidth);
			currentTopLeft[1]=yardTopLeft[1];
			//
			g.setColor(Color.YELLOW);//"bin=".concat()
			g.drawString(String.valueOf(i), (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-15);
			if(queue.widthIsTargetDimension){
				g.drawString("W", (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-40);
			}else{
				g.drawString("L", (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-40);
			}
			g.setFont(new Font("Serif",Font.BOLD,10));//|Font.ITALIC
			g.drawString(String.valueOf(queue.targetDimensionLength).substring(0, 5), (int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1])-55);
			g.setFont(new Font("Serif",Font.BOLD,15));//|Font.ITALIC
			//			
			//
			Rectangle qRect=yardLanes[i].head;
			while(qRect!=null){
				if(qRect.binNumber==-1){
					g.setColor(Color.LIGHT_GRAY);
				}else{
					g.setColor(qRect.colour);
				}
				//g.setColor(qRect.colour);
				g.fillRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(qRect.w), (int)Math.round(qRect.l));
				g.setColor(Color.BLACK);
				g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(qRect.w), (int)Math.round(qRect.l));
				//
				g.setColor(Color.BLACK);//"bin=".concat()
				g.drawString(String.valueOf(qRect.binNumber), (int)Math.round(currentTopLeft[0])+numberXShift, (int)Math.round(currentTopLeft[1])+25);
				//
				currentTopLeft[1]+=qRect.l;
				qRect=qRect.nextRectInQueue;
			}
			//demarkate the lanes
			currentTopLeft[0]=yardTopLeft[0]+(i*yardLaneWidth);
			currentTopLeft[1]=yardTopLeft[1];
			g.setColor(Color.GREEN);
			g.drawRect((int)Math.round(currentTopLeft[0]), (int)Math.round(currentTopLeft[1]), (int)Math.round(yardLanes[i].maxWidth), (int)Math.round(yardLanes[i].maxLength));
			
		}
		
		
		
		g.setColor(Color.GREEN);
		
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
		
		//vehicle packed those and the total vehicles of each type to begin with
		for(int i=0;i<v_count.length;i++){
			g.setColor(Color.YELLOW);//"bin=".concat()
			g.drawString("v_type".concat(String.valueOf(i)).concat(":"), 1000, 600+(i*20));
			g.drawString(String.valueOf(vCountByType[i]), 1100, 600+(i*20));
			g.drawString(String.valueOf(v_count[i]), 1150, 600+(i*20));
		}
		
		
		//if(printBinAndRectangleCoordinates){
			/*for(int i=0;i<5;i++){
				System.out.println(v_count[i]);
			}*/
			
		//}
		
		g.setColor(Color.YELLOW);
		g.drawString("objective_value=".concat(String.valueOf(objectiveValue)), 200, 80);
		//utilisation
		g.drawString("utilisation=".concat(String.valueOf(utilisation)), 200, 120);
		g.drawString("utilisation=".concat(String.valueOf(utilisation)), 2000, 1520);
	}
}
