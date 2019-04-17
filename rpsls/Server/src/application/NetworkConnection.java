package application;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

import javafx.scene.control.TextArea;

public abstract class NetworkConnection {
	
	private ConnThread connthread = new ConnThread();
	private Consumer<Serializable> callback;
    ArrayList<ClientThread> ct;
    boolean clientOne, clientTwo;
    String dataOne, dataTwo;
    String p1Moves = "Player 1 played: ";
    String p2Moves = "\nPlayer 2 Played: ";
    int p1Points,p2Points;
    
	public NetworkConnection(Consumer<Serializable> callback) {
		this.callback = callback;
		connthread.setDaemon(true);
        ct = new ArrayList<ClientThread>();
        clientOne = false;
        clientTwo = false;
	}
	
	public void startConn() throws Exception{
		connthread.start();
	}
	
	public void showPoints() {
		ct.forEach(t ->{ try{t.tout.writeObject("Player 1: "+p1Points + " points\nPlayer 2: "+ p2Points +" points"); }
        catch(Exception e) {e.printStackTrace();}
        });      
	}
	
	public void showMoves() {
		ct.forEach(t ->{ try{t.tout.writeObject(p1Moves + p2Moves); }
        catch(Exception e) {e.printStackTrace();}
        });  
	}
	
	public void send(Serializable data) throws Exception{
        if (isServer() && clientOne && clientTwo){
            String message = compare();
            ct.forEach(t ->{ try{t.tout.writeObject(message);
                                 clientOne =false;
                                 clientTwo =false;}
            catch(Exception e) {e.printStackTrace();}
            });
        }
        else{
            connthread.out.writeObject(data);
        }
	}
	
	public void closeConn() throws Exception{
		connthread.socket.close();
	}
	
    public String compare(){
    	String message;
    	switch(dataOne) {
    
    	case "paper":
    		p1Moves += " paper";
    		if(dataTwo.contentEquals("rock")) {
    			message = "Client 1 Wins, Paper > Rock";
    			p1Points++;
    			p2Moves += " rock";
    		}
    		else if (dataTwo.contentEquals("paper")) {
    			message = "Draw with Paper";
    			p2Moves += " paper";
    		}
    		else if (dataTwo.contentEquals("spock")) {
    			message = "Client 1 Wins, Paper > Spock";
    			p1Points++;
    			p2Moves += " spock";
    		}
    		else if (dataTwo.contentEquals("scissors")) {
    			message = "Client 2 Wins, Scissors > Paper";
    			p2Points++;
    			p2Moves += " scissors";
    		}
    		else {
    			message = "Client 2 Wins, Lizard > Paper";
    			p2Points++;
    			p2Moves += " lizard";
    		}
    		break;
    	case "rock":
    		p1Moves += " rock";
    		if(dataTwo.contentEquals("rock")) {
    			message = "Draw with Rock";
    			p2Moves += " rock";
    		}
    		else if (dataTwo.contentEquals("scissors")) {
    			message = "Client 1 Wins, Rock > Scissors";
    			p1Points++;
    			p2Moves += " scissors";
    		}
    		else if (dataTwo.contentEquals("lizard")) {
    			message = "Client 1 Wins, Rock > Lizard";
    			p1Points++;
    			p2Moves += " lizard";
    		}
    		else if (dataTwo.contentEquals("spock")) {
    			message = "Client 2 Wins, Spock > Rock";
    			p2Points++;
    			p2Moves += " spock";
    		}
    		else {
    			message = "Client 2 Wins, Paper > Rock";
    			p2Points++;
    			p2Moves += " paper";
    			
    		}
    		break;
    	case "scissors":
    		p1Moves += " scissors";
    		if(dataTwo.contentEquals("scissors")) {
    			message = "Draw with scissors";
    			p2Moves += " scissors";
    		}
    		else if(dataTwo.contentEquals("paper")) {
    			message = "Client 1 Wins, Scissors > Paper";
    			p1Points++;
    			p2Moves += " paper";
    		}
    		else if(dataTwo.contentEquals("lizard")) {
    			message = "Client 1 Wins, Scissors > Lizard";
    			p1Points++;
    			p2Moves += " lizard";
    		}
    		else if(dataTwo.contentEquals("spock")) {
    			message = "Client 2 Wins, Spock > Sciccors";
    			p2Points++;
    			p2Moves += " spock";
    		}
    		else {
    			message = "Client 2 Wins, Rock > Sciccors";
    			p2Points++;
    			p2Moves += " rock";
    			
    		}
    	case "lizard":
    		p1Moves += " lizard";
    		if(dataTwo.contentEquals("spock")) {
    			message = "Client 1 Wins, Lizard > Spock";
    			p1Points++;
    			p2Moves += " spock";
    		}
    		else if(dataTwo.contentEquals("paper")) {
    			message = "Client 1 Wins, Lizard > Paper";
    			p1Points++;
    			p2Moves += " paper";
    		}
    		else if(dataTwo.contentEquals("lizard")){
    			message = "Draw with Lizard";
    			p2Moves += " lizard";
    		}
    		else if(dataTwo.contentEquals("scissors")){
    			message = "Client 2 Wins, Scissors > Lizard";
    			p2Points++;
    			p2Moves += " scissors";
    		}
    		else {
    			message = "Client 2 Wins, Rock > Lizard";
    			p2Points++;
    			p2Moves += " rock";
    		}
    		break;
    		
    	case "spock":
    		p1Moves += " spock";
    		if(dataTwo.contentEquals("rock")) {
    			message = "Client 1 Wins, Spock > Rock";
    			p1Points++;
    			p2Moves += " rock";
    		}
    		else if(dataTwo.contentEquals("scissors")) {
    			message = "Client 1 Wins, Spock > Scissors";
    			p1Points++;
    			p2Moves += " scissors";
    		}
    		else if(dataTwo.contentEquals("spock")) {
    			message = "Draw with Spock";
    			p2Moves += " spock";
    		}
    		else if(dataTwo.contentEquals("Paper")) {
    			message = "Client 2 Wins, Paper > Spock";
    			p2Points++;
    			p2Moves += " spock";
    		}
    		else {
    			message = "Client 2 Wins, Lizard > Spock";
    			p2Points++;
    			p2Moves += " lizard";
    		}
    		break;

    	default:
    		message = "Undecided...";
    	}
    	message += "\nPress Play Again to start a New Game";
    	return message;
    }
    
