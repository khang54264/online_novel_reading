package com.example.btlandroid;

public class Novel {
    private String novelName;
    private String novelAuthor;
    private int novelImage;
    public Novel(String novelName, String novelAuthor, int novelImage) {
        this.novelName = novelName;
        this.novelAuthor = novelAuthor;
        this.novelImage = novelImage;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getNovelAuthor() {
        return novelAuthor;
    }

    public void setNovelAuthor(String novelAuthor) {
        this.novelAuthor = novelAuthor;
    }

    public int getNovelImage() {
        return novelImage;
    }

    public void setNovelImage(int novelImage) {
        this.novelImage = novelImage;
    }


}
