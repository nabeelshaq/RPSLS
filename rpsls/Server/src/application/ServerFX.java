package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServerFX extends Application{

	private boolean isServer = true;
	
	private NetworkConnection  conn;
	private TextArea messages = new TextArea();
	
	private Parent createContent() {
		messages.setPrefHeight(550);
		TextField input = new TextField("enter messages, or press return to show winner");

		input.setOnAction(event -> {
			
			String message = isServer ? "Server: " : "Client: ";
			message += input.getText();
			input.clear();
			
			messages.appendText(message + "\n");
			try {
				conn.send(message);
			}
			catch(Exception e) {
				
			}
			
		});
		
		Button showPoints = new Button("Show Points");
		Button round = new Button("Show Player Moves");
		input.setPrefWidth(565);
		VBox center = new VBox(messages);
		HBox bottom = new HBox(input,showPoints,round);
		
		Button sOn = new Button("Server On");
		Button sOff = new Button("Server Off");
		
		TextField portNum = new TextField("5555");
		
		sOn.setOnAction(event ->{
			int portNumber=Integer.parseInt(portNum.getText());
			conn=createServer(portNumber);
			try {
				conn.startConn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			portNum.clear();
			
		});
		
		sOff.setOnAction(event ->{
			try {
				conn.closeConn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		});
		
		showPoints.setOnAction(event -> {
			try {
				conn.showPoints();
			}
			catch(Exception e) {
				
			}
		});
		
		round.setOnAction(event -> {
			try {
				conn.showMoves();
			}
			catch(Exception e) {
				
			}
		});
		
		Text warn = new Text("Create Server before connecting Clients!");
		Region r = new Region();
		Region z = new Region();
		r.setPrefSize(50, 27);
		z.setPrefSize(70, 27);
		HBox t = new HBox(portNum,r,warn,z,sOn,sOff);
		t.setPrefSize(736, 27);
		t.setPadding(new Insets(8,8,8,8));
		bottom.setPrefWidth(706);
		bottom.setPadding(new Insets(1,1,1,1));
		VBox top = new VBox(t);
		top.setPrefSize(752, 43);
		Text right = new Text("");
		Text left = new Text("");
		
		center.setAlignment(Pos.CENTER);
		bottom.setAlignment(Pos.BOTTOM_LEFT);
		top.setAlignment(Pos.TOP_LEFT);
		
		top.setPadding(new Insets(5,5,5,5));
		center.setPrefSize(800, 382);
		top.setPrefSize(353, 27);
		bottom.setPrefSize(800, 29);
		
		BorderPane root = new BorderPane(center,top,right,bottom,left);	
		return root;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.show();
		
	}

	
	@Override
	public void stop() throws Exception{
		conn.closeConn();
	}
	
	private Server createServer(int portNumber) {
		return new Server(portNumber, data-> {
			Platform.runLater(()->{
				messages.appendText(data.toString() + "\n");
			});
		});
	}
	


}
