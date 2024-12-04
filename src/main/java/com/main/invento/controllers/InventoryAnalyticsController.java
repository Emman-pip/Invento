package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Size;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.*;

public class InventoryAnalyticsController {

    private ObjectId inventoryId;

    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
        loadAddCharts(Integer.parseInt(new SimpleDateFormat("MM").format(new Date())));
    }

    @FXML
    private BorderPane chartsBorderPane;
    @FXML
    private VBox vbox;

    @FXML
    private void initialize(){
    }

    private void loadAddCharts(int month){
        BorderPane bp = new BorderPane();
        loadAreaChart(bp, loadAreaChartData(month));
        loadPieChart(bp, loadPieChartData(month));

        ChoiceBox<Integer> monthSelector = new ChoiceBox<>();
        monthSelector.getItems().addAll(
                1,2,3,4,5,6,7,8,9,10,11,12
        );
        monthSelector.setValue(month);
//        monthSelector.setValue(Integer.parseInt(new SimpleDateFormat("MM").format(new Date())));

        monthSelector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                loadAddCharts((int)newValue+1);
            }
        });

        HBox container = new HBox();
        container.setPadding(new Insets(10, 0, 10, 0));
        container.setAlignment(Pos.CENTER_RIGHT);
        container.getChildren().addAll(new Label("Choose month: "), monthSelector);
        bp.setTop(container);

        chartsBorderPane.setCenter(bp);
    }

    private HashMap<ObjectId,  Double> loadPieChartData(int month){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Document inventoryData = db.find(new Document("_id", inventoryId)).first();
        Iterable<Document> logs = (Iterable<Document>) inventoryData.get("logs");
//        HashMap<ObjectId, ArrayList<Document>> groupedLogs = new HashMap<ObjectId, ArrayList<Document>>();
        HashMap<ObjectId, Double> groupedLogs = new HashMap<>();
        for (Document log : logs) {
            long unixTime = log.getInteger("timestamp");
            Date datePre = new Date(unixTime * 1000);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM");
            if (log.get("capitalPerUnit") == null || Integer.parseInt(dateFormat1.format(datePre)) != month){
                continue;
            }

            else if (!groupedLogs.containsKey(log.get("itemId"))){
                groupedLogs.put((ObjectId) log.get("itemId"), Double.parseDouble("0"));
            }
            double revenue = (double) log.get("sales") - (log.getInteger("capitalPerUnit") * log.getDouble("unitsSold"));
            double totalRevenue = groupedLogs.get((ObjectId) log.get("itemId")) + revenue;

            groupedLogs.put((ObjectId) log.get("itemId"),  totalRevenue);
        }
        HashMap<String, Double> toPercentage  = new HashMap<>();
        double totalAll = 0;
        for (ObjectId key: groupedLogs.keySet()){
            totalAll += groupedLogs.get(key);
        }

        for (ObjectId key: groupedLogs.keySet()){

            groupedLogs.put(key, groupedLogs.get(key)/totalAll);
        }
        return groupedLogs;


    }
    private void loadPieChart(BorderPane parent, HashMap<ObjectId, Double> data){
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        Iterable<Document> items = (Iterable<Document>) new Database().getConnection("Inventories").find(new Document("_id", inventoryId)).first().get("items");
        for (ObjectId key : data.keySet()){
            String name = "no name";
            if (data.get(key) < 0){
                continue;
            }
            for (Document doc : items ){

                if (doc.get("itemId").equals(key)){
                    name = doc.getString("itemName");
                    break;
                }
            }

            pieChartData.add(new PieChart.Data(name, data.get(key)));
        }
        final PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Profit portion of each item");

        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(pieChart);
        pane.setBorder(Border.stroke(Paint.valueOf("gray")));
        pane.setStyle("-fx-border-radius: 10px; -fx-background-radius: 10px;-fx-border-color: gray");
        parent.setRight(pane);
    }

    private HashMap<ObjectId, HashMap<Integer, Double>> loadAreaChartData(int month){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Document inventoryData = db.find(new Document("_id", inventoryId)).first();
        Iterable<Document> logs = (Iterable<Document>) inventoryData.get("logs");
//        HashMap<ObjectId, ArrayList<Document>> groupedLogs = new HashMap<ObjectId, ArrayList<Document>>();
        HashMap<ObjectId, HashMap<Integer, Double>> groupedLogs = new HashMap<ObjectId, HashMap<Integer, Double>>();
        for (Document log : logs) {
            long unixTime = log.getInteger("timestamp");
            Date datePre = new Date(unixTime * 1000);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM");
            if (log.get("capitalPerUnit") == null || Integer.parseInt(dateFormat1.format(datePre)) != month){
                continue;
            }
            else if (!groupedLogs.containsKey(log.get("itemId"))){
                groupedLogs.put((ObjectId) log.get("itemId"), new HashMap<Integer, Double>());
            }
            Date date = new Date(Long.parseLong(String.valueOf(log.get("timestamp"))) * (long)1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd");
            int dateKey = Integer.parseInt(sdf.format(date));
            if (!groupedLogs.get((ObjectId) log.get("itemId")).containsKey(dateKey)){
                groupedLogs.get((ObjectId) log.get("itemId")).put(dateKey, Double.parseDouble(String.valueOf(0.0)));
            }
            double oldRev = groupedLogs.get((ObjectId) log.get("itemId")).get(dateKey);
            double revenue = (double)log.get("sales")  - (log.getDouble("unitsSold") * log.getInteger("capitalPerUnit"));
            groupedLogs.get((ObjectId) log.get("itemId")).put(dateKey, oldRev +  revenue);
        }
        return groupedLogs;
    }

    private void loadAreaChart(BorderPane parent, HashMap<ObjectId, HashMap<Integer, Double>> data){
        Set<ObjectId> keys = data.keySet();

        int longest= 0;
        for (HashMap<Integer, Double> datum: data.values()){
            for (int key : datum.keySet()){
                if (key > longest){
                    longest = key;
                }
            }
        }

        final NumberAxis xAxis = new NumberAxis(1, longest + 1, 1);
        xAxis.setLabel("Time");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Profits");
        final AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis, yAxis);
        ac.setTitle("Area Chart of Profits Overtime");

        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Document inventoryData = db.find(new Document("_id", inventoryId)).first();
        Iterable<Document> items = (Iterable<Document>) inventoryData.get("items");

        for (ObjectId key : keys) {
            XYChart.Series stack1 = new XYChart.Series();
            for (Document item : items) {
                if (item.get("itemId").equals(key)){
                    stack1.setName((String)item.get("itemName"));
                }
            }
            for (int singleEntry : data.get(key).keySet()) {
//                System.out.println(document.toString());
                double revenue = data.get(key).get(singleEntry);
                stack1.getData().add(new XYChart.Data(singleEntry, revenue));
            }
            ac.getData().addAll(stack1);
        }



        AnchorPane pane = new AnchorPane();
        pane.setBorder(Border.stroke(Paint.valueOf("gray")));
        pane.setStyle("-fx-border-radius: 10px; -fx-background-radius: 10px; -fx-border-color: gray");

        pane.getChildren().add(ac);
        parent.setCenter(pane);

//        XYChart.Series stack1 = new XYChart.Series();
//        stack1.setName("stack 1");
//
//        stack1.getData().add(new XYChart.Data( 1,4));
//        stack1.getData().add(new XYChart.Data( 2,40));
//        stack1.getData().add(new XYChart.Data( 3,32));
//        stack1.getData().add(new XYChart.Data( 4,8));
//        stack1.getData().add(new XYChart.Data( 5,21));
//
//        XYChart.Series stack2 = new XYChart.Series();
//        stack2.setName("stack 2");
//
//        stack2.getData().add(new XYChart.Data( 4,8));
//        stack2.getData().add(new XYChart.Data( 2,40));
//        stack2.getData().add(new XYChart.Data(10, 99));
//        stack2.getData().add(new XYChart.Data( 1,4));
//        stack2.getData().add(new XYChart.Data( 5,21));
//
//        ac.getData().addAll(stack1, stack2);
//        ac.setTitle("ransom");
//        ac.setLegendVisible(true);
//        ac.setLegendSide(Side.BOTTOM);
//
//        AnchorPane pane = new AnchorPane();
//        pane.setPrefSize(ac.getWidth(), ac.getHeight());
//
//        pane.getChildren().add(ac);
//        parent.setLeft(pane);
    }
}
