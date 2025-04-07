package src.model;

import src.model.Spieler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DynConfig {

    private static List <Spieler> finishedPlayers= new ArrayList<>();
    private static int numberOfPlayers;

    private DynConfig(){ // private Constructor -> no instances

    }

    public void SetNumberofPlayers(int number){
        numberOfPlayers = number;
    }

    public int getPlayerConfig() {

        if( numberOfPlayers >= 4 ){
            return 1;
        }

        return 0;
    }

    public void addfinishedPlayer(Spieler finishedplayer){

        finishedPlayers.add(finishedplayer);

    }

    public int getNumberofFinsihedPlayers(){
        return finishedPlayers.size();
    }

}
