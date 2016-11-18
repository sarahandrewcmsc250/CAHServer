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
    private int czar;
    private CAHDao dao;
    private static Lock lock;
    private static Condition hasPlayed;
    private static Condition hasChosen;
    private BlackCard currentBlackCard;
    
    public CAHGame(){
        dao = new CAHDao();
        playedCards = new HashMap<String, Integer>();
        players = new HashMap<Integer, Player>();
        whiteDeck = dao.getWhiteDeck();
        blackDeck = dao.getBlackDeck();
        lock = new ReentrantLock();
        hasPlayed = lock.newCondition();
        hasChosen = lock.newCondition();
        played = 0;
        czar = 0;
    }
    
    public ArrayList<WhiteCard> getWhiteDeck(){
        return whiteDeck;
    }
    
    public void setWhiteDeck(ArrayList deck){
        whiteDeck = deck;
    }
    
    public WhiteCard getWhiteCard(int index){
        return whiteDeck.get(index);
    }
    
    public ArrayList<BlackCard> getBlackDeck(){
        return blackDeck;
    }
    
    public void setBlackDeck(ArrayList deck){
        blackDeck = deck;
    }
    
    public BlackCard getBlackCard(int index){
        return blackDeck.get(index);
    }
    
    public void setCurrentBlackCard(BlackCard card){
        currentBlackCard = card;
    }
    
    public BlackCard getCurrentBlackCard(){
        return currentBlackCard;
    }
    
    public void playCard(String card, int playerNo){
        ++ played;
        playedCards.put(card, playerNo);
        if(played == players.size() - 1){
            lock.lock();
            hasPlayed.signalAll();
            lock.unlock();
        }                
    }
    
    public void waitForReview(){
        System.out.println(players.size());
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
    
    public void choose(){
        lock.lock();
        hasChosen.signal();
        lock.unlock();
    }
    
    public void waitForChoice(){
        lock.lock();
        try{
            hasChosen.await();
            System.out.println("waiting for choice");
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
       
    public Map getPlayedCards(){
        return playedCards;
    }
    
    public void clearPlayedCards(){
        playedCards = null;
    }
    
    public void addPlayer(int number, Player player){
        players.put(number, player);
    }
    
    public Player getPlayer(int id){
        return (Player) players.get(id);
    }
    
    public int getPlayersSize(){
        return players.size();
    }
    
    public Map getPlayers(){
        return players;
    }
    
    public void setCzar(int a){
        czar = a;
    }
    
    public int getCzar(){
        return czar;
    }
    
    public CAHDao getDAO(){
        return dao;
    }
}
