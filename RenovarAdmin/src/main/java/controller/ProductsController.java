package controller;

import Models.*;
import Models.Collection;
import animatefx.animation.ZoomIn;
import com.jfoenix.controls.*;
import io.datafx.controller.ViewController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.FTPUploader;
import utils.Utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@ViewController(value = "/fxml/Products.fxml", title = "Renovar")
public class ProductsController implements Initializable {

    private Service<Void> service;
    private ObservableList<ProductsView> productsList;
    private ObservableList<Category> categoryList;
    private ObservableList<Collection> collectionsList;


    @FXML
    private JFXSnackbar snackbar;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane masonryPane;
    @FXML
    private StackPane root;

    String jsonString,jsonCategories,jsonCollections;
    String fileExtension = "";
    File file = new File("");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productsList = FXCollections.observableArrayList();
        snackbar = new JFXSnackbar(root);

        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            jsonString = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_products_view.php");
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
                    String id = String.valueOf(obj.getInt("id"));
                    String name = obj.getString("name");;
                    String image_url = obj.getString("image_url");;
                    String description = obj.getString("description");;
                    double price = obj.getDouble("price");;
                    int interval = obj.getInt("interval");;
                    String category_id = obj.getString("category");;
                    String camera_message = obj.getString("cam_message");;
                    String collection_id = obj.getString("collection");
                    int category_ids = obj.getInt("category_id");
                    int collection_ids = obj.getInt("collection_id");

                    productsList.add(new ProductsView(id,name,image_url,description,price,interval,category_id,camera_message,collection_id,category_ids,collection_ids));

