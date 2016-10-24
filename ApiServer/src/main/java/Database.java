import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joaki on 10/13/2016.
 */
public class Database{

    private Connection connection = null;

    public Database() throws ClassNotFoundException{
        Class.forName("com.mysql.jdbc.Driver");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://195.178.232.16/ae3529","ae3529","Guldfisk1337");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable{
        try {
            connection.close();
        } catch(SQLException e){
        }
        super.finalize();
    }

    public void updatePrice(String email, int price, String image){
        PreparedStatement ps = null;
        System.out.println(email + price + image);
        try {
            ps = connection.prepareStatement(
                    "INSERT INTO bid"
                            + "(Email, Bid, Image) VALUES"
                            + "(?,?,?)");

            ps.setString(1,email);
            ps.setInt(2,price);
            ps.setString(3,image);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Bid getHighBidder(){
        Bid bid = null;
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM bid where Bid = (SELECT MAX(Bid) from Bid)");
            while (rs.next()) {
                bid = new Bid(rs.getString("Email"), rs.getInt("Bid"), rs.getString("Image"));

            }

            statement.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return bid;
    }

    public void clearDatabase(){
        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement(
                    "TRUNCATE TABLE bid");

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

