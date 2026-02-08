package Helper;

public enum GameMode {
    NORMAL('N'),
    SPEED('S'),
    REVERSE('R');

    private final char shortened;

    GameMode(char shortened) {
        this.shortened = shortened;
    }

    public char getShort() {
        return shortened;
    }
}