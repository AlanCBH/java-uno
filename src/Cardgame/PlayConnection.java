package Cardgame;

import java.io.*;
import java.net.*;

public class PlayConnection implements Runnable {
	private Socket game;
	private BufferedReader in;
	private PrintWriter out;
	
	public PlayConnection (Socket s) throws IOException {
		game = s;
		in = new BufferedReader(new InputStreamReader(game.getInputStream()));
		out = new PrintWriter(game.getOutputStream(),true);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//String serverResponse = null;
		try {
			while(true) {
				String serverResponse = in.readLine();
				if (serverResponse== null) break;
				System.out.println(serverResponse);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
