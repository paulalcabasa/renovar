package controller;

import Models.JSONReader;
import Models.Product;
import com.sun.tools.javac.util.StringUtils;
import io.datafx.controller.ViewController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;

@ViewController(value = "/fxml/Dashboard.fxml", title = "Renovar")
public class DashboardController implements Initializable {

    private Service<Void> service,sAmount,sPending;
    private String pending,completed,pendingString,rejected,allOrders,productCount,completedString;

    @FXML
    private Label lblCountPending,lblCompletedOrder,lblPendingAmount,lblCompleteAmount,lblRejected,lblTotalOrders,lblProductCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getCounts();
        loadOrdersPending();
        loadOrdersCompleted();
    }

    public void getCounts()
    {
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            pending = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_counts.php?status=0");
                            completed = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_counts.php?status=1");
                            rejected = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_counts.php?status=2");
                            allOrders = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_counts.php?status=");
                            productCount = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_product_count.php");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };


        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                lblCountPending.setText(pending);
                lblCompletedOrder.setText(completed);
                lblRejected.setText(rejected);
                lblTotalOrders.setText(allOrders);
                lblProductCount.setText(productCount);
            }
        });


        service.restart();
    }


    public void loadOrdersPending()
    {
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            pendingString = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_orders.php?status=0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };

        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                JSONObject jsonObj = new JSONObject("{ \"dataArray\" : "+pendingString+"}");

                JSONArray c = jsonObj.getJSONArray("dataArray");

                double grand_total = 0;
                for (int i = 0 ; i < c.length(); i++) {
                    JSONObject obj = c.getJSONObject(i);
                    String orders = obj.getString("orders");
                    int status = obj.getInt("status");


                    JSONObject jsonOrders = new JSONObject("{ \"dataArray\" : "+orders+"}");
                    JSONArray orderArray = jsonOrders.getJSONArray("dataArray");

                    double totalPrice = 0;

                    for (int index = 0 ; index < orderArray.length(); index++) {
                        JSONObject orderObject = orderArray.getJSONObject(index);
                        totalPrice += orderObject.getDouble("total_price");
                    }

                    grand_total += totalPrice;
                }
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                String currency = format.format(grand_total);
                lblPendingAmount.setText("$US "+currency);
            }
        });


        service.restart();
    }


    public void loadOrdersCompleted()
    {
        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            completedString = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_orders.php?status=1");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
            }
        };

        service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                JSONObject jsonObj = new JSONObject("{ \"dataArray\" : "+completedString+"}");

                JSONArray c = jsonObj.getJSONArray("dataArray");

                double grand_total = 0;
                for (int i = 0 ; i < c.length(); i++) {
                    JSONObject obj = c.getJSONObject(i);
                    String orders = obj.getString("orders");
                    int status = obj.getInt("status");


                    JSONObject jsonOrders = new JSONObject("{ \"dataArray\" : "+orders+"}");
                    JSONArray orderArray = jsonOrders.getJSONArray("dataArray");

                    double totalPrice = 0;

                    for (int index = 0 ; index < orderArray.length(); index++) {
                        JSONObject orderObject = orderArray.getJSONObject(index);
                        totalPrice += orderObject.getDouble("total_price");
                    }

                    grand_total += totalPrice;
                }
                NumberFormat format = NumberFormat.getInstance(Locale.US);
                String currency = format.format(grand_total);
                lblCompleteAmount.setText("$US "+currency);
            }
        });


        service.restart();
    }

}
