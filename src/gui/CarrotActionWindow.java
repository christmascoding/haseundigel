package src.gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class CarrotActionWindow {

    public enum Action {
        AUFNEHMEN, ABGEBEN
    }

    public static Action showCarrotActionDialog() {
        // because this is called async we wait until were in the javafx thread, this time without runLater
        if (!Platform.isFxApplicationThread()) {
            final Action[] resultHolder = new Action[1];
            final Object lock = new Object();

            Platform.runLater(() -> {
                synchronized (lock) {
                    resultHolder[0] = showDialog();
                    lock.notify();
                }
            });

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            return resultHolder[0];
        } else {
            return showDialog();
        }
    }

    private static Action showDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Karottenaktion");
        alert.setHeaderText("Was möchtest du tun?");
        alert.setContentText("Möchtest du 10 Karotten aufnehmen oder abgeben?");

        ButtonType aufnehmenButton = new ButtonType("Aufnehmen", ButtonData.YES);
        ButtonType abgebenButton = new ButtonType("Abgeben", ButtonData.NO);

        alert.getButtonTypes().setAll(aufnehmenButton, abgebenButton);

        // prevent closing window
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setOnCloseRequest(WindowEvent::consume);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == aufnehmenButton) {
                return Action.AUFNEHMEN;
            } else if (result.get() == abgebenButton) {
                return Action.ABGEBEN;
            }
        }
        return Action.AUFNEHMEN; // sanity check

    }
}
