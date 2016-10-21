/**
 * Created by davve on 2016-10-21.
 */
public class Bid {
    private String image;
    private int bid;
    private String email;

    public Bid(String email, int bid, String image){
        this.email = email;
        this.bid = bid;
        this.image = image;
    }

    public String getImage(){
        return image;
    }

    public String getEmail(){
        return email;
    }

    public int getBid(){
        return bid;
    }
}
