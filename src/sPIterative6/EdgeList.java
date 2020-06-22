package sPIterative6;

//Christopher Bayliss: University of Southampton, 2016


public class EdgeList {
	Edge head;
	Edge tail;
	
	Edge initialHead;
	Edge initialTail;
	
	boolean breakTiesWithSmallestInterval;
	
	int numTimesAddedToThisList;
	int numTimesRemovedfromThisList;
	int listNumber;
	
	int length;
	
	int initialLength;
	
	boolean type;
	
	//the default case is that edges are order on add
	//boolean notOrdered;
	
	EdgeList(boolean breakTiesWithSmallestInterval, int listNumber, boolean type){
		this.breakTiesWithSmallestInterval=breakTiesWithSmallestInterval;
		this.listNumber=listNumber;
		this.type=type;
	}
	
	void addEdgeToLinkedList2(Edge newEdge){
		newEdge.edgeType=type;
		numTimesAddedToThisList++;
		//log that the edge will now be in a linked list
		newEdge.edgeInList=true;
		
		newEdge.nextEdge=null;
		newEdge.prevEdge=null;
		Edge currentEdge=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newEdge;
			head.prevEdge=null;
			tail=newEdge;
			tail.nextEdge=null;
		}else if(head==tail){//one member
			if(newEdge.pointPos<=head.pointPos){
				if(newEdge.pointPos==head.pointPos){
					newEdge.nextEdge=head;
					head.prevEdge=newEdge;
					head=newEdge;	
				}else{
					newEdge.nextEdge=head;
					head.prevEdge=newEdge;
					head=newEdge;
				}
			}else{
				newEdge.prevEdge=head;
				head.nextEdge=newEdge;
				tail=newEdge;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newEdge.pointPos<=head.pointPos){
				//before the head
				posFound=true;
				newEdge.nextEdge=head;
				head.prevEdge=newEdge;
				head=newEdge;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newEdge.pointPos>=tail.pointPos){
					posFound=true;
					newEdge.prevEdge=tail;
					tail.nextEdge=newEdge;
					tail=newEdge;
				}
			}
			//if not somewhere in between
			if(!posFound){
				currentEdge=head.nextEdge;
				while(!posFound && currentEdge!=null){
					if(newEdge.pointPos<=currentEdge.pointPos){
						//before the current edge
						posFound=true;
						newEdge.nextEdge=currentEdge;
						newEdge.prevEdge=currentEdge.prevEdge;
						currentEdge.prevEdge.nextEdge=newEdge;
						currentEdge.prevEdge=newEdge;
					}
					currentEdge=currentEdge.nextEdge;
				}
			}
		}
		length++;
	}
	
	
	void addEdgeToLinkedList(Edge newEdge){
		newEdge.edgeType=type;
		
		numTimesAddedToThisList++;
		//log that the edge will now be in a linked list
		newEdge.edgeInList=true;
		
		newEdge.nextEdge=null;
		newEdge.prevEdge=null;
		Edge currentEdge=head;
		if(head==null){
			//this implies there are no elements in the list
			head=newEdge;
			head.prevEdge=null;
			tail=newEdge;
			tail.nextEdge=null;
		}else if(head==tail){//one member
			if(newEdge.pointPos<=head.pointPos){
				if(newEdge.pointPos==head.pointPos){
					newEdge.nextEdge=head;
					head.prevEdge=newEdge;
					head=newEdge;	
				}else{
					newEdge.nextEdge=head;
					head.prevEdge=newEdge;
					head=newEdge;
				}
			}else{
				newEdge.prevEdge=head;
				head.nextEdge=newEdge;
				tail=newEdge;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the head
			if(newEdge.pointPos<=head.pointPos){
				//before the head
				posFound=true;
				newEdge.nextEdge=head;
				head.prevEdge=newEdge;
				head=newEdge;
			}
			//does the edge go after the tail
			if(!posFound){
				if(newEdge.pointPos>=tail.pointPos){
					posFound=true;
					newEdge.prevEdge=tail;
					tail.nextEdge=newEdge;
					tail=newEdge;
				}
			}
			//if not somewhere in between
			if(!posFound){
				//is the new edge closer to the head or the tail
				if((newEdge.pointPos-head.pointPos)<(tail.pointPos-newEdge.pointPos)){
					//start searching for the position from the head
					currentEdge=head.nextEdge;
					while(!posFound && currentEdge!=null){
						if(newEdge.pointPos<=currentEdge.pointPos){
							//before the current edge
							posFound=true;
							newEdge.nextEdge=currentEdge;
							newEdge.prevEdge=currentEdge.prevEdge;
							if(currentEdge.prevEdge==null){
								System.out.println();
							}
							currentEdge.prevEdge.nextEdge=newEdge;
							currentEdge.prevEdge=newEdge;
						}
						currentEdge=currentEdge.nextEdge;
					}
				}else{
					//start searching for the position from the tail
					currentEdge=tail.prevEdge;
					while(!posFound && currentEdge!=null){
						if(newEdge.pointPos>=currentEdge.pointPos){
							//before the current edge
							posFound=true;
							newEdge.prevEdge=currentEdge;
							newEdge.nextEdge=currentEdge.nextEdge;
							currentEdge.nextEdge.prevEdge=newEdge;
							currentEdge.nextEdge=newEdge;
						}
						currentEdge=currentEdge.prevEdge;
					}
				}
				
				
				
			}
		}
		length++;
	}
	
	void addEdgeList(EdgeList edgeList){
		Edge currentEdge=edgeList.head;
		Edge nextEdge=null;
		if(currentEdge!=null){
			nextEdge=currentEdge.nextEdge;
			addEdgeToLinkedList(currentEdge);
		}
		//
		while(nextEdge!=null){
			currentEdge=nextEdge;
			nextEdge=currentEdge.nextEdge;
			addEdgeToLinkedList(currentEdge);
		}
	}
	
	void moveEdgeInLinkedList(Edge newEdge, boolean moveForwards){
		if(listNumber==6 && numTimesAddedToThisList==3){
			System.out.println();
		}
		
		if(length>1){
			if(moveForwards){
				if(newEdge.nextEdge!=null){
					boolean edgeNeedsToBeMoved=false;
					if(newEdge.pointPos>newEdge.nextEdge.pointPos){
						edgeNeedsToBeMoved=true;
					}
					//
					if(edgeNeedsToBeMoved){
						Edge currentEdge=newEdge.nextEdge;
						if(length==2){
							tail=newEdge;
							head=currentEdge;
							head.prevEdge=null;
							tail.nextEdge=null;
							head.nextEdge=tail;
							tail.prevEdge=head;
						}else{
							if(newEdge.prevEdge==null){
								//the next edge becomes the new head
								//cut and insert
								head=newEdge.nextEdge;
								newEdge.nextEdge.prevEdge=head;
								head.prevEdge=null;
							}else{
								//cut
								newEdge.prevEdge.nextEdge=newEdge.nextEdge;
								newEdge.nextEdge.prevEdge=newEdge.prevEdge;
							}
							//find which edge the edge goes before
							if(currentEdge.nextEdge!=null){
								currentEdge=currentEdge.nextEdge;
								boolean positionFound=false;
								while(!positionFound){
									if(newEdge.pointPos<currentEdge.pointPos){
										//position found
										positionFound=true;
										currentEdge.prevEdge.nextEdge=newEdge;
										newEdge.prevEdge=currentEdge.prevEdge;
										newEdge.nextEdge=currentEdge;
										currentEdge.prevEdge=newEdge;
									}else if(newEdge.pointPos==currentEdge.pointPos){
										//position found
										positionFound=true;
										currentEdge.prevEdge.nextEdge=newEdge;
										newEdge.prevEdge=currentEdge.prevEdge;
										newEdge.nextEdge=currentEdge;
										currentEdge.prevEdge=newEdge;
									}
									//next if there is one
									if(!positionFound){
										if(currentEdge.nextEdge==null){
											//then the edge goes at the end
											//position found
											positionFound=true;
											currentEdge.nextEdge=newEdge;
											tail=newEdge;
											tail.prevEdge=currentEdge;
											tail.nextEdge=null;
										}else{
											//then consider the next edge
											currentEdge=currentEdge.nextEdge;
										}
									}
								}
							}else{
								//the edge goes at the end
								currentEdge.nextEdge=newEdge;
								tail=newEdge;
								tail.prevEdge=currentEdge;
								tail.nextEdge=null;
							}
						}
					}
				}
			}else{
				if(newEdge.prevEdge!=null){
					boolean edgeNeedsToBeMoved=false;
					if(newEdge.pointPos<newEdge.prevEdge.pointPos){
						edgeNeedsToBeMoved=true;
					}
					//
					if(edgeNeedsToBeMoved){
						Edge currentEdge=newEdge.prevEdge;
						if(length==2){
							tail=currentEdge;
							head=newEdge;
							head.prevEdge=null;
							tail.nextEdge=null;
							head.nextEdge=tail;
							tail.prevEdge=head;
						}else{
							if(newEdge.nextEdge==null){
								//the previous edge becomes the new tail
								tail=newEdge.prevEdge;
								newEdge.prevEdge.nextEdge=tail;
								tail.nextEdge=null;
							}else{
								newEdge.nextEdge.prevEdge=newEdge.prevEdge;
								newEdge.prevEdge.nextEdge=newEdge.nextEdge;
							}
							//find which edge the edge goes after to
							if(currentEdge.prevEdge!=null){
								currentEdge=currentEdge.prevEdge;
								boolean positionFound=false;
								while(!positionFound){
									if(newEdge.pointPos>currentEdge.pointPos){
										//position found
										positionFound=true;
										currentEdge.nextEdge.prevEdge=newEdge;
										newEdge.nextEdge=currentEdge.nextEdge;
										newEdge.prevEdge=currentEdge;
										currentEdge.nextEdge=newEdge;
									}else if(newEdge.pointPos==currentEdge.pointPos){
										//position found
										positionFound=true;
										currentEdge.nextEdge.prevEdge=newEdge;
										newEdge.nextEdge=currentEdge.nextEdge;
										newEdge.prevEdge=currentEdge;
										currentEdge.nextEdge=newEdge;
									}
									//previous if there is one
									if(!positionFound){
										if(currentEdge.prevEdge==null){
											//then the edge goes at the start
											//position found
											positionFound=true;
											currentEdge.prevEdge=newEdge;
											head=newEdge;
											head.nextEdge=currentEdge;
											head.prevEdge=null;
										}else{
											//then consider the previous edge
											currentEdge=currentEdge.prevEdge;
										}
									}
									
								}
							}else{
								//the edge goes at the start
								currentEdge.prevEdge=newEdge;
								head=newEdge;
								head.nextEdge=currentEdge;
								head.prevEdge=null;
							}
						}
					}
				}
			}
		}
		numTimesAddedToThisList++;
	}
	
	void removeEdgeFromLinkedList(Edge edgeToRemove){
		numTimesRemovedfromThisList++;
		
		boolean edgeRemoved=false;
		//if(edgeToRemove.edgeInList){
			//assumes that this method will not be called if the linked list is empty
			//printList(); and that the edge is definitely in the list
			//
			if(head==tail){
				if(head==null){
					System.out.println("head is null but should not be, line 269, EdgeList");
				}
				head=null;
				tail=null;
			}else{
				if(edgeToRemove==head){
					head=head.nextEdge;
					head.prevEdge=null;
					edgeRemoved=true;
				}else if(edgeToRemove==tail){
					tail=tail.prevEdge;
					tail.nextEdge=null;
					edgeRemoved=true;
				}else{
					if(edgeToRemove.edgeInList){
						edgeToRemove.prevEdge.nextEdge=edgeToRemove.nextEdge;
						edgeToRemove.nextEdge.prevEdge=edgeToRemove.prevEdge;
					}
				}
			}
			length--;
		//}
		//edgeToRemove.edgeInList=false;
	}
	
	void removeDeletedEdges(){
		Edge currentEdge=head;
		while(currentEdge!=null){
			if(currentEdge.deleteEdge){
				removeEdgeFromLinkedList(currentEdge);
			}
			//
			currentEdge=currentEdge.nextEdge;
		}
	}
	
	//In areaCalc (EDeck method) edges are moved and modified
	//call this method to update the order of this list after such modifications
	void updatePointPositionOfEdgeInList(Edge movedEdge){
		//check if the position has actually changed first
		if(movedEdge.nextEdge!=null && movedEdge.prevEdge!=null){
			if(movedEdge.nextEdge.pointPos<=movedEdge.pointPos || movedEdge.prevEdge.pointPos>=movedEdge.pointPos){
				//remove edge and add it back in
				removeEdgeFromLinkedList(movedEdge);
				addEdgeToLinkedList(movedEdge);
			}
		}else if(movedEdge.nextEdge!=null){
			if(movedEdge.nextEdge.pointPos<=movedEdge.pointPos){
				//remove edge and add it back in
				removeEdgeFromLinkedList(movedEdge);
				addEdgeToLinkedList(movedEdge);
			}
		}else if(movedEdge.prevEdge!=null){
			if(movedEdge.prevEdge.pointPos>=movedEdge.pointPos){
				//remove edge and add it back in
				removeEdgeFromLinkedList(movedEdge);
				addEdgeToLinkedList(movedEdge);
			}
		}else{
			//one edge in the list
			
		}
	}
	
	boolean edgeInList(Edge edge){
		Edge currentEdge=head;
		while(currentEdge!=null){
			if(edge==currentEdge){
				return true;
			}
			currentEdge=currentEdge.nextEdge;
		}
		return false;
	}
	
	//start behind oppositeEdge ("oppositeEdge" is the name from this list's perspective)
	double distanceToNearestOppositeEdge(Edge oppositeEdge, boolean startAtHead){
		double nearestDistanceToOppositeEdge=-1;
		if(startAtHead){
			Edge currentEdge=head;
			while(currentEdge!=null && nearestDistanceToOppositeEdge==-1){
				if(currentEdge.interval[0]<oppositeEdge.interval[1] && currentEdge.interval[1]>oppositeEdge.interval[0] && oppositeEdge.pointPos<currentEdge.pointPos){
					nearestDistanceToOppositeEdge=currentEdge.pointPos-oppositeEdge.pointPos;
				}
				currentEdge=currentEdge.nextEdge;
			}
		}else{
			Edge currentEdge=tail;
			while(currentEdge!=null && nearestDistanceToOppositeEdge==-1){
				if(currentEdge.interval[0]<oppositeEdge.interval[1] && currentEdge.interval[1]>oppositeEdge.interval[0] && oppositeEdge.pointPos>currentEdge.pointPos){
					nearestDistanceToOppositeEdge=oppositeEdge.pointPos-currentEdge.pointPos;
				}
				currentEdge=currentEdge.prevEdge;
			}
		}
		return nearestDistanceToOppositeEdge;
	}
	
	double pointPosOfNearestOppositeEdge(double pointPos, double intervalPos, boolean belowThisPointPos){
		double pointPosOfNearestOppositeEdge=-1;
		if(belowThisPointPos){
			Edge currentEdge=tail;
			while(currentEdge!=null && pointPosOfNearestOppositeEdge==-1){
				if(currentEdge.pointPos<pointPos){
					if(currentEdge.interval[0]-0.000001<=intervalPos && currentEdge.interval[1]+0.000001>=intervalPos){
						pointPosOfNearestOppositeEdge=currentEdge.pointPos;
					}
				}
				currentEdge=currentEdge.prevEdge;
			}
		}else{
			Edge currentEdge=head;
			while(currentEdge!=null && pointPosOfNearestOppositeEdge==-1){
				if(currentEdge.pointPos>pointPos){
					if(currentEdge.interval[0]-0.000001<=intervalPos && currentEdge.interval[1]+0.000001>=intervalPos){
						pointPosOfNearestOppositeEdge=currentEdge.pointPos;
					}
				}
				currentEdge=currentEdge.nextEdge;
			}
		}
		return pointPosOfNearestOppositeEdge;
	}
	
	//remove edges beyond head or tail
	//this method is very problem specific
	void removeEdgesBeyondHeadOrTail(boolean beyondHead){
		if(beyondHead){
			double limitPP=head.pointPos;
			Edge currentEdge=head.nextEdge;
			while(currentEdge!=null && currentEdge.pointPos<=limitPP){
				removeEdgeFromLinkedList(currentEdge);
				currentEdge=currentEdge.nextEdge;
			}
		}else{
			double limitPP=tail.pointPos;
			Edge currentEdge=tail.prevEdge;
			while(currentEdge!=null && currentEdge.pointPos>=limitPP){
				removeEdgeFromLinkedList(currentEdge);
				currentEdge=currentEdge.prevEdge;
			}
		}
	}
	
	void setIntervalOfEdgesInList(int intervalIndex, double intervalValue, boolean min){
		Edge currentEdge=head;
		while(currentEdge!=null){
			if(min){
				currentEdge.interval[intervalIndex]=Math.min(currentEdge.interval[intervalIndex], intervalValue);
			}else{
				currentEdge.interval[intervalIndex]=Math.max(currentEdge.interval[intervalIndex], intervalValue);
			}
			//remove edges with negative length
			if(currentEdge.lengthOfEdge()<=0){
				removeEdgeFromLinkedList(currentEdge);
			}
			currentEdge=currentEdge.nextEdge;
		}
	}
	
	void removeEdgeWithIntervalsOutsideRemainingSpace(double intervalThreshold, boolean deleteIfLessThan){
		Edge currentEdge=head;
		while(currentEdge!=null){
			if(deleteIfLessThan){
				if(currentEdge.interval[0]<intervalThreshold && currentEdge.interval[1]<intervalThreshold){
					removeEdgeFromLinkedList(currentEdge);
				}
			}else{
				if(currentEdge.interval[0]>intervalThreshold && currentEdge.interval[1]>intervalThreshold){
					removeEdgeFromLinkedList(currentEdge);
				}
			}
			currentEdge=currentEdge.nextEdge;
		}
	}
	
	double lengthOfEdgeWhichIntersectsThisCoordinate(double pp, double intervalC){
		double lengthOfEdge=-1;
		Edge currentEdge=head;
		while(currentEdge!=null){
			if(Math.abs(currentEdge.pointPos-pp)<0.000001 && currentEdge.interval[0]-0.000001<=intervalC && currentEdge.interval[1]+0.000001>=intervalC){
				lengthOfEdge=currentEdge.lengthOfEdge();
				return lengthOfEdge;
			}
			currentEdge=currentEdge.nextEdge;
		}
		return lengthOfEdge;
	}
	
	void copyOfEdgeList(EdgeList edgeList){
		reset();
		this.type=edgeList.type;
		//EdgeList edgeListCopy=new EdgeList(edgeList.breakTiesWithSmallestInterval, edgeList.listNumber);
		Edge currentEdge=edgeList.head;
		while(currentEdge!=null){
			Edge edgeCopy=currentEdge.copyOfThisSpaceEdge();
			addEdgeToEndAlreadyOrdered(edgeCopy);
			currentEdge=currentEdge.nextEdge;
		}
		//return edgeListCopy;
	}
	
	void addEdgeToEndAlreadyOrdered(Edge newEdge){
		newEdge.edgeType=type;
		if(head==null){
			//this implies there are no elements in the list
			head=newEdge;
			head.prevEdge=null;
			tail=newEdge;
			tail.nextEdge=null;
		}else if(head==tail){
			head.nextEdge=newEdge;
			newEdge.prevEdge=head;
			tail=newEdge;
		}else{
			tail.nextEdge=newEdge;
			newEdge.prevEdge=tail;
			tail=newEdge;
		}
		newEdge.edgeInList=true;
		length++;
	}
	
	Edge overlapEdgeYYY(double[] ppRange, double[] intRange){
		Edge overlapEdge=null;
		//start at the beginning or the end
		double tolDir=-1;
		if(!head.edgeType){
			tolDir=1;
		}
		if((ppRange[0]-head.pointPos)<(ppRange[0]-tail.pointPos)){
			//start at the head
			Edge currentEdge=head;
			while(currentEdge!=null){
				if(currentEdge.pointPos<ppRange[1]-tolDir*0.000001){
					if(currentEdge.pointPos>ppRange[0]+tolDir*0.000001){
						if(currentEdge.interval[0]<intRange[1]-0.000001 && currentEdge.interval[1]>intRange[0]+0.000001){
							overlapEdge=currentEdge;
							currentEdge=null;
						}else{
							currentEdge=currentEdge.nextEdge;
						}
					}else{
						currentEdge=currentEdge.nextEdge;
					}
				}else{
					currentEdge=null;
				}
			}
		}else{
			//start at the tail
			Edge currentEdge=tail;
			while(currentEdge!=null){
				if(currentEdge.pointPos>ppRange[0]+tolDir*0.000001){
					if(currentEdge.pointPos<ppRange[1]-tolDir*0.000001){
						if(currentEdge.interval[0]<intRange[1]-0.000001 && currentEdge.interval[1]>intRange[0]+0.000001){
							overlapEdge=currentEdge;
							currentEdge=null;
						}else{
							currentEdge=currentEdge.prevEdge;
						}
					}else{
						currentEdge=currentEdge.prevEdge;
					}
				}else{
					currentEdge=null;
				}
			}
		}
		return overlapEdge;
	}
	
	
	Edge overlapEdge(double[] ppRange, double[] intRange){
		Edge overlapEdge=null;
		//start at the beginning or the end
		if((ppRange[0]-head.pointPos)<(ppRange[0]-tail.pointPos)){
			//start at the head
			Edge currentEdge=head;
			while(currentEdge!=null){
				
				//pretend this is a horizontal edge
				double combinedWidth=Math.max(intRange[1], currentEdge.interval[1])-Math.min(intRange[0], currentEdge.interval[0]);
				double combinedHeight=0;
				if(currentEdge.edgeType){
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos)-Math.min(ppRange[0], currentEdge.pointPos-1);
				}else{
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos+1)-Math.min(ppRange[0], currentEdge.pointPos);
				}
				double sumOfWidths=(intRange[1]-intRange[0])+(currentEdge.interval[1]-currentEdge.interval[0]);
				double sumOfHeights=(ppRange[1]-ppRange[0])+1;
				//
				if(combinedWidth+0.000001<sumOfWidths && combinedHeight+0.000001<sumOfHeights){
					overlapEdge=currentEdge;
					currentEdge=null;
				}else{
					currentEdge=currentEdge.nextEdge;
				}
				
			}
		}else{
			//start at the tail
			Edge currentEdge=tail;
			while(currentEdge!=null){
				
				
				//pretend this is a horizontal edge
				double combinedWidth=Math.max(intRange[1], currentEdge.interval[1])-Math.min(intRange[0], currentEdge.interval[0]);
				double combinedHeight=0;
				if(currentEdge.edgeType){
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos)-Math.min(ppRange[0], currentEdge.pointPos-1);
				}else{
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos+1)-Math.min(ppRange[0], currentEdge.pointPos);
				}
				double sumOfWidths=(intRange[1]-intRange[0])+(currentEdge.interval[1]-currentEdge.interval[0]);
				double sumOfHeights=(ppRange[1]-ppRange[0])+1;
				//
				if(combinedWidth+0.000001<sumOfWidths && combinedHeight+0.000001<sumOfHeights){
					overlapEdge=currentEdge;
					currentEdge=null;
				}else{
					currentEdge=currentEdge.prevEdge;
				}
				
				
				
				
			}
		}
		return overlapEdge;
	}
	
	Edge overlapEdgeZZZ(double[] ppRange, double[] intRange){
		Edge overlapEdge=null;
		//start at the beginning or the end
		if((ppRange[0]-head.pointPos)<(ppRange[0]-tail.pointPos)){
			//start at the head
			Edge currentEdge=head;
			while(currentEdge!=null){
				if(currentEdge.pointPos<ppRange[1]-0.000001){
					if(currentEdge.pointPos>ppRange[0]+0.000001){
						if(currentEdge.interval[0]<intRange[1]-0.000001 && currentEdge.interval[1]>intRange[0]+0.000001){
							overlapEdge=currentEdge;
							currentEdge=null;
						}else{
							currentEdge=currentEdge.nextEdge;
						}
					}else{
						currentEdge=currentEdge.nextEdge;
					}
				}else{
					currentEdge=null;
				}
			}
		}else{
			//start at the tail
			Edge currentEdge=tail;
			while(currentEdge!=null){
				if(currentEdge.pointPos>ppRange[0]+0.000001){
					if(currentEdge.pointPos<ppRange[1]-0.000001){
						if(currentEdge.interval[0]<intRange[1]-0.000001 && currentEdge.interval[1]>intRange[0]+0.000001){
							overlapEdge=currentEdge;
							currentEdge=null;
						}else{
							currentEdge=currentEdge.prevEdge;
						}
					}else{
						currentEdge=currentEdge.prevEdge;
					}
				}else{
					currentEdge=null;
				}
			}
		}
		return overlapEdge;
	}
	
	boolean anEdgeOverlapsRectangle(double[] ppRange, double[] intRange){
		boolean overlapExists=false;
		//start at the beginning or the end
		if((ppRange[0]-head.pointPos)<(ppRange[0]-tail.pointPos)){
			//start at the head
			Edge currentEdge=head;
			while(currentEdge!=null){
				
				//pretend this is a horizontal edge
				double combinedWidth=Math.max(intRange[1], currentEdge.interval[1])-Math.min(intRange[0], currentEdge.interval[0]);
				double combinedHeight=0;
				if(currentEdge.edgeType){
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos)-Math.min(ppRange[0], currentEdge.pointPos-1);
				}else{
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos+1)-Math.min(ppRange[0], currentEdge.pointPos);
				}
				double sumOfWidths=(intRange[1]-intRange[0])+(currentEdge.interval[1]-currentEdge.interval[0]);
				double sumOfHeights=(ppRange[1]-ppRange[0])+1;
				//combined
				if(combinedWidth+0.000001<sumOfWidths && combinedHeight+0.000001<sumOfHeights){
					return true;
				}else{
					currentEdge=currentEdge.nextEdge;
				}
				
				
			}
		}else{
			//start at the tail
			Edge currentEdge=tail;
			while(currentEdge!=null){
				
				
				//pretend this is a horizontal edge
				double combinedWidth=Math.max(intRange[1], currentEdge.interval[1])-Math.min(intRange[0], currentEdge.interval[0]);
				double combinedHeight=0;
				if(currentEdge.edgeType){
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos)-Math.min(ppRange[0], currentEdge.pointPos-1);
				}else{
					combinedHeight=Math.max(ppRange[1], currentEdge.pointPos+1)-Math.min(ppRange[0], currentEdge.pointPos);
				}
				double sumOfWidths=(intRange[1]-intRange[0])+(currentEdge.interval[1]-currentEdge.interval[0]);
				double sumOfHeights=(ppRange[1]-ppRange[0])+1;
				//,
				if(combinedWidth+0.000001<sumOfWidths && combinedHeight+0.000001<sumOfHeights){
					return true;
				}else{
					currentEdge=currentEdge.prevEdge;
				}
				
				
				
				
			}
		}
		return false;
	}
	
	boolean anEdgeOverlapsRectangleZZZ(double[] ppRange, double[] intRange){
		boolean overlapExists=false;
		//start at the beginning or the end
		if((ppRange[0]-head.pointPos)<(ppRange[0]-tail.pointPos)){
			//start at the head
			Edge currentEdge=head;
			while(currentEdge!=null){
				if(currentEdge.pointPos<ppRange[1]-0.000001){
					if(currentEdge.pointPos>ppRange[0]+0.000001){
						if(currentEdge.interval[0]<intRange[1]-0.000001 && currentEdge.interval[1]>intRange[0]+0.000001){
							overlapExists=true;
							currentEdge=null;
						}else{
							currentEdge=currentEdge.nextEdge;
						}
					}else{
						currentEdge=currentEdge.nextEdge;
					}
				}else{
					currentEdge=null;
				}
			}
		}else{
			//start at the tail
			Edge currentEdge=tail;
			while(currentEdge!=null){
				if(currentEdge.pointPos>ppRange[0]+0.000001){
					if(currentEdge.pointPos<ppRange[1]-0.000001){
						if(currentEdge.interval[0]<intRange[1]-0.000001 && currentEdge.interval[1]>intRange[0]+0.000001){
							overlapExists=true;
							currentEdge=null;
						}else{
							currentEdge=currentEdge.prevEdge;
						}
					}else{
						currentEdge=currentEdge.prevEdge;
					}
				}else{
					currentEdge=null;
				}
			}
		}
		return overlapExists;
	}
	
	void assignToThisEdgeList(EdgeList edgeList){
		head=edgeList.head;
		tail=edgeList.tail;
		length=edgeList.length;
		this.type=edgeList.type;

	}
	
	void reset(){
		head=null;
		tail=null;
		length=0;
	}
	
	void clear(){
		head=null;
		tail=null;
		length=0;
	}
	
	boolean listIsCorrect(){
		boolean inOrder=true;
		Edge currentEdge=head;
		int len=0;
		while(currentEdge!=null){
			len++;
			if(currentEdge.nextEdge!=null){
				if(currentEdge.nextEdge.pointPos<currentEdge.pointPos){
					inOrder=false;
				}
			}
			currentEdge=currentEdge.nextEdge;
		}
		if(len==length && inOrder){
			return true;
		}else{
			return false;
		}
	}
	
	void printList(){
		System.out.print("[");
		Edge currentEdge=head;
		while(currentEdge!=null){
			System.out.print(currentEdge.pointPos+","+currentEdge.interval[0]+","+currentEdge.interval[1]+";");
			currentEdge=currentEdge.nextEdge;
		}
		System.out.println("];");
	}
	
	void initialiseLinkedListReferences(double pointPositionCorrection, double intervalCorrection){
		initialHead=head;
		initialTail=tail;
		initialLength=length;
		Edge currentEdge=head;
		while(currentEdge!=null){
			currentEdge.initialiseEdgeReferences(pointPositionCorrection, intervalCorrection);
			currentEdge=currentEdge.nextEdge;
		}
	}
	
	void reinitialiseLinkedList(){
		head=initialHead;
		tail=initialTail;
		length=initialLength;
		Edge currentEdge=initialHead;
		while(currentEdge!=null){
			currentEdge.resetEdge();
			currentEdge=currentEdge.initialNextEdge;
		}
	}
}
