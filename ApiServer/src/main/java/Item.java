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
    private String datee;



    public Item(int id, String title, String image, String description, String datee){
        this.id = id;
        this.title = title;
        this.image = image;
        this.description = description;
        this.datee = datee;
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

    public String getDatee() {
        return datee;
    }


}


