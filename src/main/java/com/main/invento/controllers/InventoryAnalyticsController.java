package com.main.invento.controllers;

import com.main.invento.utilityClasses.Database;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Size;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;

public class InventoryAnalyticsController {

    private ObjectId inventoryId;

    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
        loadAddCharts();
    }

    @FXML
    private BorderPane chartsBorderPane;
    @FXML
    private VBox vbox;

    @FXML
    private void initialize(){
    }

    private void loadAddCharts(){
        BorderPane bp = new BorderPane();
        loadAreaChart(bp, loadAreaChartData());
        loadPieChart(bp);
        chartsBorderPane.setCenter(bp);
    }

    private void loadPieChart(BorderPane parent){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        final PieChart pieChart = new PieChart(pieChartData);

        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll(pieChart);
        parent.setRight(pane);
    }

    private HashMap<ObjectId, ArrayList<Document>> loadAreaChartData(){
        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Document inventoryData = db.find(new Document("_id", inventoryId)).first();
        Iterable<Document> logs = (Iterable<Document>) inventoryData.get("logs");
        HashMap<ObjectId, ArrayList<Document>> groupedLogs = new HashMap<ObjectId, ArrayList<Document>>();
        for (Document log : logs) {
            if (log.get("capitalPerUnit") == null){
                continue;
            }
            else if (!groupedLogs.containsKey(log.get("itemId"))){
                groupedLogs.put((ObjectId) log.get("itemId"), new ArrayList<Document>());
            }
            groupedLogs.get((ObjectId)log.get("itemId")).add(log);
        }
//            Bson filter = Aggregates.match(Filters.and(Filters.eq("_id", inventoryId), Filters.eq("logs.$.itemId", log.get("itemId"))));
//            AggregateIterable<Document> aggregated = db.aggregate(Arrays.asList(filter));
//            for (Document docu : aggregated){
//                res.add(docu);
//            }
        return groupedLogs;
    }

    private void loadAreaChart(BorderPane parent, HashMap<ObjectId, ArrayList<Document>> data){
//        System.out.println(data.toString());
        int size = data.size();

        Set<ObjectId> keys = data.keySet();

        int longest= 0;
        for (ArrayList<Document> value : data.values()) {
            if (value.size() > longest) {
                longest = value.size();
            }
        }

        final NumberAxis xAxis = new NumberAxis(1, longest, 1);
        xAxis.setLabel("Time");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales");
        final AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis, yAxis);

        MongoCollection<Document> db = new Database().getConnection("Inventories");
        Document inventoryData = db.find(new Document("_id", inventoryId)).first();
        Iterable<Document> items = (Iterable<Document>) inventoryData.get("items");

        for (ObjectId key : keys) {
            XYChart.Series stack1 = new XYChart.Series();
            int counter = 1;
            for (Document item : items) {
                if (item.get("itemId").equals(key)){
                    stack1.setName((String)item.get("itemName"));
                }
            }
            for (Document document : data.get(key)) {
//                System.out.println(document.toString());
                double revenue = Double.valueOf(String.valueOf(document.get("sales"))) - (Double.valueOf(String.valueOf(document.get("unitsSold"))) * Double.valueOf(String.valueOf(document.get("capitalPerUnit"))));
                stack1.getData().add(new XYChart.Data(counter, revenue));
                counter ++;
            }
            ac.getData().addAll(stack1);
        }



        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(ac.getWidth(), ac.getHeight());

        pane.getChildren().add(ac);
        parent.setLeft(pane);

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