                    try {
                        loadProducts(productsList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        service.restart();
    }


    public void loadProducts(ObservableList<ProductsView> products) throws IOException {
        ArrayList<Node> children = new ArrayList<>();
        masonryPane.getChildren().clear();

        for (ProductsView product : products) {
            StackPane body = FXMLLoader.load(getClass().getClassLoader().getResource("views/item_product.fxml"));

            Label lblProductName = (Label) body.lookup("#lblProductName");
            Label lblCategory = (Label) body.lookup("#lblCategory");
            Label lblPrice = (Label) body.lookup("#lblPrice");
            ImageView imageProduct = (ImageView) body.lookup("#imageProduct");
            JFXSpinner progressIndicator = (JFXSpinner) body.lookup("#progressIndicator");




            service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            if(!product.getImage_url().equals("")) {
                                Image emp_image = new Image(product.getImage_url(), false);
                                imageProduct.setImage(emp_image);
                            }
                            return null;
                        }
                    };
                }
            };

            service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent event) {
                    progressIndicator.setVisible(false);
                }
            });

            service.restart();


            lblProductName.setText(product.getName());
            lblCategory.setText(product.getCategory());
            lblPrice.setText("US$"+product.getPrice());

            VBox content = new VBox();
            content.getChildren().addAll(body);
            children.add(content);

            content.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        openProductDialog(product);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            new ZoomIn(content).play();
        }

        masonryPane.getChildren().addAll(children);
        Platform.runLater(() -> scrollPane.requestLayout());

        JFXScrollPane.smoothScrolling(scrollPane);
    }

    public void loadCategories(ComboBox<Category> comboBox,int id)
    {
        categoryList = FXCollections.observableArrayList();

        comboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category object) {
                return object.getCategory();
            }

            @Override
            public Category fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            jsonCategories = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_categories.php");
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
                JSONObject jsonObj = new JSONObject("{ \"dataArray\" : " + jsonCategories + "}");

                JSONArray c = jsonObj.getJSONArray("dataArray");
                for (int i = 0; i < c.length(); i++) {
                    JSONObject obj = c.getJSONObject(i);

                    categoryList.add(new Category(
                            obj.getInt("id"),
                            obj.getString("category"),
                            obj.getString("image_url")
                    ));
                }

                comboBox.setItems(categoryList);

                if(id != 0) {
                    comboBox.getItems().forEach(comboData1 ->
                    {
                        if (comboData1.getId() == id) {
                            comboBox.getSelectionModel().select(comboData1);
                        }
                    });
                }
            }
        });


        service.restart();
    }

    public void loadCollections(ComboBox<Collection> comboBox,int id,int category_id)
    {
        collectionsList = FXCollections.observableArrayList();

        comboBox.setConverter(new StringConverter<Collection>() {
            @Override
            public String toString(Collection object) {
                return object.getCategory();
            }

            @Override
            public Collection fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            jsonCollections = JSONReader.readJSONTextFromUrl("http://renovar.health/renovarmobile/get_collections.php?category_id="+id);
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
                JSONObject jsonObj = new JSONObject("{ \"dataArray\" : "+jsonCollections+"}");

                JSONArray c = jsonObj.getJSONArray("dataArray");
                for (int i = 0 ; i < c.length(); i++) {
                    JSONObject obj = c.getJSONObject(i);

                    collectionsList.add(new Collection(
                            obj.getInt("id"),
                            obj.getString("collection"),
                            obj.getString("image_url"),
                            obj.getInt("category_id")
                    ));
                }

                comboBox.setItems(collectionsList);

                if(category_id != 0) {
                    comboBox.getItems().forEach(comboData1 ->
                    {
                        if (comboData1.getId() == category_id) {
                            comboBox.getSelectionModel().select(comboData1);
                        }
                    });
                }
            }
        });


        service.restart();
    }

    public void openProductDialog(ProductsView productview) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/dialogs/add_product.fxml"));
        ImageView imageProduct = (ImageView) node.lookup("#imageProduct");
        JFXButton btnPositive = new JFXButton("Update");
        JFXButton btnDelete = new JFXButton("Delete");
        TextField txtProductName = (TextField) node.lookup("#txtProductName");
        TextArea txtProductDesc = (TextArea) node.lookup("#txtProductDesc");
        TextField txtProductPrice = (TextField) node.lookup("#txtProductPrice");
        TextField txtCameraMessage = (TextField) node.lookup("#txtCameraMessage");
        Slider daysInterval = (Slider) node.lookup("#daysInterval");
        ComboBox<Category> cboCategory = (ComboBox) node.lookup("#cboCategory");
        ComboBox<Collection> cboCollection = (ComboBox) node.lookup("#cboCollection");

        Image image = new Image(productview.getImage_url(), true);
        imageProduct.setImage(image);

        txtProductName.setText(productview.getName());
        txtProductDesc.setText(productview.getDescription());
        txtProductPrice.setText(String.valueOf(productview.getPrice()));
        txtCameraMessage.setText(String.valueOf(productview.getCamera_message()));
        daysInterval.setValue(productview.getInterval());

        loadCategories(cboCategory,productview.getCategory_id());
        loadCollections(cboCollection,productview.getCategory_id(),productview.getCollection_id());

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Product product = new Product();
                    product.setId(productview.getId());
                    snackbar.show(product.deleteProduct(), 3000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnPositive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Product product = new Product();
                    product.setCamera_message(txtCameraMessage.getText().toString());
                    product.setDescription(txtProductDesc.getText().toString());
                    product.setInterval((int) daysInterval.getValue());
                    product.setName(txtProductName.getText().toString());
                    product.setPrice(Double.parseDouble(txtProductPrice.getText().toString()));
                    product.setCategory_id(cboCategory.getSelectionModel().getSelectedItem().getId());
                    product.setCollection_id(cboCollection.getSelectionModel().getSelectedItem().getId());
                    product.setImage_url(productview.getImage_url());
                    product.setId(productview.getId());

                    snackbar.show(product.updateProduct(), 3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        showDialogs(root, Arrays.asList(btnDelete,btnPositive,new JFXButton("Cancel")), "Update Product", node);
    }


    public void onNewProductClicked(ActionEvent event) throws IOException {
        Node node = FXMLLoader.load(getClass().getResource("/dialogs/add_product.fxml"));

        ImageView imageProduct = (ImageView) node.lookup("#imageProduct");
        JFXButton btnPositive = new JFXButton("Add");
        TextField txtProductName = (TextField) node.lookup("#txtProductName");
        TextArea txtProductDesc = (TextArea) node.lookup("#txtProductDesc");
        TextField txtProductPrice = (TextField) node.lookup("#txtProductPrice");
        TextField txtCameraMessage = (TextField) node.lookup("#txtCameraMessage");
        Slider daysInterval = (Slider) node.lookup("#daysInterval");
        ComboBox<Category> cboCategory = (ComboBox) node.lookup("#cboCategory");
        ComboBox<Collection> cboCollection = (ComboBox) node.lookup("#cboCollection");
        loadCategories(cboCategory,0);

        cboCategory.valueProperty().addListener(new ChangeListener<Category>() {
            @Override
            public void changed(ObservableValue<? extends Category> observable, Category oldValue, Category newValue) {
                loadCollections(cboCollection,newValue.getId(),0);
            }
        });

        imageProduct.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    file = Utility.BrowseImage(file);
                    if(!file.getPath().equals("")) {
                        String fileName = file.getName();
                        fileExtension = "." + fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
                        Image image = new Image(file.toURI().toString(), true);
                        imageProduct.setImage(image);
                        System.out.println(file.getPath());
                    }
                    else
                    {
                        System.out.println(file.getPath());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnPositive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!file.getPath().equals("")) {
                    try {
                        Product product = new Product();
                        product.setCamera_message(txtCameraMessage.getText().toString());
                        product.setDescription(txtProductDesc.getText().toString());
                        product.setInterval((int)daysInterval.getValue());
                        product.setName(txtProductName.getText().toString());
                        product.setPrice(Double.parseDouble(txtProductPrice.getText().toString()));
                        product.setCategory_id(cboCategory.getSelectionModel().getSelectedItem().getId());
                        product.setCollection_id(cboCollection.getSelectionModel().getSelectedItem().getId());

                        long file_id = Calendar.getInstance().getTimeInMillis();
                        FTPUploader ftpUploader = new FTPUploader("renovar.health", "renovarmobile@renovar.health", "Renovar1234");
                        ftpUploader.uploadFile(file, file_id + ".jpg", "");
                        product.setImage_url("http://renovar.health/renovarmobile/products/" +file_id+".jpg");
                        ftpUploader.disconnect();

                        snackbar.show(product.insertProduct(),3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        showDialogs(root, Arrays.asList(btnPositive,new JFXButton("Cancel")),"New Product",node);
    }


    public void showDialogs(StackPane root, List<JFXButton> controls, String header, Node body) {
        if (controls.isEmpty()) {
            controls.add(new JFXButton("Done"));
        }
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.CENTER);

        controls.forEach(controlButton -> {
            controlButton.getStyleClass().add("dialog-button");
            controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close();
            });
        });

        Label lblHeader = new Label(header);
        lblHeader.setStyle("-fx-font-size: 16; -fx-font-family: 'Product Sans'");
        dialogLayout.setHeading(lblHeader);
        dialogLayout.setBody(body);
        dialogLayout.setActions(controls);
        dialog.show();
    }
}
