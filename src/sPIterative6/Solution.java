package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.Random;


public class Solution {
	int[] sol;//store max length solution
	int activeLength;//use this to select crossover point and mutation positions that are relevant. This is calculated when the solution is implemented
	
	Gene head;
	Gene tail;
	int length;
	
	static int alphabetLength;
	static int maxSolutionLength;
	static int numberOfQuantiles;
	static int numberOfStripTypes;
	
	int[] consectiveRowsCols=new int[100];
	
	//initial random solution
	Solution(Random randNumGen){
		int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
		head=new Gene(randomValue);
		tail=head;
		//
		for(int i=1;i<maxSolutionLength;i++){
			randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			Gene nextGene=new Gene(randomValue);
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
		}
	}
	
	Solution(String storedSolString){
		String[] lineA=storedSolString.split(",");
		int geneCounter=0;
		head=new Gene(Integer.parseInt(lineA[geneCounter]));	geneCounter++;
		tail=head;
		//
		for(int i=1;i<maxSolutionLength;i++){
			Gene nextGene=new Gene(Integer.parseInt(lineA[geneCounter]));	geneCounter++;
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
		}
	}
	
	void setSolution(int[] intSol){
		int geneCounter=0;
		head=new Gene(intSol[geneCounter]);	geneCounter++;
		tail=head;
		//
		for(int i=1;i<Math.min(intSol.length, maxSolutionLength);i++){
			Gene nextGene=new Gene(intSol[geneCounter]);	geneCounter++;
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
		}
	}
	
	Solution(Solution sol){
		activeLength=maxSolutionLength;
		Gene currentGeneThat=sol.head;
		head=new Gene(currentGeneThat.value);
		tail=head;
		//
		currentGeneThat=currentGeneThat.nextGene;
		//
		for(int i=1;i<maxSolutionLength;i++){
			Gene nextGene=new Gene(currentGeneThat.value);
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
			//
			currentGeneThat=currentGeneThat.nextGene;
		}
	}
	
	Gene getGene(int ind){
		Gene currentGene=head;
		int counter=0;
		while(counter<ind){
			currentGene=currentGene.nextGene;
			counter++;
		}
		return currentGene;
	}
	
	void randomiseSol(Random randNumGen){
		Gene currentGene=head;
		while(currentGene!=null){
			int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			currentGene.setGeneValue(randomValue);
			//
			currentGene=currentGene.nextGene;
		}
	}
	
