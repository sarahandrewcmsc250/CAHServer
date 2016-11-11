package cahserver;

public class WhiteCard {
    private String text;
    
    public WhiteCard(){
        
    }
    
    public WhiteCard(String text){
        this.text = text;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public String getText(){
        return text;
    }
}

