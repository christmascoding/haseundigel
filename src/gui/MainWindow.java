package src.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import src.Controller;
import src.model.SpielLogik;
import src.model.Spieler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.stage.StageStyle;

public class MainWindow extends Application {

    private static MainWindow instance;

    public MainWindow(){
        instance = this;
    }

    public int guiFieldWidth = 1280;
    public int guiFieldHeight = 720;
    GUICoordinateTable coordinateTable = new GUICoordinateTable();
    private List<Coordinate> coordinates = coordinateTable.getCoordinateTable();

    //private Label resourceLabel = new Label("Ressourcen: 5 Karotten, 3 Salat");
    private TextField stepInput = new TextField();
    private Button moveForwardBtn = new Button("Vorwärts");
    private Button moveBackwardBtn = new Button("Rückwärts");
    private Button eatCarrotBtn = new Button("Karotten essen");
    private Button getCarrotBtn = new Button("Karotten erhalten");
    private Button eatSaladBtn = new Button("Salat essen");
    private Button endTurnBtn = new Button("Zug beenden");
    private VBox pauseScreen = new VBox();
    private double imgWidth;
    private double imgHeight;
    private Pane fieldPane = new Pane();
    private final int playerWidth = 40;
    private HBox playerUI = new HBox(20);
    private Controller controller;
    private SpielLogik logik;
    GridPane resourceTable = new GridPane();
    Label karrottenLabel = new Label("0");
    Label salateLabel = new Label("0");
    private InputFormat inputs = new InputFormat(0, false, false, false, false, false);
    private HBox turnBox = new HBox(10);
    private Label currentPlayerLabel = new Label("Spieler am Zug:");
    private ImageView currentPlayerIcon = new ImageView();





    // TEST

