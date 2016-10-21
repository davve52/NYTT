

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BooleanSupplier;

import static spark.Spark.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Created by davve on 2016-10-12.
 */
public class ApiMain {
    private static Image image = new Image();;
    //private static Database database = new Database();
    private static Boolean bool = true;

    public static void main(String[]args) throws ClassNotFoundException {
        ApiMain.apply();

        get("/image", (request, response) -> {
            String imageURL;
            if (bool || isBridegeOpen()){
                System.out.println("Första");

                uploadImageToWebb();
                imageURL = showImageToUser();
                response.body(imageURL);
                response.status(200);
                request.headers("Accept");
                bool= false;

            } else{
                System.out.println("VISA BARA BILDEN");
                imageURL = showImageToUser();
                response.body(imageURL);
                response.status(200);
                request.headers("Accept");
            }

            return response.body(); // Skicka tillbaka svaret
        });

        get("/item/next-week", (request, response) -> {
            response.body("ITEM FÖR NÄSTA VECKA");
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });

        put("/item/:itemId/update-price", (request, response) -> {
          //  database.updatePrice();
            response.body("ANVÄNDARE BUDAR PÅ ITEM");
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });
    }

    private static void startBackgroundWork() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isBridegeOpen()) {

                        uploadImageToWebb();

                    }
                }
            }

        });
        t1.start();
    }

    private static final HashMap<String, String> corsHeaders = new HashMap<String, String>();

    static {
        corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
        corsHeaders.put("Access-Control-Allow-Origin", "*");
        corsHeaders.put("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
        corsHeaders.put("Access-Control-Allow-Credentials", "true");
    }

    public final static void apply() {
        Filter filter = new Filter() {

            @Override
            public void handle(Request request, Response response) throws Exception {
                corsHeaders.forEach((key, value) -> {
                    response.header(key, value);
                });
            }
        };
        Spark.after(filter);
    }

    private static String getRandomNumberApi(int size){
        GetRequest response;
        String returnValue = "";

        try {
            response = Unirest.get("https://www.random.org/integers/?num=1&min=1&max="+size+"&col=1&base=10&format=plain&rnd=new");

            returnValue = response.asString().getBody();

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private static Boolean isBridegeOpen(){
        HttpResponse<JsonNode> response;
        Boolean bool = false;

        try {
            response = Unirest.get("http://data.goteborg.se/BridgeService/v1.0/GetGABOpenedStatus/8ff334db-2c18-4c35-b6bb-e191ad1f655d?format=Json").asJson();

            JSONObject myObj = response.getBody().getObject();
            if(myObj.getBoolean("Value")){
                bool = true;
                System.out.println("BRON ÄR ÖPPEN");
            }
            else {
                bool = false;
                System.out.println("BRON ÄR STÄNGD");
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return bool;
    }

    private static List<Item> getItemsFromAPI() {
        HttpResponse<JsonNode> response;
        List<Item> items = new ArrayList<Item>();
        try {
            response = Unirest.get("http://ddwap.mah.se/ae3529/dittemitt/server.php?item=getItem").asJson();

            JSONObject myObj = response.getBody().getObject();
            JSONArray jsonArray = myObj.getJSONArray("item");
            for (int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Item item = new Item(jsonObject.getInt("ID"), jsonObject.getString("Title"),
                        jsonObject.getString("Image"), jsonObject.getString("Description"), jsonObject.getString("Datee"));
                items.add(item);
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return items;
    }

    private static void uploadImageToWebb(){
        List<Item> list = getItemsFromAPI();
        String random = getRandomNumberApi(list.size());
        int randNbr = Integer.parseInt(String.valueOf(random.charAt(0)));

        for (int i = 0; i < list.size(); i ++){
            if (randNbr == list.get(i).getId()){
                String img = list.get(i).getImage();
                String imagePath = "http://ddwap.mah.se/ae3529/dittemitt/";
                image.uploadImage(imagePath + img);
            }
        }
    }

    private static String showImageToUser(){
        return image.getImage();
    }
}
