package src.model;

public interface Config {

    public static int [] initKarotten = {68, 98}; // Startwert Karotten eines jeden Spielers
    public static int initSalate = 3; // Startwert Salate eines jeden Spielers
    public static int finishKarottenMultiply = 10; // zulässiger Höchstendwert für Anzahl von Karotten eines Speielrs bei Zieleinlauf, multipliziert mit Posiiton bei Zieleinlauf
    public static int  finishSalate = 0; // zulässiger Endwert für Anzahl von Salaten eines Spielers bei Zieleinlauf
    public static int [] numFeldListe = {0,1,2,1,5,2,1,7,8,6,4,8,5,2,1,8,3,4,6,8,5,2,7,4,8,1,2,6,5,4,8,
                                         1,3,2,1,4,5,8,2,1,2,4,7,8,5,6,1,4,3,2,8,1,5,4,6,2,8,7,1,2,4,1,7,1,9}; // 65 lang
                                    // enthält die Reihenfolge der Felder auf dem Spielbrett gemäß des PositionsFeld Enums
    public static int firstIgelFeld = 8;

    public static int [] Renntafel = {0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 66, 78, 91, 105,
                                      120, 136, 153, 171, 190, 210, 231, 253, 276, 300, 325, 351,
                                      378, 406, 435, 465, 496, 528, 561, 595, 630, 666, 703, 741,
                                      780, 820, 861, 903, 946, 990, 1035, 1081, 1128, 1176, 1225,
                                      1275, 1326, 1378, 1431, 1485, 1540, 1596, 1653, 1711, 1770, 1830,
                                      1891, 1953, 2016, 2080}; //enthält Karottenkosten / [index] vorwärts zu laufendem Feld
}
