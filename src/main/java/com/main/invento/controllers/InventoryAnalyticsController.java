package com.main.invento.controllers;

import javafx.css.Size;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.bson.types.ObjectId;

public class InventoryAnalyticsController {

    private ObjectId inventoryId;

    public void setInventoryId(ObjectId inventoryId) {
        this.inventoryId = inventoryId;
    }

    @FXML
    private BorderPane chartsBorderPane;
    @FXML
    private VBox vbox;

    @FXML
    private void initialize(){
        loadAddCharts();
    }

    private void loadAddCharts(){
        BorderPane bp = new BorderPane();
        loadAreaChart(bp);
        chartsBorderPane.setCenter(bp);
    }

    private void loadPieChart(){

    }

    private void loadAreaChart(BorderPane parent){
        final NumberAxis xAxis = new NumberAxis(0, 10, 1);
        xAxis.setLabel("Time");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales");

        XYChart.Series stack1 = new XYChart.Series();
        stack1.setName("stack 1");

        stack1.getData().add(new XYChart.Data( 1,4));
        stack1.getData().add(new XYChart.Data( 2,40));
        stack1.getData().add(new XYChart.Data( 3,32));
        stack1.getData().add(new XYChart.Data( 4,8));
        stack1.getData().add(new XYChart.Data( 5,21));

        XYChart.Series stack2 = new XYChart.Series();
        stack2.setName("stack 2");

        stack2.getData().add(new XYChart.Data( 4,8));
        stack2.getData().add(new XYChart.Data( 2,40));
        stack2.getData().add(new XYChart.Data(10, 99));
        stack2.getData().add(new XYChart.Data( 1,4));
        stack2.getData().add(new XYChart.Data( 5,21));

        final AreaChart<Number, Number> ac = new AreaChart<Number, Number>(xAxis, yAxis);
        ac.getData().addAll(stack1, stack2);
        ac.setTitle("ransom");
        ac.setLegendVisible(true);
        ac.setLegendSide(Side.BOTTOM);

        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(ac.getWidth(), ac.getHeight());

        pane.getChildren().add(ac);
        parent.setCenter(pane);
    }
}
