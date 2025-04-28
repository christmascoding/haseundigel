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
        SpielLogik logik = new SpielLogik();
        Application.launch(MainWindow.class, args);
        Controller ctrl = new Controller(logik, MainWindow.getInstance());
        MainWindow.getInstance().setController(ctrl);
        logik.setController(ctrl);
        ctrl.start();

    }
}
