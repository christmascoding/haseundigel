package src;

import src.model.Spieler;

public class Controller {


    //functions to control GUI
    private void showEndTurnScreen(Spieler nächsterSpieler){

    }
    private void showTurnOptions(Spieler derzeitigerSpieler){

    }
    //functions called by the GUI
    /**
     * GUI fragt hier, ob der Zug beendet werden kann (Knopf wird gedrückt)
     * @return true wenn erlaubt, false wenn nicht erlaubt
     */
    public boolean endTurnCalled(){
        //hier spiellogike benachrichtigen
        return true;
    }
}
