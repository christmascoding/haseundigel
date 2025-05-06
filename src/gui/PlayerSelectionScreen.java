package src.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.model.Spieler;

import java.io.FileInputStream;
import java.util.*;

public class PlayerSelectionScreen {

    private final ListView<String> playerListView = new ListView<>();
    private final TextField nameInput = new TextField();
    private final Map<String, Spieler> spielerMap = new HashMap<>();
    private final Set<Integer> usedImages = new HashSet<>();
    private int selectedImageIndex = -1;

    private final String[] imagePaths = {
            "src/assets/spielfiguren/igel1.png",
            "src/assets/spielfiguren/igel2.png",
            "src/assets/spielfiguren/igel3.png",
            "src/assets/spielfiguren/igel4.png",
            "src/assets/spielfiguren/igel5.png",
            "src/assets/spielfiguren/igel6.JPEG"
    };

    private final Image[] loadedImages = new Image[6];
    private final ToggleButton[] imageButtons = new ToggleButton[6];

    public List<Spieler> getPlayers() {
        List<Spieler> orderedList = new ArrayList<>();
        for (String name : playerListView.getItems()) {
            orderedList.add(spielerMap.get(name));
        }
        return orderedList;
    }


    public void showAndWait() {
        Stage stage = new Stage();
        stage.setTitle("Spielerauswahl");

        // Bilder vorladen
        for (int i = 0; i < imagePaths.length; i++) {
            try {
                loadedImages[i] = new Image(new FileInputStream(imagePaths[i]));
            } catch (Exception e) {
                System.out.println("Bild nicht gefunden: " + imagePaths[i]);
            }
        }

        VBox playerListBox = new VBox(new Label("Spielerliste:"), playerListView);
        playerListBox.setPadding(new Insets(10));
        playerListBox.setPrefWidth(200);

        Label nameLabel = new Label("Spielername:");
        nameInput.setPromptText("Name eingeben");

        GridPane imageGrid = new GridPane();
        imageGrid.setHgap(10);
        imageGrid.setVgap(10);
        imageGrid.setPadding(new Insets(10));
        ToggleGroup toggleGroup = new ToggleGroup();

        for (int i = 0; i < loadedImages.length; i++) {
            if (loadedImages[i] == null) continue;

            ImageView imageView = new ImageView(loadedImages[i]);
            imageView.setFitWidth(64);
            imageView.setFitHeight(64);

            ToggleButton button = new ToggleButton();
            button.setGraphic(imageView);
            button.setUserData(i);
            button.setToggleGroup(toggleGroup);
            imageButtons[i] = button;

            imageGrid.add(button, i % 3, i / 3);
        }

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                selectedImageIndex = (int) newToggle.getUserData();
            } else {
                selectedImageIndex = -1;
            }
        });

        Button addButton = new Button("Spieler hinzufügen");
        addButton.setOnAction(e -> {
            String name = nameInput.getText().trim();
            if (name.isEmpty() || selectedImageIndex == -1 || usedImages.contains(selectedImageIndex)) return;
            if (!spielerMap.containsKey(name)) {
                int imageIndex = selectedImageIndex;
                Image image = loadedImages[imageIndex];
                Spieler spieler = new Spieler(name, image);
                spielerMap.put(name, spieler);
                usedImages.add(imageIndex);
                playerListView.getItems().add(name);
                nameInput.clear();
                toggleGroup.selectToggle(null);

                imageButtons[imageIndex].setDisable(true);
                ((ImageView) imageButtons[imageIndex].getGraphic()).setOpacity(0.3);

                selectedImageIndex = -1;
            }
        });

        Button removeButton = new Button("Spieler entfernen");
        removeButton.setOnAction(e -> {
            String selected = playerListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Spieler removed = spielerMap.remove(selected);
                playerListView.getItems().remove(selected);

                int idx = Arrays.asList(loadedImages).indexOf(removed.getPlayerImage());
                if (idx >= 0) {
                    usedImages.remove(idx);
                    imageButtons[idx].setDisable(false);
                    ((ImageView) imageButtons[idx].getGraphic()).setOpacity(1.0);
                }
            }
        });

        Button doneButton = new Button("Fertig");
        doneButton.setOnAction(e -> {
            if(spielerMap.size() >= 2){
                stage.close();
            }
        });

        stage.setOnCloseRequest(e -> System.exit(0));

        HBox buttonRow = new HBox(10, addButton, removeButton, doneButton);
        buttonRow.setAlignment(Pos.CENTER_RIGHT);

        VBox rightBox = new VBox(10, nameLabel, nameInput, new Label("Bild auswählen:"), imageGrid, buttonRow);
        rightBox.setPadding(new Insets(10));
        rightBox.setAlignment(Pos.TOP_LEFT);
        HBox root = new HBox(playerListBox, rightBox);
        Scene scene = new Scene(root, 750, 360);
        stage.setScene(scene);
        stage.showAndWait(); // blockiert Aufrufer, bis Fenster geschlossen wirdk
    }
}
