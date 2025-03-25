package src;

import java.awt.*;

public class Spieler {

    private int id;
    private String name;
    private Color color;
    private int karotten; // Anzahl Karotten eines Spielers auf der Hand
    private int salate;   // Anzahl Salate eines Spielers auf der Hand


    public Spieler(int karotten, int salate) { // to do how does inti Ressource data from Spiellogik get in here?
        this.karotten = karotten;
        this.salate = salate;
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
        this.karotten = karotten;  // to do how does inti Ressource data from Spiellogik get in here?
        this.salate = 3;
    }



    public int getSalate(){
        return this.salate;
    }



    public int getKarotten() {
        return this.karotten;
    }
}
