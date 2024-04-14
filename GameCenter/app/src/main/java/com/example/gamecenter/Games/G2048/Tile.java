package com.example.gamecenter.Games.G2048;

import android.widget.TextView;

public class Tile {

    // Attributes:
    private int i, j;
    private int value;
    private boolean merged;
    private TextView textView;

    // Constructor:
    public Tile(int i, int j, TextView textView) {
        
        this.i = i;
        this.j = j;
        value = 2;
        this.merged = false;
        this.textView = textView;
    }

    // Setter:
    public void setValue(int value) {
        this.value = value;
        textView.setText(String.valueOf(value));
    }
    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    // Getters:
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
    public int getValue() {
        return value;
    }
    public boolean hasMerged() {
        return merged;
    }
    public TextView getTextView() {
        return textView;
    }

    // Public methods:
    public int[] getPosition() {

        return new int[]{i, j};
    }
    public void setPosition(int i, int j) {

        this.i = i;
        this.j = j;
    }
}