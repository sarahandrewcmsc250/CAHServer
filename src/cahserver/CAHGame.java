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
    private ArrayList<PlayedCard> playedCards;
    private Map players;
    private int played;
    private int czar;
    private CAHDao dao;
    private static Lock lock;
    private static Condition hasPlayed;
    private static Condition hasChosen;
    private BlackCard currentBlackCard;
    private int gameState;
    
    public CAHGame(){
        dao = new CAHDao();
        playedCards = new ArrayList<>();
        players = new HashMap<>();
        whiteDeck = dao.getWhiteDeck();
        blackDeck = dao.getBlackDeck();
        lock = new ReentrantLock();
        hasPlayed = lock.newCondition();
        hasChosen = lock.newCondition();
        played = 0;
        czar = 0;
        gameState = 1;
    }
    
    public int getGameState(){
        return gameState;
    }
    
    public void setGameState(int state){
        this.gameState = state;
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
    
    public int getPlayed(){
        return played;
    }
    
    public void playCard(int playerNo, String card){
        ++ played;
        PlayedCard pCard = new PlayedCard(playerNo, card);
        playedCards.add(pCard);
        if(played == players.size() - 1){
            lock.lock();
            hasPlayed.signalAll();
            lock.unlock();
        }              
    }
    
    public void waitForReview(){
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
        hasChosen.signalAll();
        lock.unlock();
    }
    
    public void waitForChoice(){
        lock.lock();
        try{
            hasChosen.await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
       
    public ArrayList<PlayedCard> getPlayedCards(){
        return playedCards;
    }
    
    public void clearPlayedCards(){
        playedCards.clear();
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
