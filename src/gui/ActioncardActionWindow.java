package src.gui;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActioncardActionWindow {

    public static void showAction(String text) {
        if (Platform.isFxApplicationThread()) {
            openWindow(text);
        } else {
            Platform.runLater(() -> openWindow(text));
        }
    }

    private static void openWindow(String text) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // Blockiert andere Fenster, bis geschlossen

        Label label = new Label(text);
        label.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, okButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-spacing: 20;");

        Scene scene = new Scene(layout, 400, 200);
        window.setScene(scene);
        window.setTitle("Aktionskarte");
        window.showAndWait(); // wartet, bis Fenster geschlossen wird
    }
}
