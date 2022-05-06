package com.bluehomestudio.luckywheel;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by mohamed on 22/04/17.
 */

public class WheelItem implements Serializable {

    public int color;
    public CustomBitmap bitmap;
    public String text;
    public int textColor;

    public WheelItem(int color, Bitmap bitmap) {
        this.color = color;
        this.bitmap = new CustomBitmap(bitmap);
        textColor = 0xffffffff;
    }

    public WheelItem(int color, Bitmap bitmap, String text) {
        this.color = color;
        this.bitmap = new CustomBitmap(bitmap);
        this.text = text;
        textColor = 0xffffffff;
    }

    public WheelItem(int color, Bitmap bitmap, String text, int textColor) {
        this.color = color;
        this.bitmap = new CustomBitmap(bitmap);
        this.text = text;
        this.textColor = textColor;
    }
    public void setColor(int color)
    {
        this.color = color;
    }
    public void setText(String text){
        this.text = text;
    }
    public void setTextColor(int textColor){
        this.textColor = textColor;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WheelItem wheelItem = (WheelItem) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return color == wheelItem.color && Objects.equals(bitmap, wheelItem.bitmap) && Objects.equals(text, wheelItem.text) && textColor == wheelItem.textColor;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(color, bitmap, text, textColor);
        }
        return 0;
    }
}
