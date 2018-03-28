package com.slient.pikachugame;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slient.pikachugame.board.PikachuBoard;
import com.slient.pikachugame.spritemap.SpriteMapFactory;

public class MainActivity extends AppCompatActivity{

    private ImageView boardGameImageView;
    private TextView scoreText;
    private TextView turnText;
    private PikachuBoard pikachuBoard;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardGameImageView = findViewById(R.id.boardGameImageView);
        scoreText = findViewById(R.id.scoreText);
        turnText = findViewById(R.id.turnText);
        pikachuBoard = new PikachuBoard(this, 4, 4, 400, 400, scoreText, turnText);

        boardGameImageView.setImageBitmap(pikachuBoard.drawPikachuBoard());
        boardGameImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pikachuBoard.onTouch(boardGameImageView, event);
                return false;
            }
        });
    }

}
