
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BooleanSupplier;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import spark.*;


/**
 * Created by davve on 2016-10-12.
 */
public class ApiMain {
    private static Image image = new Image();
    private static Database database;
    private static Boolean bool = true;
    private static Boolean startDatabase = true;
    private static Gson gson;

    public static void main(String[] args) throws ClassNotFoundException {
        port(getHerokuAssignedPort());

        if (startDatabase){
            enableCORS();
            database = new Database();
            gson = new GsonBuilder().create();
            startDatabase = false;

        }


        get("/image", (request, response) -> {
            String imageURL;
            if (bool || isBridegeOpen()){
                Bid winner = database.getHighBidder(); // VINNARE AV BILDEN GÖR NÅGOT AV DET OM MAN VILL
                database.clearDatabase();
                uploadImageToWebb();
                imageURL = showImageToUser();
                response.body(imageURL);
                response.status(200);
                request.headers("Accept");
                bool= false;

            } else{
                System.out.println("VISA BARA BILDEN");
                imageURL = showImageToUser();
                Bid bid = database.getHighBidder();
                String json = gson.toJson(bid);
                System.out.println(json);
                response.body(imageURL);
                response.status(200);
                request.headers("Accept");
            }

            return response.body(); // Skicka tillbaka svaret
        });

        get("/bid", (request, response) -> {
            System.out.println(request.body().toString());
            String bidValue = request.queryParams("bidValue");
            String email = request.queryParams("email");
            String image = request.queryParams("image");

            database.updatePrice(email, Integer.parseInt(bidValue), image);
            response.body("ANVÄNDARE BUDAR PÅ ITEM");
            response.status(200);
            request.headers("Accept");
            return response.body(); // Skicka tillbaka svaret
        });
    }
    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS() {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*" );
            response.header("Access-Control-Request-Method", "GET,PUT,POST,DELETE,OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,");
            // Note: this may or may not be necessary in your particular application
            response.type("application/html");
        });
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


    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }


}
