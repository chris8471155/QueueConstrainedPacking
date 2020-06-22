package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class Queue {
	double maxLength;//length of yard lanes
	double maxWidth;//width of lane
	double lengthOfQueue;//distance measure
	//
	Rectangle head;
	Rectangle tail;//rectangle that arrive are assigned to the back of a queue
	int length;
	//
	Rectangle currentRect;
	//
	Rectangle rectAtFront;//this moves along the queue as vehicles are loaded from lanes, the currentRect version is used to avoid modifying the state of the yard
	//
	//what a chromosome is translated into (the following two lines)
	boolean widthIsTargetDimension;
	double targetDimensionLength;
	//
	int number;
	//
	static int vTypes;
	//initial queues before loading begins
	int numberOfVehicleTypesInQueue=0;
	int[] vCounts;
	//vopies (as vehicles are only added to the queues on initialisation
	int currentNumberOfVehicleTypesInQueue=0;
	int[] currentVCounts;
	//
	Queue(double maxLength, double maxWidth, int number){
		this.maxLength=maxLength;
		this.maxWidth=maxWidth;
		this.number=number;
		
		vCounts=new int[vTypes];
		currentVCounts=new int[vTypes];
	}
	
	
	Queue(Queue queue){
		maxLength=queue.maxLength;
		maxWidth=queue.maxWidth;
		length=queue.length;
		
		widthIsTargetDimension=queue.widthIsTargetDimension;
		targetDimensionLength=queue.targetDimensionLength;
		number=queue.number;
		
		vCounts=new int[vTypes];
		currentVCounts=new int[vTypes];
		
	}
	
	Queue(Queue queue, boolean copyRectangles){
		maxLength=queue.maxLength;
		maxWidth=queue.maxWidth;
		length=queue.length;
		
		widthIsTargetDimension=queue.widthIsTargetDimension;
		targetDimensionLength=queue.targetDimensionLength;
		number=queue.number;
		
		vCounts=new int[vTypes];
		currentVCounts=new int[vTypes];
		
		//prev rectangle references are assumed unnecessary for the purpose of this particular constructor
		Rectangle currentRectangle=queue.head;
		Rectangle currentThisRectangle=null;
		if(currentRectangle!=null){
			head=new Rectangle(currentRectangle);
			currentThisRectangle=head;
			currentRectangle=currentRectangle.nextRectInQueue;
		}
		
		while(currentRectangle!=null){
			Rectangle newQueueRect=new Rectangle(currentRectangle);
			currentThisRectangle.nextRectInQueue=newQueueRect;
			currentThisRectangle=newQueueRect;
			//
			currentRectangle=currentRectangle.nextRectInQueue;
		}
		
	}
	
	//methods: add, remove, fill Row/Column from this queue (or similar)
	
	//iterator type methods
	void resetCurrentRectangleToFront(){
		currentRect=rectAtFront;
	}
	//
	void resetFrontRectangle(boolean resetBinNumbers){
		rectAtFront=head;
		if(resetBinNumbers){
			Rectangle currentRectangle=rectAtFront;
			while(currentRectangle!=null){
				currentRectangle.binNumber=-1;
				currentRectangle=currentRectangle.nextRectInQueue;
			}
		}
		//
		currentNumberOfVehicleTypesInQueue=numberOfVehicleTypesInQueue;
		for(int i=0;i<vTypes;i++){
			currentVCounts[i]=vCounts[i];
		}
	}
	//
	double currentRectangleDimension(boolean forNewRow){
		double dimension=-1;
		if(currentRect!=null){
			if(forNewRow){
				dimension=currentRect.w;
			}else{
				dimension=currentRect.l;
			}
		}
		return dimension;
	}
	
	void nextRectangle(){
		if(currentRect!=null){
			currentRect=currentRect.nextRectInQueue;
		}
	}
	
	Rectangle popQueue(){
		Rectangle frontRectangle=rectAtFront;
		if(rectAtFront!=null){
			//maintain the counts of the number of vehicle types
			if(currentVCounts[rectAtFront.type]==1){
				currentNumberOfVehicleTypesInQueue--;
			}
			currentVCounts[rectAtFront.type]--;
			//
			rectAtFront=rectAtFront.nextRectInQueue;
		}
		return frontRectangle;
	}
	
	boolean addRectangleToQueue(Rectangle newRectangle){
		boolean rectFitsInQueue=false;
		double lengthOfNewRectangle=newRectangle.l;
		if(lengthOfQueue+lengthOfNewRectangle<=maxLength && newRectangle.w<=maxWidth){
			//maintain vCounts and the number of vehicle types in this queue
			if(vCounts[newRectangle.type]==0){
				numberOfVehicleTypesInQueue++;
				currentNumberOfVehicleTypesInQueue++;//as there are no other pairs of (initial and current variables)
			}
			vCounts[newRectangle.type]++;
			currentVCounts[newRectangle.type]++;
			//
			rectFitsInQueue=true;
			//
			newRectangle.queueNumber=this.number;
			//
			lengthOfQueue+=lengthOfNewRectangle;
			length++;
			if(head==null){
				head=newRectangle;
				tail=newRectangle;
			}else{
				tail.nextRectInQueue=newRectangle;
				newRectangle.prevRectInQueue=tail;
				tail=newRectangle;
			}
		}
		return rectFitsInQueue;
	}
	
	//when changing yard policy or vehicle arrival order the yard queues have to be reset
	void clearQueue(){
		lengthOfQueue=0;//distance measure
		//
		head=null;
		tail=null;//rectangle that arrive are assigned to the back of a queue
		length=0;
		//
		currentRect=null;
		//
		rectAtFront=null;//
		//
		numberOfVehicleTypesInQueue=0;
		currentNumberOfVehicleTypesInQueue=0;
		for(int i=0;i<vTypes;i++){
			vCounts[i]=0;
			currentVCounts[i]=0;
		}
	}
	
}
