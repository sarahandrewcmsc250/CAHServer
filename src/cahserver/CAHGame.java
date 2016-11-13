package cahserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CAHGame {
    private ArrayList<WhiteCard> whiteDeck;
    private ArrayList<BlackCard> blackDeck;
    private Map playedCards;
    private Map players;
    private int played;
    private CAHDao dao;
    private static Lock lock;
    private static Condition hasPlayed;
    private static Condition isOver;
    private BlackCard currentBlackCard;
    
    public CAHGame(){
        dao = new CAHDao();
        playedCards = new HashMap<Integer, WhiteCard>();
        players = new HashMap<String, Player>();
        whiteDeck = dao.getWhiteDeck();
        blackDeck = dao.getBlackDeck();
        lock = new ReentrantLock();
        hasPlayed = lock.newCondition();
        isOver = lock.newCondition();
        played = 0;
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
    
    public void setWhiteDeck(ArrayList deck){
        whiteDeck = deck;
    }
    
    public ArrayList<BlackCard> getBlackDeck(){
        return blackDeck;
    }
    
    public void setBlackDeck(ArrayList deck){
        blackDeck = deck;
    }
    
    public void setCurrentBlackCard(BlackCard card){
        currentBlackCard = card;
    }
    
    public BlackCard getCurrentBlackCard(){
        return currentBlackCard;
    }
    
    public void playCard(int playerNo, WhiteCard card){
        lock.lock();
        hasPlayed.signalAll();
        lock.unlock();
        ++ played;
        playedCards.put(playerNo, card);
    }
    
    public void waitForReview(){
         if (players.size() == played){
            lock.lock();
            try{
                hasPlayed.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally{
                lock.unlock();
                played = 0;
            }
        }
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
    
    public CAHDao getDAO(){
        return dao;
    }
    
    public void signalGameOver(){
        lock.lock();
        isOver.signalAll();
        lock.unlock();
    }
}
