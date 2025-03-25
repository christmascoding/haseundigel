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
    private List<Coordinate> coordinates = coordinateTable.getCoordinateTable();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hase und Igel");

        // Spielfeld-Bild
        ImageView boardImage = new ImageView(new Image("file:src/assets/spielbrett.jpg"));
        boardImage.setPreserveRatio(true);
        boardImage.setFitWidth(guiFieldWidth);
        boardImage.setFitHeight(guiFieldHeight);

        // Spielfeld-Container (Pane für exakte Positionierung)
        Pane boardPane = new Pane();
        boardPane.setMinSize(guiFieldWidth, guiFieldHeight);
        boardPane.getChildren().add(boardImage);

        // Felder-Container (bleibt genau über dem Bild)
        Pane fieldPane = new Pane();
        fieldPane.setMinSize(guiFieldWidth, guiFieldHeight);
        boardPane.getChildren().add(fieldPane);

        displayFields(fieldPane, boardImage);

        // UI unten
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

        // Listener für Fenstergrößenänderung
        scene.widthProperty().addListener((obs, oldVal, newVal) -> updateFieldPositions(fieldPane, boardImage));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> updateFieldPositions(fieldPane, boardImage));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void displayFields(Pane pane, ImageView boardImage) {
        for (Coordinate coordinate : coordinates) {
            double relX = (double) coordinate.x() / 2892;
            double relY = (double) coordinate.y() / 2184;

            Rectangle field = new Rectangle(20, 20);
            field.setFill(Color.TRANSPARENT);
            field.setStroke(Color.BLACK);
            field.setUserData(new double[]{relX, relY});
            pane.getChildren().add(field);
        }

        updateFieldPositions(pane, boardImage);
    }

    private void updateFieldPositions(Pane pane, ImageView boardImage) {
        double imgWidth = boardImage.getBoundsInParent().getWidth();
        double imgHeight = boardImage.getBoundsInParent().getHeight();

        // Felder genau auf das Bild anpassen
        for (var node : pane.getChildren()) {
            if (node instanceof Rectangle field) {
                double[] relPos = (double[]) field.getUserData();
                field.setLayoutX(imgWidth * relPos[0] - field.getWidth() / 2);
                field.setLayoutY(imgHeight * relPos[1] - field.getHeight() / 2);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void movePlayerToField(Player p, int fieldID){

    }
}

/**
 * Feld für Spielermitteilungen
 * Funktionen für GUI-Steuerung
 * Calls an das Backend
 */