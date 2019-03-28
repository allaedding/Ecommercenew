package com.allaeddine.guissou.ecommerce;
import android.graphics.Bitmap;
/**
 * Created by guissous on 05-05-2017.
 */

public class AddToolItem {
    public String ImagePath;
    public int Image;
    public  Bitmap bitmap;
    AddToolItem(String ImagePath, int Image, Bitmap bitmap){
        this.ImagePath=ImagePath;
        this.Image=Image;
        this.bitmap=bitmap;
    }
}
