package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.io.PrintWriter;

public class aNumberList {
	aNumber head;
	aNumber tail;
	
	int listNumber;
	int length;
	
	public aNumberList(int listNumber){
		this.listNumber=listNumber;
	}
	
	void addEdgeToLinkedList(aNumber newNum){
		
		
		newNum.nextNum=null;
		newNum.prevNum=null;
		aNumber currentNum=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newNum;
			head.prevNum=null;
			tail=newNum;
			tail.nextNum=null;
		}else if(head==tail){//one member
			if(newNum.value<=head.value){
				if(newNum.value==head.value){
					newNum.nextNum=head;
					head.prevNum=newNum;
					head=newNum;	
				}else{
					newNum.nextNum=head;
					head.prevNum=newNum;
					head=newNum;
				}
			}else{
				newNum.prevNum=head;
				head.nextNum=newNum;
				tail=newNum;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newNum.value<=head.value){
				if(newNum.value==head.value){
					posFound=true;
					newNum.nextNum=head;
					head.prevNum=newNum;
					head=newNum;
				}else{
					//before the head
					posFound=true;
					newNum.nextNum=head;
					head.prevNum=newNum;
					head=newNum;
				}
			}
			//does the edge go after the tail
			if(!posFound){
				if(newNum.value>=tail.value){
					if(newNum.value==tail.value){
						posFound=true;
						newNum.prevNum=tail;
						tail.nextNum=newNum;
						tail=newNum;
					}else{
						posFound=true;
						newNum.prevNum=tail;
						tail.nextNum=newNum;
						tail=newNum;
					}
				}
			}
			//if not somewhere in between
			if(!posFound){
				currentNum=head.nextNum;
				while(!posFound && currentNum!=null){
					if(newNum.value<=currentNum.value){
						if(newNum.value==currentNum.value){
							posFound=true;
							newNum.nextNum=currentNum;
							newNum.prevNum=currentNum.prevNum;
							currentNum.prevNum.nextNum=newNum;
							currentNum.prevNum=newNum;
						}else{
							//before the current edge
							posFound=true;
							newNum.nextNum=currentNum;
							newNum.prevNum=currentNum.prevNum;
							currentNum.prevNum.nextNum=newNum;
							currentNum.prevNum=newNum;
						}
					}
					currentNum=currentNum.nextNum;
				}
			}
		}
		length++;
		//printList();
		
	}
	
	void removeEdgeFromLinkedList(aNumber numToRemove){
		//boolean edgeRemoved=false;

		if(head==tail){
			if(head==null){
				System.out.println("head is null but should not be, line 269, EdgeList");
			}
			head=null;
			tail=null;
		}else{
			if(numToRemove==head){
				//if(head.nextNum==tail){
					head=head.nextNum;
					head.prevNum=null;
				/*}else{
					head=head.nextNum;
					head.prevNum=null;
				}*/
				//edgeRemoved=true;
			}else if(numToRemove==tail){
				//if(tail.prevNum==head){
					tail=tail.prevNum;
					tail.nextNum=null;
				/*}else{
					tail=tail.prevNum;
					tail.nextNum=null;
				}*/
				//edgeRemoved=true;
			}else{
				
				numToRemove.prevNum.nextNum=numToRemove.nextNum;
				numToRemove.nextNum.prevNum=numToRemove.prevNum;
			}
		}
		length--;
	}
	
	public void add(aNumber newNum){
		newNum.nextNum=null;
		newNum.prevNum=null;
		aNumber currentNum=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newNum;
			head.prevNum=null;
			tail=newNum;
			tail.nextNum=null;
		}else if(head==tail){//one member
			newNum.prevNum=head;
			head.nextNum=newNum;
			tail=newNum;
		}else{
			newNum.prevNum=tail;
			tail.nextNum=newNum;
			tail=newNum;
		}
		length++;
		//printList();
		
	}
	
	public void reset(){
		head=null;
		tail=null;
		length=0;
	}
	
	public boolean valueInList(double[] aValues){
		aNumber currentNumber=head;
		int counter=0;
		while(currentNumber!=null){
			if(counter>4 && Math.abs(currentNumber.values[0]-aValues[0])<0.000001 && Math.abs(currentNumber.values[1]-aValues[1])<0.000001){
				return true;
			}else{
				currentNumber=currentNumber.nextNum;
			}
			counter++;
		}
		return false;
	}
	
	double getQuantile(double quantile){//quantile is in range 0-1
		int quantIndex=(int)(Math.min(length-1, Math.round(quantile*length)));
		aNumber currentNum=head;
		double theQuantile=head.value;
		int counter=0;
		while(counter<quantIndex){
			currentNum=currentNum.nextNum;
			if(currentNum!=null){
				theQuantile=currentNum.value;
			}
			counter++;
		}
		//theQuantile=currentNum.value;
		return theQuantile;
	}
	
	void printWrite(PrintWriter pr){
		aNumber currentNum=head;
		while(currentNum!=null){
			pr.println(currentNum.value);
			currentNum=currentNum.nextNum;
		}
	}
}
