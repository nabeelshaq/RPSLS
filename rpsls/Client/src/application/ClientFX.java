package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClientFX extends Application{

	private boolean isServer = false;
	
	private NetworkConnection  conn;
	private TextArea messages = new TextArea();
    String rockLink = "http://picoplex.net/games/rpslp/rock.png";
    String paperLink ="http://picoplex.net/games/rpslp/paper.png";
    String scissorsLink = "http://picoplex.net/games/rpslp/scissors.png";
    String lizardLink = "http://picoplex.net/games/rpslp/lizard.png";
    String spockLink = "http://picoplex.net/games/rpslp/spock.png";
	
	private Parent createContent() {
		TextField input = new TextField();

		input.setOnAction(event -> {

			String message = input.getText(); 
			input.clear();
			
			messages.appendText(message + "\n");
			try {
				conn.send(message);
			}
			catch(Exception e) {
				
			}
			
		});
		
		Button connect = new Button("Connect");
		TextField portNum = new TextField("5555");
		TextField ipAddress = new TextField("127.0.0.1");
		HBox t = new HBox(portNum, ipAddress, connect);
		t.setPrefSize(736, 27);
		t.setPadding(new Insets(8,8,8,8));
		VBox top = new VBox(t);
		top.setPrefSize(752, 43);
		
		Button playAgain = new Button("Play Again");
		Button quit = new Button("Quit");
		HBox bottom = new HBox(playAgain,quit,input);
		bottom.setPrefWidth(706);
		bottom.setPadding(new Insets(3,3,3,3));
		
        ImageView rgraphic = new ImageView(new Image(rockLink));
        rgraphic.setFitWidth(75);
        rgraphic.setFitHeight(76);
        ImageView pgraphic = new ImageView(new Image(paperLink));
        pgraphic.setFitWidth(75);
        pgraphic.setFitHeight(76);
        ImageView sgraphic = new ImageView(new Image(scissorsLink));
        sgraphic.setFitWidth(75);
        sgraphic.setFitHeight(76);
        ImageView lgraphic = new ImageView(new Image(lizardLink));
        lgraphic.setFitWidth(75);
        lgraphic.setFitHeight(76);
        ImageView spgraphic = new ImageView(new Image(spockLink));
        spgraphic.setFitWidth(75);
        spgraphic.setFitHeight(76);
        
        Button rock = new Button(null,rgraphic);
		rock.setPrefSize(75, 76);
		Button paper = new Button(null,pgraphic);
		paper.setPrefSize(75, 76);
		Button scissors = new Button(null,sgraphic);
		scissors.setPrefSize(75, 76);
		Button lizard = new Button(null,lgraphic);
		lizard.setPrefSize(75, 76);
		Button spock = new Button(null,spgraphic);
		spock.setPrefSize(75, 76);
		VBox l = new VBox(rock,paper,scissors,lizard,spock);
		HBox left = new HBox(l);
		
		connect.setOnAction(event ->{
			int portNumber=Integer.parseInt(portNum.getText());
			String ip =ipAddress.getText();
			conn=createClient(portNumber,ip);
			try {
				conn.startConn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		rock.setOnAction(event -> {
			try {
				conn.send("rock");
			}
			catch(Exception e) {
				
			}
		});
		paper.setOnAction(event -> {
			try {
				conn.send("paper");
			}
			catch(Exception e) {
				
			}
		});
		scissors.setOnAction(event -> {
			try {
				conn.send("scissors");
			}
			catch(Exception e) {
				
			}
		});
		lizard.setOnAction(event -> {
			try {
				conn.send("lizard");
			}
			catch(Exception e) {
				
			}
		});
		spock.setOnAction(event -> {
			try {
				conn.send("spock");
			}
			catch(Exception e) {
				
			}
		});
		
		playAgain.setOnAction(event ->{
			messages.appendText("New Game started\nMake your moves\n");
		});
		
		quit.setOnAction(event ->{
			try {
				conn.closeConn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Platform.exit();
		});
		

		messages.setPrefSize(700, 350);
		input.setPrefSize(666, 27);
		Text right = new Text("");
		BorderPane root = new BorderPane(messages,top,right,bottom,left);
		
		return root;
	
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setScene(new Scene(createContent(),800,505));
		primaryStage.show();
		
	}
	

	@Override
	public void stop() throws Exception{
		conn.closeConn();
	}
	

	
	private Client createClient(int portNumber, String ipAddress) {
		return new Client(ipAddress, portNumber, data -> {
			Platform.runLater(()->{
				messages.appendText(data.toString() + "\n");
			});
		});
	}

}
