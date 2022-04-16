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
    public Bitmap bitmap;
    public String text;

    public WheelItem(int color, Bitmap bitmap) {
        this.color = color;
        this.bitmap = bitmap;
    }

    public WheelItem(int color, Bitmap bitmap, String text) {
        this.color = color;
        this.bitmap = bitmap;
        this.text = text;
    }
    public void setColor(int color)
    {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WheelItem wheelItem = (WheelItem) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return color == wheelItem.color && Objects.equals(bitmap, wheelItem.bitmap) && Objects.equals(text, wheelItem.text);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(color, bitmap, text);
        }
        return 0;
    }
}
