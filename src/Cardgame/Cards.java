package Cardgame;

public  class Cards {
	private int color=0; //0 is a placeholder, 1,2,3,4 represents 4 colors 1:red 2:yellow 3:green 4:blue
	private int number=-1; // -1 is a placeholder
	// the following are refer to the actual functions that card could display
	private boolean wild=false; 
	private boolean wildDraw=false;
	private boolean skip=false;
	private boolean reverse=false;
	private boolean drawTwo=false;
	private boolean isNumber=false;
	public Cards (int myColor,int myNumber, int func,int myWild) {
		if (myWild == 1) {
			wild = true;
		} else if (myWild == 2) {
			wildDraw=true;
		} else {
			color = myColor;
			if (myNumber != -1) {
				isNumber=true;
			}
			if (isNumber) number=myNumber;
			if (func == 0) skip=true; 
			if (func == 1) reverse=true;
			if (func == 2) drawTwo=true; 
		}
		
	}
	public int getColor() {
		return this.color;
	}
	public int getNumber() {
		return this.number;
	}
	public boolean isWild() {
		return this.wild;
	}
	public boolean isWildDraw() {
		return this.wildDraw;
	}
	public boolean isNumber() {
		return this.isNumber;
	}
	public boolean isReverse() {
		return this.reverse;
	}
	public boolean isSkip() {
		return this.skip;
	}
	public boolean isDrawTwo() {
		return this.drawTwo;
	}
	public String toString() {
		String resC="",resN="",resF="",resW="";
		if (this.color == 1) resC="Red";
		if (this.color == 2) resC="Yellow";
		if (this.color == 3) resC="Green";
		if (this.color == 4) resC="Blue";
		for (int i = 0; i < 10; i++) {
			if (this.number == i) resN = String.valueOf(i);
		}
		if (skip) resF="skip";
		if (reverse) resF="reverse";
		if (drawTwo) resF="drawTwo";
		if (wild) resW="Wild";
		if (wildDraw) resW="Wild draw";
		return resC+resN+resF+resW;
	}
}
