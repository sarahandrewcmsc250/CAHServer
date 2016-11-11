package cahserver;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class FXMLDocumentController implements Initializable {
    private int playerNo = 0;
    @FXML
    private TextArea txaServerText;
    private Map lobbies;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lobbies = new HashMap<Integer, Lobby>();
        
        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(80);

                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();

                    // Increment clientNo
                    playerNo++;

                    Platform.runLater( () -> {
                    // Display the player number
                    txaServerText.appendText("Starting thread for client " + playerNo + " at " + new Date() + '\n');
                    });

                    // Create and start a new thread for the connection
                    ++ playerNo;
                    new Thread(new HandleAPlayer(socket, txaServerText, lobbies, playerNo)).start();
                }
            }catch(IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }
}

class HandleAPlayer implements Runnable, cah.CAHConstants{
    private Socket socket;
    private TextArea textArea;
    private CAHGame game;
    private Player player;
    private Map lobbies;
    private Lobby lobby;
    int playerNo;
    Random rng;
    
    public HandleAPlayer(Socket socket, TextArea area, Map lobbies, int playerNo){
        this.socket = socket;
        this.textArea = area;
        this.lobbies = lobbies;
        this.playerNo = playerNo;
        rng = new Random();
    }
    
    public void run(){
        try {
            // Create reading and writing streams
            BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter outputToClient = new PrintWriter(socket.getOutputStream());

            // Continuously serve the client
            while (true) {
                // Receive request code from the client
                int request = Integer.parseInt(inputFromClient.readLine());
                // Process request
                switch(request) {
                    case(DRAW_WHITE):
                        int draws = Integer.parseInt(inputFromClient.readLine());
                        for(int i = 1; i <= draws; ++ i){
                            int temp = rng.nextInt(game.getWhiteDeck().size());
                            WhiteCard card = game.getWhiteDeck().get(temp);
                            player.addToHand(card);
                        }
                        break;
                    case(PLAY_WHITE):
                        break;
                    case(PICK_WHITE):
                        int num = Integer.parseInt(inputFromClient.readLine());
                        WhiteCard card = new WhiteCard(inputFromClient.readLine());
                        game.playCard(num, card);
                        break;
                    case(GET_LOBBIES):
                        outputToClient.println("1");
                        outputToClient.println("2");
                        outputToClient.println("3");
                        outputToClient.println("4");
                        outputToClient.println("5");
                        outputToClient.flush();
                        break;
                    case(SEND_LOBBY):
                        int lobbyNum = Integer.parseInt(inputFromClient.readLine());
                        lobby = (Lobby) lobbies.get(lobbyNum);
                        lobby.addPlayer(player);
                        break;
                    case(SEND_HANDLE):
                        String handle = inputFromClient.readLine();
                        player.setHandle(handle);
                        break;
                    case(READY_UP):
                        
                        break;
                }
            }
        }catch(IOException ex) {
            Platform.runLater(()->textArea.appendText("Exception in client thread: "+ex.toString()+"\n"));
        }                    
    }
}

