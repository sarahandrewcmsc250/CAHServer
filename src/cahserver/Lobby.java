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
    
    public Lobby(int ident){
        id = ident;
        players = new HashMap<>();
        playerIds = new ArrayList<>();
        playersReady = 0;
        lock = new ReentrantLock();
        isReady = lock.newCondition();
        rng = new Random();
        czar = 0;
    }
    
    public void setID(int ident){
        id = ident;
    }
    
    public int getID(){
        return id;
    }
    
    public void readyUp(){
        ++ playersReady;
        if (playersReady == players.size()){
            lock.lock();
            isReady.signalAll();
            lock.unlock();
        }
    }
    
    public void removeReady(){
        -- playersReady;
    }
    
    public void waitForReady(){
        if (players.size() >= 3){
            lock.lock();
            try{
                isReady.await();
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally{
                lock.unlock();
            }
        }
    }
    
    public void addPlayer(Player player){
        int id = player.getID();
        players.put(id, player);
        czar = rng.nextInt(players.size());
    }
    
    public void removePlayer(Player player){
        int id = player.getID();
        players.remove(id);
        czar = rng.nextInt(players.size());
    }
    
    public Map getPlayers(){
        return players;
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
}
