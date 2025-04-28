package src;

import src.gui.ActioncardActionWindow;
import src.gui.InputFormat;
import src.gui.MainWindow;
import src.model.SpielLogik;
import src.model.Spieler;

import java.util.List;
import java.util.concurrent.Semaphore;

public class Controller {

    public boolean gameActive;
    /**
     * The playerSelectionLock Semaphore is used once per game.
     * It gets acquired by the logic before the logic starts
     * and is freed by the game after the playerSelectionScreen is closed.
     */
    public Semaphore playerSelectionLock = new Semaphore(0);
    /**
     * The startTurnLock is used once per turn.
     * It gets acquired by the logic before the logic triggers the GUI to update the player statistics.
     * After it's freed by the GUI by a press of the "Start turn" button, the logic can update
     * the player stats in the GUI.
     */
    public Semaphore startTurnLock = new Semaphore(0);
    /**
     * The logicInputReadyLock is acquired by the GUI to make sure the logic is done with any calculations before it's ready to wait for player input.
     * It gets freed by the logic after it can start waiting for Input
     */
    public Semaphore logicInputReadyLock = new Semaphore(0);
    /**
     * This lock is acquired by the logic when it expects an input by the GUI.
     * After this is released by the GUI, the logic will check if the input is valid
     * and will re-acquire(lock) it as long as the inputs are invalid
     */
    public Semaphore waitForInputLock = new Semaphore(0);

    private SpielLogik logik;
    private MainWindow mainWindow;

    public Controller(SpielLogik logik, MainWindow mainWindow){
        this.logik = logik;
        this.mainWindow = mainWindow;
        gameActive = false;
    }

    /**
     * Funktion die zum Spielstart aufgerufen wird
     */
    public void start(){
        gameActive = true;
        Thread logikThread = new Thread(logik);
        logikThread.start();

    }

    /**
     * Function that gets Inputs from the GUI
     */
    public InputFormat grabInputFromGUI(){
        return MainWindow.getInstance().getInputs();
    }
    public void beginTurnTrigger(){

    }
    public void triggerFieldRender(List<Spieler> players){
        MainWindow.getInstance().clearPlayersOnField();
        MainWindow.getInstance().renderField(players);
    }
    public void showCarrotBtn(boolean show){
        MainWindow.getInstance().showEatCarrot(show);
    }
    public void showSaladBtn(boolean show){
        MainWindow.getInstance().showEatSalad(show);
    }
    public void showMoveForwardBtn(boolean show){
        MainWindow.getInstance().showMoveForward(show);
    }
    public void showMoveBackwardBtn(boolean show){
        MainWindow.getInstance().showMoveBackward(show);
    }

    /**
     * Opens an Action Window with the given Text
     * @param s Text to be displayed in the action window
     */
    public void openActionCardActionWindow(String s){
        ActioncardActionWindow.showAction(s);
    }

    /**
     * Trigger the GUI to update player stats
     */
    public void updateStats(){
        MainWindow.getInstance().updatePlayerGUI();
    }
}
