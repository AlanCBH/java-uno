package Cardgame;

import java.io.* ;
import java.net.* ;
import java.util.ArrayList;
import java.util.Random;

import gameGUI.Mainframe;

public class PlayerHandler implements Runnable {
	private Socket player;
	private BufferedReader in;
	public PrintWriter out;
	private ArrayList<PlayerHandler> allHandler; 
	private int playerNumber=0; 
	private boolean firstDraw=true;
	private Game myGame;
	private String request="";
	private String myAnswer="";
	private Mainframe myFrame;
	public void setAnswer(String answer) {
		myAnswer = answer;
	}
	
	public PlayerHandler(Game newGame,Socket playerSocket,int myNum, ArrayList<PlayerHandler> all) throws IOException {
		this.myGame = newGame;
		this.player = playerSocket;
		this.playerNumber= myNum;
		this.allHandler = all;
		in = new BufferedReader(new InputStreamReader(player.getInputStream()));
		out = new PrintWriter(player.getOutputStream(),true);
		
	}
	public void setMyframe(Mainframe temp) {
		this.myFrame = temp;
	}
	public Mainframe getMyframe() {
		return myFrame;
	}
	private void sendtoAll(String infoString) {
		for (PlayerHandler player:allHandler) {
			player.getMyframe().setInfoString(infoString);
		}
	}
	
