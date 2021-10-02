package com.example.triviaproject;

public class Player {
    String userName;
    String password;
    String image;
    String musicPlay;

    public Player(String name, String setPassword, String setImage, String setMusicPlay){
        this.userName = name;
        this.password = setPassword;
        this.image = setImage;
        this.musicPlay = setMusicPlay;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMusicPlay() {
        return musicPlay;
    }

    public void setMusicPlay(String password) {
        this.musicPlay = password;
    }

}
