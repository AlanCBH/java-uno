package Cardgame;
import gameGUI.Mainframe;
import java.util.*;
import java.util.concurrent.Executors;

import AI.BaselineAI;
import gameGUI.Mainframe;

import java.util.concurrent.ExecutorService;
import java.net.*;
import java.io.*;

public class Game implements Runnable {
	private static final int PORT = 9090;
	public int totalPlayer=4;
	public ArrayList<PlayerHandler> players = new ArrayList<>();
	public ExecutorService pool = Executors.newFixedThreadPool(this.getTotalPlayer());
	private int totalBots = -1;
	
	private Queue<Cards> drawPile = new ArrayDeque<Cards>();
	private  Vector<Cards> discardPile = new Vector<Cards>();
	private Map<Integer,Vector<Cards>> playerMap = new HashMap<Integer,Vector<Cards>>();
	public boolean currentOrder=true;
	public Cards currentCard;
	public int currentColor;
	public int currentPlayer=1;
	private boolean gameEnd=false; 
	private int finalWinner=0;
	public  int stackCard = 0;
	public boolean isPrevDrawTwo=false;
	public boolean isPrevDrawFour=false;
	private Vector<Mainframe> allFrames = new Vector<Mainframe>();
	//private boolean firstCard=true;
	
	
	
	
	public Game(int i,int j) throws IOException {
		this.totalPlayer = i;
		this.totalBots = j;
	}
	public Vector<Cards> getHandCard(int playerNumber) {
		return playerMap.get(playerNumber);
	}
	
