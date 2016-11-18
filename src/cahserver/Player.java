package cahserver;

import java.util.ArrayList;

public class Player {
    private int id;
    private String handle;
    private ArrayList<String> hand;
    private int points;

    public Player(){
        points = 0;
        this.hand = new ArrayList<>();
    }
    
    public Player(int id, String handle, ArrayList<String> hand){
        this.id = id;
        this.handle = handle;
        this.hand = hand;
        this.points = 0;
    }
    
    public void setID(int ident){
        id = ident;
    }
    
    public int getID(){
        return id;
    }
    
    public void setHandle(String hand){
        handle = hand;
    }
    
    public String getHandle(){
        return handle;
    }
    
    public void setHand(ArrayList<String> hand){
        this.hand = hand;
    }
    
    public ArrayList<String> getHand(){
        return hand;
    }
    
    public void addToHand(String card){
        hand.add(card);
    }
    
    public void removeFromHand(WhiteCard card){
        String text = card.getText();
        boolean found = false;
        int index = 0;
        while (found == false){
            if(card.getText().equals(hand.get(index))){
                hand.remove(index);
                found = true;
            }
            ++ index;
        }
    }
    
    public void addPoint(){
        ++ points;
    }
    
    public int getPoints(){
        return points;
    }
}
