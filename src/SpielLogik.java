package src;

public class SpielLogik implements Config{

    private final int initKarotten = Config.initKarotten[0]; // to do add to some creation steps to also use [1] if 5-6 players
    private final int initSalate = Config.initSalate;




    public int getInitKarotten() {
        return initKarotten;
    }

    public int getInitSalate() {
        return initSalate;
    }
}
