package cahserver;

public class PlayedCard {
    private int playerID;
    private String cardText;
    
    public PlayedCard(int id, String text){
        this.playerID = id;
        this.cardText = text;
    }
    
    public int getID(){
        return playerID;
    }
    
    public String getText(){
        return cardText;
    }
}
