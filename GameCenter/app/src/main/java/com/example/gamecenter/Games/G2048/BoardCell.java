package com.example.gamecenter.Games.G2048;

import android.widget.FrameLayout;

public class BoardCell {

    // Attributes:
    private final int i, j;
    private final FrameLayout frameLayout;
    private Tile tile;

    // Constructor:
    public BoardCell(int i, int j, FrameLayout frameLayout) {
        this.i = i;
        this.j = j;
        this.frameLayout = frameLayout;
        tile = null;
    }

    // Setter:
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    // Getters:
    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
    public FrameLayout getFrameLayout() {
        return frameLayout;
    }
    public Tile getTile() {
        return tile;
    }
}