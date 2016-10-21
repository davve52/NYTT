import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by Joaki on 10/13/2016.
 */
public class Item {
    private String title = "";
    private int id = 0;
    private String image = "";
    private String description = "";
    private int currentBid = 0;
    private Timestamp datee;



    public Item(int id, String title, String image, String description, Timestamp datee, int currentBid){
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.datee = datee;
        this.currentBid = currentBid;
    }

    public String getTitle(){
        return title;
    }

    public String getImage(){
        return image;
    }

    public String getDescription(){
        return description;
    }

    public int getId(){
        return id;
    }

    public String getStringId(){
        return String.valueOf(id);
    }

    public Timestamp getDatee() {
        return datee;
    }

    public int getCurrentBid(){
        return currentBid;
    }



}