	//this method is called with an externally defined probability
	//gene[rand*activeLength]=mod(current value+rand*(alphabetLength-1), alphabetLength)
	void mutation(Random randNumGen){
		int randInd=(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		geneToMutate.value=Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
	}
	
	boolean mutationTypeNeighbour(int positionIndex, int newGeneValue) {
		Gene geneToMutate=getGene(positionIndex);
		if(geneToMutate.value==newGeneValue) {
			return false;
		}else {
			geneToMutate.value=newGeneValue;//Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
			return true;
		}
		
		
		
	}
	
	//when using mutation to generate part of a population
	//in an iterative genetic algorithm mutation is considered for each gene at
	//the specified rate
	void mutation(Random randNumGen, double rate){
		Gene currentGene=head;
		while(currentGene!=null){
			if(randNumGen.nextDouble()<rate){
				currentGene.value=Math.floorMod(currentGene.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
			}
			//
			currentGene=currentGene.nextGene;
		}
	}
	
	//test this
	void activeGenesFirstOrder(){//calculate active length in the method as well
		activeLength=0;
		//in the following (rather cheekily) I do not attempt to maintain the tail, which is because the maximum length of solutions is set such that the tail is never used
		//however it is still possible that the tail is used and the maximum solution length is insufficient, this occurs in the case that
		//the solution lists only moves that are not possible but another move is possible, come to think of it, this is not possible (a row and a column is the same if it only contains one rectangle, which if not possible, no moves are possible, inwhich case the last gene will not be used, so it should be OK
		
		//find the first unused gene
		//used genes are cut and inserted before it
		//it is convenient that the first move is always possible (unless the problem consists of only rectangles that do not fit in the container)
		Gene currentGene=head;
		Gene firstUnusedGene=null;
		//identify the first unused gene
		while(currentGene.used){
			activeLength++;
			currentGene=currentGene.nextGene;
		}
		firstUnusedGene=currentGene;
		//cut used gene that appear after this and insert before the first unused gene, (the following is not done because the maximum length of solutions is an overestimate based on the worst case of one row/column for each rectangle, (if 0 is possible so are 1 and 2, if 1 is possible so is 0 and 2, and if 2 is possible so are 0 and 1). whilst ensuring that tail is set correctly and that it has no nextGene
		currentGene=currentGene.nextGene;//gene after the first unused gene
		Gene nextUsedGene=null;
		while(currentGene!=null){
			if(currentGene.used){
				activeLength++;
				nextUsedGene=currentGene;
				currentGene=currentGene.nextGene;
				//cut
				nextUsedGene.nextGene.prevGene=nextUsedGene.prevGene;
				nextUsedGene.prevGene.nextGene=nextUsedGene.nextGene;
				//insert
				firstUnusedGene.prevGene.nextGene=nextUsedGene;
				nextUsedGene.prevGene=firstUnusedGene.prevGene;
				firstUnusedGene.prevGene=nextUsedGene;
				nextUsedGene.nextGene=firstUnusedGene;
				//
			}else{
				currentGene=currentGene.nextGene;
			}
		}
	}
	
	void copyGeneSequence(Solution sol){
		activeLength=sol.activeLength;
		Gene currentGeneThis=head;
		Gene currentGeneThat=sol.head;
		while(currentGeneThat!=null){
			currentGeneThis.value=currentGeneThat.value;
			currentGeneThis.value2=currentGeneThat.value2;
			currentGeneThis=currentGeneThis.nextGene;
			currentGeneThat=currentGeneThat.nextGene;
		}
	}
	 
	void quantileBasedNeighbour(Random randNumGen){
		int randInd=(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		int stripType=(int)Math.floor((double)geneToMutate.value/numberOfQuantiles);
		geneToMutate.value=(numberOfQuantiles*stripType)+Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(numberOfQuantiles-1)), numberOfQuantiles);//check that floorMod behaves the same as mod
	}
	
	
	void stripTypeBasedNeighbour(Random randNumGen){
		int randInd=(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		int c=numberOfQuantiles*(1+(int)Math.round(randNumGen.nextDouble()*(numberOfStripTypes-2)));
		geneToMutate.value=Math.floorMod(geneToMutate.value+c, alphabetLength);//check that floorMod behaves the same as mod
	}
	
	void quantileBasedNeighbourTriangularDistributionGeneSelection(Random randNumGen, double TT, double A, double B){
		int randInd=geneToMutateTriangularDistributionFunctionOfTime(randNumGen.nextDouble(), TT, A, B);//(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		int stripType=(int)Math.floor((double)geneToMutate.value/numberOfQuantiles);
		geneToMutate.value=(numberOfQuantiles*stripType)+Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(numberOfQuantiles-1)), numberOfQuantiles);//check that floorMod behaves the same as mod
	}
	
	void stripTypeBasedNeighbourTriangularDistributionGeneSelection(Random randNumGen, double TT, double A, double B){
		int randInd=geneToMutateTriangularDistributionFunctionOfTime(randNumGen.nextDouble(), TT, A, B);//(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		int c=numberOfQuantiles*(1+(int)Math.round(randNumGen.nextDouble()*(numberOfStripTypes-2)));
		geneToMutate.value=Math.floorMod(geneToMutate.value+c, alphabetLength);//check that floorMod behaves the same as mod
	}
	
