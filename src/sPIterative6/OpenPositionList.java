package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class OpenPositionList {
	OpenPosition head;
	OpenPosition tail;
	
	int length;
	
	int numStillExistAndNotMeasured;
	
	int numberOfTimes=0;
	

	OpenPositionList(){
		
	}
	
	//should be a set, i.e. check for equality with an already existing OP
	void addOPToLinkedList(OpenPosition newOP){
		newOP.nextOP=null;
		newOP.prevOP=null;
		OpenPosition currentOP=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newOP;
			head.prevOP=null;
			tail=newOP;
			tail.nextOP=null;
			length++;
		}else if(head==tail){//one member
			if(!equal(head,newOP)){
				newOP.nextOP=head;
				head.prevOP=newOP;
				head=newOP;
				length++;
			}
		}else{
			//check for repeat
			boolean OPExists=false;
			currentOP=head;
			while(currentOP!=null && !OPExists){
				if(equal(currentOP, newOP)){
					OPExists=true;
				}
				currentOP=currentOP.nextOP;
			}
			if(!OPExists){
				newOP.nextOP=head;
				head.prevOP=newOP;
				head=newOP;
				length++;
			}
		}
		//printList();
	}
	
	void addOPToLinkedList3(OpenPosition newOP){
		newOP.nextOP3=null;
		newOP.prevOP3=null;
		OpenPosition currentOP=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newOP;
			head.prevOP3=null;
			tail=newOP;
			tail.nextOP3=null;
			length++;
		}else if(head==tail){//one member
			if(!equal(head,newOP)){
				newOP.nextOP3=head;
				head.prevOP3=newOP;
				head=newOP;
				length++;
			}
		}else{
			//check for repeat
			boolean OPExists=false;
			currentOP=head;
			while(currentOP!=null && !OPExists){
				if(equal(currentOP, newOP)){
					OPExists=true;
				}
				currentOP=currentOP.nextOP3;
			}
			if(!OPExists){
				newOP.nextOP3=head;
				head.prevOP3=newOP;
				head=newOP;
				length++;
			}
		}
		
		//printList();
	}
	
	void add(OpenPosition newOP){
		//newOP.nextOP2=null;
		newOP.prevOP2=null;
		OpenPosition currentOP=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newOP;
			head.prevOP2=null;
			//tail=newOP;
			//tail.nextOP=null;
			length++;
		}else if(head==tail){//one member
			if(!equal(head,newOP)){
			//	newOP.nextOP=head;
				newOP.prevOP2=head;
				head=newOP;
				length++;
			}
		}else{
			//check for repeat
			//newOP.nextOP=head;
			boolean OPExists=false;
			currentOP=head;
			while(currentOP!=null && !OPExists){
				if(equal(currentOP, newOP)){
					OPExists=true;
				}
				currentOP=currentOP.prevOP2;
			}
			if(!OPExists){
				newOP.prevOP2=head;
				head=newOP;
				length++;
			}
		}
		
		//printList();
	}
	
	//the assumption is that this is already a set of open positions
	void addOPToLinkedListOrderedByScore(OpenPosition newOP){
		newOP.nextOP=null;
		newOP.prevOP=null;
		OpenPosition currentOP=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newOP;
			head.prevOP=null;
			tail=newOP;
			tail.nextOP=null;
		}else if(head==tail){//one member
			if(newOP.score>head.score){
				newOP.nextOP=head;
				head.prevOP=newOP;
				head=newOP;
			}else{
				newOP.prevOP=head;
				head.nextOP=newOP;
				tail=newOP;
			}
		}else{
			if(newOP.score>head.score){
				newOP.nextOP=head;
				head.prevOP=newOP;
				head=newOP;
			}else if(newOP.score<=tail.score){
				newOP.prevOP=tail;
				tail.nextOP=newOP;
				tail=newOP;
			}else{
				boolean posFound=false;
				currentOP=head.nextOP;
				while(!posFound && currentOP!=null){
					if(newOP.score>currentOP.score){
						newOP.nextOP=currentOP;
						newOP.prevOP=currentOP.prevOP;
						currentOP.prevOP.nextOP=newOP;
						currentOP.prevOP=newOP;
						posFound=true;
					}else{
						currentOP=currentOP.nextOP;
					}
				}
				//
				if(!posFound){
					newOP.prevOP=tail;
					tail.nextOP=newOP;
					tail=newOP;
				}
			}
		}
		length++;
		//printList();
	}
	
	//this is failing to identify repeats
	void addOPToLinkedListOrderedByGeneratioNumber(OpenPosition newOP){
		
		/*if(newOP.r[0]==451 && newOP.r[1]==101){
			numberOfTimes++;
		}
		if(numberOfTimes==2){
			System.out.println();
		}*/
		newOP.nextOP=null;
		newOP.prevOP=null;
		OpenPosition currentOP=head;
		
		if(head==null){
			//this implies there are no elements in the list
			head=newOP;
			head.prevOP=null;
			tail=newOP;
			tail.nextOP=null;
			length++;
		}else if(head==tail){//one member
			if(!equal(head,newOP)){
				if(newOP.createdAtMoveNumber<head.createdAtMoveNumber){
					newOP.nextOP=head;
					head.prevOP=newOP;
					head=newOP;
				}else{
					newOP.prevOP=head;
					head.nextOP=newOP;
					tail=newOP;
				}
				length++;
			}
		}else{
			
			//it is necessary to cycle through all of the open positions
			//to check for a repeat
			boolean alreadyExists=false;
			currentOP=head;
			while(!alreadyExists && currentOP!=null){
				if(equal(currentOP, newOP)){
					alreadyExists=true;
				}else{
					currentOP=currentOP.nextOP;
				}
			}
			
			if(!alreadyExists){
				if(newOP.createdAtMoveNumber<head.createdAtMoveNumber ){
					newOP.nextOP=head;
					head.prevOP=newOP;
					head=newOP;
					length++;
				}else if(newOP.createdAtMoveNumber>=tail.createdAtMoveNumber){
					newOP.prevOP=tail;
					tail.nextOP=newOP;
					tail=newOP;
					length++;
				}else{
					boolean posFound=false;
					currentOP=head.nextOP;
					while(!posFound && currentOP!=null){
						if(newOP.createdAtMoveNumber<currentOP.createdAtMoveNumber){
							posFound=true;
							newOP.nextOP=currentOP;
							newOP.prevOP=currentOP.prevOP;
							currentOP.prevOP.nextOP=newOP;
							currentOP.prevOP=newOP;
							length++;
						}
						currentOP=currentOP.nextOP;
					}
				}
			}
			
		}
		//printList();
	}
	
	void removeOPFromLinkedList(OpenPosition opToRemove){
		//assumes that this method will not be called if the linked list is empty
		//printList();
		if(head==tail){
			head=null;
			tail=null;
		}else{
			if(opToRemove==head){
				head=head.nextOP;
				head.prevOP=null;
			}else if(opToRemove==tail){
				tail=tail.prevOP;
				tail.nextOP=null;
			}else{
				OpenPosition currentOP=head;
				while(currentOP!=opToRemove){
					currentOP=currentOP.nextOP;
				}
				currentOP.prevOP.nextOP=currentOP.nextOP;
				currentOP.nextOP.prevOP=currentOP.prevOP;
			}
		}
		length--;//assumes that opToRemove is definitely in the list
		//printList();
	}
	
	void removeOPFromLinkedList3(OpenPosition opToRemove){
		//assumes that this method will not be called if the linked list is empty
		//printList();
		if(head==tail){
			head=null;
			tail=null;
		}else{
			if(opToRemove==head){
				head=head.nextOP3;
				head.prevOP3=null;
			}else if(opToRemove==tail){
				tail=tail.prevOP3;
				tail.nextOP3=null;
			}else{
				OpenPosition currentOP=head;
				while(currentOP!=opToRemove){
					currentOP=currentOP.nextOP3;
				}
				currentOP.prevOP3.nextOP3=currentOP.nextOP3;
				currentOP.nextOP3.prevOP3=currentOP.prevOP3;
			}
		}
		length--;//assumes that opToRemove is definitely in the list
		//printList();
	}
	
	//use this remove based method in a first in first out context
	OpenPosition popOP(){
		OpenPosition popOP=null;
		if(head!=null){
			popOP=head;
			removeOPFromLinkedList(head);
		}
		return popOP;
	}
	
	//sort according to a lowest score first order
	void sortList(){//int sortMethod
		OpenPosition HEAD=head;
		//OpenPosition TAIL=tail;
		reset();
		OpenPosition currentOP=HEAD;
		while(currentOP!=null){
			OpenPosition nextOP=currentOP.nextOP;
			/*if(){
				
			}*/
			addOPToLinkedListOrderedByScore(currentOP);
			currentOP=nextOP;
		}
	}
	
	//assumes that OP lists are vehicle type specific
	boolean equal(OpenPosition oneOP, OpenPosition otherOP){
		boolean isEqual=false;
		if(Math.abs(otherOP.r[0]-oneOP.r[0])<0.000001 && Math.abs(otherOP.r[1]-oneOP.r[1])<0.000001){
			isEqual=true;
		}
		return isEqual;
	}
	
	
	
	void reset(){
		head=null;
		tail=null;
		length=0;
	}
	
	void resetMeasured(){
		numStillExistAndNotMeasured=0;
		OpenPosition currentOP=head;
		while(currentOP!=null){
			currentOP.measured=false;
			if(currentOP.stillExists){
				numStillExistAndNotMeasured++;
			}
			currentOP=currentOP.nextOP;
		}
	}
	
	OpenPosition getExistingNonMeasureOP(){
		boolean OPFound=false;
		OpenPosition currentOP=head;
		while(currentOP!=null && !OPFound){
			if(currentOP.measured || !currentOP.stillExists){
				currentOP=currentOP.nextOP;
			}else{
				currentOP.measured=true;//to save having to find this later after physically taking the measurement starting from this position
				OPFound=true;
			}
		}
		if(OPFound){
			return currentOP;
		}else{
			return null;
		}
	}
	
	void copyList(OpenPositionList VList){
		//separately copies are required (because the links will change)
		reset();
		OpenPosition currentOP=VList.tail;//tail to avoid creating a lis in the opposite order
		while(currentOP!=null){
			addOPToLinkedList(currentOP.copyOfThis());
			currentOP=currentOP.prevOP;
		}
	}
	
	OpenPositionList copy(){
		OpenPositionList OPL=new OpenPositionList();
		OPL.copyList(this);
		return OPL;
	}
	
	void printList(){
		OpenPosition currentOP=head;
		while(currentOP!=null){
			System.out.println(currentOP.vType+","+currentOP.score+","+currentOP.r[0]+","+currentOP.r[1]);
			currentOP=currentOP.nextOP;
		}
		System.out.println();
	}
}

