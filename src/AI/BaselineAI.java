package AI;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import Cardgame.Cards;
import Cardgame.Game;
import Cardgame.PlayerHandler;

public class BaselineAI implements Runnable  {
	private int playerNumber;
	private Game myGame;
	private Vector<Cards> myHand;
	private ArrayList<PlayerHandler> allHandler; 
	private Map<Integer,Vector<Cards>> playerMap;
	public BaselineAI(int Number, Game curGame, ArrayList<PlayerHandler> all) {
		this.allHandler = all;
		this.playerNumber = Number;
		this.myGame = curGame;
		this.myHand = curGame.getHandCard(playerNumber);
	}

	public void gamePlay() throws InterruptedException {
		while(true) {
			TimeUnit.SECONDS.sleep(10);
			Vector<Cards> myHand = myGame.getHandCard(playerNumber);
			if (myGame.isGameEnd()) {
				break;
			}
			Cards cardToPlay = null;
			if (playerNumber == myGame.getCurrentPlayer()) {
				boolean noValid = true;
				
				for (int i = 0; i <  myGame.getHandCard(playerNumber).size(); i++) {
					if (myGame.isValid(playerNumber, i)) {
						cardToPlay= myGame.getHandCard(playerNumber).get(i);
						myGame.playerPlayed(playerNumber,i,false);
						noValid = false;
						break;
					}
					
				}
				if (noValid) {
					myGame.draw(playerNumber);
					for (int i = 0; i <  myGame.getHandCard(playerNumber).size(); i++) {
						if (myGame.isValid(playerNumber, i)) {
							myGame.playerPlayed(playerNumber,i,false);
							break;
						}	
					}
					myGame.playerSkip();
				} else {
					if ((cardToPlay).isWild() ||  (cardToPlay).isWildDraw()) {
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
					} else if ((cardToPlay).isSkip()) {
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
					sendtoAll("bot "+this.playerNumber+ " played "+ cardToPlay.toString());
					//myGame.playerPlayed(playerNumber,cardToPlay,false);
					TimeUnit.SECONDS.sleep(2);
					sendtoAll("bot "+this.playerNumber+ " just finished");
					
				} 
				
				
			} else if (playerNumber != myGame.getCurrentPlayer()) {
				for (int i = 0; i <  myGame.getHandCard(playerNumber).size(); i++) {
					if (myGame.isIdentical(playerNumber, i)) {
						myGame.playerPlayed(playerNumber,i,true);
						break;
					}
				}
			}
		}
	}
	private void sendtoAll(String infoString) throws InterruptedException {
		for (PlayerHandler player:allHandler) {
			player.getMyframe().setInfoString(infoString);
			player.getMyframe().updateInfo();
		}
		//TimeUnit.SECONDS.sleep(10);
	}
	
	
	
	public void run() {
		try {
			this.gamePlay();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
