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
        try {
            ps = connection.prepareStatement(
                    "UPDATE bid SET Email = ?, Bid = ?, Image = ?");

            ps.setString(1,email);
            ps.setInt(2,price);
            ps.setString(3,image);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public List<Bid> getHighBidder(){
        List<Bid> bids = new ArrayList<Bid>();
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM bid");
            while (rs.next()) {
                Bid bid = new Bid(rs.getString("Email"), rs.getInt("Bid"), rs.getString("Image"));
                bids.add(bid);
            }

            statement.close();
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return bids;
    }

}

