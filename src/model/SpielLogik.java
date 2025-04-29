package src.model;

import java.util.*;
import java.util.List;

import javafx.application.Application;
import javafx.scene.image.Image;
import src.Controller;
import src.gui.ActioncardActionWindow;
import src.gui.GameWonActionWindow;
import src.gui.InputFormat;

public class SpielLogik implements Config, Runnable{

    private int configMorePlayers = 0; // configures 1 for more than 4 players

    private int indexCtr; // zählt mit wer dran ist

    private Aktionskartenstapel aktionsKartenStapel;

    private List<PositionsFeld> posFeldListe = new ArrayList<PositionsFeld>(); // enthält nach Instanziierung alle
                                                                               // Positionsfelder in Reihenfolge auf dem Spielbrett

    private List<Spieler> mitspieler = new ArrayList<Spieler>();               // Liste alle Mitspieler

    private List<Spieler> platzierungsliste = new ArrayList<Spieler>();        // Liste alle Mitspieler im Ziel, chronologisch nach Zieleinlauf

    private Controller controller;

    private Image testimage;

    private InputFormat input;

    @Override
    public void run() {
        //Wird aufgerufen, wenn SpielLogik-Instanz in seinem Thread aufgerufen wird
        //Warte darauf, dass Spieler alle regisriert sind
        try{
            controller.logicInputReadyLock.acquire();
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }
        while(controller.gameActive){
            playRound();
        }
    }

    public SpielLogik(){
        createPosFeldListe();

        // to do -> interaction with GUI to add players
        this.aktionsKartenStapel = new Aktionskartenstapel();
        this.aktionsKartenStapel.mischen();
        this.indexCtr = 0;
    }

