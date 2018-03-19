package sample;


/**
 * Created by Marcello395 on 2/12/17.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.*;

public class Controller extends Application implements Serializable {


    //all objects which are referenced in gui.
    Stage window;
    Scene scene;
    Button button;
    Button button2;
    Button button3;
    ComboBox<Card> comboBox;
    DataGet getHearth = new DataGet();
    ListView listView;
    HashMap hsCache = new HashMap();
    BTree<String, Double> hsTree = new BTree<>();
    KMeans km = new KMeans();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("HearthStone 365");
        button = new Button("Cache!");
        button2 = new Button("Deserialize");


        //grabs data from JSON array
        ArrayList<Card> grab = getHearth.grabber();

        /*
        Populates the hashmap with key value pairs, this saves memory by
        only providing relevant data for the serialized file.
         */
        for (Card c : grab) {
            hsCache.put(c.getName(), c.getValue());
            km.addPoint(new Point(c.getCost(), c.getValue()));
            km.setClusterNum((int) Math.sqrt(c.getHealth() * 3));
            km.init();
            km.calculate();
        }

        //combobox which holds all cards for selection
        comboBox = new ComboBox<>(FXCollections.observableArrayList());
        ObservableList<Card> list = FXCollections.observableArrayList(grab);
        comboBox.setItems(list);
        comboBox.setPromptText("Please select a card");
        //ComboBoxes also generate actions if you need to get value instantly
        comboBox.setOnAction(e -> System.out.println("User selected " + comboBox.getValue()));

        //Listview holds data when button is pressed
        listView = new ListView<>();
        listView.getItems().addAll();
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        //calls the similarity metric
        // button.setOnAction(e -> getDistance(comboBox.getValue().getName()));
        button.setOnAction(e -> buttonPress1());
        button2.setOnAction(e -> buttonPress2());

        //layout stuff below
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(comboBox, button, button2, listView);
        scene = new Scene(layout, 700, 550);
        window.setScene(scene);
        window.show();
    }

    //Connects our listview with an observable list with an arrayList
    private void buttonPress1() {
        //stores card data in a serialized file.
        try {
            FileOutputStream fos = new FileOutputStream("hsCache.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(hsCache);
            oos.close();
            fos.close();
            //saved.
            System.out.println("Serialized Cache is saved in hsCache.ser");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
    }


    public void buttonPress2() {
        System.out.println("Deserialized Files..");
        Set set = hsCache.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry memEntry = (Map.Entry) iterator.next();
            //System.out.println("Key; " + memEntry.getKey() + " Value; " + memEntry.getValue());
            System.out.print(memEntry.getKey());
            hsTree.put((String) memEntry.getKey(), (Double) memEntry.getValue());
            Point q = null;
            q = km.getPoint((String) memEntry.getKey());
            Point near = km.getNearestPoint(q);
            String n = near.toString();
            String t = q.toString();

        }
    }
}


