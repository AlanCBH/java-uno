package gameGUI;

import gameGUI.Mainframe;
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

import Cardgame.Game;

public class GameGUI implements ActionListener {
	final JTextField textPlayer;
	final JTextField textBot;
	public ExecutorService pool = Executors.newFixedThreadPool(1);
	final JButton answerButton;
	private JLabel startLabel1;
	private JLabel startLabel2;
	private Game myGame;
	public GameGUI() {		
		startLabel1 = new JLabel();
		LoadQuestion(startLabel1,"How many people to start the game with?");
		startLabel2 = new JLabel();
		LoadQuestion(startLabel2,"How many bot to start the game with?");
		textPlayer = new JTextField(15);//answer textfield
		textBot = new JTextField(15);
		answerButton=new JButton("Start Game!");
		answerButton.addActionListener(this);
		JButton buttonGameStart = new JButton("New Game");
		JButton buttonCardSelection = new JButton("Confirm Card");
		
		JButton buttonColorSelection = new JButton("Confirm Color");
		//buttonColorSelection.addActionListener(this);
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		panel.setLayout(new GridLayout(0,1));
		panel.add(startLabel1);
		panel.add(textPlayer);
		panel.add(startLabel2);
		panel.add(textBot);
		panel.add(answerButton);
		//panel.add(buttonCardSelection);
		//panel.add(buttonColorSelection);
		//panel.add(labelButtonCard);
		//panel.add(labelButtonColor);
		frame.add(panel,BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Game UI");
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		GameGUI myUI = new GameGUI();
	}
	public void actionPerformed(ActionEvent e) {
		LoadQuestion(startLabel1,"Get here!");
		String playerString = textPlayer.getText();
		String botString = textBot.getText();
		int playerNumber = Integer.parseInt(playerString);
		int botNumber = Integer.parseInt(botString);
		//Game myGame;
		try {
			//JLabel labelButtonCard = new JLabel("Confirm the card you want to play");
			myGame = new Game(playerNumber,botNumber);
			this.pool.execute(myGame);
			LoadQuestion(startLabel1,"Game started");
			//Mainframe frame = new Mainframe(myGame);
			//frame.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Invalid player number or bot number!");
		} 
		
	}
	public Game getGame() {
		return this.myGame;
	}
	public void LoadQuestion(JLabel label1,String question)
	{
	    label1.setText(question);//label1 is the questions label which sets the string question to the label
	}
}
