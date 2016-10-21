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

    public List<Item> fetchItems() {
        List<Item> items = new ArrayList<Item>();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT * FROM item");
            while (rs.next()) {
                Item item = new Item(rs.getInt("ID"), rs.getString("Title"), rs.getString("Image"),
                        rs.getString("Description"), rs.getTimestamp("Datee"), rs.getInt("CurrentBid") );

                items.add(item);
            }

            statement.close();
        }
         catch(SQLException e) {
             e.printStackTrace();
         }

        return items;
    }
/*
    public Item fetchItem(int id) {
        Item item = null;
        try{
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT FROM items WHERE ID = " + id);
            if(rs.next()){
                item = new Item(rs.getInt("ID"), rs.getInt("userID"), rs.getString("title"),rs.getString("image"),
                        rs.getString("description"), rs.getInt("estimatedPrice"),rs.getString("datee"),rs.getString("tradeFor"),
                        rs.getString("category"),rs.getString("city") );

            }

            statement.close();
        } catch(SQLException e){
            e.printStackTrace();
        }

        return item;
    }
*/

    public void updatePrice(int price, int id){
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(
                    "UPDATE Messages SET CurrentBid = ?, WHERE id = ?");

            ps.setInt(1,price);
            ps.setInt(2,id);

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void deleteItem(int id) {
        try {
            Statement statement = connection.createStatement();

            statement.executeUpdate("DELETE FROM items WHERE ID = " + id);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

