package com.slient.pikachugame.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.slient.pikachugame.R;
import com.slient.pikachugame.board.model.Line;
import com.slient.pikachugame.spritemap.SpriteMapFactory;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

/**
 * Created by silent on 3/28/2018.
 */
public class PikachuBoard {

    public static final int NOT_EMPTY = 1;

    private int colQty;
    private int rowQty;

    private int bitmapWidth;
    private int bitmapHeight;

    private List<Line> lines;

    private Context context;

    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;

    private Bitmap defaultBitmap;
    private Bitmap correctBitmap;
    private Bitmap[] bitmaps;
    private Bitmap[][] bitmapOnBoard;
    private int[][] boardIndex;

    private int chosenCard;
    private int oldColIndex;
    private int oldRowIndex;
    private int celWidth;
    private int celHeight;
    private int celWidthBM;
    private int celHeightBM;

    private int maxTurn;
    private int score;

    private TextView scoreText;
    private TextView turnText;

    public PikachuBoard(Context context, int rowQty, int colQty, int bitmapWidth, int bitmapHeight, TextView scoreText, TextView turnText) {
        this.context = context;
        this.colQty = colQty;
        this.rowQty = rowQty;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;
        this.scoreText = scoreText;
        this.turnText = turnText;
        init();
    }

    private void init() {
        celWidthBM = bitmapWidth / colQty;
        celHeightBM = bitmapHeight / rowQty;
        bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);
        maxTurn = 10;
        score = 0;
        chosenCard = 0;
        scoreText.setText("Score " + score);
        turnText.setText("Turn " + maxTurn);
        Bitmap spriteMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_map);
        bitmaps = SpriteMapFactory.split(spriteMap, colQty / 2, colQty / 2);
        defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_card);
        correctBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.correct_card);
        paint = new Paint();
        paint.setStrokeWidth(2);
        setupLines();
        setupMatrixBitmapAndIndex();
    }

    private void setupMatrixBitmapAndIndex() {
        boardIndex = new int[rowQty][colQty];
        bitmapOnBoard = new Bitmap[rowQty][colQty];
        int[] dup = new int[bitmaps.length];
        Random random = new Random();
        int index;
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                while (true) {
                    index = random.nextInt(4);
                    if (dup[index] < 5) {
                        dup[index] = dup[index] + 1;
                        bitmapOnBoard[i][j] = bitmaps[random.nextInt(bitmaps.length)];
                        Log.d("TRUNG", "setupMatrixBitmapAndIndex: " + index);
                        break;
                    }
                }
            }
        }
    }

    private void setupLines() {
        lines = new ArrayList<>();
        celWidth = bitmapWidth / colQty;
        celHeight = bitmapHeight / rowQty;
        for (int i = 0; i <= colQty; i++) {
            lines.add(new Line(0, i * celWidth, bitmapWidth, i * celWidth));
        }
        for (int i = 0; i <= rowQty; i++) {
            lines.add(new Line(i * celHeight, 0, i * celHeight, bitmapHeight));
        }
    }

    public Bitmap drawPikachuBoard() {
        for (Line line : lines) {
            canvas.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY(), paint);
        }
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                canvas.drawBitmap(defaultBitmap, new Rect(0, 0, defaultBitmap.getWidth(), defaultBitmap.getHeight()),
                        new Rect(j * celWidthBM, i * celHeightBM, (j + 1) * celWidthBM, (i + 1) * celHeightBM), paint);
            }
        }
        return bitmap;
    }

    public void onTouch(final View view, MotionEvent event) {
        if (maxTurn < 0) {
            Toast.makeText(context, "You are loose", Toast.LENGTH_SHORT).show();
            return;
        } else if (score < 0) {
            Toast.makeText(context, "You are winner", Toast.LENGTH_SHORT).show();
            return;
        }

        celWidth = view.getWidth() / colQty;
        celHeight = view.getHeight() / rowQty;

        final int colIndex = (int) (event.getX() / celWidth);
        final int rowIndex = (int) (event.getY() / celHeight);

        if (boardIndex[rowIndex][colIndex] != NOT_EMPTY) {
            boardIndex[rowIndex][colIndex] = NOT_EMPTY;
            canvas.drawBitmap(bitmapOnBoard[rowIndex][colIndex], new Rect(0, 0, bitmapOnBoard[rowIndex][colIndex].getWidth(), bitmapOnBoard[rowIndex][colIndex].getHeight()),
                    new Rect(colIndex * celWidthBM, rowIndex * celHeightBM, (colIndex + 1) * celWidthBM, (rowIndex + 1) * celHeightBM), paint);
            chosenCard++;

            if (chosenCard == 2) {

                turnText.setText("Turn " + maxTurn);
                maxTurn--;
                chosenCard = 0;
                if (bitmapOnBoard[oldRowIndex][oldColIndex].equals(bitmapOnBoard[rowIndex][colIndex])) {

                    canvas.drawBitmap(correctBitmap, new Rect(0, 0, correctBitmap.getWidth(), correctBitmap.getHeight()),
                            new Rect(colIndex * celWidthBM, rowIndex * celHeightBM, (colIndex + 1) * celWidthBM, (rowIndex + 1) * celHeightBM), paint);
                    canvas.drawBitmap(correctBitmap, new Rect(0, 0, correctBitmap.getWidth(), correctBitmap.getHeight()),
                            new Rect(oldColIndex * celWidthBM, oldRowIndex * celHeightBM, (oldColIndex + 1) * celWidthBM, (oldRowIndex + 1) * celHeightBM), paint);
                    Toast.makeText(context, "Giong nhau", Toast.LENGTH_SHORT).show();
                    score++;
                    scoreText.setText("Score " + score);
                } else {
                    Toast.makeText(context, "Khac nhau", Toast.LENGTH_SHORT).show();
                    canvas.drawBitmap(defaultBitmap, new Rect(0, 0, defaultBitmap.getWidth(), defaultBitmap.getHeight()),
                            new Rect(colIndex * celWidthBM, rowIndex * celHeightBM, (colIndex + 1) * celWidthBM, (rowIndex + 1) * celHeightBM), paint);
                    canvas.drawBitmap(defaultBitmap, new Rect(0, 0, defaultBitmap.getWidth(), defaultBitmap.getHeight()),
                            new Rect(oldColIndex * celWidthBM, oldRowIndex * celHeightBM, (oldColIndex + 1) * celWidthBM, (oldRowIndex + 1) * celHeightBM), paint);

                    boardIndex[rowIndex][colIndex] = 0;
                    boardIndex[oldRowIndex][oldColIndex] = 0;
                }
            }

            if (maxTurn < 0) {
                view.setFocusable(false);
                scoreText.setText("You are loose");
            } else if (isGameOver()) {
                score = -1;
                scoreText.setText("You are winner");
                Toast.makeText(context, "You are winner", Toast.LENGTH_SHORT).show();
            }
                oldRowIndex = rowIndex;
                oldColIndex = colIndex;
        }
        view.invalidate();
    }

    public boolean isGameOver() {
        for (int i = 0; i < rowQty; i++) {
            for (int j = 0; j < colQty; j++) {
                if (boardIndex[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
