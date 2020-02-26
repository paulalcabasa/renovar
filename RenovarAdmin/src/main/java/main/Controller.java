package main;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import controller.DashboardController;
import datafxs.ExtendedAnimatedFlowContainer;
import io.datafx.controller.ViewController;
import io.datafx.controller.flow.Flow;
import io.datafx.controller.flow.FlowHandler;
import io.datafx.controller.flow.context.FXMLViewFlowContext;
import io.datafx.controller.flow.context.ViewFlowContext;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import sidemenus.SideMenuController;

import javax.annotation.PostConstruct;

import static io.datafx.controller.flow.container.ContainerAnimations.SWIPE_LEFT;

@ViewController(value = "/Main.fxml", title = "Renovar")
public final class Controller {

    @FXMLViewFlowContext
    static public ViewFlowContext context;

    @FXML
    public static StackPane root;

    @FXML
    private StackPane titleBurgerContainer;
    @FXML
    private JFXHamburger titleBurger;

    @FXML
    private StackPane optionsBurger;
    @FXML
    private JFXRippler optionsRippler;
    @FXML
    public static JFXDrawer drawer;

    private JFXPopup toolbarPopup;

    @FXML
    public static Label lblTitle;

    @FXML
    public static TextField globalSearch;

    @FXML
    private SVGPath circleRedCross;

    @PostConstruct
    public void init() throws Exception {

        drawer.setOnDrawerOpening(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(1);
            animation.play();

//            new SlideInLeft(SideMenuController.navDashboard).play();
//            new SlideInLeft(SideMenuController.navRequests).play();
//            new SlideInLeft(SideMenuController.navArticles).play();
//            new SlideInLeft(SideMenuController.navBT).play();
//            new SlideInLeft(SideMenuController.navDonors).play();
//            new SlideInLeft(SideMenuController.navEmployees).play();
//            new SlideInLeft(SideMenuController.navEvents).play();
//            new SlideInLeft(SideMenuController.navInventory).play();
//            new SlideInLeft(SideMenuController.navTransaction).play();
        });

        drawer.setOnDrawerClosing(e -> {
            final Transition animation = titleBurger.getAnimation();
            animation.setRate(-1);
            animation.play();
        });

        titleBurgerContainer.setOnMouseClicked(e -> {
            if (drawer.isClosed() || drawer.isClosing()) {
                drawer.open();
            } else {
                drawer.close();
            }
        });

        context = new ViewFlowContext();
        Flow innerFlow = new Flow(DashboardController.class);

        final FlowHandler flowHandler = innerFlow.createHandler(context);
        context.register("ContentFlowHandler", flowHandler);
        context.register("ContentFlow", innerFlow);
        final Duration containerAnimationDuration = Duration.millis(320);
        drawer.setContent(flowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration, SWIPE_LEFT)));
        context.register("ContentPane", drawer.getContent().get(0));

        Flow sideMenuFlow = new Flow(SideMenuController.class);
        final FlowHandler sideMenuFlowHandler = sideMenuFlow.createHandler(context);
        drawer.setSidePane(sideMenuFlowHandler.start(new ExtendedAnimatedFlowContainer(containerAnimationDuration,
                SWIPE_LEFT)));
    }
}
