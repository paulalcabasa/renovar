package utils;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import main.Main;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utility {
    public static void ImageCircle(ImageView imageUser) {
        Rectangle clip = new Rectangle(
                imageUser.getFitWidth(), imageUser.getFitHeight()
        );
        clip.setArcWidth(imageUser.getFitWidth());
        clip.setArcHeight(imageUser.getFitHeight());
        imageUser.setClip(clip);
    }


    public static File BrowseImage(File file) {
        FileChooser fileChooser = new FileChooser();
        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        file = fileChooser.showOpenDialog(Main.primaryStage);
        if(file == null)
        {
            file = new File("");
        }
        else
        {
            file.getPath();
        }
        return file;
    }

    public static File BrowseEventPhoto(ImageView imageEventCover, File file) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        file = fileChooser.showOpenDialog(Main.primaryStage);


        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageEventCover.setImage(image);
            imageEventCover.setPreserveRatio(true);


            double newMeasure = (imageEventCover.getImage().getWidth() < imageEventCover.getImage().getHeight()) ? imageEventCover.getImage().getWidth() : imageEventCover.getImage().getHeight();
            double x = (imageEventCover.getImage().getWidth() - newMeasure) / 2;
            double y = (imageEventCover.getImage().getHeight() - newMeasure) / 2;

            Rectangle2D rect = new Rectangle2D(x, y, newMeasure, newMeasure);
            imageEventCover.setViewport(rect);
            imageEventCover.setFitWidth(imageEventCover.getFitWidth());
            imageEventCover.setFitHeight(imageEventCover.getFitHeight());
            imageEventCover.setSmooth(true);

        } catch (IOException ex) {

        }
        return file;
    }

    public static void ShowDialogs(StackPane root, List<JFXButton> controls, String header, Node body) {
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
