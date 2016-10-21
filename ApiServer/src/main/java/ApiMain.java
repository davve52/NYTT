

import java.util.HashMap;
import java.util.List;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import org.json.JSONObject;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Created by davve on 2016-10-12.
 */
public class ApiMain {
    private static Image image;
    private static String imagePatherino;

    public static void main(String[]args) throws ClassNotFoundException {
        ApiMain.apply();
        Database database = new Database();
        image = new Image();
        get("/item", (request, response) -> {
            itBridegeOpen();
            imagePatherino = getRandomItem(database.fetchItems());
            response.body(imagePatherino);
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });

        get("/item/next-week", (request, response) -> {
            response.body("ITEM FÖR NÄSTA VECKA");
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });

        put("/item/:itemId/update-price", (request, response) -> {
            database.updatePrice(Integer.parseInt(request.queryParams("price")), Integer.parseInt(request.params(":itemId") ));
            response.body("ANVÄNDARE BUDAR PÅ ITEM");
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });

        delete("/item/:itemId", (request, response) -> {
            database.deleteItem(Integer.parseInt(request.params(":itemId")));
            response.body("Användaren tar bort vara");
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });
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

            System.out.println("Response from RANDOM NBR GENERATOR:");
            returnValue = response.asString().getBody();

            /*Unirest.shutdown();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();*/
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    private static Boolean itBridegeOpen(){
        HttpResponse<JsonNode> response;
        Boolean bool = false;

        try {
            response = Unirest.get("http://data.goteborg.se/BridgeService/v1.0/GetGABOpenedStatus/8ff334db-2c18-4c35-b6bb-e191ad1f655d?format=Json").asJson();

            System.out.println("Response from GÖTAELVEN:");
            // retrieve the parsed JSONObject from the response
            JSONObject myObj = response.getBody().getObject();
            if(myObj.getBoolean("Value")){
                bool = false;
                System.out.println("BRON ÄR STÄNGD");
            }
            else {
                bool = true;
                System.out.println("BRON ÄR ÖPPEN");
            }

            /*Unirest.shutdown();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();*/
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return bool;
    }

    private static String getRandomItem(List<Item> items){
        String randomItemValue = getRandomNumberApi(items.size());

        String img = "";
        int result = 0;
        try {
            result = Integer.parseInt(String.valueOf(randomItemValue.charAt(0)));
            System.out.println(result);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        for (int i = 0; i < items.size(); i ++){
            if (result == items.get(i).getId()){
                img = items.get(i).getImage();
                String imagePath = "http://ddwap.mah.se/ae3529/dittemitt/";
                image.uploadImage(imagePath + img);
            }
        }
        return image.getImage();
    }

}
