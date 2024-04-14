package com.example.gamecenter.Games.Senku;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.gamecenter.Main.MainActivity;
import com.example.gamecenter.R;

public class SenkuGame {

    // Attributes:
    int[][] board;
    private TextView selectedTile;
    private long start;
    private boolean gameOver;

    private int selectedRow;
    private int selectedCol;
    private int targetRow;
    private int targetCol;
    private int midRow;
    private int midCol;

    GridLayout gridLayout;
    Context context;

    // Constructor:
    public SenkuGame(Context context) {

        this.context = context;
        board = new int[7][7];
        gameOver = false;
        gridLayout = ((Activity) context).findViewById(R.id.gridLayout);
        fillBoard();
        start = System.currentTimeMillis();
    }

    // Setters:
    private void setClickListener(TextView textView, GridLayout gridLayout) {

        textView.setOnClickListener(view -> {
            if (selectedTile != null) {

                if (textView.getTag() != null && textView.getTag().equals("void")) {

                    if (isMoveValid(textView, gridLayout)) {
                        moveTile(textView);

                    } else {
                        selectedTile = null;
                    }
                }
            } else {

                if (textView.getTag() != null && textView.getTag().equals("visible")) {
                    selectedTile = textView;
                }
            }
        });
    }

    // Getters:
    private int getIPosition(GridLayout gridLayout, TextView textView) {

        int indexOfChild = gridLayout.indexOfChild(textView);
        int numRows = gridLayout.getRowCount();
        return indexOfChild / numRows;
    }
    private int getJPosition(GridLayout gridLayout, TextView textView) {

        int indexOfChild = gridLayout.indexOfChild(textView);
        int numColumns = gridLayout.getColumnCount();
        return indexOfChild % numColumns;
    }
    private TextView getTile(GridLayout gridLayout, int i, int j) {

        int index = i * gridLayout.getColumnCount() + j;
        if (index >= 0 && index < gridLayout.getChildCount()) {

            View view = gridLayout.getChildAt(index);

            if (view instanceof TextView) {
                return (TextView) view;
            }
        }
        return null;
    }

    // Private methods:
    private void fillBoard() {

        int rows = gridLayout.getRowCount();
        int columns = gridLayout.getColumnCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                int index = i * columns + j;
                TextView textView = (TextView) gridLayout.getChildAt(index);

                setClickListener(textView, gridLayout);

                if (textView.getTag() != null && textView.getTag().equals("invisible")) {
                    board[i][j] = -1;

                } else if (textView.getTag() != null && textView.getTag().equals("visible")) {
                    board[i][j] = 1;

                } else {
                    board[i][j] = 0;
                }
            }
        }
    }
    private void updateBoard(){

        board[selectedRow][selectedCol] = 0;
        board[targetRow][targetCol]=1;
        board[midRow][midCol]=0;

        TextView middleTextView = getTile(gridLayout, midRow, midCol);

        if (middleTextView != null) {

            middleTextView.setBackgroundResource(R.drawable.empty_tile_senku);
            middleTextView.setTag("void");
            middleTextView.setText("");
            middleTextView.setVisibility(View.VISIBLE);
        }
    }
    private boolean isMoveValid(TextView targetTextView, GridLayout gridLayout) {

        selectedRow = getIPosition(gridLayout, selectedTile);
        selectedCol = getJPosition(gridLayout, selectedTile);

        targetRow = getIPosition(gridLayout, targetTextView);
        targetCol = getJPosition(gridLayout, targetTextView);

        boolean isHorizontalMove = selectedRow == targetRow && Math.abs(selectedCol - targetCol) == 2;
        boolean isVerticalMove = selectedCol == targetCol && Math.abs(selectedRow - targetRow) == 2;

        midRow = (selectedRow + targetRow) / 2;
        midCol = (selectedCol + targetCol) / 2;

        boolean isMiddleCellOccupied = board[midRow][midCol] == 1;

        return (isHorizontalMove || isVerticalMove) && isMiddleCellOccupied;
    }
    private void moveTile(TextView targetTextView) {

        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(250);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {

                targetTextView.setBackgroundResource(R.drawable.corners_grid);
                targetTextView.setTag("visible");
                selectedTile.setBackgroundResource(R.drawable.empty_tile_senku);
                selectedTile.setTag("void");
                selectedTile.setText("");
                selectedTile.setVisibility(View.VISIBLE);
                selectedTile = null;

                updateBoard();

                if(checkDefeat()) {
                    gameOverDialog();
                }
            }
        });
        selectedTile.startAnimation(fadeOut);
    }
    private boolean canMove(int srcRow, int srcCol, int destRow, int destCol) {
        if (destRow >= 0 && destRow < board.length && destCol >= 0 && destCol < board[0].length) {
            if (board[destRow][destCol] == 0) {
                int midRow = (srcRow + destRow) / 2;
                int midCol = (srcCol + destCol) / 2;
                return board[midRow][midCol] == 1;
            }
        }
        return false;
    }
    public boolean checkDefeat() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    if (canMove(i, j, i, j + 2) || canMove(i, j, i, j - 2) ||
                            canMove(i, j, i + 2, j) || canMove(i, j, i - 2, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private void gameOverDialog() {

        long end = System.currentTimeMillis();
        long time = end - start;

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_senku);
        dialog.setCancelable(false);

        Button backButton = dialog.findViewById(R.id.backToMenuButton);
        backButton.setOnClickListener(v -> {

            Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);

            dialog.dismiss();
        });

        TextView textView = dialog.findViewById(R.id.senkuScore);

        SharedPreferences users = context.getSharedPreferences("Users", context.MODE_PRIVATE);
        String actualUser = users.getString("ActualUser", null);

        if (actualUser != null) {

            SharedPreferences pref = context.getSharedPreferences("Senku_times", Context.MODE_PRIVATE);
            long bestTime = pref.getLong(actualUser, 0L);

            if (time < bestTime) {

                textView.setText("New best time: " + time);

                SharedPreferences.Editor editor = pref.edit();
                editor.putLong(actualUser, time);
                editor.apply();

            } else {
                textView.setText("Best time: " + bestTime + "\nTime: " + time);
            }
        }

        dialog.show();
    }
}