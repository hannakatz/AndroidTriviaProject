package com.example.triviaproject;

public class PlayerScore {
    private String gameName;
    public int score;

    public PlayerScore(int score, String gameName){
        this.score = score;
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
