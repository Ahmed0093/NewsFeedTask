package com.development.task.newsfeedtask.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


import java.io.ByteArrayOutputStream;

@Entity
public class WeatherModel {
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] imgBitmap;
    @PrimaryKey(autoGenerate = true)
    private  int id;
    @ColumnInfo(name = "imguri")
    private String imgUri;
    @ColumnInfo(name = "imgpath")
    private String imagepath;
    @ColumnInfo(name = "weatherdescription")
    private String descriptionWeather;

    public byte[] getImgBitmap() {
        return imgBitmap;
    }

    public String getImgUri() {
        return imgUri;
    }

    public String getDescriptionWeather() {
        return descriptionWeather;
    }

    public void setDescriptionWeather(String descriptionWeather) {
        this.descriptionWeather = descriptionWeather;
    }

    public void setImgBitmap(byte[] imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public Bitmap getImgBitmapConverted() {
        return BitmapFactory.decodeByteArray(imgBitmap, 0, imgBitmap.length);
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = getBitmap(imgBitmap);
    }

    public Uri getImgUriConverted() {
        return  Uri.parse(imgUri);
    }



    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri.toString();
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    private  byte[] getBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
