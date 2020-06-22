package sPIterative6;

public class QueueConstrainedBinPackingAlgorithm {
	
	
	int[][] packBins(Bin[] bins, int numberOfBins, Queue[] Qs, double[][] rectangleDistribution, double externalUB) throws Exception{//FLEXIPAX (, int[] committedVMix)
	
		double Tol=0.000000000000001;
		
		//static fields
		QBinTree.rectangleDimensions=rectangleDistribution;
		QBinTree.vTypes=rectangleDistribution[0].length;
		QBinTree.numberOfBins=numberOfBins;
		QBinTree.numberOfQueues=Qs.length;
		
		QBinTreeNode.vTypes=rectangleDistribution[0].length;
		QBinTreeNode.numberOfBins=numberOfBins;
		QBinTreeNode.numberOfQueues=Qs.length;
		
		boolean packingIsComplete=false;
		int vTypes=Qs.length;
		//int numberOfBins=bins.length;
		
		//just one tree
		QBinTree tree=new QBinTree(bins, Qs, externalUB);
		
		int t1=(int)System.currentTimeMillis();
		//boolean feasibleTreeSequenceFound=false;
		while(tree.leavesRemain){
			if(Math.abs(19162.619765736137-tree.internalUpperBound)<0.0000001){
				System.out.println();
			}
			tree.getNextFeasiblePath();
			if((int)System.currentTimeMillis()-t1>5000){
				tree.leavesRemain=false;
			}
		}
		
		
		int t2=(int)System.currentTimeMillis();
		//System.out.println(t2-t1);
		//return the optimal packing solution
		return tree.queuePopOrder;
	}
}
