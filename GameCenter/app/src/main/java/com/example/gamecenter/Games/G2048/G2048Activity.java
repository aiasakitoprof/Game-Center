package com.example.gamecenter.Games.G2048;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.gamecenter.Games.Senku.SenkuActivity;
import com.example.gamecenter.Main.MainActivity;
import com.example.gamecenter.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

public class G2048Activity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    // Attributes:
    private int height, width;
    private BoardCell[][] board;
    private ArrayList<Tile> tiles;
    private int score;
    private boolean gameOver;

    private Random random;

    GestureDetector gestureDetector;

    // Constructor & Main:
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048);

        Button menu = findViewById(R.id.menu2048);
        menu.setOnClickListener(v -> {
            Intent intent = new Intent(G2048Activity.this, MainActivity.class);
            startActivity(intent);
        });

        gestureDetector = new GestureDetector(this, this);

        tiles = new ArrayList<>();
        score = 0;

        width = 4;
        height = 4;
        board = new BoardCell[height][width];
        initializeBoard();

        random = new Random();
        startGame();
    }


    // Private methods:
    // Start game:
    private void initializeBoard() {

        Resources resources = getResources();
        String packageName = getPackageName();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                String id = "F" + i + j;
                FrameLayout frameLayout = findViewById(resources.getIdentifier(id, "id", packageName));
                BoardCell boardCell = new BoardCell(i, j, frameLayout);
                board[i][j] = boardCell;
            }
        }
    }
    private void startGame() {

        for (int k = 0; k < 2; k++) {

            boolean control = true;
            while (control) {

                int i = random.nextInt(height);
                int j = random.nextInt(width);

                if (!isOccupied(i, j)) {

                    createTile(i, j);
                    control = false;
                }
            }
        }
        updateScore();
    }


    // Board verifications:
    private boolean isOccupied(int i, int j) {

        return board[i][j].getTile() != null;
    }
    private boolean isInBoard(int i, int j) {

        if (i >= 0 && i < height) {
            if (j >= 0 && j < width) {
                return true;
            }
        }
        return false;
    }
    private boolean canFuse(int i, int j, Tile tile) {

        Tile target = board[i][j].getTile();

        if (target != null) {
            if (!tile.hasMerged() && !target.hasMerged()) {
                if (tile.getValue() == target.getValue()) {

                    return true;
                }
            }
        }
        return false;
    }


    // Board modification:
    private void createTile(int i, int j) {

        TextView textView = createTextView();
        Tile tile = new Tile(i, j, textView);

        tiles.add(tile);
        board[i][j].setTile(tile);

        addTextViewToUI(i, j, tile.getTextView());
    }
    private void fuse(int i, int j, Tile tile, Iterator<Tile> iterator) {

        Tile target = board[i][j].getTile();
        int newValue = target.getValue() * 2;
        score += newValue;

        target.setValue(newValue);
        target.setMerged(true);

        deleteTextView(tile.getI(), tile.getJ());
        updateTextViewValue(target, newValue);

        board[tile.getI()][tile.getJ()].setTile(null);
        iterator.remove();
    }


    // Movement:
    private void move(String direction) {

        switch (direction) {
            case "up":
                moveUp();
                break;
            case "down":
                moveDown();
                break;
            case "left":
                moveLeft();
                break;
            case "right":
                moveRight();
                break;
        }

        updateScore();
        checkGameOver();

        if (!gameOver) {

            for (Tile tile : tiles) {

                tile.setMerged(false);
            }

            boolean control = true;

            while (control) {

                int i = random.nextInt(height);
                int j = random.nextInt(width);

                if (!isOccupied(i, j)) {

                    createTile(i, j);
                    control = false;
                }
            }
        } else {
            gameOverDialog();
        }
    }
    private void moveUp() {

        tiles.sort(Comparator.comparingInt(Tile::getI));
        Iterator<Tile> iterator = tiles.iterator();

        while (iterator.hasNext()) {

            Tile tile = iterator.next();
            boolean control = true;
            int i = tile.getI();
            int j = tile.getJ();

            while (i > 0 && control) {

                i--;

                if (isInBoard(i,j)) {
                    if (isOccupied(i,j)) {
                        if (canFuse(i, j, tile)) {

                            fuse(i, j, tile, iterator);
                        }
                        control = false;

                    } else {

                        moveTextView(tile.getI(), tile.getJ(), i, j);
                        board[tile.getI()][tile.getJ()].setTile(null);

                        tile.setPosition(i, j);
                        board[i][j].setTile(tile);
                    }
                } else {
                    control = false;
                }
            }

        }
    }
    private void moveDown() {

        tiles.sort(Comparator.comparingInt(Tile::getI).reversed());
        Iterator<Tile> iterator = tiles.iterator();

        while (iterator.hasNext()) {

            Tile tile = iterator.next();
            boolean control = true;
            int i = tile.getI();
            int j = tile.getJ();

            while (i < height - 1 && control) {

                i++;

                if (isInBoard(i,j)) {
                    if (isOccupied(i,j)) {
                        if (canFuse(i, j, tile)) {

                            fuse(i, j, tile, iterator);
                        }
                        control = false;

                    } else {

                        moveTextView(tile.getI(), tile.getJ(), i, j);
                        board[tile.getI()][tile.getJ()].setTile(null);

                        tile.setPosition(i, j);
                        board[i][j].setTile(tile);
                    }
                } else {
                    control = false;
                }
            }
        }
    }
    private void moveLeft() {

        tiles.sort(Comparator.comparingInt(Tile::getJ));
        Iterator<Tile> iterator = tiles.iterator();

        while (iterator.hasNext()) {

            Tile tile = iterator.next();
            boolean control = true;
            int i = tile.getI();
            int j = tile.getJ();

            while (j > 0 && control) {

                j--;

                if (isInBoard(i,j)) {
                    if (isOccupied(i,j)) {
                        if (canFuse(i, j, tile)) {

                            fuse(i, j, tile, iterator);
                        }
                        control = false;

                    } else {
                        moveTextView(tile.getI(), tile.getJ(), i, j);
                        board[tile.getI()][tile.getJ()].setTile(null);

                        tile.setPosition(i, j);
                        board[i][j].setTile(tile);
                    }
                } else {
                    control = false;
                }
            }
        }
    }
    private void moveRight() {

        tiles.sort(Comparator.comparingInt(Tile::getJ).reversed());
        Iterator<Tile> iterator = tiles.iterator();

        while (iterator.hasNext()) {

            Tile tile = iterator.next();
            boolean control = true;
            int i = tile.getI();
            int j = tile.getJ();

            while (j < width - 1 && control) {

                j++;

                if (isInBoard(i,j)) {
                    if (isOccupied(i,j)) {
                        if (canFuse(i, j, tile)) {

                            fuse(i, j, tile, iterator);
                        }
                        control = false;

                    } else {
                        moveTextView(tile.getI(), tile.getJ(), i, j);
                        board[tile.getI()][tile.getJ()].setTile(null);

                        tile.setPosition(i, j);
                        board[i][j].setTile(tile);
                    }
                } else {
                    control = false;
                }
            }
        }
    }


    // Game over validation:
    private boolean isBoardFull() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                if (board[i][j].getTile() == null) {
                    return false;
                }
            }
        }
        return true;
    }
    private boolean canAnyMerge() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Tile currentTile = board[i][j].getTile();

                if (currentTile != null) {

                    int currentValue = currentTile.getValue();

                    if ((i > 0 && board[i - 1][j].getTile() != null && board[i - 1][j].getTile().getValue() == currentValue) || // Arriba
                            (i < height - 1 && board[i + 1][j].getTile() != null && board[i + 1][j].getTile().getValue() == currentValue) || // Abajo
                            (j > 0 && board[i][j - 1].getTile() != null && board[i][j - 1].getTile().getValue() == currentValue) || // Izquierda
                            (j < width - 1 && board[i][j + 1].getTile() != null && board[i][j + 1].getTile().getValue() == currentValue)) { // Derecha

                        return true;
                    }
                }
            }
        }
        return false;
    }
    private void checkGameOver() {

        if (isBoardFull() && !canAnyMerge()) {

            gameOver = true;
        }
    }


    // UI:
    private void updateScore() {
        TextView textView = findViewById(R.id.score);
        textView.setText("Score: " + score);
    }
    private TextView createTextView() {

        TextView textView = new TextView(new ContextThemeWrapper(this, R.style.TileStyle), null, 0);
        textView.setText("2");

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);

        return textView;
    }
    private void addTextViewToUI(int i, int j, TextView textView) {
        runOnUiThread(() -> {

            FrameLayout frameLayout = board[i][j].getFrameLayout();

            if (frameLayout != null && textView.getParent() == null) {

                frameLayout.addView(textView);
            }
        });
    }
    private void deleteTextView(int i, int j) {
        runOnUiThread(() -> {

            FrameLayout frameLayout = board[i][j].getFrameLayout();

            if (frameLayout != null) {

                frameLayout.removeAllViews();
                frameLayout.invalidate();
            }
        });
    }
    private void updateTextViewValue(Tile tile, int newValue) {
        runOnUiThread(() -> {

            if (tile != null && tile.getTextView() != null) {

                TextView textView = tile.getTextView();
                textView.setText(String.valueOf(newValue));

                String colorName = "color" + textView.getText();
                int colorId = getResources().getIdentifier(colorName, "color", getPackageName());

                if (colorId != 0) {
                    int color = ContextCompat.getColor(this, colorId);
                    textView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                }
            }
        });
    }
    private void moveTextView(int i, int j, int newI, int newJ) {
        runOnUiThread(() -> {

            FrameLayout originalFrameLayout = board[i][j].getFrameLayout();
            Tile tile = board[i][j].getTile();

            if (tile != null) {
                TextView textView = tile.getTextView();

                if (textView != null) {

                    ViewGroup parentViewGroup = (ViewGroup) textView.getParent();

                    if (parentViewGroup != null) {

                        parentViewGroup.removeView(textView);
                    }

                    FrameLayout targetFrameLayout = board[newI][newJ].getFrameLayout();

                    if (targetFrameLayout != null) {

                        targetFrameLayout.addView(textView);
                        originalFrameLayout.invalidate();
                        targetFrameLayout.invalidate();
                    }
                }
            }
        });
    }
    private void gameOverDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_2048);
        dialog.setCancelable(false);

        Button backButton = dialog.findViewById(R.id.backToMenuButton);
        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            dialog.dismiss();
        });

        TextView textView = dialog.findViewById(R.id.score);

        SharedPreferences users = getSharedPreferences("Users", MODE_PRIVATE);
        String actualUser = users.getString("ActualUser", null);

        if (actualUser != null) {

            SharedPreferences prefs = getSharedPreferences("2048_Scores", MODE_PRIVATE);
            int hiScore = prefs.getInt(actualUser, 0);

            if (score > hiScore) {

                textView.setText("New high score: " + score);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(actualUser, score);
                editor.apply();

            } else {
                textView.setText("High score: " + hiScore + "\nScore: " + score);
            }
        }

        dialog.show();
    }

    // Listener:
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
    @Override
    public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        float deltaX = e2.getX() - e1.getX();
        float deltaY = e2.getY() - e1.getY();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (deltaX > 0) {
                move("right");
            } else {
                move("left");
            }
        } else {
            if (deltaY > 0) {
                move("down");
            } else {
                move("up");
            }
        }
        return true;
    }


    // Debug methods:
    public void seeBoard() {

        System.out.println("============");

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Tile tile = board[i][j].getTile();
                System.out.print(tile != null ? " " + tile.getValue() + " " : " · ");
            }
            System.out.println();
        }
        seeTextViews();
    }
    private void seeTextViews() {

        System.out.println("============");

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {


                FrameLayout frameLayout = board[i][j].getFrameLayout();
                System.out.print(frameLayout.getChildCount() != 0 ? " T " : " · ");
            }
            System.out.println();
        }
        System.out.println("============");
    }









    // Useless:
    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }
    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
    @Override
    public void onLongPress(@NonNull MotionEvent e) {

    }
}