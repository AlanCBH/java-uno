package Cardgame;
import java.util.*;
import java.net.*;
import java.io.*;

public class Player {
	private static final String SERVER_IP="127.0.0.1";
	private static final int SERVER_PORT = 9090;
	//private int number = 0;
	public PlayConnection myPc;
	public PrintWriter outWriter;
	public Socket server;
	public Player() throws IOException {
		Socket socket = new Socket(SERVER_IP,SERVER_PORT);
		this.server = socket;
		PlayConnection pc = new PlayConnection(socket);
		this.myPc = pc;
		PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
		this.outWriter = out;
	}
	
	public void playerSend(String text) {
		outWriter.println(text);
	}
	public static void main(String[] args) throws IOException {
		Player myPlayer = new Player();
		
		//BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		
		new Thread(myPlayer.myPc).start();
		while(true) {
			//String serverResponse = input.readLine();
			//System.out.println("Game: "+serverResponse);
			
			System.out.println("> ");
			String command = keyboard.readLine();
			
			if(command.equals("quit")) break;
			
			myPlayer.outWriter.println(command);
		
			
		}
		//.close();
	}
}