	abstract protected boolean isServer();
	abstract protected String getIP();
	abstract protected int getPort();
	
	class ConnThread extends Thread{
		private Socket socket;
		private ObjectOutputStream out;
		
		public void run() {
            int number =1;
            
            if(isServer()){
                try{
                    ServerSocket server = new ServerSocket(getPort());
                    while(true){
                        ClientThread t1 = new ClientThread(server.accept(),number);
                        number++;
                        ct.add(t1);
                        t1.start();
                    }
                    
                }
                catch(Exception e){
                    
                }
            }
            
            else{
                try{
                    Socket socket = new Socket(getIP(), getPort());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    
                    this.socket =socket;
                    this.out =out;
                    socket.setTcpNoDelay(true);
                    
                    while(true){
                        Serializable data = (Serializable) in.readObject();
                        callback.accept(data);
                    }
                }
                catch(Exception e) {
                	
                }
            }
		}// close run
	}// close connthread
                       
    class ClientThread extends Thread{
        Socket s;
        int number;
        ObjectOutputStream tout;
        ObjectInputStream tin;
        
        ClientThread(Socket socket, int num){
            this.s = socket;
            this.number = num;
        
        }
        public void run(){
            try(
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream())){
                s.setTcpNoDelay(true);
                this.tout = out;
                this.tin = in;
                tout.writeObject("welcome player " + number);
                
                if (number ==2 ) {
            		tout.writeObject("2 clients connected");
            	}

                while(true){
                	
                	
                    Serializable data = (Serializable) in.readObject();
                    if(number ==1){
                        clientOne =true;                      
                        dataOne=data.toString();
                    }
                    else{
                        clientTwo =true;
                        dataTwo=data.toString();
                    }
                    callback.accept("Player " + number + ", " + data);
                }
                
            }
            catch(Exception e){
                callback.accept("connection closed");
                
            }
        }
	
    }
}