	//all of the mutation operators above change values, this reorders them in a way that could be beneficial when you consider the queue constraints
	void swapConsecutiveRowsOrColumns(Random randNumGen){
		//count the number of consective rows or column within the active length of the chromosome
		//if non exist perform a random swap
		//make sure "activeLength" is set correctly
		int consectiveSwapOppurtunities=0;
		int geneCount=0;
		Gene gene=head;
		while(geneCount<activeLength && gene.nextGene!=null){
			int orientation1=(int)Math.floor((double)gene.value/numberOfQuantiles);
			int orientation2=(int)Math.floor((double)gene.nextGene.value/numberOfQuantiles);
			if(orientation1==orientation2){
				consectiveRowsCols[consectiveSwapOppurtunities]=geneCount;
				consectiveSwapOppurtunities++;
			}else if(orientation1>0 && orientation2>0){
				consectiveRowsCols[consectiveSwapOppurtunities]=geneCount;
				consectiveSwapOppurtunities++;
			}
			geneCount++;
			gene=gene.nextGene;
		}
		//
		if(consectiveSwapOppurtunities>0){
			int randConsectiveCutSwapIndex=(int)(randNumGen.nextDouble()*consectiveSwapOppurtunities);
			Gene gene1=getGene(consectiveRowsCols[randConsectiveCutSwapIndex]);
			Gene gene2=getGene(consectiveRowsCols[randConsectiveCutSwapIndex]+1);
			int value1=gene1.value;
			gene1.value=gene2.value;
			gene2.value=value1;
			
		}else{
			//random swap
			int randSwapIndex1=(int)(randNumGen.nextDouble()*activeLength);
			int randSwapIndex2=(int)(randNumGen.nextDouble()*activeLength);
			Gene gene1=getGene(randSwapIndex1);
			Gene gene2=getGene(randSwapIndex2);
			int value1=gene1.value;
			gene1.value=gene2.value;
			gene2.value=value1;
		}
	}
	
	
	
	//this method is called with an externally defined probability
	//gene[rand*activeLength]=mod(current value+rand*(alphabetLength-1), alphabetLength)
	void mutationTriangularDistributionGeneSelection(Random randNumGen, double TT, double A, double B){
		int randInd=geneToMutateTriangularDistributionFunctionOfTime(randNumGen.nextDouble(), TT, A, B);//(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		geneToMutate.value=Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
	}
	
	//when using mutation to generate part of a population
	//in an iterative genetic algorithm mutation is considered for each gene at
	//the specified rate
	void mutation(double rate, Random randNumGen){
		Gene currentGene=head;
		while(currentGene!=null){
			if(randNumGen.nextDouble()<rate){
				currentGene.value=Math.floorMod(currentGene.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
			}
			//
			currentGene=currentGene.nextGene;
		}
	}
	
	int geneToMutateTriangularDistributionFunctionOfTime(double rand, double TT, double A, double B){//TT=(iteration/TotalIterations), int activeLength
		int geneToMutate=-1;
		double tt=-1;
		if(TT<=0.5){
           double gT=0.5*Math.pow((1-TT), 2)+(TT*(1-2*TT))+0.5*Math.pow(TT, 2);
           double cT=A/gT;
           double jT=(0.5*Math.pow(TT, 2)+(1-2*TT)*TT)/gT; 
           if(rand<=jT){
               //case 1
               tt=(-(cT-2*TT*cT+B)+Math.sqrt(Math.max(0, Math.pow((cT-2*TT*cT+B), 2)+2*rand*cT)))/(cT);
           }else{
               //case 2
        	   double H2=-cT*Math.pow(TT, 2)-rand;
               tt=(-(cT+B)+Math.sqrt(Math.max(0, Math.pow((cT+B), 2)+2*cT*H2)))/(-cT);
           }
		}else{
			double gT=0.5*(Math.pow(TT, 2))+((1-TT)*(2*TT-1))+0.5*Math.pow(1-TT, 2);
			double cT=A/gT;
			double jT=(0.5*Math.pow(TT, 2))/gT;
		   if(rand<=jT){
		       //case 3
		       tt=(-B+Math.sqrt(Math.max(0, Math.pow(B, 2)+2*cT*rand)))/(cT);
		   }else{
		       //case 4
		       tt=(-(2*cT*TT+B)+Math.sqrt(Math.max(0, Math.pow((2*cT*TT+B), 2)-(2*cT*(cT*Math.pow(TT, 2)+rand)))))/(-cT);
		   }
		}
		geneToMutate=(int)Math.round(activeLength*tt);//
		return geneToMutate;
	}
	
	String getSolutionString(){
		String solutionString="";
		Gene currentGene=head;
		while(currentGene!=null){
			solutionString=solutionString.concat(String.valueOf(currentGene.value)+",");
			currentGene=currentGene.nextGene;
		}
		return solutionString;
	}
	
	String getSolutionString2(){
		String solutionString="";
		Gene currentGene=head;
		while(currentGene!=null){
			solutionString=solutionString.concat(String.valueOf(currentGene.value2)+",");
			currentGene=currentGene.nextGene;
		}
		return solutionString;
	}
	
	
	void printSolutionString(){
		System.out.println(getSolutionString());
	}
}
