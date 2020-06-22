package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class IntersectionNode {
	
	static double[] A;
	
	static int vTypes;
	
	static VMixIntersectionCalculator VMC;
	
	int scenariosIncluded;
	boolean skipNode;
	
	boolean solutionToIncludeIncluded;
	
	int scenarioNumber;
	ScenarioSolution SS;
	
	int[] vMix;
	double intersectionArea;
	
	int[] intersectionVMix;
	
	int[] vMixRef;
	
	IntersectionNode parentNode;
	//
	int numberOfChildNodes=0;
	IntersectionNode headChildNode;
	IntersectionNode tailChildNode;
	//
	IntersectionNode nextChildNode;
	IntersectionNode prevChildNode;
	//
	IntersectionNode currentChildNode;
	
	//root node constructor
	IntersectionNode(){
		this.scenarioNumber=-1;
	}
	
	IntersectionNode(int scenarioNumber, IntersectionNode parentNode, ScenarioSolution SS){
		this.scenarioNumber=scenarioNumber;
		this.SS=SS;
		vMix=SS.vMix[scenarioNumber];
		//
		this.parentNode=parentNode;
		if(parentNode.scenarioNumber==-1){
			intersectionVMix=new int[vMix.length];
			for(int v=0;v<vMix.length;v++){
				intersectionVMix[v]=vMix[v];
				intersectionArea+=(A[v]*intersectionVMix[v]);
			}
		}else{
			intersectionVMix=new int[vMix.length];
			//
			if(parentNode.intersectionVMix==null || vMix==null){
				System.out.println();
			}
			VMC.calculateIntersectionVMix(parentNode.intersectionVMix, vMix);
			vMixRef=VMC.intersectionVMix;
			for(int v=0;v<vTypes;v++){
				intersectionVMix[v]=vMixRef[v];
			}
			
		}
	}
	
	IntersectionNode(int scenarioNumber, int scenariosIncluded, IntersectionNode parentNode, ScenarioSolution SS){
		this.scenarioNumber=scenarioNumber;
		this.scenariosIncluded=scenariosIncluded;
		this.SS=SS;
		vMix=SS.vMix[scenarioNumber];
		//
		this.parentNode=parentNode;
		if(parentNode.scenarioNumber==-1){
			intersectionVMix=new int[vMix.length];
			for(int v=0;v<vMix.length;v++){
				intersectionVMix[v]=vMix[v];
				intersectionArea+=(A[v]*intersectionVMix[v]);
			}
		}else{
			intersectionVMix=new int[vMix.length];
			/*for(int v=0;v<vMix.length;v++){
				intersectionVMix[v]=Math.min(parentNode.intersectionVMix[v], vMix[v]);
				intersectionArea+=(A[v]*intersectionVMix[v]);
			}*/
			VMC.calculateIntersectionVMix(parentNode.intersectionVMix, vMix);
			vMixRef=VMC.intersectionVMix;
			for(int v=0;v<vTypes;v++){
				intersectionVMix[v]=vMixRef[v];
			}
			
			
		}
	}
	
	
	IntersectionNode(int scenarioNumber, int scenariosIncluded, IntersectionNode parentNode, ScenarioSolution SS, boolean thisIsSolutionToInclude){
		this.scenarioNumber=scenarioNumber;
		this.scenariosIncluded=scenariosIncluded;
		this.SS=SS;
		vMix=SS.vMix[scenarioNumber];
		//
		if(thisIsSolutionToInclude){
			solutionToIncludeIncluded=true;
		}else{
			solutionToIncludeIncluded=parentNode.solutionToIncludeIncluded;
		}
		//
		this.parentNode=parentNode;
		if(parentNode.scenarioNumber==-1){
			intersectionVMix=new int[vMix.length];
			for(int v=0;v<vMix.length;v++){
				intersectionVMix[v]=vMix[v];
				intersectionArea+=(A[v]*intersectionVMix[v]);
			}
		}else{
			intersectionVMix=new int[vMix.length];
			
			VMC.calculateIntersectionVMix(parentNode.intersectionVMix, vMix);
			vMixRef=VMC.intersectionVMix;
			for(int v=0;v<vTypes;v++){
				intersectionVMix[v]=vMixRef[v];
			}
			
			
		}
	}
	
	
	IntersectionNode(int scenarioNumber, int scenariosIncluded, IntersectionNode parentNode){
		this.scenarioNumber=scenarioNumber;
		this.scenariosIncluded=scenariosIncluded;
		//this.SS=SS;
		vMix=parentNode.vMix;//[scenarioNumber];//same as the parent node
		//
		this.parentNode=parentNode;
		
		skipNode=true;
		
		intersectionVMix=new int[vMix.length];
		intersectionArea=parentNode.intersectionArea;
		for(int v=0;v<vMix.length;v++){
			intersectionVMix[v]=parentNode.intersectionVMix[v];//vMix[v];
		}
		
	}
	
	
	
	void addChildNodeToLinkedList(IntersectionNode newChildNode){
		
		
		newChildNode.nextChildNode=null;
		newChildNode.prevChildNode=null;
		IntersectionNode currentNum=headChildNode;
		if(headChildNode==null){
			//this implies there are no elements in the list
			headChildNode=newChildNode;
			headChildNode.prevChildNode=null;
			tailChildNode=newChildNode;
			tailChildNode.nextChildNode=null;
			//
			currentChildNode=headChildNode;
		}else if(headChildNode==tailChildNode){//one member
			if(newChildNode.intersectionArea>=headChildNode.intersectionArea){
				if(newChildNode.intersectionArea==headChildNode.intersectionArea){
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;	
				}else{
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;
				}
			}else{
				newChildNode.prevChildNode=headChildNode;
				headChildNode.nextChildNode=newChildNode;
				tailChildNode=newChildNode;
			}
		}else{
			boolean posFound=false;
			//does the edge go before the headChildNode
			if(newChildNode.intersectionArea>=headChildNode.intersectionArea){
				if(newChildNode.intersectionArea==headChildNode.intersectionArea){
					posFound=true;
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;
				}else{
					//before the headChildNode
					posFound=true;
					newChildNode.nextChildNode=headChildNode;
					headChildNode.prevChildNode=newChildNode;
					headChildNode=newChildNode;
				}
			}
			//does the edge go after the tailChildNode
			if(!posFound){
				if(newChildNode.intersectionArea<=tailChildNode.intersectionArea){
					if(newChildNode.intersectionArea==tailChildNode.intersectionArea){
						posFound=true;
						newChildNode.prevChildNode=tailChildNode;
						tailChildNode.nextChildNode=newChildNode;
						tailChildNode=newChildNode;
					}else{
						posFound=true;
						newChildNode.prevChildNode=tailChildNode;
						tailChildNode.nextChildNode=newChildNode;
						tailChildNode=newChildNode;
					}
				}
			}
			//if not somewhere in between
			if(!posFound){
				currentNum=headChildNode.nextChildNode;
				while(!posFound && currentNum!=null){
					if(newChildNode.intersectionArea>=currentNum.intersectionArea){
						if(newChildNode.intersectionArea==currentNum.intersectionArea){
							posFound=true;
							newChildNode.nextChildNode=currentNum;
							newChildNode.prevChildNode=currentNum.prevChildNode;
							currentNum.prevChildNode.nextChildNode=newChildNode;
							currentNum.prevChildNode=newChildNode;
						}else{
							//before the current edge
							posFound=true;
							newChildNode.nextChildNode=currentNum;
							newChildNode.prevChildNode=currentNum.prevChildNode;
							currentNum.prevChildNode.nextChildNode=newChildNode;
							currentNum.prevChildNode=newChildNode;
						}
					}
					currentNum=currentNum.nextChildNode;
				}
			}
		}
		numberOfChildNodes++;
		//printList();
		
	}
	
	void removeChildNodeFromLinkedList(IntersectionNode scenSolToRemove){
		if(headChildNode==tailChildNode){
			headChildNode=null;
			tailChildNode=null;
		}else{
			if(scenSolToRemove==headChildNode){
				headChildNode=headChildNode.nextChildNode;
				headChildNode.prevChildNode=null;
			}else if(scenSolToRemove==tailChildNode){
				tailChildNode=tailChildNode.prevChildNode;
				tailChildNode.nextChildNode=null;
			}else{
				scenSolToRemove.prevChildNode.nextChildNode=scenSolToRemove.nextChildNode;
				scenSolToRemove.nextChildNode.prevChildNode=scenSolToRemove.prevChildNode;
			}
		}
		numberOfChildNodes--;
	}
}
