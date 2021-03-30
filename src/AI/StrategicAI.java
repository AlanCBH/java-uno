package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import Cardgame.Cards;
import Cardgame.Game;
import Cardgame.PlayerHandler;

public class StrategicAI implements Runnable {
	private int playerNumber;
	private Game myGame;
	private Vector<Cards> myHand;
	private ArrayList<PlayerHandler> allHandler; 
	
	public StrategicAI(int Number, Game curGame, ArrayList<PlayerHandler> all) {
		this.allHandler = all;
		this.playerNumber = Number;
		this.myGame = curGame;
	}
	public void gamePlay() {
		Vector<Cards> myHand = myGame.getHandCard(playerNumber);
		while(!myGame.isGameEnd()) {
			if (playerNumber == myGame.getCurrentPlayer()) {
				int [] myCard = new int[5],validIndex = new int[5];
				Arrays.fill(myCard, 0);
				Arrays.fill(validIndex,-1);
				boolean haveValid=false;
				for (int i = 0; i < myHand.size(); i++) {
					if (myHand.get(i).getColor() == 1) {
						myCard[1]++;
					} else if (myHand.get(i).getColor() == 2) {
						myCard[2]++;
					} else if (myHand.get(i).getColor() == 3) {
						myCard[3]++;
					} else if (myHand.get(i).getColor() == 4) {
						myCard[4]++;
					} else {
						myCard[0]++;
					}
					if (myGame.isValid(playerNumber, i)) {
						validIndex[myHand.get(i).getColor()]=i;
						haveValid=true;
					}
				}
				int nextCard=-1;
				if (haveValid) {
					if (validIndex[0]>=0 && myCard[0]>=myCard[1] && myCard[0]>=myCard[2] && myCard[0]>=myCard[3] && myCard[0]>=myCard[4]) {
						nextCard = validIndex[0];
					} else if (validIndex[1]>=0 && myCard[1]>=myCard[0] && myCard[1]>=myCard[2] && myCard[1]>=myCard[3] && myCard[1]>=myCard[4]) {
						nextCard = validIndex[1];
					} else if (validIndex[2]>=0 && myCard[2]>=myCard[0] && myCard[2]>=myCard[1] && myCard[2]>=myCard[3] && myCard[2]>=myCard[4]) {
						nextCard = validIndex[2];
					} else if (validIndex[3]>=0 && myCard[3]>=myCard[0] && myCard[3]>=myCard[1] && myCard[3]>=myCard[2] && myCard[3]>=myCard[4]) {
						nextCard = validIndex[3];
					} else {
						nextCard = validIndex[4];
					}
					myGame.playerPlayed(playerNumber,nextCard,false);
					////////////strategy above
					////////////card effect below
					if (myGame.isCurrentWild()) {
						Random rand = new Random();
						int nextColor= rand.nextInt(4) + 1;
						int nextReverse = rand.nextInt(2);
						if (nextColor==1) {
							sendtoAll("Color changed to red!");
						} else if (nextColor==2) {
							sendtoAll("Color changed to yellow!");
						} else if (nextColor==3) {
							sendtoAll("Color changed to green!");
						} else {
							sendtoAll("Color changed to blue!");
						}
						myGame.setCurrentColor(nextColor);
						if (nextReverse == 0) {
							sendtoAll("Player(AI) "+ this.playerNumber+ " reversed order!");
							myGame.wildChangeOrder();
						}
					} else if (myGame.isCurrentSkip()) {
						boolean order = myGame.getCurrentOrder();
						int temp;
						if (order) {
							temp=playerNumber%myGame.getTotalPlayer()+1;
						} else {
							temp=playerNumber-1;
							if (temp==0) temp = myGame.getTotalPlayer();
						}
						sendtoAll("Player "+temp+" is skipped");
					}
					
					
					
					
					
					
					
					
					
					
					
					
				} else {
					myGame.draw(playerNumber);
					if (myGame.isValid(playerNumber,myHand.size()-1) ) {
						myGame.playerPlayed(playerNumber,myHand.size()-1,false);
					}	
				}
				
			} else if (playerNumber != myGame.getCurrentPlayer()) {
				for (int i = 0; i < myHand.size(); i++) {
					if (myGame.isIdentical(playerNumber, i)) {
						myGame.playerPlayed(playerNumber,i,true);
						break;
					}
				}
			}
		}
	}
	private void sendtoAll(String infoString) {
		for (PlayerHandler player:allHandler) {
			player.out.println(infoString);
		}
	}
	
	public void run() {
		this.gamePlay();
	}
}
