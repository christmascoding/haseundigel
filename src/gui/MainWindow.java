package src.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import src.model.SpielLogik;
import src.model.Spieler;

import java.util.List;

public class MainWindow extends Application {

    public int guiFieldWidth = 1280;
    public int guiFieldHeight = 720;
    GUICoordinateTable coordinateTable = new GUICoordinateTable();
    private List<Coordinate> coordinates = coordinateTable.getCoordinateTable();

    private Label resourceLabel = new Label("Ressourcen: 5 Karotten, 3 Salat");
    private TextField stepInput = new TextField();
    private Button moveForwardBtn = new Button("Vorwärts");
    private Button moveBackwardBtn = new Button("Rückwärts");
    private Button eatCarrotBtn = new Button("Karotten essen");
    private Button eatSaladBtn = new Button("Salat essen");
    private TextField eatCarrotInput = new TextField();
    private Button endTurnBtn = new Button("Zug beenden");
    private VBox pauseScreen = new VBox();

    private HBox playerUI = new HBox(20);

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hase und Igel");

        ImageView boardImage = new ImageView(new Image("file:src/assets/spielbrett.jpg"));
        boardImage.setPreserveRatio(true);
        boardImage.setFitWidth(guiFieldWidth);
        boardImage.setFitHeight(guiFieldHeight);

        Pane boardPane = new Pane();
        boardPane.setMinSize(guiFieldWidth, guiFieldHeight);
        boardPane.getChildren().add(boardImage);

        Pane fieldPane = new Pane();
        fieldPane.setMinSize(guiFieldWidth, guiFieldHeight);
        boardPane.getChildren().add(fieldPane);
        displayFields(fieldPane, boardImage);

        playerUI.setAlignment(Pos.CENTER);
        playerUI.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;");

        stepInput.setPrefWidth(50);
        stepInput.setPromptText("Schritte");
        eatCarrotInput.setPrefWidth(50);

        eatCarrotBtn.setDisable(true);
        eatSaladBtn.setDisable(true);

        endTurnBtn.setOnAction(e -> endTurn());

        playerUI.getChildren().addAll(resourceLabel, stepInput, moveForwardBtn, moveBackwardBtn, eatCarrotInput, eatCarrotBtn, eatSaladBtn, endTurnBtn);

        pauseScreen.setAlignment(Pos.CENTER);
        pauseScreen.setVisible(false);
        Button startTurnBtn = new Button("Zug beginnen");
        startTurnBtn.setOnAction(e -> beginTurn());
        pauseScreen.getChildren().add(startTurnBtn);

        BorderPane root = new BorderPane();
        root.setCenter(boardPane);
        root.setBottom(playerUI);
        root.setBottom(pauseScreen);

        Scene scene = new Scene(root, 1920, 1080);
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
    // Knopf-Funktionen

    /**
     * Wird aufgerufen, wenn der Rückwärts-Knopf gedrückt wird
     */
    private void reversePressed(){

    }

    /**
     * Wird aufgerufen, wenn der Vorwärts-Knopf gedrückt wird
     * @param amount Anzahl an Vorwärtszügen, die vollendet werden
     */
    private void forwardPressed(int amount){
        //notify backend
    }

    /**
     * Wird aufgerufen, wenn der User den nächsten Zug über einen Knopf betätigt
     */ // to do - beachte MVC, GUI darf nicht direkt etwas ändern: eigentlich Pause drücken -> Controller -> Model: "okay, ja gehe in pause" -> Controller -> update GUI
    private void endTurn(){
        showPauseScreen(true);
    }

    /**
     * Wird aufgerufen, wenn der User den nächsten Zug beginnen möchte (in der Pause)
     */ // to do - auch hier -> MVC
    private void beginTurn(){
        showPauseScreen(false);
        //Contorller: Abfrage, ob der Spieler sich auf einem Karrotten- bzw. Salatfeld befindet, falls das der Fall ist, die jeweiligen Knöpfe einblenden
    }

    /**
     * Blendet einen Knopf und links davon ein Textfeld ein, um eine bestimmte Anzahl an Karrotten essen zu können
     * @param state True falls angezeigt werden soll, bei false ist es ausgegraut
     */
    private void showEatCarrot(boolean state){
        if(state){

        }else{

        }
    }

    /**
     * Blendet einen Knopf und links davon ein Textfeld ein, um eine bestimmte Anzahl an Salat essen zu können
     * @param state True falls angezeigt werden soll, bei false ist es ausgegraut
     */
    private void showEatSalad(boolean state){
        if(state){

        }else{

        }
    }
    /**
     * Wird aufgerufen, wenn der Spieler nichts mehr machen kann (wenn sein Zug vollendet ist, dann kann er nur noch seinen Zug beenden mit dem Zug Beenden button)
     */
    private void grayOutButtons(){

    }

    /**
     * Wird aufgerufen, wenn der Spieler seinen Zug beendet, und der nächste den Zug beginnen muss (damit sie sich nicht in die Möhren schauen!)
     * Diese Funktion versteckt alle Knöpfe und zeigt nur einen Knopf mit "Starte nächsten Zug" an.
     * @param state true, wenn Pausescreen angezeigt werden soll
     */
    private void showPauseScreen(boolean state){
        Spieler nächsterSpieler = null; //TODO
        nächsterSpieler.getName();

    }

    /*
    Wenn Spieler auf Salatfeld ist: Kann er Salat essen
    Wenn er auf einem Karottenfeld ist: Kann Karotten nehmen/abgeben
    Wenn er das macht, setzt er aus
    */

    /**
     * Aktualisiert die GUI mit den Spielerressourcen
     * @param p Spieler, dessen Ressourcen angezeigt werden sollen
     */
    private void updatePlayerGUI(Spieler p) {
        resourceLabel.setText("Ressourcen: " + p.getKarotten() + " Karotten, " + p.getSalate() + " Salat");
    }

    /**
     * Setzt den Spieler, der gerade am Zug ist
     * @param p Spieler, der am Zug ist
     */
    public void changePlayerAtTurn(Spieler p) {
        updatePlayerGUI(p);
    }

    /**
     * Zeigt dem Spieler eine Karte an, sobald er eine gezogen hat
     * @param cardText Text, der auf der Karte ist
     */
    public void showCardText(String cardText){

    }


}
