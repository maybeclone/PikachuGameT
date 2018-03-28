package com.slient.pikachugame.spritemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;

import com.slient.pikachugame.R;

/**
 * Created by silent on 3/28/2018.
 */
public class SpriteMapFactory {

    public static Bitmap[] split(Bitmap spriteMap, int col, int row){
        Bitmap[] bitmaps = new Bitmap[col*row];

        int widthBitmap = spriteMap.getWidth()/col;
        int heightBitmap = spriteMap.getHeight()/row;

        for(int i=0 ; i<col; i++){
            bitmaps[i] = Bitmap.createBitmap(spriteMap, i*widthBitmap, 0, widthBitmap, heightBitmap);
            bitmaps[i+col] =  Bitmap.createBitmap(spriteMap, i*widthBitmap, heightBitmap, widthBitmap, heightBitmap);
        }


        return bitmaps;
    }
}
