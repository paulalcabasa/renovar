package controller;

import Models.ItemOrder;
import Models.JSONReader;
import Models.Orders;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.html.HTML;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

@ViewController(value = "/fxml/Orders.fxml", title = "Renovar")
public class OrdersController implements Initializable {

    private Service<Void> service;
    private ObservableList<Orders> ordersList;
    private JFXSnackbar snackbar;

    @FXML
    private HTMLEditor orderText;
    @FXML
    private StackPane root;
    @FXML
    private TableView<Orders> tableOrders;
    @FXML
    private TableView<ItemOrder> tableItemOrders;
    @FXML
    private TableColumn<String,Orders> colId,colEmail,colFirstname,colLastname,
            colStreet1,colStreet2,colCountry,colState,colZipCode,colDate,colTotal,colStatus,colCity;
    @FXML
    private TableColumn<Integer,ItemOrder> colQuantity;
    @FXML
    private TableColumn<Double,ItemOrder> colGrandPrice;
    @FXML
    private TableColumn<String,ItemOrder> colProduct;

    @FXML
    private JFXProgressBar progressBar;

    @FXML
    private ComboBox<String> cboStatus;


    String jsonString = "";

    private void initCol()
    {
        //tableOrders
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colLastname.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        colStreet1.setCellValueFactory(new PropertyValueFactory<>("street1"));
        colStreet2.setCellValueFactory(new PropertyValueFactory<>("street2"));
        colCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));
        colZipCode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));

        //tableItemOrders
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colGrandPrice.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("item_name"));

        tableOrders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Orders>() {
            @Override
            public void changed(ObservableValue<? extends Orders> observable, Orders oldValue, Orders newValue) {
                ObservableList<ItemOrder> itemOrders = FXCollections.observableArrayList();
                JSONObject jsonOrders = new JSONObject("{ \"dataArray\" : "+newValue.getOrders()+"}");
                JSONArray orderArray = jsonOrders.getJSONArray("dataArray");

                orderText.setHtmlText(newValue.getHTMLforEmail());

                for (int index = 0 ; index < orderArray.length(); index++) {
                    JSONObject orderObject = orderArray.getJSONObject(index);
                    int quantity = orderObject.getInt("quantity");
                    String item_name = orderObject.getString("item_name");
                    String image_url = orderObject.getString("image_url");
                    double total_price = orderObject.getDouble("total_price");

                    itemOrders.add(new ItemOrder(quantity,item_name,image_url,total_price));
                }

                tableItemOrders.setItems(itemOrders);

                System.out.println(newValue.getOrders());


                ContextMenu contextMenu = new ContextMenu();

                Menu parentMenu = new Menu("Mark as");
                MenuItem childMenuItem1 = new MenuItem("Pending");
                MenuItem childMenuItem2 = new MenuItem("Complete");
                MenuItem childMenuItem3 = new MenuItem("Reject");

                childMenuItem1.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            if(updateOrder(newValue.getId(),"0").contains("Record updated successfully")) {
                                tableOrders.getSelectionModel().getSelectedItem().setStatus("0");
                                tableOrders.refresh();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            snackbar.show("An error occured. Please make sure you have an internet connection.",3000);
                        }
                    }
                });

                childMenuItem2.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            if(updateOrder(newValue.getId(),"1").contains("Record updated successfully")) {
                                tableOrders.getSelectionModel().getSelectedItem().setStatus("1");
                                tableOrders.refresh();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            snackbar.show("An error occured. Please make sure you have an internet connection.",3000);
                        }
                    }
                });

                childMenuItem3.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            if(updateOrder(newValue.getId(),"2").contains("Record updated successfully")) {
                                tableOrders.getSelectionModel().getSelectedItem().setStatus("2");
                                tableOrders.refresh();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            snackbar.show("An error occured. Please make sure you have an internet connection.",3000);
                        }
                    }
                });

                parentMenu.getItems().addAll(childMenuItem1, childMenuItem2,childMenuItem3);

                MenuItem copy = new MenuItem("Copy Order Details");


                copy.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Toolkit.getDefaultToolkit()
                                .getSystemClipboard()
                                .setContents(
                                        new StringSelection(newValue.copyFullDetails()),
                                        null
                                );
                        contextMenu.hide();
                    }
                });

                contextMenu.getItems().addAll(parentMenu,copy);

                tableOrders.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        contextMenu.hide();
                        if(t.getButton() == MouseButton.SECONDARY) {
                            contextMenu.show(tableOrders, t.getScreenX(), t.getScreenY());
                        }
                    }
                });

            }
        });
    }



    public String updateOrder(String id,String status) throws IOException {

        String link = "http://renovar.health/renovarmobile/update_orderstatus.php";
        String data = URLEncoder.encode("id", "UTF-8") + "=" +
                URLEncoder.encode(id, "UTF-8");
        data += "&" + URLEncoder.encode("status", "UTF-8") + "=" +
                URLEncoder.encode(status, "UTF-8");

        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

        wr.write(data);
        wr.flush();

        BufferedReader reader = new BufferedReader(new
                InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            break;
        }
        snackbar.show(sb.toString(),3000);
        return sb.toString();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initCol();

        //ComboBox
        cboStatus.getItems().add("Pending");
        cboStatus.getItems().add("Completed");
        cboStatus.getItems().add("Rejected");

        snackbar = new JFXSnackbar(root);

        loadOrders("");

        cboStatus.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                tableItemOrders.getItems().clear();
                progressBar.setVisible(true);
                loadOrders(String.valueOf(cboStatus.getSelectionModel().getSelectedIndex()));
            }
        });
    }

    public void loadOrders(String status)
    {
        ordersList = FXCollections.observableArrayList();

        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            jsonString = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_orders.php?status="+status);
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
                JSONObject jsonObj = new JSONObject("{ \"dataArray\" : "+jsonString+"}");

                JSONArray c = jsonObj.getJSONArray("dataArray");
                for (int i = 0 ; i < c.length(); i++) {
                    JSONObject obj = c.getJSONObject(i);
                    String id = obj.getString("id");
                    String email = obj.getString("email");
                    String firstname = obj.getString("firstname");
                    String lastname = obj.getString("lastname");
                    String street1 = obj.getString("street1");
                    String street2 = obj.getString("street2");
                    String country = obj.getString("country");
                    String city = obj.getString("city");
                    String state = obj.getString("state");
                    String zipcode = obj.getString("zipcode");
                    String orders = obj.getString("orders");
                    int status = obj.getInt("status");

                    long timeMillis = Long.parseLong(obj.getString("id"));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(timeMillis);

                    JSONObject jsonOrders = new JSONObject("{ \"dataArray\" : "+orders+"}");
                    JSONArray orderArray = jsonOrders.getJSONArray("dataArray");

                    double totalPrice = 0;

                    for (int index = 0 ; index < orderArray.length(); index++) {
                        JSONObject orderObject = orderArray.getJSONObject(index);
                        totalPrice += orderObject.getDouble("total_price");
                    }

                    ordersList.add(new Orders(id, email, firstname, lastname, street1,
                            street2, country, city,state, zipcode, orders,calendar.getTime().toGMTString(),
                            ("US$"+totalPrice),String.valueOf(status)));
                }


                tableOrders.setItems(ordersList);
                progressBar.setVisible(false);
            }
        });


        service.restart();
    }

}
