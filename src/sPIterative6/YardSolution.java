package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.Random;

public class YardSolution {
	int[] sol;//store max length solution
	int activeLength;//use this to select crossover point and mutation positions that are relevant. This is calculated when the solution is implemented
	
	
	Gene head;
	Gene tail;
	int length;
	
	//int activeLength2;//use this to select crossover point and mutation positions that are relevant. This is calculated when the solution is implemented
	
	
	//Gene head2;
	//Gene tail2;
	//int length2;
	
	static int alphabetLength;
	static int maxSolutionLength;//number of yard lanes
	
	//initial random solution
	YardSolution(Random randNumGen){
		activeLength=maxSolutionLength;
		int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
		head=new Gene(randomValue);
		tail=head;
		for(int i=1;i<maxSolutionLength;i++){
			randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			Gene nextGene=new Gene(randomValue);
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
		}
		//
		//generateChromosome2();
		//or set all static fields together in the initialisation part of the main method
		/*Solution.alphabetLength=alphabetLength;
		Solution.maxSolutionLength=maxSolutionLength;*/
	}
	
	
	YardSolution(YardSolution sol){
		activeLength=maxSolutionLength;
		//int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
		Gene currentGeneThat=sol.head;
		head=new Gene(currentGeneThat.value);
		tail=head;
		//
		currentGeneThat=currentGeneThat.nextGene;
		//
		for(int i=1;i<maxSolutionLength;i++){
			//randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			Gene nextGene=new Gene(currentGeneThat.value);
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
			//
			currentGeneThat=currentGeneThat.nextGene;
		}
		//
		//generateChromosome2();
		//or set all static fields together in the initialisation part of the main method
		/*Solution.alphabetLength=alphabetLength;
		Solution.maxSolutionLength=maxSolutionLength;*/
	}
	
	YardSolution(String yardSolutionString){
		String[] lineA=yardSolutionString.split(",");
		
		maxSolutionLength=lineA.length;
		
		activeLength=maxSolutionLength;
		
		//int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
		head=new Gene(Integer.parseInt(lineA[0]));
		tail=head;
		//
		for(int i=1;i<maxSolutionLength;i++){
			//randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			Gene nextGene=new Gene(Integer.parseInt(lineA[i]));
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
		}
		//
		//generateChromosome2();
		//or set all static fields together in the initialisation part of the main method
		/*Solution.alphabetLength=alphabetLength;
		Solution.maxSolutionLength=maxSolutionLength;*/
	}
	
	YardSolution(String storedSolString, boolean g){
		String[] lineA=storedSolString.split(",");
		int geneCounter=0;
		head=new Gene(Integer.parseInt(lineA[geneCounter]));	geneCounter++;
		tail=head;
		for(int i=1;i<maxSolutionLength;i++){
			Gene nextGene=new Gene(Integer.parseInt(lineA[geneCounter]));	geneCounter++;
			tail.nextGene=nextGene;
			nextGene.prevGene=tail;
			tail=nextGene;
		}
		//
		//generateChromosome2();
		//or set all static fields together in the initialisation part of the main method
		/*Solution.alphabetLength=alphabetLength;
		Solution.maxSolutionLength=maxSolutionLength;*/
	}
	
	/*void generateChromosome2(){
		//int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
		activeLength=activeLength2;
		length2=length;
		//
		head2=new Gene(head.value);
		tail2=head2;
		//
		Gene currentGene=head.nextGene;
		while(currentGene!=null){
			//randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			Gene nextGene=new Gene(currentGene.value);
			tail2.nextGene=nextGene;
			nextGene.prevGene=tail2;
			tail2=nextGene;
		}
	}*/
	
	void randomiseSol(Random randNumGen){
		Gene currentGene=head;
		while(currentGene!=null){
			int randomValue=(int)(randNumGen.nextDouble()*alphabetLength);
			currentGene.setGeneValue(randomValue);
			//
			currentGene=currentGene.nextGene;
		}
	}
	
	void consectutiveIntegerSol(){
		int value=0;
		Gene currentGene=head;
		while(currentGene!=null){
			currentGene.setGeneValue(value);
			value++;
			//
			currentGene=currentGene.nextGene;
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
	
	//this method is called with an externally defined probability
	//gene[rand*activeLength]=mod(current value+rand*(alphabetLength-1), alphabetLength)
	void mutation(Random randNumGen){
		int randInd=(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randInd);
		geneToMutate.value=Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
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
	
	boolean mutationTypeNeighbour(int randomPositionIndex, int newGeneValue){
		//int randInd=(int)(randNumGen.nextDouble()*activeLength);
		Gene geneToMutate=getGene(randomPositionIndex);
		if(geneToMutate.value==newGeneValue) {
			return false;
		}else {
			geneToMutate.value=newGeneValue;
			return true;
		}
		//geneToMutate.value=newGeneValue;//Math.floorMod(geneToMutate.value+(int)(randNumGen.nextDouble()*(alphabetLength-1)), alphabetLength);//check that floorMod behaves the same as mod
	}
	
	//test this
	/*void activeGenesFirstOrder(){//calculate active length in the method as well
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
	}*/
	
	void copyGeneSequence(YardSolution sol){//this is sol2 in "Container" object. unless its sol in "Container"
		activeLength=sol.activeLength;
		Gene currentGeneThis=head;
		Gene currentGeneThat=sol.head;
		while(currentGeneThat!=null){
			currentGeneThis.value=currentGeneThat.value;
			currentGeneThis=currentGeneThis.nextGene;
			currentGeneThat=currentGeneThat.nextGene;
		}
	}
	
	/*void sol2EqualsSol(){
		activeLength2=activeLength;
		Gene currentGene1=head;
		Gene currentGene2=head2;
		while(currentGene1!=null){
			currentGene2.value=currentGene1.value;
			//
			currentGene1=currentGene1.nextGene;
			currentGene2=currentGene2.nextGene;
		}
	}*/
	
	String getSolutionString(){
		String solutionString="";
		Gene currentGene=head;
		while(currentGene!=null){
			solutionString=solutionString.concat(String.valueOf(currentGene.value)+",");
			currentGene=currentGene.nextGene;
		}
		return solutionString;
	}
	
	void printSolutionString(){
		System.out.println(getSolutionString());
	}
}
