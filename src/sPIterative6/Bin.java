package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
//bins and the rows and columns within the container
public class Bin {//in the solution representation the orientation and packing heuristic can be captured by a single integer
	int orientation;//0:b, 1:l, 2:r.
	int packingHeuristic;//select subset of rectangles from this quantile of the
	//remaining rectangles (by width or length depending on the orientation)
	//pack in increasing or decreasing order (shuffling extension)
	boolean increasing;//packing order within bin, this will be useful for shuffling (shuffling is probably not the commonly used word for what is meant here)
	//dimensions are dependent upon the rectangles placed in the bin
	double l;
	double w;
	double h;
	
	double allocatedLength;
	double allocatedWidth;
	
	double[] pos=new double[2];//top left
	
	Rectangle[] rectangles;
	int numberOfRectangles;
	
	Bin(int maximumNumberOfRectangles){
		//maximum number of rectangles (the number that there are)
		rectangles=new Rectangle[maximumNumberOfRectangles];
	}
	
	Bin(Bin aBin){
		orientation=aBin.orientation;
		pos[0]=aBin.pos[0];
		pos[1]=aBin.pos[1];
		w=aBin.w;
		l=aBin.l;
		h=aBin.h;
		rectangles=new Rectangle[aBin.rectangles.length];
		numberOfRectangles=aBin.numberOfRectangles;
		for(int i=0;i<numberOfRectangles;i++){
			rectangles[i]=new Rectangle(aBin.rectangles[i]);
		}
	}
	
	//methods
	//add rectangles (include orientation and allocated dimensions in the argument
	
	//get bin dimensions (max
	void getBinDimensions(){
		l=0;
		w=0;
		h=0;
		for(int i=0;i<numberOfRectangles;i++){
			if(orientation<1){
				//row
				l=Math.max(l, rectangles[i].l);
				w+=rectangles[i].w;
			}else{
				//col
				w=Math.max(w, rectangles[i].w);
				l+=rectangles[i].l;
			}
			h=Math.max(h, rectangles[i].h);
		}
	}
	
	void setBinDimensions(int orientation, double size){
		this.orientation=orientation;
		if(orientation<1){
			l=size;
			w=allocatedWidth;
		}else{
			w=size;
			l=allocatedLength;
		}
	}
	
	double wastedBinSpace(){//call after getting the bins dimensions
		double wastedBinSpace=0;
		if(orientation<1){
			wastedBinSpace=l*(allocatedWidth-w);
		}else{
			wastedBinSpace=w*(allocatedLength-l);
		}
		for(int i=0;i<numberOfRectangles;i++){
			if(orientation<1){
				//row
				wastedBinSpace+=(l-rectangles[i].l)*rectangles[i].w;
			}else{
				//col
				wastedBinSpace+=(w-rectangles[i].w)*rectangles[i].l;
			}
		}
		return wastedBinSpace;
	}
}
