package cahserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class FXMLDocumentController implements Initializable {
    @FXML
    private TextArea txaServerText;
    private int playerNo = 0;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);

                while (true) {
                    System.out.println("Waiting for client.");
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();

                    // Increment clientNo
                    ++ playerNo;

                    Platform.runLater( () -> {
                    // Display the player number
                    txaServerText.appendText("Starting thread for client " + playerNo + " at " + new Date() + '\n');
                    });

                    // Create and start a new thread for the connection
                    new Thread(new HandleAPlayer(socket, txaServerText, playerNo)).start();
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
    private Lobby lobby;
    private int playerNo;
    private int gameState;
    private int czar;
    private Random rng;
    
    public HandleAPlayer(Socket socket, TextArea area, int playerNo){
        this.player = new Player();
        this.socket = socket;
        this.textArea = area;
        this.playerNo = playerNo;
        this.rng = new Random();
        //this.game = new CAHGame();
        this.gameState = 1;
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
                            if (game.getWhiteDeck().isEmpty()){
                                game.setWhiteDeck(game.getDAO().getWhiteDeck());
                            }
                            int temp = rng.nextInt(game.getWhiteDeck().size());
                            WhiteCard card = game.getWhiteDeck().get(temp);
                            game.getWhiteDeck().remove(card);
                            player.addToHand(card.getText());
                        }
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " drew " + draws + " cards" + '\n');
                        });
                        break;
                    case(DRAW_BLACK):
                        int temp = rng.nextInt(game.getBlackDeck().size());
                        BlackCard bCard = game.getBlackDeck().get(temp);
                        game.getBlackDeck().remove(bCard);
                        game.setCurrentBlackCard(bCard);
                        outputToClient.println(bCard.getText());
                        outputToClient.flush();
                        Platform.runLater( () -> {
                            textArea.appendText("New black card drawn" + '\n');
                        });
                        break;
                    case(GET_BLACK):
                        outputToClient.println(game.getCurrentBlackCard().getText());
                        outputToClient.flush();                        
                        Platform.runLater( () -> {
                            textArea.appendText("Current black card's information reported" + '\n');
                        });
                        break;
                    case(PLAY_WHITE):
                        String card = inputFromClient.readLine();
                        game.playCard(card, player.getID());
                        new Thread(()->{
                            game.waitForChoice();
                            System.out.println("WHY WONT THIS THREAD RUN");
                            Platform.runLater( () -> {
                                textArea.appendText("Client " + playerNo + " has played a white card" + '\n');
                            });
                        }).start();
                        break;
                    case(PICK_WHITE):
                        String winner = inputFromClient.readLine();
                        int win = (int) (game.getPlayedCards().get(winner));
                        game.getPlayer(win).addPoint();
                        if (game.getPlayer(win).getPoints() == 5){
                            gameState = 0;
                        }                        
                        int size = game.getCzar();
                        if (size == game.getPlayersSize()){
                            size = 1;
                        }else{
                            ++ size;
                        }
                        game.setCzar(size);
                        game.choose();
                        Platform.runLater( () -> {
                            textArea.appendText("The card czar " + playerNo + " has picked a white card" + '\n');
                        });
                        break;
                    case(GET_HAND):
                        ArrayList hand = lobby.getPlayer(player.getID()).getHand();
                        int total = hand.size();
                        outputToClient.println(total);
                        for(int i = 0; i < total; ++ i){
                            outputToClient.println(hand.get(i).toString());
                        }
                        outputToClient.flush();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has requested the info of the cards in thier hand" + '\n');
                        });
                        break;
                    case(GET_SCORE):
                        outputToClient.println(player.getPoints());
                        outputToClient.flush();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has requested thier score" + '\n');
                        });
                        break;
                    case(GET_ALL_SCORES):
                        outputToClient.println(gameState);
                        Map map = game.getPlayers();
                        Set set = map.keySet();
                        Iterator iter = set.iterator();
                        while(iter.hasNext()){
                            int id = (int) iter.next();
                            Player user = (Player) map.get(id);
                            outputToClient.println(user.getHandle());
                            outputToClient.println(user.getPoints());
                        }
                        outputToClient.flush();
                        game.clearPlayedCards();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has requested everyone's score" + '\n');
                            textArea.appendText("The list of played cards has been cleared" + '\n');
                        });
                        break;
                    case(GET_LOBBIES):
                        int count = 5;
                        outputToClient.println(count);
                        for(int i = 1; i <= count; ++ i){
                            outputToClient.println(i);
                        }
                        outputToClient.flush();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has asked for the list of lobbies" + '\n');
                        });
                        break;
                    case(SEND_LOBBY):
                        int lobbyNum = Integer.parseInt(inputFromClient.readLine());
                        switch(lobbyNum){
                            case(1):
                                lobby = LOBBY_1;
                                break;
                            case(2):
                                lobby = LOBBY_2;
                                break;
                            case(3):
                                lobby = LOBBY_3;
                                break;
                            case(4):
                                lobby = LOBBY_4;
                                break;
                            case(5):
                                lobby = LOBBY_5;
                                break;
                        }
                        lobby.addPlayer(player);
                        lobby.addPlayerID(playerNo);
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has sent thier lobby choice" + '\n');
                        });
                        break;
                    case(SEND_HANDLE):
                        String handle = inputFromClient.readLine();
                        player.setHandle(handle);
                        player.setID(playerNo);
                        outputToClient.println(playerNo);
                        outputToClient.flush();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has sent thier handle" + '\n');
                        });
                        break;
                    case(READY_UP):                        
                        lobby.readyUp();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has readied up" + '\n');
                        });
                        new Thread(()->{
                            if ((lobby.getPlayersReady() != lobby.getPlayers().size()) || (lobby.getPlayersReady() < 3)){
                                lobby.waitForReady();
                            }    
                            lobby.addToGame(player.getID(), player);
                            this.game = lobby.getGame();
                            int a = 0;
                            for(int i = 1; i <= 10; ++ i){
                                if (game.getWhiteDeck().isEmpty()){
                                    game.setWhiteDeck(game.getDAO().getWhiteDeck());
                                }
                                a = rng.nextInt(game.getWhiteDeck().size());
                                WhiteCard crd = game.getWhiteCard(a);
                                player.addToHand(crd.getText());
                                game.getWhiteDeck().remove(crd);
                            }
                            czar = lobby.getCzar();
                            game.setCzar(czar);
                            outputToClient.println(lobby.getPlayerIds().get(czar - 1));
                            outputToClient.flush();
                            Platform.runLater( () -> {
                                textArea.appendText("Starting hands have been drawn" + '\n');
                                textArea.appendText("The first card czar has been chosen" + '\n');
                            });
                        }).start();
                        break;
                    case(UNREADY):
                        lobby.removeReady();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + playerNo + " has removed thier ready" + '\n');
                        });
                        break;
                    case(GET_CZAR):
                        int tzar = game.getCzar();
                        System.out.println("tzar = " + tzar);
                        outputToClient.println(tzar);
                        outputToClient.flush();
                        Platform.runLater( () -> {
                            textArea.appendText("Client " + tzar + " will be the new card czar" + '\n');
                        });
                        break;
                    case(GET_TO_JUDGE):
                        new Thread(()->{
                            game.waitForReview();
                            int length = game.getPlayedCards().size();
                            outputToClient.println(length);
                            Map toJudge = game.getPlayedCards();
                            Set keys = toJudge.keySet();
                            Iterator i = keys.iterator();
                            while(i.hasNext()){
                                outputToClient.println(i.next().toString());
                            }
                            outputToClient.flush();
                            Platform.runLater( () -> {
                                textArea.appendText("Client " + playerNo + " has requested the list of played cards" + '\n');
                            });
                        });                        
                        break;
                }
            }
        }catch(IOException ex) {
            Platform.runLater(()->textArea.appendText("Exception in client thread: "+ex.toString()+"\n"));
        }                    
    }
}

