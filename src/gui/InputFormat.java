package src.gui;

public class InputFormat {

    private int walkWide;
    private boolean walkForwardPressed;
    private boolean walkBackwardPressed;
    private boolean saladEatenPressed;
    private boolean getCarrotPressed;
    private boolean eatCarrotsPressed;

    public InputFormat(int walkWide, boolean walkForwardPressed, boolean walkBackwardPressed,
                       boolean saladEatenPressed, boolean getCarrotPressed, boolean eatCarrotsPressed) {
        this.walkWide = walkWide;
        this.walkForwardPressed = walkForwardPressed;
        this.walkBackwardPressed = walkBackwardPressed;
        this.saladEatenPressed = saladEatenPressed;
        this.getCarrotPressed = getCarrotPressed;
        this.eatCarrotsPressed = eatCarrotsPressed;
    }

    public int getWalkWide() {
        return walkWide;
    }

    public void setWalkWide(int walkWide) {
        this.walkWide = walkWide;
    }

    public boolean isWalkForwardPressed() {
        return walkForwardPressed;
    }

    public void setWalkForwardPressed(boolean walkForwardPressed) {
        this.walkForwardPressed = walkForwardPressed;
    }

    public boolean isWalkBackwardPressed() {
        return walkBackwardPressed;
    }

    public void setWalkBackwardPressed(boolean walkBackwardPressed) {
        this.walkBackwardPressed = walkBackwardPressed;
    }

    public boolean isSaladEatenPressed() {
        return saladEatenPressed;
    }

    public void setSaladEatenPressed(boolean saladEatenPressed) {
        this.saladEatenPressed = saladEatenPressed;
    }

    public boolean isGetCarrotPressed() {
        return getCarrotPressed;
    }

    public void setGetCarrotPressed(boolean getCarrotPressed) {
        this.getCarrotPressed = getCarrotPressed;
    }

    public boolean isEatCarrotsPressed() {
        return eatCarrotsPressed;
    }

    public void setEatCarrotsPressed(boolean eatCarrotsPressed) {
        this.eatCarrotsPressed = eatCarrotsPressed;
    }
}
