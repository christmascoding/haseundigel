package src.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class MainWindow extends Application {

    public int guiFieldWidth = 1280;
    public int guiFieldHeight = 720;
    GUICoordinateTable coordinateTable = new GUICoordinateTable();
    private List<Coordinate> coordinates = coordinateTable.getCoordinateTable();//coord list from other class
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hase und Igel");

        //spielbrettle
        ImageView boardImage = new ImageView(new Image("file:src/assets/spielbrett.jpg"));
        boardImage.setPreserveRatio(true);
        boardImage.setFitWidth(guiFieldWidth);
        boardImage.setFitHeight(guiFieldHeight);

        // center board (THIS DONT WORK)
        double xOffset = (guiFieldWidth - boardImage.getFitWidth()) / 2;
        double yOffset = (guiFieldHeight - boardImage.getFitHeight()) / 2;

        //pane containing the manually positioned fields n shit
        Pane boardPane = new Pane();
        boardPane.getChildren().add(boardImage);
        boardImage.setLayoutX(xOffset);
        boardImage.setLayoutY(yOffset);

        displayFields(boardPane, boardImage);

        // player UI on lower screen
        HBox playerUI = new HBox(20);
        playerUI.setAlignment(Pos.CENTER);
        playerUI.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;");

        Label resourceLabel = new Label("Ressourcen: 5 Karotten, 3 Salat");
        Button moveForwardBtn = new Button("Vorwärts");
        Button moveBackwardBtn = new Button("Rückwärts");

        playerUI.getChildren().addAll(resourceLabel, moveForwardBtn, moveBackwardBtn);

        BorderPane root = new BorderPane();
        root.setCenter(boardPane);
        root.setBottom(playerUI);

        Scene scene = new Scene(root, 1920, 1080);

        //resize update thingy if it fails -> probs dont need this
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateFieldPositions(boardPane, boardImage));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateFieldPositions(boardPane, boardImage));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //display field
    private void displayFields(Pane pane, ImageView boardImage) {
        double imgWidth = boardImage.getFitWidth();
        double imgHeight = boardImage.getFitHeight();
        // for each field, SET IT!
        for (Coordinate coordinate : coordinates) {
            double relX = (double) coordinate.x() / 2892;
            double relY = (double) coordinate.y() / 2184;

            Rectangle field = new Rectangle(20, 20);  // px size for field here, will need readjusting
            field.setFill(Color.TRANSPARENT);
            field.setStroke(Color.BLACK); //black stroke

            field.setUserData(new double[]{relX, relY});  //save relative position
            pane.getChildren().add(field);
        }

        updateFieldPositions(pane, boardImage);
    }

    // call when update fields
    private void updateFieldPositions(Pane pane, ImageView boardImage) {
        double imgWidth = boardImage.getFitWidth();
        double imgHeight = boardImage.getFitHeight();

        // center field hopefully
        double offsetX = (guiFieldWidth - imgWidth) / 2;
        double offsetY = (guiFieldHeight - imgHeight) / 2;

        boardImage.setLayoutX(offsetX);
        boardImage.setLayoutY(offsetY);

        // relative coords calculation (adjusting coordinates from the guicoordtable)
        for (var node : pane.getChildren()) {
            if (node instanceof Rectangle field) {
                double[] relPos = (double[]) field.getUserData();
                field.setLayoutX(offsetX + imgWidth * relPos[0] - field.getWidth() / 2);
                field.setLayoutY(offsetY + imgHeight * relPos[1] - field.getHeight() / 2);
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void switchToNextPlayer() {
        // Hier rein was passiert, wenn der nächste Spieler am Zug ist
    }
}
