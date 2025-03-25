package src.model;

import java.awt.*;

public class Spieler {
    

    private int id; // definiert durch intialisierung - genutzt zur identifikation des spielers
    private String name; // Name des Spielers
    private Color color;
    private int karotten; // Anzahl Karotten eines Spielers auf der Hand
    private int salate;   // Anzahl Salate eines Spielers auf der Hand
    private boolean nextSuspend; // true wenn spieler nächste runde aussetzt


    public Spieler(int id, String name, int karotten, int salate) { // to do how does inti Ressource data from Spiellogik get in here?
        this.id = id;
        this.name = name;
        this.karotten = karotten;
        this.salate = salate;
        this.nextSuspend = false;
    }



    // ändere Anzahl von Karotten (negative oder positive Zahl)
    // returns 1 for success or 0 for error
    public int changeKarotten(int chKarotten) {

        // wenn nach potentiellem Abzug von Karotten noch 0 oder mehr vorhanden sind
        if(this.karotten + chKarotten >= 0){
            this.karotten = this.karotten + chKarotten;
            return 1;
        }

        return 0;

    }



    public void eatSalate(){
        this.salate --;
    }

    // bei Zwangsrücksetzung auf Start, eines Spielers aufgerufen
    public void resetRessources(){
        this.karotten = Config.initKarotten[0];  // to do how does inti Ressource data from Spiellogik get in here?
        this.salate = Config.initSalate;
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
}
