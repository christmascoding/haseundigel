package src.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.image.Image;
public class Spieler implements Config {
    

    // never needed? private int id; // definiert durch intialisierung - genutzt zur identifikation des spielers
    private String name; // Name des Spielers
    // to do private Color color;
    private int karotten; // Anzahl Karotten eines Spielers auf der Hand
    private int salate;   // Anzahl Salate eines Spielers auf der Hand
    private boolean nextSuspend; // true wenn spieler nächste runde aussetzt
    private int aktuelleFeldNr; // Nr des aktuellen Feldes -> Start ist 0, Ziel ist 64
    private int lastWalkedWide;
    private Image playerImage; // holds Image Icon for Players to do  -  implement
    // to do change posFeldListe to Config with defines of ints to field names -> only one single point of truth
    private List<PositionsFeld> posFeldListe = new ArrayList<PositionsFeld>(); // enthält nach Instanziierung alle
                                                                                // Positionsfelder in Reihenfolge auf dem Spielbrett

    public Spieler(String name, int karotten, int salate, Image playerImage) { // to do how does inti Ressource data from Spiellogik get in here?
        this.name = name;
        this.karotten = karotten;
        this.salate = salate;
        this.nextSuspend = false;
        this.aktuelleFeldNr = 0;
        this.lastWalkedWide = 0;    // stores wide that was walked last -> fpr Aktionskarte "Letzter Zug kostet nichts"
        this.playerImage = playerImage;
        createPosFeldListe();
    }



    // ändere Anzahl von Karotten (negative oder positive Zahl)
    // returns true for success or false for error
    public boolean changeKarotten(int chKarotten) {

        // wenn nach potentiellem Abzug von Karotten noch 0 oder mehr vorhanden sind
        if( this.karotten + chKarotten >= 0 ){
            this.karotten = this.karotten + chKarotten;
            return true;
        }

        return false;

    }

    /** Aktion Vorwärts bewegen eines Spielers - returned
     *  1 - success
     *  3 - sucess Zieleinlauf
     * -1 - Feld exisitiert nicht
     * -2 - nicht genug Karotten
     * -3 - Zieleinlauf nicht zulässig
     * -4 - Salatfeld anvisiert, darf aber nicht betreten werden -> keine Salate mehr übrig
     * -10 - unzulässige Eingabeparameter (0 oder negative Zahl)
    */
    public int moveForward(int wide, int numFinishedPlayers){

        if( wide <= 0 ) return -10; // nur vowärts laufen zulässig

        if( this.aktuelleFeldNr + wide >= numFeldListe.length ) return -1; // anvisiertes Feld liegt nach Ziel

        if( this.posFeldListe.get(this.aktuelleFeldNr + wide) == PositionsFeld.Salatfeld && this.salate == 0 ) return -4; //Salatfeld anvisiert, darf aber nicht betreten werden -> keine Salate mehr übrig
        // Zieleinlauf
        if( this.aktuelleFeldNr + wide == numFeldListe.length-1 ){

            if( validFinish(wide, numFinishedPlayers) ){

                if( changeKarotten(-1 * Renntafel[wide]) ){

                    this.aktuelleFeldNr += wide; // set to finish field
                    this.lastWalkedWide = wide;
                    return 3;

                }

                else return -2;

            }

            else return -3;

        }

        // normaler, zulässiger Vorwärtszug
        else{

            if( changeKarotten(-1 * Renntafel[wide]) ){
                this.aktuelleFeldNr += wide;
                this.lastWalkedWide = wide;
                return 1;
            }

            else return -2;
        }

    }

    /* moves this player to specific field, throws error if field does not exist
    * if targeted new Field is finish, but requirements for finishing are not met -> player is placed to last field for finish
     */
    public void moveToField(int newFieldNr, int numFinishedPlayers){

        // valid fieldNr
        if( newFieldNr >=0 && newFieldNr < Config.numFeldListe.length - 1 ){

            this.aktuelleFeldNr = newFieldNr;

        }
        // Finish -> check for requirements
        else if ( newFieldNr == Config.numFeldListe.length - 1 ) {

            if( validFinish(0, numFinishedPlayers) ) this.aktuelleFeldNr = newFieldNr;

            else this.aktuelleFeldNr = newFieldNr - 1;

        }
        // invalid Input argument
        else{
            System.out.println("Error - moveToField was used with invalid Input arguments");
        }


    }

    /* moves a Player backwards (only for Igelfeld) + increases Karotten with 10*wide
    * newFeldNr needs to be validated before
    * */
    public void moveBackwardto(int newFeldNr){

        changeKarotten((this.aktuelleFeldNr - newFeldNr) * 10);
        this.lastWalkedWide = newFeldNr - this.aktuelleFeldNr;
        this.aktuelleFeldNr = newFeldNr;

    }

    // Spieler kann 10 Karotten nehmen oder abgeben; nimmt automatisch auf, wenn < 10 karotten vorhanden
    public void karottenFeldAction(){

        if( this.karotten >= 10 ){

            // to do replace by GUI interaction
            System.out.println("0 - abgeben; sonst aufnehmen");
            Scanner scan = new Scanner(System.in);
            int inp = scan.nextInt();

            // abgeben
            if( inp == 0 ){
                // wenn mehr als 10 Karotten vorhanden - eigentlich schon gecheckt
                if( changeKarotten(-10) ) return;
            }

        }

        // aufnehmen
        changeKarotten(10);
    }

    // Aktion Salat essen eines Spielers
    public void eatSalate(){
        if( this.salate > 0 ){
            this.salate --;
        }

    }

    // Aktion von Aktionskarte -> letzter Zug kostet nichts
    public void costLastRoundback(){
        changeKarotten( Renntafel[lastWalkedWide] );
    }

    // bei Zwangsrücksetzung auf Start, eines Spielers aufgerufen
    public void resetRessources(){
        this.karotten = initKarotten[0];  // to do how does inti Ressource data from Spiellogik get in here?
        this.salate = initSalate;
    }

    // zwingt Spieler die nächste Runde auszusetzen
    public void hasToSuspendNextRound(){
        this.nextSuspend = true;
    }
    // Aktion aussetzen eines Spielers
    public void suspend(){
        if(this.nextSuspend){
            this.nextSuspend = false;
            return;
        }
        System.out.println("An Error occured - this Player --" + this.name + " --has not to suspend");
    }

    // prüft ob ein Zieleinlauf zulässig ist: nicht zu viele Karotten, 0 Salate - prüft nicht ob Karotten reichen
    public boolean validFinish(int wide, int numFinishedPlayers){

        if(        this.salate == finishSalate
                && this.karotten - Renntafel[wide] <= finishKarottenMultiply * (numFinishedPlayers+1) )
                return true;

        return false;
    }


    // sets lastWalkedWide to 0
    public void didNotWalked(){
        this.lastWalkedWide = 0;
    }

    public int getSalate(){
        return this.salate;
    }

    public int getKarotten() {
        return this.karotten;
    }

    public String getName(){
        return this.name;
    }

    public boolean isNextSuspend() {
        return this.nextSuspend;
    }

    public int getAktuelleFeldNr() {
        return this.aktuelleFeldNr;
    }

    public Image getPlayerImage() {
        return this.playerImage;
    }

    public int getLastWalkedWide(){
        return this.lastWalkedWide;
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
}
