package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class ScenarioSolution {
	
	static double[] A;//
	
	Container container;
	Solution sol;
	YardSolution ySol;
	int packingSolutionNumber;
	//int scenarioNumber;
	int[][] vMix;//vehicle mix per scenario
	int[][][] ns;
	
	double[] totalRectangleArea;
	
	double intersectionArea;//instead of creating a wrapper return object
	
	boolean[] dominated;
	
	//
	boolean[] constrainedDominated;//for ensuring that the specified candidate is selected for the specified scenario
	
	boolean[] candidate;
	boolean[] isInIntersection;
	
	int numberOfCandidates;//is candidate and not dominated
	
	ScenarioSolution nextScenSol;
	ScenarioSolution prevScenSol;
	
	ScenarioSolution nextScenSolCL;
	ScenarioSolution prevScenSolCL;
	
	ScenarioSolution(Solution sol, YardSolution ySol, int[][] vMixes){//YardSolution ySolSolution sol, Container container, 
		//
		//this.container=container;
		this.sol=new Solution(sol);//container.
		this.ySol=new YardSolution(ySol);//container.YA.sol
		//
		totalRectangleArea=new double[vMixes.length];
		//
		vMix=new int[vMixes.length][vMixes[0].length];
		//ns=new int[vMixes.length][1][1];
		for(int i=0;i<vMixes.length;i++){
			//ns[i]=container.ns[i];
			for(int j=0;j<vMixes[i].length;j++){
				vMix[i][j]=vMixes[i][j];
				totalRectangleArea[i]+=(vMix[i][j]*A[j]);
			}
		}
		//
		dominated=new boolean[vMix.length];
		constrainedDominated=new boolean[vMix.length];
		candidate=new boolean[vMix.length];
		isInIntersection=new boolean[vMix.length];
	}
	
	ScenarioSolution(Solution sol, YardSolution ySol, int[][] vMixes, int solutionNumber){//YardSolution ySolSolution sol, 
		//
		//this.container=container;
		this.sol=new Solution(sol);//container.
		this.ySol=new YardSolution(ySol);//container.YA.sol
		//
		totalRectangleArea=new double[vMixes.length];
		//
		vMix=new int[vMixes.length][vMixes[0].length];
		//ns=new int[vMixes.length][1][1];
		for(int i=0;i<vMixes.length;i++){
			//ns[i]=container.ns[i];
			for(int j=0;j<vMixes[i].length;j++){
				vMix[i][j]=vMixes[i][j];
				totalRectangleArea[i]+=(vMix[i][j]*A[j]);
			}
		}
		//
		packingSolutionNumber=solutionNumber;
		
		//
		dominated=new boolean[vMix.length];
		constrainedDominated=new boolean[vMix.length];
		candidate=new boolean[vMix.length];
		isInIntersection=new boolean[vMix.length];
	}
	
	ScenarioSolution(int[][] vMixes){//YardSolution ySolSolution sol, 
		//
		//this.container=container;
		//this.sol=new Solution(container.sol);
		//this.ySol=new YardSolution(container.YA.sol);
		//
		totalRectangleArea=new double[vMixes.length];
		//
		vMix=new int[vMixes.length][vMixes[0].length];
		//ns=new int[vMixes.length][1][1];
		for(int i=0;i<vMixes.length;i++){
			//ns[i]=container.ns[i];
			for(int j=0;j<vMixes[i].length;j++){
				vMix[i][j]=vMixes[i][j];
				totalRectangleArea[i]+=(vMix[i][j]*A[j]);
			}
		}
		//
		dominated=new boolean[vMix.length];
		constrainedDominated=new boolean[vMix.length];
		candidate=new boolean[vMix.length];
		isInIntersection=new boolean[vMix.length];
	}
	
	void reuseScenarioSolution(ScenarioSolution SS, int packSolNumber){
		//totalRectangleArea=new double[vMixes.length];
		//
		if(SS!=this){
			//vMix=new int[vMixes.length][vMixes[0].length];
			//ns=new int[vMixes.length][1][1];
			for(int i=0;i<vMix.length;i++){
				//ns[i]=container.ns[i];
				totalRectangleArea[i]=0;
				for(int j=0;j<vMix[i].length;j++){
					vMix[i][j]=SS.vMix[i][j];
					totalRectangleArea[i]+=(vMix[i][j]*A[j]);
				}
				//
				dominated[i]=false;
				constrainedDominated[i]=false;
				candidate[i]=false;
				isInIntersection[i]=false;
			}
			packingSolutionNumber=packSolNumber;
		}
		
	}
}
