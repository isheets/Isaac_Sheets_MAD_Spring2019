package com.example.project02;

import java.util.ArrayList;
import java.util.List;

//TODO: think about the scopes of all the functions. what needs to be accessed publicly?

public class Game {
    //a list of from 0 to levelNum
    private List<Integer> sequence = new ArrayList<>();
    private int levelNum = 1;
    private int numLives = 3;

    //constructor
    public void Game() {

    }

    //randomize sequence
    private void newSequence() {

    }

    //start new level: get new sequence, fetch question,
    private void startLevel() {

    }

    //persist level num via shared prefs
    public void saveGame() {

    }

    //load persisted level from shared prefs
    public void loadGame() {

    }

    //check lives and either lose life or lose game
    public void wrong() {

    }


    public void gridTap(int buttonTapped) {

    }

    public void loseLife() {

    }

    public void loseLevel() {

    }
}
