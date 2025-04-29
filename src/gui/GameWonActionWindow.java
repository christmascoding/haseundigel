package src.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import src.model.Spieler;

public class GameWonActionWindow {

    public static void showWinnerPopup(Spieler spieler) {
        Platform.runLater(() -> {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Spiel beendet");

            // Großes Spielerbild
            ImageView playerImageView = new ImageView(spieler.getPlayerImage());
            playerImageView.setFitHeight(200);  // Größe nach Wunsch anpassen
            playerImageView.setPreserveRatio(true);

            // Gewinner-Text
            Label winLabel = new Label(spieler.getName() + " hat Hase und Igel gewonnen!");
            winLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

            // OK-Button zum Beenden des Programms
            Button okButton = new Button("OK");
            okButton.setStyle("-fx-font-size: 16px;");
            okButton.setOnAction(e -> {
                popupStage.close();
                System.exit(0); // Beendet die komplette JavaFX-Anwendung
            });

            VBox layout = new VBox(20);
            layout.getChildren().addAll(playerImageView, winLabel, okButton);
            layout.setAlignment(Pos.CENTER);
            layout.setStyle("-fx-padding: 30px; -fx-background-color: white;");

            popupStage.setScene(new Scene(layout));
            popupStage.showAndWait();
        });
    }
}
