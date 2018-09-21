package com.jaimedediego.cubemaster.methods;

import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ScrambleMethods {

    private static ScrambleMethods instance;

    private ScrambleMethods() {
    }

    public static ScrambleMethods getInstance() {
        if (instance == null) {
            instance = new ScrambleMethods();
        }
        return instance;
    }

    public void getCurrentNxNxNPuzzleNotation() {
        int size = Integer.parseInt(DatabaseMethods.getInstance().getCurrentPuzzleName().substring(0, 1));
        List<String> allSides = new ArrayList<>();
        if (size % 2 != 0) {
            allSides.addAll(Constants.getInstance().oddNumberNxNxN);
        }
        /*if(PrefsMethods.getInstance().isWholeCubeRotationsEnabled()){
            allSides.add("X");
            allSides.add("Y");
            allSides.add("Z");
        }*/
        List<String> extSides = Constants.getInstance().NxNxN;
        for (int i = 1; i < (size / 2); i++) {
            for (int j = 0; j < extSides.size(); j++) {
                String subindex = extSides.get(j) + (i + 1);
                subindex = subindex.replace("2", "\u2082");
                allSides.add(subindex);
                allSides.add((i + 1) + extSides.get(j));
            }
        }
        allSides.addAll(extSides);
        Session.getInstance().currentPuzzleNotation = allSides;
    }

    public String scramble() {
        StringBuilder stringBuilder = new StringBuilder();
        List<String> movements = new ArrayList<>(Session.getInstance().currentPuzzleNotation);
        int size = Integer.parseInt(DatabaseMethods.getInstance().getCurrentPuzzleName().substring(0, 1));
        for (String face : new ArrayList<>(movements)) {
            movements.add(face + "'");
            movements.add(face + "2");
        }
        Collections.sort(movements);
        String faceMoved = "";
        List<String> prevMovesDisabled = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < PrefsMethods.getInstance().getScrambleLength(); i++) {
            if (!faceMoved.equals("")) {
                for (String j : new ArrayList<>(movements)) {
                    if (j.startsWith(faceMoved)) {
                        movements.remove(j);
                        prevMovesDisabled.add(j);
                    }
                }
            }
            String movement = movements.get(random.nextInt(movements.size()));
            stringBuilder.append(movement).append("  ");
            if (size / 2 >= 2) {
                if (movement.length() == 1) {
                    faceMoved = movement.substring(0, 1);
                } else {
                    faceMoved = movement.substring(0, 2);
                }
            } else {
                faceMoved = movement.substring(0, 1);
            }
            movements.addAll(prevMovesDisabled);
            prevMovesDisabled.clear();
        }
        return stringBuilder.toString();
    }
}
