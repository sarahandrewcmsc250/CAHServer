package cahserver;

import java.util.ArrayList;

public class Player {
    private int id;
    private String handle;
    private ArrayList<WhiteCard> hand;

    public Player(){
        
    }
    
    public Player(int ident, String hand, ArrayList<WhiteCard> hnd){
        id = ident;
        handle = hand;
        this.hand = hnd;
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
    
    public void setHand(ArrayList<WhiteCard> hand){
        this.hand = hand;
    }
    
    public ArrayList<WhiteCard> getHand(){
        return hand;
    }
    
    public void addToHand(WhiteCard card){
        hand.add(card);
    }
    
    public void removeFromHand(WhiteCard card){
        String text = card.getText();
        boolean found = false;
        int index = 0;
        while (found == false){
            if(card.getText().equals(hand.get(index).getText())){
                hand.remove(index);
                found = true;
            }
            ++ index;
        }
    }
}
