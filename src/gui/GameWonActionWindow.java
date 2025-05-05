package src.gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
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

import java.util.List;

public class GameWonActionWindow {

    public static void showWinnersPopup(List<Spieler> platzierungsliste) {
        Platform.runLater(() -> {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UTILITY);
            popupStage.setTitle("Spiel beendet");

            // Titelüberschrift
            Label titleLabel = new Label("Hase und Igel ist beendet");
            titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
            titleLabel.setAlignment(Pos.CENTER);

            VBox winnerBox = new VBox(15);
            winnerBox.setAlignment(Pos.CENTER);

            for (int i = 0; i < platzierungsliste.size(); i++) {
                Spieler spieler = platzierungsliste.get(i);
                int place = i + 1;

                // Spielerbild
                ImageView imageView = new ImageView(spieler.getPlayerImage());
                imageView.setFitWidth(64);
                imageView.setFitHeight(64);
                imageView.setPreserveRatio(true);

                // Platzierung + Name
                Label platzLabel = new Label("#" + place + "  " + spieler.getName());
                platzLabel.setGraphic(imageView);
                platzLabel.setGraphicTextGap(10);

                // Stil entsprechend Platzierung
                switch (place) {
                    case 1 -> platzLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: gold; -fx-font-weight: bold;");
                    case 2 -> platzLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: silver;");
                    case 3 -> platzLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: peru;");
                    default -> platzLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
                }

                winnerBox.getChildren().add(platzLabel);
            }

            // OK-Button
            Button okButton = new Button("OK");
            okButton.setStyle("-fx-font-size: 16px;");
            okButton.setOnAction(e -> {
                popupStage.close();
                System.exit(0);
            });

            VBox layout = new VBox(25, titleLabel, winnerBox, okButton);
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(30));
            layout.setStyle("-fx-background-color: white;");

            popupStage.setScene(new Scene(layout));
            popupStage.showAndWait();
        });
    }
}
