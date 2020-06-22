package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;

public class OpenPosition {
	int widthDirection;//+ve => left open position
	double[] r=new double[2];
	
	double maxWidth;//this can be useful for packing algorithms based on creating efficient rows
	double maxLength;//what is the length of an open position given its maximum width
	boolean adjacentToSide;//
	boolean liftAccess;
	
	double adjacentBottomWidth;
	
	//linked list
	double scoreOPVT;
	double score;
	OpenPosition nextOP3;//
	OpenPosition prevOP3;//
	OpenPosition nextOP;//=new OpenPosition;
	OpenPosition prevOP2;//
	OpenPosition prevOP;//=new OpenPosition[2];
	int createdAtMoveNumber;//if rewind goes to an earlier move number remove it, if it is used or a vehicle is placedon it or overlapping it
	int overlappedAtMoveNumber;
	
	double[] attributeScores=new double[16];
	
	boolean stillExists;
	boolean measured;
	
	ArrayList<Integer> vTypesThatFit=new ArrayList<Integer>(10);
	int vType;
	
	Edge horizontalAdjacentEdge;
	Edge verticalAdjacentEdge;
	
	//OPTag optag;
	double mezzonineDeckOverlapRatio;
	double parkingLoss;//if a vehicle is parked in a position that wastes no other space than the lateral and longitudinal parking gaps (that value is stored as this field)
	
	//linked list can be used to speed up lists of ordered open positions
	
	OpenPosition(double[] r, int widthDirection){
		this.r[0]=r[0];
		this.r[1]=r[1];
		this.widthDirection=widthDirection;
	}
	
	OpenPosition(double[] r, int widthDirection, boolean adjacentToSide){
		this.r[0]=r[0];
		this.r[1]=r[1];
		this.widthDirection=widthDirection;
		this.adjacentToSide=adjacentToSide;
	}
	//
	OpenPosition(double[] r, int widthDirection, boolean adjacentToSide, double adjacentBottomWidth, double maxWidth, double maxLength){
		this.r[0]=r[0];
		this.r[1]=r[1];
		this.widthDirection=widthDirection;
		this.adjacentToSide=adjacentToSide;
		this.adjacentBottomWidth=adjacentBottomWidth;
		this.maxWidth=maxWidth;
		this.maxLength=maxLength;
	}
	
	OpenPosition(double[] r, int widthDirection, boolean adjacentToSide, Edge horizontalAdjacentEdge, Edge verticalAdjacentEdge){
		this.r[0]=r[0];
		this.r[1]=r[1];
		this.widthDirection=widthDirection;
		this.adjacentToSide=adjacentToSide;
		this.horizontalAdjacentEdge=horizontalAdjacentEdge;
		this.verticalAdjacentEdge=verticalAdjacentEdge;
	}
	
	OpenPosition(double[] r, int widthDirection, boolean adjacentToSide, int createdAtMoveNumber, int overlappedAtMoveNumber, double maxWidth, double maxLength, Edge horizontalAdjacentEdge, Edge verticalAdjacentEdge){
		this.r[0]=r[0];
		this.r[1]=r[1];
		this.widthDirection=widthDirection;
		this.adjacentToSide=adjacentToSide;
		//this.nextOP=new OpenPosition[2];
		//this.prevOP=new OpenPosition[2];
		this.createdAtMoveNumber=createdAtMoveNumber;
		this.overlappedAtMoveNumber=overlappedAtMoveNumber;
		this.maxWidth=maxWidth;
		this.maxLength=maxLength;
		if(horizontalAdjacentEdge!=null){
			this.horizontalAdjacentEdge=horizontalAdjacentEdge.copyOfThis();
		}
		if(verticalAdjacentEdge!=null){
			this.verticalAdjacentEdge=verticalAdjacentEdge.copyOfThis();
		}
	}
	
	//this constructor is for initialisation starting from an
	//empty bin
	OpenPosition(double[] r,double[] widthInterval, double[] heightInterval, boolean bottomLeft){
		this.r=r;
	}
	
	OpenPosition copyOfThis(){
		return new OpenPosition(this.r, this.widthDirection, this.adjacentToSide,this.createdAtMoveNumber, this.overlappedAtMoveNumber, this.maxWidth, this.maxLength,this.horizontalAdjacentEdge,this.verticalAdjacentEdge);
	}
	
	boolean isSamePosition(OpenPosition op){
		if(Math.abs(r[0]-op.r[0])<0.000001 && Math.abs(r[1]-op.r[1])<0.000001){
			return true;
		}else{
			return false;
		}
	}
	
	
}
