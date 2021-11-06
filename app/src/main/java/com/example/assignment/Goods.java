package com.example.assignment;

import android.graphics.Bitmap;

public class Goods {
    private String name_goods;      // 등록할 문구
    private Bitmap image;       // 이미지 파일

    public Goods(Bitmap image, String name_goods){
        this.image = image;
        this.name_goods = name_goods;
    }

    public String getName_goods(){
        return name_goods;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap img){
        this.image = image;
    }

    public void setName_goods(String name_goods){
        this.name_goods = name_goods;
    }
}