	public void run() {
		boolean first = true;
		try {
			while(true) {
				if (first) {
				String hand = myGame.printHandCard(this.playerNumber);
				String currentCard = myGame.printCurrentCard();
				out.println("You are player "+playerNumber+"! Your hand card: "+hand+" Current card:"+currentCard);
				first=false;
				}
				if (!this.request.equals("")) {
					out.println(request);
				int pos;
				try {
					pos = Integer.parseInt(request);
				}
				catch (NumberFormatException e)
				{
					pos = -1;
				}
				out.println("Currentcard: "+myGame.getCurrentCard().toString());
				if (this.playerNumber != myGame.getCurrentPlayer() && myGame.isIdentical(playerNumber, pos) == false) {
					out.println("Not your turn Yet!");			
				} else if (this.playerNumber != myGame.getCurrentPlayer() && myGame.isIdentical(playerNumber, pos) == true) {
					sendtoAll("Player "+playerNumber+" played the identical card: "+myGame.getCurrentCard().toString());
					myGame.playerPlayed(playerNumber, pos, true);
			     } else if (myGame.getCurrentStack()>0 &&  (request.equals("skip")||request.equals("Skip") || request.equals("s"))) { 
					sendtoAll("Player "+playerNumber+" has to draw "+myGame.getCurrentStack()+" cards");
					sendtoAll("Player "+playerNumber+" skipped!");
					myGame.playerSkip();
					sendtoAll("It's player "+myGame.getCurrentPlayer()+"'s turn!");
					first=true;
					firstDraw=true;
				
				} else if ((request.equals("draw") || request.equals("Draw") || request.equals("d")) && firstDraw) {
					if (myGame.getCurrentStack()>0 ) {
						out.println("You cannot draw at this moment!");
					} else {
						myGame.draw(playerNumber);
						String myhand = myGame.printHandCard(this.playerNumber);
						String mycurrentCard = myGame.printCurrentCard();
						out.println("You have drawed and your hand card updated!\n"+
								"Your hand card: "+myhand+" Current card:"+mycurrentCard);
						firstDraw=false;
					}					
				} else if (!firstDraw && (request.equals("skip")||request.equals("Skip") || request.equals("s")) ) {
					sendtoAll("Player "+playerNumber+" finished!");
					myGame.playerSkip();
					sendtoAll("It's player "+myGame.getCurrentPlayer()+"'s turn!");
					first=true;
					firstDraw=true;
					
				} else {
					if (myGame.isValid(playerNumber, pos)) {
						if (myGame.isCurrentWild()) {
							while(true) {
								out.println("What's next color?");
								String newcolor = in.readLine();
								if (newcolor.equals("red") || newcolor.equals("Red") || newcolor.equals("r") ) {
									myGame.setCurrentColor(1);
									sendtoAll("Color changed to red!");
									break;
								} else if (newcolor.equals("yellow") || newcolor.equals("Yellow") || newcolor.equals("y")) {
									myGame.setCurrentColor(2);
									sendtoAll("Color changed to yellow!");
									break;
								} else if (newcolor.equals("green") || newcolor.equals("Green") || newcolor.equals("g")) {
									myGame.setCurrentColor(3);
									sendtoAll("Color changed to green!");
									break;
								} else if (newcolor.equals("blue") || newcolor.equals("Blue") || newcolor.equals("b")) {
									myGame.setCurrentColor(4);
									sendtoAll("Color changed to blue!");
									break;
								}
							}
							//special function Reverse on black
							while(true) {
								out.println("Do you want to reverse?Yes/No");
								String answer = in.readLine();
								if (answer.equals("Yes")||answer.equals("yes")||answer.equals("y")) {
									sendtoAll("Player "+ this.playerNumber+ " reversed order!");
									myGame.wildChangeOrder();
									break;
								} else if (answer.equals("No")||answer.equals("no")||answer.equals("n")) {
									sendtoAll("Player "+ this.playerNumber+ " did not change the order!");
									break;
								}
							}
							//
							
							
						}
						sendtoAll("Player "+playerNumber+" finished!");
						
						sendtoAll("Player "+playerNumber+" played "+myGame.printCurrentCard());
						if (myGame.isCurrentSkip()) {
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
						sendtoAll("It's player "+myGame.getCurrentPlayer()+"'s turn!");
						first=true;
						firstDraw=true;
					} else {
						out.println("Invalid Move, try again or draw!");
					}
				}
				request="";
				myFrame.updateInfo();
				
				
			}
			}
		} catch (IOException e) {
			System.err.println("error!");
		}finally {
			out.close();
			try {
			in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}//listener.close();
		}
	}
	public void getString(String myString) {
		this.request = new String(myString);
		out.println("After set up "+ this.request);
		int pos;
		try {
			pos = Integer.parseInt(request);
		}
		catch (NumberFormatException e)
		{
			pos = -1;
		}
		out.println("Currentcard: "+myGame.getCurrentCard().toString());
		if (this.playerNumber != myGame.getCurrentPlayer() && myGame.isIdentical(playerNumber, pos) == false) {
			myFrame.setInfoString("Not your turn yet!");			
		} else if (this.playerNumber != myGame.getCurrentPlayer() && myGame.isIdentical(playerNumber, pos) == true) {
			sendtoAll("Player "+playerNumber+" played the identical card: "+myGame.getCurrentCard().toString());
			myGame.playerPlayed(playerNumber, pos, true);
	     } else if (myGame.getCurrentStack()>0 &&  (request.equals("skip")||request.equals("Skip") || request.equals("s"))) { 
			sendtoAll("Player "+playerNumber+" has to draw "+myGame.getCurrentStack()+" cards");
			sendtoAll("Player "+playerNumber+" skipped!");
			myGame.playerSkip();
			sendtoAll("It's player "+myGame.getCurrentPlayer()+"'s turn!");
		
			firstDraw=true;
		
		} else if ((request.equals("draw") || request.equals("Draw") || request.equals("d")) && firstDraw) {
			if (myGame.getCurrentStack()>0 ) {
				myFrame.setInfoString("You cannot draw at this moment!");
			} else {
				myGame.draw(playerNumber);
				String myhand = myGame.printHandCard(this.playerNumber);
				String mycurrentCard = myGame.printCurrentCard();
				myFrame.setInfoString("You have drawed and your hand card updated!\n"+
						"Your hand card: "+myhand+" Current card:"+mycurrentCard);
				this.myFrame.updateHand();
				firstDraw=false;
			}					
		} else if (!firstDraw && (request.equals("skip")||request.equals("Skip") || request.equals("s")) ) {
			sendtoAll("Player "+playerNumber+" finished!");
			myGame.playerSkip();
			sendtoAll("It's player "+myGame.getCurrentPlayer()+"'s turn!");
		
			firstDraw=true;
			
		} else {
			if (myGame.isValid(playerNumber, pos)) {
				if (myGame.isCurrentWild()) {
					
						myFrame.setInfoString("What's next color? Do you want to reverse order?");
						String newcolor = myFrame.getColor();
						if (newcolor.equals("red") || newcolor.equals("Red") || newcolor.equals("r") ) {
							myGame.setCurrentColor(1);
							sendtoAll("Color changed to red!");
							//break;
						} else if (newcolor.equals("yellow") || newcolor.equals("Yellow") || newcolor.equals("y")) {
							myGame.setCurrentColor(2);
							sendtoAll("Color changed to yellow!");
							//break;
						} else if (newcolor.equals("green") || newcolor.equals("Green") || newcolor.equals("g")) {
							myGame.setCurrentColor(3);
							sendtoAll("Color changed to green!");
							//break;
						} else if (newcolor.equals("blue") || newcolor.equals("Blue") || newcolor.equals("b")) {
							myGame.setCurrentColor(4);
							sendtoAll("Color changed to blue!");
							//break;
						} else {
							Random rand = new Random();
							int nextColor= rand.nextInt(4) + 1;
							myGame.setCurrentColor(nextColor);
							if (nextColor==1) {
								sendtoAll("Color changed to red!");
							} else if (nextColor==2) {
								sendtoAll("Color changed to yellow!");
							} else if (nextColor==3) {
								sendtoAll("Color changed to green!");
							} else {
								sendtoAll("Color changed to blue!");
							}
						}
					
					//special function Reverse on black
					
						//out.println("Do you want to reverse?Yes/No");
						String answer = myFrame.getOrder();
						if (answer.equals("Yes")||answer.equals("yes")||answer.equals("y")) {
							sendtoAll("Player "+ this.playerNumber+ " reversed order!");
							myGame.wildChangeOrder();
							//break;
						} else if (answer.equals("No")||answer.equals("no")||answer.equals("n")) {
							sendtoAll("Player "+ this.playerNumber+ " did not change the order!");
							//break;
						} else {
							Random rand = new Random();
							int nextReverse = rand.nextInt(2);
							if (nextReverse == 0) {
								sendtoAll("Player "+ this.playerNumber+ " reversed order!");
								myGame.wildChangeOrder();
							}
						}
					
					//
					
					
				}
				sendtoAll("Player "+playerNumber+" finished!");
				
				sendtoAll("Player "+playerNumber+" played "+myGame.printCurrentCard());
				if (myGame.isCurrentSkip()) {
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
				sendtoAll("It's player "+myGame.getCurrentPlayer()+"'s turn!");
				
				firstDraw=true;
			} else {
				myFrame.setInfoString("Invalid Move, try again or draw!");
			}
		}
		request="";
		myFrame.updateInfo();
	}
	
}
