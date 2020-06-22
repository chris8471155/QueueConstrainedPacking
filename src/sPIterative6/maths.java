package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
import java.util.ArrayList;

public class maths {
	
	public static void main(String[] args){
		int g=4;
		s=doubleToBinaryString(g);
		System.out.println(s);
		
		for(int i=0;i<100;i++){
			System.out.println(i+":"+(char)i);
		}
	}
	
	static String s="";
	static int intNum;
	
	public static int Mod(int b,int c)
	{
		int a=0;
		a=(int)Math.round((((double)b/(double)c)-(Math.floor((double)b/(double)c)))*((double)c));
		return a;
	}
	
	public static double boundedDeciMod(double b,double lb, double ub)
	{
		double c=(b-lb)/(ub-lb);
		return lb+(c-Math.floor(c))*(ub-lb);
	}
	
	public static String doubleToBinaryString(int n){
		s="";
		while(n>0){
			intNum=Mod(n,2);
			s=s.concat(String.valueOf(intNum));
			n=(n-intNum)/2;
		}
		return s;
	}
	
	public static double roundToSigFig(double a, int sigFig){
		String A=String.valueOf(a);
		return Double.parseDouble(A.substring(0, Math.min(A.length(), sigFig+1)));
	}
	
	
	public static int[] Sort(int[] A,int effectiveSizeOfA,int maxSize)
	{
		int[] AA=new int[effectiveSizeOfA];
		//B if record of original indices is required
		int[] B=new int[maxSize];
		for(int i=0;i<effectiveSizeOfA;i++)
		{
			B[i]=i;
			AA[i]=A[i];
		}
		int numOfIts=0;
		for(int i=0;i<effectiveSizeOfA;i++)
		{
			int smallest=AA[i];
			int position=i;
			for(int j=i+1;j<effectiveSizeOfA;j++)
			{
				numOfIts=numOfIts+1;
				if(AA[j]<smallest)
				{
					position=j;
					smallest=AA[j];
				}
			}
			Interchange(i,position,AA);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(int[] A,int effectiveSizeOfA)
	{
		//int[] AA=new int[effectiveSizeOfA];
		//B if record of original indices is required
		int[] B=new int[effectiveSizeOfA];
		for(int i=0;i<effectiveSizeOfA;i++)
		{
			B[i]=i;
			//AA[i]=A[i];
		}
		int numOfIts=0;
		for(int i=0;i<effectiveSizeOfA;i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSizeOfA;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(int[] A)
	{
		
		//B if record of original indices is required
		int[] B=new int[A.length];
		for(int i=0;i<A.length;i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(int[] A, boolean decreasing){
		
		//B if record of original indices is required
		int[] B=new int[A.length];
		for(int i=0;i<A.length;i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			int largest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]>largest)
				{
					position=j;
					largest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(double[] A, boolean decreasing){
		
		//B if record of original indices is required
		int[] B=new int[A.length];
		for(int i=0;i<A.length;i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double largest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]>largest)
				{
					position=j;
					largest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(double[] A)
	{
		
		//B if record of original indices is required
		int[] B=new int[A.length];
		for(int i=0;i<A.length;i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(double[] A, int effectiveSize)
	{
		
		//B if record of original indices is required
		int[] B=new int[A.length];
		for(int i=0;i<effectiveSize;i++)//A.length
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++)//A.length
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSize;j++)//A.length
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(double[] A, int[] B)
	{
		for(int i=0;i<A.length;i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(ArrayList<Integer> A)
	{
		
		//B if record of original indices is required
		int[] B=new int[A.size()];
		for(int i=0;i<A.size();i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			int smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static ArrayList<Integer> Sort(ArrayList<Double> A, ArrayList<Integer> B)
	{
		
		//B if record of original indices is required
		B.clear();
		for(int i=0;i<A.size();i++)
		{
			B.add(i);
		}
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			double smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] Sort(ArrayList<Double> A, boolean isDouble)
	{
		
		//B if record of original indices is required
		int[] B=new int[A.size()];
		for(int i=0;i<A.size();i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			double smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] SortDecrease(ArrayList<Double> A)
	{
		
		//B if record of original indices is required
		int[] B=new int[A.size()];
		for(int i=0;i<A.size();i++)
		{
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			double largest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)>largest)
				{
					position=j;
					largest=A.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static void justSort(double[] A)
	{
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
		}
   }
	
	public static void justSort(int[] A)
	{
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
		}
   }
	
	public static void justSort(int[] A, boolean decreasing)
	{
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			int largest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]>largest)
				{
					position=j;
					largest=A[j];
				}
			}
			Interchange(i,position,A);
		}
   }
	
	public static void justSort(ArrayList<Integer> A)
	{
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			int smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
		}
   }
	
	public static void justSortDouble(ArrayList<Double> A)
	{
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			double smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
		}
   }
	
	public static void justSort(int[] A,int a)
	{
		int numOfIts=0;
		for(int i=0;i<Math.min(a, A.length);i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<Math.min(a, A.length);j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
		}
   }
	
	/*public static ArrayList<Integer> justSort(ArrayList<Integer> A)
	{
		ArrayList<Integer>
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
		}
		return A;
   }*/
	
	public static void Interchange(int i, int j,int[] a)
	{
		int ai=a[i];
		int aj=a[j];
		a[i]=aj;
		a[j]=ai;
	}
	
	public static void Interchange(int i, int j,double[] a)
	{
		double ai=a[i];
		double aj=a[j];
		a[i]=aj;
		a[j]=ai;
	}
	
	public static <T> void justSortGeneric(int[] A,T[] B,int effectiveSize)
	{
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSize;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGeneric(double[] A,T[] B,int effectiveSize)
	{
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSize;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGenericDecreasing(double[] A,T[] B,int effectiveSize){
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++){
			double largest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSize;j++){
				numOfIts=numOfIts+1;
				if(A[j]>largest)
				{
					position=j;
					largest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGeneric(int[] A,ArrayList<T> B,int effectiveSize)
	{
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++)
		{
			int smallest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSize;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGeneric(ArrayList<Integer> A,ArrayList<T> B,int effectiveSize)
	{
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++)
		{
			int smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<effectiveSize;j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGeneric(ArrayList<Double> A,ArrayList<T> B)
	{
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			double smallest=A.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(A.get(j)<smallest)
				{
					position=j;
					smallest=A.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGeneric(double[] A,ArrayList<T> B)
	{
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void justSortGeneric(double[] A,ArrayList<T> B, int effectiveSize)
	{
		int numOfIts=0;
		for(int i=0;i<effectiveSize;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<effectiveSize;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	public static <T> void Interchange(int i, int j,T[] a)
	{
		T ai=a[i];
		T aj=a[j];
		a[i]=aj;
		a[j]=ai;
	}
	
	public static <T> void Interchange(int i, int j,ArrayList<T> a)
	{
		T ai=a.get(i);
		T aj=a.get(j);
		a.set(i, aj);
		a.set(j, ai);
	}
	
	//sort A in terms of B (whilst also sorting B) (A and B have to be the same size)
	public static void sortAInTermsOfB(ArrayList<Integer> A, ArrayList<Integer> B)
	{
		int numOfIts=0;
		for(int i=0;i<A.size();i++)
		{
			int smallest=B.get(i);
			int position=i;
			for(int j=i+1;j<A.size();j++)
			{
				numOfIts=numOfIts+1;
				if(B.get(j)<smallest)
				{
					position=j;
					smallest=B.get(j);
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
   }
	
	//sort A in terms of B (whilst also sorting B) (A and B have to be the same size)
		public static void sortAInTermsOfB(double[] A, double[] B)
		{
			int numOfIts=0;
			for(int i=0;i<A.length;i++)
			{
				double smallest=B[i];
				int position=i;
				for(int j=i+1;j<A.length;j++)
				{
					numOfIts=numOfIts+1;
					if(B[j]<smallest)
					{
						position=j;
						smallest=B[j];
					}
				}
				Interchange(i,position,A);
				Interchange(i,position,B);
			}
	   }
		
		//sort A in terms of B (whilst also sorting B) (A and B have to be the same size)
		public static void sortAInTermsOfB(int[] A, double[] B)
		{
			int numOfIts=0;
			for(int i=0;i<A.length;i++)
			{
				double smallest=B[i];
				int position=i;
				for(int j=i+1;j<A.length;j++)
				{
					numOfIts=numOfIts+1;
					if(B[j]<smallest)
					{
						position=j;
						smallest=B[j];
					}
				}
				Interchange(i,position,A);
				Interchange(i,position,B);
			}
	   }
		
		//sort A in terms of B (whilst also sorting B) (A and B have to be the same size)
		public static void sortAInTermsOfB(int[] A, int[] B)
		{
			int numOfIts=0;
			for(int i=0;i<A.length;i++)
			{
				double smallest=B[i];
				int position=i;
				for(int j=i+1;j<A.length;j++)
				{
					numOfIts=numOfIts+1;
					if(B[j]<smallest)
					{
						position=j;
						smallest=B[j];
					}
				}
				Interchange(i,position,A);
				Interchange(i,position,B);
			}
	   }
	
	/*public static void Interchange(int i, int j, ArrayList<Integer> a)
	{
		int ai=a.get(i);
		int aj=a.get(j);
		a.set(i, aj);
		a.set(j, ai);
	}*/
		
	public static int[] indexOrder(double[] AA)
	{
		
		//B if record of original indices is required
		int[] B=new int[AA.length];
		double[] A=new double[AA.length];
		for(int i=0;i<A.length;i++)
		{
			A[i]=AA[i];
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] indexOrder(int[] AA)
	{
		
		//B if record of original indices is required
		int[] B=new int[AA.length];
		int[] A=new int[AA.length];
		for(int i=0;i<A.length;i++)
		{
			A[i]=AA[i];
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] indexOrder(int[] AA, boolean decreasing)
	{
		
		//B if record of original indices is required
		int[] B=new int[AA.length];
		int[] A=new int[AA.length];
		for(int i=0;i<A.length;i++)
		{
			A[i]=AA[i];
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double largest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]>largest)
				{
					position=j;
					largest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] indexOrder(double[] AA, boolean decreasing)
	{
		
		//B if record of original indices is required
		int[] B=new int[AA.length];
		double[] A=new double[AA.length];
		for(int i=0;i<A.length;i++)
		{
			A[i]=AA[i];
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double largest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]>largest)
				{
					position=j;
					largest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static int[] indexOrder(ArrayList<Double> AA)
	{
		
		//B if record of original indices is required
		int[] B=new int[AA.size()];
		double[] A=new double[AA.size()];
		for(int i=0;i<A.length;i++)
		{
			A[i]=AA.get(i);
			B[i]=i;
		}
		int numOfIts=0;
		for(int i=0;i<A.length;i++)
		{
			double smallest=A[i];
			int position=i;
			for(int j=i+1;j<A.length;j++)
			{
				numOfIts=numOfIts+1;
				if(A[j]<smallest)
				{
					position=j;
					smallest=A[j];
				}
			}
			Interchange(i,position,A);
			Interchange(i,position,B);
		}
		//System.out.println(numOfIts);
		//for(int i=0;i<A.length;i++)
		//{
			//System.out.print(A[i]+" ");
		//}
		return B;
   }
	
	public static <T> ArrayList<T> inThisOrder(int[] A,ArrayList<T> B){
		ArrayList<T> newArrayList=new ArrayList<T>(A.length);
		for(int i=0;i<A.length;i++){
			newArrayList.add(B.get(A[i]));
		}
		return newArrayList;
	}
	
	public static <T> T[] inThisOrder(int[] A,T[] B){
		ArrayList<T> newArrayList=new ArrayList<T>(A.length);
		for(int i=0;i<A.length;i++){
			newArrayList.add(B[A[i]]);
		}
		for(int i=0;i<A.length;i++){
			B[i]=newArrayList.get(i);
		}
		return B;
	}
	
	public static double[] inThisOrder(int[] A,double[] B){
		double[] newArrayList=new double[A.length];
		for(int i=0;i<A.length;i++){
			newArrayList[i]=B[A[i]];
		}
		for(int i=0;i<A.length;i++){//
			B[i]=newArrayList[i];
		}
		return B;
	}
	
	public static int[] inThisOrder(int[] A, int[] B){
		int[] newArrayList=new int[A.length];
		for(int i=0;i<A.length;i++){
			newArrayList[i]=B[A[i]];
		}
		for(int i=0;i<A.length;i++){//
			B[i]=newArrayList[i];
		}
		return B;
	}
	
	public static <T> T[] inThisOrder(int[] A,T[] B, int effectiveSize){
		ArrayList<T> newArrayList=new ArrayList<T>(effectiveSize);
		for(int i=0;i<effectiveSize;i++){
			newArrayList.add(B[A[i]]);
		}
		for(int i=0;i<effectiveSize;i++){//A.length
			B[i]=newArrayList.get(i);
		}
		return B;
	}
	
	public static double[] inThisOrder(int[] A,double[] B, int effectiveSize){
		double[] newArrayList=new double[effectiveSize];//A.length
		for(int i=0;i<effectiveSize;i++){//A.length
			newArrayList[i]=B[A[i]];
		}
		for(int i=0;i<effectiveSize;i++){//A.length
			B[i]=newArrayList[i];
		}
		return B;
	}
		
	public static int max(int[] b)
	{
		int max=0;
		for(int i=0;i<b.length;i++)
		{
			if(b[i]>max)
			{
				max=b[i];
			}
		}
		return max;
	}
	
	public static double max(double[] b)
	{
		double max=0;
		for(int i=0;i<b.length;i++)
		{
			if(b[i]>max)
			{
				max=b[i];
			}
		}
		return max;
	}
	
	public static int min(int[] b)
	{
		int min=0;
		for(int i=0;i<b.length;i++)
		{
			if(b[i]<min)
			{
				min=b[i];
			}
		}
		return min;
	}
	
	public static int maxIndex(int[] b,int c)
	{
		int maxInd=0;
		int max=0;
		for(int i=0;i<c;i++)
		{
			if(b[i]>max)
			{
				maxInd=i;
				max=b[i];
			}
		}
		return maxInd;
	}
	
	public static int sum(int[] b)
	{
		int sum=0;
		for(int i=0;i<b.length;i++)
		{
			sum=sum+b[i];
		}
		return sum;
	}
	
	public static double sum(double[] b)
	{
		double sum=0;
		for(int i=0;i<b.length;i++)
		{
			sum=sum+b[i];
		}
		return sum;
	}
	
	public static double sumMatrix(double[][] a)
	{
		double sum=0;
		for(int i=0;i<a.length;i++)
		{
			for(int j=0;j<a[i].length;j++)
			{
				sum=sum+a[i][j];
			}
		}
		return sum;
	}
	
	public static double sumMatrixWeighted(double[][] a,double[][] b)
	{
		double sum=0;
		for(int i=0;i<a.length;i++)
		{
			for(int j=0;j<a[i].length;j++)
			{
				sum=sum+(a[i][j]*b[i][j]);
			}
		}
		return sum;
	}
	
	public static double dotProduct(double[] a,double[] b)
	{
		double sum=0;
		for(int i=0;i<b.length;i++)
		{
			sum=sum+b[i]*a[i];
		}
		return sum;
	}
	
	
	
	public static int booleanToInt(boolean a)
	{
		int b=0;
		if(a)
		{
			b=1;
		}
		return b;
	}
	
	public static double mean(int[] a)
	{
		double mean=0;
		int sum=0;
		for(int i=0;i<a.length;i++)
		{
			sum=sum+a[i];
		}
		mean=(double)sum/(double)a.length;
		return mean;
	}
	
	public static double mean(int[] a,int b)
	{
		double mean=0;
		int sum=0;
		for(int i=0;i<b;i++)
		{
			sum=sum+a[i];
		}
		mean=(double)sum/b;
		return mean;
	}
	
	public static double mean(double[] a)
	{
		double mean=0;
		double sum=0;
		for(int i=0;i<a.length;i++)
		{
			sum=sum+a[i];
		}
		mean=sum/(double)a.length;
		return mean;
	}
	
	public static double[] mean(double[][] a)
	{
		double[] mean=new double[a[0].length];
		for(int j=0;j<a[0].length;j++){
			double sum=0;
			for(int i=0;i<a.length;i++)
			{
				sum=sum+a[i][j];
			}
			mean[j]=sum/(double)a.length;
		}
		return mean;
	}
	
	public static double[] mean(ArrayList<Double[]> a, boolean g)
	{
		double[] mean=new double[a.get(0).length];
		for(int j=0;j<a.get(0).length;j++){
			double sum=0;
			for(int i=0;i<a.size();i++)
			{
				sum=sum+a.get(i)[j];
			}
			mean[j]=sum/(double)a.size();
		}
		return mean;
	}
	
	public static double mean(ArrayList<Double> a)
	{
		double mean=0;
		double sum=0;
		for(int i=0;i<a.size();i++)
		{
			sum=sum+a.get(i);
		}
		mean=sum/(double)a.size();
		return mean;
	}
	
	public static double mean(double[] a,int b)
	{
		double mean=0;
		double sum=0;
		for(int i=0;i<b;i++)
		{
			sum=sum+a[i];
		}
		mean=sum/b;
		return mean;
	}
	
	public static double standardDeviation(int[] a,double mean)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		
		double SS=0;
		for(int i=0;i<a.length;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/a.length;
		c=Math.sqrt(SS);
		return c;
	}
	
	public static double standardDeviation(int[] a,double mean, int size)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		
		double SS=0;
		for(int i=0;i<size;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/size;
		c=Math.sqrt(SS);
		return c;
	}
	
	public static double standardDeviation(double[] a,double mean)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		
		double SS=0;
		for(int i=0;i<a.length;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/a.length;
		c=Math.sqrt(SS);
		return c;
	}
	
	public static double[] standardDeviation(double[][] a,double[] mean)//perhaps an argument to indicate whether its a population or sample of data
	{
		double[] c=new double[a[0].length];
		for(int j=0;j<a[0].length;j++){
			double SS=0;
			for(int i=0;i<a.length;i++)
			{
				SS=SS+Math.pow((a[i][j]-mean[j]), 2);
			}
			SS=SS/(a.length);
			c[j]=Math.sqrt(SS);
		}
		
		return c;
	}
	
	public static double[] sampleStandardDeviation(double[][] a,double[] mean)//perhaps an argument to indicate whether its a population or sample of data
	{
		double[] c=new double[a[0].length];
		for(int j=0;j<a[0].length;j++){
			double SS=0;
			for(int i=0;i<a.length;i++)
			{
				SS=SS+Math.pow((a[i][j]-mean[j]), 2);
			}
			SS=SS/(a.length-1);
			c[j]=Math.sqrt(SS);
		}
		
		return c;
	}
	
	public static double[] sampleStandardDeviation(ArrayList<Double[]> a,double[] mean)//perhaps an argument to indicate whether its a population or sample of data
	{
		double[] c=new double[a.get(0).length];
		for(int j=0;j<a.get(0).length;j++){
			double SS=0;
			for(int i=0;i<a.size();i++)
			{
				SS=SS+Math.pow((a.get(i)[j]-mean[j]), 2);
			}
			SS=SS/(a.size()-1);
			c[j]=Math.sqrt(SS);
		}
		
		return c;
	}
	
	public static double sampleStandardDeviation(int[] a)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		double mean=mean(a);
		double SS=0;
		for(int i=0;i<a.length;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/(a.length-1);
		c=Math.sqrt(SS);
		return c;
	}
	
	public static double sampleStandardDeviation(int[] a,int b)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		double mean=mean(a,b);
		double SS=0;
		for(int i=0;i<b;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/(b-1);
		if(b>1)
		{
			c=Math.sqrt(SS);
		}
		else
		{
			c=0;
		}
		
		return c;
	}
	
	
	public static double sampleStandardDeviation(double[] a)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		double mean=mean(a);
		double SS=0;
		for(int i=0;i<a.length;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/(a.length-1);
		c=Math.sqrt(SS);
		return c;
	}
	
	public static double sampleStandardDeviation(ArrayList<Double> a)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		double mean=mean(a);
		double SS=0;
		for(int i=0;i<a.size();i++)
		{
			SS=SS+Math.pow((a.get(i)-mean), 2);
		}
		if(a.size()>1){
			SS=SS/(a.size()-1);
			c=Math.sqrt(SS);
		}
		return c;
	}
	
	public static double sampleStandardDeviation(double[] a,int b)//perhaps an argument to indicate whether its a population or sample of data
	{
		double c=0;
		double mean=mean(a,b);
		double SS=0;
		for(int i=0;i<b;i++)
		{
			SS=SS+Math.pow((a[i]-mean), 2);
		}
		SS=SS/(b-1);
		if(b>1)
		{
			c=Math.sqrt(SS);
		}
		else
		{
			c=0;
		}
		return c;
	}
	
	public static double correlation(double[]x, double[]y){
		double r=0;
		if(x.length==y.length){
			double xBar=mean(x);
			double yBar=mean(y);
			double top=0;
			double bottomLeft=0;
			double bottomRight=0;
			for(int i=0;i<x.length;i++){
				top=top+((x[i]-xBar)*(y[i]-yBar));
				bottomLeft=bottomLeft+((x[i]-xBar)*(x[i]-xBar));
				bottomRight=bottomRight+((y[i]-yBar)*(y[i]-yBar));
			}
			r=top/Math.sqrt(bottomLeft*bottomRight);
		}
		return r;
	}
	
	public static double[] trendLine(double[]x, double[]y){
		double[] BA=new double[2];
		if(x.length==y.length){
			double xBar=mean(x);
			double yBar=mean(y);
			double top=0;
			double bottomLeft=0;
			for(int i=0;i<x.length;i++){
				top=top+((x[i]-xBar)*(y[i]-yBar));
				bottomLeft=bottomLeft+((x[i]-xBar)*(x[i]-xBar));
			}
			BA[0]=top/bottomLeft;//b(x) gradient
			BA[1]=yBar-BA[0]*xBar;//a constant
		}
		return BA;
	} 
	
	public static int power(int x,int n)
	{
		if(n>1)
		{
			x=power(x,n-1)*x;
		}
		return x;
	}
	
	public static double power(double x,double n)
	{
		if(n>1)
		{
			x=power(x,n-1)*x;
		}
		return x;
	}
	
	public static long power(long x,long n)
	{
		if(n>1)
		{
			x=power(x,n-1)*x;
		}
		return x;
	}
	
	public static int[][] getCombinationsArray(int[] s, int r){
		if(r<=s.length){
			int sl=s.length;
			int TotCombos=(int) combinations(s.length,r);
			int[][] combos=new int[TotCombos][r];
			int[] markers=new int[r];
			for(int i=0;i<r;i++){markers[i]=i;}
			int numCombos=0;
			
			while(numCombos<TotCombos){
				//yield current combination
				for(int i=0;i<r;i++){
					combos[numCombos][i]=s[markers[i]];
				}
				numCombos++;
				//find first moveable marker
				int moveableMarker=-1;
				if(markers[r-1]<sl-1){
					moveableMarker=r-1;
				}
				for(int i=1;i<r && moveableMarker==-1;i++){
					if(markers[r-1-i]+1<markers[r-i]){
						moveableMarker=r-1-i;
					}
				}
				if(moveableMarker>-1){
					markers[moveableMarker]++;
					for(int i=moveableMarker+1;i<r;i++){
						markers[i]=markers[moveableMarker]+i-moveableMarker;
					}
				}
			}
			return combos;
		}else{
			return null;
		}
		
	}
	
	public static double combinations(int n,int r)
	{
		double a=1;
		
		for(long i=n-r+1;i<=n;i++){
			a=a*i;
		}
		if(r==0){
			a=1;
		}else{
			a=(a/factorial(r));
		}
		
		
		//a=(int)((double)factorial(n)/(factorial(r)*factorial(n-r)));
		
		return a;
	}
	
	/*public static long factorial(int a)
	{
		long b=1;
		
		for(long i=2;i<=a;i++)
		{
			b=b*i;
		}
		
		return b;
	}*/
	
	public static double factorial(int a)
	{
		double b=1;
		
		for(int i=2;i<=a;i++)
		{
			b=b*i;
		}
		
		return b;
	}
	
	public static double delayCancellationMeasure(double delay, double delayExponent, double delayConstant){
		return Math.min(1, (delayConstant*(Math.pow(delay, delayExponent))));
	}
	
	public static double standardNormalProbability(double z){
		double prob=0.5;
		double[][] snp={{0.0, 0.5},{0.01, 0.504},{0.02, 0.508},{0.03, 0.512},{0.04, 0.516},{0.05, 0.5199},{0.06, 0.5239},{0.07, 0.5279},{0.08, 0.5319},{0.09, 0.5359},{0.1, 0.5398},{0.11, 0.5438},{0.12, 0.5478},{0.13, 0.5517},{0.14, 0.5557},{0.15, 0.5596},{0.16, 0.5636},{0.17, 0.5675},{0.18, 0.5714},{0.19, 0.5753},{0.2, 0.5793},{0.21, 0.5832},{0.22, 0.5871},{0.23, 0.591},{0.24, 0.5948},{0.25, 0.5987},{0.26, 0.6026},{0.27, 0.6064},{0.28, 0.6103},{0.29, 0.6141},{0.3, 0.6179},{0.31, 0.6217},{0.32, 0.6255},{0.33, 0.6293},{0.34, 0.6331},{0.35, 0.6368},{0.36, 0.6406},{0.37, 0.6443},{0.38, 0.648},{0.39, 0.6517},{0.4, 0.6554},{0.41, 0.6591},{0.42, 0.6628},{0.43, 0.6664},{0.44, 0.67},{0.45, 0.6736},{0.46, 0.6772},{0.47, 0.6808},{0.48, 0.6844},{0.49, 0.6879},{0.5, 0.6915},{0.51, 0.695},{0.52, 0.6985},{0.53, 0.7019},{0.54, 0.7054},{0.55, 0.7088},{0.56, 0.7123},{0.57, 0.7157},{0.58, 0.719},{0.59, 0.7224},{0.6, 0.7257},{0.61, 0.7291},{0.62, 0.7324},{0.63, 0.7357},{0.64, 0.7389},{0.65, 0.7422},{0.66, 0.7454},{0.67, 0.7486},{0.68, 0.7517},{0.69, 0.7549},{0.7, 0.758},{0.71, 0.7611},{0.72, 0.7642},{0.73, 0.7673},{0.74, 0.7704},{0.75, 0.7734},{0.76, 0.7764},{0.77, 0.7794},{0.78, 0.7823},{0.79, 0.7852},{0.8, 0.7881},{0.81, 0.791},{0.82, 0.7939},{0.83, 0.7967},{0.84, 0.7995},{0.85, 0.8023},{0.86, 0.8051},{0.87, 0.8078},{0.88, 0.8106},{0.89, 0.8133},{0.9, 0.8159},{0.91, 0.8186},{0.92, 0.8212},{0.93, 0.8238},{0.94, 0.8264},{0.95, 0.8289},{0.96, 0.8315},{0.97, 0.834},{0.98, 0.8365},{0.99, 0.8389},{1.0, 0.8413},{1.01, 0.8438},{1.02, 0.8461},{1.03, 0.8485},{1.04, 0.8508},{1.05, 0.8531},{1.06, 0.8554},{1.07, 0.8577},{1.08, 0.8599},{1.09, 0.8621},{1.1, 0.8643},{1.11, 0.8665},{1.12, 0.8686},{1.13, 0.8708},{1.14, 0.8729},{1.15, 0.8749},{1.16, 0.877},{1.17, 0.879},{1.18, 0.881},{1.19, 0.883},{1.2, 0.8849},{1.21, 0.8869},{1.22, 0.8888},{1.23, 0.8907},{1.24, 0.8925},{1.25, 0.8944},{1.26, 0.8962},{1.27, 0.898},{1.28, 0.8997},{1.29, 0.9015},{1.3, 0.9032},{1.31, 0.9049},{1.32, 0.9066},{1.33, 0.9082},{1.34, 0.9099},{1.35, 0.9115},{1.36, 0.9131},{1.37, 0.9147},{1.38, 0.9162},{1.39, 0.9177},{1.4, 0.9192},{1.41, 0.9207},{1.42, 0.9222},{1.43, 0.9236},{1.44, 0.9251},{1.45, 0.9265},{1.46, 0.9279},{1.47, 0.9292},{1.48, 0.9306},{1.49, 0.9319},{1.5, 0.9332},{1.51, 0.9345},{1.52, 0.9357},{1.53, 0.937},{1.54, 0.9382},{1.55, 0.9394},{1.56, 0.9406},{1.57, 0.9418},{1.58, 0.9429},{1.59, 0.9441},{1.6, 0.9452},{1.61, 0.9463},{1.62, 0.9474},{1.63, 0.9484},{1.64, 0.9495},{1.65, 0.9505},{1.66, 0.9515},{1.67, 0.9525},{1.68, 0.9535},{1.69, 0.9545},{1.7, 0.9554},{1.71, 0.9564},{1.72, 0.9573},{1.73, 0.9582},{1.74, 0.9591},{1.75, 0.9599},{1.76, 0.9608},{1.77, 0.9616},{1.78, 0.9625},{1.79, 0.9633},{1.8, 0.9641},{1.81, 0.9649},{1.82, 0.9656},{1.83, 0.9664},{1.84, 0.9671},{1.85, 0.9678},{1.86, 0.9686},{1.87, 0.9693},{1.88, 0.9699},{1.89, 0.9706},{1.9, 0.9713},{1.91, 0.9719},{1.92, 0.9726},{1.93, 0.9732},{1.94, 0.9738},{1.95, 0.9744},{1.96, 0.975},{1.97, 0.9756},{1.98, 0.9761},{1.99, 0.9767},{2.0, 0.9772},{2.01, 0.9778},{2.02, 0.9783},{2.03, 0.9788},{2.04, 0.9793},{2.05, 0.9798},{2.06, 0.9803},{2.07, 0.9808},{2.08, 0.9812},{2.09, 0.9817},{2.1, 0.9821},{2.11, 0.9826},{2.12, 0.983},{2.13, 0.9834},{2.14, 0.9838},{2.15, 0.9842},{2.16, 0.9846},{2.17, 0.985},{2.18, 0.9854},{2.19, 0.9857},{2.2, 0.9861},{2.21, 0.9864},{2.22, 0.9868},{2.23, 0.9871},{2.24, 0.9875},{2.25, 0.9878},{2.26, 0.9881},{2.27, 0.9884},{2.28, 0.9887},{2.29, 0.989},{2.3, 0.9893},{2.31, 0.9896},{2.32, 0.9898},{2.33, 0.9901},{2.34, 0.9904},{2.35, 0.9906},{2.36, 0.9909},{2.37, 0.9911},{2.38, 0.9913},{2.39, 0.9916},{2.4, 0.9918},{2.41, 0.992},{2.42, 0.9922},{2.43, 0.9925},{2.44, 0.9927},{2.45, 0.9929},{2.46, 0.9931},{2.47, 0.9932},{2.48, 0.9934},{2.49, 0.9936},{2.5, 0.9938},{2.51, 0.994},{2.52, 0.9941},{2.53, 0.9943},{2.54, 0.9945},{2.55, 0.9946},{2.56, 0.9948},{2.57, 0.9949},{2.58, 0.9951},{2.59, 0.9952},{2.6, 0.9953},{2.61, 0.9955},{2.62, 0.9956},{2.63, 0.9957},{2.64, 0.9959},{2.65, 0.996},{2.66, 0.9961},{2.67, 0.9962},{2.68, 0.9963},{2.69, 0.9964},{2.7, 0.9965},{2.71, 0.9966},{2.72, 0.9967},{2.73, 0.9968},{2.74, 0.9969},{2.75, 0.997},{2.76, 0.9971},{2.77, 0.9972},{2.78, 0.9973},{2.79, 0.9974},{2.8, 0.9974},{2.81, 0.9975},{2.82, 0.9976},{2.83, 0.9977},{2.84, 0.9977},{2.85, 0.9978},{2.86, 0.9979},{2.87, 0.9979},{2.88, 0.998},{2.89, 0.9981},{2.9, 0.9981},{2.91, 0.9982},{2.92, 0.9982},{2.93, 0.9983},{2.94, 0.9984},{2.95, 0.9984},{2.96, 0.9985},{2.97, 0.9985},{2.98, 0.9986},{2.99, 0.9986},{3.0, 0.9987},{3.01, 0.9987},{3.02, 0.9987},{3.03, 0.9988},{3.04, 0.9988},{3.05, 0.9989},{3.06, 0.9989},{3.07, 0.9989},{3.08, 0.999},{3.09, 0.999}};
		int i=0;
		while(z>snp[i][0] && i<snp.length-1){
			prob=snp[i][1];
			i++;
		}
		return prob;
	}
	
	public static double sumOfSquares(double[] v){
		double ss=0;
		for(int i=0;i<v.length;i++){
			ss=ss+(v[i]*v[i]);
		}
		return ss;
	}
	
	public static double[] binomialDist(int n, double p){
		double[] binomDist=new double[n+1];
		for(int i=0;i<=n;i++){
			binomDist[i]=(factorial(n)/(factorial(i)*factorial(n-i)))*Math.pow(p, i)*Math.pow((1-p), n-i);
		}
		return binomDist;
	}
	
	public static double binomProb(int n, double p, int i){
		return (factorial(n)/(factorial(i)*factorial(n-i)))*Math.pow(p, i)*Math.pow((1-p), n-i);
	}
	
	public static double binomialCoef(int n, int i){
		return (factorial(n)/(factorial(i)*factorial(n-i)));
	}
	
	public static double[] poissonDist(double lambda, double cdl){
		//
		ArrayList<Double> pd=new ArrayList<Double>(10);
		double cd=0;
		double p=0;
		int n=0;
		while(cd<cdl){
			p=((Math.exp(-lambda)*Math.pow(lambda, n)))/factorial(n);
			pd.add(p);
			cd=cd+p;
			n++;
		}
		double[] PD=new double[n];
		for(int i=0;i<n;i++){
			PD[i]=pd.get(i);//(1/cdl)*
		}
		//normalise
		
		return PD;
	}
	
}
