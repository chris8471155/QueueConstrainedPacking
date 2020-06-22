package sPIterative6;
//Christopher Bayliss: University of Southampton, 2016
public class aNumber {
	aNumber nextNum;
	aNumber prevNum;
	double value;
	double[] values=new double[2];
	
	aNumber(double theValue){
		value=theValue;
	}
	
	public aNumber(double[] theValues){
		values[0]=theValues[0];
		values[1]=theValues[1];
	}
}
