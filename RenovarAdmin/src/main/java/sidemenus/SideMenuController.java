package sidemenus;

import com.jfoenix.controls.JFXButton;
import controller.DashboardController;
import controller.OrdersController;
import controller.ProductsController;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowException;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import io.datafx.controller.util.VetoException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import main.Controller;


import javax.annotation.PostConstruct;
import java.util.Objects;

@ViewController(value = "/NavMenu.fxml", title = "Renovar")
public class SideMenuController {

    @FXMLViewFlowContext
    private ViewFlowContext context;

    @FXML
    public static JFXButton navDashboard,navOrders,navProducts;

    @FXML
    public VBox navigationList;

    @FXML
    private Circle userImage;

    @FXML
    private Label lblUsername,lblRole;

    @PostConstruct
    public void init() {
        Objects.requireNonNull(context, "context");
        FlowHandler contentFlowHandler = (FlowHandler) context.getRegisteredObject("ContentFlowHandler");

        setNavButton(navDashboard,contentFlowHandler);
        setNavButton(navOrders,contentFlowHandler);
        setNavButton(navProducts,contentFlowHandler);
//        setNavButton(navEmployees,contentFlowHandler);

        Flow contentFlow = (Flow) context.getRegisteredObject("ContentFlow");
        bindNodeToController(navDashboard, DashboardController.class, contentFlow, contentFlowHandler);
        bindNodeToController(navOrders, OrdersController.class, contentFlow, contentFlowHandler);
        bindNodeToController(navProducts, ProductsController.class, contentFlow, contentFlowHandler);

        navDashboard.fire();
//        initializeControlsByRoles();
    }

    private void bindNodeToController(Node node, Class<?> controllerClass, Flow flow, FlowHandler flowHandler) {
        flow.withGlobalLink(node.getId(), controllerClass);
    }



    public void resetButtons()
    {
        String style = "-fx-background-color : TRANSPARENT; -fx-background-radius: 0 20 20 0; -fx-text-fill : #ffa875;";
        String graphicStyle = "-fx-fill: #ffa875;";

        navDashboard.setStyle(style);
        navOrders.setStyle(style);
        navProducts.setStyle(style);

        navDashboard.getGraphic().setStyle(graphicStyle);
        navOrders.getGraphic().setStyle(graphicStyle);
        navProducts.getGraphic().setStyle(graphicStyle);
    }

    public void selectButton(JFXButton button)
    {
        button.setStyle("-fx-background-color: #e3f2fd; -fx-background-radius: 0 20 20 0; -fx-text-fill : #1565c0");
        button.getGraphic().setStyle("-fx-fill: #1565c0");
        Controller.lblTitle.setText(button.getText());
        Controller.globalSearch.setPromptText("Search "+button.getText());
    }

    public void setNavButton(JFXButton navButton, FlowHandler flowHandler){
        navButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    flowHandler.handle(navButton.getId());
                    resetButtons();
                    selectButton(navButton);
                    Controller.globalSearch.setText("");
//                    Controller.drawer.close();
                } catch (VetoException exc) {
                    exc.printStackTrace();
                } catch (FlowException exc) {
                    exc.printStackTrace();
                }
            }
        });
    }
}
