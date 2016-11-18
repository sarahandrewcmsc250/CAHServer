package cahserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lobby {
    private int id;
    private int czar;
    private Map<Integer, Player> players;
    private ArrayList<Integer> playerIds;
    private int playersReady;
    private static Lock lock;
    private static Condition isReady;
    private Random rng;
    private CAHGame game;
    
    public Lobby(int ident){
        id = ident;
        players = new HashMap<>();
        playerIds = new ArrayList<>();
        playersReady = 0;
        lock = new ReentrantLock();
        isReady = lock.newCondition();
        rng = new Random();
        czar = 0;
        game = new CAHGame();
    }
    
    public void setID(int ident){
        id = ident;
    }
    
    public int getID(){
        return id;
    }
    
    public void readyUp(){
        ++ playersReady;
        if ((playersReady == players.size()) && (playersReady >= 3)){
            lock.lock();
            isReady.signalAll();
            int temp = rng.nextInt(game.getBlackDeck().size());
            BlackCard bCard = game.getBlackDeck().get(temp);
            game.getBlackDeck().remove(bCard);
            game.setCurrentBlackCard(bCard);
            lock.unlock();
        }
    }
    
    public void removeReady(){
        -- playersReady;
    }
    
    public void waitForReady(){
        lock.lock();
        try{
            isReady.await();
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    }
    
    public int getPlayersReady(){
        return playersReady;
    }
    
    public void addPlayer(Player player){
        int id = player.getID();
        players.put(id, player);
        czar = rng.nextInt(players.size()) + 1;
        game.setCzar(czar);
    }
    
    public void removePlayer(Player player){
        int id = player.getID();
        players.remove(id);
        czar = rng.nextInt(players.size()) + 1;
        game.setCzar(czar);
    }
    
    public Map getPlayers(){
        return players;
    }
    
    public Player getPlayer(int id){
        return players.get(id);
    }
    
    public void addPlayerID(int id){
        playerIds.add(id);
    }
    
    public void removePlayerID(int id){
        playerIds.remove(id);
    }
    
    public ArrayList getPlayerIds(){
        return playerIds;
    }
    
    public int getCzar(){
        return czar;
    }
    
    public CAHGame getGame(){
        return game;
    }
    
    public void addToGame(int number, Player player){
        game.addPlayer(number, player);
    }
}
