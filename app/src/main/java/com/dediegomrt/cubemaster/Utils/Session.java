package com.dediegomrt.cubemaster.Utils;

public class Session {

    private static Session instance;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public int currentUserId=1;
    public int currentPuzzleId;

    public int darkColorTheme=0;
    public int lightColorTheme=0;

}
