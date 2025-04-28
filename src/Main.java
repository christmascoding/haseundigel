package src;

import javafx.application.Application;
import src.gui.MainWindow;
import src.gui.PlayerSelectionScreen;
import src.model.DynConfig;
import src.model.SpielLogik;
import src.model.Spieler;

import java.util.List;

public class Main {
    public List<Spieler> initialPlayers;
    public static void main(String[] args) {
        Application.launch(MainWindow.class, args);

    }
}