	public int getFinalWinner() {
		if (gameEnd) {
			return finalWinner;
		} else {
			return -1;
		}
	}
	public int getTotalPlayer() {
		return this.totalPlayer+this.totalBots;
	}
	public boolean isCurrentSkip() {
		if (currentCard.isSkip()) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isGameEnd() {
		return gameEnd;
	}
	public Queue<Cards> getDrawPile() {
		return drawPile;
	}
	public Vector<Cards> getDiscardPile() {
		return discardPile;
	} 
	
	public boolean getCurrentOrder() {
		return currentOrder;
	}
	public void setCurrentColor(int myColor) {
		currentColor = myColor;
	}
	public String getCurrentColor() {
		if (currentCard.getColor()==1  || this.currentColor==1) {
			return "red";
		} else if (currentCard.getColor()==2  || this.currentColor==2) {
			return "yellow";
		} else if (currentCard.getColor()==3  || this.currentColor==3) {
			return "green";
		} else if (currentCard.getColor()==4 || this.currentColor==4) {
			return "blue";
		} else {
			return "no color";
		}
	}
	public int getCurrentStack() {
		return stackCard;
	}
	public Cards getCurrentCard() {
		return this.currentCard;
	}
	public void setCurrentCard(Cards card) {
		currentCard = card;
	}
	public void startPlayer(int playerNum) {
		Vector<Cards> hand = new Vector<Cards>();
		for(int i =0; i < 7; i++) {
			hand.add(drawPile.poll());
		}
		playerMap.put(playerNum, hand);
	}
	public int getCurrentPlayer() {
		return currentPlayer;
	}
	public boolean isCurrentWild() {
		return (currentCard.isWild() || currentCard.isWildDraw());
	}
	//function for special rules
	public boolean isIdentical(Cards card) {
		boolean identical = false;
		Cards myCard = card;
		Cards cur = this.getCurrentCard();
		if (cur.isNumber() && cur.getColor() == myCard.getColor() && cur.getNumber() == myCard.getNumber()) {
			identical = true;
		}
		return identical;
	}
	
	
	public boolean isIdentical(int playerNum, int pos) {
		Vector<Cards> hand = playerMap.get(playerNum);
		Cards myCard;
		boolean identical=false;
		try {
			myCard = hand.get(pos);
		} catch (Exception e) {
			return false;
		}
		Cards cur = this.getCurrentCard();
		if (cur.isNumber() && cur.getColor() == myCard.getColor() && cur.getNumber() == myCard.getNumber()) {
			identical = true;
			//playerPlayed(playerNum,pos,true);
		}
		return identical;
	}
	
	public void wildChangeOrder() {
		this.currentOrder = !this.currentOrder;
		for (int i = 0; i<2;i++) {
			if (currentOrder) {
				currentPlayer=currentPlayer%this.getTotalPlayer()+1;
			} else {
				currentPlayer=currentPlayer-1;
				if (currentPlayer==0) currentPlayer = this.getTotalPlayer();
			}
		} 
	}
	////update mainframe 
	public void updateAllFrame() {
		for (int i =0; i < allFrames.size(); i++) {
			Mainframe temp = allFrames.get(i);
			temp.updateInfo();
		}
	}
	
	
	////
	public void playerPlayed(int playerNum, int pos,boolean identical) {
		Vector<Cards> hand = playerMap.get(playerNum);
		Cards myCard=hand.get(pos);
		hand.remove(pos);
		if (hand.isEmpty()) {
			gameEnd = true;
			finalWinner = playerNum;
		}
		discardPile.add(currentCard);
		cardPlayed(myCard,identical);
		System.out.println("currentplayer: "+ currentPlayer + " played "+ myCard.toString());
	}
	
	public void cardPlayed(Cards myCard,boolean identical) {
		currentCard=myCard;
		//game's order
		if (currentCard.isReverse()) {
			currentOrder=!currentOrder;
		}
		//
		if (!identical) {
			this.nextPlayer();
		}
		////afterMath not coded!
		if (currentCard.isDrawTwo()) {
			isPrevDrawTwo=true;
			stackCard+=2;
		} else if (currentCard.isWildDraw()) {
			isPrevDrawFour=true;
			stackCard+=4;
		} else {
			afterNonStackCardPlayed(currentCard);
		}
	}
	public void nextPlayer() {
		if (this.getCurrentOrder()) {
			this.currentPlayer = this.currentPlayer%this.getTotalPlayer()+1;
		} else {
			this.currentPlayer=this.currentPlayer-1;
			if (this.currentPlayer==0) this.currentPlayer = this.getTotalPlayer();
		}
	}
	
	
	
	
	public  void playerSkip() {
		for (int i = 0; i < stackCard;i++) {
			draw(currentPlayer);
		}
		stackCard=0;
		isPrevDrawFour=false;
		isPrevDrawTwo=false;
		if (currentOrder) {
			currentPlayer=currentPlayer%this.getTotalPlayer()+1;
		} else {
			currentPlayer=currentPlayer-1;
			if (currentPlayer==0) currentPlayer = this.getTotalPlayer();
		}
		System.out.println("currentPlayer "+ this.currentPlayer +" " + this.printHandCard(currentPlayer));//////////////////
		
	}
	public boolean isValid (Cards myCard) {
		boolean valid=false;
		if (currentCard==null) {
			valid=true;
		} else if (myCard.isWild() && !isPrevDrawTwo && !isPrevDrawFour) {
			valid=true;
		} else if (myCard.isWildDraw() && !isPrevDrawTwo) {
			valid=true;
		} else if (myCard.isDrawTwo() && isPrevDrawTwo) {
			valid = true;
		} else if (myCard.isReverse() == true && currentCard.isReverse() == true) {
			valid=true;
		} else if (myCard.isSkip() == true && currentCard.isSkip() == true ) {
			valid=true;
		} else if ((myCard.getColor() == currentCard.getColor() || (myCard.getColor() ==currentColor && (currentCard.isWild() || currentCard.isWildDraw()))) && !isPrevDrawTwo && !isPrevDrawFour ) {
			valid=true;
		} else if (myCard.getNumber() == currentCard.getNumber() && myCard.isNumber() && currentCard.isNumber()) {
			valid=true;
		}
		System.out.println("currentplayer: "+ currentPlayer);
		return valid;		
	}
	
	public boolean isValid(int playerNum,int pos) {
		Vector<Cards> hand = playerMap.get(playerNum);
		Cards myCard;
		boolean valid=false;
		try {
			myCard = hand.get(pos);
		} catch (Exception e) {
			return false;
		}
		if (currentCard==null) {
			valid=true;
		} else if (myCard.isWild() && !isPrevDrawTwo && !isPrevDrawFour) {
			valid=true;
		} else if (myCard.isWildDraw() && !isPrevDrawTwo) {
			valid=true;
		} else if (myCard.isDrawTwo() && isPrevDrawTwo) {
			valid = true;
		} else if (myCard.isReverse() == true && currentCard.isReverse() == true) {
			valid=true;
		} else if (myCard.isSkip() == true && currentCard.isSkip() == true ) {
			valid=true;
		} else if ((myCard.getColor() == currentCard.getColor() || (myCard.getColor()==currentColor && (currentCard.isWild() || currentCard.isWildDraw()))) && !isPrevDrawTwo && !isPrevDrawFour ) {
			valid=true;
		} else if (myCard.getNumber() == currentCard.getNumber() && myCard.isNumber() && currentCard.isNumber()) {
			valid=true;
		}
		if (valid) {
			playerPlayed(playerNum,pos,false);
		}
		
		return valid;		
	}
	
	
	public void afterNonStackCardPlayed(Cards card) {
		if (!card.isWild()) {
			currentColor = card.getColor();
		}
		if (card.isSkip()) {
			if (currentOrder) {
				currentPlayer=currentPlayer%this.getTotalPlayer()+1;
			} else {
				currentPlayer=currentPlayer-1;
				if (currentPlayer==0) currentPlayer = this.getTotalPlayer();
			}
		}
		
	}
	
	public void reshuffle() {
		Collections.shuffle(discardPile);
		for (int i =discardPile.size()-1; i >= 0; i--) {
			drawPile.add(discardPile.get(i));
			discardPile.remove(i);
		}
	}
	
	
	public void draw(int playerNum) {
		Vector<Cards> hand = playerMap.get(playerNum);
		if(drawPile.peek() == null) {
			reshuffle();
		}
		hand.add(drawPile.poll());
	}
	
	public String printHandCard(int playerNum) {
		String res="";
		Vector<Cards> temp = playerMap.get(playerNum);
		for (int i=0; i<temp.size(); i++) {
			res+= temp.get(i).toString()+" ";
		}
		return res;
	}
	
	public String printCurrentCard() {
		if(currentCard==null) {
			return "No card played";
		} else {
			return currentCard.toString();
		}
	}
	public void gameStart() {
		List<Cards> temp = new ArrayList<Cards>();
		for (int i = 1; i <= 4; i++) {
			///number cards
			for (int j = 0; j <= 9; j++) {
				if (j != 0) {
					Cards card = new Cards(i,j,-1,-1);
					temp.add(card);
				}
				Cards secCard = new Cards(i,j,-1,-1);
				temp.add(secCard);
			}
			///function cards
			for (int j = 0; j <= 2; j++) {
				Cards card = new Cards(i,-1,j,-1);
				temp.add(card);
				Cards secCard = new Cards(i,-1,j,-1);
				temp.add(secCard);
			}
			Cards wild = new Cards(-1,-1,-1,1);
			Cards wildDraw = new Cards(-1,-1,-1,2);
			temp.add(wild);
			temp.add(wildDraw);
		}
		Collections.shuffle(temp);
		for (int i = 0; i < 108; i++) {
			if (temp.get(i).isNumber()) {
				this.setCurrentCard(temp.get(i));
				temp.remove(i);
				break;
			}
		}
		drawPile = new LinkedList<>(temp);
		for (PlayerHandler player:players) {
			player.out.println("Game start, let's start with player 1!");
		}
	}
	
	public static void main(String[] args) throws IOException {
		int totalPlayer; 
		try { totalPlayer = Integer.parseInt(args[0]); 
		} catch (Exception e) {
			totalPlayer = 1; //set to default
		}
		Game newGame = new Game(totalPlayer,3);
		ServerSocket listener = new ServerSocket(PORT);
		int playerNum = newGame.totalBots;
		newGame.gameStart();
		while (!newGame.isGameEnd()) {
			System.out.println("Server ready!");
			Socket client = listener.accept();
			playerNum++;
			newGame.startPlayer(playerNum);
			System.out.println("Player connected!");
			PlayerHandler playerThread = new PlayerHandler(newGame,client,playerNum,newGame.players);
			Mainframe frame = new Mainframe(newGame,playerNum,playerThread);
			frame.show();
			newGame.players.add(playerThread);
			newGame.pool.execute(playerThread);
		}
		System.out.println("Player "+newGame.finalWinner+" is the winner");
		listener.close();
		//PrintWriter out = new PrintWriter(client.getOutputStream(),true);
		//BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket listener;
		try {
			listener = new ServerSocket(PORT);
			int playerNum = 0;
			this.gameStart();
			boolean playerAllConnected = false;
			while (!this.isGameEnd()) {
				System.out.println("Server ready!");
				Socket client;
				try {
					client = listener.accept();
					playerNum++;
					this.startPlayer(playerNum);
					System.out.println("Player connected!");
					PlayerHandler playerThread;
					try {
						playerThread = new PlayerHandler(this,client,playerNum,this.players);
						Mainframe frame = new Mainframe(this,playerNum,playerThread);
						frame.show();
						playerThread.setMyframe(frame);
						allFrames.add(frame);
						this.players.add(playerThread);
						this.pool.execute(playerThread);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (playerNum==this.totalPlayer) {
					playerAllConnected=true;
				}
				if (playerAllConnected) {
					for (int i=playerNum+1; i < this.getTotalPlayer()+1; i++) {
						System.out.println(i);
						BaselineAI bot = new BaselineAI(i,this,this.players);
						this.startPlayer(i);
						this.pool.execute(bot);
					}
					playerAllConnected=false;
				}
				
			
			}
			System.out.println("Player "+this.finalWinner+" is the winner");
			listener.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
	}
}