    @Override
    public void start(Stage primaryStage) {
        // Create logic and controller instance, set variables
        SpielLogik logik = new SpielLogik();
        Controller ctrl = new Controller(logik, MainWindow.getInstance());
        this.controller = ctrl;
        this.logik = logik;
        MainWindow.getInstance().setController(ctrl);
        logik.setController(ctrl);
        ctrl.start();



        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setTitle("Hase und Igel");
        // riege

        // selection screen
        //PlayerSelectionScreen selectionScreen = new PlayerSelectionScreen();
        //selectionScreen.showAndWait();
        Spieler test1 = new Spieler("hi", new Image("file:src/assets/spielfiguren/igel1.png"));
        Spieler test2 = new Spieler("h2i", new Image("file:src/assets/spielfiguren/igel2.png"));
        List<Spieler> spielerle = new ArrayList<>();
        spielerle.add(test1);
        spielerle.add(test2);
        //release lock so logic can work
        controller.logicInputReadyLock.release();
        // TEST REMOVE SELECTIONSCREEN
        //logik.addPlayers(selectrionScreen.);
        logik.addPlayers(spielerle);
        logik.setConfigMorePlayers();
        logik.setStartRsrctoPlayers();


        // create Spielbrett
        ImageView boardImage = new ImageView(new Image("file:src/assets/spielbrett.jpg"));
        boardImage.setPreserveRatio(true);
        boardImage.setFitWidth(guiFieldWidth);
        boardImage.setFitHeight(guiFieldHeight);

        Pane boardPane = new Pane();
        boardPane.setMinSize(guiFieldWidth, guiFieldHeight);
        boardPane.getChildren().add(boardImage);


        fieldPane.setMinSize(guiFieldWidth, guiFieldHeight);
        boardPane.getChildren().add(fieldPane);
        displayFields(fieldPane, boardImage);

        playerUI.setAlignment(Pos.CENTER);
        playerUI.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20px;");

        turnBox.setAlignment(Pos.CENTER);
        turnBox.setPadding(new Insets(5));
        turnBox.setMaxWidth(Double.MAX_VALUE); //das vvlt käse code
        turnBox.getChildren().addAll(currentPlayerLabel, currentPlayerIcon);

        currentPlayerIcon.setFitWidth(32); //icon size
        currentPlayerIcon.setFitHeight(32);

        stepInput.setPrefWidth(50);
        stepInput.setPromptText("Schritte");


        eatCarrotBtn.setDisable(true);
        eatSaladBtn.setDisable(true);
        getCarrotBtn.setDisable(true);

        endTurnBtn.setOnAction(e -> endTurn());
        moveForwardBtn.setOnAction(e -> triggerMoveForward());

        // Icons für Karotten und Salat
        ImageView carrotIcon = new ImageView(new Image("file:src/assets/carrot.jpg"));
        carrotIcon.setFitHeight(20);
        carrotIcon.setFitWidth(20);

        ImageView saladIcon = new ImageView(new Image("file:src/assets/salad.jpg"));
        saladIcon.setFitHeight(20);
        saladIcon.setFitWidth(20);

        // Ressourcen Tabelle

        resourceTable.setHgap(10);
        resourceTable.setVgap(5);
        resourceTable.add(new Label("Karotten:"), 0, 0);
        resourceTable.add(new Label("Salat:"), 0, 1);
        resourceTable.add(karrottenLabel, 1, 0);  // Platzhalterwert
        resourceTable.add(salateLabel, 1, 1);
        resourceTable.add(carrotIcon, 2, 0);
        resourceTable.add(saladIcon, 2, 1);


        playerUI.getChildren().addAll(resourceTable, stepInput, moveForwardBtn, moveBackwardBtn, getCarrotBtn, eatCarrotBtn, eatSaladBtn, endTurnBtn);

        // Pausenbildschirm unter dem Spielfeld, aber über der Spielerleiste
        pauseScreen.setAlignment(Pos.CENTER);
        pauseScreen.setVisible(true);
        Button startTurnBtn = new Button("Zug beginnen");
        startTurnBtn.setOnAction(e -> beginTurn());
        pauseScreen.getChildren().add(startTurnBtn);

        moveForwardBtn.setOnAction(e -> forwardPressed());
        moveBackwardBtn.setOnAction(e -> backwardPressed());
        eatCarrotBtn.setOnAction(e -> eatCarrotPressed());
        eatSaladBtn.setOnAction(e -> eatSaladPressed());
        BorderPane root = new BorderPane();

        //spielfeld and pause
        VBox centerBox = new VBox(boardPane, pauseScreen);
        root.setCenter(centerBox);

        //bottom box with player thing and other thing
        VBox bottomBox = new VBox(5, turnBox, playerUI);
        bottomBox.setAlignment(Pos.CENTER);
        root.setBottom(bottomBox);


        Scene scene = new Scene(root, 1500, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

        // end turn screen
        endTurn();
        //spielerles.add(test5);
        //spielerles.add(test6);
        //spielerles.add(test7);
        //spielerles.add(test8);

        primaryStage.show();
        //displayPlayersOnField(spielerles, 0);

    }


    private void displayFields(Pane pane, ImageView boardImage) { //has to be run once
        imgWidth = boardImage.getBoundsInParent().getWidth();  // Breite des Spielfeldes
        imgHeight = boardImage.getBoundsInParent().getHeight(); // Höhe des Spielfeldes

        for (Coordinate coordinate : coordinates) {
            double relX = (double) coordinate.x() / 2892;  // Relativkoordinate X
            double relY = (double) coordinate.y() / 2184;  // Relativkoordinate Y

            // Berechne die absolute Position im sichtbaren Bereich
            double absX = imgWidth * relX;
            double absY = imgHeight * relY;

            // Rechteck für das Spielfeld (bleibt so wie es ist)
            Rectangle field = new Rectangle(20, 20);
            field.setFill(Color.TRANSPARENT);
            field.setStroke(Color.BLACK);
            field.setUserData(new double[]{relX, relY});
            field.setX(absX - 10);  // Zentrieren des Rechtecks
            field.setY(absY - 10);  // Zentrieren des Rechtecks
            pane.getChildren().add(field);
        }
    }


    public void clearPlayersOnField() {
        Platform.runLater(() -> {
            fieldPane.getChildren().removeIf(node -> node instanceof ImageView);
        });
    }

    /**
     * Resets all Inputs
     */
    private void resetInputs(){
        inputs = new InputFormat(0, false, false, false, false, false);
    }
    private void displayPlayersOnField(List<Spieler> players, int fieldIndex) {
        Platform.runLater(() -> {
            if (players.isEmpty()) return;

            // Hole die Koordinaten des aktuellen Feldes
            Coordinate fieldCoords = coordinateTable.getCoordinateTable().get(fieldIndex);

            double relX = (double) fieldCoords.x() / 2892;
            double relY = (double) fieldCoords.y() / 2184;

            // Berechne die absolute Position des Feldes
            double absX = imgWidth * relX - 20;
            double absY = imgHeight * relY - 20;

            // Wenn mehr als 2 Spieler vorhanden sind, runde Anordnung
            if (players.size() >= 2) {
                // Verringere den Radius, um die Spieler näher zusammenzubringen
                double radius = 20; // Kleineren Radius verwenden, um Spieler näher zu positionieren
                double angleStep = 2 * Math.PI / players.size(); // Der Abstand zwischen den Spielern im Kreis

                // Iteriere über alle Spieler und setze ihre Position auf dem Kreis
                for (int i = 0; i < players.size(); i++) {
                    Spieler p = players.get(i);

                    // Lade das Bild für den Spieler (Igel)
                    Image img;
                    try {
                        img = p.getPlayerImage();
                        if (img.isError()) {
                            System.out.println("FEHLER: Bild konnte nicht geladen werden.");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("FEHLER: Ausnahme beim Laden des Bildes.");
                        continue;
                    }

                    ImageView piece = new ImageView(img);
                    piece.setFitWidth(playerWidth / 1.5);
                    piece.setFitHeight(playerWidth / 1.5);

                    // Berechne die Position jedes Spielers im Kreis
                    double angle = angleStep * i; // Der aktuelle Winkel für den Spieler
                    double offsetX = radius * Math.cos(angle); // X-Offset basierend auf dem Winkel
                    double offsetY = radius * Math.sin(angle); // Y-Offset basierend auf dem Winkel

                    // Setze die Position des Igels auf dem Kreis um das Feld
                    piece.setX(absX + offsetX);  // Zentriere das Bild und setze den X-Offset
                    piece.setY(absY + offsetY);  // Zentriere das Bild und setze den Y-Offset

                    // Das Bild in den Vordergrund setzen
                    piece.toFront();

                    // Füge das Spielfigur-Bild zum Pane hinzu
                    fieldPane.getChildren().add(piece);
                }
            } else {
                // Anordnung wie vorher, wenn weniger als oder genau 2 Spieler
                for (int i = 0; i < players.size(); i++) {
                    Spieler p = players.get(i);

                    // Lade das Bild für den Spieler (Igel)
                    Image img;
                    try {
                        img = p.getPlayerImage();
                        if (img.isError()) {
                            System.out.println("FEHLER: Bild konnte nicht geladen werden.");
                            continue;
                        }
                    } catch (Exception e) {
                        System.out.println("FEHLER: Ausnahme beim Laden des Bildes.");
                        continue;
                    }

                    ImageView piece = new ImageView(img);
                    piece.setFitWidth(playerWidth);
                    piece.setFitHeight(playerWidth);

                    // Setze die Position des Igels relativ zum Feld, sodass der Igel zentriert wird
                    piece.setX(absX);  // Zentriere auf dem Feld
                    piece.setY(absY);  // Zentriere auf dem Feld

                    // Das Bild in den Vordergrund setzen
                    piece.toFront();

                    // Füge das Spielfigur-Bild zum Pane hinzu
                    fieldPane.getChildren().add(piece);
                }
            }
        });
    }



    public void renderField(List<Spieler> players) {
        for (int i = 0; i < coordinateTable.getCoordinateTable().size(); i++) { //iterate all fields
            List<Spieler> spielerAufFeld = new ArrayList<>();

            for (Spieler p : players) { //save all players on field
                if (p.getAktuelleFeldNr() == i) {
                    spielerAufFeld.add(p);
                }
            }
            if (!spielerAufFeld.isEmpty()) {
                displayPlayersOnField(spielerAufFeld, i);
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
    private void reversePressed() {
        if(controller.waitForInputLock.hasQueuedThreads()){ //falls acquired, dann releasen
            controller.waitForInputLock.release();
        }
        resetInputs();
        inputs.setWalkBackwardPressed(true);
    }

    /**
     * Wird aufgerufen, wenn der Vorwärts-Knopf gedrückt wird
     */
    private void forwardPressed() {
        if (Integer.parseInt(stepInput.getCharacters().toString()) > 0) {
            // if input valid
            if (controller.waitForInputLock.hasQueuedThreads()) {  // Falls acquired, dann releasen und ausführen
                controller.waitForInputLock.release();
                resetInputs();  // Input-Variablen resetten, dann setzen
                inputs.setWalkForwardPressed(true);
                inputs.setWalkWide(Integer.parseInt(stepInput.getCharacters().toString()));
            }
        }


    }

    /**
     * Wird aufgerufen, wenn Rückwärts-Knopf gedürckt wird
     */
    private void backwardPressed(){
        if (controller.waitForInputLock.hasQueuedThreads()) {  // Falls acquired, dann releasen und ausführen
            controller.waitForInputLock.release();
            resetInputs();  // Input-Variablen resetten, dann setzen
            inputs.setWalkBackwardPressed(true);
        }
    }
    private void eatSaladPressed(){
        if (controller.waitForInputLock.hasQueuedThreads()) {  // Falls acquired, dann releasen und ausführen
            controller.waitForInputLock.release();
            resetInputs();  // Input-Variablen resetten, dann setzen
            inputs.setSaladEatenPressed(true);
        }
    }

    private void eatCarrotPressed(){
        if (controller.waitForInputLock.hasQueuedThreads()) {  // Falls acquired, dann releasen und ausführen
            controller.waitForInputLock.release();
            resetInputs();  // Input-Variablen resetten, dann setzen
            inputs.setEatCarrotsPressed(true);
        }
    }

    /**
     * Wird aufgerufen, wenn der User den nächsten Zug über einen Knopf betätigt
     */ // to do - beachte MVC, GUI darf nicht direkt etwas ändern: eigentlich Pause drücken -> Controller -> Model: "okay, ja gehe in pause" -> Controller -> update GUI
    private void endTurn() {
        showPauseScreen(true);
    }

    /**
     * Wird aufgerufen, wenn der User den nächsten Zug beginnen möchte (in der Pause)
     */ // to do - auch hier -> MVC
    private void beginTurn() {

        //unlock the begin turn semaphore, so the logic can start working
        controller.startTurnLock.release();
        showPauseScreen(false);
        updatePlayerGUI();


        //Contorller: Abfrage, ob der Spieler sich auf einem Karrotten- bzw. Salatfeld befindet, falls das der Fall ist, die jeweiligen Knöpfe einblenden
    }

    /**
     * Blendet einen Knopf und links davon ein Textfeld ein, um eine bestimmte Anzahl an Karrotten essen zu können
     *
     * @param state True falls angezeigt werden soll, bei false ist es ausgegraut
     */
    public void showEatCarrot(boolean state) {
        eatCarrotBtn.setDisable(!state);
    }

    /**
     * Blendet einen Knopf und links davon ein Textfeld ein, um eine bestimmte Anzahl an Salat essen zu können
     *
     * @param state True falls angezeigt werden soll, bei false ist es ausgegraut
     */
    public void showEatSalad(boolean state) {
        eatSaladBtn.setDisable(!state);
    }
    public void showMoveForward(boolean state){
        moveForwardBtn.setDisable(!state);
    }
    public void showMoveBackward(boolean state){
        moveBackwardBtn.setDisable(!state);
    }

    /**
     * Wird aufgerufen, wenn der Spieler nichts mehr machen kann (wenn sein Zug vollendet ist, dann kann er nur noch seinen Zug beenden mit dem Zug Beenden button)
     */
    private void grayOutButtons(boolean state) {
        moveForwardBtn.setDisable(!state);
        moveBackwardBtn.setDisable(!state);
        eatCarrotBtn.setDisable(!state);
        eatSaladBtn.setDisable(!state);
        endTurnBtn.setDisable(!state);
        getCarrotBtn.setDisable(!state);
    }

    /**
     * Wird aufgerufen, wenn der Spieler seinen Zug beendet, und der nächste den Zug beginnen muss (damit sie sich nicht in die Möhren schauen!)
     * Diese Funktion versteckt alle Knöpfe und zeigt nur einen Knopf mit "Starte nächsten Zug" an.
     *
     * @param state true, wenn Pausescreen angezeigt werden soll
     */
    private void showPauseScreen(boolean state) {
        //Spieler nächsterSpieler = null; //TODO
        //nächsterSpieler.getName();
        pauseScreen.setVisible(state);
        playerUI.setVisible(!state);
    }

    /*
    Wenn Spieler auf Salatfeld ist: Kann er Salat essen
    Wenn er auf einem Karottenfeld ist: Kann Karotten nehmen/abgeben
    Wenn er das macht, setzt er aus
    */

    /**
     * Aktualisiert die GUI mit den Spielerressourcen mit dem nächsten Spieler, der am Zug ist
     */
    private void updatePlayerGUI() {
        Spieler current = this.logik.getCurrentPlayer();

        //update resource
        resourceTable.getChildren().remove(karrottenLabel);
        resourceTable.getChildren().remove(salateLabel);
        karrottenLabel = new Label(String.valueOf(current.getKarotten()));
        salateLabel   = new Label(String.valueOf(current.getSalate()));
        resourceTable.add(karrottenLabel, 1, 0);
        resourceTable.add(salateLabel,  1, 1);

        //update turnbox
        currentPlayerLabel.setText("Spieler am Zug: " + current.getName());
        currentPlayerIcon.setImage(current.getPlayerImage());
    }


    public InputFormat getInputs(){
        return inputs;
    }


    /**
     * Zeigt dem Spieler eine Karte an, sobald er eine gezogen hat
     *
     * @param cardText Text, der auf der Karte ist
     */
    public void showCardText(String cardText) {

    }

    /**
     * Schaltet den "Karotten erhalten" Knopf ein und aus
     * @param state true, wenn der Knopf sichtbar sein soll
     */
    public void showGetCarrotBtn(boolean state) {
        getCarrotBtn.setDisable(!state);
    }


    /**
     * Sets the input parameters from the GUI into the controller
     * REDUNDANT?
     */
    private void setInputParameters(){
        resetInputs();
    }

    /**
     * Sets the controller for the MainWindow view
     * @param ctrl controller class
     */
    public void setController(Controller ctrl){
        controller = ctrl;
    }

    public int getWalkwide(){
        while(!inputs.isWalkForwardPressed()){
            //idle thread
        }
        resetInputs();
        stepInput.getCharacters();
        return Integer.parseInt(stepInput.getCharacters().toString());

    }

    private void triggerMoveForward(){
        //button action
        inputs.setWalkForwardPressed(true);
    }

    /**
     * Returns the instance of the MainWindow
     * @return
     */
    public static MainWindow getInstance() {
        return instance;
    }



}