    public Spieler getCurrentPlayer(){

            return this.mitspieler.get(indexCtr);

    }
    public void playRound(){
        //wait for startround semaphore to be unlocked UwU
        controller.triggerFieldRender(mitspieler);
        try {
            controller.startTurnLock.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Spieler roundplayer = this.mitspieler.get(this.indexCtr);

        controller.showMoveBackwardBtn(true);
        controller.showMoveForwardBtn(true);

        // player has already finished -> go to next player
        if( roundplayer.getAktuelleFeldNr() == Config.numFeldListe.length -1 ) return;

        // player has to suspend
        if(roundplayer.isNextSuspend()) {

            roundplayer.suspend();
            return;

        }

        //now continue hehe

//--------> to do GUI Interaction should start here, not by suspending or already finsihed ---------------

        // check fields at begin of round (positionfeld + Karottenfeld + Salatfeld)
        PositionsFeld currentFeld = this.posFeldListe.get(roundplayer.getAktuelleFeldNr());
        switch(currentFeld){

            case Karottenfeld:

                // nehmen/abgeben, oder laufen? lastWalkedWide auf 0
                // to do replace by GUI interaction
                System.out.println("0 - Karottenfeld nutzen; sonst laufen");
                controller.showCarrotBtn(true);
                /*Scanner scan = new Scanner(System.in);
                int inp = scan.nextInt();*/
                // wait for the GUI -> lock semaphore
                try {
                    controller.waitForInputLock.acquire(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                acquireInputs(); //set inputs to new inputs
                controller.showCarrotBtn(false);


                // KarottenFeld nutzen
                if(input.isEatCarrotsPressed()){

                    roundplayer.karottenFeldAction();
                    controller.triggerFieldRender(mitspieler);
                    controller.showMoveBackwardBtn(false);
                    controller.showMoveForwardBtn(false);
                    roundplayer.didNotWalked();
                    debugRoundplayerOutput();
                    endOfTurnAction();
                    return;

                }

                break;

            case Positionsfeld156:

                // Edgecase: a Player is 5th and 6th at the same time -> gets 110 Karotten (is both Positions)
                ArrayList<Integer> actPositions = getPosition();
                for (int actPosition : actPositions) {

                    if( actPosition == 1 || actPosition == 5 || actPosition == 6 ){

                        roundplayer.changeKarotten(actPosition * 10);

                    }

                }
                break;

            case Positionsfeld2:

                actPositions = getPosition();
                for (int actPosition : actPositions) {

                    if( actPosition == 2 ){

                        roundplayer.changeKarotten(actPosition * 10);

                    }

                }
                break;

            case Positionsfeld3:

                actPositions = getPosition();
                for (int actPosition : actPositions) {

                    if( actPosition == 3 ){

                        roundplayer.changeKarotten(actPosition * 10);

                    }

                }
                break;

            case Positionsfeld4:

                actPositions = getPosition();
                for (int actPosition : actPositions) {

                    if( actPosition == 4 ){

                        roundplayer.changeKarotten(actPosition * 10);

                    }

                }
                break;

            case Salatfeld :
                // essen (lastWalkedWide auf 0) oder laufen
                // -> nur essen wenn salat > 0 && letzte runde nicht schon gegessen (lastWalkedWide != 0) [--> Salate 0 Edgecase, durch Aktionskarte gemoved]
                if( roundplayer.getLastWalkedWide() > 0 && roundplayer.getSalate() > 0 ){

                    System.out.println("0 - Salatfeld nutzen; sonst laufen");
                    //scan = new Scanner(System.in);
                    //inp = scan.nextInt();
                    // wait for the GUI -> lock semaphore
                    controller.showSaladBtn(true);
                    try {
                        controller.waitForInputLock.acquire(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    acquireInputs(); //set inputs to new inputs
                    controller.showSaladBtn(false);
                    // Salatfeld nutzen
                    if(input.isSaladEatenPressed()){

                        roundplayer.eatSalate();
                        // Position * 10 karotten bekommen
                        for(int position: getPosition()){

                            roundplayer.changeKarotten(position * 10);

                        }

                        roundplayer.didNotWalked();
                        debugRoundplayerOutput();
                        endOfTurnAction();
                        return;

                    }

                }
                break;
        }
        // move backward?
        System.out.println("Rückwärts auf Igelfeld? - 0 -- du "+ roundplayer.getName()+" hast noch " +roundplayer.getKarotten() +" Karotten" );
        // wait for the GUI -> lock semaphore
        //Don't wait for input if the input was already received (in Karottenfeld or Salatfeld)

        if(currentFeld != PositionsFeld.Salatfeld && currentFeld != PositionsFeld.Karottenfeld){
            try {
                controller.waitForInputLock.acquire(1);
                System.out.println("Semaphore locked at pos1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            acquireInputs(); //set inputs to new inputs
        }

        //int inp = MainWindow.getInstance().getWalkwide();

        if(input.isWalkBackwardPressed()) { //used to be: if input == 0 -> moved backward

            // if valid moved backwards to Igelfeld
            if (IgeldFeldAction()) {

                debugRoundplayerOutput();
                controller.showMoveForwardBtn(false);
                controller.showMoveBackwardBtn(false);
                endOfTurnAction();
                return;

            }
        }
        // stays in loop till player sucessful moved forward
        // to do not on Salatfeld if no Salate left
        System.out.println("Zugweite bitte - du "+ roundplayer.getName()+" hast noch " +roundplayer.getKarotten() +" Karotten" );

        //int walkwide = 1;       // input paramter - needs to come from GUI
        int walkwide = input.getWalkWide();

        int moveSuccess = -1;

        do{
            // forced to start from beginning
            if( roundplayer.getKarotten() == 0 ){

                System.out.println(" Keine Karotten mehr -> Zurück an Start + Resrc reset");
                controller.openActionCardActionWindow("Du hast keine Karotten mehr! Du wurdest mit neuen Ressourcen an den Sart gesetzt.");
                roundplayer.resetRessources(this.configMorePlayers);
                roundplayer.moveToField(0, this.platzierungsliste.size());
                controller.triggerFieldRender(mitspieler);

            }

            //inp = MainWindow.getInstance().getWalkwide();
            walkwide = input.getWalkWide();
            moveSuccess = roundplayer.moveForward(walkwide, this.platzierungsliste.size());
            controller.triggerFieldRender(mitspieler);

            if( moveSuccess < 0 ){
                switch (moveSuccess){

                    case -1:
                        System.out.println("Bitte nochmal eingeben - dieses Feld existiert nicht!");
                        guiInputErrorHandler("Bitte nochmal eingeben - dieses Feld existiert nicht!");
                        break;

                    case -2:
                        System.out.println("Bitte nochmal eingeben - Du hast nicht genug Karotten!");
                        guiInputErrorHandler("Bitte nochmal eingeben - Du hast nicht genug Karotten!");
                        break;

                    case -3:
                        System.out.println("Bitte nochmal eingeben - Du darfst das Ziel noch nicht betreten!");
                        guiInputErrorHandler("Bitte nochmal eingeben - Du darfst das Ziel noch nicht betreten!");
                        break;

                    case -4:
                        System.out.println("Bitte nochmal eingeben - Du darfst das Salatfeld nicht betreten!");
                        guiInputErrorHandler("Bitte nochmal eingeben - Du darfst das Salatfeld nicht betreten!");
                        break;

                    case -10:
                        System.out.println("Bitte nochmal eingeben - Unzulässiger Eingabeparameter");
                        guiInputErrorHandler("Bitte nochmal eingeben - Unzulässiger Eingabeparameter");
                        break;

                }

            }

        }while (moveSuccess != 1 && moveSuccess != 3);

        // player finished
        if( moveSuccess == 3 ){

            this.platzierungsliste.add( roundplayer );
            System.out.println(roundplayer.getName() + " finished");

            if( this.platzierungsliste.size() == this.mitspieler.size() - 1 ){
                System.out.println("Game finished");

                GameWonActionWindow.showWinnerPopup(roundplayer);
                // to do finish Game

            }
        }


        debugRoundplayerOutput();
        controller.updatePlayerResources();
        // check for Hasenfeld + new Hasenfeld after Hasenfeld
        int posBefore;

        do{

            posBefore = roundplayer.getAktuelleFeldNr();
            if( this.posFeldListe.get(roundplayer.getAktuelleFeldNr()) == PositionsFeld.Hasenfeld ) this.Hasenfeld();

        }while(posBefore != roundplayer.getAktuelleFeldNr());

        // increase counter or start from 0 again
        endOfTurnAction();

    }

    /**
     * Function that should be called at the end of a turn - resets buttons and increases the increment counter
     */
    private void endOfTurnAction(){
        this.indexCtr ++;
        if( this.indexCtr >= this.mitspieler.size() ) this.indexCtr = 0;
        controller.showMoveBackwardBtn(false);
        controller.showMoveForwardBtn(false);
        controller.showCarrotBtn(false);
        controller.showSaladBtn(false);
    }


    private void Hasenfeld(){

        int cardID = this.aktionsKartenStapel.getAktionskartenID(); // take card
        System.out.println(this.aktionsKartenStapel.getAktionsKartenBeschreibungFromID(cardID));
        Spieler roundplayer = this.mitspieler.get(this.indexCtr);
        controller.openActionCardActionWindow("Aktion für Spieler "+roundplayer.getName() + " "+this.aktionsKartenStapel.getAktionsKartenBeschreibungFromID(cardID));



        switch (cardID) {

            case 0:

                roundplayer.eatSalate();
                controller.updatePlayerResources();
                break;

            case 1:

                // nochmal dran
                this.indexCtr --;
                break;

            case 2:

                movePositionActPlayer(1);
                break;

            case 3:

                roundplayer.karottenFeldAction();
                controller.updatePlayerResources();
                break;

            case 4:

                movePositionActPlayer(-1);
                break;

            case 5:

                // forward to next Karottenfeld
                int nextKarottenfeld = findNextField(roundplayer.getAktuelleFeldNr(), PositionsFeld.Karottenfeld);
                roundplayer.moveToField( nextKarottenfeld, this.platzierungsliste.size() );
                break;

            case 6:

                roundplayer.costLastRoundback();
                break;

            case 7:

                // suspend
                roundplayer.hasToSuspendNextRound();
                break;

            case 8:

                // backward to previous Karottenfeld
                int prevKarottenfeld = findPrevField(roundplayer.getAktuelleFeldNr(), PositionsFeld.Karottenfeld);
                roundplayer.moveToField( prevKarottenfeld, this.platzierungsliste.size() );
                break;

        }

    }

    private void guiInputErrorHandler(String message){
        ActioncardActionWindow.showAction(message);
        //lock semaphore to wait for another input
        try {
            controller.waitForInputLock.acquire(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        acquireInputs();
    }
    private void createPosFeldListe(){ // füllt PosFeldListe mit enstprechenden Positionsfeld-enums

        for (int i:numFeldListe){

            PositionsFeld nextFeld;

            switch (i){
                case(0):
                    nextFeld=PositionsFeld.Startfeld;
                    break;
                case(1):
                    nextFeld=PositionsFeld.Hasenfeld;
                    break;
                case(2):
                    nextFeld=PositionsFeld.Karottenfeld;
                    break;
                case(3):
                    nextFeld=PositionsFeld.Positionsfeld156;
                    break;
                case(4):
                    nextFeld=PositionsFeld.Positionsfeld2;
                    break;
                case(5):
                    nextFeld=PositionsFeld.Positionsfeld3;
                    break;
                case(6):
                    nextFeld=PositionsFeld.Positionsfeld4;
                    break;
                case(7):
                    nextFeld=PositionsFeld.Salatfeld;
                    break;
                case(8):
                    nextFeld=PositionsFeld.Igelfeld;
                    break;
                case(9):
                    nextFeld=PositionsFeld.Zielfeld;
                    break;
                default:
                    System.out.println("An Error occured while creating PosFeldListe - this Position Feld type does not exist");
                    return;

            }

            posFeldListe.add(nextFeld);
        }

        /** printe posFeldListe Reihenfolge
        for(PositionsFeld thisfeld: posFeldListe){
            System.out.println(thisfeld);
        }
        */

    }


    /** to do replace locationID with this.mitspieler.get(this.indexCtr).getAktuellesFeld() ?
    * returned index des nächsten Positionsfeldes eines bestimmten types, existiert kein Folgendes mehr,
     * so wird die eingegebene ID returned */
    private int findNextField(int locationID, PositionsFeld type){

        // list with all elements from nextField till Ziel
        List<PositionsFeld> sublist = new ArrayList<>(this.posFeldListe.subList( locationID+1, this.posFeldListe.size()) );

        int indexInSublist = sublist.indexOf(type);

        // gefunden
        if( indexInSublist != -1 ){

            return locationID+1+indexInSublist;

        }
        // nicht gefunden
        return locationID;


    }

    /** tries to move actual player on prev. Igelfeld -> if it is occupied by other player; the Igelfeld before ....
    * if no preveious Igelfeld is free -> it is not allowed to move backwards
    **/
    private boolean IgeldFeldAction(){

        Spieler roundplayer = this.mitspieler.get(indexCtr);
        int prevIgelfeld = findPrevField(roundplayer.getAktuelleFeldNr(), PositionsFeld.Igelfeld);

        // check for existing Igeldfeld behind
        if( prevIgelfeld == roundplayer.getAktuelleFeldNr() ) return false;

        while(true){

            // check if Igelfeld is occupied
            if( fieldisNotOccupied(prevIgelfeld) ){

                roundplayer.moveBackwardto(prevIgelfeld);
                controller.triggerFieldRender(mitspieler);
                return true;

            }
            else{

                // first IgelFeld is occupied
                if( prevIgelfeld == Config.firstIgelFeld ) return false;

                // look for prev IgelFeld
                prevIgelfeld = findPrevField(prevIgelfeld, PositionsFeld.Igelfeld);

            }
        }

    }

    /** returned index des vorherigen Positionsfeldes eines bestimmten types, existiert kein vorheriges,
     so wird die eingegebene ID returned **/
    private int findPrevField(int locationID, PositionsFeld type){

        // list with all elements from Start till ID
        List<PositionsFeld> sublist = new ArrayList<>(this.posFeldListe.subList( 0, locationID ));

        int indexInSublist = sublist.lastIndexOf(type);

        // gefunden
        if( indexInSublist != -1 ){

            return indexInSublist;

        }
        // nicht gefunden
        return locationID;


    }

    /**returned Positionen des Spielers der an der Reihe ist **/
    private ArrayList<Integer> getPosition (){

        List <Spieler> sortMitspieler = new ArrayList<>(this.mitspieler); // Kopie -> original unverändert

        sortMitspieler.sort(Comparator.comparingInt(Spieler::getAktuelleFeldNr).reversed());

        int aktindex = sortMitspieler.indexOf(this.mitspieler.get(this.indexCtr)); // index of actual player in sorted list

        ArrayList<Integer>ret = new ArrayList<>();

        // check for other players on same field as actual player
        for(int i = 0; i < sortMitspieler.size(); i++){

            // add own Position + if some player has the same fieldNr as actual player
            if( sortMitspieler.get(i).getAktuelleFeldNr() == sortMitspieler.get(aktindex).getAktuelleFeldNr() ){
                ret.add(i+1);
            }

        }

        return ret;
    }

    private void acquireInputs(){
        input = controller.grabInputFromGUI();
    }


    /** returns FeldNr+1 of player next before actual player, or own FeldNr if actual player is first one
    * can only be called safely when getPosition returned ArrayList with length 1 -> Player is on a field alone
    * */
    private int getFieldFrontPlayer (){

        if( getPosition().size() != 1 ) System.out.println("Error - getFieldFrontPlayer was called unsafely!");

        List <Spieler> sortMitspieler = new ArrayList<>(this.mitspieler); // Kopie -> original unverändert

        sortMitspieler.sort(Comparator.comparingInt(Spieler::getAktuelleFeldNr).reversed());

        int aktindex = sortMitspieler.indexOf(this.mitspieler.get(this.indexCtr)); // index of actual player in sorted list

        // return own FeldNr -> actual player is first player
        if( aktindex == 0 ) return this.mitspieler.get(this.indexCtr).getAktuelleFeldNr();

        // return next Player Field +1; or Finish Field

        return Math.min(sortMitspieler.get(aktindex - 1).getAktuelleFeldNr() + 1, Config.numFeldListe.length - 1);

    }

    /** returns FeldNr+1 of player next behind actual player, or own FeldNr if actual player is last one
     * can only be called safely when getPosition returned ArrayList with length 1 -> Player is on a field alone
     * */
    private int getFieldBackPlayer (){

        if( getPosition().size() != 1 ) System.out.println("Error - getFieldBackPlayer was called unsafely!");

        List <Spieler> sortMitspieler = new ArrayList<>(this.mitspieler); // Kopie -> original unverändert

        sortMitspieler.sort(Comparator.comparingInt(Spieler::getAktuelleFeldNr));

        int aktindex = sortMitspieler.indexOf(this.mitspieler.get(this.indexCtr)); // index of actual player in sorted list

        // return own FeldNr -> actual player is last player
        if( aktindex == 0 ) return this.mitspieler.get(this.indexCtr).getAktuelleFeldNr();

        // return next Player behind Field -1; or Start Field
        return Math.max(sortMitspieler.get(aktindex-1).getAktuelleFeldNr()-1, 0) ;

    }

    /** returns True if a given FieldID is not occupied by a Player*/
    private boolean fieldisNotOccupied(int locationID){

        for(Spieler spieler : this.mitspieler){
            // Field is occupied
             if( spieler.getAktuelleFeldNr() == locationID ) return false;
        }

        return true;
    }

    /** moved Spieler, der an der Reihe ist, eine Position nach vorne oder nach hinten
    *  1 für nach vorne
    * -1 für nach hinten
    * Edge Cases:
    *   Spieler auf selbem Feld mit anderen -> geht ein Feld vor/zurück
    *   Spieler vor/danach sind mehrere auf selbem Feld -> geht trotzdem eins davor oder dahinter
    *   Spieler kommt ins Ziel (weil Spieler in front vor Ziel steht, oder bereits Spieler im Ziel sind -> Siegbedingungen
    *       werden überprüft, und wenn nicht erfüllt ein Feld vor Ziel gesetzt
    */
    private void movePositionActPlayer(int mode){

        // check mode for valid value 1 or -1
        if( mode != 1 && mode != -1 ){
            System.out.println("Error - invalid Input Argument for movePosition");
            return;
        }

        // wenn position.length > 1 -> nur ein Schritt vor/zurück, sonst von nächstem Spieler +/- 1
        ArrayList<Integer>position = getPosition();

        // actual player is on same field with other player(s) -> just move one field
        if( getPosition().size() > 1 ){

            // move to AktuelleFeldNr +/- 1
            this.mitspieler.get(this.indexCtr).moveToField(this.mitspieler.get(this.indexCtr).getAktuelleFeldNr() + mode, this.platzierungsliste.size());
            // is only called from hasenfeld -> no need to check for borders of spielbrett
        }
        // actual Player is on field alone
        else if( getPosition().size() == 1 ){

            if( mode == 1) this.mitspieler.get(this.indexCtr).moveToField( getFieldFrontPlayer(), this.platzierungsliste.size() );

            if( mode == -1 ) this.mitspieler.get(this.indexCtr).moveToField( getFieldBackPlayer(), this.platzierungsliste.size() );


        }
        else System.out.println("Error no Position for actual Player was found");


    }

    /** adds Player to game - called from Playerselection screen */
    public void addPlayers(List<Spieler> spieler){
        mitspieler.addAll(spieler);
    }

    /** sets Rsrc to Players in beginning of game */
    public void setStartRsrctoPlayers(){

        for(Spieler player : mitspieler){

            player.resetRessources(this.configMorePlayers);

        }
    }

    /** sets ConfigMorePlayers to 1 or 0, depending to number of Players in the beginning */
    public void setConfigMorePlayers(){

        if( mitspieler.size() > 4 ){
            this.configMorePlayers = 1;
        }
    }
    /** Debug Output fo actual player(Field + Karotten Left)
    * */
    private void debugRoundplayerOutput(){

        System.out.println(this.mitspieler.get(indexCtr).getName()+" auf Feld "+this.mitspieler.get(indexCtr).getAktuelleFeldNr()+" mit Karotten:  "+ this.mitspieler.get(indexCtr).getKarotten());
        controller.triggerFieldRender(mitspieler);
        controller.updatePlayerResources();
    }


    /**
     * Sets the controller for this SpielLogik Model
     * @param ctrl controller class
     */
    public void setController(Controller ctrl){
        controller = ctrl;
    }



}
