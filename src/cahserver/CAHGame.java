package cahserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CAHGame {
    private ArrayList<WhiteCard> whiteDeck;
    private ArrayList<BlackCard> blackDeck;
    private Map playedCards;
    private Map players;
    private CAHDao dao;
    
    public CAHGame(){
        dao = new CAHDao();
        playedCards = new HashMap<Integer, WhiteCard>();
        players = new HashMap<String, Player>();
        whiteDeck = dao.getWhiteDeck();
        blackDeck = dao.getBlackDeck();
    }
    
    public CAHGame(HashMap<String, Player> map){
        dao = new CAHDao();
        playedCards = new HashMap<Integer, WhiteCard>();
        players = map;
        whiteDeck = dao.getWhiteDeck();
        blackDeck = dao.getBlackDeck();
    }
    
    public ArrayList<WhiteCard> getWhiteDeck(){
        return whiteDeck;
    }
    
    public ArrayList<BlackCard> getBlackDeck(){
        return blackDeck;
    }
    
    public void playCard(int playerNo, WhiteCard card){
        playedCards.put(playerNo, card);
    }
    
    public Map getPlayedCards(){
        return playedCards;
    }
    
    public void addPlayer(String handle, Player player){
        players.put(handle, player);
    }
    
    public Player getPlayer(String handle){
        return (Player) players.get(handle);
    }
}
