package cahserver;

public class BlackCard {
    private String text;
    private int draw;
    private int play;
    
    public BlackCard(){
        
    }
    
    public BlackCard(String text, int draw, int play){
        this.text = text;
        this.draw = draw;
        this.play = play;
    }
    
    public void setText(String text){
        this.text = text;
    }
    
    public String getTest(){
        return text;
    }
    
    public void setDraw(int draw){
        this.draw = draw;
    }
    
    public int getDraw(){
        return draw;
    }
    
    public void setPlay(int play){
        this.play = play;
    }
    
    public int getPlay(){
        return play;
    }
}
