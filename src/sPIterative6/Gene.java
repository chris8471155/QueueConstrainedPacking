package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class Gene {
	int value;
	int value2;
	//linked lists convenient for cross-over
	Gene nextGene;
	Gene prevGene;
	
	boolean used;//for computing active length and swapping positions of genes so that the sequence of used genes are listed first
	
	Gene(int value){
		this.value=value;
	}
	
	void setGeneValue(int value){
		this.value=value;
	}
}
