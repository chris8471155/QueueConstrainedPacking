package sPIterative6;

import java.awt.Color;
//Christopher Bayliss: University of Southampton, 2016
public class BasicPalette {
	public Color[] palette;
	
	int[][] RGBs={{249,80,49},
	{255,162,58},
	{255,190,54},
	{255,212,50},
	{255,232,50},
	{249,246,49},
	{243,255,54},
	{166,255,54},
	{54,255,71},
	{58,255,204},
	{54,255,255},
	{58,239,255},
	{58,220,255},
	{50,154,255},
	{66,69,255},
	{163,58,255},
	{201,54,255},
	{249,49,249},
	{249,49,229},
	{249,49,180}};
	
	
	
	BasicPalette(){
		palette=new Color[20];
		palette[0]=new Color(249,80,49);
		palette[1]=new Color(255,162,58);
		palette[2]=new Color(255,190,54);
		palette[3]=new Color(255,212,50);
		palette[4]=new Color(255,232,50);
		palette[5]=new Color(249,246,49);
		palette[6]=new Color(243,255,54);
		palette[7]=new Color(166,255,54);
		palette[8]=new Color(54,255,71);
		palette[9]=new Color(58,255,204);
		palette[10]=new Color(54,255,255);
		palette[11]=new Color(58,239,255);
		palette[12]=new Color(58,220,255);
		palette[13]=new Color(50,154,255);
		palette[14]=new Color(66,69,255);
		palette[15]=new Color(163,58,255);
		palette[16]=new Color(201,54,255);
		palette[17]=new Color(249,49,249);
		palette[18]=new Color(249,49,229);
		palette[19]=new Color(249,49,180);
		/*palette=new Color[10];
		palette[0]=new Color(249,85,49);
		palette[1]=new Color(255,197,66);
		palette[2]=new Color(255,236,54);
		palette[3]=new Color(237,255,50);
		palette[4]=new Color(50,255,112);
		palette[5]=new Color(54,255,217);
		palette[6]=new Color(54,237,255);
		palette[7]=new Color(192,70,255);
		palette[8]=new Color(229,54,255);
		palette[9]=new Color(255,58,216);*/
		/*palette=new Color[10][2];
		palette[0][0]=new Color(249,88,49);
		palette[0][1]=new Color(255,215,205);
		palette[1][0]=new Color(255,191,50);
		palette[1][1]=new Color(255,240,209);
		palette[2][0]=new Color(255,236,50);
		palette[2][1]=new Color(255,250,209);
		palette[3][0]=new Color(226,255,50);
		palette[3][1]=new Color(245,255,201);
		palette[4][0]=new Color(49,249,49);
		palette[4][1]=new Color(209,255,210);
		palette[5][0]=new Color(50,255,211);
		palette[5][1]=new Color(205,255,245);
		palette[6][0]=new Color(54,243,255);
		palette[6][1]=new Color(201,252,255);
		palette[7][0]=new Color(50,193,255);
		palette[7][1]=new Color(205,239,255);
		palette[8][0]=new Color(180,48,244);
		palette[8][1]=new Color(241,214,255);
		palette[9][0]=new Color(255,58,254);
		palette[9][1]=new Color(254,218,255);*/
	}
	
	public BasicPalette(int colours){
		palette=new Color[colours];
		for(int i=0;i<colours;i++){
			int colourInd1=(int)Math.floor(((double)i/(colours-1))*19);
			int colourInd2=(int)Math.ceil(((double)i/(colours-1))*19);
			double weight=1-(((double)i/(colours-1))*19-colourInd1);
			palette[i]=new Color((int)Math.round((RGBs[colourInd1][0]*weight)+(RGBs[colourInd2][0]*(1-weight))),(int)Math.round((RGBs[colourInd1][1]*weight)+(RGBs[colourInd2][1]*(1-weight))),(int)Math.round((RGBs[colourInd1][2]*weight)+(RGBs[colourInd2][2]*(1-weight))));
		}
	}
	

}
