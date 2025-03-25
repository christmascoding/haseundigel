package src.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aktionskartenstapel {

    private final int numberCards = 9;
    private List<Integer> idList = new ArrayList<Integer>(); // idListe enthält id für jede der 9 Aktionskarten
    private List<String>  cardtextList = new ArrayList<String>(); // enthält nach initalisierung Texte der Aktionskarten


    public Aktionskartenstapel(){

        // füge id von 0-8 der id liste hinzu
        for(int i=0; i<this.numberCards; i++){
            idList.add(i);
        }

        try {
            // JSON-Datei laden
            String content = new String(Files.readAllBytes(Path.of("src/model/Aktionskarten.json")));

            // JSON-Array parsen
            JSONArray jsonArray = new JSONArray(content);

            // Text in ArrayList von AKtionskartenstapel übertragen
            for(int i = 0; i<this.numberCards; i ++){

                JSONObject Card = jsonArray.getJSONObject(i);
                cardtextList.add(Card.getString("text"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // mischt den Stapel von Aktionskarten
    public void mischen(){
        Collections.shuffle(idList);
    }

    // gibt Beschreibung der obersten Aktionskarte zurück und fügt Karte unten am Stapel wieder ein
    public String getAktionsBeschreibung(){

        String ret = cardtextList.get( idList.getFirst() ); // gebe String der obersten Karte zurück

        // setze Erstes Element ans ende
        int firstElement = idList.remove(0);
        idList.add(firstElement);

        return ret;

    }
}
