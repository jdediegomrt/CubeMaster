package com.jaimedediego.cubemaster.methods;

import com.jaimedediego.cubemaster.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ScrambleMethods {

    private static ScrambleMethods instance;

    private ScrambleMethods() {}

    public static ScrambleMethods getInstance() {
        if (instance == null) {
            instance = new ScrambleMethods();
        }
        return instance;
    }

    public String[] getScrambleLengths(){
        String[] lengths = new String[Constants.getInstance().maxScrambleLength];
        for(int i = 0; i< lengths.length; i++){
            lengths[i] = ""+(i+1);
        }
        return lengths;
    }

    public List<String> getNxNxNNotation(int size){
        List<String> allSides = new ArrayList<>();
        if(size%2!=0){
            allSides.addAll(Constants.getInstance().oddNumberNxNxN);
        }
        /*if(PrefsMethods.getInstance().isWholeCubeRotationsEnabled()){
            allSides.add("X");
            allSides.add("Y");
            allSides.add("Z");
        }*/
        List<String> extSides = Constants.getInstance().NxNxN;
        for(int i = 0; i < (size/2); i++){
            for(int j = 0; j < extSides.size(); j++){
                if(i>0) {
                    allSides.add(extSides.get(j) + (i + 1));
                    allSides.add((i + 1) + extSides.get(j));
                }
            }
        }
        allSides.addAll(extSides);
        return allSides;
    }

    //TODO: implement
    public List<String> scramble(List<String> notation, int moves){
        return null;
    }
}
