package sample;

//unirest libraries
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

//json arraylists
import org.json.JSONArray;

//utils
import java.io.*;
import java.util.*;

/**
 * Created by Marcello395 on 2/12/17.
 */
public class DataGet implements Serializable {
    String name;
    int cost;
    int health;
    int attack;
    double value;
    KMeans km = new KMeans();

    //Imported Unirest Library to pull all data and parse JSON into objects
    public ArrayList<Card> grabber() throws Exception {
        //mashape url + key grab, this grabs only "minion" type cards
        HttpResponse<JsonNode> response = Unirest.get("https://omgvamp-hearthstone-v1.p.mashape.com/cards/types/Minion")
                .header("X-Mashape-Key", "W4S5VWkaH1msh4JqoHo99DS9umjVp1cGIPIjsnO8vZRFUtjk99")
                .header("Accept", "application/json")
                .asJson();

        //Gets the entire JSON and puts everything in a 'Card' type array list.
        JSONArray JSONDataArray = response.getBody().getArray();
        ArrayList<Card> cardArrayList = new ArrayList();

        for (int i = 0; i < JSONDataArray.length(); i++) {
            if (response.getBody().getArray().optJSONObject(i).has("cost") &&
                    response.getBody().getArray().getJSONObject(i).has("health") &&
                    response.getBody().getArray().getJSONObject(i).has("attack")
                    ) {

                name = response.getBody().getArray().getJSONObject(i).getString("name");
                cost = response.getBody().getArray().getJSONObject(i).getInt("cost");
                health = response.getBody().getArray().getJSONObject(i).getInt("health");
                attack = response.getBody().getArray().getJSONObject(i).getInt("attack");

                //value will be used for similarity metric, different attributes have different weights
                value = ((cost * .25) + (health * .45) + (attack * .30));

                //Adds all attributes to the card, this will be referenced in for my hashtable
                Card hsValues = new Card(name, cost, health, attack, value);
                cardArrayList.add(hsValues);
                //km.addPoint(new Point(cost, value));

            }
        }
        return cardArrayList;
    }
}


