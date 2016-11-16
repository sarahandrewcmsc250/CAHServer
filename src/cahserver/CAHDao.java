package cahserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CAHDao {    
    private Connection connection;
    
    public CAHDao(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cah?user=root&password=cmsc250");            
            System.out.println("Database connected");        
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    public ArrayList getWhiteDeck(){
        ArrayList<WhiteCard> deck = new ArrayList<>();
        WhiteCard card = new WhiteCard();
        try{
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM cah.white_cards WHERE white_cards.card_set_id = 1");
            while (set.next()){
                card.setText(set.getString(2));
                deck.add(card);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return deck;
    }
    
    public ArrayList getBlackDeck(){
        ArrayList<BlackCard> deck = new ArrayList<>();
        BlackCard card = new BlackCard();
        try{
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * FROM cah.black_cards WHERE black_cards.pick = 1");
            while (set.next()){
                card.setText(set.getString(2));
                card.setDraw(set.getInt(3));
                card.setPlay(4);
                deck.add(card);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return deck;
    }
    
    public void closeConnection(){        
        try{
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
