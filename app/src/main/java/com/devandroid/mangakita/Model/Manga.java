package com.devandroid.mangakita.Model;

public class Manga {
    public int ID;
    public String Name;
    public String Image;

    public Manga() {
    }

    public Manga(int ID, String name, String image) {
        this.ID = ID;
        Name = name;
        Image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
