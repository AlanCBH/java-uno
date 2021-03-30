package gameTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import Cardgame.Cards;
import Cardgame.Game;
import Cardgame.Player;
import Cardgame.PlayerHandler;
import gameGUI.GameGUI;
import gameGUI.Mainframe;
import Cardgame.PlayConnection;
public class TestRunnerTest {
	
	@Test
	public void setUp() throws IOException {
		Game myGame=new Game(4,0);
		myGame.gameStart();
		assertEquals("Initial card is number",true,myGame.getCurrentCard().isNumber());
		assertEquals("107 cards to begin with",107,myGame.getDrawPile().size());
		assertEquals( "0 card in discard pile",0,myGame.getDiscardPile().size());
	}
	@Test
	public void testBasicCardPlay()  throws IOException {
		Game myGame=new Game(4,0);
		Cards card = new Cards(1,9,-1,-1); //red 9
		myGame.setCurrentCard(card);
		Cards temp = new Cards(1,2,-1,-1); //red 2
		Cards temp1 = new Cards(1,-1,0,-1); //red skip
		assertEquals("Card of same color",true, myGame.isValid(temp));
		assertEquals("Card of same color but function card",true, myGame.isValid(temp1));
		temp = new Cards(2,9,-1,-1); // yellow 9
		assertEquals("Card of same number",true, myGame.isValid(temp));
		myGame.setCurrentCard(temp1);
		temp = new Cards(3,-1,0,-1); //green skip
		assertEquals("Card of same function",true, myGame.isValid(temp));
		temp = new Cards(3,-1,1,-1); // green reverse;
		assertEquals("Card of different function different color ",false, myGame.isValid(temp));
		temp = new Cards(3,0, -1,-1); // green 0;
		myGame.setCurrentCard(temp);
		temp = new Cards(1,1, -1,-1); // red 1;
		assertEquals("Card of different number different color ",false, myGame.isValid(temp));
	}
	@Test
	public void testWildCardPlay() throws IOException {
		Game myGame=new Game(4,0);
		Cards card = new Cards(1,9,-1,-1); //red 9
		myGame.setCurrentCard(card);
		Cards temp = new Cards(-1,-1,-1,1); //wild
		Cards temp1 = new Cards(1,-1,-1,2); //wildDraw
		assertEquals("Can play wild",true, myGame.isValid(temp));
		assertEquals("Can play wild+4",true, myGame.isValid(temp1));
		myGame.setCurrentCard(temp);
		myGame.setCurrentColor(4); //blue
		temp1 = new Cards(4,-1,1,-1); //blue reverse
		assertEquals("Can play blue card",true, myGame.isValid(temp1));
		temp1 = new Cards(3,-1,1,-1); //green reverse
		assertEquals("Cannot play green card",false, myGame.isValid(temp1));
	}
	@Test
	public void testDrawTwoCard() throws IOException {
		Game myGame=new Game(4,0);
		Cards card = new Cards(1,9,-1,-1); //red 9
		myGame.setCurrentCard(card);
		Cards temp = new Cards(1,-1,2,-1); //red drawTwo
		assertEquals("Can play drawTwo of same color",true, myGame.isValid(temp));
		myGame.cardPlayed(temp,false);
		assertEquals("we just played DrawTWo",true, myGame.isPrevDrawTwo);
		assertEquals("next should play drawTwo or draw 2",2, myGame.getCurrentStack());
		temp = new Cards(1,3,-1,-1); // red 3
		assertEquals("Cannot play regular card ",false, myGame.isValid(temp));
		temp = new Cards(2,-1,2,-1); //yellow drawTwo
		assertEquals("Can play other drawTwo",true, myGame.isValid(temp));
		myGame.cardPlayed(temp,false);
		assertEquals("next should play drawTwo or draw 4",4, myGame.getCurrentStack());
		
		
	}
	@Test
	public void testDrawFourCard() throws IOException {
		Game myGame=new Game(4,0);
		Cards card = new Cards(1,9,-1,-1); //red 9
		myGame.setCurrentCard(card);
		Cards temp = new Cards(-1,-1,-1,2); //wildDraw
		assertEquals("Can play drawFour",true, myGame.isValid(temp));
		myGame.cardPlayed(temp,false);
		myGame.setCurrentColor(4);
		assertEquals("we just played DrawFour",true, myGame.isPrevDrawFour);
		assertEquals("next should play drawFour or draw 4",4, myGame.getCurrentStack());
		temp = new Cards(1,3,-1,-1); // red 3
		assertEquals("Cannot play regular card ",false, myGame.isValid(temp));
		temp = new Cards(-1,-1,-1,2); //yellow drawTwo
		assertEquals("Can play another drawFour",true, myGame.isValid(temp));
		myGame.cardPlayed(temp,false);
		assertEquals("next should play drawTwo or draw 8",8, myGame.getCurrentStack());
		
		
	}
	@Test
	public void testIdentical() throws IOException{
		Game myGame=new Game(4,0);
		Cards card = new Cards(1,9,-1,-1); //red 9
		myGame.setCurrentCard(card);
		Cards temp = new Cards(1,9,-1,-1); //another red 9
		assertEquals("Cards are identical",true, myGame.isIdentical(temp));
	}
	

	@Test
	public void testMainFrame() throws IOException {
		//Game myGame = new Game(4);
		//GameGUI UI = new GameGUI();
		//Game myGame = UI.getGame();
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Game myGame = new Game(4,0);
		pool.execute(myGame);
		Mainframe myframe = new Mainframe(myGame,4,null);
		assertEquals("Current card's info should be same","Current card of game: "+myGame.printCurrentCard(), myframe.getCurCardLabal());
	}
	

}
