package gameGUI;

import Cardgame.Game;
import Cardgame.PlayerHandler;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Mainframe implements ActionListener {
	public JFrame pubFrame;
	final JTextField text2,text3,text4;
	private JLabel infoString = new JLabel("No info!");
	public ExecutorService pool = Executors.newFixedThreadPool(1);
	private String answer = "default",answerColor,answerOrder; 
	private PlayerHandler myPlayer; 
	private int myPlayerNumber;
	private JLabel questionLabel;
	private JLabel startLabel;
	private JLabel userLabel;
	private JLabel stackLabel,currentColorLabel;
	private JLabel currentLabel;
	private JLabel currentOrder;
	private JLabel handLabel;
	private JLabel colorLabel,reverseLabel;
	private Game curGame;
	private JButton buttonCard,buttonColorCard;
	public Mainframe(Game myGame,int playerNumber,PlayerHandler handler) {
		this.myPlayer=handler;
		curGame=myGame;
		myPlayerNumber=playerNumber;
		pubFrame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0,1));
		userLabel = new JLabel("You are player "+myPlayerNumber+ ",you can play when current player is the same as your player number.");
		startLabel = new JLabel("Current card of game: "+myGame.printCurrentCard());
		currentColorLabel = new JLabel("Current Color: "+myGame.getCurrentColor());
		stackLabel = new JLabel("Current stacked number: "+myGame.getCurrentStack());
		currentLabel = new JLabel("Current player number: "+myGame.getCurrentPlayer());
		//currentColorLabel = new JLabel("Current Color: "+myGame.getCurrentColor());
		colorLabel = new JLabel("If you are playing a wild card, type your next color here");
		reverseLabel = new JLabel("Whether you want to reverse the order?");
		int order = myGame.getCurrentOrder()? 1:-1;
		currentOrder = new JLabel("Current game order: "+order);
		try {handLabel = new JLabel("My handcard: "+ myGame.printHandCard(playerNumber));}
		catch (Exception e) {
			handLabel = new JLabel("No handcard");
		}
		questionLabel = new JLabel("type commands or type the card you want to play (card's position)");
		text2 = new JTextField(15);
		text3 = new JTextField(6);
		text4 = new JTextField(6);
		buttonCard = new JButton("Confirm card or command!");
		buttonColorCard = new JButton("Confirm answer!");
		buttonCard.addActionListener(this);
		panel.add(infoString);
		panel.add(userLabel);
		panel.add(startLabel);
		panel.add(currentColorLabel);
		panel.add(handLabel);
		panel.add(stackLabel);
		panel.add(currentLabel);
		panel.add(currentOrder);
		panel.add(questionLabel);
		panel.add(text2);;
		panel.add(colorLabel);
		panel.add(text3);
		panel.add(reverseLabel);
		panel.add(text4);
		panel.add(buttonCard);
		pubFrame.add(panel,BorderLayout.CENTER);
		pubFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pubFrame.setTitle("Game ongoing");
		pubFrame.pack();
	}

	public void show() {
		// TODO Auto-generated method stub
		this.pubFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//if (e.getSource() == buttonCard) {
		String number = text2.getText();
		//System.out.println(number);
		this.myPlayer.getString(number);
		//System.out.println("At least I'm here");
		updateInfo();
		curGame.updateAllFrame();
		answerColor = text3.getText();
		answerOrder = text4.getText();
			//this.myPlayer.setAnswer(answer);
		
	}
	public void updateInfo() {
		this.startLabel.setText("Current card of game: "+curGame.printCurrentCard());
		this.stackLabel.setText("Current stacked number: "+curGame.getCurrentStack());
		this.currentLabel.setText("Current player number: "+curGame.getCurrentPlayer());
		this.currentColorLabel.setText("Current Color: "+curGame.getCurrentColor());
		int order = curGame.getCurrentOrder()? 1:-1;
		this.currentOrder.setText("Current game order: "+order);
		//if (myPlayerNumber != null) {
		this.handLabel.setText("My handcard: "+ curGame.printHandCard(myPlayerNumber));
		answerColor = "default";
		answerOrder = "default";
		pubFrame.invalidate();
		pubFrame.validate();
		pubFrame.repaint();
	}
	public void setLabel(String myString) {
		questionLabel.setText(myString);
		this.show();
	}
	public void updateHand() {
		this.handLabel.setText("My handcard: "+ curGame.printHandCard(myPlayerNumber));
		SwingUtilities.updateComponentTreeUI(this.pubFrame);
		pubFrame.invalidate();
		pubFrame.validate();
		pubFrame.repaint();
	}
	public String getCurCardLabal() {
		return startLabel.getText();
	}
	public void setInfoString(String info) {
		infoString.setText(info);
		pubFrame.invalidate();
		pubFrame.validate();
		pubFrame.repaint();
	}

	public String getColor() {
		// TODO Auto-generated method stub
		return answerColor;
	}
	public String getOrder() {
		return answerOrder;
	}
}
